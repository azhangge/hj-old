package com.huajie.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hjedu.examination.dao.IExamCheatDAO;
import com.hjedu.examination.entity.ExamCheat;
import com.huajie.util.SpringHelper;

@WebServlet(urlPatterns = {"/servlet/ExamCheatLogger"})
public class ExamCheatLogger extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            String userId = request.getParameter("user_id");
            String examId = request.getParameter("exam_id");
            String caseId = request.getParameter("case_id");
            if (userId!=null&&examId!=null&&caseId!=null) {
                IExamCheatDAO cheatDAO = SpringHelper.getSpringBean("ExamCheatDAO");
                ExamCheat ec = new ExamCheat();
                ec.setCaseId(caseId);
                ec.setExamId(examId);
                ec.setUserId(userId);
                ec.setIp(request.getRemoteAddr());
                cheatDAO.addExamCheat(ec);
            }
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
