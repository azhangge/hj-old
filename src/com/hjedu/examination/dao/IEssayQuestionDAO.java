package com.hjedu.examination.dao;

import java.util.List;
import java.util.Map;

import com.hjedu.examination.entity.EssayQuestion;

public interface IEssayQuestionDAO {

    public abstract void addEssayQuestion(EssayQuestion m);

    public abstract void updateEssayQuestion(EssayQuestion m);

    public abstract void deleteEssayQuestion(String id);

    public abstract void deleteEssayQuestionByModule(String moduleId);

    public abstract EssayQuestion findEssayQuestion(String id);

    public abstract EssayQuestion findEssayQuestionByName(String id);
    
    public abstract EssayQuestion findEssayQuestionByHashCode(String code);

    public abstract List<EssayQuestion> findAllEssayQuestion();

    public abstract List<EssayQuestion> findEssayQuestionByModule(String moduleId);

    public List<EssayQuestion> findEssayQuestionByModule(String id, int offSet, int num);

    public List<EssayQuestion> findEssayQuestionByModuleAndLike(String id, Map<String, Object> fms);

    public List<EssayQuestion> findOrderedEssayQuestionByModule(String id, int offSet, int num, String field, String type);

    public List<EssayQuestion> findQuestionByLike(String str,String businessId);

    public long getQuestionNumByModule(String id);

    public List<EssayQuestion> getRandomQuestionOfNumInModule(long num, String mid);

    public EssayQuestion findQuestionByIndex(int index, String mid);

}
