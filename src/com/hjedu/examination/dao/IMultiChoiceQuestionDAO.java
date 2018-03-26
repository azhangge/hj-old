package com.hjedu.examination.dao;

import java.util.List;
import java.util.Map;

import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.MultiChoiceQuestion;

public interface IMultiChoiceQuestionDAO {

    public abstract void addMultiChoiceQuestion(MultiChoiceQuestion m);

    public abstract void updateMultiChoiceQuestion(MultiChoiceQuestion m);

    public abstract void deleteMultiChoiceQuestion(String id);

    public abstract void deleteMultiChoiceQuestionByModule(String moduleId);

    public abstract MultiChoiceQuestion findMultiChoiceQuestion(String id);

    public abstract MultiChoiceQuestion findMultiChoiceQuestionByName(String id);
    
    public abstract MultiChoiceQuestion findMultiChoiceQuestionByHashCode(String id);

    public abstract List<MultiChoiceQuestion> findAllMultiChoiceQuestion();

    public abstract List<MultiChoiceQuestion> findMultiChoiceQuestionByModule(String moduleId);

    public long getQuestionNumByModule(String id);

    public List<MultiChoiceQuestion> findMultiChoiceQuestionByModule(String id, int offSet, int num);

    public List<MultiChoiceQuestion> findMultiChoiceQuestionByModuleAndLike(String id, Map<String,Object> fms);

    public List<MultiChoiceQuestion> findOrderedMultiChoiceQuestionByModule(String id, int offSet, int num, String field, String type);

    public List<MultiChoiceQuestion> findQuestionByLike(String str,String businessId);

    public List<MultiChoiceQuestion> getRandomQuestionOfNumInModule(long num, String mid);

    public MultiChoiceQuestion findQuestionByIndex(int index, String mid);
    
    public List<MultiChoiceQuestion> findWrongQuestion() ;

	List<MultiChoiceQuestion> findMultiChoiceQuestionByModuleIds(List<String> ids);

}
