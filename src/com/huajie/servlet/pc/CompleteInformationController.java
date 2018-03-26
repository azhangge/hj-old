package com.huajie.servlet.pc;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.Examination;
import com.hjedu.platform.controller.ClientSession;
import com.huajie.util.SpringHelper;

@WebServlet(urlPatterns = {"/servlet/pc/CompleteInformationController"})
public class CompleteInformationController extends HttpServlet {

    public void doJump(HttpServletRequest request, HttpServletResponse response) {
        try {
            String fromUrl = "";
            String uu = request.getRequestURI() + "?" + request.getQueryString();
            if (uu != null) {
                fromUrl = URLEncoder.encode(uu, SpringHelper.getSpringBean("webServerEncoding").toString());
            }
            String url = "/talk/PidClientLogin.jspx?fromUrl=" + fromUrl;
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(url);
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String url = "";
        try {
            ClientSession cs = (ClientSession) request.getSession().getAttribute("clientSession");
            if (cs == null) {
                this.doJump(request, response);
                return;
            } else if (cs.getUsr() == null) {
                this.doJump(request, response);
                return;
            } else {
                if(cs.getUsr().getPid()!=null){
                	//打印报名成功
                	
                }

            }
            
            String id = request.getParameter("id");
            String path = request.getContextPath() + "/talk/CompleteInformation.jspx?id="+id;
            //System.out.println(path);
            response.sendRedirect(path);
            //RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(path);
            //dispatcher.forward(request, response);
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
