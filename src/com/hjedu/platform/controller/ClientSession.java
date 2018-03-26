package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.service.IThirdPartyUserService;
import com.hjedu.customer.service.IUserSessionStateService;
import com.huajie.app.util.FileUtil;
import com.huajie.filter.Authentication2;
import com.huajie.seller.model.SaleOrder;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.MD5;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@SessionScoped
//@RequestScoped
public class ClientSession implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	BbsUser usr = null;
    boolean ifLogin = false;
    Date lastTime = new Date();
    String pwd_temp = "";
    String pwd_n1;
    String pwd_n2;
    String ie6 = "";

    SaleOrder payingOrder;
    //本步骤的作用是引入版权验证类
    Authentication2 auth2;

    public BbsUser getUsr() {
        return usr;
    }

    public void setUsr(BbsUser usr) {
        this.usr = usr;
    }

    public String getPwd_temp() {
        return pwd_temp;
    }

    public void setPwd_temp(String pwdTemp) {
        pwd_temp = pwdTemp;
    }

    public String getPwd_n1() {
        return pwd_n1;
    }

    public void setPwd_n1(String pwdN1) {
        pwd_n1 = pwdN1;
    }

    public String getPwd_n2() {
        return pwd_n2;
    }

    public void setPwd_n2(String pwdN2) {
        pwd_n2 = pwdN2;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public String getIe6() {
        return ie6;
    }

    public void setIe6(String ie6) {
        this.ie6 = ie6;
    }

    public boolean isIfLogin() {
        return this.ifLogin;
    }

    public void setIfLogin(boolean ifLogin) {
        this.ifLogin = ifLogin;
    }

    public SaleOrder getPayingOrder() {
        return payingOrder;
    }

    public void setPayingOrder(SaleOrder payingOrder) {
        this.payingOrder = payingOrder;
    }

    @PostConstruct
    public void init() {
        //this.count = casusDAO.casusCountPlusOne();
        try {
            JsfHelper.getRequest().setCharacterEncoding("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //this.checkBrowse();
        this.readCookies2();
    }

    /**
     * 读取本地COOKIES，如果用户设置了记录登录状态，则直接登入
     * 写COOKIES的功能在BbsLogin.java中
     */
    private void readCookies() {
        Cookie[] cookies = JsfHelper.getRequest().getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // 获得Cookie的名称
                String cname = cookie.getName();
                String value = cookie.getValue();
				System.out.print("Cookie名:" + cname + " &nbsp; Cookie值:" + value + "<br>");                if ("rereexam.username".equals(cname)) {
                    if (value != null) {
                        IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
                        BbsUser bu = bbsUserDAO.findBbsUserByUrn(value);
                        if (bu != null) {
                            refreshSession(bu);
                        }
                    }
                    break;
                }
            }
        }
    }

    /**
     * 读取本地COOKIES，如果用户设置了记录登录状态，则直接登入
     * 写COOKIES的功能在BbsLogin.java中
     * 远程取us
     */
    private void readCookies2() {
        Cookie[] cookies = JsfHelper.getRequest().getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // 获得Cookie的名称
                String cname = cookie.getName();
                String value = cookie.getValue();
                if ("rereexam.tel".equals(cname)) {
                    if (value != null) {
                        IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
                        BbsUser bu = bbsUserDAO.findBbsUserByPhone(value);
                        if (bu != null) {
                            refreshSession(bu);
                        }
                    }
                    break;
                }
            }
        }
    }
    
    /**
     * 读取本地COOKIES，如果用户设置了记录登录状态，则直接登入
     * 写COOKIES的功能在BbsLogin.java中
     * 远程取us
     */
    private void readCookies3() {
        Cookie[] cookies = JsfHelper.getRequest().getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // 获得Cookie的名称
                String cname = cookie.getName();
                String value = cookie.getValue();
                if ("rereexam.id".equals(cname)) {
                    if (value != null) {
                        IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
                        BbsUser bu = bbsUserDAO.findBbsUser(value);
                        if (bu != null) {
                            refreshSession(bu);
                        }
                    }
                    break;
                }
            }
        }
    }
    
    /**
     * 刷新用户的状态SESSION，使用户处于登录状态
     *
     * @param bu
     */
    private void refreshSession(BbsUser bu) {
        this.setLastTime(bu.getLastTime());
        this.setUsr(bu);
        this.setIfLogin(true);
        //重新设置登录用户的SESSION
        //JsfHelper.getRequest().getSession().setAttribute("clientSession", cs);
    }

    /**
     * 主要用于验证用户修改的密码
     *
     * @return
     */
    @Deprecated
    public String saveUserChange() {

        IBbsUserDAO clientUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
        String temp = this.pwd_n1;
        boolean notOk = false;

        Pattern p = Pattern.compile("[A-Z]+");
        Pattern q = Pattern.compile("[a-z]+");
        Pattern r = Pattern.compile("[0-9]+");
        Pattern s = Pattern.compile("\\p{Punct}+");
        Matcher m1 = p.matcher(temp); // 判断是否含有大写字符
        Matcher m2 = q.matcher(temp); // 判断是否含有小写字符
        Matcher m3 = r.matcher(temp);// 判断是否含有数字
        Matcher m4 = s.matcher(temp);// 判断是否含有特殊字符
        if ((!m1.find(0) && !m2.find(0)) || !m3.find(0) || !m4.find(0)) {
            MyLogger.echo("密码不符合规则");
            notOk = true;
        }

        FacesMessage fm = new FacesMessage();
        fm.setSeverity(FacesMessage.SEVERITY_ERROR);
        if (!MD5.bit32(this.pwd_temp).equals(this.usr.getPassword())) {
            fm.setSummary("原密码不正确！");
            FacesContext.getCurrentInstance().addMessage("", fm);

        } else if (!this.pwd_n1.equals(this.pwd_n2)) {
            fm.setSummary("两次输入的新密码不一致！");
            FacesContext.getCurrentInstance().addMessage("", fm);
        } else if (this.pwd_n1.equals("")) {
            fm.setSummary("密码不能为空！");
            FacesContext.getCurrentInstance().addMessage("", fm);
        } else if (notOk) {
            fm.setSummary("密码不符合规则，密码必须包含字母、数字和特殊符号！");
            FacesContext.getCurrentInstance().addMessage("", fm);
        } else {
            this.usr.setPassword(this.pwd_n1);

            clientUserDAO.updateBbsUser(usr);
            fm.setSummary("修改密码完成！");
            FacesContext.getCurrentInstance().addMessage("", fm);
        }
        return null;
    }

    /**
     * 刷新登录的用户，主要用于当登录的用户修改自身信息后，让SESSION中保存的信息与实际信息同步
     */
    public void refreshUser() {
        if (this.usr != null) {
            IBbsUserDAO clientUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
            this.usr = clientUserDAO.findBbsUser(usr.getId());
        }
    }

    /**
     * 在开放式登录界面退出
     *
     * @return
     */
    public String exit() {
    	HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        this.setUsr(null);
        this.setIfLogin(false);
        IUserSessionStateService iussService = SpringHelper.getSpringBean("UserSessionStateService");
        iussService.logout();
        this.deleteLoginStatus2();
        //第三方平台登出
        Boolean ifLDAP = Boolean.parseBoolean(SpringHelper.getSpringBean("usercheck_thirdParty").toString());
        if (ifLDAP) {//使用第三方验证的情况
            IThirdPartyUserService third=SpringHelper.getSpringBean("ThirdPartyUserService");
            third.synlogout();
        }
        //this.deleteCookie();
//        return "/welcome?faces-redirect=true";
//        return UrlUtil.getPrefixUrlByRequest(request)+"HJ/index.jspx?faces-redirect=true";
//        return "/HJ/index?faces-redirect=true";
//        return "/JT/JTindex?faces-redirect=true";
        String businessId = CookieUtils.getBusinessId(request);
        return FileUtil.getWelcomePageByBusinessId(businessId)+"?faces-redirect=true";
    }

    /**
     * 退出向封闭登录界面
     *
     * @return
     */
    public String exit2() {
        this.exit();
        //this.deleteCookie();
        return "PidClientLogin?faces-redirect=true";
    }

    /**
     * 从触屏设备退出
     *
     * @return
     */
    public String exitMobile() {
        this.exit();
        return "/mobile/MobileLogin?faces-redirect=true";
    }

    public String reserveSession() {
        MyLogger.echo("reserveSession sucess.");
        return null;
    }

    /**
     * 退出登录操作后将之前设置的记录登录状态清除
     */
    public void deleteLoginStatus() {
        Cookie ck = new Cookie("rereexam.username", null);
        ck.setMaxAge(0);
        //ck.setPath("/"); //项目所有目录均有效，这句很关键，否则不敢保证删除
        //重新写入，将覆盖之前的
        JsfHelper.getResponse().addCookie(ck);
    }

    /**
     * 退出登录操作后将之前设置的记录登录状态清除
     * 远程
     */
    public void deleteLoginStatus2() {
        Cookie ck = new Cookie("rereexam.tel", null);
        ck.setMaxAge(0);
        //ck.setPath("/"); //项目所有目录均有效，这句很关键，否则不敢保证删除
        //重新写入，将覆盖之前的
        JsfHelper.getResponse().addCookie(ck);
    }
    
    /**
     * 实时获取用户的帐户余额，主要是防止用户登录后SESSION中的对象不能同步金额
     *
     * @param uid 用户ID
     * @return
     */
    public double fetchRealMoney(String uid) {
        IBbsUserDAO clientUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
        BbsUser buu = clientUserDAO.findBbsUser(uid);
        if (buu != null) {
            return buu.getFinanceBalance();
        } else {
            return 0d;
        }
    }

    /**
     * 实时获取用户的积分，，主要是防止用户登录后SESSION中的对象不能同步积分
     *
     * @param uid 用户ID
     * @return
     */
    public long fetchRealScore(String uid) {
    	long scroe = 0;
        IBbsUserDAO clientUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
        BbsUser buu = clientUserDAO.findBbsUser(uid);
        if(buu!=null){
        	scroe = buu.getScore();
        }
        return scroe;
    }

    /**
     * 此方法用于将以秒为单位的时长变换为文本格式，用于在前端页面显示时间长度
     *
     * @param len 时间长度，以豪秒为单位
     * @return 返回 XX小时XX分XX秒 格式的文本
     */
    public String wrapperTimeLen(long len) {
        len = Math.round(len / 1000d);
        String str = "";
        int hour = 0;
        int min = 0;
        int sec = 0;
        hour = (int) len / 3600;
        min = (int) (len % 3600) / 60;
        sec = (int) len % 60;
        if (hour != 0) {
            str += hour + "小时";
        }
        if (min != 0) {
            str += min + "分";
        }
        str += sec + "秒";
        return str;
    }

    @PreDestroy
    public void destroy11() {
        MyLogger.echo("ExamCase destroyed.");
        IUserSessionStateService iussService = SpringHelper.getSpringBean("UserSessionStateService");
        iussService.sessionDestroyed(null);
    }

}
