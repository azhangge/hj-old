package com.hjedu.platform.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.dao.IBbsScoreRegularDAO;
import com.hjedu.platform.dao.IBbsTalkDAO;
import com.hjedu.platform.dao.IBbsThreadDAO;
import com.hjedu.platform.dao.IBbsZoneDAO;
import com.hjedu.platform.entity.BbsTalk;
import com.hjedu.platform.entity.BbsThread;
import com.hjedu.platform.entity.BbsZone;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.huajie.util.IpHelper;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AddNewThread implements Serializable {

    BbsZone zone = null;
    BbsTalk talk = new BbsTalk();
    BbsThread thread = new BbsThread();
    IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
    IBbsThreadDAO bbsThreadDAO = SpringHelper.getSpringBean("BbsThreadDAO");
    IBbsTalkDAO bbsTalkDAO = SpringHelper.getSpringBean("BbsTalkDAO");
    IBbsZoneDAO bbsZoneDAO = SpringHelper.getSpringBean("BbsZoneDAO");
    IBbsScoreRegularDAO scoreRegularDAO = SpringHelper.getSpringBean("BbsScoreRegularDAO");
    @ManagedProperty(value = "#{clientSession}")
    ClientSession cs;

    public BbsZone getZone() {
        return zone;
    }

    public void setZone(BbsZone zone) {
        this.zone = zone;
    }

    public BbsTalk getTalk() {
        return talk;
    }

    public void setTalk(BbsTalk talk) {
        this.talk = talk;
    }

    public BbsThread getThread() {
        return thread;
    }

    public void setThread(BbsThread thread) {
        this.thread = thread;
    }

    public ClientSession getCs() {
        return cs;
    }

    public void setCs(ClientSession cs) {
        this.cs = cs;
    }

    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        String idt = request.getParameter("zone_id");
        if (idt != null) {
            this.zone = this.bbsZoneDAO.findById(idt);
        }
    }

    public String add_ok() {
        HttpServletRequest request = JsfHelper.getRequest();
        FacesContext fc = FacesContext.getCurrentInstance();
        BbsUser bu = cs.getUsr();
        //bu.setScore(bu.getScore() + this.scoreRegularDAO.findScoreRegular().getNewThread());
        IBbsScoreLogService bsl=SpringHelper.getSpringBean("BbsScoreLogService");
                bsl.log("发表新话题", this.scoreRegularDAO.findScoreRegular().getNewThread());
        this.userDAO.updateBbsUser(bu);

        this.thread.setGenBy(bu);
        this.thread.setGenTime(new Date());
        this.thread.setZone(this.zone);
        this.thread.setReadCount(0);
        this.talk.setGenBy(cs.getUsr());
        this.talk.setGenTime(new Date());
        this.talk.setLastEditTime(new Date());
        this.talk.setThread(this.thread);
        this.talk.setIp(IpHelper.getRemoteAddr(request));
        
        this.thread.setLatestTalk(talk);
        bbsThreadDAO.save(thread);
        //bbsTalkDAO.save(talk);//此talk已被级联保存

        HttpServletResponse response = (HttpServletResponse) fc
                .getExternalContext().getResponse();
        try {
            response.sendRedirect("ThreadList.jspx?id=" + this.zone.getId());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        fc.responseComplete();
        return null;
    }
}
