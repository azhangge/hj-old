package com.hjedu.platform.dao;

import java.util.List;

import com.hjedu.platform.entity.BbsMessage;

public abstract interface IBbsMessageDAO {

    public abstract void addMessageModel(BbsMessage paramMessageModel);

    public abstract BbsMessage findMessageModel(String paramString);

    public abstract List<BbsMessage> findAllMessageModel();

    public abstract List<BbsMessage> findMessageModelByReceiver(String paramString);

    public abstract List<BbsMessage> findUnReadedMsgByReceiver(String paramString);

    public abstract long getUnReadedMsgNumByReceiver(String paramString);

    public abstract List<BbsMessage> findMessageModelBySender(String paramString);

    public abstract void deleteMessageModel(String paramString);

    public abstract void receiverMarkDel(String id);

    public abstract void senderMarkDel(String id);

    public abstract void updateMessageReaded(String paramString);

    public void deleteMsgBySender(final String id);

    public void deleteMsgByReceiver(final String id);
}