package com.huajie.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.huajie.util.MD5;
import com.huajie.util.SpringHelper;

@WebServlet(urlPatterns = {"/servlet/RegisterActivate"})
public class RegisterActivate extends HttpServlet {

    public void destroy() {
        super.destroy();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        IBbsUserDAO clientUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
        String urn = request.getParameter("urn");
        String sign = request.getParameter("sign");
        if ((urn == null) || (sign == null)) {
            return;
        }
        urn=URLDecoder.decode(urn, "UTF-8");
        System.out.println(urn);
        
        BbsUser cu = clientUserDAO.findBbsUserByUrn(urn);
        if (cu != null) {
            String temp = MD5.bit32(cu.getId());
            if (!sign.equalsIgnoreCase(temp)) {
                return;
            }
            clientUserDAO.activateUser(cu.getId());
            response.sendRedirect(request.getContextPath() + "/talk/ActivateOk.jspx");
        }
    }

    public void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.println("<HTML>");
        out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
        out.println("  <BODY>");
        out.print("    This is ");
        out.print(super.getClass());
        out.println(", using the POST method");
        out.println("  </BODY>");
        out.println("</HTML>");
        out.flush();
        out.close();
    }

    public void init()
            throws ServletException {
    }
}