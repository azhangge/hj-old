package com.hjedu.examination.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.huajie.util.SpringHelper;

import java.io.Serializable;
import javax.persistence.*;
/**
 * 考试模块
 * ？
 * @author h j
 *
 */
@Entity
@Table(name = "exam_case_item_file")
public class ExamCaseItemFile extends GeneralExamCaseItem implements Serializable, Comparable {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "question_id")
    private FileQuestion question;

    @Column(name = "right_ratio")
    private float rightRatio = 0f;

    @Column(name = "remark")
    private String remark = "";

    @Transient
    private boolean attached;
    @Transient
    private boolean uploading = false;

    public ExamCaseItemFile() {
    }

    public FileQuestion getQuestion() {
        return question;
    }

    public void setQuestion(FileQuestion question) {
        this.question = question;
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

    @Override
    public int compareTo(Object o) {
        ExamCaseItemFile cq = (ExamCaseItemFile) o;
        if (this.getQuestion() == null || cq.getQuestion() == null) {
            return 1;
        }
        return this.getQuestion().compareTo(cq.getQuestion());
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
        if (!(object instanceof ExamCaseItemFile)) {
            return false;
        }
        ExamCaseItemFile other = (ExamCaseItemFile) object;
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
