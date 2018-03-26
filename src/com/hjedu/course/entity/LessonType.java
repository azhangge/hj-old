package com.hjedu.course.entity;

import java.io.Serializable;
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
import com.hjedu.course.dao.ILessonDAO;
import com.hjedu.course.dao.ILessonLogDAO;
import com.hjedu.course.dao.ILessonTypeLogDAO;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.entity.Teacher;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.CourseType;
import com.hjedu.examination.entity.Examination;
import com.hjedu.platform.controller.ClientSession;
import com.huajie.app.util.DateUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.ObjectWithAdmin;
import com.huajie.util.SpringHelper;
/**
 * 课程模块
 * 课程
 */
@Entity
@Table(name = "exam_lesson_type")
public class LessonType implements Serializable, Comparable,ObjectWithAdmin {

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
    @Column(name = "ord")
    @Expose
    private int ord;
    @Column(name = "open_exam_num")
    @Expose
    private Double openExamNum = 0d;
    @Lob
    @Column(name = "decription1")
    @Expose
    private String description1;
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    private Date genTime = new Date();
    
    @Lob
    @Column(name = "exam_str")
    @Expose
    private String examStr;
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "exam_admin_lesson_type",
            joinColumns = {
                @JoinColumn(name = "lesson_type_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "admin_id", referencedColumnName = "id")})
    private List<AdminInfo> admins;
    
    
    @Transient
    private int lessonNum=0;//该类别下的课程数
    @Transient
    private int showedLessonNum=0;//该类别下的向前台显示的课程数
    
    
    @ManyToMany(fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "hj_lessonType_examination",
            joinColumns = {
                @JoinColumn(name = "lessonType_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "exam_id", referencedColumnName = "id")})
    private List<Examination> exams;//关联的考试
    @Transient
    private List<Lesson> mostLessons;
    @Transient
    private List<Lesson> lessons;

	@Transient
    private boolean selected = false;
    @Column(name = "picture")
    @Expose
    private String picture = "servlet/ShowImage?id=100";
    
    /**
     * 0下架 1上架
     */
    @Basic(optional = false)
    @Column(name = "enabled")
    private boolean enabled;
    
    /**
     * 0免审批状态 1初始状态 2待审批 3审批中 4通过 5驳回
     */
    @Column(name = "approved")
    @Expose
    private int approved=-1;

	@Column(name = "price")
    @Expose
    private double price;
    
    @Column(name = "MD5")
    @Expose
    private String MD5;
    
    @Column(name = "version")
    @Expose
    private int version = 0;
    
    @Column(name = "jsonDownLoadUrl")
    @Expose
    private String jsonDownLoadUrl;
    
    @Column(name = "jsonFilePath")
    @Expose
    private String jsonFilePath;
    
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    
    @Column(name = "teacher")
    private String teacherName;
    //课程所属类型（包含类型的父节点）
    @Lob
    @Column(name = "labelStr")
    @Expose
    private String labelStr;
    
    @Basic(optional = false)
	@Column(name = "available_begain")
	@Temporal(TemporalType.TIMESTAMP)
	@Expose
	private Date availableBegain = new Date();

	@Basic(optional = false)
	@Column(name = "available_end")
	@Temporal(TemporalType.TIMESTAMP)
	private Date availableEnd = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 365L * 5);

	//课程需要消耗的总积分
	@Column(name = "totalScorePaid")
    @Expose
	private long totalScorePaid;
	
	//	课程资料里的总课时数
	@Column(name = "totalClassNum")
    @Expose
	private Double totalClassNum = 0D;

	//是否开放中
	@Transient
	private boolean ifOpen;
	//课程直属类型
	@Lob
    @Column(name = "course_type_str")
    @Expose
    private String courseTypeStr;
	
	//课程直属类型中文名称
	@Lob
    @Column(name = "course_type_cn")
    @Expose
    private String courseTypeCN;
	
	//“1”赠送课程，“2”非赠送课程
	@Column(name = "classType")
    @Expose
    private String classType;
	
	//学习人数
	@Column(name = "user_num")
    @Expose
    private int userNum=0;
	
	//有效天数
	@Transient
	private long effectiveDays;
	
	/**
	 * 关联练习数目
	 */
	@Column(name = "practice_num")
    @Expose
    private int practiceNum=0;
	
	/**
	 * 审批人
	 */
	@OneToOne
	@JoinColumn(name = "approveTeacher_id")
	private Teacher approveTeacher;
	
	/**
	 * 审批理由
	 */
	@Lob
	@Column(name = "reason")
    @Expose
	private String reason;
	
	/**
	 * 课程下所有资料总时长
	 */
	@Column(name = "total_time")
    @Expose
	private long totalTime=0;
	
    /**
     * 0公开 1私密
     */
    @Basic(optional = false)
    @Column(name = "secreted")
    private boolean secreted;
    
    /**
     * 部门
     */
    @Lob
    @Column(name = "group_str")
    private String groupStr = "";
    
    @Column(name = "business_id")
    @Expose
	private String businessId;
    
    public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getGroupStr() {
		return groupStr;
	}

	public void setGroupStr(String groupStr) {
		this.groupStr = groupStr;
	}

	public boolean isSecreted() {
		return secreted;
	}

	public void setSecreted(boolean secreted) {
		this.secreted = secreted;
	}

	public long getEffectiveDays() {
		return DateUtil.getDayNumBetweenTwoDate(new Date(), this.availableEnd);
	}

	public void setEffectiveDays(long effectiveDays) {
		this.effectiveDays = effectiveDays;
	}

	public int getUserNum() {
		return userNum;
	}

	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}

	public int getPracticeNum() {
		return practiceNum;
	}

	public void setPracticeNum(int practiceNum) {
		this.practiceNum = practiceNum;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getClassType() {
		return classType;
	}
	
	public void setClassType(String classType) {
		this.classType = classType;
	}

	public String getCourseTypeStr() {
		return courseTypeStr;
	}

	public void setCourseTypeStr(String courseTypeStr) {
		this.courseTypeStr = courseTypeStr;
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
	
	public Double getTotalClassNum() {
		return totalClassNum;
	}

	public void setTotalClassNum(Double totalClassNum) {
		this.totalClassNum = totalClassNum;
	}

	public long getTotalScorePaid() {
		return totalScorePaid;
	}

	public void setTotalScorePaid(long totalScorePaid) {
		this.totalScorePaid = totalScorePaid;
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

	public String getLabelStr() {
		return labelStr;
	}

	public void setLabelStr(String labelStr) {
		this.labelStr = labelStr;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public String getMD5() {
		return MD5;
	}

	public void setMD5(String mD5) {
		MD5 = mD5;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getJsonDownLoadUrl() {
		return jsonDownLoadUrl;
	}

	public void setJsonDownLoadUrl(String jsonDownLoadUrl) {
		this.jsonDownLoadUrl = jsonDownLoadUrl;
	}

	public String getJsonFilePath() {
		return jsonFilePath;
	}

	public void setJsonFilePath(String jsonFilePath) {
		this.jsonFilePath = jsonFilePath;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

    public int getApproved() {
		return approved;
	}

	public void setApproved(int approved) {
		this.approved = approved;
	}
	
	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public LessonType() {
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

    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }

    public List<AdminInfo> getAdmins() {
        return admins;
    }

    public void setAdmins(List<AdminInfo> admins) {
        this.admins = admins;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public String getDescription1() {
        return description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public List<Lesson> getMostLessons() {
        ILessonDAO ls = SpringHelper.getSpringBean("LessonDAO");
        this.mostLessons = ls.findMostLessonByType(this.getId());
        return mostLessons;
    }

    public void setMostLessons(List<Lesson> mostLessons) {
        this.mostLessons = mostLessons;
    }

    public List<Lesson> getLessons() {
		return lessons;
	}

	public void setLessons(List<Lesson> lessons) {
		this.lessons = lessons;
	}
    
    public String getExamStr() {
        return examStr;
    }

    public void setExamStr(String examStr) {
        this.examStr = examStr;
    }

    public List<Examination> getExams() {
//        exams = new LinkedList<>();
//        if (this.examStr != null) {
//            String[] strs = examStr.split(";");
//            IExaminationDAO examDAO = SpringHelper.getSpringBean("ExaminationDAO");
//            for (String str : strs) {
//                if (str != null) {
//                    Examination exam = examDAO.findExamination(str);
//                    if (exam != null) {
//                        exams.add(exam);
//                    }
//                }
//            }
//        }
        return exams;
    }

    public void setExams(List<Examination> exams) {
        this.exams = exams;
    }

    public Double getOpenExamNum() {
        return openExamNum;
    }

    public void setOpenExamNum(Double openExamNum) {
        this.openExamNum = openExamNum;
    }

    public int getShowedLessonNum() {
        ILessonDAO dao=SpringHelper.getSpringBean("LessonDAO");
        int num=(int)dao.getShowedLessonNumByType(id);
        this.showedLessonNum=num;
        return showedLessonNum;
    }

    public void setShowedLessonNum(int showedLessonNum) {
        this.showedLessonNum = showedLessonNum;
    }

    public int getLessonNum() {
        ILessonDAO dao=SpringHelper.getSpringBean("LessonDAO");
        int num=(int)dao.getLessonNumByType(id);
        this.lessonNum=num;
        return lessonNum;
    }

    public void setLessonNum(int lessonNum) {
        this.lessonNum = lessonNum;
    }
    
    public Teacher getApproveTeacher() {
		return approveTeacher;
	}

	public void setApproveTeacher(Teacher approveTeacher) {
		this.approveTeacher = approveTeacher;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public long getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}

	public String getCourseTypeCN() {
		return courseTypeCN;
	}

	public void setCourseTypeCN(String courseTypeCN) {
		this.courseTypeCN = courseTypeCN;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
    public int compareTo(Object o) {
        LessonType cq = (LessonType) o;
        if (this.getOrd() == cq.getOrd()) {
            return this.getId().compareTo(cq.getId());
        } else {
            return this.getOrd() - cq.getOrd();
        }
    }

    @Override
    public boolean equals(Object object) {
        LessonType other = (LessonType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.model.LessonType[ id=" + id + " ]";
    }
    
    public static String getOrderStr(int order) {
		if(order==0){
			return "genTime";
		}else if(order==1){
			return "userNum";
		}else if(order==2){
			return "totalScorePaid";
		}else if(order==3){
			return "name";
		}else if(order==4){
			return "teacher.name";
		}else{
			return null;
		}
	}
}
