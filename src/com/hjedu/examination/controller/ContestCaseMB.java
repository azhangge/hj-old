package com.hjedu.examination.controller;

import com.google.gson.annotations.Expose;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IContestCaseDAO;
import com.hjedu.examination.dao.IExamContestDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.entity.ExamMultiChoice;
import com.hjedu.examination.entity.contest.ContestCase;
import com.hjedu.examination.entity.contest.ContestCaseItemAdapter;
import com.hjedu.examination.entity.contest.ContestCaseItemCase;
import com.hjedu.examination.entity.contest.ContestCaseItemChoice;
import com.hjedu.examination.entity.contest.ContestCaseItemEssay;
import com.hjedu.examination.entity.contest.ContestCaseItemFile;
import com.hjedu.examination.entity.contest.ContestCaseItemFill;
import com.hjedu.examination.entity.contest.ContestCaseItemFillBlock;
import com.hjedu.examination.entity.contest.ContestCaseItemJudge;
import com.hjedu.examination.entity.contest.ContestCaseItemMultiChoice;
import com.hjedu.examination.entity.contest.ExamContestSession;
import com.hjedu.examination.entity.random2.Random2PaperPart;
import com.hjedu.examination.service.impl.IContestCaseRandom2Service;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.huajie.exam.agent.ContestAgent;
import com.huajie.exam.thread.ContestCaseRanker;
import com.huajie.util.IpHelper;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@ManagedBean
@ViewScoped
public class ContestCaseMB implements Serializable {

    IExamContestDAO examinationDAO = SpringHelper.getSpringBean("ExamContestDAO");
    IContestCaseRandom2Service examCaseService = SpringHelper.getSpringBean("ContestCaseRandom2Service");
    IContestCaseDAO examCaseDAO = SpringHelper.getSpringBean("ContestCaseDAO");
    IFileQuestionDAO fileQuestionDAO = SpringHelper.getSpringBean("FileQuestionDAO");
    IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
    //ClientSession cs = JsfHelper.getBean("clientSession");
    @Expose
    ContestCase examCase = null;
    Date begainTime = new Date();
    long remainingTime = 0;
    String errStr = "";
    String redirectId = "-1";
    int redirectIndex = -1;
    int total = 0;

    private boolean submited = false;
    String tempFileId = null;
    Map<Integer, ContestCaseItemAdapter> adapterMap = new HashMap();

    public Date getBegainTime() {
        return begainTime;
    }

    public void setBegainTime(Date begainTime) {
        this.begainTime = begainTime;
    }

    public ContestCase getExamCase() {
        return examCase;
    }

    public void setExamCase(ContestCase examCase) {
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

    public int getRedirectIndex() {
        return redirectIndex;
    }

    public void setRedirectIndex(int redirectIndex) {
        this.redirectIndex = redirectIndex;
    }

    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        String case_id = request.getParameter("case_id");
        //设置新考试
        String exam_id = request.getParameter("exam_id");
        String id = exam_id;
        if (exam_id == null) {
            return;
        }
        ClientSession cs = JsfHelper.getBean("clientSession");
        if (cs.getUsr() == null) {
            return;
        }
        this.initx(exam_id, cs.getUsr(), request);

    }

    /**
     * 初始化竞赛，本方法适用于从WEB或者APK调用
     *
     * @param examId
     * @param bu
     */
    public void initx(String examId, BbsUser bu, HttpServletRequest request) {

        examCase = new ContestCase();
        ExamContestSession exam = this.examinationDAO.findExamContest(examId);
        this.examCase.setExamination(exam);
        this.examCase.setUser(bu);
        this.examCase.setIp(IpHelper.getRemoteAddr(request));
        //根据竞赛的试题存根构建一个新的考试实例，以保证所有考生同一期竞赛的试题是一样的
        this.examCase = ContestAgent.buildContestCase(examCase);

        MyLogger.echo("ExamCase initialized.");

        //考试前扣用户积分（只针对新考试）
        if (bu != null) {
            //bu.setScore(bu.getScore() - examCase.getExamination().getScorePaid());
            IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
            bsl.log("竞赛消耗积分", (int) (-1 * examCase.getExamination().getScorePaid()));
            //this.userDAO.updateBbsUser(bu);
        }
        this.loadItemIndex();
        this.computeRemainingTime();
    }

