package com.hjedu.customer.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hjedu.customer.dao.IAdminDAO;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@WebServlet(urlPatterns={"/RereAdminConnector"})
public class RereAdminConnector extends HttpServlet {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IAdminDAO adminDAO = SpringHelper.getSpringBean("AdminDAO");
    ISystemConfigDAO ts = SpringHelper.getSpringBean("SystemConfigDAO");

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            boolean conn = ts.getSystemConfig().isIfConnect();
            if (!conn) {
                out.println("第三方平台连接未开放！");
                return;
            }
            HttpSession session = ((HttpServletRequest) request).getSession();
            //AdminSessionBean asb = (AdminSessionBean) session.getAttribute("adminSessionBean");
            AdminSessionBean asb = new AdminSessionBean();
            String username = request.getParameter("username");//第三方平台传入的用户名
            String password = request.getParameter("password");//第三方平台传入的email
            AdminInfo bu = null;
            String ip = request.getRemoteAddr();
            Date nowt = new Date();
            if (username == null) {
                out.println("第三方平台连接'username'参数不能为空！");
                return;
            } else {
            	String businessId = CookieUtils.getBusinessId((HttpServletRequest) request);
                bu = adminDAO.findAdminByUrnByBusinessId(username,businessId);
                if (bu == null) {
                    out.println("第三方平台连接的管理用户名'" + username + "'不存在！");
                    return;
                } else {
                    String pw = bu.getPwd();
                    if (!pw.equals(password)) {
                        out.println("第三方平台连接的管理用户名'" + username + "'对应的密码不正确！");
                        return;
                    } else {
                        bu.setLtime(nowt);
                        adminDAO.updateAdmin(bu);
                        asb.setAdmin(bu);
                        asb.setIfLogin(true);
//                        boolean t = bu.getGrp().equals("admin");
                        boolean t = !bu.getGrp().equals("company");
                        asb.setIfSuper(t);
                        session.setAttribute("adminSessionBean",asb);
                        logger.log("从第三方平台登录了系统");
                    }
                }
            }
            ((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + "/m/Welcome.jspx");
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
