package com.hjedu.examination.dao;

import java.util.List;

import com.hjedu.examination.entity.ExamKnowledge;

public interface IExamKnowledgeDAO {

    public abstract void addExamKnowledge(ExamKnowledge m);

    public abstract void updateExamKnowledge(ExamKnowledge m);

    public abstract void deleteExamKnowledge(String id);
    
    public abstract void deleteExamKnowledgeByModule(String id);

    public abstract ExamKnowledge findExamKnowledge(String id);

    public abstract List<ExamKnowledge> findAllExamKnowledge();
    
    public abstract List<ExamKnowledge> findExamKnowledgeByModule(String id);
    
    public abstract long getKnowledgeNumByModule(String id);

}
