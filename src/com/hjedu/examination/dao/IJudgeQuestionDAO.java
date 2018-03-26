package com.hjedu.examination.dao;

import java.util.List;
import java.util.Map;

import com.hjedu.examination.entity.JudgeQuestion;

public interface IJudgeQuestionDAO {

    public abstract void addJudgeQuestion(JudgeQuestion m);

    public abstract void updateJudgeQuestion(JudgeQuestion m);

    public abstract void deleteJudgeQuestion(String id);

    public abstract void deleteJudgeQuestionByModule(String moduleId);

    public abstract JudgeQuestion findJudgeQuestion(String id);

    public abstract JudgeQuestion findJudgeQuestionByName(String id);
    
    public abstract JudgeQuestion findJudgeQuestionByHashCode(String id);

    public abstract List<JudgeQuestion> findAllJudgeQuestion();

    public abstract List<JudgeQuestion> findJudgeQuestionByModule(String moduleId);

    public long getQuestionNumByModule(String id);

    public List<JudgeQuestion> findJudgeQuestionByModule(String id, int offSet, int num);

    public List<JudgeQuestion> findJudgeQuestionByModuleAndLike(String id, Map<String, Object> fms);

    public List<JudgeQuestion> findOrderedJudgeQuestionByModule(String id, int offSet, int num, String field, String type);

    public List<JudgeQuestion> findQuestionByLike(String str,String businessId);

    public List<JudgeQuestion> getRandomQuestionOfNumInModule(long num, String mid);

    public JudgeQuestion findQuestionByIndex(int index, String mid);
}
