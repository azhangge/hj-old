package com.hjedu.examination.entity.random2;

import com.google.gson.annotations.Expose;
import com.hjedu.examination.entity.contest.ContestCaseItemAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * 随机试卷模块
 * 本类为随机试卷中设置的分段，即试卷大题 ModuleRandom2Part建立了试题模块中各类题型与本类的关联
 * 每个竞赛中，此分段将为单例
 * 若将Adapter设置在此分段上，将造成各个不同用户的试卷为同一个
 *
 * @author Administrator
 */
@Entity
@Table(name = "random2_paper_part")
public class Random2PaperPart implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @Expose
    private String id = UUID.randomUUID().toString();
    @Lob
    @Column(name = "name")
    @Expose
    private String name;
    @Column(name = "ord")
    @Expose
    private int ord = 0;
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    private Date genTime = new Date();

    @ManyToOne
    @JoinColumn(name = "paper_id")
    private ExamPaperRandom2 paper;
    @Column(name = "if_show_name")
    private boolean ifShowName = false;
    @Column(name = "item_num")
    private int itemNum = 0;
    @Column(name = "total_score")
    private double totalScore = 0.0;//总分值

    @Column(name = "choice_score")
    @Expose
    private double choiceScore = 1.0;//分值
    @Column(name = "multi_choice_score")
    @Expose
    private double multiChoiceScore = 1.0;//分值
    @Column(name = "fill_score")
    @Expose
    private double fillScore = 1.0;//分值
    @Column(name = "judge_score")
    @Expose
    private double judgeScore = 1.0;//分值
    @Column(name = "essay_score")
    @Expose
    private double essayScore = 5.0;//分值
    @Column(name = "file_score")
    @Expose
    private double fileScore = 5.0;//分值

    @Transient
    @Expose
    List<ContestCaseItemAdapter> cadapters = new ArrayList();
    //最终用于暂时存放PART中的用于竞赛的试题实例适配器

    //@Transient
    //List<ExamCaseItemAdapter> adapters=new ArrayList();
    //最终用于暂时存放PART中的用于综合考试的试题实例适配器
    @Transient
    int userNum = 15;

    public Random2PaperPart() {
    }

    public Random2PaperPart(String id) {
        this.id = id;
    }

    public Random2PaperPart(String id, Date genTime) {
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

    public double getFileScore() {
        return fileScore;
    }

    public void setFileScore(double fileScore) {
        this.fileScore = fileScore;
    }

    public boolean isIfShowName() {
        return ifShowName;
    }

    public void setIfShowName(boolean ifShowName) {
        this.ifShowName = ifShowName;
    }

    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExamPaperRandom2 getPaper() {
        return paper;
    }

    public void setPaper(ExamPaperRandom2 paper) {
        this.paper = paper;
    }

//    public List<ExamCaseItemAdapter> getAdapters() {
//        
//        return adapters;
//    }
//
//    public void setAdapters(List<ExamCaseItemAdapter> adapters) {
//        this.adapters = adapters;
//    }
    public List<ContestCaseItemAdapter> getCadapters() {

        return cadapters;
    }

    public void setCadapters(List<ContestCaseItemAdapter> cadapters) {
        this.cadapters = cadapters;
    }

    public int getItemNum() {
        return itemNum;
    }

    public void setItemNum(int itemNum) {
        this.itemNum = itemNum;
    }

    public int getUserNum() {
        return userNum;
    }

    public void setUserNum(int userNum) {
        this.userNum = userNum;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    @Override
    public int compareTo(Object o) {
        Random2PaperPart cq = (Random2PaperPart) o;
        if (this.getOrd() == cq.getOrd()) {
            return this.getId().compareTo(cq.getId());
        } else {
            return this.getOrd() - cq.getOrd();
        }
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
        if (!(object instanceof Random2PaperPart)) {
            return false;
        }
        Random2PaperPart other = (Random2PaperPart) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
