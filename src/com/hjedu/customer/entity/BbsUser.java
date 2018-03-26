package com.hjedu.customer.entity;

import com.google.gson.annotations.Expose;
import com.hjedu.businessuser.entity.BusinessUser;
import com.hjedu.course.dao.ILessonLogDAO;
import com.hjedu.course.entity.LessonLog;
import com.hjedu.customer.dao.IBbsFileDAO;
import com.hjedu.customer.dao.IBbsUserGradeDAO;
import com.hjedu.examination.dao.IDictionaryDAO;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.service.impl.ExamDepartmentService;
import com.hjedu.platform.dao.IBbsMessageDAO;
import com.hjedu.platform.dao.IBbsTalkDAO;
import com.hjedu.platform.dao.IBbsThreadDAO;
import com.hjedu.platform.dao.INoticeUserDAO;
import com.hjedu.platform.dao.IThreadTradeDAO;
import com.hjedu.platform.entity.BbsFileModel;
import com.hjedu.platform.entity.BbsThreadTrade;
import com.hjedu.platform.entity.BbsUserGrade;
import com.hjedu.platform.entity.BbsZone;
import com.hjedu.platform.entity.DictCity;
import com.hjedu.platform.entity.DictProvince;
import com.hjedu.platform.entity.NoticeModel;
import com.huajie.app.util.StringUtil;
import com.huajie.ejb.IPSeekerServiceRemote;
import com.huajie.util.CookieUtils;
import com.huajie.util.DateHelper;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;
import org.eclipse.persistence.annotations.Noncacheable;

/**
 * 
 * 前台用户表
 * 用户模块
 *
 */
