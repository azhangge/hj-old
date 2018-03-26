package com.huajie.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;

import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.examination.service.impl.ExamCaseExportService;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@WebServlet(urlPatterns = {"/servlet/ExportExamCase"})
public class ExportExamCase extends HttpServlet {
    
    public ExportExamCase() {
        super();
    }
    
    public void destroy() {
        super.destroy();
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
        	String bussinessId = CookieUtils.getBusinessId(request);
        	
            ILogService logger = SpringHelper.getSpringBean("LogService");
            ExamCaseExportService ees = SpringHelper.getSpringBean("ExamCaseExportService");
            HttpSession session = request.getSession();
            AdminSessionBean cs = (AdminSessionBean) session.getAttribute("adminSessionBean");
            String bn = "RereExam" + System.currentTimeMillis() + ".xls";
            
            String nfn = this.getServletContext().getRealPath("upload/" + bn);
            ees.export(nfn, bussinessId);
            File f = new File(nfn);
            //nfn = URLEncoder.encode(nfn, "UTF-8");
            // the file to download.
            // the dialogbox of download file.
            response.setHeader("Content-disposition", "attachment;filename="
                    + bn);
            // set the MIME type.
            response.setContentType("application/octet-stream");
            // get the file length.
            String length = String.valueOf(f.length());
            response.setHeader("Content-Length", length);

            // download the file.
            response.flushBuffer();
            //DESTool des = new DESTool();
//			BufferedInputStream is = new BufferedInputStream(des
//					.decryptInputStream(new FileInputStream(f)));
            BufferedInputStream is = new BufferedInputStream(new FileInputStream(f));
            OutputStream os = response.getOutputStream();
            
            IOUtils.copy(is, os);

//            byte buf[] = new byte[1024];
//            int n = 0;
//            while ((n = is.read(buf)) != -1) {
//                os.write(buf, 0, n);
//            }
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
            
            //File ff = new File(nfn);
            //ff.delete();
            logger.log("导出了成绩",cs.getAdmin());
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.flushBuffer();
        
        
        
    }

    /**
     * The doPost method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to
     * post.
     *
     * @param request the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException if an error occurred
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.println("<HTML>");
        out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
        out.println("  <BODY>");
        out.print("    This is ");
        out.print(this.getClass());
        out.println(", using the POST method");
        out.println("  </BODY>");
        out.println("</HTML>");
        out.flush();
        out.close();
    }

    /**
     * Initialization of the servlet. <br>
     *
     * @throws ServletException if an error occurs
     */
    public void init() throws ServletException {
        // Put your code here
    }
}
