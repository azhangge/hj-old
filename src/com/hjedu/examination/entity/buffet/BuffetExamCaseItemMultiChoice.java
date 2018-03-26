package com.hjedu.examination.entity.buffet;

import com.google.gson.annotations.Expose;
import com.hjedu.examination.dao.IBuffetExaminationPartDAO;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.huajie.util.SpringHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
/**
 * 练习模块
 * 多选题记录
 * @author h j
 *
 */
@Entity
@Table(name = "b_exam_case_item_multi_choice")
public class BuffetExamCaseItemMultiChoice extends BuffetGeneralExamCaseItem implements Serializable, Comparable {

    @ManyToOne
    @JoinColumn(name = "case_item_id")
    private BuffetExamCaseItemCase caseItem;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private MultiChoiceQuestion question;
    @Column(name = "user_answer")
    @Expose
    private String userAnswer;
    @Column(name = "if_right")
    @Expose
    private Boolean ifRight;
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
    @Transient
    private List<String> selectedLabels=new ArrayList();

    public BuffetExamCaseItemMultiChoice() {
    }

    public MultiChoiceQuestion getQuestion() {
        return question;
    }

    public void setQuestion(MultiChoiceQuestion question) {
        this.question = question;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public BuffetExamCaseItemCase getCaseItem() {
        return caseItem;
    }

    public void setCaseItem(BuffetExamCaseItemCase caseItem) {
        this.caseItem = caseItem;
    }

    public Boolean getIfRight() {
        return ifRight;
    }

    public void setIfRight(Boolean ifRight) {
        this.ifRight = ifRight;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public List<String> getSelectedLabels() {
        return selectedLabels;
    }

    public void setSelectedLabels(List<String> selectedLabels) {
        this.selectedLabels = selectedLabels;
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
        BuffetExamCaseItemMultiChoice cq = (BuffetExamCaseItemMultiChoice) o;
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
        if (!(object instanceof BuffetExamCaseItemMultiChoice)) {
            return false;
        }
        BuffetExamCaseItemMultiChoice other = (BuffetExamCaseItemMultiChoice) object;
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
