/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hjedu.platform.service.impl;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.servlet.ServletContext;
import org.apache.commons.io.IOUtils;

import com.hjedu.platform.dao.ISystemConfigDAO;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;


public class WordImgService  implements Serializable{

    
    private String relativeDir = SpringHelper.getSpringBean("relativeWordImg");

    public void deleteRelativeWordImg(ServletContext sc, String id) {
        String path = sc.getRealPath(relativeDir);
        path = path + "/" + id ;
        File ff1 = new File(path);
        if (ff1.exists()) {
            ff1.delete();
        }
    }

    public void transferAllWordImgs(ServletContext sc) {
        StringBuilder dir = new StringBuilder();
        dir.append(this.loadOriginalFilePath(sc));
        //dir.append("c:\\tt\\");
        dir.append("word_images/");
        String nfn = dir.toString();
        //MyLogger.echo(nfn);
        File fff = new File(nfn);
        String fs[] = fff.list();
        if (fs != null) {
            for (String s : fs) {
                MyLogger.echo(s);
                this.transferWordImgToRelativeDir(sc, s);
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
            path = path + "/" + id ;
            File ff1 = new File(path);
            if (ff1.exists()) {
                return;
            }
            out = new BufferedOutputStream(new FileOutputStream(path));
            IOUtils.copy(is, out);
            is.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void transferWordImgToRelativeDir(ServletContext sc, String id) {
        try {

            InputStream is = null;
            OutputStream out = null;
            //byte[] buf = new byte[bufLen];


            //获得原图片文件的流
            StringBuilder dir = new StringBuilder();
            dir.append(this.loadOriginalFilePath(sc));
            dir.append("word_images/");
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
            path = path + "/" + id ;
            File ff1 = new File(path);
            if (ff1.exists()) {
                return;
            }
            out = new BufferedOutputStream(new FileOutputStream(path));

            IOUtils.copy(is, out);
            is.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String loadOriginalFilePath(ServletContext sc) {
        String dir = "";
        ISystemConfigDAO scd = SpringHelper.getSpringBean("SystemConfigDAO");
        boolean b = scd.getSystemConfig().getIfRelative();
        if (b) {
            String tp = scd.getSystemConfig().getFilePath();
            if (!tp.endsWith("/")) {
                tp = tp + "/";
            }

            dir = sc.getRealPath(tp);
        } else {
            dir = scd.getSystemConfig().getFilePath();
        }
        if (!(dir.endsWith("\\")||dir.endsWith("/"))) {
            dir = dir + "/";
        }
        return dir;
    }

    public static void main(String args[]) {
        //ImageService.transferAllUserImages(null);
    }
}
