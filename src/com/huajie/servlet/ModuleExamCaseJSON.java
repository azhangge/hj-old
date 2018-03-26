package com.huajie.servlet;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hjedu.examination.controller.ExamCaseModule2;
import com.hjedu.examination.entity.module2.ModuleExam2CaseItemAdapter;

@WebServlet(urlPatterns = {"/servlet/ModuleExamCaseJSON"})
public class ModuleExamCaseJSON extends HttpServlet {

    /**
     * Constructor of the object.
     */
    public ModuleExamCaseJSON() {
        super();
    }

    /**
     * Destruction of the servlet. <br>
     */
    public void destroy() {
        super.destroy(); // Just puts "destroy" string in log
        // Put your code here
    }

    /**
     * The doGet method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to get.
     *
     * @param request the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException if an error occurred
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        ExamCaseModule2 ec = (ExamCaseModule2) session.getAttribute("examCaseModule2");
        if (ec != null) {
            Map map = ec.getAdapterMap();

            List list = new ArrayList();
            list.add("first");
            list.add("second");
            //JSONArray jsonArray = new JSONArray();

        }
    }

    private String buildAdapterJSON(ModuleExam2CaseItemAdapter adapter) {
        try {

        } catch (Exception e) {
        }

        return null;
    }

}
