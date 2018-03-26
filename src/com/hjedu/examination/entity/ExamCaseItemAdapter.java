package com.hjedu.examination.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.UUID;

/**
 * 本类在ExamCaseItemX和试卷大题之间建立了一种适配关系，用于人工试卷和随机试卷
 * Random2PaperPart表示随机试卷分段（试卷大题），通过ModuleRandom2Part再获得每个试题模块每种题型的抽题信息，其中可以包含adapters和cadapters，分别代表综合考试试题实例适配器和竞赛试题实例适配器
 * ExamPaperManualPart表示人工试卷分段，其中还包含ManualPartItem，表示人工选取的试题设置信息，考试时根据ManualPartItem为ExamPaperManualPart构建ExamCaseItemAdapter
 *
 * @author www.wbdwl.com
 */
public class ExamCaseItemAdapter implements Serializable, Comparable {

    private String id = UUID.randomUUID().toString();

    private String qtype;
    
    @JsonIgnore
    private VirtualExamPart virtualPart;
    
    @JsonIgnore
    private GeneralExamCaseItem item;

    private ExamCaseItemChoice choiceItem;

    private ExamCaseItemMultiChoice multiChoiceItem;

    private ExamCaseItemFill fillItem;

    private ExamCaseItemJudge judgeItem;

    private ExamCaseItemEssay essayItem;

    private ExamCaseItemFile fileItem;

    private ExamCaseItemCase caseItem;

    private GeneralQuestion question;

    private int ord = 0;

    private double score = 0;//本题分值

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

    public ExamCaseItemChoice getChoiceItem() {
        return choiceItem;
    }

    public GeneralQuestion getQuestion() {
        return question;
    }

    public void setQuestion(GeneralQuestion question) {
        this.question = question;
    }

    public GeneralExamCaseItem getItem() {
        return item;
    }

    public void setItem(GeneralExamCaseItem item) {
        this.item = item;
    }

    public void setChoiceItem(ExamCaseItemChoice choiceItem) {
        this.choiceItem = choiceItem;
    }

    public ExamCaseItemMultiChoice getMultiChoiceItem() {
        return multiChoiceItem;
    }

    public void setMultiChoiceItem(ExamCaseItemMultiChoice multiChoiceItem) {
        this.multiChoiceItem = multiChoiceItem;
    }

    public ExamCaseItemFill getFillItem() {
        return fillItem;
    }

    public void setFillItem(ExamCaseItemFill fillItem) {
        this.fillItem = fillItem;
    }

    public ExamCaseItemJudge getJudgeItem() {
        return judgeItem;
    }

    public void setJudgeItem(ExamCaseItemJudge judgeItem) {
        this.judgeItem = judgeItem;
    }

    public ExamCaseItemEssay getEssayItem() {
        return essayItem;
    }

    public void setEssayItem(ExamCaseItemEssay essayItem) {
        this.essayItem = essayItem;
    }

    public ExamCaseItemFile getFileItem() {
        return fileItem;
    }

    public void setFileItem(ExamCaseItemFile fileItem) {
        this.fileItem = fileItem;
    }

    public VirtualExamPart getVirtualPart() {
        return virtualPart;
    }

    public void setVirtualPart(VirtualExamPart virtualPart) {
        this.virtualPart = virtualPart;
    }

    public ExamCaseItemCase getCaseItem() {
        return caseItem;
    }

    public void setCaseItem(ExamCaseItemCase caseItem) {
        this.caseItem = caseItem;
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

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public int compareTo(Object o) {
        ExamCaseItemAdapter oo = (ExamCaseItemAdapter) o;
        try {
            return this.getOrd() - oo.getOrd();
        } catch (Exception ee) {
            ee.printStackTrace();
            return -1;
        }
    }

}
