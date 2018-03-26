package com.hjedu.examination.entity;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.Date;

public class WrongTestInfo implements Serializable {

    @Expose
    long id = System.nanoTime();
    String name = "错题练习";
    Date genTime = new Date();
    @Expose
    boolean ifDel = true;
    @Expose
    boolean questionRandom = true;
    @Expose
    boolean choiceRandom = false;
    @Expose
    boolean multiChoiceRandom = false;
    @Expose
    long testMinute = 30L;
    WrongQuestionWrapper2 wqWrapper;
    Examination examination;
    @Expose
    int choiceNum = 10;
    @Expose
    int multiChoiceNum = 10;
    @Expose
    int fillNum = 10;
    @Expose
    int judgeNum = 10;
    @Expose
    int essayNum = 2;
    @Expose
    int fileNum = 0;

    public WrongTestInfo() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Examination getExamination() {
        return examination;
    }

    public void setExamination(Examination examination) {
        this.examination = examination;
    }

    public boolean isQuestionRandom() {
        return questionRandom;
    }

    public void setQuestionRandom(boolean questionRandom) {
        this.questionRandom = questionRandom;
    }

    public boolean isChoiceRandom() {
        return choiceRandom;
    }

    public void setChoiceRandom(boolean choiceRandom) {
        this.choiceRandom = choiceRandom;
    }

    public boolean isMultiChoiceRandom() {
        return multiChoiceRandom;
    }

    public void setMultiChoiceRandom(boolean multiChoiceRandom) {
        this.multiChoiceRandom = multiChoiceRandom;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public boolean isIfDel() {
        return ifDel;
    }

    public void setIfDel(boolean ifDel) {
        this.ifDel = ifDel;
    }

    public WrongQuestionWrapper2 getWqWrapper() {
        return wqWrapper;
    }

    public void setWqWrapper(WrongQuestionWrapper2 wqWrapper) {
        this.wqWrapper = wqWrapper;
    }

    public long getTestMinute() {
        return testMinute;
    }

    public void setTestMinute(long testMinute) {
        this.testMinute = testMinute;
    }

    public int getChoiceNum() {
        return choiceNum;
    }

    public void setChoiceNum(int choiceNum) {
        this.choiceNum = choiceNum;
    }

    public int getMultiChoiceNum() {
        return multiChoiceNum;
    }

    public void setMultiChoiceNum(int multiChoiceNum) {
        this.multiChoiceNum = multiChoiceNum;
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
}
