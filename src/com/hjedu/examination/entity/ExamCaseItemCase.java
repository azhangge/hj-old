package com.hjedu.examination.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
/**
 * 考试模块
 * ？
 * @author h j
 *
 */
@Entity
@Table(name = "exam_case_item_case")
public class ExamCaseItemCase extends GeneralExamCaseItem implements Serializable, Comparable {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "question_id")
    private CaseQuestion question;

    @OneToMany(mappedBy = "caseItem", targetEntity = ExamCaseItemChoice.class, fetch = FetchType.EAGER, cascade = {CascadeType.ALL})

    
    private List<ExamCaseItemChoice> choiceItems;
    @OneToMany(mappedBy = "caseItem", targetEntity = ExamCaseItemFill.class, fetch = FetchType.EAGER, cascade = {CascadeType.ALL})

    private List<ExamCaseItemFill> fillItems;
    @OneToMany(mappedBy = "caseItem", targetEntity = ExamCaseItemMultiChoice.class, fetch = FetchType.EAGER, cascade = {CascadeType.ALL})

    private List<ExamCaseItemMultiChoice> multiChoiceItems;
    @OneToMany(mappedBy = "caseItem", targetEntity = ExamCaseItemJudge.class, fetch = FetchType.EAGER, cascade = {CascadeType.ALL})

    private List<ExamCaseItemJudge> judgeItems;
    @OneToMany(mappedBy = "caseItem", targetEntity = ExamCaseItemEssay.class, fetch = FetchType.EAGER, cascade = {CascadeType.ALL})

    private List<ExamCaseItemEssay> essayItems;

    @Column(name = "choice_score")
    private double choiceScore;
    @Column(name = "multi_choice_score")

    private double multiChoiceScore;
    @Column(name = "fill_score")

    private double fillScore;
    @Column(name = "judge_score")

    private double judgeScore;
    @Column(name = "essay_score")

    private double essayScore;
    @Column(name = "remark")

    private String remark = "";

    public ExamCaseItemCase() {
    }

    public CaseQuestion getQuestion() {
        return question;
    }

    public void setQuestion(CaseQuestion question) {
        this.question = question;
    }

    public double getChoiceScore() {
        return choiceScore;
    }

    public void setChoiceScore(double choiceScore) {
        this.choiceScore = choiceScore;
    }

    public double getEssayScore() {
        return essayScore;
    }

    public void setEssayScore(double essayScore) {
        this.essayScore = essayScore;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<ExamCaseItemChoice> getChoiceItems() {
        return choiceItems;
    }

    public void setChoiceItems(List<ExamCaseItemChoice> choiceItems) {
        this.choiceItems = choiceItems;
    }

    public List<ExamCaseItemFill> getFillItems() {
        return fillItems;
    }

    public void setFillItems(List<ExamCaseItemFill> fillItems) {
        this.fillItems = fillItems;
    }

    public List<ExamCaseItemMultiChoice> getMultiChoiceItems() {
        return multiChoiceItems;
    }

    public void setMultiChoiceItems(List<ExamCaseItemMultiChoice> multiChoiceItems) {
        this.multiChoiceItems = multiChoiceItems;
    }

    public List<ExamCaseItemJudge> getJudgeItems() {
        return judgeItems;
    }

    public void setJudgeItems(List<ExamCaseItemJudge> judgeItems) {
        this.judgeItems = judgeItems;
    }

    public List<ExamCaseItemEssay> getEssayItems() {
        return essayItems;
    }

    public void setEssayItems(List<ExamCaseItemEssay> essayItems) {
        this.essayItems = essayItems;
    }

    public double getMultiChoiceScore() {
        return multiChoiceScore;
    }

    public void setMultiChoiceScore(double multiChoiceScore) {
        this.multiChoiceScore = multiChoiceScore;
    }

    public double getFillScore() {
        return fillScore;
    }

    public void setFillScore(double fillScore) {
        this.fillScore = fillScore;
    }

    public double getJudgeScore() {
        return judgeScore;
    }

    public void setJudgeScore(double judgeScore) {
        this.judgeScore = judgeScore;
    }

    @Override
    public int compareTo(Object o) {
        ExamCaseItemCase cq = (ExamCaseItemCase) o;
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
        if (!(object instanceof ExamCaseItemCase)) {
            return false;
        }
        ExamCaseItemCase other = (ExamCaseItemCase) object;
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
