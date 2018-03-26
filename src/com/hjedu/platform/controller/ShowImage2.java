package com.hjedu.platform.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hjedu.customer.dao.IBbsFileDAO;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.entity.BbsFileModel;
import com.hjedu.platform.service.impl.ImageService;
import com.huajie.app.util.FileUtil;
import com.huajie.util.SpringHelper;
//@WebServlet(urlPatterns={"/servlet/ShowImage","/talk/servlet/ShowImage","/m/servlet/ShowImage","/content/servlet/ShowImage"})
public class ShowImage2 extends HttpServlet {
	IBbsFileDAO cfb = SpringHelper.getSpringBean("BbsFileDAO");
    ISystemConfigDAO sys = SpringHelper.getSpringBean("SystemConfigDAO");
    //static IAdminFileDAO cfb = SpringHelper.getSpringBean("AdminFileDAO");
    //static ISystemConfigDAO sys = SpringHelper.getSpringBean("SystemConfigDAO");
    /**
     * Constructor of the object.
     */
    public ShowImage2() {
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
//            String relativePath = SpringHelper.getSpringBean("relativeDir").toString() + id + ".jpg";
//            String path = request.getServletContext().getRealPath(relativePath);
//            File fff = new File(path);
//            if (fff.exists()) {
//                System.out.println(relativePath);
//                RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/" + relativePath);
//                rd.forward(request, response);
//            } else {
////            	ImageService s = SpringHelper.getSpringBean("ImageService");
////            	s.transferUserImagesToRelativeDir(request.getServletContext(), id);
//            	File f = new File(path);
//            	RequestDispatcher rd;
//            	if(f.exists()){
//            		rd = this.getServletContext().getRequestDispatcher("/" + relativePath);
//            	}else{
//            		rd = this.getServletContext().getRequestDispatcher("/" + "resources/sys/defaultCourse.jpg");
//            	}
//                rd.forward(request, response);
//
//            }
        	String phyPath = FileUtil.getImageRealPathById(id);
        	File f = new File(phyPath);
        	if(!f.exists()){
        	  RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/" + "resources/sys/defaultCourse.jpg");
              rd.forward(request, response);
              return;
        	}
            String length = String.valueOf(f.length());
            response.setHeader("Content-Length", length);
            response.setContentType("application/octet-stream");
            response.flushBuffer();
            FileInputStream in = new FileInputStream(f); 
            OutputStream o = response.getOutputStream();
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
