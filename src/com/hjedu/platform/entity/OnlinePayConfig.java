package com.hjedu.platform.entity;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 
 * 支付宝配置表
 * 用户模块
 *
 */
@Entity
@Table(name = "y_online_pay_config")public class OnlinePayConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id=UUID.randomUUID().toString();
    @Column(name = "alipay1_url")
    private String alipay1Url="";
    @Column(name = "alipay1_partner")
    private String alipay1Partner="";
    @Column(name = "alipay1_key")
    private String alipay1Key="";
    @Column(name = "alipay_type")
    private String alipayType="";
    @Column(name = "alipay_seller_email")
    private String alipaySellerEmail="";
    @Lob
    @Column(name = "description")
    private String description;
    @Column(name = "score_per_yuan")
    private double scorePerYuan=10;

    public OnlinePayConfig() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlipay1Url() {
        return alipay1Url;
    }

    public void setAlipay1Url(String alipay1Url) {
        this.alipay1Url = alipay1Url;
    }

    public String getAlipay1Partner() {
        return alipay1Partner;
    }

    public void setAlipay1Partner(String alipay1Partner) {
        this.alipay1Partner = alipay1Partner;
    }

    public String getAlipay1Key() {
        return alipay1Key;
    }

    public void setAlipay1Key(String alipay1Key) {
        this.alipay1Key = alipay1Key;
    }

    public String getAlipaySellerEmail() {
        return alipaySellerEmail;
    }

    public void setAlipaySellerEmail(String alipaySellerEmail) {
        this.alipaySellerEmail = alipaySellerEmail;
    }

    public String getAlipayType() {
        return alipayType;
    }

    public void setAlipayType(String alipayType) {
        this.alipayType = alipayType;
    }

    public double getScorePerYuan() {
        return scorePerYuan;
    }

    public void setScorePerYuan(double scorePerYuan) {
        this.scorePerYuan = scorePerYuan;
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
        if (!(object instanceof OnlinePayConfig)) {
            return false;
        }
        OnlinePayConfig other = (OnlinePayConfig) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.model.SystemInfo[ id=" + id + " ]";
    }
    
}
