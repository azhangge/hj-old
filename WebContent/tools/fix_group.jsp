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
        <title>JSP Page</title>
    </head>
    <body>
        <%
            com.huajie.rerebbs.dao.IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
            java.util.List<com.huajie.rerebbs.model.BbsUser> users = bbsUserDAO.findAllBbsUser();
            for (com.huajie.rerebbs.model.BbsUser bu : users) {
                if (bu.getGroupStr() == null || "".equals(bu.getGroupStr())) {
                    bu.setGroupStr(bu.getGroupId() + ";");
                    bbsUserDAO.updateBbsUser(bu);
                }
                
            }
            

        %>
    </body>
</html>
