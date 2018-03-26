package com.hjedu.businessuser.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.annotations.Expose;

/**
 * 企业用户
 * @author LENOVO
 *
 */
@Entity
@Table(name = "business_user")
public class BusinessUser implements Serializable {

	private static final long serialVersionUID = 1L;
	
    @Id
    @Basic(optional = false)
    @Column(name = "id",length = 200)
    private String id = UUID.randomUUID().toString();
	
    /**
	 * 创建时间
	 */
	@Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    protected Date createTime = new Date();
	
	/**
	 * 最后修改时间
	 */
	@Column(name = "last_modify_time")
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    protected Date lastModifyTime = new Date();
	
	/**
	 * 逻辑删除
	 */
    @Column(name = "delete_flag")
    @Expose
    protected int deleteFlag;
    
    /**
     * 法人
     */
    @Column(name = "legal_person")
    @Expose
    protected String legalPerson;
    
    /**
     * 企业名称
     */
    @Column(name = "business_name_cn")
    @Expose
    protected String businessNameCn;
    
    
    @Column(name = "domain_name")
    @Expose
    protected String domainName;

    @Column(name = "is_open")
    @Expose
    private Boolean isOpen = false;
    
    @Column(name = "has_admin")
    @Expose
    private Boolean hasAdmin = false;
    
    @Column(name = "admin_id")
    @Expose
    private String adminId;
    
    @Column(name = "is_default")
    @Expose
    private String isDefault = "0";
    
    @Column(name = "sys_pic")
    @Expose
    private String sysPic;

    @Column(name = "port")
    @Expose
    private String port;
    
    @Column(name = "sort")
    @Expose
    private int sort;

    @Column(name = "login_logo")
    @Expose
    private Boolean loginLogo = false;
        
	public Boolean getLoginLogo() {
		return loginLogo;
	}

	public void setLoginLogo(Boolean loginLogo) {
		this.loginLogo = loginLogo;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getSysPic() {
		return sysPic;
	}

	public void setSysPic(String sysPic) {
		this.sysPic = sysPic;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public Boolean getHasAdmin() {
		return hasAdmin;
	}

	public void setHasAdmin(Boolean hasAdmin) {
		this.hasAdmin = hasAdmin;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public Boolean getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Boolean isOpen) {
		this.isOpen = isOpen;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public int getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getLegalPerson() {
		return legalPerson;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}

	public String getBusinessNameCn() {
		return businessNameCn;
	}

	public void setBusinessNameCn(String businessNameCn) {
		this.businessNameCn = businessNameCn;
	}
    
}
