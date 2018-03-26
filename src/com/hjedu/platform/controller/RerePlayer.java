/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hjedu.platform.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.hjedu.course.dao.ILessonDAO;
import com.hjedu.course.entity.Lesson;
import com.huajie.app.util.FileUtil;
import com.huajie.app.util.UrlUtil;
import com.huajie.util.SpringHelper;

/**
 *
 * @author huajie.com
 */

@WebServlet(urlPatterns = {"*.rplayer"})
public class RerePlayer extends HttpServlet {

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
            String fid;
            
            String source = "700.mp4";
            String duration = "";
            String title = "RerePlayer Test";
            
            String fid0 = request.getParameter("fid");
            if (fid0 != null) {
//                com.huajie.exam.web.mb.ApplicationBean ab = (com.huajie.exam.web.mb.ApplicationBean) this.getServletContext().getAttribute("applicationBean");
//                String rp = ab.getRelativePath();
//                source = "../" + rp + fid0 + ".mp4";
            	source = UrlUtil.getMp4VirtualUrlByRequest(request)+fid0+".mp4";
            }
            //System.out.println(source);
            
            String lid = request.getParameter("lid");
            if (lid != null) {
                ILessonDAO lessonDAO=SpringHelper.getSpringBean("LessonDAO");
                Lesson le=lessonDAO.findLesson(lid);
                source = UrlUtil.getVirtualUrlByRequest(request)+le.getSourceUrl();
            }
            System.out.println(source);
            String title0 = request.getParameter("title");
            if (title0 != null) {
                title = title0;
            }
            
            Map m = new HashMap();
            m.put("contextPath", request.getContextPath());
            m.put("source", source);
            m.put("title", title);
            m.put("duration", duration);
            
            VelocityEngine velocityEngine = SpringHelper.getSpringBean("VelocityEngine");//spring配置中定义
            
            String xml = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "rere_player.vm", "UTF-8", m);
            out.print(xml);
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
