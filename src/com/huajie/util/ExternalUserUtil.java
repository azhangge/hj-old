package com.huajie.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.IOUtils;

import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.customer.dao.IAdminDAO;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.dao.IExternalUserDAO;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.entity.ExternalUser;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.service.ILogService;

public class ExternalUserUtil {

    static String externalFilePath = SpringHelper.getSpringBean("external_user_addr");
    static String localFilePath = "c:\\t_teller.txt";
    static IExternalUserDAO externalDAO = SpringHelper.getSpringBean("ExternalUserDAO");
    static IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
    static IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
    static IAdminDAO adminDAO = SpringHelper.getSpringBean("AdminDAO");
    static ILogService logger = SpringHelper.getSpringBean("LogService");

    public static boolean downloadExternalFile() {
        long dateMill = System.currentTimeMillis() - ((long) 1000) * 60 * 60 * 24;
        String dateStr1 = new SimpleDateFormat("yyyy/MM/dd").format(dateMill);
        String dateStr2 = new SimpleDateFormat("yyyyMMdd").format(dateMill);
        String dateStr = dateStr1 + "/all-" + dateStr2;
        externalFilePath = externalFilePath.replace("xxxx", dateStr);
        System.out.println("Reading external users from:" + externalFilePath);
        try {
            URL url = new URL(externalFilePath);
            BufferedInputStream is = new BufferedInputStream(url.openStream());

            File localFile = new File(localFilePath);
            if (localFile.exists()) {
                localFile.delete();
            }
            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(localFile));
            IOUtils.copy(is, os);
            is.close();
            os.close();
            String str = "下载远程用户文件成功。";
            logger.log(str);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            String str = "试图下载远程用户文件，但下载出错。错误类型："+e.toString()+"，外部文件下载地址："+externalFilePath;
            logger.log(str);
            return false;
        }

    }

    public static void readExternalUsers() {
        InputStream is = null;
        BufferedReader reader = null;
        try {

            String begainStr = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date());
            is = new FileInputStream(localFilePath);
            reader = new BufferedReader(new InputStreamReader(is, "GB2312"));
            String userLine = null;
            int max = 30000;
            int i = 0;
            int newNum = 0;
            while ((userLine = reader.readLine()) != null) {
                i++;
                //System.out.println("One User is readed.");
                //System.out.println(userLine);
                boolean ifNew = parseExternalUser(userLine);
                if (ifNew) {
                    newNum++;
                }
                if (i > max) {
                    break;
                }
            }

            String endStr = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date());
            String str = "读取外部用户完成，开始时间：" + begainStr + "，结束时间：" + endStr + "，共读取" + i + "条用户数据，新增用户数据" + newNum + "条.";
            logger.log(str);
            reader.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            String str = "试图读取外部用户文件，但发生错误";
            logger.log(str);
        }
    }

    public static boolean parseExternalUser(String str) {
        String ss[] = str.split("\\|");
        //System.out.println(ss);
        if (ss != null) {
            //System.out.println(ss.length);
            if (ss.length >= 7) {
                String tellerNo = ss[0].replace("\"", "");
                String tellerType = ss[1].replace("\"", "");
                String userKindNo = ss[2].replace("\"", "");
                String tellerName = ss[3].replace("\"", "");
                String cardStat = ss[4].replace("\"", "");
                String accessCode = ss[5].replace("\"", "");
                String sysFlag = ss[6].replace("\"", "");
                ExternalUser eu = externalDAO.findExternalUserByNo(tellerNo);
                //System.out.println("TellerNo:"+tellerNo);
                if (eu == null) {
                    eu = new ExternalUser();
                    eu.setTellerNo(tellerNo.trim());
                    eu.setAccessCode(accessCode.trim());
                    eu.setCardStat(cardStat.trim());
                    eu.setSysFlag(sysFlag.trim());
                    eu.setTellerName(tellerName.trim());
                    eu.setTellerType(tellerType.trim());
                    eu.setUserKindNo(userKindNo.trim());
                    externalDAO.addExternalUser(eu);
                    adaptExternalUsertoBbsUser(eu);
                    return true;
                    //System.out.println("One external User: "+tellerName+" is added.");
                }
            }
        }
        return false;
    }

    private static void adaptExternalUsertoBbsUser(ExternalUser eu) {
        BbsUser bu = bbsUserDAO.findBbsUserByUrn(eu.getTellerNo());
        if (bu == null) {
            bu = new BbsUser();
            bu.setUsername(eu.getTellerNo());
            bu.setName(eu.getTellerName());
            bbsUserDAO.addBbsUser(bu);
            //System.out.println("One external User is added to system.");
        }
    }

    public static void deleteObsoletedExamCase() {
        List<ExamCase> ecs = examCaseDAO.findObsolatedExternalExamCase();
        for (ExamCase ec : ecs) {
            examCaseDAO.deleteExamCase(ec.getId());
        }

    }

    public static void main(String args[]) {

        //ExternalUserUtil.downloadExternalFile();
        //ExternalUserUtil.readExternalUsers();
        ExternalUserUtil.deleteObsoletedExamCase();
    }
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
        list.add(ExternalUserUtil.getAdminBySession());
        obj.setAdmins(list);
    }
}
