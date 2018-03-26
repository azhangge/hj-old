package com.hjedu.customer.controller;

import com.fivestars.interfaces.bbs.client.UcUserCode;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.service.IThirdPartyUserService;
import com.hjedu.customer.service.IUserSessionStateService;
import com.hjedu.customer.service.impl.ComplexBbsUserService;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.dao.IBbsScoreRegularDAO;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.huajie.app.util.HttpClientUtil;
import com.huajie.app.util.NetworkUtil;
import com.huajie.app.util.StringUtil;
import com.huajie.app.util.Validator;
import com.huajie.corss.util.Conn;
import com.huajie.corss.util.Conn2;
import com.huajie.corss.util.ObjectConvent;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLDecoder;
import java.util.Date;


import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.json.JSONObject;

@ManagedBean
@ViewScoped
public class BbsLogin implements Serializable {

    IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
    IBbsScoreRegularDAO scoreRegularDAO = SpringHelper.getSpringBean("BbsScoreRegularDAO");
    IUserSessionStateService iussService = SpringHelper.getSpringBean("UserSessionStateService");
    
    String urn;
    String pwd;
    String pid;
    String phone;
    String name;
    String authStr = "urn";
    String tempId = "";
    String fromUrl = "";
    String para="";
    
    boolean keepStatus = false;//保持登录状态7天

	public String getPara() {
		return para;
	}

	public void setPara(String para) {
		this.para = para;
	}

	public String getAuthStr() {
        return authStr;
    }

    public void setAuthStr(String authStr) {
        this.authStr = authStr;
    }

    public String getFromUrl() {
        return fromUrl;
    }

    public void setFromUrl(String fromUrl) {
        this.fromUrl = fromUrl;
    }

    public boolean isKeepStatus() {
        return keepStatus;
    }

    public void setKeepStatus(boolean keepStatus) {
        this.keepStatus = keepStatus;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }
 
    public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @PostConstruct
    public void init() {
        String ff = JsfHelper.getRequest().getParameter("fromUrl");
        if (ff != null) {
            this.fromUrl = ff;
        }
        this.para=JsfHelper.getRequest().getQueryString();
    }

    /**
     * 如果用户设置了保存登录状态N天，则写COOKIES 验证COOKIES的功能在ClientSession中
     */
    private void writeCookies(BbsUser bu) {
        if (this.keepStatus) {
            Cookie ck = new Cookie("rereexam.username", bu.getUsername());
            int age = 7 * 24 * 3600;
            ck.setMaxAge(age);
            JsfHelper.getResponse().addCookie(ck);
        }
    }

    /**
     * 如果用户设置了保存登录状态N天，则写COOKIES 验证COOKIES的功能在ClientSession中
     * 远程
     */
    private void writeCookies2(BbsUser bu) {
        if (this.keepStatus) {
            Cookie ck = new Cookie("rereexam.tel", bu.getTel());
            int age = 7 * 24 * 3600;
            ck.setMaxAge(age);
            JsfHelper.getResponse().addCookie(ck);
        }
    }
    
    /**
     * 如果用户设置了保存登录状态N天，则写COOKIES 验证COOKIES的功能在ClientSession中
     * 远程
     */
    private void writeCookies3(BbsUser bu) {
        if (this.keepStatus) {
            Cookie ck = new Cookie("rereexam.id", bu.getId());
            int age = 7 * 24 * 3600;
            ck.setMaxAge(age);
            JsfHelper.getResponse().addCookie(ck);
        }
    }
    
    /**
     * 刷新用户的状态SESSION，使用户处于登录状态
     *
     * @param bu
     */
    private void refreshSession(BbsUser bu) {
        ClientSession cs = JsfHelper.getBean("clientSession");
        cs.setLastTime(bu.getLastTime());
        cs.setUsr(bu);
        cs.setIfLogin(true);
        //重新设置登录用户的SESSION
        //JsfHelper.getRequest().getSession().setAttribute("clientSession", cs);
    }

    /**
     * 刷新用户的状态SESSION，使用户处于登录状态
     *远程
     * @param bu
     */
    private void refreshSession2(BbsUser bu) {
        ClientSession cs = JsfHelper.getBean("clientSession");
        cs.setLastTime(bu.getLastTime());
        cs.setUsr(bu);
        cs.setIfLogin(true);
        //重新设置登录用户的SESSION
        //JsfHelper.getRequest().getSession().setAttribute("clientSession", cs);
    }
    
