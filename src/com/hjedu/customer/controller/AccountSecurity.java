package com.hjedu.customer.controller;

import com.fivestars.interfaces.bbs.client.UcUserCode;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.service.IThirdPartyUserService;
import com.hjedu.customer.service.IUserSessionStateService;
import com.hjedu.customer.service.impl.ComplexBbsUserService;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.dao.IBbsScoreRegularDAO;
import com.hjedu.platform.dao.ISendCodeFrequencyDAO;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.huajie.app.model.SendCodeFrequency;
import com.huajie.app.util.CodeUtils;
import com.huajie.app.util.DateUtil;
import com.huajie.app.util.NetworkUtil;
import com.huajie.app.util.SmsHelper;
import com.huajie.app.util.StringUtil;
import com.huajie.app.util.Validator;
import com.huajie.app.util.sendMessageHelper;
import com.huajie.util.Constants;
import com.huajie.util.DESTool;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.primefaces.json.JSONObject;

@ManagedBean
@ViewScoped
public class AccountSecurity implements Serializable {

	private static final long serialVersionUID = 1L;
	IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
	IBbsScoreRegularDAO scoreRegularDAO = SpringHelper.getSpringBean("BbsScoreRegularDAO");
	IUserSessionStateService iussService = SpringHelper.getSpringBean("UserSessionStateService");
	ISystemConfigDAO sysDAO = SpringHelper.getSpringBean("SystemConfigDAO");
	ISendCodeFrequencyDAO sendCodeFrequencyDAO = SpringHelper.getSpringBean("SendCodeFrequencyDAO");
	
	private String oldPassword;
	private String newPassword;
	private String newPassword2;
	private boolean ifPass1 = false;
	private boolean ifPass2 = false;
	private BbsUser user;
	/**
	 * 确认密码
	 */
	private String affirmPassword;
	/**
	 * 确认密码
	 */
	private String affirmPassword2;
	/**
	 * 短信验证码
	 */
	private String messageCode;
	/**
	 * 验证码
	 * 
	 * @return
	 */
	private String checkCode;

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getAffirmPassword() {
		return affirmPassword;
	}

	public void setAffirmPassword(String affirmPassword) {
		this.affirmPassword = affirmPassword;
	}

	public boolean isIfPass1() {
		return ifPass1;
	}

	public void setIfPass1(boolean ifPass1) {
		this.ifPass1 = ifPass1;
	}

	public boolean isIfPass2() {
		return ifPass2;
	}

	public void setIfPass2(boolean ifPass2) {
		this.ifPass2 = ifPass2;
	}

	public BbsUser getUser() {
		return user;
	}

	public void setUser(BbsUser user) {
		this.user = user;
	}

	public String getAffirmPassword2() {
		return affirmPassword2;
	}

	public void setAffirmPassword2(String affirmPassword2) {
		this.affirmPassword2 = affirmPassword2;
	}

	public String getNewPassword2() {
		return newPassword2;
	}

	public void setNewPassword2(String newPassword2) {
		this.newPassword2 = newPassword2;
	}

	@PostConstruct
	public void init() {
//		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//        String temp = request.getParameter("id");
//        if (temp != null) {
//          this.user = UserUtil.getBbsUser(temp.trim());
//        }
        
		ClientSession cs = JsfHelper.getBean("clientSession");
		if (cs != null) {
			BbsUser user = cs.getUsr();
			if (user != null) {
				this.user = user;
			}
		}
	}
	
	public String savePassword() {
		if(this.user!=null){
			if(StringUtil.isEmpty(this.oldPassword)){
				JsfHelper.error("请确认旧密码");
				return null;
			}
			if(StringUtil.isEmpty(this.newPassword)){
				JsfHelper.error("请输入新密码");
				return null;
			}
			if(StringUtil.isEmpty(this.affirmPassword)){
				JsfHelper.error("请确认新密码");
				return null;
			}
			DESTool dt = new DESTool();
			String pw = dt.encrypt(this.oldPassword);
			if (!pw.equals(this.user.getPassword())){
				JsfHelper.error("旧密码不正确");
				return null;
			}
			if (StringUtil.isNotEmpty(this.newPassword) && StringUtil.isNotEmpty(this.affirmPassword) && newPassword.equals(this.affirmPassword)) {
				if(!Validator.isPassword(newPassword)){
					JsfHelper.error("密码必须6位至16位数字字母组成");
					return null;
				}
			}else{
				JsfHelper.error("新密码与确认密码不一致，请重新输入");
				return null;
			}
			String pww = dt.encrypt(this.newPassword);
//			this.user.setPassword(pww);
//			bbsUserDAO.updateBbsUser(this.user);
			
			/* 统一修改系统上用户的密码  */
			this.modifyAllPassword(pww);
			
			
			JsfHelper.info("修改完成！");
        	return "/talk/UserDetail.jspx?faces-redirect=true&id="+this.user.getId();
		}

//		if (user != null) {
//			String pwd = user.getPassword();
//			if (StringUtil.isNotEmpty(pwd)) {
//				DESTool tool = new DESTool();
//				String password = tool.decrypt(pwd);
//				if (password.equals(this.oldPassword)) {
//					saveNewPassword(tool, this.newPassword, this.affirmPassword);
//				} else {
//					JsfHelper.error("旧密码不正确，请重新输入！");
//				}
//			}
//		}
		return null;
	}
	
