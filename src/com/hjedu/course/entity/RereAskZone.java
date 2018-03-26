package com.hjedu.course.entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;
import org.eclipse.persistence.annotations.Noncacheable;

import com.hjedu.course.dao.IRereAskDAO;
import com.hjedu.course.dao.IRereAskZoneDAO;
import com.huajie.util.SpringHelper;
/**
 * ？
 * @author h j
 *
 */
@Entity
@Table(name = "rere_ask_zone")
public class RereAskZone implements java.io.Serializable {

    // Fields
    @Id
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();
    @Column(name = "name")
    private String name;
    @Lob
    @Column(name = "description")
    private String description;
    @Column(name = "gen_time", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date genTime = new Date();
    @Column(name = "order_index")
    private Integer orderIndex = 0;
    @Column(name = "if_only_mag")
    private boolean ifOnlyMag = false;
    @Column(name = "check_user")
    private boolean checkUser = false;
    @Lob
    @Column(name = "group_str")
    private String groupStr="";
    
    @OneToMany(mappedBy = "zone", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @Noncacheable
    List<RereAsk> asks;
    
    @Transient
    private long askCount;
    @Transient
    private RereAsk lastestAsk;

    // Constructors
    /**
     * default constructor
     */
    public RereAskZone() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getGenTime() {
        return this.genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public Integer getOrderIndex() {
        return this.orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getGroupStr() {
        return groupStr;
    }

    public void setGroupStr(String groupStr) {
        this.groupStr = groupStr;
    }

    public List<RereAsk> getAsks() {
        return asks;
    }

    public void setAsks(List<RereAsk> asks) {
        this.asks = asks;
    }

    public boolean isIfOnlyMag() {
        return ifOnlyMag;
    }

    public void setIfOnlyMag(boolean ifOnlyMag) {
        this.ifOnlyMag = ifOnlyMag;
    }

    public long getAskCount() {
        IRereAskDAO threadDAO = SpringHelper.getSpringBean("RereAskDAO");
        askCount = threadDAO.findByZone(id).size();
        return askCount;
    }

    public void setAskCount(long threadCount) {
        this.askCount = threadCount;
    }

    public RereAsk getLastestAsk() {
        IRereAskZoneDAO zoneDAO = SpringHelper.getSpringBean("RereAskZoneDAO");
        lastestAsk = zoneDAO.findLatestAsk(id);
        return lastestAsk;
    }

    public void setLastestAsk(RereAsk lastestThread) {
        this.lastestAsk = lastestThread;
    }


    public boolean getCheckUser() {
        return checkUser;
    }

    public void setCheckUser(boolean checkUser) {
        this.checkUser = checkUser;
    }
    
    
    
}