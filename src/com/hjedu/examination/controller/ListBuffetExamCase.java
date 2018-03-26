package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.examination.dao.IBuffetExamCaseDAO;
import com.hjedu.examination.entity.buffet.BuffetExamCase;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListBuffetExamCase implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IBuffetExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("BuffetExamCaseDAO");

    List<BuffetExamCase> cases;
    //List<ModuleExamCase> cases2 = new ArrayList();
    //以下属性用于绑定页面计算部门或考试的平均分

    boolean selectAll = false;

    public List<BuffetExamCase> getCases() {
        return cases;
    }

    public void setCases(List<BuffetExamCase> cases) {
        this.cases = cases;
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }

    @PostConstruct
    public void init() {
        this.synDB();
        //this.buildLazy();
    }

    public void synDB() {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        try {
            this.cases = this.examCaseDAO.findAllBuffetExamCase(businessId);

        } catch (Exception e) {
        }
    }

    public void deleteReport(String id) {
        BuffetExamCase ec = this.examCaseDAO.findBuffetExamCase(id);
        this.logger.log("删除了自助练习成绩，" + "，用户为：" + ec.getUser().getUsername());
        this.examCaseDAO.deleteBuffetExamCase(id);
        this.synDB();
    }

    public String selectAllOrNot() {
        for (BuffetExamCase ec : this.cases) {
            if (this.isSelectAll()) {
                ec.setSelected(true);
            } else {
                ec.setSelected(false);
            }
        }
        return "";
    }

    public String batchDelete() {
        for (BuffetExamCase ec : this.cases) {
            if (ec.isSelected()) {
                try {
                    String nname = ec.getUser() == null ? "null" : ec.getUser().getUsername();
                    this.logger.log("删除了自助练习成绩:" + ec.getName() + "，用户为：" + nname);
                    this.examCaseDAO.deleteBuffetExamCase(ec.getId());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        this.synDB();
        return null;
    }

    public String deleteAll() {
        try {
            this.logger.log("清空了所有自助练习成绩。");
            for (BuffetExamCase ec : this.cases) {
                this.examCaseDAO.deleteBuffetExamCase(ec.getId());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.synDB();
        return null;
    }

}
