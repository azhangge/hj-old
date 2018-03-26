package com.hjedu.examination.entity.buffet;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.Noncacheable;

import com.google.gson.annotations.Expose;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.entity.DictionaryModel;
import com.huajie.util.ObjectWithAdmin;

/**
 * 练习模块
 * 自助练习
 */
@Entity
@Table(name = "buffet_examination")
public class BuffetExamination implements Serializable,ObjectWithAdmin {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @Expose
    private String id = UUID.randomUUID().toString();
    @Lob
    @Column(name = "name")
    @Expose
    private String name;
    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genTime=new Date();
    @Noncacheable
    @OneToMany(cascade = {CascadeType.ALL},fetch = FetchType.LAZY,mappedBy = "exam")
    @Expose
    private List<BuffetExaminationPart> parts;
    
    @Noncacheable
    @OneToMany(cascade = {CascadeType.ALL},fetch = FetchType.LAZY,mappedBy = "exam")
    private List<ModuleBuffetPart> mparts;
    
    @Column(name = "choice_random")
    private boolean choiceRandom = false;
    @Column(name = "multi_choice_random")
    private boolean multiChoiceRandom = false;
    @Column(name = "bbs_score")
    @Expose
    private long bbsScore = 0;//考试满分获取的积分
    @Column(name = "score_paid")
    @Expose
    private long scorePaid = 0;
    
    @Column(name = "time_len")
    @Expose
    private int timeLen = 120;
    
    @Column(name = "if_show")
    @Expose
    private boolean ifShow=true;
    @Column(name = "ord")
    private int ord=0;
    @Lob
    @Column(name = "group_str")
    private String groupStr = "";
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "exam_admin_examination",
            joinColumns = {
                @JoinColumn(name = "exam_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "admin_id", referencedColumnName = "id")})
    private List<AdminInfo> admins;
    
    public List<AdminInfo> getAdmins() {
		return admins;
	}

	public void setAdmins(List<AdminInfo> admins) {
		this.admins = admins;
	}

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "exam_examination_department",
            joinColumns = {
                @JoinColumn(name = "examination_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "department_id", referencedColumnName = "id")})
    private List<DictionaryModel> departments;
    
    public List<DictionaryModel> getDepartments() {
		return departments;
	}

	public void setDepartments(List<DictionaryModel> departments) {
		this.departments = departments;
	}

	@Lob
    @Column(name = "label_str")
    private String labelStr = "";
    
    @Column(name = "if_count_down")
    @Expose
    private boolean ifCountDown = true;//在考试页中倒计时，时间到后自动提交
    @Column(name = "show_answer")
    @Expose
    private boolean showAnswer = true;//在考试详情中显示答案
    @Column(name = "show_right_str")
    @Expose
    private boolean showRightStr = true;//在考试详情中显示试题解析
    
    @Column(name = "business_id")
    @Expose
	private String businessId;

    public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public BuffetExamination() {
    }

    public BuffetExamination(String id) {
        this.id = id;
    }

    public BuffetExamination(String id, Date genTime) {
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

    public String getLabelStr() {
        return labelStr;
    }

    public void setLabelStr(String labelStr) {
        this.labelStr = labelStr;
    }

    public List<ModuleBuffetPart> getMparts() {
        return mparts;
    }

    public void setMparts(List<ModuleBuffetPart> mparts) {
        this.mparts = mparts;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public String getGroupStr() {
        return groupStr;
    }

    public void setGroupStr(String groupStr) {
        this.groupStr = groupStr;
    }

    public boolean isIfCountDown() {
        return ifCountDown;
    }

    public void setIfCountDown(boolean ifCountDown) {
        this.ifCountDown = ifCountDown;
    }

    public boolean isShowAnswer() {
        return showAnswer;
    }

    public void setShowAnswer(boolean showAnswer) {
        this.showAnswer = showAnswer;
    }

    public boolean isShowRightStr() {
        return showRightStr;
    }

    public void setShowRightStr(boolean showRightStr) {
        this.showRightStr = showRightStr;
    }

    public List<BuffetExaminationPart> getParts() {
        return parts;
    }

    public void setParts(List<BuffetExaminationPart> parts) {
        this.parts = parts;
    }

    public int getTimeLen() {
        return timeLen;
    }

    public void setTimeLen(int timeLen) {
        this.timeLen = timeLen;
    }

    public boolean isChoiceRandom() {
        return choiceRandom;
    }

    public void setChoiceRandom(boolean choiceRandom) {
        this.choiceRandom = choiceRandom;
    }

    public boolean isMultiChoiceRandom() {
        return multiChoiceRandom;
    }

    public void setMultiChoiceRandom(boolean multiChoiceRandom) {
        this.multiChoiceRandom = multiChoiceRandom;
    }

    
/**
    public List<ChoiceQuestion> getChoices() {
        return choices;
    }

    public void setChoices(List<ChoiceQuestion> choices) {
        this.choices = choices;
    }

    public List<MultiChoiceQuestion> getMultiChoices() {
        return multiChoices;
    }

    public void setMultiChoices(List<MultiChoiceQuestion> multiChoices) {
        this.multiChoices = multiChoices;
    }

    public List<FillQuestion> getFills() {
        return fills;
    }

    public void setFills(List<FillQuestion> fills) {
        this.fills = fills;
    }

    public List<JudgeQuestion> getJudges() {
        return judges;
    }

    public void setJudges(List<JudgeQuestion> judges) {
        this.judges = judges;
    }

    public List<EssayQuestion> getEssaies() {
        return essaies;
    }

    public void setEssaies(List<EssayQuestion> essaies) {
        this.essaies = essaies;
    }

    public List<FileQuestion> getFiles() {
        return files;
    }

    public void setFiles(List<FileQuestion> files) {
        this.files = files;
    }

    public List<CaseQuestion> getCases() {
        return cases;
    }

    public void setCases(List<CaseQuestion> cases) {
        this.cases = cases;
    }
**/
    public long getBbsScore() {
        return bbsScore;
    }

    public void setBbsScore(long bbsScore) {
        this.bbsScore = bbsScore;
    }

    public long getScorePaid() {
        return scorePaid;
    }

    public void setScorePaid(long scorePaid) {
        this.scorePaid = scorePaid;
    }

    public boolean isIfShow() {
        return ifShow;
    }

    public void setIfShow(boolean ifShow) {
        this.ifShow = ifShow;
    }

    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
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
        if (!(object instanceof BuffetExamination)) {
            return false;
        }
        BuffetExamination other = (BuffetExamination) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    
    
}
