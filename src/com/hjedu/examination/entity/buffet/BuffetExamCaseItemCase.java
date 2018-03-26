package com.hjedu.examination.entity.buffet;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hjedu.examination.dao.IBuffetExaminationPartDAO;
import com.hjedu.examination.entity.CaseQuestion;
import com.huajie.util.SpringHelper;
/**
 * 练习模块
 * 练习对应大题
 * @author h j
 *
 */
@Entity
@Table(name = "b_exam_case_item_case")
public class BuffetExamCaseItemCase extends BuffetGeneralExamCaseItem implements Serializable, Comparable {

    @ManyToOne
    @JoinColumn(name = "question_id")
    private CaseQuestion question;

    @OneToMany(mappedBy = "caseItem", targetEntity = BuffetExamCaseItemChoice.class, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<BuffetExamCaseItemChoice> choiceItems;
    @OneToMany(mappedBy = "caseItem", targetEntity = BuffetExamCaseItemFill.class, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<BuffetExamCaseItemFill> fillItems;
    @OneToMany(mappedBy = "caseItem", targetEntity = BuffetExamCaseItemMultiChoice.class, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<BuffetExamCaseItemMultiChoice> multiChoiceItems;
    @OneToMany(mappedBy = "caseItem", targetEntity = BuffetExamCaseItemJudge.class, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<BuffetExamCaseItemJudge> judgeItems;
    @OneToMany(mappedBy = "caseItem", targetEntity = BuffetExamCaseItemEssay.class, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<BuffetExamCaseItemEssay> essayItems;

    @Column(name = "choice_score")
    private double choiceScore;
    @Column(name = "multi_choice_score")
    private double multiChoiceScore;
    @Column(name = "fill_score")
    private double fillScore;
    @Column(name = "judge_score")
    private double judgeScore;
    @Column(name = "essay_score")
    private double essayScore;
    @Column(name = "remark")
    private String remark = "";
    @Column(name = "part_id")
    private String partId;

    @Transient
    private BuffetExaminationPart part;
    @Transient
    private boolean marked = false;

    public BuffetExamCaseItemCase() {
    }

    public CaseQuestion getQuestion() {
        return question;
    }

    public void setQuestion(CaseQuestion question) {
        this.question = question;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public double getChoiceScore() {
        return choiceScore;
    }

    public void setChoiceScore(double choiceScore) {
        this.choiceScore = choiceScore;
    }

    public double getEssayScore() {
        return essayScore;
    }

    public void setEssayScore(double essayScore) {
        this.essayScore = essayScore;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<BuffetExamCaseItemChoice> getChoiceItems() {
        return choiceItems;
    }

    public void setChoiceItems(List<BuffetExamCaseItemChoice> choiceItems) {
        this.choiceItems = choiceItems;
    }

    public List<BuffetExamCaseItemFill> getFillItems() {
        return fillItems;
    }

    public void setFillItems(List<BuffetExamCaseItemFill> fillItems) {
        this.fillItems = fillItems;
    }

    public List<BuffetExamCaseItemMultiChoice> getMultiChoiceItems() {
        return multiChoiceItems;
    }

    public void setMultiChoiceItems(List<BuffetExamCaseItemMultiChoice> multiChoiceItems) {
        this.multiChoiceItems = multiChoiceItems;
    }

    public List<BuffetExamCaseItemJudge> getJudgeItems() {
        return judgeItems;
    }

    public void setJudgeItems(List<BuffetExamCaseItemJudge> judgeItems) {
        this.judgeItems = judgeItems;
    }

    public List<BuffetExamCaseItemEssay> getEssayItems() {
        return essayItems;
    }

    public void setEssayItems(List<BuffetExamCaseItemEssay> essayItems) {
        this.essayItems = essayItems;
    }

    public double getMultiChoiceScore() {
        return multiChoiceScore;
    }

    public void setMultiChoiceScore(double multiChoiceScore) {
        this.multiChoiceScore = multiChoiceScore;
    }

    public double getFillScore() {
        return fillScore;
    }

    public void setFillScore(double fillScore) {
        this.fillScore = fillScore;
    }

    public double getJudgeScore() {
        return judgeScore;
    }

    public void setJudgeScore(double judgeScore) {
        this.judgeScore = judgeScore;
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
        BuffetExamCaseItemCase cq = (BuffetExamCaseItemCase) o;
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
        if (!(object instanceof BuffetExamCaseItemCase)) {
            return false;
        }
        BuffetExamCaseItemCase other = (BuffetExamCaseItemCase) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.exam.model.ExamCaseItemEssay[ id=" + id + " ]";
    }
}
