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
import org.eclipse.persistence.annotations.Noncacheable;
/**
 * 习题模块
 * 习题类型
 * @author h j
 *
 */
@Entity
@Table(name = "knowledge_label_type")
public class KnowledgeLabelType implements Serializable, Comparable {

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
    private List<KnowledgeLabel> labels;


    public KnowledgeLabelType() {
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

    public List<KnowledgeLabel> getLabels() {
        Collections.sort(labels);
        return labels;
    }

    public void setLabels(List<KnowledgeLabel> labels) {
        this.labels = labels;
    }

    
    
    @Override
    public int compareTo(Object o) {
        KnowledgeLabelType ob = (KnowledgeLabelType) o;
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
        KnowledgeLabelType other = (KnowledgeLabelType) object;
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
