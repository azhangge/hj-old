package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.hazelcast.util.CollectionUtil;
import com.hjedu.common.dao.LogoDAO;
import com.hjedu.platform.entity.Logo;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.Cat;
import com.huajie.util.CookieUtils;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AddLogo implements Serializable {
	private static final long serialVersionUID = 1L;
	ILogService logger = SpringHelper.getSpringBean("LogService");
    Logo adv = null;
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    LogoDAO advDAO = SpringHelper.getSpringBean("LogoDAO");

    @PostConstruct
    public void init() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String id = request.getParameter("id");
        if (id != null) {
            this.adv = advDAO.findById(id);
        }
    }

    public Logo getAdv() {
        return this.adv;
    }

    public void setAdv(Logo adv) {
        this.adv = adv;
    }


    public String button1_action() {
    	this.adv.setAdminId(ExternalUserUtil.getAdminBySession().getId());
    	this.adv.setBusinessId(CookieUtils.getBusinessId(JsfHelper.getRequest()));
    	
//    	Map<String, Object> map = advDAO.getScrollData(Logo.class, 0, 1, null, null, null);
//    	@SuppressWarnings("unchecked")
//		List<Logo> list = (List<Logo>) map.get("list");
//    	
//    	if(CollectionUtil.isEmpty(list)){
//    		advDAO.add(this.adv);
//    	}else{
//    		advDAO.update(this.adv);
//    	}
    	
    	if(advDAO.findLogo(this.adv.getId())==null){
    		advDAO.add(this.adv);
    	}else{
    		advDAO.update(this.adv);
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
            	this.adv = new Logo();
            }
            this.adv.setSrc(picUrl);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }
}