package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.platform.dao.INoticeDAO;
import com.hjedu.platform.entity.NoticeModel;
import com.huajie.util.CookieUtils;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListNotice implements Serializable {

    List<NoticeModel> noticeList = new ArrayList();
    INoticeDAO noticeDAO = SpringHelper.getSpringBean("NoticeDAO");
    NoticeModel noticeModel;

    public List<NoticeModel> getNoticeList() {
        return this.noticeList;
    }

    public void setNoticeList(List<NoticeModel> noticeList) {
        this.noticeList = noticeList;
    }

    public INoticeDAO getNoticeDAO() {
        return noticeDAO;
    }

    public void setNoticeDAO(INoticeDAO noticeDAO) {
        this.noticeDAO = noticeDAO;
    }

    public NoticeModel getNotice() {
        return noticeModel;
    }

    public void setNotice(NoticeModel noticeModel) {
        this.noticeModel = noticeModel;
    }

    @PostConstruct
    public void init() {
        synchronizeDB();
    }

    private void synchronizeDB() {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        this.noticeList = this.noticeDAO.findAllNoticesByBusinessId(businessId);
//        AdminInfo ai = ExternalUserUtil.getAdminBySession();
//        this.casusList = this.casusDAO.findCasusesByAdmin(ai.getId());
    }

    public void deleteNotice(String idt) {
        if (idt != null) {
            this.noticeDAO.deleteNotice(idt);
        }
        synchronizeDB();
    }

    public String viewNotice(String id) {
    	noticeModel = this.noticeDAO.findNotice(id);
        return null;
    }
    
    public String editOrd(String id) {
        for (NoticeModel cq : this.noticeList) {
            if (id.equals(cq.getId())) {
                this.noticeDAO.updateNotice(cq);
                break;
            }
        }
        return null;
    }
}
