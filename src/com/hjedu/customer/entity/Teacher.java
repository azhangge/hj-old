package com.hjedu.customer.entity;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.google.gson.annotations.Expose;
import com.hjedu.course.entity.LessonType;
/**
 * 
 * 讲师表
 * 用户模块
 *
 */
@Entity
@Table(name = "teacher")
public class Teacher implements Serializable{
	@Id
    @Column(name = "id")
    @Expose
    private String id = UUID.randomUUID().toString();
	@Column(name = "name")
    @Expose
	private String name;
	/**
	 * 讲师身份证背面
	 */
	@Column(name = "back_picture")
    @Expose
	private String backPicture;
	/**
	 * 讲师身份证正面
	 */
	@Column(name = "front_picture")
    @Expose
	private String frontPicture;
	/**
	 * 讲师照片
	 */
	@Column(name = "picture")
    @Expose
	private String picture;
	@Column(name = "introduction")
	@Lob
    @Expose
	private String introduction ;
	@Column(name = "ord")
	private int ord = 0;
	private boolean selected;
	
	@Column(name = "qq")
    @Expose
	private String qq ;
	
	@Column(name = "email")
    @Expose
	private String email ;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy="teacher")
	private List<LessonType> LessonTypes;
	
	/**
	 * 讲师类型：0：外部讲师，1：内部讲师
	 * 默认为0
	 */
	@Column(name = "teacher_type")
    @Expose
	private int teacherType=0;
	
	/**
	 * 是否展示：0：否，1：是
	 * 默认为1
	 */
	@Column(name = "if_show")
    @Expose
	private boolean ifShow=true;
	
	/**
	 * 讲师有权限的课程分类
	 */
    @Lob
    @Column(name = "course_type_str")
    private String courseTypeStr = "";
    
    /**
     * 讲师有权限的课程分类（包括分类的所有父节点的id）
     */
    @Lob
    @Column(name = "labelStr")
    @Expose
    private String labelStr;

	/**
	 * 是否有审核其他讲师权限：0：否，1：是
	 * 默认为0
	 */
	@Column(name = "can_approved")
    @Expose
	private boolean canApproved=false;

	/**
	 * 是否需要被其他讲师审核：0：否，1：是
	 * 默认为0
	 */
	@Column(name = "need_approved")
    @Expose
	private boolean needApproved=false;

	@OneToOne
    @JoinColumn(name = "user_id")
    private BbsUser user;
	/**
	 * 职称
	 */
	@OneToMany(mappedBy="teacher",cascade={CascadeType.ALL})
	private List<JobTitle> jobTitles;
	
	/**
	 * 工作经历
	 */
	@OneToMany(mappedBy="teacher",cascade={CascadeType.ALL})
	private List<WorkExperience> workExperience;
	
	/**
	 * 项目经历
	 */
	@OneToMany(mappedBy="teacher",cascade={CascadeType.ALL})
	private List<ProjectExperience> projectExperience;
	
	/**
	 * 证书
	 */
	@OneToMany(mappedBy="teacher",cascade={CascadeType.ALL})
	private List<Certificate> certificate;
	
	/**
     * 从业年限
     */
    @Column(name = "work_years")
    @Expose
    private String workYears;
    /**
     * 工作单位
     */
    @Lob
    @Column(name = "company")
    @Expose
    private String company;
    /**
     * 身份证号
     */
    @Column(name = "id_num")
    @Expose
    private String idNum;
    
    /**
     * 性别（0保密1男2女）
     */
    @Column(name = "gender")
    @Expose
    private int gender=0;
    
    /**
     * 企业id
     */
    @Column(name = "business_id")
    @Expose
    private String businessId;

	public String getBusinessId() {
		return businessId;
	}
	
	public void setBussinessId(String businessId) {
		this.businessId = businessId;
	}
	
	public String getCourseTypeStr() {
		return courseTypeStr;
	}
	public void setCourseTypeStr(String courseTypeStr) {
		this.courseTypeStr = courseTypeStr;
	}
	
	public String getLabelStr() {
		return labelStr;
	}
	public void setLabelStr(String labelStr) {
		this.labelStr = labelStr;
	}
	public boolean isCanApproved() {
		return canApproved;
	}
	public void setCanApproved(boolean canApproved) {
		this.canApproved = canApproved;
	}
	public boolean isNeedApproved() {
		return needApproved;
	}
	public void setNeedApproved(boolean needApproved) {
		this.needApproved = needApproved;
	}
	public BbsUser getUser() {
		return user;
	}
	public void setUser(BbsUser user) {
		this.user = user;
	}
	public int getTeacherType() {
		return teacherType;
	}
	public void setTeacherType(int teacherType) {
		this.teacherType = teacherType;
	}
	public boolean isIfShow() {
		return ifShow;
	}
	public void setIfShow(boolean ifShow) {
		this.ifShow = ifShow;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<LessonType> getLessonTypes() {
		return LessonTypes;
	}
	public void setLessonTypes(List<LessonType> lessonTypes) {
		LessonTypes = lessonTypes;
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
	public List<JobTitle> getJobTitles() {
		return jobTitles;
	}
	public void setJobTitles(List<JobTitle> jobTitles) {
		this.jobTitles = jobTitles;
	}
	public List<WorkExperience> getWorkExperience() {
		return workExperience;
	}
	public void setWorkExperience(List<WorkExperience> workExperience) {
		this.workExperience = workExperience;
	}
	public List<ProjectExperience> getProjectExperience() {
		return projectExperience;
	}
	public void setProjectExperience(List<ProjectExperience> projectExperience) {
		this.projectExperience = projectExperience;
	}
	public List<Certificate> getCertificate() {
		return certificate;
	}
	public void setCertificate(List<Certificate> certificate) {
		this.certificate = certificate;
	}
	public String getWorkYears() {
		return workYears;
	}
	public void setWorkYears(String workYears) {
		this.workYears = workYears;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getBackPicture() {
		return backPicture;
	}
	public void setBackPicture(String backPicture) {
		this.backPicture = backPicture;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getFrontPicture() {
		return frontPicture;
	}
	public void setFrontPicture(String frontPicture) {
		this.frontPicture = frontPicture;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	
}
