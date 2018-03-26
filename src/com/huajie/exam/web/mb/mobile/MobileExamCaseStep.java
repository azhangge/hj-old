package com.huajie.exam.web.mb.mobile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpServletRequest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.service.IUserSessionStateService;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.dao.IExamCaseLogDAO;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.ExamCaseItemAdapter;
import com.hjedu.examination.entity.ExamCaseItemCase;
import com.hjedu.examination.entity.ExamCaseItemChoice;
import com.hjedu.examination.entity.ExamCaseItemEssay;
import com.hjedu.examination.entity.ExamCaseItemFile;
import com.hjedu.examination.entity.ExamCaseItemFill;
import com.hjedu.examination.entity.ExamCaseItemFillBlock;
import com.hjedu.examination.entity.ExamCaseItemJudge;
import com.hjedu.examination.entity.ExamCaseItemMultiChoice;
import com.hjedu.examination.entity.ExamCaseLog;
import com.hjedu.examination.entity.ExamChoice;
import com.hjedu.examination.entity.ExamMultiChoice;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.entity.GeneralExamCaseItem;
import com.hjedu.examination.entity.VirtualExamPart;
import com.hjedu.examination.service.IExamCaseCacheService;
import com.hjedu.examination.service.IExamCaseService;
import com.hjedu.platform.controller.ApplicationBean;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.exam.thread.ExamCaseSaver;
import com.huajie.exam.thread.ExamCaseSubmitter;
import com.huajie.util.IpHelper;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@SessionScoped
public class MobileExamCaseStep implements Serializable {

    transient IExaminationDAO examinationDAO = SpringHelper.getSpringBean("ExaminationDAO");
    transient IExamCaseService examCaseService;
    transient IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
    transient IFileQuestionDAO fileQuestionDAO = SpringHelper.getSpringBean("FileQuestionDAO");
    transient IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
    transient IUserSessionStateService iussService = SpringHelper.getSpringBean("UserSessionStateService");
    IExamCaseCacheService cacheService = SpringHelper.getSpringBean("ExamCaseCacheService");

    ExamCase examCase = null;
    List<GeneralExamCaseItem> stepItems = new ArrayList();//封装所有题目
    GeneralExamCaseItem currentItem = null;//当前题目
    int currentIndex = 1;
    Map<Integer, ExamCaseItemChoice> choiceMap = new HashMap();//封装单选题供调用
    Map<Integer, ExamCaseItemMultiChoice> mchoiceMap = new HashMap();
    Map<Integer, ExamCaseItemFill> fillMap = new HashMap();
    Map<Integer, ExamCaseItemJudge> judgeMap = new HashMap();
    Map<Integer, ExamCaseItemEssay> essayMap = new HashMap();

    Date begainTime = new Date();
    long remainingTime = 0;
    String errStr = "";
    String redirectId = "-1";
    boolean submited = false;

    String secretUrn = "";//用于记录当前考生用户名，用以防止用户名万一丢失
    int total = 0;

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

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public List<GeneralExamCaseItem> getStepItems() {
        return stepItems;
    }

    public void setStepItems(List<GeneralExamCaseItem> stepItems) {
        this.stepItems = stepItems;
    }

    public ExamCase getExamCase() {
        return examCase;
    }

    public void setExamCase(ExamCase examCase) {
        this.examCase = examCase;
    }

    public Map<Integer, ExamCaseItemChoice> getChoiceMap() {
        return choiceMap;
    }

    public void setChoiceMap(Map<Integer, ExamCaseItemChoice> choiceMap) {
        this.choiceMap = choiceMap;
    }

    public Map<Integer, ExamCaseItemMultiChoice> getMchoiceMap() {
        return mchoiceMap;
    }

    public void setMchoiceMap(Map<Integer, ExamCaseItemMultiChoice> mchoiceMap) {
        this.mchoiceMap = mchoiceMap;
    }

    public Map<Integer, ExamCaseItemFill> getFillMap() {
        return fillMap;
    }

    public void setFillMap(Map<Integer, ExamCaseItemFill> fillMap) {
        this.fillMap = fillMap;
    }

    public Map<Integer, ExamCaseItemJudge> getJudgeMap() {
        return judgeMap;
    }

    public void setJudgeMap(Map<Integer, ExamCaseItemJudge> judgeMap) {
        this.judgeMap = judgeMap;
    }

    public Map<Integer, ExamCaseItemEssay> getEssayMap() {
        return essayMap;
    }

    public void setEssayMap(Map<Integer, ExamCaseItemEssay> essayMap) {
        this.essayMap = essayMap;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public long getRemainingTime() {
        this.computeRemainingTime();
        return remainingTime;
    }

    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }

