package com.hjedu.platform.dao.impl;


import java.io.*;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import com.hjedu.platform.dao.IMp3DAO;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.service.impl.Mp3Service;
import com.huajie.util.Cat;
import com.huajie.util.SpringHelper;

public class Mp3DAO implements IMp3DAO , Serializable {
    
    ISystemConfigDAO systemConfigDAO;

    public ISystemConfigDAO getSystemConfigDAO() {
        return systemConfigDAO;
    }

    public void setSystemConfigDAO(ISystemConfigDAO systemConfigDAO) {
        this.systemConfigDAO = systemConfigDAO;
    }

    public String saveMp3(File localFile) {
        try {
            File f0 = localFile;
            byte[] bb = new byte[512];
            int i = 0;
            InputStream is = new FileInputStream(f0);
            String ofn = f0.getName();
            int t = ofn.lastIndexOf('.');
            String ext = ofn.substring(t, ofn.length());
            String ext1 = ".pip2";
            String nfn = "/resources/sys/" + Cat.getUniqueId() + ext1;
            String afp = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(nfn);
            File f1 = new File(afp);
            FileOutputStream fos = new FileOutputStream(f1);
            while ((i = is.read(bb)) != -1) {
                fos.write(bb, 0, i);
            }
            is.close();
            fos.close();
            return nfn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveMp3(InputStream fis, String  id) {
        try {
            byte[] bb = new byte[1024];
            String dir = "";
            boolean f = this.systemConfigDAO.getSystemConfig().getIfRelative();
            if (f) {
                String tp = this.systemConfigDAO.getSystemConfig().getFilePath();
                if (!tp.endsWith("/")) {
                    tp = tp + "/";
                }
                dir = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(tp);

            } else {
                dir = this.systemConfigDAO.getSystemConfig().getFilePath();

            }
            if (!(dir.endsWith("\\")||dir.endsWith("/"))) {
                dir = dir + "/";
            }
            
            File f_dir0 = new File(dir);
            if (!f_dir0.exists()) {
                f_dir0.mkdir();
            }
            
            dir=dir+"user_mp3s";

            File f_dir = new File(dir);
            if (!f_dir.exists()) {
                f_dir.mkdir();
            }
            System.out.println("用户MP3存储路径：" + dir);

            
            String nfn = dir +"/" + id;
            File ffff=new File(nfn);
            FileOutputStream fos = new FileOutputStream(ffff);
            // DESTool des = new DESTool();
            // OutputStream cos = des.encryptOutputStream(fos);// 将文件流转换为加密过的文件流
            BufferedInputStream is = new BufferedInputStream(fis);
            BufferedOutputStream os = new BufferedOutputStream(fos);// 将加密过的文件流转换为缓冲流
            int len = 0;
            while ((len = is.read(bb)) != -1) {
                os.write(bb, 0, len);
            }
            is.close();
            os.close();
            fis.close();
            // cos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delMp3(String id) {
        String dir = "";
        boolean f = this.systemConfigDAO.getSystemConfig().getIfRelative();
        if (f) {
            String tp = this.systemConfigDAO.getSystemConfig().getFilePath();
            if (!tp.endsWith("/")) {
                tp = tp + "/";
            }
            dir = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(tp);
        } else {
            dir = this.systemConfigDAO.getSystemConfig().getFilePath();
        }
        if (!(dir.endsWith("\\")||dir.endsWith("/"))) {
            dir = dir + "/";
        }
        String rp = dir;
        String nfn = rp + "/" + id;
        File f1 = new File(nfn);
        if (f1.exists()) {
            f1.delete();
        }
        //将MP3专用文件夹中的文件删除
        String nfn2 = rp + "user_mp3s/" + id;
        File f2 = new File(nfn2);
        if (f2.exists()) {
            f2.delete();
        }
        
        Mp3Service iss=SpringHelper.getSpringBean("Mp3Service");
        iss.deleteRelativeMp3(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()), id);
        
    }

    
    
    
}
