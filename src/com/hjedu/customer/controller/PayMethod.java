package com.hjedu.customer.controller;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.service.impl.FinanceService;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.dao.IOnlinePayConfigDAO;
import com.hjedu.platform.entity.OnlinePayConfig;
import com.hjedu.platform.service.impl.AlipayService;
import com.huajie.seller.dao.ISaleOrderDAO;
import com.huajie.seller.model.SaleOrder;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class PayMethod {
    
    private IOnlinePayConfigDAO onlinePayDAO = SpringHelper.getSpringBean("OnlinePayConfigDAO");
    
    AlipayService aliService = SpringHelper.getSpringBean("AlipayService");
    private String method = "alipay1";
    private OnlinePayConfig config;
    private boolean useBalance = true;
    private SaleOrder order;
    
    public String getMethod() {
        return method;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }
    
    public OnlinePayConfig getConfig() {
        return config;
    }
    
    public SaleOrder getOrder() {
        return order;
    }
    
    public void setOrder(SaleOrder order) {
        this.order = order;
    }
    
    public boolean isUseBalance() {
        return useBalance;
    }
    
    public void setUseBalance(boolean useBalance) {
        this.useBalance = useBalance;
    }
    
    public void setConfig(OnlinePayConfig config) {
        this.config = config;
    }
    
    @PostConstruct
    public void init() {
        //this.onlinePayDAO.findOnlinePayConfig();
        ClientSession cs = JsfHelper.getBean("clientSession");
        BbsUser bu = cs.getUsr();
        
        String uid = JsfHelper.getRequest().getParameter("uid");
        if (uid != null) {
            IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
            bu = userDAO.findBbsUser(uid);
            if (cs != null) {
                cs.setUsr(bu);
            }
        }
        
        if (bu != null) {
            double balance = bu.getFinanceBalance();
            if (balance == 0) {
                this.useBalance = false;
            }
        }
        order = cs.getPayingOrder();
        if ("cash".equals(order.getOrderType())) {
            this.useBalance = false;
        }
        this.reloadPayMoney();
    }
    
    public void reloadPayMoney() {
        ClientSession cs = JsfHelper.getBean("clientSession");
        BbsUser bu = cs.getUsr();
        double balance = bu.getFinanceBalance();
        if (this.useBalance) {
            double total = order.getTotalMoney();
            if (balance < total) {
                
                order.setPayMoney(total - balance);
            } else {
                order.setPayMoney(0);
            }
        } else {
            order.setPayMoney(order.getTotalMoney());
        }
        System.out.println(order.getTotalMoney());
        
    }
    
    public void gotoPay() {
        this.reloadPayMoney();
        ClientSession cs = JsfHelper.getBean("clientSession");
        SaleOrder so = cs.getPayingOrder();
        String path = "";
        
        if (so.getPayMoney() == 0) {
            FinanceService financeService = SpringHelper.getSpringBean("FinanceService");
            ISaleOrderDAO orderDAO = SpringHelper.getSpringBean("SaleOrderDAO");
            so.setStatus("paid");
            orderDAO.updateSaleOrder(so);
            financeService.processOrder(so.getOid(), 0, so.getUser().getId());
            path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/talk/CashPayResult.jspx";
        } else {
            
            String aliReturn = aliService.buildReturnUrl1(JsfHelper.getRequest());
            String aliNotifier = aliService.buildNotifierUrl1(JsfHelper.getRequest());
            
            if ("alipay1".equals(this.method)) {
                path = this.onlinePayDAO.findOnlinePayConfig().getAlipay1Url();
                path = path + "?" + aliService.buildAlipay1Param(so, aliReturn, aliNotifier);
            } else if ("alipay2".equals(this.method)) {
                
            }
        }
        
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        try {
            response.sendRedirect(path);
            System.out.println(path);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
