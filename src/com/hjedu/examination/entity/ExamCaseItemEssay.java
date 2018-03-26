package com.hjedu.examination.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
/**
 * 考试模块
 * ？
 * @author h j
 *
 */
@Entity
@Table(name = "exam_case_item_essay")
public class ExamCaseItemEssay extends GeneralExamCaseItem implements  Comparable {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "question_id")
    private EssayQuestion question;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "case_item_id")
    private ExamCaseItemCase caseItem;
    
    @Column(name = "right_ratio")
    private float rightRatio=0f;
    
    public ExamCaseItemEssay() {
    }


    public ExamCaseItemCase getCaseItem() {
        return caseItem;
    }

    public void setCaseItem(ExamCaseItemCase caseItem) {
        this.caseItem = caseItem;
    }

    public EssayQuestion getQuestion() {
        return question;
    }

    public void setQuestion(EssayQuestion question) {
        this.question = question;
    }

    public float getRightRatio() {
        //System.out.println("ratio:"+rightRatio);
        long rt=(long)(rightRatio*1000L);
        //System.out.println("now:"+rt);
        rightRatio=((float)rt)/1000.0F;
        //System.out.println("after:"+rightRatio);
        return rightRatio;
    }

    public void setRightRatio(float rightRatio) {
        this.rightRatio = rightRatio;
    }

    @Override
    public int compareTo(Object o) {
        ExamCaseItemEssay cq = (ExamCaseItemEssay) o;
        if (this.getQuestion() == null || cq.getQuestion() == null) {
            return 1;
        }
        return this.getQuestion().compareTo(cq.getQuestion());
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
        if (!(object instanceof ExamCaseItemEssay)) {
            return false;
        }
        ExamCaseItemEssay other = (ExamCaseItemEssay) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.exam.model.ExamCaseItemEssay[ id=" + id + " ]";
    }
}
