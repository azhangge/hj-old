package com.hjedu.examination.entity.module;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hjedu.examination.entity.CaseQuestion;

/**
 * 章节练习模块
 * 章节练习实例
 * @author h j
 *
 */
@Entity
@Table(name = "m_exam_case_item_case")
public class ModuleExamCaseItemCase  extends ModuleGeneralExamCaseItem implements Serializable , Comparable {

    private static final long serialVersionUID = 1L;
    
    @ManyToOne
    @JoinColumn(name = "question_id")
    private CaseQuestion question;
    
    @OneToMany(mappedBy = "caseItem", targetEntity=ModuleExamCaseItemChoice.class, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ModuleExamCaseItemChoice> choiceItems;
    @OneToMany(mappedBy = "caseItem", targetEntity=ModuleExamCaseItemFill.class, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ModuleExamCaseItemFill> fillItems;
    @OneToMany(mappedBy = "caseItem", targetEntity=ModuleExamCaseItemMultiChoice.class, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ModuleExamCaseItemMultiChoice> multiChoiceItems;
    @OneToMany(mappedBy = "caseItem", targetEntity=ModuleExamCaseItemJudge.class, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ModuleExamCaseItemJudge> judgeItems;
    @OneToMany(mappedBy = "caseItem", targetEntity=ModuleExamCaseItemEssay.class, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ModuleExamCaseItemEssay> essayItems;
    
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
    private String remark="";
    @Transient
    private boolean marked = false;
    

    
    
    
    public ModuleExamCaseItemCase() {
    }

    public ModuleExamCaseItemCase(String id) {
        this.id = id;
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

    public List<ModuleExamCaseItemChoice> getChoiceItems() {
        return choiceItems;
    }

    public void setChoiceItems(List<ModuleExamCaseItemChoice> choiceItems) {
        this.choiceItems = choiceItems;
    }

    public List<ModuleExamCaseItemFill> getFillItems() {
        return fillItems;
    }

    public void setFillItems(List<ModuleExamCaseItemFill> fillItems) {
        this.fillItems = fillItems;
    }

    public List<ModuleExamCaseItemMultiChoice> getMultiChoiceItems() {
        return multiChoiceItems;
    }

    public void setMultiChoiceItems(List<ModuleExamCaseItemMultiChoice> multiChoiceItems) {
        this.multiChoiceItems = multiChoiceItems;
    }

    public List<ModuleExamCaseItemJudge> getJudgeItems() {
        return judgeItems;
    }

    public void setJudgeItems(List<ModuleExamCaseItemJudge> judgeItems) {
        this.judgeItems = judgeItems;
    }

    public List<ModuleExamCaseItemEssay> getEssayItems() {
        return essayItems;
    }

    public void setEssayItems(List<ModuleExamCaseItemEssay> essayItems) {
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
        ModuleExamCaseItemCase cq = (ModuleExamCaseItemCase) o;
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
        if (!(object instanceof ModuleExamCaseItemCase)) {
            return false;
        }
        ModuleExamCaseItemCase other = (ModuleExamCaseItemCase) object;
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
