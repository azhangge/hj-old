
package com.hjedu.examination.entity.contest;

import com.google.gson.annotations.Expose;
import com.hjedu.examination.entity.GeneralQuestion;

import java.io.Serializable;
import java.util.UUID;

public class ContestCaseItemAdapter implements Serializable, Comparable {

    @Expose
    private String id = UUID.randomUUID().toString();
    
    @Expose
    private String qtype;
    
    private GeneralContestCaseItem item;
    
    @Expose
    private ContestCaseItemChoice choiceItem;
    
    @Expose
    private ContestCaseItemMultiChoice multiChoiceItem;
    
    @Expose
    private ContestCaseItemFill fillItem;
    
    @Expose
    private ContestCaseItemJudge judgeItem;
    
    @Expose
    private ContestCaseItemEssay essayItem;
    
    @Expose
    private ContestCaseItemFile fileItem;
    
    @Expose
    private ContestCaseItemCase caseItem;
    
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
    
    private int index=0;

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

    public ContestCaseItemChoice getChoiceItem() {
        return choiceItem;
    }

    public GeneralContestCaseItem getItem() {
        return item;
    }

    public void setItem(GeneralContestCaseItem item) {
        this.item = item;
    }

    public void setChoiceItem(ContestCaseItemChoice choiceItem) {
        this.choiceItem = choiceItem;
    }

    public ContestCaseItemMultiChoice getMultiChoiceItem() {
        return multiChoiceItem;
    }

    public void setMultiChoiceItem(ContestCaseItemMultiChoice multiChoiceItem) {
        this.multiChoiceItem = multiChoiceItem;
    }

    public ContestCaseItemFill getFillItem() {
        return fillItem;
    }

    public void setFillItem(ContestCaseItemFill fillItem) {
        this.fillItem = fillItem;
    }

    public ContestCaseItemJudge getJudgeItem() {
        return judgeItem;
    }

    public void setJudgeItem(ContestCaseItemJudge judgeItem) {
        this.judgeItem = judgeItem;
    }

    public ContestCaseItemEssay getEssayItem() {
        return essayItem;
    }

    public void setEssayItem(ContestCaseItemEssay essayItem) {
        this.essayItem = essayItem;
    }

    public ContestCaseItemFile getFileItem() {
        return fileItem;
    }

    public void setFileItem(ContestCaseItemFile fileItem) {
        this.fileItem = fileItem;
    }

    public ContestCaseItemCase getCaseItem() {
        return caseItem;
    }

    public void setCaseItem(ContestCaseItemCase caseItem) {
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

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int compareTo(Object o) {
        ContestCaseItemAdapter oo = (ContestCaseItemAdapter) o;
        return this.getIndex()- oo.getIndex();
    }
}
