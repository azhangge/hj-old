package com.ucenter.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.ServletContextAware;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.huajie.app.util.DateUtil;
import com.huajie.app.util.FileUtil;
import com.huajie.app.util.HttpClientUtil;
import com.huajie.app.util.NetworkUtil;
import com.huajie.app.util.StringUtil;
import com.huajie.util.CookieUtils;
import com.huajie.util.DESTool;
import com.huajie.util.SpringHelper;
import com.jspsmart.upload.File;
import com.jspsmart.upload.SmartUpload;
import com.ucenter.json.BaseJson;
import com.ucenter.json.ChangePasswordJson;
import com.ucenter.json.ConfirmCodeJson;
import com.ucenter.json.FindUserListJson;
import com.ucenter.json.GetUserInfoJson;
import com.ucenter.json.ImportUserListJson;
import com.ucenter.json.LoginInUserJson;
import com.ucenter.json.UploadFileJson;
import com.ucenter.json.UploadUserPictureJson;
import com.ucenter.json.UserDetail;
import com.ucenter.json.UserInf;
import com.ucenter.json.UserSys;

@Controller
@RequestMapping("/api")
public class UcenterController implements ServletConfigAware,ServletContextAware{
	
	private ServletContext servletContext; 
	
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;  
	}

	private ServletConfig servletConfig; 
	
	public void setServletConfig(ServletConfig servletConfig) {
		this.servletConfig = servletConfig;  
	}
    
	/**
	 * 接收json类型数据
	 */
	public JSONObject receivedJson(HttpServletRequest request){
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader reader;
		JSONObject jsonObject = new JSONObject();
		try {
			reader = request.getReader();
			while((line = reader.readLine()) != null) {
	            sb.append(line);
	        }
			jsonObject = new JSONObject(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	/**
	 * 发送验证码
	 */
	@RequestMapping(value={"/sendCode.do"},method = RequestMethod.POST)
	@ResponseBody
	public BaseJson sendCode(HttpServletRequest request){
		JSONObject receivedJson = receivedJson(request);
		BaseJson sendJson = new BaseJson();
		String retCode = "0";
        String message = "用户中心异常";
		
        ISystemConfigDAO sysDAO = SpringHelper.getSpringBean("SystemConfigDAO");
        String uCenter=sysDAO.getSystemConfig().getYun_host();
		    
	    String result;
	    try {
			result = HttpClientUtil.httpPostWithJSON(uCenter+"/interface/sendCode",receivedJson);
			JSONObject myJson = new JSONObject(result.toString());
			if( !myJson.isNull("retCode") && !myJson.isNull("message")){
				retCode = myJson.getString("retCode");;
		        message = myJson.getString("message");
			}
	    } catch(Exception e){
	    	e.printStackTrace();
	    }
        sendJson.setRetCode(retCode);
        sendJson.setMessage(message);
		return sendJson;
	}
	
	/**
	 * 确认验证码
	 */
	@RequestMapping(value={"/confirmCode.do"},method = RequestMethod.POST)
	@ResponseBody
	public ConfirmCodeJson confirmCode(HttpServletRequest request){
		JSONObject receivedJson = receivedJson(request);
		ConfirmCodeJson sendJson = new ConfirmCodeJson();
		
		String retCode = "0";
        String message = "用户中心异常";
        String token = "";
        String user_id = "";
        String targetSysId = "";
        List<UserSys> userSyslist = new ArrayList<UserSys>();
        
        ISystemConfigDAO sysDAO = SpringHelper.getSpringBean("SystemConfigDAO");
        String uCenter=sysDAO.getSystemConfig().getYun_host();
		    
	    String result;
	    try {
			result = HttpClientUtil.httpPostWithJSON(uCenter+"/interface/confirmCode",receivedJson);
			JSONObject myJson = new JSONObject(result.toString());
			if( !myJson.isNull("retCode") && !myJson.isNull("message") ){
				retCode = myJson.getString("retCode");
		        message = myJson.getString("message");
		        sendJson.setRetCode(retCode);
		        sendJson.setMessage(message);
		        if( !myJson.isNull("token")){
		        	token = myJson.getString("token");
		        }
		        if( !myJson.isNull("targetSysId")){
		        	targetSysId = myJson.getString("targetSysId");
		        }
		        if(!myJson.isNull("userSyslist")){
		        	JSONArray jsonArray = myJson.getJSONArray("userSyslist");
		        	for(int i=0;i<jsonArray.length();i++){
		        		UserSys us = new UserSys();
		        		JSONObject jsonobject=(JSONObject) jsonArray.get(i);
		        		us.setSysId((String)jsonobject.get("sysId"));
		        		us.setSysName((String)jsonobject.get("sysName"));
		        		us.setSysRootPath((String)jsonobject.get("sysRootPath"));
		        		us.setSysPic((String)jsonobject.get("sysPic"));
		        		userSyslist.add(us);
		        	}
		        }
		        if(retCode.equals("1") && receivedJson.getString("type").equals("1") && !myJson.isNull("user_id")){
		        	user_id= myJson.getString("user_id");
		        }		        	
//		        	String phoneno = receivedJson.getString("phoneno");
//		        	BbsUser bbsUser = new BbsUser();
//		        	String ip = "";
//		    		try {
//		    			ip = NetworkUtil.getIpAddress(request);
//		    		} catch (IOException e) {
//		    			e.printStackTrace();
//		    		}
//		    		Date now=new Date();
//		    		if(bbsUser != null){
//		    			bbsUser.setId(user_id);
//			        	bbsUser.setTel(phoneno);
//			        	bbsUser.setRegIp(ip);
//			        	bbsUser.setRegTime(now);
//			        	bbsUser.setLastIp(ip);
//			        	bbsUser.setLastTime(now);
//			        	bbsUser.setGroupStr("");
//			        	bbsUser.setPwdEncoder("des");
//			        	bbsUser.setActivated(true);
//			        	bbsUser.setChecked(sysDAO.getSystemConfig().getAutoCheck());
//			        	bbsUser.setScore(1000);
//			        	IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
//			        	bbsUserDAO.addBbsUser2(bbsUser);
//		    		}
//		    		
//		        }
			}
	    } catch(Exception e){
	    	e.printStackTrace();
	    }
        if(StringUtil.isNotEmpty(token)){
        	sendJson.setToken(token);
        }
        if(StringUtil.isNotEmpty(user_id)){
        	sendJson.setUser_id(user_id);
        }
        if(StringUtil.isNotEmpty(targetSysId)){
        	sendJson.setTargetSysId(targetSysId);
        }
        if(userSyslist.size()>0){
	    	sendJson.setUserSyslist(userSyslist);
	    }
		return sendJson;
	}
	
	/**
	 * 校验手机号
	 */
	@RequestMapping(value={"/verifyPhoneno.do"},method = RequestMethod.POST)
	@ResponseBody
	public BaseJson verifyPhoneno(HttpServletRequest request){
		JSONObject receivedJson = receivedJson(request);
		BaseJson sendJson = new BaseJson();
		
		String retCode = "0";
        String message = "用户中心异常";
		
        ISystemConfigDAO sysDAO = SpringHelper.getSpringBean("SystemConfigDAO");
        String uCenter=sysDAO.getSystemConfig().getYun_host();
		    
	    String result;
	    try {
			result = HttpClientUtil.httpPostWithJSON(uCenter+"/interface/verifyPhoneno",receivedJson);
			JSONObject myJson = new JSONObject(result.toString());
			if( !myJson.isNull("retCode") && !myJson.isNull("message")){
				retCode = myJson.getString("retCode");
		        message = myJson.getString("message");
			}
	    } catch(Exception e){
	    	e.printStackTrace();
	    }
	    sendJson.setRetCode(retCode);
	    sendJson.setMessage(message);
		return sendJson;
	}
	
	/**
	 * 校验手机号2
	 */
	@RequestMapping(value={"/verifyPhoneno2.do"},method = RequestMethod.POST)
	@ResponseBody
	public BaseJson verifyPhoneno2(HttpServletRequest request){
		JSONObject receivedJson = receivedJson(request);
		BaseJson sendJson = new BaseJson();
		
		String retCode = "0";
        String message = "用户中心异常";
		
        ISystemConfigDAO sysDAO = SpringHelper.getSpringBean("SystemConfigDAO");
        String uCenter=sysDAO.getSystemConfig().getYun_host();
		    
	    String result;
	    try {
			result = HttpClientUtil.httpPostWithJSON(uCenter+"/interface/verifyPhoneno2",receivedJson);
			JSONObject myJson = new JSONObject(result.toString());
			if( !myJson.isNull("retCode") && !myJson.isNull("message")){
				retCode = myJson.getString("retCode");
		        message = myJson.getString("message");
			}
	    } catch(Exception e){
	    	e.printStackTrace();
	    }
	    sendJson.setRetCode(retCode);
	    sendJson.setMessage(message);
		return sendJson;
	}
	
	/**
	 * 普通登录
	 */
	@RequestMapping(value={"/loginInUser.do"},method = RequestMethod.POST)
	@ResponseBody
	public LoginInUserJson loginInUser(HttpServletRequest request){
		JSONObject receivedJson = receivedJson(request);
		LoginInUserJson sendJson = new LoginInUserJson();
		
		String retCode = "0";
        String message = "用户中心异常";
        String token = "";
        String user_id = "";
        String targetSysId = "";
        List<UserSys> userSyslist = new ArrayList<UserSys>();
		
        ISystemConfigDAO sysDAO = SpringHelper.getSpringBean("SystemConfigDAO");
        String uCenter=sysDAO.getSystemConfig().getYun_host();
		    
	    String result;
	    try {
			result = HttpClientUtil.httpPostWithJSON(uCenter+"/interface/loginInUser",receivedJson);
			JSONObject myJson = new JSONObject(result.toString());
			if( !myJson.isNull("retCode") && !myJson.isNull("message")){
				retCode = myJson.getString("retCode");
		        message = myJson.getString("message");
		        if(!myJson.isNull("token")){
		        	token = myJson.getString("token");
		        }
		        if(retCode.equals("1") && !myJson.isNull("user_id")){
		        	user_id = myJson.getString("user_id");
			        String ip= "";
		    		try {
		    			ip = NetworkUtil.getIpAddress(request);
		    		} catch (IOException e) {
		    			e.printStackTrace();
		    		}
	    			Date now=new Date();
		        	IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
		        	BbsUser bbsUser = bbsUserDAO.findBbsUser(user_id);
		        	if(bbsUser != null){
		        		bbsUser.setLastIp(ip);
		        		bbsUser.setLastTime(now);
		        		bbsUserDAO.updateBbsUser(bbsUser);
		        	}
		        }
		        if(!myJson.isNull("targetSysId")){
		        	targetSysId = myJson.getString("targetSysId");
		        }
		        if(!myJson.isNull("userSyslist")){
		        	JSONArray jsonArray = myJson.getJSONArray("userSyslist");
		        	for(int i=0;i<jsonArray.length();i++){
		        		UserSys us = new UserSys();
		        		JSONObject jsonobject=(JSONObject) jsonArray.get(i);
		        		us.setSysId((String)jsonobject.get("sysId"));
		        		us.setSysName((String)jsonobject.get("sysName"));
		        		us.setSysRootPath((String)jsonobject.get("sysRootPath"));
		        		us.setSysPic((String)jsonobject.get("sysPic"));
		        		userSyslist.add(us);
		        	}
		        }
			}
	    } catch(Exception e){
	    	e.printStackTrace();
	    }
	    sendJson.setRetCode(retCode);
	    sendJson.setMessage(message);
	    if(StringUtil.isNotEmpty(token)){
	    	sendJson.setToken(token);
	    }
	    if(StringUtil.isNotEmpty(user_id)){
	    	sendJson.setUser_id(user_id);
	    }
	    if(StringUtil.isNotEmpty(targetSysId)){
	    	sendJson.setTargetSysId(targetSysId);
	    }
	    if(userSyslist.size()>0){
	    	sendJson.setUserSyslist(userSyslist);
	    }
		return sendJson;
	}
	
	/**
	 * 设置密码
	 */
	@RequestMapping(value={"/setPassword.do"},method = RequestMethod.POST)
	@ResponseBody
	public BaseJson setPassword(HttpServletRequest request){
		JSONObject receivedJson = receivedJson(request);
		BaseJson sendJson = new BaseJson();
		
		String retCode = "0";
        String message = "用户中心异常";
		
        ISystemConfigDAO sysDAO = SpringHelper.getSpringBean("SystemConfigDAO");
        String uCenter=sysDAO.getSystemConfig().getYun_host();
		    
	    String result;
	    try {
			result = HttpClientUtil.httpPostWithJSON(uCenter+"/interface/setPassword",receivedJson);
			JSONObject myJson = new JSONObject(result.toString());
			if( !myJson.isNull("retCode") && !myJson.isNull("message")){
				retCode = myJson.getString("retCode");
		        message = myJson.getString("message");
			}
	    } catch(Exception e){
	    	e.printStackTrace();
	    }
	    sendJson.setRetCode(retCode);
	    sendJson.setMessage(message);
		return sendJson;
	}
	
	/**
	 * 修改密码
	 */
	@RequestMapping(value={"/changePassword.do"},method = RequestMethod.POST)
	@ResponseBody
	public ChangePasswordJson changePassword(HttpServletRequest request){
		JSONObject receivedJson = receivedJson(request);
		ChangePasswordJson sendJson = new ChangePasswordJson();
		
		String retCode = "0";
        String message = "用户中心异常";
		String token = "";
		String user_id = "";
		List<UserSys> userSyslist = new ArrayList<UserSys>();
        
        ISystemConfigDAO sysDAO = SpringHelper.getSpringBean("SystemConfigDAO");
        String uCenter=sysDAO.getSystemConfig().getYun_host();
		    
	    String result;
	    try {
			result = HttpClientUtil.httpPostWithJSON(uCenter+"/interface/changePassword",receivedJson);
			JSONObject myJson = new JSONObject(result.toString());
			if( !myJson.isNull("retCode") && !myJson.isNull("message")){
				retCode = myJson.getString("retCode");
		        message = myJson.getString("message");
		        if(!myJson.isNull("token")){
		        	token = myJson.getString("token");
		        }
		        if(!myJson.isNull("user_id")){
		        	user_id = myJson.getString("user_id");
		        }
		        if(!myJson.isNull("userSyslist")){
		        	JSONArray jsonArray = myJson.getJSONArray("userSyslist");
		        	for(int i=0;i<jsonArray.length();i++){
		        		UserSys us = new UserSys();
		        		JSONObject jsonobject=(JSONObject) jsonArray.get(i);
		        		us.setSysId((String)jsonobject.get("sysId"));
		        		us.setSysName((String)jsonobject.get("sysName"));
		        		us.setSysRootPath((String)jsonobject.get("sysRootPath"));
		        		userSyslist.add(us);
		        	}
		        }
			}
	    } catch(Exception e){
	    	e.printStackTrace();
	    }
	    sendJson.setRetCode(retCode);
	    sendJson.setMessage(message);
	    if(StringUtil.isNotEmpty(token)){
        	sendJson.setToken(token);
        }
        if(StringUtil.isNotEmpty(user_id)){
        	sendJson.setUser_id(user_id);
        }
        if(userSyslist.size()>0){
	    	sendJson.setUserSyslist(userSyslist);
	    }
		return sendJson;
	}
	
	/**
	 * 用户注销
	 */
	@RequestMapping(value={"/loginOutUser.do"},method = RequestMethod.POST)
	@ResponseBody
	public BaseJson loginOutUser(HttpServletRequest request){
		JSONObject receivedJson = receivedJson(request);
		BaseJson sendJson = new BaseJson();
		
		String retCode = "0";
        String message = "用户中心异常";
        
        ISystemConfigDAO sysDAO = SpringHelper.getSpringBean("SystemConfigDAO");
        String uCenter=sysDAO.getSystemConfig().getYun_host();
		    
	    String result;
	    try {
			result = HttpClientUtil.httpPostWithJSON(uCenter+"/interface/loginOutUser",receivedJson);
			JSONObject myJson = new JSONObject(result.toString());
			if( !myJson.isNull("retCode") && !myJson.isNull("message")){
				retCode = myJson.getString("retCode");
		        message = myJson.getString("message");
			}
	    } catch(Exception e){
	    	e.printStackTrace();
	    }
	    sendJson.setRetCode(retCode);
	    sendJson.setMessage(message);
		return sendJson;
	}
	
	/**
	 * 删除用户信息
	 */
	@RequestMapping(value={"/deleteUser.do"},method = RequestMethod.POST)
	@ResponseBody
	public BaseJson deleteUser(HttpServletRequest request){
		JSONObject receivedJson = receivedJson(request);
		BaseJson sendJson = new BaseJson();
		
		String retCode = "0";
        String message = "用户中心异常";
        
        ISystemConfigDAO sysDAO = SpringHelper.getSpringBean("SystemConfigDAO");
        String uCenter=sysDAO.getSystemConfig().getYun_host();
		    
	    String result;
	    try {
			result = HttpClientUtil.httpPostWithJSON(uCenter+"/interface/deleteUser",receivedJson);
			JSONObject myJson = new JSONObject(result.toString());
			if( !myJson.isNull("retCode") && !myJson.isNull("message")){
				retCode = myJson.getString("retCode");
		        message = myJson.getString("message");
		        if(retCode.equals("1") && !myJson.isNull("userId")){
		        	IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
		        	String userId = myJson.getString("userId");
		        	bbsUserDAO.deleteBbsUser(userId);
		        }
			}
	    } catch(Exception e){
	    	e.printStackTrace();
	    }
	    sendJson.setRetCode(retCode);
	    sendJson.setMessage(message);
		return sendJson;
	}
	
	/**
	 * 从其他子系统添加用户
	 */
	@RequestMapping(value={"/addUserFromOtherSys.do"},method = RequestMethod.POST)
	@ResponseBody
	public BaseJson addUserFromOtherSys(HttpServletRequest request){
		JSONObject receivedJson = receivedJson(request);
		String phoneNo = "";
		String sysId = "";
		String userId = "";
		String password = "";
		BaseJson sendJson = new BaseJson();
		sendJson.setRetCode("2");
		sendJson.setMessage("注册失败");
		ISystemConfigDAO sysDAO = SpringHelper.getSpringBean("SystemConfigDAO");
		if(!receivedJson.isNull("phoneNo") && !receivedJson.isNull("sysId") && !receivedJson.isNull("userId")){
			phoneNo = receivedJson.getString("phoneNo");
			sysId = receivedJson.getString("sysId");
			userId = receivedJson.getString("userId");
			if(receivedJson.isNull("password")){
				password = receivedJson.getString("password");
			}
			IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
			BbsUser bbsUser = bbsUserDAO.findBbsUserByPhone(phoneNo);
			if(bbsUser==null){
				bbsUser = new BbsUser();
			}else{
				sendJson.setRetCode("1");
	        	sendJson.setMessage("注册成功");
	        	return sendJson;
			}
        	String ip = "";
    		try {
    			ip = NetworkUtil.getIpAddress(request);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		Date now=new Date();
    		if(bbsUser != null){
    			if(StringUtil.isNotEmpty(userId)){
    				bbsUser.setId(userId);
    			}
    			if(StringUtil.isNotEmpty(phoneNo)){
    				bbsUser.setTel(phoneNo);
    			}
	        	bbsUser.setRegIp(ip);
	        	bbsUser.setRegTime(now);
	        	bbsUser.setLastIp(ip);
	        	bbsUser.setLastTime(now);
	        	bbsUser.setGroupStr("");
	        	bbsUser.setPwdEncoder("des");
	        	bbsUser.setActivated(true);
	        	bbsUser.setChecked(sysDAO.getSystemConfig().getAutoCheck());
	        	bbsUser.setScore(1000);
	        	bbsUserDAO.addBbsUser2(bbsUser);
	        	sendJson.setRetCode("1");
	        	sendJson.setMessage("注册成功");
    		}
		}
		return sendJson;
	}
	
	/**
	 * 添加用户信息
	 */
	@RequestMapping(value={"/addUser.do"},method = RequestMethod.POST)
	@ResponseBody
	public BaseJson addUser(HttpServletRequest request){
		JSONObject receivedJson = receivedJson(request);
		BaseJson sendJson = new BaseJson();
		
		String retCode = "0";
        String message = "用户中心异常";
        
        ISystemConfigDAO sysDAO = SpringHelper.getSpringBean("SystemConfigDAO");
        String uCenter = sysDAO.getSystemConfig().getYun_host();
		    
	    String result;
	    try {
			result = HttpClientUtil.httpPostWithJSON(uCenter+"/interface/addUser",receivedJson);
			JSONObject myJson = new JSONObject(result.toString());
			if( !myJson.isNull("retCode") && !myJson.isNull("message")){
				retCode = myJson.getString("retCode");
		        message = myJson.getString("message");
			}
	    } catch(Exception e){
	    	e.printStackTrace();
	    }
	    sendJson.setRetCode(retCode);
	    sendJson.setMessage(message);
		return sendJson;
	}
	
	/**
	 * 更新用户信息
	 */
	@RequestMapping(value={"/updateUserInfo.do"},method = RequestMethod.POST)
	@ResponseBody
	public BaseJson updateUserInfo(HttpServletRequest request){
		JSONObject receivedJson = receivedJson(request);
		BaseJson sendJson = new BaseJson();
		
		String retCode = "0";
        String message = "用户中心异常";
        
        ISystemConfigDAO sysDAO = SpringHelper.getSpringBean("SystemConfigDAO");
        String uCenter=sysDAO.getSystemConfig().getYun_host();
		    
	    String result;
	    try {
			result = HttpClientUtil.httpPostWithJSON(uCenter+"/interface/updateUserInfo",receivedJson);
			JSONObject myJson = new JSONObject(result.toString());
			if( !myJson.isNull("retCode") && !myJson.isNull("message")){
				retCode = myJson.getString("retCode");
		        message = myJson.getString("message");
			}
	    } catch(Exception e){
	    	e.printStackTrace();
	    }
	    sendJson.setRetCode(retCode);
	    sendJson.setMessage(message);
		return sendJson;
	}
	
	/**
	 * 更新用户
	 */
	@RequestMapping(value={"/getUserInfo.do"},method = RequestMethod.POST)
	@ResponseBody
	public GetUserInfoJson getUserInfo(HttpServletRequest request){
		JSONObject receivedJson = receivedJson(request);
		GetUserInfoJson sendJson = new GetUserInfoJson();
		
		String retCode = "0";
        String message = "用户中心异常";
        
         ISystemConfigDAO sysDAO = SpringHelper.getSpringBean("SystemConfigDAO");
        String uCenter=sysDAO.getSystemConfig().getYun_host();
		    
	    String result;
	    try {
			result = HttpClientUtil.httpPostWithJSON(uCenter+"/interface/getUserInfo",receivedJson);
			JSONObject myJson = new JSONObject(result.toString());
			if( !myJson.isNull("retCode") && !myJson.isNull("message")){
				retCode = myJson.getString("retCode");
		        message = myJson.getString("message");
		        if(!myJson.isNull("userInf")){
		        	JSONObject jsonObject = myJson.getJSONObject("userInf");
		        	UserInf userInf = new UserInf();
		        	IBbsUserDAO bbsUserDao = SpringHelper.getSpringBean("BbsUserDAO");
		        	BbsUser bbsUser = bbsUserDao.findBbsUser(jsonObject.getString("user_id"));
		        	userInf.setUser_id(jsonObject.getString("user_id"));
		        	userInf.setUser_phone(jsonObject.getString("phone"));
		        	if(StringUtil.isNotEmpty(jsonObject.getString("truename"))){
		        		userInf.setUser_name(jsonObject.getString("truename"));
		        	}
		        	if(StringUtil.isNotEmpty(jsonObject.getString("gender"))){
		        		userInf.setUser_sex(jsonObject.getString("gender"));
		        	}
		        	if(StringUtil.isNotEmpty(jsonObject.getString("email"))){
		        		userInf.setUser_email(jsonObject.getString("email"));
		        	}
		        	if(StringUtil.isNotEmpty(jsonObject.getString("birthday"))){
		        		userInf.setUser_birthday(jsonObject.getString("birthday"));
		        	}
		        	if(StringUtil.isNotEmpty(jsonObject.getString("qq"))){
		        		userInf.setUser_qq(jsonObject.getString("qq"));
		        	}
		        	if(StringUtil.isNotEmpty(jsonObject.getString("pid"))){
		        		userInf.setUser_pid(jsonObject.getString("pid"));
		        	}
		        	userInf.setUser_regtime(bbsUser.getRegTime()==null?"":DateUtil.formateDateToString2(bbsUser.getRegTime()));
		        	userInf.setUser_score(String.valueOf(bbsUser.getScore()));
		        	userInf.setUser_picture(bbsUser.getPicUrl()==null?"":bbsUser.getPicUrl());
		        	sendJson.setUserInf(userInf);
		        }
			}else{
				sendJson.setRetCode(retCode);
			    sendJson.setMessage(message);
			}
	    } catch(Exception e){
	    	e.printStackTrace();
	    }
	    sendJson.setRetCode(retCode);
	    sendJson.setMessage(message);
		return sendJson;
	}
	
	/**
	 * 上传文件
	 */
	@RequestMapping(value={"/uploadFile.do"},method = RequestMethod.POST)
	@ResponseBody
	public UploadFileJson uploadFile(HttpServletRequest request,HttpServletResponse response){
		UploadFileJson uploadUserPictureJson=new UploadFileJson();
		
		String retCode = "0";
        String message = "用户中心异常";
        
        String url = "";
        String file_id = "";
        String ext = "";
        
        SmartUpload smartUpload = new SmartUpload();
        try {
			smartUpload.initialize(this.servletConfig,request, response);
			smartUpload.upload();
			File smartFile = smartUpload.getFiles().getFile(0);
			if (!smartFile.isMissing()) {
				file_id = UUID.randomUUID().toString();
				ext = FileUtil.getExtByFileName(smartFile.getFileName());
				url = FileUtil.getDownloadFilePath(file_id, ext);
				smartFile.saveAs(url, smartUpload.SAVE_PHYSICAL);	
				uploadUserPictureJson.setUrl(url);
				uploadUserPictureJson.setFile_id(file_id);
				uploadUserPictureJson.setExt(ext);
				uploadUserPictureJson.setRetCode("1");
				uploadUserPictureJson.setMessage("上传成功");
			 }else{
				uploadUserPictureJson.setRetCode("2");
				uploadUserPictureJson.setMessage("没有获取到文件");
			 }
        } catch (Exception e) {
			e.printStackTrace();
		}
		return uploadUserPictureJson;
	}
	
	/**
	 * 设置用户图像
	 */
	@RequestMapping(value={"/setUserPicture.do"},method = RequestMethod.POST)
	@ResponseBody
	public UploadUserPictureJson setUserPicture(HttpServletRequest request){
		JSONObject receivedJson = receivedJson(request);
		UploadUserPictureJson uploadUserPictureJson=new UploadUserPictureJson();
		
		String retCode = "0";
        String message = "用户中心异常";

		String device = "";
		String token = "";
		String user_id = "";
		String file_id = "";
		String ext = "";
		boolean hasUser_id = false;
		
        ISystemConfigDAO sysDAO = SpringHelper.getSpringBean("SystemConfigDAO");
        String uCenter=sysDAO.getSystemConfig().getYun_host();
        
        String result;
		if(!receivedJson.isNull("device") && !receivedJson.isNull("file_id") && !receivedJson.isNull("ext")){
			device = receivedJson.getString("device");
			file_id = receivedJson.getString("file_id");
			ext = receivedJson.getString("ext");
			if(device.equals("1")){//1 手机
				if(!receivedJson.isNull("token")){
					token = receivedJson.getString("token");
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("token", token);
					try {
						result = HttpClientUtil.httpPostWithJSON(uCenter+"/interface/getUserIdByToken",jsonObject);
						JSONObject myJson = new JSONObject(result.toString());
						if( !myJson.isNull("retCode") && !myJson.isNull("message")){
							retCode = myJson.getString("retCode");
							message = myJson.getString("message");
							if(!myJson.isNull("user_id")){
								user_id = myJson.getString("user_id");
								hasUser_id = true;
							}else{
								uploadUserPictureJson.setRetCode(retCode);
								uploadUserPictureJson.setMessage(message);
							}
						}else{
							uploadUserPictureJson.setRetCode(retCode);
							uploadUserPictureJson.setMessage(message);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					uploadUserPictureJson.setRetCode("2");
					uploadUserPictureJson.setMessage("系统没有获取到正确的参数");
				}
			}else if(device.equals("2")){//2 PC
				if(!receivedJson.isNull("user_id")){
					user_id = receivedJson.getString("user_id");
					hasUser_id = true;
				}else{
					uploadUserPictureJson.setRetCode("2");
					uploadUserPictureJson.setMessage("系统没有获取到正确的参数");
				}
			}
		}else{
			uploadUserPictureJson.setRetCode("2");
			uploadUserPictureJson.setMessage("系统没有获取到正确的参数");
		}
		if(hasUser_id == true){
			IBbsUserDAO bbsUserDao = SpringHelper.getSpringBean("BbsUserDAO");
			BbsUser bu = bbsUserDao.findBbsUser(user_id);
			String businessId = CookieUtils.getBusinessId(request);
			bu.setPicUrl(FileUtil.getImageURLByIdAndBusinessId(file_id,businessId));
			bbsUserDao.updateBbsUser(bu);
			uploadUserPictureJson.setRetCode("1");
			uploadUserPictureJson.setMessage("更新成功");
		}else{
			uploadUserPictureJson.setRetCode("1");
			uploadUserPictureJson.setMessage("更新成功");
		}
		return uploadUserPictureJson;
	}
	
	/**
	 * 查询用户列表
	 */
	@RequestMapping(value={"/findUserList.do"},method = RequestMethod.POST)
	@ResponseBody
	public FindUserListJson findUserList(HttpServletRequest request){
		JSONObject receivedJson = receivedJson(request);
		FindUserListJson findUserListJson=new FindUserListJson();
		String retCode = "0";
        String message = "用户中心异常";
		List<UserDetail> userDetailList = new ArrayList<UserDetail>();
		ISystemConfigDAO sysDAO = SpringHelper.getSpringBean("SystemConfigDAO");
        String uCenter=sysDAO.getSystemConfig().getYun_host();
        String result;
		if(!receivedJson.isNull("sysId") && !receivedJson.isNull("ids")){
			String sysId = receivedJson.getString("sysId");
			JSONArray ids = receivedJson.getJSONArray("ids");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("sysId", sysId);
			jsonObject.put("ids", ids);
			try {
				result = HttpClientUtil.httpPostWithJSON(uCenter+"/interface/findUserList",jsonObject);
				JSONObject myJson = new JSONObject(result.toString());
				if( !myJson.isNull("retCode") && !myJson.isNull("message")){
					retCode = myJson.getString("retCode");
					message = myJson.getString("message");
					findUserListJson.setRetCode(retCode);
					findUserListJson.setMessage(message);
					if(!myJson.isNull("userDetailist")){
						JSONArray jsonArray = myJson.getJSONArray("userDetailist");
			        	for(int i=0;i<jsonArray.length();i++){
			        		UserDetail ud = new UserDetail();
			        		JSONObject jsonobject=(JSONObject) jsonArray.get(i);
			        		if(!jsonobject.isNull("id")){
			        			ud.setId(jsonobject.getString("id"));
			        		}
			        		if(!jsonobject.isNull("password")){
			        			ud.setPassword(jsonobject.getString("password"));
			        		}
			        		if(!jsonobject.isNull("phoneno")){
			        			ud.setPhoneno(jsonobject.getString("phoneno"));
			        		}
			        		if(!jsonobject.isNull("truename")){
			        			ud.setTruename(jsonobject.getString("truename"));
			        		}
			        		if(!jsonobject.isNull("gender")){
			        			ud.setGender(jsonobject.getString("gender"));
			        		}
			        		if(!jsonobject.isNull("email")){
			        			ud.setEmail(jsonobject.getString("email"));
			        		}
			        		if(!jsonobject.isNull("birthday")){
			        			ud.setBirthday(jsonobject.getString("birthday"));
			        		}
			        		if(!jsonobject.isNull("qq")){
			        			ud.setQq(jsonobject.getString("qq"));
			        		}
			        		if(!jsonobject.isNull("pid")){
			        			ud.setPid(jsonobject.getString("pid"));
			        		}
			        		userDetailList.add(ud);
			        	}
			        	findUserListJson.setUserDetailist(userDetailList);
					}
				}
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		return findUserListJson;
	}
	
	/**
	 * 导入用户列表
	 */
	@RequestMapping(value={"/importUserList.do"},method = RequestMethod.POST)
	@ResponseBody
	public BaseJson importUserList(HttpServletRequest request){
		JSONObject receivedJson = receivedJson(request);
		BaseJson baseJson = new BaseJson();
		String retCode = "0";
        String message = "用户中心异常";
		List<UserDetail> userDetailList = new ArrayList<UserDetail>();
		ISystemConfigDAO sysDAO = SpringHelper.getSpringBean("SystemConfigDAO");
        String uCenter=sysDAO.getSystemConfig().getYun_host();
        String result;
		if(!receivedJson.isNull("sysId") && !receivedJson.isNull("userDetailList")){
			String sysId = receivedJson.getString("sysId");
			JSONArray userDetailListJson = receivedJson.getJSONArray("userDetailList");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("sysId", sysId);
			jsonObject.put("userDetailList", userDetailListJson);
			try {
				result = HttpClientUtil.httpPostWithJSON(uCenter+"/interface/importUserList",jsonObject);
				JSONObject myJson = new JSONObject(result.toString());
				if( !myJson.isNull("retCode") && !myJson.isNull("message")){
					retCode = myJson.getString("retCode");
					message = myJson.getString("message");
//					if( !myJson.isNull("userDetailist")){
//						JSONArray jsonArray = myJson.getJSONArray("userDetailist");
//			        	for(int i=0;i<jsonArray.length();i++){
//			        		UserDetail ud = new UserDetail();
//			        		JSONObject jsonobject=(JSONObject) jsonArray.get(i);
//			        		BbsUser bbsUser = new BbsUser();
//			        		IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
//			        		if(!jsonobject.isNull("id")){
//			        			String id = jsonobject.getString("id");
//			        			bbsUser.setId(id);
//			        		}
//			        		if(!jsonobject.isNull("password")){
//			        			String password = jsonobject.getString("password");
//			        			DESTool dt = new DESTool();
//			        			bbsUser.setPassword(dt.encrypt(password));
//			        		}
//			        		if(!jsonobject.isNull("phoneno")){
//			        			String phoneno = jsonobject.getString("phoneno");
//			        			bbsUser.setTel(phoneno);
//			        		}
//			        		if(!jsonobject.isNull("truename")){
//			        			String truename = jsonobject.getString("truename");
//			        			bbsUser.setName(truename);
//			        		}
//			        		if(!jsonobject.isNull("gender")){
//			        			String gender = jsonobject.getString("gender");
//			        			bbsUser.setGender(gender);
//			        		}
//			        		if(!jsonobject.isNull("email")){
//			        			String email = jsonobject.getString("email");
//			        			bbsUser.setEmail(email);
//			        		}
//			        		if(!jsonobject.isNull("birthday")){
//			        			String birthday = jsonobject.getString("birthday");
//			        			bbsUser.setBirthDay(DateUtil.formateStringToDate2(birthday));
//			        		}
//			        		if(!jsonobject.isNull("qq")){
//			        			String qq = jsonobject.getString("qq");
//			        			bbsUser.setQq(qq);
//			        		}
//			        		if(!jsonobject.isNull("pid")){
//			        			String pid = jsonobject.getString("pid");
//			        			bbsUser.setPid(pid);
//			        		}
//			        		if(!jsonobject.isNull("score")){
//			        			String score = jsonobject.getString("score");
//			        			bbsUser.setScore(Long.valueOf(score));
//			        		}
//			        		if(!jsonobject.isNull("username")){
//			        			String username = jsonobject.getString("username");
//			        			bbsUser.setUsername(username);
//			        		}
//			        		bbsUserDAO.addBbsUser(bbsUser);
//			        	}
//					}
				}
	        	baseJson.setRetCode(retCode);
				baseJson.setMessage(message);
			} catch(Exception e){
					e.printStackTrace();
			}
		}
		return baseJson;
	}
	
	/**
	 * 从其他系统导入用户列表
	 */
	@RequestMapping(value={"/importUserListFromOtherSys.do"},method = RequestMethod.POST)
	@ResponseBody
	public BaseJson importUserListFromOtherSys(HttpServletRequest request){
		JSONObject receivedJson = receivedJson(request);
		BaseJson baseJson = new BaseJson();
		String retCode = "0";
        String message = "用户中心异常";
        String phoneno = "";
		if(!receivedJson.isNull("sysId") && !receivedJson.isNull("userDetailList")){
			String sysId = receivedJson.getString("sysId");
			JSONArray userDetailListJson = receivedJson.getJSONArray("userDetailList");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("sysId", sysId);
			jsonObject.put("userDetailList", userDetailListJson);
			
			if( !receivedJson.isNull("retCode") && !receivedJson.isNull("message")){
				retCode = receivedJson.getString("retCode");
				message = receivedJson.getString("message");
				if( userDetailListJson != null){
		        	for(int i=0;i<userDetailListJson.length();i++){
		        		BbsUser bbsUser =null;
		        		JSONObject jsonobject=(JSONObject) userDetailListJson.get(i);
		        		if(!jsonobject.isNull("phoneno")){
		        			phoneno = jsonobject.getString("phoneno");
		        		}
		        		IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
		        		BbsUser usr = bbsUserDAO.findBbsUserByPhone(phoneno);
		        		if(usr == null){
		        			bbsUser = new BbsUser();
		        			if(!jsonobject.isNull("phoneno")){
			        			bbsUser.setTel(phoneno);
			        		}
			        		if(!jsonobject.isNull("id")){
			        			String id = jsonobject.getString("id");
			        			bbsUser.setId(id);
			        		}
			        		if(!jsonobject.isNull("password")){
			        			String password = jsonobject.getString("password");
			        			DESTool dt = new DESTool();
			        			bbsUser.setPassword(dt.encrypt(password));
			        		}
			        		if(!jsonobject.isNull("truename")){
			        			String truename = jsonobject.getString("truename");
			        			bbsUser.setName(truename);
			        		}
			        		if(!jsonobject.isNull("gender")){
			        			String gender = jsonobject.getString("gender");
			        			bbsUser.setGender(gender);
			        		}
			        		if(!jsonobject.isNull("email")){
			        			String email = jsonobject.getString("email");
			        			bbsUser.setEmail(email);
			        		}
			        		if(!jsonobject.isNull("birthday")){
			        			String birthday = jsonobject.getString("birthday");
			        			bbsUser.setBirthDay(DateUtil.formateStringToDate2(birthday));
			        		}
			        		if(!jsonobject.isNull("qq")){
			        			String qq = jsonobject.getString("qq");
			        			bbsUser.setQq(qq);
			        		}
			        		if(!jsonobject.isNull("pid")){
			        			String pid = jsonobject.getString("pid");
			        			bbsUser.setPid(pid);
			        		}
			        		if(!jsonobject.isNull("score")){
			        			String score = jsonobject.getString("score");
			        			bbsUser.setScore(Long.valueOf(score));
			        		}
			        		if(!jsonobject.isNull("username")){
			        			String username = jsonobject.getString("username");
			        			bbsUser.setUsername(username);
			        		}
			        		bbsUserDAO.addBbsUser2(bbsUser);
		        		}
		        	}
				}
			}
        	baseJson.setRetCode(retCode);
			baseJson.setMessage(message);

		}
		return baseJson;
	}
	
	public static void main(String[] args) {
//		List<UserSys> userSyslist = new ArrayList<UserSys>();
//		System.out.println(userSyslist.size());
		String a="czfE7ts+kws=";
		DESTool dt=new DESTool();
		String b=dt.decrypt(a);
		System.out.println(b);
	}
	
	
}