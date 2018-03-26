/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hjedu.customer.entity;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *外部用户表，暂时未用到
 * @author huajie
 */
@Entity
@Table(name = "external_user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExternalUser.findAll", query = "SELECT e FROM ExternalUser e"),
    @NamedQuery(name = "ExternalUser.findById", query = "SELECT e FROM ExternalUser e WHERE e.id = :id"),
    @NamedQuery(name = "ExternalUser.findByTellerNo", query = "SELECT e FROM ExternalUser e WHERE e.tellerNo = :tellerNo"),
    @NamedQuery(name = "ExternalUser.findByTellerType", query = "SELECT e FROM ExternalUser e WHERE e.tellerType = :tellerType"),
    @NamedQuery(name = "ExternalUser.findByUserKindNo", query = "SELECT e FROM ExternalUser e WHERE e.userKindNo = :userKindNo"),
    @NamedQuery(name = "ExternalUser.findByTellerName", query = "SELECT e FROM ExternalUser e WHERE e.tellerName = :tellerName"),
    @NamedQuery(name = "ExternalUser.findByCardStat", query = "SELECT e FROM ExternalUser e WHERE e.cardStat = :cardStat"),
    @NamedQuery(name = "ExternalUser.findByAccessCode", query = "SELECT e FROM ExternalUser e WHERE e.accessCode = :accessCode"),
    @NamedQuery(name = "ExternalUser.findBySysFlag", query = "SELECT e FROM ExternalUser e WHERE e.sysFlag = :sysFlag")})
public class ExternalUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id=UUID.randomUUID().toString();
    @Column(name = "TELLER_NO")
    private String tellerNo;
    @Column(name = "TELLER_TYPE")
    private String tellerType;
    @Column(name = "USER_KIND_NO")
    private String userKindNo;
    @Column(name = "TELLER_NAME")
    private String tellerName;
    @Column(name = "CARD_STAT")
    private String cardStat;
    @Column(name = "ACCESS_CODE")
    private String accessCode;
    @Column(name = "SYS_FLAG")
    private String sysFlag;

    public ExternalUser() {
    }

    public ExternalUser(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTellerNo() {
        return tellerNo;
    }

    public void setTellerNo(String tellerNo) {
        this.tellerNo = tellerNo;
    }

    public String getTellerType() {
        return tellerType;
    }

    public void setTellerType(String tellerType) {
        this.tellerType = tellerType;
    }

    public String getUserKindNo() {
        return userKindNo;
    }

    public void setUserKindNo(String userKindNo) {
        this.userKindNo = userKindNo;
    }

    public String getTellerName() {
        return tellerName;
    }

    public void setTellerName(String tellerName) {
        this.tellerName = tellerName;
    }

    public String getCardStat() {
        return cardStat;
    }

    public void setCardStat(String cardStat) {
        this.cardStat = cardStat;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getSysFlag() {
        return sysFlag;
    }

    public void setSysFlag(String sysFlag) {
        this.sysFlag = sysFlag;
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
        if (!(object instanceof ExternalUser)) {
            return false;
        }
        ExternalUser other = (ExternalUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.exam.web.mb.external.ExternalUser[ id=" + id + " ]";
    }
    
}
