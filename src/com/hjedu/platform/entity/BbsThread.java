package com.hjedu.platform.entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;
import org.eclipse.persistence.annotations.Noncacheable;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.dao.IBbsThreadDAO;
import com.huajie.util.SpringHelper;
/**
 * 
 * 帖子表
 * 用户模块
 *
 */
@Entity
@Table(name = "rerebbs_thread")
public class BbsThread implements java.io.Serializable, Comparable {

    // Fields
    @Id
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();
    @Lob
    @Column(name = "title")
    private String title;
    @Column(name = "genTime", nullable = false, length = 0)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date genTime;
    @ManyToOne(targetEntity = BbsUser.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "genBy")
    private BbsUser genBy;
    @ManyToOne(targetEntity = BbsZone.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "zone")
    private BbsZone zone;
    @Column(name = "readCount")
    private Integer readCount;
    @Column(name = "top")
    private boolean top = false;
    @Column(name = "if_locked")
    private boolean ifLocked = false;
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "last_talk_id")
    private BbsTalk latestTalk;
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE}, mappedBy = "thread")
    @Noncacheable
    private List<BbsTalk> talks;
    @OneToMany(mappedBy = "thread", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Noncacheable
    List<BbsThreadTrade> trades;
    @OneToMany(mappedBy = "thread", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Noncacheable
    List<BbsThreadBookMark> bookMarks;
    @Column(name = "score")
    int score = 0;
    @Transient
    private long replyCount;

    // Constructors
    /**
     * default constructor
     */
    public BbsThread() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getGenTime() {
        return this.genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public BbsUser getGenBy() {
        return genBy;
    }

    public void setGenBy(BbsUser genBy) {
        this.genBy = genBy;
    }

    public BbsTalk getLatestTalk() {
        return latestTalk;
    }

    public void setLatestTalk(BbsTalk latestTalk) {
        this.latestTalk = latestTalk;
    }

    public BbsZone getZone() {
        return zone;
    }

    public void setZone(BbsZone zone) {
        this.zone = zone;
    }

    public Integer getReadCount() {
        return this.readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public long getReplyCount() {
        IBbsThreadDAO threadDAO = SpringHelper.getSpringBean("BbsThreadDAO");
        this.replyCount = threadDAO.getTalksNumByThread(id) - 1;
        return replyCount;
    }

    public void setReplyCount(long replyCount) {
        this.replyCount = replyCount;
    }

    public List<BbsTalk> getTalks() {
        return talks;
    }

    public void setTalks(List<BbsTalk> talks) {
        this.talks = talks;
    }

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public boolean isIfLocked() {
        return ifLocked;
    }

    public void setIfLocked(boolean ifLocked) {
        this.ifLocked = ifLocked;
    }

    public List<BbsThreadTrade> getTrades() {
        return trades;
    }

    public void setTrades(List<BbsThreadTrade> trades) {
        this.trades = trades;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<BbsThreadBookMark> getBookMarks() {
        return bookMarks;
    }

    public void setBookMarks(List<BbsThreadBookMark> bookMarks) {
        this.bookMarks = bookMarks;
    }

    @Override
    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        BbsThread os = (BbsThread) o;
        return this.latestTalk.getGenTime().compareTo(os.latestTalk.getGenTime());
    }
}