package com.hjedu.examination.controller;


import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IContestCaseDAO;
import com.hjedu.examination.entity.contest.ContestCase;
import com.hjedu.platform.controller.ClientSession;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ContestCaseReportList  implements Serializable{

    IContestCaseDAO examCaseDAO = SpringHelper.getSpringBean("ContestCaseDAO");
    List<ContestCase> cases;

    public List<ContestCase> getCases() {
        return cases;
    }

    public void setCases(List<ContestCase> cases) {
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
            this.cases = this.examCaseDAO.findContestCaseByUser(u.getId());
        }
    }

    public void deleteReport(String id) {
        this.examCaseDAO.deleteContestCase(id);
        this.synDB();
    }
}
