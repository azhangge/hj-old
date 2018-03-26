package com.hjedu.platform.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.service.impl.WordImgService;
import com.huajie.util.SpringHelper;

@WebServlet(name = "WordImageUploader", urlPatterns = {"/servlet/WordImageUploader"})
public class WordImageUploader extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String ext = ".png";

        ISystemConfigDAO sys = SpringHelper.getSpringBean("SystemConfigDAO");

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
        if (!(dir.endsWith("\\") || dir.endsWith("/"))) {
            dir = dir + "/";
        }

        File f_dir0 = new File(dir);
        if (!f_dir0.exists()) {
            f_dir0.mkdir();
        }

        dir = dir + "word_images";

        File f_dir = new File(dir);
        if (!f_dir.exists()) {
            f_dir.mkdir();
        }


        String name = request.getParameter("unique_name");
        Map map = request.getParameterMap();
        Set<String> keys = map.keySet();
        for (String s : keys) {
            System.out.println(s + ":" + map.get(s));
        }
        if (name == null) {
            name = UUID.randomUUID().toString();
        }

        String nfn = dir + "/" + name + ext;
        System.out.println(nfn);
        try {
            // 将文件流转换为加密过的文件流
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload fu = new ServletFileUpload(factory);
            fu.setSizeMax(Long.MAX_VALUE); // 设置允许用户上传文件大小,单位:字节

            //fu.setSizeThreshold(4096); // 设置最多只允许在内存中存储的数据,单位:字节
            //fu.setRepositoryPath(rp); //

            // 设置一旦文件大小超过getSizeThreshold()的值时数据存放在硬盘的目录

            // 开始读取上传信息
            List fileItems = fu.parseRequest(request);
            Iterator iter = fileItems.iterator(); // 依次处理每个上传的文件

            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();// 忽略其他不是文件域的所有表单信息
                if (!item.isFormField()) {
                    System.out.println("发现文件");
                    // InputStream fis = item.getInputStream();
                    OutputStream fos = new FileOutputStream(nfn);
                    InputStream fis = item.getInputStream();
                    System.out.println(item.getFieldName());
                    System.out.println(item.getSize());
                    System.out.println(item.getName());
                    // DESTool des = new DESTool();
                    // OutputStream cos = des.encryptOutputStream(fos);//
                    BufferedInputStream is = new BufferedInputStream(fis);
                    BufferedOutputStream os = new BufferedOutputStream(fos);// 将加密过的文件流转换为缓冲流

                    IOUtils.copy(is, os);
                    os.flush();
                    //cos.flush();
                    fos.flush();

                    os.close();
                    is.close();
                    fis.close();
                    WordImgService s = SpringHelper.getSpringBean("WordImgService");
                    s.transferWordImgToRelativeDir(this.getServletContext(), name + ext);
                    break;
                }
            }
            String host = request.getServerName();
            int port = request.getServerPort();
            String path = request.getContextPath();
            String data = "http://" + host + ":" + port + path + "/word_images/" + name + ext;
            out.print(data);

            //将图片由原始保存地址复制到网站目录以备用

        } catch (Exception eee) {
            String data = "error";
            out.print(data);
            eee.printStackTrace();
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
