<%-- 
    Document   : exam_list
    Created on : 2015-11-4, 9:31:23
    Author     : huajie
--%>

<%@page import="com.google.gson.*"%>
<%@page import="com.google.gson.GsonBuilder"%>
<%@page import="com.huajie.util.SpringHelper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.huajie.exam.model.contest.*,com.huajie.exam.dao.*"%>

<%

    java.util.List<ExamContestSession> exams = com.huajie.exam.agent.ContestAgent.getContests();

    java.util.List<ExamContestSession> exs = new java.util.LinkedList();
    if (exams != null) {
        for (ExamContestSession ex : exams) {
            if (ex.isIfToday()) {
                ex.getSessionStr();
                ex.getCaseNum();
                exs.add(ex);
            }
        }
    }


    GsonBuilder gb = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation() //不导出实体中没有用@Expose注解的属性  
            .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式  
            .serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss")//时间转化为特定格式      
            .setPrettyPrinting() //对json结果格式化.  
            .setVersion(1.0);  //有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化.  
    //@Since(版本号)能完美地实现这个功能.还的字段可能,随着版本的升级而删除,那么  
    //@Until(版本号)也能实现这个功能,GsonBuilder.setVersion(double)方法需要调用.  

    Gson gson = gb.create();
    String json = gson.toJson(exs);
    out.println(json);
%>

