/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hjedu.examination.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.dao.IExaminationDAO;
import com.huajie.ejb.IPSeekerServiceRemote;
import com.huajie.util.SpringHelper;

/**
 * 考试模块
 * ?
 * @author h j
 *
 */
@Entity
@Table(name = "exam_cheat")
public class ExamCheat implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id=UUID.randomUUID().toString();
    @Column(name = "user_id")
    private String userId;
    @Column(name = "exam_id")
    private String examId;
    @Column(name = "case_id")
    private String caseId;
    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genTime=new Date();
    @Column(name = "ip")
    private String ip;
    @Transient
    private BbsUser user;
    @Transient
    private Examination exam;
    @Transient
    private ExamCase examCase;
    @Transient
    private boolean selected;
    @Transient
    private String ipAddr;

    public ExamCheat() {
    }

    public ExamCheat(String id) {
        this.id = id;
    }

    public ExamCheat(String id, Date genTime) {
        this.id = id;
        this.genTime = genTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIpAddr() {
        IPSeekerServiceRemote ips = SpringHelper.getSpringBean("ipSeekerService");
        ipAddr=ips.seek(ip);
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public BbsUser getUser() {
        IBbsUserDAO userDAO=SpringHelper.getSpringBean("BbsUserDAO");
        this.user=userDAO.findBbsUser(this.userId);
        return user;
    }

    public void setUser(BbsUser user) {
        this.user = user;
    }

    public Examination getExam() {
        IExaminationDAO userDAO=SpringHelper.getSpringBean("ExaminationDAO");
        this.exam=userDAO.findExamination(this.examId);
        return exam;
    }

    public void setExam(Examination exam) {
        this.exam = exam;
    }

    public ExamCase getExamCase() {
        IExamCaseDAO caseDAO=SpringHelper.getSpringBean("ExamCaseDAO");
        this.examCase=caseDAO.findExamCase(this.caseId);
        return examCase;
    }

    public void setExamCase(ExamCase examCase) {
        this.examCase = examCase;
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
        if (!(object instanceof ExamCheat)) {
            return false;
        }
        ExamCheat other = (ExamCheat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.exam.model.ExamCheat[ id=" + id + " ]";
    }
    
}