	private String saveNewPassword(DESTool tool,String newPassword,String affirmPassword){
		if(StringUtil.isEmpty(newPassword)){
			JsfHelper.error("请输入新密码");
			return null;
		}
		if(StringUtil.isEmpty(affirmPassword)){
			JsfHelper.error("请确认新密码");
			return null;
		}
		if (StringUtil.isNotEmpty(newPassword) && StringUtil.isNotEmpty(affirmPassword) && newPassword.equals(affirmPassword)) {
			if(!Validator.isPassword(newPassword)){
				JsfHelper.error("密码必须6位至16位数字字母组成");
				return null;
			}
			String newPwd = tool.encrypt(newPassword);
			
//			user.setPassword(newPwd);
//			bbsUserDAO.updateBbsUser(user);
			
			/* 统一修改系统上用户的密码  */
			this.modifyAllPassword(newPwd);
			
			JsfHelper.info("密码修改成功!");
//			 try {
//			 Thread.sleep(2000);
//			 } catch (InterruptedException e) {
//			 // TODO Auto-generated catch block
//			 e.printStackTrace();
//			 }
			return "/talk/UserCenter.jspx";
		} else {
			JsfHelper.error("新密码与确认密码不一致，请重新输入");
		}
		return null;
	}
	
	public String submit1(){
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String path=request.getSession().getServletContext().getRealPath("");//项目物理路径
		if(StringUtil.isEmpty(this.checkCode)){
			JsfHelper.error("请输入验证码");
			return null;
		}
		if(StringUtil.isEmpty(this.messageCode)){
			JsfHelper.error("请输入短信验证码");
			return null;
		}
		
		if(this.user!=null){
	        HttpSession s = (HttpSession) request.getSession(true);
	        String str = (String) s.getAttribute("rand");
	        if (!this.checkCode.equals(str.trim())) {
	            JsfHelper.error("验证码不正确");
	            return null;
	        }
	        if (this.messageCode.equals("")){
	        	JsfHelper.error("短信验证码不能为空");
	            return null;
	        }  

	        CacheManager cacheManager = CacheManager.newInstance(path+"\\WEB-INF\\ehcache.xml");//创建缓存manager
			Cache cache = cacheManager.getCache("verifyCode");
			if(cache.get(user.getTel().trim()+this.messageCode)!=null){//该手机号的验证码element缓存存在
				if("true".equals(cache.get(user.getTel().trim()+this.messageCode).getValue())){//验证码正确
					cache.remove(user.getTel().trim()+this.messageCode);//验证成功，移除此验证码
					this.ifPass2 = true;
				}
			}else{//短信验证码错误或失效
				JsfHelper.error("短信验证码错误或已过期");
	            return null;
			}
		}

//		if(user!=null){
//			JSONObject jsonConfirmCode =Conn.confirmCodePC(yun_host,user.getTel(), this.messageCode);
//			String resultConfirmCode=(String) jsonConfirmCode.get("status");
//			this.ifPass1 = true;
//			if(resultConfirmCode.equals("1")){
//				this.ifPass2 = true;
//			}
//			if(resultConfirmCode.equals("2")){
//				JsfHelper.error("系统没有获取到正确的参数");
//				return null;
//			}
//			if(resultConfirmCode.equals("3")){
//				JsfHelper.error("不是合格的手机号");
//				return null;
//			}
//			if(resultConfirmCode.equals("4")){
//				JsfHelper.error("短信验证码错误或失效");
//				return null;
//			}
//		}
		return null;
	}
	
	public String submit2(){
//		if (user != null&&this.ifPass1&&this.ifPass2) {
//			DESTool tool = new DESTool();
//			saveNewPassword(tool, this.newPassword2, this.affirmPassword2);
//		}
		if(this.user!=null && this.ifPass1 && this.ifPass2) {
			if(StringUtil.isEmpty(this.newPassword2)){
				JsfHelper.error("请输入新密码！");
				return null;
			}
			if(StringUtil.isEmpty(this.affirmPassword2)){
				JsfHelper.error("请确认新密码！");
				return null;
			}
			if (StringUtil.isNotEmpty(this.newPassword2) && StringUtil.isNotEmpty(this.affirmPassword2) && newPassword2.equals(this.affirmPassword2)) {
				if(!Validator.isPassword(newPassword2)){
					JsfHelper.error("密码必须6位至16位数字字母组成");
					return null;
				}
			}else{
				JsfHelper.error("新密码与确认密码不一致，请重新输入！");
				return null;
			}
			DESTool dt = new DESTool();
			String pw = dt.encrypt(this.newPassword2);
//			this.user.setPassword(pw);
//			bbsUserDAO.updateBbsUser(this.user);
			
			/* 统一修改系统上用户的密码  */
			this.modifyAllPassword(pw);

			JsfHelper.info("修改完成！");
        	return "/talk/UserDetail.jspx??faces-redirect=true&id="+this.user.getId();
		}
		return null;
	}
	
