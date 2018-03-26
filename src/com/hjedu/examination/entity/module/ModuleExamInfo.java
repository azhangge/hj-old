package com.hjedu.examination.entity.module;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.hjedu.examination.dao.IExamCaseDAO;
import com.huajie.util.SpringHelper;
/**
 * 章节练习模块
 * 章节练习配置表
 * @author h j
 *
 */
@Entity
@Table(name = "module_exam_info")
public class ModuleExamInfo implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genTime = new Date();
    @Column(name = "if_show_answer")
    private boolean ifShowAnswer = true;
    @Column(name = "time_len")
    private int timeLen = 120;
    @Column(name = "choice_score")
    private double choiceScore = 1D;
    @Column(name = "multi_choice_score")
    private double multiChoiceScore = 1D;
    @Column(name = "fill_score")
    private double fillScore = 1D;
    @Column(name = "judge_score")
    private double judgeScore = 1D;
    @Column(name = "essay_score")
    private double essayScore = 10D;
    @Column(name = "file_score")
    private double fileScore = 10D;
   
    @Column(name = "choice_random")
    private boolean choiceRandom = false;
    @Column(name = "multi_choice_random")
    private boolean multiChoiceRandom = false;
    
    
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
    @Column(name = "if_random")
    private boolean ifRandom = true;//在考试页中的试题是否排序
    @Column(name = "bbs_score")
    private long bbsScore = 0;//考试满分获取的积分
    @Column(name = "score_paid")
    private long scorePaid = 0;
   
    @Column(name = "auto_save")
    private Boolean autoSave = false;
    @Column(name = "auto_save_interval")
    private long autoSaveInterval = 30;
    @Column(name = "lock_time")
    private long lockTime = 0;
    
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
    
    @Transient
    private boolean selected = false;

    public ModuleExamInfo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getChoiceScore() {
        return choiceScore;
    }

    public void setChoiceScore(double choiceScore) {
        this.choiceScore = choiceScore;
    }

    public double getMultiChoiceScore() {
        return multiChoiceScore;
    }

    public void setMultiChoiceScore(double multiChoiceScore) {
        this.multiChoiceScore = multiChoiceScore;
    }

    public boolean isIfShowAnswer() {
        return ifShowAnswer;
    }

    public void setIfShowAnswer(boolean ifShowAnswer) {
        this.ifShowAnswer = ifShowAnswer;
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

    public double getFillScore() {
        return fillScore;
    }

    public void setFillScore(double fillScore) {
        this.fillScore = fillScore;
    }

    public double getJudgeScore() {
        return judgeScore;
    }

    public void setJudgeScore(double judgeScore) {
        this.judgeScore = judgeScore;
    }

    public int getTimeLen() {
        return timeLen;
    }

    public void setTimeLen(int timeLen) {
        this.timeLen = timeLen;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }


    public double getEssayScore() {
        return essayScore;
    }

    public void setEssayScore(double essayScore) {
        this.essayScore = essayScore;
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

    public boolean isIfRandom() {
        return ifRandom;
    }

    public void setIfRandom(boolean ifRandom) {
        this.ifRandom = ifRandom;
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

    public long checkParticipateTimes(String uid) {
        IExamCaseDAO caseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
        long times = caseDAO.getParticipateNumByExamAndUser(id, uid);
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

    public double getFileScore() {
        return fileScore;
    }

    public void setFileScore(double fileScore) {
        this.fileScore = fileScore;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ModuleExamInfo)) {
            return false;
        }
        ModuleExamInfo other = (ModuleExamInfo) object;
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
