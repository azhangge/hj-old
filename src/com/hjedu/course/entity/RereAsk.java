package com.hjedu.course.entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;
import org.eclipse.persistence.annotations.Noncacheable;

import com.hjedu.course.dao.IRereAskDAO;
import com.hjedu.customer.entity.BbsUser;
import com.huajie.util.SpringHelper;
/**
 * ？
 * @author h j
 *
 */
@Entity
@Table(name = "rere_ask")
public class RereAsk implements java.io.Serializable, Comparable {

    // Fields
    @Id
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();
    @Column(name = "title", length = 200)
    private String title;
    @Column(name = "gen_time", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date genTime;
    @ManyToOne(targetEntity = BbsUser.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "gen_by")
    private BbsUser genBy;
    @ManyToOne(targetEntity = RereAskZone.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "zone")
    private RereAskZone zone;
    @Column(name = "readCount")
    private Integer readCount;
    @Column(name = "top")
    private boolean top = false;
    @Column(name = "if_locked")
    private boolean ifLocked = false;
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "last_talk_id")
    private RereAskTalk latestTalk;
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE}, mappedBy = "ask")
    @Noncacheable
    private List<RereAskTalk> talks;
    
    @Column(name = "score")
    int score = 0;//悬赏积分
    @Transient
    private long replyCount;

    // Constructors
    /**
     * default constructor
     */
    public RereAsk() {
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

    public RereAskTalk getLatestTalk() {
        return latestTalk;
    }

    public void setLatestTalk(RereAskTalk latestTalk) {
        this.latestTalk = latestTalk;
    }

    public RereAskZone getZone() {
        return zone;
    }

    public void setZone(RereAskZone zone) {
        this.zone = zone;
    }

    public Integer getReadCount() {
        return this.readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public long getReplyCount() {
        IRereAskDAO threadDAO = SpringHelper.getSpringBean("RereAskDAO");
        this.replyCount = threadDAO.getTalksNumByAsk(id) - 1;
        return replyCount;
    }

    public void setReplyCount(long replyCount) {
        this.replyCount = replyCount;
    }

    public List<RereAskTalk> getTalks() {
        return talks;
    }

    public void setTalks(List<RereAskTalk> talks) {
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        RereAsk os = (RereAsk) o;
        return this.latestTalk.getGenTime().compareTo(os.latestTalk.getGenTime());
    }
}