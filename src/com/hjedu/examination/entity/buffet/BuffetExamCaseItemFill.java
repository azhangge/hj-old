package com.hjedu.examination.entity.buffet;

import com.google.gson.annotations.Expose;
import com.hjedu.examination.dao.IBuffetExaminationPartDAO;
import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.examination.service.impl.IBuffetExamCaseService;
import com.huajie.util.SpringHelper;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
/**
 * 练习模块
 * 填空题记录
 * @author h j
 *
 */
@Entity
@Table(name = "b_exam_case_item_fill")
public class BuffetExamCaseItemFill extends BuffetGeneralExamCaseItem implements Serializable, Comparable {

    @ManyToOne
    @JoinColumn(name = "case_item_id")
    private BuffetExamCaseItemCase caseItem;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private FillQuestion question;
    @Column(name = "user_answer_str")
    @Expose
    private String userAnswerStr;
    @Column(name = "right_answer_str")
    @Expose
    private String rightAnswerStr;
    @Column(name = "right_num")
    @Expose
    private int rightNum;
    @Transient
    @Expose
    private int totalNum = 0;
    @Transient
    @Expose
    private List<BuffetExamCaseItemFillBlock> blocks = null;
    @Transient
    @Expose
    private String lastStr;
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

    public BuffetExamCaseItemFill() {
    }

    public FillQuestion getQuestion() {
        return question;
    }

    public void setQuestion(FillQuestion question) {
        this.question = question;
    }

    public String getRightAnswerStr() {
        return rightAnswerStr;
    }

    public void setRightAnswerStr(String rightAnswerStr) {
        this.rightAnswerStr = rightAnswerStr;
    }

    public BuffetExamCaseItemCase getCaseItem() {
        return caseItem;
    }

    public void setCaseItem(BuffetExamCaseItemCase caseItem) {
        this.caseItem = caseItem;
    }

    public int getRightNum() {
        return rightNum;
    }

    public void setRightNum(int rightNum) {
        this.rightNum = rightNum;
    }

    public String getUserAnswerStr() {
        return userAnswerStr;
    }

    public void setUserAnswerStr(String userAnswerStr) {
        this.userAnswerStr = userAnswerStr;
    }

    public List<BuffetExamCaseItemFillBlock> getBlocks() {
        if (this.blocks == null) {
            IBuffetExamCaseService ecs = SpringHelper.getSpringBean("BuffetExamCaseService");
            ecs.buildItemFillBlocks(this);
        }
        return blocks;
    }

    public void setBlocks(List<BuffetExamCaseItemFillBlock> blocks) {
        this.blocks = blocks;
    }

    public String getLastStr() {
        return lastStr;
    }

    public void setLastStr(String lastStr) {
        this.lastStr = lastStr;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getTotalNum() {
        if (this.blocks != null) {
            totalNum = this.blocks.size();
        }
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
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
        BuffetExamCaseItemFill cq = (BuffetExamCaseItemFill) o;
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
        if (!(object instanceof BuffetExamCaseItemFill)) {
            return false;
        }
        BuffetExamCaseItemFill other = (BuffetExamCaseItemFill) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.exam.model.ExamCaseItemFill[ id=" + id + " ]";
    }
}
