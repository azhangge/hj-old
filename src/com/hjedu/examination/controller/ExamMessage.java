
package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.UUID;


public class ExamMessage implements Serializable{
    
    
    
    String id=UUID.randomUUID().toString();
    String urn="";
    String dateStr="";
    String examId="";
    boolean ifPass=false;
    String ip="";
    boolean ifSubmited=false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public boolean isIfPass() {
        return ifPass;
    }

    public void setIfPass(boolean ifPass) {
        this.ifPass = ifPass;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isIfSubmited() {
        return ifSubmited;
    }

    public void setIfSubmited(boolean ifSubmited) {
        this.ifSubmited = ifSubmited;
    }
    
    
    
    
    
    
}
