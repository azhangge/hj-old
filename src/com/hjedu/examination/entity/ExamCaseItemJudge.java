package com.hjedu.examination.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
/**
 * 考试模块
 * ？
 * @author h j
 *
 */
@Entity
@Table(name = "exam_case_item_judge")
public class ExamCaseItemJudge extends GeneralExamCaseItem implements Serializable , Comparable {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "case_item_id")
    private ExamCaseItemCase caseItem;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "question_id")
    private JudgeQuestion question;
    
 

    public ExamCaseItemJudge() {
    }


    public JudgeQuestion getQuestion() {
        return question;
    }

    public void setQuestion(JudgeQuestion question) {
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
        ExamCaseItemJudge cq = (ExamCaseItemJudge) o;
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
        if (!(object instanceof ExamCaseItemJudge)) {
            return false;
        }
        ExamCaseItemJudge other = (ExamCaseItemJudge) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.exam.model.ExamCaseItemJudge[ id=" + id + " ]";
    }
}
