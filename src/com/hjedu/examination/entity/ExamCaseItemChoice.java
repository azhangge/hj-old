package com.hjedu.examination.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.*;
/**
 * 考试模块
 * ？
 * @author h j
 *
 */
@Entity
@Table(name = "exam_case_item_choice")
public class ExamCaseItemChoice extends GeneralExamCaseItem implements Serializable , Comparable {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "question_id")
    private ChoiceQuestion question;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "case_item_id")
    private ExamCaseItemCase caseItem;
    

    public ExamCaseItemChoice() {
    }


    public ChoiceQuestion getQuestion() {
        return question;
    }

    public void setQuestion(ChoiceQuestion question) {
        this.question = question;
    }


    public ExamCaseItemCase getCaseItem() {
        return caseItem;
    }

    public void setCaseItem(ExamCaseItemCase caseItem) {
        this.caseItem = caseItem;
    }

    @Override
    public int compareTo(Object o) {
        ExamCaseItemChoice cq = (ExamCaseItemChoice) o;
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
        if (!(object instanceof ExamCaseItemChoice)) {
            return false;
        }
        ExamCaseItemChoice other = (ExamCaseItemChoice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.exam.model.ExamCaseItemChoice[ id=" + id + " ]";
    }
}
