package com.hjedu.examination.entity;

import java.io.Serializable;
import java.util.Collections;
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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.Noncacheable;

import com.hjedu.customer.entity.AdminInfo;

/**
 *试卷模块
 *人工试卷
 */
@Entity
@Table(name = "exam_paper_manual")
public class ExamPaperManual implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genTime=new Date();
    @Noncacheable
    @OneToMany(cascade = {CascadeType.ALL},fetch = FetchType.LAZY,mappedBy = "paper")
    private List<ExamPaperManualPart> parts;
    @Column(name = "business_id")
    private String businessId;
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "exam_admin_ExamPaper",
            joinColumns = {
                @JoinColumn(name = "examPaper_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "admin_id", referencedColumnName = "id")})
    private List<AdminInfo> admins;
    
    private double totalScore;
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "paper_question_choice",
    joinColumns = {
        @JoinColumn(name = "paper_id", referencedColumnName = "id")},
    inverseJoinColumns = {
        @JoinColumn(name = "question_id", referencedColumnName = "id")})
    private List<ChoiceQuestion> choices;
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "paper_question_m_choice",
    joinColumns = {
        @JoinColumn(name = "paper_id", referencedColumnName = "id")},
    inverseJoinColumns = {
        @JoinColumn(name = "question_id", referencedColumnName = "id")})
    private List<MultiChoiceQuestion> multiChoices;
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "paper_question_fill",
    joinColumns = {
        @JoinColumn(name = "paper_id", referencedColumnName = "id")},
    inverseJoinColumns = {
        @JoinColumn(name = "question_id", referencedColumnName = "id")})
    private List<FillQuestion> fills;
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "paper_question_judge",
    joinColumns = {
        @JoinColumn(name = "paper_id", referencedColumnName = "id")},
    inverseJoinColumns = {
        @JoinColumn(name = "question_id", referencedColumnName = "id")})
    private List<JudgeQuestion> judges;
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "paper_question_essay",
    joinColumns = {
        @JoinColumn(name = "paper_id", referencedColumnName = "id")},
    inverseJoinColumns = {
        @JoinColumn(name = "question_id", referencedColumnName = "id")})
    private List<EssayQuestion> essaies;
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "paper_question_file",
    joinColumns = {
        @JoinColumn(name = "paper_id", referencedColumnName = "id")},
    inverseJoinColumns = {
        @JoinColumn(name = "question_id", referencedColumnName = "id")})
    private List<FileQuestion> files;
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "paper_question_case",
    joinColumns = {
        @JoinColumn(name = "paper_id", referencedColumnName = "id")},
    inverseJoinColumns = {
        @JoinColumn(name = "question_id", referencedColumnName = "id")})
    private List<CaseQuestion> cases;
    
    @Column(name = "ord")
    private int ord=0;
    
    
    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public ExamPaperManual() {
    }

    public ExamPaperManual(String id) {
        this.id = id;
    }

    public ExamPaperManual(String id, Date genTime) {
        this.id = id;
        this.genTime = genTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public List<ExamPaperManualPart> getParts() {
        Collections.sort(parts);
        return parts;
    }

    public void setParts(List<ExamPaperManualPart> parts) {
        this.parts = parts;
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

    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }
    
    public double getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(double totalScore) {
		this.totalScore = totalScore;
	}

	public List<AdminInfo> getAdmins() {
		return admins;
	}

	public void setAdmins(List<AdminInfo> admins) {
		this.admins = admins;
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
        if (!(object instanceof ExamPaperManual)) {
            return false;
        }
        ExamPaperManual other = (ExamPaperManual) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.exam.model.ExamPaperManual[ id=" + id + " ]";
    }
    
}
