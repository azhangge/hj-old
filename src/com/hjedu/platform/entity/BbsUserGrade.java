
package com.hjedu.platform.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * 用户等级表
 * 用户模块
 *
 */
@Entity
@Table(name = "rerebbs_user_grade")
public class BbsUserGrade implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id=UUID.randomUUID().toString();
    @Column(name = "begain_score")
    private long begainScore;
    @Column(name = "end_score")
    private long endScore;
    @Column(name = "grade_name")
    private String gradeName;
    @Column(name = "description")
    private String description;
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genTime;

    public BbsUserGrade() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public long getBegainScore() {
        return begainScore;
    }

    public void setBegainScore(long begainScore) {
        this.begainScore = begainScore;
    }

    public long getEndScore() {
        return endScore;
    }

    public void setEndScore(long endScore) {
        this.endScore = endScore;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(object instanceof BbsUserGrade)) {
            return false;
        }
        BbsUserGrade other = (BbsUserGrade) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.rerebbs.model.BbsUserGrade[ id=" + id + " ]";
    }
    
}