    public GeneralExamCaseItem getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(GeneralExamCaseItem currentItem) {
        this.currentItem = currentItem;
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

        String case_id = request.getParameter("case_id");

        ClientSession cs = JsfHelper.getBean("clientSession");
        if (cs.getUsr() == null) {
            return;
        } else {
            //设置保险用户名
            this.secretUrn = cs.getUsr().getUsername();
        }

        IExamCaseService examCaseServiceRandom = SpringHelper.getSpringBean("ExamCaseService");
        IExamCaseService examCaseServiceRandom2 = SpringHelper.getSpringBean("ExamCaseRandom2Service");
        IExamCaseService examCaseServiceManual = SpringHelper.getSpringBean("ManualExamCaseService");
        if (case_id != null) {//恢复老考试
            examCase = this.examCaseDAO.findExamCase(case_id);

            if ("random".equals(examCase.getExamination().getPaperType())) {
                examCaseService = examCaseServiceRandom;
            } else if ("random2".equals(examCase.getExamination().getPaperType())) {
                examCaseService = examCaseServiceRandom2;
            } else if ("manual".equals(examCase.getExamination().getPaperType())) {
                examCaseService = examCaseServiceManual;
            }

            this.examCaseService.resumeExamCase(examCase);
            //examCase.setGenTime(this.begainTime);

            this.checkDoneQuestion();//只针对恢复的保存过的考试
        } else {//设置新考试
            String exam_id = request.getParameter("exam_id");
            String id = exam_id;
            if (exam_id == null) {
                return;
            }

            Examination examt = this.examinationDAO.findExamination(id);

            if ("random".equals(examt.getPaperType())) {
                examCaseService = examCaseServiceRandom;
            } else if ("random2".equals(examt.getPaperType())) {
                examCaseService = examCaseServiceRandom2;
            } else if ("manual".equals(examt.getPaperType())) {
                examCaseService = examCaseServiceManual;
            }

            this.examCase = ExamPaperPool.takePaper(id);//从试卷池中取一份试卷

            //System.out.println("ExamCaseMB is built.");
//            Examination exam = this.examinationDAO.findExamination(Long.parseLong(exam_id));
//            this.examCase.setExamination(exam);
//            this.examCaseService.buildExamCase(examCase);
            examCase.setDisplayMode(examt.getDisplayMode());
            this.examCase.setUser(cs.getUsr());

            MyLogger.echo("ExamCase initialized.");

//            ExamCaseSaver ecs = new ExamCaseSaver(examCase);
//            ThreadPoolTaskExecutor exec = SpringHelper.getSpringBean("taskExecutor");
//            exec.execute(ecs);
            //考试前扣用户积分（只针对新考试）
            BbsUser bu = cs.getUsr();
            if (bu != null) {
                //bu.setScore(bu.getScore() - examCase.getExamination().getScorePaid());
                IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
                bsl.log("考试消耗积分", (int) (-1 * examCase.getExamination().getScorePaid()));
                //this.userDAO.updateBbsUser(bu);
            }
        }

        this.computeRemainingTime();
        this.examCase.setIp(IpHelper.getRemoteAddr(request));
        this.examCase.setPlatform("phone");
        iussService.loginExam(examCase);

        if ("random".equals(examCase.getExamination().getPaperType())) {
            this.loadRandomStepItems();
        } else if ("random2".equals(examCase.getExamination().getPaperType())) {
            this.loadRandom2StepItems();
        } else if ("manual".equals(examCase.getExamination().getPaperType())) {
            this.loadManualStepItems();
        }

        this.adjustIndex();
        this.currentItem = this.stepItems.get(this.currentIndex - 1);
        this.logExam(examCase.getExamination());//添加抽卷记录
    }

    public void logExam(Examination exam) {
        ExamCaseLog log = new ExamCaseLog();
        ClientSession cs = JsfHelper.getBean("clientSession");
        BbsUser bu = cs.getUsr();
        if (bu != null) {
            log.setUser(bu);
            log.setExamination(exam);
            HttpServletRequest req = JsfHelper.getRequest();
            log.setIp(req.getRemoteAddr());
            log.setPlatform("phone");
            IExamCaseLogDAO logDAO2 = SpringHelper.getSpringBean("ExamCaseLogDAO");
            logDAO2.addExamCaseLog(log);
        }
    }

    //调整当前做题进度，主要作用于恢复的老考试
    private void adjustIndex() {
        if (stepItems != null) {
            int i = 0;
            for (GeneralExamCaseItem s : stepItems) {
                i++;
                if (!s.isDone()) {
                    this.currentIndex = i;
                    break;
                }
            }
        }

    }

