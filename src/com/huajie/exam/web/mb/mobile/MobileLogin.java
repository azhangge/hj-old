package com.huajie.exam.web.mb.mobile;

import com.fivestars.interfaces.bbs.client.UcUserCode;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.service.IThirdPartyUserService;
import com.hjedu.customer.service.IUserSessionStateService;
import com.hjedu.customer.service.impl.ComplexBbsUserService;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.dao.IBbsScoreRegularDAO;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.huajie.exam.web.mb.*;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.Cookie;

@ManagedBean
@ViewScoped
public class MobileLogin implements Serializable {

    IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
    IBbsScoreRegularDAO scoreRegularDAO = SpringHelper.getSpringBean("BbsScoreRegularDAO");
    IUserSessionStateService iussService = SpringHelper.getSpringBean("UserSessionStateService");

    String urn;
    String pwd;
    String pid;
    String name;
    String authStr = "urn";
    String tempId = "";
    boolean keepStatus = false;//保持登录状态7天

    public String getAuthStr() {
        return authStr;
    }

    public void setAuthStr(String authStr) {
        this.authStr = authStr;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public boolean isKeepStatus() {
        return keepStatus;
    }

    public void setKeepStatus(boolean keepStatus) {
        this.keepStatus = keepStatus;
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

    public MobileLogin() {
    }

    
    /**
     * 登录方式一，封闭登录情况
     * @return 
     */
    public String checkUserWithPid() {
        BbsUser us = null;
        Boolean ifLDAP = Boolean.parseBoolean(SpringHelper.getSpringBean("usercheck_thirdParty").toString());
        if (ifLDAP) {//使用第三方验证的情况
            try {
                IThirdPartyUserService third=SpringHelper.getSpringBean("ThirdPartyUserService");
                UcUserCode code=third.loginCheck(tempId, pwd);
                int result = code.getCode();
                if (result == -2) {//验证不正确
                    JsfHelper.error("密码不正确");
                    return null;
                } else if (result == -1) {
                    JsfHelper.error("无此用户");
                    return null;
                } else if (result>= 0) {//验证成功
                    //与第三方同步用户信息
                    us=third.synUserFromUcUserCode(code);
                }
                MyLogger.echo(String.valueOf(result));
            } catch (Exception e) {
                e.printStackTrace();
                JsfHelper.error("第三方验证已开启，但发生内部错误。");
                return null;
            }
        } else {//不使用第三方验证的情况
            if ("pid".equals(authStr)) {
                us = bbsUserDAO.findBbsUserByPid(tempId);
            } else if ("cid".equals(authStr)) {
                us = bbsUserDAO.findBbsUserByCid(tempId);
            } else if ("urn".equals(authStr)) {
                us = bbsUserDAO.findBbsUserByUrn(tempId);
            }
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
        }
        if (us == null) {//防止US空指针引用
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

        iussService.login(us);
        if (!tooShort) {
            IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
            bsl.log("每天第一次登录系统", this.scoreRegularDAO.findScoreRegular().getLogin());
        }
        this.bbsUserDAO.updateBbsUser(us);
        //JsfHelper.info("登录成功，获得积分"+BBSScoreRegular.LOGIN);

        this.urn = "";
        this.pwd = "";

        this.writeCookies(us);

        return "/mobile/Default?faces-redirect=true";

    }

    
    
    /**
     * 登录方式二，主要用于开放式登录的弹出面板登录，此种方式无需页面跳转
     *
     * @return
     */
    public String checkUserWithoutRedirecting() {
        String result=this.checkUserWithPid();
        return null;
    }
    

    public String checkUser() {
        this.checkUserWithoutRedirecting();
        return "zoneList";

    }
    
    /**
     * 如果用户设置了保存登录状态N天，则写COOKIES
     * 验证COOKIES的功能在ClientSession中
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
}
