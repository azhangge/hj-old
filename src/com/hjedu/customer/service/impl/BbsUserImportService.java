package com.hjedu.customer.service.impl;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;

import com.hjedu.businessuser.dao.IBusinessUserDao;
import com.hjedu.businessuser.entity.BusinessUser;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.dao.IUserInfoDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.service.impl.ExamDepartmentService;
import com.hjedu.platform.dao.ComplexDepartmentLogic;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.huajie.app.util.StringUtil;
import com.huajie.util.CookieUtils;
import com.huajie.util.DESTool;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

import jxl.Sheet;
import jxl.Workbook;

public class BbsUserImportService implements Serializable {

    IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
    //IDictionaryDAO dicDAO = SpringHelper.getSpringBean("DictionaryDAO");
    ExamDepartmentService dicDAO = SpringHelper.getSpringBean("ExamDepartmentService");
    ComplexDepartmentLogic complex3 = SpringHelper.getSpringBean("ComplexDepartmentLogic");
    ComplexBbsUserService cbus = SpringHelper.getSpringBean("ComplexBbsUserService");
    ISystemConfigDAO sysDAO = SpringHelper.getSpringBean("SystemConfigDAO");
    IBusinessUserDao businessUserDAO = SpringHelper.getSpringBean("BusinessUserDAO");
    IUserInfoDAO userInfoDAO = SpringHelper.getSpringBean("UserInfoDAO");
    String businessId;
    
    public void import1(String nfn) throws Exception {
        try {
            Workbook book = Workbook.getWorkbook(new File(nfn));
            this.importBbsUser(book);
            book.close();
        } catch (Exception e) {
            throw e;
        }
    }
    
    private void importBbsUser(Workbook book) throws Exception {
    	this.businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        Sheet sheet = book.getSheet(0);
        int total = sheet.getRows();
        String password = "";
        String pid = "";
        String email = "";
        String name = "";
        String gender = "";
        BusinessUser openBussinessUser = businessUserDAO.findOpenBussinessUser();
        BusinessUser businessUser = businessUserDAO.findBussinessUser(this.businessId);
//        if(businessUser != null){
//			if(businessUser.getIsOpen()==false){
//				publicList.add(businessUser);
//			}
//		}
        
        for (int i = 1; i < total; i++) {
        	if(StringUtil.isNotEmpty(sheet.getCell(1, i).getContents())){
        		String tel=sheet.getCell(1, i).getContents().trim();
                BbsUser bbsUser=bbsUserDAO.findBbsUserByPhoneBusinessId(tel, businessId);
                if (bbsUser != null) {
                  continue;
                }else{
                	List<BbsUser> bbsUserList = bbsUserDAO.findBbsUserByPhones(tel);
                	bbsUser = new BbsUser();
                	bbsUser.setTel(tel);
					if(StringUtil.isNotEmpty(sheet.getCell(2, i).getContents())){
						bbsUser.setUsername(sheet.getCell(2, i).getContents().trim());
		        	}
					if(StringUtil.isNotEmpty(sheet.getCell(3, i).getContents())){
		        		pid = sheet.getCell(3, i).getContents().trim();
		        		bbsUser.setPid(pid);
		        	}
		        	if(StringUtil.isNotEmpty(sheet.getCell(4, i).getContents())){
		        		email = sheet.getCell(4, i).getContents().trim();
		        		bbsUser.setEmail(email);
		        	}
		        	DESTool dt = new DESTool();
		        	if(StringUtil.isNotEmpty(sheet.getCell(5, i).getContents())){
		        		password = sheet.getCell(5, i).getContents().trim();
		        	}else{
		        		if(pid.length() == 18){
		        			password = pid.substring(pid.length()-6, pid.length());
		        		}else{
		        			password = "123456";
		        		}
		        	}
		        	password = dt.encrypt(password);
		        	bbsUser.setPassword(password);
		        	if(StringUtil.isNotEmpty(sheet.getCell(6, i).getContents())){
		        		String ss=sheet.getCell(6, i).getContents().trim();
		        		Integer score= 0;
		        		try{
		        			score = Integer.valueOf(ss);
		        		}catch(Exception e){
		        			e.printStackTrace();
		        			score = 0;
		        		}
		        		bbsUser.setScore(score);
		        	}
		        	if(StringUtil.isNotEmpty(sheet.getCell(7, i).getContents())){
		        		
		        		name = sheet.getCell(7, i).getContents().trim();
		        		bbsUser.setName(name);
		        	}
		        	if(StringUtil.isNotEmpty(sheet.getCell(8, i).getContents())){
		        		gender = sheet.getCell(8, i).getContents().trim();
		        		bbsUser.setGender(gender);
		        	}
                	
                		
            		bbsUser.setId(UUID.randomUUID().toString());
            		bbsUser.setBusinessId(this.businessId);
            		bbsUser.setGroupStr(this.businessId+ ";");
            		bbsUserDAO.addBbsUser(bbsUser); 
                	if(!openBussinessUser.getId().equals(this.businessId)){//如果在私有系统增加用户，则需要在公有系统再次添加了一条新数据
                		BbsUser openUser=bbsUserDAO.findBbsUserByPhoneBusinessId(tel, openBussinessUser.getId());
                		if (openUser==null) {
                			openUser = new BbsUser();
                			BeanUtils.copyProperties(bbsUser, openUser);
                			openUser.setId(UUID.randomUUID().toString());
                			openUser.setBusinessId(openBussinessUser.getId());
                			openUser.setGroupStr(openBussinessUser.getId() + ";");
							bbsUserDAO.addBbsUser(openUser);
						} 
                	}
                }            	
        	}else{
        		continue;
        	}
        	
        }
        JsfHelper.info("导入成功！");
    }
    
    public static void main(String[] args) {
		String a = "420103198501071210";
		String b = a.substring(a.length()-6, a.length());
		System.out.println(b);
	}
}
