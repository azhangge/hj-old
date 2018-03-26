package com.hjedu.examination.entity.buffet;

import com.google.gson.annotations.Expose;
import com.hjedu.examination.dao.IBuffetExaminationPartDAO;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.huajie.util.SpringHelper;

import java.io.Serializable;
import javax.persistence.*;
/**
 * 练习模块
 * 单选题记录
 * @author h j
 *
 */
@Entity
@Table(name = "b_exam_case_item_choice")
public class BuffetExamCaseItemChoice extends BuffetGeneralExamCaseItem implements Serializable, Comparable {

    @ManyToOne
    @JoinColumn(name = "question_id")
    private ChoiceQuestion question;
    @ManyToOne
    @JoinColumn(name = "case_item_id")
    private BuffetExamCaseItemCase caseItem;

    @Column(name = "user_answer")
    @Expose
    private String userAnswer;
    @Column(name = "if_right")
    @Expose
    private boolean ifRight;
    @Column(name = "right_answer")
    @Expose
    private String rightAnswer;
    @Column(name = "marked")
    @Expose
    private boolean marked = false;
    @Column(name = "score")
    @Expose
    private double score = 1;
    @Column(name = "part_id")
    @Expose
    private String partId;

    @Transient
    private BuffetExaminationPart part;

    public BuffetExamCaseItemChoice() {
    }

    public ChoiceQuestion getQuestion() {
        return question;
    }

    public void setQuestion(ChoiceQuestion question) {
        this.question = question;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public boolean getIfRight() {
        return ifRight;
    }

    public void setIfRight(boolean ifRight) {
        this.ifRight = ifRight;
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

    public BuffetExamCaseItemCase getCaseItem() {
        return caseItem;
    }

    public void setCaseItem(BuffetExamCaseItemCase caseItem) {
        this.caseItem = caseItem;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public BuffetExaminationPart getPart() {
        if (partId != null) {
            IBuffetExaminationPartDAO pDAO = SpringHelper.getSpringBean("BuffetExaminationPartDAO");
            part = pDAO.findBuffetExaminationPart(partId);
        }
        return part;
    }

    public void setPart(BuffetExaminationPart part) {
        this.part = part;
    }

    @Override
    public int compareTo(Object o) {
        BuffetExamCaseItemChoice cq = (BuffetExamCaseItemChoice) o;
        if (this.getQuestion() == null || cq.getQuestion() == null) {
            return 1;
        }
        return this.getQuestion().getId().compareTo(cq.getQuestion().getId());
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
        if (!(object instanceof BuffetExamCaseItemChoice)) {
            return false;
        }
        BuffetExamCaseItemChoice other = (BuffetExamCaseItemChoice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.exam.model.ExamCaseItemChoice[ id=" + id + " ]";
    }
}
