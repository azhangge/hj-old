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
 * 系统信息表
 * 用户模块
 *
 */
@Entity
@Table(name = "y_system_info")public class SystemInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id=UUID.randomUUID().toString();
    @Column(name = "site_name")
    private String siteName="华杰云教育系统";
    @Column(name = "site_header")
    private String siteHeader="华杰云教育系统";
    @Column(name = "copyright")//Copyright © 2011-2014 版权所有：华杰工程咨询有限公司
    private String copyright="Copyright ©2016 华杰工程咨询有限公司 版权所有";
    @Column(name = "contact")
    private String contact="E-MAIL:webmaster@huajie.com";    
    @Column(name = "address")
    private String address="华杰工程咨询有限公司";
    
    @Column(name = "email")
    private String email="webmaster@huajie.com";

	@Column(name = "site_back_name")
    private String siteBackName="华杰云教育系统 - 后台管理";
    @Lob
    @Column(name = "key_words")
    private String keyWords;
    @Lob
    @Column(name = "description")
    private String description;
    
    @Column(name = "welcomePage")
    private String welcomePage;
    
    /**
     * 定位地址
     */
    @Column(name="url")
    private String url;
    
    /**
     * 企业ID
     */
    @Column(name = "business_id")
	private String businessId;

    
    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
    
    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public SystemInfo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    
    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteHeader() {
        return siteHeader;
    }

    public void setSiteHeader(String siteHeader) {
        this.siteHeader = siteHeader;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getSiteBackName() {
        return siteBackName;
    }

    public void setSiteBackName(String siteBackName) {
        this.siteBackName = siteBackName;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWelcomePage() {
		return welcomePage;
	}

	public void setWelcomePage(String welcomePage) {
		this.welcomePage = welcomePage;
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
        if (!(object instanceof SystemInfo)) {
            return false;
        }
        SystemInfo other = (SystemInfo) object;
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
