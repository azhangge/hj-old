package com.hjedu.platform.controller;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

import com.huajie.util.SpringHelper;

public class ShowAbstractImage1 extends HttpServlet {

    /**
     * Constructor of the object.
     */
    //static IAdminFileDAO cfb = SpringHelper.getSpringBean("AdminFileDAO");
    //static ISystemConfigDAO sys = SpringHelper.getSpringBean("SystemConfigDAO");
    public ShowAbstractImage1() {
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

            String relativePath = SpringHelper.getSpringBean("relativeDir").toString() + id + ".jpg";

            String path = request.getServletContext().getRealPath(relativePath);
            File fff = new File(path);
            if (fff.exists()) {
                System.out.println(relativePath);

                RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/" + relativePath);
                rd.forward(request, response);
                //return;
            } else {
                response.setContentType("image/JPEG");
                OutputStream out = response.getOutputStream();

                StringBuilder dir = new StringBuilder();
                ApplicationBean ab = (ApplicationBean) request.getServletContext().getAttribute("applicationBean");
                dir.append(ab.getFilePath());
                dir.append("\\");
                dir.append(id);
                String nfn = dir.toString();
                //System.out.println(nfn);
                try {
                    is = new BufferedInputStream(new FileInputStream(nfn));
                } catch (Exception e) {
                    String fp = request.getServletContext().getRealPath("resources/sys/default.png");
                    //System.out.println(fp);
                    is = new FileInputStream(fp);
                    is = new BufferedInputStream(is);
                }

                try {
                    src = ImageIO.read(is);
                    int width = src.getWidth(null);
                    int height = src.getHeight(null);
                    image = new BufferedImage(width, height,
                            BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = image.createGraphics();
                    image.getGraphics().drawImage(src, 0, 0, null);
                    g.drawImage(src, 0, 0, null);
                    g.dispose();
                    g = null;

                    int w = 600;
                    String ww = request.getParameter("w");
                    if (ww != null) {
                        w = Integer.parseInt(ww);
                    }

                    bimage = this.getResizePicture(image, w);

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ImageOutputStream imOut = ImageIO.createImageOutputStream(bos);
                    ImageIO.write(bimage, "jpg", imOut);   //scaledImage1为BufferedImage，jpg为图像的类型 

                    InputStream bis = new ByteArrayInputStream(bos.toByteArray());
                    //使用这样的方法可以让图片逐渐输出，避免ImageIO.write直接输出向response.getOutputStream造成的网速慢时图片不显示的问题。
                    bos.close();
                    IOUtils.copy(bis, out);
                    bis.close();
                    bos = null;
                    imOut = null;
                    bis = null;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    is.close();
                    src = null;
                    image = null;
                    bimage = null;
                    out.flush();
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.flushBuffer();
                    out.close();

                }


            }
        }

    }

    public final BufferedImage getResizePicture(BufferedImage originalPic,
            int w) {

        // 获得原始图片的宽度。   
        int originalImageWidth = originalPic.getWidth();
        // 获得原始图片的高度。   
        int originalImageHeight = originalPic.getHeight();

        double bo = ((double) w) / ((double) originalImageWidth);

        // 根据缩放比例获得处理后的图片宽度。  
        //System.out.println(originalImageHeight + ":" + bo);
        int changedImageWidth = w;
        // 根据缩放比例获得处理后的图片高度。   
        int changedImageHeight = (int) (originalImageHeight * bo);

        // 生成处理后的图片存储空间。   
        BufferedImage changedImage = new BufferedImage(changedImageWidth,
                changedImageHeight, originalPic.getType());

        // double widthBo = (double) yourWidth / originalImageWidth;   
        // double heightBo = (double) yourHeightheight / originalImageHeight;   
        // 宽度缩放比例。   
        //double widthBo = bo;
        // 高度缩放比例。   
        //double heightBo = bo;

//        AffineTransform transform = new AffineTransform();
//        transform.setToScale(widthBo, heightBo);
//
//        // 根据原始图片生成处理后的图片。   
//        AffineTransformOp ato = new AffineTransformOp(transform, null);
//        ato.filter(originalPic, changedImage);
        // 返回处理后的图片   
        changedImage.getGraphics().drawImage(originalPic, 0, 0, changedImageWidth, changedImageHeight, null); //绘制缩小后的图
        return changedImage;
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
