package com.hjedu.course.entity;

import java.util.Collections;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.Noncacheable;

import com.google.gson.annotations.Expose;

/**
 * 学习计划
 * @author h j
 *
 */
@Entity
@Table(name = "study_plan")
public class StudyPlan {
	@Id
    @Column(name = "id")
    @Expose
    private String id = UUID.randomUUID().toString();
	@Column(name = "name")
    @Expose
	private String name;
	@Column(name = "picture")
    @Expose
	private String picture="servlet/ShowImage?id=100";
	@Column(name = "introduction")
	@Lob
    @Expose
	private String introduction ;
	@Column(name = "ord")
	private int ord = 0;
	private boolean selected;
	@Column(name = "exam_str")
	@Lob
    @Expose
	private String examsStr;
	@Column(name = "course_str")
	@Lob
    @Expose
	private String courseStr;
	@Column(name = "user_str")
	@Lob
    @Expose
	private String userStr;
	
	@OneToMany(mappedBy="studyPlan",cascade={CascadeType.ALL})
	private List<StudyPlanLog> studyPlanLog;
	
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "type_log_study_plan",
            joinColumns = {
                @JoinColumn(name = "plan_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "type_log_id", referencedColumnName = "id")})
	private List<LessonTypeLog> lessonTypeLog;
	
	@Column(name = "exam_str_cn")
	@Lob
    @Expose
	private String examsStrCN;
	@Column(name = "course_str_cn")
	@Lob
    @Expose
	private String courseStrCN;
	@Column(name = "user_str_cn")
	@Lob
    @Expose
	private String userStrCN;
	/**
	 * 参加学习计划人数
	 */
	@Column(name = "user_num")
    @Expose
	private int userNum=0;
	/**
	 * 学习计划课程数目
	 */
	@Column(name = "course_num")
    @Expose
	private int courseNum=0;
	
	/**
	 * 计划需要完成学时
	 */
	@Column(name = "finish_plan_num")
    @Expose
	private Double finishPlanNum;
	
	/**
	 * 计划总学时
	 */
	@Column(name = "plan_total_num")
    @Expose
    private Double planTotalNum;
    
	/**
	 * 是否需要完成学时
	 */
    @Column(name = "if_finish_num")
    @Expose
    private boolean ifFinishNum=false;
    
    /**
     * 是否需要完成考试
     */
    @Column(name = "if_finish_exam")
    @Expose
    private boolean ifFinishExam=false;
    
    @OneToMany(mappedBy="studyPlan",cascade={CascadeType.ALL})
    private List<CourseOfPlan> courses;
    
    /**
     * 计划完成最少学时数
     */
    @Column(name = "min_class_num")
    @Expose
    private Double minClassNum;
    
    //学习计划开始时间
    @Basic(optional = false)
	@Column(name = "available_begain")
	@Temporal(TemporalType.TIMESTAMP)
	@Expose
	private Date availableBegain = new Date();
    
    //学习计划结束时间
	@Basic(optional = false)
	@Column(name = "available_end")
	@Temporal(TemporalType.TIMESTAMP)
	private Date availableEnd = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 365L * 5);
	
	/**
     * 需要完成必修课程数目
     */
    @Column(name = "required_course_num")
    @Expose
	private int requiredCourseNum;
    
    /**
     * 需要通过考试数目
     */
    @Column(name = "required_exam_num")
    @Expose
	private int requiredExamNum;
    
    /**
     * 是否为参加学习计划的用户购买该计划下所有课程
     */
    @Column(name = "if_buy_courses")
    @Expose
    private boolean ifBuyCourses = true;
    
    /**
     * 企业id
     */
    @Column(name = "business_id")
    @Expose
	private String businessId;
    
    public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
    
	public boolean isIfBuyCourses() {
		return ifBuyCourses;
	}
	public void setIfBuyCourses(boolean ifBuyCourses) {
		this.ifBuyCourses = ifBuyCourses;
	}
	public Date getAvailableBegain() {
		return availableBegain;
	}
	public void setAvailableBegain(Date availableBegain) {
		this.availableBegain = availableBegain;
	}
	public Date getAvailableEnd() {
		return availableEnd;
	}
	public void setAvailableEnd(Date availableEnd) {
		this.availableEnd = availableEnd;
	}
	public Double getMinClassNum() {
		return minClassNum;
	}
	public void setMinClassNum(Double minClassNum) {
		this.minClassNum = minClassNum;
	}
	public List<CourseOfPlan> getCourses() {
		if(courses!=null){
			Collections.sort(courses);
			Collections.reverse(courses);
		}
		return courses;
	}
	public void setCourses(List<CourseOfPlan> courses) {
		this.courses = courses;
	}
	public boolean isIfFinishNum() {
		return ifFinishNum;
	}
	public void setIfFinishNum(boolean ifFinishNum) {
		this.ifFinishNum = ifFinishNum;
	}
	public boolean isIfFinishExam() {
		return ifFinishExam;
	}
	public void setIfFinishExam(boolean ifFinishExam) {
		this.ifFinishExam = ifFinishExam;
	}
	public Double getPlanTotalNum() {
		return planTotalNum;
	}
	public void setPlanTotalNum(Double planTotalNum) {
		this.planTotalNum = planTotalNum;
	}
	public Double getFinishPlanNum() {
		return finishPlanNum;
	}
	public void setFinishPlanNum(Double finishPlanNum) {
		this.finishPlanNum = finishPlanNum;
	}
	public String getExamsStrCN() {
		return examsStrCN;
	}
	public void setExamsStrCN(String examsStrCN) {
		this.examsStrCN = examsStrCN;
	}
	public String getCourseStrCN() {
		return courseStrCN;
	}
	public void setCourseStrCN(String courseStrCN) {
		this.courseStrCN = courseStrCN;
	}
	public String getUserStrCN() {
		return userStrCN;
	}
	public void setUserStrCN(String userStrCN) {
		this.userStrCN = userStrCN;
	}
	public String getExamsStr() {
		return examsStr;
	}
	public void setExamsStr(String examsStr) {
		this.examsStr = examsStr;
	}
	public String getCourseStr() {
		return courseStr;
	}
	public void setCourseStr(String courseStr) {
		this.courseStr = courseStr;
	}
	public String getUserStr() {
		return userStr;
	}
	public void setUserStr(String userStr) {
		this.userStr = userStr;
	}
	public int getOrd() {
		return ord;
	}
	public void setOrd(int ord) {
		this.ord = ord;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public int getRequiredCourseNum() {
		return requiredCourseNum;
	}
	public void setRequiredCourseNum(int requiredCourseNum) {
		this.requiredCourseNum = requiredCourseNum;
	}
	public int getRequiredExamNum() {
		return requiredExamNum;
	}
	public void setRequiredExamNum(int requiredExamNum) {
		this.requiredExamNum = requiredExamNum;
	}
	public List<StudyPlanLog> getStudyPlanLog() {
		return studyPlanLog;
	}
	public void setStudyPlanLog(List<StudyPlanLog> studyPlanLog) {
		this.studyPlanLog = studyPlanLog;
	}
	public List<LessonTypeLog> getLessonTypeLog() {
		return lessonTypeLog;
	}
	public void setLessonTypeLog(List<LessonTypeLog> lessonTypeLog) {
		this.lessonTypeLog = lessonTypeLog;
	}
	
	public int getUserNum() {
		return userNum;
	}
	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}
	public int getCourseNum() {
		return courseNum;
	}
	public void setCourseNum(int courseNum) {
		this.courseNum = courseNum;
	}
	public static String getOrderStr(int order){
		if(order==0){
			return "availableBegain";
		}else if(order==1){
			return "userNum";
		}else if(order==2){
			return "courseNum";
		}else{
			return null;
		}
	}
}
