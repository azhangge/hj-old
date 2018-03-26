

package com.hjedu.platform.entity;

import java.io.Serializable;
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
 * IP规则表
 * 用户模块
 *
 */
@Entity
@Table(name = "y_ip_rule")

public class IpRule implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id=UUID.randomUUID().toString();
    @Basic(optional = false)
    @Column(name = "begain_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date begainTime=new Date();
    @Basic(optional = false)
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime=new Date();
    @Column(name = "from_ip")
    private String fromIp;
    @Column(name = "to_ip")
    private String toIp;
    @Column(name = "if_in_use")
    private boolean ifInUse=true;
    @Column(name = "wall_type")
    private String wallType="white";//valid value is white or black
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "gen_time", length = 0)
    private Date genTime=new Date();

    public IpRule() {
    }

    public IpRule(String id) {
        this.id = id;
    }

    public IpRule(String id, Date begainTime, Date endTime) {
        this.id = id;
        this.begainTime = begainTime;
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getBegainTime() {
        return begainTime;
    }

    public void setBegainTime(Date begainTime) {
        this.begainTime = begainTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getFromIp() {
        return fromIp;
    }

    public void setFromIp(String fromIp) {
        this.fromIp = fromIp;
    }

    public String getToIp() {
        return toIp;
    }

    public void setToIp(String toIp) {
        this.toIp = toIp;
    }

    public boolean isIfInUse() {
        return ifInUse;
    }

    public void setIfInUse(boolean ifInUse) {
        this.ifInUse = ifInUse;
    }

    
    public String getWallType() {
        return wallType;
    }

    public void setWallType(String wallType) {
        this.wallType = wallType;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
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
        if (!(object instanceof IpRule)) {
            return false;
        }
        IpRule other = (IpRule) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.model.IpRule[ id=" + id + " ]";
    }
    
}
