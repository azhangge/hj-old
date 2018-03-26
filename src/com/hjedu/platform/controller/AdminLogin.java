package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.customer.dao.IAdminDAO;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.service.ILogService;
import com.huajie.ejb.IPSeekerServiceRemote;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AdminLogin implements Serializable {

    AdminInfo user = new AdminInfo();
    String authStr = null;
    String backSiteName = "";
    IAdminDAO adminDAO = SpringHelper.getSpringBean("AdminDAO");
    ISystemConfigDAO scDAO = SpringHelper.getSpringBean("SystemConfigDAO");
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    ILogService logger = SpringHelper.getSpringBean("LogService");

    long sTime = System.nanoTime();
    boolean showVerificationCode;
    int time;

    public String getAuthStr() {
        return this.authStr;
    }

    public void setAuthStr(String authStr) {
        this.authStr = authStr;
    }

    public AdminInfo getUser() {
        return this.user;
    }

    public void setUser(AdminInfo user) {
        this.user = user;
    }

    public long getSTime() {
        return sTime;
    }

    public void setSTime(long sTime) {
        this.sTime = sTime;
    }

    public String getBackSiteName() {
        return backSiteName;
    }

    public void setBackSiteName(String backSiteName) {
        this.backSiteName = backSiteName;
    }

    public boolean isShowVerificationCode() {
		return showVerificationCode;
	}

	public void setShowVerificationCode(boolean showVerificationCode) {
		this.showVerificationCode = showVerificationCode;
	}

	@PostConstruct
    public void init() {
        this.authStr = "";
        this.backSiteName = this.scDAO.getSystemConfig().getSiteName();
        this.time=0;
        this.showVerificationCode=false;
    }

    public String login_action() {
        String rand = (String) this.session.getAttribute("rand");
        FacesMessage fm = new FacesMessage();
        fm.setSeverity(FacesMessage.SEVERITY_ERROR);
        MyLogger.echo("I'm here.");
        if(this.showVerificationCode){
        	if (rand != null) {
        		if (!rand.toLowerCase().equals(this.authStr.toLowerCase())) {
        			fm.setSummary("验证码错误！");
        			this.authStr = "";
        			FacesContext.getCurrentInstance().addMessage("", fm);
        			return null;
        		}
        	} else {
        		fm.setSummary("验证码错误！");
        		FacesContext.getCurrentInstance().addMessage("", fm);
        		return null;
        	}
        }
        String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        AdminInfo admin = adminDAO.findAdminByUrnByBusinessId(this.user.getUrn(),businessId);
        if (admin == null) {
            fm.setSummary("不存在此用户！");
            this.authStr = "";
            FacesContext.getCurrentInstance().addMessage("", fm);
            refresh();
            return null;
        }
        if (!admin.getPwd().toLowerCase().equals(this.user.getPwd().toLowerCase())) {
            fm.setSummary("密码错误！");
            refresh();
            this.authStr = "";
            FacesContext.getCurrentInstance().addMessage("", fm);
            return null;
        }
        if (!admin.isIfInValidTime()) {
            fm.setSummary("对不起，此用户已过期！");
            refresh();
            this.authStr = "";
            FacesContext.getCurrentInstance().addMessage("", fm);
            return null;
        }
        if (!admin.getEnabled().booleanValue()) {
            fm.setSummary("对不起，此用户没有启用！");
            refresh();
            this.authStr = "";
            FacesContext.getCurrentInstance().addMessage("", fm);
            return null;
        }
        if (!admin.getChecked().booleanValue()) {
            fm.setSummary("对不起，此用户没有激活！");
            refresh();
            this.authStr = "";
            FacesContext.getCurrentInstance().addMessage("", fm);
            return null;
        }
        AdminSessionBean asb=new AdminSessionBean();
        asb.setAdmin(admin);
        asb.setIfLogin(true);
//        boolean t = admin.getGrp().equals("admin");
        boolean t = !admin.getGrp().equals("company");
        asb.setIfSuper(t);
        asb.setLtime(admin.getLtime());
        JsfHelper.getRequest().getSession().setAttribute("adminSessionBean", asb);

        admin.setLtime(new Date());
        adminDAO.updateAdmin(admin);
        IPSeekerServiceRemote ips = SpringHelper.getSpringBean("ipSeekerService");
        String ip = JsfHelper.getRequest().getRemoteAddr();
        String addr = ips.seek(ip);
        try {
            logger.log("登录了系统（IP：" + ip + "，" + addr + "）。");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //MyLogger.echo("Ready to jump.");
        this.time=0;
        return "/m/Welcome?faces-redirect=true";
    }

    public String refresh() {
        this.authStr = "";
        this.sTime = System.nanoTime();
        this.time++;
        if(this.time>=3){
        	this.showVerificationCode=true;
        }else{
        	this.showVerificationCode=false;
        }
        return null;
    }
    
}
