package com.hjedu.platform.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

import com.huajie.app.util.FileUtil;
import com.huajie.util.CookieUtils;


@WebServlet(urlPatterns={"/JT/servlet/ShowAbstractImage","/HJ/servlet/ShowAbstractImage","/servlet/ShowAbstractImage","/content/servlet/ShowAbstractImage","/talk/servlet/ShowAbstractImage","/m/servlet/ShowAbstractImage","/fm/servlet/ShowAbstractImage"})
public class ShowAbstractImage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ShowAbstractImage() {
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
        String id = request.getParameter("id");
        if (id != null) {
        	String businessId = CookieUtils.getBusinessId(request);
        	String phyPath = FileUtil.getImageURLByIdAndBusinessId(id,businessId);
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
