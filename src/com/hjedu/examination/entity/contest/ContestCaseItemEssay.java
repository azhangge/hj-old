package com.hjedu.examination.entity.contest;

import com.google.gson.annotations.Expose;
import com.hjedu.examination.entity.EssayQuestion;

import javax.persistence.*;
/**
 * 竞赛模块
 * 竞赛简单题记录
 * @author h j
 *
 */
@Entity
@Table(name = "contest_case_item_essay")
public class ContestCaseItemEssay extends GeneralContestCaseItem implements Comparable {

    @ManyToOne
    @JoinColumn(name = "question_id")
    private EssayQuestion question;
    
    @ManyToOne
    @JoinColumn(name = "case_item_id")
    private ContestCaseItemCase caseItem;
    
    @Lob
    @Column(name = "user_answer")
    @Expose
    private String userAnswer = "";
    
    @Column(name = "right_ratio")
    @Expose
    private float rightRatio = 0f;
    
    @Lob
    @Column(name = "right_answer")
    @Expose
    private String rightAnswer = "";
    
    @Column(name = "marked")
    @Expose
    private boolean marked = false;
    
    @Column(name = "score")
    @Expose
    private double score = 5;
    
    
    

    public ContestCaseItemEssay() {
    }

    public ContestCaseItemCase getCaseItem() {
        return caseItem;
    }

    public void setCaseItem(ContestCaseItemCase caseItem) {
        this.caseItem = caseItem;
    }

    public EssayQuestion getQuestion() {
        return question;
    }

    public void setQuestion(EssayQuestion question) {
        this.question = question;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public float getRightRatio() {
        //System.out.println("ratio:"+rightRatio);
        long rt = (long) (rightRatio * 1000L);
        //System.out.println("now:"+rt);
        rightRatio = ((float) rt) / 1000.0F;
        //System.out.println("after:"+rightRatio);
        return rightRatio;
    }

    public void setRightRatio(float rightRatio) {
        this.rightRatio = rightRatio;
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
        ContestCaseItemEssay cq = (ContestCaseItemEssay) o;
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
        if (!(object instanceof ContestCaseItemEssay)) {
            return false;
        }
        ContestCaseItemEssay other = (ContestCaseItemEssay) object;
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
