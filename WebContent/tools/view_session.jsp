<%-- 
    Document   : view_session
    Created on : 2015-5-12, 13:46:18
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
        String sid=session.getId();
        out.println(sid);
        %>
    </body>
</html>
