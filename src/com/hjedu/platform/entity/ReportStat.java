package com.hjedu.platform.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;


public class ReportStat implements Serializable{
    
    private String id=UUID.randomUUID().toString();
    private Date genTime=new Date();
    private String stat;
    private String info="";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    
    
    
    
    
}
