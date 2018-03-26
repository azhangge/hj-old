package com.hjedu.examination.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.service.IUserSessionStateService;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.ExamCaseItemAdapter;
import com.hjedu.examination.entity.ExamCaseItemFill;
import com.hjedu.examination.entity.ExamCaseItemFillBlock;
import com.hjedu.examination.entity.VirtualExamPart;
import com.hjedu.examination.service.IExamCaseCacheService;
import com.hjedu.examination.service.IExamCaseService;
import com.hjedu.platform.controller.ClientSession;
import com.huajie.exam.thread.ExamCaseSaver;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

/**
 * 本类用于提交考试失败时直接从REQUEST MAP中解析出作答数据并提交
 *
 * @author huajie
 */
public class ExamCaseSubmitUtil {

    private static IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
    private static IUserSessionStateService iussService = SpringHelper.getSpringBean("UserSessionStateService");
    private static IExamCaseCacheService cacheService = SpringHelper.getSpringBean("ExamCaseCacheService");

    private static String form = "myForm";//考试页面form的id
    private static String part = "part";
    private static String adapter = "adapter";
    private static String choice = "examCaseChoice";
    private static String mchoice = "examCaseMultiChoice";
    private static String fill = "examCaseFill";
    private static String judge = "examCaseJudge";
    private static String essay = "examCaseEssay";
    private static String file = "examCaseFile";
    private static String secretCase = ":secretCaseId";
    private static String secretUrn = ":secretUrn";

    /**
     * 从request map中恢复数据并提交成绩，本方法为在页面上提交数据失败时的一种补救措施
     *
     * @param map
     */
    public static String submitFromRequestMap(Map<String, String[]> map) {
        MyLogger.println("Start to submit...");
        MyLogger.explainMap(map);
        String caseId = null;
        String urn = null;

        String ss[] = map.get(form + secretCase);
        if (ss != null) {
            if (ss.length > 0) {
                caseId = ss[0];
                MyLogger.println(caseId);
            }
        }
        String uu[] = map.get(form + secretUrn);
        if (uu != null) {
            if (uu.length > 0) {
                urn = uu[0];
                if (urn != null) {
                    MyLogger.println(urn);
                    BbsUser ut = userDAO.findBbsUserByUrn(urn);
                    //尝试恢复session
                    ClientSession cs = JsfHelper.getBean("clientSession");
                    if (cs == null) {
                        cs = new ClientSession();
                        JsfHelper.getRequest().getSession().setAttribute("clientSession", cs);
                    }
                    cs.setUsr(ut);
                    cs.setIfLogin(true);
                }
            }
        }
        //验证是在提交还是在保存试卷
        boolean ifSubmit = false;
        String[] submit = map.get(form + ":subcase");
        if (submit != null) {
            if (submit.length > 0) {
                ifSubmit = true;
            }
        }
        if (caseId != null && urn != null) {
            handleMap(map, caseId, ifSubmit);
        }

        return null;
    }

