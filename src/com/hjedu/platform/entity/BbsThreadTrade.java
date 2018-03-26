package com.hjedu.platform.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hjedu.customer.entity.BbsUser;

/**
 * 
 * 用户帖子关联表
 * 用户模块
 *
 */
@Entity
@Table(name = "rerebbs_thread_trade")
public class BbsThreadTrade implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id= UUID.randomUUID().toString();
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "thread_id")
    private BbsThread thread;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user_id")
    private BbsUser user;
    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genTime=new Date();

    public BbsThreadTrade() {
    }

    public BbsThreadTrade(String id) {
        this.id = id;
    }

    public BbsThreadTrade(String id, Date genTime) {
        this.id = id;
        this.genTime = genTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BbsThread getThread() {
        return thread;
    }

    public void setThread(BbsThread thread) {
        this.thread = thread;
    }

    public BbsUser getUser() {
        return user;
    }

    public void setUser(BbsUser user) {
        this.user = user;
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
        if (!(object instanceof BbsThreadTrade)) {
            return false;
        }
        BbsThreadTrade other = (BbsThreadTrade) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.rerebbs.model.BbsThreadTrade[ id=" + id + " ]";
    }
    
}
