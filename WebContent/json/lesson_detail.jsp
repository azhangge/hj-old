<%-- 
    Document   : casus_list
    Created on : 2015-11-4, 9:31:23
    Author     : huajie
--%>

<%@page import="com.google.gson.Gson"%>
<%@page import="com.google.gson.GsonBuilder"%>
<%@page import="com.huajie.util.SpringHelper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.huajie.learning.model.*,com.huajie.rerebbs.model.*,com.huajie.learning.dao.*,com.huajie.rerebbs.dao.*,com.huajie.exam.web.mb.*"%>

<%
    ILessonLogDAO casusDAO = SpringHelper.getSpringBean("LessonLogDAO");
    String uid = request.getParameter("uid");
    String lid = request.getParameter("lid");
    IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
    BbsUser bu = userDAO.findBbsUser(uid);
    if (lid!=null&&bu != null) {
        LessonDetail detail = new LessonDetail();
        detail.initx(lid, bu, request);
        GsonBuilder gb = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation() //不导出实体中没有用@Expose注解的属性  
                .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式  
                .serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss")//时间转化为特定格式      
                .setPrettyPrinting() //对json结果格式化.  
                .setVersion(1.0);  //有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化.  
        //@Since(版本号)能完美地实现这个功能.还的字段可能,随着版本的升级而删除,那么  
        //@Until(版本号)也能实现这个功能,GsonBuilder.setVersion(double)方法需要调用.  

        Gson gson = gb.create();
        String json = gson.toJson(detail);
        //String json2="{\"meta\":"+json+",\"content\":\""+casus.getContent()+"\"}";//默认情况下未取信息内容值，取信息详情时需要手工加上此信息
        out.println(json);
    }
%>

