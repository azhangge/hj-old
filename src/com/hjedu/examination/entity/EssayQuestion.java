package com.hjedu.examination.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemEssay;
import com.hjedu.examination.entity.contest.ContestCaseItemEssay;
import com.hjedu.examination.entity.module.ModuleExamCaseItemEssay;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import org.eclipse.persistence.annotations.Noncacheable;
/**
 * 习题模块
 * 简单题
 * @author h j
 *
 */
@Entity
@Table(name = "essay_question")
public class EssayQuestion extends GeneralQuestion implements Serializable {

    @Lob
    @Column(name = "answer")
    private String answer="";
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "case_id")
    CaseQuestion caseQuestion;
    
    @JsonIgnore
    @Column(name="difficulty_degree")
    private double difficultyDegree=0.5D;
    
    @JsonIgnore
    @ManyToMany(mappedBy = "essaies", fetch = FetchType.EAGER)
    @Noncacheable
    private List<ExamKnowledge> knowledges;
    
    @JsonIgnore
    @ManyToMany(mappedBy = "essaies", fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    @Noncacheable
    private List<ExamPaperManual> papers;
    
    @JsonIgnore
    @Transient
    long wrongTimes;
    

    public EssayQuestion() {
    }



    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public CaseQuestion getCaseQuestion() {
        return caseQuestion;
    }

    public void setCaseQuestion(CaseQuestion caseQuestion) {
        this.caseQuestion = caseQuestion;
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
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EssayQuestion)) {
            return false;
        }
        EssayQuestion other = (EssayQuestion) object;
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
