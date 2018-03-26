/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hjedu.customer.entity;

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

/**
 * 
 * 用户关系表
 * 用户模块
 *
 */
@Entity
@Table(name = "rerebbs_friendship")
public class Friendship implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id=UUID.randomUUID().toString();
    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private BbsUser fromUser;
    @ManyToOne
    @JoinColumn(name = "to_user_id")
    private BbsUser toUser;
    @Column(name = "if_bilateral")
    private boolean ifBilateral=false;
    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genTime=new Date();

    public Friendship() {
    }

    public Friendship(String id) {
        this.id = id;
    }

    public Friendship(String id, Date genTime) {
        this.id = id;
        this.genTime = genTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BbsUser getFromUser() {
        return fromUser;
    }

    public void setFromUser(BbsUser fromUser) {
        this.fromUser = fromUser;
    }

    public BbsUser getToUser() {
        return toUser;
    }

    public void setToUser(BbsUser toUser) {
        this.toUser = toUser;
    }


    public boolean getIfBilateral() {
        return ifBilateral;
    }

    public void setIfBilateral(boolean ifBilateral) {
        this.ifBilateral = ifBilateral;
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
        if (!(object instanceof Friendship)) {
            return false;
        }
        Friendship other = (Friendship) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.rerebbs.model.Friendship[ id=" + id + " ]";
    }
    
}
