package com.hjedu.course.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.entity.Lesson;
import com.hjedu.course.entity.LessonType;
import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.JsonUtil;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListLessonType implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    ILessonTypeDAO lessonTypeDAO = SpringHelper.getSpringBean("LessonTypeDAO");
    List<LessonType> lessonTypes;

    public List<LessonType> getLessonTypes() {
        return lessonTypes;
    }

    public void setLessonTypes(List<LessonType> lessonTypes) {
        this.lessonTypes = lessonTypes;
    }

    @PostConstruct
    public void init() {
    	AdminSessionBean asb = JsfHelper.getBean("adminSessionBean");
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    	if(asb!=null){
    		if(asb.isIfSuper()){
    			this.lessonTypes = this.lessonTypeDAO.findAllLessonType(businessId);
    		}else{
    			AdminInfo ai = asb.getAdmin();
    			if (ai != null) {
    				this.lessonTypes = this.lessonTypeDAO.findLessonTypeByAdmin(ai,businessId);
    			}
    		}
    	}
//        this.lessonTypes = this.lessonTypeDAO.findAllLessonType();
    }

    public void delete(String id) {
        LessonType er=this.lessonTypeDAO.findLessonType(id);
        this.logger.log("删除了课程类别："+er.getName());
        this.lessonTypeDAO.deleteLessonType(id);
        init();
    }
    
    
    public String editOrd(String id) {
        for (LessonType cq : this.lessonTypes) {
            if (id.equals(cq.getId())) {
                this.lessonTypeDAO.updateLessonType(cq);
                break;
            }
        }
        return null;
    }
    
    public void someAbleUser(String id) {
    	LessonType lt = lessonTypeDAO.findLessonType(id);
        if (lt.isEnabled()) {
            this.logger.log("禁用了管理员：" + lt.getName());
            lt.setEnabled(false);
        } else {
        	lt.setEnabled(true);
            this.logger.log("激活了管理员：" + lt.getName());
        }
        lessonTypeDAO.updateLessonType(lt);
        for (LessonType b : this.lessonTypes) {
            if (b.getId().equals(id)) {
                if (b.isEnabled()) {
                    b.setEnabled(false);
                } else {
                    b.setEnabled(true);
                }
                break;
            }
        }
    }
    
    public void createZip(String id){
    	FacesMessage fm = new FacesMessage();
    	fm.setSeverity(FacesMessage.SEVERITY_INFO);
    	if(JsonUtil.createJsonZipByLessonTypeId(JsfHelper.getRequest(),id)){
    		fm.setSummary("生成文件成功！");
    	}else{
    		fm.setSummary("生成文件失败，请联系后台人员查看原因！");
    	}
    	FacesContext.getCurrentInstance().addMessage("", fm);
    }
    
    public void createZipAll(){
    	FacesMessage fm = new FacesMessage();
    	fm.setSeverity(FacesMessage.SEVERITY_INFO);
    	try {
    		List<LessonType> ls = lessonTypeDAO.findAllLessonType(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        	for(LessonType l : ls){
        		JsonUtil.createJsonZipByLessonTypeId(JsfHelper.getRequest(),l.getId());
        	}
        	fm.setSummary("生成全部课程文件成功！");
		} catch (Exception e) {
			fm.setSummary("生成文件失败，请联系后台人员查看原因！");
		}
    	FacesContext.getCurrentInstance().addMessage("", fm);
    }
}
