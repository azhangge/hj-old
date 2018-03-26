package com.hjedu.examination.entity.random2;

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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.eclipse.persistence.annotations.Noncacheable;

import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.entity.buffet.BuffetExamination;

/**
 * 随机试卷模块
 * 随机试卷
 */
@Entity
@Table(name = "exam_paper_random2")
public class ExamPaperRandom2 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();
    @Column(name = "name", length = 300)
    private String name;
    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genTime=new Date();
    @Noncacheable
    @OneToMany(cascade = {CascadeType.ALL},fetch = FetchType.LAZY,mappedBy = "paper")
    private List<Random2PaperPart> parts;
    
    @Noncacheable
    @OneToMany(cascade = {CascadeType.ALL},fetch = FetchType.LAZY,mappedBy = "paper")
    private List<ModuleRandom2Part> mparts;
    
    @Column(name = "choice_random")
    private boolean choiceRandom = false;
    @Column(name = "multi_choice_random")
    private boolean multiChoiceRandom = false;
    @Column(name = "bbs_score")
    private long bbsScore = 0;//考试满分获取的积分
    @Column(name = "score_paid")
    private long scorePaid = 0;
    
    @Column(name = "time_len")
    private int timeLen = 120;
    
    @Column(name = "if_show")
    private boolean ifShow=true;
    @Column(name = "ord")
    private int ord=0;
    
    
    @Column(name = "if_count_down")
    private boolean ifCountDown = true;//在考试页中倒计时，时间到后自动提交
    @Column(name = "show_answer")
    private boolean showAnswer = true;//在考试详情中显示答案
    @Column(name = "show_right_str")
    private boolean showRightStr = true;//在考试详情中显示试题解析
    
    @Column(name = "business_id")
    private String businessId;
    
    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "exam_admin_ExamPaper",
            joinColumns = {
                @JoinColumn(name = "examPaper_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "admin_id", referencedColumnName = "id")})
    private List<AdminInfo> admins;
    
    private double totalScore;
    
    public double getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(double totalScore) {
		this.totalScore = totalScore;
	}

	public List<AdminInfo> getAdmins() {
		return admins;
	}

	public void setAdmins(List<AdminInfo> admins) {
		this.admins = admins;
	}

    public ExamPaperRandom2() {
    }

    public ExamPaperRandom2(String id) {
        this.id = id;
    }

    public ExamPaperRandom2(String id, Date genTime) {
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

    public List<ModuleRandom2Part> getMparts() {
        return mparts;
    }

    public void setMparts(List<ModuleRandom2Part> mparts) {
        this.mparts = mparts;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
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

    public List<Random2PaperPart> getParts() {
        return parts;
    }

    public void setParts(List<Random2PaperPart> parts) {
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
        ExamPaperRandom2 other = (ExamPaperRandom2) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    
    
}
