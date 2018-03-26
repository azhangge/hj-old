package com.hjedu.examination.entity.random2;


import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;

import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.buffet.ModuleBuffetPart;

/**
 * 随机试卷模块
 * 多对多关系分解表，此表为试题模块的各类题型与随机试卷大题之间建立了关联
 * @author huajie.com
 */
@Entity
@Table(name = "module_random2_part")
public class ModuleRandom2Part implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();
    @ManyToOne(cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "module_id")
    private ExamModuleModel module;
    
    @Column(name = "choice_part_id")
    private String choicePartId;//表示此节单选题对应的试卷大题ID
    @Column(name = "mchoice_part_id")
    private String mchoicePartId;
    @Column(name = "fill_part_id")
    private String fillPartId;
    @Column(name = "judge_part_id")
    private String judgePartId;
    @Column(name = "essay_part_id")
    private String essayPartId;
    @Column(name = "file_part_id")
    private String filePartId;
    @Column(name = "case_part_id")
    private String casePartId;
    
    @ManyToOne(cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "paper_id")
    private ExamPaperRandom2 paper;
    
    
    @Column(name = "fill_weight")
    private int fillWeight=0;
    @Column(name = "choice_weight")
    private int choiceWeight=0;
    @Column(name = "multi_choice_weight")
    private int multiChoiceWeight=0;
    @Column(name = "judge_weight")
    private int judgeWeight=0;
    @Column(name = "essay_weight")
    private int essayWeight=0;
    @Column(name = "file_weight")
    private int fileWeight=0;
    @Column(name = "case_weight")
    private int caseWeight=0;

    public ModuleRandom2Part() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getFillWeight() {
        return fillWeight;
    }

    public void setFillWeight(int fillWeight) {
        this.fillWeight = fillWeight;
    }

    public int getChoiceWeight() {
        return choiceWeight;
    }

    public void setChoiceWeight(int choiceWeight) {
        this.choiceWeight = choiceWeight;
    }

    public int getMultiChoiceWeight() {
        return multiChoiceWeight;
    }

    public void setMultiChoiceWeight(int multiChoiceWeight) {
        this.multiChoiceWeight = multiChoiceWeight;
    }

    public int getJudgeWeight() {
        return judgeWeight;
    }

    public void setJudgeWeight(int judgeWeight) {
        this.judgeWeight = judgeWeight;
    }

    public int getEssayWeight() {
        return essayWeight;
    }

    public void setEssayWeight(int essayWeight) {
        this.essayWeight = essayWeight;
    }

    public int getFileWeight() {
        return fileWeight;
    }

    public void setFileWeight(int fileWeight) {
        this.fileWeight = fileWeight;
    }

    public int getCaseWeight() {
        return caseWeight;
    }

    public void setCaseWeight(int caseWeight) {
        this.caseWeight = caseWeight;
    }



    public ExamModuleModel getModule() {
        return module;
    }

    public void setModule(ExamModuleModel module) {
        this.module = module;
    }


    public String getChoicePartId() {
        return choicePartId;
    }

    public void setChoicePartId(String choicePartId) {
        this.choicePartId = choicePartId;
    }

    public String getMchoicePartId() {
        return mchoicePartId;
    }

    public void setMchoicePartId(String mchoicePartId) {
        this.mchoicePartId = mchoicePartId;
    }

    public String getFillPartId() {
        return fillPartId;
    }

    public void setFillPartId(String fillPartId) {
        this.fillPartId = fillPartId;
    }

    public String getJudgePartId() {
        return judgePartId;
    }

    public void setJudgePartId(String judgePartId) {
        this.judgePartId = judgePartId;
    }

    public String getEssayPartId() {
        return essayPartId;
    }

    public void setEssayPartId(String essayPartId) {
        this.essayPartId = essayPartId;
    }

    public String getFilePartId() {
        return filePartId;
    }

    public void setFilePartId(String filePartId) {
        this.filePartId = filePartId;
    }

    public String getCasePartId() {
        return casePartId;
    }

    public void setCasePartId(String casePartId) {
        this.casePartId = casePartId;
    }

    public ExamPaperRandom2 getPaper() {
        return paper;
    }

    public void setPaper(ExamPaperRandom2 paper) {
        this.paper = paper;
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
        if (!(object instanceof ModuleBuffetPart)) {
            return false;
        }
        ModuleRandom2Part other = (ModuleRandom2Part) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.exam.model.ModuleRandomPart[ id=" + id + " ]";
    }
    
}
