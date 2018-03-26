package com.hjedu.examination.dao;

import java.util.List;
import java.util.Map;

import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.FillQuestion;

public interface IFillQuestionDAO {

    public abstract void addFillQuestion(FillQuestion m);

    public abstract void updateFillQuestion(FillQuestion m);

    public abstract void deleteFillQuestion(String id);

    public abstract void deleteFillQuestionByModule(String moduleId);

    public abstract FillQuestion findFillQuestion(String id);

    public abstract FillQuestion findFillQuestionByName(String id);
    
     public abstract FillQuestion findFillQuestionByHashCode(String id);

    public abstract List<FillQuestion> findAllFillQuestion();

    public abstract List<FillQuestion> findFillQuestionByModule(String moduleId);

    public long getQuestionNumByModule(String id);

    public List<FillQuestion> findFillQuestionByModule(String id, int offSet, int num);

    public List<FillQuestion> findFillQuestionByModuleAndLike(String id, Map<String, Object> fms);

    public List<FillQuestion> findOrderedFillQuestionByModule(String id, int offSet, int num, String field, String type);

    public List<FillQuestion> findQuestionByLike(String str,String businessId);

    public List<FillQuestion> getRandomQuestionOfNumInModule(long num, String mid);

    public FillQuestion findQuestionByIndex(int index, String mid);

	List<FillQuestion> findFillQuestionByModuleIds(List<String> ids);

}
