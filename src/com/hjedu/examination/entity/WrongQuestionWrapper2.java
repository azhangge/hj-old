package com.hjedu.examination.entity;

import com.google.gson.annotations.Expose;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IWrongQuestionDAO;
import com.huajie.util.SpringHelper;

import java.io.Serializable;
import java.util.List;

public class WrongQuestionWrapper2 implements Serializable {

    private IWrongQuestionDAO wrongDAO = SpringHelper.getSpringBean("WrongQuestionDAO");
    private BbsUser user;
    @Expose
    private List<ChoiceQuestion> choiceQuestions;
    @Expose
    private List<MultiChoiceQuestion> multiChoiceQuestions;
    @Expose
    private List<FillQuestion> fillQuestions;
    @Expose
    private List<JudgeQuestion> judgeQuestions;
    @Expose
    private List<EssayQuestion> essayQuestions;
    @Expose
    private List<FileQuestion> fileQuestions;
    private int max = 999999;

    public WrongQuestionWrapper2(BbsUser u) {
        this.user = u;
        choiceQuestions = wrongDAO.findWrongChoiceQuestionByUsr(user.getId(), 0, max);
        multiChoiceQuestions = wrongDAO.findWrongMultiChoiceQuestionByUsr(user.getId(), 0, max);
        fillQuestions = wrongDAO.findWrongFillQuestionByUsr(user.getId(), 0, max);
        judgeQuestions = wrongDAO.findWrongJudgeQuestionByUsr(user.getId(), 0, max);
        essayQuestions = wrongDAO.findWrongEssayQuestionByUsr(user.getId(), 0, max);
        fileQuestions = wrongDAO.findWrongFileQuestionByUsr(user.getId(), 0, max);
    }

    public WrongQuestionWrapper2(BbsUser u, String qtype, int num) {
        this.user = u;
        this.max = num;
        if ("choice".equals(qtype)) {
            choiceQuestions = wrongDAO.findWrongChoiceQuestionByUsr(user.getId(), 0, max);
        }
        if ("mchoice".equals(qtype)) {
            multiChoiceQuestions = wrongDAO.findWrongMultiChoiceQuestionByUsr(user.getId(), 0, max);
        }
        if ("fill".equals(qtype)) {
            fillQuestions = wrongDAO.findWrongFillQuestionByUsr(user.getId(), 0, max);
        }
        if ("judge".equals(qtype)) {
            judgeQuestions = wrongDAO.findWrongJudgeQuestionByUsr(user.getId(), 0, max);
        }
        if ("essay".equals(qtype)) {
            essayQuestions = wrongDAO.findWrongEssayQuestionByUsr(user.getId(), 0, max);
        }
        if ("file".equals(qtype)) {
            fileQuestions = wrongDAO.findWrongFileQuestionByUsr(user.getId(), 0, max);
        }
    }

    public BbsUser getUser() {
        return user;
    }

    public void setUser(BbsUser user) {
        this.user = user;
    }

    public List<ChoiceQuestion> getChoiceQuestions() {
        return choiceQuestions;
    }

    public void setChoiceQuestions(List<ChoiceQuestion> choiceQuestions) {
        this.choiceQuestions = choiceQuestions;
    }

    public List<MultiChoiceQuestion> getMultiChoiceQuestions() {
        return multiChoiceQuestions;
    }

    public void setMultiChoiceQuestions(List<MultiChoiceQuestion> multiChoiceQuestions) {
        this.multiChoiceQuestions = multiChoiceQuestions;
    }

    public List<FillQuestion> getFillQuestions() {
        return fillQuestions;
    }

    public void setFillQuestions(List<FillQuestion> fillQuestions) {
        this.fillQuestions = fillQuestions;
    }

    public List<JudgeQuestion> getJudgeQuestions() {
        return judgeQuestions;
    }

    public void setJudgeQuestions(List<JudgeQuestion> judgeQuestions) {
        this.judgeQuestions = judgeQuestions;
    }

    public List<EssayQuestion> getEssayQuestions() {
        return essayQuestions;
    }

    public void setEssayQuestions(List<EssayQuestion> essayQuestions) {
        this.essayQuestions = essayQuestions;
    }

    public List<FileQuestion> getFileQuestions() {
        return fileQuestions;
    }

    public void setFileQuestions(List<FileQuestion> fileQuestions) {
        this.fileQuestions = fileQuestions;
    }
}
