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

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.Examination;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.huajie.util.SpringHelper;

@WebServlet(urlPatterns = {"/DownloadFileQuestionAttach"})
public class DownloadFileQuestionAttach extends HttpServlet {

    ISystemConfigDAO sys = SpringHelper.getSpringBean("SystemConfigDAO");
    IBbsUserDAO cuDAO = SpringHelper.getSpringBean("BbsUserDAO");
    IExaminationDAO xDAO = SpringHelper.getSpringBean("ExaminationDAO");

    public DownloadFileQuestionAttach() {
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
        FileInputStream is = null;
        OutputStream os = null;
        try {
            response.setContentType("text/html;charset=UTF-8");
            String uid = request.getParameter("uid");
            String eid = request.getParameter("eid");

            BbsUser bu=null;
            if (uid != null) {
                bu = cuDAO.findBbsUser(uid);
            }
            Examination ex=null;
            if (eid != null) {
                ex = this.xDAO.findExamination(eid);
            }
            String id = request.getParameter("id");

            String dir = "";
            boolean b = sys.getSystemConfig().getIfRelative();
            if (b) {
                String tp = sys.getSystemConfig().getFilePath();
                if (!tp.endsWith("/")) {
                    tp = tp + "/";
                }
                dir = this.getServletContext().getRealPath(tp);
            } else {
                dir = sys.getSystemConfig().getFilePath();
            }
            if (!dir.endsWith("\\")) {
                dir = dir + "\\";
            }
            String rp = dir + "user_question_file";
            String nfn = rp + "\\" + id;

            File f = new File(nfn);
            String fn = id;

            String name1 = "";
            if (bu != null) {
                name1 = bu.getName() + "_";
            }
            String name2 = "";
            if (ex != null) {
                name1 = bu.getName() + "_";
            }

            String targetName = name1 + name2 + fn + ".rar";
            targetName = URLEncoder.encode(targetName, "UTF-8");
            //System.out.println(targetName);
            // the file to download.
            // the dialogbox of download file.

            String length = String.valueOf(f.length());
            response.setHeader("Content-Length", length);
            // set the MIME type.
            response.setContentType("application/octet-stream;charset=UTF-8");
            //response.setContentType("application/x-download");
            // get the file length.
            response.setHeader("Content-disposition", "attachment;filename="
                    + targetName);

            response.flushBuffer();//向客户端发达文件头信息，以免长时间等待，客户抓狂
            // download the file.

            is = new FileInputStream(f);
            os = response.getOutputStream();
            //os = new BufferedOutputStream(os);//servlet无需使用BufferedOutputStream,它对读硬盘数据有用
            //

            int bufLen = bufLen = 256 * 1024;
            System.out.println("文件大小：" + length);
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