    public void loadRandomStepItems() {
        for (ExamCaseItemChoice ei : examCase.getChoiceItems()) {
            ei.setIndex(++total);
            ei.setPartName("单选题");
            ei.setScore(examCase.getExamination().getRandomPaper().getChoiceScore());
            ei.setQtype("choice");
            this.stepItems.add(ei);
            this.choiceMap.put(total, ei);

        }
        for (ExamCaseItemMultiChoice ei : examCase.getMultiChoiceItems()) {
            ei.setIndex(++total);
            ei.setPartName("多选题");
            ei.setScore(examCase.getExamination().getRandomPaper().getMultiChoiceScore());
            ei.setQtype("mchoice");
            this.stepItems.add(ei);
            this.mchoiceMap.put(total, ei);

        }
        for (ExamCaseItemFill ei : examCase.getFillItems()) {
            ei.setIndex(++total);
            ei.setPartName("填空题");
            ei.setScore(examCase.getExamination().getRandomPaper().getFillScore());
            ei.setQtype("fill");
            this.stepItems.add(ei);
            this.fillMap.put(total, ei);

        }
        for (ExamCaseItemJudge ei : examCase.getJudgeItems()) {
            ei.setIndex(++total);
            ei.setPartName("判断题");
            ei.setScore(examCase.getExamination().getRandomPaper().getJudgeScore());
            ei.setQtype("judge");
            this.stepItems.add(ei);
            this.judgeMap.put(total, ei);

        }
        for (ExamCaseItemEssay ei : examCase.getEssayItems()) {
            ei.setIndex(++total);
            ei.setPartName("问答题");
            ei.setScore(examCase.getExamination().getRandomPaper().getEssayScore());
            ei.setQtype("essay");
            this.stepItems.add(ei);
            this.essayMap.put(total, ei);

        }
        for (ExamCaseItemCase eic : examCase.getCaseItems()) {
            int caseIndex = 0;
            for (ExamCaseItemChoice ei : eic.getChoiceItems()) {
                ei.setPartName("综合题");
                ei.setScore(eic.getQuestion().getChoiceScore());
                ei.setQtype("case");
                this.stepItems.add(ei);
                ei.setIndex(++total);
                ei.setCaseType("choice");
                ei.setCaseIndex(++caseIndex);
                this.choiceMap.put(total, ei);

            }
            for (ExamCaseItemMultiChoice ei : eic.getMultiChoiceItems()) {
                ei.setPartName("综合题");
                ei.setScore(eic.getQuestion().getMultiChoiceScore());
                ei.setQtype("case");
                this.stepItems.add(ei);
                ei.setIndex(++total);
                ei.setCaseType("mchoice");
                ei.setCaseIndex(++caseIndex);
                this.mchoiceMap.put(total, ei);
            }
            for (ExamCaseItemFill ei : eic.getFillItems()) {
                ei.setPartName("综合题");
                ei.setScore(eic.getQuestion().getFillScore());
                ei.setQtype("case");
                this.stepItems.add(ei);
                ei.setIndex(++total);
                ei.setCaseType("fill");
                ei.setCaseIndex(++caseIndex);
                this.fillMap.put(total, ei);
            }
            for (ExamCaseItemJudge ei : eic.getJudgeItems()) {
                ei.setPartName("综合题");
                ei.setScore(eic.getQuestion().getJudgeScore());
                ei.setQtype("case");
                this.stepItems.add(ei);
                ei.setIndex(++total);
                ei.setCaseType("judge");
                ei.setCaseIndex(++caseIndex);
                this.judgeMap.put(total, ei);
            }
            for (ExamCaseItemEssay ei : eic.getEssayItems()) {
                ei.setPartName("综合题");
                ei.setScore(eic.getQuestion().getEssayScore());
                ei.setQtype("case");
                this.stepItems.add(ei);
                ei.setIndex(++total);
                ei.setCaseType("essay");
                ei.setCaseIndex(++caseIndex);
                this.essayMap.put(total, ei);
            }
        }

    }

