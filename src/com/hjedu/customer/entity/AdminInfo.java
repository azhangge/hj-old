package com.hjedu.customer.entity;

import com.google.gson.annotations.Expose;
import com.hjedu.course.entity.LessonType;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.Examination;
import com.hjedu.platform.entity.CasusModel;
import com.huajie.util.SpringHelper;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import javax.persistence.Transient;
import org.eclipse.persistence.annotations.Noncacheable;
/**
 * 
 * 管理员表
 * 用户模块
 * 
 */
@Entity
@Table(name = "y_admin")
public class AdminInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id",length = 200)
    private String id = UUID.randomUUID().toString();
    @Basic(optional = false)
    @Column(name = "urn")
    private String urn;
    @Basic(optional = false)
    @Column(name = "pwd")
    private String pwd;
    @Basic(optional = false)
    @Column(name = "grp")
    private String grp;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "tel")
    private String tel;
    @Lob
    @Column(name = "authstr")
    private String authstr = "";
    @Column(name = "persona")
    private String persona = "";
    @Basic(optional = false)
    @Column(name = "enabled")
    private Boolean enabled;
    @Basic(optional = false)
    @Column(name = "checked")
    private Boolean checked = false;
    @Basic(optional = false)
    @Column(name = "ltime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ltime;
    @Column(name = "ctime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ctime;//current time
    @Column(name = "nickname")
    private String nickname;
    @Basic(optional = false)
    @Column(name = "genTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genTime;
    @OneToMany(mappedBy = "admin", cascade = CascadeType.MERGE)
    List<CasusModel> casuses;

    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @Noncacheable
    @JoinTable(name = "exam_admin_module",
            joinColumns = {
                @JoinColumn(name = "admin_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "module_id", referencedColumnName = "id")})
    private List<ExamModuleModel> modules;

    @ManyToMany(cascade = CascadeType.MERGE)
    @Noncacheable
    @JoinTable(name = "exam_admin_examination",
            joinColumns = {
                @JoinColumn(name = "admin_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "exam_id", referencedColumnName = "id")})
    private List<Examination> exams;
    
    @ManyToMany(cascade = CascadeType.MERGE)
    @Noncacheable
    @JoinTable(name = "exam_admin_department",
            joinColumns = {
                @JoinColumn(name = "admin_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "department_id", referencedColumnName = "id")})
    private List<DictionaryModel> dics;
    
    @ManyToMany(cascade = CascadeType.MERGE)
    @Noncacheable
    @JoinTable(name = "exam_admin_lesson_type",
            joinColumns = {
                @JoinColumn(name = "admin_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "lesson_type_id", referencedColumnName = "id")})
    private List<LessonType> lessonTypes;
    
    /**
     * 轮播图版本号（修改轮播图次数，初始值为0，修改一次递增1）
     */
    @Column(name = "carouselVersion")
    @Expose
    private long carouselVersion = 0;
    
    @Column(name = "available_time")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date availableTime = new Date();//有效期起始时间
    @Column(name = "expire_time")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date expireTime = new Date(System.currentTimeMillis() + ((Long) SpringHelper.getSpringBean("user_valid_year")) * 365 * 24 * 60 * 60 * 1000);//默认有效期5年

    @Transient
    private boolean ifInValidTime = true;//判断是否在有效期内
    
    @Transient
    String authCnStr = "";

    @Transient
    private boolean selected = false;
    
    @Column(name = "superId")
    private String superId = "";
    
    @Column(name = "business_id")
	private String businessId;
    
    public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

    public AdminInfo() {
    }
    
    public String getSuperId() {
		return superId;
	}

	public void setSuperId(String superId) {
		this.superId = superId;
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

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    
    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAuthstr() {
        return authstr;
    }

    public void setAuthstr(String authstr) {
        this.authstr = authstr;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public String getGrp() {
        return grp;
    }

    public void setGrp(String grp) {
        this.grp = grp;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    
    public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public Date getLtime() {
        return ltime;
    }

    public void setLtime(Date ltime) {
        this.ltime = ltime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public String getAuthCnStr() {
        Map auths = SpringHelper.getSpringBean("authorities");
        StringBuilder cn = new StringBuilder();
        if (authstr != null) {
            String[] ss = this.authstr.split(";");
            if (ss != null) {
                for (String s : ss) {
                    if (s != null && (!"".equals(s))) {
                        Object c = auths.get(s);
                        if (c != null) {
                            cn.append(c.toString());
                            cn.append(" ");
                        }
                        
                    }
                }
            }
        }
        authCnStr = cn.toString();
        return authCnStr;
    }

    public void setAuthCnStr(String authCnStr) {
        this.authCnStr = authCnStr;
    }

    public List<ExamModuleModel> getModules() {
        return modules;
    }

    public void setModules(List<ExamModuleModel> modules) {
        this.modules = modules;
    }

    public List<Examination> getExams() {
        return exams;
    }

    public void setExams(List<Examination> exams) {
        this.exams = exams;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public List<CasusModel> getCasuses() {
        return casuses;
    }

    public void setCasuses(List<CasusModel> casuses) {
        this.casuses = casuses;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public List<DictionaryModel> getDics() {
        return dics;
    }

    public void setDics(List<DictionaryModel> dics) {
        this.dics = dics;
    }

    public List<LessonType> getLessonTypes() {
        return lessonTypes;
    }

    public void setLessonTypes(List<LessonType> lessonTypes) {
        this.lessonTypes = lessonTypes;
    }

	public long getCarouselVersion() {
		return carouselVersion;
	}

	public void setCarouselVersion(long carouselVersion) {
		this.carouselVersion = carouselVersion;
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

}
