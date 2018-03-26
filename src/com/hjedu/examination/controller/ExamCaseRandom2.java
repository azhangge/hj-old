package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.service.IUserSessionStateService;
import com.hjedu.examination.dao.IExamCaseDAO;
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
import com.hjedu.examination.entity.ExamMultiChoice;
import com.hjedu.examination.entity.VirtualExamPart;
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
@ViewScoped
public class ExamCaseRandom2 implements Serializable {

    IExaminationDAO examinationDAO = SpringHelper.getSpringBean("ExaminationDAO");
    IExamCaseService examCaseService = SpringHelper.getSpringBean("ExamCaseRandom2Service");
    IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
    IFileQuestionDAO fileQuestionDAO = SpringHelper.getSpringBean("FileQuestionDAO");
    IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
    IUserSessionStateService iussService = SpringHelper.getSpringBean("UserSessionStateService");
    //ClientSession cs = JsfHelper.getBean("clientSession");
    ExamCase examCase = null;
    Date begainTime = new Date();
    long remainingTime = 0;
    String errStr = "";
    String redirectId = "-1";
    int redirectIndex = -1;
    int total = 0;
    private boolean submited = false;
    boolean flag = false;

    int currentIndex = 1;
    ExamCaseItemAdapter currentAdapter;
    Map<Integer, ExamCaseItemAdapter> adapterMap = new HashMap();
    boolean curentIfRight = false;

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

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getRedirectIndex() {
        return redirectIndex;
    }

    public void setRedirectIndex(int redirectIndex) {
        this.redirectIndex = redirectIndex;
    }

    public ExamCase getExamCase() {
        return examCase;
    }

