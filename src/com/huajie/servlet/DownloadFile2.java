package com.huajie.servlet;

import java.io.*;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.ClientAbortException;

import com.hjedu.customer.dao.IBbsFileDAO;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.entity.BbsFileModel;
import com.hjedu.customer.dao.IUserTokenDAO;
import com.huajie.app.service.UserAppService;
import com.huajie.app.util.DateUtil;
import com.huajie.app.util.FileUtil;
import com.huajie.app.util.StringUtil;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@WebServlet(urlPatterns = {"/servlet/DownloadFile2"})
public class DownloadFile2 extends HttpServlet {

    /**
     * Constructor of the object.
     */
    IBbsFileDAO cfb = SpringHelper.getSpringBean("BbsFileDAO");
    ISystemConfigDAO sys = SpringHelper.getSpringBean("SystemConfigDAO");
    IBbsUserDAO cuDAO = SpringHelper.getSpringBean("BbsUserDAO");
    IUserTokenDAO userTokenDAO = SpringHelper.getSpringBean("UserTokenDAO");
    
    public DownloadFile2() {
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
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(!checkRight(request)){
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("未登录不允许下载！");
			out.flush();
			out.close();
			return;
		}
    	RandomAccessFile is = null;
        OutputStream os = null;
        try {
            String id = request.getParameter("id");
            BbsFileModel cf = cfb.findClientFile(id);
            if (cf == null) {
                return;
            }
            String nfn = FileUtil.getPdfFileRealPath(cf);
            File f = new File(nfn);
            String fn = cf.getFileName();
            if(!f.exists()){
            	return;
            }
            fn = URLEncoder.encode(fn, "UTF-8");
            String targetName = fn + ".pdf";
            response.setHeader("Content-disposition", "attachment;filename=\""+ targetName+"\"");
            Calendar cal = Calendar.getInstance(); 
            cal.setTimeInMillis(f.lastModified());
            DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'",Locale.US);
            response.setHeader("Last-Modified",df.format(cal.getTime()));
            response.setHeader("Etag","\"627-4d648041f6b80\"");
            String length = String.valueOf(f.length());
            //断点续传
            response.setHeader("Accept-Ranges", "bytes");
            int status = HttpServletResponse.SC_OK;
            String range = request.getHeader("Range");
            // 如果range下载区域为空，则首次下载，  
            if(range == null){  
                range = "bytes=0-";  
            } else {  
                // 通过下载区域下载，使用206状态码支持断点续传  
                status = HttpServletResponse.SC_PARTIAL_CONTENT;  
            }
            long start = 0, end = 0;  
            if (null != range && range.startsWith("bytes=")) {  
                String[] values = range.split("=")[1].split("-");  
                start = Integer.parseInt(values[0]);  
                // 如果服务器端没有设置end结尾，默认取下载全部  
                if(values.length == 1){  
                    end = f.length();  
                } else {  
                    end = Integer.parseInt(values[1]);  
                }  
            }
            // 此次数据响应大小  
            long responseSize = 0;  
            if (end != 0 && end > start) {  
                responseSize = end - start;  
                // 返回当前连接下载的数据大小,也就是此次数据传输大小  
            }
            response.addHeader("Content-Length", "" + (responseSize));  
            response.setStatus(status);
            if(status == HttpServletResponse.SC_PARTIAL_CONTENT){  
                // 设置断点续传的Content-Range传输字节和总字节  
            	response.addHeader("Content-Range", "bytes " + start + "-" + end + "/" + (f.length()+1));  
            }  
            // 设置响应客户端内容类型  
            response.setContentType("application/octet-stream");
            // 当前需要下载文件的大小  
            int needSize = (int)responseSize;  
            is = new RandomAccessFile(nfn, "rw");
            os = response.getOutputStream();  
            if(start != 0){  
                // 跳过已经传输的字节  
            	is.seek(start);
            }  
            byte buffer[] = new byte[256 * 1024];
            while (needSize > 0) {  
                int len = is.read(buffer);  
                if (needSize < buffer.length) {  
                    os.write(buffer, 0, needSize);  
                } else {  
                    os.write(buffer, 0, len);  
                    // 如果读取文件大小小于缓冲字节大小，表示已写入完，直接跳出  
                    if (len < buffer.length) {  
                        break;  
                    }  
                }  
                // 不断更新当前可下载文件大小  
                needSize -= buffer.length;  
            }  
        } catch (Exception e) {
        	if(!(e instanceof ClientAbortException)){
        		MyLogger.log(e);
        		e.printStackTrace();
        	}
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
            	os.close();
            }
        }
    }
    
    public boolean checkRight(HttpServletRequest request){
    	HttpSession session = request.getSession();
        ClientSession cs = (ClientSession) session.getAttribute("clientSession");
        String token = request.getParameter("token");
        UserAppService us = SpringHelper.getSpringBean("UserAppService");
        String id = userTokenDAO.getIdByToken(token);
		BbsUser bu = this.cuDAO.findBbsUser(id);
        if (cs == null&&bu == null) {
            return false;
        } else {
        	if(bu == null){
        		bu = cs.getUsr();
        	}
            if (bu == null) {
                return false;
            }
        }
        return true;
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
    
    public static void main(String[] args) {
    	String range="9617400-30408703";
    	String[] values = range.split("=")[1].split("-");  
        long start = Integer.parseInt(values[0]);  
        long end;
        // 如果服务器端没有设置end结尾，默认取下载全部  
        if(values.length == 1){  
            end = 50846333;  
        } else {  
            end = Integer.parseInt(values[1]);  
        }  
	}
}
