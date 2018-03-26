package com.hjedu.platform.controller;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huajie.util.SpringHelper;

public class ShowImage1 extends HttpServlet {

    //static IAdminFileDAO cfb = SpringHelper.getSpringBean("AdminFileDAO");
    //static ISystemConfigDAO sys = SpringHelper.getSpringBean("SystemConfigDAO");
    /**
     * Constructor of the object.
     */
    public ShowImage1() {
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

        //int bufLen = 1024;
        FileInputStream is = null;
        //byte[] buf = new byte[bufLen];
        String id = request.getParameter("id");
        if (id != null) {

            String relativePath = SpringHelper.getSpringBean("relativeDir").toString() + id + ".jpg";

            String path = request.getServletContext().getRealPath(relativePath);
            File fff = new File(path);
            if (fff.exists()) {
                System.out.println(relativePath);

                RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/" + relativePath);
                rd.forward(request, response);
                //return;
            } else {

                StringBuffer dir = new StringBuffer();
                ApplicationBean ab = (ApplicationBean) request.getServletContext().getAttribute("applicationBean");
                dir.append(ab.getFilePath());

                //AdminFile cf = cfb.findAdminFile(Long.parseLong(id));


                dir.append("\\");
                dir.append(id);
                String nfn = dir.toString();
                //System.out.println(nfn);
                try {
                    is = new FileInputStream(nfn);
                } catch (Exception e) {
                    String fp = request.getRealPath("resources/sys/default.png");
                    //System.out.println(fp);
                    is = new FileInputStream(fp);
                    //is = new BufferedInputStream(is);
                }

                OutputStream out = response.getOutputStream();
                response.setContentType("image/JPEG");

                try {
                    int bufLen = 1024;
                    byte buf[] = new byte[bufLen];
                    FileChannel channel = is.getChannel(); //NIO通信
                    ByteBuffer byteBuffer = ByteBuffer.wrap(buf);
                    int n = 0;
                    int total = 0;
                    while ((n = channel.read(byteBuffer)) != -1) {
                        out.write(buf, 0, n);
                        //total += n;
                        byteBuffer.clear();
                        //System.out.println(Thread.currentThread().getName() + "正在下载....");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    is.close();
                    out.flush();
                    out.close();
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.flushBuffer();
                }
            }
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
