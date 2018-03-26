package com.hjedu.customer.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.service.impl.ComplexBbsUserService;
import com.hjedu.examination.dao.IDictionaryDAO;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.huajie.util.SpringHelper;

@WebServlet(urlPatterns = {"/RereClientConnector"})
public class RereClientConnector extends HttpServlet {

    IBbsUserDAO clientUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
    IDictionaryDAO dicDAO = SpringHelper.getSpringBean("DictionaryDAO");
    ISystemConfigDAO ts = SpringHelper.getSpringBean("SystemConfigDAO");
    ComplexBbsUserService cbs = SpringHelper.getSpringBean("ComplexBbsUserService");

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

            //ClientSession cs = (ClientSession) session.getAttribute("clientSession");
            String username = request.getParameter("username");//第三方平台传入的用户名
            String email = request.getParameter("email");//第三方平台传入的email
            String name = request.getParameter("name");//第三方平台传入的姓名
            String group = request.getParameter("group");//第三方平台传入的用户组

            String result = cbs.loginFromOutside(request, username, email, name, group);
            if (result == null) {
                out.println("第三方平台连接'username'参数不能为空！");
                return;
            }

            ((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + "/");
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
