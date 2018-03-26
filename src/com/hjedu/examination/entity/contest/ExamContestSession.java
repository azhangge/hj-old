package com.hjedu.examination.entity.contest;

import com.google.gson.annotations.Expose;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IContestCaseDAO;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.dao.IExamPaperManualDAO;
import com.hjedu.examination.dao.IExamPaperRandom2DAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.ExamPaperManual;
import com.hjedu.examination.entity.random2.ExamPaperRandom2;
import com.hjedu.platform.controller.ClientSession;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
/**
 * 竞赛模块
 * 竞赛配置表
 * @author h j
 *
 */
@Entity
@Table(name = "exam_contest_session")
public class ExamContestSession implements Serializable, Comparable {

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
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    private Date genTime = new Date();
    @Column(name = "if_direct")
    private boolean ifDirect = true;
    @Column(name = "if_show_answer")
    @Expose
    private boolean ifShowAnswer = true;
    @Column(name = "time_len")
    @Expose
    private int timeLen = 120;
    
    

    @Column(name = "if_show")
    @Expose
    private boolean ifShow = true;
    @Column(name = "retry_interval")
    private long retryInterval = 10L;
    @Column(name = "if_random")
    private boolean ifRandom = false;
    @Column(name = "choice_random")
    private boolean choiceRandom = false;
    @Column(name = "multi_choice_random")
    private boolean multiChoiceRandom = false;
    @Lob
    @Column(name = "group_str")
    private String groupStr = "";
    @Lob
    @Column(name = "label_str")
    private String labelStr = "";
    @Column(name = "allow_user_del_score")
    private boolean allowUserDelScore = true;
    @Column(name = "if_allow_save")
    private boolean ifAllowSave = true;
    @Column(name = "allow_user_repeat")
    private boolean allowUserRepeat = true;
    @Column(name = "show_answer")
    private boolean showAnswer = true;//在考试详情中显示答案
    @Column(name = "show_right_str")
    private boolean showRightStr = true;//在考试详情中显示试题解析
    @Column(name = "if_count_down")
    private boolean ifCountDown = true;//在考试页中倒计时，时间到后自动提交

