package com.hjedu.customer.controller;

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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.hjedu.customer.dao.IAdminDAO;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.dao.impl.AdminDAO;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.service.IThirdPartyUserService;
import com.hjedu.customer.service.IUserSessionStateService;
import com.hjedu.customer.service.impl.ComplexBbsUserService;
import com.hjedu.examination.dao.IExamDepartmentDAO;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.entity.ExamDepartment;
import com.hjedu.examination.service.impl.ExamDepartmentService;
import com.hjedu.platform.dao.ISendCodeFrequencyDAO;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.dao.ISystemInfoDAO;
import com.hjedu.platform.service.impl.EmailService;
import com.huajie.app.model.SendCodeFrequency;
import com.huajie.app.util.CodeUtils;
import com.huajie.app.util.NetworkUtil;
import com.huajie.app.util.SmsCountHelper;
import com.huajie.app.util.SmsHelper;
import com.huajie.app.util.Validator;
import com.huajie.util.CookieUtils;
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
public class Register5 implements Serializable {

    BbsUser user = new BbsUser();
    IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
    
    ISystemConfigDAO scd = SpringHelper.getSpringBean("SystemConfigDAO");
    EmailService emailService = SpringHelper.getSpringBean("EmailService");
    ISystemInfoDAO infoDAO = SpringHelper.getSpringBean("SystemInfoDAO");
    ISendCodeFrequencyDAO sendCodeFrequencyDAO = SpringHelper.getSpringBean("SendCodeFrequencyDAO");
    
    AdminInfo adminInfo =new AdminInfo();
    IAdminDAO adminDAO = SpringHelper.getSpringBean("AdminDAO");
    
    IUserSessionStateService iussService = SpringHelper.getSpringBean("UserSessionStateService");
    
    String bussinessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    
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
    String type ="2";
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
    String businessId;

    DictionaryModel selectedNode = null;
    String selectedId = null;

    boolean ifOnlyOne = false;
    DictionaryModel theOne = null;

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

    public AdminInfo getAdminInfo() {
		return adminInfo;
	}

