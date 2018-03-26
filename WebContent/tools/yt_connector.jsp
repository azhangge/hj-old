
<%@page contentType="text/html" pageEncoding="UTF-8" import="com.huajie.exam.dao.IDictionaryDAO,com.huajie.exam.model.DictionaryModel,com.huajie.exam.web.mb.ClientSession,com.huajie.rerebbs.dao.IBbsUserDAO,com.huajie.rerebbs.dao.ISystemConfigDAO,com.huajie.rerebbs.model.BbsUser,com.huajie.util.SpringHelper"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            IBbsUserDAO clientUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
            IDictionaryDAO dicDAO = SpringHelper.getSpringBean("DictionaryDAO");
            ISystemConfigDAO ts = SpringHelper.getSpringBean("SystemConfigDAO");
            boolean conn = ts.getSystemConfig().isIfConnect();
            if (!conn) {
                out.println("第三方平台连接未开放！");
                return;
            }
            ClientSession cs = new ClientSession();
            //ClientSession cs = (ClientSession) session.getAttribute("clientSession");
            String username = request.getParameter("username");//第三方平台传入的用户名
            //String email = request.getParameter("email");//第三方平台传入的email
            String name = request.getParameter("realname");//第三方平台传入的姓名
            String group = request.getParameter("dept");//第三方平台传入的用户组
            BbsUser bu = null;
            String ip = request.getRemoteAddr();
            java.util.Date nowt = new java.util.Date();
            if (username == null) {
                out.println("第三方平台连接'username'参数不能为空！");
                return;
            } else {
                bu = clientUserDAO.findBbsUserByUrn(username);
                if (bu == null) {
                    bu = new BbsUser();
                    bu.setUsername(username);
                    bu.setPassword(ts.getSystemConfig().getInitPwd());
//                    if (email != null) {
//                        bu.setEmail(email);
//                    }
                    if (name != null) {
                        bu.setName(name);
                    }
                    
                    String businessId = com.huajie.util.CookieUtils.getBusinessId(request);
                    if (group != null) {
                        DictionaryModel dm = dicDAO.findDictionaryModelByName(group);
                        if (dm != null) {
                            bu.setGroupStr(dm.getId() + ";");
                        } else {
                            bu.setGroupStr(businessId + ";");
                        }
                    } else {
                        bu.setGroupStr(SpringHelper.getSpringBean(businessId+ ";");
                    }
                    bu.setRegIp(ip);
                    bu.setLastIp(ip);
                    bu.setRegTime(nowt);
                    bu.setLastTime(nowt);
                    clientUserDAO.addBbsUser(bu);
                } else {
//                    if (email != null) {
//                        bu.setEmail(email);
//                    }
                    if (name != null) {
                        bu.setName(name);
                    }
                    if (group != null) {
                        DictionaryModel dm = dicDAO.findDictionaryModelByName(group);
                        if (dm != null) {
                            bu.setGroupStr(dm.getId() + ";");
                        }
                    }
                    bu.setLastIp(ip);
                    bu.setLastTime(nowt);
                    clientUserDAO.updateBbsUser(bu);
                }
                cs.setUsr(bu);
                cs.setIfLogin(true);
                session.setAttribute("clientSession", cs);

            }
            response.sendRedirect(request.getContextPath() + "/");
        %>
    </body>
</html>
