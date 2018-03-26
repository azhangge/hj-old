package com.hjedu.platform.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpServletRequest;

import com.huajie.util.JsfHelper;

@ManagedBean
@SessionScoped
public class General implements Serializable {

    private String co;
    private String wrongInfo="";

    public String getCo() {
        return co;
    }

    public void setCo(String co) {
        this.co = co;
    }

    public String getWrongInfo() {
        return wrongInfo;
    }

    public void setWrongInfo(String wrongInfo) {
        this.wrongInfo = wrongInfo;
    }
    
    
    
    

    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        String url = request.getRequestURL().toString();
        if (!url.contains("CopyrightError")) {
            //this.checkCopyright();
        }
    }

}