	public void setAdminInfo(AdminInfo adminInfo) {
		this.adminInfo = adminInfo;
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
            if (this.bussinessId.equals(moduleId1)) {
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
        List<ExamDepartment> depts = deptDAO.findAllExamDepartment(this.businessId);
        for (ExamDepartment e : depts) {
            this.deptMap.put(e.getName(), e.getName());
        }
    }

    public void loadStructure(String id) {
        if (id == null || this.businessId.equals(id)) {
            DictionaryModel l = dicDAO.findDictionaryModel(this.businessId,bussinessId);
            this.root = new DefaultTreeNode(l, null);
            test(l, root);
            //this.dics = l.getSons();
        } else {
            DictionaryModel l = dicDAO.findDictionaryModel(id,bussinessId);
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
    	HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	HttpSession s = (HttpSession) request.getSession(true);
        String str = (String) s.getAttribute("rand");
        if (!this.authStr.equals(str)) {
            JsfHelper.error("请填写正确验证码后再获取短信验证码");
            return null;
        }

    	String ip;
    	int ip_count;
    	int phone_count;
    	long send_last_time;
		long now_time;
		String verify_code;
    	SendCodeFrequency sendCodeFrequency = new SendCodeFrequency();//验证码发送频率对象
    	String phone = this.adminInfo.getTel().trim();
    	String path=request.getSession().getServletContext().getRealPath("");//项目物理路径
		ip=NetworkUtil.getIpAddress(request);
		
    	if(phone != null && !"".equals(phone)){
    		if(Validator.isMobile(phone)){//是合格的手机号
				AdminInfo ai = this.adminDAO.findAdminByPhone(phone);
				BbsUser bu = this.bbsUserDAO.findBbsUserByPhone(phone);
	            if (ai != null || (bu != null && bu.getAppRegTime()!=null)) {
	                this.phoneOk = false;
	                this.phoneTip = "此手机号已存在,无法注册";
	                return null;
	            } else {
//	            	ip_count=sendCodeFrequencyDAO.getCountByIPOneDay(ip);
//	            	ip_count=0;
//	            	if(ip_count>=SmsCountHelper.ip_count){//此ip超过24小时内最多发送次数
//	            		this.phoneOk = false;
//	 	                this.phoneTip = "此ip超过24小时内最多发送次数";
//	 	                return null;
//	        		}
//	            	phone_count=sendCodeFrequencyDAO.getCountByPhoneOneDay(phone);
	            	phone_count=0;
	            	if(phone_count>=SmsCountHelper.phone_count){//此手机号超过24小时内最多发送次数
	            		this.phoneOk = false;
	 	                this.phoneTip = "此手机号超过24小时内最多发送次数";
	 	                return null;
	        		}
	            	if(sendCodeFrequencyDAO.getByPhone(phone)!=null){//有发送验证码记录
	        			send_last_time=sendCodeFrequencyDAO.getByPhone(phone).getSendLastTime().getTime();
	        			now_time=new Date().getTime();
	        			if(now_time-send_last_time<2*60*1000){//此手机号的发送间隔小于2分钟
	        				this.phoneOk = false;
		 	                this.phoneTip = "此手机号的发送间隔小于2分钟";
		 	                return null;
	        			}
	        		}
	            	verify_code=CodeUtils.generateVerifyCode(4);//生成验证码
//	            	verify_code="1234";
	        		CacheManager cacheManager = CacheManager.newInstance(path+"\\WEB-INF\\classes\\ehcache.xml");//创建缓存manager
	        		Cache cache = cacheManager.getCache("verifyCode");
	        		Element element = new Element(phone+verify_code,"true");
	        		cache.put(element);
	        		SmsHelper smsHelper=new SmsHelper();
	        		smsHelper.sendOneSms(phone, "您当前的验证码为:"+verify_code+"，请您在5分钟之内验证。", null);//发送短信
	        		sendCodeFrequency.setPhone(phone);
	        		sendCodeFrequency.setSendLastTime(new Date());
	        		sendCodeFrequency.setIp(ip);
	        		sendCodeFrequency.setCode(verify_code);
	        		sendCodeFrequencyDAO.addSendCodeFrequency(sendCodeFrequency);//添加
	            	this.phoneOk = true;
	    			this.phoneTip = "已发送验证码";
	            }
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
//        String urn = this.user.getUsername().trim(); 
        String urn = this.adminInfo.getUrn().trim();
        
        if (urn != null && !"".equals(urn)) {
        	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        	AdminInfo ai=this.adminDAO.findAdminByUrnByBusinessId(urn,businessId);	
            BbsUser bu = this.bbsUserDAO.findBbsUserByUrn(urn);
            if (ai != null || bu != null) {
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
    	String phone = this.adminInfo.getTel().trim();
    	if (phone != null && !"".equals(phone)) {
    		if(Validator.isMobile(phone)){//是合格的手机号
				AdminInfo ai = this.adminDAO.findAdminByPhone(phone);
				BbsUser bu = this.bbsUserDAO.findBbsUserByPhone(phone);
	            if (ai != null || (bu != null && bu.getAppRegTime()!=null)) {
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
        String email = this.adminInfo.getEmail();
        if (email != null && !"".equals(email)) {
            if (!email.contains("@")) {
                this.emailOk = false;
                this.emailTip = "邮箱格式不正确";
                return null;
            }
            AdminInfo ai = this.adminDAO.findAdminByEmail(email);
            BbsUser bu = this.bbsUserDAO.findBbsUserByEmail(email);
            if (ai != null || bu != null) {
                this.emailOk = false;
                this.emailTip = "此邮箱已存在";
            } else {
                this.emailOk = true;
                this.emailTip = "此邮箱可以注册";
            }
        }
        return null;
    }

    public String refreshAuth() {
        this.id_temp = new Long(System.currentTimeMillis()).toString();
        return null;
    }

    public String reg_ok() {
        //Cat.println("hihi");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String path=request.getSession().getServletContext().getRealPath("");//项目物理路径
        
//        if (adminInfo.getUrn().trim().equals("")){
//        	JsfHelper.error("用户名不能为空");
//            return null;
//        }
        
        if(adminInfo.getPwd().trim().equals("")){
        	JsfHelper.error("密码不能为空");
            return null;
        }
        
        if(this.pwd_re.trim().equals("")){
        	JsfHelper.error("密码确认不能为空");
            return null;
        }
        
//        if(adminInfo.getNickname().trim().equals("")){
//        	JsfHelper.error("公司名称不能为空");
//            return null;
//        }
        
//        if (adminInfo.getEmail().trim().equals("") && this.scd.getSystemConfig().getEmailValidation()) {
//            JsfHelper.error("邮箱不能为空");
//            return null;
//        }

        if (adminInfo.getTel().trim().equals("")){
        	JsfHelper.error("手机号不能为空");
            return null;
        }
        
        if (this.verifyCode.trim().equals("")){
        	JsfHelper.error("短信验证码不能为空");
            return null;
        }
        
        //是否强制验证身份证
//        if (this.scd.getSystemConfig().isUsePid()) {
//            if (user.getPid().trim().equals("")) {
//                JsfHelper.error("身份证号不能为空");
//                return null;
//            } else if (this.user.getPid().length() != 18) {
//                JsfHelper.error("身份证号必须为18位");
//                return null;
//            } else {
//                BbsUser us0 = bbsUserDAO.findBbsUserByPid(user.getPid());
//                if (us0 != null) {
//                    JsfHelper.error("此身份证号已经存在");
//                    return null;
//                }
//            }
//        }

        //验证部门是否作了选择
//        boolean departOk = true;
//        if (this.selectedNode == null) {
//            departOk = false;
//        }
        //所选部门若为空，则设置其部门为默认部门，若默认部门为空，则提示用户必须选择一个部门
//        StringBuilder sb = new StringBuilder();
//        if (departOk) {
//            
//            this.user.setGroupStr(selectedNode.getId());
//        } else {
//            String departStr = this.dicDAO.findAllDefaultDepartmentStr();
//            if (departStr == null || "".equals(departStr)) {
//                ApplicationBean ab = JsfHelper.getBean("applicationBean");
//                JsfHelper.error("必须选择一个" + ab.getDepartmentStr() + "！");
//                return null;
//            } else {
//                this.user.setGroupStr(departStr);
//            }
//        }

        if (!adminInfo.getPwd().equals(this.pwd_re)) {
            JsfHelper.error("密码与确认密码不一致");
            return null;
        }
        
        HttpSession s = (HttpSession) request.getSession(true);
        String str = (String) s.getAttribute("rand");
        if (!this.authStr.equals(str)) {
            JsfHelper.error("验证码不正确");
            return null;
        }

//        if (adminDAO.findAdminByUrn(adminInfo.getUrn()) != null || bbsUserDAO.findBbsUserByUrn(adminInfo.getUrn())!=null) {
//            JsfHelper.error("此用户已经存在");
//            return null;
//        }
        
        if ((adminDAO.findAdminByEmail(adminInfo.getEmail()) != null||bbsUserDAO.findBbsUserByEmail(adminInfo.getEmail())!=null)  && scd.getSystemConfig().getEmailValidation()) {
            JsfHelper.error("此E-Mail已经存在");
            return null;
        }
        
        CacheManager cacheManager = CacheManager.newInstance(path+"\\WEB-INF\\classes\\ehcache.xml");//创建缓存manager
		Cache cache = cacheManager.getCache("verifyCode");
		if(cache.get(adminInfo.getTel().trim()+this.verifyCode)!=null){//该手机号的验证码element缓存存在
			if("true".equals(cache.get(adminInfo.getTel().trim()+this.verifyCode).getValue())){//验证码正确
				cache.remove(adminInfo.getTel().trim()+this.verifyCode);//验证成功，移除此验证码
			}
		}else{//短信验证码错误或失效
			JsfHelper.error("短信验证码错误或已过期");
            return null;
		}
        
        BbsUser user1=bbsUserDAO.findBbsUserByPhone(adminInfo.getTel());
        AdminInfo admin1=adminDAO.findAdminByPhone(adminInfo.getTel());
        if ( admin1!= null || (user1!=null && user1.getAppRegTime()!=null)) {
            JsfHelper.error("此手机号已经存在");
            return null;
        }else{
        	if(user1!=null){
        		user1.setUsername(adminInfo.getTel().trim());
            	user1.setPassword(adminInfo.getPwd().trim());
            	user1.setName(adminInfo.getNickname().trim());
            	user1.setEmail(adminInfo.getEmail().trim());
            	user1.setTel(adminInfo.getTel().trim());
            	ComplexBbsUserService cbus = SpringHelper.getSpringBean("ComplexBbsUserService");
            	cbus.handlePwd(user1);
            	user1.setChecked(this.scd.getSystemConfig().getAutoCheck());
            	user1.setAppRegTime(new Date());
            	bbsUserDAO.updateBbsUser(user1);
        	}else{
        		this.user.setPassword(adminInfo.getPwd().trim());
        		this.user.setUsername(adminInfo.getTel().trim());
        		this.user.setTel(adminInfo.getTel().trim());
           	 	this.user.setAppRegTime(new Date());
                String ip = IpHelper.getRemoteAddr(request);
                this.user.setRegIp(ip);
                this.user.setLastIp(ip);
                //将密码变换
                ComplexBbsUserService cbus = SpringHelper.getSpringBean("ComplexBbsUserService");
                cbus.handlePwd(user);
                this.user.setChecked(this.scd.getSystemConfig().getAutoCheck());
                this.user.setAppRegTime(new Date());
                this.user.setGroupStr("");
                bbsUserDAO.addBbsUser(user);
        	}
        	 this.adminInfo.setUrn(adminInfo.getTel().trim());
             this.adminInfo.setGrp("company");
             this.adminInfo.setAuthstr("lesson;user;class");
             this.adminInfo.setEnabled(true);
             this.adminInfo.setGenTime(new Date());//产生时间
             this.adminInfo.setLtime(new Date());//注册时间
             this.adminInfo.setChecked(this.scd.getSystemConfig().getAutoCheck());
             adminDAO.addAdmin(adminInfo);
             
             if (!this.scd.getSystemConfig().getAutoCheck()) {
                 return "WaitingCheck?faces-redirect=true";
             }
             
             return "RegFinished";
        }
        

        
//        String ip = IpHelper.getRemoteAddr(request);
//        this.user.setRegIp(ip);
//        this.user.setLastIp(ip);
        //将密码变换
//        ComplexBbsUserService cbus = SpringHelper.getSpringBean("ComplexBbsUserService");
//        cbus.handlePwd(user);
//        if (this.scd.getSystemConfig().getEmailValidation()) {
//            HttpServletRequest req = JsfHelper.getRequest();
//            String host = req.getServerName();
//            int port = req.getServerPort();
//            String sign = MD5.bit32(user.getId());
//            String url = "http://" + host + ":" + port
//                    + req.getContextPath() + "/servlet/RegisterActivate?urn=";
//            String urn = user.getUsername();
//            try {
//                urn = URLEncoder.encode(urn, "UTF-8");
//                System.out.println(url);
//            } catch (Exception ee) {
//            }
//            url = url + urn + "&amp;sign=" + sign;
//            //String mailbody = "点击<a href=\"" + url + "\">这里</a>激活帐号！";
//            //ServletContext sc = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
//
//            Map map = new HashMap();
//            map.put("username", urn);
//            map.put("url", url);
//            map.put("sysname", this.infoDAO.findSystemInfo().getSiteName());
//            boolean b = this.emailService.sendWithTemplate(this.user.getEmail(), this.infoDAO.findSystemInfo().getSiteName() + "系统帐号激活信", "reg_mail.vm", map);
//            if (!b) {
//                JsfHelper.error("本系统要求验证邮箱，但验证邮件无法发出，因此无法注册");
//                return null;
//            }
//            this.user.setActivated(false);
//            this.user.setChecked(this.scd.getSystemConfig().getAutoCheck());
//
//            bbsUserDAO.addBbsUser(user);
//            return "RegEmailOut?faces-redirect=true";
//        }
       
        
//        this.user.setUsername(adminInfo.getUrn().trim());
//        this.user.setPassword(this.pwd_re.trim());
//        this.user.setEmail(adminInfo.getEmail());
//        this.user.setTel(adminInfo.getTel());
//        String ip = IpHelper.getRemoteAddr(request);
//        this.user.setRegIp(ip);
//        this.user.setLastIp(ip);
//        this.user.setName(adminInfo.getNickname());
//        this.user.setType("company");
//        this.user.setEnabled(true);
//        this.user.setChecked(this.scd.getSystemConfig().getAutoCheck());
//        this.user.setRegTime(date);
//        ComplexBbsUserService cbus = SpringHelper.getSpringBean("ComplexBbsUserService");
//        cbus.handlePwd(user);
        
//        this.user.setChecked(this.scd.getSystemConfig().getAutoCheck());
//        bbsUserDAO.addBbsUser(user);

       

//        ClientSession cs = JsfHelper.getBean("clientSession");
//        cs.setIfLogin(true);
//        cs.setUsr(user);
//
//        iussService.login(user);

        //第三方平台添加用户
//        Boolean ifLDAP = Boolean.parseBoolean(SpringHelper.getSpringBean("usercheck_thirdParty").toString());
//        if (ifLDAP) {//使用第三方验证的情况
//            IThirdPartyUserService third = SpringHelper.getSpringBean("ThirdPartyUserService");
//            third.register(user.getUsername(), pwd_re, user.getEmail());
//        }

        

    }
}
