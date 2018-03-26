package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IModuleExamCaseDAO;
import com.hjedu.examination.entity.module.ModuleExamCase;
import com.hjedu.platform.controller.ClientSession;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ModuleExamCaseList  implements Serializable{

    IModuleExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ModuleExamCaseDAO");
    List<ModuleExamCase> cases;

    public List<ModuleExamCase> getCases() {
        return cases;
    }

    public void setCases(List<ModuleExamCase> cases) {
        this.cases = cases;
    }

    @PostConstruct
    public void init() {
        this.synDB();
    }

    public void synDB() {
        ClientSession cs = JsfHelper.getBean("clientSession");
        BbsUser u = cs.getUsr();
        if (u != null) {
            this.cases = this.examCaseDAO.findModuleExamCaseByUser(u.getId());
        }
    }

    public void deleteReport(String id) {
        this.examCaseDAO.deleteModuleExamCase(id);
        this.synDB();
    }
}
