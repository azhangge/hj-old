package com.hjedu.customer.service.impl;

import java.io.Serializable;
import java.util.Date;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.dao.IBbsScoreLogDAO;
import com.hjedu.platform.entity.BbsScoreLog;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

public class BbsScoreLogService implements IBbsScoreLogService, Serializable {

    IBbsScoreLogDAO olDAO;
    IBbsUserDAO userDAO=SpringHelper.getSpringBean("BbsUserDAO");
    
    public IBbsScoreLogDAO getOlDAO() {
        return olDAO;
    }

    public void setOlDAO(IBbsScoreLogDAO olDAO) {
        this.olDAO = olDAO;
    }

    @Override
    public void log(String str, long score) {
        if (score == 0) {
            return;
        }
        try {
            ClientSession ab = JsfHelper.getBean("clientSession");
            if(ab!=null&&ab.getUsr()!=null){
            	log(str, score, ab.getUsr());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void log(String str, long score, BbsUser bu) {
        if (score == 0) {
            return;
        }
        try {
            BbsUser user=userDAO.findBbsUser(bu.getId());
            updateUserScore(score, user);
            createLog(str, score, user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void log(String str, int score) {
        long s=score;
        this.log(str, s);
    }

    @Override
    public void log(String str, int score, BbsUser bu) {
        long s=score;
        this.log(str, s, bu);
    }
    
    private void createLog(String str, long score, BbsUser user){
    	BbsScoreLog log = new BbsScoreLog();
        log.setDescription(str);
        log.setUser(user);
        log.setScore(score);
        log.setScoreBalance(user.getScore());
        log.setGenTime(new Date());
        olDAO.addBbsScoreLog(log);
    }
    
    //除了购买课程或者后天修改用户积分，目前其他操作不改变用户积分
    private void updateUserScore (long score, BbsUser user){
        long newScore=user.getScore()+score;
        user.setScore(newScore);
        userDAO.updateBbsUser(user);
    }
}
