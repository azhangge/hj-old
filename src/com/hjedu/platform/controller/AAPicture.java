package com.hjedu.platform.controller;


import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.entity.LessonType;
import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.customer.dao.IAdminDAO;
import com.hjedu.customer.dao.impl.AdminDAO;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.platform.dao.IImgDAO;
import com.hjedu.platform.dao.IPictureDAO;
import com.hjedu.platform.entity.PictureModel;
import com.hjedu.platform.service.impl.ImageService;
import com.huajie.util.Cat;
import com.huajie.util.CookieUtils;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAPicture  implements Serializable{

    PictureModel picture = new PictureModel();
    IPictureDAO pictureDAO = SpringHelper.getSpringBean("PictureDAO");
    IImgDAO imgDAO = SpringHelper.getSpringBean("ImgDAO");
    IAdminDAO adminDAO = SpringHelper.getSpringBean("AdminDAO");
    boolean flag = false;
    String nothing;
    FileUpload fu = new FileUpload();
    List<LessonType> lessonTypes;
    
    @ManagedProperty(value = "#{adminSessionBean}")
    AdminSessionBean adminSessionBean;

    public List<LessonType> getLessonTypes() {
		return lessonTypes;
	}

	public void setLessonTypes(List<LessonType> lessonTypes) {
		this.lessonTypes = lessonTypes;
	}

	public String getNothing() {
        return Cat.getUniqueId();
    }

    public void setNothing(String nothing) {
        this.nothing = nothing;
    }

    public AdminSessionBean getAdminSessionBean() {
        return adminSessionBean;
    }

    public void setAdminSessionBean(AdminSessionBean adminSessionBean) {
        this.adminSessionBean = adminSessionBean;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public FileUpload getFu() {
        return fu;
    }

    public void setFu(FileUpload fu) {
        this.fu = fu;
    }

    public PictureModel getPicture() {
        return picture;
    }

    public void setPicture(PictureModel picture) {
        this.picture = picture;
    }

    public AAPicture() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        String id = req.getParameter("id");
        ILessonTypeDAO lessonTypeDAO = SpringHelper.getSpringBean("LessonTypeDAO");
        this.lessonTypes = lessonTypeDAO.findAllEnableLessonType(businessId);
        if (id != null) {
            this.picture = this.pictureDAO.findPictureModel(id);
            String lessonId = this.picture.getLessonTypeId();
            if(lessonId!=null&&!lessonId.equals("")){
            	for(LessonType lt : this.lessonTypes){
            		if(lt.getId().equals(picture.getLessonTypeId())){
            			lt.setSelected(true);
            			break;
            		}
            	}
            }
            this.flag = true;
        }
    }

    public String save_action() {
    	HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        PictureModel pp = this.picture;
        pp.setBusinessId(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        AdminInfo ai = ExternalUserUtil.getAdminBySession();
        pp.setAdminId(ai.getId());
        if(pp.getLinkType().equals("inner")){
        	for(LessonType lt : this.lessonTypes){
        		if(lt.isSelected()){
        			pp.setLessonTypeId(lt.getId());
        			pp.setLink("/talk/LessonList.jspx?tid="+lt.getId());
        			break;
        		}
        	}
        }else{
        	pp.setLessonTypeId("");
        	pp.setLink(pp.getLink());
        }
        try {
            if (!flag) {
                //picture.setGenTime(new Date());
                //picture.setEditor(this.adminSessionBean.getAdmin());
            	picture.setVersion(0);
                pictureDAO.addPictureModel(pp);
                //logger.log("添加了系统图片："+this.picture.getTitle()+",ID:"+this.picture.getId());
            } else {
            	picture.setVersion(picture.getVersion()+1);
                pictureDAO.updatePictureModel(pp);
                //logger.log("修改了系统图片："+this.picture.getTitle()+",ID:"+this.picture.getId());
            }
            adminDAO.setCarouselVersion(ai);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "ListPicture?faces-redirect=true";
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
            this.fu = new FileUpload();
            String picUrl = "servlet/ShowImage?id=" + imgId;
            this.picture.setUrl(picUrl);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

}