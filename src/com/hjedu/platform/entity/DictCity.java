package com.hjedu.platform.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 城市字典表
 * 用户模块
 *
 */
@Entity
@Table(name = "dict_city")
public class DictCity implements java.io.Serializable {

    // Fields    
    @Id
    @Column(name = "N_CITYID")
    private Integer NCityid;
    @Column(name = "S_CITYNAME", nullable = false, length = 30)
    private String SCityname;
    @Column(name = "N_PROVID", nullable = false)
    private Integer NProvid;
    @Column(name = "S_STATE", length = 1)
    private String SState;

    // Constructors
    /**
     * default constructor
     */
    public DictCity() {
    }

    /**
     * minimal constructor
     */
    public DictCity(Integer NCityid, String SCityname, Integer NProvid) {
        this.NCityid = NCityid;
        this.SCityname = SCityname;
        this.NProvid = NProvid;
    }

    /**
     * full constructor
     */
    public DictCity(Integer NCityid, String SCityname, Integer NProvid, String SState) {
        this.NCityid = NCityid;
        this.SCityname = SCityname;
        this.NProvid = NProvid;
        this.SState = SState;
    }

    // Property accessors
    public Integer getNCityid() {
        return this.NCityid;
    }

    public void setNCityid(Integer NCityid) {
        this.NCityid = NCityid;
    }

    public String getSCityname() {
        return this.SCityname;
    }

    public void setSCityname(String SCityname) {
        this.SCityname = SCityname;
    }

    public Integer getNProvid() {
        return this.NProvid;
    }

    public void setNProvid(Integer NProvid) {
        this.NProvid = NProvid;
    }

    public String getSState() {
        return this.SState;
    }

    public void setSState(String SState) {
        this.SState = SState;
    }
}