package com.hjedu.platform.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

import com.hjedu.customer.dao.IBbsFileDAO;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.huajie.app.util.FileUtil;
import com.huajie.util.CookieUtils;
import com.huajie.util.SpringHelper;
@WebServlet(urlPatterns={"/servlet/ShowImage","/HJ/servlet/ShowImage","/talk/servlet/ShowImage","/m/servlet/ShowImage","/content/servlet/ShowImage"})
public class ShowImage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	IBbsFileDAO cfb = SpringHelper.getSpringBean("BbsFileDAO");
    ISystemConfigDAO sys = SpringHelper.getSpringBean("SystemConfigDAO");
    //static IAdminFileDAO cfb = SpringHelper.getSpringBean("AdminFileDAO");
    //static ISystemConfigDAO sys = SpringHelper.getSpringBean("SystemConfigDAO");
    /**
     * Constructor of the object.
     */
    public ShowImage() {
        super();
    }

    /**
     * Destruction of the servlet. <br>
     */
    public void destroy() {
        super.destroy(); // Just puts "destroy" string in log
        // Put your code here
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        if (id != null) {
        	String businessId = CookieUtils.getBusinessId(request);
        	String phyPath = FileUtil.getImageURLByIdAndBusinessId(id, businessId);
            URL url = new URL(phyPath);
            URLConnection conn = url.openConnection();
            String message = conn.getHeaderField(0);//文件存在‘HTTP/1.1 200’  
            InputStream in = null;
            OutputStream o = response.getOutputStream();
            if (StringUtils.hasText(message) && message.startsWith("HTTP/1.1 200")) { 
            	in = conn.getInputStream();
            }else{  
            	String path = FileUtil.getImageRealPathById(id);
            	File file = new File(path);
            	if(!file.exists()){
            		path = request.getSession().getServletContext().getRealPath("resources/sys/defaultCourse.jpg");
            		file = new File(path);
            	}
            	in = new FileInputStream(file);
            } 
            int l = 0; 
            byte[] buffer = new byte[4096]; 
            while ((l = in.read(buffer)) != -1) {
            	o.write(buffer, 0, l);
            }
            o.flush();
            in.close();
            o.close(); 
        }
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

    public void init() throws ServletException {
        // Put your code here
    }
}