@Entity
@Table(name = "rerebbs_user")
public class BbsUser implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@Id
    @Column(name = "id")
    @Expose
    private String id = UUID.randomUUID().toString();
    @Column(name = "username")
    @Expose
    private String username;
    @Column(name = "type")
    @Expose
    private String type;
    @Column(name = "password")
    @Expose
    private String password;
    @Column(name = "hand_password")
    @Expose
    private String handPassword;
    @Column(name = "email")
    @Expose
    private String email="";
    @Column(name = "tel")
    @Expose
    private String tel;
    @Column(name = "reg_ip")
    @Expose
    private String regIp;
    @Column(name = "last_ip")
    @Expose
    private String lastIp;
    @Column(name = "app_reg_time", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Expose
    private Date appRegTime;
    @Column(name = "reg_time", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Expose
    private Date regTime = new Date();
    @Column(name = "birth_day", nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    @Expose
    private Date birthDay = new Date();
    @Column(name = "last_time", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Expose
    private Date lastTime = new Date();//Last time to login.
    @Column(name = "ctime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date currentTime = new Date();//current time
/*    @Column(name = "name")
    @Expose*/
    private String name;
/*    @Column(name = "gender")
    @Expose*/
    private String gender;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province")
    private DictProvince province;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city")
    private DictCity city;
    @Column(name = "id_num")
    private String idNum;
    @Column(name = "activated")
    @Expose
    private Boolean activated = true;
    @Column(name = "enabled")
    @Expose
    private Boolean enabled = true;
    @Column(name = "pid")
    @Expose
    private String pid = "";//身份证号
    @Column(name = "custom_id")
    @Expose
    private String cid = "";
    @Column(name = "group1")
    private String group1;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "rerebbs_zonemag",
            joinColumns = {
                @JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "zone_id", referencedColumnName = "id")})
    List<BbsZone> managedZone;

//    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
//    @Noncacheable
//    @JoinTable(name = "notice_user",
//            joinColumns = {
//                @JoinColumn(name = "user_id", referencedColumnName = "id")},
//            inverseJoinColumns = {
//                @JoinColumn(name = "notice_id", referencedColumnName = "id")})
//    private List<NoticeModel> notices;
    
    @Column(name = "session_id")
    private String sessionId;//记录当前用户SessionID
    @Column(name = "exam_id")
    private String examId;//记录当前用户正在考试的ID
    @Column(name = "module2Id")
    private String module2Id;//记录当前用户正在章节练习的ID

    @Column(name = "score")
    @Expose
    private long score = 1000;//积分
    @Column(name = "mark_del")
    private boolean markDel;
    @Column(name = "address")
    @Expose
    private String address;
    @Column(name = "qq")
    @Expose
    private String qq;
    @Column(name = "pic_url")
    @Expose
    private String picUrl = "";
    @Column(name = "pwd_encoder")
    private String pwdEncoder = "none";//md5_32|md5_16|des|none
    @Column(name = "group_id")
    private String groupId;
    @Lob
    @Column(name = "group_str")
    private String groupStr;

    @ManyToMany(mappedBy = "managers", fetch = FetchType.LAZY)
    @Noncacheable
    private List<BbsZone> managedZones;
    @Column(name = "total_length")
    long totalLength = 100;//以M为单位,默认为5120M
    @Column(name = "checked")
    private Boolean checked = true;
    @Column(name = "custom1")
    private String custom1;
    @Column(name = "custom2")
    private String custom2;
    @Column(name = "custom3")
    private String custom3;
    @Column(name = "custom4")
    private String custom4;
    @Column(name = "custom5")
    private String custom5;
    @Column(name = "finance_balance")
    @Expose
    private double financeBalance = 0;

    @Column(name = "available_time")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date availableTime = new Date();//有效期起始时间
    @Column(name = "expire_time")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date expireTime = new Date(System.currentTimeMillis() + ((Long) SpringHelper.getSpringBean("user_valid_year")) * 365 * 24 * 60 * 60 * 1000);//默认有效期5年
    
    /**
     * 外部ID，主要用于记录从外部集成用户的ID，如ucenter
     */
    @Column(name = "external_id")
    String externalId;
    
    /**
     * 职位
     */
    @Column(name = "position")
    String position;

    /**
     * 所属公司
     */
    @Column(name = "company")
    private String company;
    
    /**
     * 收藏课程id，多个以;隔开
     */
    @Lob
    @Column(name = "collection_courses")
    private String collectionCourses;

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@OneToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    
    public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	@Column(name = "login_count")
    @Expose
    private Integer loginCount = 0;

	public Integer getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}

	//岗位类别
	@Column(name = "post_type")
    @Expose
    private String postType;
	
	public String getPostType() {
		return postType;
	}

	public void setPostType(String postType) {
		this.postType = postType;
	}

	@Column(name = "business_id")
    @Expose
    private String businessId;

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	@Transient
    private Integer threadCount;//贴子数
    @Transient
    private Integer talkCount;//发言数（包括帖子+回复）
    @Transient
    private Integer age;
    @Transient
    private String regIpAddr;
    @Transient
    private String lastIpAddr;
    @Transient
    private List<String> buyedThreadIds;
    @Transient
    private long unReadedMsgNum;
    @Transient
    private long unReadedNoticeNum;
    @Transient
    double realLength;//以M为单位
    @Transient
    String gradeName;
    @Transient
    DictionaryModel group;//deprecated
    @Transient
    List<DictionaryModel> groups;
    @Transient
    @Expose
    private String groupCnStr = "";
    @Transient
    private String lessonStr = "";
    @Lob
    @Column(name = "lesson_type_str")
    private String lessonTypeStr = "";
    @Transient
    private boolean ifInValidTime = true;//判断是否在有效期内

    @Transient
    private long examScore = 0L;
    @Transient
    private long lessonScore = 0L;

    @Transient
    private long examScoreCM = 0L;
    @Transient
    private long lessonScoreCM = 0L;

    @Transient
    private long examScoreCY = 0L;
    @Transient
    private long lessonScoreCY = 0L;

    @Transient
    private boolean selected = false;
    
    @Transient
    private boolean ifFlow = false;

    // Constructors
    /**
     * default constructor
     */
    public BbsUser() {
    }

    /**
     * minimal constructor
     */
    public BbsUser(Timestamp regTime) {
        this.regTime = regTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHandPassword() {
		return handPassword;
	}

	public void setHandPassword(String handPassword) {
		this.handPassword = handPassword;
	}

	public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return this.tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPwdEncoder() {
        return pwdEncoder;
    }

    public void setPwdEncoder(String pwdEncoder) {
        this.pwdEncoder = pwdEncoder;
    }

    public String getRegIp() {
        return this.regIp;
    }

    public void setRegIp(String regIp) {
        this.regIp = regIp;
    }

    public String getLastIp() {
        return this.lastIp;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }
    
    public Date getAppRegTime() {
		return appRegTime;
	}

	public void setAppRegTime(Date appRegTime) {
		this.appRegTime = appRegTime;
	}

	public Date getRegTime() {
        return this.regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getFinanceBalance() {
        return financeBalance;
    }

    public void setFinanceBalance(double financeBalance) {
        this.financeBalance = financeBalance;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return this.age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getIdNum() {
        return this.idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public Boolean getActivated() {
        return this.activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getThreadCount() {
        IBbsThreadDAO tDAO = SpringHelper.getSpringBean("BbsThreadDAO");
        threadCount = tDAO.findByGenBy(this, 0, 9999999).size();
        return threadCount;
    }

    public void setThreadCount(Integer threadCount) {
        this.threadCount = threadCount;
    }

    public Integer getTalkCount() {
        IBbsTalkDAO talkDAO = SpringHelper.getSpringBean("BbsTalkDAO");
        talkCount = talkDAO.findByGenBy(this, 0, 9999999).size();
        return talkCount;
    }

    public void setTalkCount(Integer talkCount) {
        this.talkCount = talkCount;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public boolean isMarkDel() {
        return markDel;
    }

    public void setMarkDel(boolean markDel) {
        this.markDel = markDel;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public List<BbsZone> getManagedZones() {
        return managedZones;
    }

    public void setManagedZones(List<BbsZone> managedZones) {
        this.managedZones = managedZones;
    }

    public String getRegIpAddr() {
        IPSeekerServiceRemote ips = SpringHelper.getSpringBean("ipSeekerService");
        if (regIp != null) {
            this.regIpAddr = ips.seek(this.regIp);
        }
        return regIpAddr;
    }

    public void setRegIpAddr(String ipAddr) {
        this.regIpAddr = ipAddr;
    }

    public String getLastIpAddr() {
        IPSeekerServiceRemote ips = SpringHelper.getSpringBean("ipSeekerService");
        if (lastIp != null) {
            this.lastIpAddr = ips.seek(this.lastIp);
        }
        return lastIpAddr;
    }

    public void setLastIpAddr(String lastIpAddr) {
        this.lastIpAddr = lastIpAddr;
    }

    public long getUnReadedMsgNum() {
        IBbsMessageDAO ips = SpringHelper.getSpringBean("BbsMessageDAO");
        unReadedMsgNum = ips.getUnReadedMsgNumByReceiver(this.id);
        return unReadedMsgNum;
    }

    public void setUnReadedMsgNum(long unReadedMsgNum) {
        this.unReadedMsgNum = unReadedMsgNum;
    }

    public long getUnReadedNoticeNum() {
        INoticeUserDAO ips = SpringHelper.getSpringBean("NoticeUserDAO");
        unReadedNoticeNum = ips.findUnreadNoticeListByUser(this.id, 0, 0, null).size();
        return unReadedNoticeNum;
    }

    public void setUnReadedNoticeNum(long unReadedNoticeNum) {
        this.unReadedNoticeNum = unReadedNoticeNum;
    }
    
    public DictCity getCity() {
        return city;
    }

    public void setCity(DictCity city) {
        this.city = city;
    }

    public DictProvince getProvince() {
        return province;
    }

    public void setProvince(DictProvince province) {
        this.province = province;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getPicUrl() {
//    	if(StringUtil.isEmpty(picUrl)){
//    		picUrl="/servlet/ShowImage?id=100";
//    	}
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public List<BbsZone> getManagedZone() {
        return managedZone;
    }

    public void setManagedZone(List<BbsZone> managedZone) {
        this.managedZone = managedZone;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public List<String> getBuyedThreadIds() {
        IThreadTradeDAO bbsThreadTradeDAO = SpringHelper.getSpringBean("ThreadTradeDAO");
        List<BbsThreadTrade> tss = bbsThreadTradeDAO.findThreadTradeByUsr(id);
        List<String> ts = new LinkedList<>();
        if (tss != null) {
            for (BbsThreadTrade t : tss) {
                ts.add(t.getThread().getId());
            }
        }
        this.buyedThreadIds = ts;
        return this.buyedThreadIds;
    }

    public void setBuyedThreads(List<String> buyedThreads) {
        this.buyedThreadIds = buyedThreads;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getGroup1() {
        return group1;
    }

    public void setGroup1(String group1) {
        this.group1 = group1;
    }

    public String getGroupStr() {
        return groupStr;
    }

    public void setGroupStr(String groupStr) {
        if(StringUtil.isEmpty(groupStr) && StringUtil.isNotEmpty(this.businessId)){
        	groupStr = this.businessId + ";";
    	}
        this.groupStr = groupStr;
    }

    public String getGroupCnStr() {
        List<DictionaryModel> gs = this.getGroups();
        if (gs != null) {
            int size = gs.size();
            int i = 0;
            StringBuilder sb = new StringBuilder();
            for (DictionaryModel dm : gs) {
                i++;
                sb.append(dm.getName());
                if (i < size) {
                    sb.append("、");
                }
            }
            groupCnStr = sb.toString();

        }
        return groupCnStr;
    }

    public void setGroupCnStr(String groupCnStr) {
        this.groupCnStr = groupCnStr;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public DictionaryModel getGroup() {
        try {
            IDictionaryDAO dicDAO = SpringHelper.getSpringBean("DictionaryDAO");
            if (this.getGroupId() != null && !"".equals(this.getGroupId())) {
                String gid = this.getGroupId();
                this.group = dicDAO.findDictionaryModel(gid);
            }
            return group;

        } catch (Exception e) {
            return null;
        }

    }

    public boolean testIfIn(String str) {

        if (str == null) {
            return false;
        }
        if (this.getGroupStr() == null) {
            return false;
        }
        if (str.equals("")) {
            return false;
        }
        if (this.getGroupStr().equals("")) {
            return false;
        }
        if (str.equals(this.getGroupStr())) {
            return true;
        }

        String gids[] = this.getGroupStr().split(";");
        for (String gid : gids) {
            if (gid != null) {
//                Pattern p = Pattern.compile(gid);
//                Matcher m = p.matcher(str);
//                if (m.find()) {
//                    return true;
//                }
                if (str.indexOf(gid) != -1) {
                    return true;
                }
            }
        }

        return false;
    }

    public List<DictionaryModel> getGroups() {
        List<DictionaryModel> dics = new ArrayList<>();
        try {
            ExamDepartmentService dicDAO = SpringHelper.getSpringBean("ExamDepartmentService");
            if (this.getGroupStr() != null && !"".equals(this.getGroupStr())) {
                String gids[] = this.getGroupStr().split(";");
                for (String gid : gids) {
                    if (gid != null) {
                        DictionaryModel groupt = dicDAO.findDictionaryModel(gid,this.businessId);
                        if (groupt != null) {
                            dics.add(groupt);
                        }
                    }
                }
            }
        } catch (Exception e) {
            groups = dics;
            return groups;
        }
        groups = dics;
        return groups;
    }

    public void setGroups(List<DictionaryModel> groups) {
        this.groups = groups;
    }

    public void setGroup(DictionaryModel group) {
        this.group = group;
    }

    public long getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(long totalLength) {
        this.totalLength = totalLength;
    }

    public double getRealLength() {
        IBbsFileDAO cfDAO = SpringHelper.getSpringBean("BbsFileDAO");
        List<BbsFileModel> cfs = cfDAO.findAllClientFileByUsr(this.id);
        double len = 0;
        for (BbsFileModel f : cfs) {
            len += f.getRealLength();
        }
        //System.out.println(this.urn+"本用户的文件总长度是："+len);
        len = len / 1024 / 1024;
        this.realLength = len;
        return realLength;
    }

    public void setRealLength(double realLength) {
        this.realLength = realLength;
    }

    public String getGradeName() {
        IBbsUserGradeDAO gradeDAO = SpringHelper.getSpringBean("BbsUserGradeDAO");
        List<BbsUserGrade> grades = gradeDAO.findAllUserGrade();
        for (BbsUserGrade g : grades) {
            if (this.getScore() >= g.getBegainScore() && this.getScore() <= g.getEndScore()) {
                this.gradeName = g.getGradeName();
                break;
            }
        }
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }

    public String getCustom1() {
        return custom1;
    }

    public void setCustom1(String custom1) {
        this.custom1 = custom1;
    }

    public String getCustom2() {
        return custom2;
    }

    public void setCustom2(String custom2) {
        this.custom2 = custom2;
    }

    public String getCustom3() {
        return custom3;
    }

    public void setCustom3(String custom3) {
        this.custom3 = custom3;
    }

    public String getCustom4() {
        return custom4;
    }

    public void setCustom4(String custom4) {
        this.custom4 = custom4;
    }

    public String getCustom5() {
        return custom5;
    }

    public void setCustom5(String custom5) {
        this.custom5 = custom5;
    }

    public Date getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(Date availableTime) {
        this.availableTime = availableTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public boolean isIfInValidTime() {
        boolean b = true;
        if (this.availableTime != null && this.expireTime != null) {
            long now = System.currentTimeMillis();
            b = now >= this.availableTime.getTime() && now <= this.expireTime.getTime();
        }
        ifInValidTime = b;
        return ifInValidTime;
    }

    public void setIfInValidTime(boolean ifInValidTime) {
        this.ifInValidTime = ifInValidTime;
    }

    public String getLessonStr() {
        StringBuilder sb = new StringBuilder();
        ILessonLogDAO logDAO = SpringHelper.getSpringBean("LessonLogDAO");
        List<LessonLog> logs = logDAO.findLessonLogByUsr(id);
        if (logs != null) {
            for (LessonLog ll : logs) {
                sb.append(ll.getLesson().getId());
                sb.append(";");
            }
        }
        lessonStr = sb.toString();
        return lessonStr;
    }

    public void setLessonStr(String lessonStr) {
        this.lessonStr = lessonStr;
    }

    public String getLessonTypeStr() {
		return lessonTypeStr;
	}
    
	public void setLessonTypeStr(String lessonTypeStr) {
		this.lessonTypeStr = lessonTypeStr;
	}

	public long getExamScore() {
        IExamCaseDAO aDAO = SpringHelper.getSpringBean("ExamCaseDAO");
        examScore = aDAO.getTotalExamCaseBbsScore(id, new Date(0), new Date());
        return examScore;
    }

    public void setExamScore(long examScore) {
        this.examScore = examScore;
    }

    public long getLessonScore() {
        ILessonLogDAO aDAO = SpringHelper.getSpringBean("LessonLogDAO");
        lessonScore = aDAO.getTotalLessonLogBbsScore(id, new Date(0), new Date());
        return lessonScore;
    }

    public void setLessonScore(long lessonScore) {
        this.lessonScore = lessonScore;
    }

    public long getExamScoreCM() {
        IExamCaseDAO aDAO = SpringHelper.getSpringBean("ExamCaseDAO");
        examScoreCM = aDAO.getTotalExamCaseBbsScore(id, DateHelper.getBeginningOfThisMonth(), new Date());
        return examScoreCM;
    }

    public void setExamScoreCM(long examScoreCM) {
        this.examScoreCM = examScoreCM;
    }

    public long getLessonScoreCM() {
        ILessonLogDAO aDAO = SpringHelper.getSpringBean("LessonLogDAO");
        lessonScoreCM = aDAO.getTotalLessonLogBbsScore(id, DateHelper.getBeginningOfThisMonth(), new Date());
        return lessonScoreCM;
    }

    public void setLessonScoreCM(long lessonScoreCM) {
        this.lessonScoreCM = lessonScoreCM;
    }

    public long getExamScoreCY() {
        IExamCaseDAO aDAO = SpringHelper.getSpringBean("ExamCaseDAO");
        examScoreCY = aDAO.getTotalExamCaseBbsScore(id, DateHelper.getBeginningOfThisYear(), new Date());
        return examScoreCY;
    }

    public void setExamScoreCY(long examScoreCY) {
        this.examScoreCY = examScoreCY;
    }

    public long getLessonScoreCY() {
        ILessonLogDAO aDAO = SpringHelper.getSpringBean("LessonLogDAO");
        lessonScoreCY = aDAO.getTotalLessonLogBbsScore(id, DateHelper.getBeginningOfThisYear(), new Date());
        return lessonScoreCY;
    }

    public void setLessonScoreCY(long lessonScoreCY) {
        this.lessonScoreCY = lessonScoreCY;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getModule2Id() {
        return module2Id;
    }

    public void setModule2Id(String module2Id) {
        this.module2Id = module2Id;
    }

    public boolean isIfFlow() {
		return ifFlow;
	}

	public void setIfFlow(boolean ifFlow) {
		this.ifFlow = ifFlow;
	}

	public String getCollectionCourses() {
		return collectionCourses;
	}

	public void setCollectionCourses(String collectionCourses) {
		this.collectionCourses = collectionCourses;
	}

	public static void main(String args[]) {
        BbsUser bu = new BbsUser();
        bu.setGroupStr("0449f665-6e5d-4dfc-bb77-6e3286c76632;");
        String str = "e65aafcd-ce0a-4a5f-837f-0df33c92ecb5,";
        boolean r = bu.testIfIn(str);
        System.out.println(r);

    }

}
