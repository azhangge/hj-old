package com.hjedu.examination.entity.contest;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.*;
/**
 * 竞赛模块
 * 此对象为一场竞赛的一个周期（会话）实例
 * @author h j
 *
 */
@Entity
@Table(name = "contest_session_case")
public class ContestSessionCase implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();
    
    @ManyToOne
    @JoinColumn(name = "contest_id")
    private ExamContestSession examination;
    
    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genTime = new Date();
    
    //表示竞赛了结时间，这个时间是指结束竞赛并计算奖励的时间
    @Basic(optional = false)
    @Column(name = "submit_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date submitTime ;
    
    @Column(name = "stat")
    private String stat="processing";// processing submitted
    
    @Column(name = "session_str")
    private String sessionStr;//
    
    @Transient
    boolean selected = false;

    public ContestSessionCase() {
    }

    public ContestSessionCase(String id) {
        this.id = id;
    }

    public ContestSessionCase(String id, Date genTime, Date submitTime) {
        this.id = id;
        this.genTime = genTime;
        this.submitTime = submitTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ExamContestSession getExamination() {
        return examination;
    }

    public void setExamination(ExamContestSession examination) {
        this.examination = examination;
    }


    public String getSessionStr() {
        return sessionStr;
    }

    public void setSessionStr(String sessionStr) {
        this.sessionStr = sessionStr;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }


    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContestSessionCase)) {
            return false;
        }
        ContestSessionCase other = (ContestSessionCase) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.exam.model.ExamCase[ id=" + id + " ]";
    }
}
