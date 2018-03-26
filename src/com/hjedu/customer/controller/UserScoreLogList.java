package com.hjedu.customer.controller;

import com.google.gson.annotations.Expose;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.dao.IBbsScoreLogDAO;
import com.hjedu.platform.entity.BbsScoreLog;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class UserScoreLogList implements Serializable {

    long totalScore = 0;
    @Expose
    List<BbsScoreLog> logs;
    IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
    IBbsScoreLogDAO logDAO = SpringHelper.getSpringBean("BbsScoreLogDAO");
    BbsUser bu = null;

    public List<BbsScoreLog> getLogs() {
        return logs;
    }

    public void setLogs(List<BbsScoreLog> logs) {
        this.logs = logs;
    }

    public long getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(long totalScore) {
        this.totalScore = totalScore;
    }

    @PostConstruct
    public void init() {
        String uid = JsfHelper.getRequest().getParameter("uid");
        if (uid != null) {
            bu = userDAO.findBbsUser(uid);
        } else {
            ClientSession cs = JsfHelper.getBean("clientSession");
            bu = cs.getUsr();
        }
        this.synDB();
    }
    
    public void initx(String uid) {
        if (uid != null) {
            bu = userDAO.findBbsUser(uid);
        } 
        this.synDB();
    }

    private void synDB() {

        if (bu != null) {
            BbsUser newU = this.userDAO.findBbsUser(bu.getId());
            this.totalScore = newU.getScore();
            logs = this.logDAO.findBbsScoreLogByUsr(bu.getId());
        }
    }

    public String delete(String id) {
        this.logDAO.deleteBbsScoreLog(id);
        if (bu != null) {
            this.synDB();
        }
        return null;
    }

    public String deleteByUsr() {
        ClientSession cs = JsfHelper.getBean("clientSession");
        BbsUser bu = cs.getUsr();
        if (bu != null) {
            this.logDAO.deleteByUsr(bu.getId());
        }
        if (bu != null) {
            this.synDB();
        }
        return null;
    }

}
