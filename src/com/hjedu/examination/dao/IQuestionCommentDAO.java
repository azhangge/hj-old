/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hjedu.examination.dao;

import java.util.List;

import com.hjedu.examination.entity.QuestionComment;

public interface IQuestionCommentDAO {

    public void updateQuestionComment(QuestionComment model);

    public void addQuestionComment(QuestionComment model);

    public void deleteAll();

    public void deleteQuestionComment(String id);

    public List<QuestionComment> findAllQuestionComment();

    public List<QuestionComment> findQuestionCommentByUsr(final String uid);

    public QuestionComment findQuestionComment(String id);

    public List<QuestionComment> findQuestionCommentByQuestion(String qid);

    public void deleteCommentByQuestion(String qid);
    
    public long getCommentNumByQuestion(String qid);
    
}
