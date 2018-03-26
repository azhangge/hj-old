package com.hjedu.examination.entity;

import com.google.gson.annotations.Expose;
import com.hjedu.platform.entity.PartnerType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.eclipse.persistence.annotations.Noncacheable;

/**
 * 考试模块
 * 考试分类，未用
 * @author h j
 *
 */
@Entity
@Table(name = "exam_label_type")
public class ExamLabelType implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @Expose
    private String id = UUID.randomUUID().toString();
    @Column(name = "name")
    @Expose
    private String name;
    @Column(name = "ord")
    @Expose
    private int ord;
    @Lob
    @Column(name = "decription1")
    @Expose
    private String description1;
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    private Date genTime = new Date();

    @Noncacheable
    @OneToMany(cascade = {CascadeType.REMOVE}, mappedBy = "labelType")
    @Expose
    private List<ExamLabel> labels;

    @Transient
    private List<ExamLabel> examLabels = new ArrayList();
    @Transient
    private List<ExamLabel> buffetLabels = new ArrayList();
    @Transient
    private List<ExamLabel> contestLabels = new ArrayList();
    
    @Transient
    private List<ExamLabel> examLabels2 = new ArrayList();//用于存放当前部门能够访问的考试类别
    @Transient
    private List<ExamLabel> buffetLabels2 = new ArrayList();//用于存放当前部门能够访问的考试类别
    @Transient
    private List<ExamLabel> contestLabels2 = new ArrayList();//用于存放当前部门能够访问的考试类别
    
    @Column(name = "business_id")
    @Expose
    private String businessId;
    
    public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public ExamLabelType() {
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

    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public String getDescription1() {
        return description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public List<ExamLabel> getLabels() {
        Collections.sort(labels);
        return labels;
    }

    public void setLabels(List<ExamLabel> labels) {
        this.labels = labels;
    }

    public List<ExamLabel> getExamLabels() {
        this.examLabels.clear();
        for (ExamLabel el : this.labels) {
            String str = el.getTypeStr();
            if (str != null) {
                if (str.contains("exam")) {
                    this.examLabels.add(el);
                }
            }
        }
        Collections.sort(examLabels);
        return examLabels;
    }

    public void setExamLabels(List<ExamLabel> examLabels) {
        this.examLabels = examLabels;
    }

    public List<ExamLabel> getBuffetLabels() {
        this.buffetLabels.clear();
        for (ExamLabel el : this.labels) {
            String str = el.getTypeStr();
            if (str != null) {
                if (str.contains("buffet")) {
                    this.buffetLabels.add(el);
                }
            }
        }
        Collections.sort(buffetLabels);
        return buffetLabels;
    }

    public void setBuffetLabels(List<ExamLabel> buffetLabels) {
        this.buffetLabels = buffetLabels;
    }

    public List<ExamLabel> getContestLabels() {
        this.contestLabels.clear();
        for (ExamLabel el : this.labels) {
            String str = el.getTypeStr();
            if (str != null) {
                if (str.contains("contest")) {
                    this.contestLabels.add(el);
                }
            }
        }
        Collections.sort(contestLabels);
        return contestLabels;
    }

    public void setContestLabels(List<ExamLabel> contestLabels) {
        this.contestLabels = contestLabels;
    }

    public List<ExamLabel> getExamLabels2() {
        return examLabels2;
    }

    public void setExamLabels2(List<ExamLabel> examLabels2) {
        this.examLabels2 = examLabels2;
    }

    public List<ExamLabel> getBuffetLabels2() {
        return buffetLabels2;
    }

    public void setBuffetLabels2(List<ExamLabel> buffetLabels2) {
        this.buffetLabels2 = buffetLabels2;
    }

    public List<ExamLabel> getContestLabels2() {
        return contestLabels2;
    }

    public void setContestLabels2(List<ExamLabel> contestLabels2) {
        this.contestLabels2 = contestLabels2;
    }

    @Override
    public int compareTo(Object o) {
        ExamLabelType ob = (ExamLabelType) o;
        if (ob.getOrd() < this.getOrd()) {
            return 1;
        } else if (ob.getOrd() == this.getOrd()) {
            return 0;
        } else {
            return -1;
        }
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PartnerType)) {
            return false;
        }
        ExamLabelType other = (ExamLabelType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.model.PartnerType[ id=" + id + " ]";
    }
}
