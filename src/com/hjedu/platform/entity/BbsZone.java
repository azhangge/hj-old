package com.hjedu.platform.entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;
import org.eclipse.persistence.annotations.Noncacheable;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.dao.IBbsThreadDAO;
import com.hjedu.platform.dao.IBbsZoneDAO;
import com.huajie.util.SpringHelper;
/**
 * 
 * 论坛板块表
 * 用户模块
 *
 */
@Entity
@Table(name = "rerebbs_zone")
public class BbsZone implements java.io.Serializable {

    // Fields
    @Id
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "genTime", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date genTime = new Date();
    @Column(name = "orderIndex")
    private Integer orderIndex = 0;
    @Column(name = "if_only_mag")
    private boolean ifOnlyMag = false;
    @Column(name = "check_user")
    private boolean checkUser = false;
    @Column(name = "group_str",length = 3600)
    private String groupStr="";
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "rerebbs_zonemag",
            joinColumns = {
        @JoinColumn(name = "zone_id", referencedColumnName = "id")},
            inverseJoinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "id")})
    private List<BbsUser> managers;
    @OneToMany(mappedBy = "zone", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @Noncacheable
    List<BbsThread> threads;
    @Transient
    private String magsStr;
    @Transient
    private long threadCount;
    @Transient
    private BbsThread lastestThread;

    // Constructors
    /**
     * default constructor
     */
    public BbsZone() {
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

    public List<BbsUser> getManagers() {
        return managers;
    }

    public void setManagers(List<BbsUser> managers) {
        this.managers = managers;
    }

    public String getGroupStr() {
        return groupStr;
    }

    public void setGroupStr(String groupStr) {
        this.groupStr = groupStr;
    }

    public List<BbsThread> getThreads() {
        return threads;
    }

    public void setThreads(List<BbsThread> threads) {
        this.threads = threads;
    }

    public boolean isIfOnlyMag() {
        return ifOnlyMag;
    }

    public void setIfOnlyMag(boolean ifOnlyMag) {
        this.ifOnlyMag = ifOnlyMag;
    }

    public long getThreadCount() {
        IBbsThreadDAO threadDAO = SpringHelper.getSpringBean("BbsThreadDAO");
        threadCount = threadDAO.findByZone(id).size();
        return threadCount;
    }

    public void setThreadCount(long threadCount) {
        this.threadCount = threadCount;
    }

    public BbsThread getLastestThread() {
        IBbsZoneDAO zoneDAO = SpringHelper.getSpringBean("BbsZoneDAO");
        lastestThread = zoneDAO.findLatestThread(id);
        return lastestThread;
    }

    public void setLastestThread(BbsThread lastestThread) {
        this.lastestThread = lastestThread;
    }

    public String getMagsStr() {
        StringBuilder sb = new StringBuilder();
        for (BbsUser b : this.managers) {
            sb.append(b.getUsername());
            sb.append(",");
        }
        this.magsStr = sb.toString();
        return magsStr;
    }

    public void setMagsStr(String magsStr) {
        this.magsStr = magsStr;
    }

    public boolean getCheckUser() {
        return checkUser;
    }

    public void setCheckUser(boolean checkUser) {
        this.checkUser = checkUser;
    }
    
    
    
}