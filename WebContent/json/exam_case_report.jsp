<%-- 
    Document   : exam_case
    Created on : 2015-11-16, 13:59:03
    Author     : huajie
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.google.gson.GsonBuilder"%>
<%@page import="com.huajie.util.SpringHelper"%>

<%@page import="com.huajie.exam.model.Examination,com.huajie.exam.dao.IExaminationDAO,com.huajie.exam.thread.ExamCaseSaver,org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor"%>
<%@page import="com.huajie.rerebbs.dao.IBbsUserDAO"%>
<%@page import="com.huajie.rerebbs.model.BbsUser"%>
<%@page import="com.huajie.exam.model.*,com.huajie.exam.model.ExamCaseLog,com.huajie.service.*,com.huajie.exam.dao.*"%>
<%@page import="com.huajie.exam.pool.ExamPaperPool"%>
<%@page import="com.huajie.rerebbs.dao.IBbsUserDAO"%>
<%
    String case_id = request.getParameter("eid");
    IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
    IExamCaseService examCaseService;
    ExamCase examCase = null;
    if (case_id != null) {
        examCase = examCaseDAO.findExamCase(case_id);
        if (examCase != null) {
            if ("random".equals(examCase.getExamination().getPaperType())) {
                examCaseService = SpringHelper.getSpringBean("ExamCaseService");;
            } else if ("random2".equals(examCase.getExamination().getPaperType())) {
                examCaseService = SpringHelper.getSpringBean("ExamCaseRandom2Service");
            } else {
                examCaseService = SpringHelper.getSpringBean("ManualExamCaseService");
            }
            examCase = examCaseService.resumeExamCase(examCase);

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
            out.print(json);
        }

    }

%>
