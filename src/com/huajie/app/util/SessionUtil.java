package com.huajie.app.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.customer.dao.IAdminDAO;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.entity.BbsFileModel;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.ObjectWithAdmin;
import com.huajie.util.SpringHelper;

/**
 *	文件工具类
 */
public final class SessionUtil {
	private static final  Logger logger = Logger.getLogger(SessionUtil.class);
	static IAdminDAO adminDAO = SpringHelper.getSpringBean("AdminDAO");
	
	/**
     * 根据用户过滤数据
     * @param dicList
     * @return
     */
    public static List filterByAdmin(List dicList){
    	List returnList = new ArrayList(); 
    	AdminSessionBean asb = JsfHelper.getBean("adminSessionBean");
    	if(asb.isIfSuper()){
    		return dicList;
    	}
    	AdminInfo ai = getAdminBySession();
    	if (ai != null) {
    		String adminid = ai.getId();
    		for(Object obj : dicList){
    			ObjectWithAdmin dm = (ObjectWithAdmin) obj;
    			if(dm.getAdmins().size()>0&&dm.getAdmins().get(0).getId().equals(adminid)){
    				returnList.add(dm);
    			}
    		}
    	}
    	return returnList;
    }
    /**
     * 获取当前管理员
     * @return
     */
    public static AdminInfo getAdminBySession(){
    	AdminInfo ai = null;
    	AdminSessionBean asb = JsfHelper.getBean("adminSessionBean");
    	if (asb != null) {
    		ai = asb.getAdmin();
    	}
    	if(ai == null){
    		ai = new AdminInfo();
    		ai.setId("0");
    	}
    	return ai;
    }
    
    /**
     * 获取当前用户所属的管理员
     * @return
     */
    public static AdminInfo getAdminByUser(){
    	AdminInfo ai = null;
    	ClientSession cs = JsfHelper.getBean("clientSession");
    	BbsUser user = cs.getUsr();
		if(user!=null){
			ai = adminDAO.findAdmin(user.getGroupId());
		}
		if(ai == null){
    		ai = new AdminInfo();
    		ai.setId("0");
    	}
    	return ai;
    }
    
    /**
     * 设置当前登陆管理员为所属管理员
     */
    public static void setAdminBySession(ObjectWithAdmin obj){
    	List<AdminInfo> list = new ArrayList<>();
        list.add(getAdminBySession());
        obj.setAdmins(list);
    }
}
