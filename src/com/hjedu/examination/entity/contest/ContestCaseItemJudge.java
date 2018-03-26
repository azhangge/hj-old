package com.hjedu.examination.entity.contest;

import com.google.gson.annotations.Expose;
import com.hjedu.examination.dao.IBuffetExaminationPartDAO;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.examination.entity.buffet.BuffetExaminationPart;
import com.huajie.util.SpringHelper;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
/**
 * 竞赛模块
 * 竞赛判断题记录
 * @author h j
 *
 */
@Entity
@Table(name = "contest_case_item_judge")
public class ContestCaseItemJudge extends GeneralContestCaseItem implements Serializable, Comparable {

    @ManyToOne
    @JoinColumn(name = "case_item_id")
    private ContestCaseItemCase caseItem;
    
    @ManyToOne
    @JoinColumn(name = "question_id")
    private JudgeQuestion question;
    
    @Column(name = "user_answer")
    @Expose
    private String userAnswer = "null";
    
    @Column(name = "if_right")
    @Expose
    private Boolean ifRight;
    
    @Column(name = "right_answer")
    @Expose
    private String rightAnswer;
    
    @Column(name = "marked")
    @Expose
    private boolean marked = false;
    
    @Column(name = "score")
    @Expose
    private double score = 1;

    public ContestCaseItemJudge() {
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

    public ContestCaseItemCase getCaseItem() {
        return caseItem;
    }

    public void setCaseItem(ContestCaseItemCase caseItem) {
        this.caseItem = caseItem;
    }

    public Boolean getIfRight() {
        return ifRight;
    }

    public void setIfRight(Boolean ifRight) {
        this.ifRight = ifRight;
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

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public int compareTo(Object o) {
        ContestCaseItemJudge cq = (ContestCaseItemJudge) o;
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
        if (!(object instanceof ContestCaseItemJudge)) {
            return false;
        }
        ContestCaseItemJudge other = (ContestCaseItemJudge) object;
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
