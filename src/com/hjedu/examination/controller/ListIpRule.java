package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.platform.dao.IIpRuleDAO;
import com.hjedu.platform.entity.IpRule;
import com.hjedu.platform.service.impl.FireWallService;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListIpRule implements Serializable {

    List<IpRule> ruleList = new ArrayList();
    IIpRuleDAO ruleDAO = SpringHelper.getSpringBean("IpRuleDAO");
    FireWallService fireService = SpringHelper.getSpringBean("FireWallService");
    IpRule rule;

    public List<IpRule> getRuleList() {
        return this.ruleList;
    }

    public void setRuleList(List<IpRule> ruleList) {
        this.ruleList = ruleList;
    }

    

    public IpRule getRule() {
        return rule;
    }

    public void setRule(IpRule rule) {
        this.rule = rule;
    }

    @PostConstruct
    public void init() {
        synchronizeDB();
    }

    private void synchronizeDB() {
        this.ruleList = this.ruleDAO.findAllIpRule();
    }

    public void deleteRule(String idt) {
        if (idt != null) {
            this.ruleDAO.deleteIpRule(idt);
        }
        synchronizeDB();
        fireService.reBuildCache();
    }

    public String viewRule(String id) {
        rule = this.ruleDAO.findIpRule(id);
        return null;
    }
}
