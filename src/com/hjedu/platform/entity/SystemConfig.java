package com.hjedu.platform.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.*;

/**
 * 
 * 系统配置表
 * 用户模块
 *
 */
@Entity
@Table(name = "y_system_config")
public class SystemConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id=UUID.randomUUID().toString();
    @Column(name = "emailValidation")
    private boolean emailValidation=false;
    @Column(name = "systemClosed")
    private int systemClosed=1;
    @Column(name = "siteName")
    private String siteName="华杰云教育系统";
    @Column(name = "filePath")
    private String filePath="/upload/exam_upload";
    @Column(name = "ifRelative")
    private boolean ifRelative=true;
    @Column(name = "initPwd")
    private String initPwd="123456";
    @Column(name="copy_right")
    private String copyRight="华杰工程咨询有限公司 版权所有";
    @Column(name="theme")
    private String theme="aristo";
    @Column(name="if_connect")
    private boolean ifConnect=false;
    @Column(name="allow_chg_dept")
    private boolean allowChgDept=false;
    @Column(name = "open_forum")
    private boolean openForum=true;
    @Column(name = "open_message")
    private boolean openMessage=true;
    @Column(name = "open_file")
    private boolean openFile=true;
    @Column(name = "open_wrong_question")
    private boolean openWrong=true;
    @Column(name = "open_wrong_knowledge")
    private boolean openWrong2=true;
    @Column(name = "open_collection")
    private boolean openCollection=true;
    @Column(name = "open_login2")
    private boolean openLogin2=true;
    @Column(name = "open_pid")
    private boolean openPid=true;
    @Column(name = "open_cid")
    private boolean openCid=true;
    @Column(name = "open_urn")
    private boolean openUrn=true;
    @Column(name = "open_phone")
    private boolean openPhone=true;
    @Column(name = "auto_check")
    private boolean autoCheck=true;
    @Column(name = "show_score")
    private boolean showScore=true;
    @Column(name = "only_dept")
    private boolean onlyDept=false;//仅开放本部门考试
    @Column(name = "only_dept_lesson")
    private boolean onlyDeptLesson=false;//仅开放本部门课程
    @Column(name = "async_submit")
    private boolean asyncSubmit=false;
    @Column(name = "auto_upload")
    private boolean autoUpload=false;
    @Column(name = "open_score")
    private boolean openScore=true;
    @Column(name = "open_user_center")
    private boolean openUserCenter=true;
    @Column(name = "open_customer_service")
    private boolean openCustomerService=true;
    @Column(name = "open_module_exam")
    private boolean openModuleExam=true;
    @Column(name = "open_lesson")
    private boolean openLesson=true;
    @Column(name = "open_search")
    private boolean openSearch=true;
    @Column(name = "open_buffet")
    private boolean openBuffet=true;
    @Column(name = "open_contest")
    private boolean openContest=true;
    @Column(name = "open_picture")
    private boolean openPicture=true;
    @Column(name = "open_trade")
    private boolean openTrade=false;
    @Column(name = "open_position")
    private boolean openPosition=false;//开放职位
    @Column(name = "use_pid")
    private boolean usePid=false;//是否使用身份证
    @Column(name = "single_login")
    private boolean singleLogin=false;//同一帐号只允许一人登录
    @Column(name = "sub_id")
    private String sub_id;//子系统id
    @Column(name = "yun_host")
    private String yun_host;//云服务器host
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "gen_time")
    private Date genTime=new Date();
    @Column(name="sys_name")
    private String sysName;//系统标识，名字唯一
    @Column(name="sys_rootpath")
    private String sysRootPath;//系统标识，名字唯一

	@Transient
    boolean selected=false;
    
    /**
     * 轮播图切换间隔时间（秒）
     */
    @Column(name="pic_change_time")
    private int picChangeTime=6;
    
    @Column(name="business_id")
    private String businessId;
    
	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public int getPicChangeTime() {
		return picChangeTime;
	}

	public void setPicChangeTime(int picChangeTime) {
		this.picChangeTime = picChangeTime;
	}

	public SystemConfig() {
    }

    public SystemConfig(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getEmailValidation() {
        return emailValidation;
    }

    public void setEmailValidation(boolean emailValidation) {
        this.emailValidation = emailValidation;
    }

    public int getSystemClosed() {
		return systemClosed;
	}

	public void setSystemClosed(int systemClosed) {
		this.systemClosed = systemClosed;
	}

	public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public boolean isSingleLogin() {
        return singleLogin;
    }

    public void setSingleLogin(boolean singleLogin) {
        this.singleLogin = singleLogin;
    }

    public boolean isOnlyDeptLesson() {
        return onlyDeptLesson;
    }

    public void setOnlyDeptLesson(boolean onlyDeptLesson) {
        this.onlyDeptLesson = onlyDeptLesson;
    }

    public boolean isOpenPosition() {
        return openPosition;
    }

    public void setOpenPosition(boolean openPosition) {
        this.openPosition = openPosition;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean getIfRelative() {
        return ifRelative;
    }

    public void setIfRelative(boolean ifRelative) {
        this.ifRelative = ifRelative;
    }

    public boolean isOpenPicture() {
        return openPicture;
    }

    public void setOpenPicture(boolean openPicture) {
        this.openPicture = openPicture;
    }

    public boolean isOpenCollection() {
        return openCollection;
    }

    public void setOpenCollection(boolean openCollection) {
        this.openCollection = openCollection;
    }

    public boolean getOpenModuleExam() {
        return openModuleExam;
    }

    public void setOpenModuleExam(boolean openModuleExam) {
        this.openModuleExam = openModuleExam;
    }

    public String getInitPwd() {
        return initPwd;
    }

    public void setInitPwd(String initPwd) {
        this.initPwd = initPwd;
    }

    public String getCopyRight() {
        return copyRight;
    }

    public void setCopyRight(String copyRight) {
        this.copyRight = copyRight;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public boolean isIfConnect() {
        return ifConnect;
    }

    public void setIfConnect(boolean ifConnect) {
        this.ifConnect = ifConnect;
    }

    public boolean isAllowChgDept() {
        return allowChgDept;
    }

    public void setAllowChgDept(boolean allowChgDept) {
        this.allowChgDept = allowChgDept;
    }

    public boolean getOpenForum() {
        return openForum;
    }

    public void setOpenForum(boolean openForum) {
        this.openForum = openForum;
    }

    public boolean getOpenMessage() {
        return openMessage;
    }

    public void setOpenMessage(boolean openMessage) {
        this.openMessage = openMessage;
    }

    public boolean getOpenFile() {
        return openFile;
    }

    public void setOpenFile(boolean openFile) {
        this.openFile = openFile;
    }

    public boolean getOpenWrong() {
        return openWrong;
    }

    public void setOpenWrong(boolean openWrong) {
        this.openWrong = openWrong;
    }

    public boolean getOpenLogin2() {
        return openLogin2;
    }

    public void setOpenLogin2(boolean openLogin2) {
        this.openLogin2 = openLogin2;
    }

    public boolean getOpenWrong2() {
        return openWrong2;
    }

    public void setOpenWrong2(boolean openWrong2) {
        this.openWrong2 = openWrong2;
    }

    public boolean getOpenPid() {
        return openPid;
    }

    public void setOpenPid(boolean openPid) {
        this.openPid = openPid;
    }

    public boolean getOpenCid() {
        return openCid;
    }

    public void setOpenCid(boolean openCid) {
        this.openCid = openCid;
    }

    public boolean getOpenUrn() {
        return openUrn;
    }

    public void setOpenUrn(boolean openUrn) {
        this.openUrn = openUrn;
    }
    
    public boolean isOpenPhone() {
		return openPhone;
	}

	public void setOpenPhone(boolean openPhone) {
		this.openPhone = openPhone;
	}

	public boolean getAutoCheck() {
        return autoCheck;
    }

    public void setAutoCheck(boolean autoCheck) {
        this.autoCheck = autoCheck;
    }

    public boolean getShowScore() {
        return showScore;
    }

    public void setShowScore(boolean showScore) {
        this.showScore = showScore;
    }

    public boolean getOnlyDept() {
        return onlyDept;
    }

    public void setOnlyDept(boolean onlyDept) {
        this.onlyDept = onlyDept;
    }

    public boolean getAsyncSubmit() {
        return asyncSubmit;
    }

    public void setAsyncSubmit(boolean asyncSubmit) {
        this.asyncSubmit = asyncSubmit;
    }

    public boolean getAutoUpload() {
        return autoUpload;
    }

    public void setAutoUpload(boolean autoUpload) {
        this.autoUpload = autoUpload;
    }

    public boolean getOpenScore() {
        return openScore;
    }

    public void setOpenScore(boolean openScore) {
        this.openScore = openScore;
    }

    public boolean isOpenTrade() {
        return openTrade;
    }

    public void setOpenTrade(boolean openTrade) {
        this.openTrade = openTrade;
    }

    public boolean getOpenUserCenter() {
        return openUserCenter;
    }

    public void setOpenUserCenter(boolean openUserCenter) {
        this.openUserCenter = openUserCenter;
    }

    public boolean getOpenCustomerService() {
        return openCustomerService;
    }

    public void setOpenCustomerService(boolean openCustomerService) {
        this.openCustomerService = openCustomerService;
    }

    public boolean getOpenLesson() {
        return openLesson;
    }

    public void setOpenLesson(boolean openLesson) {
        this.openLesson = openLesson;
    }

    public boolean isOpenSearch() {
        return openSearch;
    }

    public void setOpenSearch(boolean openSearch) {
        this.openSearch = openSearch;
    }

    public boolean isOpenBuffet() {
        return openBuffet;
    }

    public void setOpenBuffet(boolean openBuffet) {
        this.openBuffet = openBuffet;
    }

    public boolean isOpenContest() {
        return openContest;
    }

    public void setOpenContest(boolean openContest) {
        this.openContest = openContest;
    }

    public boolean isUsePid() {
        return usePid;
    }

    public void setUsePid(boolean usePid) {
        this.usePid = usePid;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getSub_id() {
		return sub_id;
	}

	public void setSub_id(String sub_id) {
		this.sub_id = sub_id;
	}

	public String getYun_host() {
		return yun_host;
	}

	public void setYun_host(String yun_host) {
		this.yun_host = yun_host;
	}

    public String getSysRootPath() {
		return sysRootPath;
	}

	public void setSysRootPath(String sysRootPath) {
		this.sysRootPath = sysRootPath;
	}
	
	public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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
        if (!(object instanceof SystemConfig)) {
            return false;
        }
        SystemConfig other = (SystemConfig) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.model.SystemConfig[ id=" + id + " ]";
    }
    
}
