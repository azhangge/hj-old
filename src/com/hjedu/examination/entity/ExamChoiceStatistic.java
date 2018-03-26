package com.hjedu.examination.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.*;

import com.hjedu.customer.entity.BbsUser;
/**
 * 考试模块
 * ？
 * @author h j
 *
 */
@Entity
@Table(name = "e_choice_statistic")
public class ExamChoiceStatistic implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();
    
    
    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genTime = new Date();
    
    @ManyToOne
    @JoinColumn(name = "choice_id")
    private ExamChoice choice;
    
    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Examination exam;
    
    
    @Column(name = "case_id")
    private String examCaseId;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private BbsUser user;
    
    
    @Transient
    boolean selected = false;
    

    public ExamChoiceStatistic() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ExamChoice getChoice() {
        return choice;
    }

    public void setChoice(ExamChoice choice) {
        this.choice = choice;
    }

    public String getExamCaseId() {
        return examCaseId;
    }

    public void setExamCaseId(String examCaseId) {
        this.examCaseId = examCaseId;
    }

    

    public BbsUser getUser() {
        return user;
    }

    public void setUser(BbsUser user) {
        this.user = user;
    }

    public Examination getExam() {
        return exam;
    }

    public void setExam(Examination exam) {
        this.exam = exam;
    }

   


    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
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
        if (!(object instanceof ExamChoiceStatistic)) {
            return false;
        }
        ExamChoiceStatistic other = (ExamChoiceStatistic) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.exam.model.ExamChoice[ id=" + id + " ]";
    }
}
