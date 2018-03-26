package com.hjedu.customer.controller;

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

import com.hjedu.customer.service.impl.BbsUserExportService;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.SpringHelper;

@WebServlet(urlPatterns = {"/servlet/ExportBbsUser"})
public class ExportBbsUser extends HttpServlet {
    
    public ExportBbsUser() {
        super();
    }
    
    public void destroy() {
        super.destroy();
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            ILogService logger = SpringHelper.getSpringBean("LogService");
            BbsUserExportService ees = SpringHelper.getSpringBean("BbsUserExportService");
            
            HttpSession session = request.getSession();
            AdminSessionBean cs = (AdminSessionBean) session.getAttribute("adminSessionBean");
            boolean illegel = false;
            if (cs == null) {
                illegel = true;
            } else {
                if (!cs.isIfLogin()) {
                    illegel = true;
                }
            }
            if (illegel) {
                response.setContentType("text/html;charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("非法访问，你的行为已被追踪！");
                logger.log("记录到有外部用户试图非法访问人员库，但未成功！");
                return;
            }
            
            String bn = "RereUser" + System.currentTimeMillis() + ".xls";
            
            String nfn = this.getServletContext().getRealPath("upload/" + bn);
            ees.export(nfn,cs.getAdmin());
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
            logger.log("导出了用户",cs.getAdmin());
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.flushBuffer();
        
        
        
    }

   
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
