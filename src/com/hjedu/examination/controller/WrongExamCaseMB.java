package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.servlet.http.HttpSession;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.ExamCaseItemChoice;
import com.hjedu.examination.entity.ExamCaseItemFile;
import com.hjedu.examination.entity.ExamChoice;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.entity.WrongQuestionWrapper2;
import com.hjedu.examination.entity.WrongTestInfo;
import com.hjedu.examination.service.IExamCaseCacheService;
import com.hjedu.examination.service.IWrongExamCaseService;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class WrongExamCaseMB implements Serializable {
    
    IExaminationDAO examinationDAO = SpringHelper.getSpringBean("ExaminationDAO");
    IWrongExamCaseService examCaseService = SpringHelper.getSpringBean("WrongExamCaseService");
    IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
    IFileQuestionDAO fileQuestionDAO = SpringHelper.getSpringBean("FileQuestionDAO");
    IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
    //IUserSessionStateService iussService = SpringHelper.getSpringBean("UserSessionStateService");
    ClientSession cs = JsfHelper.getBean("clientSession");
    WrongTestInfo info;
    ExamCase examCase = new ExamCase();
    Date begainTime = new Date();
    long remainingTime = 0;
    
    public Date getBegainTime() {
        return begainTime;
    }
    
    public void setBegainTime(Date begainTime) {
        this.begainTime = begainTime;
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
    
    @PostConstruct
    public void init() {
        HttpSession session = JsfHelper.getRequest().getSession();
        this.info = (WrongTestInfo) session.getAttribute("wrongTestInfo");
        
        Examination examination = examinationDAO.findExamination("7");
        examination.setTimeLen((int) info.getTestMinute());
        info.setExamination(examination);
        WrongQuestionWrapper2 wqWrapper = new WrongQuestionWrapper2(cs.getUsr());
        info.setWqWrapper(wqWrapper);
        
        this.examCase.setExamination(examination);
        this.examCase.setName(info.getName());
        this.examCase.setUser(this.cs.getUsr());
        this.examCaseService.buildWrongTestCase(info, examCase);
        MyLogger.echo("ExamCase initialized.");
        this.computeRemainingTime();
        this.examCase.setIp(JsfHelper.getRequest().getRemoteAddr());
        //iussService.loginExam(examCase);

        //考试前扣用户积分
        BbsUser bu = cs.getUsr();
        if (bu != null) {
            IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
            bsl.log("错题练习消耗积分", (int) (-1 * examCase.getExamination().getScorePaid()));
            this.userDAO.updateBbsUser(bu);
        }
        
    }
    
    public void computeRemainingTime() {
        long n = System.currentTimeMillis();
        long o = this.begainTime.getTime();
        long len = this.info.getTestMinute();
        int past = (int) (n - o) / (1000 * 60);
        this.remainingTime = len - past;
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
        String result = "ExamCaseReportList?faces-redirect=true";
        this.examCaseService.computeWrongTestCase(info, examCase);
        examCase.setSubmitTime(new Date());
        examCase.setIfPub(examCase.getExamination().isIfDirect());
        this.examCaseDAO.addExamCase(examCase);
        IExamCaseCacheService cacheService = SpringHelper.getSpringBean("ExamCaseCacheService");
        cacheService.addExamCase(examCase);
        //考试后给用户加积分
        BbsUser bu = cs.getUsr();
        //bu.setScore(bu.getScore() + examCase.getBbsScore());
        IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
        bsl.log("错题练习取得成绩自动积分", (int) examCase.getBbsScore());
        
        return result;
    }
    
    @PreDestroy
    public void destroy11() {
        MyLogger.echo("ExamCase destroyed.");
        //iussService.logoutExam();
    }
}
