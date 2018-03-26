
package com.hjedu.examination.dao;

import java.util.List;

import com.hjedu.examination.entity.ExamComment;

public interface IExamCommentDAO {

    public void updateExamComment(ExamComment model);

    public void addExamComment(ExamComment model);

    public void deleteAll();

    public void deleteExamComment(String id);

    public List<ExamComment> findAllExamComment();

    public List<ExamComment> findExamCommentByUsr(final String uid);

    public ExamComment findExamComment(String id);

    public List<ExamComment> findExamCommentByExamination(String qid);

    public void deleteCommentByExamination(String qid);
    
    public long getCommentNumByExamination(String qid);
    
}
