package com.hjedu.platform.controller;

import java.io.Serializable;
import java.net.InetAddress;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.platform.dao.IIpRuleDAO;
import com.hjedu.platform.entity.IpRule;
import com.hjedu.platform.service.ILogService;
import com.hjedu.platform.service.impl.FireWallService;
import com.huajie.util.IpHelper;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAIpRule implements Serializable {

    IpRule rule;
    IIpRuleDAO ruleDAO = SpringHelper.getSpringBean("IpRuleDAO");
    FireWallService fireService = SpringHelper.getSpringBean("FireWallService");
    private boolean flag = false;
    ILogService logger = SpringHelper.getSpringBean("LogService");

    public IpRule getRule() {
        return rule;
    }

    public void setRule(IpRule rule) {
        this.rule = rule;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @PostConstruct
    public void init() {
        this.rule = new IpRule();
        String idt = JsfHelper.getRequest().getParameter("id");
        if (idt != null) {
            this.rule = this.ruleDAO.findIpRule(idt);
            this.flag = true;
        }
    }

    public String submit_action() {
//验证IP格式
        try {
            byte[] oo = InetAddress.getByName(this.rule.getFromIp()).getAddress();
        } catch (Exception e) {
            JsfHelper.warn("起始IP格式错误！");
            return null;
        }
        try {
            byte[] tt = InetAddress.getByName(this.rule.getToIp()).getAddress();
        } catch (Exception e) {
            JsfHelper.warn("终止IP格式错误！");
            return null;
        }

        if (!IpHelper.compareIp(this.rule.getFromIp(), this.rule.getToIp())) {
            JsfHelper.warn("终止IP不能小于起始IP！");
            return null;
        }

        try {
            if (!this.flag) {
                IpRule n = this.rule;
                this.ruleDAO.addIpRule(n);
                this.logger.log("添加了新的IP过滤规则");
            } else {
                IpRule nn = this.rule;
                this.ruleDAO.updateIpRule(nn);
                this.logger.log("修改了IP过滤规则");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fireService.reBuildCache();
        return "ListIpRule?faces-redirect=true";
    }
}
