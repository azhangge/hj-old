<%-- 
    Document   : exam_list
    Created on : 2015-11-4, 9:31:23
    Author     : huajie
--%>

<%@page import="com.google.gson.*"%>
<%@page import="com.google.gson.GsonBuilder"%>
<%@page import="com.huajie.util.SpringHelper"%>
<%@page import="com.huajie.rerebbs.dao.IBbsUserDAO"%>
<%@page import="com.huajie.rerebbs.model.BbsUser"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.huajie.exam.model.*,com.huajie.exam.dao.*,com.huajie.exam.web.mb.*"%>

<%
    String uid = request.getParameter("uid");
    String qtype = request.getParameter("qtype");
    IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
    BbsUser bu = userDAO.findBbsUser(uid);
    if (bu != null && qtype != null) {
        //IQuestionCollectionDAO collectDAO = SpringHelper.getSpringBean("QuestionCollectionDAO");
        QuestionCollectionList qlst = new QuestionCollectionList();
        qlst.synDB(bu, qtype);

        java.util.List<QuestionCollection> cqs = qlst.getChoices();
        if (cqs != null) {
            for (QuestionCollection qc : cqs) {
                qc.getChoiceQuestion();
            }
        }
        java.util.List<QuestionCollection> mcqs = qlst.getMchoices();
        if (mcqs != null) {
            for (QuestionCollection qc : mcqs) {
                qc.getMultiChoiceQuestion();
            }
        }
        java.util.List<QuestionCollection> fqs = qlst.getFills();
        if (fqs != null) {
            for (QuestionCollection qc : fqs) {
                qc.getFillQuestion();
            }
        }
        java.util.List<QuestionCollection> jqs = qlst.getJudges();
        if (jqs != null) {
            for (QuestionCollection qc : jqs) {
                qc.getJudgeQuestion();
            }
        }
        java.util.List<QuestionCollection> eqs = qlst.getEssaies();
        if (eqs != null) {
            for (QuestionCollection qc : eqs) {
                qc.getEssayQuestion();
            }
        }
        java.util.List<QuestionCollection> ffqs = qlst.getFiles();
        if (ffqs != null) {
            for (QuestionCollection qc : ffqs) {
                qc.getFileQuestion();
            }
        }

        com.google.gson.ExclusionStrategy myExclusionStrategy = new com.google.gson.ExclusionStrategy() {

            @Override
            public boolean shouldSkipField(FieldAttributes fa) {
                return fa.getName().equals("content");
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        };
        GsonBuilder gb = new GsonBuilder().setExclusionStrategies(myExclusionStrategy)
                .excludeFieldsWithoutExposeAnnotation() //不导出实体中没有用@Expose注解的属性  
                .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式  
                .serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss")//时间转化为特定格式      
                .setPrettyPrinting() //对json结果格式化.  
                .setVersion(1.0);  //有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化.  
        //@Since(版本号)能完美地实现这个功能.还的字段可能,随着版本的升级而删除,那么  
        //@Until(版本号)也能实现这个功能,GsonBuilder.setVersion(double)方法需要调用.  

        Gson gson = gb.create();
        String json = gson.toJson(qlst);
        out.println(json);
    }
%>

