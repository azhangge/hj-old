/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hjedu.examination.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.ICaseQuestionDAO;
import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IEssayQuestionDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.dao.IFillQuestionDAO;
import com.hjedu.examination.dao.IJudgeQuestionDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.huajie.util.SpringHelper;
/**
 * 习题模块
 * 用户对习题的评论
 * @author h j
 *
 */
@Entity
@Table(name = "question_comment")
public class QuestionComment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();
    @Lob
    @Column(name = "word1")
    private String word1;
    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genTime = new Date();
    @Column(name = "question_id")
    private String questionId;
    @Column(name = "question_type")
    private String questionType;
    @Column(name = "comment_type")
    private String commentType = "comment";
    @ManyToOne
    @JoinColumn(name = "user_id")
    private BbsUser user;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "rate1")
    private int rate1 = 5;
    @Transient
    private GeneralQuestion question;
    @Transient
    private boolean selected=false;

    public QuestionComment() {
    }

    public QuestionComment(String id) {
        this.id = id;
    }

    public QuestionComment(String id, Date genTime) {
        this.id = id;
        this.genTime = genTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord1() {
        return word1;
    }

    public void setWord1(String word) {
        this.word1 = word;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public BbsUser getUser() {
        return user;
    }

    public void setUser(BbsUser user) {
        this.user = user;
    }

    public int getRate1() {
        return rate1;
    }

    public void setRate1(int rate) {
        this.rate1 = rate;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public GeneralQuestion getQuestion() {
        if ("choice".equals(this.questionType)) {
            IChoiceQuestionDAO qDAO = SpringHelper.getSpringBean("ChoiceQuestionDAO");
            question = qDAO.findChoiceQuestion(this.questionId);
        } else if ("mchoice".equals(this.questionType)) {
            IMultiChoiceQuestionDAO qDAO = SpringHelper.getSpringBean("MultiChoiceQuestionDAO");
            question = qDAO.findMultiChoiceQuestion(this.questionId);
        }else if ("fill".equals(this.questionType)) {
            IFillQuestionDAO qDAO = SpringHelper.getSpringBean("FillQuestionDAO");
            question = qDAO.findFillQuestion(this.questionId);
        }
        else if ("judge".equals(this.questionType)) {
            IJudgeQuestionDAO qDAO = SpringHelper.getSpringBean("JudgeQuestionDAO");
            question = qDAO.findJudgeQuestion(this.questionId);
        }
        else if ("essay".equals(this.questionType)) {
            IEssayQuestionDAO qDAO = SpringHelper.getSpringBean("EssayQuestionDAO");
            question = qDAO.findEssayQuestion(this.questionId);
        }
        else if ("file".equals(this.questionType)) {
            IFileQuestionDAO qDAO = SpringHelper.getSpringBean("FileQuestionDAO");
            question = qDAO.findFileQuestion(this.questionId);
        }
        else if ("case".equals(this.questionType)) {
            ICaseQuestionDAO qDAO = SpringHelper.getSpringBean("CaseQuestionDAO");
            question = qDAO.findCaseQuestion(this.questionId);
        }
        
        return question;
    }

    public void setQuestion(GeneralQuestion question) {
        this.question = question;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QuestionComment)) {
            return false;
        }
        QuestionComment other = (QuestionComment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.exam.model.QuestionComment[ id=" + id + " ]";
    }

}
