package com.hjedu.customer.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.service.impl.BbsUserImportService;
import com.hjedu.customer.service.impl.ComplexBbsUserService;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class UserImportExport implements Serializable {
    
    ILogService logger = SpringHelper.getSpringBean("LogService");
    BbsUserImportService eis = SpringHelper.getSpringBean("BbsUserImportService");
    ComplexBbsUserService cbs = SpringHelper.getSpringBean("ComplexBbsUserService");
    
    @PostConstruct
    public void init() {
    }
    
    public List up_action(FileUploadEvent event) {
        try {
            UploadedFile item = event.getFile();
            String n = item.getFileName();
            int l2 = n.lastIndexOf(".");
            String ext = "." + n.substring(l2 + 1).toLowerCase();
            long imgId = System.nanoTime();
            String sn = imgId + ext;
            InputStream fis = item.getInputstream();
            byte[] bb = new byte[1024];
            String tp = "upload/";
            String dir = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(tp);
            if (!dir.endsWith("\\")) {
                dir = dir + "\\";
            }
            
            String nfn = dir + sn;
            File ffff = new File(nfn);
            FileOutputStream fos = new FileOutputStream(ffff);
            BufferedInputStream is = new BufferedInputStream(fis);
            BufferedOutputStream os = new BufferedOutputStream(fos);// 将加密过的文件流转换为缓冲流
            int len = 0;
            while ((len = is.read(bb)) != -1) {
                os.write(bb, 0, len);
            }
            is.close();
            os.close();
            fis.close();
            fos.close();
            MyLogger.echo("upload executed.");
            this.eis.import1(nfn);
            
            FacesMessage fm = new FacesMessage();
            fm.setSeverity(FacesMessage.SEVERITY_INFO);
            fm.setSummary("导入用户完成！请整体刷新以显示用户！");
            FacesContext.getCurrentInstance().addMessage("", fm);
            
            File ff = new File(nfn);
            ff.delete();
            this.logger.log("导入了用户");
            return null;
        } catch (Exception ee) {
            ee.printStackTrace();
            FacesMessage fm = new FacesMessage();
            fm.setSeverity(FacesMessage.SEVERITY_FATAL);
            fm.setSummary("导入用户出错了！请仔细核对您的用户文件格式是否正确");
            FacesContext.getCurrentInstance().addMessage("", fm);
            this.logger.log("试图导入用户，但导入出错");
            return null;
        }
    }
    
    public String clearAllUsers() {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        this.logger.log("清空了所有用户");
        IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
        List<BbsUser> users = bbsUserDAO.findAllBbsUser(businessId);
        for (BbsUser bu : users) {
            try {
                this.cbs.deleteBbsUserSafely(bu.getId());
            } catch (Exception e) {
            	
            }
        }
        FacesMessage fm = new FacesMessage();
        fm.setSeverity(FacesMessage.SEVERITY_INFO);
        fm.setSummary("清空用户完成！所有和用户相关的考试、发言、文件、日志都被删除了！");
        FacesContext.getCurrentInstance().addMessage("", fm);
        return null;
    }
}
