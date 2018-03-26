package com.hjedu.examination.entity;

import com.google.gson.annotations.Expose;
import com.hjedu.course.entity.*;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.huajie.util.SpringHelper;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
/**
 * 考试模块
 * 分类标签
 */
@Entity
@Table(name = "exam_label")
public class ExamLabel implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @Expose
    private String id = UUID.randomUUID().toString();
    @Column(name = "name")
    @Expose
    private String name;
    @Column(name = "description")
    @Expose
    private String description = "";
    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    private Date genTime = new Date();
    @ManyToOne
    @JoinColumn(name = "type_id")
    private ExamLabelType labelType;
    
    @Column(name = "if_show")
    private boolean ifShow = false;

    @Column(name = "label_info")
    @Expose
    private String labelInfo = "";
    @Column(name = "ord")
    @Expose
    private int ord = 0;
    
    @Column(name = "type_str")
    private String typeStr="";
    
    @Transient
    private boolean selected = false;
    

    public ExamLabel() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExamLabelType getLabelType() {
        return labelType;
    }

    public void setLabelType(ExamLabelType labelType) {
        this.labelType = labelType;
    }

    public String getLabelInfo() {
        return labelInfo;
    }

    public void setLabelInfo(String labelInfo) {
        this.labelInfo = labelInfo;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

   

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isIfShow() {
        return ifShow;
    }

    public void setIfShow(boolean ifShow) {
        this.ifShow = ifShow;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }


    public long checkParticipateTimes(String uid) {
        IExamCaseDAO caseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
        long times = caseDAO.getParticipateNumByExamAndUser(id, uid);
        return times;
    }

    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }

    @Override
    public int compareTo(Object o) {
        ExamLabel ob = (ExamLabel) o;
        if (ob.getOrd() < this.getOrd()) {
            return 1;
        } else if (ob.getOrd() == this.getOrd()) {
            return 0;
        } else {
            return -1;
        }
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
        if (!(object instanceof Lesson)) {
            return false;
        }
        ExamLabel other = (ExamLabel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.exam.model.Examination[ id=" + id + " ]";
    }
}
