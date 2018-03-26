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

import com.hjedu.examination.dao.IExamKnowledgeDAO;
import com.huajie.util.SpringHelper;

/**
 * 习题模块
 * ？
 * @author h j
 *
 */
@Entity
@Table(name = "wrong_knowledge")
public class WrongKnowledge implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();
    @Column(name = "knowledge_id")
    private String knowledgeId;
    @Column(name = "wrong_times")
    private Long wrongTimes=0L;
    @Column(name = "user_id")
    private String userId;
    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genTime=new Date();
    @Transient
    private ExamKnowledge knowledge;

    public WrongKnowledge() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    

    public String getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(String knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public Long getWrongTimes() {
        return wrongTimes;
    }

    public void setWrongTimes(Long wrongTimes) {
        this.wrongTimes = wrongTimes;
    }

    public ExamKnowledge getKnowledge() {
        IExamKnowledgeDAO knowDAO=SpringHelper.getSpringBean("ExamKnowledgeDAO");
        knowledge=knowDAO.findExamKnowledge(this.knowledgeId);
        return knowledge;
    }

    public void setKnowledge(ExamKnowledge knowledge) {
        this.knowledge = knowledge;
    }
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
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
        if (!(object instanceof WrongKnowledge)) {
            return false;
        }
        WrongKnowledge other = (WrongKnowledge) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.exam.model.WrongQuestion[ id=" + id + " ]";
    }
    
}
