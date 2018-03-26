package com.hjedu.examination.dao;

import java.util.List;

import com.hjedu.examination.entity.CaseQuestion;

public interface ICaseQuestionDAO {

    public abstract void addCaseQuestion(CaseQuestion m);

    public abstract void updateCaseQuestion(CaseQuestion m);

    public abstract void deleteCaseQuestion(String id);

    public abstract void deleteCaseQuestionByModule(String moduleId);

    public abstract CaseQuestion findCaseQuestion(String id);

    public abstract CaseQuestion findCaseQuestionByName(String id);

    public abstract List<CaseQuestion> findAllCaseQuestion();

    public abstract List<CaseQuestion> findCaseQuestionByModule(String moduleId);

    public long getQuestionNumByModule(String id);

    public List<CaseQuestion> getRandomQuestionOfNumInModule(long num, String mid);

    public List<CaseQuestion> findQuestionByLike(String str,String businessId);

    public CaseQuestion findQuestionByIndex(int index, String mid);

}
