package com.hjedu.examination.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import org.eclipse.persistence.annotations.Noncacheable;
/**
 * 习题模块
 * 文件题，暂时未用
 * @author h j
 *
 */
@Entity
@Table(name = "file_question")
public class FileQuestion extends GeneralQuestion implements Serializable{

    @Lob
    @Column(name = "answer")
    private String answer="";
    
    
    @JsonIgnore
    @Column(name="difficulty_degree")
    private double difficultyDegree=0.5D;
    
    @JsonIgnore
    @ManyToMany(mappedBy = "files", fetch = FetchType.EAGER)
    @Noncacheable
    private List<ExamKnowledge> knowledges;
    
    @JsonIgnore
    @ManyToMany(mappedBy = "files", fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    @Noncacheable
    private List<ExamPaperManual> papers;
    
    @JsonIgnore
    @Transient
    long wrongTimes;
    
    

    public FileQuestion() {
    }


    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    public double getDifficultyDegree() {
        return difficultyDegree;
    }

    public void setDifficultyDegree(double difficultyDegree) {
        this.difficultyDegree = difficultyDegree;
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

    public long getWrongTimes() {
        return wrongTimes;
    }

    public void setWrongTimes(long wrongTimes) {
        this.wrongTimes = wrongTimes;
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
        if (!(object instanceof FileQuestion)) {
            return false;
        }
        FileQuestion other = (FileQuestion) object;
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
