package com.hjedu.common.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

/**
 * 主表信息表
 */
@Entity
@Table(name = "A_MASTERTABLE")
public class MasterTable implements Serializable{
	
	@Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
	
    @Column(name = "atable")
    @Expose
	private String aTable;
    
    @Column(name = "akey")
    @Expose
	private String aKey;
    
    @Column(name = "status")
    @Expose
	private int status;
    
    @Column(name = "remark")
    @Expose
	private String remark;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getaTable() {
		return aTable;
	}

	public void setaTable(String aTable) {
		this.aTable = aTable;
	}

	public String getaKey() {
		return aKey;
	}

	public void setaKey(String aKey) {
		this.aKey = aKey;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
