package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.ArrayList;
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
import com.hjedu.examination.entity.ExamMultiChoice;
import com.hjedu.examination.entity.module.ModuleExamCase;
import com.hjedu.examination.entity.module.ModuleExamCaseItemCase;
import com.hjedu.examination.entity.module.ModuleExamCaseItemChoice;
import com.hjedu.examination.entity.module.ModuleExamCaseItemEssay;
import com.hjedu.examination.entity.module.ModuleExamCaseItemFile;
import com.hjedu.examination.entity.module.ModuleExamCaseItemFill;
import com.hjedu.examination.entity.module.ModuleExamCaseItemFillBlock;
import com.hjedu.examination.entity.module.ModuleExamCaseItemJudge;
import com.hjedu.examination.entity.module.ModuleExamCaseItemMultiChoice;
import com.hjedu.examination.entity.module2.ModuleExam2CaseItemAdapter;
import com.hjedu.examination.entity.module2.ModuleExam2Part;
import com.hjedu.examination.entity.module2.ModuleExamination2;
import com.hjedu.examination.service.IModuleExamCaseService;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.huajie.exam.thread.ModuleExamCaseSaver;
import com.huajie.util.CookieUtils;
import com.huajie.util.IpHelper;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@SessionScoped
public class ExamCaseModule2 implements Serializable {

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
    List<Integer> indices = new ArrayList();
    boolean curentIfRight = false;
    String type = "1";

    String secretUrn = "";//用于记录当前考生用户名，用以防止用户名万一丢失

    String tid ="";

    public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

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

    public List<Integer> getIndices() {
        return indices;
    }

