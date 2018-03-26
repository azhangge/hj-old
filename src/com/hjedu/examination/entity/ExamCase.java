package com.hjedu.examination.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.service.IExamCaseService;
import com.huajie.cache.annotation.RereIndex;
import com.huajie.ejb.IPSeekerServiceRemote;
import com.huajie.util.SpringHelper;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;
/**
 * 考试模块
 * 考试记录
 */
@Entity
@Table(name = "exam_case")
public class ExamCase implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    
    private String id = UUID.randomUUID().toString();
    @Column(name = "name")
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private BbsUser user;
    
    @ManyToOne
    @JoinColumn(name = "examination_id")
    @JsonIgnore
    private Examination examination;
    
    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    
    private Date genTime = new Date();
    @Basic(optional = false)
    @Column(name = "submit_time")
    @Temporal(TemporalType.TIMESTAMP)
    
    private Date submitTime = new Date();
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "score")
    
    private Double score = 0D;
    @Column(name = "choice_score")
    
    private Double choiceScore = 0D;
    @Column(name = "multi_choice_score")
    
    private Double multiChoiceScore = 0D;
    @Column(name = "fill_score")
    
    private Double fillScore = 0D;
    @Column(name = "judge_score")
    
    private Double judgeScore = 0D;
    @Column(name = "essay_score")
    
    private Double essayScore = 0D;
    @Column(name = "file_score")
    
    private Double fileScore = 0D;
    @Column(name = "case_score")
    
    private Double caseScore = 0D;
    @Column(name = "if_pub")
    
    private boolean ifPub = true;
    @Column(name = "ip")
    
    private String ip;
    
    @OneToMany(mappedBy = "examCase", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JsonIgnore
    private List<ExamCaseItemChoice> choiceItems;
    
    @OneToMany(mappedBy = "examCase", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JsonIgnore
    private List<ExamCaseItemMultiChoice> multiChoiceItems;
//    @Transient
//    private List<ExamCaseItemChoice> choiceItemsAsList;
    
    @OneToMany(mappedBy = "examCase", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JsonIgnore
    private List<ExamCaseItemFill> fillItems;
//    @Transient
//    private List<ExamCaseItemFill> fillItemsAsList;
    
    @OneToMany(mappedBy = "examCase", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JsonIgnore
    private List<ExamCaseItemJudge> judgeItems;
    
    @OneToMany(mappedBy = "examCase", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JsonIgnore
    private List<ExamCaseItemEssay> essayItems;
    
    @OneToMany(mappedBy = "examCase", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JsonIgnore
    private List<ExamCaseItemFile> fileItems;
    
    @OneToMany(mappedBy = "examCase", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JsonIgnore
    private List<ExamCaseItemCase> caseItems;
    
    @Column(name = "bbs_score")
    private long bbsScore;
    
    @Column(name = "time_passed")
    private long timePassed = 0;
    
    @Column(name = "stat")
    private String stat;//
    
    @Column(name = "total_full_score")
    private double totalFullScore = 0D;
    
    @Column(name = "ranking")
    private int ranking = 0;//排名
    
    @Column(name = "progress")
    private int progress = 0;//进度
    
    @Column(name = "current_index")
    private int currentIndex = 0;//做题当前进度
    
    @Column(name = "wrong_num")
    private int wrongNum = 0;

    @Column(name = "platform")
    private String platform = "pc";//valid value are 'pc' and 'phone'.

    @Column(name = "display_mode")
    private String displayMode = "single";//valid value are 'single' and 'multiple'.

    @Lob
    @Column(name = "json")
    @JsonIgnore
    private String json = "";//考试实例对应的JSON串
    
    @Lob
    @Column(name = "vparts_json")
    @JsonIgnore
    private String vpartsJson = "";//考试实例对应的JSON串
    
    @Column(name = "if_passed")
    @JsonIgnore
    private boolean ifPassed = false;//考试是否通过

    @Column(name = "business_id")
	private String businessId;
    
    public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
    
    @Transient
    private long newBbsScore = 0;
//    @Transient
//    private List<ExamCaseItemJudge> judgeItemsAsList;
    @Transient
    @JsonIgnore
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
    
    private boolean selected = false;

    @Transient
    @JsonIgnore
    private List<ExamPaperManualPart> parts;//用于加载使用人工试卷中的考试的详情，内部可以放ExamCaseItemAdapter，其中又存放了ExamCaseItem信息
    //@Transient
    //List<Random2PaperPart> cparts;//用于加载使用随机试卷中的考试的详情，内部可以放ExamCaseItemAdapter，其中又存放了ExamCaseItem信息
    @Transient
    private List<VirtualExamPart> vparts;//用于加载使用随机试卷中的考试的详情，内部可以放ExamCaseItemAdapter，其中又存放了ExamCaseItem信息

    @Transient
    @RereIndex
    @JsonIgnore
    private String userId;//主要用户在缓存实例中查找时使用本属性
    
    @Column(name = "start_time")
    private Date startTime;
    
    @Column(name = "ifSimulate")
    private boolean ifSimulate;//true：模拟考试；false:正式考试

    public boolean isIfSimulate() {
		return ifSimulate;
	}

	public void setIfSimulate(boolean ifSimulate) {
		this.ifSimulate = ifSimulate;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public ExamCase() {
    }

    public ExamCase(String id) {
        this.id = id;
    }

    public ExamCase(String id, Date genTime, Date submitTime) {
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

    public Examination getExamination() {
        return examination;
    }

    public void setExamination(Examination examination) {
        this.examination = examination;
    }

    public BbsUser getUser() {
        return user;
    }

    public void setUser(BbsUser user) {
        this.user = user;
    }

    public String getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(String displayMode) {
        this.displayMode = displayMode;
    }

    public Double getMultiChoiceScore() {
        return multiChoiceScore;
    }

    public void setMultiChoiceScore(Double multiChoiceScore) {
        this.multiChoiceScore = multiChoiceScore;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
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

    public int getWrongNum() {
        return wrongNum;
    }

    public void setWrongNum(int wrongNum) {
        this.wrongNum = wrongNum;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public List<ExamCaseItemChoice> getChoiceItems() {
        return choiceItems;
    }

    public void setChoiceItems(List<ExamCaseItemChoice> choiceItems) {
        this.choiceItems = choiceItems;
    }

    public List<ExamCaseItemMultiChoice> getMultiChoiceItems() {
        return multiChoiceItems;
    }

    public void setMultiChoiceItems(List<ExamCaseItemMultiChoice> multiChoiceItems) {
        this.multiChoiceItems = multiChoiceItems;
    }

    public List<ExamCaseItemFill> getFillItems() {
        return fillItems;
    }

    public void setFillItems(List<ExamCaseItemFill> fillItems) {
        this.fillItems = fillItems;
    }

    public List<ExamCaseItemJudge> getJudgeItems() {
        return judgeItems;
    }

    public void setJudgeItems(List<ExamCaseItemJudge> judgeItems) {
        this.judgeItems = judgeItems;
    }

    public boolean isIfPub() {
        return ifPub;
    }

    public void setIfPub(boolean ifPub) {
        this.ifPub = ifPub;
    }

    public Double getChoiceScore() {
        return choiceScore;
    }

    public void setChoiceScore(Double choiceScore) {
        this.choiceScore = choiceScore;
    }

    public Double getFillScore() {
        return fillScore;
    }

    public void setFillScore(Double fillScore) {
        this.fillScore = fillScore;
    }

    public Double getJudgeScore() {
        return judgeScore;
    }

    public void setJudgeScore(Double judgeScore) {
        this.judgeScore = judgeScore;
    }

    public Double getEssayScore() {
        return essayScore;
    }

    public void setEssayScore(Double essayScore) {
        this.essayScore = essayScore;
    }

    public List<ExamCaseItemEssay> getEssayItems() {
        return essayItems;
    }

    public void setEssayItems(List<ExamCaseItemEssay> essayItems) {
        this.essayItems = essayItems;
    }

    public Double getFileScore() {
        return fileScore;
    }

    public void setFileScore(Double fileScore) {
        this.fileScore = fileScore;
    }

    public List<ExamCaseItemFile> getFileItems() {
        return fileItems;
    }

    public void setFileItems(List<ExamCaseItemFile> fileItems) {
        this.fileItems = fileItems;
    }

    public Double getCaseScore() {
        return caseScore;
    }

    public void setCaseScore(Double caseScore) {
        this.caseScore = caseScore;
    }

    public List<ExamCaseItemCase> getCaseItems() {
        return caseItems;
    }

    public void setCaseItems(List<ExamCaseItemCase> caseItems) {
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

    public double getCaseFullScore() {
        caseFullScore = 0d;
        try {
            if (this.caseItems != null) {
                for (ExamCaseItemCase c : this.caseItems) {
                    if (c != null) {
                        caseFullScore += c.getQuestion().getTotalScore();
                    }
                }
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return caseFullScore;
    }

    public double getChoiceFullScore() {
        ExamPaperRandom rp = this.getExamination().getRandomPaper();
        if (rp != null) {
            choiceFullScore = rp.getChoiceScore() * this.getExamination().getChoiceTotal();
        }
        return choiceFullScore;
    }

    public void setChoiceFullScore(double choiceFullScore) {
        this.choiceFullScore = choiceFullScore;
    }

    public double getMultiChoiceFullScore() {
        ExamPaperRandom rp = this.getExamination().getRandomPaper();
        if (rp != null) {
            multiChoiceFullScore = rp.getMultiChoiceScore() * this.getExamination().getMultiChoiceTotal();
        }
        return multiChoiceFullScore;
    }

    public void setMultiChoiceFullScore(double multiChoiceFullScore) {
        this.multiChoiceFullScore = multiChoiceFullScore;
    }

    public double getFillFullScore() {
        ExamPaperRandom rp = this.getExamination().getRandomPaper();
        if (rp != null) {
            fillFullScore = rp.getFillScore() * this.getExamination().getFillTotal();
        }
        return fillFullScore;
    }

    public void setFillFullScore(double fillFullScore) {
        this.fillFullScore = fillFullScore;
    }

    public double getJudgeFullScore() {
        ExamPaperRandom rp = this.getExamination().getRandomPaper();
        if (rp != null) {
            judgeFullScore = rp.getJudgeScore() * this.getExamination().getJudgeTotal();
        }
        return judgeFullScore;
    }

    public void setJudgeFullScore(double judgeFullScore) {
        this.judgeFullScore = judgeFullScore;
    }

    public double getEssayFullScore() {
        ExamPaperRandom rp = this.getExamination().getRandomPaper();
        if (rp != null) {
            essayFullScore = rp.getEssayScore() * this.getExamination().getEssayTotal();
        }
        return essayFullScore;
    }

    public void setEssayFullScore(double essayFullScore) {
        this.essayFullScore = essayFullScore;
    }

    public double getFileFullScore() {
        ExamPaperRandom rp = this.getExamination().getRandomPaper();
        if (rp != null) {
            fileFullScore = rp.getFileScore() * this.getExamination().getFileTotal();
        }
        return fileFullScore;
    }

    public void setFileFullScore(double fileFullScore) {
        this.fileFullScore = fileFullScore;
    }

    public double getTotalFullScore() {

        return totalFullScore;
    }

    public void setTotalFullScore(double totalFullScore) {
        this.totalFullScore = totalFullScore;
    }

    public void setCaseFullScore(double caseFullScore) {
        this.caseFullScore = caseFullScore;
    }

    public long getTimePassed() {
        return timePassed;
    }

    public void setTimePassed(long timePassed) {
        this.timePassed = timePassed;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public List<ExamPaperManualPart> getParts() {
        return parts;
    }

    public void setParts(List<ExamPaperManualPart> parts) {
        this.parts = parts;
    }

//    public List<Random2PaperPart> getCparts() {
//        return cparts;
//    }
//
//    public void setCparts(List<Random2PaperPart> cparts) {
//        this.cparts = cparts;
//    }
    public List<VirtualExamPart> getVparts() {
        return vparts;
    }

    public void setVparts(List<VirtualExamPart> vparts) {
        this.vparts = vparts;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
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

    public String getVpartsJson() {
        return vpartsJson;
    }

    public void setVpartsJson(String vpartsJson) {
        this.vpartsJson = vpartsJson;
    }

    public boolean isIfPassed() {
		return ifPassed;
	}

	public void setIfPassed(boolean ifPassed) {
		this.ifPassed = ifPassed;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ExamCase)) {
            return false;
        }
        ExamCase other = (ExamCase) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.exam.model.ExamCase[ name=" + name + " ]";
    }
}
