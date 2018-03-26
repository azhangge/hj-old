package com.hjedu.platform.entity;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 
 * 友情链接表
 * 用户模块
 *
 */
@Entity
@Table(name = "y_partner")
public class PartnerModel implements Serializable {

    @Id
    @Column(name = "id")
    private String id=UUID.randomUUID().toString();
    @Column(name = "name")
    private String name;
    @Lob
    @Column(name = "url")
    private String url;
    @Lob
    @Column(name = "description")
    private String description;
    @Column(name = "type_id")
    private String typeId;
    @Column(name = "ord")
    private int ord;

    public PartnerModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

  

    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }
}
