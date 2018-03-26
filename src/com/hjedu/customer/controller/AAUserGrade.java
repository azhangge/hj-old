package com.hjedu.customer.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.customer.dao.IBbsUserGradeDAO;
import com.hjedu.platform.entity.BbsUserGrade;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAUserGrade implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IBbsUserGradeDAO gradeDAO = SpringHelper.getSpringBean("BbsUserGradeDAO");
    BbsUserGrade grade = null;
    boolean flag = false;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public BbsUserGrade getGrade() {
        return grade;
    }

    public void setGrade(BbsUserGrade grade) {
        this.grade = grade;
    }

    

    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        String id = request.getParameter("id");
        if (id != null) {
            this.flag = true;
            this.grade = this.gradeDAO.findUserGrade(id);
        } else {
            this.grade = new BbsUserGrade();
        }
    }

    public String done() {
        if (flag) {
            this.logger.log("修改了积分等级：" + grade.getGradeName());
            this.gradeDAO.updateUserGrade(grade);
        } else {
            this.logger.log("添加了积分等级：" + grade.getGradeName());
            this.gradeDAO.addUserGrade(grade);
        }
        return "ListUserGrade?faces-redirect=true";
    }
}
