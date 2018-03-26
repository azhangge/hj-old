package com.hjedu.examination.entity;

import java.io.Serializable;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IWrongQuestionDAO;
import com.hjedu.examination.entity.lazy.LazyWrongChoiceQuestionList;
import com.hjedu.examination.entity.lazy.LazyWrongEssayQuestionList;
import com.hjedu.examination.entity.lazy.LazyWrongFileQuestionList;
import com.hjedu.examination.entity.lazy.LazyWrongFillQuestionList;
import com.hjedu.examination.entity.lazy.LazyWrongJudgeQuestionList;
import com.hjedu.examination.entity.lazy.LazyWrongMultiChoiceQuestionList;
import com.huajie.util.SpringHelper;

public class WrongQuestionWrapper implements Serializable {

    private IWrongQuestionDAO wrongDAO = SpringHelper.getSpringBean("WrongQuestionDAO");
    private BbsUser user;
    private LazyWrongChoiceQuestionList choiceQuestions;
    private LazyWrongMultiChoiceQuestionList multiChoiceQuestions;
    private LazyWrongFillQuestionList fillQuestions;
    private LazyWrongJudgeQuestionList judgeQuestions;
    private LazyWrongEssayQuestionList essayQuestions;
    private LazyWrongFileQuestionList fileQuestions;

    public WrongQuestionWrapper(BbsUser u) {
        this.user = u;
        choiceQuestions = new LazyWrongChoiceQuestionList(user.getId());
        multiChoiceQuestions = new LazyWrongMultiChoiceQuestionList(user.getId());
        fillQuestions = new LazyWrongFillQuestionList(user.getId());
        judgeQuestions = new LazyWrongJudgeQuestionList(user.getId());
        essayQuestions = new LazyWrongEssayQuestionList(user.getId());
        fileQuestions = new LazyWrongFileQuestionList(user.getId());
    }

    public BbsUser getUser() {
        return user;
    }

    public void setUser(BbsUser user) {
        this.user = user;
    }

    public IWrongQuestionDAO getWrongDAO() {
        return wrongDAO;
    }

    public void setWrongDAO(IWrongQuestionDAO wrongDAO) {
        this.wrongDAO = wrongDAO;
    }

    public LazyWrongChoiceQuestionList getChoiceQuestions() {
        return choiceQuestions;
    }

    public void setChoiceQuestions(LazyWrongChoiceQuestionList choiceQuestions) {
        this.choiceQuestions = choiceQuestions;
    }

    public LazyWrongMultiChoiceQuestionList getMultiChoiceQuestions() {
        return multiChoiceQuestions;
    }

    public void setMultiChoiceQuestions(LazyWrongMultiChoiceQuestionList multiChoiceQuestions) {
        this.multiChoiceQuestions = multiChoiceQuestions;
    }

    public LazyWrongFillQuestionList getFillQuestions() {
        return fillQuestions;
    }

    public void setFillQuestions(LazyWrongFillQuestionList fillQuestions) {
        this.fillQuestions = fillQuestions;
    }

    public LazyWrongJudgeQuestionList getJudgeQuestions() {
        return judgeQuestions;
    }

    public void setJudgeQuestions(LazyWrongJudgeQuestionList judgeQuestions) {
        this.judgeQuestions = judgeQuestions;
    }

    public LazyWrongEssayQuestionList getEssayQuestions() {
        return essayQuestions;
    }

    public void setEssayQuestions(LazyWrongEssayQuestionList essayQuestions) {
        this.essayQuestions = essayQuestions;
    }

    public LazyWrongFileQuestionList getFileQuestions() {
        return fileQuestions;
    }

    public void setFileQuestions(LazyWrongFileQuestionList fileQuestions) {
        this.fileQuestions = fileQuestions;
    }
}
