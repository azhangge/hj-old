<%-- 
    Document   : fix_group
    Created on : 2014-8-14, 0:33:58
    Author     : Administrator
--%>

<%@page import="com.huajie.util.SpringHelper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>补救性提交所有未提交试卷</title>
    </head>
    <body>
        <!--本文件用户提交所有保存的未保存的考试-->
        <%
            com.huajie.service.IExamCaseService examCaseService = null;
            com.huajie.exam.dao.IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
            java.util.List<com.huajie.exam.model.ExamCase> users = examCaseDAO.findAllSavedExamCase();
            int count=0;
            for (com.huajie.exam.model.ExamCase examCase : users) {
                try {
                    if ("random".equals(examCase.getExamination().getPaperType())) {
                        examCaseService = SpringHelper.getSpringBean("ExamCaseService");;
                    } else if ("random2".equals(examCase.getExamination().getPaperType())) {
                        examCaseService = SpringHelper.getSpringBean("ExamCaseRandom2Service");
                    } else if ("manual".equals(examCase.getExamination().getPaperType())) {
                        examCaseService = SpringHelper.getSpringBean("ManualExamCaseService");
                    }
                    examCaseService.resumeExamCase(examCase);
                    examCaseService.computeExamCase(examCase);
                     examCase.setStat("submitted");
                    com.huajie.exam.thread.ExamCaseSaver.saveProcessedExamCase(examCase);
                    count++;
                } catch (Exception e) {
                    e.printStackTrace();
                    out.print(e+"<br/>");
                }

            }
            out.println("<br/><br/>");
            out.println("提交完成，共提交"+count+"份。");

        %>
        
        
        
        
    </body>
</html>
