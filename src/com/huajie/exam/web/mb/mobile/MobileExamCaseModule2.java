package com.huajie.exam.web.mb.mobile;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.service.IUserSessionStateService;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.dao.IModuleExamCaseDAO;
import com.hjedu.examination.dao.IModuleExaminationDAO;
import com.hjedu.examination.entity.module.ModuleExamCase;
import com.hjedu.examination.entity.module.ModuleExamCaseItemCase;
import com.hjedu.examination.entity.module.ModuleExamCaseItemChoice;
import com.hjedu.examination.entity.module.ModuleExamCaseItemEssay;
import com.hjedu.examination.entity.module.ModuleExamCaseItemFile;
import com.hjedu.examination.entity.module.ModuleExamCaseItemFill;
import com.hjedu.examination.entity.module.ModuleExamCaseItemJudge;
import com.hjedu.examination.entity.module.ModuleExamCaseItemMultiChoice;
import com.hjedu.examination.entity.module2.ModuleExam2CaseItemAdapter;
import com.hjedu.examination.entity.module2.ModuleExam2Part;
import com.hjedu.examination.entity.module2.ModuleExamination2;
import com.hjedu.examination.service.IModuleExamCaseService;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.huajie.exam.thread.ModuleExamCaseSaver;
import com.huajie.exam.web.mb.*;
import com.huajie.util.IpHelper;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@SessionScoped
public class MobileExamCaseModule2 implements Serializable {

    transient IModuleExaminationDAO examinationDAO = SpringHelper.getSpringBean("ModuleExaminationDAO");

    transient IModuleExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ModuleExamCaseDAO");
    transient IFileQuestionDAO fileQuestionDAO = SpringHelper.getSpringBean("FileQuestionDAO");
    transient IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
    transient IUserSessionStateService iussService = SpringHelper.getSpringBean("UserSessionStateService");
    //ClientSession cs = JsfHelper.getBean("clientSession");
    ModuleExamCase examCase = null;
    Date begainTime = new Date();
    long remainingTime = 0;
    String errStr = "";
    String redirectId = "-1";
    int total = 0;
    boolean submited = false;
    boolean flag = false;

    int currentIndex = 1;
    ModuleExam2CaseItemAdapter currentAdapter;
    Map<Integer, ModuleExam2CaseItemAdapter> adapterMap = new HashMap();
    boolean curentIfRight = false;
    String type = "1";

    String secretUrn = "";//用于记录当前考生用户名，用以防止用户名万一丢失

    public String getSecretUrn() {
        return secretUrn;
    }

    public void setSecretUrn(String secretUrn) {
        this.secretUrn = secretUrn;
    }

    public Date getBegainTime() {
        return begainTime;
    }

