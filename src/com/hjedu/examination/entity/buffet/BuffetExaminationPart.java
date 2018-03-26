package com.hjedu.examination.entity.buffet;

import com.google.gson.annotations.Expose;
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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
/**
 * 练习模块
 * 练习配置表
 * @author h j
 *
 */
@Entity
@Table(name = "buffet_examination_part")
public class BuffetExaminationPart implements Serializable,Comparable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @Expose
    private String id = UUID.randomUUID().toString();
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
    @JoinColumn(name = "exam_id")
    private BuffetExamination exam;
    @Column(name = "if_show_name")
    @Expose
    private boolean ifShowName = false;
    @Column(name = "item_num")
    @Expose
    private int itemNum = 0;
    @Column(name = "total_score")
    @Expose
    private double totalScore = 0.0;//总分值

    @Column(name = "score")
    private double score = 1.0;//每道题目分值
    
    @Column(name = "choice_score")
    private double choiceScore = 1.0;//分值
    @Column(name = "multi_choice_score")
    private double multiChoiceScore = 1.0;//分值
    @Column(name = "fill_score")
    private double fillScore = 1.0;//分值
    @Column(name = "judge_score")
    private double judgeScore = 1.0;//分值
    @Column(name = "essay_score")
    private double essayScore = 5.0;//分值
    @Column(name = "file_score")
    private double fileScore = 5.0;//分值

    @Transient
    @Expose
    List<BuffetExamCaseItemAdapter> adapters;
    //最终用于暂时存放PART中的试题
    @Transient
    @Expose
    int userNum = 15;

    public BuffetExaminationPart() {
    }

    public BuffetExaminationPart(String id) {
        this.id = id;
    }

    public BuffetExaminationPart(String id, Date genTime) {
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

    

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
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

    public BuffetExamination getExam() {
        return exam;
    }

    public void setExam(BuffetExamination exam) {
        this.exam = exam;
    }

    public List<BuffetExamCaseItemAdapter> getAdapters() {
        if (adapters == null) {
            adapters = new ArrayList();
        }
        return adapters;
    }

    public void setAdapters(List<BuffetExamCaseItemAdapter> adapters) {
        this.adapters = adapters;
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
        BuffetExaminationPart cq = (BuffetExaminationPart) o;
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
        if (!(object instanceof BuffetExaminationPart)) {
            return false;
        }
        BuffetExaminationPart other = (BuffetExaminationPart) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
