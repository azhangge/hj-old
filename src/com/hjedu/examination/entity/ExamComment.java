
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
/**
 * 考试模块
 * ？
 * @author h j
 *
 */
@Entity
@Table(name = "exam_comment")
public class ExamComment implements Serializable {

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
    
    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Examination exam;
    
    @Column(name = "comment_type")
    private String commentType = "comment";
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private BbsUser user;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "rate1")
    private int rate1 = 5;
    
    @Transient
    private boolean selected=false;

    public ExamComment() {
    }

    public ExamComment(String id) {
        this.id = id;
    }

    public ExamComment(String id, Date genTime) {
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

    public Examination getExam() {
        return exam;
    }

    public void setExam(Examination exam) {
        this.exam = exam;
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
        if (!(object instanceof ExamComment)) {
            return false;
        }
        ExamComment other = (ExamComment) object;
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
