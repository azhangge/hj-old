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


public class Mp3Service  implements Serializable{

    
    private String relativeDir = SpringHelper.getSpringBean("relativeDir");

    public void deleteRelativeMp3(ServletContext sc, String id) {
        String path = sc.getRealPath(relativeDir);
        path = path + "/" + id + ".mp3";
        File ff1 = new File(path);
        if (ff1.exists()) {
            ff1.delete();
        }
    }

    public void transferAllUserMp3s(ServletContext sc) {
        StringBuilder dir = new StringBuilder();
        dir.append(this.loadOriginalFilePath(sc));
        //dir.append("c:\\tt\\");
        dir.append("user_mp3s/");
        String nfn = dir.toString();
        //MyLogger.echo(nfn);
        File fff = new File(nfn);
        String fs[] = fff.list();
        if (fs != null) {
            for (String s : fs) {
                MyLogger.echo(s);
                this.transferUserMp3sToRelativeDir(sc, s);
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
            path = path + "/" + id + ".mp3";
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

    public void transferUserMp3sToRelativeDir(ServletContext sc, String id) {
        try {

            InputStream is = null;
            OutputStream out = null;
            //byte[] buf = new byte[bufLen];


            //获得原图片文件的流
            StringBuilder dir = new StringBuilder();
            dir.append(this.loadOriginalFilePath(sc));
            dir.append("user_mp3s/");
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
            path = path + "/" + id + ".mp3";
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
