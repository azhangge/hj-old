package com.hjedu.platform.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.hjedu.course.entity.Chapter;
import com.hjedu.course.entity.HJCourse;
import com.hjedu.course.entity.Lesson;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.dao.IShareDAO;
import com.huajie.util.SpringHelper;
/**
 * 
 * 用户上传文件记录
 * 用户模块
 *
 */
@Entity
@Table(name = "rerebbs_client_file")
public class BbsFileModel implements Comparable, Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();
    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
    
    @ManyToOne
    @JoinColumn(name = "course_id")
    private HJCourse course;
    
    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;
    
    /**
     * 资料视频时长，单位为秒
     */
    @Column(name = "total_time")
    private long total_time;
    @Column(name = "file_Name")
    String fileName = "";
    @Column(name = "file_Abstract")
    String fileAbstract;
    @Column(name = "file_Ext")
    String fileExt;
    @Column(name = "file_Size")
    String fileSize;
    @Column(name = "file_Full_Path")
    String fileFullPath;
    @Column(name = "secret_Grade")
    String secretGrade;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "upload_Time")
    Date uploadTime = new Date();//上传时间
    @Column(name = "if_Folder")
    boolean ifFolder;//folder or file.
    @Column(name = "file_scope")
    String scope;//dept/coop/person
    @Column(name = "father_ID")
    String fatherID;
    @Column(name = "user_id")
    String userId;
    @Transient
    BbsUser user;
    @Column(name = "real_Length")
    long realLength;
    @Column(name = "ancestors")
    String ancestors;
    @Transient
    int sonNum = 0;
    @Transient
    boolean selected = false;
    @Transient
    List<BbsFileModel> familyTree;
    @Transient
    String sharedUserStr;
    @Transient
    String pinyin;
    @Column(name = "md5")
    String md5;
    @Column(name = "admin_id")
    String adminId;
    @Column(name = "pdf_md5")
    String pdfmd5;
    @Column(name = "pdf_size")
    String pdfsize;

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	public String getPdfsize() {
		return pdfsize;
	}

	public void setPdfsize(String pdfsize) {
		this.pdfsize = pdfsize;
	}

	public String getPdfmd5() {
		return pdfmd5;
	}

	public void setPdfmd5(String pdfmd5) {
		this.pdfmd5 = pdfmd5;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

    public long getTotal_time() {
		return total_time;
	}

	public void setTotal_time(long total_time) {
		this.total_time = total_time;
	}

	public Lesson getLesson() {
		return lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileAbstract() {
        return fileAbstract;
    }

    public void setFileAbstract(String fileAbstract) {
        this.fileAbstract = fileAbstract;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getSecretGrade() {
        return secretGrade;
    }

    public void setSecretGrade(String secretGrade) {
        this.secretGrade = secretGrade;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public BbsUser getUser() {
        return user;
    }

    public void setUser(BbsUser user) {
        this.user = user;
    }

    public String getFileFullPath() {
        return fileFullPath;
    }

    public void setFileFullPath(String fileFullPath) {
        this.fileFullPath = fileFullPath;
    }

    public long getRealLength() {
        return realLength;
    }

    public void setRealLength(long realLength) {
        this.realLength = realLength;
    }

    public boolean getIfFolder() {
        return ifFolder;
    }

    public void setIfFolder(boolean ifFolder) {
        this.ifFolder = ifFolder;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public int getSonNum() {
        return sonNum;
    }

    public void setSonNum(int sonNum) {
        this.sonNum = sonNum;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public List<BbsFileModel> getFamilyTree() {
        return familyTree;
    }

    public void setFamilyTree(List<BbsFileModel> familyTree) {
        this.familyTree = familyTree;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public HJCourse getCourse() {
		return course;
	}

	public void setCourse(HJCourse course) {
		this.course = course;
	}

	public String getSharedUserStr() {
        String s = "";
        IShareDAO shareDAO = SpringHelper.getSpringBean("ShareDAO");
        List<BbsUser> us = shareDAO.findSharedUsersByFile(this.getId());
        for (BbsUser u : us) {
            s = s + u.getName() + " ";
        }
        this.sharedUserStr = s;
        return sharedUserStr;
    }

    public void setSharedUserStr(String sharedUserStr) {
        this.sharedUserStr = sharedUserStr;
    }

//    public String getPinyin() {
//        String fn=new String(this.fileName);
//        fn=fn.toLowerCase();
//        if (!"".equals(fn)) {
//            char a = fn.toLowerCase().charAt(0);
//            if ((a > 47 && a < 58) || (a > 96 && a < 123)) {
//                this.pinyin = fn;
//                return this.pinyin;
//            } else {
//                String ps[] = PinyinHelper.toHanyuPinyinStringArray(a);
//                if (ps != null) {
//                    this.pinyin = ps[0].substring(0, ps[0].length() - 1);
//                    return this.pinyin;
//                } else {
//                    return fn;
//                }
//            }
//        } else {
//            return fn;
//        }
//    }
//
//    public void setPinyin(String pinyin) {
//        this.pinyin = pinyin;
//    }
    @Override
    public int compareTo(Object o) {
        BbsFileModel t = (BbsFileModel) o;
        long i = this.getUploadTime().getTime() - t.getUploadTime().getTime();
        if (i > 0) {
            return 1;
        } else {
            return -1;
        }
    }
    
    public String toString(){
    	return getClass().getName() + "@" + this.fileName;
    }
}
