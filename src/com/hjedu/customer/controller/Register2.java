package com.hjedu.customer.controller;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.primefaces.json.JSONObject;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.BeanUtils;

import com.hjedu.businessuser.dao.IBusinessUserDao;
import com.hjedu.businessuser.entity.BusinessUser;
import com.hjedu.customer.UserUtil;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.service.IThirdPartyUserService;
import com.hjedu.customer.service.IUserSessionStateService;
import com.hjedu.customer.service.impl.ComplexBbsUserService;
import com.hjedu.examination.dao.IExamDepartmentDAO;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.entity.ExamDepartment;
import com.hjedu.examination.service.impl.ExamDepartmentService;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.dao.ISendCodeFrequencyDAO;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.dao.ISystemInfoDAO;
import com.hjedu.platform.entity.SystemConfig;
import com.hjedu.platform.service.impl.EmailService;
import com.huajie.app.model.SendCodeFrequency;
import com.huajie.app.util.CodeUtils;
import com.huajie.app.util.DateUtil;
import com.huajie.app.util.HttpClientUtil;
import com.huajie.app.util.NetworkUtil;
import com.huajie.app.util.SmsCountHelper;
import com.huajie.app.util.SmsHelper;
import com.huajie.app.util.StringUtil;
import com.huajie.app.util.Validator;
import com.huajie.app.util.sendMessageHelper;
import com.huajie.util.Constants;
import com.huajie.util.CookieUtils;
import com.huajie.util.DESTool;
import com.huajie.util.IpHelper;
import com.huajie.util.JsfHelper;
import com.huajie.util.MD5;
import com.huajie.util.SpringHelper;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * @author huajie
 *
 */
@ManagedBean
@ViewScoped
public class Register2 implements Serializable {

    BbsUser user = new BbsUser();
    IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
    ISystemConfigDAO scd = SpringHelper.getSpringBean("SystemConfigDAO");
    EmailService emailService = SpringHelper.getSpringBean("EmailService");
    ISystemInfoDAO infoDAO = SpringHelper.getSpringBean("SystemInfoDAO");
    ISendCodeFrequencyDAO sendCodeFrequencyDAO = SpringHelper.getSpringBean("SendCodeFrequencyDAO");
    IUserSessionStateService iussService = SpringHelper.getSpringBean("UserSessionStateService");
    IBusinessUserDao businessUserDAO = SpringHelper.getSpringBean("BusinessUserDAO");
        
    String pwd_re = "";
    String authStr = "";
    String id_temp = UUID.randomUUID().toString();
    String urnTip = "";
    boolean urnOk = true;
    String phoneTip = "";
    boolean phoneOk = true;
    String codeTip = "";
    boolean codeOk = true;
    String emailTip = "";
    boolean emailOk = true;
    
    String usernameTip = "";
    boolean usernameOK = true;
    String type ="1";
    String verifyCode = "";
    boolean accept = true;
    Map deptMap = new HashMap();
    ExamDepartmentService dicDAO = SpringHelper.getSpringBean("ExamDepartmentService");
    List<DictionaryModel> dics = new ArrayList();
    DictionaryModel dic = new DictionaryModel();
    TreeNode root = new DefaultTreeNode();

    String moduleId1 = "";
    String moduleId2 = "";
    String moduleId3 = "";
    String moduleId4 = "";
    List<DictionaryModel> modules1 = new ArrayList();
    List<DictionaryModel> modules2 = new ArrayList();
    List<DictionaryModel> modules3 = new ArrayList();
    List<DictionaryModel> modules4 = new ArrayList();

    DictionaryModel selectedNode = null;
    String selectedId = null;

    boolean ifOnlyOne = false;
    DictionaryModel theOne = null;
    String businessId;

    public String getId_temp() {
        return id_temp;
    }

    public void setId_temp(String id_temp) {
        this.id_temp = id_temp;
    }

    public String getAuthStr() {
        return authStr;
    }

    public void setAuthStr(String authStr) {
        this.authStr = authStr;
    }

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    public List<DictionaryModel> getDics() {
        return dics;
    }

    public void setDics(List<DictionaryModel> dics) {
        this.dics = dics;
    }

    public DictionaryModel getDic() {
        return dic;
    }

