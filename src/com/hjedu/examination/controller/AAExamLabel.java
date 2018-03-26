package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.customer.dao.IAdminDAO;
import com.hjedu.examination.dao.IExamLabelDAO;
import com.hjedu.examination.dao.IExamLabelTypeDAO;
import com.hjedu.examination.entity.ExamLabel;
import com.hjedu.examination.entity.ExamLabelType;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAExamLabel implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IExamLabelDAO examDAO = SpringHelper.getSpringBean("ExamLabelDAO");
    IExamLabelTypeDAO lessonTypeDAO = SpringHelper.getSpringBean("ExamLabelTypeDAO");
    IAdminDAO adminDAO = SpringHelper.getSpringBean("AdminDAO");
    ExamLabel exam;

    String typeId = "0";
    List<ExamLabelType> types;
    boolean flag = false;

    boolean ifExam = true;
    boolean ifContest = true;
    boolean ifBuffet = true;

    public ExamLabel getExam() {
        return exam;
    }

    public void setExam(ExamLabel exam) {
        this.exam = exam;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isIfExam() {
        return ifExam;
    }

    public void setIfExam(boolean ifExam) {
        this.ifExam = ifExam;
    }

    public boolean isIfContest() {
        return ifContest;
    }

    public void setIfContest(boolean ifContest) {
        this.ifContest = ifContest;
    }

    public boolean isIfBuffet() {
        return ifBuffet;
    }

    public void setIfBuffet(boolean ifBuffet) {
        this.ifBuffet = ifBuffet;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public List<ExamLabelType> getTypes() {
        return types;
    }

    public void setTypes(List<ExamLabelType> types) {
        this.types = types;
    }

    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        String businessId = CookieUtils.getBusinessId(request);
        String id = request.getParameter("id");
        if (id != null) {
            this.flag = true;
            this.exam = this.examDAO.findExamLabel(id);
        } else {
            this.exam = new ExamLabel();
            //this.buildModuleExamLabels(exam);
        }

        //加载课程类别
        this.types = this.lessonTypeDAO.findAllExamLabelTypeByBusinessId(businessId);
        //设置当前课程的类别
        if (this.exam.getLabelType() != null) {
            this.typeId = this.exam.getLabelType().getId();
        }
        this.loadTypeStr();
    }

    public void loadTypeStr() {
        if (flag) {
            String str = this.exam.getTypeStr();
            if (str != null) {
                if (str.contains("exam")) {
                    this.ifExam = true;
                } else {
                    this.ifExam = false;
                }
                if (str.contains("contest")) {
                    this.ifContest = true;
                } else {
                    this.ifContest = false;
                }
                if (str.contains("buffet")) {
                    this.ifBuffet = true;
                } else {
                    this.ifBuffet = false;
                }
            }
        }
    }

    public void resetTypeStr() {
        StringBuilder sb = new StringBuilder();
        if (this.ifExam) {
            sb.append("exam;");
        }
        if (this.ifContest) {
            sb.append("contest;");
        }
        if (this.ifBuffet) {
            sb.append("buffet;");
        }
        //System.out.println(sb.toString());
        this.exam.setTypeStr(sb.toString());

    }

    public String done() {
        this.resetTypeStr();
        //设置课程的类型
        ExamLabelType lt = this.lessonTypeDAO.findExamLabelType(typeId);
        if (lt != null) {
            this.exam.setLabelType(lt);
        }
        if (flag) {
            this.logger.log("修改了考试标签：" + exam.getName());
            this.examDAO.updateExamLabel(exam);
        } else {
            this.logger.log("添加了考试标签：" + exam.getName());
            this.examDAO.addExamLabel(exam);
        }
        AdminSessionBean asb = JsfHelper.getBean("adminSessionBean");
        asb.refreshAdmin();
        return "ListExamLabel?faces-redirect=true";
    }
}
