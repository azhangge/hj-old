<%-- 
    Document   : exam_case
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
    final String tempId = "";
    String pwd = "";

    final IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
    IBbsScoreRegularDAO scoreRegularDAO = SpringHelper.getSpringBean("BbsScoreRegularDAO");
    IUserSessionStateService iussService = SpringHelper.getSpringBean("UserSessionStateService");

    GsonBuilder gb = new GsonBuilder();
    Gson gson = gb.create();
    String data = request.getParameter("user");
    BbsUser bu = gson.fromJson(data, com.huajie.rerebbs.model.BbsUser.class);

    if (bu != null) {

        String ip = request.getRemoteAddr();
        bu.setRegIp(ip);
        bu.setLastIp(ip);
        //将密码变换
        ComplexBbsUserService cbus = SpringHelper.getSpringBean("ComplexBbsUserService");
        String prePwd = bu.getPassword();
        cbus.handlePwd(bu);

        bu.setChecked(true);
        bbsUserDAO.addBbsUser(bu);
//        
//        com.huajie.exam.web.mb.ClientSession cs = JsfHelper.getBean("clientSession");
//        cs.setIfLogin(true);
//        cs.setUsr(bu);
//        
        iussService.login(bu);

        //第三方平台添加用户
        Boolean ifLDAP = Boolean.parseBoolean(SpringHelper.getSpringBean("bucheck_thirdParty").toString());
        if (ifLDAP) {//使用第三方验证的情况
            IThirdPartyUserService third = SpringHelper.getSpringBean("ThirdPartyUserService");
            third.register(bu.getUsername(), prePwd, bu.getEmail());
        }
    }
    
    out.print("success");

%>
