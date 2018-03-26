package com.hjedu.examination.dao;

import java.util.List;

import com.hjedu.examination.entity.QuestionCollection;

public interface IQuestionCollectionDAO {

    public List<QuestionCollection> findQuestionCollectionByUser(String userId, String qtype);
    
    public List<QuestionCollection> findQuestionCollectionByUser(String userId);
    
    public QuestionCollection findQuestionCollection(String id);

    public QuestionCollection findQuestionCollectionByQandU(String qid,String uid);
    
    public long getQuestionCollectionNumByUsrAndQtype(String userId,String qtype);

    public void addQuestionCollection(QuestionCollection log);

    public List<QuestionCollection> findAllQuestionCollection();

    public void deleteQuestionCollection(String id);

    public void deleteAllQuestionCollectionByUser(String uid);
    
    public void deleteAllQuestionCollectionByUser(String uid,String qtype);
}
