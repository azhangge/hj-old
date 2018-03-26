
package com.hjedu.examination.controller;


import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.hjedu.examination.entity.buffet.BuffetExamCase;
import com.huajie.util.JsfHelper;

@ManagedBean
@RequestScoped
public class BuffetExamCaseResult {

    BuffetExamCase examCase = null;

    public BuffetExamCase getExamCase() {
        return examCase;
    }

    public void setExamCase(BuffetExamCase examCase) {
        this.examCase = examCase;
    }

    @PostConstruct
    public void init() {
        examCase = (BuffetExamCase) JsfHelper.getRequest().getSession().getAttribute("tempBuffetExamCase");
        JsfHelper.getRequest().getSession().removeAttribute("tempBuffetExamCase");
    }

}
