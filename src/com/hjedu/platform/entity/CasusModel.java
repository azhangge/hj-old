package com.hjedu.platform.entity;

import com.google.gson.FieldAttributes;
import com.google.gson.annotations.Expose;
import com.hjedu.customer.entity.AdminInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
/**
 * 
 * 系统公告表
 * 用户模块
 * 
 */
@Entity
@Table(name = "y_casus")
public class CasusModel implements Serializable, Comparable {

    @Id
    @Column(name = "id")
    @Expose
    private String id = UUID.randomUUID().toString();
    @Lob
    @Column(name = "title")
    @Expose
    private String title;
    @Lob
    @Column(name = "content")
    @Expose
    private String content;
    @Column(name = "ord")
    private int ord = 0;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "inputdate")
    @Expose
    private Date inputdate = new Date();
    @ManyToOne
    @JoinColumn(name = "admin_id")
    AdminInfo admin;
    @Column(name = "count")
    @Expose
    private long count = 0;
    @Transient
    boolean selected = false;
    
    @Column(name = "business_id")
	private String businessId;
    
    public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getInputdate() {
        
        return inputdate;
    }

    public void setInputdate(Date inputdate) {
        this.inputdate = inputdate;
    }

    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AdminInfo getAdmin() {
        return admin;
    }

    public void setAdmin(AdminInfo admin) {
        this.admin = admin;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int compareTo(Object o) {
        CasusModel ob = (CasusModel) o;
        if (ob.getOrd() > this.getOrd()) {
            return 1;
        } else if (ob.getOrd() == this.getOrd()) {
            return 0;
        } else {
            return -1;
        }
    }

}
