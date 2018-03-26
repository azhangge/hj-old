package com.hjedu.course.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.course.dao.ILessonTypeLogDAO;
import com.hjedu.course.entity.LessonLog;
import com.hjedu.course.entity.LessonTypeLog;
import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class LessonTypeLogList implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ILogService logger = SpringHelper.getSpringBean("LogService");
    ILessonTypeLogDAO logDAO = SpringHelper.getSpringBean("LessonTypeLogDAO");
    List<LessonTypeLog> logs;

    public List<LessonTypeLog> getLogs() {
        return logs;
    }

    public void setLogs(List<LessonTypeLog> logs) {
        this.logs = logs;
    }

    
    @PostConstruct
    public void init() {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    	this.logs = this.logDAO.findAllLessonTypeLog(businessId);
    }
    
    public String delete(String id) {
        LessonTypeLog ll=this.logDAO.findById(id);
        this.logger.log("删除了考生："+ll.getUser().getName()+"的"+ll.getLessonType().getName()+" 课程记录");
        this.logDAO.delete(id);
        this.logs = this.logDAO.findAllLessonTypeLog(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        return null;
    }

    public String deleteAll() {
        this.logger.log("清空了所有课程记录");
        this.logDAO.deleteAll();
        this.init();
        return null;
    }
    
    
    public void batchDel() {
        for (LessonTypeLog c : this.logs) {
            if (c.isSelected()) {
                this.logger.log("删除了一条课程记录，课程及用户是：" + c.getLessonType().getName() + "," + c.getUser().getName());
                this.logDAO.delete(c.getId());
            }
        }
        this.init();
    }
}
