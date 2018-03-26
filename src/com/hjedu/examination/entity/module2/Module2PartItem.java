package com.hjedu.examination.entity.module2;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.hjedu.examination.dao.ICaseQuestionDAO;
import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IEssayQuestionDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.dao.IFillQuestionDAO;
import com.hjedu.examination.dao.IJudgeQuestionDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.entity.GeneralQuestion;
import com.hjedu.examination.entity.ManualPartItem;
import com.huajie.util.SpringHelper;

/**
 * 章节练习模块
 * ？
 * @author h j
 *
 */
@Entity
@Table(name = "module2_part_item")
public class Module2PartItem implements Serializable,Comparable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id=UUID.randomUUID().toString();
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genTime=new Date();
    @Column(name = "question_id")
    private String questionId;
    @Column(name = "qtype")
    private String qtype;
    
    @Column(name = "ord")
    private int ord=0;
    @Column(name = "question_score")
    private double questionScore=1;
    @ManyToOne
    @JoinColumn(name = "part_id")
    private ModuleExam2Part part;
    @Transient
    private GeneralQuestion question;
    @Transient
    private boolean selected=false;
    
    private String tempPid;

    public Module2PartItem() {
    }

    public Module2PartItem(String id) {
        this.id = id;
    }

    public Module2PartItem(String id, Date genTime) {
        this.id = id;
        this.genTime = genTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public double getQuestionScore() {
        return questionScore;
    }

    public void setQuestionScore(double questionScore) {
        this.questionScore = questionScore;
    }

    public String getQtype() {
        return qtype;
    }

    public void setQtype(String qtype) {
        this.qtype = qtype;
    }

    

    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }

    public ModuleExam2Part getPart() {
        return part;
    }

    public void setPart(ModuleExam2Part part) {
        this.part = part;
    }

    public GeneralQuestion getQuestion() {
        if ("choice".equals(this.qtype)) {
            IChoiceQuestionDAO qDAO = SpringHelper.getSpringBean("ChoiceQuestionDAO");
            question = qDAO.findChoiceQuestion(this.questionId);
        } else if ("mchoice".equals(this.qtype)) {
            IMultiChoiceQuestionDAO qDAO = SpringHelper.getSpringBean("MultiChoiceQuestionDAO");
            question = qDAO.findMultiChoiceQuestion(this.questionId);
        }else if ("fill".equals(this.qtype)) {
            IFillQuestionDAO qDAO = SpringHelper.getSpringBean("FillQuestionDAO");
            question = qDAO.findFillQuestion(this.questionId);
        }
        else if ("judge".equals(this.qtype)) {
            IJudgeQuestionDAO qDAO = SpringHelper.getSpringBean("JudgeQuestionDAO");
            question = qDAO.findJudgeQuestion(this.questionId);
        }
        else if ("essay".equals(this.qtype)) {
            IEssayQuestionDAO qDAO = SpringHelper.getSpringBean("EssayQuestionDAO");
            question = qDAO.findEssayQuestion(this.questionId);
        }
        else if ("file".equals(this.qtype)) {
            IFileQuestionDAO qDAO = SpringHelper.getSpringBean("FileQuestionDAO");
            question = qDAO.findFileQuestion(this.questionId);
        }
        else if ("case".equals(this.qtype)) {
            ICaseQuestionDAO qDAO = SpringHelper.getSpringBean("CaseQuestionDAO");
            question = qDAO.findCaseQuestion(this.questionId);
        }
        
        return question;
    }

    public void setQuestion(GeneralQuestion question) {
        this.question = question;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getTempPid() {
        return tempPid;
    }

    public void setTempPid(String tempPid) {
        this.tempPid = tempPid;
    }

    @Override
    public int compareTo(Object o) {
        Module2PartItem oo=(Module2PartItem)o;
        return this.ord-oo.ord;
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
        if (!(object instanceof ManualPartItem)) {
            return false;
        }
        Module2PartItem other = (Module2PartItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.exam.model.ManualPartItem[ id=" + id + " ]";
    }
    
}
