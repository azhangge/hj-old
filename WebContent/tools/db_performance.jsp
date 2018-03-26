
<%@page import="java.sql.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.huajie.util.SpringHelper,com.huajie.exam.model.ChoiceQuestion"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>DataBase Performance Test.</title>
    </head>
    <body>
        <h4>This file is used to test the performance of the DataBase.</h4>
        <hr/>
        <%
            com.huajie.exam.dao.IChoiceQuestionDAO choiceDAO = SpringHelper.getSpringBean("ChoiceQuestionDAO");

            int round = 5;
            
            java.util.List<com.huajie.exam.model.ChoiceQuestion> choices = choiceDAO.findAllChoiceQuestion();
            long begin = System.currentTimeMillis();
            long num = choices.size();
            
            for (int i = 0; i < round; i++) {
                for (ChoiceQuestion ch : choices) {
                    String id = ch.getId();
                    ChoiceQuestion cq = choiceDAO.findChoiceQuestion(id);
                    String name = cq.getName();
                }
            }

            long readEnd = System.currentTimeMillis();

            long interval1 = readEnd - begin;
            double readSecond = ((double) (interval1)) / 1000d;


        %>
        Data Size: <%=num%>.<br/>
        Rounds:<%=round%>.<br/>
        Read Time: <%=readSecond%> seconds.
        <br/>
        <%
            for (int i = 0; i < round; i++) {
                for (ChoiceQuestion ch : choices) {
                    String id = ch.getId();
                    ChoiceQuestion cq = choiceDAO.findChoiceQuestion(id);
                    String name = cq.getName();
                    cq.setName(name);
                    choiceDAO.updateChoiceQuestion(cq);
                }
            }

            long writeEnd = System.currentTimeMillis();

            long interval2 = writeEnd - readEnd;
            double writeSecond = ((double) (interval2)) / 1000d;

        %>

        Write Time: <%=writeSecond%> seconds.
        <br/>
        Total Performance:( <%=readSecond%> + <%=writeSecond%> ) / <%=num%> x <%=round%>
    </body>
</html>
