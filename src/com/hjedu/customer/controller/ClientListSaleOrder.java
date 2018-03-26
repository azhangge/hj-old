package com.hjedu.customer.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.service.ILogService;
import com.huajie.seller.dao.ISaleOrderDAO;
import com.huajie.seller.model.SaleOrder;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ClientListSaleOrder implements Serializable {

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
        type = JsfHelper.getRequest().getParameter("type");
        this.synDB(type);

    }

    private void synDB(String type) {
        ClientSession cs = JsfHelper.getBean("clientSession");
        if (cs != null) {
            BbsUser bu = cs.getUsr();
            if (bu != null) {
                if ("finished".equals(type)) {
                    this.orders = this.orderDAO.findFinishedSaleOrderByUser(bu.getId());
                } else if ("unfinished".equals(type)) {
                    this.orders = this.orderDAO.findUnFinishedSaleOrderByUser(bu.getId());
                } else {
                    this.orders = this.orderDAO.findSaleOrderByUser(bu.getId());
                }
            }
        }

    }

    public String payOrder(String id) {
        SaleOrder cq = this.orderDAO.findSaleOrder(id);
        ClientSession cs = JsfHelper.getBean("clientSession");
        if (cs != null) {
            cs.setPayingOrder(cq);
        }
        return "PayMethod?faces-redirect=true";
    }

    public void delete(String id) {
        SaleOrder cq = this.orderDAO.findSaleOrder(id);
        this.orderDAO.deleteSaleOrder(id);
        this.synDB(type);
    }

    public void batchDelete() {
        for (SaleOrder c : this.orders) {
            if (c.isSelected()) {
                this.orderDAO.deleteSaleOrder(c.getId());
            }
        }
        this.synDB(type);
    }

    public void deleteAll() {
        this.orderDAO.deleteSaleOrder(null);

    }

}
