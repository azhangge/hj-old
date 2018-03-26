package com.hjedu.platform.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.json.JSONObject;

import com.hjedu.customer.UserUtil;
import com.hjedu.customer.controller.ViewClientUser;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.service.impl.FinanceService;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.hjedu.platform.service.ILogService;
import com.huajie.corss.util.Conn;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ScoreOperation implements Serializable {
    
    ILogService logger = SpringHelper.getSpringBean("LogService");
    IBbsUserDAO BbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
    IBbsScoreLogService logService = SpringHelper.getSpringBean("BbsScoreLogService");
    FinanceService financeService = SpringHelper.getSpringBean("FinanceService");
    ISystemConfigDAO scDAO = SpringHelper.getSpringBean("SystemConfigDAO");
    String constants_sub_id=this.scDAO.getSystemConfig().getSub_id();
    String yun_host=this.scDAO.getSystemConfig().getYun_host();
    BbsUser cu;
    String scoreOperator = "plus";
    String moneyOperator = "plus";
    long score = 0;
    double money = 0;
    String uid = "";
    
    public BbsUser getCu() {
        return cu;
    }
    
    public void setCu(BbsUser cu) {
        this.cu = cu;
    }
    
    public String getScoreOperator() {
        return scoreOperator;
    }
    
    public void setScoreOperator(String scoreOperator) {
        this.scoreOperator = scoreOperator;
    }
    
    public long getScore() {
        return score;
    }
    
    public void setScore(long score) {
        this.score = score;
    }
    
    public String getMoneyOperator() {
        return moneyOperator;
    }
    
    public void setMoneyOperator(String moneyOperator) {
        this.moneyOperator = moneyOperator;
    }
    
    public double getMoney() {
        return money;
    }
    
    public void setMoney(double money) {
        this.money = money;
    }
    
    public String getUid() {
        return uid;
    }
    
    public void setUid(String uid) {
        this.uid = uid;
    }
    
    @PostConstruct
    public void init() {
        String idt = JsfHelper.getRequest().getParameter("id");
        if (idt != null) {
            uid = idt;
        }
        this.cu = this.BbsUserDAO.findBbsUser(uid);
//        this.synUser();
    }
    
    public void synUser() {
        this.cu = this.BbsUserDAO.findBbsUser(uid);
        JSONObject jsongetUserScore=Conn.getUserScore(yun_host,this.cu.getExternalId());
		String statusgetUserScore=(String) jsongetUserScore.get("status");
		if(statusgetUserScore.equals("1")){
			this.cu.setScore(Long.valueOf((String) jsongetUserScore.get("score")));
		}
    }
    
    public void synUser2(long netAdd) {
//    	this.cu = UserUtil.getBbsUser(uid);
    	if(this.cu.getScore()+netAdd>=0){
    		this.cu.setScore(this.cu.getScore()+netAdd);
    	}else{
    		this.cu.setScore(0);
    	}
    }
    
    public void operateScore() {
    	ViewClientUser cv = JsfHelper.getBean("viewClientUser");
        long netAdd = 0;
        if (score < 0) {
            JsfHelper.error("积分不能为负");
            return;
        }
        if (!"plus".equalsIgnoreCase(scoreOperator)) {
            score = -score;
            netAdd = score;
        } else {
            netAdd = score;
        }
//        this.logService.log("管理员加减积分", score, cu);
//       logger.log("为用户：" + cu.getUsername() + " 增加了积分：" + netAdd);
        this.synUser2(netAdd);
        score = 0;
        
        
        if (cv != null) {
            cv.getCu().setScore(this.cu.getScore());
        }
    }
    
    public void operateMoney() {
        double netAdd = 0;
        double income = 0;
        double expense = 0;
        if (money < 0) {
            JsfHelper.error("金额不能为负");
            return;
        }
        if ("plus".equalsIgnoreCase(moneyOperator)) {
            income = money;
        } else {
            expense = money;
        }
        netAdd = income - expense;
        this.financeService.logMoney(income, expense, cu.getId(), "管理员加减金额");
        logger.log("为用户：" + cu.getUsername() + " 增加了金额：" + netAdd);
        this.synUser();
        money = 0;
        
        ViewClientUser cv = JsfHelper.getBean("viewClientUser");
        if (cv != null) {
            cv.getCu().setFinanceBalance(this.cu.getFinanceBalance());
        }
    }
    
}
