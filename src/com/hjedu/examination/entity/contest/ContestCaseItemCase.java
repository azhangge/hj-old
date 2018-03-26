package com.hjedu.examination.entity.contest;

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
import javax.persistence.Transient;

import com.hjedu.examination.dao.IBuffetExaminationPartDAO;
import com.hjedu.examination.entity.CaseQuestion;
import com.hjedu.examination.entity.buffet.BuffetExaminationPart;
import com.huajie.util.SpringHelper;
/**
 * 竞赛模块
 * 竞赛实例
 * @author h j
 *
 */
@Entity
@Table(name = "contest_case_item_case")
public class ContestCaseItemCase extends GeneralContestCaseItem implements Serializable, Comparable {

    @ManyToOne
    @JoinColumn(name = "question_id")
    private CaseQuestion question;

    @OneToMany(mappedBy = "caseItem", targetEntity = ContestCaseItemChoice.class, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ContestCaseItemChoice> choiceItems;
    @OneToMany(mappedBy = "caseItem", targetEntity = ContestCaseItemFill.class, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ContestCaseItemFill> fillItems;
    @OneToMany(mappedBy = "caseItem", targetEntity = ContestCaseItemMultiChoice.class, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ContestCaseItemMultiChoice> multiChoiceItems;
    @OneToMany(mappedBy = "caseItem", targetEntity = ContestCaseItemJudge.class, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ContestCaseItemJudge> judgeItems;
    @OneToMany(mappedBy = "caseItem", targetEntity = ContestCaseItemEssay.class, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ContestCaseItemEssay> essayItems;
    
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

    @Transient
    private boolean marked = false;

    public ContestCaseItemCase() {
    }

    public CaseQuestion getQuestion() {
        return question;
    }

    public void setQuestion(CaseQuestion question) {
        this.question = question;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
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

    public List<ContestCaseItemChoice> getChoiceItems() {
        return choiceItems;
    }

    public void setChoiceItems(List<ContestCaseItemChoice> choiceItems) {
        this.choiceItems = choiceItems;
    }

    public List<ContestCaseItemFill> getFillItems() {
        return fillItems;
    }

    public void setFillItems(List<ContestCaseItemFill> fillItems) {
        this.fillItems = fillItems;
    }

    public List<ContestCaseItemMultiChoice> getMultiChoiceItems() {
        return multiChoiceItems;
    }

    public void setMultiChoiceItems(List<ContestCaseItemMultiChoice> multiChoiceItems) {
        this.multiChoiceItems = multiChoiceItems;
    }

    public List<ContestCaseItemJudge> getJudgeItems() {
        return judgeItems;
    }

    public void setJudgeItems(List<ContestCaseItemJudge> judgeItems) {
        this.judgeItems = judgeItems;
    }

    public List<ContestCaseItemEssay> getEssayItems() {
        return essayItems;
    }

    public void setEssayItems(List<ContestCaseItemEssay> essayItems) {
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
        ContestCaseItemCase cq = (ContestCaseItemCase) o;
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
        if (!(object instanceof ContestCaseItemCase)) {
            return false;
        }
        ContestCaseItemCase other = (ContestCaseItemCase) object;
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
