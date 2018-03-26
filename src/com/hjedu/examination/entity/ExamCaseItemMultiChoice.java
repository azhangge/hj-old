package com.hjedu.examination.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
/**
 * 考试模块
 * ？
 * @author h j
 *
 */
@Entity
@Table(name = "exam_case_item_multi_choice")
public class ExamCaseItemMultiChoice extends GeneralExamCaseItem implements Serializable , Comparable {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "case_item_id")
    private ExamCaseItemCase caseItem;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "question_id")
    private MultiChoiceQuestion question;

    
    @Transient
    private List<String> selectedLabels=new ArrayList();


    public ExamCaseItemMultiChoice() {
    }

    public MultiChoiceQuestion getQuestion() {
        return question;
    }

    public void setQuestion(MultiChoiceQuestion question) {
        this.question = question;
    }

    public ExamCaseItemCase getCaseItem() {
        return caseItem;
    }

    public void setCaseItem(ExamCaseItemCase caseItem) {
        this.caseItem = caseItem;
    }

    public List<String> getSelectedLabels() {
        return selectedLabels;
    }

    public void setSelectedLabels(List<String> selectedLabels) {
        this.selectedLabels = selectedLabels;
    }

    

    @Override
    public int compareTo(Object o) {
        ExamCaseItemMultiChoice cq = (ExamCaseItemMultiChoice) o;
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
        if (!(object instanceof ExamCaseItemMultiChoice)) {
            return false;
        }
        ExamCaseItemMultiChoice other = (ExamCaseItemMultiChoice) object;
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