    public void loadRandom2StepItems() {
        List<VirtualExamPart> parts = this.examCase.getVparts();
        for (VirtualExamPart part : parts) {
            List<ExamCaseItemAdapter> adapters = part.getAdapters();
            for (ExamCaseItemAdapter adapter : adapters) {
                if (adapter.getQtype().equals("choice")) {
                    ExamCaseItemChoice ei = adapter.getChoiceItem();
                    ei.setIndex(++total);
                    ei.setPartName(part.getName());
                    ei.setScore(part.getChoiceScore());
                    ei.setQtype("choice");
                    this.stepItems.add(ei);
                    this.choiceMap.put(total, ei);

                } else if (adapter.getQtype().equals("mchoice")) {
                    ExamCaseItemMultiChoice ei = adapter.getMultiChoiceItem();
                    ei.setIndex(++total);
                    ei.setPartName(part.getName());
                    ei.setScore(part.getMultiChoiceScore());
                    ei.setQtype("mchoice");
                    this.stepItems.add(ei);
                    this.mchoiceMap.put(total, ei);
                } else if (adapter.getQtype().equals("fill")) {
                    ExamCaseItemFill ei = adapter.getFillItem();
                    ei.setIndex(++total);
                    ei.setPartName(part.getName());
                    ei.setScore(part.getFillScore());
                    ei.setQtype("fill");
                    this.stepItems.add(ei);
                    this.fillMap.put(total, ei);
                } else if (adapter.getQtype().equals("judge")) {
                    ExamCaseItemJudge ei = adapter.getJudgeItem();
                    ei.setIndex(++total);
                    ei.setPartName(part.getName());
                    ei.setScore(part.getJudgeScore());
                    ei.setQtype("judge");
                    this.stepItems.add(ei);
                    this.judgeMap.put(total, ei);
                } else if (adapter.getQtype().equals("essay")) {
                    ExamCaseItemEssay ei = adapter.getEssayItem();
                    ei.setIndex(++total);
                    ei.setPartName(part.getName());
                    ei.setScore(part.getEssayScore());
                    ei.setQtype("essay");
                    this.stepItems.add(ei);
                    this.essayMap.put(total, ei);
                } else if (adapter.getQtype().equals("case")) {
                    ExamCaseItemCase eic = adapter.getCaseItem();
                    int caseIndex = 0;
                    for (ExamCaseItemChoice ei : adapter.getCaseItem().getChoiceItems()) {
                        ei.setIndex(++total);
                        ei.setPartName(part.getName());
                        ei.setScore(eic.getQuestion().getChoiceScore());
                        ei.setQtype("case");
                        ei.setCaseType("choice");
                        ei.setCaseIndex(++caseIndex);
                        this.stepItems.add(ei);
                        this.choiceMap.put(total, ei);
                    }
                    for (ExamCaseItemMultiChoice ei : adapter.getCaseItem().getMultiChoiceItems()) {
                        ei.setIndex(++total);
                        ei.setPartName(part.getName());
                        ei.setScore(eic.getQuestion().getChoiceScore());
                        ei.setQtype("case");
                        ei.setCaseType("mchoice");
                        ei.setCaseIndex(++caseIndex);
                        this.stepItems.add(ei);
                        this.mchoiceMap.put(total, ei);
                    }
                    for (ExamCaseItemFill ei : adapter.getCaseItem().getFillItems()) {
                        ei.setPartName(part.getName());
                        ei.setScore(eic.getQuestion().getFillScore());
                        this.stepItems.add(ei);
                        ei.setIndex(++total);
                        ei.setQtype("case");
                        ei.setCaseType("fill");
                        ei.setCaseIndex(++caseIndex);
                        this.fillMap.put(total, ei);
                    }
                    for (ExamCaseItemJudge ei : adapter.getCaseItem().getJudgeItems()) {
                        ei.setIndex(++total);
                        ei.setPartName(part.getName());
                        ei.setScore(eic.getQuestion().getChoiceScore());
                        ei.setQtype("case");
                        ei.setCaseType("judge");
                        ei.setCaseIndex(++caseIndex);
                        this.stepItems.add(ei);
                        this.judgeMap.put(total, ei);
                    }
                    for (ExamCaseItemEssay ei : adapter.getCaseItem().getEssayItems()) {
                        ei.setPartName(part.getName());
                        ei.setScore(eic.getQuestion().getEssayScore());
                        ei.setQtype("case");
                        ei.setCaseType("essay");
                        ei.setCaseIndex(++caseIndex);
                        this.stepItems.add(ei);
                        ei.setIndex(++total);

                        this.essayMap.put(total, ei);
                    }
                }
            }
        }
    }