    public void setIndices(List<Integer> indices) {
        this.indices = indices;
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
        IModuleExamCaseService examCaseService = SpringHelper.getSpringBean("Module2ExamCaseService");
        IModuleExaminationDAO examinationDAO = SpringHelper.getSpringBean("ModuleExaminationDAO");
        IModuleExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ModuleExamCaseDAO");
        System.out.println("module2 case init.");
        HttpServletRequest request = JsfHelper.getRequest();

        String tid = request.getParameter("tid");    
        this.tid=tid;
        
        String ttp = request.getParameter("type");
        if (ttp != null) {
            type = ttp;
        }
        String case_id = request.getParameter("case_id");
        if (case_id != null) {//恢复老考试
            examCase = examCaseDAO.findModuleExamCase(case_id);
            examCaseService.resumeExamCase(examCase);
            //this.currentIndex = examCase.getProgress();
            //examCase.setGenTime(this.begainTime);
            //this.checkDoneQuestion();//只针对恢复的保存过的考试
            this.flag = true;
        } else {//设置新考试
            String exam_id = request.getParameter("exam_id");
            String id = exam_id;
            if (exam_id == null) {
                return;
            }

            ClientSession cs = JsfHelper.getBean("clientSession");
            if (cs.getUsr() == null) {
                return;
            }

            ModuleExamCase ecc = examCaseDAO.findModuleExamCaseByExaminationAndUser(id, cs.getUsr().getId());
            if (ecc != null) {
            	examCase = ecc;
                examCaseService.resumeExamCase(examCase);
                //this.currentIndex = examCase.getProgress();
                //examCase.setGenTime(this.begainTime);
                //this.checkDoneQuestion();//只针对恢复的保存过的考试
                this.flag = true;
            } else {
            	examCase = new ModuleExamCase();
                ModuleExamination2 exam = examinationDAO.findExamination(exam_id);

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
        //将所有的INDEX加入LIST备用
        this.indices.clear();
        this.indices.addAll(this.adapterMap.keySet());
        //记录开始了章节练习
        this.iussService.loginModuleExam(examCase);
    }

    public void checkCurrent() {
        IModuleExamCaseService examCaseService = SpringHelper.getSpringBean("Module2ExamCaseService");
        ModuleExam2CaseItemAdapter adapter = this.currentAdapter;
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
    }

    public void previousAdapter() {
        if (this.currentIndex > 1) {
            this.currentIndex--;
            this.currentAdapter = this.adapterMap.get(currentIndex);
        }
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
        if (examCase != null) {
            examCase.setProgress(currentIndex);
            examCase.setBusinessId(CookieUtils.getBusinessId(JsfHelper.getRequest()));
            ModuleExamCaseSaver ecs = new ModuleExamCaseSaver(examCase);
            ThreadPoolTaskExecutor exec = SpringHelper.getSpringBean("taskExecutor");
            exec.execute(ecs);
        }
        return null;
    }

    public String saveAndExit() {
        this.saveExamCase("auto");
        String result = "ExamModule2List?faces-redirect=true";
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
            IFileQuestionDAO fileQuestionDAO = SpringHelper.getSpringBean("FileQuestionDAO");
            fileQuestionDAO.saveFile(item.getInputstream(), imgId);
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
        IModuleExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ModuleExamCaseDAO");
        this.examCase = examCaseService.computeExamCase(examCase);
        if (!flag) {
            examCaseDAO.addModuleExamCase(examCase);
        } else {
            examCaseDAO.updateModuleExamCase(examCase);
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

    //检查是否有题目未做
    public String checkNull() {
        System.out.println("checking...");
        this.errStr = "";
        this.redirectId = "-1";
        StringBuilder sb = new StringBuilder();
        ModuleExamCase ec = this.examCase;
        List<ModuleExamCaseItemChoice> cqs = ec.getChoiceItems();
        List<ModuleExamCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
        List<ModuleExamCaseItemFill> fqs = ec.getFillItems();
        List<ModuleExamCaseItemJudge> jqs = ec.getJudgeItems();
        List<ModuleExamCaseItemEssay> eqs = ec.getEssayItems();
        List<ModuleExamCaseItemFile> ffqs = ec.getFileItems();
        List<ModuleExamCaseItemCase> ccqs = ec.getCaseItems();
        for (ModuleExamCaseItemChoice c : cqs) {
            if (c.getUserAnswer() == null || "".equals(c.getUserAnswer())) {
                sb.append("您还有单选题未做；");
                if (this.redirectId.equals("-1")) {
                    this.redirectId = c.getId();
                }
                break;
            }
        }
        for (ModuleExamCaseItemMultiChoice c : mcqs) {
            List<ExamMultiChoice> ls = c.getQuestion().getChoices();
            StringBuilder sb2 = new StringBuilder();
            for (ExamMultiChoice e : ls) {
                if (e.isSelected()) {
                    sb2.append(e.getLabel());
                }
            }
            String ss2 = sb2.toString();
            c.setUserAnswer(ss2);
            if (c.getUserAnswer() == null || "".equals(c.getUserAnswer())) {
                sb.append("您还有多选题未做；");
                if (this.redirectId.equals("-1")) {
                    this.redirectId = c.getId();
                }
                break;
            }
        }

        for (ModuleExamCaseItemFill c : fqs) {
            List<ModuleExamCaseItemFillBlock> ls = c.getBlocks();
            StringBuilder us = new StringBuilder();
            boolean tt = false;
            for (ModuleExamCaseItemFillBlock e : ls) {
                if (e.getUserAnswer() == null || "".equals(e.getUserAnswer())) {
                    sb.append("您还有填空题未做；");
                    if (this.redirectId.equals("-1")) {
                        this.redirectId = c.getId();
                    }
                    tt = true;
                    break;
                }
            }
            if (tt) {
                break;
            }
        }

        for (ModuleExamCaseItemJudge c : jqs) {
            if ("null".equals(c.getUserAnswer())) {
                sb.append("您还有判断题未做；");
                if (this.redirectId.equals("-1")) {
                    this.redirectId = c.getId();
                }
                break;
            }
        }
        for (ModuleExamCaseItemEssay c : eqs) {
            if (c.getUserAnswer() == null || "".equals(c.getUserAnswer())) {
                sb.append("您还有问答题未做；");
                if (this.redirectId.equals("-1")) {
                    this.redirectId = c.getId();
                }
                break;
            }
        }
        for (ModuleExamCaseItemFile c : ffqs) {
            if (!c.isAttached()) {
                sb.append("您还有文件题未做；");
                if (this.redirectId.equals("-1")) {
                    this.redirectId = c.getId();
                }
                break;
            }
        }

        for (ModuleExamCaseItemCase c : ccqs) {
            List<ModuleExamCaseItemChoice> cqs1 = c.getChoiceItems();
            List<ModuleExamCaseItemEssay> eqs1 = c.getEssayItems();
            for (ModuleExamCaseItemChoice cc : cqs1) {
                if (cc.getUserAnswer() == null || "".equals(cc.getUserAnswer())) {
                    sb.append("您还有综合题中的单选题未做；");
                    if (this.redirectId.equals("-1")) {
                        this.redirectId = cc.getId();
                    }
                    break;
                }
            }
            for (ModuleExamCaseItemEssay cc : eqs1) {
                if (cc.getUserAnswer() == null || "".equals(cc.getUserAnswer())) {
                    sb.append("您还有综合题中的问题题未做；");
                    if (this.redirectId.equals("-1")) {
                        this.redirectId = cc.getId();
                    }
                    break;
                }
            }
        }

        this.errStr = sb.toString();
        System.out.println("checked");
        System.out.println(errStr);
        return null;
    }
    
    public String logout() {
        //记录开始了章节练习
        this.iussService.logoutModuleExam();
        return "/talk/LessonList.jspx?tid="+this.tid+"&faces-redirect=true";
        
//        return SpringHelper.getSpringBean("module2_logout_page");
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
