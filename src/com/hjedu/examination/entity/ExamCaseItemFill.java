package com.hjedu.examination.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.hjedu.examination.service.IExamCaseService;
import com.huajie.util.SpringHelper;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;
/**
 * 考试模块
 * ？
 * @author h j
 *
 */
@Entity
@Table(name = "exam_case_item_fill")
public class ExamCaseItemFill extends GeneralExamCaseItem implements Serializable, Comparable {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "case_item_id")
    private ExamCaseItemCase caseItem;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "question_id")
    private FillQuestion question;
    @Column(name = "user_answer_str")

    private String userAnswerStr;
    @Column(name = "right_answer_str")

    private String rightAnswerStr;
    @Column(name = "right_num")

    private int rightNum;
    @Transient

    private int totalNum = 0;
    @Transient

    private List<ExamCaseItemFillBlock> blocks = null;
    @Transient

    private String lastStr;

    public ExamCaseItemFill() {
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

    public ExamCaseItemCase getCaseItem() {
        return caseItem;
    }

    public void setCaseItem(ExamCaseItemCase caseItem) {
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

    public List<ExamCaseItemFillBlock> getBlocks() {
        if (this.blocks == null) {
            IExamCaseService ecs = SpringHelper.getSpringBean("ExamCaseService");
            ecs.buildItemFillBlocks(this);
        }
        return blocks;
    }

    public void setBlocks(List<ExamCaseItemFillBlock> blocks) {
        this.blocks = blocks;
    }

    public String getLastStr() {
        return lastStr;
    }

    public void setLastStr(String lastStr) {
        this.lastStr = lastStr;
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
        ExamCaseItemFill cq = (ExamCaseItemFill) o;
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
        if (!(object instanceof ExamCaseItemFill)) {
            return false;
        }
        ExamCaseItemFill other = (ExamCaseItemFill) object;
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
