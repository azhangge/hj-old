package com.hjedu.examination.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.examination.dao.IExamDepartmentDAO;
import com.hjedu.examination.entity.ExamDepartment;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAExamDepartment  implements Serializable{
ILogService logger = SpringHelper.getSpringBean("LogService");
    IExamDepartmentDAO deptDAO = SpringHelper.getSpringBean("ExamDepartmentDAO");
    ExamDepartment dept = null;
    boolean flag = false;
    String businessId;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public ExamDepartment getDept() {
        return dept;
    }

    public void setDept(ExamDepartment dept) {
        this.dept = dept;
    }

    
    

    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        this.businessId = CookieUtils.getBusinessId(request);
        String id = request.getParameter("id");
        if (id != null) {
            this.flag = true;
            this.dept = this.deptDAO.findExamDepartment(id);
        } else {
            this.dept = new ExamDepartment();
            this.dept.setFatherId(this.businessId);
        }
    }
    
    public String done() {
        if (flag) {
            this.logger.log("修改了班级："+dept.getName());
            this.deptDAO.updateExamDepartment(dept);
        } else {
            this.logger.log("添加了班级："+dept.getName());
            this.deptDAO.addExamDepartment(dept);
        }
        return "ListExamDepartment?faces-redirect=true";
    }
    
    
}
