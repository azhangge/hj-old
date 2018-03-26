/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.Noncacheable;

import com.hjedu.customer.entity.AdminInfo;
import com.sun.star.installation.protocols;

/**
 *试卷模块
 *简单随机试卷
 */
@Entity
@Table(name = "exam_paper_random")
public class ExamPaperRandom implements Serializable {

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
    private Date genTime = new Date();
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "exam_admin_ExamPaper",
            joinColumns = {
                @JoinColumn(name = "examPaper_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "admin_id", referencedColumnName = "id")})
    private List<AdminInfo> admins;
    
    //0：正式模拟考试都可以；1：只能进行正式考试;2：只能进行模拟考试
    @Column(name = "ifSimulate")
    private String ifSimulate;
    

	public String getIfSimulate() {
		return ifSimulate;
	}

	public void setIfSimulate(String ifSimulate) {
		this.ifSimulate = ifSimulate;
	}

	public List<AdminInfo> getAdmins() {
		return admins;
	}

	public void setAdmins(List<AdminInfo> admins) {
		this.admins = admins;
	}

	//加载试卷时应该直接加载试题模块分段设置
    //此类实体没有对应的DAO，完全依赖本类
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "randomPaper", fetch = FetchType.EAGER)
    @Noncacheable
    List<ModuleRandomPaper> modulePapers;

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
    @Column(name = "file_score")
    private double fileScore = 10D;
    @Column(name = "toal_score")
    double totalScore = 0;

    @Column(name = "choice_num")
    int choiceNum = 0;
    @Column(name = "choice_total_score")
    double choiceTotalScore = 0;
    @Column(name = "mchoice_num")
    int mchoiceNum = 0;
    @Column(name = "mchoice_total_score")
    double mchoiceTotalScore = 0;
    @Column(name = "fill_num")
    int fillNum = 0;
    @Column(name = "fill_total_score")
    double fillTotalScore = 0;
    @Column(name = "judge_num")
    int judgeNum = 0;
    @Column(name = "judge_total_score")
    double judgeTotalScore = 0;
    @Column(name = "essay_num")
    int essayNum = 0;
    @Column(name = "essay_total_score")
    double essayTotalScore = 0;
    @Column(name = "file_num")
    int fileNum = 0;
    @Column(name = "file_total_score")
    double fileTotalScore = 0;
    @Column(name = "case_num")
    int caseNum = 0;
    @Column(name = "ord")
    private int ord=0;
    @Column(name = "business_id")
    private String businessId;
    
    
    public ExamPaperRandom() {
    }

    public ExamPaperRandom(String id) {
        this.id = id;
    }

    public ExamPaperRandom(String id, Date genTime) {
        this.id = id;
        this.genTime = genTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void computeTotalNumAndScore() {
        choiceNum = 0;
        choiceTotalScore = 0;
        mchoiceNum = 0;
        mchoiceTotalScore = 0;
        fillNum = 0;
        fillTotalScore = 0;
        judgeNum = 0;
        judgeTotalScore = 0;
        essayNum = 0;
        essayTotalScore = 0;
        fileNum = 0;
        fileTotalScore = 0;
        caseNum = 0;
        totalScore = 0;
        List<ModuleRandomPaper> oss = this.getModulePapers();
        if (oss != null) {
            for (ModuleRandomPaper me : oss) {
                choiceNum += me.getChoiceNum();
                mchoiceNum += me.getMultiChoiceNum();
                fillNum += me.getFillNum();
                judgeNum += me.getJudgeNum();
                essayNum += me.getEssayNum();
                fileNum += me.getFileNum();
                caseNum += me.getCaseNum();
            }
            choiceTotalScore = this.getChoiceScore() * choiceNum;
            mchoiceTotalScore = this.getMultiChoiceScore() * mchoiceNum;
            fillTotalScore = this.getFillScore() * fillNum;
            judgeTotalScore = this.getJudgeScore() * judgeNum;
            essayTotalScore = this.getEssayScore() * essayNum;
            fileTotalScore = this.getFileScore() * fileNum;
            
        }
        this.totalScore = choiceTotalScore + mchoiceTotalScore + fillTotalScore + judgeTotalScore + essayTotalScore + fileTotalScore;
        
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
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

    public List<ModuleRandomPaper> getModulePapers() {
        return modulePapers;
    }

    public void setModulePapers(List<ModuleRandomPaper> modulePapers) {
        this.modulePapers = modulePapers;
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

    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }

    public double getEssayScore() {
        return essayScore;
    }

    public void setEssayScore(double essayScore) {
        this.essayScore = essayScore;
    }

    public double getFileScore() {
        return fileScore;
    }

    public void setFileScore(double fileScore) {
        this.fileScore = fileScore;
    }

    public int getChoiceNum() {
        return choiceNum;
    }

    public void setChoiceNum(int choiceNum) {
        this.choiceNum = choiceNum;
    }

    public double getChoiceTotalScore() {
        return choiceTotalScore;
    }

    public void setChoiceTotalScore(double choiceTotalScore) {
        this.choiceTotalScore = choiceTotalScore;
    }

    public int getMchoiceNum() {
        return mchoiceNum;
    }

    public void setMchoiceNum(int mchoiceNum) {
        this.mchoiceNum = mchoiceNum;
    }

    public double getMchoiceTotalScore() {
        return mchoiceTotalScore;
    }

    public void setMchoiceTotalScore(double mchoiceTotalScore) {
        this.mchoiceTotalScore = mchoiceTotalScore;
    }

    public int getFillNum() {
        return fillNum;
    }

    public void setFillNum(int fillNum) {
        this.fillNum = fillNum;
    }

    public double getFillTotalScore() {
        return fillTotalScore;
    }

    public void setFillTotalScore(double fillTotalScore) {
        this.fillTotalScore = fillTotalScore;
    }

    public int getJudgeNum() {
        return judgeNum;
    }

    public void setJudgeNum(int judgeNum) {
        this.judgeNum = judgeNum;
    }

    public double getJudgeTotalScore() {
        return judgeTotalScore;
    }

    public void setJudgeTotalScore(double judgeTotalScore) {
        this.judgeTotalScore = judgeTotalScore;
    }

    public int getEssayNum() {
        return essayNum;
    }

    public void setEssayNum(int essayNum) {
        this.essayNum = essayNum;
    }

    public double getEssayTotalScore() {
        return essayTotalScore;
    }

    public void setEssayTotalScore(double essayTotalScore) {
        this.essayTotalScore = essayTotalScore;
    }

    public int getFileNum() {
        return fileNum;
    }

    public void setFileNum(int fileNum) {
        this.fileNum = fileNum;
    }

    public double getFileTotalScore() {
        return fileTotalScore;
    }

    public void setFileTotalScore(double fileTotalScore) {
        this.fileTotalScore = fileTotalScore;
    }

    public int getCaseNum() {
        return caseNum;
    }

    public void setCaseNum(int caseNum) {
        this.caseNum = caseNum;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
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
        if (!(object instanceof ExamPaperRandom)) {
            return false;
        }
        ExamPaperRandom other = (ExamPaperRandom) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.exam.model.ExamPaperRandom[ id=" + id + " ]";
    }
}
