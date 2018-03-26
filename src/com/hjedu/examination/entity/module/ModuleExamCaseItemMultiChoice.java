package com.hjedu.examination.entity.module;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;

import com.google.gson.annotations.Expose;
import com.hjedu.examination.entity.MultiChoiceQuestion;
/**
 * 章节练习模块
 * 章节练习多选题记录
 * @author h j
 *
 */
@Entity
@Table(name = "m_exam_case_item_multi_choice")
public class ModuleExamCaseItemMultiChoice  extends ModuleGeneralExamCaseItem implements Serializable , Comparable {

    private static final long serialVersionUID = 1L;
   
    @ManyToOne
    @JoinColumn(name = "case_item_id")
    private ModuleExamCaseItemCase caseItem;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private MultiChoiceQuestion question;
    @Column(name = "user_answer")
    private String userAnswer;
    @Column(name = "right_answer")
    private String rightAnswer;
    @Column(name = "marked")
    private boolean marked = false;
    @Basic(optional = false)
    @Column(name = "updateTime")
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    private Date updateTime = new Date();
    
    public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Transient
    private List<String> selectedLabels=new ArrayList();

    public ModuleExamCaseItemMultiChoice() {
    }

    public MultiChoiceQuestion getQuestion() {
        return question;
    }

    public void setQuestion(MultiChoiceQuestion question) {
        this.question = question;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public ModuleExamCaseItemCase getCaseItem() {
        return caseItem;
    }

    public void setCaseItem(ModuleExamCaseItemCase caseItem) {
        this.caseItem = caseItem;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public List<String> getSelectedLabels() {
        return selectedLabels;
    }

    public void setSelectedLabels(List<String> selectedLabels) {
        this.selectedLabels = selectedLabels;
    }

    @Override
    public int compareTo(Object o) {
        ModuleExamCaseItemMultiChoice cq = (ModuleExamCaseItemMultiChoice) o;
        if (this.getQuestion() == null || cq.getQuestion() == null) {
            return 1;
        }
        return this.getQuestion().getId().compareTo(cq.getQuestion().getId());
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
        if (!(object instanceof ModuleExamCaseItemMultiChoice)) {
            return false;
        }
        ModuleExamCaseItemMultiChoice other = (ModuleExamCaseItemMultiChoice) object;
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