    /**
     * 刷新用户的状态SESSION，使用户处于登录状态
     *远程
     * @param bu
     */
    private void refreshSession3(BbsUser bu) {
        ClientSession cs = JsfHelper.getBean("clientSession");
        cs.setLastTime(bu.getLastTime());
        cs.setUsr(bu);
        cs.setIfLogin(true);
        //重新设置登录用户的SESSION
        //JsfHelper.getRequest().getSession().setAttribute("clientSession", cs);
    }
    
    /**
     * 登录方式一，封闭登录情况
     *
     * @return
     */
    public String checkUserWithPid() {
        BbsUser us = null;
        String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        if(StringUtil.isEmpty(tempId)){
        	JsfHelper.error("手机号不能为空");
            return null;
        }
        if(tempId.trim().equals("")){
        	JsfHelper.error("手机号不能为空");
            return null;
        }
        if(Validator.isMobile(tempId)){//是合格的手机号
        	us = bbsUserDAO.findBbsUserByPhoneBusinessId(tempId, businessId);       
            if (us == null) {
                JsfHelper.error("无此用户");
                return null;
            }
            ComplexBbsUserService cbus = SpringHelper.getSpringBean("ComplexBbsUserService");
            boolean b = cbus.checkPwd(us, pwd);
            if (!b) {
                JsfHelper.error("密码不正确");
                return null;
            }
            if (!us.getEnabled()) {
                JsfHelper.error("此用户已被禁用！");
                return null;
            }
            if (!us.isIfInValidTime()) {
                JsfHelper.error("此用户不在有效期内！");
                return null;
            }
            if (us.getGroup() != null) {
                if (!us.getGroup().isIfInValidTime()) {
                    JsfHelper.error("此用户所在的部门不在有效期内！");
                    return null;
                }
            }
            if (!us.getActivated()) {
                JsfHelper.error("此用户尚未激活！");
                return null;
            }
            if (!us.getChecked()) {
                JsfHelper.error("此用户尚未被审核！");
                return null;
            }

            us.setLastIp(JsfHelper.getRequest().getRemoteAddr());
            Date old = us.getLastTime();

            boolean tooShort = true;
            if ((new Date().getTime() - old.getTime()) > 1000L * 60L * 60L * 24L) {
                tooShort = false;
            }

            //SESSION刷新应发生在设置用户上次登录时间之前，因为要把用户的上次信息首先暂存在SESSION中
            this.refreshSession(us);
            us.setLastTime(new Date());
            us.setLoginCount(us.getLoginCount() + 1);
            iussService.login(us);
            if (!tooShort) {
                IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
                bsl.log("每天第一次登录系统", this.scoreRegularDAO.findScoreRegular().getLogin());
            }
            this.bbsUserDAO.updateBbsUser(us);
            //JsfHelper.info("登录成功，获得积分"+BBSScoreRegular.LOGIN);

            this.urn = "";
            this.pwd = "";

            this.writeCookies2(us);

            return "1";
        }else{//不是合格的手机号
        	JsfHelper.error("不是合格的手机号");
            return null;
        }
        
        

    }

    /**
     * 远程登录
     * */
    public String checkUserWithPid2() {
    	BbsUser us = bbsUserDAO.findBbsUserByPhone(tempId);
        return null;
    }

    /**
     * 远程登录
     * */
    public String checkUserWithPid3() {
    	String phoneno = null;
    	String password = null;
    	if(StringUtil.isNotEmpty(tempId)){
    		phoneno = tempId;
    	}else{
    		JsfHelper.error("手机号不能为空");
    		return null;
    	}
    	if(StringUtil.isNotEmpty(pwd)){
    		password = pwd;
    	}else{
    		JsfHelper.error("密码不能为空");
    		return null;
    	}
    	 return null;
    }
    
    /**
     * 登录方式二，主要用于开放式登录的弹出面板登录，此种方式无需页面跳转
     *
     * @return
     */
    public String checkUserWithoutRedirecting() {
        String result = this.checkUserWithPid(); 
        String returnPath =JsfHelper.getRequest().getServletPath();
        if(result==null){
        	return null;
        }
        if(para!=null && !para.equals("")){
        	return returnPath+"?"+para+"&faces-redirect=true";
        }else{
        	return returnPath+"?faces-redirect=true";
        }
    }

    /**
     * 本方法用于拦截登录验证的场景，主要用于直接打开考试界面链接时检查登录状态
     */
    public void checkUser() {
        this.checkUserWithoutRedirecting();
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        try {
            fromUrl = URLDecoder.decode(fromUrl, SpringHelper.getSpringBean("webServerEncoding").toString());
//            System.out.println(fromUrl);
            response.sendRedirect(fromUrl);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
