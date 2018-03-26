package com.huajie.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hjedu.course.dao.ILessonDAO;
import com.hjedu.course.dao.ILessonLogDAO;
import com.hjedu.course.entity.Lesson;
import com.hjedu.course.entity.LessonLog;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.huajie.util.SpringHelper;

@WebServlet(name = "LessonLogNotifier", urlPatterns = {"/servlet/LessonLogNotifier"})
public class LessonLogNotifier extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {

            String action = request.getParameter("action");

            if (action != null) {
                ILessonLogDAO logDAO = SpringHelper.getSpringBean("LessonLogDAO");
                ILessonDAO lessonDAO = SpringHelper.getSpringBean("LessonDAO");
                double length = 0d;
                String len = request.getParameter("len");
                if (len != null) {
                    length = Double.parseDouble(len);
                }
                String lessionId = request.getParameter("lid");
                ClientSession cs = (ClientSession) request.getSession().getAttribute("clientSession");
                BbsUser bu = null;
                if (cs != null) {
                    bu = cs.getUsr();
                }
                if ("finished".equals(action)) {
                    if (bu != null & lessionId != null) {
                        List<LessonLog> llogs = logDAO.findLessonLogByLessonAndUsr(lessionId, bu.getId());
                        if (llogs != null) {
                            if (!llogs.isEmpty()) {
                                LessonLog llog = llogs.get(0);
                                if (!llog.isFinished()) {
                                    IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
                                    bsl.log(llog.getLesson().getName()+" 学习完成获得积分", (int) llog.getLesson().getBbsScore(), llog.getUser());
                                    llog.setFinished(true);
                                }
                                logDAO.updateLessonLog(llog);
                            }
                        }
                        //System.out.println("学习完成");
                    }
                } else if ("time".equals(action)) {
                    if (bu != null & lessionId != null) {
                        List<LessonLog> llogs = logDAO.findLessonLogByLessonAndUsr(lessionId, bu.getId());
                        if (llogs != null) {
                            if (!llogs.isEmpty()) {
                                LessonLog llog = llogs.get(0);
                                llog.setTimeFinished(length);
                                //如果超过最短学习时间，则认为此课程已经完成
                                int m = (int) length / 60;
                                if (m >= llog.getLesson().getLeastTime()) {
                                    if (!llog.isFinished()) {
                                        IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
                                        bsl.log(llog.getLesson().getName()+" 学习完成获得积分", (int) llog.getLesson().getBbsScore(), llog.getUser());
                                        llog.setFinished(true);
                                    }
                                }
                                logDAO.updateLessonLog(llog);
                            }
                            //System.out.println("学习时间更新");
                        }

                    }

                } else if ("total".equals(action)) {
                    if (lessionId != null) {
                        Lesson lesson = lessonDAO.findLesson(lessionId);
                        lesson.setTimeLen(length);
                        lessonDAO.updateLesson(lesson);
                        //System.out.println("视频时长更新");
                    }
                } else if("vedioTime".equals(action)){
                	if (bu != null & lessionId != null) {
                        List<LessonLog> llogs = logDAO.findLessonLogByLessonAndUsr(lessionId, bu.getId());
                        if (llogs != null) {
                            if (!llogs.isEmpty()) {
                                LessonLog llog = llogs.get(0);
                                llog.setVideoTime(length);
                                logDAO.updateLessonLog(llog);
                            }
                            //System.out.println("学习时间更新");
                        }

                    }
                }
                //System.out.println(bu.getId()+":"+lessionId);
            }

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
