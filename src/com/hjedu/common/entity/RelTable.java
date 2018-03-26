package com.hjedu.common.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

/**
 * 从表信息表
 */
@Entity
@Table(name = "A_RELTABLE")
public class RelTable {
	@Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;

    @Column(name = "atable")
    @Expose
	private String aTable;
	
    @Column(name = "ftable")
    @Expose
	private String fTable;
	
    @Column(name = "fkey")
    @Expose
	private String fKey;
    
    @Column(name = "dataType")
    @Expose
	private String dataType;
    
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

	public String getfTable() {
		return fTable;
	}

	public void setfTable(String fTable) {
		this.fTable = fTable;
	}

	public String getfKey() {
		return fKey;
	}

	public void setfKey(String fKey) {
		this.fKey = fKey;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
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
