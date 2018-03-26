<%-- 
    Document   : exam_case_post
    Created on : 2015-11-17, 17:52:41
    Author     : huajie
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.google.gson.GsonBuilder"%>
<%@page import="com.huajie.util.SpringHelper"%>

<%@page import="com.huajie.exam.model.Examination,com.huajie.exam.dao.IExaminationDAO,com.huajie.exam.thread.ExamCaseSaver,org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor"%>
<%@page import="com.huajie.rerebbs.dao.IBbsUserDAO"%>
<%@page import="com.huajie.rerebbs.model.BbsUser,com.huajie.exam.thread.ExamCaseSubmitter"%>
<%@page import="com.huajie.exam.model.*,com.huajie.exam.model.ExamCaseLog,com.huajie.service.IBbsScoreLogService,com.huajie.service.IUserSessionStateService"%>
<%@page import="com.huajie.exam.pool.ExamPaperPool"%>
<%@page import="com.huajie.rerebbs.dao.IBbsUserDAO"%>
<%

    String uid = request.getParameter("uid");
    String data = null;

    BbsUser bu = null;
    if (uid != null) {
        IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
        bu = userDAO.findBbsUser(uid);
    }

    try {
        if (bu != null) {
            GsonBuilder gb = new GsonBuilder();
            Gson gson = gb.create();
            data = request.getParameter("examcase");
            if (data != null) {

                ExamCase ecec = gson.fromJson(data, com.huajie.exam.model.ExamCase.class);
                ecec.setSubmitTime(new java.util.Date());
                ecec.setUser(bu);
                
                //将JSON恢复的实例转换为在考试系统可保存的状态
                com.huajie.service.IExamCaseService examCaseService = SpringHelper.getSpringBean("ExamCaseService");;
                ecec = examCaseService.restoreItemsFromJSON(ecec);
                //加入存储队列
                ExamCaseSaver ecs = new ExamCaseSaver(ecec, false);
                ThreadPoolTaskExecutor exec = SpringHelper.getSpringBean("taskExecutor");
                exec.execute(ecs);
                //out.print(ecec.getId());
                out.print("success");
                //bu.setScore(bu.getScore() + examCase.getBbsScore());
                IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
                bsl.log("考试取得成绩自动积分", (int) ecec.getBbsScore());
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        out.print("error");
    }
    //out.println("uid:"+uid);
    //out.println("data:"+data);

%>
