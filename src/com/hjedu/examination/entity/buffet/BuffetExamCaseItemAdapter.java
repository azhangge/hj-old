/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hjedu.examination.entity.buffet;

import com.google.gson.annotations.Expose;
import com.hjedu.examination.entity.GeneralQuestion;

import java.io.Serializable;
import java.util.UUID;

public class BuffetExamCaseItemAdapter implements Serializable, Comparable {

    @Expose
    private String id = UUID.randomUUID().toString();
    @Expose
    private String qtype;
    
    private BuffetGeneralExamCaseItem item;
    @Expose
    private BuffetExamCaseItemChoice choiceItem;
    @Expose
    private BuffetExamCaseItemMultiChoice multiChoiceItem;
    @Expose
    private BuffetExamCaseItemFill fillItem;
    @Expose
    private BuffetExamCaseItemJudge judgeItem;
    @Expose
    private BuffetExamCaseItemEssay essayItem;
    @Expose
    private BuffetExamCaseItemFile fileItem;
    @Expose
    private BuffetExamCaseItemCase caseItem;
    @Expose
    private GeneralQuestion question;
    @Expose
    private int ord = 0;
    @Expose
    private double score = 0;
    @Expose
    private boolean marked = false;
    @Expose
    boolean done = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQtype() {
        return qtype;
    }

    public void setQtype(String qtype) {
        this.qtype = qtype;
    }

    public BuffetExamCaseItemChoice getChoiceItem() {
        return choiceItem;
    }

    public BuffetGeneralExamCaseItem getItem() {
        return item;
    }

    public void setItem(BuffetGeneralExamCaseItem item) {
        this.item = item;
    }

    public void setChoiceItem(BuffetExamCaseItemChoice choiceItem) {
        this.choiceItem = choiceItem;
    }

    public BuffetExamCaseItemMultiChoice getMultiChoiceItem() {
        return multiChoiceItem;
    }

    public void setMultiChoiceItem(BuffetExamCaseItemMultiChoice multiChoiceItem) {
        this.multiChoiceItem = multiChoiceItem;
    }

    public BuffetExamCaseItemFill getFillItem() {
        return fillItem;
    }

    public void setFillItem(BuffetExamCaseItemFill fillItem) {
        this.fillItem = fillItem;
    }

    public BuffetExamCaseItemJudge getJudgeItem() {
        return judgeItem;
    }

    public void setJudgeItem(BuffetExamCaseItemJudge judgeItem) {
        this.judgeItem = judgeItem;
    }

    public BuffetExamCaseItemEssay getEssayItem() {
        return essayItem;
    }

    public void setEssayItem(BuffetExamCaseItemEssay essayItem) {
        this.essayItem = essayItem;
    }

    public BuffetExamCaseItemFile getFileItem() {
        return fileItem;
    }

    public void setFileItem(BuffetExamCaseItemFile fileItem) {
        this.fileItem = fileItem;
    }

    public BuffetExamCaseItemCase getCaseItem() {
        return caseItem;
    }

    public void setCaseItem(BuffetExamCaseItemCase caseItem) {
        this.caseItem = caseItem;
    }

    public GeneralQuestion getQuestion() {
        return question;
    }

    public void setQuestion(GeneralQuestion question) {
        this.question = question;
    }

    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public boolean isMarked() {
        if ("choice".equals(this.getQtype())) {//判断试卷条目的类型
            this.marked = this.choiceItem.isMarked();
        } else if ("mchoice".equals(this.getQtype())) {
            this.marked = this.multiChoiceItem.isMarked();
        } else if ("fill".equals(this.getQtype())) {
            this.marked = this.fillItem.isMarked();
        } else if ("judge".equals(this.getQtype())) {
            this.marked = this.judgeItem.isMarked();
        } else if ("essay".equals(this.getQtype())) {
            this.marked = this.essayItem.isMarked();
        } else if ("file".equals(this.getQtype())) {
            this.marked = this.fileItem.isMarked();
        } else if ("case".equals(this.getQtype())) {
            this.marked = this.caseItem.isMarked();
        }
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public boolean isDone() {
        if ("choice".equals(this.getQtype())) {//判断试卷条目的类型
            this.done = this.choiceItem.isDone();
        } else if ("mchoice".equals(this.getQtype())) {
            this.done = this.multiChoiceItem.isDone();
        } else if ("fill".equals(this.getQtype())) {
            this.done = this.fillItem.isDone();
        } else if ("judge".equals(this.getQtype())) {
            this.done = this.judgeItem.isDone();
        } else if ("essay".equals(this.getQtype())) {
            this.done = this.essayItem.isDone();
        } else if ("file".equals(this.getQtype())) {
            this.done = this.fileItem.isDone();
        } else if ("case".equals(this.getQtype())) {
            this.done = this.caseItem.isDone();
        }
        return done;
    }

    @Override
    public int compareTo(Object o) {
        BuffetExamCaseItemAdapter oo = (BuffetExamCaseItemAdapter) o;
        return this.getOrd() - oo.getOrd();
    }

    public void setDone(boolean done) {
        this.done = done;
    }

}