    public void loadManualStepItems() {
        List<VirtualExamPart> parts = this.examCase.getVparts();
        for (VirtualExamPart part : parts) {
            List<ExamCaseItemAdapter> adapters = part.getAdapters();
            for (ExamCaseItemAdapter adapter : adapters) {
                if (adapter.getQtype().equals("choice")) {
                    ExamCaseItemChoice ei = adapter.getChoiceItem();
                    ei.setIndex(++total);
                    ei.setPartName(part.getName());
                    ei.setScore(adapter.getScore());
                    ei.setQtype("choice");
                    this.stepItems.add(ei);
                    this.choiceMap.put(total, ei);

                } else if (adapter.getQtype().equals("mchoice")) {
                    ExamCaseItemMultiChoice ei = adapter.getMultiChoiceItem();
                    ei.setIndex(++total);
                    ei.setPartName(part.getName());
                    ei.setScore(adapter.getScore());
                    ei.setQtype("mchoice");
                    this.stepItems.add(ei);
                    this.mchoiceMap.put(total, ei);
                } else if (adapter.getQtype().equals("fill")) {
                    ExamCaseItemFill ei = adapter.getFillItem();
                    ei.setIndex(++total);
                    ei.setPartName(part.getName());
                    ei.setScore(adapter.getScore());
                    ei.setQtype("fill");
                    this.stepItems.add(ei);
                    this.fillMap.put(total, ei);
                } else if (adapter.getQtype().equals("judge")) {
                    ExamCaseItemJudge ei = adapter.getJudgeItem();
                    ei.setIndex(++total);
                    ei.setPartName(part.getName());
                    ei.setScore(adapter.getScore());
                    ei.setQtype("judge");
                    this.stepItems.add(ei);
                    this.judgeMap.put(total, ei);
                } else if (adapter.getQtype().equals("essay")) {
                    ExamCaseItemEssay ei = adapter.getEssayItem();
                    ei.setIndex(++total);
                    ei.setPartName(part.getName());
                    ei.setScore(adapter.getScore());
                    ei.setQtype("essay");
                    this.stepItems.add(ei);
                    this.essayMap.put(total, ei);
                } else if (adapter.getQtype().equals("case")) {
                    int caseIndex = 0;
                    ExamCaseItemCase eic = adapter.getCaseItem();
                    for (ExamCaseItemChoice ei : adapter.getCaseItem().getChoiceItems()) {
                        ei.setIndex(++total);
                        ei.setPartName(part.getName());
                        ei.setScore(eic.getQuestion().getChoiceScore());
                        ei.setQtype("case");
                        ei.setCaseType("choice");
                        ei.setCaseIndex(++caseIndex);
                        this.stepItems.add(ei);
                        this.choiceMap.put(total, ei);
                    }
                    for (ExamCaseItemMultiChoice ei : adapter.getCaseItem().getMultiChoiceItems()) {
                        ei.setIndex(++total);
                        ei.setPartName(part.getName());
                        ei.setScore(eic.getQuestion().getChoiceScore());
                        ei.setQtype("case");
                        ei.setCaseType("mchoice");
                        ei.setCaseIndex(++caseIndex);
                        this.stepItems.add(ei);
                        this.mchoiceMap.put(total, ei);
                    }
                    for (ExamCaseItemFill ei : adapter.getCaseItem().getFillItems()) {
                        ei.setPartName(part.getName());
                        ei.setScore(eic.getQuestion().getFillScore());
                        this.stepItems.add(ei);
                        ei.setIndex(++total);
                        ei.setQtype("case");
                        ei.setCaseType("fill");
                        ei.setCaseIndex(++caseIndex);
                        this.fillMap.put(total, ei);
                    }
                    for (ExamCaseItemJudge ei : adapter.getCaseItem().getJudgeItems()) {
                        ei.setIndex(++total);
                        ei.setPartName(part.getName());
                        ei.setScore(eic.getQuestion().getChoiceScore());
                        ei.setQtype("case");
                        ei.setCaseType("judge");
                        ei.setCaseIndex(++caseIndex);
                        this.stepItems.add(ei);
                        this.judgeMap.put(total, ei);
                    }
                    for (ExamCaseItemEssay ei : adapter.getCaseItem().getEssayItems()) {
                        ei.setPartName(part.getName());
                        ei.setScore(eic.getQuestion().getEssayScore());
                        ei.setQtype("case");
                        ei.setCaseType("essay");
                        ei.setCaseIndex(++caseIndex);
                        this.stepItems.add(ei);
                        ei.setIndex(++total);

                        this.essayMap.put(total, ei);
                    }
                }
            }
        }
    }

    public String gotoQuestion(int index) {
        this.currentItem = this.stepItems.get(index);
        return null;
    }

    public void computeRemainingTime() {
        if (examCase != null) {
            long n = System.currentTimeMillis();
            long o = this.begainTime.getTime();
            long len = this.examCase.getExamination().getTimeLen() * 60;
            long past = (long) (n - o) / 1000;
            this.remainingTime = len - past - examCase.getTimePassed();
        }
    }

    public String saveExamCase(String str) {
        if (!this.submited) {
            long n = System.currentTimeMillis();
            long o = this.begainTime.getTime();
            long past = (long) (n - o) / 1000;
            examCase.setTimePassed(examCase.getTimePassed() + past);
            ExamCaseSaver ecs = new ExamCaseSaver(examCase);
            ThreadPoolTaskExecutor exec = SpringHelper.getSpringBean("taskExecutor");
            exec.execute(ecs);
            if ("auto".equals(str)) {
                JsfHelper.info("自动保存考试成功，如果考试异常中断，请在综合考试成绩查询中恢复！");
            } else {
                JsfHelper.info("考试保存成功，如果考试异常中断，请在综合考试成绩查询中恢复！");
            }
        }
        return null;
    }

