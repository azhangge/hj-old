package com.hjedu.examination.entity;

import com.google.gson.annotations.Expose;
import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.entity.LessonType;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.dao.IExamCaseLogDAO;
import com.hjedu.examination.entity.random2.ExamPaperRandom2;
import com.hjedu.platform.controller.ClientSession;
import com.huajie.util.JsfHelper;
import com.huajie.util.ObjectWithAdmin;
import com.huajie.util.SpringHelper;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.eclipse.persistence.annotations.Noncacheable;
/**
 *考试模块
 *综合考试
 */
@Entity
@Table(name = "examination")
public class Examination implements Serializable, Comparable,Cloneable,ObjectWithAdmin {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @Expose
    private String id = UUID.randomUUID().toString();
    @Column(name = "name")
    @Expose
    private String name;
    @Basic(optional = false)
    @Column(name = "available_begain")
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    private Date availableBegain = new Date();
    @Basic(optional = false)
    @Column(name = "available_end")
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    private Date availableEnd = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 30L);

    @Column(name = "last_rank_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastRankTime = new Date();//上次生成排名时间

    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    private Date genTime = new Date();
    @Column(name = "if_direct")
    private boolean ifDirect = true;//定义是否是智能阅卷
    @Column(name = "if_show_answer")
    private boolean ifShowAnswer = false;
    @Column(name = "display_mode")
    private String displayMode = "single";//定义显示模式是单题模式还是多题模式，有效值为：multiple|single

    @Expose
    @Column(name = "time_len")
    private int timeLen = 120;//考试时长

    @Column(name = "if_show")
    private boolean ifShow = true;

    @Column(name = "if_test_retry")
    private boolean ifTestRetry = false;
    
    @Column(name = "if_check_photo")
    private boolean ifCheckPhoto= false;

    @Column(name = "retry_interval")
    private long retryInterval = 0L;
    @Column(name = "choice_random")
    private boolean choiceRandom = false;
    @Column(name = "multi_choice_random")
    private boolean multiChoiceRandom = false;
    @Column(name = "if_random")
    private boolean ifRandom = false;//在考试页中的试题是否排序
    @Column(name = "group_str")
    private String groupStr = "";

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "exam_examination_department",
            joinColumns = {
                @JoinColumn(name = "examination_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "department_id", referencedColumnName = "id")})
    private List<DictionaryModel> departments;
    
    @Column(name = "allow_user_del_score")
    @Expose
    private boolean allowUserDelScore = true;
    @Column(name = "if_allow_save")
    private boolean ifAllowSave = true;
    @Column(name = "allow_user_repeat")
    private boolean allowUserRepeat = true;
    @Column(name = "show_answer")
    @Expose
    private boolean showAnswer = true;//在考试详情中显示答案
    @Column(name = "show_right_str")
    private boolean showRightStr = true;//在考试详情中显示试题解析
    @Column(name = "show_statistic")
    private boolean showStatistic = true;//在考试详情中显示作案统计

    @Expose
    @Column(name = "if_count_down")
    private boolean ifCountDown = true;//在考试页中倒计时，时间到后自动提交

    @Column(name = "bbs_score")
    @Expose
    private long bbsScore = 0;//考试满分获取的积分
    @Column(name = "score_paid")
    @Expose
    private long scorePaid = 0;
    @Column(name = "max_num")
    private long maxNum = 3;
    @Column(name = "auto_save")
    private Boolean autoSave = false;
    @Column(name = "if_external")
    private Boolean ifExternal = false;
    @Column(name = "auto_save_interval")
    private long autoSaveInterval = 30;
    @Column(name = "add_wrong")
    private boolean addWrong = false;//是否统计错集并加入错题集
    @Column(name = "add_statistic")
    private boolean addStatistic = false;//是否统计作答情况

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "exam_admin_examination",
            joinColumns = {
                @JoinColumn(name = "exam_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "admin_id", referencedColumnName = "id")})
    private List<AdminInfo> admins;
    @Lob
    @Column(name = "label_str")
    private String labelStr = "";
    @Column(name = "lock_time")
    private long lockTime = 0;
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
    @Column(name = "choice_alias")
    private String choiceAlias = "单选题";
    @Column(name = "multi_choice_alias")
    private String multiChoiceAlias = "多选题";
    @Column(name = "fill_alias")
    private String fillAlias = "填空题";
    @Column(name = "judge_alias")
    private String judgeAlias = "判断题";
    @Column(name = "file_alias")
    private String fileAlias = "文件题";
    @Column(name = "essay_alias")
    private String essayAlias = "简答题";
    @Column(name = "case_alias")
    private String caseAlias = "综合题";
    @Column(name = "log_cheat")
    private boolean logCheat = false;
    @Column(name = "cheat_submit")
    private boolean cheatSubmit = false;
    @Column(name = "show_info")
    private boolean showInfo = false;
    @Lob
    @Column(name = "exam_info")
    private String examInfo = "";
    @Column(name = "ord")
    @Expose
    private int ord = 0;
    @Column(name = "paper_type")
    private String paperType = "random";//Valid values are manual,random and random2

    @Column(name = "external_url1")
    private String externalUrl1 = "";
    @Column(name = "external_url2")
    private String externalUrl2 = "";

    @Column(name = "qualified")
    private double qualified;

    @Column(name = "paper_pool_size")
    private int paperPoolSize = 0;

    @Column(name = "business_id")
	private String businessId;
    
    public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	
    @ManyToOne
    @JoinColumn(name = "random_id")
    private ExamPaperRandom randomPaper;
    @ManyToOne
    @JoinColumn(name = "random2_id")
    private ExamPaperRandom2 random2Paper;
    @ManyToOne
    @JoinColumn(name = "manual_id")
    private ExamPaperManual manualPaper;
    @Transient
    private boolean ifOpen;
    @Transient
    boolean ifManualPaper = false;
    @Transient
    boolean ifRandomPaper = false;
    @Transient
    boolean ifRandom2Paper = false;
    @Transient
    private boolean retry = false;
    @Transient
    private boolean scoreExsits = false;
    @Transient
    private long examCaseNum = 0;
    @Transient
    private ExamCase topExamCase;//得分最高的成绩
    @Transient
    private ExamCase usersTopExamCase;//个人得分最高的成绩

	@Transient
    @Expose
    private List<LessonType> lessonTypes;

    @Transient
    private boolean selected = false;
    
    @Column(name = "version")
    private int version = 0;
    
    @Column(name = "captureTime")
    private long captureTime;
    
    @Lob
    @Column(name = "user_str")
    private String userStr;
    
    /**
     * 集中考试及格分
     */
    @Column(name = "i_pass_score")
    private String iPassScore;
    
    /**
     * 考试类型：0 默认考试，1 集中考试
     */
    @Column(name = "exam_type")
    private String examType="0";
    
    /**
     * 考试公告
     */
    @Lob
    @Column(name = "exam_notice")
    private String examNotice;
    
    /**
     * 考试发布时间
     */
    @Column(name = "pub_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pubTime = new Date();
    
    /**
     * 考试公告名称
     */
    @Column(name = "notice_name")
    private String examNoticeName;
    
    public String getUserStr() {
		return userStr;
	}

	public void setUserStr(String userStr) {
		this.userStr = userStr;
	}

	public long getCaptureTime() {
    	if(captureTime==0){
    		captureTime=3600;
    	}
		return captureTime;
	}

	public void setCaptureTime(long captureTime) {
		this.captureTime = captureTime;
	}

    public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Examination() {
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

    public boolean isIfDirect() {
        return ifDirect;
    }

    public void setIfDirect(boolean ifDirect) {
        this.ifDirect = ifDirect;
    }

    public Date getAvailableBegain() {
        return availableBegain;
    }

    public void setAvailableBegain(Date availableBegain) {
        this.availableBegain = availableBegain;
    }

    public boolean isIfTestRetry() {
        return ifTestRetry;
    }

    public void setIfTestRetry(boolean ifTestRetry) {
        this.ifTestRetry = ifTestRetry;
    }

    public boolean isIfCheckPhoto() {
        return ifCheckPhoto;
    }

    public void setIfCheckPhoto(boolean ifCheckPhoto) {
        this.ifCheckPhoto = ifCheckPhoto;
    }

    public boolean isIfShowAnswer() {
        return ifShowAnswer;
    }

    public void setIfShowAnswer(boolean ifShowAnswer) {
        this.ifShowAnswer = ifShowAnswer;
    }

    public String getLabelStr() {
        return labelStr;
    }

    public void setLabelStr(String labelStr) {
        this.labelStr = labelStr;
    }

    public boolean isShowStatistic() {
        return showStatistic;
    }

    public void setShowStatistic(boolean showStatistic) {
        this.showStatistic = showStatistic;
    }

    public int getPaperPoolSize() {
        return paperPoolSize;
    }

    public void setPaperPoolSize(int paperPoolSize) {
        this.paperPoolSize = paperPoolSize;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public long getLockTime() {
        return lockTime;
    }

    public void setLockTime(long lockTime) {
        this.lockTime = lockTime;
    }

    public boolean isLogCheat() {
        return logCheat;
    }

    public void setLogCheat(boolean logCheat) {
        this.logCheat = logCheat;
    }

    public boolean isCheatSubmit() {
        return cheatSubmit;
    }

    public void setCheatSubmit(boolean cheatSubmit) {
        this.cheatSubmit = cheatSubmit;
    }

    public Date getAvailableEnd() {
        return availableEnd;
    }

    public void setAvailableEnd(Date availableEnd) {
        this.availableEnd = availableEnd;
    }

    public int getTimeLen() {
        return timeLen;
    }

    public void setTimeLen(int timeLen) {
        this.timeLen = timeLen;
    }

    public ExamPaperRandom getRandomPaper() {
        return randomPaper;
    }

    public void setRandomPaper(ExamPaperRandom randomPaper) {
        this.randomPaper = randomPaper;
    }

    public ExamPaperRandom2 getRandom2Paper() {
        return random2Paper;
    }

    public void setRandom2Paper(ExamPaperRandom2 randomPaper2) {
        this.random2Paper = randomPaper2;
    }

    public ExamPaperManual getManualPaper() {
        return manualPaper;

    }

    public void setManualPaper(ExamPaperManual manualPaper) {
        this.manualPaper = manualPaper;
    }

    public boolean isIfOpen() {
        long b = this.getAvailableBegain().getTime();
        long e = this.getAvailableEnd().getTime();
        long n = System.currentTimeMillis();
        if (n > b && n < e) {
            this.ifOpen = true;
        } else {
            this.ifOpen = false;
        }
        return ifOpen;
    }

    public void setIfOpen(boolean ifOpen) {
        this.ifOpen = ifOpen;
    }

    public Date getGenTime() {
        return genTime;
    }

    public boolean isIfManualPaper() {
        if (this.paperType.equals("manual")) {
            ifManualPaper = true;
        } else {
            ifManualPaper = false;
        }
        return ifManualPaper;
    }

    public void setIfManualPaper(boolean ifManualPaper) {
        this.ifManualPaper = ifManualPaper;
    }

    public boolean isIfRandomPaper() {
        if (this.paperType.equals("random")) {
            ifRandomPaper = true;
        } else {
            ifRandomPaper = false;
        }
        return ifRandomPaper;
    }

    public void setIfRandomPaper(boolean ifManualPaper) {
        this.ifRandomPaper = ifManualPaper;
    }

    public boolean isIfRandom2Paper() {
        if (this.paperType.equals("random2")) {
            ifRandom2Paper = true;
        } else {
            ifRandom2Paper = false;
        }
        return ifRandom2Paper;
    }

    public void setIfRandom2Paper(boolean ifManualPaper) {
        this.ifRandom2Paper = ifManualPaper;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public boolean isIfRandom() {
        return ifRandom;
    }

    public void setIfRandom(boolean ifRandom) {
        this.ifRandom = ifRandom;
    }

    public boolean isAddWrong() {
        return addWrong;
    }

    public void setAddWrong(boolean addWrong) {
        this.addWrong = addWrong;
    }

    public String getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(String displayMode) {
        this.displayMode = displayMode;
    }

    public int fetchChoiceTotal() {
        int i = 0;
        List<ModuleRandomPaper> oss = null;
        if (this.getRandomPaper() != null) {
            oss = this.getRandomPaper().getModulePapers();
        }
        if (oss != null) {
            for (ModuleRandomPaper me : oss) {
                i += me.getChoiceNum();
            }
        }
        if (this.getManualPaper() != null) {
            List<ChoiceQuestion> cqs = this.getManualPaper().getChoices();
            if (cqs != null) {
                i += cqs.size();
            }
        }
        choiceTotal = i;
        return choiceTotal;
    }

    public void setChoiceTotal(int choiceTotal) {
        this.choiceTotal = choiceTotal;
    }

    public int fetchMultiChoiceTotal() {
        int i = 0;
        List<ModuleRandomPaper> oss = null;
        if (this.getRandomPaper() != null) {
            oss = this.getRandomPaper().getModulePapers();
        }
        if (oss != null) {
            for (ModuleRandomPaper me : oss) {
                i += me.getMultiChoiceNum();
            }
        }
        if (this.getManualPaper() != null) {
            List<MultiChoiceQuestion> cqs = this.getManualPaper().getMultiChoices();
            if (cqs != null) {
                i += cqs.size();
            }
        }
        multiChoiceTotal = i;
        return multiChoiceTotal;
    }

    public void setMultiChoiceTotal(int multiChoiceTotal) {
        this.multiChoiceTotal = multiChoiceTotal;
    }

    public int fetchFillTotal() {
        int i = 0;
        List<ModuleRandomPaper> oss = null;
        if (this.getRandomPaper() != null) {
            oss = this.getRandomPaper().getModulePapers();
        }
        if (oss != null) {
            for (ModuleRandomPaper me : oss) {
                i += me.getFillNum();
            }
        }
        if (this.getManualPaper() != null) {
            List<FillQuestion> cqs = this.getManualPaper().getFills();
            if (cqs != null) {
                i += cqs.size();
            }
        }
        fillTotal = i;
        return fillTotal;
    }

    public void setFillTotal(int fillTotal) {
        this.fillTotal = fillTotal;
    }

    public int fetchJudgeTotal() {
        int i = 0;
        List<ModuleRandomPaper> oss = null;
        if (this.getRandomPaper() != null) {
            oss = this.getRandomPaper().getModulePapers();
        }
        if (oss != null) {
            for (ModuleRandomPaper me : oss) {
                i += me.getJudgeNum();
            }
        }
        if (this.getManualPaper() != null) {
            List<JudgeQuestion> cqs = this.getManualPaper().getJudges();
            if (cqs != null) {
                i += cqs.size();
            }
        }
        judgeTotal = i;
        return judgeTotal;
    }

    public void setJudgeTotal(int judgeTotal) {
        this.judgeTotal = judgeTotal;
    }


    public boolean isIfShow() {
        return ifShow;
    }

    public void setIfShow(boolean ifShow) {
        this.ifShow = ifShow;
    }

    public boolean isRetry() {
        return retry;
    }

    public void setRetry(boolean retry) {
        this.retry = retry;
    }

    public long getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(long retryInterval) {
        this.retryInterval = retryInterval;
    }

    public int fetchEssayTotal() {
        int i = 0;
        List<ModuleRandomPaper> oss = null;
        if (this.getRandomPaper() != null) {
            oss = this.getRandomPaper().getModulePapers();
        }
        if (oss != null) {
            for (ModuleRandomPaper me : oss) {
                i += me.getEssayNum();
            }
        }
        if (this.getManualPaper() != null) {
            List<EssayQuestion> cqs = this.getManualPaper().getEssaies();
            if (cqs != null) {
                i += cqs.size();
            }
        }
        essayTotal = i;
        return essayTotal;
    }

    public void setEssayTotal(int essayTotal) {
        this.essayTotal = essayTotal;
    }

    public int fetchFileTotal() {
        int i = 0;
        List<ModuleRandomPaper> oss = null;
        if (this.getRandomPaper() != null) {
            oss = this.getRandomPaper().getModulePapers();
        }
        if (oss != null) {
            for (ModuleRandomPaper me : oss) {
                i += me.getFileNum();
            }
        }
        if (this.getManualPaper() != null) {
            List<FileQuestion> cqs = this.getManualPaper().getFiles();
            if (cqs != null) {
                i += cqs.size();
            }
        }
        fileTotal = i;
        return fileTotal;
    }

    public int fetchCaseTotal() {
        int i = 0;
        List<ModuleRandomPaper> oss = null;
        if (this.getRandomPaper() != null) {
            oss = this.getRandomPaper().getModulePapers();
        }
        if (oss != null) {
            for (ModuleRandomPaper me : oss) {
                i += me.getCaseNum();
            }
        }
        if (this.getManualPaper() != null) {
            List<CaseQuestion> cqs = this.getManualPaper().getCases();
            if (cqs != null) {
                i += cqs.size();
            }
        }
        caseTotal = i;
        return caseTotal;
    }

    public void setCaseTotal(int caseTotal) {
        this.caseTotal = caseTotal;
    }

    public int getChoiceTotal() {
        return choiceTotal;
    }

    public int getMultiChoiceTotal() {
        return multiChoiceTotal;
    }

    public int getFillTotal() {
        return fillTotal;
    }

    public int getJudgeTotal() {
        return judgeTotal;
    }

    public int getEssayTotal() {
        return essayTotal;
    }

    public int getFileTotal() {
        return fileTotal;
    }

    public int getCaseTotal() {
        return caseTotal;
    }

    public void setFileTotal(int fileTotal) {
        this.fileTotal = fileTotal;
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

    public String getGroupStr() {
        return groupStr;
    }

    public void setGroupStr(String groupStr) {
        this.groupStr = groupStr;
    }

    public boolean isScoreExsits() {
        scoreExsits = false;
        ClientSession cs = JsfHelper.getBean("clientSession");
        if (cs != null) {
            BbsUser bu = cs.getUsr();
            if (bu != null) {
                IExamCaseDAO ecDAO = SpringHelper.getSpringBean("ExamCaseDAO");
                List<ExamCase> ecs = ecDAO.findExamCaseByExaminationAndUser(id, bu.getId());
                if (ecs != null) {
                    if (!ecs.isEmpty()) {
                        scoreExsits = true;
                    }
                }
            }
        }
        return scoreExsits;
    }

    public void setScoreExsits(boolean scoreExsits) {
        this.scoreExsits = scoreExsits;
    }

    public long getExamCaseNum() {
        IExamCaseDAO ecDAO = SpringHelper.getSpringBean("ExamCaseDAO");
        examCaseNum = ecDAO.getParticipateNumByExam(this.id);
        return examCaseNum;
    }

    public void setExamCaseNum(long examCaseNum) {
        this.examCaseNum = examCaseNum;
    }

    public boolean isAllowUserDelScore() {
        return allowUserDelScore;
    }

    public void setAllowUserDelScore(boolean allowUserDelScore) {
        this.allowUserDelScore = allowUserDelScore;
    }

    public boolean isAllowUserRepeat() {
        return allowUserRepeat;
    }

    public void setAllowUserRepeat(boolean allowUserRepeat) {
        this.allowUserRepeat = allowUserRepeat;
    }

    public long getBbsScore() {
        return bbsScore;
    }

    public void setBbsScore(long bbsScore) {
        this.bbsScore = bbsScore;
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

    public boolean isIfAllowSave() {
        return ifAllowSave;
    }

    public void setIfAllowSave(boolean ifAllowSave) {
        this.ifAllowSave = ifAllowSave;
    }

    public boolean isIfCountDown() {
        return ifCountDown;
    }

    public void setIfCountDown(boolean ifCountDown) {
        this.ifCountDown = ifCountDown;
    }

    public long getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(long maxNum) {
        this.maxNum = maxNum;
    }

    public List<AdminInfo> getAdmins() {
        return admins;
    }

    public void setAdmins(List<AdminInfo> admins) {
        this.admins = admins;
    }

    public Boolean getAutoSave() {
        return autoSave;
    }

    public void setAutoSave(Boolean autoSave) {
        this.autoSave = autoSave;
    }

    public boolean isShowInfo() {
        return showInfo;
    }

    public void setShowInfo(boolean showInfo) {
        this.showInfo = showInfo;
    }

    public String getExamInfo() {
        return examInfo;
    }

    public void setExamInfo(String examInfo) {
        this.examInfo = examInfo;
    }

    public long getScorePaid() {
        return scorePaid;
    }

    public void setScorePaid(long scorePaid) {
        this.scorePaid = scorePaid;
    }

    public long getAutoSaveInterval() {
        return autoSaveInterval;
    }

    public void setAutoSaveInterval(long autoSaveInterval) {
        this.autoSaveInterval = autoSaveInterval;
    }
    
    /**
     * 返回该用户参与考试次数
     * @param uid
     * @return
     */
    public long checkParticipateTimes(String uid) {
//        IExamCaseDAO caseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
//        long times = caseDAO.getParticipateNumByExamAndUser(id, uid);
//        return times;
        
        IExamCaseLogDAO logDAO2 = SpringHelper.getSpringBean("ExamCaseLogDAO");
        long times = logDAO2.findNumExamCaseLogByUser(uid, id);
        return times;
    }

    /**
     * 本方法用于检测用户是否打破了“不允许重复参加”的限制
     *
     * @param uid
     * @return
     */
    public boolean checkIfBreakRepeat(String uid) {
        boolean b = false;
        if (!this.isAllowUserRepeat()) {
            if (uid != null) {
                IExamCaseDAO ecDAO = SpringHelper.getSpringBean("ExamCaseDAO");
                List<ExamCase> ecs = ecDAO.findExamCaseByExaminationAndUser(id, uid);
                if (ecs != null) {
                    if (!ecs.isEmpty()) {
                        scoreExsits = true;
                    }
                }
            }
        }
        return b;
    }

    public String getChoiceAlias() {
        return choiceAlias;
    }

    public void setChoiceAlias(String choiceAlias) {
        this.choiceAlias = choiceAlias;
    }

    public String getMultiChoiceAlias() {
        return multiChoiceAlias;
    }

    public void setMultiChoiceAlias(String multiChoiceAlias) {
        this.multiChoiceAlias = multiChoiceAlias;
    }

    public Boolean isIfExternal() {
        return ifExternal;
    }

    public void setIfExternal(Boolean ifExternal) {
        this.ifExternal = ifExternal;
    }

    public String getFillAlias() {
        return fillAlias;
    }

    public void setFillAlias(String fillAlias) {
        this.fillAlias = fillAlias;
    }

    public String getJudgeAlias() {
        return judgeAlias;
    }

    public void setJudgeAlias(String judgeAlias) {
        this.judgeAlias = judgeAlias;
    }

    public String getExternalUrl1() {
        return externalUrl1;
    }

    public void setExternalUrl1(String externalUrl1) {
        this.externalUrl1 = externalUrl1;
    }

    public String getExternalUrl2() {
        return externalUrl2;
    }

    public void setExternalUrl2(String externalUrl2) {
        this.externalUrl2 = externalUrl2;
    }

    public String getFileAlias() {
        return fileAlias;
    }

    public void setFileAlias(String fileAlias) {
        this.fileAlias = fileAlias;
    }

    public String getEssayAlias() {
        return essayAlias;
    }

    public void setEssayAlias(String essayAlias) {
        this.essayAlias = essayAlias;
    }

    public double getQualified() {
        return qualified;
    }

    public void setQualified(double qualified) {
        this.qualified = qualified;
    }

    public Date getLastRankTime() {
        return lastRankTime;
    }

    public void setLastRankTime(Date lastRankTime) {
        this.lastRankTime = lastRankTime;
    }

    public String getCaseAlias() {
        return caseAlias;
    }

    public void setCaseAlias(String caseAlias) {
        this.caseAlias = caseAlias;
    }

    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }

    public ExamCase getTopExamCase() {
        IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
        topExamCase = examCaseDAO.findTopScoreExamCase(this.getId());
        return topExamCase;
    }
 
    public void setTopExamCase(ExamCase topExamCase) {
        this.topExamCase = topExamCase;
    }
    
    public ExamCase getUserTopExamCase(String userId) {
        IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
        topExamCase = examCaseDAO.findUserTopScoreExamCase(this.getId(),userId);
        return topExamCase;
    }
    
    public ExamCase getUsersTopExamCase() {
    	IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
    	ClientSession cs = JsfHelper.getBean("clientSession");
        if(cs!=null && cs.getUsr()!=null){
        	usersTopExamCase = examCaseDAO.findUserTopScoreExamCase(this.getId(),cs.getUsr().getId());
        }
		return usersTopExamCase;
	}

	public void setUsersTopExamCase(ExamCase usersTopExamCase) {
		this.usersTopExamCase = usersTopExamCase;
	}

    public String getPaperType() {
        return paperType;
    }

    public void setPaperType(String paperType) {
        this.paperType = paperType;
    }

    public List<LessonType> getLessonTypes() {
        ILessonTypeDAO ltDAO = SpringHelper.getSpringBean("LessonTypeDAO");
        lessonTypes = ltDAO.findLessonTypeByExam(this.id);
        return lessonTypes;
    }

    public void setLessonTypes(List<LessonType> lessonTypes) {
        this.lessonTypes = lessonTypes;
    }

    public boolean isAddStatistic() {
        return addStatistic;
    }

    public void setAddStatistic(boolean addStatistic) {
        this.addStatistic = addStatistic;
    }
    
    public List<DictionaryModel> getDepartments() {
		return departments;
	}

	public void setDepartments(List<DictionaryModel> departments) {
		this.departments = departments;
	}

    public String getiPassScore() {
		return iPassScore;
	}

	public void setiPassScore(String iPassScore) {
		this.iPassScore = iPassScore;
	}

	public String getExamType() {
		return examType;
	}

	public void setExamType(String examType) {
		this.examType = examType;
	}

	public String getExamNotice() {
		return examNotice;
	}

	public void setExamNotice(String examNotice) {
		this.examNotice = examNotice;
	}


	public Date getPubTime() {
		return pubTime;
	}

	public void setPubTime(Date pubTime) {
		this.pubTime = pubTime;
	}

	public String getExamNoticeName() {
		return examNoticeName;
	}

	public void setExamNoticeName(String examNoticeName) {
		this.examNoticeName = examNoticeName;
	}

	@Override
    public int compareTo(Object o) {
        Examination ob = (Examination) o;
        if (ob.getOrd() > this.getOrd()) {
            return 1;
        } else if (ob.getOrd() == this.getOrd()) {
            return 0;
        } else {
            return -1;
        }
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
        if (!(object instanceof Examination)) {
            return false;
        }
        Examination other = (Examination) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public Object clone() {
        Object o = null;
        try {
            o = (Examination) super.clone();//Object 中的clone()识别出你要复制的是哪一个对象。 
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return o;
    }

    @Override
    public String toString() {
        return "com.huajie.exam.model.Examination[ id=" + id + " ]";
    }
}
