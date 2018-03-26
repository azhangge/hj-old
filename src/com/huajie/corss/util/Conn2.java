package com.huajie.corss.util;

import java.util.HashMap;
import java.util.Map;

import org.primefaces.json.JSONObject;

import com.huajie.app.util.HttpClientUtil;
import com.huajie.app.util.StringUtil;

public class Conn2 {
	/**
	 * 注册子系统
	 */
	public static JSONObject registerSubSystem(String uCenter,String sysName,String sysRootPath){
		JSONObject json = new JSONObject();  
        json.put("sysName", sysName);
        json.put("sysRootPath", sysRootPath);
        String result;
        Map<String,Object> map=new HashMap<String,Object>();
		try {
			result = HttpClientUtil.httpPostWithJSON(uCenter+"/interface/registerSubSystem",json);
			JSONObject myJson = new JSONObject(result.toString());
			if( !myJson.isNull("retCode") && !myJson.isNull("message")){
				return  myJson;
			}else{
				map.put("retCode", "2");
				map.put("message", "系统没有获取到正确的参数");
				return  new JSONObject(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("retCode", "0");
			map.put("message", "用户中心异常");
			return  new JSONObject(map);
		}
	}

	/**
	 * 发送短信验证码
	 */
	public static JSONObject sendCode(String uCenter,String phoneno,String sysId,String type,String device){
		JSONObject json = new JSONObject();  
        json.put("phoneno", phoneno);
        json.put("sysId", sysId);
        json.put("type",type);
        json.put("device",device);
        String result;
        Map<String,Object> map=new HashMap<String,Object>();
		try {
			result = HttpClientUtil.httpPostWithJSON(uCenter+"/interface/sendCode",json);
			JSONObject myJson = new JSONObject(result.toString());
			if( !myJson.isNull("retCode") && !myJson.isNull("message")){
				return  myJson;
			}else{
				map.put("retCode", "0");
				map.put("message", "用户中心异常");
				return  new JSONObject(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("retCode", "0");
			map.put("message", "用户中心异常");
			return  new JSONObject(map);
		}
	}
	
	/**
	 * 确认验证码
	 */
	public static JSONObject confirmCode(String uCenter,String phoneno,String password,String code,String type,String sysId,String device){
		JSONObject json = new JSONObject();  
        json.put("phoneno", phoneno);
        json.put("code", code);
        json.put("password", password);
        json.put("sysId", sysId);
        json.put("type",type);
        json.put("device",device);
        String result;
        Map<String,Object> map=new HashMap<String,Object>();
		try {
			result = HttpClientUtil.httpPostWithJSON(uCenter+"/interface/confirmCode",json);
			JSONObject myJson = new JSONObject(result.toString());
			if( !myJson.isNull("retCode") && !myJson.isNull("message")){
				return  myJson;
			}else{
				map.put("retCode", "0");
				map.put("message", "用户中心异常");
				return  new JSONObject(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("retCode", "0");
			map.put("message", "用户中心异常");
			return  new JSONObject(map);
		}
	}
	
	/**
	 * 校验用户数据
	 */
	public static JSONObject verifyPhoneno(String uCenter,String phoneno,String sysId,String device){
		JSONObject json = new JSONObject();  
        json.put("phoneno", phoneno);
        json.put("sysId", sysId);
        json.put("device",device);
        String result;
        Map<String,Object> map=new HashMap<String,Object>();
		try {
			result = HttpClientUtil.httpPostWithJSON(uCenter+"/interface/verifyPhoneno",json);
			JSONObject myJson = new JSONObject(result.toString());
			if( !myJson.isNull("retCode") && !myJson.isNull("message")){
				return  myJson;
			}else{
				map.put("retCode", "0");
				map.put("message", "用户中心异常");
				return  new JSONObject(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("retCode", "0");
			map.put("message", "用户中心异常");
			return  new JSONObject(map);
		}
	}
	
	/**
	 * 登录用户
	 */
	public static JSONObject loginUser(String uCenter,String phoneno,String password,String sysId,String isSwap,String device){
		JSONObject json = new JSONObject();  
        json.put("phoneno", phoneno);
        json.put("password", password);
        if(StringUtil.isNotEmpty(isSwap)){
        	json.put("isSwap", isSwap);
        }
        json.put("sysId", sysId);
        json.put("device",device);
        String result;
        Map<String,Object> map=new HashMap<String,Object>();
		try {
			result = HttpClientUtil.httpPostWithJSON(uCenter+"/interface/loginUser",json);
			JSONObject myJson = new JSONObject(result.toString());
			if( !myJson.isNull("retCode") && !myJson.isNull("message")){
				return  myJson;
			}else{
				map.put("retCode", "0");
				map.put("message", "用户中心异常");
				return  new JSONObject(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("retCode", "0");
			map.put("message", "用户中心异常");
			return  new JSONObject(map);
		}
	}
	
	public static void main(String[] args) {
		
	}
}
