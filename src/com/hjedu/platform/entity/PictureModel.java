package com.hjedu.platform.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.*;

import com.google.gson.annotations.Expose;

/**
 * 
 * 轮播图表
 * 用户模块
 *
 */
@Entity
@Table(name = "y_picture")
public class PictureModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id=UUID.randomUUID().toString();
    @Lob
    @Column(name = "title")
    private String title;
    @Lob
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "genTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genTime=new Date();
    /**
     * 轮播图版本号（修改轮播图次数，初始值为0，修改一次递增1）
     */
    @Column(name = "version")
    @Expose
    private long version = 0;
    
    @Column(name = "lesson_type_id")
    @Expose
    private String lessonTypeId;
    
    //@ManyToOne(cascade={CascadeType.REFRESH},fetch= FetchType.LAZY)
    //@JoinColumn(name = "admin_id")
    //private AdminInfo editor;
    
    @Column(name = "admin_id")
    private String adminId;
    
    @Column(name = "backcolor")
    private String backcolor="00000";
    
    @Column(name = "linkType")
    private String linkType="inner";
    
    @Column(name = "business_id")
    private String businessId;
    
    public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public String getBackcolor() {
		return backcolor;
	}

	public void setBackcolor(String backcolor) {
		this.backcolor = backcolor;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getLessonTypeId() {
		return lessonTypeId;
	}

	public void setLessonTypeId(String lessonTypeId) {
		this.lessonTypeId = lessonTypeId;
	}

	@Lob
    @Column(name = "url")
    private String url;
    @Lob
    @Column(name = "link")
    private String link;
    
    @Column(name = "ord")
    private int ord;

    public PictureModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }

    public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
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
        if (!(object instanceof PictureModel)) {
            return false;
        }
        PictureModel other = (PictureModel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "com.huajie.model.YPicture[ id=" + id + " ]";
    }
    
}
