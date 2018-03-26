package com.huajie.servlet.pc;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/servlet/pc/ExamCaseModule2Controller"})
public class ExamCaseModule2Controller extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String url = "ExamCaseModule4.jspx";//默认使用批量加载
        String type = "1";
        try {
            //删除正在进行的考试
            HttpSession session = request.getSession();
            session.removeAttribute("examCaseModule2");

            String case_id = request.getParameter("case_id");
            String type_1 = request.getParameter("type");
            String mode = request.getParameter("mode");
            String tid = request.getParameter("tid");

            if (type_1 != null) {
                type = type_1;
            }
            
            if (mode != null) {
                //若为单题加载
                if ("single".equals(mode.trim())) {
                    url = "ExamCaseModule2.jspx";
                }
            }

            if (case_id != null) {//恢复老考试
                url += "?case_id=" + case_id;
            } else {//加载新考试
                String exam_id = request.getParameter("exam_id");
                url += "?exam_id=" + exam_id;
            }

            String path = request.getContextPath() + "/talk/" + url + "&type=" + type;
            //System.out.println(path);
            response.sendRedirect(path+"&tid="+tid);
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
