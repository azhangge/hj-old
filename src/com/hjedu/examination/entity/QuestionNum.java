/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hjedu.examination.entity;

import com.google.gson.annotations.Expose;
import java.util.UUID;

/**
 *
 * @author huajie
 */
public class QuestionNum {
    @Expose
    private String id=UUID.randomUUID().toString();
    @Expose
    private long choiceNum;
    @Expose
    private long multiChoiceNum;
    @Expose
    private long fillNum;
    @Expose
    private long judgeNum;
    @Expose
    private long essayNum;
    @Expose
    private long fileNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getChoiceNum() {
        return choiceNum;
    }

    public void setChoiceNum(long choiceNum) {
        this.choiceNum = choiceNum;
    }

    public long getMultiChoiceNum() {
        return multiChoiceNum;
    }

    public void setMultiChoiceNum(long multiChoiceNum) {
        this.multiChoiceNum = multiChoiceNum;
    }

    public long getFillNum() {
        return fillNum;
    }

    public void setFillNum(long fillNum) {
        this.fillNum = fillNum;
    }

    public long getJudgeNum() {
        return judgeNum;
    }

    public void setJudgeNum(long judgeNum) {
        this.judgeNum = judgeNum;
    }

    public long getEssayNum() {
        return essayNum;
    }

    public void setEssayNum(long essayNum) {
        this.essayNum = essayNum;
    }

    public long getFileNum() {
        return fileNum;
    }

    public void setFileNum(long fileNum) {
        this.fileNum = fileNum;
    }
    
    
    
    
    
}
