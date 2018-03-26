<%-- 
    Document   : test_jndi
    Created on : 2015-12-26, 0:36:03
    Author     : huajie
--%>
<%@ page import="javax.naming.*"%>
<%@ page import="org.apache.activemq.ActiveMQConnectionFactory"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
        Context ctx = new InitialContext() ;
        Context envContext = (Context) ctx.lookup("java:comp/env");
        ActiveMQConnectionFactory ff=(ActiveMQConnectionFactory) envContext.lookup("jms/ConnectionFactory");
        out.print(ff.getBrokerURL());
        %>
    </body>
</html>
