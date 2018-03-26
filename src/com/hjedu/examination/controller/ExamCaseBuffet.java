package com.hjedu.examination.controller;

import com.google.gson.annotations.Expose;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.service.IUserSessionStateService;
import com.hjedu.examination.dao.IBuffetExamCaseDAO;
import com.hjedu.examination.dao.IBuffetExaminationDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.entity.ExamMultiChoice;
import com.hjedu.examination.entity.buffet.BuffetExamCase;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemCase;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemChoice;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemEssay;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemFile;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemFill;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemFillBlock;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemJudge;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemMultiChoice;
import com.hjedu.examination.entity.buffet.BuffetExamination;
import com.hjedu.examination.service.impl.IBuffetExamCaseService;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.IpHelper;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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

@ManagedBean
@SessionScoped
public class ExamCaseBuffet implements Serializable {
    
    IBuffetExaminationDAO examinationDAO = SpringHelper.getSpringBean("BuffetExaminationDAO");
    IBuffetExamCaseService examCaseService = SpringHelper.getSpringBean("BuffetExamCaseService");
    IBuffetExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("BuffetExamCaseDAO");
    IFileQuestionDAO fileQuestionDAO = SpringHelper.getSpringBean("FileQuestionDAO");
    IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
    IUserSessionStateService iussService = SpringHelper.getSpringBean("UserSessionStateService");
    //ClientSession cs = JsfHelper.getBean("clientSession");
    @Expose
    BuffetExamCase examCase = null;
    Date begainTime = new Date();
    @Expose
    long remainingTime = 0;
    String errStr = "";
    String redirectId = "-1";
    private boolean submited = false;
    
    public Date getBegainTime() {
        return begainTime;
    }
    
    public void setBegainTime(Date begainTime) {
        this.begainTime = begainTime;
    }
    
    public BuffetExamCase getExamCase() {
        return examCase;
    }
    
    public void setBuffetExamCase(BuffetExamCase examCase) {
        this.examCase = examCase;
    }
    
