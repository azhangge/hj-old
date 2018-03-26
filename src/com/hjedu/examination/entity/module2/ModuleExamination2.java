package com.hjedu.examination.entity.module2;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.eclipse.persistence.annotations.Noncacheable;

import com.hjedu.course.entity.Lesson;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IModuleExamCaseDAO;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.module.ModuleExamCase;
import com.hjedu.platform.controller.ApplicationBean;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.entity.SystemConfig;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

/**
 * 章节练习模块
 * 章节逐题练习
 *
 */
@Entity
@Table(name = "module_examination2")
public class ModuleExamination2 implements Serializable {

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
	private Date genTime = new Date();

	@Noncacheable
	@OneToOne
	@JoinColumn(name = "module_id")
	private ExamModuleModel module;

	@Noncacheable
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "exam")
	private List<ModuleExam2Part> parts;

	@Noncacheable
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "exam")
	private List<ModuleModule2Part> mparts;

	@Lob
	@Column(name = "group_str")
	private String groupStr = "";

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@Noncacheable
	@JoinTable(name = "exam_examination_department", joinColumns = {
			@JoinColumn(name = "examination_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "department_id", referencedColumnName = "id") })
	private List<DictionaryModel> departments;

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@Noncacheable
	@JoinTable(name = "exam_lesson_moduleExam", joinColumns = {
			@JoinColumn(name = "moduleExam_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "lesson_id", referencedColumnName = "id") })
	private List<Lesson> lessons;

	@Column(name = "choice_random")
	private boolean choiceRandom = false;
	@Column(name = "multi_choice_random")
	private boolean multiChoiceRandom = false;
	@Column(name = "bbs_score")
	private long bbsScore = 0;// 考试满分获取的积分
	@Column(name = "score_paid")
	private long scorePaid = 0;
	@Column(name = "if_show_answer")
	private boolean ifShowAnswer = false;

	@Column(name = "if_open")
	private boolean ifOpen = true;

	@Column(name = "time_len")
	private int timeLen = 120;

	@Column(name = "if_show")
	private boolean ifShow = true;
	@Column(name = "ord")
	private int ord = 0;

	@Column(name = "if_count_down")
	private boolean ifCountDown = true;// 在考试页中倒计时，时间到后自动提交
	@Column(name = "show_answer")
	private boolean showAnswer = true;// 在考试详情中显示答案
	@Column(name = "show_right_str")
	private boolean showRightStr = true;// 在考试详情中显示试题解析

	@Column(name = "total_num")
	private int totalNum = 0;// 试题总数

	@Column(name = "display_mode")
	private String displayMode = "multiple";// 定义显示模式是单题加载还是批量加载，有效值为：multiple|single

	@Transient
	private long caseNum = 0;

	@Transient
	private boolean ifPaid = false;
	
	@Column(name = "version")
	private int version = 0;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public ModuleExamination2() {
	}

	public ModuleExamination2(String id) {
		this.id = id;
	}

	public ModuleExamination2(String id, Date genTime) {
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

	public boolean isIfShowAnswer() {
		return ifShowAnswer;
	}

	public void setIfShowAnswer(boolean ifShowAnswer) {
		this.ifShowAnswer = ifShowAnswer;
	}

	public boolean isShowAnswer() {
		return showAnswer;
	}

	public void setShowAnswer(boolean showAnswer) {
		this.showAnswer = showAnswer;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public String getDisplayMode() {
		return displayMode;
	}

	public void setDisplayMode(String displayMode) {
		this.displayMode = displayMode;
	}

	public ExamModuleModel getModule() {
		return module;
	}

	public void setModule(ExamModuleModel module) {
		this.module = module;
	}

	public boolean isShowRightStr() {
		return showRightStr;
	}

	public void setShowRightStr(boolean showRightStr) {
		this.showRightStr = showRightStr;
	}

	public List<ModuleExam2Part> getParts() {
		return parts;
	}

	public void setParts(List<ModuleExam2Part> parts) {
		this.parts = parts;
	}

	public List<ModuleModule2Part> getMparts() {
		return mparts;
	}

	public void setMparts(List<ModuleModule2Part> mparts) {
		this.mparts = mparts;
	}

	public String getGroupStr() {
		return groupStr;
	}

	public void setGroupStr(String groupStr) {
		this.groupStr = groupStr;
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
	 * public List<ChoiceQuestion> getChoices() { return choices; }
	 *
	 * public void setChoices(List<ChoiceQuestion> choices) { this.choices =
	 * choices; }
	 *
	 * public List<MultiChoiceQuestion> getMultiChoices() { return multiChoices;
	 * }
	 *
	 * public void setMultiChoices(List<MultiChoiceQuestion> multiChoices) {
	 * this.multiChoices = multiChoices; }
	 *
	 * public List<FillQuestion> getFills() { return fills; }
	 *
	 * public void setFills(List<FillQuestion> fills) { this.fills = fills; }
	 *
	 * public List<JudgeQuestion> getJudges() { return judges; }
	 *
	 * public void setJudges(List<JudgeQuestion> judges) { this.judges = judges;
	 * }
	 *
	 * public List<EssayQuestion> getEssaies() { return essaies; }
	 *
	 * public void setEssaies(List<EssayQuestion> essaies) { this.essaies =
	 * essaies; }
	 *
	 * public List<FileQuestion> getFiles() { return files; }
	 *
	 * public void setFiles(List<FileQuestion> files) { this.files = files; }
	 *
	 * public List<CaseQuestion> getCases() { return cases; }
	 *
	 * public void setCases(List<CaseQuestion> cases) { this.cases = cases; }
	 *
	 */
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

	public boolean isIfOpen() {
		return ifOpen;
	}

	public void setIfOpen(boolean ifOpen) {
		this.ifOpen = ifOpen;
	}

	public int getOrd() {
		return ord;
	}

	public void setOrd(int ord) {
		this.ord = ord;
	}

	public long getCaseNum() {
		IModuleExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ModuleExamCaseDAO");
		caseNum = examCaseDAO.getParticipateNumByExam2(id);
		return caseNum;
	}

	public void setCaseNum(long caseNum) {
		this.caseNum = caseNum;
	}

	// 验证用户是否已经支付了此章节练习
	public boolean isIfPaid() {
		ApplicationBean ab = JsfHelper.getBean("applicationBean");
		if (ab != null) {
			SystemConfig sc = ab.getSystemConfig();
			if (sc != null) {
				boolean scoreOpen = sc.getShowScore();// 积分体系是否开放
				// 只有开放了积分体系后才验证支付情况，最大限度节约数据库查询
				if (scoreOpen) {
					IModuleExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ModuleExamCaseDAO");
					ClientSession cs = JsfHelper.getBean("clientSession");
					if (cs != null) {
						BbsUser bu = cs.getUsr();
						if (bu != null) {
							ModuleExamCase examCase = examCaseDAO.findModuleExamCaseByExaminationAndUser(id,
									bu.getId());
							if (examCase != null) {
								ifPaid = true;
							}
						}
					}
				}
			}
		}
		return ifPaid;
	}

	public void setIfPaid(boolean ifPaid) {
		this.ifPaid = ifPaid;
	}

	public List<Lesson> getLessons() {
		return lessons;
	}

	public void setLessons(List<Lesson> lessons) {
		this.lessons = lessons;
	}

	public List<DictionaryModel> getDepartments() {
		return departments;
	}

	public void setDepartments(List<DictionaryModel> departments) {
		this.departments = departments;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof ModuleExamination2)) {
			return false;
		}
		ModuleExamination2 other = (ModuleExamination2) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

}
