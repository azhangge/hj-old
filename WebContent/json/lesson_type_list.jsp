<%-- 
    Document   : casus_list
    Created on : 2015-11-14, 9:31:23
    Author     : huajie
--%>

<%@page import="com.google.gson.*"%>
<%@page import="com.google.gson.GsonBuilder"%>
<%@page import="com.huajie.util.SpringHelper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.huajie.learning.model.LessonType,com.huajie.learning.dao.ILessonTypeDAO"%>

<%
    ILessonTypeDAO casusDAO = SpringHelper.getSpringBean("LessonTypeDAO");

    java.util.List<LessonType> lessonTypes = casusDAO.findAllLessonType();

    com.google.gson.ExclusionStrategy myExclusionStrategy = new com.google.gson.ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes fa) {
            return fa.getName().equals("exams");
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
    String json = gson.toJson(lessonTypes);
    out.println(json);
%>

