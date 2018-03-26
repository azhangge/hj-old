package com.huajie.corss.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.primefaces.json.JSONObject;

import com.huajie.app.util.HttpClientUtil;
import com.huajie.util.DESTool;

public class Conn {
	/**
	 * 发送短信验证码PC
	 */
	public static JSONObject sendCodePC(String yun_host,String tel){
		JSONObject json = new JSONObject();  
        json.put("tel", tel);
        String result;
        Map<String,Object> map=new HashMap<String,Object>();
		try {
			result = HttpClientUtil.httpPostWithJSON(yun_host+"/corss/sendCode",json);
			JSONObject myJsonObject = new JSONObject(result.toString());
			if( !myJsonObject.isNull("status")){
				//根据不同的状态码处理业务
				map.put("status", myJsonObject.getString("status").trim());
				return  new JSONObject(map);
			}else{
				map.put("status", "2");
				//系统没有获取到正确的参数
				return  new JSONObject(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", "0");
			//云服务器接口不通
			return  new JSONObject(map);
		}
	}
	
	/**
	 * 确认短信验证码PC
	 */
	public static JSONObject confirmCodePC(String yun_host,String tel,String code){
		JSONObject json = new JSONObject();  
        json.put("tel", tel);
        json.put("code", code);
        String result;
        Map<String,Object> map=new HashMap<String,Object>();
		try {
			result = HttpClientUtil.httpPostWithJSON(yun_host+"/corss/confirmCode",json);
			JSONObject myJsonObject = new JSONObject(result.toString());
			if(!myJsonObject.isNull("status")){
				//根据不同的状态码处理业务
				map.put("status", myJsonObject.getString("status").trim());
				return  new JSONObject(map);
			}else{
				map.put("status", "2");
				//系统没有获取到正确的参数
				return  new JSONObject(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", "0");
			//云服务器接口不通
			return  new JSONObject(map);
		}
	}

	/**
	 * 校验用户通过手机号
	 */
	public static JSONObject verifySubUserByTel(String yun_host,String sub_id,String tel){
		JSONObject json = new JSONObject();  
        json.put("sub_id", sub_id);
        json.put("tel", tel);
        String result;
        Map<String,Object> map=new HashMap<String,Object>();
		try {
			result = HttpClientUtil.httpPostWithJSON(yun_host+"/corss/verifySubUserByTel",json);
			JSONObject myJsonObject = new JSONObject(result.toString());
			if(!myJsonObject.isNull("status")){
				//根据不同的状态码处理业务
				map.put("status", myJsonObject.getString("status").trim());
				return  new JSONObject(map);
			}else{
				map.put("status", "2");
				//系统没有获取到正确的参数
				return  new JSONObject(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", "0");
			//云服务器接口不通
			return  new JSONObject(map);
		}
	}
	
	/**
	 * 校验用户通过手机号管理员调用
	 */
	public static JSONObject verifySubUserByTelAdmin(String yun_host,String sub_id,String tel){
		JSONObject json = new JSONObject();  
        json.put("sub_id", sub_id);
        json.put("tel", tel);
        String result;
        Map<String,Object> map=new HashMap<String,Object>();
		try {
			result = HttpClientUtil.httpPostWithJSON(yun_host+"/corss/verifySubUserByTelAdmin",json);
			JSONObject myJsonObject = new JSONObject(result.toString());
			if(!myJsonObject.isNull("status")){
				//根据不同的状态码处理业务
				map.put("status", myJsonObject.getString("status").trim());
				return  new JSONObject(map);
			}else{
				map.put("status", "2");
				//系统没有获取到正确的参数
				return  new JSONObject(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", "0");
			//云服务器接口不通
			return  new JSONObject(map);
		}
	}
	
	/**
	 * 校验用户通过手机号管理员调用编辑页面
	 */
	public static JSONObject verifySubUserByTelAdminEdit(String yun_host,String sub_id,String tel){
		JSONObject json = new JSONObject();  
        json.put("sub_id", sub_id);
        json.put("tel", tel);
//        json.put("user_id", user_id);
        String result;
        Map<String,Object> map=new HashMap<String,Object>();
		try {
			result = HttpClientUtil.httpPostWithJSON(yun_host+"/corss/verifySubUserByTelAdminEdit",json);
			JSONObject myJsonObject = new JSONObject(result.toString());
			if(!myJsonObject.isNull("status")){
				//根据不同的状态码处理业务
				map.put("status", myJsonObject.getString("status").trim());
				return  new JSONObject(map);
			}else{
				map.put("status", "2");
				//系统没有获取到正确的参数
				return  new JSONObject(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", "0");
			//云服务器接口不通
			return  new JSONObject(map);
		}
	}
	
	/**
	 * 校验用户通过用户名
	 */
	public static JSONObject verifySubUserByUsername(String yun_host,String sub_id,String username){
		JSONObject json = new JSONObject();  
        json.put("sub_id", sub_id);
        json.put("username", username);
        String result;
        Map<String,Object> map=new HashMap<String,Object>();
		try {
			result = HttpClientUtil.httpPostWithJSON(yun_host+"/corss/verifySubUserByUsername",json);
			JSONObject myJsonObject = new JSONObject(result.toString());
			if(!myJsonObject.isNull("status")){
				//根据不同的状态码处理业务
				map.put("status", myJsonObject.getString("status").trim());
				return  new JSONObject(map);
			}else{
				map.put("status", "2");
				//系统没有获取到正确的参数
				return  new JSONObject(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", "0");
			//云服务器接口不通
			return  new JSONObject(map);
		}
	}

	/**
	 * 校验用户通过用户名管理员调用
	 */
	public static JSONObject verifySubUserByUsernameAdmin(String yun_host,String sub_id,String username){
		JSONObject json = new JSONObject();  
        json.put("sub_id", sub_id);
        json.put("username", username);
        String result;
        Map<String,Object> map=new HashMap<String,Object>();
		try {
			result = HttpClientUtil.httpPostWithJSON(yun_host+"/corss/verifySubUserByUsernameAdmin",json);
			JSONObject myJsonObject = new JSONObject(result.toString());
			if( !myJsonObject.isNull("status")){
				//根据不同的状态码处理业务
				map.put("status", myJsonObject.getString("status").trim());
				return  new JSONObject(map);
			}else{
				map.put("status", "2");
				//系统没有获取到正确的参数
				return  new JSONObject(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", "0");
			//云服务器接口不通
			return  new JSONObject(map);
		}
	}
	
	/**
	 * 校验用户通过邮箱
	 */
	public static JSONObject verifySubUserByEmail(String yun_host,String sub_id,String email){
		JSONObject json = new JSONObject();  
        json.put("sub_id", sub_id);
        json.put("email", email);
        String result;
        Map<String,Object> map=new HashMap<String,Object>();
		try {
			result = HttpClientUtil.httpPostWithJSON(yun_host+"/corss/verifySubUserByEmail",json);
			JSONObject myJsonObject = new JSONObject(result.toString());
			if(!myJsonObject.isNull("status")){
				//根据不同的状态码处理业务
				map.put("status", myJsonObject.getString("status").trim());
				return  new JSONObject(map);
			}else{
				map.put("status", "2");
				//系统没有获取到正确的参数
				return  new JSONObject(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", "0");
			//云服务器接口不通
			return  new JSONObject(map);
		}
	}
	
	/**
	 * 校验用户通过邮箱管理员调用
	 */
	public static JSONObject verifySubUserByEmailAdmin(String yun_host,String sub_id,String email){
		JSONObject json = new JSONObject();  
        json.put("sub_id", sub_id);
        json.put("email", email);
        String result;
        Map<String,Object> map=new HashMap<String,Object>();
		try {
			result = HttpClientUtil.httpPostWithJSON(yun_host+"/corss/verifySubUserByEmailAdmin",json);
			JSONObject myJsonObject = new JSONObject(result.toString());
			if(!myJsonObject.isNull("status")){
				//根据不同的状态码处理业务
				map.put("status", myJsonObject.getString("status").trim());
				return  new JSONObject(map);
			}else{
				map.put("status", "2");
				//系统没有获取到正确的参数
				return  new JSONObject(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", "0");
			//云服务器接口不通
			return  new JSONObject(map);
		}
	}
	
	/**
	 * 添加用户PC
	 */
	public static JSONObject addSubUser(String yun_host,String sub_id,JSONObject subUser){
		JSONObject json = new JSONObject();  
        json.put("sub_id", sub_id);
        json.put("subUser", subUser);
        String result;
        Map<String,Object> map=new HashMap<String,Object>();
		try {
			result = HttpClientUtil.httpPostWithJSON(yun_host+"/corss/addSubUser",json);
			JSONObject myJsonObject = new JSONObject(result.toString());
			if(!myJsonObject.isNull("status")){
				//根据不同的状态码处理业务
				map.put("status", myJsonObject.getString("status").trim());
				return  new JSONObject(map);
			}else{
				map.put("status", "2");
				//系统没有获取到正确的参数
				return  new JSONObject(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", "0");
			//云服务器异常
			return  new JSONObject(map);
		}
	}
	
	/**
	 * 管理员添加用户PC
	 */
	public static JSONObject addSubUserAdmin(String yun_host,String sub_id,JSONObject subUser){
		JSONObject json = new JSONObject();  
        json.put("sub_id", sub_id);
        json.put("subUser", subUser);
        String result;
        Map<String,Object> map=new HashMap<String,Object>();
		try {
			result = HttpClientUtil.httpPostWithJSON(yun_host+"/corss/addSubUserAdmin",json);
			JSONObject myJsonObject = new JSONObject(result.toString());
			if(!myJsonObject.isNull("status")){
				//根据不同的状态码处理业务
				map.put("status", myJsonObject.getString("status").trim());
				return  new JSONObject(map);
			}else{
				map.put("status", "2");
				//系统没有获取到正确的参数
				return  new JSONObject(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", "0");
			//云服务器接口不通
			return  new JSONObject(map);
		}
	}

	/**
	 * 更新用户
	 */
	public static JSONObject updateSubUser(String yun_host,JSONObject subUser){
		JSONObject json = new JSONObject();  
        json.put("subUser", subUser);
        String result;
        Map<String,Object> map=new HashMap<String,Object>();
		try {
			result = HttpClientUtil.httpPostWithJSON(yun_host+"/corss/updateSubUser",json);
			JSONObject myJsonObject = new JSONObject(result.toString());
			if(!myJsonObject.isNull("status")){
				//根据不同的状态码处理业务
				map.put("status", myJsonObject.getString("status").trim());
				return  new JSONObject(map);
			}else{
				map.put("status", "2");
				//系统没有获取到正确的参数
				return  new JSONObject(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", "0");
			//云服务器接口不通
			return  new JSONObject(map);
		}
	}
	/**
	 * 登录用户
	 */
	public static JSONObject loginSubUser(String yun_host,String sub_id,String tel,String password){
		JSONObject json = new JSONObject();  
        json.put("sub_id", sub_id);
        json.put("tel", tel);
        json.put("password", password);
        String result;
        Map<String,Object> map=new HashMap<String,Object>();
		try {
			result = HttpClientUtil.httpPostWithJSON(yun_host+"/corss/loginSubUser",json);
			JSONObject myJsonObject = new JSONObject(result.toString());
			if(!myJsonObject.isNull("status")){
				//根据不同的状态码处理业务
				map.put("status", myJsonObject.getString("status").trim());
				if(!myJsonObject.isNull("subUser")){
					map.put("subUser", myJsonObject.getJSONObject("subUser"));
				}
				return  new JSONObject(map);
			}else{
				map.put("status", "2");
				//系统没有获取到正确的参数
				return  new JSONObject(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", "0");
			//云服务器接口不通
			return  new JSONObject(map);
		}
	}	

	/**
	 * 更新子系统用户表eid为空的字段
	 */
	public static JSONObject syncEid(String yun_host,String sub_id,Map<String,String> subtelrnoeidmap){
		JSONObject json = new JSONObject();
        json.put("sub_id", sub_id);
        json.put("subtelrnoeidmap", new JSONObject(subtelrnoeidmap));
        String result;
        Map<String,Object> map=new HashMap<String,Object>();
		try {
			result = HttpClientUtil.httpPostWithJSON(yun_host+"/corss/syncEid",json);
			JSONObject myJsonObject = new JSONObject(result.toString());
			if(!myJsonObject.isNull("status")){
				//根据不同的状态码处理业务
				map.put("status", myJsonObject.getString("status").trim());
				if(!myJsonObject.isNull("telmap")){
					map.put("telmap", myJsonObject.get("telmap"));
				}
				return  new JSONObject(map);
			}else{
				map.put("status", "2");
				//系统没有获取到正确的参数
				return  new JSONObject(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", "0");
			//云服务器接口不通
			return  new JSONObject(map);
		}
	}
	
	/**
	 * 同步用户到子系统
	 */
	public static JSONObject syncSubUser(String yun_host,String sub_id,Map<String,String> subtelmap){
		JSONObject json = new JSONObject();
        json.put("sub_id", sub_id);
        json.put("subtelmap", new JSONObject(subtelmap));
        String result;
        Map<String,Object> map=new HashMap<String,Object>();
		try {
			result = HttpClientUtil.httpPostWithJSON(yun_host+"/corss/syncSubUser",json);
			JSONObject myJsonObject = new JSONObject(result.toString());
			if(!myJsonObject.isNull("status")){
				//根据不同的状态码处理业务
				map.put("status", myJsonObject.getString("status").trim());
				map.put("subuserlist", myJsonObject.get("subuserlist"));
				return  new JSONObject(map);
			}else{
				map.put("status", "2");
				//系统没有获取到正确的参数
				return  new JSONObject(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", "0");
			//云服务器接口不通
			return  new JSONObject(map);
		}
	}
	
	/**
	 * 同步用户到云平台校验
	 */
	public static JSONObject syncVerifySubUser(String yun_host,String sub_id,Map<String,String> subtelmap){
		JSONObject json = new JSONObject();  
        json.put("sub_id", sub_id);
        json.put("subtelmap", new JSONObject(subtelmap));
        String result;
        Map<String,Object> map=new HashMap<String,Object>();
		try {
			result = HttpClientUtil.httpPostWithJSON(yun_host+"/corss/syncVerifySubUser",json);
			JSONObject myJsonObject = new JSONObject(result.toString());
			if(!myJsonObject.isNull("status")){
				//根据不同的状态码处理业务
				map.put("status", myJsonObject.getString("status").trim());
				map.put("subtellist", myJsonObject.get("subtellist"));
				return  new JSONObject(map);
			}else{
				map.put("status", "2");
				//系统没有获取到正确的参数
				return  new JSONObject(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", "0");
			//云服务器接口不通
			return  new JSONObject(map);
		}
	}

	/**
	 * 同步用户到云平台添加
	 */
	public static JSONObject syncAddSubUser(String yun_host,String sub_id, Map<String,Object> subusermap) {
		JSONObject json = new JSONObject(); 
		json.put("sub_id", sub_id);
		json.put("subusermap",new JSONObject(subusermap));
		String result;
        Map<String,Object> map=new HashMap<String,Object>();
		try {
			result = HttpClientUtil.httpPostWithJSON(yun_host+"/corss/syncAddSubUser",json);
			JSONObject myJsonObject = new JSONObject(result.toString());
			if(!myJsonObject.isNull("status")){
				//根据不同的状态码处理业务
				map.put("status", myJsonObject.getString("status").trim());
				return  new JSONObject(map);
			}else{
				map.put("status", "2");
				//系统没有获取到正确的参数
				return  new JSONObject(map);
			}
		}catch (Exception e) {
			e.printStackTrace();
			map.put("status", "0");
			//云服务器接口不通
			return  new JSONObject(map);
		}
	}

	/**
	 * 禁用用户
	 */
	public static JSONObject disableSubUserByTel(String yun_host,String sub_id, String tel) {
		JSONObject json = new JSONObject(); 
		json.put("sub_id", sub_id);
		json.put("tel",tel);
		String result;
        Map<String,Object> map=new HashMap<String,Object>();
		try {
			result = HttpClientUtil.httpPostWithJSON(yun_host+"/corss/disableSubUserByTel",json);
			JSONObject myJsonObject = new JSONObject(result.toString());
			if(!myJsonObject.isNull("status")){
				//根据不同的状态码处理业务
				map.put("status", myJsonObject.getString("status").trim());
				return  new JSONObject(map);
			}else{
				map.put("status", "2");
				//系统没有获取到正确的参数
				return  new JSONObject(map);
			}
		}catch (Exception e) {
			e.printStackTrace();
			map.put("status", "0");
			//云服务器接口不通
			return  new JSONObject(map);
		}
	}
	
	/**
	 * 启用用户
	 */
	public static JSONObject enableSubUserByTel(String yun_host,String sub_id, String tel) {
		JSONObject json = new JSONObject(); 
		json.put("sub_id", sub_id);
		json.put("tel",tel);
		String result;
        Map<String,Object> map=new HashMap<String,Object>();
		try {
			result = HttpClientUtil.httpPostWithJSON(yun_host+"/corss/enableSubUserByTel",json);
			JSONObject myJsonObject = new JSONObject(result.toString());
			if(!myJsonObject.isNull("status")){
				//根据不同的状态码处理业务
				map.put("status", myJsonObject.getString("status").trim());
				return  new JSONObject(map);
			}else{
				map.put("status", "2");
				//系统没有获取到正确的参数
				return  new JSONObject(map);
			}
		}catch (Exception e) {
			e.printStackTrace();
			map.put("status", "0");
			//云服务器接口不通
			return  new JSONObject(map);
		}
	}
	
	public static  JSONObject uploadUserPicture(String yun_host,Map<String,String> textMap,File file){
		String result;
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			result=HttpClientUtil.postFile(yun_host+"/corss/uploadUserPicture",textMap,file);
			JSONObject myJsonObject = new JSONObject(result.toString());
			if(!myJsonObject.isNull("status")){
				//根据不同的状态码处理业务
				map.put("status", myJsonObject.getString("status").trim());
				return  new JSONObject(map);
			}else{
				map.put("status", "2");
				//系统没有获取到正确的参数
				return  new JSONObject(map);
			}
		}catch (Exception e) {
			e.printStackTrace();
			map.put("status", "0");
			//云服务器接口不通
			return  new JSONObject(map);
		}
	}
	
	/**
	 * 更新用户默认头像
	 */
	public static JSONObject updateUserDefaultPicture(String yun_host,String user_id,String imgId,String ext) {
		JSONObject json = new JSONObject(); 
		json.put("user_id", user_id);
		json.put("imgId",imgId);
		json.put("ext",ext);
		String result;
        Map<String,Object> map=new HashMap<String,Object>();
		try {
			result = HttpClientUtil.httpPostWithJSON(yun_host+"/corss/updateUserDefaultPicture",json);
			JSONObject myJsonObject = new JSONObject(result.toString());
			if(!myJsonObject.isNull("status")){
				//根据不同的状态码处理业务
				map.put("status", myJsonObject.getString("status").trim());
				return  new JSONObject(map);
			}else{
				map.put("status", "2");
				//系统没有获取到正确的参数
				return  new JSONObject(map);
			}
		}catch (Exception e) {
			e.printStackTrace();
			map.put("status", "0");
			//云服务器接口不通
			return  new JSONObject(map);
		}
	}
	
	public static JSONObject getEid(String yun_host,String token) {
		JSONObject json = new JSONObject(); 
		json.put("token", token);
		String result;
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			result=HttpClientUtil.httpPostWithJSON(yun_host+"/corss/getEid",json);
			JSONObject myJsonObject = new JSONObject(result.toString());
			if(!myJsonObject.isNull("status")){
				//根据不同的状态码处理业务
				map.put("status", myJsonObject.getString("status").trim());
				if(!myJsonObject.isNull("user_id")){
					map.put("user_id", myJsonObject.getString("user_id").trim());
				}
				return  new JSONObject(map);
			}else{
				map.put("status", "2");
				//系统没有获取到正确的参数
				return  new JSONObject(map);
			}
		}catch (Exception e) {
			e.printStackTrace();
			map.put("status", "0");
			//云服务器接口不通
			return  new JSONObject(map);
		}
	}

	public static JSONObject getUserScore(String yun_host,String user_id){
		JSONObject json = new JSONObject(); 
		json.put("user_id", user_id);
		String result;
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			result=HttpClientUtil.httpPostWithJSON(yun_host+"/corss/getUserScore",json);
			JSONObject myJsonObject = new JSONObject(result.toString());
			if(!myJsonObject.isNull("status")){
				//根据不同的状态码处理业务
				map.put("status", myJsonObject.getString("status").trim());
				if(!myJsonObject.isNull("score")){
					map.put("score", myJsonObject.getString("score").trim());
				}
				return  new JSONObject(map);
			}else{
				map.put("status", "2");
				//系统没有获取到正确的参数
				return  new JSONObject(map);
			}
		}catch (Exception e) {
			e.printStackTrace();
			map.put("status", "0");
			//云服务器接口不通
			return  new JSONObject(map);
		}
	}
	
	public static JSONObject updateUserScore(String yun_host,String user_id,String score){
		JSONObject json = new JSONObject(); 
		json.put("user_id", user_id);
		json.put("score", score);
		String result;
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			result=HttpClientUtil.httpPostWithJSON(yun_host+"/corss/updateUserScore",json);
			JSONObject myJsonObject = new JSONObject(result.toString());
			if(!myJsonObject.isNull("status")){
				//根据不同的状态码处理业务
				map.put("status", myJsonObject.getString("status").trim());
				return  new JSONObject(map);
			}else{
				map.put("status", "2");
				//系统没有获取到正确的参数
				return  new JSONObject(map);
			}
		}catch (Exception e) {
			e.printStackTrace();
			map.put("status", "0");
			//云服务器接口不通
			return  new JSONObject(map);
		}
	}
	
	public static void main(String[] args) {
		DESTool dt=new DESTool();
		String str=dt.decrypt("GbuXxBJy6eW7IVk1wgITcQ==");
		System.out.println(str);
	}
}
