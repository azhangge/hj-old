package com.hjedu.platform.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.hjedu.platform.dao.IAdvDAO;
import com.hjedu.platform.dao.IImgDAO;
import com.hjedu.platform.entity.AdvModel;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.Cat;
import com.huajie.util.CookieUtils;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAdv implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    AdvModel adv = null;
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    IAdvDAO advDAO = SpringHelper.getSpringBean("AdvDAO");
    IImgDAO imgDAO = SpringHelper.getSpringBean("ImgDAO");
    //FileUpload fu = new FileUpload();

    @PostConstruct
    public void init() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String id = request.getParameter("id");
        if (id != null) {
            this.adv = advDAO.findAdv(id);
        }
    }

    public AdvModel getAdv() {
        return this.adv;
    }

    public void setAdv(AdvModel adv) {
        this.adv = adv;
    }


    public String button1_action() {
    	this.adv.setAdminId(ExternalUserUtil.getAdminBySession().getId());
    	this.adv.setBusinessId(CookieUtils.getBusinessId(JsfHelper.getRequest()));
//    	if(advDAO.findAdvByAdminId().size()==0){
//    		advDAO.addAdvModel(this.adv);
//    	}else{
//    		advDAO.updateAdvModel(this.adv);
//    	}
    	
    	if(advDAO.findAdv(this.adv.getId())==null){
    		advDAO.addAdvModel(this.adv);
    	}else{
    		advDAO.updateAdvModel(this.adv);
    	}
        logger.log("修改了系统图片："+this.adv.getName()+",ID:"+this.adv.getId());
        return "ListAdv?faces-redirect=true";
    }

    public void up_action(FileUploadEvent event) {
        try {
            UploadedFile item = event.getFile();
            String n = item.getFileName();
            int l2 = n.lastIndexOf(".");
            String ext = "." + n.substring(l2 + 1).toLowerCase();
            String imgId = Cat.getUniqueId();
            
			AdminNewFile.saveFile(item, ext,imgId);
            MyLogger.echo("upload executed.");
            String picUrl = "servlet/ShowAbstractImage?id=" + imgId;
            if(this.adv==null){
            	this.adv = new AdvModel();
            }
            this.adv.setSrc(picUrl);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }
}