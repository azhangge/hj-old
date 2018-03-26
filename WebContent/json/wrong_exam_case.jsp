<%-- 
    Document   : exam_case
    Created on : 2015-11-16, 13:59:03
    Author     : huajie
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.google.gson.GsonBuilder"%>
<%@page import="com.huajie.util.SpringHelper"%>
<%@page import="com.huajie.exam.model.*,com.huajie.service.*,com.huajie.exam.dao.*,com.huajie.exam.web.mb.*,com.huajie.exam.web.mb.mag.*"%>
<%@page import="com.huajie.exam.model.Examination,com.huajie.exam.dao.IExaminationDAO,com.huajie.exam.thread.ExamCaseSaver,org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor"%>
<%@page import="com.huajie.rerebbs.dao.IBbsUserDAO"%>
<%@page import="com.huajie.rerebbs.model.BbsUser"%>
<%@page import="com.huajie.service.wrong.IWrongExamCaseService"%>

<%
    //String eid = request.getParameter("eid");
    String uid = request.getParameter("uid");

    if (uid != null) {
        IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
        IExaminationDAO examinationDAO = SpringHelper.getSpringBean("ExaminationDAO");
        IWrongExamCaseService examCaseService = SpringHelper.getSpringBean("WrongExamCaseService");
        BbsUser bu = userDAO.findBbsUser(uid);
        
        WrongTestInfo info = null;
        //客户端应该以POST方法传递错题练习的配置参数，如果不存在，则以默认配置创建一个
        String data = request.getParameter("wrong_info");
        if (data != null) {
            //com.huajie.util.MyLogger.println("The Data: " + data);
            GsonBuilder gb = new GsonBuilder();
            Gson gson = gb.create();
            info = gson.fromJson(data, com.huajie.exam.model.WrongTestInfo.class);
        }
        if (info == null) {
            info = new WrongTestInfo();
        }
        if (bu != null) {
            ExamCase examCase = new ExamCase();
            Examination examination = examinationDAO.findExamination("7");
            info.setExamination(examination);
            WrongQuestionWrapper2 wqWrapper = new WrongQuestionWrapper2(bu);
            info.setWqWrapper(wqWrapper);

            examCase.setExamination(examination);
            examCase.setName(info.getName());
            examCase.setUser(bu);
            //以标准的考试格式创建错题练习，此练习完成后应提交至标准考试提交接口
            examCaseService.buildExamCase(info, examCase);

            examCase.setIp(request.getRemoteAddr());
            //记录考试状态
            IUserSessionStateService iussService = SpringHelper.getSpringBean("UserSessionStateService");
            iussService.loginExam(examCase, request);
            //消耗积分
            IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
            bsl.log("考试消耗积分", (int) (-1 * examination.getScorePaid()));
            //加入抽题记录
            ExamCaseLog log = new ExamCaseLog();
            log.setExamination(examCase.getExamination());
            log.setUser(bu);
            log.setIp(request.getRemoteAddr());
            IExamCaseLogDAO logDAO2 = SpringHelper.getSpringBean("ExamCaseLogDAO");
            logDAO2.addExamCaseLog(log);

            ExamCaseSaver ecs = new ExamCaseSaver(examCase, false);
            ThreadPoolTaskExecutor exec = SpringHelper.getSpringBean("taskExecutor");
            exec.execute(ecs);
            //将考试实例封装为JSON
            GsonBuilder gb = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation() //不导出实体中没有用@Expose注解的属性  
                    .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式  
                    .serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss")//时间转化为特定格式      
                    .setPrettyPrinting() //对json结果格式化.  
                    .setVersion(1.0);  //有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化.  
            //@Since(版本号)能完美地实现这个功能.还的字段可能,随着版本的升级而删除,那么  
            //@Until(版本号)也能实现这个功能,GsonBuilder.setVersion(double)方法需要调用.  

            Gson gson = gb.create();
            String json = gson.toJson(examCase);
            com.huajie.util.MyLogger.println("The Data: " + json);
            out.print(json);
        }

    }

%>
