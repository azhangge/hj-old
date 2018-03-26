package com.hjedu.examination.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hjedu.examination.entity.ChoiceQuestion;

public interface IChoiceQuestionDAO {

    public abstract void addChoiceQuestion(ChoiceQuestion m);

    public abstract void updateChoiceQuestion(ChoiceQuestion m);

    public abstract void deleteChoiceQuestion(String id);

    public abstract void deleteChoiceQuestionByModule(String moduleId);

    public abstract ChoiceQuestion findChoiceQuestion(String id);

    public abstract ChoiceQuestion findChoiceQuestionByName(String id);
    
    public abstract ChoiceQuestion findChoiceQuestionByHashCode(String code);

    public abstract List<ChoiceQuestion> findAllChoiceQuestion();

    public abstract List<ChoiceQuestion> findChoiceQuestionByModule(String moduleId);

    public List<ChoiceQuestion> findChoiceQuestionByModule(String id, int offSet, int num);

    public List<ChoiceQuestion> findChoiceQuestionByModuleAndLike(String id, Map<String,Object> fms);

    public List<ChoiceQuestion> findQuestionByLike(String str,String businessId);

    public List<ChoiceQuestion> findOrderedChoiceQuestionByModule(String id, int offSet, int num, String field, String type);

    public long getQuestionNumByModule(String id);

    public List<ChoiceQuestion> getRandomQuestionOfNumInModule(long num, String mid);

    public List<ChoiceQuestion> getRandomQuestionOfNumInModule(long num, String mid, List<ChoiceQuestion> cqs2);

    public ChoiceQuestion findQuestionByIndex(int index, String mid);
    
    public List<ChoiceQuestion> findQuestionsByIndexSet(Set<Long> set, String mid);
    
    public List<ChoiceQuestion> findWrongQuestion() ;

	List<ChoiceQuestion> findChoiceQuestionByModuleIds(List<String> ids);

    //public String getQuestionTotalNum() ;
}
