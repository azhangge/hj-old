package com.hjedu.examination.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IEssayQuestionDAO;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.dao.IFillQuestionDAO;
import com.hjedu.examination.dao.IJudgeQuestionDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.dao.IWrongQuestionDAO;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.entity.WrongQuestionWrapper;
import com.hjedu.examination.entity.WrongTestInfo;
import com.hjedu.platform.controller.ClientSession;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class WrongQuestionList implements Serializable {
    
    ClientSession cs = JsfHelper.getBean("clientSession");
    IWrongQuestionDAO wrongDAO = SpringHelper.getSpringBean("WrongQuestionDAO");
    IChoiceQuestionDAO choiceQuestionDAO = SpringHelper.getSpringBean("ChoiceQuestionDAO");
    IMultiChoiceQuestionDAO multiChoiceQuestionDAO = SpringHelper.getSpringBean("MultiChoiceQuestionDAO");
    IFillQuestionDAO fillQuestionDAO = SpringHelper.getSpringBean("FillQuestionDAO");
    IJudgeQuestionDAO judgeQuestionDAO = SpringHelper.getSpringBean("JudgeQuestionDAO");
    IEssayQuestionDAO essayQuestionDAO = SpringHelper.getSpringBean("EssayQuestionDAO");
    IFileQuestionDAO fileQuestionDAO = SpringHelper.getSpringBean("FileQuestionDAO");
    IExaminationDAO examDAO=SpringHelper.getSpringBean("ExaminationDAO");
    WrongQuestionWrapper wqWrapper;
    WrongTestInfo wrongTestInfo = new WrongTestInfo();
    Examination exam;
    
    public WrongQuestionWrapper getWqWrapper() {
        return wqWrapper;
    }
    
    public void setWqWrapper(WrongQuestionWrapper wqWrapper) {
        this.wqWrapper = wqWrapper;
    }

    public Examination getExam() {
        return exam;
    }

    public void setExam(Examination exam) {
        this.exam = exam;
    }
    
    public WrongTestInfo getWrongTestInfo() {
        return wrongTestInfo;
    }
    
    public void setWrongTestInfo(WrongTestInfo wrongTestInfo) {
        this.wrongTestInfo = wrongTestInfo;
    }
    
    @PostConstruct
    public void init() {
        this.exam=this.examDAO.findExamination("7");
        this.synDB();
    }
    
    private void synDB() {
        BbsUser bu = cs.getUsr();
        if (bu != null) {
            wqWrapper = new WrongQuestionWrapper(bu);
        }
    }
    
    public String delQuestion(String id) {
        this.wrongDAO.deleteWrongQuestionByQuestion(id);
        this.synDB();
        return null;
    }
    
    public String begainTest() {
        HttpSession session = JsfHelper.getRequest().getSession();
        //this.wrongTestInfo.setWqWrapper(wqWrapper);
        session.setAttribute("wrongTestInfo", this.wrongTestInfo);
        return null;
    }
    
    public String deleteAllByUserAndType(String userId, String type) {
        this.wrongDAO.deleteWrongQuestionByUsrAndType(userId, type);
        return null;
    }
}
