package com.hjedu.examination.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.hjedu.examination.dao.IExamChoiceStatisticDAO;
import com.huajie.util.SpringHelper;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.*;
/**
 * 习题模块
 * 单选题题目选项
 * @author h j
 *
 */
@Entity
@Table(name = "e_choice")
public class ExamChoice implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();
    
    @Column(name = "name")
    private String name;
    
    @JsonIgnore
    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genTime = new Date();
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "question_id")
    private ChoiceQuestion question;
    
    @Column(name = "label")
    private String label;

    @JsonIgnore
    @Transient
    long selectNum = 0;

    @JsonIgnore
    @Transient
    boolean selected = false;
    @Transient
    
    String labelRendered = null;

    public ExamChoice() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void fetchSelectNum(String examId) {//用于获取当前考试此选项的选择数量
        IExamChoiceStatisticDAO sDAO = SpringHelper.getSpringBean("ExamChoiceStatisticDAO");
        selectNum = sDAO.getNumByChoiceAndExam(id, examId);
    }

    public long getSelectNum() {

        return selectNum;
    }

    public void setSelectNum(long selectNum) {
        this.selectNum = selectNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public ChoiceQuestion getQuestion() {
        return question;
    }

    public void setQuestion(ChoiceQuestion question) {
        this.question = question;
    }

    public String getLabel() {
        if (label != null) {
            label = label.toUpperCase().trim();
        }
        return label;
    }

    public void setLabel(String label) {
        this.label = label.toUpperCase().trim();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getLabelRendered() {
        if ((labelRendered == null) || "".equals(labelRendered)) {
            labelRendered = this.label;
        }
        return labelRendered;
    }

    public void setLabelRendered(String labelRendered) {
        this.labelRendered = labelRendered;
    }

    @Override
    public int compareTo(Object o) {
        ExamChoice e = (ExamChoice) o;
        String lable1 = this.getLabelRendered().trim();
        String lable2 = e.getLabelRendered().trim();
        return lable1.compareToIgnoreCase(lable2);
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
        if (!(object instanceof ExamChoice)) {
            return false;
        }
        ExamChoice other = (ExamChoice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.exam.model.ExamChoice[ id=" + id + " ]";
    }
}
