package com.hjedu.examination.entity.contest;

import com.google.gson.annotations.Expose;
import com.hjedu.examination.dao.IRandom2PaperPartDAO;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.buffet.BuffetExaminationPart;
import com.hjedu.examination.entity.random2.Random2PaperPart;
import com.huajie.util.SpringHelper;

import java.io.Serializable;
import javax.persistence.*;
/**
 * 竞赛模块
 * 竞赛单选题记录
 * @author h j
 *
 */
@Entity
@Table(name = "contest_case_item_choice")
public class ContestCaseItemChoice extends GeneralContestCaseItem implements Serializable, Comparable {

    @ManyToOne
    @JoinColumn(name = "question_id")
    private ChoiceQuestion question;
    
    @ManyToOne
    @JoinColumn(name = "case_item_id")
    private ContestCaseItemCase caseItem;

    @Column(name = "user_answer")
    @Expose
    private String userAnswer;
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
    

    public ContestCaseItemChoice() {
    }

    public ChoiceQuestion getQuestion() {
        return question;
    }

    public void setQuestion(ChoiceQuestion question) {
        this.question = question;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
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

    public ContestCaseItemCase getCaseItem() {
        return caseItem;
    }

    public void setCaseItem(ContestCaseItemCase caseItem) {
        this.caseItem = caseItem;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    

    @Override
    public int compareTo(Object o) {
        ContestCaseItemChoice cq = (ContestCaseItemChoice) o;
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
        if (!(object instanceof ContestCaseItemChoice)) {
            return false;
        }
        ContestCaseItemChoice other = (ContestCaseItemChoice) object;
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
