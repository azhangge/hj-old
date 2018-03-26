package com.hjedu.examination.entity.contest;

import com.google.gson.annotations.Expose;
import com.hjedu.examination.dao.IBuffetExaminationPartDAO;
import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.examination.entity.buffet.BuffetExaminationPart;
import com.hjedu.examination.service.impl.ContestCaseService;
import com.huajie.util.SpringHelper;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
/**
 * 竞赛模块
 * 竞赛填空题记录
 * @author h j
 *
 */
@Entity
@Table(name = "contest_case_item_fill")
public class ContestCaseItemFill extends GeneralContestCaseItem implements Serializable, Comparable {

    @ManyToOne
    @JoinColumn(name = "case_item_id")
    private ContestCaseItemCase caseItem;
    
    @ManyToOne
    @JoinColumn(name = "question_id")
    private FillQuestion question;
    
    @Column(name = "user_answer_str")
    @Expose
    private String userAnswerStr;
    
    @Column(name = "right_answer_str")
    @Expose
    private String rightAnswerStr;
    
    @Column(name = "right_num")
    @Expose
    private int rightNum;
    
    @Transient
    @Expose
    private int totalNum = 0;
    
    @Transient
    @Expose
    private List<ContestCaseItemFillBlock> blocks = null;
    
    @Transient
    @Expose
    private String lastStr;
    
    @Column(name = "marked")
    @Expose
    private boolean marked = false;
    
    @Column(name = "score")
    @Expose
    private double score = 1;

    public ContestCaseItemFill() {
    }

    public FillQuestion getQuestion() {
        return question;
    }

    public void setQuestion(FillQuestion question) {
        this.question = question;
    }

    public String getRightAnswerStr() {
        return rightAnswerStr;
    }

    public void setRightAnswerStr(String rightAnswerStr) {
        this.rightAnswerStr = rightAnswerStr;
    }

    public ContestCaseItemCase getCaseItem() {
        return caseItem;
    }

    public void setCaseItem(ContestCaseItemCase caseItem) {
        this.caseItem = caseItem;
    }

    public int getRightNum() {
        return rightNum;
    }

    public void setRightNum(int rightNum) {
        this.rightNum = rightNum;
    }

    public String getUserAnswerStr() {
        return userAnswerStr;
    }

    public void setUserAnswerStr(String userAnswerStr) {
        this.userAnswerStr = userAnswerStr;
    }

    public List<ContestCaseItemFillBlock> getBlocks() {
        if (this.blocks == null) {
            ContestCaseService ecs = SpringHelper.getSpringBean("ContestCaseService");
            ecs.buildItemFillBlocks(this);
        }
        return blocks;
    }

    public void setBlocks(List<ContestCaseItemFillBlock> blocks) {
        this.blocks = blocks;
    }

    public String getLastStr() {
        return lastStr;
    }

    public void setLastStr(String lastStr) {
        this.lastStr = lastStr;
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

    public int getTotalNum() {
        if (this.blocks != null) {
            totalNum = this.blocks.size();
        }
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    @Override
    public int compareTo(Object o) {
        ContestCaseItemFill cq = (ContestCaseItemFill) o;
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
        if (!(object instanceof ContestCaseItemFill)) {
            return false;
        }
        ContestCaseItemFill other = (ContestCaseItemFill) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.exam.model.ExamCaseItemFill[ id=" + id + " ]";
    }
}
