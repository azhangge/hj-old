package com.hjedu.platform.entity;

import java.util.Date;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import com.hjedu.customer.entity.BbsUser;
import com.huajie.ejb.IPSeekerServiceRemote;
import com.huajie.util.HTMLCleaner;
import com.huajie.util.SpringHelper;
/**
 * 
 * 用户留言表
 * 用户模块
 *
 */
@Entity()
@Table(name = "rerebbs_talk")
public class BbsTalk implements java.io.Serializable {

    // Fields
    @Id
    @Column(name = "id")
    private String id=UUID.randomUUID().toString();
    @Lob
    @Column(name = "content")
    private String content;
    @ManyToOne(targetEntity = BbsThread.class, cascade = {})
    @JoinColumn(name = "threadId")
    private BbsThread thread;
    @Basic(optional = false)
    @Column(name = "genTime")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date genTime= new Date();
    @Column(name = "lastEditTime")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastEditTime= new Date();
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "genBy")
    private BbsUser genBy;
    @Column(name = "ifPub")
    private Boolean ifPub = true;
    @Column(name = "ip")
    private String ip;
    @Column(name = "media_url")
    private String mediaUrl="";
    @Column(name = "media_type")
    private String mediaType;
    @Transient
    private boolean ifMedia=false;
    @Transient
    private String ipAddr;
    @Transient
    private String cleanContent;

    // Constructors
    /**
     * default constructor
     */
    public BbsTalk() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BbsThread getThread() {
        return thread;
    }

    public void setThread(BbsThread thread) {
        this.thread = thread;
    }

    public Date getGenTime() {
        return this.genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public Date getLastEditTime() {
        return this.lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public BbsUser getGenBy() {
        return genBy;
    }

    public void setGenBy(BbsUser genBy) {
        this.genBy = genBy;
    }

    public Boolean getIfPub() {
        return this.ifPub;
    }

    public void setIfPub(Boolean ifPub) {
        this.ifPub = ifPub;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCleanContent() {
        cleanContent = HTMLCleaner.delHTMLTag(this.content);
        return cleanContent;
    }

    public void setCleanContent(String cleanContent) {
        this.cleanContent = cleanContent;
    }

    public String getIpAddr() {
        IPSeekerServiceRemote ips = SpringHelper.getSpringBean("ipSeekerService");
        if (ip != null) {
            this.ipAddr = ips.seek(ip);
        }
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public boolean isIfMedia() {
        if (this.mediaUrl != null) {
            if (!"".equals(this.mediaUrl.trim())) {
                this.ifMedia = true;
            }
        }
        return ifMedia;
    }

    public void setIfMedia(boolean ifMedia) {
        this.ifMedia = ifMedia;
    }
}