    public void changSelect(long questionId, long choiceId) {
        //MyLogger.echo(questionId + ":" + choiceId);
        List<ExamCaseItemChoice> ecics = this.examCase.getChoiceItems();
        for (ExamCaseItemChoice e : ecics) {
            String qid = e.getQuestion().getId();
            if (qid.equals(questionId)) {
                List<ExamChoice> ecs = e.getQuestion().getChoices();
                for (ExamChoice ec : ecs) {
                    ec.setSelected(false);
                }
                for (ExamChoice ec : ecs) {
                    if (ec.getId().equals(choiceId)) {
                        ec.setSelected(true);
                        break;
                    }
                }
                break;
            }

        }
    }

    //恢复页面左边做题进度的显示情况
    public String checkDoneQuestion() {
        //System.out.println(type + ":" + id);

        List<ExamCaseItemChoice> chs1 = this.examCase.getChoiceItems();
        for (ExamCaseItemChoice c : chs1) {
            if (!(c.getUserAnswer() == null || "".equals(c.getUserAnswer()))) {
                c.setDone(true);
                //System.out.println("done:" + c.getQuestion().getName());
            }
        }

        List<ExamCaseItemMultiChoice> chs2 = this.examCase.getMultiChoiceItems();
        for (ExamCaseItemMultiChoice c : chs2) {
            if (!(c.getUserAnswer() == null || "".equals(c.getUserAnswer()))) {
                c.setDone(true);
            }
        }

        List<ExamCaseItemFill> chs3 = this.examCase.getFillItems();
        for (ExamCaseItemFill c : chs3) {
            List<ExamCaseItemFillBlock> ls = c.getBlocks();
            StringBuilder us = new StringBuilder();
            boolean tt = false;
            for (ExamCaseItemFillBlock e : ls) {
                if (e.getUserAnswer() == null || "".equals(e.getUserAnswer())) {
                    tt = true;
                    break;
                }
            }
            if (!tt) {
                c.setDone(true);
                //System.out.println("done:" + c.getQuestion().getName());
            } else {
                c.setDone(false);
            }
        }

        List<ExamCaseItemJudge> chs4 = this.examCase.getJudgeItems();
        for (ExamCaseItemJudge c : chs4) {
            if (!((c.getUserAnswer() == null || "".equals(c.getUserAnswer()) || "null".equals(c.getUserAnswer())))) {
                c.setDone(true);
                //System.out.println("done:" + c.getQuestion().getName());
            }
        }

        List<ExamCaseItemEssay> chs5 = this.examCase.getEssayItems();
        for (ExamCaseItemEssay c : chs5) {
            if (!(c.getUserAnswer() == null || "".equals(c.getUserAnswer()))) {
                c.setDone(true);
                //System.out.println("done:" + c.getQuestion().getName());
            }
        }

        List<ExamCaseItemFile> chs6 = this.examCase.getFileItems();
        for (ExamCaseItemFile c : chs6) {
            if (c.isAttached()) {
                c.setDone(true);
                //System.out.println("done:" + c.getQuestion().getName());

            }
        }

        List<ExamCaseItemCase> chs7 = this.examCase.getCaseItems();
        for (ExamCaseItemCase cc : chs7) {
            List<ExamCaseItemChoice> chs11 = cc.getChoiceItems();
            for (ExamCaseItemChoice c : chs11) {
                if (!(c.getUserAnswer() == null || "".equals(c.getUserAnswer()))) {
                    c.setDone(true);
                    //System.out.println("done:" + c.getQuestion().getName());
                }
            }

            List<ExamCaseItemMultiChoice> chs21 = cc.getMultiChoiceItems();
            for (ExamCaseItemMultiChoice c : chs21) {
                if (!(c.getUserAnswer() == null || "".equals(c.getUserAnswer()))) {
                    c.setDone(true);
                }
            }

            List<ExamCaseItemFill> chs31 = cc.getFillItems();
            for (ExamCaseItemFill c : chs31) {
                List<ExamCaseItemFillBlock> ls = c.getBlocks();
                StringBuilder us = new StringBuilder();
                boolean tt = false;
                for (ExamCaseItemFillBlock e : ls) {
                    if (e.getUserAnswer() == null || "".equals(e.getUserAnswer())) {
                        tt = true;
                        break;
                    }
                }
                if (!tt) {
                    c.setDone(true);
                    //System.out.println("done:" + c.getQuestion().getName());
                } else {
                    c.setDone(false);
                }
            }

            List<ExamCaseItemJudge> chs41 = cc.getJudgeItems();
            for (ExamCaseItemJudge c : chs41) {
                if (!((c.getUserAnswer() == null || "".equals(c.getUserAnswer()) || "null".equals(c.getUserAnswer())))) {
                    c.setDone(true);
                    //System.out.println("done:" + c.getQuestion().getName());
                }
            }

            List<ExamCaseItemEssay> chs51 = cc.getEssayItems();
            for (ExamCaseItemEssay c : chs51) {
                if (!(c.getUserAnswer() == null || "".equals(c.getUserAnswer()))) {
                    c.setDone(true);
                    //System.out.println("done:" + c.getQuestion().getName());
                }
            }
        }

        return null;
    }

