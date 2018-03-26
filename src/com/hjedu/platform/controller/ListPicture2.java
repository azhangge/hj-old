package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.platform.dao.IImgDAO;
import com.hjedu.platform.dao.IPictureDAO;
import com.hjedu.platform.entity.PictureModel;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListPicture2 implements Serializable {
    //ILogService logger = SpringHelper.getSpringBean("JsfLogService");

    List<PictureModel> pictureList = new ArrayList();
    IPictureDAO pictureDAO = SpringHelper.getSpringBean("PictureDAO");
    IImgDAO imgDAO = SpringHelper.getSpringBean("ImgDAO");

    public List<PictureModel> getPictureList() {
        return this.pictureList;
    }

    public void setPictureList(List<PictureModel> pictureList) {
        this.pictureList = pictureList;
    }

    public ListPicture2() {
        synchronizeDB();
    }

    public void delete(ActionEvent e) {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String id = req.getParameter("id");
        pictureDAO.deletePictureModel(id);
        synchronizeDB();
    }

    public void synchronizeDB() {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        this.pictureList = pictureDAO.findAllPictureModelByBusinessId(businessId);
    }
}