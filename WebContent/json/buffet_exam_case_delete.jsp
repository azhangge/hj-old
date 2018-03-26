<%-- 
    Document   : exam_list
    Created on : 2015-11-4, 9:31:23
    Author     : huajie
--%>

<%@page import="com.google.gson.*"%>
<%@page import="com.google.gson.GsonBuilder"%>
<%@page import="com.huajie.util.SpringHelper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.huajie.exam.model.ExamCase,com.huajie.exam.dao.*"%>

<%
    String eid = request.getParameter("eid");
    if (eid != null) {
        IBuffetExamCaseDAO examDAO = SpringHelper.getSpringBean("BuffetExamCaseDAO");
        examDAO.deleteBuffetExamCase(eid);
    }
%>

