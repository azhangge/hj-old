package com.hjedu.platform.entity;

import java.util.Date;
import java.util.UUID;
import javax.persistence.*;

import com.hjedu.customer.entity.BbsUser;
/**
 * 
 * 用户站内信
 * 用户模块
 *
 */
@Entity
@Table(name = "rerebbs_message")
public class BbsMessage implements java.io.Serializable{

    @Id
    @Column(name = "id")
    private String id=UUID.randomUUID().toString();
    @Column(name = "title")
    String title;
    @Column(name = "content")
    String content;
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    BbsUser receiver;
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    BbsUser sender;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "gen_time", nullable = false)
    Date genTime = new Date();
    @Column(name = "readed")
    boolean readed = false;
    @Column(name = "message_type")
    String messageType;
    @Column(name = "receiver_mark_del")
    boolean receiverMarkDel = false;
    @Column(name = "sender_mark_del")
    boolean senderMarkDel = false;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public boolean isReaded() {
        return readed;
    }

    public void setReaded(boolean readed) {
        this.readed = readed;
    }

    public BbsUser getReceiver() {
        return receiver;
    }

    public void setReceiver(BbsUser receiver) {
        this.receiver = receiver;
    }

    public BbsUser getSender() {
        return sender;
    }

    public void setSender(BbsUser sender) {
        this.sender = sender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public boolean isReceiverMarkDel() {
        return receiverMarkDel;
    }

    public void setReceiverMarkDel(boolean receiverMarkDel) {
        this.receiverMarkDel = receiverMarkDel;
    }

    public boolean isSenderMarkDel() {
        return senderMarkDel;
    }

    public void setSenderMarkDel(boolean senderMarkDel) {
        this.senderMarkDel = senderMarkDel;
    }
}
