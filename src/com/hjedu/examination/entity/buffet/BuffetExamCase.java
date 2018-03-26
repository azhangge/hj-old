package com.hjedu.examination.entity.buffet;

import com.google.gson.annotations.Expose;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.entity.ExamRoom;
import com.hjedu.examination.service.IExamCaseService;
import com.huajie.ejb.IPSeekerServiceRemote;
import com.huajie.util.SpringHelper;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;
import org.eclipse.persistence.annotations.Noncacheable;
/**
 * 练习模块
 * 自助练习记录
 */
@Entity
@Table(name = "buffet_exam_case")
public class BuffetExamCase implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @Expose
    private String id = UUID.randomUUID().toString();
    @Column(name = "name")
    @Expose
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private BbsUser user;
    @ManyToOne
    @JoinColumn(name = "examination_id")
    @Expose
    private BuffetExamination examination;
    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    private Date genTime = new Date();
    @Basic(optional = false)
    @Column(name = "submit_time")
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    private Date submitTime = new Date();
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "score")
    @Expose
    private Double score = 0D;
    @Column(name = "if_pub")
    @Expose
    private boolean ifPub = true;
    @Column(name = "ip")
    @Expose
    private String ip;

    @Column(name = "business_id")
	private String businessId;
    
    public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	
	
    /**
     * @Column(name = "choice_num") private int choiceNum = 0;
     * @Column(name = "multi_choice_num") private int multiChoiceNum = 0;
     * @Column(name = "fill_num") private int fillNum = 0;
     * @Column(name = "judge_num") private int judgeNum = 0;
     * @Column(name = "essay_num") private int essayNum = 0;
     * @Column(name = "file_num") private int fileNum = 0;
     * @Column(name = "case_num") private int caseNum = 0; *
     */
    @Noncacheable
    @OneToMany(mappedBy = "examCase", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<BuffetExamCaseItemChoice> choiceItems;
    @Noncacheable
    @OneToMany(mappedBy = "examCase", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<BuffetExamCaseItemMultiChoice> multiChoiceItems;
//    @Transient
//    private List<ExamCaseItemChoice> choiceItemsAsList;
    @Noncacheable
    @OneToMany(mappedBy = "examCase", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<BuffetExamCaseItemFill> fillItems;
//    @Transient
//    private List<ExamCaseItemFill> fillItemsAsList;
    @Noncacheable
    @OneToMany(mappedBy = "examCase", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<BuffetExamCaseItemJudge> judgeItems;
    @Noncacheable
    @OneToMany(mappedBy = "examCase", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<BuffetExamCaseItemEssay> essayItems;
    @Noncacheable
    @OneToMany(mappedBy = "examCase", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<BuffetExamCaseItemFile> fileItems;
    @Noncacheable
    @OneToMany(mappedBy = "examCase", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<BuffetExamCaseItemCase> caseItems;
    @Column(name = "bbs_score")
    @Expose
    private long bbsScore;
    @Column(name = "time_passed")
    @Expose
    private long timePassed = 0;
    @Column(name = "stat")
    @Expose
    private String stat;//
    @Column(name = "total_full_score")
    @Expose
    private double totalFullScore = 0D;

    @Transient
    private long newBbsScore = 0;
//    @Transient
//    private List<ExamCaseItemJudge> judgeItemsAsList;
    @Transient
    private ExamRoom room;
    @Transient
    private String ipAddr;
//    @Transient
//    private double examinationTotalScore;
    @Transient//单选题的满分
    private double choiceFullScore = 0D;
    @Transient//多选题的满分
    private double multiChoiceFullScore = 0D;
    @Transient//填空题的满分
    private double fillFullScore = 0D;
    @Transient//判断题的满分
    private double judgeFullScore = 0D;
    @Transient//问答题的满分
    private double essayFullScore = 0D;
    @Transient//文件题的满分
    private double fileFullScore = 0D;
    @Transient//综合题的满分
    private double caseFullScore = 0D;

    @Transient
    boolean selected = false;
    @Transient
    @Expose
    List<BuffetExaminationPart> parts;//用于加载使用人工试卷中的考试的详情，内部可以放ExamCaseItemAdapter，其中又存放了ExamCaseItem信息

    public BuffetExamCase() {
    }

    public BuffetExamCase(String id) {
        this.id = id;
    }

    public BuffetExamCase(String id, Date genTime, Date submitTime) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BuffetExamination getExamination() {
        return examination;
    }

    public void setExamination(BuffetExamination examination) {
        this.examination = examination;
    }

    public BbsUser getUser() {
        return user;
    }

    public void setUser(BbsUser user) {
        this.user = user;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public List<BuffetExamCaseItemChoice> getChoiceItems() {
        return choiceItems;
    }

    public void setChoiceItems(List<BuffetExamCaseItemChoice> choiceItems) {
        this.choiceItems = choiceItems;
    }

    public List<BuffetExamCaseItemMultiChoice> getMultiChoiceItems() {
        return multiChoiceItems;
    }

    public void setMultiChoiceItems(List<BuffetExamCaseItemMultiChoice> multiChoiceItems) {
        this.multiChoiceItems = multiChoiceItems;
    }

    public List<BuffetExamCaseItemFill> getFillItems() {
        return fillItems;
    }

    public void setFillItems(List<BuffetExamCaseItemFill> fillItems) {
        this.fillItems = fillItems;
    }

    public List<BuffetExamCaseItemJudge> getJudgeItems() {
        return judgeItems;
    }

    public void setJudgeItems(List<BuffetExamCaseItemJudge> judgeItems) {
        this.judgeItems = judgeItems;
    }

    public boolean isIfPub() {
        return ifPub;
    }

    public void setIfPub(boolean ifPub) {
        this.ifPub = ifPub;
    }

    public List<BuffetExamCaseItemEssay> getEssayItems() {
        return essayItems;
    }

    public void setEssayItems(List<BuffetExamCaseItemEssay> essayItems) {
        this.essayItems = essayItems;
    }

    public List<BuffetExamCaseItemFile> getFileItems() {
        return fileItems;
    }

    public void setFileItems(List<BuffetExamCaseItemFile> fileItems) {
        this.fileItems = fileItems;
    }

    public List<BuffetExamCaseItemCase> getCaseItems() {
        return caseItems;
    }

    public void setCaseItems(List<BuffetExamCaseItemCase> caseItems) {
        this.caseItems = caseItems;
    }

    public ExamRoom getRoom() {
        IExamCaseService s = SpringHelper.getSpringBean("ExamCaseService");
        room = s.confirmExamRoom(ip);
        return room;
    }

    public void setRoom(ExamRoom room) {
        this.room = room;
    }

    public String getIpAddr() {
        if (this.ip != null) {
            IPSeekerServiceRemote ips = SpringHelper.getSpringBean("ipSeekerService");
            ipAddr = ips.seek(ip);
        }
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

//    public double getExaminationTotalScore() {
//        Examination ex = this.getExamination();
//        examinationTotalScore = ex.getChoiceScore() * ex.getChoiceTotal() + ex.getMultiChoiceScore() * ex.getMultiChoiceTotal() + ex.getFillScore() * ex.getFillTotal() + ex.getJudgeScore() + ex.getJudgeTotal() + ex.getEssayScore() * ex.getEssayTotal() + ex.getFileScore() * ex.getFileTotal();
//        return examinationTotalScore;
//    }
//
//    public void setExaminationTotalScore(double examinationTotalScore) {
//        this.examinationTotalScore = examinationTotalScore;
//    }
    public long getBbsScore() {
        return bbsScore;
    }

    public void setBbsScore(long bbsScore) {
        this.bbsScore = bbsScore;
    }

    public long getNewBbsScore() {
        if (newBbsScore == 0) {
            newBbsScore = bbsScore;
        }
        return newBbsScore;
    }

    public void setNewBbsScore(long newBbsScore) {
        this.newBbsScore = newBbsScore;
    }

    public double getTotalFullScore() {
        return totalFullScore;
    }

    public void setTotalFullScore(double totalFullScore) {
        this.totalFullScore = totalFullScore;
    }

    public long getTimePassed() {
        return timePassed;
    }

    public void setTimePassed(long timePassed) {
        this.timePassed = timePassed;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public List<BuffetExaminationPart> getParts() {
        return parts;
    }

    public void setParts(List<BuffetExaminationPart> parts) {
        this.parts = parts;
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
        if (!(object instanceof BuffetExamCase)) {
            return false;
        }
        BuffetExamCase other = (BuffetExamCase) object;
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