    public long getRemainingTime() {
        return remainingTime;
    }
    
    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
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
        }
        BuffetExamination exam = (BuffetExamination) JsfHelper.getRequest().getSession().getAttribute("tempBuffetExam");
        HttpServletRequest request = JsfHelper.getRequest();
        this.initx(cs.getUsr(), exam, request);
    }
    
    /**
     * 从外部加载或者由本类调用加载
     * @param bu
     * @param exam
     * @param request 
     */
    
    public void initx(BbsUser bu, BuffetExamination exam, HttpServletRequest request) {
        begainTime = new Date();
        remainingTime = 0;
        errStr = "";
        redirectId = "-1";

        //设置新考试
        examCase = new BuffetExamCase();

        //JsfHelper.getRequest().removeAttribute("tempBuffetExam");
        this.examCase.setExamination(exam);
        this.examCaseService.buildExamCase(examCase);
        this.examCase.setUser(bu);
        
        MyLogger.echo("ExamCase initialized.");

//            ExamCaseSaver ecs = new ExamCaseSaver(examCase);
//            ThreadPoolTaskExecutor exec = SpringHelper.getSpringBean("taskExecutor");
//            exec.execute(ecs);
        //考试前扣用户积分（只针对新考试）
        if (bu != null) {
            //bu.setScore(bu.getScore() - examCase.getExamination().getScorePaid());
            IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
            bsl.log("考试消耗积分", (int) (-1 * exam.getScorePaid()));
            //this.userDAO.updateBbsUser(bu);
        }
        
        this.computeRemainingTime();
        this.examCase.setIp(IpHelper.getRemoteAddr(request));
        //iussService.loginExam(examCase);

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

        List<BuffetExamCaseItemChoice> chs1 = this.examCase.getChoiceItems();
        for (BuffetExamCaseItemChoice c : chs1) {
            if (!(c.getUserAnswer() == null || "".equals(c.getUserAnswer()))) {
                c.setDone(true);
                //System.out.println("done:" + c.getQuestion().getName());
            }
        }
        
        List<BuffetExamCaseItemMultiChoice> chs2 = this.examCase.getMultiChoiceItems();
        for (BuffetExamCaseItemMultiChoice c : chs2) {
            if (!(c.getUserAnswer() == null || "".equals(c.getUserAnswer()))) {
                c.setDone(true);
            }
        }
        
        List<BuffetExamCaseItemFill> chs3 = this.examCase.getFillItems();
        for (BuffetExamCaseItemFill c : chs3) {
            List<BuffetExamCaseItemFillBlock> ls = c.getBlocks();
            StringBuilder us = new StringBuilder();
            boolean tt = false;
            for (BuffetExamCaseItemFillBlock e : ls) {
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
        
        List<BuffetExamCaseItemJudge> chs4 = this.examCase.getJudgeItems();
        for (BuffetExamCaseItemJudge c : chs4) {
            if (!((c.getUserAnswer() == null || "".equals(c.getUserAnswer()) || "null".equals(c.getUserAnswer())))) {
                c.setDone(true);
                //System.out.println("done:" + c.getQuestion().getName());
            }
        }
        
        List<BuffetExamCaseItemEssay> chs5 = this.examCase.getEssayItems();
        for (BuffetExamCaseItemEssay c : chs5) {
            if (!(c.getUserAnswer() == null || "".equals(c.getUserAnswer()))) {
                c.setDone(true);
                //System.out.println("done:" + c.getQuestion().getName());
            }
        }
        
        List<BuffetExamCaseItemFile> chs6 = this.examCase.getFileItems();
        for (BuffetExamCaseItemFile c : chs6) {
            if (c.isAttached()) {
                c.setDone(true);
                //System.out.println("done:" + c.getQuestion().getName());

            }
        }
        
        List<BuffetExamCaseItemCase> chs7 = this.examCase.getCaseItems();
        for (BuffetExamCaseItemCase cc : chs7) {
            List<BuffetExamCaseItemChoice> chs11 = cc.getChoiceItems();
            for (BuffetExamCaseItemChoice c : chs11) {
                if (!(c.getUserAnswer() == null || "".equals(c.getUserAnswer()))) {
                    c.setDone(true);
                    //System.out.println("done:" + c.getQuestion().getName());
                }
            }
            
            List<BuffetExamCaseItemMultiChoice> chs21 = cc.getMultiChoiceItems();
            for (BuffetExamCaseItemMultiChoice c : chs21) {
                if (!(c.getUserAnswer() == null || "".equals(c.getUserAnswer()))) {
                    c.setDone(true);
                }
            }
            
            List<BuffetExamCaseItemFill> chs31 = cc.getFillItems();
            for (BuffetExamCaseItemFill c : chs31) {
                List<BuffetExamCaseItemFillBlock> ls = c.getBlocks();
                StringBuilder us = new StringBuilder();
                boolean tt = false;
                for (BuffetExamCaseItemFillBlock e : ls) {
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
            
            List<BuffetExamCaseItemJudge> chs41 = cc.getJudgeItems();
            for (BuffetExamCaseItemJudge c : chs41) {
                if (!((c.getUserAnswer() == null || "".equals(c.getUserAnswer()) || "null".equals(c.getUserAnswer())))) {
                    c.setDone(true);
                    //System.out.println("done:" + c.getQuestion().getName());
                }
            }
            
            List<BuffetExamCaseItemEssay> chs51 = cc.getEssayItems();
            for (BuffetExamCaseItemEssay c : chs51) {
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
            List<BuffetExamCaseItemChoice> chs = this.examCase.getChoiceItems();
            for (BuffetExamCaseItemChoice c : chs) {
                if (c.getId().equals(id)) {
                    c.setDone(true);
                    //System.out.println("done:" + c.getQuestion().getName());
                    break;
                }
                
            }
        }
        if (type.equals("mchoice")) {
            List<BuffetExamCaseItemMultiChoice> chs = this.examCase.getMultiChoiceItems();
            for (BuffetExamCaseItemMultiChoice c : chs) {
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
            List<BuffetExamCaseItemFill> chs = this.examCase.getFillItems();
            for (BuffetExamCaseItemFill c : chs) {
                if (c.getId().equals(id)) {
                    List<BuffetExamCaseItemFillBlock> ls = c.getBlocks();
                    StringBuilder us = new StringBuilder();
                    boolean tt = false;
                    for (BuffetExamCaseItemFillBlock e : ls) {
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
            List<BuffetExamCaseItemJudge> chs = this.examCase.getJudgeItems();
            for (BuffetExamCaseItemJudge c : chs) {
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
            List<BuffetExamCaseItemEssay> chs = this.examCase.getEssayItems();
            for (BuffetExamCaseItemEssay c : chs) {
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
            List<BuffetExamCaseItemFile> chs = this.examCase.getFileItems();
            for (BuffetExamCaseItemFile c : chs) {
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
            BuffetExamCaseItemFile nm = null;
            while ((ui != null) && (!(ui instanceof UIData))) {
                ui = ui.getParent();
            }
            if ((ui != null) && (ui instanceof UIData)) {
                Object rowData = ((UIData) ui).getRowData();
                if (rowData instanceof BuffetExamCaseItemFile) {
                    nm = (BuffetExamCaseItemFile) rowData;
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
        if (cs.getUsr() == null) {
            JsfHelper.error("登录信息已经失效");
            return null;
        }
        examCase.setStat("submitted");
        this.submited = true;
        String result = "BuffetExamCaseResult?faces-redirect=true";
        examCase.setSubmitTime(new Date());
        examCase.setIfPub(true);
        examCase.setBusinessId(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        
        this.examCase = this.examCaseService.computeExamCase(examCase);
        
        this.examCaseDAO.addBuffetExamCase(examCase);

        //System.out.println("------------------------------------");
        List<BuffetExamCaseItemChoice> bics = examCase.getChoiceItems();
        for (BuffetExamCaseItemChoice ei : bics) {
            //System.out.println("题目：" + ei.getQuestion().getName() + ",选择：" + ei.getUserAnswer() + "，答案：" + ei.getRightAnswer() + "，是否正确：" + ei.getIfRight());

        }

        //this.examCaseService.computeExamCase(examCase);
        //this.examCaseDAO.addExamCase(examCase);
        //iussService.logoutExam();
        //考试后给用户加积分
        BbsUser bu = cs.getUsr();
        //bu.setScore(bu.getScore() + examCase.getBbsScore());
        IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
        bsl.log("考试取得成绩自动积分", (int) examCase.getBbsScore());
        //this.userDAO.updateBbsUser(bu);

        //将考试结果暂时保存在SESSION中并跳转
        JsfHelper.getRequest().getSession().setAttribute("tempBuffetExamCase", examCase);
        
        return result;
        
    }

    //检查是否有题目未做
    public void checkNull() {
        System.out.println("checking...");
        this.errStr = "";
        this.redirectId = "-1";
        StringBuilder sb = new StringBuilder();
        BuffetExamCase ec = this.examCase;
        List<BuffetExamCaseItemChoice> cqs = ec.getChoiceItems();
        List<BuffetExamCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
        List<BuffetExamCaseItemFill> fqs = ec.getFillItems();
        List<BuffetExamCaseItemJudge> jqs = ec.getJudgeItems();
        List<BuffetExamCaseItemEssay> eqs = ec.getEssayItems();
        List<BuffetExamCaseItemFile> ffqs = ec.getFileItems();
        List<BuffetExamCaseItemCase> ccqs = ec.getCaseItems();
        for (BuffetExamCaseItemChoice c : cqs) {
            if (c.getUserAnswer() == null || "".equals(c.getUserAnswer())) {
                sb.append("您还有单选题未做；");
                if (this.redirectId.equals("-1")) {
                    this.redirectId = c.getId();
                }
                break;
            }
        }
        for (BuffetExamCaseItemMultiChoice c : mcqs) {
            List<String> ls = c.getSelectedLabels();
            boolean nul = false;
            if (ls == null) {
                nul = true;
            } else if (ls.isEmpty()) {
                nul = true;
            }
            if (nul) {
                sb.append("您还有多选题未做；");
                if (this.redirectId.equals("-1")) {
                    this.redirectId = c.getId();
                }
                break;
            }
        }
        
        for (BuffetExamCaseItemFill c : fqs) {
            List<BuffetExamCaseItemFillBlock> ls = c.getBlocks();
            StringBuilder us = new StringBuilder();
            boolean tt = false;
            for (BuffetExamCaseItemFillBlock e : ls) {
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
        
        for (BuffetExamCaseItemJudge c : jqs) {
            if ("null".equals(c.getUserAnswer())) {
                sb.append("您还有判断题未做；");
                if (this.redirectId.equals("-1")) {
                    this.redirectId = c.getId();
                }
                break;
            }
        }
        for (BuffetExamCaseItemEssay c : eqs) {
            if (c.getUserAnswer() == null || "".equals(c.getUserAnswer())) {
                sb.append("您还有问答题未做；");
                if (this.redirectId.equals("-1")) {
                    this.redirectId = c.getId();
                }
                break;
            }
        }
        for (BuffetExamCaseItemFile c : ffqs) {
            if (!c.isAttached()) {
                sb.append("您还有文件题未做；");
                if (this.redirectId.equals("-1")) {
                    this.redirectId = c.getId();
                }
                break;
            }
        }
        
        for (BuffetExamCaseItemCase c : ccqs) {
            List<BuffetExamCaseItemChoice> cqs1 = c.getChoiceItems();
            List<BuffetExamCaseItemEssay> eqs1 = c.getEssayItems();
            for (BuffetExamCaseItemChoice cc : cqs1) {
                if (cc.getUserAnswer() == null || "".equals(cc.getUserAnswer())) {
                    sb.append("您还有综合题中的单选题未做；");
                    if (this.redirectId.equals("-1")) {
                        this.redirectId = cc.getId();
                    }
                    break;
                }
            }
            for (BuffetExamCaseItemEssay cc : eqs1) {
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