    @Basic(optional = false)
    @Column(name = "begain_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date begainTime = new Date();
    @Basic(optional = false)
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime = new Date();
    @Column(name = "mon")
    private boolean mon = false;
    @Column(name = "tues")
    private boolean tues = false;
    @Column(name = "wed")
    private boolean wed = false;
    @Column(name = "thur")
    private boolean thur = false;
    @Column(name = "fri")
    private boolean fri = false;
    @Column(name = "sat")
    private boolean sat = false;
    @Column(name = "sun")
    private boolean sun = false;

    @Column(name = "first_score")
    @Expose
    private long firstScore = 50;//第1名获得积分
    @Column(name = "second_score")
    @Expose
    private long secondScore = 40;//第2名获得积分
    @Column(name = "third_score")
    @Expose
    private long thirdScore = 30;//第3名获得积分
    @Column(name = "fourth_score")
    @Expose
    private long fourthScore = 0;//第4名获得积分
    @Column(name = "fifth_score")
    @Expose
    private long fifthScore = 0;//第5名获得积分
    @Column(name = "participate_score")
    @Expose
    private long participateScore = 5;//参与奖积分
    
    @Column(name = "bbs_score")
    @Expose
    private long bbsScore = 0;//考试满分获取的积分
    @Column(name = "score_paid")
    @Expose
    private long scorePaid = 0;
    @Column(name = "first_paid")
    private long firstPaid = 0;//第一名获得积分
    @Column(name = "second_paid")
    private long secondPaid = 0;//第二名获得积分
    @Column(name = "third_paid")
    private long thirdPaid = 0;//第三名获得积分
    @Column(name = "max_num")
    private long maxNum = 1;
    @Column(name = "auto_save")
    private Boolean autoSave = false;
    @Column(name = "auto_save_interval")
    private long autoSaveInterval = 30;
    @Column(name = "random2_id")
    private String random2Id;
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
    @Column(name = "exam_info")
    private String examInfo = "";
    @Column(name = "ord")
    private int ord = 0;
    @Column(name = "allow_max")
    @Expose
    private long allowCaseMax = 10;
    @Transient
    private ExamPaperRandom2 random2Paper;
    @Transient
    private ExamPaperManual manualPaper;
    @Transient
    private boolean ifOpen;
    @Transient
    @Expose
    private long caseNum;
    @Transient
    private boolean retry = false;
    @Transient
    private boolean scoreExsits = false;
    @Transient
    private boolean selected = false;
    @Transient
    private boolean available = false;
    @Transient
    private boolean ifToday = false;
    @Transient
    @Expose
    private String sessionStr;
    @Transient
    private ContestSessionCase sessionCase;//本竞赛的当前正在进行的会话实例

    public ExamContestSession() {
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

    public boolean isIfToday() {
        return ifToday;
    }

    public void setIfToday(boolean ifToday) {
        this.ifToday = ifToday;
    }

    public boolean isIfShowAnswer() {
        return ifShowAnswer;
    }

    public void setIfShowAnswer(boolean ifShowAnswer) {
        this.ifShowAnswer = ifShowAnswer;
    }

    public String getSessionStr() {
        if (sessionStr == null) {
            sessionStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        return sessionStr;
    }

    public void setSessionStr(String sessionStr) {
        this.sessionStr = sessionStr;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getRandom2Id() {
        return random2Id;
    }

    public void setRandom2Id(String random2Id) {
        this.random2Id = random2Id;
    }

    public boolean isIfRandom() {
        return ifRandom;
    }

    public void setIfRandom(boolean ifRandom) {
        this.ifRandom = ifRandom;
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

    public long getFirstPaid() {
        return firstPaid;
    }

    public void setFirstPaid(long firstPaid) {
        this.firstPaid = firstPaid;
    }

    public long getSecondPaid() {
        return secondPaid;
    }

    public void setSecondPaid(long secondPaid) {
        this.secondPaid = secondPaid;
    }

    public long getThirdPaid() {
        return thirdPaid;
    }

    public void setThirdPaid(long thirdPaid) {
        this.thirdPaid = thirdPaid;
    }

    public int getTimeLen() {
        return timeLen;
    }

    public void setTimeLen(int timeLen) {
        this.timeLen = timeLen;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public ExamPaperRandom2 getRandom2Paper() {
        IExamPaperRandom2DAO manualDAO = SpringHelper.getSpringBean("ExamPaperRandom2DAO");
        if (this.getRandom2Id() != null) {
            random2Paper = manualDAO.findExamPaperRandom2(this.getRandom2Id());
            return random2Paper;
        } else {
            return null;
        }
    }

    public void setRandom2Paper(ExamPaperRandom2 manualPaper) {
        this.random2Paper = manualPaper;
    }

    public ExamPaperManual getManualPaper() {
        IExamPaperManualDAO manualDAO = SpringHelper.getSpringBean("ExamPaperManualDAO");
        if (this.getRandom2Id() != null) {
            manualPaper = manualDAO.findExamPaperManual(this.getRandom2Id());
            return manualPaper;
        } else {
            return null;
        }
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

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public int fetchChoiceTotal() {
        int i = 0;
//        if (this.getManualPaper() != null) {
//            List<ChoiceQuestion> cqs = this.getManualPaper().getChoices();
//            if (cqs != null) {
//                i += cqs.size();
//            }
//        }
        choiceTotal = i;
        return choiceTotal;
    }

    public void setChoiceTotal(int choiceTotal) {
        this.choiceTotal = choiceTotal;
    }

    public int fetchMultiChoiceTotal() {
        int i = 0;
        
        multiChoiceTotal = i;
        return multiChoiceTotal;
    }

    public void setMultiChoiceTotal(int multiChoiceTotal) {
        this.multiChoiceTotal = multiChoiceTotal;
    }

    public int fetchFillTotal() {
        int i = 0;
       
        fillTotal = i;
        return fillTotal;
    }

    public void setFillTotal(int fillTotal) {
        this.fillTotal = fillTotal;
    }

    public int fetchJudgeTotal() {
        int i = 0;
        
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
        
        essayTotal = i;
        return essayTotal;
    }

    public void setEssayTotal(int essayTotal) {
        this.essayTotal = essayTotal;
    }

    public int fetchFileTotal() {
        int i = 0;
        
        fileTotal = i;
        return fileTotal;
    }

    public int fetchCaseTotal() {
        int i = 0;
        
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
        ClientSession cs = JsfHelper.getBean("clientSession");
        if (cs != null) {
            BbsUser bu = cs.getUsr();
            if (bu != null) {
                IExamCaseDAO ecDAO = SpringHelper.getSpringBean("ExamCaseDAO");
                List<ExamCase> ecs = ecDAO.findExamCaseByExamination(this.id);
                if (ecs != null) {
                    for (ExamCase ec : ecs) {
                        BbsUser ecu = ec.getUser();
                        if (ecu != null) {
                            if (ecu.getId().equals(bu.getId())) {
                                scoreExsits = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return scoreExsits;
    }

    public void setScoreExsits(boolean scoreExsits) {
        this.scoreExsits = scoreExsits;
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

    public String getLabelStr() {
        return labelStr;
    }

    public void setLabelStr(String labelStr) {
        this.labelStr = labelStr;
    }

    public long getAllowCaseMax() {
        return allowCaseMax;
    }

    public void setAllowCaseMax(long allowCaseMax) {
        this.allowCaseMax = allowCaseMax;
    }
    
    public long fetchCaseNum(String examId,String sstr) {
        IContestCaseDAO caseDAO = SpringHelper.getSpringBean("ContestCaseDAO");
        caseNum = caseDAO.getParticipateNumByContestAndSession(examId,sstr);
        return caseNum;
    }

    public long getCaseNum() {
        IContestCaseDAO caseDAO = SpringHelper.getSpringBean("ContestCaseDAO");
        caseNum = caseDAO.getParticipateNumByContestAndSession(id,this.sessionStr);
        return caseNum;
    }

    public void setCaseNum(long caseNum) {
        this.caseNum = caseNum;
    }

    public long checkParticipateTimes(String uid) {
        IContestCaseDAO caseDAO = SpringHelper.getSpringBean("ContestCaseDAO");
        long times = caseDAO.getParticipateNumByContestAndUser(id, this.sessionStr,uid);
        return times;
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

    public Date getBegainTime() {
        return begainTime;
    }

    public void setBegainTime(Date begainTime) {
        this.begainTime = begainTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public boolean isMon() {
        return mon;
    }

    public void setMon(boolean mon) {
        this.mon = mon;
    }

    public boolean isTues() {
        return tues;
    }

    public void setTues(boolean tues) {
        this.tues = tues;
    }

    public boolean isWed() {
        return wed;
    }

    public void setWed(boolean wed) {
        this.wed = wed;
    }

    public boolean isThur() {
        return thur;
    }

    public void setThur(boolean thur) {
        this.thur = thur;
    }

    public boolean isFri() {
        return fri;
    }

    public void setFri(boolean fri) {
        this.fri = fri;
    }

    public boolean isSat() {
        return sat;
    }

    public void setSat(boolean sat) {
        this.sat = sat;
    }

    public boolean isSun() {
        return sun;
    }

    public void setSun(boolean sun) {
        this.sun = sun;
    }

    public ContestSessionCase getSessionCase() {
        return sessionCase;
    }

    public void setSessionCase(ContestSessionCase sessionCase) {
        this.sessionCase = sessionCase;
    }

    public long getFirstScore() {
        return firstScore;
    }

    public void setFirstScore(long firstScore) {
        this.firstScore = firstScore;
    }

    public long getSecondScore() {
        return secondScore;
    }

    public void setSecondScore(long secondScore) {
        this.secondScore = secondScore;
    }

    public long getThirdScore() {
        return thirdScore;
    }

    public void setThirdScore(long thirdScore) {
        this.thirdScore = thirdScore;
    }

    public long getFourthScore() {
        return fourthScore;
    }

    public void setFourthScore(long fourthScore) {
        this.fourthScore = fourthScore;
    }

    public long getFifthScore() {
        return fifthScore;
    }

    public void setFifthScore(long fifthScore) {
        this.fifthScore = fifthScore;
    }

    public long getParticipateScore() {
        return participateScore;
    }

    public void setParticipateScore(long participateScore) {
        this.participateScore = participateScore;
    }

    @Override
    public int compareTo(Object o) {
        ExamContestSession ob = (ExamContestSession) o;
        if (ob.getOrd() > this.getOrd()) {
            return 1;
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
        if (!(object instanceof ExamContestSession)) {
            return false;
        }
        ExamContestSession other = (ExamContestSession) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.exam.model.Examination[ id=" + id + " ]";
    }
}
