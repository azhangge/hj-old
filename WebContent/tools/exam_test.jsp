
<%@page import="java.sql.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>

        <%
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://127.0.0.1:3306/exam2_portal?characterEncoding=UTF-8";
            String user = "root";
            String password = "123456";

            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, user, password);

            String sql = "select * from Examination";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                String title = rs.getString("name");
                out.println(title+"<br/>");
            }
        %>

    </body>
</html>
