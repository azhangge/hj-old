package com.hjedu.examination.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.persistence.*;
import org.eclipse.persistence.annotations.Noncacheable;
/**
 *习题模块
 *单选题
 */
@Entity
@Table(name = "choice_question")
@NamedQuery(name = "findChoiceQuestionByModule", query = "Select cq from ChoiceQuestion cq where cq.module.id=:id order by cq.genTime desc,cq.ord")
public class ChoiceQuestion extends GeneralQuestion implements Serializable {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "case_id")
    CaseQuestion caseQuestion;
    
    @Column(name = "answer")
    private String answer;
    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER, targetEntity = ExamChoice.class, cascade = {CascadeType.ALL})
    private List<ExamChoice> choices;


    @JsonIgnore
    @Column(name = "difficulty_degree")
    private double difficultyDegree = 0.5D;

    @JsonIgnore
    @Column(name = "right_num")
    private long rightNum = 0;

    @JsonIgnore
    @Column(name = "wrong_num")
    private long wrongNum = 0;

    @JsonIgnore
    @ManyToMany(mappedBy = "choices", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @Noncacheable
    private List<ExamKnowledge> knowledges;

    @JsonIgnore
    @ManyToMany(mappedBy = "choices", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @Noncacheable
    private List<ExamPaperManual> papers;

    @Column(name = "allow_choice_random")
    private boolean allowChoiceRandom = true;

    @JsonIgnore
    @Transient
    long wrongTimes;
    

    @JsonIgnore
    @Transient
    long choicesTotalSelectNum = 0;//所有选项的总选择次数,需要调用fetchChoicesTotalSelectNum

    public ChoiceQuestion() {
    }

    public void fetchChoicesTotalSelectNum() {
        long num = 0;
        for (ExamChoice ec : this.choices) {
            num += ec.getSelectNum();
        }
        choicesTotalSelectNum = num;
    }


    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<ExamChoice> getChoices() {
        if (choices != null) {
            Collections.sort(choices);
        }
        return choices;
    }

    public void setChoices(List<ExamChoice> choices) {
        this.choices = choices;
    }

    public List<ExamPaperManual> getPapers() {
        return papers;
    }

    public void setPapers(List<ExamPaperManual> papers) {
        this.papers = papers;
    }

    public List<ExamKnowledge> getKnowledges() {
        return knowledges;
    }

    public void setKnowledges(List<ExamKnowledge> knowledges) {
        this.knowledges = knowledges;
    }


    public boolean isAllowChoiceRandom() {
        return allowChoiceRandom;
    }

    public void setAllowChoiceRandom(boolean allowChoiceRandom) {
        this.allowChoiceRandom = allowChoiceRandom;
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

    public long getRightNum() {
        return rightNum;
    }

    public void setRightNum(long rightNum) {
        this.rightNum = rightNum;
    }

    public long getWrongNum() {
        return wrongNum;
    }

    public void setWrongNum(long wrongNum) {
        this.wrongNum = wrongNum;
    }

    public CaseQuestion getCaseQuestion() {
        return caseQuestion;
    }

    public void setCaseQuestion(CaseQuestion caseQuestion) {
        this.caseQuestion = caseQuestion;
    }

    public long getChoicesTotalSelectNum() {
        return choicesTotalSelectNum;
    }

    public void setChoicesTotalSelectNum(long choicesTotalSelectNum) {
        this.choicesTotalSelectNum = choicesTotalSelectNum;
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
        if (!(object instanceof ChoiceQuestion)) {
            return false;
        }
        ChoiceQuestion other = (ChoiceQuestion) object;
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
