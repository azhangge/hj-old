package com.hjedu.customer.service.impl;

import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.dao.IFinanceLogDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.entity.FinanceLog;
import com.huajie.seller.dao.ISaleOrderDAO;
import com.huajie.seller.model.SaleOrder;

public class FinanceService {

    IFinanceLogDAO financeLogDAO;
    IBbsUserDAO bbsUserDAO;
    ISaleOrderDAO orderDAO;
    private static LinkedBlockingQueue<String> processQueue = new LinkedBlockingQueue();//订单处理队列

    public IFinanceLogDAO getFinanceLogDAO() {
        return financeLogDAO;
    }

    public void setFinanceLogDAO(IFinanceLogDAO financeLogDAO) {
        this.financeLogDAO = financeLogDAO;
    }

    public IBbsUserDAO getBbsUserDAO() {
        return bbsUserDAO;
    }

    public void setBbsUserDAO(IBbsUserDAO bbsUserDAO) {
        this.bbsUserDAO = bbsUserDAO;
    }

    public ISaleOrderDAO getOrderDAO() {
        return orderDAO;
    }

    public void setOrderDAO(ISaleOrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    public void logMoney(double income, double expense, String uid, String str) {

        double gap = expense - income;
        BbsUser bu = this.bbsUserDAO.findBbsUser(uid);
        FinanceLog fl = new FinanceLog();
        fl.setUser(bu);
        fl.setExpense(expense);
        fl.setIncome(income);
        fl.setGenTime(new Date());
        fl.setTransactionAmount(Math.abs(income) + Math.abs(expense));
        fl.setDescription1(str);

        double balanceMoney = bu.getFinanceBalance();
        balanceMoney -= gap;
        fl.setBalance(balanceMoney);
        this.financeLogDAO.addFinanceLog(fl);

        bu.setFinanceBalance(balanceMoney);
        this.bbsUserDAO.updateBbsUser(bu);
    }

    public void processBuyScore(double newMoney, double score, String uid) {

        BbsUser bu = this.bbsUserDAO.findBbsUser(uid);
        double expence = newMoney;
        FinanceLog fl = new FinanceLog();
        fl.setUser(bu);

        fl.setExpense(expence);

        fl.setIncome(0);
        fl.setGenTime(new Date());
        fl.setTransactionAmount(expence);
        fl.setDescription1("购买" + score + "积分");

        double balanceMoney = bu.getFinanceBalance();
        balanceMoney -= expence;
        fl.setBalance(balanceMoney);
        this.financeLogDAO.addFinanceLog(fl);

        bu.setFinanceBalance(balanceMoney);
        this.bbsUserDAO.updateBbsUser(bu);
    }

    public void processOrder(String oid, double newMoney, String uid) {
        if (!FinanceService.processQueue.contains(oid)) {
            processQueue.add(oid);
            SaleOrder so = this.orderDAO.findSaleOrderByOid(oid);
            if (so != null) {
                if (!"processed".equals(so.getFinanceStatus())) {
                    BbsUser bu = this.bbsUserDAO.findBbsUser(uid);
                    double expence = so.getTotalMoney();
                    if ("cash".equals(so.getOrderType())) {
                        //如果是购买商品，消费金额为实额，如果是现金充值，消费金额为0
                        expence = 0;
                    }
                    double gap = expence - newMoney;
                    FinanceLog fl = new FinanceLog();
                    fl.setUser(bu);

                    fl.setExpense(expence);

                    fl.setIncome(newMoney);
                    fl.setGenTime(new Date());
                    fl.setTransactionAmount(expence);
                    fl.setDescription1(so.getName());

                    double balanceMoney = bu.getFinanceBalance();
                    balanceMoney -= gap;
                    fl.setBalance(balanceMoney);
                    this.financeLogDAO.addFinanceLog(fl);

                    bu.setFinanceBalance(balanceMoney);
                    this.bbsUserDAO.updateBbsUser(bu);
                    so.setFinanceStatus("processed");
                    orderDAO.updateSaleOrder(so);
                }
            }
            processQueue.remove(oid);
        }
    }

}
