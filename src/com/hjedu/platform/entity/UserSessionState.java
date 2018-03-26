package com.hjedu.platform.entity;

import com.google.gson.annotations.Expose;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.entity.module2.ModuleExamination2;
import com.huajie.cache.annotation.RereIndex;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
/**
 * 
 * 用户会话状态
 * 用户模块
 *
 */
@Entity
@Table(name = "y_user_session_state")
public class UserSessionState implements Serializable, Comparable {

    @Id
    @Column(name = "id")
    @Expose
    private String id = UUID.randomUUID().toString();

    @OneToOne
    @JoinColumn(name = "user_id")
    private BbsUser user;

    @Column(name = "gen_time", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Expose
    private Date genTime = new Date();

    @Column(name = "login_time", nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Expose
    private Date loginTime = new Date();

    @Column(name = "exam_time", nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Expose
    private Date examTime = new Date();

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Examination exam = null;

    @Column(name = "end_exam_time", nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Expose
    private Date endExamTime = new Date();

    @Column(name = "module_exam_time", nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Expose
    private Date moduleExamTime = new Date();

    @ManyToOne
    @JoinColumn(name = "module_exam_id")
    private ModuleExamination2 moduleExam = null;

    @Column(name = "end_module_exam_time", nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Expose
    private Date endModuleExamTime = new Date();

    @Column(name = "ip")
    @Expose
    private String ip;

    @Column(name = "agent")
    @Expose
    private String agent;

    @Column(name = "ipAddr")
    @Expose
    private String ipAddr;

    @Column(name = "session_id")
    @Expose
    private String sessionId;

    @Column(name = "kicked")
    @Expose
    private boolean kicked = false;

    @Column(name = "kicker_ip")
    @Expose
    private String kickerIp;

    @Transient
    @RereIndex
    private String userId;//供缓存中的索引查询使用

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Examination getExam() {
        return exam;
    }

    public void setExam(Examination exam) {
        this.exam = exam;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public BbsUser getUser() {
        return user;
    }

    public void setUser(BbsUser user) {
        this.user = user;
    }

    public boolean isKicked() {
        return kicked;
    }

    public void setKicked(boolean kicked) {
        this.kicked = kicked;
    }

    public String getKickerIp() {
        return kickerIp;
    }

    public void setKickerIp(String kickerIp) {
        this.kickerIp = kickerIp;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Date getExamTime() {
        return examTime;
    }

    public void setExamTime(Date examTime) {
        this.examTime = examTime;
    }

    public Date getEndExamTime() {
        return endExamTime;
    }

    public void setEndExamTime(Date endExamTime) {
        this.endExamTime = endExamTime;
    }

    public Date getModuleExamTime() {
        return moduleExamTime;
    }

    public void setModuleExamTime(Date moduleExamTime) {
        this.moduleExamTime = moduleExamTime;
    }

    public ModuleExamination2 getModuleExam() {
        return moduleExam;
    }

    public void setModuleExam(ModuleExamination2 moduleExam) {
        this.moduleExam = moduleExam;
    }

    public Date getEndModuleExamTime() {
        return endModuleExamTime;
    }

    public void setEndModuleExamTime(Date endModuleExamTime) {
        this.endModuleExamTime = endModuleExamTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIpAddr() {

        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getUserId() {
        if (this.user != null) {
            userId = user.getId();
        }
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public int compareTo(Object o) {
        UserSessionState oo = (UserSessionState) o;
        if (this.getGenTime().getTime() < oo.getGenTime().getTime()) {
            return 1;
        } else if (this.getGenTime().getTime() > oo.getGenTime().getTime()) {
            return -1;
        } else {
            return 0;
        }
    }
}
