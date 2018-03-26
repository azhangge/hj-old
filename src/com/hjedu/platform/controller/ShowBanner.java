package com.hjedu.platform.controller;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

import com.huajie.util.SpringHelper;

@WebServlet(urlPatterns = {"/servlet/showBanner"})
public class ShowBanner extends HttpServlet {

    String picPath = "";
    String siteBackName = SpringHelper.getSpringBean("siteBackName");

    /**
     * Constructor of the object.
     */
    public ShowBanner() {
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

        String font = "黑体";
        int size = 36;
        Color color = Color.blue;

        String f = request.getParameter("font");
        String s = request.getParameter("size");
        String c = request.getParameter("color");

        if (f != null) {
            font = f;
        }
        if (s != null) {
            size = Integer.parseInt(s);
        }
        if (c != null) {
            try {
                color = new Color(Integer.parseInt(c, 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int bufLen = 1024;
        URLConnection conn = null;
        InputStream is = null;
        byte[] buf = new byte[bufLen];
        URL url;

        String fp = this.getServletContext().getRealPath("/resources/images/banner_base.jpg");
        //System.out.println(fp);
        //String fp = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/resources/images/banner_base.jpg";
        //System.out.println(fp);
        //url = new URL(fp);
        // if (exists(url)) {
        try {
//            conn = url.openConnection();
//            conn.setConnectTimeout(connTimeout);
//            conn.setReadTimeout(readTimeout);
//            is = new BufferedInputStream(conn.getInputStream());
            is = new BufferedInputStream(new FileInputStream(fp));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Image src = ImageIO.read(is);
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(src, 0, 0, null);



        String text = siteBackName;;
        Font mFont = new Font(font, Font.BOLD, size);
        g.setFont(mFont);
        g.setColor(color);

        g.drawString(text, 50, height / 2 + size / 2);

        g.dispose();

        OutputStream out = response.getOutputStream();
        response.setContentType("image/JPEG");


        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageOutputStream imOut = ImageIO.createImageOutputStream(bos);
        ImageIO.write(image, "jpg", imOut);   //scaledImage1为BufferedImage，jpg为图像的类型 

        InputStream bis = new ByteArrayInputStream(bos.toByteArray());
        //使用这样的方法可以让图片逐渐输出，避免ImageIO.write直接输出向response.getOutputStream造成的网速慢时图片不显示的问题。
        bos.close();
        IOUtils.copy(bis, out);
        bis.close();
        bos = null;
        imOut = null;
        bis = null;

        is.close();
        out.flush();
        out.close();
        response.setStatus(HttpServletResponse.SC_OK);
        response.flushBuffer();
        src = null;
        image = null;
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
    }
}
