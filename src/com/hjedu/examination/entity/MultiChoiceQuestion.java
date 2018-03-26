package com.hjedu.examination.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemMultiChoice;
import com.hjedu.examination.entity.contest.ContestCaseItemMultiChoice;
import com.hjedu.examination.entity.module.ModuleExamCaseItemMultiChoice;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.persistence.*;
import org.eclipse.persistence.annotations.Noncacheable;
/**
 * 习题模块
 * 多选题
 * @author h j
 *
 */
@Entity
@Table(name = "multi_choice_question")
public class MultiChoiceQuestion extends GeneralQuestion implements Serializable {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "case_id")
    CaseQuestion caseQuestion;

    @Column(name = "answer")
    private String answer;
    
    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    List<ExamMultiChoice> choices;
    
    @JsonIgnore
    @Column(name = "difficulty_degree")
    private double difficultyDegree = 0.5D;
    
    @JsonIgnore
    @ManyToMany(mappedBy = "multiChoices", fetch = FetchType.EAGER)
    @Noncacheable
    private List<ExamKnowledge> knowledges;
    
    @JsonIgnore
    @ManyToMany(mappedBy = "multiChoices", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @Noncacheable
    private List<ExamPaperManual> papers;

    @Column(name = "allow_choice_random")
    
    private boolean allowChoiceRandom = true;
    @JsonIgnore
    @Transient
    long wrongTimes;

    public MultiChoiceQuestion() {
    }


    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<ExamMultiChoice> getChoices() {
        Collections.sort(choices);
        return choices;
    }

    public void setChoices(List<ExamMultiChoice> choices) {
        this.choices = choices;
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

    public boolean isAllowChoiceRandom() {
        return allowChoiceRandom;
    }

    public void setAllowChoiceRandom(boolean allowChoiceRandom) {
        this.allowChoiceRandom = allowChoiceRandom;
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
        if (!(object instanceof MultiChoiceQuestion)) {
            return false;
        }
        MultiChoiceQuestion other = (MultiChoiceQuestion) object;
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