    /**
     * 加载试题的编号
     */
    public void loadItemIndex() {
        //以下代码加载试题编号
        List<Random2PaperPart> parts = this.examCase.getCparts();
        for (Random2PaperPart part : parts) {
            List<ContestCaseItemAdapter> adapters = part.getCadapters();
            for (ContestCaseItemAdapter adapter : adapters) {
                if (adapter.getQtype().equals("choice")) {
                    adapter.getChoiceItem().setIndex(++total);
                    adapter.setOrd(total);
                    this.adapterMap.put(total, adapter);
                } else if (adapter.getQtype().equals("mchoice")) {
                    adapter.getMultiChoiceItem().setIndex(++total);
                    adapter.setOrd(total);
                    this.adapterMap.put(total, adapter);
                } else if (adapter.getQtype().equals("fill")) {
                    adapter.getFillItem().setIndex(++total);
                    adapter.setOrd(total);
                    this.adapterMap.put(total, adapter);
                } else if (adapter.getQtype().equals("judge")) {
                    adapter.getJudgeItem().setIndex(++total);
                    adapter.setOrd(total);
                    this.adapterMap.put(total, adapter);
                } else if (adapter.getQtype().equals("essay")) {
                    adapter.getEssayItem().setIndex(++total);
                    adapter.setOrd(total);
                    this.adapterMap.put(total, adapter);
                } else if (adapter.getQtype().equals("file")) {
                    adapter.getFileItem().setIndex(++total);
                    adapter.setOrd(total);
                    this.adapterMap.put(total, adapter);
                } else if (adapter.getQtype().equals("case")) {
                    adapter.getCaseItem().setIndex(total + 1);//加综合题的顺序设置其中小题的第一个，以方便恢复顺序
                    //综合题的试题编号保存在item中，adapter不能说明问题
                    for (ContestCaseItemChoice ei : adapter.getCaseItem().getChoiceItems()) {
                        ei.setIndex(++total);
                        this.adapterMap.put(total, adapter);
                    }
                    for (ContestCaseItemMultiChoice ei : adapter.getCaseItem().getMultiChoiceItems()) {
                        ei.setIndex(++total);
                        this.adapterMap.put(total, adapter);
                    }
                    for (ContestCaseItemFill ei : adapter.getCaseItem().getFillItems()) {
                        ei.setIndex(++total);
                        this.adapterMap.put(total, adapter);
                    }
                    for (ContestCaseItemJudge ei : adapter.getCaseItem().getJudgeItems()) {
                        ei.setIndex(++total);
                        this.adapterMap.put(total, adapter);
                    }
                    for (ContestCaseItemEssay ei : adapter.getCaseItem().getEssayItems()) {
                        ei.setIndex(++total);
                        this.adapterMap.put(total, adapter);
                    }
                }
            }
        }
    }

    public void computeRemainingTime() {
        long n = System.currentTimeMillis();
        long o = this.begainTime.getTime();
        long len = this.examCase.getExamination().getTimeLen() * 60;
        long past = (long) (n - o) / 1000;
        this.remainingTime = len - past - examCase.getTimePassed();
    }

