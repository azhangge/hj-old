package com.huajie.listener;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.primefaces.json.JSONObject;

import com.hjedu.businessuser.dao.IBusinessUserDao;
import com.hjedu.businessuser.entity.BusinessUser;
import com.hjedu.customer.dao.IBbsFileDAO;
import com.hjedu.examination.dao.IDictionaryDAO;
import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.service.ComplexExamModuleLogic;
import com.hjedu.platform.dao.ComplexDepartmentLogic;
import com.hjedu.platform.dao.ComplexFileLogic;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.entity.BbsFileModel;
import com.hjedu.platform.entity.SystemConfig;
import com.hjedu.platform.service.impl.FlashService;
import com.hjedu.platform.service.impl.ImageService;
import com.hjedu.platform.service.impl.Mp3Service;
import com.hjedu.platform.service.impl.WordImgService;
import com.huajie.exam.agent.ContestAgent;
import com.huajie.exam.pool.ExamCaseController;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.exam.pool.ModuleExamCaseController;
import com.huajie.util.CookieUtils;
import com.huajie.util.InitDB;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;
import com.hjedu.business.service.impl.*;

public class BBSServletContextListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // TODO Auto-generated method stub
        synchronized (ExamCaseController.running) {
            ExamCaseController.running = false;
        }
        synchronized (ModuleExamCaseController.running) {
            ModuleExamCaseController.running = false;
        }
        synchronized (ExamPaperPool.running) {
            ExamPaperPool.running = false;
        }

        ContestAgent.destroySchedule();

        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true;user=db_exam2;password=123456");
        } catch (SQLException se) {
            if (se.getSQLState().equals("XJ015")) {
                System.out.println("derby shutdown normally.");
            }
        }
        
        
        System.out.println("RereExam Stopped.");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
    	try {
//    		InitDB.init();
//    		IBusinessUserDao businessUserDao = SpringHelper.getSpringBean("BusinessUserDAO");
//    		BusinessUser businessUser = businessUserDao.findDefaultBussinessUser();
//    		if (businessUser != null) {
//				ServletContext sc = sce.getServletContext();
//				sc.setAttribute("businessId", businessUser.getId());
//				SpringHelper.buildSpringWebCtx(sc);
//			}
    		BusinessUserService bs = SpringHelper.getSpringBean("BusinessUserService");
    		bs.getInstance();
			//传输BBS用户设置的个人头像
    		//SpringHelper.buildSpringWebCtx(sce.getServletContext());
//        ImageService iss = SpringHelper.getSpringBean("ImageService");
//        FlashService ifs = SpringHelper.getSpringBean("FlashService");
//        Mp3Service mfs = SpringHelper.getSpringBean("Mp3Service");
//        WordImgService wis = SpringHelper.getSpringBean("WordImgService");
//        iss.transferAllUserImages(sce.getServletContext());
//        ifs.transferAllUserFlashs(sce.getServletContext());
//        mfs.transferAllUserMp3s(sce.getServletContext());
//        wis.transferAllWordImgs(sce.getServletContext());
    		ExamCaseController.check();//交卷队列
    		ModuleExamCaseController.check();//章节练习保存队列
    		ExamPaperPool.check();//试卷池
    		System.out.println("Paper ok.");
    		//每每个文件生成祖先文件列表
    		IBbsFileDAO cfDAO = SpringHelper.getSpringBean("BbsFileDAO");
    		ComplexFileLogic complex = SpringHelper.getSpringBean("ComplexFileLogic");
    		List<BbsFileModel> cfms = cfDAO.findAllClientFile();
    		for (BbsFileModel c : cfms) {
    			//if (c.getAncestors() == null || "".equals(c.getAncestors())) {
    			c.setAncestors(complex.genAncestors(c.getId()));
    			cfDAO.updateClientFileInfo(c);
    			//}
    		}
    		cfms = null;
    		System.out.println("File ok.");
    		//每每个试题模块生成祖先模块列表
    		IExamModule2DAO cf2DAO = SpringHelper.getSpringBean("ExamModule2DAO");
    		ComplexExamModuleLogic complex2 = SpringHelper.getSpringBean("ComplexExamModuleLogic");
    		List<ExamModuleModel> cfms2 = cf2DAO.findAllExamModuleModel(CookieUtils.getBusinessId(JsfHelper.getRequest()));
    		for (ExamModuleModel c : cfms2) {
    			//if (c.getAncestors() == null || "".equals(c.getAncestors())) {
    			c.setAncestors(complex2.genAncestors(c.getId()));
    			cf2DAO.updateExamModuleModel(c);
    			//}
    		}
    		cfms2 = null;
    		System.out.println("ExamModule ok.");
    		//每个部门生成祖先模块列表
    		IDictionaryDAO cf3DAO = SpringHelper.getSpringBean("DictionaryDAO");
    		ComplexDepartmentLogic complex3 = SpringHelper.getSpringBean("ComplexDepartmentLogic");
    		List<DictionaryModel> cfms3 = cf3DAO.findAllDictionaryModel(CookieUtils.getBusinessId(JsfHelper.getRequest()));
    		for (DictionaryModel c : cfms3) {
    			//if (c.getAncestors() == null || "".equals(c.getAncestors())) {
    			c.setAncestors(complex3.genAncestors(c.getId()));
    			cf3DAO.updateDictionaryModel(c);
    			//}
    		}
    		cfms3 = null;
    		System.out.println("Department ok.");
    		//启动竞赛的计时器
//    		ContestAgent.buildSchedule();
    		
    		//System.gc();
		} catch (Exception e) {
			e.printStackTrace();
			MyLogger.log(e);
		}
    }
}