    public void setDic(DictionaryModel dic) {
        this.dic = dic;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public String getPwd_re() {
        return pwd_re;
    }

    public void setPwd_re(String pwd_re) {
        this.pwd_re = pwd_re;
    }

    public BbsUser getUser() {
        return user;
    }

    public void setUser(BbsUser user) {
        this.user = user;
    }

    public boolean isUrnOk() {
        return urnOk;
    }

    public void setUrnOk(boolean urnOk) {
        this.urnOk = urnOk;
    }

    public String getUrnTip() {
        return urnTip;
    }

    public void setUrnTip(String urnTip) {
        this.urnTip = urnTip;
    }
    
    public String getPhoneTip() {
		return phoneTip;
	}

	public void setPhoneTip(String phoneTip) {
		this.phoneTip = phoneTip;
	}

	public boolean isPhoneOk() {
		return phoneOk;
	}

	public void setPhoneOk(boolean phoneOk) {
		this.phoneOk = phoneOk;
	}

	public String getCodeTip() {
		return codeTip;
	}

	public void setCodeTip(String codeTip) {
		this.codeTip = codeTip;
	}

	public boolean isCodeOk() {
		return codeOk;
	}

	public void setCodeOK(boolean codeOk) {
		this.codeOk = codeOk;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getModuleId1() {
        return moduleId1;
    }

    public void setModuleId1(String moduleId1) {
        this.moduleId1 = moduleId1;
    }

    public String getModuleId2() {
        return moduleId2;
    }

    public void setModuleId2(String moduleId2) {
        this.moduleId2 = moduleId2;
    }

    public String getModuleId3() {
        return moduleId3;
    }

    public void setModuleId3(String moduleId3) {
        this.moduleId3 = moduleId3;
    }

    public String getModuleId4() {
        return moduleId4;
    }

    public void setModuleId4(String moduleId4) {
        this.moduleId4 = moduleId4;
    }

    public List<DictionaryModel> getModules1() {
        return modules1;
    }

    public void setModules1(List<DictionaryModel> modules1) {
        this.modules1 = modules1;
    }

    public List<DictionaryModel> getModules2() {
        return modules2;
    }

    public void setModules2(List<DictionaryModel> modules2) {
        this.modules2 = modules2;
    }

    public List<DictionaryModel> getModules3() {
        return modules3;
    }

    public void setModules3(List<DictionaryModel> modules3) {
        this.modules3 = modules3;
    }

    public List<DictionaryModel> getModules4() {
        return modules4;
    }

    public void setModules4(List<DictionaryModel> modules4) {
        this.modules4 = modules4;
    }

    public boolean isIfOnlyOne() {
        return ifOnlyOne;
    }

    public void setIfOnlyOne(boolean ifOnlyOne) {
        this.ifOnlyOne = ifOnlyOne;
    }

    public DictionaryModel getTheOne() {
        return theOne;
    }

    public void setTheOne(DictionaryModel theOne) {
        this.theOne = theOne;
    }

    public boolean isEmailOk() {
        return emailOk;
    }

    public void setEmailOk(boolean emailOk) {
        this.emailOk = emailOk;
    }

    public String getEmailTip() {
        return emailTip;
    }

    public void setEmailTip(String emailTip) {
        this.emailTip = emailTip;
    }

    public Map getDeptMap() {
        return deptMap;
    }

    public void setDeptMap(Map deptMap) {
        this.deptMap = deptMap;
    }

    @PostConstruct
    public void init() {
        //this.loadDepts();
    	this.businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        this.modules1 = loadModules(this.dicDAO.findAllSonDictionaryModel2(this.businessId));
        this.loadStructure(null);
    }

    public String changeModule(int deep) {
        if (deep == 1) {
            moduleId2="10";
            moduleId3="100";
            if (this.businessId.equals(moduleId1)) {
                this.modules1 = loadModules(this.dicDAO.findAllSonDictionaryModel(this.businessId));
                modules2 = new ArrayList();
                modules3 = new ArrayList();
                modules4 = new ArrayList();
                this.loadStructure(null);
            } else {
                this.modules2 = loadModules(this.dicDAO.findAllSonDictionaryModel2(this.moduleId1));
                this.loadStructure(this.moduleId1);
                modules3 = new ArrayList();
                modules4 = new ArrayList();
            }
        } else if (deep == 2) {
            moduleId3="100";
            if ("10".equals(moduleId2)) {
                //this.modules1 = loadModules(this.dicDAO.findAllSonDictionaryModel(moduleId2));
                modules3 = new ArrayList();
                modules4 = new ArrayList();
                this.loadStructure(moduleId1);
            } else {
                this.modules3 = loadModules(this.dicDAO.findAllSonDictionaryModel2(this.moduleId2));
                this.loadStructure(this.moduleId2);
                modules4 = new ArrayList();
            }
        } else if (deep == 3) {
            //this.modules4 = this.dicDAO.findAllSonDictionaryModel(this.moduleId4);
            if ("100".equals(moduleId3)) {
                //this.modules1 = loadModules(this.dicDAO.findAllSonDictionaryModel(moduleId2));
                this.loadStructure(moduleId2);
            } else {
                this.loadStructure(this.moduleId3);
            }
        }
        this.checkTheOne();
        return null;
    }

    private void checkTheOne() {
        try {
            this.ifOnlyOne = false;
            theOne = null;
            selectedNode = theOne;
            int num = root.getChildCount();
            if (num == 1) {
                int num2 = root.getChildren().get(0).getChildCount();
                if (num2 == 0) {
                    this.ifOnlyOne = true;
                    theOne = (DictionaryModel) root.getChildren().get(0).getData();
                    selectedNode = theOne;
                }
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private List<DictionaryModel> loadModules(List<DictionaryModel> mms) {
        List<DictionaryModel> modules = new ArrayList();
        if (mms != null) {
            for (DictionaryModel m : mms) {
                if (m.isIfInValidTime() && m.getFrontShow()) {
                    modules.add(m);
                }
            }
        }
        return modules;
    }

    public void loadDepts() {
        this.deptMap.clear();
        IExamDepartmentDAO deptDAO = SpringHelper.getSpringBean("ExamDepartmentDAO");
        this.businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        List<ExamDepartment> depts = deptDAO.findAllExamDepartment(this.businessId);
        for (ExamDepartment e : depts) {
            this.deptMap.put(e.getName(), e.getName());
        }
    }

    public void loadStructure(String id) {
        if (id == null || this.businessId.equals(id)) {
            DictionaryModel l = dicDAO.findDictionaryModel(this.businessId,this.businessId);
            this.root = new DefaultTreeNode(l, null);
            test(l, root);
            //this.dics = l.getSons();
        } else {
            DictionaryModel l = dicDAO.findDictionaryModel(id,this.businessId);
            this.root = new DefaultTreeNode(l, null);
            test(l, new DefaultTreeNode(l, root));
            //this.dics = l.getSons();
        }

    }

    public void test(DictionaryModel dd, TreeNode node) {
        //node.setExpanded(true);
        List<DictionaryModel> ls = dicDAO.findAllSonDictionaryModel(dd.getId());
        if (ls.isEmpty()) {
            node.setSelectable(true);
            return;
        } else {
            Collections.sort(ls);
            dd.setSons(ls);
            node.setSelectable(false);
            for (DictionaryModel d : ls) {
                if (d.getFrontShow()) {
                    TreeNode t = new DefaultTreeNode(d, node);
                    test(d, t);
                }
            }
        }
    }
    
    //发送验证码
    public String sendCode() throws Exception{
        if (user.getPassword().trim().equals("")) {
        	JsfHelper.error("密码不能为空");
            return null;
        }
        if (this.pwd_re.trim().equals("")) {
        	JsfHelper.error("密码确认不能为空");
            return null;
        } 
    	HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	HttpSession s = (HttpSession) request.getSession(true);
        String str = (String) s.getAttribute("rand");
        if (!this.authStr.equals(str)) {
            JsfHelper.error("请填写正确验证码后再获取短信验证码");
            return null;
        }
    	
        String phoneno = this.user.getTel();
        String type = "1";//注册	
        
    	if(phoneno != null && !"".equals(phoneno)){
    		if(Validator.isMobile(phoneno)){//是合格的手机号
				BbsUser bu = this.bbsUserDAO.findBbsUserByPhoneBusinessId(phoneno,this.businessId);
				if(type.equals("1")){//1 注册
					if(bu!=null){
	        			JsfHelper.error("有此手机号的用户");
	                    return null;
					}
	    		}else if(type.equals("2")){//2 登录
        			if(bu==null){
	        			JsfHelper.error("没有此手机号的用户");
	                    return null;
	        		}
        		}else if(type.equals("3")){//3 忘记密码
        			if(bu==null){
	        			JsfHelper.error("没有此手机号的用户");
	                    return null;
	        		}
        		}else{
        			JsfHelper.error("type值无效");
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
        		
//        		ip_count=sendCodeFrequencyDAO.getCountByIPOneDay(ip,todayTime,tomorrowTime);
//            	if(ip_count>=Constants.IP_MAX_COUNT){//此ip超过24小时内最多发送次数
//            		this.phoneOk = false;
// 	                this.phoneTip = "此ip超过24小时内最多发送次数";
// 	                return null;
//        		}
            	
            	phone_count=sendCodeFrequencyDAO.getCountByPhoneOneDay(phoneno,todayTime,tomorrowTime);
            	if(phone_count>=Constants.PHONE_MAX_COUNT){//此手机号超过24小时内最多发送次数
            		this.phoneOk = false;
 	                this.phoneTip = "此手机号超过24小时内最多发送次数";
 	                return null;
        		}
            	
            	long send_last_time;
            	long now_time;
            	if(sendCodeFrequencyDAO.getByPhone(phoneno)!=null){//有发送验证码记录
        			send_last_time=sendCodeFrequencyDAO.getByPhone(phoneno).getSendLastTime().getTime();
        			now_time=nowTime.getTime();
        			if(now_time-send_last_time<2*60*1000){//此手机号的发送间隔小于2分钟
        				this.phoneOk = false;
	 	                this.phoneTip = "此手机号的发送间隔小于2分钟";
	 	                return null;
        			}
        		}
        
            	String verify_code;
            	SendCodeFrequency sendCodeFrequency = new SendCodeFrequency();//验证码发送频率对象
            	String phone = this.user.getTel().trim();
            	String path=request.getSession().getServletContext().getRealPath("");//项目物理路径
            	ip=NetworkUtil.getIpAddress(request);
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
            	this.phoneOk = true;
    			this.phoneTip = "已发送验证码";
    		}else{//不是合格的手机号
    			this.phoneOk = false;
    			this.phoneTip = "请填写正确的手机号";
    			return null;
    		}
    	}else{
    		this.phoneOk = false;
			this.phoneTip = "手机号不能为空";
			return null;
    	}
    	return null;
    }
    
    public String onChangeUrn() {
        String urn = this.user.getUsername().trim();
        if (urn != null && !"".equals(urn)) {
            BbsUser bu = this.bbsUserDAO.findBbsUserByUrn(urn);
            if (bu != null) {
                this.urnOk = false;
                this.urnTip = "此用户名已存在";
            } else {
                this.urnOk = true;
                this.urnTip = "此用户名可以注册";
            }
        } else{
        	this.urnOk = false;
            this.urnTip = "用户名不能为空";
        }
        return null;
    }
    
    public String onChangePhone() {
    	String phone = this.user.getTel().trim();
    	if (phone != null && !"".equals(phone)) {
    		if(Validator.isMobile(phone)){//是合格的手机号
    			BbsUser bu=bbsUserDAO.findBbsUserByPhoneBusinessId(phone,this.businessId);
	            if ( bu!=null) {
	            	this.phoneOk = false;
	                this.phoneTip = "此手机号已存在";
            	} else {
	                this.phoneOk = true;
	                this.phoneTip = "此手机号可以注册";
	            }
    		}else{
    			this.phoneOk = false;
                this.phoneTip = "请填写正确的手机号";
    		}
        } else{
        	this.phoneOk = false;
            this.phoneTip = "手机号不能为空";
        }
    	return null;
    }
    
    public String onChangeEmail() {
        if (this.scd.getSystemConfig().getEmailValidation()) {
            
        }
        String email = this.user.getEmail();
        if (email != null && !"".equals(email.trim())) {
            if (!email.contains("@")) {
                this.emailOk = false;
                this.emailTip = "邮箱格式不正确";
                return null;
            }
            BbsUser bu = this.bbsUserDAO.findBbsUserByEmail(email.trim());
            if (bu != null) {
                this.emailOk = false;
                this.emailTip = "此邮箱已存在";
            } else {
                this.emailOk = true;
                this.emailTip = "此邮箱可以注册";
            }
        }
        return null;
    }
    
    public String onChangeUserName() {
    	String username = this.user.getUsername();
    	if (StringUtils.isBlank(username)) {
    		this.phoneOk = false;
            this.phoneTip = "请填写姓名";
        } 
    	return null;
    }

    public String refreshAuth() {
        this.id_temp = new Long(System.currentTimeMillis()).toString();
        return null;
    }

    public String reg_ok() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String path=request.getSession().getServletContext().getRealPath("");//项目物理路径
        String phoneno=this.user.getTel().trim();
        String password=this.user.getPassword().trim();
        String username=this.user.getUsername().trim();
        String password_re =this.pwd_re.trim();
        String authstr=this.authStr.trim();
        String verifycode=this.verifyCode.trim();
        
        if (username.equals("")){
        	JsfHelper.error("姓名不能为空");
            return null;
        }
        if (phoneno.equals("")){
        	JsfHelper.error("手机号不能为空");
            return null;
        }
        if (password.equals("")) {
        	JsfHelper.error("密码不能为空");
            return null;
        }
        if (password_re.equals("")) {
        	JsfHelper.error("密码确认不能为空");
            return null;
        }
        if(!Validator.isPassword(password)){
        	JsfHelper.error("密码必须6位至16位数字字母组成");
            return null;
        }
        if (!password.equals(password_re)) {
            JsfHelper.error("密码与确认密码不一致");
            return null;
        }
        HttpSession s = (HttpSession) request.getSession(true);
        String str = (String) s.getAttribute("rand");
        if (!authstr.equals(str.trim())) {
            JsfHelper.error("验证码不正确");
            return null;
        }
        if (verifycode.equals("")){
        	JsfHelper.error("短信验证码不能为空");
            return null;
        }  

        CacheManager cacheManager = CacheManager.newInstance(path+"\\WEB-INF\\ehcache.xml");//创建缓存manager
		Cache cache = cacheManager.getCache("verifyCode");
		if(cache.get(user.getTel().trim()+this.verifyCode)!=null){//该手机号的验证码element缓存存在
			if("true".equals(cache.get(user.getTel().trim()+this.verifyCode).getValue())){//验证码正确
				cache.remove(user.getTel().trim()+this.verifyCode);//验证成功，移除此验证码
			}
		}else{//短信验证码错误或失效
			JsfHelper.error("短信验证码错误或已过期");
            return null;
		}

		BusinessUser openBussinessUser = businessUserDAO.findOpenBussinessUser();
        BbsUser user1=bbsUserDAO.findBbsUserByPhoneBusinessId(user.getTel(), this.businessId);
        if(user1!=null){
        	JsfHelper.error("此手机号已经存在");
        	return null;
        }else{//user==null，直接add
        	 this.user.setUsername(user.getTel().trim());
             String ip = IpHelper.getRemoteAddr(request);
             this.user.setRegIp(ip);
             this.user.setLastIp(ip);
             //将密码变换
             ComplexBbsUserService cbus = SpringHelper.getSpringBean("ComplexBbsUserService");
             cbus.handlePwd(user);
             this.user.setChecked(this.scd.getSystemConfig().getAutoCheck());
             this.user.setBusinessId(this.businessId);
             this.user.setGroupStr(this.businessId+ ";");
             this.user.setUsername(username);
             this.user.setName(username);
             bbsUserDAO.addBbsUser(user);
             ClientSession cs = JsfHelper.getBean("clientSession");
             cs.setIfLogin(true);
             cs.setUsr(user);
             if(!this.businessId.equals(openBussinessUser.getId())) {
            	 BbsUser openUser = bbsUserDAO.findBbsUserByPhoneBusinessId(user.getTel(), openBussinessUser.getId());
            	 if(openUser == null){
            		 openUser = new BbsUser();
            		 BeanUtils.copyProperties(user, openUser);
            		 openUser.setId(UUID.randomUUID().toString());
            		 openUser.setBusinessId(openBussinessUser.getId());
            		 openUser.setGroupStr(openBussinessUser.getId()+ ";");
            		 bbsUserDAO.addBbsUser(openUser);
            	 }
             }

             iussService.login(user);
             

             return "RegFinished";
        }
		
    }
    
    public String getUsernameTip() {
		return usernameTip;
	}

	public void setUsernameTip(String usernameTip) {
		this.usernameTip = usernameTip;
	}

	public boolean isUsernameOK() {
		return usernameOK;
	}

	public void setUsernameOK(boolean usernameOK) {
		this.usernameOK = usernameOK;
	}

	/**
     * 刷新用户的状态SESSION，使用户处于登录状态
     *远程
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
     * 如果用户设置了保存登录状态N天，则写COOKIES 验证COOKIES的功能在ClientSession中
     * 远程
     */
    private void writeCookies(BbsUser bu) {
        Cookie ck = new Cookie("rereexam.id", bu.getId());
        int age = 7 * 24 * 3600;
        ck.setMaxAge(age);
        JsfHelper.getResponse().addCookie(ck);
    }
}
