<%-- 
    Document   : exam_list
    Created on : 2015-11-4, 9:31:23
    Author     : huajie
--%>

<%@page import="com.google.gson.Gson"%>
<%@page import="com.google.gson.GsonBuilder"%>
<%@page import="com.huajie.util.SpringHelper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.huajie.exam.model.Examination,com.huajie.exam.dao.IExaminationDAO"%>

<%

    IExaminationDAO examDAO = SpringHelper.getSpringBean("ExaminationDAO");

    String lid = request.getParameter("lid");//标签类别
    String gid = request.getParameter("gid");//部门
    java.util.List<Examination> exams = new java.util.ArrayList();
    if (lid != null && gid == null) {//按标签查询
        exams = examDAO.findExaminationByLabel(lid);
    } else if (lid == null && (gid != null)) {//按部门查询
        exams = examDAO.findExaminationByDept(gid);
    } else if (lid != null && (gid != null)) {//按标签与部门查询
        exams = examDAO.findExaminationByLabelAndDept(lid, gid);
    } else if (lid == null && (gid == null)) {//查询全部
        exams = examDAO.findAllShowedExamination();
    }

    GsonBuilder gb = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation() //不导出实体中没有用@Expose注解的属性  
            .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式  
            .serializeNulls().setDateFormat("yyyy-MM-dd HH:mm")//时间转化为特定格式      
            .setPrettyPrinting() //对json结果格式化.  
            .setVersion(1.0);  //有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化.  
    //@Since(版本号)能完美地实现这个功能.还的字段可能,随着版本的升级而删除,那么  
    //@Until(版本号)也能实现这个功能,GsonBuilder.setVersion(double)方法需要调用.  

    Gson gson = gb.create();
    String json = gson.toJson(exams);
    out.println(json);
%>

