package com.hjedu.examination.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.util.EntityUtils;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.service.impl.ComplexBbsUserService;
import com.hjedu.examination.dao.IIntensiveExamAndUserDAO;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.entity.IntensiveExamAndUser;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.service.ILogService;
import com.huajie.app.util.StringUtil;
import com.huajie.app.util.UrlUtil;
import com.huajie.corss.model.SubUser;
import com.huajie.corss.util.Conn;
import com.huajie.corss.util.ObjectConvent;
import com.huajie.util.JsfHelper;
import com.huajie.util.JsonUtil;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class IntensiveExamUser{

    ILogService logger = SpringHelper.getSpringBean("LogService");
    List<BbsUser> casusList = new ArrayList<>();
    Map<String,IntensiveExamAndUser> userMap = new HashMap<>();
    IBbsUserDAO cud = SpringHelper.getSpringBean("BbsUserDAO");
    ComplexBbsUserService complexUserService = SpringHelper.getSpringBean("ComplexBbsUserService");
    //LogDAO logDAO = SpringHelper.getSpringBean("logDAO");
    ISystemConfigDAO sysDAO = SpringHelper.getSpringBean("SystemConfigDAO");
    String constants_sub_id=this.sysDAO.getSystemConfig().getSub_id();
    String yun_host=this.sysDAO.getSystemConfig().getYun_host();
    IIntensiveExamAndUserDAO IntensiveExamAndUserDAO = SpringHelper.getSpringBean("IntensiveExamAndUserDAO");
    private String examName;
    
    public List<BbsUser> getCasusList() {
        return casusList;
    }

    public void setCasusList(List<BbsUser> casusList) {
        this.casusList = casusList;
    }

	public Map<String, IntensiveExamAndUser> getUserMap() {
		return userMap;
	}

	public void setUserMap(Map<String, IntensiveExamAndUser> userMap) {
		this.userMap = userMap;
	}

	public String getExamName() {
		return examName;
	}

	public void setExamName(String examName) {
		this.examName = examName;
	}

	@PostConstruct
    public void init() {
    	HttpServletRequest request = JsfHelper.getRequest();
    	if(request!=null){
    		String id = request.getParameter("id");
    		String name = request.getParameter("name");
    		examName = UrlUtil.getCN(name);
    		if(StringUtil.isNotEmpty(id)){
    			List<IntensiveExamAndUser> ieaus = IntensiveExamAndUserDAO.getIntensiveExamAndUserByExamId(id);
    			if(ieaus!=null&&ieaus.size()>0){
    				for(IntensiveExamAndUser iea : ieaus){
    					BbsUser user = iea.getUser();
    					if(user!=null){
    						if(iea.isIfAllow()){
    							user.setIfFlow(true);
    						}
    						userMap.put(user.getId(), iea);
    						if(!this.casusList.contains(user)){
    							this.casusList.add(user);
    						}
    					}
    				}
    			}
    		}
    	}
    }

    private void synchronizeDB() {
    	init();
    }

    public void deleteUser(String id) {
        BbsUser cu = this.cud.findBbsUser(id);
        this.complexUserService.deleteBbsUserSafely(id);
        this.logger.log("删除了用户：" + cu.getUsername());
        synchronizeDB();

    }

    public void batDeleteUser() {
        for (BbsUser bu : this.casusList) {
            if (bu.isSelected()) {
                this.logger.log("删除了用户：" + bu.getUsername());
                this.complexUserService.deleteBbsUserSafely(bu.getId());
            }
        }

        synchronizeDB();

    }

    public void batSyncUser(){
    	boolean b=false;
    	FacesMessage fm = new FacesMessage();
    	//将本地数据同步到云服务器
    	Map<String,String> subtelmap1=new HashMap();
    	Map<String,String> subtelrnoeidmap=new HashMap<String, String>();
    	for(BbsUser bu : this.casusList){
    		subtelmap1.put(bu.getTel(),"true");
//    		if(bu.getExternalId()==null){
    			subtelrnoeidmap.put(bu.getTel(), "true");
//    		}
    	}
    	
    	JSONObject jsonsyncVerify=Conn.syncVerifySubUser(yun_host,constants_sub_id, subtelmap1);
    	String statusyncVerify=(String) jsonsyncVerify.get("status");
    	Map<String,Object> subusermap=new HashMap();
    	if(statusyncVerify.equals("1")){
    		JSONArray jsonSyncVerifyArray=jsonsyncVerify.getJSONArray("subtellist");
			for(int i=0;i<jsonSyncVerifyArray.length();i++){
				JSONObject jsonobject=(JSONObject) jsonSyncVerifyArray.get(i);
    			BbsUser us=this.cud.findBbsUserByPhone(jsonobject.getString("tel"));
    			if(us!=null){
    				SubUser subUser=ObjectConvent.BbsUser2SubUser(us);
    				subusermap.put(us.getTel(),subUser);
    			}else{
    				break;
    			}
			}
    	}
    	if(statusyncVerify.equals("2")){
    		b=true;
    		fm.setSummary("服务器异常");
    		FacesContext.getCurrentInstance().addMessage("", fm);
    	}
		if(statusyncVerify.equals("0")){
			b=true;
			fm.setSummary("服务器异常");
    		FacesContext.getCurrentInstance().addMessage("", fm);
		}    	
    	
		
		if(!subusermap.isEmpty()){
			JSONObject jsonsyncAdd=Conn.syncAddSubUser(yun_host,constants_sub_id,  subusermap);
			String statusyncAdd=(String) jsonsyncAdd.get("status");
			if(statusyncAdd.equals("1")){
				
			}
			if(statusyncAdd.equals("2")){
				b=true;
				fm.setSummary("服务器异常");
	    		FacesContext.getCurrentInstance().addMessage("", fm);
			}
			if(statusyncAdd.equals("0")){
				b=true;
				fm.setSummary("服务器异常");
	    		FacesContext.getCurrentInstance().addMessage("", fm);
			}
		}
		
    	JSONObject jsonsyncEid=Conn.syncEid(yun_host, constants_sub_id, subtelrnoeidmap);
        String statusyncEid=(String) jsonsyncEid.get("status");
        if(statusyncEid.equals("1")){
        	if(!jsonsyncEid.isNull("telmap")){
        		JSONObject telmapJSONObject=jsonsyncEid.getJSONObject("telmap");
        		Iterator it=telmapJSONObject.keys();
				while (it.hasNext()){
		           String key = String.valueOf(it.next());
		           String value=telmapJSONObject.getString(key);
		           for(BbsUser bu : casusList){
		        	   if(bu.getTel().equals(key)){
		        		   bu.setExternalId(value);
		        		   this.cud.updateBbsUser(bu);
		        	   }
		           }
			    }
        	}
        }
        
//    	//将本地数据同步到云服务器
//    	for (BbsUser bu : this.casusList) {
//    		JSONObject jsonVerify=Conn.verifySubUserByTelAdmin(constants_sub_id, bu.getTel().trim());
//    		String statusVerify=(String) jsonVerify.get("status");
//    		if(statusVerify.equals("1")){
//    			//同步账号到云服务器 这里有云端没有
//				SubUser subUser=ObjectConvent.BbsUser2SubUser(bu);
//				if(StringUtil.isEmpty(bu.getExternalId())){
//					bu.setExternalId(subUser.getId());
//				}
//				this.cud.updateBbsUser(bu);
//				JSONObject subUserJSONObject=new JSONObject(subUser);
//				JSONObject jsonAdd=Conn.addSubUserAdmin(constants_sub_id,  subUserJSONObject);
//				String statusAdd=(String) jsonAdd.get("status");
//		       if(statusAdd.equals("2")){
//		    	   //"系统没有获取到正确的参数";
//		    	   fm.setSummary("服务器异常");
//		    		FacesContext.getCurrentInstance().addMessage("", fm);
//		    		break;
//		       }
//		       if(statusAdd.equals("0")){
//		    	   //"云服务器出现异常";
//		    	   fm.setSummary("服务器异常");
//		    		FacesContext.getCurrentInstance().addMessage("", fm);
//		    		break;
//		       }
//    		}if(statusVerify.equals("2")){
//				// "系统没有获取到正确的参数";
//    			fm.setSummary("服务器异常");
//	    		FacesContext.getCurrentInstance().addMessage("", fm);
//	    		break;
//			}
//			if(statusVerify.equals("0")){
//				//"服务器异常";
//				fm.setSummary("服务器异常");
//	    		FacesContext.getCurrentInstance().addMessage("", fm);
//	    		break;
//			}
//        }
    	//将服务器数据下拉下来
    	Map<String,String> subtelmap2=new HashMap();
    	for(BbsUser bu : this.casusList){
    		subtelmap2.put(bu.getTel(),"true");
    	}
    	JSONObject jsonSync=Conn.syncSubUser(yun_host,constants_sub_id, subtelmap2);
    	String statusVerify=(String) jsonSync.get("status");
    	if(statusVerify.equals("1")){
    		JSONArray jsonSyncArray=jsonSync.getJSONArray("subuserlist");
			for(int i=0;i<jsonSyncArray.length();i++){
				JSONObject jsonobject=(JSONObject) jsonSyncArray.get(i);
    			BbsUser us=this.cud.findBbsUserByPhone(jsonobject.getString("tel"));
    			if(us==null){
    				us=new BbsUser();
    				if(us.getExternalId()==null){
    					us.setExternalId(jsonobject.getString("id"));
    				}
        			us=ObjectConvent.SubUser2BbsUser(jsonobject, us);
        			this.cud.addBbsUser(us);
    			}else{
    				if(us.getExternalId()==null){
    					us.setExternalId(jsonobject.getString("id"));
    				}
    				us=ObjectConvent.SubUser2BbsUser(jsonobject, us);
        			this.cud.updateBbsUser(us);
    			}
			}
			synchronizeDB();
		}
    	
				

    	if(statusVerify.equals("2")){
    		b=true;
    		fm.setSummary("服务器异常");
    		FacesContext.getCurrentInstance().addMessage("", fm);
    	}
		if(statusVerify.equals("0")){
			b=true;
			fm.setSummary("服务器异常");
    		FacesContext.getCurrentInstance().addMessage("", fm);
		}
		if(b==true){
			synchronizeDB();
		}
    }
    
    public void activateUser(String id) {
        BbsUser cu = this.cud.findBbsUser(id);

        if (!cu.getActivated()) {
            this.logger.log("激活了用户：" + cu.getUsername());
            this.cud.activateUser(id);
        }
        for (BbsUser b : this.casusList) {
            if (b.getId().equals(id)) {
                b.setActivated(true);
                break;
            }
        }
        //synchronizeDB();
    }

    public void someAbleUser(String id) {
        BbsUser cu = this.cud.findBbsUser(id);

        if (cu.getEnabled()) {
            this.logger.log("禁用了用户：" + cu.getUsername());
            this.cud.disableUser(id);
        } else {
            this.cud.enableUser(id);
            this.logger.log("启用了用户：" + cu.getUsername());
        }
        //System.out.println(id);
        for (BbsUser b : this.casusList) {
            if (b.getId().equals(id)) {
                //System.out.println(id);
                if (b.getEnabled()) {
                    b.setEnabled(false);
                } else {
                    b.setEnabled(true);
                }
                break;
            }
        }

        //synchronizeDB();
    }

    public void someAbleUser2(String id) {
    	IntensiveExamAndUser iea = userMap.get(id);
        if (iea.isIfAllow()) {
        	//不允许用户考试
        	iea.setIfAllow(false);
        	for(BbsUser b : this.casusList){
	    		if (b.getId().equals(id)) {
	    			b.setIfFlow(false);
	    			break;
	    		}
        	}
        } else {
        	//允许用户考试
        	iea.setIfAllow(true);
        	for(BbsUser b : this.casusList){
        		if (b.getId().equals(id)) {
        			b.setIfFlow(true);
        			break;
              }
        	}
        }
        this.IntensiveExamAndUserDAO.updateIntensiveExamAndUser(iea);
        //synchronizeDB();
    }
    
    public void someCheckUser(String id) {
        BbsUser cu = this.cud.findBbsUser(id);

        if (cu.getChecked()) {
            this.logger.log("取消审核了用户：" + cu.getUsername());
            this.cud.uncheck(id);
        } else {
            this.cud.check(id);
            this.logger.log("审核了用户：" + cu.getUsername());
        }
        //System.out.println(id);
        for (BbsUser b : this.casusList) {
            if (b.getId().equals(id)) {
                //System.out.println(id);
                if (b.getChecked()) {
                    b.setChecked(false);
                } else {
                    b.setChecked(true);
                }
                break;
            }
        }

        //synchronizeDB();
    }

    public void someDelUser(String id) {
        BbsUser cu = this.cud.findBbsUser(id);

        if (!cu.isMarkDel()) {
            this.cud.enMarkDel(id);
        } else {
            this.cud.deMarkDel(id);
        }

        synchronizeDB();
    }
    
    public DictionaryModel getCompanyByPart(List<DictionaryModel> p){
    	if(p!=null&&p.size()>0){
    		return(JsonUtil.getCompanyByPart(p.get(0)));
    	}else{
    		return null;
    	}
    }
    
    public void batchOpen(){
    	setUserIfFlow(true);
    }
    
    public void batchClose(){
    	setUserIfFlow(false);
    }
    
    private void setUserIfFlow(boolean bo){
    	for (BbsUser bu : this.casusList) {
    		if(bu!=null&&bu.isSelected()){
    			String id = bu.getId();
    			IntensiveExamAndUser iea = userMap.get(id);
    			//允许用户考试
            	iea.setIfAllow(bo);
            	for(BbsUser b : this.casusList){
            		if (b.getId().equals(id)) {
            			b.setIfFlow(bo);
            			break;
                  }
            	}
            	this.IntensiveExamAndUserDAO.updateIntensiveExamAndUser(iea);
    		}
        }
    }
}
