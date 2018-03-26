package com.hjedu.platform.controller;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huajie.util.SpringHelper;


@WebServlet(urlPatterns={"/servlet/ShowFlash","/content/servlet/ShowFlash","/talk/servlet/ShowFlash","/m/servlet/ShowFlash"})
public class ShowFlash extends HttpServlet {
    //static IAdminFileDAO cfb = SpringHelper.getSpringBean("AdminFileDAO");
    //static ISystemConfigDAO sys = SpringHelper.getSpringBean("SystemConfigDAO");
    public ShowFlash() {
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
        InputStream is = null;
        //byte[] buf = new byte[bufLen];
        String id = request.getParameter("id");
        if (id != null) {

            String relativePath = SpringHelper.getSpringBean("relativeDir").toString() + id + ".mp4";

            String path = request.getServletContext().getRealPath(relativePath);
            File fff = new File(path);
            if (fff.exists()) {
                System.out.println(relativePath);

                RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/" + relativePath);
                rd.forward(request, response);
                //return;
            } else {
                //RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/" + "resources/sys/default.jpg");
                //rd.forward(request, response);
                System.out.println("FLASH不存在");
            }
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
