
package com.hjedu.examination.controller;


import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.hjedu.examination.entity.contest.ContestCase;
import com.huajie.util.JsfHelper;

@ManagedBean
@RequestScoped
public class ContestCaseResult {

    ContestCase examCase = null;

    public ContestCase getExamCase() {
        return examCase;
    }

    public void setExamCase(ContestCase examCase) {
        this.examCase = examCase;
    }

    @PostConstruct
    public void init() {
        examCase = (ContestCase) JsfHelper.getRequest().getSession().getAttribute("tempContestCase");
        JsfHelper.getRequest().getSession().removeAttribute("tempContestCase");
    }

}
