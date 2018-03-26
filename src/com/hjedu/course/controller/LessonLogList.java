package com.hjedu.course.controller;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.IteratorUtils;
import org.springframework.util.StringUtils;

import com.hjedu.course.dao.ILessonLogDAO;
import com.hjedu.course.entity.LessonLog;
import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.customer.dao.IBbsFileDAO;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.platform.entity.BbsFileModel;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class LessonLogList implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ILogService logger = SpringHelper.getSpringBean("LogService");
    ILessonLogDAO logDAO = SpringHelper.getSpringBean("LessonLogDAO");
    IBbsFileDAO bbsFileDAO = SpringHelper.getSpringBean("BbsFileDAO");
    List<LessonLog> logs;

    public List<LessonLog> getLogs() {
        return logs;
    }

    public void setLogs(List<LessonLog> logs) {
        this.logs = logs;
    }

    
    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        String businessId = CookieUtils.getBusinessId(request);
        AdminSessionBean asb = JsfHelper.getBean("adminSessionBean");
        if (asb != null) {
//            AdminInfo ai = asb.getAdmin();
//            if (ai != null) {
//                this.logs = this.logDAO.findLessonLogByAdmin(ai,businessId);
//            } else {
//                this.logs = this.logDAO.findAllLessonLog(businessId);
//            }
            this.logs = this.logDAO.findAllLessonLog(businessId);
            Iterator<LessonLog> its = this.logs.iterator();
            while(its.hasNext()){
            	LessonLog log = its.next();
            	BbsFileModel client = this.bbsFileDAO.findAllClientFileByLessionId(log.getLesson().getId());
        		if (client == null || StringUtils.isEmpty(client.getFileFullPath())) {
    				its.remove();
    			}
            }            
            this.logs = IteratorUtils.toList(its);            
        }
    }

    public String delete(String id) {
        LessonLog ll=this.logDAO.findLessonLog(id);
        this.logger.log("删除了考生："+ll.getUser().getName()+"的"+ll.getLesson().getName()+" 学习资料记录");
        this.logDAO.deleteLessonLog(id);
        this.logs = this.logDAO.findAllLessonLog(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        return null;
    }

    public String deleteAll() {
        this.logger.log("清空了所有学习资料记录");
        this.logDAO.deleteAll();
        this.init();
        return null;
    }
    
    
    public void batchDel() {
        for (LessonLog c : this.logs) {
            if (c.isSelected()) {
                this.logger.log("删除了一条学习资料记录，学习资料及用户是：" + c.getLesson().getName() + "," + c.getUser().getName());
                this.logDAO.deleteLessonLog(c.getId());
            }
        }
        this.init();
    }
    
}
