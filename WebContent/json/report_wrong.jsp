<%-- 
    Document   : casus_list
    Created on : 2015-11-4, 9:31:23
    Author     : huajie
--%>

<%@page import="com.google.gson.Gson"%>
<%@page import="com.google.gson.GsonBuilder"%>
<%@page import="com.huajie.util.SpringHelper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.huajie.exam.model.*,com.huajie.exam.dao.*"%>
<%@page import="com.huajie.rerebbs.model.*,com.huajie.rerebbs.dao.*"%>

<%

    String uid = request.getParameter("uid");
    String qtype = request.getParameter("qtype");
    String qid = request.getParameter("qid");
    IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
    IQuestionCommentDAO commentDAO = SpringHelper.getSpringBean("QuestionCommentDAO");
    BbsUser bu = userDAO.findBbsUser(uid);
    if (bu != null && qid != null&&qtype!=null) {
        QuestionComment newComment = new QuestionComment();
        newComment.setUser(bu);
        newComment.setQuestionType(qtype);
        newComment.setQuestionId(qid);
        newComment.setCommentType("wrong");
        newComment.setWord1("试题错误");
        commentDAO.addQuestionComment(newComment);

        out.println("ok");
    }
%>

