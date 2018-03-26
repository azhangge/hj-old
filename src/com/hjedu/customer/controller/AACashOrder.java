package com.hjedu.customer.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.service.ILogService;
import com.huajie.seller.dao.ISaleOrderDAO;
import com.huajie.seller.model.SaleOrder;
import com.huajie.seller.model.SaleOrderItem;
import com.huajie.util.JsfHelper;
import com.huajie.util.RereRandom;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AACashOrder implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    ISaleOrderDAO orderDAO = SpringHelper.getSpringBean("SaleOrderDAO");
    IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
    SaleOrder cq;

    boolean flag = false;

    double money=100d;


    //SaleOrder newOrder;
    public SaleOrder getCq() {
        return cq;
    }

    public void setCq(SaleOrder cq) {
        this.cq = cq;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        String id = request.getParameter("id");
        if (id != null) {
            this.flag = true;
            this.cq = this.orderDAO.findSaleOrder(id);
        } else {
            this.cq = new SaleOrder();
            String oid = RereRandom.fetchOrderId();
            cq.setOid(oid);
            ClientSession cs = JsfHelper.getBean("clientSession");
            cq.setUser(cs.getUsr());
        }

    }

    public String reloadTotalMoney() {
        double mm = money;
        cq.setTotalMoney(mm);
        return null;
    }

    public String done() {
        //构造订单名称与订单内容
        cq.setOrderType("cash");
        cq.setBody("充值");
        cq.setName("充值");
        this.reloadTotalMoney();
        if (flag) {
            this.orderDAO.updateSaleOrder(cq);
        } else {
            this.orderDAO.addSaleOrder(cq);
        }
        return "SaleOrderList?faces-redirect=true";
    }

    public String doneAndPay() {

        this.done();
        ClientSession cs = JsfHelper.getBean("clientSession");
        cs.setPayingOrder(cq);
        return "PayMethod?faces-redirect=true";
    }

}
