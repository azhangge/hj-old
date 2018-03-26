<%@page import="com.huajie.util.SpringHelper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>找回管理员密码</title>
        <style type="text/css">
            div,font,a,td{font-size:13px;}
            table{
                border-collapse:collapse; 
                border-spacing:0; 
                border-left:1px solid #aaa; 
                border-top:1px solid #aaa; 
                background:#efefef;
                margin: 0px auto;
                width: 300px;
                margin-top: 5px;
                margin-bottom: 5px;
            }
            th{
                border-right:1px solid #888; 
                border-bottom:1px solid #888; 
                padding:3px 15px; 
                text-align:center; 
                font-weight:bold; 
                background:#ccc; 
                font-size:13px;
            }
            td{
                border-right:1px solid #888; 
                border-bottom:1px solid #888; 
                padding:3px 15px; 
                text-align:center; 
                color:#3C3C3C;}
            </style>
        </head>
        <body>
            <!--本功能用于在服务器上找回所有管理员密码-->
            <%
                com.huajie.rerebbs.dao.IAdminDAO adminDAO = SpringHelper.getSpringBean("AdminDAO");
                java.util.List<com.huajie.model.AdminInfo> admins = adminDAO.findAllAdmin();
                String local = request.getRemoteAddr();
            %>
            <div style="margin-top:100px;"/>
        <div style="width:600px;margin:0px auto;text-align: center;">
            <span style="font-weight: bolder;">管理员列表</span>
            <table>
                <tr>
                    <th>用户名</th>
                    <th>密码</th>
                </tr>
                <%
                    if ("127.0.0.1".equals(local)||"0:0:0:0:0:0:0:1".equals(local)) {
                        for (com.huajie.model.AdminInfo admin : admins) {
                            String urn = admin.getUrn();
                            String pwd = admin.getPwd();

                %>
                <tr>
                    <td><%=urn%></td>
                    <td><%=pwd%></td>
                </tr>
                <%}
                    }%>
            </table>
            <span style="font-style: normal;color: #3C3C3C;">*当前访问IP：<%=local%>；</span>
            <br/>
            <span style="font-style: normal;color: #3C3C3C;">*本功能用于在服务器上找回管理员密码，仅以127.0.0.1访问时有效！</span>
        </div>
    </body>
</html>
