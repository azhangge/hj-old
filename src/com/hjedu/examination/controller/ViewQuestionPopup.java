package com.hjedu.examination.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IEssayQuestionDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.dao.IFillQuestionDAO;
import com.hjedu.examination.dao.IJudgeQuestionDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.EssayQuestion;
import com.hjedu.examination.entity.FileQuestion;
import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ViewQuestionPopup implements Serializable {

    
    IChoiceQuestionDAO choiceQuestionDAO = SpringHelper.getSpringBean("ChoiceQuestionDAO");
    IMultiChoiceQuestionDAO multiChoiceQuestionDAO = SpringHelper.getSpringBean("MultiChoiceQuestionDAO");
    IFillQuestionDAO fillQuestionDAO = SpringHelper.getSpringBean("FillQuestionDAO");
    IJudgeQuestionDAO judgeQuestionDAO = SpringHelper.getSpringBean("JudgeQuestionDAO");
    IEssayQuestionDAO essayQuestionDAO = SpringHelper.getSpringBean("EssayQuestionDAO");
    IFileQuestionDAO fileQuestionDAO = SpringHelper.getSpringBean("FileQuestionDAO");
    
    ChoiceQuestion nowCq;
    MultiChoiceQuestion nowMcq;
    FillQuestion nowFq;
    JudgeQuestion nowJq;
    EssayQuestion nowEq;
    FileQuestion nowFileq;

    

    public MultiChoiceQuestion getNowMcq() {
        return nowMcq;
    }

    public void setNowMcq(MultiChoiceQuestion nowMcq) {
        this.nowMcq = nowMcq;
    }

    public FillQuestion getNowFq() {
        return nowFq;
    }

    public void setNowFq(FillQuestion nowFq) {
        this.nowFq = nowFq;
    }

    public JudgeQuestion getNowJq() {
        return nowJq;
    }

    public void setNowJq(JudgeQuestion nowJq) {
        this.nowJq = nowJq;
    }

    public EssayQuestion getNowEq() {
        return nowEq;
    }

    public void setNowEq(EssayQuestion nowEq) {
        this.nowEq = nowEq;
    }

    public FileQuestion getNowFileq() {
        return nowFileq;
    }

    public void setNowFileq(FileQuestion nowFileq) {
        this.nowFileq = nowFileq;
    }

    public ChoiceQuestion getNowCq() {
        return nowCq;
    }

    public void setNowCq(ChoiceQuestion nowCq) {
        this.nowCq = nowCq;
    }

    @PostConstruct
    public void init() {
    }



    public String selectChoice(String id) {
        this.nowCq = this.choiceQuestionDAO.findChoiceQuestion(id);
        return null;
    }

    public String selectMultiChoice(String id) {
        this.nowMcq = this.multiChoiceQuestionDAO.findMultiChoiceQuestion(id);
        return null;
    }

    public String selectFill(String id) {
        this.nowFq = this.fillQuestionDAO.findFillQuestion(id);
        return null;
    }

    public String selectJudge(String id) {
        this.nowJq = this.judgeQuestionDAO.findJudgeQuestion(id);
        return null;
    }

    public String selectEssay(String id) {
        this.nowEq = this.essayQuestionDAO.findEssayQuestion(id);
        return null;
    }

    public String selectFile(String id) {
        this.nowFileq = this.fileQuestionDAO.findFileQuestion(id);
        return null;
    }

    
}
