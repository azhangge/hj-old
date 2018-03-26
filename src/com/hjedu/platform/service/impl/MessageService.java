package com.hjedu.platform.service.impl;

import java.util.List;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.dao.IBbsMessageDAO;
import com.hjedu.platform.entity.BbsMessage;
import com.hjedu.platform.service.IMessageService;
import com.huajie.util.JsfHelper;

public class MessageService implements IMessageService {

    IBbsMessageDAO msgDAO;
    IBbsUserDAO userDAO;

    public IBbsMessageDAO getMsgDAO() {
        return msgDAO;
    }

    public void setMsgDAO(IBbsMessageDAO msgDAO) {
        this.msgDAO = msgDAO;
    }

    public IBbsUserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(IBbsUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public List<BbsMessage> findBbsMessageByReceiver(String uid) {
        return msgDAO.findMessageModelByReceiver(uid);
    }

    @Override
    public void setMessageReaded(String id) {
        msgDAO.updateMessageReaded(id);
    }

    @Override
    public void sendMessage(String uid, String title, String content) {
        BbsMessage mm = new BbsMessage();
        mm.setReaded(false);
        mm.setTitle(title);
        mm.setContent(content);
        mm.setReceiver(this.userDAO.findBbsUser(uid));
        ClientSession cs = JsfHelper.getBean("clientSession");
        BbsUser user = null;
        if (cs == null) {
            user=userDAO.findSysUser();
        } else {
            user = cs.getUsr();
        }
        mm.setSender(user);
        this.msgDAO.addMessageModel(mm);
    }

    @Override
    public void sendMessageByAdmin(String uid, String title, String content) {
        BbsMessage mm = new BbsMessage();
        mm.setReaded(false);
        mm.setTitle(title);
        mm.setContent(content);
        mm.setReceiver(this.userDAO.findBbsUser(uid));
        mm.setSender(this.userDAO.findSysUser());
        this.msgDAO.addMessageModel(mm);
    }

    @Override
    public void sendMessage(String rec, String sender, String title, String content) {
        BbsMessage mm = new BbsMessage();
        mm.setReaded(false);
        mm.setTitle(title);
        mm.setContent(content);
        mm.setReceiver(this.userDAO.findBbsUser(rec));
        mm.setSender(this.userDAO.findBbsUser(sender));
        this.msgDAO.addMessageModel(mm);
    }

	@Override
	public void sendMessageToAllUsers(String bussinessId, String title, String content) {
		List<BbsUser> users = userDAO.findAllBbsUser(bussinessId);
		for(BbsUser us : users){
			BbsMessage mm = new BbsMessage();
	        mm.setReaded(false);
	        mm.setTitle(title);
	        mm.setContent(content);
	        mm.setReceiver(us);
	        mm.setSender(userDAO.findSysUser());
	        this.msgDAO.addMessageModel(mm);
		}
	}
}
