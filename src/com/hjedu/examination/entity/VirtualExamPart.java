package com.hjedu.examination.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.hjedu.examination.entity.contest.ContestCaseItemAdapter;
import com.hjedu.examination.entity.random2.Random2PaperPart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 本类为考试的试卷分段，由于试卷分类经常是可持久化的，在系统内实例唯一，而考试实例各不相同，因此本类为可多对象存在的考试虚拟分段
 * 本类暂用于随机试卷和人工试卷
 *
 * @author Administrator
 */
public class VirtualExamPart implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;

    private String id = UUID.randomUUID().toString();

    private String name;

    private int ord = 0;

    private String description;

    private Date genTime = new Date();

    private boolean ifShowName = false;

    private int itemNum = 0;

    private double totalScore = 0.0;//总分值

    private double realScore = 0.0;//实得分值

    private double choiceScore = 1.0;//分值

    private double multiChoiceScore = 1.0;//分值

    private double fillScore = 1.0;//分值

    private double judgeScore = 1.0;//分值

    private double essayScore = 5.0;//分值

    private double fileScore = 5.0;//分值
    
    @JsonIgnore
    List<ContestCaseItemAdapter> cadapters = new ArrayList();
    //最终用于暂时存放PART中的用于竞赛的试题实例适配器

    List<ExamCaseItemAdapter> adapters = new ArrayList();
    //最终用于暂时存放PART中的用于综合考试的试题实例适配器

    int userNum = 15;

    public VirtualExamPart() {
    }

    public VirtualExamPart(String id) {
        this.id = id;
    }

    public VirtualExamPart(String id, Date genTime) {
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

    public double getRealScore() {
        return realScore;
    }

    public void setRealScore(double realScore) {
        this.realScore = realScore;
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

    public List<ExamCaseItemAdapter> getAdapters() {

        return adapters;
    }

    public void setAdapters(List<ExamCaseItemAdapter> adapters) {
        this.adapters = adapters;
    }

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
        VirtualExamPart cq = (VirtualExamPart) o;
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
        VirtualExamPart other = (VirtualExamPart) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
