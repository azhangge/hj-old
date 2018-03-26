package com.hjedu.platform.controller;


import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.platform.dao.IOnlinePayConfigDAO;
import com.hjedu.platform.entity.OnlinePayConfig;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAOnlinePayConfig implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    private IOnlinePayConfigDAO infoDAO = SpringHelper.getSpringBean("OnlinePayConfigDAO");
    private OnlinePayConfig info;

    public OnlinePayConfig getInfo() {
        return info;
    }

    public void setInfo(OnlinePayConfig info) {
        this.info = info;
    }

    @PostConstruct
    public void init() {
        this.info = this.infoDAO.findOnlinePayConfig();
    }

    public String apply() {
        this.infoDAO.updateOnlinePayConfig(info);
        JsfHelper.info("应用新的在线支付信息成功！");
        this.logger.log("应用新的在线支付信息");
        return null;
    }
}
