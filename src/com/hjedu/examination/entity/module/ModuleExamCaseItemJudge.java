package com.hjedu.examination.entity.module;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.annotations.Expose;
import com.hjedu.examination.entity.JudgeQuestion;
/**
 * 章节练习模块
 * 章节练习判断题记录
 * @author h j
 *
 */
@Entity
@Table(name = "m_exam_case_item_judge")
public class ModuleExamCaseItemJudge extends ModuleGeneralExamCaseItem implements Serializable , Comparable {

    private static final long serialVersionUID = 1L;
    
    @ManyToOne
    @JoinColumn(name = "case_item_id")
    private ModuleExamCaseItemCase caseItem;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private JudgeQuestion question;
    @Column(name = "user_answer")
    private String userAnswer = "";
    
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

	public ModuleExamCaseItemJudge() {
    }

    public JudgeQuestion getQuestion() {
        return question;
    }

    public void setQuestion(JudgeQuestion question) {
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

    @Override
    public int compareTo(Object o) {
        ModuleExamCaseItemJudge cq = (ModuleExamCaseItemJudge) o;
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
        if (!(object instanceof ModuleExamCaseItemJudge)) {
            return false;
        }
        ModuleExamCaseItemJudge other = (ModuleExamCaseItemJudge) object;
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
