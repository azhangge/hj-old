package com.hjedu.platform.controller;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huajie.app.util.FileUtil;


//@WebServlet(urlPatterns={"/servlet/ShowAbstractImage","/content/servlet/ShowAbstractImage","/talk/servlet/ShowAbstractImage","/m/servlet/ShowAbstractImage","/fm/servlet/ShowAbstractImage"})
public class ShowAbstractImage2 extends HttpServlet {
    //static IAdminFileDAO cfb = SpringHelper.getSpringBean("AdminFileDAO");
    //static ISystemConfigDAO sys = SpringHelper.getSpringBean("SystemConfigDAO");
    public ShowAbstractImage2() {
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

        //int bufLen = 1024;
        Image src = null;
        BufferedImage image = null;
        BufferedImage bimage = null;
        InputStream is = null;
        //byte[] buf = new byte[bufLen];
        String id = request.getParameter("id");
        if (id != null) {
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

//            String relativePath = SpringHelper.getSpringBean("relativeDir").toString() + id + ".jpg";
//
//            String path = request.getServletContext().getRealPath(relativePath);
//            File fff = new File(path);
//            if (fff.exists()) {
//                System.out.println(relativePath);
//
//                RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/" + relativePath);
//                rd.forward(request, response);
//                //return;
//            } else {
//                RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/" + "resources/sys/default.png");
//                rd.forward(request, response);
//            }
        }

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
        this.doGet(request, response);
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
