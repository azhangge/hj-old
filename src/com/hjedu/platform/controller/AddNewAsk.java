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

import com.hjedu.course.dao.IRereAskDAO;
import com.hjedu.course.dao.IRereAskTalkDAO;
import com.hjedu.course.dao.IRereAskZoneDAO;
import com.hjedu.course.entity.RereAsk;
import com.hjedu.course.entity.RereAskTalk;
import com.hjedu.course.entity.RereAskZone;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.dao.IBbsScoreRegularDAO;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.huajie.util.IpHelper;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AddNewAsk implements Serializable {

    RereAskZone zone = null;
    RereAskTalk talk = new RereAskTalk();
    RereAsk ask = new RereAsk();
    IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
    IRereAskDAO bbsAskDAO = SpringHelper.getSpringBean("RereAskDAO");
    IRereAskTalkDAO bbsTalkDAO = SpringHelper.getSpringBean("RereAskTalkDAO");
    IRereAskZoneDAO bbsZoneDAO = SpringHelper.getSpringBean("RereAskZoneDAO");
    IBbsScoreRegularDAO scoreRegularDAO = SpringHelper.getSpringBean("BbsScoreRegularDAO");
    @ManagedProperty(value = "#{clientSession}")
    ClientSession cs;

    public RereAskZone getZone() {
        return zone;
    }

    public void setZone(RereAskZone zone) {
        this.zone = zone;
    }

    public RereAskTalk getTalk() {
        return talk;
    }

    public void setTalk(RereAskTalk talk) {
        this.talk = talk;
    }

    public RereAsk getAsk() {
        return ask;
    }

    public void setAsk(RereAsk ask) {
        this.ask = ask;
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
        //bu.setScore(bu.getScore() + this.scoreRegularDAO.findScoreRegular().getNewAsk());
        IBbsScoreLogService bsl=SpringHelper.getSpringBean("BbsScoreLogService");
        bsl.log("问答悬赏",- this.ask.getScore());//扣除悬赏的积分
        this.userDAO.updateBbsUser(bu);

        this.talk.setIfQuestion(true);
        
        this.ask.setGenBy(bu);
        this.ask.setGenTime(new Date());
        this.ask.setZone(this.zone);
        this.ask.setReadCount(0);
        this.talk.setGenBy(cs.getUsr());
        this.talk.setGenTime(new Date());
        this.talk.setLastEditTime(new Date());
        this.talk.setAsk(this.ask);
        this.talk.setIp(IpHelper.getRemoteAddr(request));
        
        this.ask.setLatestTalk(talk);
        bbsAskDAO.save(ask);
        //bbsTalkDAO.save(talk);//此talk已被级联保存

        HttpServletResponse response = (HttpServletResponse) fc
                .getExternalContext().getResponse();
        try {
            response.sendRedirect("AskList.jspx?id=" + this.zone.getId());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        fc.responseComplete();
        return null;
    }
}
