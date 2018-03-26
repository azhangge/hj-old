package com.hjedu.platform.entity;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 用户活跃度统计表
 * 用户模块
 *
 */
@Entity
@Table(name = "rerebbs_score_regular")
public class BbsScoreRegular implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id=UUID.randomUUID().toString();
    @Column(name = "login")
    private Integer login=50;
    @Column(name = "new_thread")
    private Integer newThread=20;
    @Column(name = "new_talk")
    private Integer newTalk=10;
    @Column(name = "top")
    private Integer top=100;
    
    public BbsScoreRegular() {
    }

    public BbsScoreRegular(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getLogin() {
        return login;
    }

    public void setLogin(Integer login) {
        this.login = login;
    }

    public Integer getNewThread() {
        return newThread;
    }

    public void setNewThread(Integer newThread) {
        this.newThread = newThread;
    }

    public Integer getNewTalk() {
        return newTalk;
    }

    public void setNewTalk(Integer newTalk) {
        this.newTalk = newTalk;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
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
        if (!(object instanceof BbsScoreRegular)) {
            return false;
        }
        BbsScoreRegular other = (BbsScoreRegular) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.rerebbs.model.BbsScoreRegular[ id=" + id + " ]";
    }
    
}
