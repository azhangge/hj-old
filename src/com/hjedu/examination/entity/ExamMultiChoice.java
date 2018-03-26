package com.hjedu.examination.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.*;
/**
 * 习题模块
 * 多选题选项
 * @author h j
 *
 */
@Entity
@Table(name = "e_multi_choice")
public class ExamMultiChoice implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();
    
    @Column(name = "name")
    private String name;
    
    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genTime = new Date();
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "question_id")
    private MultiChoiceQuestion question;
    
    @Column(name = "label")
    private String label;
    
    @Transient
    boolean selected = false;
    
    @Transient
    String labelRendered = null;

    public ExamMultiChoice() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public MultiChoiceQuestion getQuestion() {
        return question;
    }

    public void setQuestion(MultiChoiceQuestion question) {
        this.question = question;
    }

    public String getLabel() {
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
        ExamMultiChoice e = (ExamMultiChoice) o;
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
        if (!(object instanceof ExamMultiChoice)) {
            return false;
        }
        ExamMultiChoice other = (ExamMultiChoice) object;
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
