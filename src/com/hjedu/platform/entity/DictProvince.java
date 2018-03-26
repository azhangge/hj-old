package com.hjedu.platform.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 省字典表
 * 用户模块
 *
 */
@Entity
@Table(name = "dict_province")
public class DictProvince implements java.io.Serializable {

    // Fields    
    @Id
    @Column(name = "N_PROVID")
    private Integer NProvid;
    @Column(name = "S_PROVNAME", nullable = false, length = 30)
    private String SProvname;
    @Column(name = "S_TYPE", length = 1)
    private String SType;
    @Column(name = "S_STATE", length = 1)
    private String SState;

    // Constructors
    /**
     * default constructor
     */
    public DictProvince() {
    }

    /**
     * minimal constructor
     */
    public DictProvince(Integer NProvid, String SProvname) {
        this.NProvid = NProvid;
        this.SProvname = SProvname;
    }

    /**
     * full constructor
     */
    public DictProvince(Integer NProvid, String SProvname, String SType, String SState) {
        this.NProvid = NProvid;
        this.SProvname = SProvname;
        this.SType = SType;
        this.SState = SState;
    }

    // Property accessors
    public Integer getNProvid() {
        return this.NProvid;
    }

    public void setNProvid(Integer NProvid) {
        this.NProvid = NProvid;
    }

    public String getSProvname() {
        return this.SProvname;
    }

    public void setSProvname(String SProvname) {
        this.SProvname = SProvname;
    }

    public String getSType() {
        return this.SType;
    }

    public void setSType(String SType) {
        this.SType = SType;
    }

    public String getSState() {
        return this.SState;
    }

    public void setSState(String SState) {
        this.SState = SState;
    }
}