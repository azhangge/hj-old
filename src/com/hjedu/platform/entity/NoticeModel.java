package com.hjedu.platform.entity;

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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.eclipse.persistence.annotations.Noncacheable;

import com.google.gson.annotations.Expose;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.entity.DictionaryModel;

/**
 * 
 * 系统通知表
 * 用户模块
 * 
 */
@Entity
@Table(name = "y_notice")
public class NoticeModel {

    @Id
    @Column(name = "id")
    @Expose
    private String id = UUID.randomUUID().toString();
    @Lob
    @Column(name = "title")
    @Expose
    private String title;
    @Lob
    @Column(name = "content")
    @Expose
    private String content;
    @Column(name = "ord")
    private int ord = 0;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdate")
    @Expose
    private Date createDate = new Date();
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modifydate")
    @Expose
    private Date modifyDate = new Date();
    @ManyToOne
    @JoinColumn(name = "admin_id")
    AdminInfo admin;
    @Column(name = "count")
    @Expose
    private long count = 0;
    @Transient
    boolean selected = false;
    @Lob
    @Column(name = "group_str")
    private String groupStr = "";
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "notice_department",
            joinColumns = {
                @JoinColumn(name = "notice_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "department_id", referencedColumnName = "id")})
    private List<DictionaryModel> departments;

//    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
//    @Noncacheable
//    @JoinTable(name = "notice_user",
//            joinColumns = {
//                @JoinColumn(name = "notice_id", referencedColumnName = "id")},
//            inverseJoinColumns = {
//                @JoinColumn(name = "user_id", referencedColumnName = "id")})
//    private List<BbsUser> users;
    
	@Basic(optional = false)
	@Column(name = "isreleased")
    Boolean isReleased = false;
	
	@Column(name = "business_id")
	private String businessId;
    
    public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	

    public Boolean getIsReleased() {
		return isReleased;
	}

	public void setIsReleased(Boolean isReleased) {
		this.isReleased = isReleased;
	}

//	public List<BbsUser> getUsers() {
//		return users;
//	}
//
//	public void setUsers(List<BbsUser> users) {
//		this.users = users;
//	}

	public List<DictionaryModel> getDepartments() {
		return departments;
	}

	public void setDepartments(List<DictionaryModel> departments) {
		this.departments = departments;
	}

	public String getGroupStr() {
		return groupStr;
	}

	public void setGroupStr(String groupStr) {
		this.groupStr = groupStr;
	}

	public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AdminInfo getAdmin() {
        return admin;
    }

    public void setAdmin(AdminInfo admin) {
        this.admin = admin;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int compareTo(Object o) {
        NoticeModel ob = (NoticeModel) o;
        if (ob.getOrd() > this.getOrd()) {
            return 1;
        } else if (ob.getOrd() == this.getOrd()) {
            return 0;
        } else {
            return -1;
        }
    }
}
