package com.hjedu.examination.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.eclipse.persistence.annotations.Noncacheable;

import com.google.gson.annotations.Expose;
import com.hjedu.course.dao.ICourseTypeDAO;
import com.hjedu.course.entity.LessonType;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

/**
 * 课程模块
 * 课程分类
 * @author huajie
 */
@Entity
@Table(name = "course_type")
public class CourseType implements Serializable, Comparable<Object> {

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
    
    @Column(name = "floor_show")
    private Boolean floorShow = false;
    
    @Column(name = "description")
    private String description;
    
    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genTime = new Date();
    
    @Column(name = "ancestors", length = 900)
    private String ancestors = "";

    @Column(name = "front_show")
    private Boolean frontShow = true;
    
    @Column(name = "type_level")
    private Integer typeLevel;

    public Integer getTypeLevel() {
		return typeLevel;
	}

	public void setTypeLevel(Integer typeLevel) {
		this.typeLevel = typeLevel;
	}

	@Lob
    @Column(name = "group_str")
    private String groupStr = "";

    @Transient
    private boolean selected = false;
    
    @Transient
    private List<CourseType> sons;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "course_to_courseType",
            joinColumns = {
                @JoinColumn(name = "type_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "course_id", referencedColumnName = "id")})
    private List<LessonType> LessonTypes;
    
    @Column(name = "navigation_show")
    private Boolean navigationShow = false;
    
    @Column(name = "picture")
    @Expose
    private String picture = "servlet/ShowImage?id=100";
    
    @Column(name = "business_id")
    @Expose
	private String businessId;
    
    public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
    
    private String totalClassNum;
    
    private boolean ifRequired;

	public boolean isIfRequired() {
		return ifRequired;
	}

	public void setIfRequired(boolean ifRequired) {
		this.ifRequired = ifRequired;
	}

	public String getTotalClassNum() {
		return totalClassNum;
	}

	public void setTotalClassNum(String totalClassNum) {
		this.totalClassNum = totalClassNum;
	}

	public List<LessonType> getLessonTypes() {
		return LessonTypes;
	}

	public void setLessonTypes(List<LessonType> lessonTypes) {
		LessonTypes = lessonTypes;
	}

	public String getPicture() {
		if(picture==null){
			return "servlet/ShowImage?id=100";
		}
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Boolean getNavigationShow() {
		return navigationShow;
	}

	public void setNavigationShow(Boolean navigationShow) {
		this.navigationShow = navigationShow;
	}

	public CourseType() {
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

    public List<CourseType> getSons() {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    	ICourseTypeDAO CourseTypeDAO = SpringHelper.getSpringBean("CourseTypeDAO");
    	this.sons =  CourseTypeDAO.findAllSonCourseType(this.id,businessId);
        return sons;
    }

    public void setSons(List<CourseType> sons) {
        this.sons = sons;
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

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Boolean getFrontShow() {
        return frontShow;
    }

    public void setFrontShow(Boolean frontShow) {
        this.frontShow = frontShow;
    }

    public String getGroupStr() {
        return groupStr;
    }

    public void setGroupStr(String groupStr) {
        this.groupStr = groupStr;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CourseType)) {
            return false;
        }
        CourseType other = (CourseType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        CourseType oo = (CourseType) o;
        return this.ord - oo.getOrd();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "com.huajie.model.CourseType[ name=" + name + " ]";
    }
}
