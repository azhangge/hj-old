package com.hjedu.examination.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hjedu.examination.dao.IRandom2PaperPartDAO;
import com.hjedu.examination.entity.random2.Random2PaperPart;
import com.huajie.util.SpringHelper;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
/**
 * 考试模块
 * 考试记录父类
 * @author h j
 *
 */
@Entity
@Table(name = "general_exam_case_item")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class GeneralExamCaseItem implements Serializable {

    static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    String id = UUID.randomUUID().toString();
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "case_id")
    ExamCase examCase;
    @JsonIgnore
    @Column(name = "part_id")
    private String partId;

    @Column(name = "done")
    
    boolean done = false;

    @Lob
    @Column(name = "user_answer")
    
    private String userAnswer = "";
    @Lob
    @Column(name = "right_answer")
    
    private String rightAnswer = "";
    @Column(name = "marked")
    
    private boolean marked = false;

    @Column(name = "if_right")
    
    boolean ifRight;

    @Column(name = "start_time_stamp")
    long startTimeStamp;//start time

    @Column(name = "end_time_stamp")
    long endTimeStamp;//end time

    @Column(name = "duration")
    long duration;//duration of doing the question(millsecond)

    @Column(name = "score")
    private double score = 0;//分值

    @Column(name = "real_score")
    private double realScore = 0;//实得分值
    @JsonIgnore
    @Transient
    private Random2PaperPart part;

    //在整张试卷内的标号
    @Column(name = "index2")
    int index = 0;

    //在综合题内的标号
    @Transient
    int caseIndex = 0;
    @JsonIgnore
    @Transient
    String partName;
    @JsonIgnore
    @Transient
    String qtype = "choice";
    @Transient
    String caseType = "";

    @Transient
    String tempQuestionId = null;//在计算过程中临时使用
    @Transient
    String tempUserId = null;//在计算过程中临时使用

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ExamCase getExamCase() {
        return examCase;
    }

    public void setExamCase(ExamCase examCase) {
        this.examCase = examCase;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public Random2PaperPart getPart() {
        if (partId != null) {
            IRandom2PaperPartDAO pDAO = SpringHelper.getSpringBean("Random2PaperPartDAO");
            part = pDAO.findRandom2PaperPart(partId);
        }
        return part;
    }

    public void setPart(Random2PaperPart part) {
        this.part = part;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double getRealScore() {
        return realScore;
    }

    public void setRealScore(double realScore) {
        this.realScore = realScore;
    }

    public String getTempUserId() {
        return tempUserId;
    }

    public void setTempUserId(String tempUserId) {
        this.tempUserId = tempUserId;
    }

    public int getCaseIndex() {
        return caseIndex;
    }

    public void setCaseIndex(int caseIndex) {
        this.caseIndex = caseIndex;
    }

    public boolean isIfRight() {
        return ifRight;
    }

    public void setIfRight(boolean ifRight) {
        this.ifRight = ifRight;
    }

    public long getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public long getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(long endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getQtype() {
        return qtype;
    }

    public void setQtype(String qtype) {
        this.qtype = qtype;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getTempQuestionId() {
        return tempQuestionId;
    }

    public void setTempQuestionId(String tempQuestionId) {
        this.tempQuestionId = tempQuestionId;
    }

}
