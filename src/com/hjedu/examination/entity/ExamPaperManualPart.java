package com.hjedu.examination.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.eclipse.persistence.annotations.Noncacheable;

import com.hjedu.examination.entity.contest.ContestCaseItemAdapter;


/**
 * 试卷模块
 * 人工试卷中的大题
 * ManualPartItem为大题中的小的条目，记录了人工试卷大题中的基础试题，建立了关联关系
 * @author www.wbdwl.com
 */
@Entity
@Table(name = "exam_paper_manual_part")
public class ExamPaperManualPart implements Serializable ,Comparable{
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();
    @Column(name = "name")
    private String name;
    @Column(name = "ord")
    private int ord=0;
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genTime=new Date();
    @Noncacheable
    @OneToMany(cascade = {CascadeType.ALL},mappedBy = "part",fetch = FetchType.LAZY,orphanRemoval = true)
    List<ManualPartItem> items;
    @ManyToOne
    @JoinColumn(name="paper_id")
    private ExamPaperManual paper;
    @Column(name = "if_show_name")
    private boolean ifShowName=false;
    @Column(name = "item_num")
    private int itemNum=0;
    @Column(name = "total_score")
    private double totalScore=0.0;
    
    @Transient
    List<ExamCaseItemAdapter> adapters;
    
    @Transient
    List<ContestCaseItemAdapter> cadapters;
    
    

    public ExamPaperManualPart() {
    }

    public ExamPaperManualPart(String id) {
        this.id = id;
    }

    public ExamPaperManualPart(String id, Date genTime) {
        this.id = id;
        this.genTime = genTime;
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

    public boolean isIfShowName() {
        return ifShowName;
    }

    public void setIfShowName(boolean ifShowName) {
        this.ifShowName = ifShowName;
    }
    
    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExamPaperManual getPaper() {
        return paper;
    }

    public void setPaper(ExamPaperManual paper) {
        this.paper = paper;
    }

    public List<ManualPartItem> getItems() {
        Collections.sort(items);
        return items;
    }

    public void setItems(List<ManualPartItem> items) {
        this.items = items;
    }

    public List<ExamCaseItemAdapter> getAdapters() {
        return adapters;
    }

    public void setAdapters(List<ExamCaseItemAdapter> adapters) {
        this.adapters = adapters;
    }

    public int getItemNum() {
        return itemNum;
    }

    public void setItemNum(int itemNum) {
        this.itemNum = itemNum;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public List<ContestCaseItemAdapter> getCadapters() {
        return cadapters;
    }

    public void setCadapters(List<ContestCaseItemAdapter> cadapters) {
        this.cadapters = cadapters;
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
        if (!(object instanceof ExamPaperManualPart)) {
            return false;
        }
        ExamPaperManualPart other = (ExamPaperManualPart) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    public int compareTo(Object o) {
        ExamPaperManualPart cq = (ExamPaperManualPart) o;
        if (this.getOrd() == cq.getOrd()) {
            return this.getId().compareTo(cq.getId());
        } else {
            return this.getOrd() - cq.getOrd();
        }
    }

    @Override
    public String toString() {
        return "com.huajie.exam.model.ExamPaperManual[ id=" + id + " ]";
    }
    
}