	public String sendCode(){
		if(user!=null){
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			HttpSession s = (HttpSession) request.getSession(true);
			String str = (String) s.getAttribute("rand");
			if (!this.checkCode.equals(str)) {
				JsfHelper.error("请填写正确验证码后再获取短信验证码");
				return null;
			}
			String phoneno = this.user.getTel();
			if(phoneno != null && !"".equals(phoneno)){
				if(Validator.isMobile(phoneno)){//是合格的手机号
					if(this.user != null){
						BbsUser bu = this.bbsUserDAO.findBbsUserByPhone(phoneno);
						if(bu==null){
			    			JsfHelper.error("没有此手机号的用户");
			                return null;
			    		}
						int ip_count=0;
			    		int phone_count=0;
			    		String ip=null;
						try {
							ip = NetworkUtil.getIpAddress(request);
						} catch (IOException e) {
							e.printStackTrace();
						}
						Date nowTime=new Date();
			    		Date todayTime = DateUtil.getNeedTime(0,0,0,0);
			    		Date tomorrowTime = DateUtil.getNeedTime(23,59,59,0);
			    		
//			    		ip_count=sendCodeFrequencyDAO.getCountByIPOneDay(ip,todayTime,tomorrowTime);
//			        	if(ip_count>=Constants.IP_MAX_COUNT){//此ip超过24小时内最多发送次数
//			        		JsfHelper.error("此ip超过24小时内最多发送次数");
//			        		return null;
//			    		}
			        	
			        	phone_count=sendCodeFrequencyDAO.getCountByPhoneOneDay(phoneno,todayTime,tomorrowTime);
			        	if(phone_count>=Constants.PHONE_MAX_COUNT){//此手机号超过24小时内最多发送次数
			        		JsfHelper.error("此手机号超过24小时内最多发送次数");
			        		return null;  
			    		}
			        	
			        	long send_last_time;
			        	long now_time;
			        	if(sendCodeFrequencyDAO.getByPhone(phoneno)!=null){//有发送验证码记录
			    			send_last_time=sendCodeFrequencyDAO.getByPhone(phoneno).getSendLastTime().getTime();
			    			now_time=nowTime.getTime();
			    			if(now_time-send_last_time<2*60*1000){//此手机号的发送间隔小于2分钟
			 	               JsfHelper.error("此手机号的发送间隔小于2分钟");
				        		return null;  
			    			}
			    		}
		    
			        	String verify_code;
			        	SendCodeFrequency sendCodeFrequency = new SendCodeFrequency();//验证码发送频率对象
			        	String phone = this.user.getTel().trim();
			        	String path=request.getSession().getServletContext().getRealPath("");//项目物理路径
			        	try {
							ip=NetworkUtil.getIpAddress(request);
						} catch (IOException e) {
							e.printStackTrace();
						}
			        	verify_code=CodeUtils.generateVerifyCode(4);//生成验证码
			    		CacheManager cacheManager = CacheManager.newInstance(path+"\\WEB-INF\\ehcache.xml");//创建缓存manager
			    		Cache cache = cacheManager.getCache("verifyCode");
			    		Element element = new Element(phone+verify_code,"true");
			    		cache.put(element);
			        	SmsHelper smsHelper=new SmsHelper();
			        	smsHelper.sendOneSms(phone, "您当前的验证码为:"+verify_code+"，请您在5分钟之内验证。", null);//发送短信
			    		sendMessageHelper.sendMessage("234971", phone, verify_code);
			    		sendCodeFrequency.setPhone(phone);
			    		sendCodeFrequency.setSendLastTime(new Date());
			    		sendCodeFrequency.setIp(ip);
			    		sendCodeFrequency.setCode(verify_code);
			    		sendCodeFrequencyDAO.addSendCodeFrequency(sendCodeFrequency);//添加
			    		this.ifPass1 = true;
				        return null;
					}
				}
			}else{
				JsfHelper.error("请填写正确的手机号");
				return null;
			}
			if(StringUtil.isNotEmpty(str)){
				JsfHelper.info("已发送验证码");
			}
		}else{
			JsfHelper.error("手机号不能为空");
            return null;
		}
    	return null;
	} 
	
	private void modifyAllPassword(String password){
		
		List<BbsUser> userList = bbsUserDAO.findBbsUserByPhones(this.user.getTel());
		for(BbsUser bbsUser:userList) {
			bbsUser.setPassword(password);
			bbsUserDAO.updateBbsUser(bbsUser);
		}
	}
}
