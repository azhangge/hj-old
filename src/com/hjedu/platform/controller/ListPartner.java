package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.platform.dao.IPartnerDAO;
import com.hjedu.platform.dao.IPartnerTypeDAO;
import com.hjedu.platform.entity.PartnerModel;
import com.hjedu.platform.entity.PartnerType;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListPartner implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    List<PartnerModel> partnerList = new ArrayList();
    List<PartnerType> typeList = new ArrayList();
    PartnerType pt = new PartnerType();
    IPartnerDAO partnerDAO = SpringHelper.getSpringBean("PartnerDAO");
    IPartnerTypeDAO partnerTypeDAO = SpringHelper.getSpringBean("PartnerTypeDAO");
    Map partnerMap = new HashMap();

    public List<PartnerModel> getPartnerList() {
        return this.partnerList;
    }

    public void setPartnerList(List<PartnerModel> partnerList) {
        this.partnerList = partnerList;
    }

    public PartnerType getPt() {
        return pt;
    }

    public void setPt(PartnerType pt) {
        this.pt = pt;
    }

    public List<PartnerType> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<PartnerType> typeList) {
        this.typeList = typeList;
    }

    public Map getPartnerMap() {
        return partnerMap;
    }

    public void setPartnerMap(Map partnerMap) {
        this.partnerMap = partnerMap;
    }

    @PostConstruct
    public void init() {
        synchronizeDB();
        this.synType();
    }

    public void delete(ActionEvent e) {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String id = req.getParameter("id");
        logger.log("删除了友情链接：" + this.partnerDAO.findPartnerModel(id).getName() + ",ID:" + id);
        partnerDAO.deletePartnerModel(id);
        synchronizeDB();
    }

    public String deleteType() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String tid = req.getParameter("tid");
        logger.log("删除了友情链接类型：" + this.partnerTypeDAO.findPartnerType(tid).getName() + ",ID:" + tid);
        partnerTypeDAO.deletePartnerType(tid);
        synType();
        return null;
    }

    public String addType() {
        logger.log("添加了友情链接类型：" + pt.getName() + ",ID:" + pt.getId());
        partnerTypeDAO.addPartnerType(pt);
        this.pt = new PartnerType();
        this.synType();
        return null;
    }

    public String batchUpdateType() {
        for (PartnerType p : this.typeList) {
            partnerTypeDAO.updatePartnerType(p);
        }
        logger.log("批量修改了友情链接类型。");
        this.synType();
        return null;
    }

    public void synType() {
        this.typeList = partnerTypeDAO.findAllPartnerType();
        this.partnerMap.clear();
        for (PartnerType p : typeList) {
            partnerMap.put(p.getId(), p.getName());
        }
    }

    public void synchronizeDB() {
        this.partnerList = partnerDAO.findAllPartnerModel();
    }
    
    public String editOrd(String id) {
        for (PartnerModel cq : this.partnerList) {
            if (id.equals(cq.getId())) {
                this.partnerDAO.updatePartnerModel(cq);
                break;
            }
        }
        return null;
    }
    
}