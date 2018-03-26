package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.bouncycastle.asn1.x509.UserNotice;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.dao.IBbsMessageDAO;
import com.hjedu.platform.dao.INoticeDAO;
import com.hjedu.platform.dao.INoticeUserDAO;
import com.hjedu.platform.entity.BbsMessage;
import com.hjedu.platform.entity.NoticeModel;
import com.hjedu.platform.entity.NoticeUser;
import com.huajie.app.util.StringUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class NoticeUserList  implements Serializable{

    List<NoticeModel> readNoticeModels;
    List<NoticeModel> unreadNoticeModels;
    INoticeUserDAO noticeUserDAO = SpringHelper.getSpringBean("NoticeUserDAO");
    INoticeDAO noticeDAO = SpringHelper.getSpringBean("NoticeDAO");
    ClientSession cs = JsfHelper.getBean("clientSession");
    NoticeModel mm = new NoticeModel();
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

	
	public List<NoticeModel> getReadNoticeModels() {
		return readNoticeModels;
	}

	public void setReadNoticeModels(List<NoticeModel> readNoticeModels) {
		this.readNoticeModels = readNoticeModels;
	}

	public List<NoticeModel> getUnreadNoticeModels() {
		return unreadNoticeModels;
	}

	public void setUnreadNoticeModels(List<NoticeModel> unreadNoticeModels) {
		this.unreadNoticeModels = unreadNoticeModels;
	}

	public NoticeModel getMm() {
        return mm;
    }

    public void setMm(NoticeModel mm) {
        this.mm = mm;
    }

    @PostConstruct
    public void init() {
        synchronizeDB();
    }

    private void synchronizeDB() {
        BbsUser cu = cs.getUsr();
        Map<String,String> orderbymap = new HashMap<String,String>();
		orderbymap.put("createDate", "desc");
		List<NoticeUser> readNoticeList = this.noticeUserDAO.findReadNoticeListByUser(cu.getId(),0,0,orderbymap);
        String readNoticeIdsStr = "";
        if(readNoticeList != null){
        	for(NoticeUser nu:readNoticeList){
        		readNoticeIdsStr = readNoticeIdsStr + ";";
            }
        }
        List<String> readNoticeIds = StringUtil.idsToIdList(readNoticeIdsStr);
        this.readNoticeModels = this.noticeDAO.findNoticeByIds(readNoticeIds);
        
        
        List<NoticeUser> unreadNoticeList = this.noticeUserDAO.findUnreadNoticeListByUser(cu.getId(),0,0,orderbymap);
        String unreadNoticeIdsStr = "";
        if(unreadNoticeList != null){
        	for(NoticeUser nu:unreadNoticeList){
        		unreadNoticeIdsStr = unreadNoticeIdsStr + ";";
            }
        }
        List<String> unreadNoticeIds = StringUtil.idsToIdList(unreadNoticeIdsStr);
        this.unreadNoticeModels = this.noticeDAO.findNoticeByIds(unreadNoticeIds);
    }

    public String senderMarkdel(String id) {
        this.noticeUserDAO.deleteNoticeUser(id);
        synchronizeDB();
        return null;
    }

    public void viewMM(String id) {
        this.mm = this.noticeDAO.findNotice(id);
    }
}
