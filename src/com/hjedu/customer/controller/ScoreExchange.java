package com.hjedu.customer.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.service.impl.FinanceService;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.dao.IBbsScoreLogDAO;
import com.hjedu.platform.dao.IOnlinePayConfigDAO;
import com.hjedu.platform.entity.OnlinePayConfig;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ScoreExchange implements Serializable {

    IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
    IOnlinePayConfigDAO configDAO = SpringHelper.getSpringBean("OnlinePayConfigDAO");
    FinanceService finService = SpringHelper.getSpringBean("FinanceService");

    OnlinePayConfig config;

    boolean flag = false;

    long score = 100L;
    double money = 0d;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public OnlinePayConfig getConfig() {
        return config;
    }

    public void setConfig(OnlinePayConfig config) {
        this.config = config;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    @PostConstruct
    public void init() {
        config = this.configDAO.findOnlinePayConfig();
        this.reloadMoney();
    }

    public String reloadMoney() {

        this.money = ((double) score) / config.getScorePerYuan();
        BigDecimal bg = new BigDecimal(money);
        money = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return null;
    }

    public String exchange() {
        this.reloadMoney();
        ClientSession cs = JsfHelper.getBean("clientSession");
        String id = cs.getUsr().getId();
        BbsUser bu = this.userDAO.findBbsUser(id);

        if (score <= 0) {
            JsfHelper.error("积分不能小于等于0！");
            return null;
        }

        if (bu.getFinanceBalance() < money) {
            JsfHelper.error("帐户余额不够！");
            return null;
        }

        IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
        bsl.log("积分兑换", score);

        this.finService.processBuyScore(money, score, id);

        cs.refreshUser();
        this.score = 100;
        this.reloadMoney();
        JsfHelper.info("积分兑换成功！");
        return null;
    }

}
