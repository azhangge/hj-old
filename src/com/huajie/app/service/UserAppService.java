package com.huajie.app.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.jcajce.provider.asymmetric.RSA;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

import com.hazelcast.util.CollectionUtil;
import com.hjedu.businessuser.dao.IBusinessUserDao;
import com.hjedu.businessuser.entity.BusinessUser;
import com.hjedu.common.dao.SuggestionDAO;
import com.hjedu.common.dao.UserCommentDAO;
import com.hjedu.common.entity.Suggestion;
import com.hjedu.course.dao.CourseTypeDAO;
import com.hjedu.course.dao.ICourseApproveHistoryDAO;
import com.hjedu.course.dao.ICourseTypeDAO;
import com.hjedu.course.dao.ILessonDAO;
import com.hjedu.course.dao.ILessonLogDAO;
import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.dao.ILessonTypeLogDAO;
import com.hjedu.course.dao.IStudyPlanChangeLogDAO;
import com.hjedu.course.dao.IStudyPlanDAO;
import com.hjedu.course.dao.IStudyPlanLogDAO;
import com.hjedu.course.entity.CourseApproveHistory;
import com.hjedu.course.entity.CourseOfPlan;
import com.hjedu.course.entity.Lesson;
import com.hjedu.course.entity.LessonLog;
import com.hjedu.course.entity.LessonType;
import com.hjedu.course.entity.LessonTypeLog;
import com.hjedu.course.entity.StudyPlan;
import com.hjedu.course.entity.StudyPlanChangeLog;
import com.hjedu.course.entity.StudyPlanLog;
import com.hjedu.course.entity.UserComment;
import com.hjedu.course.service.IBuyCourseService;
import com.hjedu.course.vo.LessonVO;
import com.hjedu.customer.dao.CertificateDAO;
import com.hjedu.customer.dao.IAdminDAO;
import com.hjedu.customer.dao.IBbsFileDAO;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.dao.ITeacherDAO;
import com.hjedu.customer.dao.IUserInfoDAO;
import com.hjedu.customer.dao.JobTitleDAO;
import com.hjedu.customer.dao.ProjectExperienceDAO;
import com.hjedu.customer.dao.WorkExperienceDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.entity.Certificate;
import com.hjedu.customer.entity.JobTitle;
import com.hjedu.customer.entity.ProjectExperience;
import com.hjedu.customer.entity.Teacher;
import com.hjedu.customer.entity.WorkExperience;
import com.hjedu.customer.service.IUserService;
import com.hjedu.customer.service.IUserSessionStateService;
import com.hjedu.customer.service.impl.ComplexBbsUserService;
import com.hjedu.examination.dao.GeneralQuestionDAO;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.dao.IModuleExamCaseDAO;
import com.hjedu.examination.dao.IModuleExaminationDAO;
import com.hjedu.examination.dao.IQuestionCollectionDAO;
import com.hjedu.examination.entity.CourseType;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.ExamCaseItemAdapter;
import com.hjedu.examination.entity.ExamCaseItemChoice;
import com.hjedu.examination.entity.ExamCaseItemEssay;
import com.hjedu.examination.entity.ExamCaseItemFill;
import com.hjedu.examination.entity.ExamCaseItemJudge;
import com.hjedu.examination.entity.ExamCaseItemMultiChoice;
import com.hjedu.examination.entity.ExamChoice;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.ExamPaperRandom;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.entity.GeneralQuestion;
import com.hjedu.examination.entity.QuestionCollection;
import com.hjedu.examination.entity.VirtualExamPart;
import com.hjedu.examination.entity.module.ModuleExamCase;
import com.hjedu.examination.entity.module.ModuleExamCaseItemChoice;
import com.hjedu.examination.entity.module.ModuleExamCaseItemJudge;
import com.hjedu.examination.entity.module.ModuleExamCaseItemMultiChoice;
import com.hjedu.examination.entity.module2.ModuleExamination2;
import com.hjedu.examination.service.IExamCaseCacheService;
import com.hjedu.examination.service.IExamCaseService;
import com.hjedu.examination.service.IModuleExamCaseService;
import com.hjedu.platform.controller.ApplicationBean;
import com.hjedu.platform.dao.ComplexFileLogic;
import com.hjedu.platform.dao.IAdvDAO;
import com.hjedu.platform.dao.IBbsScoreLogDAO;
import com.hjedu.platform.dao.IImgDAO;
import com.hjedu.platform.dao.INoticeDAO;
import com.hjedu.platform.dao.INoticeUserDAO;
import com.hjedu.platform.dao.IPictureDAO;
import com.hjedu.platform.dao.ISendCodeFrequencyDAO;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.entity.BbsFileModel;
import com.hjedu.platform.entity.NoticeModel;
import com.hjedu.platform.entity.NoticeUser;
import com.hjedu.platform.entity.PictureModel;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.hjedu.customer.dao.IUserTokenDAO;
import com.huajie.app.model.SendCodeFrequency;
import com.huajie.app.model.UserToken;
import com.huajie.app.util.CodeUtils;
import com.huajie.app.util.DateUtil;
import com.huajie.app.util.FileUtil;
import com.huajie.app.util.NetworkUtil;
import com.huajie.app.util.SmsHelper;
import com.huajie.app.util.StringUtil;
import com.huajie.app.util.UrlUtil;
import com.huajie.app.util.Validator;
import com.huajie.app.util.sendMessageHelper;
import com.huajie.app.view.Carousel;
import com.huajie.app.view.CarouselGetView;
import com.huajie.app.view.CourseDetailView;
import com.huajie.app.view.Exam;
import com.huajie.app.view.GetUserIdByTokenJson;
import com.huajie.app.view.GetUserInfoJson;
import com.huajie.app.view.LoginInUserJson;
import com.huajie.app.view.Material;
import com.huajie.app.view.Practice;
import com.huajie.app.view.UserInf;
import com.huajie.app.view.UserSys;
import com.huajie.exam.pool.ExamCaseController;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.exam.thread.ExamCaseSaver;
import com.huajie.exam.thread.ModuleExamCaseSaver;
import com.huajie.util.Constants;
import com.huajie.util.DESTool;
import com.huajie.util.CookieUtils;
import com.huajie.util.IpHelper;
import com.huajie.util.JsfHelper;
import com.huajie.util.JsonUtil;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;
import com.jspsmart.upload.Request;
import com.jspsmart.upload.SmartUpload;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class UserAppService implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(UserAppService.class);
	IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
	IUserTokenDAO userTokenDAO = SpringHelper.getSpringBean("UserTokenDAO");
	ISendCodeFrequencyDAO sendCodeFrequencyDAO = SpringHelper.getSpringBean("SendCodeFrequencyDAO");
	ComplexBbsUserService userService = SpringHelper.getSpringBean("ComplexBbsUserService");
	IAdminDAO adminDAO = SpringHelper.getSpringBean("AdminDAO");
	IAdvDAO advDAO = SpringHelper.getSpringBean("AdvDAO");
	IPictureDAO pictureDAO = SpringHelper.getSpringBean("PictureDAO");
	ILessonTypeDAO lessonTypeDAO = SpringHelper.getSpringBean("LessonTypeDAO");
	IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
	IExaminationDAO examinationDAO = SpringHelper.getSpringBean("ExaminationDAO");
	ILessonDAO lessonDAO = SpringHelper.getSpringBean("LessonDAO");
	IImgDAO imgDAO = SpringHelper.getSpringBean("ImgDAO");
	ILessonLogDAO lessonLogDAO = SpringHelper.getSpringBean("LessonLogDAO");
	IModuleExamCaseService examCaseService = SpringHelper.getSpringBean("Module2ExamCaseService");
	IModuleExaminationDAO mexaminationDAO = SpringHelper.getSpringBean("ModuleExaminationDAO");
	IModuleExamCaseDAO mexamCaseDAO = SpringHelper.getSpringBean("ModuleExamCaseDAO");
	IQuestionCollectionDAO selectDAO = SpringHelper.getSpringBean("QuestionCollectionDAO");
	IExamModule2DAO examModule2DAO = SpringHelper.getSpringBean("ExamModule2DAO");
	ICourseTypeDAO courseTypeDAO = SpringHelper.getSpringBean("CourseTypeDAO");
	IBbsScoreLogDAO olDAO = SpringHelper.getSpringBean("BbsScoreLogDAO");
	IStudyPlanDAO studyPlanDAO = SpringHelper.getSpringBean("StudyPlanDAO");
	ILessonTypeLogDAO lessonTypeLogDAO = SpringHelper.getSpringBean("LessonTypeLogDAO");
	ITeacherDAO TeacherDAO = SpringHelper.getSpringBean("TeacherDAO");
	IStudyPlanLogDAO studyPlanLogDAO = SpringHelper.getSpringBean("StudyPlanLogDAO");
	IStudyPlanChangeLogDAO studyPlanChangeLogDAO = SpringHelper.getSpringBean("StudyPlanChangeLogDAO");
	ISystemConfigDAO sysDAO = SpringHelper.getSpringBean("SystemConfigDAO");
	IModuleExamCaseDAO moduleExamCaseDAO = SpringHelper.getSpringBean("ModuleExamCaseDAO");
	IBbsFileDAO bbsFileDAO = SpringHelper.getSpringBean("BbsFileDAO");
	IBbsFileDAO project2DAO = SpringHelper.getSpringBean("BbsFileDAO");
	ComplexFileLogic cxLogic = SpringHelper.getSpringBean("ComplexFileLogic");
	ICourseApproveHistoryDAO courseApproveHistoryDAO = SpringHelper.getSpringBean("CourseApproveHistoryDAO");
	IUserService uService = SpringHelper.getSpringBean("UserService");
	JobTitleDAO jtdao = SpringHelper.getSpringBean("JobTitleDAO");
	WorkExperienceDAO wedao = SpringHelper.getSpringBean("WorkExperienceDAO");
	ProjectExperienceDAO pedao = SpringHelper.getSpringBean("ProjectExperienceDAO");
	CertificateDAO cfdao = SpringHelper.getSpringBean("CertificateDAO");
	IBusinessUserDao businessUserDAO = SpringHelper.getSpringBean("BusinessUserDAO");
	IUserInfoDAO userInfoDAO = SpringHelper.getSpringBean("UserInfoDAO");
	
	
	
	/**
	 * 接收json类型数据
	 */
	public JSONObject getJSONObjectByRequest(HttpServletRequest request) {
		StringBuffer json = new StringBuffer();
		String line = null;
		JSONObject myJsonObject = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				json.append(line);
			}
			myJsonObject = new JSONObject(json.toString());
			reader.close();
		} catch (IOException e) {
			MyLogger.println("解析手机端发送的json数据失败，请查看后台日志");
			e.printStackTrace();
		}
		return myJsonObject;
	}
	/**
	 * 通过token获取用户id
	 */
	public Map<String,Object> getUserIdByToken(HttpServletRequest request){
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		Map<String, Object> sendJson = new HashMap<>();
		if (myJsonObject.has("token")) {
			String token=myJsonObject.getString("token");
			String id = userTokenDAO.getIdByToken(token);
			if (id == null) {// 该token已失效
				sendJson.put("result", "3");
				sendJson.put("message", "该token已失效");
				return sendJson;
			} else {
				sendJson.put("result", "1");
				sendJson.put("message", "获取成功");
				sendJson.put("user_id", id);
			}
		} else {// 没有获取到token
			sendJson.put("result", "2");
			sendJson.put("message", "没有获取到参数");
		}
		return sendJson;
	}
	
	/**
	 * 给指定手机号发送验证码
	 */
	public Map<String,Object> sendCode(HttpServletRequest request){
		String bussinessId = CookieUtils.getBusinessId(request);
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		Map<String, Object> sendJson = new HashMap<>();
		String phoneno = "";
		String sysId = bussinessId;
		String type = "";
		if(!myJsonObject.isNull("phoneno") && !myJsonObject.isNull("type")){
			phoneno = myJsonObject.getString("phoneno");
			type = myJsonObject.getString("type");
			if(Validator.isMobile(phoneno)){//是合格的手机号
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("phoneno", phoneno);
				map.put("sysId", sysId);
				BbsUser user =  bbsUserDAO.findBbsUserByPhoneBusinessId(phoneno,sysId);
				if(type.equals("1")){//1 注册
					if(user!=null){
						sendJson.put("result", "8");
						sendJson.put("message", "此子系统有此手机号的用户");
	        			return sendJson;
					}
	    		}else if(type.equals("2")){//2 登录
        			if(user==null){
        				sendJson.put("result", "7");
						sendJson.put("message", "此子系统没有此手机号的用户");
	        			return sendJson;
	        		}
        		}else if(type.equals("3")){//3 忘记密码
        			if(user==null){
        				sendJson.put("result", "7");
						sendJson.put("message", "此子系统没有此手机号的用户");
	        			return sendJson;
	        		}
        		}else{
        			sendJson.put("result", "9");
					sendJson.put("message", "type值无效");
        			return sendJson;
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
//        		if(ip_count>=Constants.IP_MAX_COUNT){
//        			sendJson.put("result", "4");
//					sendJson.put("message", "此ip今天已超过最多发送次数");
//        			return sendJson;
//        		}
        		phone_count=sendCodeFrequencyDAO.getCountByPhoneOneDay(phoneno,todayTime,tomorrowTime);
        		if(phone_count>=Constants.PHONE_MAX_COUNT){//此手机号超过24小时内最多发送次数
        			sendJson.put("result", "5");
					sendJson.put("message", "此手机号今天已超过24小时内最多发送次数");
        			return sendJson;
        		}
        		long send_last_time;
            	long now_time;
            	if(sendCodeFrequencyDAO.getByPhone(phoneno)!=null){//有发送验证码记录
        			send_last_time=sendCodeFrequencyDAO.getByPhone(phoneno).getSendLastTime().getTime();
        			now_time=nowTime.getTime();
        			if(now_time-send_last_time<2*60*1000){//此手机号的发送间隔小于2分钟
        				sendJson.put("result", "6");
    					sendJson.put("message", "此手机号的发送间隔小于2分钟");
	        			return sendJson;
        			}
        		}
        		
        		//发送短信	        		
        		String verify_code;
            	SendCodeFrequency sendCodeFrequency = new SendCodeFrequency();//验证码发送频率对象
            	String phone = phoneno;
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
    			sendJson.put("result", "1");
				sendJson.put("message", "短信发送成功");
			}else{
				sendJson.put("result", "3");
				sendJson.put("message", "不是合格的手机号");
			}
		}else{
			sendJson.put("result", "2");
			sendJson.put("message", "系统没有获取到正确的参数");
		}
		return sendJson;
	}

	/**
	 * 确认手机发过来的短信验证码
	 */
	public Map<String,Object> confirmCode(HttpServletRequest request) {
		String bussinessId = CookieUtils.getBusinessId(request);
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		Map<String, Object> sendJson = new HashMap<>();
		String phoneno= "";
		String code = "";
		String type = "";
		String sysId = bussinessId;
		String device = "";
		String password = "";
		String targetSysId = "";
		if(!myJsonObject.isNull("phoneno") && !myJsonObject.isNull("code") && !myJsonObject.isNull("type") 
				&& !myJsonObject.isNull("device")){
			 phoneno = myJsonObject.getString("phoneno");
			 code = myJsonObject.getString("code");
			 type = myJsonObject.getString("type");
			 device = myJsonObject.getString("device");
			 if(!myJsonObject.isNull("password")){
				password = myJsonObject.getString("password");
				
			}
			 
			 if(Validator.isMobile(phoneno)){//是合格的手机号
				 String path=request.getRealPath("");
				 CacheManager cacheManager = CacheManager.newInstance(path+"\\WEB-INF\\classes\\ehcache.xml");
				 Cache cache = cacheManager.getCache("verifyCode");
				 if(cache.get(phoneno+code)!=null){//该手机号的验证码element缓存存在
					cache.remove(phoneno+code);//验证成功，移除此验证码
					String user_id = null;
					BbsUser user = null;
					String ip = null;
					try {
						ip = NetworkUtil.getIpAddress(request);
					} catch (IOException e) {
						e.printStackTrace();
					}
					user = bbsUserDAO.findBbsUserByPhoneBusinessId(phoneno, sysId);
					DESTool tool = new DESTool();
					if(type.equals("1")){//注册
						if(user!=null){
							sendJson.put("result", "6");
							sendJson.put("message", "此手机号已注册");
		        			return sendJson;
						}

						List<BbsUser> bbsUserList = bbsUserDAO.findBbsUserByPhones(phoneno);
						String pid = "";
				        String email = "";
				        String name = "";
				        String gender = "";
				        String qq = "";
				        Date birthday = null;
				        for(BbsUser bu:bbsUserList){
				        	if(StringUtil.isNotEmpty(bu.getPid())){
				        		pid = bu.getPid();
				        	}
				        	if(StringUtil.isNotEmpty(bu.getEmail())){
				        		email = bu.getEmail();
				        	}
				        	if(StringUtil.isNotEmpty(bu.getName())){
				        		name = bu.getName();
				        	} 
				        	if(StringUtil.isNotEmpty(bu.getGender())){
				        		gender = bu.getGender();
				        	} 
				        	if(StringUtil.isNotEmpty(bu.getQq())){
				        		qq = bu.getQq();
				        	} 
				        	if(bu.getBirthDay()!=null){
				        		birthday = bu.getBirthDay();
				        	}
				        }
						
				        password = tool.encrypt(password);
				        for(BbsUser bu1:bbsUserList){
				        	if(StringUtil.isNotEmpty(pid)){
				        		bu1.setPid(pid);
				        	}
				        	if(StringUtil.isNotEmpty(email)){
				        		bu1.setEmail(email);
				        	}
				        	if(StringUtil.isNotEmpty(name)){
				        		bu1.setName(name);
				        	}
				        	if(StringUtil.isNotEmpty(gender)){
				        		bu1.setGender(gender);
				        	}
				        	if(StringUtil.isNotEmpty(qq)){
				        		bu1.setQq(qq);
				        	}
				        	if(birthday!=null){
				        		bu1.setBirthDay(birthday);
				        	}
				        	bu1.setPassword(password);
				        	bbsUserDAO.updateBbsUser(bu1);
				        }
				        
				        List<BusinessUser> publicList = businessUserDAO.findAllOpenBussinessUser();
						BusinessUser businessUser = businessUserDAO.findBussinessUser(sysId);
						if(businessUser != null){
							if(businessUser.getIsOpen()==false){
								publicList.add(businessUser);
							}
						}
						
						for(BusinessUser bus:publicList){
							for(BbsUser bu2:bbsUserList){
								if(bu2.getBusinessId().equals(bus.getId()) && bu2.getBusinessId().equals(sysId)){//已注册
									publicList.remove(bus);
								}
							}
						}
						
						for(BusinessUser busu:publicList){
							BbsUser bbsu = new BbsUser();
							bbsu.setTel(phoneno);
							if(StringUtil.isNotEmpty(password)){
								
								bbsu.setPassword(password);
							}
							if(StringUtil.isNotEmpty(pid)){
								bbsu.setPid(pid);
							}
							if(StringUtil.isNotEmpty(email)){
								bbsu.setEmail(email);
							}
							if(StringUtil.isNotEmpty(name)){
								bbsu.setName(name);
							}
							if(StringUtil.isNotEmpty(gender)){
								bbsu.setGender(gender);
							}
							if(StringUtil.isNotEmpty(qq)){
								bbsu.setQq(qq);
							}
							if(birthday!=null){
								bbsu.setBirthDay(birthday);
							}
							bbsu.setBusinessId(busu.getId());
							bbsu.setGroupStr(busu.getId()+";");
							bbsUserDAO.addBbsUser(bbsu);
							
							if(busu.getId().equals(sysId)){
								user_id = bbsu.getId();
							}
						}	
						sendJson.put("result", "1");
						sendJson.put("message", "此手机号验证成功");
					}else if(type.equals("2")){//2 登录
						if(user==null){
							sendJson.put("result", "5");
							sendJson.put("message", "没有此手机号的用户");
		        			return sendJson;
						}
					}else if(type.equals("3")){//3 忘记密码
						if(user==null){
							sendJson.put("result", "5");
							sendJson.put("message", "没有此手机号的用户");
		        			return sendJson;
						}
//						user.setPassword(tool.encrypt(password));
//						bbsUserDAO.updateBbsUser(user);
						
						this.modifyAllPassword(tool.encrypt(password), user.getTel());
						
						sendJson.put("result", "1");
						sendJson.put("message", "设置成功");
					}
					if(device.equals("1")){//手机端
						user = bbsUserDAO.findBbsUserByPhoneBusinessId(phoneno, sysId);
						UserToken userToken=new UserToken();
						userToken.setUser_id(user.getId());
						userToken.setCreateTime(new Date());
						userTokenDAO.addUserToken(userToken);
						
						List<BbsUser> bulist = bbsUserDAO.findBbsUserByPhones(phoneno);
						List<UserSys> userSyslist = new ArrayList<UserSys>();
						for(BbsUser bul:bulist){
							UserSys userSys = new UserSys();
							BusinessUser bus = businessUserDAO.findBussinessUser(bul.getBusinessId());
							userSys.setSysId(bus.getId());
							userSys.setSysName(bus.getBusinessNameCn());
							userSys.setSysRootPath("http://"+bus.getDomainName()+":"+bus.getPort());
							userSys.setSysPic(bus.getSysPic());
							userSyslist.add(userSys);
						}
						sendJson.put("userSyslist", userSyslist);
						targetSysId = sysId;
						sendJson.put("targetSysId", sysId);
						sendJson.put("token", userToken.getToken());
					}
				 }else{
					 sendJson.put("result", "4");
					 sendJson.put("message", "短信验证码错误或失效");
				 }
			 }else{
				 sendJson.put("result", "3");
				 sendJson.put("message", "不是合格的手机号");
			 }
		}else{
			sendJson.put("result", "2");
			sendJson.put("message", "系统没有获取到正确的参数");
		}
		 return sendJson;
	}
	 
	/**
	 * 校验手机号是否在此子系统存在
	 */
	public Map<String,Object> verifyPhoneno(HttpServletRequest request){
		String bussinessId = CookieUtils.getBusinessId(request);
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		Map<String, Object> sendJson = new HashMap<>();
		String phoneno = "";
		String sysId = bussinessId;
		if(!myJsonObject.isNull("phoneno")){
			phoneno = myJsonObject.getString("phoneno");
			if(Validator.isMobile(phoneno)){//是合格的手机号
				BbsUser bu = bbsUserDAO.findBbsUserByPhoneBusinessId(phoneno, sysId);
				if(bu==null){
					sendJson.put("result", "1");
					sendJson.put("message", "此手机号可以注册");
					return sendJson;
				}
				sendJson.put("result", "4");
				sendJson.put("message", "此手机号已注册");
			}else{
				sendJson.put("result", "3");
				sendJson.put("message", "不是合格的手机号");
			}
		}else{
			sendJson.put("result", "2");
			sendJson.put("message", "系统没有获取到正确的参数");
		}
		return sendJson;
	}
	
	/**
	 * 普通登录
	 */
	public Map<String,Object> loginInUser(HttpServletRequest request){
		String bussinessId = CookieUtils.getBusinessId(request);
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		Map<String, Object> sendJson = new HashMap<>();
		String phoneno = "";
		String password = "";
		String type = "";
		String sysId = bussinessId;
		String device = "";
		String targetSysId = "";
		String user_id = "";
		boolean isPassTrue = false;
		
		if(!myJsonObject.isNull("phoneno") && !myJsonObject.isNull("password") && !myJsonObject.isNull("type") 
				 && !myJsonObject.isNull("device")){
			phoneno = myJsonObject.getString("phoneno");
			password = myJsonObject.getString("password");
			device = myJsonObject.getString("device");
			type = myJsonObject.getString("type");
			
			if(Validator.isMobile(phoneno)){//是合格的手机号
				DESTool tool = new DESTool();
				BbsUser bu = bbsUserDAO.findBbsUserByPhoneBusinessId(phoneno, sysId);
				if(bu != null){
					user_id = bu.getId();
					if(type.equals("1")){//密码登录
						if(bu.getPassword()==null){
							sendJson.put("result", "6");
							sendJson.put("message", "没有设置密码");
							return sendJson;
						}
						isPassTrue = bu.getPassword().equals(tool.encrypt(password));
					}else if(type.equals("2")){//手势密码登录
						if(bu.getHandPassword()==null){
							sendJson.put("result", "7");
							sendJson.put("message", "没有设置手势密码");
							return sendJson;
						}
						isPassTrue = bu.getHandPassword().equals(tool.encrypt(password));
					}
					if(isPassTrue == true){
						List<BbsUser> bulist = bbsUserDAO.findBbsUserByPhones(phoneno);
						List<UserSys> userSyslist = new ArrayList<UserSys>();
						for(BbsUser bul:bulist){
							UserSys userSys = new UserSys();
							BusinessUser businessUser = businessUserDAO.findBussinessUser(bul.getBusinessId());
							userSys.setSysId(businessUser.getId());
							userSys.setSysName(businessUser.getBusinessNameCn());
							userSys.setSysRootPath("http://"+businessUser.getDomainName()+":"+businessUser.getPort());
							userSys.setSysPic(businessUser.getSysPic());
							userSyslist.add(userSys);
						}
						sendJson.put("userSyslist", userSyslist);
						targetSysId = sysId;
						sendJson.put("targetSysId", sysId);
						
						if(device.equals("1")){
							UserToken ut = new UserToken();
							ut.setUser_id(user_id);
							ut.setCreateTime(new Date());
							userTokenDAO.addUserToken(ut);
							sendJson.put("result", "1");
							sendJson.put("message", "登录成功");
							sendJson.put("token",ut.getToken());
						}else if(device.equals("2")){
							sendJson.put("result", "1");
							sendJson.put("message", "登录成功");
						}
					}else{
						sendJson.put("result", "5");
						sendJson.put("message", "密码错误");
						return sendJson;
					}		
				}else{
					sendJson.put("result", "4");
					sendJson.put("message", "此用户不存在");
					return sendJson;
				}
			}else{
				sendJson.put("result", "3");
				sendJson.put("message", "不是合格的手机号");
			}
		}else{
			sendJson.put("result", "2");
			sendJson.put("message", "系统没有获取到正确的参数");
		}
		return sendJson;
	}
	
	/**
	 * 设置密码
	 */
	public Map<String,Object> setPassword(HttpServletRequest request){
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		Map<String,Object> sendJson = new HashMap<>();
		String token = "";
		String user_id = "";
		String password = "";
		String type = "";
		String device = "";
		
		if(!myJsonObject.isNull("password") && !myJsonObject.isNull("type") && !myJsonObject.isNull("device")){
			password = myJsonObject.getString("password");
			type = myJsonObject.getString("type");
			device = myJsonObject.getString("device");
			if(device.equals("1")){//手机
				if(!myJsonObject.isNull("token")){
					token = myJsonObject.getString("token");
					user_id = userTokenDAO.getIdByToken(token);
					if(user_id==null){
						sendJson.put("result", "3");
						sendJson.put("message", "此token已失效");
						return sendJson;
					}
					DESTool tool = new DESTool();
					password = tool.encrypt(password);
					BbsUser bbsUser = bbsUserDAO.findBbsUser(user_id);
					
					if(bbsUser!=null){
						List<BbsUser> bbsUserList = bbsUserDAO.findBbsUserByPhones(bbsUser.getTel());
						if (!bbsUserList.isEmpty()) {
							
							if (type.equals("1")) {//普通密码
								for (BbsUser user : bbsUserList) {
									user.setPassword(password);
									bbsUserDAO.updateBbsUser(user);
								} 
								sendJson.put("result", "1");
								sendJson.put("message", "设置成功");
							} else if (type.equals("2")) {//手势密码
								for (BbsUser user : bbsUserList) {
									user.setHandPassword(password);
									bbsUserDAO.updateBbsUser(user);
								}
								sendJson.put("result", "1");
								sendJson.put("message", "设置成功");
							} 
							
						}
					}else{
						sendJson.put("result", "3");
						sendJson.put("message", "此token已失效");
					}
				}else{
					sendJson.put("result", "2");
					sendJson.put("message", "系统没有获取到正确的参数");
				}
			}
		}else{
			sendJson.put("result", "2");
			sendJson.put("message", "系统没有获取到正确的参数");
		}
		return sendJson;
	}
	
	/**
	 * 修改密码
	 */
	public Map<String,Object> changePassword(HttpServletRequest request){
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		Map<String,Object> sendJson = new HashMap<>();
		String token = "";
		String user_id = "";
		String oldpassword = "";
		String password = "";
		String device = "";
		if(!myJsonObject.isNull("oldpassword") && !myJsonObject.isNull("password") && !myJsonObject.isNull("device")){
			oldpassword = myJsonObject.getString("oldpassword");
			password = myJsonObject.getString("password");
			device = myJsonObject.getString("device");
			if(device.equals("1")){
				if(!myJsonObject.isNull("token")){
					token = myJsonObject.getString("token");
					user_id = userTokenDAO.getIdByToken(token);
					if(user_id==null){
						sendJson.put("result", "3");
						sendJson.put("message", "此token已失效");
						return sendJson;
					}
					BbsUser bbsUser = bbsUserDAO.findBbsUser(user_id);
					if(bbsUser!=null){
						DESTool tool = new DESTool();
						if(bbsUser.getPassword().equals(tool.encrypt(oldpassword))){
							bbsUser.setPassword(tool.encrypt(password));
							bbsUserDAO.updateBbsUser(bbsUser);
							userTokenDAO.deleteUserTokenByToken(token);
							UserToken ut=new UserToken();
							ut.setUser_id(bbsUser.getId());
							ut.setCreateTime(new Date());
							userTokenDAO.addUserToken(ut);
							List<BbsUser> bulist = bbsUserDAO.findBbsUserByPhones(bbsUser.getTel());
							List<UserSys> userSyslist = new ArrayList<UserSys>();
							for(BbsUser bul:bulist){
								// TODO 
								bul.setPassword(tool.encrypt(password));
								bbsUserDAO.updateBbsUser(bul);
								
								UserSys userSys = new UserSys();
								BusinessUser businessUser = businessUserDAO.findBussinessUser(bul.getBusinessId());
								userSys.setSysId(businessUser.getId());
								userSys.setSysName(businessUser.getBusinessNameCn());
								userSys.setSysRootPath("http://"+businessUser.getDomainName()+":"+businessUser.getPort());
								userSys.setSysPic(businessUser.getSysPic());
								userSyslist.add(userSys);
							}
							sendJson.put("userSyslist", userSyslist);
							sendJson.put("token", ut.getToken());
							sendJson.put("result", "1");
							sendJson.put("message", "设置成功");
						}else{
							sendJson.put("result", "4");
							sendJson.put("message", "原密码不正确");
						}	
					}else{
						sendJson.put("result", "3");
						sendJson.put("message", "此token已失效");
					}
				}else{
					sendJson.put("result", "2");
					sendJson.put("message", "系统没有获取到正确的参数");
				}
			}
		}
		return sendJson;
	}
	
	/**
	 * 用户注销
	 */
	public Map<String,Object> loginOutUser(HttpServletRequest request){
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		Map<String,Object> sendJson = new HashMap<>();
		String token = "";
		
		if( !myJsonObject.isNull("token")){
			token = myJsonObject.getString("token");
			UserToken userToken = userTokenDAO.getByToken(token);
			if(userToken != null){
				userTokenDAO.deleteUserTokenByToken(token);
				sendJson.put("result", "1");
				sendJson.put("message", "注销成功");
			}else{
				sendJson.put("result", "3");
				sendJson.put("message", "此token已失效");
			}
		}else{
			sendJson.put("result", "2");
			sendJson.put("message", "系统没有获取到正确的参数");
		}
		return sendJson;
	}
	
	
	/**
	 * 添加或修改用户信息
	 */
	public Map<String,Object> updateUserInfo(HttpServletRequest request) {
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		Map<String,Object> sendJson = new HashMap<>();
		
		String token = "";
		String user_id = "";
		String device = "";
		String phoneno = "";
		JSONObject userInfoJsonObject = new JSONObject();
		 
		BbsUser bbsUser = null;
		
		if(!myJsonObject.isNull("device")){
			device = myJsonObject.getString("device");
			if(device.equals("1")){//app
				if( !myJsonObject.isNull("token")){
					token = myJsonObject.getString("token");
					user_id = userTokenDAO.getIdByToken(token);
					if(user_id==null){
						sendJson.put("result", "3");
						sendJson.put("message", "此token已失效");
						return sendJson;
					}
				}else{
					sendJson.put("result", "2");
					sendJson.put("message", "系统没有获取到正确的参数");
				}
				
				bbsUser = bbsUserDAO.findBbsUser(user_id);
				if(bbsUser!=null){
					phoneno = bbsUser.getTel();

					if(!myJsonObject.isNull("userInf")){
						userInfoJsonObject = myJsonObject.getJSONObject("userInf");
						
						List<BbsUser> bbsUserList = bbsUserDAO.findBbsUserByPhones(phoneno);
						
						String pid = "";
						String email = "";
						String name = "";
						String gender = "";
						String qq = "";
						String birthday = "";
						Date date = null;
	
						if(!userInfoJsonObject.isNull("user_pid")){
							pid = userInfoJsonObject.getString("user_pid");
						}
						 if(!userInfoJsonObject.isNull("user_email")){
							email = userInfoJsonObject.getString("user_email");
						}
					 	if(!userInfoJsonObject.isNull("user_name")){
							name = userInfoJsonObject.getString("user_name");
					 	}
					 	if(!userInfoJsonObject.isNull("user_sex")){
							gender = userInfoJsonObject.getString("user_sex");
						}
						if(!userInfoJsonObject.isNull("user_qq")){
							qq = userInfoJsonObject.getString("user_qq");
						}
						if(!userInfoJsonObject.isNull("user_birthday")){
							birthday = userInfoJsonObject.getString("user_birthday");
							date = DateUtil.foramteStringToData2(birthday);
						} 
						
				        for(BbsUser bu1:bbsUserList){
				        	if(StringUtil.isNotEmpty(pid)){
				        		bu1.setPid(pid);
				        	}
				        	if(StringUtil.isNotEmpty(email)){
				        		bu1.setEmail(email);
				        	}
				        	if(StringUtil.isNotEmpty(name)){
				        		bu1.setName(name);
				        	}
				        	if(StringUtil.isNotEmpty(gender)){
				        		bu1.setGender(gender);
				        	}
				        	if(StringUtil.isNotEmpty(qq)){
				        		bu1.setQq(qq);
				        	}
				        	if(date!=null){
				        		bu1.setBirthDay(date);
				        	}
				        	bbsUserDAO.updateBbsUser(bu1);
				        }
				        sendJson.put("result", "1");
						sendJson.put("message", "修改成功");
					}else{
						sendJson.put("result", "2");
						sendJson.put("message", "系统没有获取到正确的参数");
					}
				}else{
					sendJson.put("result", "3");
					sendJson.put("message", "没有此用户");
				}
			}
		}else{
			sendJson.put("result", "2");
			sendJson.put("message", "系统没有获取到正确的参数");
		}
		return sendJson;
	}

	/**
	 * 获取用户信息
	 */
	public Map<String,Object> getUserInfo(HttpServletRequest request) {
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		Map<String,Object> sendJson = new HashMap<>();
		String token = "";
		String user_id = "";
		String device = "";
		String phoneno = "";

		if( !myJsonObject.isNull("device")){
			device = myJsonObject.getString("device");
			if(device.equals("1")){//app
				if( !myJsonObject.isNull("token")){
					token = myJsonObject.getString("token");
					user_id = userTokenDAO.getIdByToken(token);
					if(user_id==null){
						sendJson.put("result", "3");
						sendJson.put("message", "此token已失效");
						return sendJson;
					}
				}else{
					sendJson.put("result", "2");
					sendJson.put("message", "系统没有获取到正确的参数");
				}
			}else if(device.equals("2")){
				if( !myJsonObject.isNull("user_id")){
					user_id = myJsonObject.getString("user_id");
				}else{
					sendJson.put("result", "2");
					sendJson.put("message", "系统没有获取到正确的参数");
				}
			}
			BbsUser bbsUser = bbsUserDAO.findBbsUser(user_id);
			if(bbsUser != null){
				phoneno = bbsUser.getTel();
				
		        String pid = "";
		        String email = "";
		        String name = "";
		        String gender = "";
		        String qq = "";
		        Date birthday = null;
		        String score = "";
		        String picture = "";
		        
	        	if(StringUtil.isNotEmpty(bbsUser.getPid())){
	        		pid = bbsUser.getPid();
	        	}
	        	if(StringUtil.isNotEmpty(bbsUser.getEmail())){
	        		email = bbsUser.getEmail();
	        	}
	        	if(StringUtil.isNotEmpty(bbsUser.getName())){
	        		name = bbsUser.getName();
	        	}
	        	if(StringUtil.isNotEmpty(bbsUser.getGender())){
	        		gender = bbsUser.getGender();
	        	}
	        	if(StringUtil.isNotEmpty(bbsUser.getQq())){
	        		qq = bbsUser.getQq();
	        	}
	        	if(bbsUser.getBirthDay()!=null){
	        		birthday = bbsUser.getBirthDay();
	        	}
	        	score = bbsUser.getScore()+"";
	        	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";
	        	if(StringUtil.isNotEmpty(bbsUser.getPicUrl())){
	        		picture = basePath+bbsUser.getPicUrl();
	        	}else{
	        		picture = basePath+"image/user_default.png";
	        	}
	        	
 				UserInf userInf = new UserInf();
 				userInf.setUser_id(user_id);
				userInf.setUser_phone(phoneno);
				userInf.setUser_name(name);
				userInf.setUser_sex(gender);
				userInf.setUser_email(email);
				userInf.setUser_birthday(birthday==null?"":DateUtil.formateDateToString2(birthday));
				userInf.setUser_qq(qq);
				userInf.setUser_pid(pid);
				userInf.setUser_score(score);
				userInf.setUser_picture(picture);
				
				
				sendJson.put("result", "1");
				sendJson.put("message", "获取成功");
				sendJson.put("userInf", userInf);
			}else{
				sendJson.put("result", "3");
				sendJson.put("message", "没有此用户");
			}
		}else{
			sendJson.put("result", "2");
			sendJson.put("message", "系统没有获取到正确的参数");
		}
		return sendJson;
	}

	// 获取轮播图
	public Map<String,Object> getCarousel(HttpServletRequest request) {
		String businessId = CookieUtils.getBusinessId(request);
		Map<String,Object> sendJson = new HashMap<>();
		List<Carousel> list = new ArrayList<>();
		List<PictureModel> ys = pictureDAO.findAllPictureModelByBusinessId(businessId);
		BusinessUser businessUser = businessUserDAO.findBussinessUser(businessId);
		for (PictureModel pm : ys) {
			Carousel carousel = new Carousel();
			carousel.setCarousel_instruction(pm.getDescription());
			carousel.setCarousel_page_url(pm.getLink());
			carousel.setCarousel_url("http://"+businessUser.getDomainName()+":"+businessUser.getPort()+"/" + pm.getUrl());
			list.add(carousel);
		}
		sendJson.put("result", "1");
		sendJson.put("carousel_list", list);
		return sendJson;
	}

	public Map<String,Object> getCourseDetail(HttpServletRequest request) throws IOException {
		Map<String,Object> sendJson = new HashMap<>();
		sendJson.put("result", "2");
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		if(myJsonObject.has("token") && myJsonObject.has("course_id")){
			String token=myJsonObject.getString("token");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				sendJson.put("result", "3");
				sendJson.put("message", "该token已失效");
				return sendJson;
			}
			BbsUser bbsUser = bbsUserDAO.findBbsUser(id);
			if(bbsUser!=null){
				String course_id=myJsonObject.getString("course_id");
				//获取课程对象
				LessonType lt = lessonTypeDAO.findLessonType(course_id);
				// 更新访问课程时间
				LessonTypeLog ltl = this.lessonTypeLogDAO.findLessonTypeLogByTypeAndUsr(course_id, bbsUser.getId());
				if (ltl != null) {
					ltl.setUpdateTime(new Date());
					lessonTypeLogDAO.updateLessonTypeLog(ltl);
				}

				if (lt != null) {
					// 获取对该用户部门开放的考试列表
					List<Examination> el = lt.getExams();
					List<Exam> examList = examinationToExam(el, bbsUser);
					sendJson.put("examList", examList);

					// 获取对该用户部门开放的学习资料列表
					List<Lesson> ml = lessonDAO.findAllShowedLessonByType(lt.getId());	
					int zindex=1;
		    		int jindex=1;
					for(Lesson lesson:ml){
						if (lesson.getChapterType() == 1) {
							lesson.setChapterIndex("章节" + zindex++);
						}else if (lesson.getChapterType() == 2) {
							lesson.setChapterIndex("课时" + jindex++);
						}			        	
					}
					List<Material> materialList = lessonToMaterial(ml, request);						
					sendJson.put("materialList", materialList);
					// 获取对该用户部门开放的练习题对象列表
					List<ModuleExamination2> pl = new LinkedList<>();
					
					for (Lesson m : ml) {
						List<ModuleExamination2> l = m.getModuleExaminations();
						pl.addAll(l);									        	  			        	
					}
					List<Practice> practiceList = moduleExaminationToPractice(pl, bbsUser, request);					
					sendJson.put("practiceList", practiceList);
					sendJson.put("result", "1");
					sendJson.put("message", "查询成功");
				} else {
					sendJson.put("result", "4");
					sendJson.put("message", "没有此课程");
				}
			}
		} else {// 该token已失效
			sendJson.put("result", "3");
			sendJson.put("message", "该token已失效");
		}
		return sendJson;
	}

	public List<Practice> moduleExaminationToPractice(List<ModuleExamination2> pl, BbsUser user,
			HttpServletRequest request) {
		List<Practice> practices = new ArrayList<>();
		for (ModuleExamination2 m : pl) {
			Practice practice = new Practice();
			practice.setPractice_id(m.getModule().getId());
			practice.setPractice_num(getTotalNumOfModule(m.getModule()) + "");
			practice.setPractice_name(m.getModule().getName());
			String path = m.getModule().getJsonFilePath();
			if (StringUtil.isEmpty(path)) {
				JsonUtil.createJsonFileByModule(request, m.getModule());
			}
			// String practice_errorNum = "";
			// String done_num = "";
			// if(user!=null){
			// ModuleExamCase examCase =
			// moduleExamCaseDAO.findModuleExamCaseByExaminationAndUser(m.getId(),
			// user.getId());
			// if(examCase!=null){
			// practice_errorNum = examCase.getWrongNum()+"";
			// done_num = examCase.getDoneNum()+"";
			// }
			// }
			// practice.setDone_num(done_num);
			// practice.setPractice_errorNum(practice_errorNum);
			practice.setPractice_version(String.valueOf(m.getModule().getVersion()));
			practice.setPractice_url(JsonUtil.getDownloadUrl(request) + "/servlet/DownloadJsonFile?id="
					+ m.getModule().getId() + ".json");
			practice.setPractice_md5(m.getModule().getMD5());
			practices.add(practice);
		}
		return practices;
	}

	public long getTotalNumOfModule(ExamModuleModel e) {
		return e.getChoiceNum() + e.getJudgeNum() + e.getMultiChoiceNum();
	}

	public List<Material> lessonToMaterial(List<Lesson> ml, HttpServletRequest request) throws IOException {
		List<Material> materials = new ArrayList<>();
		for (Lesson m : ml) {
			if (m != null) {				
				Material material = new Material();
				material.setParentId(m.getParentId() + "");
				material.setChapter_index(m.getChapterIndex());
				material.setChapterType(m.getChapterType());
				material.setOrd(String.valueOf(m.getOrd()));
				material.setMaterial_id(m.getId());
				material.setMaterial_name(m.getName());
				material.setMaterial_score(m.getScorePaid() + "");
				material.setMaterial_leastTime(m.getLeastTime() + "");
				material.setMaterial_classNum(m.getClassNum() + "");
				material.setMaterial_version(String.valueOf(m.getVersion()));
				material.setKnowledge_point_id(getKnowledgePointIds(m));
				if (m.getFiles() != null && m.getFiles().size() > 0) {
					BbsFileModel bfm = m.getFiles().get(0);
					String ext = bfm.getFileExt();
					String type = getlTypeByBbsFileModel(ext);
					material.setMaterial_type(type);
					if (type.equals("1")) {// epub
						material.setMaterial_url("");
						material.setMaterial_download_url(
								JsonUtil.getDownloadUrl(request) + "/servlet/DownloadFile?id=" + bfm.getId());
						material.setMaterial_md5(bfm.getMd5());
						material.setMaterial_size(bfm.getFileSize());
					} else if (type.equals("2")) {// 视频
						material.setMaterial_url(
								UrlUtil.getMp4VirtualUrlByRequest(request) + bfm.getId() + bfm.getFileExt());
						material.setMaterial_download_url(
								JsonUtil.getDownloadUrl(request) + "/servlet/DownloadFile?id=" + bfm.getId());
						material.setMaterial_md5(bfm.getMd5());
						material.setMaterial_size(bfm.getFileSize());
					} else if (type.equals("3")) {// pdf
						material.setMaterial_url(UrlUtil.getPdfVirtualUrlByRequest(request) + bfm.getId() + ".pdf");
						material.setMaterial_download_url(JsonUtil.getDownloadUrl(request) + "/servlet/DownloadFile?id="
								+ bfm.getId() + bfm.getFileExt());
						material.setMaterial_md5(bfm.getMd5());
						material.setMaterial_size(bfm.getFileSize());
					}

					material.setVedio_totalTime(String.valueOf(bfm.getTotal_time()));
					if (StringUtil.isNotEmpty(bfm.getFileFullPath())) {
						material.setScreenshot(JsonUtil.getDownloadUrl(request) + bfm.getFileFullPath());
					} else {
						material.setScreenshot("");
					}
					material.setFile_id(bfm.getId());
				}
				materials.add(material);
			}
		}
		return materials;
	}

	private String getlTypeByBbsFileModel(String ext) {
		if (ext.equals(".epub")) {
			return "1";
		} else if (ext.equals(".ppt") || ext.equals(".pptx") || ext.equals(".doc") || ext.equals(".docx")
				|| ext.equals(".pdf")) {
			return "3";
		} else {
			return "2";
		}
	}

	private String getKnowledgePointIds(Lesson m) {
		List<ModuleExamination2> list = m.getModuleExaminations();
		String ids = "";
		for (ModuleExamination2 me : list) {
			ids += me.getModule().getId() + ";";
		}
		return ids;
	}

	public void removeMaterialsByGroups(List<String> dl, List<Lesson> ml) {
		List<Lesson> li = new LinkedList<>();
		li.addAll(ml);
		for (Lesson e : li) {
			String[] gids = e.getGroupStr().split(",");
			List<String> gl = new ArrayList<>(Arrays.asList(gids));
			if (ifShowByGroups(dl, gl)) {
				ml.remove(e);
			}
		}

	}

	private boolean ifShowByGroups(List<String> dl, List<String> gl) {
		boolean bool = true;
		for (String d : dl) {
			// 如果考试的开放部门包含用户的所属部门
			if (gl.contains(d)) {
				bool = false;
				break;
			}
		}
		return bool;
	}

	/**
	 * 获取对该用户部门开放的考试列表
	 * 
	 * @param dl
	 * @param el
	 */
	// private void removeExamsByGroups(List<String> dl,List<Examination> el){
	// List<Examination> copy = new LinkedList<>();
	// copy.addAll(el);
	// for(Examination e : copy){
	// String[] gids = e.getGroupStr().split(";");
	// List<String> gl = new ArrayList<>(Arrays.asList(gids));
	// if(ifShowByGroups(dl,gl)){
	// el.remove(e);
	// }
	// }
	// }

	public List<Exam> examinationToExam(List<Examination> el, BbsUser user) {
		List<Exam> examList = new ArrayList<>();
		Map<String, Map<String, String>> map = new HashMap<>();
		if (user != null) {
			List<ExamCase> ecs = this.examCaseDAO.findExamCaseByUser(user.getId());
			map = this.getHighestScoreExamMap(ecs);
		}
		for (Examination e : el) {
			Map<String, String> emap = map.get(e.getId());
			String exam_remain_num = "";
			if (emap != null) {
				exam_remain_num = emap.get("exam_remain_num");
			}
			Exam exam = new Exam();
			exam.setExam_id(e.getId());
			exam.setExam_name(e.getName());
			exam.setExam_question_num(String.valueOf(getQuestionsNum(e)));
			exam.setExam_start_time(DateUtil.formateDateToString(e.getAvailableBegain()));
			exam.setExam_end_time(DateUtil.formateDateToString(e.getAvailableEnd()));
			exam.setExam_remain_num(exam_remain_num);
			exam.setExam_total_score(getScoreOfExamination(e));
			exam.setExam_pass_score(e.getQualified() + "");
			exam.setExam_total_time(e.getTimeLen() * 60 + "");
			exam.setExam_type(ifSimulate(e));
			examList.add(exam);
		}
		return examList;
	}

	public List<Map<String, Object>> examToMap(List<Examination> el, BbsUser user) {
		List<Map<String, Object>> examList = new ArrayList<>();
		Map<String, Map<String, String>> map = new HashMap<>();
		if (user != null) {
			List<ExamCase> ecs = this.examCaseDAO.findExamCaseByUser(user.getId());
			map = this.getHighestScoreExamMap(ecs);
		}
		for (Examination e : el) {
			if (e != null) {
				Map<String, Object> exam = new HashMap<>();
				String exam_score = "", exam_state = "", exam_remain_num = e.getMaxNum() + "";
				Map<String, String> emap = map.get(e.getId());
				if (emap != null) {
					exam_score = emap.get("exam_score");
					exam_state = emap.get("exam_state");
					exam_remain_num = emap.get("exam_remain_num");
				}
				exam.put("exam_id", StringUtil.returnString(e.getId()));
				exam.put("exam_name", StringUtil.returnString(e.getName()));
				exam.put("exam_question_num", StringUtil.returnString(String.valueOf(getQuestionsNum(e))));
				exam.put("exam_start_time",
						StringUtil.returnString(DateUtil.formateDateToString(e.getAvailableBegain())));
				exam.put("exam_end_time", StringUtil.returnString(DateUtil.formateDateToString(e.getAvailableEnd())));
				exam.put("exam_total_score", StringUtil.returnString(getScoreOfExamination(e)));
				exam.put("exam_pass_score", StringUtil.returnString(e.getQualified() + ""));
				exam.put("exam_total_time", StringUtil.returnString(e.getTimeLen() * 60 + ""));
				exam.put("exam_type", StringUtil.returnString(ifSimulate(e)));
				exam.put("exam_score", StringUtil.returnString(exam_score));
				exam.put("exam_state", StringUtil.returnString(exam_state));
				exam.put("exam_remain_num", StringUtil.returnString(exam_remain_num));
				examList.add(exam);
			}
		}
		return examList;
	}

	/**
	 * 获取考试的总题数
	 * 
	 * @param e
	 * @return
	 */
	public int getQuestionsNum(Examination e) {
		int i = e.getChoiceTotal() + e.getMultiChoiceTotal() + e.getCaseTotal() + e.getEssayTotal() + e.getFileTotal()
				+ e.getFillTotal() + e.getJudgeTotal();
		return i;
	}

	/**
	 * 获取考试的总分
	 * 
	 * @param e
	 * @return
	 */
	public String getScoreOfExamination(Examination e) {
		String type = e.getPaperType();
		double score = 0;
		if (type.equals("random")) {// 简单随机试卷
			ExamPaperRandom epr = e.getRandomPaper();
			if (epr != null)
				score = epr.getTotalScore();
		} else if (type.equals("random2")) {// 随机试卷
			score = e.getRandom2Paper() == null ? 0 : e.getRandom2Paper().getTotalScore();
		} else {// 人工试卷
			score = e.getManualPaper() == null ? 0 : e.getManualPaper().getTotalScore();
		}
		return String.valueOf(score);
	}

	/**
	 * 是否能进行模拟考试 0：正式模拟考试都可以；1：只能进行正式考试;2：只能进行模拟考试
	 * 
	 * @param e
	 * @return
	 */
	public String ifSimulate(Examination e) {
		String type = e.getPaperType();
		if (type.equals("random")) {// 简单随机试卷
			ExamPaperRandom epr = e.getRandomPaper();
			return epr.getIfSimulate();
		}
		return "1";
	}

	public Map<String, Object> getCourseList(HttpServletRequest request) {
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		Map<String, Object> sendJson = new HashMap<>();
		// BbsUser user = null;
		if (myJsonObject.has("token")) {
			String token=myJsonObject.getString("token");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				sendJson.put("result", "3");
				return sendJson;
			}
			BbsUser user = bbsUserDAO.findBbsUser(id);
			if (user != null) {
				List<LessonType> courses = lessonTypeDAO.findAllLessonType(CookieUtils.getBusinessId(request));
				List<Map<String, Object>> courseList = lessonTypesToCourseList(courses, user, request);
				sendJson.put("courseList", courseList);
				sendJson.put("result", "1");
			} else {// 该token已失效
				sendJson.put("result", "3");
			}
		} else {// 没有获取到token
			sendJson.put("result", "2");
		}
		return sendJson;
	}

	private List<Map<String, Object>> lessonTypesToCourseList(List<LessonType> courses, BbsUser user,
			HttpServletRequest request) {
		List<Map<String, Object>> courseList = new LinkedList<>();
		if (courses == null || courses.size() == 0) {
			return courseList;
		}
		Map<String, LessonTypeLog> logMap = new HashMap<>();
		if (user != null) {
			List<LessonTypeLog> ltls = this.lessonTypeLogDAO.findLessonTypeLogByUsr(user.getId());
			for (LessonTypeLog ltl : ltls) {
				LessonType lt = ltl.getLessonType();
				if (lt != null) {
					logMap.put(lt.getId(), ltl);
				}
			}
		}
		for (LessonType lt : courses) {
			if (lt != null) {
				Map<String, Object> c = LessonTypeToMap(lt, logMap.get(lt.getId()), user, request);
				courseList.add(c);
			}
		}
		return courseList;
	}

	private List<Map<String, String>> lessonTypesToCourseList2(List<LessonType> courses, BbsUser user,
			HttpServletRequest request) {
		List<Map<String, String>> courseList = new LinkedList<Map<String, String>>();
		if (courses == null || courses.size() == 0) {
			return courseList;
		}
		Map<String, LessonTypeLog> logMap = new HashMap<>();
		if (user != null) {
			List<LessonTypeLog> ltls = this.lessonTypeLogDAO.findLessonTypeLogByUsr(user.getId());
			for (LessonTypeLog ltl : ltls) {
				LessonType lt = ltl.getLessonType();
				if (lt != null) {
					logMap.put(lt.getId(), ltl);
				}
			}
		}
		for (LessonType lt : courses) {
			if (lt != null) {
				Map<String, String> c = LessonTypeToMap2(lt, logMap.get(lt.getId()), user, request);
				courseList.add(c);
			}
		}
		return courseList;
	}

	public Double getFinishedClassNum(BbsUser user, LessonType lt) {
		Double num = 0d;
		if (user != null) {
			ILessonLogDAO logDAO = SpringHelper.getSpringBean("LessonLogDAO");
			List<LessonLog> logs = logDAO.findLessonLogByTypeAndUsr(user.getId(), lt.getId());
			for (LessonLog l : logs) {
				if (l.isFinished()) {
					num += l.getLesson().getClassNum();
				}
			}
		}
		return num;
	}

	public Map<String, Object> getPaper(HttpServletRequest request) {
		JSONObject myJsonObject = this.getJSONObjectByRequest(request);
		String token = null, exam_id = null;
		String exam_type = myJsonObject.getString("exam_type");
		Examination examt = null;
		Map<String, Object> paper = new HashMap<>();
		if (myJsonObject.has("token")) {
			token = myJsonObject.getString("token");
		} else {
			paper.put("result", "2");
			return paper;
		}
		if (myJsonObject.has("exam_id")) {
			exam_id = myJsonObject.getString("exam_id");
			examt = this.examinationDAO.findExamination(exam_id);
			if (examt == null) {
				paper.put("result", "4");
				return paper;
			}
		} else {
			paper.put("result", "2");
			return paper;
		}
		String id = userTokenDAO.getIdByToken(token);
		if(id==null){
			paper.put("result", "3");
			return paper;
		}
		BbsUser user = bbsUserDAO.findBbsUser(id);
		if (user != null) {
			String userId = user.getId();
			if (exam_type.equals("1") && (examt.getMaxNum() - examt.checkParticipateTimes(userId) <= 0)) {
				paper.put("result", "5");
				return paper;
			}
			if (StringUtil.isEmpty(userId)) {
				paper.put("result", "3");
				return paper;
			}
			// 试卷池中是否有试题，是否应该新生成或从实例池中取出等逻辑由试卷池负责
			ExamCase examCase = ExamPaperPool.takePaper(exam_id);// 从试卷池中取一份试卷
			examCase.setIfSimulate(exam_type.equals("0") ? true : false);
			examCase.setDisplayMode(examt.getDisplayMode());
			examCase.setUser(user);
			MyLogger.echo("ExamCase initialized.");
			if (user != null) {
				// bu.setScore(bu.getScore() -
				// examCase.getExamination().getScorePaid());
				IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
				bsl.log("考试消耗积分", (int) (-1 * examCase.getExamination().getScorePaid()));
				// this.userDAO.updateBbsUser(bu);
			}
			examCase.setIp(IpHelper.getRemoteAddr(request));
			IUserSessionStateService iussService = SpringHelper.getSpringBean("UserSessionStateService");
			iussService.loginExam(examCase, request);
			// this.currentAdapter = this.adapterMap.get(currentIndex);
			// //加载完成后直接加入缓存，未保存过的成绩stat为''
			IExamCaseCacheService cacheService = SpringHelper.getSpringBean("ExamCaseCacheService");
			examCase.setGenTime(new Date());
			cacheService.addExamCase(examCase);
			return getRandomPaper(examCase);
		} else {
			paper.put("result", "3");
			return paper;
		}
	}

	public Map<String, Object> getExistPaper(String id) {
		ExamCase examCase = examCaseDAO.findExamCase(id);
		return getPaperByExamCase(examCase);
	}

	public Map<String, Object> getRandomPaper(ExamCase examCase) {
		return getPaperByExamCase(examCase);
	}

	public Map<String, Object> getPaperByExamCase(ExamCase examCase) {
		// Examination en = examCase.getExamination();
		List<VirtualExamPart> parts = examCase.getVparts();
		loadItemIndex(examCase);
		Map<String, Object> paper = new HashMap<>();
		paper.put("paper_id", examCase.getId());
		paper.put("paper_name", examCase.getName());
		// paper.put("exam_fullScore", examCase.getTotalFullScore());
		List<Map<String, Object>> question_list = new LinkedList<>();
		paper.put("questionList", question_list);
		for (VirtualExamPart part : parts) {
			List<ExamCaseItemAdapter> adapters = part.getAdapters();
			GeneralQuestion gqu = null;
			for (ExamCaseItemAdapter adapter : adapters) {
				if (adapter.getQtype().equals("choice")) {
					ExamCaseItemChoice item = adapter.getChoiceItem();
					gqu = item.getQuestion();
				} else if (adapter.getQtype().equals("mchoice")) {
					ExamCaseItemMultiChoice item = adapter.getMultiChoiceItem();
					gqu = item.getQuestion();
				} else if (adapter.getQtype().equals("judge")) {
					ExamCaseItemJudge item = adapter.getJudgeItem();
					gqu = item.getQuestion();
				}
				if (gqu != null) {
					HashMap<String, Object> map = new HashMap<>();
					map = JsonUtil.getQuestionNameAnswer(gqu);
					Map<String, Object> qu = new HashMap<>();
					qu.put("question_id", adapter.getQuestion().getId());
					qu.put("type", map.get("type").toString());
					qu.put("question", JsonUtil.ToDBC(map.get("question").toString()));
					qu.put("answer", map.get("answer").toString());
					qu.put("analysis", adapter.getQuestion().getRightStr());
					qu.put("score", adapter.getScore());
					qu.put("knowledge_id", adapter.getQuestion().getModule().getId());
					qu.put("realoption", map.get("realoption"));
					question_list.add(qu);
				}
			}
		}
		paper.put("result", "1");
		paper.put("start_time", DateUtil.formateDateToString(new Date()));
		return paper;
	}

	/**
	 * 加载试题的编号
	 */
	public Map<Integer, ExamCaseItemAdapter> loadItemIndex(ExamCase examCase) {
		Map<Integer, ExamCaseItemAdapter> adapterMap = new HashMap<>();
		// 以下代码加载试题编号
		List<VirtualExamPart> parts = examCase.getVparts();
		int total = 0;
		for (VirtualExamPart part : parts) {
			List<ExamCaseItemAdapter> adapters = part.getAdapters();
			for (ExamCaseItemAdapter adapter : adapters) {
				if (adapter.getQtype().equals("choice")) {
					adapter.getChoiceItem().setIndex(++total);
					adapter.setOrd(total);
					adapterMap.put(total, adapter);
				} else if (adapter.getQtype().equals("mchoice")) {
					adapter.getMultiChoiceItem().setIndex(++total);
					adapter.setOrd(total);
					adapterMap.put(total, adapter);
				} else if (adapter.getQtype().equals("fill")) {
					adapter.getFillItem().setIndex(++total);
					adapter.setOrd(total);
					adapterMap.put(total, adapter);
				} else if (adapter.getQtype().equals("judge")) {
					adapter.getJudgeItem().setIndex(++total);
					adapter.setOrd(total);
					adapterMap.put(total, adapter);
				} else if (adapter.getQtype().equals("essay")) {
					adapter.getEssayItem().setIndex(++total);
					adapter.setOrd(total);
					adapterMap.put(total, adapter);
				} else if (adapter.getQtype().equals("file")) {
					adapter.getFileItem().setIndex(++total);
					adapter.setOrd(total);
					adapterMap.put(total, adapter);
				} else if (adapter.getQtype().equals("case")) {
					adapter.getCaseItem().setIndex(total + 1);// 加综合题的顺序设置其中小题的第一个，以方便恢复顺序
					// 综合题的试题编号保存在item中，adapter不能说明问题
					for (ExamCaseItemChoice ei : adapter.getCaseItem().getChoiceItems()) {
						ei.setIndex(++total);
						adapterMap.put(total, adapter);
					}
					for (ExamCaseItemMultiChoice ei : adapter.getCaseItem().getMultiChoiceItems()) {
						ei.setIndex(++total);
						adapterMap.put(total, adapter);
					}
					for (ExamCaseItemFill ei : adapter.getCaseItem().getFillItems()) {
						ei.setIndex(++total);
						adapterMap.put(total, adapter);
					}
					for (ExamCaseItemJudge ei : adapter.getCaseItem().getJudgeItems()) {
						ei.setIndex(++total);
						adapterMap.put(total, adapter);
					}
					for (ExamCaseItemEssay ei : adapter.getCaseItem().getEssayItems()) {
						ei.setIndex(++total);
						adapterMap.put(total, adapter);
					}
				}
			}
		}
		return adapterMap;
	}

	public Map<String, Object> uploadAnwser(String token, String paper_id, JSONArray questionResultList) {
		Map<String, Object> map = new HashMap<>();
		String uid = userTokenDAO.getIdByToken(token);
		if(uid==null){
			map.put("result", "3");
			return map;
		}
		BbsUser user = bbsUserDAO.findBbsUser(uid);
		if(user!=null){
			IExamCaseCacheService cacheService = SpringHelper.getSpringBean("ExamCaseCacheService");
			ExamCase ec = cacheService.findExamCase(paper_id);
			String id = user.getId();
			if (StringUtil.isEmpty(id)) {
				map.put("result", "3");
				return map;
			}
			if (ec == null) {
				map.put("result", "4");
				return map;
			}
			java.util.Iterator<Object> it = questionResultList.iterator();
			Map<String, String> jsonMap = new HashMap<>();
			while (it.hasNext()) {
				String o = it.next().toString();
				JSONObject myJsonObject = new JSONObject(o);
				if (myJsonObject.has("question_id") && myJsonObject.has("question_answer")) {
					jsonMap.put(myJsonObject.getString("question_id"), myJsonObject.getString("question_answer"));
				} else {
					map.put("result", "4");
					return map;
				}
			}
			List<ExamCaseItemChoice> cqs = ec.getChoiceItems();
			List<ExamCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
			List<ExamCaseItemJudge> jqs = ec.getJudgeItems();
			// 设置单选题用户选择答案
			for (ExamCaseItemChoice c : cqs) {
				if (c.getQuestion().getAnswer() == null) {
					continue;
				}
				List<ExamChoice> ls = c.getQuestion().getChoices();
				String useranswer = jsonMap.get(c.getQuestion().getId());
				for (ExamChoice e : ls) {
					if (e.getLabel().equals(useranswer)) {
						e.setSelected(true);
						break;
					}
				}
			}
			// 计算每道多选题是否正确
			for (ExamCaseItemMultiChoice c : mcqs) {
				if (c.getQuestion().getAnswer() == null) {
					continue;
				}
				String useranswer = jsonMap.get(c.getQuestion().getId());
				char[] cs = null;
				if (useranswer != null) {
					cs = useranswer.toString().toUpperCase().toCharArray();
				}
				List<String> lss = new LinkedList<>();
				if (cs != null) {
					for (char c1 : cs) {
						lss.add(String.valueOf(c1));
					}
				}
				c.setSelectedLabels(lss);
			}
			// 计算每道判断题是否正确
			for (ExamCaseItemJudge c : jqs) {
				String useranswer = jsonMap.get(c.getQuestion().getId());
				if (useranswer != null && !useranswer.equals("")) {
					c.setUserAnswer(useranswer.equals("Y") ? "true" : "false");
				} else {
					c.setUserAnswer("");
				}
			}
			long n = System.currentTimeMillis();
			long o = ec.getGenTime().getTime();
			long past = (long) (n - o) / 1000;
			ec.setTimePassed(past);
			ec.setSubmitTime(new Date());
			ec.setIfPub(ec.getExamination().isIfDirect());
			boolean re = handleExamCase(ec);
			if (re) {
				map.put("result", "1");
				cacheService.deleteExamCase(paper_id);
			}
		} else {
			map.put("result", "3");
		}
		return map;
	}

	public boolean handleExamCase(ExamCase ec) {
		try {
			ec.setStat("submitted");
			ec.setPlatform("phone");
			ApplicationBean appBean = JsfHelper.getBean("applicationBean");
			// 是否异步提交试卷
			if (appBean != null && appBean.getSystemConfig().getAsyncSubmit()) {
				// ExamCaseSubmitter ecs = new ExamCaseSubmitter(examCase);
				// ThreadPoolTaskExecutor exec =
				// SpringHelper.getSpringBean("taskExecutor");
				// exec.execute(ecs);
				ExamCaseController.getSubmitQueue().add(ec);
			} else {
				String paperType = ec.getExamination().getPaperType();
				IExamCaseService examCaseService = null;
				if ("random".equals(paperType)) {
					examCaseService = SpringHelper.getSpringBean("ExamCaseService");
				} else if ("random2".equals(paperType)) {
					examCaseService = SpringHelper.getSpringBean("ExamCaseRandom2Service");
				} else {
					examCaseService = SpringHelper.getSpringBean("ManualExamCaseService");
				}
				ec = examCaseService.computeExamCase(ec);
				// 若不在交卷队列中处理，此处则需要手动加入缓存
				IExamCaseCacheService cacheService = SpringHelper.getSpringBean("ExamCaseCacheService");
				cacheService.addExamCase(ec);
				// 处理好后加入存储队列
				ExamCaseSaver.saveProcessedExamCase(ec);
			}
			// try {
			// IUserSessionStateService iussService =
			// SpringHelper.getSpringBean("UserSessionStateService");
			// iussService.logoutExam();
			// } catch (Exception e) {
			// e.printStackTrace();
			// }

			// 考试后给用户加积分
			// BbsUser bu = cs.getUsr();
			// bu.setScore(bu.getScore() + examCase.getBbsScore());
			IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
			bsl.log("考试取得成绩自动积分", (int) ec.getBbsScore());
			// ThreadPoolTaskExecutor exec =
			// SpringHelper.getSpringBean("taskExecutor");
			// exec.execute(new ExamCaseRanker(ec.getExamination().getId()));
			JsonUtil.Ranking(ec.getExamination().getId());
			// this.userDAO.updateBbsUser(bu);

			// 将考试结果暂时保存在SESSION中并跳转
			// JsfHelper.getRequest().getSession().setAttribute("tempExamCase",
			// examCase.getId());
			// JsfHelper.getFlash().put("tempExamCase", ec.getId());
			return true;
		} catch (Exception e) {
			MyLogger.println("考试提交出错，请联系后台查看日志处理！");
			// //提交失败时，将尝试直接从request map中解析
			// Map reqMap=JsfHelper.getRequest().getParameterMap();
			// ExamCaseSubmitUtil.submitFromRequestMap(reqMap);
			// e.printStackTrace();
			return false;
		}
	}

	public Map<String, Object> getExam(String token) {
		Map<String, Object> map=new HashMap<>();
		String id = userTokenDAO.getIdByToken(token);
		if(id==null){
			map.put("result", "2");
			return map;
		}
		BbsUser user = bbsUserDAO.findBbsUser(id);
		if(user!=null){
			List<ExamCase> ecs = examCaseDAO.findExamCaseByUser(user.getId());
			List<Map<String, String>> li = new LinkedList<>();
			Map<String, Map<String, String>> mape = getHighestScoreExamMap(ecs);
			for (Entry<String, Map<String, String>> m : mape.entrySet()) {
				li.add(m.getValue());
			}
			map.put("result", "1");
			map.put("examScoreList", li);
		} else {
			map.put("result", "2");
		}
		return map;
	}

	/**
	 * 
	 * @param ecs
	 *            考试实例集合
	 * @return 考试exam_id对应最高分考试实例map：（对应考试id）exam_id，exam_name，（考试最高分）exam_score
	 *         ，（1已通过（超过及格分）2未通过）exam_state，（剩余考试次数）exam_remain_num）
	 */
	public Map<String, Map<String, String>> getHighestScoreExamMap(List<ExamCase> ecs) {
		Map<String, Map<String, String>> mape = new HashMap<>();
		for (ExamCase ec : ecs) {
			Map<String, String> map2 = new HashMap<>();
			String exam_id = ec.getExamination().getId();
			Double ecscore = ec.getScore();
			map2.put("exam_id", exam_id);
			map2.put("exam_name", ec.getExamination().getName());
			map2.put("exam_score", ecscore + "");
			double userscore = ec.getScore();
			map2.put("exam_state", userscore > ec.getExamination().getQualified() ? "1" : "2");
			long num = ec.getExamination().getMaxNum() - ec.getExamination().checkParticipateTimes(ec.getUserId());
			num = num > 0 ? num : 0;
			map2.put("exam_remain_num", num + "");
			if (mape.get(exam_id) == null) {
				mape.put(exam_id, map2);
			} else {
				String score = mape.get(exam_id).get("exam_score");
				if (Double.parseDouble(score) < ecscore) {
					mape.put(exam_id, map2);
				}
			}
		}
		return mape;
	}

	/**
	 * 设置用户头像
	 * @param config
	 * @param request
	 * @param response
	 * @return
	 */
	public Map<String, String> uploadUserPicture(ServletConfig config, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, String> map = new HashMap<>();
		String token = "";
		// String ext = "";
		String id = UUID.randomUUID().toString();
		BbsUser user = null;
		try {
			SmartUpload smartUpload = new SmartUpload();
			smartUpload.initialize(config, request, response);
			smartUpload.upload();
			// 获取其他的数据
			Request otherReq = smartUpload.getRequest();
			// 获取从客户端传输过来的用户名,在客户端中对参数进行参数URL编码了，所以服务器这里要进行URL解码
			token = URLDecoder.decode(otherReq.getParameter("token"), "utf-8");
			// ext= URLDecoder.decode(otherReq.getParameter("ext"),"utf-8");
			String uid = userTokenDAO.getIdByToken(token);
			if(uid==null){
				map.put("result", "2");
				return map;
			}
			user = bbsUserDAO.findBbsUser(uid);
			if (user == null) {
				map.put("result", "2");
				return map;
			}
			// 获取上传的文件，因为知道在客户端一次就上传一个文件，所以我们就直接取第一个文件
			com.jspsmart.upload.File smartFile = smartUpload.getFiles().getFile(0);
			// 判断文件是否丢失
			if (!smartFile.isMissing()) {
				// 另保存至本地
				smartFile.saveAs(FileUtil.getImageRealPathById(id), smartUpload.SAVE_PHYSICAL);
				user.setPicUrl("servlet/ShowAbstractImage?id=" + id);
				this.bbsUserDAO.updateBbsUser(user);
				map.put("result", "1");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	// 流转化成字符串
	public static String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}

	// 流转化成文件
	public static void inputStream2File(InputStream is, String savePath) throws Exception {
		System.out.println("文件保存路径为:" + savePath);
		File file = new File(savePath);
		InputStream inputSteam = is;
		BufferedInputStream fis = new BufferedInputStream(inputSteam);
		FileOutputStream fos = new FileOutputStream(file);
		int f;
		while ((f = fis.read()) != -1) {
			fos.write(f);
		}
		fos.flush();
		fos.close();
		fis.close();
		inputSteam.close();
	}

	public Map<String, Object> getExamRanklist(HttpServletRequest request) {
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		Map<String, Object> map = new HashMap<>();
		if (myJsonObject.has("exam_id")) {
			List<Map<String, String>> list = new LinkedList<>();
			String exam_id = myJsonObject.getString("exam_id");
			List<ExamCase> sortlist = JsonUtil.getRankedExamCaseList(exam_id);
			if (sortlist != null && sortlist.size() > 0) {
				for (int i = 0; i < sortlist.size(); i++) {
					ExamCase e = sortlist.get(i);
					Map<String, String> childmap = new HashMap<>();
					childmap.put("ranking", i + 1 + "");
					childmap.put("name", e.getUser().getName());
					childmap.put("score", e.getScore() + "");
					childmap.put("exam_time", e.getTimePassed() + "");
					childmap.put("finish_time", DateUtil.formateDateToString(e.getSubmitTime()));
					list.add(childmap);
				}
				map.put("ranklist", list);
				map.put("result", "1");
			} else {
				map.put("result", "2");
			}
		} else {
			map.put("result", "3");
		}
		return map;
	}

	public Map<String, Object> getExamLog(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		if(myJsonObject.has("token")){
			String token=myJsonObject.getString("token");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				map.put("result", "3");
				return map;
			}
			BbsUser user = bbsUserDAO.findBbsUser(id);
			String exam_id = myJsonObject.getString("exam_id");
			if (StringUtil.isNotEmpty(exam_id) && user != null) {
				List<ExamCase> es = examCaseDAO.findExamCaseByExaminationAndUser(exam_id, user.getId());
				List<Map<String, String>> list = new LinkedList<>();
				if (es != null && es.size() > 0) {
					for (ExamCase e : es) {
						Map<String, String> childmap = new HashMap<>();
						childmap.put("exam_type", e.isIfSimulate() ? "0" : "1");// true：模拟考试；false:正式考试
						childmap.put("exam_score", e.getScore() + "");
						childmap.put("exam_time", e.getTimePassed() + "");
						childmap.put("paper_id", e.getId());
						childmap.put("exam_starttime", DateUtil.formateDateToString(e.getGenTime()));
						list.add(childmap);
					}
					map.put("examLoglist", list);
					map.put("result", "1");
				} else {
					map.put("result", "2");
				}
			} else {
				map.put("result", "3");
			}
		} else {// 没有获取到token
			map.put("result", "2");
		}
		return map;
	}

	/*
	 * 同步接口
	 * */
	public Map<String, Object> syncUP(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		String businessId = CookieUtils.getBusinessId(request);
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		try {
			if (!myJsonObject.has("token")) {
				map.put("result", "2");
				return map;
			}
			String token=myJsonObject.getString("token");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				map.put("result", "2");
				return map;
			}
			
			BbsUser user = bbsUserDAO.findBbsUser(id);
			if(user==null){
				map.put("result", "2");
				return map;
			}
			// 同步学习资料
			List<LessonLog> llogs = lessonLogDAO.findLessonLogByUsr(user.getId());
			Map<String, LessonLog> llogMap = new HashMap<>();
			for (LessonLog llog : llogs) {
				Lesson le = llog.getLesson();
				if (le != null) {
					llogMap.put(le.getId(), llog);
				}
			}
			List<String> lessonIds = new LinkedList<>();
			Map<String, Map<String, Object>> lessonMap = new HashMap<>();
			if (myJsonObject.has("material_List")) {
				JSONArray materialList = myJsonObject.getJSONArray("material_List");
				Map<String, Object> paraMap = new HashMap<>();
				for (Object material : materialList) {
					String ms = material.toString();
					JSONObject questionJson = new JSONObject(ms);
					String recordTime = "", material_id = "", update_time = "";
					double studyProgress = 0;
					if (questionJson.has("recordTime")) {// 已学习累加时长
						recordTime = questionJson.getString("recordTime");
					}
					if (questionJson.has("studyProgress")) {// 电子书记录百分比，视频记录播放时间到多少秒
						String p = questionJson.getString("studyProgress");
						studyProgress = Double.parseDouble(p);
					}
					if (questionJson.has("material_id")) {// 学习资料id
						material_id = questionJson.getString("material_id");
					}
					if (questionJson.has("update_time")) {// 时间戳
						update_time = questionJson.getString("update_time");
					}
					lessonIds.add(material_id);
					paraMap.put("recordTime", recordTime);
					paraMap.put("studyProgress", studyProgress);
					paraMap.put("update_time", update_time);
					lessonMap.put(material_id, paraMap);
				}
			}
			List<Lesson> lessons = lessonDAO.findLessonByIdList(lessonIds);
			for (Lesson lesson : lessons) {
				if (lesson != null) {					
					syncUpLesson(lesson, llogMap.get(lesson.getId()), user, lessonMap.get(lesson.getId()),businessId);
				}
			}
			// 同步练习题
			String module_id = "";
			List<String> mods = new LinkedList<>();
			Map<String, Map<String, String>> jsonMap = new HashMap<>();
			// 将上传的练习题数据放进map中，方便后面根据题目id查找相关数据
			if (myJsonObject.has("question_List")) {
				JSONArray questionList = myJsonObject.getJSONArray("question_List");
				for (Object question : questionList) {
					String qs = question.toString();
					JSONObject questionJson = new JSONObject(qs);
					String update_time = "", question_id = "", collectionState = "", selectedIndex = "";
					if (questionJson.has("update_time")) {// 时间戳
						update_time = questionJson.getString("update_time");
					}
					if (questionJson.has("question_id")) {// 练习题id
						question_id = questionJson.getString("question_id");
					}
					if (questionJson.has("collectionState")) {// 题目收藏状态
						collectionState = questionJson.getString("collectionState");
					}
					if (questionJson.has("selectedIndex")) {// 选中的答案
						selectedIndex = questionJson.getString("selectedIndex");
					}
					if (questionJson.has("module_id")) {// 所属知识点id
						module_id = questionJson.getString("module_id");
					}
					// 上传题目信息
					Map<String, String> m = new HashMap<>();
					m.put("collectionState", collectionState);
					m.put("selectedIndex", selectedIndex);
					m.put("update_time", update_time);
					jsonMap.put(question_id, m);
					if (!mods.contains(module_id)) {
						mods.add(module_id);
					}
				}
			}
			// 拼装练习实例
			List<ExamModuleModel> emms = examModule2DAO.findExamModuleByIdList(mods);
			List<ModuleExamCase> mecs = mexamCaseDAO.findModuleExamCase2ByUser(user.getId());
			Map<String, ModuleExamCase> mecMap = new HashMap<>();
			for (ModuleExamCase mec : mecs) {
				ExamModuleModel emm = mec.getExamModule();
				if (emm != null) {
					mecMap.put(emm.getId(), mec);
				}
			}
			for (ExamModuleModel emm : emms) {
				if (emm != null) {
					syncUpExamModule(emm, mecMap.get(emm.getId()), user, jsonMap);
				}
			}
			map.put("result", "1");
		} catch (Exception e) {
			MyLogger.error(e);
			e.printStackTrace();
			map.put("result", "2");
		}
		return map;
	}

	private void syncUpExamModule(ExamModuleModel emm, ModuleExamCase ec, BbsUser user,
			Map<String, Map<String, String>> jsonMap) {
		// 如果用户章节练习的记录查不出结果，说明用户还未做过此章节练习，新建一条该章节练习的用户记录
		if (ec == null) {
			ec = new ModuleExamCase();
			ec.setExamModule(emm);
			ec.setExamination(emm.getExam());
			ec.setUser(user);
			examCaseService.buildExamCase(ec);
		} else {
			examCaseService.resumeExamCase(ec);
		}
		List<ModuleExamCaseItemChoice> cqs = ec.getChoiceItems();
		List<ModuleExamCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
		List<ModuleExamCaseItemJudge> jqs = ec.getJudgeItems();
		// 设置单选题用户选择答案
		for (ModuleExamCaseItemChoice c : cqs) {
			// 用户该章节练习记录该题没填
			if (c.getQuestion().getAnswer() == null) {
				continue;
			}
			// 该单选题的选项
			List<ExamChoice> ls = c.getQuestion().getChoices();
			// 用户选择的答案
			Map<String, String> useranswer = jsonMap.get(c.getQuestion().getId());
			if (useranswer != null) {
				if (useranswer.get("collectionState").equals("1")) {
					QuestionCollection qc = new QuestionCollection();
					qc.setBbsUser(user);
					qc.setQid(c.getQuestion().getId());
					qc.setQtype(c.getQtype());
					selectDAO.addQuestionCollection(qc);
				}
				c.setUpdateTime(new Date(Long.parseLong(useranswer.get("update_time"))));
				if (useranswer.get("selectedIndex") != null && !useranswer.get("selectedIndex").equals("")) {
					for (ExamChoice e : ls) {
						if (e.getLabel().equals(useranswer.get("selectedIndex"))) {
							e.setSelected(true);
							break;
						}
					}
				} else {
					c.setUserAnswer("");
				}
			}
		}

		// 计算每道多选题是否正确
		for (ModuleExamCaseItemMultiChoice c : mcqs) {
			if (c.getQuestion().getAnswer() == null) {
				continue;
			}
			// 上传题目信息
			Map<String, String> useranswer = jsonMap.get(c.getQuestion().getId());
			if (useranswer != null) {
				// 收藏题目
				if (useranswer.get("collectionState").equals("1")) {
					QuestionCollection qc = new QuestionCollection();
					qc.setBbsUser(user);
					qc.setQid(c.getQuestion().getId());
					qc.setQtype(c.getQtype());
					selectDAO.addQuestionCollection(qc);
				}
				c.setUpdateTime(new Date(Long.parseLong(useranswer.get("update_time"))));
				char[] cs = null;
				// 选中的答案
				if (useranswer.get("selectedIndex") != null && !useranswer.get("selectedIndex").equals("")) {
					cs = useranswer.get("selectedIndex").toString().toUpperCase().toCharArray();
				} else {
					c.setUserAnswer("");
				}
				List<String> lss = new LinkedList<>();
				// char数组转换String list
				if (cs != null) {
					for (char c1 : cs) {
						lss.add(String.valueOf(c1));
					}
				}
				c.setSelectedLabels(lss);
			}
		}
		// 计算每道判断题是否正确
		for (ModuleExamCaseItemJudge c : jqs) {
			Map<String, String> useranswer = jsonMap.get(c.getQuestion().getId());
			if (useranswer != null) {
				if (useranswer.get("collectionState").equals("1")) {
					QuestionCollection qc = new QuestionCollection();
					qc.setBbsUser(user);
					qc.setQid(c.getQuestion().getId());
					qc.setQtype(c.getQtype());
					selectDAO.addQuestionCollection(qc);
				}
				c.setUpdateTime(new Date(Long.parseLong(useranswer.get("update_time"))));
				if (useranswer.get("selectedIndex") != null && !useranswer.get("selectedIndex").equals("")) {
					c.setUserAnswer(useranswer.equals("Y") ? "true" : "false");
				} else {
					c.setUserAnswer("");
				}
			}
		}
		long n = System.currentTimeMillis();
		long o = ec.getGenTime().getTime();
		long past = (long) (n - o) / 1000;
		ec.setTimePassed(past);
		ec.setSubmitTime(new Date());

		ModuleExamCaseSaver ecs = new ModuleExamCaseSaver(ec);
		ThreadPoolTaskExecutor exec = SpringHelper.getSpringBean("taskExecutor");
		exec.execute(ecs);
	}

	private void syncUpLesson(Lesson lesson, LessonLog llog, BbsUser user, Map<String, Object> map, String businessId) {
		double studyProgress = 0;
		String recordTime = "", update_time = "";
		studyProgress = (double) map.get("studyProgress");
		recordTime = (String) map.get("recordTime");
		if (StringUtil.isEmpty(recordTime)) {
			recordTime = "0";
		}
		update_time = (String) map.get("update_time");
		long updatetime = new Date().getTime();
		if (StringUtil.isEmpty(update_time)) {
			update_time = new Date().getTime() + "";
			try {
				updatetime = Long.parseLong(update_time);
			} catch (Exception e) {
				updatetime = new Date().getTime();
			}
		}
		if (lesson != null) {
			// 如果用户学习资料的学习记录查不出结果，说明用户还未学习过此学习资料，新建一条该学习资料的学习记录
			if (llog == null) {
				llog = new LessonLog();
				llog.setUser(user);
				llog.setLesson(lesson);
			}
			if (llog != null) {
				// 记录此学习资料的视频播放进度
				if (llog.getLesson() != null && llog.getLesson().getFiles() != null
						&& llog.getLesson().getFiles().size() > 0) {
					BbsFileModel bf = llog.getLesson().getFiles().get(0);
					if (bf.getFileExt().equals(".mp4")) {
						llog.setVideoTime(studyProgress * bf.getTotal_time());
					} else {
						llog.setVideoTime(studyProgress);
					}
				}
				// 记录此学习资料的学习总时长
				llog.setTimeFinished(llog.getTimeFinished() + Double.parseDouble(recordTime));
				// 如果学习总时长大于此学习资料所需最少学习时长，则标记该学习资料已经完成
//				if ((llog.getTimeFinished() > lesson.getLeastTime() * 60
//						|| (llog.getTimeFinished() > 1 && llog.getLesson().getTotalTime() <= 60))
//						&& !llog.isFinished()) {
//					if (!llog.isFinished()) {
//						llog.setFinished(true);
//						if (user != null && lesson != null) {
//							System.out.println(user.getName() + "完成学习资料" + lesson.getName());
//						}
//					}
//				}
				if ((llog.getTimeFinished() > lesson.getLeastTime() * 60
						|| (llog.getTimeFinished() > llog.getLesson().getTotalTime() * 60 && llog.getLesson().getTotalTime()!=0))) {
					if (!llog.isFinished()) {
						llog.setFinished(true);
						if (user != null && lesson != null) {
							System.out.println(user.getName() + "完成学习资料" + lesson.getName());
						}
					}
				}
				// 设置学习记录的修改时间点
				llog.setGenTime(new Date(updatetime));
				llog.setBusinessId(businessId);
				lessonLogDAO.updateLessonLog(llog);
			}
		}
	}

	/*
	 * 同步下拉接口
	 * */
	public Map<String, Object> syncDown(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			JSONObject myJsonObject = getJSONObjectByRequest(request);
			map.put("result", "2");
			String token = "";
			String update_time = "";
			if (myJsonObject.has("token") && myJsonObject.has("update_time")) {
				token = myJsonObject.getString("token");
				update_time = myJsonObject.getString("update_time");
				if (update_time.equals("")) {
					update_time = "0";
				}
			} else {
				map.put("result", "2");
				return map;
			}
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				map.put("result", "3");
				return map;
			}
			
			BbsUser user = bbsUserDAO.findBbsUser(id);
			if(user==null){
				map.put("result", "3");
				return map;
			}
			// 获取资料学习记录
			List<LessonLog> logs = lessonLogDAO.findLessonLogByUsr(user.getId());
			List<Map<String, String>> material_List = new LinkedList<>();
			for (LessonLog log : logs) {
				if (log != null) {
					Lesson l = log.getLesson();
					if (l != null) {
						long genTime = log.getGenTime().getTime();
						long updateTime = Long.parseLong(update_time);
						if (genTime > updateTime) {
							Map<String, String> logmap = new HashMap<>();
							logmap.put("material_id", l.getId());
							logmap.put("iffinished", log.isFinished() ? "1" : "0");
							logmap.put("update_time", genTime + "");
							logmap.put("totalTime", log.getTimeFinished() + "");
							if (l.getFiles() != null && l.getFiles().size() > 0) {
								BbsFileModel bf = l.getFiles().get(0);
								if (bf != null) {
									if (bf.getFileExt().equals(".mp4")) {
										logmap.put("studyProgress", (log.getVideoTime() / bf.getTotal_time()) + "");
									} else {
										logmap.put("studyProgress", log.getVideoTime() + "");
									}
								}
							} else {
								logmap.put("studyProgress", "");
							}
							material_List.add(logmap);
						}
					}
				}
			}
			map.put("material_List", material_List);
			// 获取练习记录
			List<ModuleExamCase> mecs = mexamCaseDAO.findModuleExamCase2ByUser(user.getId());
			List<QuestionCollection> qsc = selectDAO.findQuestionCollectionByUser(user.getId());
			List<String> collectqs = new LinkedList<>();
			for (QuestionCollection qs : qsc) {
				collectqs.add(qs.getQid());
			}
			List<Map<String, String>> question_List = new LinkedList<>();
			for (ModuleExamCase ec : mecs) {
				List<ModuleExamCaseItemChoice> cqs = ec.getChoiceItems();
				List<ModuleExamCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
				List<ModuleExamCaseItemJudge> jqs = ec.getJudgeItems();
				for (ModuleExamCaseItemChoice cq : cqs) {
					if (cq.getUpdateTime() != null && cq.getUpdateTime().getTime() > Long.parseLong(update_time)) {
						String module_id = cq.getQuestion().getModule().getId();
						String qid = cq.getQuestion().getId();
						Map<String, String> qmap = new HashMap<>();
						qmap.put("module_id", module_id);
						qmap.put("question_id", qid);
						if (collectqs.contains(qid)) {
							qmap.put("collectionState", "1");
						} else {
							qmap.put("collectionState", "0");
						}
						qmap.put("selectedIndex", cq.getUserAnswer() == null ? "" : cq.getUserAnswer());
						if (cq.getUserAnswer() == null || cq.getUserAnswer().equals("")) {
							qmap.put("answerState", "0");
						} else {
							qmap.put("answerState", cq.getIfRight() ? "1" : "2");
						}
						question_List.add(qmap);
					}
				}
				for (ModuleExamCaseItemMultiChoice cq : mcqs) {
					if (cq.getUpdateTime() != null && cq.getUpdateTime().getTime() > Long.parseLong(update_time)) {
						String module_id = cq.getQuestion().getModule().getId();
						String qid = cq.getQuestion().getId();
						Map<String, String> qmap = new HashMap<>();
						qmap.put("module_id", module_id);
						qmap.put("question_id", qid);
						if (collectqs.contains(qid)) {
							qmap.put("collectionState", "1");
						} else {
							qmap.put("collectionState", "0");
						}
						qmap.put("selectedIndex", cq.getUserAnswer() == null ? "" : cq.getUserAnswer());
						if (cq.getUserAnswer() == null || cq.getUserAnswer().equals("")) {
							qmap.put("answerState", "0");
						} else {
							qmap.put("answerState", cq.getIfRight() ? "1" : "2");
						}
						question_List.add(qmap);
					}
				}
				for (ModuleExamCaseItemJudge cq : jqs) {
					if (cq.getUpdateTime() != null && cq.getUpdateTime().getTime() > Long.parseLong(update_time)) {
						String module_id = cq.getQuestion().getModule().getId();
						String qid = cq.getQuestion().getId();
						Map<String, String> qmap = new HashMap<>();
						qmap.put("module_id", module_id);
						qmap.put("question_id", qid);
						if (collectqs.contains(qid)) {
							qmap.put("collectionState", "1");
						} else {
							qmap.put("collectionState", "0");
						}
						if (cq.getUserAnswer() != null && !cq.getUserAnswer().equals("null")) {
							if (cq.getUserAnswer().equals("false")) {
								qmap.put("selectedIndex", "N");
							} else if (cq.getUserAnswer().equals("true")) {
								qmap.put("selectedIndex", "Y");
							} else {
								qmap.put("selectedIndex", "");
							}
						} else {
							qmap.put("selectedIndex", "");
						}
						if (cq.getUserAnswer() == null || cq.getUserAnswer().equals("")
								|| cq.getUserAnswer().equals("null")) {
							qmap.put("answerState", "0");
						} else {
							qmap.put("answerState", cq.getIfRight() ? "1" : "2");
						}
						question_List.add(qmap);
					}
				}
			}
			map.put("question_List", question_List);
			map.put("result", "1");
			map.put("update_time", new Date().getTime());
		} catch (Exception e) {
			MyLogger.error(e);
			e.printStackTrace();
			map.put("result", "2");
		}
		return map;
	}

	/**
	 * 获取课程类型
	 * @param request
	 * @return
	 */
	public Map<String, Object> getCourseTags(HttpServletRequest request) {
		String businessId = CookieUtils.getBusinessId(request);
		Map<String, Object> map = new HashMap<>();
		List<Map<String, String>> list = new LinkedList<>();
		List<CourseType> ftags = courseTypeDAO.findAllCourseTypeWithoutRoot(businessId);
		for (CourseType ft : ftags) {
			Map<String, String> fmap = CourseTypeToMap(ft, request,businessId);
			list.add(fmap);
		}
		map.put("tag_list", list);
		map.put("result", "1");
		return map;
	}

	private Map<String, String> CourseTypeToMap(CourseType ft, HttpServletRequest request,String businessId) {
		Map<String, String> fmap = new HashMap<>();
		fmap.put("tag_id", ft.getId());
		fmap.put("tag_name", ft.getName());
		fmap.put("picture",
				StringUtil.isEmpty(ft.getPicture()) ? "" : JsonUtil.getDownloadUrl(request) + "/" + ft.getPicture());
		String fid = "";
		if (ft.getFatherId() != null) {
			fid = ft.getFatherId();
		}
		fmap.put("father_id", fid);
		fmap.put("ord", ft.getOrd() + "");
		List<CourseType> cts = courseTypeDAO.findAllSonCourseType(ft.getId(),businessId);
		if (cts != null && cts.size() > 0 && cts.get(0) != null) {
			fmap.put("hassons", "1");
		} else {
			fmap.put("hassons", "0");
		}
		return fmap;
	}

	public Map<String, Object> getCourseListByTagId(HttpServletRequest request) {
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		String businessId = CookieUtils.getBusinessId(request);
		Map<String, Object> courseListView = new HashMap<>();
		if (myJsonObject.has("tagid")) {
			String tagid = myJsonObject.getString("tagid");
			List<LessonType> courses = lessonTypeDAO.findAllLessonTypeByTagId(tagid, businessId);
			List<Map<String, Object>> courseList = this.lessonTypesToCourseList(courses, null, request);
			courseListView.put("courseList", courseList);
			courseListView.put("result", "1");
		} else {
			courseListView.put("result", "2");
		}
		return courseListView;
	}

	public Map<String, Object> getCourseListByName(HttpServletRequest request) {
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		String businessId = CookieUtils.getBusinessId(request);
		Map<String, Object> courseListView = new HashMap<>();
		if (myJsonObject.has("course_name")) {
			String course_name = myJsonObject.getString("course_name");
			List<LessonType> courses = lessonTypeDAO.findLessonTypesByName(course_name, businessId);
			List<Map<String, Object>> courseList = this.lessonTypesToCourseList(courses, null, request);
			courseListView.put("courseList", courseList);
			courseListView.put("result", "1");
		} else {
			courseListView.put("result", "2");
		}
		return courseListView;
	}

	public Map<String, Object> getCourseById(HttpServletRequest request) {
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		Map<String, Object> result = new HashMap<>();
		if (myJsonObject.has("course_id")) {
			String course_id = myJsonObject.getString("course_id");
			LessonType course = lessonTypeDAO.findLessonType(course_id);
			result = this.LessonTypeToMap(course, null, null, request);
			result.put("result", "1");
		} else {
			result.put("result", "2");
		}
		return result;
	}

	/*
	 *  按照课程同步接口
	 */
	public Map<String, Object> syncDownByCourse(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		map.put("result", "2");
		String token = "";
		String update_time = "";
		String course_id = "";
		if (myJsonObject.has("token") && myJsonObject.has("update_time")) {
			token = myJsonObject.getString("token");
			update_time = myJsonObject.getString("update_time");
			course_id = myJsonObject.getString("course_id");
			if (update_time.equals("")) {
				update_time = "0";
			}
		} else {
			map.put("result", "2");
			return map;
		}
		String id = userTokenDAO.getIdByToken(token);
		if(id==null){
			map.put("result", "3");
			return map;
		}
		
		BbsUser user = bbsUserDAO.findBbsUser(id);
		if(user==null){
			map.put("result", "3");
			return map;
		}
		// 获取资料学习记录
		List<LessonLog> logs = lessonLogDAO.findLessonLogByTypeAndUsr(user.getId(), course_id);
		List<Map<String, String>> material_List = new LinkedList<>();
		for (LessonLog log : logs) {
			if (log.getGenTime().getTime() > Long.parseLong(update_time)) {
				Map<String, String> logmap = new HashMap<>();
				logmap.put("material_id", log.getLesson().getId());
				logmap.put("iffinished", log.isFinished() ? "1" : "0");
				logmap.put("update_time", log.getGenTime().getTime() + "");
				logmap.put("totalTime", log.getTimeFinished() + "");
				if (log.getLesson() != null && log.getLesson().getFiles() != null
						&& log.getLesson().getFiles().size() > 0) {
					BbsFileModel bf = log.getLesson().getFiles().get(0);
					if (bf.getFileExt().equals(".mp4")) {
						logmap.put("studyProgress", (log.getVideoTime() / bf.getTotal_time()) + "");
					} else {
						logmap.put("studyProgress", log.getVideoTime() + "");
					}
				} else {
					logmap.put("studyProgress", "");
				}
				material_List.add(logmap);
			}
		}
		map.put("material_List", material_List);

		List<Lesson> lessons = this.lessonDAO.findAllLessonByType(course_id);
		List<ModuleExamCase> mecs = new LinkedList<>();
		for (Lesson l : lessons) {
			String eid = "";
			if (l.getModuleExaminations() != null && l.getModuleExaminations().size() > 0) {
				eid = l.getModuleExaminations().get(0).getId();
			} else {
				continue;
			}
			ModuleExamCase mec = this.mexamCaseDAO.findModuleExamCaseByExaminationAndUser(eid, user.getId());
			mecs.add(mec);
		}
		// 获取练习记录
		List<QuestionCollection> qsc = selectDAO.findQuestionCollectionByUser(user.getId());
		List<String> collectqs = new LinkedList<>();
		for (QuestionCollection qs : qsc) {
			collectqs.add(qs.getQid());
		}
		List<Map<String, String>> question_List = new LinkedList<>();
		for (ModuleExamCase ec : mecs) {
			if (ec == null) {
				continue;
			}
			List<ModuleExamCaseItemChoice> cqs = ec.getChoiceItems();
			List<ModuleExamCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
			List<ModuleExamCaseItemJudge> jqs = ec.getJudgeItems();
			for (ModuleExamCaseItemChoice cq : cqs) {
				if (cq.getUpdateTime() != null && cq.getUpdateTime().getTime() > Long.parseLong(update_time)) {
					String module_id = cq.getQuestion().getModule().getId();
					String qid = cq.getQuestion().getId();
					Map<String, String> qmap = new HashMap<>();
					qmap.put("module_id", module_id);
					qmap.put("question_id", qid);
					if (collectqs.contains(qid)) {
						qmap.put("collectionState", "1");
					} else {
						qmap.put("collectionState", "0");
					}
					qmap.put("selectedIndex", cq.getUserAnswer() == null ? "" : cq.getUserAnswer());
					if (cq.getUserAnswer() == null || cq.getUserAnswer().equals("")) {
						qmap.put("answerState", "0");
					} else {
						qmap.put("answerState", cq.getIfRight() ? "1" : "2");
					}
					question_List.add(qmap);
				}
			}
			for (ModuleExamCaseItemMultiChoice cq : mcqs) {
				if (cq.getUpdateTime() != null && cq.getUpdateTime().getTime() > Long.parseLong(update_time)) {
					String module_id = cq.getQuestion().getModule().getId();
					String qid = cq.getQuestion().getId();
					Map<String, String> qmap = new HashMap<>();
					qmap.put("module_id", module_id);
					qmap.put("question_id", qid);
					if (collectqs.contains(qid)) {
						qmap.put("collectionState", "1");
					} else {
						qmap.put("collectionState", "0");
					}
					qmap.put("selectedIndex", cq.getUserAnswer() == null ? "" : cq.getUserAnswer());
					if (cq.getUserAnswer() == null || cq.getUserAnswer().equals("")) {
						qmap.put("answerState", "0");
					} else {
						qmap.put("answerState", cq.getIfRight() ? "1" : "2");
					}
					question_List.add(qmap);
				}
			}
			for (ModuleExamCaseItemJudge cq : jqs) {
				if (cq.getUpdateTime() != null && cq.getUpdateTime().getTime() > Long.parseLong(update_time)) {
					String module_id = cq.getQuestion().getModule().getId();
					String qid = cq.getQuestion().getId();
					Map<String, String> qmap = new HashMap<>();
					qmap.put("module_id", module_id);
					qmap.put("question_id", qid);
					if (collectqs.contains(qid)) {
						qmap.put("collectionState", "1");
					} else {
						qmap.put("collectionState", "0");
					}
					if (cq.getUserAnswer() != null && !cq.getUserAnswer().equals("null")) {
						if (cq.getUserAnswer().equals("false")) {
							qmap.put("selectedIndex", "N");
						} else if (cq.getUserAnswer().equals("true")) {
							qmap.put("selectedIndex", "Y");
						} else {
							qmap.put("selectedIndex", "");
						}
					} else {
						qmap.put("selectedIndex", "");
					}
					if (cq.getUserAnswer() == null || cq.getUserAnswer().equals("")
							|| cq.getUserAnswer().equals("null")) {
						qmap.put("answerState", "0");
					} else {
						qmap.put("answerState", cq.getIfRight() ? "1" : "2");
					}
					question_List.add(qmap);
				}
			}
		}
		map.put("question_List", question_List);
		map.put("result", "1");
		map.put("update_time", new Date().getTime());
		return map;
	}

	public Map<String, Object> buyCourse(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		String token = "";
		String course_id = "";
		map.put("result", "3");
		if (myJsonObject.has("token") && myJsonObject.has("course_id")) {
			token = myJsonObject.getString("token");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				return map;
			}
			
			BbsUser user = bbsUserDAO.findBbsUser(id);
			if(user!=null){
				course_id = myJsonObject.getString("course_id");
				String lts = user.getLessonTypeStr();
				if (StringUtil.isNotEmpty(lts) && lts.contains(course_id)) {// 已经购买过该课程
					map.put("result", "4");
					map.put("score", user.getScore() + "");
				} else {
					LessonType lt = this.lessonTypeDAO.findLessonType(course_id);
					IBuyCourseService buyCourseService = SpringHelper.getSpringBean("BuyCourseService");
					if (buyCourseService.buyCourse2(lt, user)) {
						map.put("result", "1");
						map.put("score", user.getScore() + "");
					} else {
						map.put("result", "2");
					}
				}
			}
		}
		return map;
	}

	public Map<String, Object> getUserCourseList(HttpServletRequest request) {
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		Map<String, Object> courseListView = new HashMap<>();
		BbsUser user = null;
		if (myJsonObject.has("token")) {
			String token = myJsonObject.getString("token");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				courseListView.put("result", "4");
				return courseListView;
			}
			
			user = bbsUserDAO.findBbsUser(id);
			if(user!=null){
				List<Map<String, Object>> courseList = new LinkedList<>();
				if (StringUtil.isNotEmpty(user.getLessonTypeStr())) {
					List<LessonType> courses = lessonTypeDAO
							.findLessonTypeByIdList(StringUtil.idsToIdList(user.getLessonTypeStr()));
					courseList = lessonTypesToCourseList(courses, user, request);
				}
				courseListView.put("courseList", courseList);
				courseListView.put("total_num", courseList.size());
				courseListView.put("result", "1");
			} else {// 无此用户
				courseListView.put("result", "4");
			}
		} else {// 没有获取到token
			courseListView.put("result", "2");
		}
		return courseListView;
	}

	public Map<String, Object> getUserSecretCourseList(HttpServletRequest request) {
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		Map<String, Object> courseListView = new HashMap<>();
		BbsUser user = null;
		if (myJsonObject.has("token")) {
			String token = myJsonObject.getString("token");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				courseListView.put("result", "4");
				return courseListView;
			}
			
			user = bbsUserDAO.findBbsUser(id);
			if(user!=null){
				List<Map<String, Object>> courseList = new LinkedList<>();
				if (StringUtil.isNotEmpty(user.getLessonTypeStr())) {
					List<LessonType> courses = lessonTypeDAO
							.findSecretLessonTypeByIdList(StringUtil.idsToIdList(user.getLessonTypeStr()));
					courseList = lessonTypesToCourseList(courses, user, request);
				}
				courseListView.put("courseList", courseList);
				courseListView.put("total_num", courseList.size());
				courseListView.put("result", "1");
			} else {// 无此用户
				courseListView.put("result", "4");
			}
		} else {// 没有获取到token
			courseListView.put("result", "2");
		}
		return courseListView;
	}
	
	/**
	 * 获取教师课程列表
	 */
	public Map<String, Object> getTeacherCourseList(HttpServletRequest request) {
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		Map<String, Object> courseListView = new HashMap<>();
		BbsUser user = null;
		if (myJsonObject.has("token")) {
			String token = myJsonObject.getString("token");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				courseListView.put("result", "3");
				return courseListView;
			}
			
			user = bbsUserDAO.findBbsUser(id);
			if(user!=null){
				Teacher t=user.getTeacher();
				if(t!=null){
					List<Map<String, String>> courseList =  new LinkedList<>();
					List<LessonType> courses = t.getLessonTypes();
					if (courses.size() > 0) {
						courseList = lessonTypesToCourseList2(courses, user, request);
					}
					courseListView.put("courseList", courseList);
					courseListView.put("result", "1");
				} else {// 无此讲师
					courseListView.put("result", "4");
				}
			} else {// 无此用户
				courseListView.put("result", "3");
			}
		} else {// 没有获取到token
			courseListView.put("result", "2");
		}
		return courseListView;
	}

	/**
	 * 获取教师信息
	 */
	public Map<String, Object> getTeacherInfo(HttpServletRequest request) {
		String businessId = CookieUtils.getBusinessId(request);
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		Map<String, Object> teacherInfo = new HashMap<>();
		BbsUser user = null;
		if (myJsonObject.has("token")) {
			String token = myJsonObject.getString("token");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				teacherInfo.put("result", "3");
				return teacherInfo;
			}
			
			user = bbsUserDAO.findBbsUser(id);
			if(user!=null){
				Teacher t = null;
				if (myJsonObject.has("id")) {
					String teacher_id = myJsonObject.getString("id");
					t = this.TeacherDAO.findTeacher(teacher_id);
				} else {
					t = user.getTeacher();
				}
				if (t != null) {
					teacherInfo.put("result", "1");
					// 能否审核
					teacherInfo.put("can_approved", t.isCanApproved() ? "1" : "0");
					// 课程是否需要被审核
					teacherInfo.put("need_approved", t.isNeedApproved() ? "1" : "0");
					// 讲师类型
					teacherInfo.put("teacher_type", t.getTeacherType() + "");
					teacherInfo.put("gender", t.getGender() + "");
					teacherInfo.put("teacher_id", StringUtil.returnString(t.getId()));
					String pic = StringUtil.isEmpty(t.getPicture()) ? ""
							: JsonUtil.getDownloadUrl(request) + "/" + t.getPicture();
					teacherInfo.put("picture", pic);
					if (myJsonObject.has("type") && myJsonObject.getInt("type") == 1) {
						teacherInfo.put("qq", StringUtil.returnString(t.getQq()));
						teacherInfo.put("email", StringUtil.returnString(t.getEmail()));
						teacherInfo.put("introduction", StringUtil.returnString(t.getIntroduction()));
						teacherInfo.put("teacher_name", StringUtil.returnString(t.getName()));
						teacherInfo.put("company", StringUtil.returnString(t.getCompany()));
						teacherInfo.put("workYears", StringUtil.returnString(t.getWorkYears()));
						teacherInfo.put("idNum", StringUtil.returnString(t.getIdNum()));

						String frontPict = StringUtil.isEmpty(t.getFrontPicture()) ? ""
								: JsonUtil.getDownloadUrl(request) + "/" + t.getFrontPicture();
						teacherInfo.put("frontPicture", frontPict);
						String backPic = StringUtil.isEmpty(t.getBackPicture()) ? ""
								: JsonUtil.getDownloadUrl(request) + "/" + t.getBackPicture();
						teacherInfo.put("backPicture", backPic);

						List<JobTitle> jts = t.getJobTitles();
						List<Map<String, String>> jtList = new LinkedList<>();
						jobTitleToMap(jtList, jts, request);
						teacherInfo.put("jobTitleList", jtList);

						List<WorkExperience> wes = t.getWorkExperience();
						List<Map<String, String>> weList = new LinkedList<>();
						workExperienceToMap(weList, wes);
						teacherInfo.put("workExperienceList", weList);

						List<ProjectExperience> pes = t.getProjectExperience();
						List<Map<String, String>> peList = new LinkedList<>();
						projectExperienceToMap(peList, pes);
						teacherInfo.put("projectExperienceList", peList);

						List<Certificate> cfs = t.getCertificate();
						List<Map<String, String>> cfList = new LinkedList<>();
						certificateToMap(cfList, cfs, request);
						teacherInfo.put("certificateList", cfList);
					}
					List<Map<String,Object>> typelist=new ArrayList<>();
					CourseType ctype = courseTypeDAO.findCourseType(businessId);
					List<CourseType> ks = courseTypeDAO.loadAllDescendants(ctype.getId(),businessId);
					for(CourseType ct:ks){
						Map<String,Object> type=new HashMap<>();
						type.put("id", ct.getId());
						type.put("name",ct.getName());
						//新增分类父节点id字符串
						type.put("ancestors",ct.getAncestors());
						typelist.add(type);
					}
					
/*					String courseTypeStr=t.getCourseTypeStr();
					if(StringUtil.isNotEmpty(courseTypeStr)){
						List<String> idList =  StringUtil.idsToIdList(courseTypeStr);
						List<CourseType> ctlist = this.courseTypeDAO.findSomeCourseType(idList);
						for(CourseType ct:ctlist){
							Map<String,Object> type=new HashMap<>();
							type.put("id", ct.getId());
							type.put("name",ct.getName());
							//新增分类父节点id字符串
							type.put("ancestors",ct.getAncestors());
							typelist.add(type);
						}
					}*/
					teacherInfo.put("typelist", typelist);
				} else {// 无此讲师
					teacherInfo.put("result", "4");
				}
			} else {// 无此用户
				teacherInfo.put("result", "3");
			}
		} else {// 没有获取到token
			teacherInfo.put("result", "2");
		}
		return teacherInfo;
	}

	private List<Map<String, String>> jobTitleToMap(List<Map<String, String>> jtList, List<JobTitle> jts,
			HttpServletRequest request) {
		for (JobTitle jt : jts) {
			if (jt != null) {
				Map<String, String> map = new HashMap<>();
				map.put("id", jt.getId());
				map.put("JTname", StringUtil.returnString(jt.getName()));
				map.put("gain_time",
						StringUtil.returnString(jt.getGainTime() == null ? "" : jt.getGainTime().getTime() + ""));
				map.put("title_level", StringUtil.returnString(jt.getTitleLevel() + ""));
				String pic = StringUtil.isEmpty(jt.getPicture()) ? ""
						: JsonUtil.getDownloadUrl(request) + "/" + jt.getPicture();
				map.put("picture", pic);
				jtList.add(map);
			}
		}
		return jtList;
	}

	private List<Map<String, String>> workExperienceToMap(List<Map<String, String>> jtList, List<WorkExperience> jts) {
		for (WorkExperience jt : jts) {
			if (jt != null) {
				Map<String, String> map = new HashMap<>();
				map.put("id", jt.getId());
				map.put("WEname", StringUtil.returnString(jt.getName()));
				map.put("begin_time",
						StringUtil.returnString(jt.getBeginTime() == null ? "" : jt.getBeginTime().getTime() + ""));
				map.put("end_time",
						StringUtil.returnString(jt.getEndTime() == null ? "" : jt.getEndTime().getTime() + ""));
				map.put("duty", StringUtil.returnString(jt.getDuty() + ""));
				jtList.add(map);
			}
		}
		return jtList;
	}

	private List<Map<String, String>> projectExperienceToMap(List<Map<String, String>> jtList,
			List<ProjectExperience> jts) {
		for (ProjectExperience jt : jts) {
			if (jt != null) {
				Map<String, String> map = new HashMap<>();
				map.put("id", jt.getId());
				map.put("PEname", StringUtil.returnString(jt.getName()));
				map.put("begin_time",
						StringUtil.returnString(jt.getBeginTime() == null ? "" : jt.getBeginTime().getTime() + ""));
				map.put("end_time",
						StringUtil.returnString(jt.getEndTime() == null ? "" : jt.getEndTime().getTime() + ""));
				jtList.add(map);
			}
		}
		return jtList;
	}

	private List<Map<String, String>> certificateToMap(List<Map<String, String>> jtList, List<Certificate> jts,
			HttpServletRequest request) {
		for (Certificate jt : jts) {
			if (jt != null) {
				Map<String, String> map = new HashMap<>();
				map.put("id", jt.getId());
				map.put("certificate_name", StringUtil.returnString(jt.getName()));
				String pic = StringUtil.isEmpty(jt.getPicture()) ? ""
						: JsonUtil.getDownloadUrl(request) + "/" + jt.getPicture();
				map.put("certificate_picture", pic);
				jtList.add(map);
			}
		}
		return jtList;
	}

	/**
	 * 教师批量上架课程
	 */
	public Map<String, Object> turnOnCourse(HttpServletRequest request) {
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		Map<String, Object> result = new HashMap<>();
		String token = "";
		String course_ids = "";
		LessonType lt = null;
		BbsUser user = null;
		if (myJsonObject.has("course_ids") && myJsonObject.has("token")) {
			token = myJsonObject.getString("token");
			course_ids = myJsonObject.getString("course_ids");
			String uid = userTokenDAO.getIdByToken(token);
			if(uid==null){
				result.put("result", "3");
				return result;
			}
			
			user = bbsUserDAO.findBbsUser(uid);
			if(user!=null){
				Teacher t=user.getTeacher();
				if(t!=null){
					String ids[]=course_ids.split(";");
			    	for(String id:ids){
			    		lt=this.lessonTypeDAO.findLessonType(id);
			    		if(lt!=null){
							int approved=lt.getApproved();//0审核中 1已审核通过 2未审核
							if(t.isNeedApproved()){//需要被审核
								if(approved==0){
									lt.setEnabled(true);
								} else if (approved == 1) {
									lt.setEnabled(true);
								} else if (approved == 2) {
									lt.setApproved(0);
									lt.setEnabled(true);
								}
								this.lessonTypeDAO.updateLessonType(lt);
							} else {// 不需要被审核
								lt.setEnabled(true);
								this.lessonTypeDAO.updateLessonType(lt);
							}
						}
					}
					result.put("result", "1");
				} else {
					// 无此讲师
					result.put("result", "4");
				}
			} else {
				// 无此用户
				result.put("result", "3");
			}
		} else {
			// 没有获取到token或course_ids
			result.put("result", "2");
		}
		return result;
	}

	/**
	 * 教师下架课程
	 */
	public Map<String, Object> turnOffCourse(HttpServletRequest request) {
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		Map<String, Object> result = new HashMap<>();
		String token = "";
		String course_ids = "";
		LessonType lt = null;
		BbsUser user = null;
		if (myJsonObject.has("course_ids") && myJsonObject.has("token")) {
			token = myJsonObject.getString("token");
			course_ids = myJsonObject.getString("course_ids");
			String uid = userTokenDAO.getIdByToken(token);
			if(uid==null){
				result.put("result", "3");
				return result;
			}
			
			user = bbsUserDAO.findBbsUser(uid);
			if(user!=null){
				Teacher t=user.getTeacher();
				if(t!=null){
					String ids[]=course_ids.split(";");
			    	for(String id:ids){
			    		lt=this.lessonTypeDAO.findLessonType(id);
						if(lt!=null){
							lt.setEnabled(false);
							this.lessonTypeDAO.updateLessonType(lt);
						}
					}
					result.put("result", "1");
				} else {
					// 无此讲师
					result.put("result", "4");
				}
			} else {
				// 无此用户
				result.put("result", "3");
			}
		} else {
			// 没有获取到token或course_id
			result.put("result", "2");
		}
		return result;
	}

	/**
	 * 教师添加课程
	 */
	public Map<String, Object> addTeacherCourse(HttpServletRequest request) {
		String businessId = CookieUtils.getBusinessId(request);
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		JSONObject json = this.getJSONObjectByRequest(request);
		Map<String, Object> result = new HashMap<>();
		BbsUser user = null;
		if (myJsonObject.has("token")) {
			String token = myJsonObject.getString("token");
			String uid = userTokenDAO.getIdByToken(token);
			if(uid==null){
				result.put("result", "3");
				return result;
			}
			
			user = bbsUserDAO.findBbsUser(uid);
			if(user!=null){
				Teacher t=user.getTeacher();
				if(t!=null){
					LessonType lt=new LessonType();
					lt.setTeacher(t);
					String number = t.getLessonTypes().size() + 1 + "";
					lt.setName(t.getName() + "自建课程" + number);
					String id = lt.getId();
					boolean needApproved = t.isNeedApproved();
					if (needApproved) { // 1需要审核
						// 课程未审核
						lt.setApproved(2);
						lt.setEnabled(false);
					} else {// 0不需要审核
						lt.setApproved(1);
						lt.setEnabled(false);
					}
					List<CourseType> courseTypeList = this.courseTypeDAO.findAllSonCourseType(businessId,businessId);
					List<CourseType> courseTypeList2 = this.courseTypeDAO.findAllSonCourseType(courseTypeList.get(0).getId(),businessId);
					lt.setCourseTypeStr(courseTypeList2.get(0).getId());
					lt.setCourseTypeCN(courseTypeList2.get(0).getName());
					lt.setBusinessId(businessId);
					this.lessonTypeDAO.addLessonType(lt);
					result.put("result", "1");
					result.put("course", id);
				} else {
					// 无此讲师
					result.put("result", "4");
				}
			} else {
				// 没有此用户
				result.put("result", "3");
			}
		} else {
			// 没有获取到token
			result.put("result", "2");
		}

		return result;
	}

	/**
	 * 教师上传课程图片
	 */
	@SuppressWarnings("static-access")
	public Map<String, String> uploadTeacherCoursePicture(ServletConfig config, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, String> map = new HashMap<>();
		String token = "";
		String course_id = "";
		String ext = "";
		BbsUser user = null;
		String id = UUID.randomUUID().toString();
		LessonType lt=null;
        try {    
            SmartUpload smartUpload = new SmartUpload();
            smartUpload.initialize(config, request, response); 
            smartUpload.upload();
            //获取其他的数据  
            Request otherReq = smartUpload.getRequest();  
            if(otherReq==null){
            	map.put("result", "2");
            	return map;
            }
            if(StringUtil.isEmpty(otherReq.getParameter("ext")) || StringUtil.isEmpty(otherReq.getParameter("token")) || StringUtil.isEmpty(otherReq.getParameter("course_id"))) {
            	map.put("result", "2");
            	return map;
            }
            //获取从客户端传输过来的用户名,在客户端中对参数进行参数URL编码了，所以服务器这里要进行URL解码 
            ext= URLDecoder.decode(otherReq.getParameter("ext"),"utf-8");  
            course_id= URLDecoder.decode(otherReq.getParameter("course_id"),"utf-8"); 
            token= URLDecoder.decode(otherReq.getParameter("token"),"utf-8"); 
			if(StringUtil.isNotEmpty(token) && StringUtil.isNotEmpty(course_id) && StringUtil.isNotEmpty(ext)){
				String uid = userTokenDAO.getIdByToken(token);
				if(uid==null){
					map.put("result","3");
					return map;
				}
				
				user = bbsUserDAO.findBbsUser(uid);
    			if(user!=null){
    				Teacher t=user.getTeacher();
    				if(t!=null){
    					List<LessonType> ltlist=t.getLessonTypes();
    					lt=this.lessonTypeDAO.findLessonType(course_id);
    					if(lt!=null){
    						if(ltlist.contains(lt)){
    							 //获取上传的文件，因为知道在客户端一次就上传一个文件，所以我们就直接取第一个文件  
    				            com.jspsmart.upload.File smartFile = smartUpload.getFiles().getFile(0);    
    				            //判断文件是否丢失  
    				            if (!smartFile.isMissing()) {    
    				                //另保存至本地
    				                smartFile.saveAs(FileUtil.getImageRealPathById(id), smartUpload.SAVE_PHYSICAL); 
    				                lt.setPicture("servlet/ShowAbstractImage?id="+id);
    				                this.lessonTypeDAO.updateLessonType(lt);
    				                map.put("result", "1");
    				            }  
    						}else{
    							//此讲师无此课程的修改权限
    							map.put("result","6");
    						}
    					}else{
    						//没有获取到课程
    						map.put("result", "5");
    					}
    				}else{
    					//无此讲师
    					map.put("result","4");
    				}
				}else{
    				//无此用户
    				map.put("result","3");
    			}
            }else{
            	//没有获取到token或course_id
    			map.put("result", "2");
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }    
		return map;
	}
	
	/**
	 * 课程所有章查询
	 */
	public Map<String,Object> getCourseChapters(HttpServletRequest request){
		Map<String,Object> sendJson = new HashMap<>();
		sendJson.put("result", "1");
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		String courseId = myJsonObject.getString("course_id");		
		if(StringUtils.isEmpty(courseId)){
			sendJson.put("result", "2");
			sendJson.put("lessons", null);
			return sendJson;
		}
		List<Lesson> lessonList = lessonDAO.findChapterLessonss(CookieUtils.getBusinessId(JsfHelper.getRequest()), courseId, 1);
		List<LessonVO> lessons = new ArrayList<LessonVO>();
		for(Lesson lesson:lessonList){
			LessonVO lessonVO = new LessonVO();
			BeanUtils.copyProperties(lesson, lessonVO);
			lessons.add(lessonVO);
		}
		sendJson.put("lessons", lessons);
		return sendJson;
	}

	/**
	 * 教师上传学习资料业务
	 */
	@SuppressWarnings("static-access")
	public Map<String, String> uploadTeacherLesson(ServletConfig config, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, String> map = new HashMap<>();
		String course_id = "";
		String material_id = "";
		String file_id = "";
		String token = "";
		String ext = "";
		String lsop = "";
		String fileop = "";
		BbsUser user = null;
		LessonType lt = null;
		Lesson ls = null;
		BbsFileModel bfm = null;
		String id = UUID.randomUUID().toString();

        try {    
            SmartUpload smartUpload = new SmartUpload();
            smartUpload.initialize(config, request, response); 
            smartUpload.upload();
            //获取其他的数据  
            Request otherReq = smartUpload.getRequest();  
            //获取从客户端传输过来的用户名,在客户端中对参数进行参数URL编码了，所以服务器这里要进行URL解码 
            if(otherReq==null){
            	map.put("result", "2");
            	return map;
            }
            if(StringUtil.isEmpty(otherReq.getParameter("ext")) || StringUtil.isEmpty(otherReq.getParameter("token")) || StringUtil.isEmpty(otherReq.getParameter("course_id"))) {
            	map.put("result", "2");
            	return map;
            }
            ext= URLDecoder.decode(otherReq.getParameter("ext"),"utf-8"); 
            token= URLDecoder.decode(otherReq.getParameter("token"),"utf-8"); 
            course_id= URLDecoder.decode(otherReq.getParameter("course_id"),"utf-8"); 

            if(StringUtil.isNotEmpty(token) && StringUtil.isNotEmpty(course_id) && StringUtil.isNotEmpty(ext)){
            	String uid = userTokenDAO.getIdByToken(token);
            	if(uid==null){
            		map.put("result","3");
            		return map;
            	}
            	
    			user = bbsUserDAO.findBbsUser(uid);
    			if(user!=null){
    				Teacher t=user.getTeacher();
    				if(t!=null){
    					List<LessonType> ltlist=t.getLessonTypes();
    					lt=this.lessonTypeDAO.findLessonType(course_id);
    					if(lt!=null){
    						if(ltlist.contains(lt)){
    							 //获取上传的文件，因为知道在客户端一次就上传一个文件，所以我们就直接取第一个文件  
    				            com.jspsmart.upload.File smartFile = smartUpload.getFiles().getFile(0);    
    				            //判断文件是否丢失  
    				            if (!smartFile.isMissing()) {
    				                //另保存至本地
    				                smartFile.saveAs(FileUtil.getMp4FilePath(id), smartUpload.SAVE_PHYSICAL); 
    				               
    				                if(StringUtil.isNotEmpty(otherReq.getParameter("material_id"))){//修改学习资料
    				                	material_id= URLDecoder.decode(otherReq.getParameter("material_id"),"utf-8"); 
    				                	ls = lessonDAO.findLesson(material_id);
    				                	
    				                	if(ls!=null){
    				                		if(ls.getLessonType().getId().equals(lt.getId())){
    				                			lsop = "update";
    				                			if(StringUtil.isNotEmpty(file_id)){
        				                			bfm = bbsFileDAO.findClientFile2(file_id);
        				                			if(bfm==null){
        				                				//没有此学习资料附件
        	    				                		map.put("result","10");
        	    				                		return map;
        				                			}
        				                			fileop="update";
        				                		}else{
        				                			bfm = new BbsFileModel();
        				                			fileop="add";
        				                		}
        									}else{
        										//无此学习资料的权限
        										map.put("result","9");
        									}
    				                	}else{
    				                		//没有此学习资料
    				                		map.put("result","8");
    				                		return map;
    				                	}    				                	
    				                }else{//新建学习资料
    				                	ls = new Lesson();
    				                	lsop = "add";
    				                	ls.setName(lt.getTeacher().getName()+"自建学习资料"+lt.getLessonNum()+1+"");
    				                	if(StringUtil.isNotEmpty(otherReq.getParameter("file_id"))){
    				                		file_id= URLDecoder.decode(otherReq.getParameter("file_id"),"utf-8"); 
				                			bfm = bbsFileDAO.findClientFile2(file_id);
				                			if(bfm==null){
				                				//没有此学习资料附件
	    				                		map.put("result","10");
	    				                		return map;
				                			}
				                			fileop="update";
				                		}else{
				                			bfm = new BbsFileModel();
				                			fileop="add";
				                		}
    				                }
    				                
				                	bfm.setRealLength(smartFile.getSize());
				                	bfm.setFileSize(getFileSize(smartFile));
				                	String n = smartFile.getFileName();
				                    int l1 = n.lastIndexOf("\\");
				                    int l2 = n.lastIndexOf(".");
				                    String ext1 = "." + n.substring(l2 + 1).toLowerCase();
				                    String name = n.substring(l1 + 1, l2);
				                    bfm.setFileName(name);
				                    bfm.setFileExt(ext1);
				                    bfm.setUploadTime(new Date());
				                    bfm.setId(id);
				                	bfm.setLesson(ls);
				                	bfm.setUser(user);
				                	
				                	String spath = bfm.getId()+bfm.getFileExt();
				            		ls.setSourceUrl(spath);
				            		
				            		bfm.setIfFolder(false);
				            		bfm.setScope("pers");
				            		bfm.setTotal_time(JsonUtil.getVedioTotalTime(bfm,getClass().getResource("/").getFile().toString()));
				                	getImageOfVideo(bfm);
				                	bfm.setFileFullPath("/servlet/ShowImage?id="+bfm.getId());
				            		
				                	File mf= new File(FileUtil.getFileRealPath(bfm));
				                	if(mf.exists()){
				        				FileInputStream fis = new FileInputStream(FileUtil.getFileRealPath(bfm));
				        				String md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis));
				        				IOUtils.closeQuietly(fis);
				        				bfm.setMd5(md5);
				        			}
				                	
				                	ls.setLessonType(lt);
				                	String businessId = CookieUtils.getBusinessId(request);
				                	ls.setBusinessId(businessId);
				                	if(lsop.equals("add")){
				                		ls.setChannel(1);//视频渠道 0 web;1 移动端
				                		lessonDAO.addLesson(ls);
				                	}else if(lsop.equals("update")){
				                		lessonDAO.updateLesson(ls);
				                	}
				                	
				                	
				                	bfm.setLesson(ls);
				                	if(fileop.equals("add")){
				                		bfm.setUploadTime(new Date());
				                        project2DAO.saveClientFileInfo(bfm);
				                        bfm.setAncestors(this.cxLogic.genAncestors(id));//生成祖先文件列表
				                        project2DAO.updateClientFileInfo(bfm);
				                	}else if(fileop.equals("update")){
				                		project2DAO.updateClientFileInfo(bfm);
				                	}
				            		map.put("material_id", ls.getId());
				            		map.put("file_id", bfm.getId());
				            		map.put("material_size", bfm.getFileSize());
				            		map.put("vedio_totalTime", bfm.getTotal_time()+"");
    				                map.put("result", "1");
    				            }  else{
    				            	//没有接收到需上传的附件
        							map.put("result","7");
    				            }
    						}else{
    							//此讲师无此课程的修改权限
    							map.put("result","6");
    						}
    					}else{
    						//没有此课程
    						map.put("result","5");
    					}
    				}else{
    					//无此讲师
    					map.put("result","4");
    				}
    			}else{
    				//无此用户
    				map.put("result","3");
    			}
            }else{
            	//没有获取到token或course_id
    			map.put("result", "2");
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }    
		return map;
	}

	/**
	 * 删除讲师自建课程
	 */
	public Map<String, Object> deleteTeacherCourse(HttpServletRequest request) {
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		Map<String, Object> result = new HashMap<>();
		BbsUser user = null;
		if (myJsonObject.has("token") && myJsonObject.has("course_id")) {
			String token = myJsonObject.getString("token");
			String course_id = myJsonObject.getString("course_id");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				result.put("result", "3");
				return result;
			}
			user = bbsUserDAO.findBbsUser(id);
			if(user!=null){
				Teacher t=user.getTeacher();
				if(t!=null){
					List<String> ids=StringUtil.idsToIdList(course_id);
					this.lessonTypeDAO.deleteLessonTypes(ids);
					this.lessonDAO.deleteLessonsByLessonTypeId(ids);
					this.project2DAO.delClientFilesByLessonTypeId(ids);
					result.put("result", "1");
				} else {
					// 无此讲师
					result.put("result", "4");
				}
			} else {
				// 无此用户
				result.put("result", "3");
			}
		} else {
			// 没有获取到token或material_id或course_id
			result.put("result", "2");
		}
		return result;
	}

	/**
	 * 删除学习资料
	 */
	public Map<String, Object> deleteTeacherLesson(HttpServletRequest request) {
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		Map<String, Object> result = new HashMap<>();
		BbsUser user = null;
		LessonType lt = null;
		Lesson ls = null;
		if (myJsonObject.has("token") && myJsonObject.has("material_id") && myJsonObject.has("course_id")) {
			String token = myJsonObject.getString("token");
			String material_id = myJsonObject.getString("material_id");
			String course_id = myJsonObject.getString("course_id");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				result.put("result", "3");
				return result;
			}
			
			user = bbsUserDAO.findBbsUser(id);
			if(user!=null){
				Teacher t=user.getTeacher();
				if(t!=null){
					List<LessonType> ltlist=t.getLessonTypes();
					lt=this.lessonTypeDAO.findLessonType(course_id);
					if(lt!=null){
						if(ltlist.contains(lt)){
							ls = lessonDAO.findLesson(material_id);
							if (ls != null) {
								if (ls.getLessonType().getId().equals(lt.getId())) {
									lessonDAO.deleteLesson(material_id);
									result.put("result", "1");
								} else {
									// 无此学习资料的权限
									result.put("result", "8");
								}
							} else {
								// 没有此学习资料
								result.put("result", "7");
							}
						} else {
							// 此讲师无此课程的修改权限
							result.put("result", "6");
						}
					} else {
						// 没有获取到课程
						result.put("result", "5");
					}
				} else {
					// 无此讲师
					result.put("result", "4");
				}
			} else {
				// 无此用户
				result.put("result", "3");
			}
		} else {
			// 没有获取到token或material_id或course_id
			result.put("result", "2");
		}
		return result;
	}
	
	/**
	 * 保存学习资料3
	 */
	public Map<String, Object> saveTeacherLessonNew(HttpServletRequest request) {
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		Map<String, Object> result = new HashMap<>();
		BbsUser user = null;
		LessonType lt = null;
		Lesson ls = null;
		String token = "";
		String course_id = "";
		String material_id = "";
		String material_name = "";
		String class_num = "";
		String least_time = "";
		String ord = "";
		String chapter_id=null;
		if (myJsonObject.has("token") && myJsonObject.has("course_id")) {
			token = myJsonObject.getString("token");
			course_id = myJsonObject.getString("course_id");
			material_id = myJsonObject.getString("material_id");
			material_name = myJsonObject.getString("material_name");
			chapter_id = myJsonObject.getString("chapter_id");
			// 课时
			class_num = myJsonObject.getString("class_num");
			// 完成课时所需时长
			least_time = myJsonObject.getString("least_time");
			
			//排序
			if(myJsonObject.has("ord")){
				ord = myJsonObject.getString("ord");
			}
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				result.put("result", "3");
				return result;
			}
			
			user = bbsUserDAO.findBbsUser(id);
			if(user!=null){
				Teacher t=user.getTeacher();
				if(t!=null){
					List<LessonType> ltlist=t.getLessonTypes();
					lt=this.lessonTypeDAO.findLessonType(course_id);
					if(lt!=null){
						if(ltlist.contains(lt)){
							ls = lessonDAO.findLesson(material_id);
							if (ls != null) {
								if (ls.getLessonType().getId().equals(lt.getId())) {
									if (StringUtil.isNotEmpty(material_id)) {
										ls.setName(material_name);
									}
									if (StringUtil.isNotEmpty(class_num)) {
										ls.setClassNum(Double.parseDouble(class_num));
									}
									if (StringUtil.isNotEmpty(least_time)) {
										ls.setLeastTime(Double.parseDouble(least_time));
									}
									if (StringUtil.isNotEmpty(ord)) {
										ls.setOrd(Integer.parseInt(ord));
									}
									if(StringUtil.isNotEmpty(chapter_id)){
										ls.setParentId(chapter_id);
										ls.setChapterType(2);
									}else {
										ls.setParentId(null);
										ls.setChapterType(1);
									}
									String businessId = CookieUtils.getBusinessId(request);
									ls.setBusinessId(businessId);
									lessonDAO.updateLesson(ls);
									result.put("result", "1");
								} else {
									// 无此学习资料的权限
									result.put("result", "8");
								}
							} else {
								// 无资料的章
								ls = new Lesson();
								ls.setId(UUID.randomUUID().toString());
								if (StringUtil.isNotEmpty(class_num)) {
									ls.setClassNum(Double.parseDouble(class_num));
								}
								if (StringUtil.isNotEmpty(least_time)) {
									ls.setLeastTime(Double.parseDouble(least_time));
								}
								if (StringUtil.isNotEmpty(ord)) {
									ls.setOrd(Integer.parseInt(ord));
								}
								if (StringUtil.isNotEmpty(material_name)) {
									ls.setName(material_name);
								}
								if (StringUtil.isNotEmpty(course_id)) {
									
								}
								if(StringUtil.isNotEmpty(chapter_id)){	
									//非节
									result.put("result", 7);
								}else {
									ls.setParentId(null);
									ls.setChapterType(1);
								}
								ls.setChannel(1);
								ls.setLessonType(lt);
								String businessId = CookieUtils.getBusinessId(request);
								ls.setBusinessId(businessId);
								lessonDAO.addLesson(ls);
								result.put("result", "1");
							}
						} else {
							// 此讲师无此课程的修改权限
							result.put("result", "6");
						}
					} else {
						// 没有获取到课程
						result.put("result", "5");
					}
				} else {
					// 无此讲师
					result.put("result", "4");
				}
			} else {
				// 无此用户
				result.put("result", "3");
			}
		} else {
			// 没有获取到token或material_id或course_id
			result.put("result", "2");
		}
		return result;
	}

	/**
	 * 保存学习资料
	 */
	public Map<String, Object> saveTeacherLesson(HttpServletRequest request) {
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		Map<String, Object> result = new HashMap<>();
		BbsUser user = null;
		LessonType lt = null;
		Lesson ls = null;
		String token = "";
		String course_id = "";
		String material_id = "";
		String material_name = "";
		String class_num = "";
		String least_time = "";
		String ord = "";
		if (myJsonObject.has("token") && myJsonObject.has("material_id") && myJsonObject.has("course_id")) {
			token = myJsonObject.getString("token");
			course_id = myJsonObject.getString("course_id");
			material_id = myJsonObject.getString("material_id");
			material_name = myJsonObject.getString("material_name");
			// 课时
			class_num = myJsonObject.getString("class_num");
			// 完成课时所需时长
			least_time = myJsonObject.getString("least_time");
			//排序
			if(myJsonObject.has("ord")){
				ord = myJsonObject.getString("ord");
			}
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				result.put("result", "3");
				return result;
			}
			
			user = bbsUserDAO.findBbsUser(id);
			if(user!=null){
				Teacher t=user.getTeacher();
				if(t!=null){
					List<LessonType> ltlist=t.getLessonTypes();
					lt=this.lessonTypeDAO.findLessonType(course_id);
					if(lt!=null){
						if(ltlist.contains(lt)){
							ls = lessonDAO.findLesson(material_id);
							if (ls != null) {
								if (ls.getLessonType().getId().equals(lt.getId())) {
									if (StringUtil.isNotEmpty(material_id)) {
										ls.setName(material_name);
									}
									if (StringUtil.isNotEmpty(class_num)) {
										ls.setClassNum(Double.parseDouble(class_num));
									}
									if (StringUtil.isNotEmpty(least_time)) {
										ls.setLeastTime(Double.parseDouble(least_time));
									}
									if (StringUtil.isNotEmpty(ord)) {
										ls.setOrd(Integer.parseInt(ord));
									}
									String businessId = CookieUtils.getBusinessId(request);
									ls.setBusinessId(businessId);
									lessonDAO.updateLesson(ls);
									result.put("result", "1");
								} else {
									// 无此学习资料的权限
									result.put("result", "8");
								}
							} else {
								// 没有此学习资料
								result.put("result", "7");
							}
						} else {
							// 此讲师无此课程的修改权限
							result.put("result", "6");
						}
					} else {
						// 没有获取到课程
						result.put("result", "5");
					}
				} else {
					// 无此讲师
					result.put("result", "4");
				}
			} else {
				// 无此用户
				result.put("result", "3");
			}
		} else {
			// 没有获取到token或material_id或course_id
			result.put("result", "2");
		}
		return result;
	}

	/**
	 * 保存学习资料
	 */
	public Map<String, Object> saveTeacherLessonOrd(HttpServletRequest request) {
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		Map<String, Object> result = new HashMap<>();
		BbsUser user = null;
		LessonType lt = null;
		Lesson ls = null;
		String token = "";
		String course_id = "";
		JSONArray materialist = null;
//		if (myJsonObject.has("token") && myJsonObject.has("course_id") && myJsonObject.has("materialist")) {
		if (myJsonObject.has("token") && myJsonObject.has("materialist")) {
			token = myJsonObject.getString("token");
//			course_id = myJsonObject.getString("course_id");
			materialist = myJsonObject.getJSONArray("materialist");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				result.put("result", "3");
				return result;
			}
			
			user = bbsUserDAO.findBbsUser(id);
			if(user!=null){
				Teacher t=user.getTeacher();
				if(t!=null){
//					lt=this.lessonTypeDAO.findLessonType(course_id);
//					if(lt!=null){
						for(int i=0;i<materialist.length();i++){
							JSONObject jsonobject=(JSONObject) materialist.get(i);
							String material_id = jsonobject.getString("material_id");
							String ord = jsonobject.getString("ord");
							Lesson lesson = this.lessonDAO.findLesson(material_id);
							if(lesson != null){
								lesson.setOrd(Integer.parseInt(ord));
								this.lessonDAO.updateLesson(lesson);
							}
						}
						result.put("result", "1");
//					} else {
//						// 没有获取到课程
//						result.put("result", "5");
//					}
				} else {
					// 无此讲师
					result.put("result", "4");
				}
			} else {
				// 无此用户
				result.put("result", "3");
			}
		} else {
			// 没有获取到token或material_id或course_id
			result.put("result", "2");
		}
		return result;
	}
	
	/**
	 * 保存课程信息
	 */
	public Map<String, Object> saveTeacherCourse(HttpServletRequest request) {
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		Map<String, Object> result = new HashMap<>();
		BbsUser user = null;
		LessonType lt = null;
		String token = "";
		String course_id = "";
		String course_name = "";
		String score_paid = "";
		String decription = "";
		String course_type = "";
		String begin_time = "";
		String end_time = "";
		String courseTypeCN = "";
		String ancestors = "";
		if (myJsonObject.has("token") && myJsonObject.has("course_id")) {
			token = myJsonObject.getString("token");
			course_id = myJsonObject.getString("course_id");
			if (myJsonObject.has("course_id")) {
				course_name = myJsonObject.getString("course_name");
			}
			if (myJsonObject.has("score_paid")) {
				score_paid = myJsonObject.getString("score_paid");
			}
			if (myJsonObject.has("decription")) {
				decription = myJsonObject.getString("decription");
			}
			if (myJsonObject.has("course_type")) {
				course_type = myJsonObject.getString("course_type");
			}
			if (myJsonObject.has("begin_time")) {
				begin_time = myJsonObject.getString("begin_time");
			}
			if (myJsonObject.has("end_time")) {
				end_time = myJsonObject.getString("end_time");
			}
			if (myJsonObject.has("ancestors")) {
				ancestors = myJsonObject.getString("ancestors");
			}
			if (myJsonObject.has("name")) {
				courseTypeCN = myJsonObject.getString("name");
			}
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				result.put("result", "3");
				return result;
			}
			
			user = bbsUserDAO.findBbsUser(id);
			if(user!=null){
				Teacher t=user.getTeacher();
				if(t!=null){
					List<LessonType> ltlist=t.getLessonTypes();
					lt=this.lessonTypeDAO.findLessonType(course_id);
					if(lt!=null){
						if(ltlist.contains(lt)){
							if(StringUtil.isNotEmpty(course_name)){
								lt.setName(course_name);
							}
							// 积分
							if (StringUtil.isNotEmpty(score_paid)) {
								lt.setTotalScorePaid(Long.valueOf(score_paid));
							}
							// 课程介绍
							if (StringUtil.isNotEmpty(decription)) {
								lt.setDescription1(decription);
							}
							// 课程分类
							if (StringUtil.isNotEmpty(course_type)) {
								lt.setCourseTypeStr(course_type);
//								 String[] sArray = course_type.split(";");
//								 String labStr = "";
//								 for(String s:sArray){
//									 CourseType ct = this.courseTypeDAO.findCourseType(s);
//									 if(ct!=null){
//										 labStr = labStr + ct.getAncestors();
//									 }
//								 }
							}
							if (StringUtil.isNotEmpty(courseTypeCN)) {
								lt.setCourseTypeCN(courseTypeCN);
							}
							if (StringUtil.isNotEmpty(ancestors)) {
								lt.setLabelStr(ancestors);
							}
							// 开放时间
							if (StringUtil.isNotEmpty(begin_time)) {
								lt.setAvailableBegain(DateUtil.formateMillisecondToDate(begin_time));
							}
							// 结束时间
							if (StringUtil.isNotEmpty(end_time)) {
								lt.setAvailableEnd(DateUtil.formateMillisecondToDate(end_time));
							}
							this.lessonTypeDAO.updateLessonType(lt);
							result.put("result", "1");
						} else {
							// 没有此课程的权限
							result.put("result", "6");
						}
					} else {
						// 没有此课程
						result.put("result", "5");
					}
				} else {
					// 没有此讲师
					result.put("result", "4");
				}
			} else {
				// 没有此用户
				result.put("result", "3");
			}
		} else {
			// 没有获取到token或course_id
			result.put("result", "2");
		}
		return result;
	}

	/**
	 * 获取文件大小，单位为B/KB/MB/GB
	 * 
	 * @param tl
	 *            为SmartUpload.getSize()
	 * @return
	 */
	public String getFileSize(com.jspsmart.upload.File smartFile) {
		int tl = smartFile.getSize();
		String str = "";
		Float k = 1024F;
		Float m = 1024 * 1024F;
		Float g = 1024 * 1024 * 1024F;
		if (tl < k) {
			str = tl + "B";
		} else if (k <= tl && tl < m) {
			Float te = Math.round(tl / k * 100) / 100.00F;
			str = te.toString() + "KB";
		} else if (tl >= m && tl < g) {
			Float te = Math.round(tl / m * 100) / 100.00F;
			str = te.toString() + "MB";
		} else {
			Float te = Math.round(tl / g * 100) / 100.00F;
			str = te.toString() + "GB";
		}
		return str;
	}

	public void getImageOfVideo(BbsFileModel bf) {
		String videoRealPath = "", imageRealPath = "";
		if (bf != null) {
			videoRealPath = FileUtil.getFileRealPath(bf);
			imageRealPath = FileUtil.getImageRealPathById(bf.getId());
			processJPG(videoRealPath, imageRealPath);
		}
	}

	public boolean processJPG(String oldfilepath, String newfilepath) {
		File file = new File(oldfilepath);
		if (!file.isFile()) {
			return false;
		}
		String classPath = getClass().getResource("/").getFile();
		List<String> commend = new java.util.ArrayList<String>();
		commend.add(classPath + "ffmpeg.exe");
		commend.add("-i");
		commend.add(oldfilepath);
		commend.add("-ss");
		commend.add("1");
		commend.add("-vframes");
		commend.add("1");
		commend.add("-r");
		commend.add("1");
		commend.add("-ac");
		commend.add("1");
		commend.add("-ab");
		commend.add("2");
		commend.add("-f");
		commend.add("image2");
		commend.add("-s");
		commend.add("360*360");
		commend.add(newfilepath);
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commend);
			builder.start();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Map<String, Object> getUserExamList(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		String token = "";
		map.put("result", "3");
		if (myJsonObject.has("token")) {
			token = myJsonObject.getString("token");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				map.put("result", "2");
				return map;
			}
			
			BbsUser user = bbsUserDAO.findBbsUser(id);
			if(user!=null){
				String userId = user.getId();
				Set<Map<String, Object>> examList = new HashSet<>();
				List<Examination> exams = examinationDAO.findAllExamination(CookieUtils.getBusinessId(request));
				for(Examination e : exams){
					if(e.getUserStr()!=null && e.getUserStr().contains(userId)){
						if(e.checkParticipateTimes(userId)<e.getMaxNum()){
							Map<String, Object> examTemp = new HashMap<>();
							examTemp.put("exam_id", e.getId());
							examTemp.put("exam_name", e.getName());
							examTemp.put("exam_question_num", String.valueOf(getQuestionsNum(e)));
							examTemp.put("exam_start_time", DateUtil.formateDateToString(e.getAvailableBegain()));
							examTemp.put("exam_end_time", DateUtil.formateDateToString(e.getAvailableEnd()));
							examTemp.put("exam_remain_num", String.valueOf(e.getMaxNum()));
							examTemp.put("exam_score", getScoreOfExamination(e));
							examTemp.put("exam_pass_score", e.getQualified() + "");
							examTemp.put("exam_total_time", e.getTimeLen() * 60 + "");
							examTemp.put("exam_type", ifSimulate(e));
							examTemp.put("exam_courseId","");
							examTemp.put("exam_highestScore", e.getUserTopExamCase(userId) != null
									? e.getUserTopExamCase(userId).getScore() : "0");
							examTemp.put("course_teacher", "");
							examTemp.put("exam_courseName","");
							examList.add(examTemp);
						}
					}
				}
				map.put("examList", examList);
				map.put("result", "1");
			}
		} else {
			map.put("result", "2");
		}
		return map;
	}

	public Map<String, Object> getUserExamListBackUp(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		String token = "";
		map.put("result", "3");
		if (myJsonObject.has("token")) {
			token = myJsonObject.getString("token");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				map.put("result", "2");
				return map;
			}
			
			BbsUser user = bbsUserDAO.findBbsUser(id);
			if(user!=null){
				String[] ltstr = user.getLessonTypeStr().split(";");
				Set<Map<String, Object>> examList = new HashSet<>();
				for (int i = 0; i < ltstr.length; i++) {
					String ltid = ltstr[i];
					if (!StringUtil.isEmpty(ltid)) {
						LessonType lt = this.lessonTypeDAO.findLessonType(ltid);
						if (lt != null && lt.getExams() != null && lt.getExams().size() > 0) {
							for (Examination e : lt.getExams()) {
								if (e != null) {
									// List<ExamCase> ecs =
									// this.examCaseDAO.findExamCaseByExaminationAndUser(e.getId(),
									// user.getId());
									// Map<String, Map<String, String>> mape =
									// this.getHighestScoreExamMap(ecs);
									Map<String, Object> exam = new HashMap<>();
									exam.put("exam_id", e.getId());
									exam.put("exam_name", e.getName());
									exam.put("exam_question_num", String.valueOf(getQuestionsNum(e)));
									exam.put("exam_start_time", DateUtil.formateDateToString(e.getAvailableBegain()));
									exam.put("exam_end_time", DateUtil.formateDateToString(e.getAvailableEnd()));
									exam.put("exam_remain_num", String.valueOf(e.getMaxNum()));
									exam.put("exam_score", getScoreOfExamination(e));
									exam.put("exam_pass_score", e.getQualified() + "");
									exam.put("exam_total_time", e.getTimeLen() * 60 + "");
									exam.put("exam_type", ifSimulate(e));
									exam.put("exam_courseId", lt.getId());
									// exam.put("exam_highestScore",mape.get(e.getId())==null?"":mape.get(e.getId()).get("exam_score"));
									exam.put("exam_highestScore", e.getUserTopExamCase(user.getId()) != null
											? e.getUserTopExamCase(user.getId()).getScore() : "0");
									if (lt.getTeacher() != null) {
										exam.put("course_teacher", StringUtil.isEmpty(lt.getTeacher().getName()) ? ""
												: lt.getTeacher().getName());
									} else {
										exam.put("course_teacher", "");
									}
									exam.put("exam_courseName", lt.getName());
									examList.add(exam);
								}
							}
						}
					}
				}
				map.put("examList", examList);
				map.put("result", "1");
			}
		} else {
			map.put("result", "2");
		}
		return map;
	}
	
	public Map<String, Object> getStudyPlanLogById(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		if (myJsonObject.has("id")) {
			String id = myJsonObject.getString("id");
			if (myJsonObject.has("token")) {
				String token = myJsonObject.getString("token");
				String uid = userTokenDAO.getIdByToken(token);
				if(uid==null){
					map.put("result", "4");// token过期
					return map;
				}
				
				BbsUser user = bbsUserDAO.findBbsUser(uid);
				StudyPlanLog spl=null;
				if(user!=null){
					spl = this.studyPlanLogDAO.findStudyPlanLogBySPAndUsr(id, user.getId());
				}
				if (spl == null || user == null) {
					map.put("result", "3");// 无此学习计划
					return map;
				}
				map = this.StudyPlanLogToMap(spl, request);
				List<CourseOfPlan> cops = spl.getStudyPlan().getCourses();
				if (cops != null && cops.size() > 0) {
					map.put("course_list", this.courseToMap(cops, user, request));
				} else {
					map.put("course_list", new ArrayList<>());
				}
				String examIds = spl.getStudyPlan().getExamsStr();
				if (StringUtil.isNotEmpty(examIds)) {
					map.put("exam_list", this.examToMap(this.getExamsByIds(examIds), user));
				} else {
					map.put("exam_list", new ArrayList<>());
				}
				map.put("result", "1");
			}
		} else {
			map.put("result", "2");// id为空
			return map;
		}
		return map;
	}

	private List<Examination> getExamsByIds(String examsStr) {
		List<Examination> results = new LinkedList<>();
		if (StringUtil.isNotEmpty(examsStr)) {
			results = this.examinationDAO.findExamsByIdList(StringUtil.idsToIdList(examsStr));
		}
		return results;
	}

	private Object courseToMap(List<CourseOfPlan> courses, BbsUser user, HttpServletRequest request) {
		List<Map<String, Object>> list = new LinkedList<>();
		List<LessonTypeLog> ltls = new LinkedList<>();
		Map<String, LessonTypeLog> logMap = new HashMap<>();
		if (user != null) {
			ltls = this.lessonTypeLogDAO.findLessonTypeLogByUsr(user.getId());
			for (LessonTypeLog ltl : ltls) {
				LessonType ltype = ltl.getLessonType();
				if (ltype != null) {
					logMap.put(ltype.getId(), ltl);
				}
			}
		}
		for (CourseOfPlan course : courses) {
			LessonType lt = course.getLessonType();
			if (lt != null) {
				Map<String, Object> cMap = LessonTypeToMap(lt, logMap.get(lt.getId()), user, request);
				cMap.put("course_totalClassNum", StringUtil.returnString(course.getTotalClassNum() + ""));
				cMap.put("ifRequired", StringUtil.returnString(course.isIfRequired() ? "1" : "0"));
				list.add(cMap);
			}
		}
		return list;
	}

	private Map<String, Object> LessonTypeToMap(LessonType lt, LessonTypeLog ltl, BbsUser user,
			HttpServletRequest request) {
		Map<String, Object> cMap = new HashMap<>();
		if (lt != null) {
			cMap.put("course_id", StringUtil.returnString(lt.getId()));
			cMap.put("course_passClassNum", "");
			cMap.put("course_ifBuy", "");
			cMap.put("course_ifCollect", 0);
			if (ltl != null) {
				cMap.put("course_passClassNum", ltl.getFinishedClassNum() + "");
				cMap.put("course_ifBuy", 1);
			}
			if (user != null) {
				String ids = user.getCollectionCourses();
				if (StringUtil.isNotEmpty(ids) && ids.contains(lt.getId())) {
					cMap.put("course_ifCollect", 1);
				}
			}
			cMap.put("course_totalClassNum", StringUtil.returnString(lt.getTotalClassNum() + ""));
			cMap.put("course_score", StringUtil.returnString(lt.getTotalScorePaid() + ""));
			cMap.put("course_pricture",
					JsonUtil.getDownloadUrl(request) + "/" + StringUtil.returnString(lt.getPicture()));
			cMap.put("course_name", StringUtil.returnString(lt.getName()));
			// 课程摘要
			cMap.put("course_summary", StringUtil.returnString(lt.getDescription1()));
			cMap.put("course_end_time", StringUtil.returnString(DateUtil.formateDateToString(lt.getAvailableEnd())));
			cMap.put("course_url", StringUtil.returnString(lt.getJsonDownLoadUrl()));
			cMap.put("course_version", StringUtil.returnString(lt.getVersion() + ""));
			cMap.put("course_md5", StringUtil.returnString(lt.getMD5()));
			cMap.put("course_start_time",
					StringUtil.returnString(DateUtil.formateDateToString(lt.getAvailableBegain())));
			cMap.put("course_teacher",
					StringUtil.returnString(lt.getTeacher() == null ? "" : lt.getTeacher().getName()));
			cMap.put("teacher_qq", StringUtil.returnString(lt.getTeacher() == null ? "" : lt.getTeacher().getQq()));
			cMap.put("teacher_id", StringUtil.returnString(lt.getTeacher() == null ? "" : lt.getTeacher().getId()));
			// 开放考试需要完成课时数
			cMap.put("openExamNum", StringUtil.returnString(lt.getOpenExamNum() + ""));
			cMap.put("course_tag", StringUtil.returnString(lt.getLabelStr()));
			cMap.put("course_user_num", StringUtil.returnString(lt.getUserNum() + ""));
			// 关联练习数目
			cMap.put("practice_num", StringUtil.returnString(lt.getPracticeNum() + ""));
			cMap.put("shareUrl", StringUtil
					.returnString(UrlUtil.getPrefixUrlByRequest(request) + "talk/LessonList.jspx?tid=" + lt.getId()));
		}
		return cMap;
	}

	private Map<String, String> LessonTypeToMap2(LessonType lt, LessonTypeLog ltl, BbsUser user,
			HttpServletRequest request) {
		Map<String, String> cMap = new HashMap<>();
		long time = 0;
		if (lt != null) {
			cMap.put("course_ifBuy", "");
			cMap.put("course_ifCollect", "");
			if (ltl != null) {
				cMap.put("course_passClassNum", ltl.getFinishedClassNum() + "");
				cMap.put("course_ifBuy", "1");
			}
			if (user != null) {
				String ids = user.getCollectionCourses();
				if (StringUtil.isNotEmpty(ids) && ids.contains(lt.getId())) {
					cMap.put("course_ifCollect", "1");
				}
			}
			cMap.put("course_score", StringUtil.returnString(lt.getTotalScorePaid() + ""));
			cMap.put("course_id", StringUtil.returnString(lt.getId()));
			cMap.put("course_name", StringUtil.returnString(lt.getName()));
			cMap.put("course_pricture",
					JsonUtil.getDownloadUrl(request) + "/" + StringUtil.returnString(lt.getPicture()));
			cMap.put("course_summary", StringUtil.returnString(lt.getDescription1()));
			cMap.put("course_version", StringUtil.returnString(lt.getVersion() + ""));
			cMap.put("course_url", StringUtil.returnString(lt.getJsonDownLoadUrl()));
			cMap.put("openExamNum", StringUtil.returnString(lt.getOpenExamNum() + ""));
			cMap.put("course_tag", StringUtil.returnString(lt.getLabelStr()));
			cMap.put("course_teacher",
					StringUtil.returnString(lt.getTeacher() == null ? "" : lt.getTeacher().getName()));
			cMap.put("course_start_time",
					StringUtil.returnString(DateUtil.formateDateToString(lt.getAvailableBegain())));
			cMap.put("course_end_time", StringUtil.returnString(DateUtil.formateDateToString(lt.getAvailableEnd())));
			cMap.put("course_md5", StringUtil.returnString(lt.getMD5()));
			cMap.put("teacher_qq", StringUtil.returnString(lt.getTeacher() == null ? "" : lt.getTeacher().getQq()));
			cMap.put("teacher_id", StringUtil.returnString(lt.getTeacher() == null ? "" : lt.getTeacher().getId()));
			cMap.put("course_user_num", StringUtil.returnString(lt.getUserNum() + ""));
			cMap.put("shareUrl", StringUtil
					.returnString(UrlUtil.getPrefixUrlByRequest(request) + "talk/LessonList.jspx?tid=" + lt.getId()));
			cMap.put("reason", StringUtil.returnString(lt.getReason() == null ? "" : lt.getReason()));

			// String courseTypeStr=lt.getCourseTypeStr();
			String typeName = lt.getCourseTypeCN();
			// if(StringUtil.isNotEmpty(courseTypeStr)){
			// List<String> idList = StringUtil.idsToIdList(courseTypeStr);
			// List<CourseType> ctlist =
			// this.courseTypeDAO.findSomeCourseType(idList);
			// for(CourseType ct:ctlist){
			// typeName=typeName+ct.getName()+";";
			// }
			// }
			cMap.put("course_type_name", StringUtil.returnString(typeName));

			// 审核状态
			cMap.put("approved", StringUtil.returnString(lt.getApproved() + ""));
			// 上下架状态
			cMap.put("enabled", StringUtil.returnString(lt.isEnabled() ? "1" : "0"));

			// List<Lesson>
			// lslist=lessonDAO.findAllShowedLessonByType(lt.getId());
			// for(Lesson ls:lslist){
			// List<BbsFileModel> filelist=ls.getFiles();
			// for(BbsFileModel bfm:filelist){
			// time=time+bfm.getTotal_time();
			// }
			// }
			time = lt.getTotalTime();
			cMap.put("total_time", String.valueOf(time * 1000));
		}
		return cMap;
	}

	public Map<String, Object> getStudyPlanList(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		map.put("result", "2");
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		BbsUser user = null;
		int isUserInfo = 0, firstSize = 0, pageSize = 0;
		boolean ifDesc = true;
		String order = null;
		List<String> idlist = null;
		boolean ifIn = true;
		if (myJsonObject.has("token")) {
			String token = myJsonObject.getString("token");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				return map;
			}
			
			user = bbsUserDAO.findBbsUser(id);
			if(user==null){
				return map;
			}
		}
		if (myJsonObject.has("isUserInfo")) {
			isUserInfo = myJsonObject.getInt("isUserInfo");
		}
		if (myJsonObject.has("pageSize") && myJsonObject.has("firstSize")) {
			firstSize = myJsonObject.getInt("firstSize");
			pageSize = myJsonObject.getInt("pageSize");
		}
		if (myJsonObject.has("ifDesc")) {
			int ifd = myJsonObject.getInt("ifDesc");
			if (ifd == 0) {
				ifDesc = false;
			}
		}
		if (myJsonObject.has("order")) {
			int ord = myJsonObject.getInt("order");
			order = StudyPlan.getOrderStr(ord);
		}
		if (myJsonObject.has("ids")) {
			String ids = myJsonObject.getString("ids");
			if (StringUtil.isNotEmpty(ids)) {
				idlist = StringUtil.idsToIdList(ids);
			}
			if (myJsonObject.has("ifIn")) {
				int i = myJsonObject.getInt("ifIn");
				if (i == 0) {
					ifIn = false;
				}
			}
		}
		List<StudyPlan> sps = studyPlanDAO.findStudyPlansByOrder(order, firstSize, pageSize, ifDesc, idlist, ifIn);
		long num = studyPlanDAO.getStudyPlansNumByOrder(idlist, ifIn,CookieUtils.getBusinessId(request));
		if (sps != null && sps.size() > 0) {
			if (isUserInfo == 1) {
				map.put("study_plan_list", this.studyPlansToMap(sps, user, request));
			} else {
				map.put("study_plan_list", this.studyPlansToMap(sps, null, request));
			}
			map.put("total_num", num);
			map.put("result", "1");
		}
		return map;
	}

	private List<Map<String, Object>> getStudyPlanList(List<StudyPlan> sps, HttpServletRequest request) {
		List<Map<String, Object>> list = new LinkedList<>();
		for (StudyPlan sp : sps) {
			list.add(studyPlanToMap(sp, request));
		}
		return list;
	}

	private Map<String, Object> studyPlanToMap(StudyPlan sp, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		if (sp != null) {
			map.put("studyPlan_id", sp.getId());
			map.put("plan_startTime", sp.getAvailableBegain() == null ? "" : sp.getAvailableBegain().getTime() + "");
			map.put("plan_endTime", sp.getAvailableEnd() == null ? "" : sp.getAvailableEnd().getTime() + "");
			map.put("name", StringUtil.returnString(sp.getName()));
			map.put("picture", JsonUtil.getDownloadUrl(request) + "/" + StringUtil.returnString(sp.getPicture()));
			map.put("introduction", StringUtil.returnString(sp.getIntroduction()));
			map.put("minPeriodNum", sp.getFinishPlanNum() + "");
			map.put("planTotalNum", sp.getPlanTotalNum() + "");
			map.put("ifFinishNum", sp.isIfFinishNum() ? "1" : "0");
			map.put("ifFinishExam", sp.isIfFinishExam() ? "1" : "0");
			map.put("courses", StringUtil.returnString(sp.getCourseStr()));
			map.put("exams", StringUtil.returnString(sp.getExamsStr()));
		}
		return map;
	}

	public Map<String, Object> getTeacherById(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		if (myJsonObject.has("id")) {
			String id = myJsonObject.getString("id");
			Teacher t = this.TeacherDAO.findTeacher(id);
			if (t != null) {
				map.put("id", StringUtil.returnString(t.getId()));
				map.put("email", StringUtil.returnString(t.getEmail()));
				map.put("introduction", StringUtil.returnString(t.getIntroduction()));
				// map.put("job_yitle",
				// StringUtil.returnString(t.getJobTitle()));
				map.put("name", StringUtil.returnString(t.getName()));
				map.put("qq", StringUtil.returnString(t.getQq()));
				map.put("teacher_type", StringUtil.returnString(t.getTeacherType() + ""));
				map.put("if_show", StringUtil.returnString(t.isIfShow() ? "1" : "0"));
				map.put("picture", JsonUtil.getDownloadUrl(request) + "/" + StringUtil.returnString(t.getPicture()));
				map.put("result", "1");
			} else {// 无此讲师
				map.put("result", "3");
			}
		} else {// 没接收到id
			map.put("result", "2");
		}
		return map;
	}

	public Map<String, Object> getUserByCourseAndNum(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		if (myJsonObject.has("courseId")) {
			List<LessonTypeLog> ltls = this.lessonTypeLogDAO
					.findLessonTypeLogByLessonType(myJsonObject.getString("courseId"));
			if (ltls != null && ltls.size() > 0) {
				int num = ltls.size();
				if (myJsonObject.has("num")) {
					int size = Integer.parseInt(myJsonObject.getString("num"));
					num = size > num ? num : size;
				}
				List<Map<String, String>> users = new LinkedList<>();
				for (int i = 0; i < num; i++) {
					BbsUser u = ltls.get(i).getUser();
					if (u == null) {
						continue;
					}
					Map<String, String> userMap = new HashMap<>();
					userMap.put("id", StringUtil.returnString(u.getId()));
					userMap.put("name", StringUtil.returnString(u.getName()));
					userMap.put("picture",
							JsonUtil.getDownloadUrl(request) + "/" + StringUtil.returnString(u.getPicUrl()));
					userMap.put("time", StringUtil.returnString(ltls.get(i).getGenTime().getTime() + ""));
					userMap.put("phone", StringUtil.returnString(u.getTel()));
					// userMap.put("date",
					// StringUtil.returnString(DateUtil.formateDataToCNString(ltls.get(i).getGenTime(),
					// false)));
					users.add(userMap);
				}
				map.put("users", users);
				map.put("result", "1");
			} else {
				map.put("result", "3");
			}
		} else {// 没接收到课程id
			map.put("result", "2");
		}
		return map;
	}

	/**
	 * 可能没用到
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Object> getStudyPlanByUser(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		if (myJsonObject.has("id")) {
			String id = myJsonObject.getString("id");
			List<StudyPlan> sps = this.studyPlanDAO.findStudyPlanByUserId(id);
			if (sps != null && sps.size() > 0) {
				map.put("study_plan_list", this.getStudyPlanList(sps, request));
				map.put("result", "1");
			}
		} else {
			map.put("result", 2);
		}
		return map;
	}

	public Map<String, Object> getStudyPlanLogByUser(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		map.put("result", 2);
		List<Map<String, Object>> list = new LinkedList<>();
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		List<StudyPlanLog> logs = null;
		if (myJsonObject.has("token")) {
			String token = myJsonObject.getString("token");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				return map;
			}
			
			BbsUser user = bbsUserDAO.findBbsUser(id);
			if(user!=null){
				String userId = user.getId();
				logs = this.studyPlanLogDAO.findStudyPlanLogByUserId(userId);
				if (logs != null) {
					for (StudyPlanLog log : logs) {
						list.add(this.StudyPlanLogToMap(log, request));
					}
					map.put("log_list", list);
				}
				map.put("result", 1);
			}
		} else {
			map.put("result", 2);
		}
		return map;
	}

	public Map<String, Object> StudyPlanChangeLogToMap(StudyPlanChangeLog log, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		if (log != null) {
			map = StudyPlanLogToMap(log.getStudyPlanLog(), request);
			map.put("changelog_id", StringUtil.returnString(log.getId()));
			map.put("change_log_type", StringUtil.returnString(log.getLogType()));
			map.put("create_time", StringUtil.returnString(log.getCreateTime().getTime() + ""));
			if (log.getExam() != null) {
				map.put("exam_id", StringUtil.returnString(log.getExam().getId()));
				map.put("exam_name", StringUtil.returnString(log.getExam().getName()));
				map.put("exam_score", StringUtil.returnString(log.getExamScore() + ""));
			}
			if (log.getLessonType() != null) {
				map.put("course_id", StringUtil.returnString(log.getLessonType().getId()));
				map.put("course_name", StringUtil.returnString(log.getLessonType().getName()));
				map.put("completion_percent", StringUtil.returnString(log.getCompletionPercent()));
			}
			if (log.getLesson() != null) {
				map.put("material_id", StringUtil.returnString(log.getLesson().getId()));
				map.put("material_name", StringUtil.returnString(log.getLesson().getName()));
				map.put("class_num", StringUtil.returnString(log.getClassNum() + ""));
			}
		}
		return map;
	}

	public Map<String, Object> StudyPlanLogToMap(StudyPlanLog log, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		if (log != null) {
			map = this.studyPlanToMap(log.getStudyPlan(), request);
			map.put("create_time", StringUtil.returnString(log.getCreateTime().getTime() + ""));
			map.put("finishPeriodNum", StringUtil.returnString(log.getFinishedClassNum() + ""));
			map.put("finishRequiredPeriodNum", StringUtil.returnString(log.getFinishedRClassNum() + ""));
			map.put("finished_examNum", StringUtil.returnString(log.getFinishedExamNum() + ""));
			map.put("finished_requiredNum", StringUtil.returnString(log.getFinishedRequiredNum() + ""));
			map.put("studyPlan_Log_id", StringUtil.returnString(log.getId()));
			String completedProgress = log.getCompletedProgress();
			completedProgress = StringUtil.isEmpty(completedProgress) ? "0" : completedProgress;
			map.put("completed_progress", StringUtil.returnString(completedProgress));
			map.put("finishDate",
					StringUtil.returnString(log.getFinishedTime() == null ? "" : log.getFinishedTime().getTime() + ""));
			map.put("ranking", StringUtil.returnString(log.getRanking() + ""));
		}
		return map;
	}

	public BbsUser getUserByTokenYun(String token) {
		BbsUser user = null;
//		JSONObject jsonObject = Conn.getEid(yun_host, token);
//		String status = jsonObject.getString("status");
//		if (status.equals("1")) {
//			String eid = jsonObject.getString("user_id");
//			user = bbsUserDAO.findBbsUserByExtid(eid);
//		}
		return user;
	}

	public BbsUser getUserByTokenYun2(String token){
		BbsUser user=null;
//		user = UserUtil.getBbsUserByToken(token);
		return user;
	}
	
	public Map<String,Object> getCoursesHasPractice(HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		JSONObject json = this.getJSONObjectByRequest(request);
		map.put("result", "2");
		if (json.has("token")) {
			map.put("result", "1");
			String token = json.getString("token");
			int firstIndex = -1;
			int pageSize = -1;
			if (json.has("firstIndex") && json.has("pageSize")) {
				firstIndex = Integer.parseInt(json.getString("firstIndex"));
				pageSize = Integer.parseInt(json.getString("pageSize"));
			}
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				return map;
			}
			
			BbsUser user = bbsUserDAO.findBbsUser(id);
			List<Map<String, Object>> courses = new LinkedList<>();
			long num = 0;
			if (user != null) {
				String ids = user.getLessonTypeStr();
				List<LessonType> lts = this.lessonTypeDAO.findLessonTypesHasPractice(StringUtil.idsToIdList(ids),
						firstIndex, pageSize, false, CookieUtils.getBusinessId(request));
				courses = this.lessonTypesToCourseList(lts, null, request);
				num = this.lessonTypeDAO.getNumOfLessonTypesHasPractice(StringUtil.idsToIdList(ids), false, CookieUtils.getBusinessId(request));
			}
			map.put("courseList", courses);
			map.put("num", num);
		}
		return map;
	}

	public Map<String, Object> getBuyedCoursesHasPractice(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject json = this.getJSONObjectByRequest(request);
		map.put("result", "2");
		String businessId = CookieUtils.getBusinessId(request);
		if (json.has("token")) {
			String token = json.getString("token");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				return map;
			}
			
			BbsUser user = bbsUserDAO.findBbsUser(id);
			if(user!=null){
				map.put("result", "1");
				String ids = user.getLessonTypeStr();
				List<Map<String, Object>> courses = new LinkedList<>();
				long num = 0;
				if (ids != null) {
					int firstIndex = -1;
					int pageSize = -1;
					if (json.has("firstIndex") && json.has("pageSize")) {
						firstIndex = Integer.parseInt(json.getString("firstIndex"));
						pageSize = Integer.parseInt(json.getString("pageSize"));
					}
					List<LessonType> lts = this.lessonTypeDAO.findLessonTypesHasPractice(StringUtil.idsToIdList(ids),
							firstIndex, pageSize, true,businessId);
					num = this.lessonTypeDAO.getNumOfLessonTypesHasPractice(StringUtil.idsToIdList(ids), true,businessId);
					map.put("num", num);
					if (lts != null && lts.size() > 0) {
						courses = this.lessonTypesToCourseList(lts, user, request);
						Map<String, Map<String, Object>> cmp = new HashMap<>();
						for (Map<String, Object> course : courses) {
							cmp.put((String) course.get("course_id"), course);
						}
						for (LessonType lt : lts) {
							if (lt != null) {
								// 获取对该用户部门开放的学习资料列表
								List<Lesson> ml = lessonDAO.findAllShowedLessonByType(lt.getId());
								// 获取对该用户部门开放的练习题对象列表
								List<ModuleExamination2> pl = new LinkedList<>();
								for (Lesson m : ml) {
									List<ModuleExamination2> l = m.getModuleExaminations();
									pl.addAll(l);
								}
								List<Practice> practiceList = moduleExaminationToPractice(pl, user, request);
								cmp.get(lt.getId()).put("practice_list", practiceList);
							}
						}
					}
				}
				map.put("courseList", courses);
				map.put("num", num);
			}
		}
		return map;
	}

	/*
	 * 获取首页数据
	 * */
	public Map<String, Object> getWelcomePageData(HttpServletRequest request) {
//		System.out.println("时间1："+new Date().getTime());
		
		String businessId = CookieUtils.getBusinessId(request);
		Map<String, Object> map = new HashMap<>();
		JSONObject json = this.getJSONObjectByRequest(request);
		map.put("result", "2");
		if (json.has("token")) {
			String token = json.getString("token");
			String type = "";
			if(!json.isNull("type")){
				type = json.getString("type");
			}
			String uid = userTokenDAO.getIdByToken(token);
			if(uid==null){
				return map;
			}
			
			BbsUser user = bbsUserDAO.findBbsUser(uid);
			if(user==null){
				return map;
			}
			
			int newFirstSize = 0,newPageSize = 0,planFirstSize=0,planPageSize=0,hotFirstSize=0,hotPageSize=0;
			if(json.has("newPageSize")){
				if(json.has("newFirstSize")){
					newPageSize = Integer.parseInt(json.getString("newPageSize"));
					newFirstSize = Integer.parseInt(json.getString("newFirstSize"));
				}
			}
			if (json.has("planPageSize")) {
				if (json.has("planFirstSize")) {
					planPageSize = Integer.parseInt(json.getString("planPageSize"));
					planFirstSize = Integer.parseInt(json.getString("planFirstSize"));
				}
			}
			if (json.has("hotPageSize")) {
				if (json.has("hotFirstSize")) {
					hotPageSize = Integer.parseInt(json.getString("hotPageSize"));
					hotFirstSize = Integer.parseInt(json.getString("hotFirstSize"));
				}
			}
			
			// 最新课程
			List<LessonType> newestCourses = this.lessonTypeDAO.findLessonTypesByOrder3("genTime",businessId, "true", newFirstSize,
					newPageSize, true, null, false, null,user);
			List<Map<String, Object>> newestCourses2 = new LinkedList<>();
			if (newestCourses != null && newestCourses.size() > 0) {
				newestCourses2 = lessonTypesToCourseList(newestCourses, user, request);
			}
			map.put("newestCourses", newestCourses2);
			System.out.println("时间3："+new Date().getTime());
			
			// 学习计划
			List<Map<String, Object>> studyPlans2 = new LinkedList<>();
			if (user != null) {
				List<StudyPlan> studyPlans = this.studyPlanDAO.findStudyPlanByUserId(user.getId(), planFirstSize,
						planPageSize);
				if (studyPlans != null && studyPlans.size() > 0) {
					studyPlans2 = studyPlansToMap(studyPlans, user, request);
				}
			}
			map.put("studyPlans", studyPlans2);
			
			// 热门课程
			List<LessonType> hotCourses = this.lessonTypeDAO.findLessonTypesByOrder("userNum", businessId, "true", hotFirstSize,
					hotPageSize, true, null, false, null);
			List<Map<String, Object>> hotCourses2 = new LinkedList<>();
			if (hotCourses != null && hotCourses.size() > 0) {
				hotCourses2 = lessonTypesToCourseList(hotCourses, user, request);
			}
			map.put("hotCourses", hotCourses2);
			
			//在学课程
			List<LessonType> activeCourses =  getActiveCourses(user);
			List<Map<String, Object>> activeCourses2 = new LinkedList<>();
			if (activeCourses != null && activeCourses.size() > 0) {
				activeCourses2 = lessonTypesToCourseList(activeCourses, user, request);
			}
			map.put("activeCourses", activeCourses2);
			
			//感兴趣
			List<LessonType> interestCourses =  getInterestCourses(user);
			List<Map<String, Object>> interestCourses2 = new LinkedList<>();
			if (interestCourses != null && interestCourses.size() > 0) {
				interestCourses2 = lessonTypesToCourseList(interestCourses, user, request);
			}
			map.put("interestCourses", interestCourses2);
			
			
			
			CourseTypeDAO ctdao = SpringHelper.getSpringBean("CourseTypeDAO2");
			List<Map<String, Object>> firstTags = new LinkedList<>();
			
//			System.out.println("时间5："+new Date().getTime());
			List<CourseType> ctList = (List<CourseType>) ctdao
					.getScrollData(CourseType.class, 0, 4, " o.frontShow=1 and  o.fatherId='"+businessId+"' ", null, null).get("list");
			for (CourseType ct : ctList) {
				Map<String, Object> ctmap = new HashMap<>();
				ctmap.put("id", ct.getId());
				ctmap.put("name", ct.getName());
				ctmap.put("picture", StringUtil.isEmpty(ct.getPicture()) ? ""
						: JsonUtil.getDownloadUrl(request) + "/" + ct.getPicture());
				firstTags.add(ctmap);
			}
			map.put("firstTags", firstTags);
//			System.out.println("时间6："+new Date().getTime());
			if(type.equals("1")){
				map.put("secondTags","");
			}else{
				List<Map<String, Object>> secondTags = (List<Map<String, Object>>)ctdao.pageAppCourseTypeByFloorShow(0, 4, businessId).get("list");
				if(secondTags.isEmpty()){//如果没有设置，首页展示
					secondTags = (List<Map<String, Object>>) ctdao.getAppCourseType(0, 4,businessId).get("list");
				}
				for (Map<String, Object> tagmap : secondTags) {
					String id = (String) tagmap.get("id");
					List<LessonType> lts = this.lessonTypeDAO.findLessonTypesByCondition(" l.businessId='"+businessId+"' and  l.labelStr like '%" + id + "%' ",
							null, 0, 4, null, 0);
					tagmap.put("courses", this.lessonTypesToCourseList(lts, user, request));
				}
				map.put("secondTags", secondTags);
			}
			map.put("result", "1");
//			System.out.println("时间7："+new Date().getTime());
		}
		return map;
	}
	
	private List<LessonType> getActiveCourses(BbsUser user){
		List<LessonType> models = null ;
//		String acs = user.getLessonTypeStr();
//    	if(StringUtil.isNotEmpty(acs)){
//    		String acs2 = getInSqlString(acs, ";");
//    		String whereSql = " l.id in "+acs2+" and l.totalScorePaid=0 and l.enabled=true and l.secreted<>1";
//    		models = lessonTypeDAO.findLessonTypesByCondition(whereSql,null, 1, 4, null, 0);
//		}
		// 查看最近看的四个课程
		List<LessonTypeLog> list = lessonTypeLogDAO.findLessonTypeLogByUsrAndMaxResults(user.getId(), 4);
		if(!list.isEmpty()){
			models = new ArrayList<LessonType>();
			for(LessonTypeLog lessonTypeLog : list){
				models.add(lessonTypeLog.getLessonType());
			}
		}
		return models;
	}
	
	private List<LessonType> getInterestCourses(BbsUser user){
		List<LessonType> models = null ;
		//最近看的一课程。同类型的其他课程
		List<LessonTypeLog> list = lessonTypeLogDAO.findLessonTypeLogByUsrAndMaxResults(user.getId(), 1);
		//最近学习的课程
		List<LessonType> lessonTypes = new ArrayList<LessonType>();
		if(list.isEmpty()){
			return lessonTypes;
		}
		LessonType lastStudy = list.get(0).getLessonType();
		lessonTypeDAO.findOnApprovalInLessonTypesByCourseType(null);
		String courseTypeStr = lastStudy.getCourseTypeStr();
		String[] courseTypeStrs = courseTypeStr.split(";");
		Random random = new Random();
		int randomInt = random.nextInt(courseTypeStrs.length);
		String whereSql = " l.labelStr like '%" + courseTypeStrs[randomInt] +"%' and l.id <> '" +lastStudy.getId() +"' and l.enabled=true and l.secreted<>1";
		lessonTypes = lessonTypeDAO.findLessonTypesByCondition(whereSql,null, 1, 4, null, 0);
	
//		List<LessonType> lts = this.lessonTypeDAO.findLessonTypesByCondition(" l.businessId='"+businessId+"' and  l.labelStr like '%" + id + "%' ",
//				null, 0, 4, null, 0);
		return lessonTypes;
	}
	
	private String getInSqlString(String acs,String split){
		String acs2 = "('";
		String[] acsarray = acs.split(split);
		for(String a : acsarray){
			acs2 = acs2+a+"','";
		}
		acs2 = acs2.substring(0, acs2.length()-2)+")";
		return acs2;
	}

	private List<Map<String, Object>> studyPlansToMap(List<StudyPlan> studyPlans, BbsUser user,
			HttpServletRequest request) {
		List<Map<String, Object>> studyPlanList = new LinkedList<>();
		if (studyPlans == null || studyPlans.size() == 0) {
			return studyPlanList;
		}
		Map<String, StudyPlanLog> logMap = new HashMap<>();
		if (user != null) {
			List<StudyPlanLog> spls = this.studyPlanLogDAO.findStudyPlanLogByUserId(user.getId());
			for (StudyPlanLog spl : spls) {
				StudyPlan sp = spl.getStudyPlan();
				if (sp != null) {
					logMap.put(sp.getId(), spl);
				}
			}
		}
		for (StudyPlan sp : studyPlans) {
			if (sp != null) {
				Map<String, Object> c = studyPlanToMap(sp, logMap.get(sp.getId()), request);
				studyPlanList.add(c);
			}
		}
		return studyPlanList;
	}

	private Map<String, Object> studyPlanToMap(StudyPlan sp, StudyPlanLog log, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		if (sp != null) {
			map.put("studyPlan_id", sp.getId());
			map.put("plan_startTime", sp.getAvailableBegain() == null ? "" : sp.getAvailableBegain().getTime() + "");
			map.put("plan_endTime", sp.getAvailableEnd() == null ? "" : sp.getAvailableEnd().getTime() + "");
			map.put("name", StringUtil.returnString(sp.getName()));
			map.put("picture", JsonUtil.getDownloadUrl(request) + "/" + StringUtil.returnString(sp.getPicture()));
			map.put("introduction", StringUtil.returnString(sp.getIntroduction()));
			map.put("minPeriodNum", sp.getFinishPlanNum() + "");
			map.put("planTotalNum", sp.getPlanTotalNum() + "");
			map.put("ifFinishNum", sp.isIfFinishNum() ? "1" : "0");
			map.put("ifFinishExam", sp.isIfFinishExam() ? "1" : "0");
			map.put("courses", StringUtil.returnString(sp.getCourseStr()));
			map.put("exams", StringUtil.returnString(sp.getExamsStr()));
		}
		map.put("create_time", "");
		map.put("finishPeriodNum", "");
		map.put("finishRequiredPeriodNum", "");
		map.put("finished_examNum", "");
		map.put("finished_requiredNum", "");
		map.put("studyPlan_Log_id", "");
		map.put("completed_progress", "");
		map.put("finishDate", "");
		map.put("ranking", "");
		if (log != null) {
			map.put("create_time", StringUtil.returnString(log.getCreateTime().getTime() + ""));
			map.put("finishPeriodNum", StringUtil.returnString(log.getFinishedClassNum() + ""));
			map.put("finishRequiredPeriodNum", StringUtil.returnString(log.getFinishedRClassNum() + ""));
			map.put("finished_examNum", StringUtil.returnString(log.getFinishedExamNum() + ""));
			map.put("finished_requiredNum", StringUtil.returnString(log.getFinishedRequiredNum() + ""));
			map.put("studyPlan_Log_id", StringUtil.returnString(log.getId()));
			String completedProgress = log.getCompletedProgress();
			completedProgress = StringUtil.isEmpty(completedProgress) ? "0" : completedProgress;
			map.put("completed_progress", StringUtil.returnString(completedProgress));
			map.put("finishDate",
					StringUtil.returnString(log.getFinishedTime() == null ? "" : log.getFinishedTime().getTime() + ""));
			map.put("ranking", StringUtil.returnString(log.getRanking() + ""));
		}
		return map;
	}

	public Map<String, Object> getCourseListByOrder(HttpServletRequest request) {
		JSONObject myJsonObject = getJSONObjectByRequest(request);
		Map<String, Object> courseListView = new HashMap<>();
		courseListView.put("result", "2");
		BbsUser user = null;
		if (myJsonObject.has("token")) {
			String token = myJsonObject.getString("token");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				return courseListView;
			}
			user = bbsUserDAO.findBbsUser(id);
			if(user==null){
				return courseListView;
			}
		}
		int firstSize = 0, pageSize = 0;
		boolean ifDesc = true;
		String ifenabled = "true", order = null;
		boolean ifUser = false;
		List<String> idlist = null;
		boolean ifIn = true;
		if (myJsonObject.has("pageSize") && myJsonObject.has("firstSize")) {
			firstSize = myJsonObject.getInt("firstSize");
			pageSize = myJsonObject.getInt("pageSize");
		}
		if (myJsonObject.has("ifDesc")) {
			int ifd = myJsonObject.getInt("ifDesc");
			if (ifd == 0) {
				ifDesc = false;
			}
		}
		if (myJsonObject.has("ifenabled")) {
			int ife = myJsonObject.getInt("ifenabled");
			if (ife == 0) {
				ifenabled = "false";
			} else if (ife == 1) {
				ifenabled = null;
			}
		}
		if (myJsonObject.has("order")) {
			int ord = myJsonObject.getInt("order");
			order = LessonType.getOrderStr(ord);
		}
		if (myJsonObject.has("isUserInfo")) {
			int ord = myJsonObject.getInt("isUserInfo");
			if (ord == 1) {
				ifUser = true;
			}
		}
		if (myJsonObject.has("ids")) {
			String ids = myJsonObject.getString("ids");
			if (StringUtil.isNotEmpty(ids)) {
				idlist = StringUtil.idsToIdList(ids);
			}
			if (myJsonObject.has("ifIn")) {
				int i = myJsonObject.getInt("ifIn");
				if (i == 0) {
					ifIn = false;
				}
			}
		}
		String queryStr = "";
		if (myJsonObject.has("query")) {
			int query = myJsonObject.getInt("query");
			String qStr = LessonType.getOrderStr(query);
			if (myJsonObject.has("min")) {
				int min = myJsonObject.getInt("min");
				queryStr += " and yis." + qStr + ">=" + min;
			}
			if (myJsonObject.has("max")) {
				int max = myJsonObject.getInt("max");
				queryStr += " and yis." + qStr + "<=" + max;
			}
		}
		if (myJsonObject.has("tagId")) {
			String tagid = myJsonObject.getString("tagId");
			if (StringUtil.isNotEmpty(tagid)) {
				queryStr += " and yis.labelStr like '%" + tagid + "%'";
			}
		}

		if (myJsonObject.has("teacher_id")) {
			String teacher_id = myJsonObject.getString("teacher_id");
			if (StringUtil.isNotEmpty(teacher_id)) {
				queryStr += " and yis.teacher.id ='" + teacher_id + "'";
			}
		}

		if (myJsonObject.has("query_name")) {
			String query_name = myJsonObject.getString("query_name");
			if (StringUtil.isNotEmpty(query_name)) {
//				queryStr += " and (yis.teacher.name like '%" + query_name + "%' or yis.name like '%" + query_name + "%')";
				queryStr += " and yis.name like '%" + query_name + "%' ";
			}
		}

		int matching = 0;
		String matchingStr = "";
		if (myJsonObject.has("matching") && myJsonObject.has("matchingStr")) {
			matching = myJsonObject.getInt("matching");
			matchingStr = myJsonObject.getString("matchingStr");
			String matchingField = LessonType.getOrderStr(matching);
			if (StringUtil.isNotEmpty(matchingField) && StringUtil.isNotEmpty(matchingStr)) {
				queryStr += " and yis." + matchingField + " like '%" + matchingStr + "%'";
			}
		}

		String businessId = CookieUtils.getBusinessId(request);
		queryStr += " and yis.businessId='" + businessId +"' ";
		List<LessonType> courses = lessonTypeDAO.findLessonTypesByOrder3(order,businessId, ifenabled, firstSize, pageSize, ifDesc,
				idlist, ifIn, queryStr,user);
		long num = 0;
		List<Map<String, Object>> courseList = new LinkedList<>();
		if (courses != null && courses.size() > 0) {
			if (StringUtil.isNotEmpty(ifenabled)) {
				queryStr = " yis.enabled=" + ifenabled + queryStr;
			}
			num = lessonTypeDAO.getLessonTypesNum(queryStr, CookieUtils.getBusinessId(request));
			if (ifUser) {
				courseList = lessonTypesToCourseList(courses, user, request);
			} else {
				courseList = lessonTypesToCourseList(courses, null, request);
			}
		}
		courseListView.put("courseList", courseList);
		courseListView.put("total_num", num);
		courseListView.put("result", "1");
		return courseListView;
	}

	/**
	 * 清除审批历史记录
	 */
	public Map<String, Object> deleteApproveHistory(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject json = this.getJSONObjectByRequest(request);
		map.put("result", "2");
		if (json.has("token") & json.has("courseApproveHistory_id")) {
			String token = json.getString("token");
			String courseApproveHistory_id= json.getString("courseApproveHistory_id");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				map.put("result", "3");
				return map;
			}
			
			BbsUser usr = bbsUserDAO.findBbsUser(id);
			if(usr!=null){
				Teacher t=usr.getTeacher();
				if(t!=null){
//					String[] ids=courseApproveHistory_id.split(";");
					List<String> idlist=StringUtil.idsToIdList(courseApproveHistory_id);
					courseApproveHistoryDAO.falseDeleteByCourseApproveHistoryByIds(idlist);
					// for(String id:ids){
					// courseApproveHistoryDAO.falseDeleteByCourseApproveHistoryById(id);
					// }
					map.put("result", "1");
				} else {// 无此讲师
					map.put("result", "4");
				}
			} else {// 无此用户
				map.put("result", "3");
			}
		}
		return map;
	}

	/**
	 * 审批历史列表
	 */
	public Map<String, Object> getApproveHistoryList(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject json = this.getJSONObjectByRequest(request);
		int firstSize = 0;
		int pageSize = 0;
		String approvalStatus = "-1";
		map.put("result", "2");
		if (json.has("token")) {
			String token = json.getString("token");
			if (json.has("firstSize")) {
				firstSize = json.getInt("firstSize");
			}
			if (json.has("pageSize")) {
				pageSize = json.getInt("pageSize");
			}
			List<Integer> asintlist = new ArrayList<>();
			if (json.has("approvalStatus")) {
				approvalStatus = json.getString("approvalStatus");
				List<String> aslist = StringUtil.idsToIdList(approvalStatus);
				for (String s : aslist) {
					asintlist.add(Integer.valueOf(s));
				}
			}
			
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				map.put("result", "3");
				return map;
			}
			BbsUser usr = bbsUserDAO.findBbsUser(id);
			if(usr!=null){
				Teacher t=usr.getTeacher();
				if(t!=null){
					List<CourseApproveHistory> cahlist = new LinkedList<>();
					long count = 0;
					if (t.isCanApproved()) {
						cahlist = courseApproveHistoryDAO.findCourseApproveHistoryByApprover(t.getId(), 1, asintlist,
								firstSize, pageSize);
						count = courseApproveHistoryDAO.findCourseApproveHistoryCountByApprover(t.getId(), 1,
								asintlist);
					} else {// 此讲师没有审核权限
						cahlist = courseApproveHistoryDAO.findCourseApproveHistoryByApprover(t.getId(), 0, asintlist,
								firstSize, pageSize);
						count = courseApproveHistoryDAO.findCourseApproveHistoryCountByApprover(t.getId(), 0,
								asintlist);
					}
					List<Map<String, String>> cahmapList = new LinkedList<>();
					if (cahlist.size() > 0) {
						for (CourseApproveHistory c : cahlist) {
							Map<String, String> cMap = new HashMap<>();
							if (c != null) {
								cMap.put("courseApproveHistory_id", StringUtil.returnString(c.getId()));
								if (c.getLessonType() != null) {
									cMap.put("course_id", StringUtil.returnString(c.getLessonType().getId()));
									cMap.put("course_name", StringUtil.returnString(c.getLessonType().getName()));
								} else {
									cMap.put("course_id", "");
									cMap.put("course_name", "");
								}
								Teacher teacher = c.getTeacher();
								Teacher approveTeacher = c.getApproveTeacher();
								if (teacher != null) {
									cMap.put("teacher_name", StringUtil.returnString(teacher.getName()));
								} else {
									cMap.put("teacher_name", "");
								}
								if (approveTeacher != null) {
									cMap.put("approve_teacher_name", StringUtil.returnString(approveTeacher.getName()));
								} else {
									cMap.put("approve_teacher_name", "");
								}
								cMap.put("approved", StringUtil.returnString(String.valueOf(c.getApproved())));
								cMap.put("reason", StringUtil.returnString(c.getReason()));
								cMap.put("create_time",
										StringUtil.returnString(String.valueOf(c.getCreateTime().getTime())));
								cahmapList.add(cMap);
							}
						}
					}
					map.put("approveHistoryList", cahmapList);
					map.put("total_num", count);
					map.put("result", "1");
				} else {// 无此讲师
					map.put("result", "4");
				}
			} else {// 无此用户
				map.put("result", "3");
			}
		}
		return map;
	}

	/**
	 * 
	 * 审批人或被审批人查询课程列表
	 */
	public Map<String, Object> getApproveList(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject json = this.getJSONObjectByRequest(request);
		int firstSize = 0;
		int pageSize = 0;
		int approvalStatus = -1;
		int enable = -1;
		map.put("result", "2");
		if (json.has("token")) {
			String token = json.getString("token");
			if (json.has("firstSize")) {
				firstSize = json.getInt("firstSize");
			}
			if (json.has("pageSize")) {
				pageSize = json.getInt("pageSize");
			}
			if (json.has("approvalStatus")) {
				approvalStatus = json.getInt("approvalStatus");
			}
			if (json.has("enable")) {
				enable = json.getInt("enable");
			}
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				map.put("result", "3");
				return map;
			}
			
			BbsUser user = bbsUserDAO.findBbsUser(id);
			if(user!=null){
				Teacher t=user.getTeacher();
				if(t!=null){
					List<Map<String,String>> courseList= new LinkedList<>();
					List<LessonType> lts = new LinkedList<>();
					long num = 0;
					if (t.isCanApproved()) {
						String courseTypeStr = t.getCourseTypeStr();
						List<LessonType> ltlist = new LinkedList<>();
						if (approvalStatus == 2 || approvalStatus == -1) {
							ltlist = this.lessonTypeDAO.findLessonTypesByBeApprover(null, 1, approvalStatus, enable,
									firstSize, pageSize);
							num = this.lessonTypeDAO.findLessonTypeNumByBeApprover(null, 1, approvalStatus, enable);
						} else {
							ltlist = this.lessonTypeDAO.findLessonTypesByBeApprover(t.getId(), 1, approvalStatus,
									enable, firstSize, pageSize);
							num = this.lessonTypeDAO.findLessonTypeNumByBeApprover(t.getId(), 1, approvalStatus,
									enable);
						}
						for (LessonType lt : ltlist) {
							if (lt != null) {
//								List<String> ids = StringUtil.idsToIdList(lt.getCourseTypeStr());
//								for (String id : ids) {
//									if (courseTypeStr.contains(id)) {
//										lts.add(lt);
//										break;
//									}
//								}
								lts.add(lt);
							}
						}
					} else {// 此讲师没有审核权限
						lts = this.lessonTypeDAO.findLessonTypesByBeApprover(t.getId(), 0, approvalStatus, enable,
								firstSize, pageSize);
						num = this.lessonTypeDAO.findLessonTypeNumByBeApprover(t.getId(), 0, approvalStatus, enable);
					}
					if (lts.size() > 0) {
						courseList = lessonTypesToCourseList2(lts, user, request);
					}
					map.put("courseList", courseList);
					map.put("total_num", num);
					map.put("result", "1");
				} else {// 无此讲师
					map.put("result", "4");
				}
			} else {// 无此用户
				map.put("result", "3");
			}
		}
		return map;
	}

	/**
	 * 
	 * 更改课程审批状态
	 */
	public Map<String, Object> changeCourseApprovalStatus(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject json = this.getJSONObjectByRequest(request);
		map.put("result", "2");
		if (json.has("token")) {
			String token = json.getString("token");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				map.put("result", "3");
				return map;
			}
			
			BbsUser user = bbsUserDAO.findBbsUser(id);
			if(user!=null){
				Teacher t=user.getTeacher();
				if(t!=null){
					int approvalStatus = -1;
					if (json.has("approvalStatus")) {
						approvalStatus = json.getInt("approvalStatus");
					}
					if (t.isCanApproved()) {
						if (approvalStatus < 0 || approvalStatus > 5) {// 无效操作
							map.put("result", "5");
							return map;
						}
					} else {
						if (approvalStatus < 0 || approvalStatus > 2) {// 无效操作
							map.put("result", "5");
							return map;
						}
					}
					String courseId = "";
					if (json.has("courseId")) {
						courseId = json.getString("courseId");
					}
					if (StringUtil.isNotEmpty(courseId)) {
						List<String> isList = StringUtil.idsToIdList(courseId);
						if (CollectionUtil.isNotEmpty(isList)) {
							List<LessonType> lts = this.lessonTypeDAO.findLessonTypeByIdList(isList);
							if (CollectionUtil.isNotEmpty(lts)) {
								for (LessonType lt : lts) {
									if (lt != null) {
										CourseApproveHistory cah = new CourseApproveHistory();
										cah.setApprove(approvalStatus);
										if (t.isCanApproved()) {
											cah.setApproveTeacher(t);
										}
										cah.setLessonType(lt);
										String reason = "";
										if (json.has("reason")) {
											reason = json.getString("reason");
										}
										cah.setReason(reason);
										cah.setTeacher(lt.getTeacher());
										lt.setReason(reason);
										lt.setApproved(approvalStatus);
										if (approvalStatus == 3) {
											if (user.getTeacher() != null) {
												lt.setApproveTeacher(t);
											}
										}
										if (approvalStatus == 4 || approvalStatus == 0) {
											lt.setEnabled(true);
										} else {
											lt.setEnabled(false);
										}
										this.lessonTypeDAO.updateLessonType(lt);
										this.courseApproveHistoryDAO.addCourseApproveHistory(cah);
									}
								}
							}
							map.put("result", "1");
						} else {// 无此课程
							map.put("result", "6");
						}
					}
				} else {// 无此讲师
					map.put("result", "4");
				}
			} else {// 无此用户
				map.put("result", "3");
			}
		}
		return map;
	}

	public Map<String, Object> getCourseComments(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject json = this.getJSONObjectByRequest(request);
		map.put("result", "2");
		if (json.has("token") && json.has("course_id") && json.has("firstIndex") && json.has("pageSize")) {
			String course_id = json.getString("course_id");
			String token = json.getString("token");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				map.put("result", "3");
				return map;
			}
			
			BbsUser user = bbsUserDAO.findBbsUser(id);
			if(user==null){
				map.put("result", "3");
				return map;
			}
			int firstSize = json.getInt("firstIndex");
			int pageSize = json.getInt("pageSize");
			List<Map<String, Object>> list = new LinkedList<>();
			UserCommentDAO ucdao = SpringHelper.getSpringBean("UserCommentDAO");
			Object[] queryParams = {course_id};
			String wheresql = " o.course.id=?1 ";
			Map<String, String> orderby = new HashMap<>();
			orderby.put("createTime", "desc");
			List<UserComment> ucs = ucdao.getScrollData(firstSize, pageSize, wheresql, queryParams, orderby);
			long num = ucdao.getCountByQueryParams(wheresql, queryParams);
			for(UserComment uc : ucs){
				if(uc!=null){
					Map<String, Object> ucmap = new HashMap<>();
					if(uc.getUser()!=null){
						ucmap.put("comment_id", StringUtil.returnString(uc.getId()));
						ucmap.put("comment_author_name", StringUtil.returnString(uc.getUser().getName()));
						ucmap.put("comment_author_picture", StringUtil.isEmpty(uc.getUser().getPicUrl()) ? ""
								: JsonUtil.getDownloadUrl(request) + "/" + uc.getUser().getPicUrl());
						ucmap.put("comment_date", StringUtil.returnString(uc.getCreateTime().getTime()+""));
						ucmap.put("comment_content", StringUtil.returnString(uc.getComment()));
						ucmap.put("comment_star", StringUtil.returnString(uc.getStar()+""));
						if(uc.getUser().getId().equals(user.getId())){
							ucmap.put("is_permisson", "true");
						}else{
							ucmap.put("is_permisson", "false");
						}
						list.add(ucmap);
					}
				}
			}
			map.put("total_num", num);
			map.put("commentList", list);
			map.put("result", "1");
		}
		return map;
	}
	
	public Map<String, Object> deleteCourseComment(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject json = this.getJSONObjectByRequest(request);
		map.put("result", "2");
		if (json.has("comment_id")) {
			String course_id = json.getString("comment_id");
			List<String> ids=StringUtil.idsToIdList(course_id);
			UserCommentDAO ucdao = SpringHelper.getSpringBean("UserCommentDAO");
			for(String id:ids){
				ucdao.delete(id);
			}
			map.put("result", "1");
		}
		return map;
	}
	
	public Map<String, Object> addCourseComment(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject json = this.getJSONObjectByRequest(request);
		map.put("result", "2");
		if (json.has("token") && json.has("course_id") && json.has("star")&&json.has("comment")) {
			String token = json.getString("token");
			String course_id = json.getString("course_id");
			String star = json.getString("star");
			String comment = json.getString("comment");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				map.put("result", "3");
				return map;
			}
			
			BbsUser user = bbsUserDAO.findBbsUser(id);
			if(user==null){
				map.put("result", "3");
				return map;
			}
			if (user != null) {
				LessonType course = lessonTypeDAO.findLessonType(course_id);
				if(course!=null){
					UserComment uc = new UserComment();
					uc.setUser(user);
					uc.setCourse(course);
					uc.setStar(Integer.parseInt(star));
					uc.setComment(comment);
					String businessId = CookieUtils.getBusinessId(request);
					uc.setBusinessId(businessId);
					UserCommentDAO ucdao = SpringHelper.getSpringBean("UserCommentDAO");
					ucdao.add(uc);
					map.put("result", "1");
				}
			}
		}
		return map;
	}
	
	
	public Map<String, Object> changeUserCollectionCourses(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject json = this.getJSONObjectByRequest(request);
		map.put("result", "2");
		BbsUser user = null;
		if (json.has("token")) {
			String token = json.getString("token");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				map.put("result", "3");
				return map;
			}
			user = bbsUserDAO.findBbsUser(id);
		}else if(json.has("userId")){
			String id = json.getString("userId");
			user = this.bbsUserDAO.findBbsUser(id);
		}
		if (user != null) {
			if (json.has("courseIds")) {
				String courseIds = json.getString("courseIds");
				List<String> courseList = StringUtil.idsToIdList(courseIds);
				if (CollectionUtil.isNotEmpty(courseList)) {
					if (json.has("addOrRemove")) {
						int addOrRemove = json.getInt("addOrRemove");
						if (addOrRemove == 0) {
							uService.updateUserCollectionCourses(user, courseList, false);
						} else if (addOrRemove == 1) {
							uService.updateUserCollectionCourses(user, courseList, true);
						}
					} else {
						return map;
					}
				}
			}
			map.put("result", "1");
		} else {
			map.put("result", "3");
		}
		return map;
	}

	public Map<String, Object> getUserCollectionCourses(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject json = this.getJSONObjectByRequest(request);
		map.put("result", "2");
		if (json.has("token")) {
			String token = json.getString("token");
			int firstSize = json.getInt("firstSize");
			int pageSize = json.getInt("pageSize");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				map.put("result", "3");
				return map;
			}
			
			BbsUser user = bbsUserDAO.findBbsUser(id);
			if(user==null){
				map.put("result", "3");
				return map;
			}
			List<Map<String, Object>> list = new LinkedList<>();
			long num = 0;
			if (user != null) {
				String ids = user.getCollectionCourses();
				if (StringUtil.isNotEmpty(ids)) {
					List<String> idlist = StringUtil.idsToIdList(ids);
					List<LessonType> lts = this.lessonTypeDAO.findLessonTypesByOrder2(null, "true", firstSize, pageSize,
							false, idlist, true, null);
					num = this.lessonTypeDAO.getNumByOrder("true", idlist, true);
					if (CollectionUtil.isNotEmpty(lts)) {
						list = this.lessonTypesToCourseList(lts, user, request);
					}
				}
			}
			map.put("courseList", list);
			map.put("total_num", num);
			map.put("result", "1");
		} else {
			map.put("result", "3");
		}
		return map;
	}

	@SuppressWarnings("static-access")
	public Map<String,Object> uploadFiles(ServletConfig config,HttpServletRequest request,HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();
        try {    
            SmartUpload smartUpload = new SmartUpload();  
            smartUpload.initialize(config, request, response);    
            smartUpload.upload();  
            //获取其他的数据  
            Request otherReq = smartUpload.getRequest();  
            //获取从客户端传输过来的用户名,在客户端中对参数进行参数URL编码了，所以服务器这里要进行URL解码  
            String token = "";
            if(otherReq.getParameter("token")!=null){
            	token = URLDecoder.decode(otherReq.getParameter("token"),"utf-8");  
            }
            		
            String uploadType = "";
            if(otherReq.getParameter("uploadType")!=null){
            	uploadType = URLDecoder.decode(otherReq.getParameter("uploadType"),"utf-8");  
            }
            BbsUser user = null;
            if(StringUtil.isNotEmpty(token)){
            	String id = userTokenDAO.getIdByToken(token);
            	if(id==null){
            		map.put("result", "2");
            		return map;
            	}
    			user = bbsUserDAO.findBbsUser(id);
            }
			if(user==null||StringUtil.isEmpty(uploadType)){
				map.put("result", "2");
				return map;
			}
			// 获取上传的文件，因为知道在客户端一次就上传一个文件，所以我们就直接取第一个文件
			if (smartUpload.getFiles() != null) {
				com.jspsmart.upload.File smartFile = smartUpload.getFiles().getFile(0);
				// 判断文件是否丢失
				if (!smartFile.isMissing()) {
					// 另保存至本地
					String id = UUID.randomUUID().toString();
					String ext = smartFile.getFileExt();
					if (!ext.startsWith(".")) {
						ext = "." + ext;
					}
					smartFile.saveAs(FileUtil.getDownloadFilePath(id, ext), smartUpload.SAVE_PHYSICAL);
					if (uploadType.equals("1")) {// 上传讲师证书
						Teacher t = user.getTeacher();
						if (t != null) {
							String url = FileUtil.getShowUrl(id, ext);
							String name = "", qid = "";
							if (otherReq.getParameter("certificate_name") != null) {
								name = URLDecoder.decode(otherReq.getParameter("certificate_name"), "utf-8");
							}
							if (otherReq.getParameter("id") != null) {
								qid = URLDecoder.decode(otherReq.getParameter("id"), "utf-8");
							}
							String cfId = addTeacherCertificate(t, name, url, qid);
							map.put("picture", JsonUtil.getDownloadUrl(request) + "/" + url);
							map.put("id", cfId);
						}
					} else if (uploadType.equals("2")) {// 上传职称
						Teacher t = user.getTeacher();
						if (t != null) {
							String url = FileUtil.getShowUrl(id, ext);
							String name = otherReq.getParameter("jobTitle_name");
							if (name != null) {
								name = URLDecoder.decode(name, "utf-8");
							}
							String gainTime = otherReq.getParameter("gainTime");
							if (gainTime != null) {
								gainTime = URLDecoder.decode(gainTime, "utf-8");
							}
							String titleLevel = otherReq.getParameter("titleLevel");
							if (titleLevel != null) {
								titleLevel = URLDecoder.decode(titleLevel, "utf-8");
							}
							String qid = "";
							if (otherReq.getParameter("id") != null) {
								qid = URLDecoder.decode(otherReq.getParameter("id"), "utf-8");
							}
							String jtId = addTeacherjobTitle(t, name, gainTime, titleLevel, url, qid);
							map.put("picture", JsonUtil.getDownloadUrl(request) + "/" + url);
							map.put("id", jtId);
						}
					} else if (uploadType.equals("3")) {// 上传讲师身份证(正面)
						Teacher t = user.getTeacher();
						if (t != null) {
							String url = FileUtil.getShowUrl(id, ext);
							t.setFrontPicture(url);
							this.TeacherDAO.updateTeacher(t);
							map.put("picture", JsonUtil.getDownloadUrl(request) + "/" + url);
							map.put("id", t.getId());
						}
					} else if (uploadType.equals("4")) {// 上传讲师身份证(背面)
						Teacher t = user.getTeacher();
						if (t != null) {
							String url = FileUtil.getShowUrl(id, ext);
							t.setBackPicture(url);
							this.TeacherDAO.updateTeacher(t);
							map.put("picture", JsonUtil.getDownloadUrl(request) + "/" + url);
							map.put("id", t.getId());
						}
					} else if (uploadType.equals("5")) {// 上传讲师照片
						Teacher t = user.getTeacher();
						if (t != null) {
							String url = FileUtil.getShowUrl(id, ext);
							t.setPicture(url);
							this.TeacherDAO.updateTeacher(t);
							map.put("picture", JsonUtil.getDownloadUrl(request) + "/" + url);
							map.put("id", t.getId());
						}
					} else if (uploadType.equals("6")) {
						String url = FileUtil.getShowUrl(id, ext);
						map.put("picture", JsonUtil.getDownloadUrl(request) + "/" + url);
						map.put("id", id);
					}
				}
			}
			map.put("result", "1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	private String addTeacherjobTitle(Teacher t, String name, String gainTime, String titleLevel, String url,
			String id) {
		List<JobTitle> jts = t.getJobTitles();
		if (jts == null) {
			jts = new LinkedList<>();
		}
		JobTitle jt = new JobTitle();
		if (StringUtil.isNotEmpty(id)) {
			jt = jtdao.findById(id);
		}
		if (jt != null) {
			if (StringUtil.isNotEmpty(name)) {
				jt.setName(name);
			}
			if (StringUtil.isNotEmpty(gainTime)) {
				try {
					long l = Long.parseLong(gainTime);
					jt.setGainTime(new Date(l));
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("转换讲师职称获取时间失败！");
				}
			}
			jt.setPicture(url);
			jt.setTeacher(t);
			jts.add(jt);
			this.TeacherDAO.updateTeacher(t);
			return jt.getId();
		} else {
			return "";
		}
	}

	private String addTeacherCertificate(Teacher t, String name, String url, String id) {
		List<Certificate> cfs = t.getCertificate();
		if (cfs == null) {
			cfs = new LinkedList<>();
		}
		Certificate cf = new Certificate();
		if (StringUtil.isNotEmpty(id)) {
			cf = cfdao.findById(id);
		}
		if (cf != null) {
			if (StringUtil.isNotEmpty(name)) {
				cf.setName(name);
			}
			cf.setPicture(url);
			cf.setTeacher(t);
			cfs.add(cf);
			this.TeacherDAO.updateTeacher(t);
			return cf.getId();
		} else {
			return "";
		}
	}

	public Map<String, Object> updateTeacherInfo(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject json = this.getJSONObjectByRequest(request);
		map.put("result", "2");
		if (json.has("token")) {
			String token = json.getString("token");
			String uid = userTokenDAO.getIdByToken(token);
			if(uid==null){
				return map;
			}
			
			BbsUser user = bbsUserDAO.findBbsUser(uid);
			if(user!=null){
				Teacher t = user.getTeacher();
				if (t != null) {
					updateTeacherInfo(t, json);
					if (json.has("type")) {
						int type = json.getInt("type");
						int addOrDel = json.getInt("addOrDel");
						if (addOrDel == 1) {// 新增
							String id = addTeacherInfo(t, json, type);
							map.put("id", id);
						} else if (addOrDel == 0) {// 删除
							if (json.has("id")) {
								String id = json.getString("id");
								deleteTeacherInfo(type, id);
							}
							if (type == 5) {
								t.setFrontPicture(null);
							}
							if (type == 6) {
								t.setBackPicture(null);
							}
						} else if (addOrDel == 2) {// 修改
							if (json.has("id")) {
								String id = json.getString("id");
								updateTeacherInfo(type, id, json);
							}
						}
					}
					this.TeacherDAO.updateTeacher(t);
					map.put("result", "1");
				}
			}
		}
		return map;
	}

	private void updateTeacherInfo(int type, String id, JSONObject json) {
		if (type == 1) {// JobTitle
			updateTeacherJobTitle(id, json);
		} else if (type == 2) {// WorkExperience
			updateTeacherWorkExperience(id, json);
		} else if (type == 3) {// ProjectExperience
			updateTeacherProjectExperience(id, json);
		} else if (type == 4) {
			updateTeacherCertificate(id, json);
		}
	}

	private void updateTeacherCertificate(String id, JSONObject json) {
		CertificateDAO dao = SpringHelper.getSpringBean("CertificateDAO");
		Certificate cf = dao.findById(id);
		if (cf != null) {
			if (json.has("CFname")) {
				cf.setName(json.getString("CFname"));
			}
			dao.update(cf);
		}
	}

	private void updateTeacherInfo(Teacher t, JSONObject json) {
		if (json.has("teacher_name")) {
			String teacher_name = json.getString("teacher_name");
			t.setName(teacher_name);
		}
		if (json.has("qq")) {
			String qq = json.getString("qq");
			t.setQq(qq);
		}
		if (json.has("email")) {
			String email = json.getString("email");
			t.setEmail(email);
		}
		if (json.has("company")) {
			String company = json.getString("company");
			t.setCompany(company);
		}
		if (json.has("workYears")) {
			String workYears = json.getString("workYears");
			t.setWorkYears(workYears);
		}
		if (json.has("idNum")) {
			String idNum = json.getString("idNum");
			t.setIdNum(idNum);
		}
		if (json.has("introduction")) {
			String introduction = json.getString("introduction");
			t.setIntroduction(introduction);
		}
		if (json.has("gender")) {
			int gender = json.getInt("gender");
			t.setGender(gender);
		}
	}

	private String addTeacherInfo(Teacher t, JSONObject json, int type) {
		String id = "";
		if (type == 1) {// JobTitle
			id = addTeacherJobTitle(t, json);
		} else if (type == 2) {// WorkExperience
			id = addTeacherWorkExperience(t, json);
		} else if (type == 3) {// ProjectExperience
			id = addTeacherProjectExperience(t, json);
		}
		return id;
	}

	private void deleteTeacherInfo(int type, String id) {
		if (type == 1) {
			jtdao.delete(id);
		} else if (type == 2) {
			wedao.delete(id);
		} else if (type == 3) {
			pedao.delete(id);
		} else if (type == 4) {
			cfdao.delete(id);
		}
	}

	private String addTeacherProjectExperience(Teacher t, JSONObject json) {
		List<ProjectExperience> jts = t.getProjectExperience();
		if (jts == null) {
			jts = new LinkedList<>();
		}
		ProjectExperience jt = new ProjectExperience();
		jt.setTeacher(t);
		setProjectExperienceParameter(json, jt);
		jts.add(jt);
		return jt.getId();
	}

	private void updateTeacherProjectExperience(String id, JSONObject json) {
		ProjectExperienceDAO dao = SpringHelper.getSpringBean("ProjectExperienceDAO");
		ProjectExperience pe = dao.findById(id);
		if (pe != null) {
			setProjectExperienceParameter(json, pe);
			dao.update(pe);
		}
	}

	private void setProjectExperienceParameter(JSONObject json, ProjectExperience jt) {
		if (json.has("begin_time")) {
			long begin_time = json.getLong("begin_time");
			jt.setBeginTime(new Date(begin_time));
		}
		if (json.has("end_time")) {
			long end_time = json.getLong("end_time");
			jt.setEndTime(new Date(end_time));
		}
		if (json.has("PEname")) {
			String name = json.getString("PEname");
			jt.setName(name);
		}
	}

	private String addTeacherJobTitle(Teacher t, JSONObject json) {
		List<JobTitle> jts = t.getJobTitles();
		if (jts == null) {
			jts = new LinkedList<>();
		}
		JobTitle jt = new JobTitle();
		jt.setTeacher(t);
		setJobTitleParameter(json, jt);
		jts.add(jt);
		return jt.getId();
	}

	private void updateTeacherJobTitle(String id, JSONObject json) {
		JobTitleDAO dao = SpringHelper.getSpringBean("JobTitleDAO");
		JobTitle jt = dao.findById(id);
		if (jt != null) {
			setJobTitleParameter(json, jt);
			dao.update(jt);
		}
	}

	private void setJobTitleParameter(JSONObject json, JobTitle jt) {
		if (json.has("gain_time")) {
			long gain_time = json.getLong("gain_time");
			jt.setGainTime(new Date(gain_time));
		}
		if (json.has("title_level")) {
			String title_level = json.getString("title_level");
			jt.setTitleLevel(title_level);
		}
		if (json.has("JTname")) {
			String name = json.getString("JTname");
			jt.setName(name);
		}
	}

	private String addTeacherWorkExperience(Teacher t, JSONObject json) {
		List<WorkExperience> jts = t.getWorkExperience();
		if (jts == null) {
			jts = new LinkedList<>();
		}
		WorkExperience jt = new WorkExperience();
		jt.setTeacher(t);
		setWorkExperienceParameter(json, jt);
		jts.add(jt);
		return jt.getId();
	}

	private void updateTeacherWorkExperience(String id, JSONObject json) {
		WorkExperienceDAO dao = SpringHelper.getSpringBean("WorkExperienceDAO");
		WorkExperience we = dao.findById(id);
		if (we != null) {
			setWorkExperienceParameter(json, we);
			dao.update(we);
		}
	}

	private void setWorkExperienceParameter(JSONObject json, WorkExperience jt) {
		if (json.has("begin_time")) {
			long begin_time = json.getLong("begin_time");
			jt.setBeginTime(new Date(begin_time));
		}
		if (json.has("end_time")) {
			long end_time = json.getLong("end_time");
			jt.setEndTime(new Date(end_time));
		}
		if (json.has("duty")) {
			String duty = json.getString("duty");
			jt.setDuty(duty);
		}
		if (json.has("WEname")) {
			String name = json.getString("WEname");
			jt.setName(name);
		}
	}

	public Map<String, Object> addTeacher(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject json = this.getJSONObjectByRequest(request);
		map.put("result", "2");
		if (json.has("token")) {
			String token = json.getString("token");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				return map;
			}
			
			BbsUser user = bbsUserDAO.findBbsUser(id);
			if(user!=null){
				if(user.getTeacher()!=null){
					map.put("result", "3");
					return map;
				}
				Teacher t = new Teacher();
				t.setUser(user);
				String businessId = CookieUtils.getBusinessId(request);
				t.setBussinessId(businessId);
				user.setTeacher(t);
				this.updateTeacherInfo(t, json);
				this.TeacherDAO.addTeacher(t);
				this.bbsUserDAO.updateBbsUser(user);
				map.put("id", t.getId());
				map.put("result", "1");
			}
		}
		return map;
	}

	public Map<String, Object> getExercises(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject json = this.getJSONObjectByRequest(request);
		map.put("result", "2");
		if (json.has("knowledge_point_name")) {
			String knowledge_point_name = json.getString("knowledge_point_name");
			List<ExamModuleModel> ems = examModule2DAO.findExamModuleBySimilarName(knowledge_point_name);
			List<String> ids = new LinkedList<>();
			for (ExamModuleModel em : ems) {
				String id = em.getId();
				ids.add(id);
			}
			List<Map<String, Object>> list = new LinkedList<>();
			long num = 0;
			if (CollectionUtil.isNotEmpty(ids)) {
				GeneralQuestionDAO gqdao = SpringHelper.getSpringBean("GeneralQuestionDAO");
				int firstindex = 0, maxresult = 0;
				if (json.has("firstSize")) {
					firstindex = json.getInt("firstSize");
				}
				if (json.has("pageSize")) {
					maxresult = json.getInt("pageSize");
				}
				int type = 0;
				if (json.has("type")) {
					type = json.getInt("type");
				}
				Map<String, Object> map2 = gqdao.getGeneralQuestionsByModuleIds(ids, firstindex, maxresult, type);
				num = (Long) map2.get("num");
				@SuppressWarnings("unchecked")
				List<GeneralQuestion> gqs = (List<GeneralQuestion>) map2.get("list");
				for (GeneralQuestion gq : gqs) {
					if (gq != null) {
						Map<String, Object> gqmap = JsonUtil.questionToMap(gq);
						list.add(gqmap);
					}
				}
			}
			map.put("total_num", num);
			map.put("list", list);
			map.put("result", "1");
		}
		return map;
	}

	public Map<String, Object> getUserCoursesHistory(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject json = this.getJSONObjectByRequest(request);
		map.put("result", "2");
		if (json.has("token") && json.has("firstSize") && json.has("pageSize")) {
			String token = json.getString("token");
			int firstSize = json.getInt("firstSize");
			int pageSize = json.getInt("pageSize");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				return map;
			}
			
			BbsUser user = bbsUserDAO.findBbsUser(id);
			List<Map<String, Object>> list = new LinkedList<>();
			long num = 0;
			if (user != null) {
				Map<String, Object> resultMap = this.lessonTypeLogDAO.findLessonTypeLogsByUsr(user.getId(), firstSize,
						pageSize);
				@SuppressWarnings("unchecked")
				List<LessonTypeLog> ltls = (List<LessonTypeLog>) resultMap.get("list");
				List<LessonType> lts = new LinkedList<>();
				for (LessonTypeLog ltl : ltls) {
					if (ltl != null && ltl.getLessonType() != null) {
						lts.add(ltl.getLessonType());
					}
				}
				if (CollectionUtil.isNotEmpty(lts)) {
					list = this.lessonTypesToCourseList(lts, user, request);
				}
				num = (long) resultMap.get("num");
			}else{
				return map;
			}
			map.put("courseList", list);
			map.put("total_num", num);
			map.put("result", "1");
		} else {
			map.put("result", "3");
		}
		return map;
	}

	public Map<String, Object> saveSuggestion(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject json = this.getJSONObjectByRequest(request);
		map.put("result", "2");
		if (json.has("token")) {
			String token = json.getString("token");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				return map;
			}
			BbsUser user = bbsUserDAO.findBbsUser(id);
			if (user != null) {
				Suggestion s = new Suggestion();
				if (json.has("type")) {
					int type = json.getInt("type");
					s.setSuggestionType(type);
				}
				if (json.has("suggestion")) {
					String suggestion = json.getString("suggestion");
					s.setSuggestion(suggestion);
				}
				if (json.has("picId")) {
					String picId = json.getString("picId");
					String url = FileUtil.getShowUrl(picId, ".jpg");
					s.setPicture(url);
				}
				s.setUser(user);
				String businessId = CookieUtils.getBusinessId(request);
				s.setBusinessId(businessId);
				SuggestionDAO dao = SpringHelper.getSpringBean("SuggestionDAO");
				dao.add(s);
				map.put("result", "1");
			}
		}
		return map;
	}

	public Map<String, Object> getSuggestionList(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject json = this.getJSONObjectByRequest(request);
		map.put("result", "2");
		if (json.has("token")) {
			String token = json.getString("token");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				return map;
			}
			
			BbsUser user = bbsUserDAO.findBbsUser(id);
			if (user != null) {
				SuggestionDAO dao = SpringHelper.getSpringBean("SuggestionDAO");
				int firstindex=0,maxresult=0;
				if(json.has("firstindex")){
					firstindex = json.getInt("firstIndex");
				}
				if(json.has("maxresult")){
					maxresult = json.getInt("pageSize");
				}
				Map<String, Object> map2 = dao.getSuggestionsByUser(user.getId(), firstindex, maxresult);
				List<Map<String, Object>> list = new LinkedList<>();
				@SuppressWarnings("unchecked")
				List<Suggestion> slist = (List<Suggestion>) map2.get("list");
				if(CollectionUtil.isNotEmpty(slist)){
					for(Suggestion s : slist){
						Map<String, Object> map3 = new HashMap<>();
						map3.put("type", s.getSuggestionType());
						map3.put("suggestion", StringUtil.returnString(s.getSuggestion()));
						map3.put("picture", StringUtil.isEmpty(s.getPicture()) ? "" : JsonUtil.getDownloadUrl(request) + "/" + s.getPicture());
						map3.put("reply", StringUtil.returnString(s.getReply()));
						list.add(map3);
					}
				}
				map.put("list", list);
				map.put("num", map2.get("num"));
				map.put("result", "1");
			}
		}
		return map;
	}
	/**
	 * 
	 * 获取消息列表
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSystemMessage(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject json = this.getJSONObjectByRequest(request);
		int firstPage = 0;
		int pageNum = 0;
		map.put("result", "2");
		if (json.has("token")) {
			String token = json.getString("token");
			if (json.has("firstPage")) {
				firstPage = json.getInt("firstPage");
			}
			if (json.has("pageNum")) {
				pageNum = json.getInt("pageNum");
			}
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				map.put("result", "3");
				return map;
			}
			
			BbsUser user = bbsUserDAO.findBbsUser(id);
			if (user != null) {
				INoticeUserDAO noticeUserDAO = SpringHelper.getSpringBean("NoticeUserDAO");
				INoticeDAO noticeDAO = SpringHelper.getSpringBean("NoticeDAO");
				Map<String,String> orderbymap = new HashMap<String,String>();
				orderbymap.put("createDate", "desc");
				List<NoticeUser> nulist = noticeUserDAO.findByUserId(user.getId(), firstPage, pageNum, orderbymap);
				List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
				for(NoticeUser nu:nulist){
					NoticeModel notice = noticeDAO.findNotice(nu.getNoticeId());
					if(notice != null){
						Map<String,Object> temp = new HashMap<String,Object>();
						temp.put("date", DateUtil.formateDateToString(nu.getCreateDate()));
						temp.put("message_id",nu.getId());
						temp.put("connect_data", "");
						temp.put("content", notice.getContent());
						temp.put("state", nu.getIsReaded());
						temp.put("type", "1");
						temp.put("title", notice.getTitle());
						resultList.add(temp);
					}
				}
				map.put("message_list", resultList);
				map.put("total_num", resultList.size()+"");
				map.put("result", "1");
			}else{//无此用户
				map.put("result", "3");
			}
		}
		return map;
	}
	
	/**
	 * 
	 * 设置消息已读状态
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> setMessageState(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject json = this.getJSONObjectByRequest(request);
		String message_id = "";
		boolean status = false;
		map.put("result", "2");
		if (json.has("token")) {
			String token = json.getString("token");
			if (json.has("message_id")) {
				message_id = json.getString("message_id");
			}
			if (json.has("status")) {
				status = json.getBoolean("status");
			}
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				map.put("result", "3");
				return map;
			}
			
			BbsUser user = bbsUserDAO.findBbsUser(id);
			if (user != null) {
				INoticeUserDAO noticeUserDAO = SpringHelper.getSpringBean("NoticeUserDAO");
				NoticeUser nu = noticeUserDAO.findNoticeUser(message_id);
				if(nu != null){
					nu.setIsReaded(status);
					noticeUserDAO.updateNoticeUser(nu);
					map.put("result", "1");
				}else{//无此消息
					map.put("result", "4");
				}
			}else{//无此用户
				map.put("result", "3");
			}
		}
		return map;
	}
	
	private void modifyAllPassword(String password,String tel){
		
		List<BbsUser> userList = bbsUserDAO.findBbsUserByPhones(tel);
		for(BbsUser bbsUser:userList) {
			bbsUser.setPassword(password);
			bbsUserDAO.updateBbsUser(bbsUser);
		}
	}
	
	
	public static void main(String[] args) {
		String phone = "13627217665";
		String verify_code = "123456";
		SmsHelper smsHelper=new SmsHelper();
    	smsHelper.sendOneSms(phone, "您当前的验证码为:"+verify_code+"，请您在5分钟之内验证。", null);//发送短信
		sendMessageHelper.sendMessage("234971", phone, verify_code);
	}
	
	public Map<String,Object> getBusinessList(HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		JSONObject json = this.getJSONObjectByRequest(request);
		map.put("result", "2");
		if (json.has("token")) {
			String token = json.getString("token");
			String id = userTokenDAO.getIdByToken(token);
			if(id==null){
				return map;
			}
			
			BbsUser user = bbsUserDAO.findBbsUser(id);
			List<BbsUser> bulist = bbsUserDAO.findBbsUserByPhones(user.getTel());
			List<UserSys> userSyslist = new ArrayList<UserSys>();
			for(BbsUser bu : bulist) {
				UserSys userSys = new UserSys();
				BusinessUser businessUser = businessUserDAO.findBussinessUser(bu.getBusinessId());
				userSys.setSysId(businessUser.getId());
				userSys.setSysName(businessUser.getBusinessNameCn());
				userSys.setSysRootPath("http://"+businessUser.getDomainName()+":"+businessUser.getPort());
				userSys.setSysPic(businessUser.getSysPic());
				userSys.setSort(businessUser.getSort());
				userSyslist.add(userSys);
			}
			map.put("userSyslist", userSyslist);
			map.put("result", "1");
			return map;
		}
		return map;
	}
}