    public void setBegainTime(Date begainTime) {
        this.begainTime = begainTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public boolean isSubmited() {
        return submited;
    }

    public void setSubmited(boolean submited) {
        this.submited = submited;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Map<Integer, ModuleExam2CaseItemAdapter> getAdapterMap() {
        return adapterMap;
    }

    public void setAdapterMap(Map<Integer, ModuleExam2CaseItemAdapter> adapterMap) {
        this.adapterMap = adapterMap;
    }

    public ModuleExamCase getExamCase() {
        return examCase;
    }

    public void setExamCase(ModuleExamCase examCase) {
        this.examCase = examCase;
    }

    public boolean isCurentIfRight() {
        return curentIfRight;
    }

    public void setCurentIfRight(boolean curentIfRight) {
        this.curentIfRight = curentIfRight;
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ModuleExam2CaseItemAdapter getCurrentAdapter() {
        return currentAdapter;
    }

    public void setCurrentAdapter(ModuleExam2CaseItemAdapter currentAdapter) {
        this.currentAdapter = currentAdapter;
    }

    public String getErrStr() {
        return errStr;
    }

    public void setErrStr(String errStr) {
        this.errStr = errStr;
    }

    public String getRedirectId() {
        return redirectId;
    }

    public void setRedirectId(String redirectId) {
        this.redirectId = redirectId;
    }

    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        IModuleExamCaseService examCaseService = SpringHelper.getSpringBean("Module2ExamCaseService");
        String ttp = request.getParameter("type");
        if (ttp != null) {
            type = ttp;
        }
        String case_id = request.getParameter("case_id");
        if (case_id != null) {//恢复老考试
            examCase = this.examCaseDAO.findModuleExamCase(case_id);
            examCaseService.resumeExamCase(examCase);
            //this.currentIndex = examCase.getProgress();
            //examCase.setGenTime(this.begainTime);
            this.flag = true;
        } else {//设置新考试
            String exam_id = request.getParameter("exam_id");
            String id = exam_id;
            if (exam_id == null) {
                return;
            }
            examCase = new ModuleExamCase();

            ClientSession cs = JsfHelper.getBean("clientSession");
            if (cs.getUsr() == null) {
                return;
            }

            ModuleExamCase ecc = this.examCaseDAO.findModuleExamCaseByExaminationAndUser(id, cs.getUsr().getId());
            if (ecc != null) {

                examCase = examCaseService.resumeExamCase(ecc);
                //this.currentIndex = examCase.getProgress();
                //examCase.setGenTime(this.begainTime);
                this.flag = true;
            } else {
                ModuleExamination2 exam = this.examinationDAO.findExamination(exam_id);

                this.examCase.setExamination(exam);
                examCaseService.buildExamCase(examCase);
                this.examCase.setUser(cs.getUsr());
            }

            MyLogger.echo("ExamCase initialized.");

//            ExamCaseSaver ecs = new ExamCaseSaver(examCase);
//            ThreadPoolTaskExecutor exec = SpringHelper.getSpringBean("taskExecutor");
//            exec.execute(ecs);
            //考试前扣用户积分（只针对新考试）
            BbsUser bu = cs.getUsr();
            if (bu != null) {
                //bu.setScore(bu.getScore() - examCase.getExamination().getScorePaid());
                IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
                bsl.log("章节练习消耗积分", (int) (-1 * examCase.getExamination().getScorePaid()));
                //this.userDAO.updateBbsUser(bu);
            }
        }
        this.examCase.setExamModule(examCase.getExamination().getModule());//设置EXAMCASE的模块

        this.examCase.setIp(IpHelper.getRemoteAddr(request));
        //iussService.loginExam(examCase);

        //以下代码加载试题编号
        List<ModuleExam2Part> parts = this.examCase.getCparts();
        for (ModuleExam2Part part : parts) {
            part.setFirstIndex(total + 1);//设置本大题的起始小题号
            List<ModuleExam2CaseItemAdapter> adapters = part.getAdapters();
            for (ModuleExam2CaseItemAdapter adapter : adapters) {
                if (adapter.getQtype().equals("choice")) {
                    if (type.equals("1")) {
                        adapter.getChoiceItem().setIndex(++total);
                        this.adapterMap.put(total, adapter);
                    } else if (type.equals("2")) {
                        if (adapter.getItem().isDone()) {
                            adapter.getChoiceItem().setIndex(++total);
                            this.adapterMap.put(total, adapter);
                        }
                    } else if (type.equals("3")) {
                        if (!adapter.getItem().isDone()) {
                            adapter.getChoiceItem().setIndex(++total);
                            this.adapterMap.put(total, adapter);
                        }
                    }
                } else if (adapter.getQtype().equals("mchoice")) {
                    if (type.equals("1")) {
                        adapter.getMultiChoiceItem().setIndex(++total);
                        this.adapterMap.put(total, adapter);
                    } else if (type.equals("2")) {
                        if (adapter.getItem().isDone()) {
                            adapter.getMultiChoiceItem().setIndex(++total);
                            this.adapterMap.put(total, adapter);
                        }
                    } else if (type.equals("3")) {
                        if (!adapter.getItem().isDone()) {
                            adapter.getMultiChoiceItem().setIndex(++total);
                            this.adapterMap.put(total, adapter);
                        }
                    }
                } else if (adapter.getQtype().equals("fill")) {
                    if (type.equals("1")) {
                        adapter.getFillItem().setIndex(++total);
                        this.adapterMap.put(total, adapter);
                    } else if (type.equals("2")) {
                        if (adapter.getItem().isDone()) {
                            adapter.getFillItem().setIndex(++total);
                            this.adapterMap.put(total, adapter);
                        }
                    } else if (type.equals("3")) {
                        if (!adapter.getItem().isDone()) {
                            adapter.getFillItem().setIndex(++total);
                            this.adapterMap.put(total, adapter);
                        }
                    }
                } else if (adapter.getQtype().equals("judge")) {
                    if (type.equals("1")) {
                        adapter.getJudgeItem().setIndex(++total);
                        this.adapterMap.put(total, adapter);
                    } else if (type.equals("2")) {
                        if (adapter.getItem().isDone()) {
                            adapter.getJudgeItem().setIndex(++total);
                            this.adapterMap.put(total, adapter);
                        }
                    } else if (type.equals("3")) {
                        if (!adapter.getItem().isDone()) {
                            adapter.getJudgeItem().setIndex(++total);
                            this.adapterMap.put(total, adapter);
                        }
                    }
                } else if (adapter.getQtype().equals("essay")) {
                    if (type.equals("1")) {
                        adapter.getEssayItem().setIndex(++total);
                        this.adapterMap.put(total, adapter);
                    } else if (type.equals("2")) {
                        if (adapter.getItem().isDone()) {
                            adapter.getEssayItem().setIndex(++total);
                            this.adapterMap.put(total, adapter);
                        }
                    } else if (type.equals("3")) {
                        if (!adapter.getItem().isDone()) {
                            adapter.getEssayItem().setIndex(++total);
                            this.adapterMap.put(total, adapter);
                        }
                    }
                } else if (adapter.getQtype().equals("file")) {
                    if (type.equals("1")) {
                        adapter.getFileItem().setIndex(++total);
                        this.adapterMap.put(total, adapter);
                    } else if (type.equals("2")) {
                        if (adapter.getItem().isDone()) {
                            adapter.getFileItem().setIndex(++total);
                            this.adapterMap.put(total, adapter);
                        }
                    } else if (type.equals("3")) {
                        if (!adapter.getItem().isDone()) {
                            adapter.getFileItem().setIndex(++total);
                            this.adapterMap.put(total, adapter);
                        }
                    }
                } else if (adapter.getQtype().equals("case")) {
                    ModuleExamCaseItemCase cc = adapter.getCaseItem();
                    if (cc.getChoiceItems() != null) {
                        System.out.println(cc.getChoiceItems().size());
                        for (ModuleExamCaseItemChoice ei : cc.getChoiceItems()) {
                            ei.setCaseType("choice");
                            ei.setExamCase(examCase);
                            ei.setPartId(part.getId());
                            ei.setCaseItem(cc);
                            ModuleExam2CaseItemAdapter ad = new ModuleExam2CaseItemAdapter();
                            ad.setChoiceItem(ei);
                            ad.setItem(ei);
                            ad.setModuleExam2Part(part);
                            ad.setQtype("case");
                            ad.setQuestion(ei.getQuestion());
                            if (type.equals("1")) {
                                ei.setIndex(++total);
                                this.adapterMap.put(total, ad);
                            } else if (type.equals("2")) {
                                if (ad.getItem().isDone()) {
                                    ei.setIndex(++total);
                                    this.adapterMap.put(total, ad);
                                }
                            } else if (type.equals("3")) {
                                if (!ad.getItem().isDone()) {
                                    ei.setIndex(++total);
                                    this.adapterMap.put(total, ad);
                                }
                            }
                        }
                    }
                    if (cc.getMultiChoiceItems() != null) {
                        for (ModuleExamCaseItemMultiChoice ei : cc.getMultiChoiceItems()) {
                            ei.setCaseType("mchoice");
                            ei.setExamCase(examCase);
                            ei.setPartId(part.getId());
                            ei.setCaseItem(cc);
                            ModuleExam2CaseItemAdapter ad = new ModuleExam2CaseItemAdapter();
                            ad.setMultiChoiceItem(ei);
                            ad.setItem(ei);
                            ad.setModuleExam2Part(part);
                            ad.setQtype("case");
                            ad.setQuestion(ei.getQuestion());
                            if (type.equals("1")) {
                                ei.setIndex(++total);
                                this.adapterMap.put(total, ad);
                            } else if (type.equals("2")) {
                                if (ad.getItem().isDone()) {
                                    ei.setIndex(++total);
                                    this.adapterMap.put(total, ad);
                                }
                            } else if (type.equals("3")) {
                                if (!ad.getItem().isDone()) {
                                    ei.setIndex(++total);
                                    this.adapterMap.put(total, ad);
                                }
                            }
                        }
                    }
                    if (cc.getFillItems() != null) {
                        for (ModuleExamCaseItemFill ei : cc.getFillItems()) {
                            ei.setCaseType("fill");
                            ei.setExamCase(examCase);
                            ei.setPartId(part.getId());
                            ei.setCaseItem(cc);
                            ModuleExam2CaseItemAdapter ad = new ModuleExam2CaseItemAdapter();
                            ad.setFillItem(ei);
                            ad.setItem(ei);
                            ad.setModuleExam2Part(part);
                            ad.setQtype("case");
                            ad.setQuestion(ei.getQuestion());
                            if (type.equals("1")) {
                                ei.setIndex(++total);
                                this.adapterMap.put(total, ad);
                            } else if (type.equals("2")) {
                                if (ad.getItem().isDone()) {
                                    ei.setIndex(++total);
                                    this.adapterMap.put(total, ad);
                                }
                            } else if (type.equals("3")) {
                                if (!ad.getItem().isDone()) {
                                    ei.setIndex(++total);
                                    this.adapterMap.put(total, ad);
                                }
                            }
                        }
                    }
                    if (cc.getJudgeItems() != null) {
                        for (ModuleExamCaseItemJudge ei : cc.getJudgeItems()) {
                            ei.setCaseType("judge");
                            ei.setExamCase(examCase);
                            ei.setPartId(part.getId());
                            ei.setCaseItem(cc);
                            ModuleExam2CaseItemAdapter ad = new ModuleExam2CaseItemAdapter();
                            ad.setJudgeItem(ei);
                            ad.setItem(ei);
                            ad.setModuleExam2Part(part);
                            ad.setQtype("case");
                            ad.setQuestion(ei.getQuestion());
                            if (type.equals("1")) {
                                ei.setIndex(++total);
                                this.adapterMap.put(total, ad);
                            } else if (type.equals("2")) {
                                if (ad.getItem().isDone()) {
                                    ei.setIndex(++total);
                                    this.adapterMap.put(total, ad);
                                }
                            } else if (type.equals("3")) {
                                if (!ad.getItem().isDone()) {
                                    ei.setIndex(++total);
                                    this.adapterMap.put(total, ad);
                                }
                            }
                        }
                    }
                    if (cc.getEssayItems() != null) {
                        for (ModuleExamCaseItemEssay ei : cc.getEssayItems()) {
                            ei.setCaseType("essay");
                            ei.setExamCase(examCase);
                            ei.setPartId(part.getId());
                            ei.setCaseItem(cc);
                            ModuleExam2CaseItemAdapter ad = new ModuleExam2CaseItemAdapter();
                            ad.setEssayItem(ei);
                            ad.setItem(ei);
                            ad.setModuleExam2Part(part);
                            ad.setQtype("case");
                            ad.setQuestion(ei.getQuestion());
                            if (type.equals("1")) {
                                ei.setIndex(++total);
                                this.adapterMap.put(total, ad);
                            } else if (type.equals("2")) {
                                if (ad.getItem().isDone()) {
                                    ei.setIndex(++total);
                                    this.adapterMap.put(total, ad);
                                }
                            } else if (type.equals("3")) {
                                if (!ad.getItem().isDone()) {
                                    ei.setIndex(++total);
                                    this.adapterMap.put(total, ad);
                                }
                            }
                        }
                    }

                }
            }
        }

        if (type.equals("1")) {
            this.currentIndex = this.examCase.getProgress();
        }
        if (this.currentIndex <= 0) {
            this.currentIndex = 1;
        }
        if (this.currentIndex > total) {
            this.currentIndex = total;
        }
        this.currentAdapter = this.adapterMap.get(currentIndex);
        //记录开始了章节练习
        this.iussService.loginModuleExam(examCase);
    }

    public void checkCurrent() {
        ModuleExam2CaseItemAdapter adapter = this.currentAdapter;
        IModuleExamCaseService examCaseService = SpringHelper.getSpringBean("Module2ExamCaseService");
        examCaseService.computeSingleAdapter(adapter);
        this.curentIfRight = adapter.getItem().getIfRight();
        adapter.getItem().setDone(true);
        //this.nextAdapter();
        if (type.equals("1")) {
            int pt = this.currentIndex + 1;
            if (pt > total) {
                pt = total;
            }
            examCase.setProgress(pt);
        }
        //this.saveExamCase("auto");
    }

    public void nextAdapter() {
        if (this.currentIndex < this.adapterMap.size()) {
            this.currentIndex++;
            this.currentAdapter = this.adapterMap.get(currentIndex);
        }
//        if (currentAdapter.getQtype().equals("mchoice") && currentAdapter.getItem().isDone()) {
//            this.recoverMultiChoices(currentAdapter.getMultiChoiceItem());
//        }
    }

    public void previousAdapter() {
        if (this.currentIndex > 1) {
            this.currentIndex--;
            this.currentAdapter = this.adapterMap.get(currentIndex);
        }
//        if (currentAdapter.getQtype().equals("mchoice") && currentAdapter.getItem().isDone()) {
//            this.recoverMultiChoices(currentAdapter.getMultiChoiceItem());
//        }
    }

    public void refreshAdapter() {
        //System.out.println(currentIndex);
        this.currentAdapter = this.adapterMap.get(this.currentIndex);

    }

    public void gotoAdapter(int ind) {
        if (ind < 1) {
            ind = 1;
        }
        if (ind > total) {
            ind = total;
        }
        this.currentIndex = ind;
        this.currentAdapter = this.adapterMap.get(ind);

    }

    public String autoSaveExamCase() {
        this.saveExamCase("auto");
        JsfHelper.info("保存成功！");
        return null;
    }

    public String saveExamCase(String str) {
        this.flag = true;
        examCase.setProgress(currentIndex);
        ModuleExamCaseSaver ecs = new ModuleExamCaseSaver(examCase);
        ThreadPoolTaskExecutor exec = SpringHelper.getSpringBean("taskExecutor");
        exec.execute(ecs);

        return null;
    }

    public String saveAndExit() {
        this.saveExamCase("auto");
        String result = "/mobile/MobileExamModule2List?faces-redirect=true";
        return result;
    }

    public List up_action(FileUploadEvent event) {
        try {
            UploadedFile item = event.getFile();
            String str = "";
            Long tl = item.getSize();
            //this.project2.setRealLength(tl);
            Float k = 1024F;
            Float m = 1024 * 1024F;
            Float g = 1024 * 1024 * 1024F;
            if (tl < k) {
                str = tl.toString() + "B";
            } else if (k <= tl && tl < m) {
                Float te = Math.round(tl / k * 100) / 100.00F;
                str = te.toString() + "KB";
            } else if (tl >= m && tl < g) {
                Float te = Math.round(tl / m * 100) / 100.00F;
                str = te.toString() + "MB";
            } else {
                Float te = Math.round(tl / g * 100) / 100.00F;
                str = te.toString() + "GB";
            }
            //this.project2.setFileSize(str);
            String n = item.getFileName();
            int l1 = n.lastIndexOf("\\");
            int l2 = n.lastIndexOf(".");
            String ext = "." + n.substring(l2 + 1).toLowerCase();
            String name = n.substring(l1 + 1, l2);
            //HttpServletRequest request = JsfHelper.getRequest();
            String imgId = UUID.randomUUID().toString();

            UIComponent ui = event.getComponent();
            ModuleExamCaseItemFile nm = null;
            while ((ui != null) && (!(ui instanceof UIData))) {
                ui = ui.getParent();
            }
            if ((ui != null) && (ui instanceof UIData)) {
                Object rowData = ((UIData) ui).getRowData();
                if (rowData instanceof ModuleExamCaseItemFile) {
                    nm = (ModuleExamCaseItemFile) rowData;
                }
            }
            if (nm != null) {
                imgId = nm.getId();
            } else {
                MyLogger.echo("未取得正在操作的ModuleExamCaseItemFile的ID");
            }

            this.fileQuestionDAO.saveFile(item.getInputstream(), imgId);
            return null;
        } catch (Exception ee) {
            ee.printStackTrace();
            return null;
        }
    }

    public String submitExamCase() {
        ClientSession cs = JsfHelper.getBean("clientSession");
        System.out.println(this.secretUrn + " begain to submit the examcase.");
        if (cs.getUsr() == null) {
            IBbsUserDAO userDAO2 = SpringHelper.getSpringBean("BbsUserDAO");
            BbsUser bu2 = userDAO2.findBbsUserByUrn(this.secretUrn);
            examCase.setUser(bu2);
        }
        //examCase.setStat("submitted");
        //this.submited = true;
        String result = "ExamModule2List?faces-redirect=true";
        examCase.setSubmitTime(new Date());
        //examCase.setIfPub(examCase.getExamination().isIfDirect());
        IModuleExamCaseService examCaseService = SpringHelper.getSpringBean("Module2ExamCaseService");
        this.examCase = examCaseService.computeExamCase(examCase);
        if (!flag) {
            this.examCaseDAO.addModuleExamCase(examCase);
        } else {
            this.examCaseDAO.updateModuleExamCase(examCase);
        }

        //this.examCaseService.computeExamCase(examCase);
        //this.examCaseDAO.addExamCase(examCase);
        //iussService.logoutExam();
        //考试后给用户加积分
        BbsUser bu = cs.getUsr();
        //bu.setScore(bu.getScore() + examCase.getBbsScore());
//        IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
//        bsl.log("考试取得成绩自动积分", (int) examCase.getBbsScore());
        //this.userDAO.updateBbsUser(bu);

        //将考试结果暂时保存在SESSION中并跳转
        return result;
    }

    public String logout() {
        //记录开始了章节练习
        this.iussService.logoutModuleExam();
        return SpringHelper.getSpringBean("mobile_module2_logout_page");
    }

    public String reserveSession() {
        MyLogger.echo("reserveSession sucess.");
        return null;
    }

    @PreDestroy
    public void destroy11() {
        MyLogger.echo("ExamCase destroyed.");
        //iussService.logoutExam();
    }
}
