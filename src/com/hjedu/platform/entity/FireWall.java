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
 * 防火墙配置表
 * 用户模块
 *
 */
@Entity
@Table(name = "y_fire_wall")
public class FireWall implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id=UUID.randomUUID().toString();
    @Column(name = "if_in_use")
    private boolean ifInUse=true;
    @Column(name = "if_black")
    private boolean ifBlack=true;
    @Column(name = "port")
    private int port=80;

    public FireWall() {
    }

    public FireWall(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isIfInUse() {
        return ifInUse;
    }

    public void setIfInUse(boolean ifInUse) {
        this.ifInUse = ifInUse;
    }

    public boolean isIfBlack() {
        return ifBlack;
    }

    public void setIfBlack(boolean ifBlack) {
        this.ifBlack = ifBlack;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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
        if (!(object instanceof FireWall)) {
            return false;
        }
        FireWall other = (FireWall) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.model.FireWall[ id=" + id + " ]";
    }
    
}
