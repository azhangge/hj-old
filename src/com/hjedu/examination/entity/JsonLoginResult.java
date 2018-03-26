/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hjedu.examination.entity;

import com.google.gson.annotations.Expose;
import com.hjedu.customer.entity.BbsUser;

import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author huajie
 */
public class JsonLoginResult implements Serializable{
    
    @Expose
    String id=UUID.randomUUID().toString();
    @Expose
    boolean ifPass=true;
    @Expose
    BbsUser user;
    @Expose
    String result="";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isIfPass() {
        return ifPass;
    }

    public void setIfPass(boolean ifPass) {
        this.ifPass = ifPass;
    }

    public BbsUser getUser() {
        return user;
    }

    public void setUser(BbsUser user) {
        this.user = user;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
    
    
    
    
}
