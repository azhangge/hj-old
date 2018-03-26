package com.hjedu.platform.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.huajie.ejb.IPSeekerServiceRemote;
import com.huajie.util.SpringHelper;


@ManagedBean
@RequestScoped
public class Index  implements Serializable{

    int i = 5;
    String ss[] = {"li", "tie"};
    String address;
    IPSeekerServiceRemote ipSeeker=SpringHelper.getSpringBean("ipSeekerService");

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String[] getSs() {
        return ss;
    }

    public void setSs(String[] ss) {
        this.ss = ss;
    }


    @PostConstruct
    public void init() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ip=request.getRemoteAddr();
        //this.address=this.ipSeeker.seek(ip);
    }
}