    /**
     *
     * @param map
     * @param caseId
     * @param ifSubmit
     */
    private static void handleMap(Map<String, String[]> map, String caseId, boolean ifSubmit) {
        //只有caseId的request map才有办法处理
        if (caseId != null) {
            //从缓存中取出考试，任何进入考试的成绩都被保存进入了缓存
            ExamCase ec = cacheService.findExamCase(caseId);
            if (ec != null) {
                List<VirtualExamPart> vparts = ec.getVparts();
                if (vparts != null) {
                    for (int i = 0; i < vparts.size(); i++) {
                        VirtualExamPart pp = vparts.get(i);
                        if (pp != null) {
                            List<ExamCaseItemAdapter> as = pp.getAdapters();
                            if (as != null) {
                                for (int j = 0; j < as.size(); j++) {
                                    ExamCaseItemAdapter a = as.get(j);
                                    String qtype = a.getQtype();
                                    if ("choice".equals(qtype)) {
                                        buildChoice(i, j, map, a);
                                    } else if ("mchoice".equals(qtype)) {
                                        buildMultiChoice(i, j, map, a);
                                    } else if ("fill".equals(qtype)) {
                                        buildFill(i, j, map, a);
                                    } else if ("judge".equals(qtype)) {
                                        buildJudge(i, j, map, a);
                                    } else if ("essay".equals(qtype)) {
                                        buildEssay(i, j, map, a);
                                    } else if ("file".equals(qtype)) {
                                        buildFile(i, j, map, a);
                                    }
                                }
                            }
                        }
                    }
                    //如果需要提交成绩 
                    if (ifSubmit) {
                        String paperType = ec.getExamination().getPaperType();
                        IExamCaseService examCaseService = null;
                        if ("random".equals(paperType)) {
                            examCaseService = SpringHelper.getSpringBean("ExamCaseService");
                        } else if ("random2".equals(paperType)) {
                            examCaseService = SpringHelper.getSpringBean("ExamCaseRandom2Service");
                        } else {
                            examCaseService = SpringHelper.getSpringBean("ManualExamCaseService");
                        }
                        ec = examCaseService.computeExamCase(ec);
                        //若不在交卷队列中处理，此处则需要手动加入缓存
                        cacheService.addExamCase(ec);
                        //处理好后加入存储队列
                        ExamCaseSaver.saveProcessedExamCase(ec);
                        JsfHelper.getRequest().getSession().setAttribute("tempExamCase", ec.getId());

                        try {
                            String url = "ExamCaseResult.jspx";
                            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                            MyLogger.echo(url);
                            response.sendRedirect(url);
                            FacesContext.getCurrentInstance().responseComplete();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        //处理好后加入存储队列
                        ExamCaseSaver ecs = new ExamCaseSaver(ec);
                        ThreadPoolTaskExecutor exec = SpringHelper.getSpringBean("taskExecutor");
                        exec.execute(ecs);
                    }
                }
            }
        }
    }

    /**
     * 从map中取出数据，存入adapter
     *
     * @param partInd part的序号
     * @param adapterInd adapter的序号
     * @param map request map
     * @param ada 适配器
     * @return
     */
    private static void buildChoice(int partInd, int adapterInd, Map<String, String[]> map, ExamCaseItemAdapter ada) {
        String id = form + ":" + part + ":" + partInd + ":" + adapter + ":" + adapterInd + ":" + choice;
        String[] ss = map.get(id);
        if (!checkEmpty(ss)) {
            String ans = ss[0];
            ada.getChoiceItem().setUserAnswer(ans);
        }
    }

    private static void buildMultiChoice(int partInd, int adapterInd, Map<String, String[]> map, ExamCaseItemAdapter ada) {
        String id = form + ":" + part + ":" + partInd + ":" + adapter + ":" + adapterInd + ":" + mchoice;
        String[] ss = map.get(id);
        if (!checkEmpty(ss)) {
            List<String> ans = new ArrayList();
            for (String an : ss) {
                ans.add(an);
            }
            ada.getMultiChoiceItem().setSelectedLabels(ans);
        }
    }

    private static void buildFill(int partInd, int adapterInd, Map<String, String[]> map, ExamCaseItemAdapter ada) {
        ExamCaseItemFill ff = ada.getFillItem();
        if (ff != null) {
            List<ExamCaseItemFillBlock> blocks = ff.getBlocks();
            if (blocks != null) {
                for (int z = 0; z < blocks.size(); z++) {
                    ExamCaseItemFillBlock b = blocks.get(z);
                    String id = form + ":" + part + ":" + partInd + ":" + adapter + ":" + adapterInd + ":blocks:" + z + ":" + fill;
                    String[] ss = map.get(id);
                    if (!checkEmpty(ss)) {
                        String ans = ss[0];
                        b.setUserAnswer(ans);
                    }
                }
            }
        }

    }

    private static void buildJudge(int partInd, int adapterInd, Map<String, String[]> map, ExamCaseItemAdapter ada) {
        String id = form + ":" + part + ":" + partInd + ":" + adapter + ":" + adapterInd + ":" + judge;
        String[] ss = map.get(id);
        if (!checkEmpty(ss)) {
            String ans = ss[0];
            ada.getJudgeItem().setUserAnswer(ans);
        }
    }

    private static void buildEssay(int partInd, int adapterInd, Map<String, String[]> map, ExamCaseItemAdapter ada) {
        String id = form + ":" + part + ":" + partInd + ":" + adapter + ":" + adapterInd + ":" + essay;
        String[] ss = map.get(id);
        if (!checkEmpty(ss)) {
            String ans = ss[0];
            ada.getEssayItem().setUserAnswer(ans);
        }
    }

    private static void buildFile(int partInd, int adapterInd, Map<String, String[]> map, ExamCaseItemAdapter ada) {
        String id = form + ":" + part + ":" + partInd + ":" + adapter + ":" + adapterInd + ":" + file;
        String[] ss = map.get(id);
        if (!checkEmpty(ss)) {
            String ans = ss[0];
            ada.getFileItem().setUserAnswer(ans);
        }
    }

    /**
     *
     * @param ss
     * @return
     */
    private static boolean checkEmpty(String ss[]) {
        if (ss == null) {
            return true;
        } else if (ss.length == 0) {
            return true;
        } else {
            return false;
        }
    }

}
