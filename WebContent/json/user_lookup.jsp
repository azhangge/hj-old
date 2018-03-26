<%-- 
    Document   : 本文件用于查找用户是否存在（按URL等等）
    Created on : 2015-11-16, 13:59:03
    Author     : huajie
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.google.gson.GsonBuilder"%>
<%@page import="com.huajie.rerebbs.dao.IBbsScoreRegularDAO,com.fivestars.interfaces.bbs.client.UcUserCode"%>
<%@page import="com.huajie.util.*"%>
<%@page import="com.huajie.service.*"%>
<%@page import="java.util.Date"%>
<%@page import="com.huajie.exam.model.Examination,com.huajie.exam.dao.IExaminationDAO,com.huajie.exam.thread.ExamCaseSaver,org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor"%>
<%@page import="com.huajie.rerebbs.dao.IBbsUserDAO"%>
<%@page import="com.huajie.rerebbs.model.BbsUser"%>
<%@page import="com.huajie.exam.model.ExamCase,com.huajie.exam.model.ExamCaseLog,com.huajie.service.IBbsScoreLogService,com.huajie.service.IUserSessionStateService,com.huajie.exam.dao.IExamCaseLogDAO"%>
<%@page import="com.huajie.exam.pool.ExamPaperPool"%>
<%@page import="com.huajie.rerebbs.dao.IBbsUserDAO"%>
<%
    String eid = request.getParameter("eid");
    String uid = request.getParameter("uid");

    String authStr = "urn";
    String tempId = "";
    String tt = request.getParameter("tempId");
    if (tt != null) {
        tempId = tt;
    }
    final IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
    IBbsScoreRegularDAO scoreRegularDAO = SpringHelper.getSpringBean("BbsScoreRegularDAO");
    IUserSessionStateService iussService = SpringHelper.getSpringBean("UserSessionStateService");

    BbsUser us = null;
    Boolean ifLDAP = Boolean.parseBoolean(SpringHelper.getSpringBean("usercheck_thirdParty").toString());

    if ("pid".equals(authStr)) {
        us = bbsUserDAO.findBbsUserByPid(tempId);
    } else if ("cid".equals(authStr)) {
        us = bbsUserDAO.findBbsUserByCid(tempId);
    } else if ("urn".equals(authStr)) {
        us = bbsUserDAO.findBbsUserByUrn(tempId);
    }
    
    //注册时，如果此用户名可用，即为ok
    if (us == null) {
        out.print("ok");
        return;
    } else {
        out.print("error");
    }


%>
