package com.hjedu.platform.service;

import java.util.List;

import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.platform.entity.BbsMessage;

public interface IMessageService {

    public List<BbsMessage> findBbsMessageByReceiver(String uid);

    public void setMessageReaded(String id);

    public void sendMessage(String uid, String title, String content);

    public void sendMessageByAdmin(String uid, String title, String content);

    public void sendMessage(String rec, String sender, String title, String content);

	void sendMessageToAllUsers(String bussinessId, String title, String content);
}
