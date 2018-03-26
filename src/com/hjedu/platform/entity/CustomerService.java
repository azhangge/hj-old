/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * 客服表
 * 用户模块
 *
 */
@Entity
@Table(name = "y_customer_service")
public class CustomerService implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id=UUID.randomUUID().toString();
    @Column(name = "position1")
    private String position1;
    @Column(name = "top1")
    private int top1=200;
    @Column(name = "width1")
    private int width1=160;
    @Column(name = "color1")
    private String color1="gray";
    @Column(name = "style1")
    private String style1;
    @Column(name = "effect1")
    private String effect1="true";
    @Column(name = "default_open")
    private String defaultOpen="false";
    @Column(name = "tel")
    private String tel;
    @Column(name = "qq_list")
    private String qqList;
    @Column(name = "qun_list")
    private String qunList;
    @Column(name = "ww_list")
    private String wwList;

    public CustomerService() {
    }

    public CustomerService(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosition1() {
        return position1;
    }

    public void setPosition1(String position1) {
        this.position1 = position1;
    }

    public int getTop1() {
        return top1;
    }

    public void setTop1(int top1) {
        this.top1 = top1;
    }

    public String getColor1() {
        return color1;
    }

    public void setColor1(String color1) {
        this.color1 = color1;
    }

    public int getWidth1() {
        return width1;
    }

    public void setWidth1(int width1) {
        this.width1 = width1;
    }

    public String getStyle1() {
        return style1;
    }

    public void setStyle1(String style1) {
        this.style1 = style1;
    }

    public String getEffect1() {
        return effect1;
    }

    public void setEffect1(String effect1) {
        this.effect1 = effect1;
    }

    public String getDefaultOpen() {
        return defaultOpen;
    }

    public void setDefaultOpen(String defaultOpen) {
        this.defaultOpen = defaultOpen;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getQqList() {
        return qqList;
    }

    public void setQqList(String qqList) {
        this.qqList = qqList;
    }

    public String getQunList() {
        return qunList;
    }

    public void setQunList(String qunList) {
        this.qunList = qunList;
    }

    public String getWwList() {
        return wwList;
    }

    public void setWwList(String wwList) {
        this.wwList = wwList;
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
        if (!(object instanceof CustomerService)) {
            return false;
        }
        CustomerService other = (CustomerService) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.model.CustomerService[ id=" + id + " ]";
    }
    
}
