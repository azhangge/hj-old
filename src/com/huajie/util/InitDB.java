package com.huajie.util;

import java.util.List;

import com.hjedu.course.dao.ICourseTypeDAO;
import com.hjedu.customer.dao.IAdminDAO;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.dao.IDictionaryDAO;
import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.CourseType;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.Examination;
import com.hjedu.platform.dao.IAdvDAO;
import com.hjedu.platform.dao.ICasusDAO;
import com.hjedu.platform.dao.IOnlinePayConfigDAO;
import com.hjedu.platform.entity.AdvModel;
import com.hjedu.platform.entity.CasusModel;
import com.hjedu.platform.entity.OnlinePayConfig;

/**
 *
 * @author huajie.com
 */
public class InitDB {

    public static void initExamination() {
        IExaminationDAO examDAO = SpringHelper.getSpringBean("ExaminationDAO");
        Examination exam = examDAO.findExamination("7");
        if (exam == null) {
            exam = new Examination();
            exam.setId("7");
            exam.setName("错题练习");
            exam.setTimeLen(120);
            examDAO.addExamination(exam);
        }
    }

    public static void initExamDepartment(String businessId,String businessName) {
        IDictionaryDAO dicDAO = SpringHelper.getSpringBean("DictionaryDAO");
        DictionaryModel dm = dicDAO.findDictionaryModel(businessId);
        if (dm == null) {
            dm = new DictionaryModel();
            dm.setId(businessId);
            dm.setName(businessName+"_根部门");
            dm.setFatherId("-101");
            dm.setOrd(1);
            dm.setBusinessId(businessId);
            dicDAO.addDictionaryModel(dm);
        }
    }
    
    public static void initCourseType(String businessId,String businessName) {
    	ICourseTypeDAO courseTypeDAO = (ICourseTypeDAO)SpringHelper.getSpringBean("CourseTypeDAO");
    	CourseType courseType = courseTypeDAO.findCourseType(businessId);
        if (courseType == null) {
        	courseType = new CourseType();
        	courseType.setId(businessId);
        	courseType.setFatherId("-101");
        	courseType.setFrontShow(false);
        	courseType.setName(businessName+"_根课程分类");
        	courseType.setOrd(0);
        	courseType.setNavigationShow(false);
        	courseType.setIfRequired(false);
        	courseType.setBusinessId(businessId);
        	courseType.setAncestors(businessId+";");
        	courseTypeDAO.addCourseType(courseType);
        }
    }

    public static void initExamModule(String businessId,String businessName) {
        IExamModule2DAO dicDAO = SpringHelper.getSpringBean("ExamModule2DAO");
        ExamModuleModel dm = dicDAO.findExamModuleModel(businessId);
        if (dm == null) {
            dm = new ExamModuleModel();
            dm.setName(businessName + "_根试题模块");
            dm.setId(businessId);
            dm.setFatherId("-101");
            dm.setOrd(1);
            dm.setBusinessId(businessId);
            dicDAO.addExamModuleModel(dm);
        }
    }

    public static void initAdmin() {
        IAdminDAO dicDAO = SpringHelper.getSpringBean("AdminDAO");
        List<AdminInfo> dm = dicDAO.findAllAdmin();
        if (dm.isEmpty()) {
            AdminInfo ai = new AdminInfo();
            ai.setUrn("admin");
            ai.setNickname("admin");
            ai.setPwd("123456");
            ai.setGrp("admin");
            ai.setAuthstr("lesson;examination;export;module;sys;score;class;analysis;safety;market;question;notice;user;talk;room;");
            ai.setEnabled(true);
            ai.setPersona("总帮主");
            dicDAO.addAdmin(ai);
        }
    }

    public static void initAdv() {
        IAdvDAO advDAO = SpringHelper.getSpringBean("AdvDAO");
        List<AdvModel> advs = advDAO.findAllAdv();
        if (advs.isEmpty()) {
            AdvModel dm = new AdvModel();
            dm.setId("2");
            dm.setName("网页头");
            dm.setUrl("#");
            dm.setSrc("/resources/images/logo2014.gif");
            dm.setWidth(960);
            advDAO.updateAdvModel(dm);
        }
    }

    public static void initCasus() {
        ICasusDAO advDAO = SpringHelper.getSpringBean("CasusDAO");
        List<CasusModel> advs = advDAO.findAllCasuses();
        if (advs.isEmpty()) {
            CasusModel dm = new CasusModel();
            dm.setTitle("欢迎访问在线考试系统");
            dm.setContent("这是您第一次访问本系统，后台地址如下：http://IP地址:端口号/AdminLogin.jspx<br/>后台默认用户及密码为：admin  123456");
            advDAO.addCasus(dm);
        }
    }

    public static void initAlipay() {
        IOnlinePayConfigDAO advDAO = SpringHelper.getSpringBean("OnlinePayConfigDAO");
        OnlinePayConfig advs = advDAO.findOnlinePayConfig();
        if (advs == null) {
            OnlinePayConfig dm = new OnlinePayConfig();
            dm.setId("1");
            dm.setAlipay1Partner("test");
            dm.setAlipay1Key("test");
            dm.setAlipaySellerEmail("test@163.com");
            dm.setAlipayType("trade_create_by_buyer");
            dm.setAlipay1Url("https://mapi.alipay.com/gateway.do");
            dm.setScorePerYuan(100);
            advDAO.updateOnlinePayConfig(dm);
        }
    }

    public static void init(String businessId,String businessName) {
        //初始化错题练习
        //initExamination();
        //初始化根章节
        //initExamDepartment();
        //初始化根部门
        initExamModule(businessId,businessName);
//        initAdmin();
//        initAdv();
//        initCasus();
//        initAlipay();
    }

}
