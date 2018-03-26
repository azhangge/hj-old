package com.huajie.servlet;

import java.io.*;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hjedu.customer.dao.IBbsFileDAO;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.entity.BbsFileModel;
import com.hjedu.customer.dao.IUserTokenDAO;
import com.huajie.app.service.UserAppService;
import com.huajie.app.util.StringUtil;
import com.huajie.util.JsonUtil;
import com.huajie.util.SpringHelper;

@WebServlet(urlPatterns = {"/servlet/DownloadJsonFile"})
public class DownloadJsonFile extends HttpServlet {

    /**
     * Constructor of the object.
     */
    IBbsFileDAO cfb = SpringHelper.getSpringBean("BbsFileDAO");
    IExamModule2DAO dicDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    ISystemConfigDAO sys = SpringHelper.getSpringBean("SystemConfigDAO");
    IBbsUserDAO cuDAO = SpringHelper.getSpringBean("BbsUserDAO");
    IUserTokenDAO userTokenDAO = SpringHelper.getSpringBean("UserTokenDAO");
    
    public DownloadJsonFile() {
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
        
        
    	HttpSession session = request.getSession();
        ClientSession cs = (ClientSession) session.getAttribute("clientSession");
        String token = request.getParameter("token");
        BbsUser bu = null;
        UserAppService us = SpringHelper.getSpringBean("UserAppService");
        String vid = userTokenDAO.getIdByToken(token);
        bu = cuDAO.findBbsUser(vid);
        if (cs == null&&bu == null) {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("未登录不允许下载！");
            return;
        } else {
        	if(bu == null){
        		bu = cs.getUsr();
        	}
            if (bu == null) {
                response.setContentType("text/html;charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("未登录不允许下载！");
                return;
            }
        }
        
        FileInputStream is = null;
        OutputStream os = null;
        try {
            String uid = request.getParameter("uid");
            String id = request.getParameter("id");
            String dir = JsonUtil.getSaveFilePath()+"json"+request.getServerPort()+"/"+id;
            if(id.endsWith(".zip")){
            	String lessonId = id.replace(".zip", "");
            	dir = JsonUtil.getSaveFilePath()+"lesson"+"/"+lessonId+"/"+id;
            }
            File f = new File(dir);
//			if (fn.length() > 15) {
//				fn = fn.substring(0, 15);
//			}
            String targetName = id;
            // the file to download.
            // the dialogbox of download file.
            response.setHeader("Content-disposition", "attachment;filename="
                    + targetName);
            String length = String.valueOf(((double)f.length())/1024);
            response.setHeader("Content-Length", f.length()+"");
            // set the MIME type.
            response.setContentType("application/octet-stream");
            //response.setContentType("application/x-download");
            // get the file length.

            response.flushBuffer();//向客户端发达文件头信息，以免长时间等待，客户抓狂
            is = new FileInputStream(f);
            os = response.getOutputStream();
            //os = new BufferedOutputStream(os);//servlet无需使用BufferedOutputStream,它对读硬盘数据有用
            int bufLen = bufLen = 256 * 1024;
            System.out.println("json文件大小：" + length +"KB");
            byte buf[] = new byte[bufLen];
            FileChannel channel = is.getChannel(); //NIO通信
            ByteBuffer byteBuffer = ByteBuffer.wrap(buf);
            int n = 0;
            int total = 0;
            while ((n = channel.read(byteBuffer)) != -1) {
                try {
                    os.write(buf, 0, n);
                    //total += n;
                    byteBuffer.clear();
                    //System.out.println(Thread.currentThread().getName() + "正在下载....");
                } catch (Exception e) {
                    continue;
                }
            }
            //System.out.println("下载完成，本线程共下载" + total);

            //IOUtils.copy(is,os);
            os.flush();
            os.close();
        } catch (Exception e) {
        	System.out.println("下载试题json文件异常");
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
            response.setStatus(HttpServletResponse.SC_OK);
            response.flushBuffer();
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
