package com.hjedu.examination.entity.buffet;

import com.google.gson.annotations.Expose;
import com.hjedu.examination.dao.IBuffetExaminationPartDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.entity.FileQuestion;
import com.huajie.util.SpringHelper;

import java.io.Serializable;
import javax.persistence.*;
/**
 * 练习模块
 * 文件题记录
 * @author h j
 *
 */
@Entity
@Table(name = "b_exam_case_item_file")
public class BuffetExamCaseItemFile extends BuffetGeneralExamCaseItem implements Serializable, Comparable {

    @ManyToOne
    @JoinColumn(name = "question_id")
    private FileQuestion question;
    @Lob
    @Column(name = "user_answer")
    @Expose
    private String userAnswer = "";
    @Column(name = "right_ratio")
    @Expose
    private float rightRatio = 0f;
    @Lob
    @Column(name = "right_answer")
    @Expose
    private String rightAnswer = "";
    @Column(name = "remark")
    @Expose
    private String remark = "";
    @Column(name = "marked")
    @Expose
    private boolean marked = false;
    @Column(name = "score")
    @Expose
    private double score = 5;
    @Column(name = "part_id")
    @Expose
    private String partId;

    @Transient
    private BuffetExaminationPart part;
    @Transient
    private boolean attached;
    @Transient
    private boolean uploading = false;

    public BuffetExamCaseItemFile() {
    }

    public FileQuestion getQuestion() {
        return question;
    }

    public void setQuestion(FileQuestion question) {
        this.question = question;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public float getRightRatio() {
        //System.out.println("ratio:"+rightRatio);
        long rt = (long) (rightRatio * 1000L);
        //System.out.println("now:"+rt);
        rightRatio = ((float) rt) / 1000.0F;
        //System.out.println("after:"+rightRatio);
        return rightRatio;
    }

    public void setRightRatio(float rightRatio) {
        this.rightRatio = rightRatio;
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

    public boolean isAttached() {
        IFileQuestionDAO fileDAO = SpringHelper.getSpringBean("FileQuestionDAO");
        attached = fileDAO.checkIfAttached(id);
        return attached;
    }

    public void setAttached(boolean attached) {
        this.attached = attached;
    }

    public boolean isUploading() {
        return uploading;
    }

    public void setUploading(boolean uploading) {
        this.uploading = uploading;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        BuffetExamCaseItemFile cq = (BuffetExamCaseItemFile) o;
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
        if (!(object instanceof BuffetExamCaseItemFile)) {
            return false;
        }
        BuffetExamCaseItemFile other = (BuffetExamCaseItemFile) object;
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