    //恢复页面左边做题进度的显示情况
    public String checkDoneQuestion() {
        //System.out.println(type + ":" + id);

        List<ContestCaseItemChoice> chs1 = this.examCase.getChoiceItems();
        for (ContestCaseItemChoice c : chs1) {
            if (!(c.getUserAnswer() == null || "".equals(c.getUserAnswer()))) {
                c.setDone(true);
                //System.out.println("done:" + c.getQuestion().getName());
            }
        }

        List<ContestCaseItemMultiChoice> chs2 = this.examCase.getMultiChoiceItems();
        for (ContestCaseItemMultiChoice c : chs2) {
            if (!(c.getUserAnswer() == null || "".equals(c.getUserAnswer()))) {
                c.setDone(true);
            }
        }

        List<ContestCaseItemFill> chs3 = this.examCase.getFillItems();
        for (ContestCaseItemFill c : chs3) {
            List<ContestCaseItemFillBlock> ls = c.getBlocks();
            StringBuilder us = new StringBuilder();
            boolean tt = false;
            for (ContestCaseItemFillBlock e : ls) {
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

        List<ContestCaseItemJudge> chs4 = this.examCase.getJudgeItems();
        for (ContestCaseItemJudge c : chs4) {
            if (!((c.getUserAnswer() == null || "".equals(c.getUserAnswer()) || "null".equals(c.getUserAnswer())))) {
                c.setDone(true);
                //System.out.println("done:" + c.getQuestion().getName());
            }
        }

        List<ContestCaseItemEssay> chs5 = this.examCase.getEssayItems();
        for (ContestCaseItemEssay c : chs5) {
            if (!(c.getUserAnswer() == null || "".equals(c.getUserAnswer()))) {
                c.setDone(true);
                //System.out.println("done:" + c.getQuestion().getName());
            }
        }

        List<ContestCaseItemFile> chs6 = this.examCase.getFileItems();
        for (ContestCaseItemFile c : chs6) {
            if (c.isAttached()) {
                c.setDone(true);
                //System.out.println("done:" + c.getQuestion().getName());

            }
        }

        List<ContestCaseItemCase> chs7 = this.examCase.getCaseItems();
        for (ContestCaseItemCase cc : chs7) {
            List<ContestCaseItemChoice> chs11 = cc.getChoiceItems();
            for (ContestCaseItemChoice c : chs11) {
                if (!(c.getUserAnswer() == null || "".equals(c.getUserAnswer()))) {
                    c.setDone(true);
                    //System.out.println("done:" + c.getQuestion().getName());
                }
            }

            List<ContestCaseItemMultiChoice> chs21 = cc.getMultiChoiceItems();
            for (ContestCaseItemMultiChoice c : chs21) {
                if (!(c.getUserAnswer() == null || "".equals(c.getUserAnswer()))) {
                    c.setDone(true);
                }
            }

            List<ContestCaseItemFill> chs31 = cc.getFillItems();
            for (ContestCaseItemFill c : chs31) {
                List<ContestCaseItemFillBlock> ls = c.getBlocks();
                StringBuilder us = new StringBuilder();
                boolean tt = false;
                for (ContestCaseItemFillBlock e : ls) {
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

            List<ContestCaseItemJudge> chs41 = cc.getJudgeItems();
            for (ContestCaseItemJudge c : chs41) {
                if (!((c.getUserAnswer() == null || "".equals(c.getUserAnswer()) || "null".equals(c.getUserAnswer())))) {
                    c.setDone(true);
                    //System.out.println("done:" + c.getQuestion().getName());
                }
            }

            List<ContestCaseItemEssay> chs51 = cc.getEssayItems();
            for (ContestCaseItemEssay c : chs51) {
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
            List<ContestCaseItemChoice> chs = this.examCase.getChoiceItems();
            for (ContestCaseItemChoice c : chs) {
                if (c.getId().equals(id)) {
                    c.setDone(true);
                    //System.out.println("done:" + c.getQuestion().getName());
                    break;
                }

            }
        }
        if (type.equals("mchoice")) {
            List<ContestCaseItemMultiChoice> chs = this.examCase.getMultiChoiceItems();
            for (ContestCaseItemMultiChoice c : chs) {
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
            List<ContestCaseItemFill> chs = this.examCase.getFillItems();
            for (ContestCaseItemFill c : chs) {
                if (c.getId().equals(id)) {
                    List<ContestCaseItemFillBlock> ls = c.getBlocks();
                    StringBuilder us = new StringBuilder();
                    boolean tt = false;
                    for (ContestCaseItemFillBlock e : ls) {
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
            List<ContestCaseItemJudge> chs = this.examCase.getJudgeItems();
            for (ContestCaseItemJudge c : chs) {
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
            List<ContestCaseItemEssay> chs = this.examCase.getEssayItems();
            for (ContestCaseItemEssay c : chs) {
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
            List<ContestCaseItemFile> chs = this.examCase.getFileItems();
            for (ContestCaseItemFile c : chs) {
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

    public String preUpAction(String id) {
        this.tempFileId = id;
        System.out.println("temp file id is getten");
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
            ContestCaseItemFile nm = null;
            while ((ui != null) && (!(ui instanceof UIData))) {
                ui = ui.getParent();
            }
            System.out.println(this.tempFileId);
            if ((ui != null) && (ui instanceof UIData)) {

                Object rowData = ((UIData) ui).getRowData();
                System.out.println(rowData);
                if (rowData instanceof ContestCaseItemAdapter) {
                    ContestCaseItemAdapter ad = (ContestCaseItemAdapter) rowData;
                    System.out.println(ad);
                    nm = ad.getFileItem();
                }
            }
            if (nm != null) {
                imgId = nm.getId();
            } else if (tempFileId != null) {
                imgId = tempFileId;
                //MyLogger.echo("通过F:PARAM取得ID");
            } else {
                MyLogger.echo("未取得正在操作的ContestCaseItemFile的ID");
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
        if (cs.getUsr() == null) {
            JsfHelper.error("登录信息已经失效");
            return null;
        }
        examCase.setStat("submitted");
        this.submited = true;
        String result = "ContestCaseResult?faces-redirect=true";
        examCase.setSubmitTime(new Date());
        examCase.setIfPub(examCase.getExamination().isIfDirect());

        this.examCase = this.examCaseService.computeExamCase(examCase);
        if (this.examCaseDAO.findContestCase(examCase.getId()) == null) {
            this.examCaseDAO.addContestCase(examCase);
        } else {
            this.examCaseDAO.updateContestCase(examCase);
        }

        //this.examCaseService.computeExamCase(examCase);
        //this.examCaseDAO.addExamCase(examCase);
        //iussService.logoutExam();
        //考试后给用户加积分
        //bu.setScore(bu.getScore() + examCase.getBbsScore());
        IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
        bsl.log("考试取得成绩自动积分", (int) examCase.getBbsScore());
        //this.userDAO.updateBbsUser(bu);

        //将考试结果暂时保存在SESSION中并跳转
        JsfHelper.getRequest().getSession().setAttribute("tempContestCase", examCase);

        //每次有新的竞赛参加者，对排名进行计算
        ThreadPoolTaskExecutor exec = SpringHelper.getSpringBean("taskExecutor");
        exec.execute(new ContestCaseRanker(this.examCase.getExamination().getId(), this.examCase.getSessionStr()));

        return result;

    }



}
