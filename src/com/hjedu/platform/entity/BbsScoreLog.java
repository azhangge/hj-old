/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hjedu.platform.entity;

import com.google.gson.annotations.Expose;
import com.hjedu.customer.entity.BbsUser;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * 用户积分记录表
 * 用户模块
 *
 */
@Entity
@Table(name = "rerebbs_score_log")
public class BbsScoreLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @Expose
    private String id=UUID.randomUUID().toString();
    @Lob
    @Column(name = "description")
    @Expose
    private String description;
    @ManyToOne(targetEntity = BbsUser.class, fetch= FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private BbsUser user;
    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    private Date genTime=new Date();
    @Column(name = "score")
    @Expose
    private long score;
    @Column(name = "score_balance")
    @Expose
    private long scoreBalance;

    public BbsScoreLog() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BbsUser getUser() {
        return user;
    }

    public void setUser(BbsUser user) {
        this.user = user;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public long getScoreBalance() {
        return scoreBalance;
    }

    public void setScoreBalance(long scoreBalance) {
        this.scoreBalance = scoreBalance;
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
        if (!(object instanceof BbsScoreLog)) {
            return false;
        }
        BbsScoreLog other = (BbsScoreLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.rerebbs.model.RerebbsScoreLog[ id=" + id + " ]";
    }
    
}
