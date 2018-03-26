package com.hjedu.examination.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.eclipse.persistence.annotations.Noncacheable;

import com.hjedu.examination.entity.buffet.BuffetExamCaseItemCase;
import com.hjedu.examination.entity.contest.ContestCaseItemCase;
import com.hjedu.examination.entity.module.ModuleExamCaseItemCase;
import com.huajie.util.HTMLCleaner;
/**
 * 案例题，暂时未用到
 * @author h j
 *
 */
@Entity
@Table(name = "case_question")
public class CaseQuestion extends GeneralQuestion implements Serializable{

    @Column(name = "nick_name")
    private String nickName;
    @Lob
    @Column(name = "content")
    private String content;
    
    @Basic(optional = false)
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "difficulty_degree")
    private Double difficultyDegree;
    
    @OneToMany(mappedBy = "caseQuestion", targetEntity = ChoiceQuestion.class, fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @Noncacheable
    private List<ChoiceQuestion> choices;
    @OneToMany(mappedBy = "caseQuestion", targetEntity = MultiChoiceQuestion.class, fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @Noncacheable
    private List<MultiChoiceQuestion> multiChoices;
    @OneToMany(mappedBy = "caseQuestion", targetEntity = FillQuestion.class, fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @Noncacheable
    private List<FillQuestion> fills;
    @OneToMany(mappedBy = "caseQuestion", targetEntity = JudgeQuestion.class, fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @Noncacheable
    private List<JudgeQuestion> judges;
    @OneToMany(mappedBy = "caseQuestion", targetEntity = EssayQuestion.class, fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @Noncacheable
    private List<EssayQuestion> essaies;
    
    
    @Column(name = "choice_score")
    private double choiceScore = 1D;
    @Column(name = "multi_choice_score")
    private double multiChoiceScore = 1D;
    @Column(name = "fill_score")
    private double fillScore = 1D;
    @Column(name = "judge_score")
    private double judgeScore = 1D;
    @Column(name = "essay_score")
    private double essayScore = 10D;
    @ManyToMany(mappedBy = "cases", fetch = FetchType.EAGER)
    @Noncacheable
    private List<ExamKnowledge> knowledges;
    @ManyToMany(mappedBy = "cases", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @Noncacheable
    private List<ExamPaperManual> papers;
    
    
    @Column(name = "audio_url")
    private String audioUrl;
    
    @Column(name = "inner_name")
    private String innerName;
    @Transient
    String cleanContent;
    @Transient
    double totalScore;
    @Transient
    double totalItemNum;

    public CaseQuestion() {
    }


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public List<ExamKnowledge> getKnowledges() {
        return knowledges;
    }

    public void setKnowledges(List<ExamKnowledge> knowledges) {
        this.knowledges = knowledges;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Double getDifficultyDegree() {
        return difficultyDegree;
    }

    public void setDifficultyDegree(Double difficultyDegree) {
        this.difficultyDegree = difficultyDegree;
    }

    public double getChoiceScore() {
        return choiceScore;
    }

    public void setChoiceScore(double choiceScore) {
        this.choiceScore = choiceScore;
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

    public double getEssayScore() {
        return essayScore;
    }

    public void setEssayScore(double essayScore) {
        this.essayScore = essayScore;
    }

    public String getCleanContent() {
        cleanContent = HTMLCleaner.delHTMLTag(content);
        return cleanContent;
    }

    public void setCleanContent(String cleanContent) {
        this.cleanContent = cleanContent;
    }

    public List<ChoiceQuestion> getChoices() {
        if (choices != null) {
            //Collections.sort(choices);
        }
        return choices;
    }

    public void setChoices(List<ChoiceQuestion> choices) {
        this.choices = choices;
    }

    public List<MultiChoiceQuestion> getMultiChoices() {
        if (multiChoices != null) {
            //Collections.sort(multiChoices);
        }
        return multiChoices;
    }

    public void setMultiChoices(List<MultiChoiceQuestion> multiChoices) {
        this.multiChoices = multiChoices;
    }

    public List<FillQuestion> getFills() {
        if (fills != null) {
            //Collections.sort(fills);
        }
        return fills;
    }

    public void setFills(List<FillQuestion> fills) {
        this.fills = fills;
    }

    public List<JudgeQuestion> getJudges() {
        if (judges != null) {
            //Collections.sort(judges);
        }
        return judges;
    }

    public void setJudges(List<JudgeQuestion> judges) {
        this.judges = judges;
    }

    public List<EssayQuestion> getEssaies() {
        if (essaies != null) {
            //Collections.sort(essaies);
        }
        return essaies;
    }

    public void setEssaies(List<EssayQuestion> essaies) {
        this.essaies = essaies;
    }

    public double getTotalScore() {
        double temp = 0D;
        if (this.choices != null) {
            temp += this.choiceScore * this.choices.size();
        }
        if (this.multiChoices != null) {
            temp += this.multiChoiceScore * this.multiChoices.size();
        }
        if (this.fills != null) {
            temp += this.fillScore * this.fills.size();
        }
        if (this.judges != null) {
            temp += this.judgeScore * this.judges.size();
        }
        if (this.essaies != null) {
            temp += this.essayScore * this.essaies.size();
        }
        totalScore = temp;
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public double getTotalItemNum() {
        int temp = 0;
        if (this.choices != null) {
            temp += this.choices.size();
        }
        if (this.multiChoices != null) {
            temp += this.multiChoices.size();
        }
        if (this.fills != null) {
            temp += this.fills.size();
        }
        if (this.judges != null) {
            temp += this.judges.size();
        }
        if (this.essaies != null) {
            temp += this.essaies.size();
        }
        totalItemNum = temp;
        return totalItemNum;
    }

    public void setTotalItemNum(double totalItemNum) {
        this.totalItemNum = totalItemNum;
    }

    public List<ExamPaperManual> getPapers() {
        return papers;
    }

    public void setPapers(List<ExamPaperManual> papers) {
        this.papers = papers;
    }

    public String getInnerName() {
        return innerName;
    }

    public void setInnerName(String innerName) {
        this.innerName = innerName;
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
        if (!(object instanceof CaseQuestion)) {
            return false;
        }
        CaseQuestion other = (CaseQuestion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    
    @Override
    public String toString() {
        return "com.huajie.exam.model.CaseQuestion[ id=" + id + " ]";
    }
}
