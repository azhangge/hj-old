package com.huajie.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.controller.ExternalIpChecker;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.Examination;
import com.hjedu.platform.controller.ClientSession;
import com.huajie.util.SpringHelper;

@WebServlet(urlPatterns = {"/servlet/ExternalController"})
public class ExternalController extends HttpServlet {

    IExaminationDAO examDAO = SpringHelper.getSpringBean("ExaminationDAO");
    IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
    IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");

    public boolean isSameDay(Date day1, Date day2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String ds1 = sdf.format(day1);
        String ds2 = sdf.format(day2);
        return ds1.equals(ds2);
    }

    public boolean checkBrowse(HttpServletRequest request) {
        String agent = request.getHeader("user-agent");
        System.out.println(agent);
        String ie6Str = "MSIE 6.0";
        String ie7Str = "MSIE 7.0";
        if (agent.contains(ie6Str) || agent.contains(ie7Str)) {
            return false;
        } else {
            return true;
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String url = "";
        try {
            String urn = request.getParameter("urn");
            String ip = request.getRemoteAddr();
            boolean result = ExternalIpChecker.checkIp(urn, ip);
            if (!result) {
                out.println("IP地址校验失败！");
                out.close();
                return;
            }

            BbsUser bu = null;
            if (urn != null) {
                bu = this.userDAO.findBbsUserByUrn(urn);
            }
            if (bu == null) {
                //out.println("考生信息不存在");
                String path = "/external/no_user.jsp";
                //System.out.println(path);
                RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(path);
                dispatcher.forward(request, response);
                out.close();
                return;
            } else {
                ClientSession cs = new ClientSession();
                cs.setUsr(bu);
                cs.setIfLogin(true);
                request.getSession().setAttribute("clientSession", cs);

            }

            String examId = request.getParameter("exam_id");
            if (examId == null) {
                Examination ex = this.examDAO.findSingleExternalExamination();
                if (ex != null) {
                    examId = ex.getId();
                }
            }
            String case_id = null;

            List<ExamCase> exams = this.examCaseDAO.findExamCaseByExaminationAndUser(examId, bu.getId());
            Date today = new Date();
            for (ExamCase ec : exams) {
                if (this.isSameDay(today, ec.getGenTime())) {
                    case_id = ec.getId();
                    break;
                }
            }

            if (case_id != null) {//恢复老考试
                ExamCase examCase = this.examCaseDAO.findExamCase(case_id);

                if (examCase != null) {
                    Examination exam = examCase.getExamination();
                    if (exam == null) {
                        return;
                    } else {
                        url = "ExternalExamCaseRandom21.xhtml?case_id=" + case_id;

                    }
                } else {
                    //若examCase==null
                    return;
                }

            } else {//加载新考试

                if (examId == null) {
                    return;
                }
                Examination exam = this.examDAO.findExamination(examId);
                if (exam == null) {
                    return;
                } else {
                    url = "ExternalExamCaseRandom21.xhtml?exam_id=" + examId;

                }
            }

            String path = "/external/" + url;
            //System.out.println(path);
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(path);
            dispatcher.forward(request, response);
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