    public String doQuestion(String type, String id) {
        //System.out.println(type + ":" + id);
        if (type.equals("choice")) {
            List<ExamCaseItemChoice> chs = this.examCase.getChoiceItems();
            for (ExamCaseItemChoice c : chs) {
                if (c.getId().equals(id)) {
                    c.setDone(true);
                    //System.out.println("done:" + c.getQuestion().getName());
                    break;
                }

            }
        }
        if (type.equals("mchoice")) {
            List<ExamCaseItemMultiChoice> chs = this.examCase.getMultiChoiceItems();
            for (ExamCaseItemMultiChoice c : chs) {
                if (c.getId().equals(id)) {
                    List<ExamMultiChoice> ls = c.getQuestion().getChoices();
                    StringBuilder sb2 = new StringBuilder();
                    for (ExamMultiChoice e : ls) {
                        if (e.isSelected()) {
                            sb2.append(e.getLabel());
                        }
                    }
                    String ss2 = sb2.toString();
                    c.setUserAnswer(ss2);
                    if (c.getUserAnswer() != null && (!"".equals(c.getUserAnswer()))) {
                        c.setDone(true);
                        //System.out.println("done:" + c.getQuestion().getName());
                    } else {
                        c.setDone(false);
                    }
                    break;
                }
            }
        }
        if (type.equals("fill")) {
            List<ExamCaseItemFill> chs = this.examCase.getFillItems();
            for (ExamCaseItemFill c : chs) {
                if (c.getId().equals(id)) {
                    List<ExamCaseItemFillBlock> ls = c.getBlocks();
                    StringBuilder us = new StringBuilder();
                    boolean tt = false;
                    for (ExamCaseItemFillBlock e : ls) {
                        if (e.getUserAnswer() == null || "".equals(e.getUserAnswer())) {
                            tt = true;
                            break;
                        }
                    }
                    if (!tt) {
                        c.setDone(true);
                        //System.out.println("done:" + c.getQuestion().getName());
                    } else {
                        c.setDone(false);
                    }
                    break;
                }
            }
        }
        if (type.equals("judge")) {
            List<ExamCaseItemJudge> chs = this.examCase.getJudgeItems();
            for (ExamCaseItemJudge c : chs) {
                if (c.getId().equals(id)) {
                    if (!c.getUserAnswer().equals("null") && (!"".equals(c.getUserAnswer()))) {
                        c.setDone(true);
                        //System.out.println("done:" + c.getQuestion().getName());
                    } else {
                        c.setDone(false);
                    }
                    break;
                }
            }
        }
        if (type.equals("essay")) {
            List<ExamCaseItemEssay> chs = this.examCase.getEssayItems();
            for (ExamCaseItemEssay c : chs) {
                if (c.getId().equals(id)) {
                    if (c.getUserAnswer() != null && (!"".equals(c.getUserAnswer()))) {
                        c.setDone(true);
                        //System.out.println("done:" + c.getQuestion().getName());
                    } else {
                        c.setDone(false);
                    }
                    break;
                }
            }
        }
        if (type.equals("file")) {
            List<ExamCaseItemFile> chs = this.examCase.getFileItems();
            for (ExamCaseItemFile c : chs) {
                if (c.getId().equals(id)) {
                    if (!c.isAttached()) {
                        c.setDone(true);
                        //System.out.println("done:" + c.getQuestion().getName());
                    } else {
                        c.setDone(false);
                    }
                    break;
                }
            }
        }
        return null;
    }

