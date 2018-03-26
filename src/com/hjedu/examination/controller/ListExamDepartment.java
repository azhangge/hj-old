package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.examination.dao.IExamDepartmentDAO;
import com.hjedu.examination.entity.ExamDepartment;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListExamDepartment implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IExamDepartmentDAO deptDAO = SpringHelper.getSpringBean("ExamDepartmentDAO");
    List<ExamDepartment> depts;
    String businessId;

    public List<ExamDepartment> getDepts() {
        return depts;
    }

    public void setDepts(List<ExamDepartment> depts) {
        this.depts = depts;
    }

    @PostConstruct
    public void init() {
    	this.businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        this.depts = this.deptDAO.findRootExamDepartment(this.businessId);
    }

    public void delete(String id) {
        ExamDepartment ed=this.deptDAO.findExamDepartment(id);
        this.logger.log("删除了班级："+ed.getName());
        this.deptDAO.deleteExamDepartment(id);
        this.depts = this.deptDAO.findRootExamDepartment(this.businessId);
    }
}
