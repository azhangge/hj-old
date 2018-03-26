package com.hjedu.course.entity;

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
import com.huajie.ejb.impl.IPSeekerService;
import com.huajie.util.HTMLCleaner;
import com.huajie.util.SpringHelper;
/**
 * ？
 * @author h j
 *
 */
@Entity()
@Table(name = "rere_ask_talk")
public class RereAskTalk implements java.io.Serializable {

    // Fields
    @Id
    @Column(name = "id")
    private String id=UUID.randomUUID().toString();
    @Lob
    @Column(name = "content")
    private String content;
    @ManyToOne(targetEntity = RereAsk.class, cascade = {})
    @JoinColumn(name = "ask_id")
    private RereAsk ask;
    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date genTime= new Date();
    @Column(name = "last_edit_time")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastEditTime= new Date();
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gen_by")
    private BbsUser genBy;
    @Column(name = "if_pub")
    private Boolean ifPub = true;
    @Column(name = "ip")
    private String ip;
    @Column(name = "media_url")
    private String mediaUrl="";
    @Column(name = "media_type")
    private String mediaType;
    
    @Column(name = "if_question")
    private boolean ifQuestion=false;//此发言是否为问题
    @Column(name = "if_answer")
    private boolean ifAnswer=true;//此发言是否为回答
    @Column(name = "if_accepted")
    private boolean ifAccepted=false;//此回答是否被采纳
    
    @Column(name = "pros_num")
    private int prosNum;//赞成数
    @Column(name = "cons_num")
    private int consNum;//反对数
    
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
    public RereAskTalk() {
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

    public RereAsk getAsk() {
        return ask;
    }

    public void setAsk(RereAsk ask) {
        this.ask = ask;
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
        IPSeekerService ips = SpringHelper.getSpringBean("ipSeekerService");
        if (ip != null) {
            this.ipAddr = ips.seek(ip);
        }
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public boolean isIfQuestion() {
        return ifQuestion;
    }

    public void setIfQuestion(boolean ifQuestion) {
        this.ifQuestion = ifQuestion;
    }

    public boolean isIfAnswer() {
        return ifAnswer;
    }

    public void setIfAnswer(boolean ifAnswer) {
        this.ifAnswer = ifAnswer;
    }

    public boolean isIfAccepted() {
        return ifAccepted;
    }

    public void setIfAccepted(boolean ifAccepted) {
        this.ifAccepted = ifAccepted;
    }

    public int getProsNum() {
        return prosNum;
    }

    public void setProsNum(int prosNum) {
        this.prosNum = prosNum;
    }

    public int getConsNum() {
        return consNum;
    }

    public void setConsNum(int consNum) {
        this.consNum = consNum;
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