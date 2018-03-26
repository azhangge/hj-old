
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>性能信息</title>
    </head>
    <body>
        <%
            System.gc();
            Runtime lRuntime = Runtime.getRuntime();
            
            out.println("*** BEGIN MEMORY STATISTICS ***<br/>");
            
            out.println("最大可用内存量: " + lRuntime.maxMemory() / 1024 / 1024 + "M<br/>");
            out.println("当前占用内存量: " + lRuntime.totalMemory() / 1024 / 1024 + "M<br/>");
            out.println("当前闲暇内存量: " + lRuntime.freeMemory() / 1024 / 1024 + "M<br/>");
            out.println("CPU数量 : " + lRuntime.availableProcessors() + "<br/>");
            out.println("*** END MEMORY STATISTICS ***");

        %>
    </body>
</html>
