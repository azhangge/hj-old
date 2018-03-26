package com.huajie.servlet.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.service.IUserSessionStateService;
import com.hjedu.examination.dao.IExamCaseLogDAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.ExamCaseLog;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.util.SpringHelper;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author wbdwl.com
 */
@WebServlet(name = "ExamCaseFetchJson", urlPatterns = {"/servlet/json/ExamCaseFetchJson"})
public class ExamCaseFetchJson extends HttpServlet {

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
            String eid = request.getParameter("eid");
            String uid = request.getParameter("uid");

            if (eid != null && uid != null) {
                IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
                BbsUser bu = userDAO.findBbsUser(uid);
                if (bu != null) {

                    ExamCase ec = ExamPaperPool.takePaper(eid);//从试卷池中取一份试卷
                    if (ec != null) {
                        ec.setIp(request.getRemoteAddr());
                        //记录考试状态
                        IUserSessionStateService iussService = SpringHelper.getSpringBean("UserSessionStateService");
                        iussService.loginExam(ec,request);
                        //消耗积分
                        IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
                        bsl.log("考试消耗积分", (int) (-1 * ec.getExamination().getScorePaid()));
                        //加入抽题记录
                        ExamCaseLog log = new ExamCaseLog();
                        log.setExamination(ec.getExamination());
                        log.setUser(bu);
                        log.setIp(request.getRemoteAddr());
                        IExamCaseLogDAO logDAO2 = SpringHelper.getSpringBean("ExamCaseLogDAO");
                        logDAO2.addExamCaseLog(log);
                        //将考试实例封装为JSON
                        GsonBuilder gb = new GsonBuilder()
                                .excludeFieldsWithoutExposeAnnotation() //不导出实体中没有用@Expose注解的属性  
                                .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式  
                                .serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")//时间转化为特定格式      
                                .setPrettyPrinting() //对json结果格式化.  
                                .setVersion(1.0);  //有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化.  
                        //@Since(版本号)能完美地实现这个功能.还的字段可能,随着版本的升级而删除,那么  
                        //@Until(版本号)也能实现这个功能,GsonBuilder.setVersion(double)方法需要调用.  

                        Gson gson = gb.create();
                        String json = gson.toJson(ec);
                        out.println(json);
                    }
                }
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
