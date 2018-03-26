package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.dao.IModuleExamCaseDAO;
import com.hjedu.examination.dao.IModuleExamInfoDAO;
import com.hjedu.examination.entity.ExamChoice;
import com.hjedu.examination.entity.ExamModuleModel;
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
import com.hjedu.examination.entity.module.ModuleExamInfo;
import com.hjedu.examination.service.IModuleExamCaseService;
import com.hjedu.platform.controller.ApplicationBean;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.huajie.util.IpHelper;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ModuleExamCaseMB implements Serializable {

    IModuleExamCaseService examCaseService = SpringHelper.getSpringBean("ModuleExamCaseService");
    IModuleExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ModuleExamCaseDAO");
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    IFileQuestionDAO fileQuestionDAO = SpringHelper.getSpringBean("FileQuestionDAO");
    IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
    IModuleExamInfoDAO examInfoDAO = SpringHelper.getSpringBean("ModuleExamInfoDAO");
    //IUserSessionStateService iussService = SpringHelper.getSpringBean("UserSessionStateService");
    ClientSession cs = JsfHelper.getBean("clientSession");
    ModuleExamCase examCase = null;
    ModuleExamInfo examInfo;
    ExamModuleModel module;
    Date begainTime = new Date();

    String errStr = "";
    String redirectId = "-1";
    private boolean submited = false;

    public Date getBegainTime() {
        return begainTime;
    }

    public void setBegainTime(Date begainTime) {
        this.begainTime = begainTime;
    }

    public ModuleExamCase getExamCase() {
        return examCase;
    }

    public void setExamCase(ModuleExamCase examCase) {
        this.examCase = examCase;
    }

    public boolean isSubmited() {
        return submited;
    }

    public void setSubmited(boolean submited) {
        this.submited = submited;
    }

    public ModuleExamInfo getExamInfo() {
        return examInfo;
    }

    public void setExamInfo(ModuleExamInfo examInfo) {
        this.examInfo = examInfo;
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
        this.examInfo=this.examInfoDAO.findModuleExamInfo();
        HttpServletRequest request = JsfHelper.getRequest();
        
        examCase=new ModuleExamCase();
        examCase.setModuleType("random");

        String module_id = request.getParameter("module_id");
        if (module_id != null) {
            this.module=this.moduleDAO.findExamModuleModel(module_id);
            examCase.setExamModule(module);
        }
        
        String choiceNum = request.getParameter("choice_num");
        if (choiceNum != null) {
            int x=Integer.parseInt(choiceNum);
            int xx=x<module.getChoiceNum()?x:(int)module.getChoiceNum();
            this.examCase.setChoiceTotal(xx);
        }
        String multiChoiceNum = request.getParameter("multi_choice_num");
        if (multiChoiceNum != null) {
            int x=Integer.parseInt(multiChoiceNum);
            int xx=x<module.getMultiChoiceNum()?x:(int)module.getMultiChoiceNum();
            this.examCase.setMultiChoiceTotal(xx);
        }
        String fillNum = request.getParameter("fill_num");
        if (fillNum != null) {
            int x=Integer.parseInt(fillNum);
            int xx=x<module.getFillNum()?x:(int)module.getFillNum();
            this.examCase.setFillTotal(xx);
        }
        String judgeNum = request.getParameter("judge_num");
        if (judgeNum != null) {
            int x=Integer.parseInt(judgeNum);
            int xx=x<module.getJudgeNum()?x:(int)module.getJudgeNum();
            this.examCase.setJudgeTotal(xx);
        }
        String essayNum = request.getParameter("essay_num");
        if (essayNum != null) {
            int x=Integer.parseInt(essayNum);
            int xx=x<module.getEssayNum()?x:(int)module.getEssayNum();
            this.examCase.setEssayTotal(xx);
        }
        String fileNum = request.getParameter("file_num");
        if (fileNum != null) {
            int x=Integer.parseInt(fileNum);
            int xx=x<module.getFileNum()?x:(int)module.getFileNum();
            this.examCase.setFileTotal(xx);
        }
        String caseNum = request.getParameter("case_num");
        if (caseNum != null) {
            int x=Integer.parseInt(caseNum);
            int xx=x<module.getCaseNum()?x:(int)module.getCaseNum();
            this.examCase.setCaseTotal(xx);
        }

        
        
        this.examCaseService.buildExamCase(examCase);

        this.examCase.setUser(this.cs.getUsr());

        MyLogger.echo("ModuleExamCase initialized.");
        this.examCase.setIp(IpHelper.getRemoteAddr(request));

        //考试前扣用户积分
        BbsUser bu = cs.getUsr();
        if (bu != null) {
            IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
            bsl.log("章节随机练习消耗积分", (int) (-1 * examInfo.getScorePaid()));
            this.userDAO.updateBbsUser(bu);
        }
    }

    public void changSelect(long questionId, long choiceId) {
        //MyLogger.echo(questionId + ":" + choiceId);
        List<ModuleExamCaseItemChoice> ecics = this.examCase.getChoiceItems();
        for (ModuleExamCaseItemChoice e : ecics) {
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

    public String doQuestion(String type, String id) {
        //System.out.println(type + ":" + id);
        if (type.equals("choice")) {
            List<ModuleExamCaseItemChoice> chs = this.examCase.getChoiceItems();
            for (ModuleExamCaseItemChoice c : chs) {
                if (c.getId().equals(id)) {
                    c.setDone(true);
                    //System.out.println("done:" + c.getQuestion().getName());
                    break;
                }

            }
        }
        if (type.equals("mchoice")) {
            List<ModuleExamCaseItemMultiChoice> chs = this.examCase.getMultiChoiceItems();
            for (ModuleExamCaseItemMultiChoice c : chs) {
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
            List<ModuleExamCaseItemFill> chs = this.examCase.getFillItems();
            for (ModuleExamCaseItemFill c : chs) {
                if (c.getId().equals(id)) {
                    List<ModuleExamCaseItemFillBlock> ls = c.getBlocks();
                    StringBuilder us = new StringBuilder();
                    boolean tt = false;
                    for (ModuleExamCaseItemFillBlock e : ls) {
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
            List<ModuleExamCaseItemJudge> chs = this.examCase.getJudgeItems();
            for (ModuleExamCaseItemJudge c : chs) {
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
            List<ModuleExamCaseItemEssay> chs = this.examCase.getEssayItems();
            for (ModuleExamCaseItemEssay c : chs) {
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
            List<ModuleExamCaseItemFile> chs = this.examCase.getFileItems();
            for (ModuleExamCaseItemFile c : chs) {
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

    public void submitModuleExamCase() {
        examCase.setStat("submitted");
        this.submited = true;
        String result = "ModuleExamCaseReportList?faces-redirect=true";
        examCase.setSubmitTime(new Date());
        examCase.setIfPub(true);

        ApplicationBean appBean = JsfHelper.getBean("applicationBean");
        //是否异步提交试卷

        this.examCase = this.examCaseService.computeExamCase(examCase);
        if (this.examCaseDAO.findModuleExamCase(examCase.getId()) == null) {
            this.examCaseDAO.addModuleExamCase(examCase);
        } else {
            this.examCaseDAO.updateModuleExamCase(examCase);
        }

        //this.examCaseService.computeModuleExamCase(examCase);
        //this.examCaseDAO.addModuleExamCase(examCase);
        //考试后给用户加积分
        BbsUser bu = cs.getUsr();
        //bu.setScore(bu.getScore() + examCase.getBbsScore());
        IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
        bsl.log("考试取得成绩自动积分", (int) examCase.getBbsScore());
        //this.userDAO.updateBbsUser(bu);

        if (true) {
            //return "ListChoiceQuestion?faces-redirect=true";
            //return result;
        } else {
            //return result;
        }
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

    public String reserveSession() {
        MyLogger.echo("reserveSession sucess.");
        return null;
    }

}
