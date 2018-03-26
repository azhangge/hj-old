package com.hjedu.examination.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IQuestionCollectionDAO;
import com.hjedu.examination.dao.IQuestionCommentDAO;
import com.hjedu.platform.controller.ClientSession;
import com.huajie.util.HTMLCleaner;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
/**
 * 习题模块
 * 习题父类
 * @author h j
 *
 */
@Entity
@Table(name = "general_question")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class GeneralQuestion implements Serializable, Comparable {

    static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)

    @Column(name = "id")
    String id = UUID.randomUUID().toString();
    @Lob
    @Column(name = "name")

    String name;
    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    Date genTime = new Date();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "module_id")
    ExamModuleModel module;

    @Lob
    @Column(name = "right_str")
    String rightStr;

    @JsonIgnore
    @Lob
    @Column(name = "clean_name")
    String cleanName;
    @JsonIgnore
    @Column(name = "hash_code", length = 100)
    String hashCode;
    @JsonIgnore
    @Column(name = "ord")
    private int ord = 0;
    @JsonIgnore
    @Transient
    boolean selected = false;
    @JsonIgnore
    @Transient
    List<QuestionComment> comments;
    @Transient
    long commentsNum;
    @JsonIgnore
    @Transient
    boolean ifCollected = false;

    @JsonIgnore
    @Transient
    String wrongStr = "";//暂时保存试题的错题原因

    @Column(name = "qtype")
    String qtype;//根据对象类别获得题型
    
    /**
     * 题干+选项
     */
    @Lob
    @Column(name = "question",length=4000)
    String question;
    
    /**
     * 题目类型
     */
    @Column(name = "type")
    String type;
    
    /**
     * 答案
     */
    @Column(name = "answers")
    String answers;
    
    /**
     * 真实选项顺序
     */
    @Column(name = "realoption")
    String realoption;

    @Column(name = "business_id")
    private String businessId;
    
    public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
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

    public ExamModuleModel getModule() {
        return module;
    }

    public void setModule(ExamModuleModel module) {
        this.module = module;
    }

    public String getRightStr() {
        return rightStr;
    }

    public void setRightStr(String rightStr) {
        this.rightStr = rightStr;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getCleanName() {
        if (cleanName == null || "".equals(cleanName)) {
            cleanName = HTMLCleaner.delHTMLTag(name);
        }
        return cleanName;
    }

    public void setCleanName(String cleanName) {
        this.cleanName = cleanName;
    }

    public List<QuestionComment> getComments() {
        IQuestionCommentDAO comDAO = SpringHelper.getSpringBean("QuestionCommentDAO");
        this.comments = comDAO.findQuestionCommentByQuestion(this.id);
        return comments;
    }

    public void setComments(List<QuestionComment> comments) {
        this.comments = comments;
    }

    public long getCommentsNum() {
        IQuestionCommentDAO comDAO = SpringHelper.getSpringBean("QuestionCommentDAO");
        commentsNum = comDAO.getCommentNumByQuestion(this.id);
        return commentsNum;
    }

    public void setCommentsNum(long commentsNum) {
        this.commentsNum = commentsNum;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getWrongStr() {
        return wrongStr;
    }

    public void setWrongStr(String wrongStr) {
        this.wrongStr = wrongStr;
    }

    public boolean isIfCollected() {
        IQuestionCollectionDAO collectDAO = SpringHelper.getSpringBean("QuestionCollectionDAO");
        BbsUser bu = null;
        ClientSession cs = JsfHelper.getBean("clientSession");
        if (cs != null) {
            bu = cs.getUsr();
            if (bu != null) {
                QuestionCollection qc = collectDAO.findQuestionCollectionByQandU(id, bu.getId());
                ifCollected = qc != null;
            } else {
                ifCollected = false;
            }
        } else {
            ifCollected = false;
        }
        return ifCollected;
    }

    public void setIfCollected(boolean ifCollected) {
        this.ifCollected = ifCollected;
    }

    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }

    public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAnswers() {
		return answers;
	}

	public void setAnswers(String answers) {
		this.answers = answers;
	}

	public String getRealoption() {
		return realoption;
	}

	public void setRealoption(String realoption) {
		this.realoption = realoption;
	}

	public String getQtype() {
        String qt = "";
        if (this instanceof ChoiceQuestion) {
            qt = "choice";
        } else if (this instanceof MultiChoiceQuestion) {
            qt = "mchoice";
        } else if (this instanceof FillQuestion) {
            qt = "fill";
        } else if (this instanceof JudgeQuestion) {
            qt = "judge";
        } else if (this instanceof EssayQuestion) {
            qt = "essay";
        } else if (this instanceof FileQuestion) {
            qt = "file";
        } else if (this instanceof CaseQuestion) {
            qt = "case";
        }
        return qt;
    }

    public void setQtype(String qtype) {

        this.qtype = qtype;
    }

    @Override
    public int compareTo(Object o) {
        GeneralQuestion cq = (GeneralQuestion) o;
        if (cq.getModule() != null && this.getModule() != null) {
            if (cq.getModule().getOrd() != this.getModule().getOrd()) {
                return this.getModule().getOrd() - cq.getModule().getOrd();
            }
        }
        if (this.getOrd() == cq.getOrd()) {
            return this.getGenTime().compareTo(cq.getGenTime());
        } else {
            return this.getOrd() - cq.getOrd();
        }
    }

}
