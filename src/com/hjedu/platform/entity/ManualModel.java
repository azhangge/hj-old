package com.hjedu.platform.entity;


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

import com.hjedu.customer.entity.AdminInfo;

/**
 * 
 * 用户手册表
 * 用户模块
 *
 */
@Entity
@Table(name = "y_manual")
public class ManualModel implements Serializable,Comparable{

    @Id
    @Column(name = "id")
    private String id=String.valueOf(System.nanoTime());
    @Lob
    @Column(name = "title")
    private String title;
    @Lob
    @Column(name = "content")
    private String content;
    @Column(name = "ord")
    private int ord=0;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "inputdate", length = 0)
    private Date inputdate;
    @ManyToOne
    @JoinColumn(name="admin_id")
    AdminInfo admin;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }

    public AdminInfo getAdmin() {
        return admin;
    }

    public void setAdmin(AdminInfo admin) {
        this.admin = admin;
    }
    @Override
    public int compareTo(Object o) {
        ManualModel ob = (ManualModel) o;
        if (ob.getOrd() > this.getOrd()) {
            return 1;
        } else if (ob.getOrd() == this.getOrd()) {
            return 0;
        } else {
            return -1;
        }
    }
    
}
