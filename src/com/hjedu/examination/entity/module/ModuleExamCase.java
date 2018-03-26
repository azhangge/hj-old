package com.hjedu.examination.entity.module;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IModuleExamInfoDAO;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.ExamRoom;
import com.hjedu.examination.entity.module2.ModuleExam2Part;
import com.hjedu.examination.entity.module2.ModuleExamination2;
import com.hjedu.examination.service.IExamCaseService;
import com.huajie.ejb.IPSeekerServiceRemote;
import com.huajie.util.SpringHelper;

/**
 * 章节练习模块
 * 章节随机练习与章节逐题练习共用此类，表示一个章节练习的实例（记录）
 * @author Administrator
 */
@Entity
@Table(name = "module_exam_case")
public class ModuleExamCase implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private BbsUser user;
    @ManyToOne
    @JoinColumn(name = "module_id")
    private ExamModuleModel examModule;
    @ManyToOne
    @JoinColumn(name = "exam_id")
    private ModuleExamination2 examination;
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
    
    
    @Column(name = "business_id")
	private String businessId;
    
    public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	
    @OneToMany(mappedBy = "examCase", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ModuleExamCaseItemChoice> choiceItems;
    @OneToMany(mappedBy = "examCase", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ModuleExamCaseItemMultiChoice> multiChoiceItems;
//    @Transient
//    private List<ExamCaseItemChoice> choiceItemsAsList;
    @OneToMany(mappedBy = "examCase", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ModuleExamCaseItemFill> fillItems;
//    @Transient
//    private List<ExamCaseItemFill> fillItemsAsList;
    @OneToMany(mappedBy = "examCase", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ModuleExamCaseItemJudge> judgeItems;
    @OneToMany(mappedBy = "examCase", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ModuleExamCaseItemEssay> essayItems;
    @OneToMany(mappedBy = "examCase", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ModuleExamCaseItemFile> fileItems;
    @OneToMany(mappedBy = "examCase", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ModuleExamCaseItemCase> caseItems;
    @Column(name = "bbs_score")
    private long bbsScore;
    @Column(name = "time_passed")
    private long timePassed = 0;
    @Column(name = "stat")
    private String stat;//

    @Column(name = "choice_total")
    private int choiceTotal;
    @Column(name = "multi_choice_total")
    private int multiChoiceTotal;
    @Column(name = "fill_total")
    private int fillTotal;
    @Column(name = "judge_total")
    private int judgeTotal;
    @Column(name = "essay_total")
    private int essayTotal;
    @Column(name = "file_total")
    private int fileTotal;
    @Column(name = "case_total")
    private int caseTotal;
    @Column(name = "progress")
    private int progress=1;
    @Column(name = "wrong_num")
    private int wrongNum=0;
    @Column(name = "done_num")
    private int doneNum=0;
    @Column(name = "module_type")//章节练习类型，有章节随机练习与章节逐题练习
    private String moduleType="single";//single,random
    
    
    @Transient
    private long newBbsScore = 0;
//    @Transient
//    private List<ExamCaseItemJudge> judgeItemsAsList;
    @Transient
    private ExamRoom room;
    @Transient
    private String ipAddr;
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
    @Transient//全部满分
    private double totalFullScore = 0D;
    @Transient
    boolean selected = false;
    @Transient
    List<ModuleExam2Part> cparts;//用于加载使用人工试卷中的考试的详情，内部可以放ExamCaseItemAdapter，其中又存放了ExamCaseItem信息

    public ModuleExamCase() {
    }

    public ModuleExamCase(String id) {
        this.id = id;
    }

    public ModuleExamCase(String id, Date genTime, Date submitTime) {
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

    public ExamModuleModel getExamModule() {
        return examModule;
    }

    public void setExamModule(ExamModuleModel examModule) {
        this.examModule = examModule;
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

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public int getWrongNum() {
        return wrongNum;
    }

    public void setWrongNum(int wrongNum) {
        this.wrongNum = wrongNum;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public ModuleExamination2 getExamination() {
        return examination;
    }

    public void setExamination(ModuleExamination2 examination) {
        this.examination = examination;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
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

    public List<ModuleExamCaseItemChoice> getChoiceItems() {
        return choiceItems;
    }

    public void setChoiceItems(List<ModuleExamCaseItemChoice> choiceItems) {
        this.choiceItems = choiceItems;
    }

    public List<ModuleExamCaseItemMultiChoice> getMultiChoiceItems() {
        return multiChoiceItems;
    }

    public void setMultiChoiceItems(List<ModuleExamCaseItemMultiChoice> multiChoiceItems) {
        this.multiChoiceItems = multiChoiceItems;
    }

    public List<ModuleExamCaseItemFill> getFillItems() {
        return fillItems;
    }

    public void setFillItems(List<ModuleExamCaseItemFill> fillItems) {
        this.fillItems = fillItems;
    }

    public List<ModuleExamCaseItemJudge> getJudgeItems() {
        return judgeItems;
    }

    public void setJudgeItems(List<ModuleExamCaseItemJudge> judgeItems) {
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

    public List<ModuleExamCaseItemEssay> getEssayItems() {
        return essayItems;
    }

    public void setEssayItems(List<ModuleExamCaseItemEssay> essayItems) {
        this.essayItems = essayItems;
    }

    public Double getFileScore() {
        return fileScore;
    }

    public void setFileScore(Double fileScore) {
        this.fileScore = fileScore;
    }

    public List<ModuleExamCaseItemFile> getFileItems() {
        return fileItems;
    }

    public void setFileItems(List<ModuleExamCaseItemFile> fileItems) {
        this.fileItems = fileItems;
    }

    public Double getCaseScore() {
        return caseScore;
    }

    public void setCaseScore(Double caseScore) {
        this.caseScore = caseScore;
    }

    public List<ModuleExamCaseItemCase> getCaseItems() {
        return caseItems;
    }

    public void setCaseItems(List<ModuleExamCaseItemCase> caseItems) {
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

    public int getChoiceTotal() {
        return choiceTotal;
    }

    public void setChoiceTotal(int choiceTotal) {
        this.choiceTotal = choiceTotal;
    }

    public int getMultiChoiceTotal() {
        return multiChoiceTotal;
    }

    public void setMultiChoiceTotal(int multiChoiceTotal) {
        this.multiChoiceTotal = multiChoiceTotal;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public int getFillTotal() {
        return fillTotal;
    }

    public void setFillTotal(int fillTotal) {
        this.fillTotal = fillTotal;
    }

    public int getJudgeTotal() {
        return judgeTotal;
    }

    public void setJudgeTotal(int judgeTotal) {
        this.judgeTotal = judgeTotal;
    }

    public int getEssayTotal() {
        return essayTotal;
    }

    public void setEssayTotal(int essayTotal) {
        this.essayTotal = essayTotal;
    }

    public int getFileTotal() {
        return fileTotal;
    }

    public void setFileTotal(int fileTotal) {
        this.fileTotal = fileTotal;
    }

    public int getCaseTotal() {
        return caseTotal;
    }

    public void setCaseTotal(int caseTotal) {
        this.caseTotal = caseTotal;
    }

    public double getCaseFullScore() {
        caseFullScore = 0d;
        try {
            if (this.caseItems != null) {
                for (ModuleExamCaseItemCase c : this.caseItems) {
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

    public void setCaseFullScore(double caseFullScore) {
        this.caseFullScore = caseFullScore;
    }

    public double getChoiceFullScore() {
        IModuleExamInfoDAO infoDAO = SpringHelper.getSpringBean("ModuleExamInfoDAO");
        ModuleExamInfo info = infoDAO.findModuleExamInfo();
        choiceFullScore = info.getChoiceScore() * this.getChoiceTotal();
        return choiceFullScore;
    }

    public void setChoiceFullScore(double choiceFullScore) {
        this.choiceFullScore = choiceFullScore;
    }

    public double getMultiChoiceFullScore() {
        IModuleExamInfoDAO infoDAO = SpringHelper.getSpringBean("ModuleExamInfoDAO");
        ModuleExamInfo info = infoDAO.findModuleExamInfo();
        multiChoiceFullScore = info.getMultiChoiceScore() * this.getMultiChoiceTotal();
        return multiChoiceFullScore;
    }

    public void setMultiChoiceFullScore(double multiChoiceFullScore) {
        this.multiChoiceFullScore = multiChoiceFullScore;
    }

    public double getFillFullScore() {
        IModuleExamInfoDAO infoDAO = SpringHelper.getSpringBean("ModuleExamInfoDAO");
        ModuleExamInfo info = infoDAO.findModuleExamInfo();
        fillFullScore = info.getFillScore() * this.getFillTotal();
        return fillFullScore;
    }

    public void setFillFullScore(double fillFullScore) {
        this.fillFullScore = fillFullScore;
    }

    public double getJudgeFullScore() {
        IModuleExamInfoDAO infoDAO = SpringHelper.getSpringBean("ModuleExamInfoDAO");
        ModuleExamInfo info = infoDAO.findModuleExamInfo();
        judgeFullScore = info.getJudgeScore() * this.getJudgeTotal();
        return judgeFullScore;
    }

    public void setJudgeFullScore(double judgeFullScore) {
        this.judgeFullScore = judgeFullScore;
    }

    public double getEssayFullScore() {
        IModuleExamInfoDAO infoDAO = SpringHelper.getSpringBean("ModuleExamInfoDAO");
        ModuleExamInfo info = infoDAO.findModuleExamInfo();
        essayFullScore = info.getEssayScore() * this.getEssayTotal();
        return essayFullScore;
    }

    public void setEssayFullScore(double essayFullScore) {
        this.essayFullScore = essayFullScore;
    }

    public double getFileFullScore() {
        IModuleExamInfoDAO infoDAO = SpringHelper.getSpringBean("ModuleExamInfoDAO");
        ModuleExamInfo info = infoDAO.findModuleExamInfo();
        fileFullScore = info.getFileScore() * this.getFileTotal();
        return fileFullScore;
    }

    public void setFileFullScore(double fileFullScore) {
        this.fileFullScore = fileFullScore;
    }

    public double getTotalFullScore() {
        totalFullScore = this.getChoiceFullScore() + this.getMultiChoiceFullScore() + this.getFillFullScore() + this.getJudgeFullScore() + this.getEssayFullScore() + this.getFileFullScore() + this.getCaseFullScore();
        return totalFullScore;
    }

    public void setTotalFullScore(double totalFullScore) {
        this.totalFullScore = totalFullScore;
    }

    public List<ModuleExam2Part> getCparts() {
        return cparts;
    }

    public void setCparts(List<ModuleExam2Part> cparts) {
        this.cparts = cparts;
    }

    public int getDoneNum() {
        return doneNum;
    }

    public void setDoneNum(int doneNum) {
        this.doneNum = doneNum;
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
        if (!(object instanceof ModuleExamCase)) {
            return false;
        }
        ModuleExamCase other = (ModuleExamCase) object;
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
