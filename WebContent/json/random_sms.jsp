<%-- 
    Document   : casus_list
    Created on : 2015-11-4, 9:31:23
    Author     : huajie
--%>

<%@page import="com.google.gson.Gson"%>
<%@page import="com.google.gson.GsonBuilder"%>
<%@page import="com.huajie.util.SpringHelper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.huajie.model.CasusModel,com.huajie.rerebbs.dao.ICasusDAO"%>

<%

    String phoneNum = request.getParameter("phone");
    String uid = request.getParameter("uid");

    if (phoneNum != null&&uid!=null) {
        double random = Math.random();
        long num = 100000 + (long)( random * 900000);
        String sms = String.valueOf(num);
        
        String json = "{\"sms\":"+"\""+sms+"\"}";
        //String json2="{\"meta\":"+json+",\"content\":\""+casus.getContent()+"\"}";//默认情况下未取信息内容值，取信息详情时需要手工加上此信息
        out.println(json);

    }
%>

