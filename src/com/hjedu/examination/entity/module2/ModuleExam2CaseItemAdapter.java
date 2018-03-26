/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hjedu.examination.entity.module2;

import java.io.Serializable;
import java.util.UUID;

import com.hjedu.examination.entity.GeneralQuestion;
import com.hjedu.examination.entity.module.ModuleExamCaseItemCase;
import com.hjedu.examination.entity.module.ModuleExamCaseItemChoice;
import com.hjedu.examination.entity.module.ModuleExamCaseItemEssay;
import com.hjedu.examination.entity.module.ModuleExamCaseItemFile;
import com.hjedu.examination.entity.module.ModuleExamCaseItemFill;
import com.hjedu.examination.entity.module.ModuleExamCaseItemJudge;
import com.hjedu.examination.entity.module.ModuleExamCaseItemMultiChoice;
import com.hjedu.examination.entity.module.ModuleGeneralExamCaseItem;

public class ModuleExam2CaseItemAdapter implements Serializable,Comparable{

    private String id = UUID.randomUUID().toString();
    private String qtype;
    private ModuleExam2Part moduleExam2Part;
    private ModuleGeneralExamCaseItem item;
    private ModuleExamCaseItemChoice choiceItem;
    private ModuleExamCaseItemMultiChoice multiChoiceItem;
    private ModuleExamCaseItemFill fillItem;
    private ModuleExamCaseItemJudge judgeItem;
    private ModuleExamCaseItemEssay essayItem;
    private ModuleExamCaseItemFile fileItem;
    private ModuleExamCaseItemCase caseItem;
    private GeneralQuestion question;
    private int ord = 0;
    private double score = 0;

    private boolean marked = false;
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

    public ModuleExamCaseItemChoice getChoiceItem() {
        return choiceItem;
    }

    public ModuleExam2Part getModuleExam2Part() {
        return moduleExam2Part;
    }

    public void setModuleExam2Part(ModuleExam2Part moduleExam2Part) {
        this.moduleExam2Part = moduleExam2Part;
    }

    public ModuleGeneralExamCaseItem getItem() {
        return item;
    }

    public void setItem(ModuleGeneralExamCaseItem item) {
        this.item = item;
    }

    public void setChoiceItem(ModuleExamCaseItemChoice choiceItem) {
        this.choiceItem = choiceItem;
    }

    public ModuleExamCaseItemMultiChoice getMultiChoiceItem() {
        return multiChoiceItem;
    }

    public void setMultiChoiceItem(ModuleExamCaseItemMultiChoice multiChoiceItem) {
        this.multiChoiceItem = multiChoiceItem;
    }

    public ModuleExamCaseItemFill getFillItem() {
        return fillItem;
    }

    public void setFillItem(ModuleExamCaseItemFill fillItem) {
        this.fillItem = fillItem;
    }

    public ModuleExamCaseItemJudge getJudgeItem() {
        return judgeItem;
    }

    public void setJudgeItem(ModuleExamCaseItemJudge judgeItem) {
        this.judgeItem = judgeItem;
    }

    public ModuleExamCaseItemEssay getEssayItem() {
        return essayItem;
    }

    public void setEssayItem(ModuleExamCaseItemEssay essayItem) {
        this.essayItem = essayItem;
    }

    public ModuleExamCaseItemFile getFileItem() {
        return fileItem;
    }

    public void setFileItem(ModuleExamCaseItemFile fileItem) {
        this.fileItem = fileItem;
    }

    public ModuleExamCaseItemCase getCaseItem() {
        return caseItem;
    }

    public void setCaseItem(ModuleExamCaseItemCase caseItem) {
        this.caseItem = caseItem;
    }

    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }

    public GeneralQuestion getQuestion() {
        return question;
    }

    public void setQuestion(GeneralQuestion question) {
        this.question = question;
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
        ModuleExam2CaseItemAdapter oo=(ModuleExam2CaseItemAdapter)o;
        return this.getOrd()-oo.getOrd();
    }

    public void setDone(boolean done) {
        this.done = done;
    }

}