    public void setExamCase(ExamCase examCase) {
        this.examCase = examCase;
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

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public ExamCaseItemAdapter getCurrentAdapter() {
        return currentAdapter;
    }

    public void setCurrentAdapter(ExamCaseItemAdapter currentAdapter) {
        this.currentAdapter = currentAdapter;
    }

    public Map<Integer, ExamCaseItemAdapter> getAdapterMap() {
        return adapterMap;
    }

    public void setAdapterMap(Map<Integer, ExamCaseItemAdapter> adapterMap) {
        this.adapterMap = adapterMap;
    }

    public boolean isCurentIfRight() {
        return curentIfRight;
    }

    public void setCurentIfRight(boolean curentIfRight) {
        this.curentIfRight = curentIfRight;
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
        ClientSession cs = JsfHelper.getBean("clientSession");
        if (cs.getUsr() == null) {
            return;
        } else {
            //设置保险用户名
            this.secretUrn = cs.getUsr().getUsername();
        }
        HttpServletRequest request = JsfHelper.getRequest();
        String case_id = request.getParameter("case_id");
        begainTime = new Date();
        if (case_id != null) {//恢复老考试
            examCase = this.examCaseDAO.findExamCase(case_id);
            this.examCaseService.resumeExamCase(examCase);
            //examCase.setGenTime(this.begainTime);
            this.flag = true;
            this.checkDoneQuestion();//只针对恢复的保存过的考试
        } else {//设置新考试
            String exam_id = request.getParameter("exam_id");
            String id = exam_id;
            if (exam_id == null) {
                return;
            }

            this.examCase = ExamPaperPool.takePaper(id);//从试卷池中取一份试卷

//            examCase = new ExamCase();
//            Examination exam = this.examinationDAO.findExamination(exam_id);
//            this.examCase.setExamination(exam);
//            this.examCaseService.buildExamCase(examCase);
//            
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
        iussService.loginExam(examCase);

        this.adapterMap = new HashMap();
        //以下代码加载试题编号
        List<VirtualExamPart> parts = this.examCase.getVparts();
        for (VirtualExamPart part : parts) {
            List<ExamCaseItemAdapter> adapters = part.getAdapters();
            for (ExamCaseItemAdapter adapter : adapters) {
                if (adapter.getQtype().equals("choice")) {
                    adapter.getChoiceItem().setIndex(++total);
                    this.adapterMap.put(total, adapter);
                } else if (adapter.getQtype().equals("mchoice")) {
                    adapter.getMultiChoiceItem().setIndex(++total);
                    this.adapterMap.put(total, adapter);
                } else if (adapter.getQtype().equals("fill")) {
                    adapter.getFillItem().setIndex(++total);
                    this.adapterMap.put(total, adapter);
                } else if (adapter.getQtype().equals("judge")) {
                    adapter.getJudgeItem().setIndex(++total);
                    this.adapterMap.put(total, adapter);
                } else if (adapter.getQtype().equals("essay")) {
                    adapter.getEssayItem().setIndex(++total);
                    this.adapterMap.put(total, adapter);
                } else if (adapter.getQtype().equals("file")) {
                    adapter.getFileItem().setIndex(++total);
                    this.adapterMap.put(total, adapter);
                } else if (adapter.getQtype().equals("case")) {
                    for (ExamCaseItemChoice ei : adapter.getCaseItem().getChoiceItems()) {
                        ei.setIndex(++total);
                        this.adapterMap.put(total, adapter);
                    }
                    for (ExamCaseItemMultiChoice ei : adapter.getCaseItem().getMultiChoiceItems()) {
                        ei.setIndex(++total);
                        this.adapterMap.put(total, adapter);
                    }
                    for (ExamCaseItemFill ei : adapter.getCaseItem().getFillItems()) {
                        ei.setIndex(++total);
                        this.adapterMap.put(total, adapter);
                    }
                    for (ExamCaseItemJudge ei : adapter.getCaseItem().getJudgeItems()) {
                        ei.setIndex(++total);
                        this.adapterMap.put(total, adapter);
                    }
                    for (ExamCaseItemEssay ei : adapter.getCaseItem().getEssayItems()) {
                        ei.setIndex(++total);
                        this.adapterMap.put(total, adapter);
                    }
                }
            }
        }
        this.currentAdapter = this.adapterMap.get(currentIndex);
    }

    public void checkCurrent() {
        ExamCaseItemAdapter adapter = this.currentAdapter;
        this.curentIfRight = this.examCaseService.computeSingleAdapter(adapter);
        if (curentIfRight) {
            //this.nextAdapter();
            examCase.setProgress(this.currentIndex);
            ExamCaseSaver ecs = new ExamCaseSaver(examCase);
            ThreadPoolTaskExecutor exec = SpringHelper.getSpringBean("taskExecutor");
            exec.execute(ecs);
        }
    }

    public void nextAdapter() {
        if (this.currentIndex < this.adapterMap.size()) {
            this.currentIndex++;
            this.currentAdapter = this.adapterMap.get(currentIndex);
        }
    }

    public void computeRemainingTime() {
        long n = System.currentTimeMillis();
        long o = this.begainTime.getTime();
        long len = this.examCase.getExamination().getTimeLen() * 60;
        long past = (long) (n - o) / 1000;
        this.remainingTime = len - past - examCase.getTimePassed();
    }

    public String saveExamCase(String str) {
        this.flag = true;
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
            ExamCaseItemFile nm = null;
            while ((ui != null) && (!(ui instanceof UIData))) {
                ui = ui.getParent();
            }
            if ((ui != null) && (ui instanceof UIData)) {
                Object rowData = ((UIData) ui).getRowData();
                if (rowData instanceof ExamCaseItemFile) {
                    nm = (ExamCaseItemFile) rowData;
                }
            }
            if (nm != null) {
                imgId = nm.getId();
            } else {
                MyLogger.echo("未取得正在操作的ExamCaseItemFile的ID");
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
        examCase.setStat("submitted");
        this.submited = true;
        String result = "ExamCaseResult?faces-redirect=true";
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
            //处理好后加入存储队列
            ExamCaseSaver.saveProcessedExamCase(examCase);
//            if (!flag) {
//                this.examCaseDAO.addExamCase(examCase);
//            } else {
//                this.examCaseDAO.updateExamCase(examCase);
//            }
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
        this.redirectIndex = -1;
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
                if (this.redirectIndex == -1) {
                    this.redirectIndex = c.getIndex();
                }
                break;
            }
        }
        for (ExamCaseItemMultiChoice c : mcqs) {
            List<String> ls = c.getSelectedLabels();
            boolean nul = false;
            if (ls == null) {
                nul = true;
            } else {
                if (ls.isEmpty()) {
                    nul = true;
                }
            }
            if (nul) {
                sb.append("您还有多选题未做；");
                if (this.redirectId.equals("-1")) {
                    this.redirectId = c.getId();
                }
                if (this.redirectIndex == -1) {
                    this.redirectIndex = c.getIndex();
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
                    if (this.redirectIndex == -1) {
                        this.redirectIndex = c.getIndex();
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
                if (this.redirectIndex == -1) {
                    this.redirectIndex = c.getIndex();
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
                if (this.redirectIndex == -1) {
                    this.redirectIndex = c.getIndex();
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
                if (this.redirectIndex == -1) {
                    this.redirectIndex = c.getIndex();
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
                    if (this.redirectIndex == -1) {
                        this.redirectIndex = c.getIndex();
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
                    if (this.redirectIndex == -1) {
                        this.redirectIndex = c.getIndex();
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
