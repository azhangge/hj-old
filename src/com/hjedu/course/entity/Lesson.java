package com.hjedu.course.entity;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.Date;
import java.util.LinkedList;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.eclipse.persistence.annotations.Noncacheable;

import com.google.gson.annotations.Expose;
import com.hjedu.course.dao.ILessonLogDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.entity.Teacher;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.entity.module2.ModuleExamination2;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.entity.BbsFileModel;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;
import com.sun.star.installation.protocols;
/**
 * 课程模块
 *学习资料
 */
@Entity
@Table(name = "y_lesson")
public class Lesson implements Serializable, Comparable {

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

	@Lob
	@Column(name = "description")
	@Expose
	private String description = "";

	@Basic(optional = false)
	@Column(name = "available_begain")
	@Temporal(TemporalType.TIMESTAMP)
	@Expose
	private Date availableBegain = new Date();

	@Basic(optional = false)
	@Column(name = "available_end")
	@Temporal(TemporalType.TIMESTAMP)
	private Date availableEnd = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 365L * 5);

	@Expose
	@Column(name = "gen_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date genTime = new Date();

	@ManyToOne
	@JoinColumn(name = "type_id")
	@Expose
	private LessonType lessonType;

	@Column(name = "time_len")
	@Expose
	private Double timeLen = 60d;

	@Column(name = "least_time")
	@Expose
	private Double leastTime = 1d;

	@Column(name = "play_type")
	@Expose
	private String playType = "inner"; // inner:内置播放器 || outter:外部播放器 ||
										// none:无视频

	@Column(name = "timing_type") // 计时方式
	@Expose
	private String timingType = "page"; // page:打开页面时间 || player:播放器计时

	@Lob
	@Column(name = "group_str")
	@Expose
	private String groupStr = "";

	@Lob
	@Column(name = "video_url")
	@Expose
	private String videoUrl = "";

//	@ManyToOne
//    @JoinColumn(name = "teacher_id")
//    private Teacher teacher;
	
	@Column(name = "parent_id")
	private String parentId;//可以多级目录	
	@Column(name = "chapter_type")
	private int chapterType;//1章  2节
	
	/**
	 * 学习资料课时数
	 */
	@Column(name = "class_num")
	private Double classNum = 1d;
	@Lob
	@Column(name = "source_url")
	@Expose
	private String sourceUrl = "";// 视频源地址

	@Column(name = "bbs_score")
	private long bbsScore = 0;// 学习完成此课程获取的积分
	@Column(name = "score_paid")
	private long scorePaid = 0;
	@Column(name = "if_show")
	private boolean ifShow = true;
	@Lob
	@Column(name = "lesson_info")
	@Expose
	private String lessonInfo = "";
	@Column(name = "ord")
	private int ord = 0;
	@Column(name = "repeat_buy")
	@Expose
	private boolean repeatBuy = false;

	@Column(name = "read_count")
	@Expose
	private int readCount = 0;

	@Lob
	@Column(name = "exam_str")
	private String examStr;

	@ManyToOne
	@JoinColumn(name = "module_exam2_id")
	@Expose
	ModuleExamination2 moduleExam2;

	@Column(name = "allow_user_del")
	@Expose
	private boolean allowUserDel = true;
	
	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	@Column(name = "channel") // 文件上传渠道 0:web;1:移动端
	@Expose
	private int channel = 0;

	@Transient
	private List<Examination> exams;
	@Transient
	private boolean ifOpen;
	@Transient
	private boolean retry = false;
	@Transient
	private boolean scoreExsits = false;
	@Transient
	private boolean selected = false;
	@Transient
	private List<BbsUser> users;
	@Transient
	private String sourceUrlCode;

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@Noncacheable
	@JoinTable(name = "exam_lesson_dic", joinColumns = {
			@JoinColumn(name = "lesson_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "dic_id", referencedColumnName = "id") })
	private List<DictionaryModel> dics;

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@Noncacheable
	@JoinTable(name = "exam_lesson_moduleExam", joinColumns = {
			@JoinColumn(name = "lesson_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "moduleExam_id", referencedColumnName = "id") })
	private List<ModuleExamination2> moduleExaminations;
	
	@Noncacheable
    @OneToMany(mappedBy = "lesson", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
	private List<BbsFileModel> files;
	
	@Column(name = "version")
	private int version = 0;
	
	private long totalTime;
	
	@Transient
	private LessonLog log;
	
	@Transient
	private List<Lesson> children;
	
	@Transient
	private String chapterIndex;
	@Transient
	private boolean hasFile;
	
	/**
	 * 关联练习数目
	 */
	@Column(name = "practice_num")
    @Expose
    private int practiceNum=0;
	
	@Column(name = "business_id")
    @Expose
	private String businessId;
    
    public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public LessonLog getLog() {
		return log;
	}

	public void setLog(LessonLog log) {
		this.log = log;
	}
	
	public List<Lesson> getChildren() {
		return children;
	}

	public void setChildren(List<Lesson> children) {
		this.children = children;
	}
	
	public String getChapterIndex() {
		return chapterIndex;
	}

	public void setChapterIndex(String chapterIndex) {
		this.chapterIndex = chapterIndex;
	}
	
	public boolean getHasFile() {
		return hasFile;
	}

	public void setHasFile(boolean hasFile) {
		this.hasFile = hasFile;
	}

	public long getTotalTime() {
		if(this.getFiles()!=null&&this.getFiles().size()>0){
			totalTime = this.files.get(0).getTotal_time();
		}else{
			totalTime = 0;
		}
		return this.totalTime;
	}

	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}

	/**
	 * 学习资料类型
	 */
	@Column(name = "lType")
	private String lType;

	public String getlType() {
		return lType;
	}

	public void setlType(String lType) {
		this.lType = lType;
	}
	
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	public int getChapterType() {
		return chapterType;
	}

	public void setChapterType(int chapterType) {
		this.chapterType = chapterType;
	}
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public List<BbsFileModel> getFiles() {
		return files;
	}

	public void setFiles(List<BbsFileModel> files) {
		this.files = files;
	}

	public List<DictionaryModel> getDics() {
		return dics;
	}

	public void setDics(List<DictionaryModel> dics) {
		this.dics = dics;
	}

	public List<ModuleExamination2> getModuleExaminations() {
		return moduleExaminations;
	}

	public void setModuleExaminations(List<ModuleExamination2> moduleExaminations) {
		this.moduleExaminations = moduleExaminations;
	}

	public Lesson() {
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getAvailableBegain() {
		return availableBegain;
	}

	public void setAvailableBegain(Date availableBegain) {
		this.availableBegain = availableBegain;
	}

	public int getReadCount() {
		return readCount;
	}

	public void setReadCount(int readCount) {
		this.readCount = readCount;
	}

	public Date getAvailableEnd() {
		return availableEnd;
	}

	public void setAvailableEnd(Date availableEnd) {
		this.availableEnd = availableEnd;
	}

	public String getLessonInfo() {
		return lessonInfo;
	}

	public void setLessonInfo(String lessonInfo) {
		this.lessonInfo = lessonInfo;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Double getClassNum() {
		return classNum;
	}

	public void setClassNum(Double classNum) {
		this.classNum = classNum;
	}

	public boolean isIfShow() {
		return ifShow;
	}

	public void setIfShow(boolean ifShow) {
		this.ifShow = ifShow;
	}

	public boolean isRepeatBuy() {
		return repeatBuy;
	}

	public void setRepeatBuy(boolean repeatBuy) {
		this.repeatBuy = repeatBuy;
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

	public boolean isAllowUserDel() {
		return allowUserDel;
	}

	public void setAllowUserDel(boolean allowUserDel) {
		this.allowUserDel = allowUserDel;
	}

	public Date getGenTime() {
		return genTime;
	}

	public void setGenTime(Date genTime) {
		this.genTime = genTime;
	}

	public boolean isRetry() {
		return retry;
	}

	public void setRetry(boolean retry) {
		this.retry = retry;
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

	public long getBbsScore() {
		return bbsScore;
	}

	public void setBbsScore(long bbsScore) {
		this.bbsScore = bbsScore;
	}

	public LessonType getLessonType() {
		return lessonType;
	}

	public void setLessonType(LessonType lessonType) {
		this.lessonType = lessonType;
	}

	public String getTimingType() {
		return timingType;
	}

	public void setTimingType(String timingType) {
		this.timingType = timingType;
	}

	public ModuleExamination2 getModuleExam2() {
		return moduleExam2;
	}

	public void setModuleExam2(ModuleExamination2 moduleExam2) {
		this.moduleExam2 = moduleExam2;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getSourceUrl() {
//		if(this.getFiles()!=null&&this.getFiles().get(0)!=null&&this.getFiles().get(0).getFileExt().equals(".mp4")){
//			String url = ((ApplicationBean)JsfHelper.getBean("applicationBean")).getMp4VirtualUrl();
//			sourceUrl = url+sourceUrl;
//		}
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getSourceUrlCode() {
		if (sourceUrl != null) {
			sourceUrlCode = URLEncoder.encode(sourceUrl);
		}
		return sourceUrlCode;
	}

	public void setSourceUrlCode(String sourceUrlCode) {
		this.sourceUrlCode = sourceUrlCode;
	}

	public long getScorePaid() {
		return scorePaid;
	}

	public void setScorePaid(long scorePaid) {
		this.scorePaid = scorePaid;
	}

	public long checkParticipateTimes(String uid) {
		IExamCaseDAO caseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
		long times = caseDAO.getParticipateNumByExamAndUser(id, uid);
		return times;
	}

	public int getOrd() {
		return ord;
	}

	public void setOrd(int ord) {
		this.ord = ord;
	}

	public Double getLeastTime() {
		return leastTime;
	}

	public void setLeastTime(Double leastTime) {
		this.leastTime = leastTime;
	}

	// 获得已经购买此课程的会员信息
	public List<BbsUser> getUsers() {
		ILessonLogDAO lessonLogDAO = SpringHelper.getSpringBean("LessonLogDAO");
		List<LessonLog> logs = lessonLogDAO.findLessonLogByLesson(id);
		List<BbsUser> userss = new LinkedList();
		for (LessonLog log : logs) {
			userss.add(log.getUser());
		}
		this.users = userss;
		return users;
	}

	public void setUsers(List<BbsUser> users) {
		this.users = users;
	}

	public String getPlayType() {
		return playType;
	}

	public void setPlayType(String playType) {
		this.playType = playType;
	}

	public String getExamStr() {
		return examStr;
	}

	public void setExamStr(String examStr) {
		this.examStr = examStr;
	}

	public List<Examination> getExams() {
		exams = new LinkedList();
		if (this.examStr != null) {
			String[] strs = examStr.split(";");
			IExaminationDAO examDAO = SpringHelper.getSpringBean("ExaminationDAO");
			for (String str : strs) {
				if (str != null) {
					Examination exam = examDAO.findExamination(str);
					if (exam != null) {
						exams.add(exam);
					}
				}
			}
		}
		return exams;
	}

	public void setExams(List<Examination> exams) {
		this.exams = exams;
	}

	public Double getTimeLen() {
		return timeLen;
	}

	public void setTimeLen(Double timeLen) {
		this.timeLen = timeLen;
	}

	public int getPracticeNum() {
		return practiceNum;
	}

	public void setPracticeNum(int practiceNum) {
		this.practiceNum = practiceNum;
	}

	@Override
	public int compareTo(Object o) {
		Lesson ob = (Lesson) o;
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
		if (!(object instanceof Lesson)) {
			return false;
		}
		Lesson other = (Lesson) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.huajie.exam.model.Lesson[ name=" + name + " ]";
	}
}