    public String submitExamCase() {

        ClientSession cs = JsfHelper.getBean("clientSession");
        System.out.println(this.secretUrn + " begain to submit the examcase.");
        if (cs.getUsr() == null) {
            IBbsUserDAO userDAO2 = SpringHelper.getSpringBean("BbsUserDAO");
            BbsUser bu2 = userDAO2.findBbsUserByUrn(this.secretUrn);
            examCase.setUser(bu2);
        }
        examCase.setStat("submitted");
        
        //计算消耗的时间
        long n = System.currentTimeMillis();
        long o = this.begainTime.getTime();
        long past = (long) (n - o) / 1000;
        examCase.setTimePassed(examCase.getTimePassed() + past);
        //计算消耗时间结束
        
        this.submited = true;
        String result = "MobileExamCaseResult?faces-redirect=true";
        examCase.setSubmitTime(new Date());
        examCase.setIfPub(examCase.getExamination().isIfDirect());

        ApplicationBean appBean = JsfHelper.getBean("applicationBean");
        //是否异步提交试卷
        if (appBean.getSystemConfig().getAsyncSubmit()) {
            ExamCaseSubmitter ecs = new ExamCaseSubmitter(examCase);
            ThreadPoolTaskExecutor exec = SpringHelper.getSpringBean("taskExecutor");
            exec.execute(ecs);
        } else {
            this.examCase = this.examCaseService.computeExamCase(examCase);
            cacheService.addExamCase(examCase);//加入缓存
            //处理好后加入存储队列
            ExamCaseSaver.saveProcessedExamCase(examCase);
        }

        //this.examCaseService.computeExamCase(examCase);
        //this.examCaseDAO.addExamCase(examCase);
        iussService.logoutExam();

        //考试后给用户加积分
        BbsUser bu = cs.getUsr();
        //bu.setScore(bu.getScore() + examCase.getBbsScore());
        IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
        bsl.log("考试取得成绩自动积分", (int) examCase.getBbsScore());
        //this.userDAO.updateBbsUser(bu);

        //将考试结果暂时保存在SESSION中并跳转
        JsfHelper.getRequest().getSession().setAttribute("tempExamCase", examCase);

        return result;

    }

    //检查是否有题目未做
    public String checkNull() {
        System.out.println("checking...");
        this.errStr = "";
        this.redirectId = "-1";
        StringBuilder sb = new StringBuilder();
        ExamCase ec = this.examCase;
        List<ExamCaseItemChoice> cqs = ec.getChoiceItems();
        List<ExamCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
        List<ExamCaseItemFill> fqs = ec.getFillItems();
        List<ExamCaseItemJudge> jqs = ec.getJudgeItems();
        List<ExamCaseItemEssay> eqs = ec.getEssayItems();
        List<ExamCaseItemFile> ffqs = ec.getFileItems();
        List<ExamCaseItemCase> ccqs = ec.getCaseItems();
        for (ExamCaseItemChoice c : cqs) {
            if (c.getUserAnswer() == null || "".equals(c.getUserAnswer())) {
                sb.append("您还有单选题未做；");
                if (this.redirectId.equals("-1")) {
                    this.redirectId = c.getId();
                }
                break;
            }
        }
        for (ExamCaseItemMultiChoice c : mcqs) {
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

        for (ExamCaseItemFill c : fqs) {
            List<ExamCaseItemFillBlock> ls = c.getBlocks();
            StringBuilder us = new StringBuilder();
            boolean tt = false;
            for (ExamCaseItemFillBlock e : ls) {
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

        for (ExamCaseItemJudge c : jqs) {
            if ("null".equals(c.getUserAnswer())) {
                sb.append("您还有判断题未做；");
                if (this.redirectId.equals("-1")) {
                    this.redirectId = c.getId();
                }
                break;
            }
        }
        for (ExamCaseItemEssay c : eqs) {
            if (c.getUserAnswer() == null || "".equals(c.getUserAnswer())) {
                sb.append("您还有问答题未做；");
                if (this.redirectId.equals("-1")) {
                    this.redirectId = c.getId();
                }
                break;
            }
        }
        for (ExamCaseItemFile c : ffqs) {
            if (!c.isAttached()) {
                sb.append("您还有文件题未做；");
                if (this.redirectId.equals("-1")) {
                    this.redirectId = c.getId();
                }
                break;
            }
        }

        for (ExamCaseItemCase c : ccqs) {
            List<ExamCaseItemChoice> cqs1 = c.getChoiceItems();
            List<ExamCaseItemEssay> eqs1 = c.getEssayItems();
            for (ExamCaseItemChoice cc : cqs1) {
                if (cc.getUserAnswer() == null || "".equals(cc.getUserAnswer())) {
                    sb.append("您还有综合题中的单选题未做；");
                    if (this.redirectId.equals("-1")) {
                        this.redirectId = cc.getId();
                    }
                    break;
                }
            }
            for (ExamCaseItemEssay cc : eqs1) {
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

    public String reserveSession() {
        MyLogger.echo("reserveSession sucess.");
        return null;
    }

    @PreDestroy
    public void destroy11() {
        MyLogger.echo("ExamCase destroyed.");
        iussService.logoutExam();
    }
}
