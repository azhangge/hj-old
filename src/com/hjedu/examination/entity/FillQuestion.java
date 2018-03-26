package com.hjedu.examination.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemFill;
import com.hjedu.examination.entity.contest.ContestCaseItemFill;
import com.hjedu.examination.entity.module.ModuleExamCaseItemFill;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import org.eclipse.persistence.annotations.Noncacheable;
/**
 * 习题模块
 * 填空题
 * @author h j
 *
 */
@Entity
@Table(name = "fill_question")
public class FillQuestion extends GeneralQuestion implements Serializable{

    @JsonIgnore
    @Column(name="difficulty_degree")
    private double difficultyDegree=0.5D;
    
    @JsonIgnore
    @ManyToMany(mappedBy = "fills", fetch = FetchType.EAGER)
    @Noncacheable
    private List<ExamKnowledge> knowledges;
    
    @JsonIgnore
    @ManyToMany(mappedBy = "fills", fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    @Noncacheable
    private List<ExamPaperManual> papers;
   
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "case_id")
    CaseQuestion caseQuestion;
    @JsonIgnore
    @Transient
    long wrongTimes;

    public FillQuestion() {
    }



    public List<ExamKnowledge> getKnowledges() {
        return knowledges;
    }

    public void setKnowledges(List<ExamKnowledge> knowledges) {
        this.knowledges = knowledges;
    }


    public List<ExamPaperManual> getPapers() {
        return papers;
    }

    public void setPapers(List<ExamPaperManual> papers) {
        this.papers = papers;
    }

    public double getDifficultyDegree() {
        return difficultyDegree;
    }

    public void setDifficultyDegree(double difficultyDegree) {
        this.difficultyDegree = difficultyDegree;
    }

    public long getWrongTimes() {
        return wrongTimes;
    }

    public void setWrongTimes(long wrongTimes) {
        this.wrongTimes = wrongTimes;
    }

    public CaseQuestion getCaseQuestion() {
        return caseQuestion;
    }

    public void setCaseQuestion(CaseQuestion caseQuestion) {
        this.caseQuestion = caseQuestion;
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
        if (!(object instanceof FillQuestion)) {
            return false;
        }
        FillQuestion other = (FillQuestion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.exam.model.ChoiceQuestion[ id=" + id + " ]";
    }
}
