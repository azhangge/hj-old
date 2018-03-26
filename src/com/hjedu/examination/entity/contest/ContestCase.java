package com.hjedu.examination.entity.contest;

import com.google.gson.annotations.Expose;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.entity.ExamPaperManualPart;
import com.hjedu.examination.entity.ExamRoom;
import com.hjedu.examination.entity.random2.Random2PaperPart;
import com.hjedu.examination.service.IExamCaseService;
import com.huajie.ejb.IPSeekerServiceRemote;
import com.huajie.util.SpringHelper;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;

/**
 * 竞赛模块
 *此类的对象为一个考生的竞赛实例（竞赛记录）
 * @author h j
 *
 */
@Entity
@Table(name = "contest_case")
public class ContestCase implements Serializable {

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
    @JoinColumn(name = "contest_id")
    @Expose
    private ExamContestSession examination;
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
    @Expose
    private String ip;
    @OneToMany(mappedBy = "examCase", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    
    private List<ContestCaseItemChoice> choiceItems;
    @OneToMany(mappedBy = "examCase", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    
    private List<ContestCaseItemMultiChoice> multiChoiceItems;
//    @Transient
//    private List<ExamCaseItemChoice> choiceItemsAsList;
    @OneToMany(mappedBy = "examCase", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    
    private List<ContestCaseItemFill> fillItems;
//    @Transient
//    private List<ExamCaseItemFill> fillItemsAsList;
    @OneToMany(mappedBy = "examCase", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    
    private List<ContestCaseItemJudge> judgeItems;
    @OneToMany(mappedBy = "examCase", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    
    private List<ContestCaseItemEssay> essayItems;
    @OneToMany(mappedBy = "examCase", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    
    private List<ContestCaseItemFile> fileItems;
    @OneToMany(mappedBy = "examCase", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    
    private List<ContestCaseItemCase> caseItems;
    @Column(name = "bbs_score")
    @Expose
    private long bbsScore;
    @Column(name = "time_passed")
    @Expose
    private long timePassed = 0;
    @Column(name = "stat")
    @Expose
    private String stat;//
    @Column(name = "ranking")
    @Expose
    private int ranking;//排名
    @Column(name = "session_str")
    @Expose
    private String sessionStr;//

    @Column(name = "session_id")
    @Expose
    private String sessionId;//用于关联竞赛的一次实例（会话）

    @Column(name = "total_full_score")//全部满分
    @Expose
    private double totalFullScore = 0D;

    @Column(name = "if_awarded")
    @Expose
    private boolean ifAwarded = false;

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

    @Transient
    boolean selected = false;
    @Transient
    List<ExamPaperManualPart> parts;//用于加载使用人工试卷中的考试的详情，内部可以放ExamCaseItemAdapter，其中又存放了ExamCaseItem信息
    @Transient
    @Expose
    List<Random2PaperPart> cparts;//用于加载使用人工试卷中的考试的详情，内部可以放ExamCaseItemAdapter，其中又存放了ExamCaseItem信息

    public ContestCase() {
    }

    public ContestCase(String id) {
        this.id = id;
    }

    public ContestCase(String id, Date genTime, Date submitTime) {
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

    public ExamContestSession getExamination() {
        return examination;
    }

    public void setExamination(ExamContestSession examination) {
        this.examination = examination;
    }

    public BbsUser getUser() {
        return user;
    }

    public void setUser(BbsUser user) {
        this.user = user;
    }

    public Double getMultiChoiceScore() {
        return multiChoiceScore;
    }

    public void setMultiChoiceScore(Double multiChoiceScore) {
        this.multiChoiceScore = multiChoiceScore;
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

    public boolean isIfPub() {
        return ifPub;
    }

    public void setIfPub(boolean ifPub) {
        this.ifPub = ifPub;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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

    public Double getFileScore() {
        return fileScore;
    }

    public void setFileScore(Double fileScore) {
        this.fileScore = fileScore;
    }

    public Double getCaseScore() {
        return caseScore;
    }

    public void setCaseScore(Double caseScore) {
        this.caseScore = caseScore;
    }

    public List<ContestCaseItemChoice> getChoiceItems() {
        return choiceItems;
    }

    public void setChoiceItems(List<ContestCaseItemChoice> choiceItems) {
        this.choiceItems = choiceItems;
    }

    public List<ContestCaseItemMultiChoice> getMultiChoiceItems() {
        return multiChoiceItems;
    }

    public void setMultiChoiceItems(List<ContestCaseItemMultiChoice> multiChoiceItems) {
        this.multiChoiceItems = multiChoiceItems;
    }

    public List<ContestCaseItemFill> getFillItems() {
        return fillItems;
    }

    public void setFillItems(List<ContestCaseItemFill> fillItems) {
        this.fillItems = fillItems;
    }

    public List<ContestCaseItemJudge> getJudgeItems() {
        return judgeItems;
    }

    public void setJudgeItems(List<ContestCaseItemJudge> judgeItems) {
        this.judgeItems = judgeItems;
    }

    public List<ContestCaseItemEssay> getEssayItems() {
        return essayItems;
    }

    public void setEssayItems(List<ContestCaseItemEssay> essayItems) {
        this.essayItems = essayItems;
    }

    public List<ContestCaseItemFile> getFileItems() {
        return fileItems;
    }

    public void setFileItems(List<ContestCaseItemFile> fileItems) {
        this.fileItems = fileItems;
    }

    public List<ContestCaseItemCase> getCaseItems() {
        return caseItems;
    }

    public void setCaseItems(List<ContestCaseItemCase> caseItems) {
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

    public List<ExamPaperManualPart> getParts() {
        return parts;
    }

    public void setParts(List<ExamPaperManualPart> parts) {
        this.parts = parts;
    }

    public List<Random2PaperPart> getCparts() {
        return cparts;
    }

    public void setCparts(List<Random2PaperPart> cparts) {
        this.cparts = cparts;
    }

    public boolean isIfAwarded() {
        return ifAwarded;
    }

    public void setIfAwarded(boolean ifAwarded) {
        this.ifAwarded = ifAwarded;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContestCase)) {
            return false;
        }
        ContestCase other = (ContestCase) object;
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
