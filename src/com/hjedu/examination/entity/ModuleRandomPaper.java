package com.hjedu.examination.entity;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;


/**
 * 试卷模块
 * 简单随机试卷和模块多对多对应关系的中间表
 * @author huajie
 */
@Entity
@Table(name = "module_random_paper")
public class ModuleRandomPaper implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();
    
    @ManyToOne
    @JoinColumn(name = "module_id")
    private ExamModuleModel module;
    
    @ManyToOne
    @JoinColumn(name = "paper_id")
    private ExamPaperRandom randomPaper;
    
    @Column(name = "fill_num")
    private int fillNum=0;
    @Column(name = "choice_num")
    private int choiceNum=0;
    @Column(name = "multi_choice_num")
    private int multiChoiceNum=0;
    @Column(name = "judge_num")
    private int judgeNum=0;
    @Column(name = "essay_num")
    private int essayNum=0;
    @Column(name = "file_num")
    private int fileNum=0;
    @Column(name = "case_num")
    private int caseNum=0;

    public ModuleRandomPaper() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public int getChoiceNum() {
        return choiceNum;
    }

    public void setChoiceNum(int choiceNum) {
        this.choiceNum = choiceNum;
    }

    public int getFillNum() {
        return fillNum;
    }

    public void setFillNum(int fillNum) {
        this.fillNum = fillNum;
    }

    public int getJudgeNum() {
        return judgeNum;
    }

    public void setJudgeNum(int judgeNum) {
        this.judgeNum = judgeNum;
    }

    public ExamModuleModel getModule() {
        return module;
    }

    public void setModule(ExamModuleModel module) {
        this.module = module;
    }

    public int getMultiChoiceNum() {
        return multiChoiceNum;
    }

    public void setMultiChoiceNum(int multiChoiceNum) {
        this.multiChoiceNum = multiChoiceNum;
    }

    public ExamPaperRandom getRandomPaper() {
        return randomPaper;
    }

    public void setRandomPaper(ExamPaperRandom randomPaper) {
        this.randomPaper = randomPaper;
    }

    public int getEssayNum() {
        return essayNum;
    }

    public void setEssayNum(int essayNum) {
        this.essayNum = essayNum;
    }

    public int getFileNum() {
        return fileNum;
    }

    public void setFileNum(int fileNum) {
        this.fileNum = fileNum;
    }

    public int getCaseNum() {
        return caseNum;
    }

    public void setCaseNum(int caseNum) {
        this.caseNum = caseNum;
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
        if (!(object instanceof ModuleRandomPaper)) {
            return false;
        }
        ModuleRandomPaper other = (ModuleRandomPaper) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.exam.model.ModuleExamination[ id=" + id + " ]";
    }
    
}
