package com.hjedu.examination.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.eclipse.persistence.annotations.Noncacheable;

/**
 * 考试模块
 * ？
 * @author h j
 *
 */
@Entity
@Table(name = "exam_knowledge")
public class ExamKnowledge implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();
    @Column(name = "title")
    private String title;
    @Lob
    @Column(name = "content")
    private String content;
    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genTime=new Date();
    @JoinColumn(name = "module_id")
    private ExamModuleModel module;
    
    @Lob
    @Column(name = "label_str")
    private String labelStr = "";//标签字符串
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "knowledge_question_choice",
    joinColumns = {
        @JoinColumn(name = "knowledge_id", referencedColumnName = "id")},
    inverseJoinColumns = {
        @JoinColumn(name = "question_id", referencedColumnName = "id")})
    private List<ChoiceQuestion> choices;
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "knowledge_question_m_choice",
    joinColumns = {
        @JoinColumn(name = "knowledge_id", referencedColumnName = "id")},
    inverseJoinColumns = {
        @JoinColumn(name = "question_id", referencedColumnName = "id")})
    private List<MultiChoiceQuestion> multiChoices;
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "knowledge_question_fill",
    joinColumns = {
        @JoinColumn(name = "knowledge_id", referencedColumnName = "id")},
    inverseJoinColumns = {
        @JoinColumn(name = "question_id", referencedColumnName = "id")})
    private List<FillQuestion> fills;
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "knowledge_question_judge",
    joinColumns = {
        @JoinColumn(name = "knowledge_id", referencedColumnName = "id")},
    inverseJoinColumns = {
        @JoinColumn(name = "question_id", referencedColumnName = "id")})
    private List<JudgeQuestion> judges;
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "knowledge_question_essay",
    joinColumns = {
        @JoinColumn(name = "knowledge_id", referencedColumnName = "id")},
    inverseJoinColumns = {
        @JoinColumn(name = "question_id", referencedColumnName = "id")})
    private List<EssayQuestion> essaies;
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "knowledge_question_file",
    joinColumns = {
        @JoinColumn(name = "knowledge_id", referencedColumnName = "id")},
    inverseJoinColumns = {
        @JoinColumn(name = "question_id", referencedColumnName = "id")})
    private List<FileQuestion> files;
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "knowledge_question_case",
    joinColumns = {
        @JoinColumn(name = "knowledge_id", referencedColumnName = "id")},
    inverseJoinColumns = {
        @JoinColumn(name = "question_id", referencedColumnName = "id")})
    private List<CaseQuestion> cases;
    
    @Transient
    boolean selected=false;

    public ExamKnowledge() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabelStr() {
        return labelStr;
    }

    public void setLabelStr(String labelStr) {
        this.labelStr = labelStr;
    }

    


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public ExamModuleModel getModule() {
        return module;
    }

    public void setModule(ExamModuleModel module) {
        this.module = module;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public List<ChoiceQuestion> getChoices() {
        return choices;
    }

    public void setChoices(List<ChoiceQuestion> choices) {
        this.choices = choices;
    }

    public List<MultiChoiceQuestion> getMultiChoices() {
        return multiChoices;
    }

    public void setMultiChoices(List<MultiChoiceQuestion> multiChoices) {
        this.multiChoices = multiChoices;
    }

    public List<FillQuestion> getFills() {
        return fills;
    }

    public void setFills(List<FillQuestion> fills) {
        this.fills = fills;
    }

    public List<JudgeQuestion> getJudges() {
        return judges;
    }

    public void setJudges(List<JudgeQuestion> judges) {
        this.judges = judges;
    }

    public List<EssayQuestion> getEssaies() {
        return essaies;
    }

    public void setEssaies(List<EssayQuestion> essaies) {
        this.essaies = essaies;
    }

    public List<FileQuestion> getFiles() {
        return files;
    }

    public void setFiles(List<FileQuestion> files) {
        this.files = files;
    }

    public List<CaseQuestion> getCases() {
        return cases;
    }

    public void setCases(List<CaseQuestion> cases) {
        this.cases = cases;
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
        if (!(object instanceof ExamKnowledge)) {
            return false;
        }
        ExamKnowledge other = (ExamKnowledge) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.exam.model.ExamKnowledge[ id=" + id + " ]";
    }
    
}
