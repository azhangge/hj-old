package com.huajie.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hjedu.examination.dao.ICaseQuestionDAO;
import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IEssayQuestionDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.dao.IFillQuestionDAO;
import com.hjedu.examination.dao.IJudgeQuestionDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.entity.CaseQuestion;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.EssayQuestion;
import com.hjedu.examination.entity.FileQuestion;
import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.hjedu.platform.service.impl.HashCodeService;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.SpringHelper;

/**
 *
 * @author huajie.com
 */
@WebServlet(urlPatterns = {"/servlet/RereControl"})
public class RereControl extends HttpServlet {

    IChoiceQuestionDAO question1DAO = SpringHelper.getSpringBean("ChoiceQuestionDAO");
    IMultiChoiceQuestionDAO question2DAO = SpringHelper.getSpringBean("MultiChoiceQuestionDAO");
    IFillQuestionDAO question3DAO = SpringHelper.getSpringBean("FillQuestionDAO");
    IJudgeQuestionDAO question4DAO = SpringHelper.getSpringBean("JudgeQuestionDAO");
    IEssayQuestionDAO question5DAO = SpringHelper.getSpringBean("EssayQuestionDAO");
    IFileQuestionDAO question6DAO = SpringHelper.getSpringBean("FileQuestionDAO");
    ICaseQuestionDAO question7DAO = SpringHelper.getSpringBean("CaseQuestionDAO");
    HashCodeService hashCodeService = SpringHelper.getSpringBean("HashCodeService");

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            String action = request.getParameter("action");
            if ("formatq1".equals(action)) {
                this.formatQuestion1();
                out.print("finished");
            } else if ("formatq2".equals(action)) {
                this.formatQuestion2();
                out.print("finished");
            } else if ("formatq3".equals(action)) {
                this.formatQuestion3();
                out.print("finished");
            } else if ("formatq4".equals(action)) {
                this.formatQuestion4();
                out.print("finished");
            } else if ("formatq5".equals(action)) {
                this.formatQuestion5();
                out.print("finished");
            } else if ("formatq6".equals(action)) {
                this.formatQuestion6();
                out.print("finished");
            } else if ("formatq7".equals(action)) {
                this.formatQuestion7();
                out.print("finished");
            } else if ("formatq8".equals(action)) {
                this.formatQuestion1();
                this.formatQuestion2();
                this.formatQuestion3();
                this.formatQuestion4();
                this.formatQuestion5();
                this.formatQuestion6();
                this.formatQuestion7();
                out.print("finished");
            }else if ("imp_external_user".equals(action)) {
                ExternalUserUtil.readExternalUsers();
                out.print("importing external users finished");
            }
            else if ("download_external_user".equals(action)) {
                ExternalUserUtil.downloadExternalFile();
                out.print("downloading external user file finished");
            }
        } finally {
            out.close();
        }
    }

    private void formatQuestion1() {
        List<ChoiceQuestion> qsAll1 = this.question1DAO.findAllChoiceQuestion();
        for (ChoiceQuestion cq : qsAll1) {
            cq.setCleanName(cq.getCleanName());
            String hashCode = this.hashCodeService.fetchHashCode(cq);
            cq.setHashCode(hashCode);
            this.question1DAO.updateChoiceQuestion(cq);
        }
    }

    private void formatQuestion2() {
        List<MultiChoiceQuestion> qsAll1 = this.question2DAO.findAllMultiChoiceQuestion();
        for (MultiChoiceQuestion cq : qsAll1) {
            cq.setCleanName(cq.getCleanName());
            String hashCode = this.hashCodeService.fetchHashCode(cq);
            cq.setHashCode(hashCode);
            this.question2DAO.updateMultiChoiceQuestion(cq);
        }
    }

    private void formatQuestion3() {
        List<FillQuestion> qsAll1 = this.question3DAO.findAllFillQuestion();
        for (FillQuestion cq : qsAll1) {
            cq.setCleanName(cq.getCleanName());
            String hashCode = this.hashCodeService.fetchHashCode(cq);
            cq.setHashCode(hashCode);
            this.question3DAO.updateFillQuestion(cq);
        }
    }

    private void formatQuestion4() {
        List<JudgeQuestion> qsAll1 = this.question4DAO.findAllJudgeQuestion();
        for (JudgeQuestion cq : qsAll1) {
            cq.setCleanName(cq.getCleanName());
            String hashCode = this.hashCodeService.fetchHashCode(cq);
            cq.setHashCode(hashCode);
            this.question4DAO.updateJudgeQuestion(cq);
        }
    }

    private void formatQuestion5() {
        List<EssayQuestion> qsAll1 = this.question5DAO.findAllEssayQuestion();
        for (EssayQuestion cq : qsAll1) {
            cq.setCleanName(cq.getCleanName());
            String hashCode = this.hashCodeService.fetchHashCode(cq);
            cq.setHashCode(hashCode);
            this.question5DAO.updateEssayQuestion(cq);
        }
    }

    private void formatQuestion6() {
        List<FileQuestion> qsAll1 = this.question6DAO.findAllFileQuestion();
        for (FileQuestion cq : qsAll1) {
            cq.setCleanName(cq.getCleanName());
            String hashCode = this.hashCodeService.fetchHashCode(cq);
            cq.setHashCode(hashCode);
            this.question6DAO.updateFileQuestion(cq);
        }
    }

    private void formatQuestion7() {
        List<CaseQuestion> qsAll1 = this.question7DAO.findAllCaseQuestion();
        for (CaseQuestion cq : qsAll1) {
            cq.setCleanName(cq.getCleanName());
            this.question7DAO.updateCaseQuestion(cq);
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
