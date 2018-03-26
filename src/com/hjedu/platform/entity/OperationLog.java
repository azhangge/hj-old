package com.hjedu.platform.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.*;

import com.hjedu.customer.dao.IAdminDAO;
import com.hjedu.customer.entity.AdminInfo;
import com.huajie.util.SpringHelper;

/**
 * 
 * 操作日志表
 * 用户模块
 *
 */
@Entity
@Table(name = "y_operation_log")
public class OperationLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id=UUID.randomUUID().toString();
    @Column(name = "admin_id")
    private String uid;
    @Lob
    @Column(name = "operation1")
    private String operation1;//operation是保留关键字
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genTime;
    @Transient
    AdminInfo admin;
    @Transient
    boolean selected=false;
    
    @Column(name = "business_id")
	private String businessId;
    
    public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

    public OperationLog() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOperation1() {
        return operation1;
    }

    public void setOperation1(String operation) {
        this.operation1 = operation;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public AdminInfo getAdmin() {
        IAdminDAO adminDAO = SpringHelper.getSpringBean("AdminDAO");
        this.admin = adminDAO.findAdmin(uid);
        return this.admin;
    }

    public void setAdmin(AdminInfo admin) {
        this.admin = admin;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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
        if (!(object instanceof OperationLog)) {
            return false;
        }
        OperationLog other = (OperationLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.model.OperationLog[ id=" + id + " ]";
    }
}
