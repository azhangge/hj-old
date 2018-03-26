package com.hjedu.customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.huajie.app.util.DateUtil;
import com.huajie.app.util.HttpClientUtil;
import com.huajie.app.util.StringUtil;
import com.huajie.util.SpringHelper;
import com.ucenter.json.UserInf;

public class UserUtil {
	public static BbsUser getBbsUser(String id){
	  	String retCode = "0";
        String message ="用户中心异常";
		BbsUser bbsUser = null;
        try {
            IBbsUserDAO BbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
            ISystemConfigDAO scDAO = SpringHelper.getSpringBean("SystemConfigDAO");
            String sysId = scDAO.getSystemConfig().getSub_id();
            String sysRootPath = scDAO.getSystemConfig().getSysRootPath();
            JSONObject jsonObject = new JSONObject();
    		jsonObject.put("sysId", sysId);
    		jsonObject.put("device", "2");
    		jsonObject.put("user_id", id);
			String result = HttpClientUtil.httpPostWithJSON(sysRootPath+"/api/getUserInfo.do",jsonObject);
			JSONObject myJson = new JSONObject(result.toString());
    		if( !myJson.isNull("retCode") && !myJson.isNull("message")){
				retCode = myJson.getString("retCode");
				message = myJson.getString("message");
				if(retCode.equals("1")){
					if(!myJson.isNull("userInf")){
						JSONObject uIjsonObject = myJson.getJSONObject("userInf");
						bbsUser = BbsUserDAO.findBbsUser(id);
			        	if(!uIjsonObject.isNull("user_name")){
			        		bbsUser.setName(uIjsonObject.getString("user_name"));
			        	}
			        	if(!uIjsonObject.isNull("user_sex")){
			        		bbsUser.setGender(uIjsonObject.getString("user_sex"));
			        	}
			        	if(!uIjsonObject.isNull("user_email")){
			        		bbsUser.setEmail(uIjsonObject.getString("user_email"));
			        	}
			        	if(!uIjsonObject.isNull("user_birthday")){
			        		bbsUser.setBirthDay(DateUtil.formateStringToDate2(uIjsonObject.getString("user_birthday")));
			        	}
			        	if(!uIjsonObject.isNull("user_qq")){
			        		bbsUser.setQq(uIjsonObject.getString("user_qq"));
			        	}
			        	if(!uIjsonObject.isNull("user_phone")){
			        		bbsUser.setTel(uIjsonObject.getString("user_phone"));
			        	}
			        	if(!uIjsonObject.isNull("user_score")){
			        		bbsUser.setScore(uIjsonObject.getInt("user_score"));
			        	}
			        	if(!uIjsonObject.isNull("user_picture")){
			        		bbsUser.setPicUrl(uIjsonObject.getString("user_picture"));
			        	}
			        	if(!uIjsonObject.isNull("user_pid")){
			        		bbsUser.setPid(uIjsonObject.getString("user_pid"));
			        	}
					}
				}
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bbsUser;
	}
	
	public static BbsUser getBbsUserByToken(String token){
	  	String retCode = "0";
        String message ="用户中心异常";
		BbsUser bbsUser = null;
		String user_id = null;
        try {
            IBbsUserDAO BbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
            ISystemConfigDAO scDAO = SpringHelper.getSpringBean("SystemConfigDAO");
            String sysId = scDAO.getSystemConfig().getSub_id();
            String sysRootPath = scDAO.getSystemConfig().getSysRootPath();
            JSONObject jsonObject = new JSONObject();
    		jsonObject.put("sysId", sysId);
    		jsonObject.put("device", "1");
    		jsonObject.put("token", token);
			String result = HttpClientUtil.httpPostWithJSON(sysRootPath+"/api/getUserInfo.do",jsonObject);
			JSONObject myJson = new JSONObject(result.toString());
    		if( !myJson.isNull("retCode") && !myJson.isNull("message")){
				retCode = myJson.getString("retCode");
				message = myJson.getString("message");
				if(retCode.equals("1")){
					if(!myJson.isNull("userInf")){
						JSONObject uIjsonObject = myJson.getJSONObject("userInf");
						if(!uIjsonObject.isNull("user_id")){
			        		user_id = (uIjsonObject.getString("user_id"));
			        		bbsUser = BbsUserDAO.findBbsUser(user_id);
			        		if(!uIjsonObject.isNull("user_name")){
				        		bbsUser.setName(uIjsonObject.getString("user_name"));
				        	}
				        	if(!uIjsonObject.isNull("user_sex")){
				        		bbsUser.setGender(uIjsonObject.getString("user_sex"));
				        	}
				        	if(!uIjsonObject.isNull("user_email")){
				        		bbsUser.setEmail(uIjsonObject.getString("user_email"));
				        	}
				        	if(!uIjsonObject.isNull("user_birthday")){
				        		bbsUser.setBirthDay(DateUtil.formateStringToDate2(uIjsonObject.getString("user_birthday")));
				        	}
				        	if(!uIjsonObject.isNull("user_qq")){
				        		bbsUser.setQq(uIjsonObject.getString("user_qq"));
				        	}
				        	if(!uIjsonObject.isNull("user_phone")){
				        		bbsUser.setTel(uIjsonObject.getString("user_phone"));
				        	}
				        	if(!uIjsonObject.isNull("user_score")){
				        		bbsUser.setScore(uIjsonObject.getInt("user_score"));
				        	}
				        	if(!uIjsonObject.isNull("user_picture")){
				        		bbsUser.setPicUrl(uIjsonObject.getString("user_picture"));
				        	}
				        	if(!uIjsonObject.isNull("user_pid")){
				        		bbsUser.setPid(uIjsonObject.getString("user_pid"));
				        	}
			        	}
					}
				}
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bbsUser;
	}
	
	public static List<BbsUser> findBbsUserList(List<BbsUser> datalist){
		String[] ids = new String[datalist.size()];
		for(int i = 0;i<datalist.size();i++){
			ids[i] = datalist.get(i).getId();
		}
		String retCode = "0";
        String message = "用户中心异常";
		String result = null;
	    ISystemConfigDAO sysDAO = SpringHelper.getSpringBean("SystemConfigDAO");
	    String sysId = sysDAO.getSystemConfig().getSub_id();
	    String uCenter = sysDAO.getSystemConfig().getYun_host();
	    String sysRootPath = sysDAO.getSystemConfig().getSysRootPath();
		List<BbsUser> bbsUserlist = new ArrayList<BbsUser>();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("sysId", sysId);
		jsonObject.put("ids", ids);
		try {
			result = HttpClientUtil.httpPostWithJSON(sysRootPath+"/api/findUserList.do",jsonObject);
			JSONObject myJson = new JSONObject(result.toString());
			if( !myJson.isNull("retCode") && !myJson.isNull("message")){
				retCode = myJson.getString("retCode");
				message = myJson.getString("message");
				if(retCode.equals("1")){
					if(!myJson.isNull("userDetailist")){
						JSONArray jsonArray = myJson.getJSONArray("userDetailist");
						for(int i=0;i<jsonArray.length();i++){
							JSONObject jsonobject=(JSONObject) jsonArray.get(i);
							for(BbsUser bu:datalist){
								if(!jsonobject.isNull("id")){
									String id=jsonobject.getString("id");
				        			if(bu.getId().equals(id)){
				        				if(!jsonobject.isNull("phoneno")){
						        			bu.setTel(jsonobject.getString("phoneno"));
						        		}
				        				if(!jsonobject.isNull("password")){
				        					bu.setPassword(jsonobject.getString("password"));
				        				}
				        				if(!jsonobject.isNull("truename")){
						        			bu.setName(jsonobject.getString("truename"));
						        		}
				        				if(!jsonobject.isNull("gender")){
						        			bu.setGender(jsonobject.getString("gender"));
						        		}
						        		if(!jsonobject.isNull("email")){
						        			bu.setEmail(jsonobject.getString("email"));
						        		}
						        		if(!jsonobject.isNull("birthday")){
						        			if(StringUtil.isNotEmpty(jsonobject.getString("birthday"))){
						        				bu.setBirthDay(DateUtil.formateStringToDate2(jsonobject.getString("birthday")));
						        			}
						        		}
						        		if(!jsonobject.isNull("qq")){
						        			bu.setQq(jsonobject.getString("qq"));
						        		}
						        		if(!jsonobject.isNull("pid")){
						        			bu.setPid(jsonobject.getString("pid"));
						        		}
						        		bbsUserlist.add(bu);
				        			}
				        		}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bbsUserlist;
	}
	
	
	public static Map<String,Object> setPassword(String id,String password){
		String retCode = "0";
        String message ="用户中心异常";
        Map<String,Object> map =  new HashMap<String,Object>();
        ISystemConfigDAO scDAO = SpringHelper.getSpringBean("SystemConfigDAO");
        String sysId = scDAO.getSystemConfig().getSub_id();
        String sysRootPath = scDAO.getSystemConfig().getSysRootPath();
        JSONObject jsonObject = new JSONObject();
		jsonObject.put("sysId", sysId);
		jsonObject.put("device", "2");
		jsonObject.put("user_id", id);
		jsonObject.put("password", password);
		jsonObject.put("type", "1");
        try {
			String result = HttpClientUtil.httpPostWithJSON(sysRootPath+"/api/setPassword.do",jsonObject);
			JSONObject myJson = new JSONObject(result.toString());
    		if( !myJson.isNull("retCode") && !myJson.isNull("message")){
				retCode = myJson.getString("retCode");
				message = myJson.getString("message");
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
        map.put("retCode", retCode);
        map.put("message", message);
		return map;
	}
	
	public static Map<String,Object> changePassword(String id,String oldpassword,String password){
		String retCode = "0";
        String message ="用户中心异常";
        Map<String,Object> map =  new HashMap<String,Object>();
        ISystemConfigDAO scDAO = SpringHelper.getSpringBean("SystemConfigDAO");
        String sysId = scDAO.getSystemConfig().getSub_id();
        String sysRootPath = scDAO.getSystemConfig().getSysRootPath();
        JSONObject jsonObject = new JSONObject();
		jsonObject.put("user_id", id);
		jsonObject.put("device", "2");
		jsonObject.put("oldpassword", oldpassword);
		jsonObject.put("password", password);
		try {
			String result = HttpClientUtil.httpPostWithJSON(sysRootPath+"/api/changePassword.do",jsonObject);
			JSONObject myJson = new JSONObject(result.toString());
			if( !myJson.isNull("retCode") && !myJson.isNull("message")){
				retCode = myJson.getString("retCode");
				message = myJson.getString("message");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("retCode", retCode);
	    map.put("message", message);
	    return map;
	}
	
	public static Map<String,Object> addUser(BbsUser bbsUser){
		String retCode = "0";
		String message ="用户中心异常";
		Map<String,Object> map =  new HashMap<String,Object>();
		ISystemConfigDAO scDAO = SpringHelper.getSpringBean("SystemConfigDAO");
		String sysId = scDAO.getSystemConfig().getSub_id();
        String sysRootPath = scDAO.getSystemConfig().getSysRootPath();
        JSONObject jsonObject = new JSONObject();
		jsonObject.put("sysId", sysId);
		jsonObject.put("device", "2");
		jsonObject.put("user_id", bbsUser.getId());
		jsonObject.put("phoneno", bbsUser.getTel().trim());
		JSONObject userInfJson = new JSONObject();
		
		if(StringUtil.isNotEmpty(bbsUser.getId())){
			userInfJson.put("user_id", bbsUser.getId().trim());
		}
		if(StringUtil.isNotEmpty(bbsUser.getPassword())){
			userInfJson.put("user_password", bbsUser.getPassword().trim());
		}
		if(StringUtil.isNotEmpty(bbsUser.getTel())){
			userInfJson.put("user_phone", bbsUser.getTel().trim());
		}
		if(StringUtil.isNotEmpty(bbsUser.getName())){
			userInfJson.put("user_name", bbsUser.getName());
		}
		if(StringUtil.isNotEmpty(bbsUser.getGender())){
			userInfJson.put("user_sex", bbsUser.getGender());
		}
		if(StringUtil.isNotEmpty(bbsUser.getEmail())){
			userInfJson.put("user_email", bbsUser.getEmail());
		}
		if(bbsUser.getBirthDay()!=null){
			userInfJson.put("user_birthday", DateUtil.formateDateToString2(bbsUser.getBirthDay()));
		}
		if(StringUtil.isNotEmpty(bbsUser.getQq())){
			userInfJson.put("user_qq", bbsUser.getQq());
		}
		if(StringUtil.isNotEmpty(bbsUser.getPid())){
			userInfJson.put("user_pid", bbsUser.getPid());
		}
		jsonObject.put("userInf", userInfJson);
        try {
			String result = HttpClientUtil.httpPostWithJSON(sysRootPath+"/api/addUser.do",jsonObject);
			JSONObject myJson = new JSONObject(result.toString());
    		if( !myJson.isNull("retCode") && !myJson.isNull("message")){
				retCode = myJson.getString("retCode");
				message = myJson.getString("message");
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
        map.put("retCode", retCode);
	    map.put("message", message);
	    return map;
	}
	
	public static Map<String,Object> deleteUser(String id){
		String retCode = "0";
		String message ="用户中心异常";
		Map<String,Object> map =  new HashMap<String,Object>();
		ISystemConfigDAO scDAO = SpringHelper.getSpringBean("SystemConfigDAO");
		String sysId = scDAO.getSystemConfig().getSub_id();
        String sysRootPath = scDAO.getSystemConfig().getSysRootPath();
        JSONObject jsonObject = new JSONObject();
		jsonObject.put("sysId", sysId);
		jsonObject.put("device", "1");
		jsonObject.put("userId", id);
        try {
			String result = HttpClientUtil.httpPostWithJSON(sysRootPath+"/api/deleteUser.do",jsonObject);
			JSONObject myJson = new JSONObject(result.toString());
    		if( !myJson.isNull("retCode") && !myJson.isNull("message")){
				retCode = myJson.getString("retCode");
				message = myJson.getString("message");
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
        map.put("retCode", retCode);
	    map.put("message", message);
	    return map;
	}
	
	public static Map<String,Object> updateUserInfo(String id,BbsUser bbsUser){
		String retCode = "0";
		String message ="用户中心异常";
		Map<String,Object> map =  new HashMap<String,Object>();
		ISystemConfigDAO scDAO = SpringHelper.getSpringBean("SystemConfigDAO");
		String sysId = scDAO.getSystemConfig().getSub_id();
        String sysRootPath = scDAO.getSystemConfig().getSysRootPath();
        JSONObject jsonObject = new JSONObject();
		jsonObject.put("sysId", sysId);
		jsonObject.put("device", "2");
		jsonObject.put("user_id", id);
		JSONObject userInfJson = new JSONObject();
		if(StringUtil.isNotEmpty(bbsUser.getName())){
			userInfJson.put("user_name", bbsUser.getName());
		}
		if(StringUtil.isNotEmpty(bbsUser.getGender())){
			userInfJson.put("user_sex", bbsUser.getGender());
		}
		if(StringUtil.isNotEmpty(bbsUser.getEmail())){
			userInfJson.put("user_email", bbsUser.getEmail());
		}
		if(bbsUser.getBirthDay()!=null){
			userInfJson.put("user_birthday", DateUtil.formateDateToString2(bbsUser.getBirthDay()));
		}
		if(StringUtil.isNotEmpty(bbsUser.getQq())){
			userInfJson.put("user_qq", bbsUser.getQq());
		}
		if(StringUtil.isNotEmpty(bbsUser.getPid())){
			userInfJson.put("user_pid", bbsUser.getPid());
		}
		jsonObject.put("userInf", userInfJson);
        try {
			String result = HttpClientUtil.httpPostWithJSON(sysRootPath+"/api/updateUserInfo.do",jsonObject);
			JSONObject myJson = new JSONObject(result.toString());
    		if( !myJson.isNull("retCode") && !myJson.isNull("message")){
				retCode = myJson.getString("retCode");
				message = myJson.getString("message");
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
        map.put("retCode", retCode);
	    map.put("message", message);
	    return map;
	}
	
	public static Map<String,Object> sendCode(String phoneno,String type){
		String retCode = "0";
        String message ="用户中心异常";
        Map<String,Object> map =  new HashMap<String,Object>();
        ISystemConfigDAO scDAO = SpringHelper.getSpringBean("SystemConfigDAO");
		String sysId = scDAO.getSystemConfig().getSub_id();
        String sysRootPath = scDAO.getSystemConfig().getSysRootPath();
        JSONObject jsonObject = new JSONObject();
		jsonObject.put("phoneno", phoneno);
		jsonObject.put("sysId", sysId);
		jsonObject.put("type", type);
		jsonObject.put("device", "2");
		try {
			String result = HttpClientUtil.httpPostWithJSON(sysRootPath+"/api/sendCode.do",jsonObject);
			JSONObject myJson = new JSONObject(result.toString());
			if( !myJson.isNull("retCode") && !myJson.isNull("message")){
				retCode = myJson.getString("retCode");
				message = myJson.getString("message");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("retCode", retCode);
	    map.put("message", message);
	    return map;
	}
	
	public static Map<String,Object> verifyPhoneno(String phoneno){
		String retCode = "0";
        String message ="用户中心异常";
        Map<String,Object> map =  new HashMap<String,Object>();
        ISystemConfigDAO scDAO = SpringHelper.getSpringBean("SystemConfigDAO");
		String sysId = scDAO.getSystemConfig().getSub_id();
        String sysRootPath = scDAO.getSystemConfig().getSysRootPath();
        JSONObject jsonObject = new JSONObject();
		jsonObject.put("phoneno", phoneno);
		jsonObject.put("sysId", sysId);
		try {
			String result = HttpClientUtil.httpPostWithJSON(sysRootPath+"/api/verifyPhoneno.do",jsonObject);
			JSONObject myJson = new JSONObject(result.toString());
			if( !myJson.isNull("retCode") && !myJson.isNull("message")){
				retCode = myJson.getString("retCode");
				message = myJson.getString("message");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("retCode", retCode);
	    map.put("message", message);
	    return map;
	}
	
	public static Map<String,Object> confirmCode(String phoneno,String code,String type,String password){
		String retCode = "0";
        String message ="用户中心异常";
        String userId = "";
        Map<String,Object> map =  new HashMap<String,Object>();
        ISystemConfigDAO scDAO = SpringHelper.getSpringBean("SystemConfigDAO");
		String sysId = scDAO.getSystemConfig().getSub_id();
        String sysRootPath = scDAO.getSystemConfig().getSysRootPath();
        JSONObject jsonObject = new JSONObject();
		jsonObject.put("phoneno", phoneno);
		jsonObject.put("code", code);
		jsonObject.put("type", type);
		if(password != null){
			jsonObject.put("password", password);
		}
		jsonObject.put("sysId", sysId);
		jsonObject.put("device", "2");
		try {
			String result = HttpClientUtil.httpPostWithJSON(sysRootPath+"/api/confirmCode.do",jsonObject);
			JSONObject myJson = new JSONObject(result.toString());
			if( !myJson.isNull("retCode") && !myJson.isNull("message")){
				retCode = myJson.getString("retCode");
				message = myJson.getString("message");
				if(!myJson.isNull("user_id")){
					userId = myJson.getString("user_id");
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("retCode", retCode);
	    map.put("message", message);
	    if(StringUtil.isNotEmpty(userId)){
	    	map.put("userId", userId);
	    }
	    return map;
	}
	
	public static Map<String,Object> importUserList(JSONArray jsonArray){
        String retCode = "0";
        String message = "用户中心异常";
        Map<String,Object> map =  new HashMap<String,Object>();
        String result;
        ISystemConfigDAO scDAO = SpringHelper.getSpringBean("SystemConfigDAO");
		String sysId = scDAO.getSystemConfig().getSub_id();
        String sysRootPath = scDAO.getSystemConfig().getSysRootPath();
        JSONObject jsonObject = new JSONObject();
		jsonObject.put("sysId", sysId);
		jsonObject.put("userDetailList", jsonArray);
		try {
			result = HttpClientUtil.httpPostWithJSON(sysRootPath+"/api/importUserList.do",jsonObject);
			JSONObject myJson = new JSONObject(result.toString());
			if( !myJson.isNull("retCode") && !myJson.isNull("message")){
				retCode = myJson.getString("retCode");
				message = myJson.getString("message");
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		map.put("retCode", retCode);
	    map.put("message", message);
		return map;
	}
	
}
