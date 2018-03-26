package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IBuffetExamCaseDAO;
import com.hjedu.examination.entity.buffet.BuffetExamCase;
import com.hjedu.platform.controller.ClientSession;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class BuffetExamCaseReportList  implements Serializable{

    IBuffetExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("BuffetExamCaseDAO");
    List<BuffetExamCase> cases;

    public List<BuffetExamCase> getCases() {
        return cases;
    }

    public void setCases(List<BuffetExamCase> cases) {
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
            this.cases = this.examCaseDAO.findBuffetExamCaseByUser(u.getId());
        }
    }

    public void deleteReport(String id) {
        this.examCaseDAO.deleteBuffetExamCase(id);
        this.synDB();
    }
}
