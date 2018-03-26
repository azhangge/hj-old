package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.examination.dao.IQuestionCommentDAO;
import com.hjedu.examination.entity.QuestionComment;
import com.hjedu.platform.controller.ClientSession;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class QuestionCommentDetail implements Serializable {

    IQuestionCommentDAO commentDAO = SpringHelper.getSpringBean("QuestionCommentDAO");

    QuestionComment newComment = new QuestionComment();//新的评论内容
    List<QuestionComment> comments = new ArrayList();//正在查看的评论

    public QuestionComment getNewComment() {
        return newComment;
    }

    public void setNewComment(QuestionComment newComment) {
        this.newComment = newComment;
    }

    public List<QuestionComment> getComments() {
        return comments;
    }

    public void setComments(List<QuestionComment> comments) {
        this.comments = comments;
    }

    @PostConstruct
    public void init() {

    }

    public String viewComment(String qid) {
        comments = this.commentDAO.findQuestionCommentByQuestion(qid);
        return null;
    }

    public String begainComment(String qid, String qtype) {
        this.newComment.setQuestionType(qtype);
        this.newComment.setQuestionId(qid);
        return null;
    }

    public String submitComment() {
        ClientSession cs = JsfHelper.getBean("clientSession");
        newComment.setUser(cs.getUsr());
        this.commentDAO.addQuestionComment(newComment);
        this.newComment = new QuestionComment();
        return null;
    }

}
