package com.hjedu.customer.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.platform.service.ILogService;
import com.huajie.seller.dao.ISaleOrderDAO;
import com.huajie.seller.model.SaleOrder;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListSaleOrder implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    ISaleOrderDAO orderDAO = SpringHelper.getSpringBean("SaleOrderDAO");
    List<SaleOrder> orders;
    String type = "all";
    String userId = "";

    public List<SaleOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<SaleOrder> orders) {
        this.orders = orders;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @PostConstruct
    public void init() {
        String uid = JsfHelper.getRequest().getParameter("uid");
        if (uid != null) {
            type = "user";
            userId=uid;
        }
        this.synDB();

    }

    private void synDB() {
        if ("all".equals(type)) {
            this.orders = this.orderDAO.findAllSaleOrder();
        } else if ("user".equals(type)) {
            this.orders = this.orderDAO.findSaleOrderByUser(userId);
        }
    }

    public void delete(String id) {
        SaleOrder cq = this.orderDAO.findSaleOrder(id);
        this.logger.log("删除了订单：" + cq.getName());
        this.orderDAO.deleteSaleOrder(id);
        this.synDB();
    }

    public void batchDelete() {
        for (SaleOrder c : this.orders) {
            if (c.isSelected()) {
                this.logger.log("删除了订单：" + c.getOid());
                this.orderDAO.deleteSaleOrder(c.getId());
            }
        }
        this.synDB();
    }

    public void deleteAll() {
        this.logger.log("删除了所有订单");
        this.orderDAO.deleteSaleOrder(null);
        
    }
    

}
