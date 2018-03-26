package com.hjedu.platform.controller;


import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.platform.dao.ICustomerServiceDAO;
import com.hjedu.platform.entity.CustomerService;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AACustomerService implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    private ICustomerServiceDAO customerServiceDAO = SpringHelper.getSpringBean("CustomerServiceDAO");
    private CustomerService customerService;

    public CustomerService getCustomerService() {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    

    @PostConstruct
    public void init() {
        this.customerService = this.customerServiceDAO.getCustomerService();
    }

    public String apply() {
        this.customerServiceDAO.updateCustomerService(customerService);
        ApplicationBean ab=JsfHelper.getBean("applicationBean");
        ab.setCustomerService(customerService);
        JsfHelper.info("修改客服中心信息成功！");
        this.logger.log("应用新的客服中心信息");
        return null;
    }
}
