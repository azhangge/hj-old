package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.dao.IBbsScoreRegularDAO;
import com.hjedu.platform.dao.IBbsThreadDAO;
import com.hjedu.platform.dao.IBbsZoneDAO;
import com.hjedu.platform.entity.BbsThread;
import com.hjedu.platform.entity.BbsZone;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

/**
 * @author huajie
 *
 */
@ManagedBean
@ViewScoped
public class ThreadList implements Serializable {

    BbsZone zone = null;
    List<BbsThread> threads = new ArrayList();
    IBbsThreadDAO bbsThreadDAO = SpringHelper.getSpringBean("BbsThreadDAO");
    IBbsScoreRegularDAO scoreRegularDAO = SpringHelper.getSpringBean("BbsScoreRegularDAO");
    IBbsZoneDAO bbsZoneDAO = SpringHelper.getSpringBean("BbsZoneDAO");
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

    public BbsZone getZone() {
        return zone;
    }

    public void setZone(BbsZone zone) {
        this.zone = zone;
    }

    public List<BbsThread> getThreads() {
        return threads;
    }

    public void setThreads(List<BbsThread> threads) {
        this.threads = threads;
    }

    @PostConstruct
    public void init() {
        //MyLogger.echo("constructor:" + System.nanoTime());
        String temp = request.getParameter("id");
        //Cat.println("I'm here!");
        if (temp != null) {
            this.zone = bbsZoneDAO.findById(temp.trim());
            this.synDB();
        }
        this.checkCheat();
    }

    public String checkCheat() {
        //String result = "ZoneList?faces-redirect=true";
        boolean cheat = false;
        if (this.zone != null) {
            ClientSession cs = JsfHelper.getBean("clientSession");
            BbsUser bu = cs.getUsr();
            if (bu == null && zone.getCheckUser()) {
                cheat = true;
            }
            if (bu != null && zone.getCheckUser()) {
                if (!bu.testIfIn(zone.getGroupStr())) {
                    cheat = true;
                }
            }

            if (cheat) {
                HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                try {
                    HttpServletRequest request1 = JsfHelper.getRequest();
                    String url = request1.getContextPath() + "/talk/ZoneList.jspx";
                    //MyLogger.echo(url);
                    response.sendRedirect(url);
                } catch (Exception e) {
                }
            }
        }
        return null;
    }

    public void synDB() {
        this.threads = bbsThreadDAO.findByZone(this.zone.getId());

    }

    public void top(String id) {
        this.bbsThreadDAO.topThread(id);
        IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
        ClientSession cs = JsfHelper.getBean("clientSession");
        BbsUser bu = cs.getUsr();
        //bu.setScore(bu.getScore() + this.scoreRegularDAO.findScoreRegular().getTop());
        IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
        bsl.log("贴子被置顶", this.scoreRegularDAO.findScoreRegular().getTop());
        //userDAO.updateBbsUser(bu);

        this.synDB();
    }

    public void lock(String id) {
        this.bbsThreadDAO.lockThread(id);
        this.synDB();
    }

    public void unlock(String id) {
        this.bbsThreadDAO.unlockThread(id);
        this.synDB();
    }

    public void unTop(String id) {
        this.bbsThreadDAO.unTopThread(id);
        this.synDB();
    }

    public void delete(String id) {
        this.bbsThreadDAO.delete(id);
        this.synDB();
    }
}
