package com.hjedu.customer.entity;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 用户文件关系表
 * 用户模块
 *
 */
@Entity
@Table(name = "rerebbs_file_share")
public class ShareModel implements java.io.Serializable {

    // Fields
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id = UUID.randomUUID().toString();
    @Column(name = "file_id")
    private String fid;
    @Column(name = "user_id")
    private String uid;

    // Constructors
    /**
     * default constructor
     */
    public ShareModel() {
    }

    /**
     * minimal constructor
     */
    public ShareModel(String id) {
        this.id = id;
    }

    /**
     * full constructor
     */
    public ShareModel(String id, String cid, String uid) {
        this.id = id;
        this.fid = cid;
        this.uid = uid;
    }

    // Property accessors
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFid() {
        return this.fid;
    }

    public void setFid(String cid) {
        this.fid = cid;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
