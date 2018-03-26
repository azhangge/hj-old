package com.hjedu.examination.dao;

import java.util.List;

import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.EssayQuestion;
import com.hjedu.examination.entity.FileQuestion;
import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.hjedu.examination.entity.WrongQuestion;

public interface IWrongQuestionDAO {

    public abstract void addWrongQuestion(WrongQuestion m);

    public abstract void updateWrongQuestion(WrongQuestion m);

    public abstract void deleteWrongQuestion(String id);

    public abstract void deleteWrongQuestionByQuestion(String id);

    public void deleteWrongQuestionByUsr(String id);

    public void deleteWrongQuestionByUsrAndType(String id, String type);

    public void wrongTimesPlusOne(String id, String userId, String type);

    public void recordWrong(String id, String userId, String type);

    public WrongQuestion findWrongQuestionByQuestionAndUsr(String kid, String userId);

    public abstract WrongQuestion findWrongQuestion(String id);

    public abstract List<WrongQuestion> findAllWrongQuestion();

    public abstract List<WrongQuestion> findWrongQuestionByUsr(String userId, int first, int pageSize);

    public abstract List<ChoiceQuestion> findWrongChoiceQuestionByUsr(String userId, int first, int pageSize);

    public abstract List<MultiChoiceQuestion> findWrongMultiChoiceQuestionByUsr(String userId, int first, int pageSize);

    public abstract List<FillQuestion> findWrongFillQuestionByUsr(String userId, int first, int pageSize);

    public abstract List<JudgeQuestion> findWrongJudgeQuestionByUsr(String userId, int first, int pageSize);

    public abstract List<EssayQuestion> findWrongEssayQuestionByUsr(String userId, int first, int pageSize);

    public abstract List<FileQuestion> findWrongFileQuestionByUsr(String userId, int first, int pageSize);

    public long getQuestionNumByTypeAndUsr(String type, String userId);
}
