package com.hjedu.platform.service.impl;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.ServletContext;
import org.apache.commons.io.IOUtils;

import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.entity.SystemConfig;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

public class ImageService implements Serializable {

    public int MAX_IMAGE_WIDTH = 960;
    private String relativeDir = SpringHelper.getSpringBean("relativeDir");

    public void deleteRelativeImage(ServletContext sc, String id) {
        String path = sc.getRealPath(relativeDir);
        path = path + "/" + id + ".jpg";
        File ff1 = new File(path);
        if (ff1.exists()) {
            ff1.delete();
        }
    }

    public void transferAllUserImages(ServletContext sc) {
        StringBuilder dir = new StringBuilder();
        dir.append(this.loadOriginalFilePath(sc));
        //dir.append("c:\\tt\\");
        dir.append("user_images/");
        String nfn = dir.toString();
        //MyLogger.echo(nfn);
        File fff = new File(nfn);
        String fs[] = fff.list();
        if (fs != null) {
            for (String s : fs) {
                MyLogger.echo(s);
                this.transferUserImagesToRelativeDir(sc, s);
            }
        }
    }

    public void transferToRelativeDir(ServletContext sc, String id) {
        try {
            Image src = null;
            BufferedImage image = null;
            BufferedImage bimage = null;
            InputStream is = null;
            OutputStream out = null;
            //byte[] buf = new byte[bufLen];

            //获得原图片文件的流
            StringBuilder dir = new StringBuilder();
            dir.append(this.loadOriginalFilePath(sc));
            dir.append(id);
            String nfn = dir.toString();
            //MyLogger.echo(nfn);
            File fff = new File(nfn);
            if (!fff.exists()) {
                return;
            }
            is = new BufferedInputStream(new FileInputStream(fff));

            //获得到相对图片文件的流
            String path = sc.getRealPath(relativeDir);
            path = path + "/" + id + ".jpg";
            File ff1 = new File(path);
            if (ff1.exists()) {
                return;
            }
            out = new BufferedOutputStream(new FileOutputStream(path));

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

            bimage = this.getResizePicture(image, MAX_IMAGE_WIDTH);

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
            is.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 从C:/huajie_exam/user_images中复制到D:\apache-tomcat-7.0.63\wtpwebapps\exam2\images\content
     * @param sc
     * @param id
     */
    public void transferUserImagesToRelativeDir(ServletContext sc, String id) {
        try {
            Image src = null;
            BufferedImage image = null;
            BufferedImage bimage = null;
            InputStream is = null;
            OutputStream out = null;
            //byte[] buf = new byte[bufLen];

            //获得原图片文件的流
            StringBuilder dir = new StringBuilder();
            dir.append(this.loadOriginalFilePath(sc));
            dir.append("user_images/");
            dir.append(id);
            String nfn = dir.toString();
            //MyLogger.echo(nfn);
            File fff = new File(nfn);
            if (!fff.exists()) {
                return;
            }
            is = new BufferedInputStream(new FileInputStream(fff));

            //获得到相对图片文件的流
            String path = sc.getRealPath(relativeDir);
            path = path + "/" + id + ".jpg";
            File ff1 = new File(path);
            if (ff1.exists()) {
                return;
            }
            out = new BufferedOutputStream(new FileOutputStream(path));

//            src = ImageIO.read(is);
//            int width = src.getWidth(null);
//            int height = src.getHeight(null);
//            image = new BufferedImage(width, height,
//                    BufferedImage.TYPE_INT_RGB);
//            Graphics2D g = image.createGraphics();
//            image.getGraphics().drawImage(src, 0, 0, null);
//            g.drawImage(src, 0, 0, null);
//            g.dispose();
//            g = null;
//
//
//            //bimage = this.getResizePicture(image, MAX_IMAGE_WIDTH);
//            bimage=image;
//
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            ImageOutputStream imOut = ImageIO.createImageOutputStream(bos);
//            ImageIO.write(bimage, "jpg", imOut);   //scaledImage1为BufferedImage，jpg为图像的类型 
//
//            InputStream bis = new ByteArrayInputStream(bos.toByteArray());
            //使用这样的方法可以让图片逐渐输出，避免ImageIO.write直接输出向response.getOutputStream造成的网速慢时图片不显示的问题。
            //bos.close();
            IOUtils.copy(is, out);
            //bis.close();
            //bos = null;
            //imOut = null;
            //bis = null;
            is.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final BufferedImage getResizePicture(BufferedImage originalPic,
            int w) {
        // 获得原始图片的宽度。   
        int originalImageWidth = originalPic.getWidth();
        // 获得原始图片的高度。   
        int originalImageHeight = originalPic.getHeight();
        if (originalImageWidth < w) {
            return originalPic;
        } else {
            double bo = ((double) w) / ((double) originalImageWidth);
            // 根据缩放比例获得处理后的图片宽度。  
            //MyLogger.echo(originalImageHeight + ":" + bo);
            int changedImageWidth = w;
            // 根据缩放比例获得处理后的图片高度。   
            int changedImageHeight = (int) (originalImageHeight * bo);
            // 生成处理后的图片存储空间。   
            BufferedImage changedImage = new BufferedImage(changedImageWidth,
                    changedImageHeight, originalPic.getType());
            changedImage.getGraphics().drawImage(originalPic, 0, 0, changedImageWidth, changedImageHeight, null); //绘制缩小后的图
            return changedImage;
        }
    }

    private String loadOriginalFilePath(ServletContext sc) {
        String dir = "";
        ISystemConfigDAO scd = SpringHelper.getSpringBean("SystemConfigDAO");
        SystemConfig sc0 = scd.getSystemConfig();
        if (sc == null) {
            sc0 = new SystemConfig();
        }
        boolean b = sc0.getIfRelative();
        if (b) {
            String tp = scd.getSystemConfig().getFilePath();
            if (!tp.endsWith("/")) {
                tp = tp + "/";
            }

            dir = sc.getRealPath(tp);
        } else {
            dir = scd.getSystemConfig().getFilePath();
        }
        if (!(dir.endsWith("\\") || dir.endsWith("/"))) {
            dir = dir + "/";
        }
        return dir;
    }

    public static void main(String args[]) {
        //ImageService.transferAllUserImages(null);
    }
}
