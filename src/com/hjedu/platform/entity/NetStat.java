/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hjedu.platform.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.*;

/**
 * 
 * 网络状态表
 * 用户模块
 *
 */
@Entity
@Table(name = "y_net_stat")
public class NetStat implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id=UUID.randomUUID().toString();
    @Column(name = "lan_ip")
    private String lanIp;
    @Column(name = "wan_ip")
    private String wanIp;
    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genTime;

    public NetStat() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLanIp() {
        return lanIp;
    }

    public void setLanIp(String lanIp) {
        this.lanIp = lanIp;
    }

    public String getWanIp() {
        return wanIp;
    }

    public void setWanIp(String wanIp) {
        this.wanIp = wanIp;
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
        if (!(object instanceof NetStat)) {
            return false;
        }
        NetStat other = (NetStat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.model.NetStat[ id=" + id + " ]";
    }
    
}
