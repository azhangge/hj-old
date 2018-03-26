package com.hjedu.examination.entity;

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
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.eclipse.persistence.annotations.Noncacheable;

import com.google.gson.annotations.Expose;
import com.hjedu.course.entity.Lesson;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.entity.buffet.BuffetExamination;
import com.hjedu.examination.entity.module2.ModuleExamination2;
import com.hjedu.examination.service.impl.ExamDepartmentService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.ObjectWithAdmin;
import com.huajie.util.SpringHelper;
/**
 * 部门模块
 * 凡是需要关联的一对多或者多对多关系，都应设为EAGER，LAZY关系或者不用，或者设置为getter方法中使用DAO获取
 * 关联还是导致数据库加载缓慢、外健约束导致数据库表破坏的原因
 * 否则在分布式环境下进行被序列化后数据库SESSION将失效，导致获取LAZY关联全部失效
 * @author huajie
 */
@Entity
@Table(name = "exam_department")
public class DictionaryModel implements Serializable, Comparable, Cloneable,ObjectWithAdmin {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();
    @Column(name = "name")
    private String name;
    @Column(name = "father_id")
    private String fatherId;
    @Column(name = "ord")
    private int ord;
    @Column(name = "tel")
    private String tel;
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "ancestors")
    private String ancestors;
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genTime = new Date();
    @Column(name = "module_str")
    private String moduleStr;
    @Column(name = "front_show")
    private Boolean frontShow = true;
    @Column(name = "as_default")
    private boolean asDefault = false;
    @Column(name = "default_days")
    private int defaultDays = 0;//该部门成员默认有效期

    @Column(name = "business_id")
    private String businessId;

    public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date availableTime = new Date();//有效期起始时间
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date expireTime = new Date(System.currentTimeMillis() + ((long) 5) * 365 * 24 * 60 * 60 * 1000);//默认有效期100年
    @Transient
    private boolean ifInValidTime = true;

    @Transient
    private List<DictionaryModel> familyTree;
    @Transient
    private boolean selected = false;
    @Transient
    private List<DictionaryModel> sons;
    @Transient
    private int deep;
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "exam_admin_department",
            joinColumns = {
                @JoinColumn(name = "department_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "admin_id", referencedColumnName = "id")})
    private List<AdminInfo> admins;
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "exam_lesson_dic",
            joinColumns = {
                @JoinColumn(name = "lesson_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "dic_id", referencedColumnName = "id")})
    private List<Lesson> lessons;

    public List<AdminInfo> getAdmins() {
		return admins;
	}

	public void setAdmins(List<AdminInfo> admins) {
		this.admins = admins;
	}
	
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "exam_examination_department",
            joinColumns = {
                @JoinColumn(name = "department_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "examination_id", referencedColumnName = "id")})
    private List<Examination> examinations;
	
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "exam_examination_department",
            joinColumns = {
                @JoinColumn(name = "department_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "examination_id", referencedColumnName = "id")})
    private List<ModuleExamination2> moduleExamination2s;
	
	public List<Examination> getExaminations() {
		return examinations;
	}

	public void setExaminations(List<Examination> examinations) {
		this.examinations = examinations;
	}

	public List<ModuleExamination2> getModuleExamination2s() {
		return moduleExamination2s;
	}

	public void setModuleExamination2s(List<ModuleExamination2> moduleExamination2s) {
		this.moduleExamination2s = moduleExamination2s;
	}

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "exam_examination_department",
            joinColumns = {
                @JoinColumn(name = "department_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "examination_id", referencedColumnName = "id")})
    private List<BuffetExamination> buffetExaminations;

	public List<BuffetExamination> getBuffetExaminations() {
		return buffetExaminations;
	}

	public void setBuffetExaminations(List<BuffetExamination> buffetExaminations) {
		this.buffetExaminations = buffetExaminations;
	}

    @ManyToMany(cascade = CascadeType.MERGE)
    @Noncacheable
    @JoinTable(name = "notice_department",
            joinColumns = {
                @JoinColumn(name = "department_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "notice_id", referencedColumnName = "id")})
    private List<DictionaryModel> dics;
	
	public DictionaryModel() {
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getDefaultDays() {
        return defaultDays;
    }

    public void setDefaultDays(int defaultDays) {
        this.defaultDays = defaultDays;
    }

    public List<DictionaryModel> getFamilyTree() {
        return familyTree;
    }

    public void setFamilyTree(List<DictionaryModel> familyTree) {
        this.familyTree = familyTree;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    public String getModuleStr() {
        return moduleStr;
    }

    public void setModuleStr(String moduleStr) {
        this.moduleStr = moduleStr;
    }

    public Boolean getFrontShow() {
        return frontShow;
    }

    public void setFrontShow(Boolean frontShow) {
        this.frontShow = frontShow;
    }

    public List<DictionaryModel> getSons() {
        return sons;
    }

    public void setSons(List<DictionaryModel> sons) {
        this.sons = sons;
    }

    public int getDeep() {
    	String roodId = this.businessId;
        ExamDepartmentService dicDAO = SpringHelper.getSpringBean("ExamDepartmentService");
        DictionaryModel dm = this;
        int i = 1;
        while (!roodId.equals(dm.getFatherId())) {
            dm = dicDAO.findDictionaryModel(dm.getFatherId(),this.getBusinessId());
            i++;
        }
        this.deep = i;
        return deep;
    }

    public void setDeep(int deep) {
        this.deep = deep;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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

    public boolean isAsDefault() {
        return asDefault;
    }

    public void setAsDefault(boolean asDefault) {
        this.asDefault = asDefault;
    }

    @Override
    public int compareTo(Object o) {
        DictionaryModel oo = (DictionaryModel) o;
        return this.ord - oo.getOrd();
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
        if (!(object instanceof DictionaryModel)) {
            return false;
        }
        DictionaryModel other = (DictionaryModel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public Object clone() {
        Object o = null;
        try {
            o = (DictionaryModel) super.clone();//Object 中的clone()识别出你要复制的是哪一个对象。 
        } catch (CloneNotSupportedException e) {
            System.out.println(e.toString());
        }
        return o;
    }

    @Override
    public String toString() {
        return "com.huajie.model.DictionaryModel[ id=" + id + " ]";
    }
}
