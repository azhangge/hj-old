package com.huajie.corss.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.json.JSONObject;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.huajie.corss.json.CorssJson;
import com.huajie.util.DESTool;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

public class CorssService implements Serializable {
	private static final long serialVersionUID = 1L;
	
	IBbsUserDAO bbsUserDAO ;
	
	 public IBbsUserDAO getBbsUserDAO() {
		return bbsUserDAO;
	}

	public void setBbsUserDAO(IBbsUserDAO bbsUserDAO) {
		this.bbsUserDAO = bbsUserDAO;
	}

	public CorssJson addUserToSubSystem(HttpServletRequest request){
		 CorssJson corssJson=new CorssJson();
		 JSONObject myJsonObject = getJSONObjectByRequest(request);
		 String tel;
		 String user_id;
		 if(myJsonObject.has("tel") && myJsonObject.has("user_id")){
			 tel = myJsonObject.getString("tel").trim();
			 user_id = myJsonObject.getString("user_id").trim();
			 BbsUser bbsUser=bbsUserDAO.findBbsUserByPhone(tel);
			 if(bbsUser!=null){//有此用户
				 if(bbsUser.getExternalId()==null){
					 bbsUser.setExternalId(user_id);
					 bbsUserDAO.updateBbsUser(bbsUser);
				 }
				 corssJson.setStatus("1");
				 //子系统有此用户
			 }else{//无此用户,添加
				 BbsUser bbsUserT=new BbsUser();
				 bbsUserT.setId(UUID.randomUUID().toString());
				 bbsUserT.setTel(tel);
				 bbsUserT.setExternalId(user_id);
				 bbsUserDAO.addBbsUser(bbsUserT);
				 corssJson.setStatus("1");
				 //子系统已将添加的用户添加
			 }
		 }else{//系统没有获取到正确的参数
			 corssJson.setStatus("2");
		 }
		 return corssJson;
	 }
	 
		public JSONObject getJSONObjectByRequest(HttpServletRequest request){
			StringBuffer json = new StringBuffer();   
	        String line = null;
	        JSONObject myJsonObject = null;
			try {
				BufferedReader reader = request.getReader();
				while((line = reader.readLine()) != null) {
					json.append(line);
				}
				myJsonObject = new JSONObject(json.toString());
			} catch (IOException e) {
				MyLogger.println("解析云平台发送的json数据失败，请查看后台日志");
				e.printStackTrace();
			}
			return myJsonObject;
		}

}
