package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.chart.PieChartModel;

import com.hjedu.examination.dao.IContestCaseDAO;
import com.hjedu.examination.entity.contest.ContestCase;
import com.hjedu.examination.entity.contest.ContestCaseItemCase;
import com.hjedu.examination.entity.contest.ContestCaseItemChoice;
import com.hjedu.examination.entity.contest.ContestCaseItemEssay;
import com.hjedu.examination.entity.contest.ContestCaseItemFile;
import com.hjedu.examination.entity.contest.ContestCaseItemFill;
import com.hjedu.examination.entity.contest.ContestCaseItemJudge;
import com.hjedu.examination.entity.contest.ContestCaseItemMultiChoice;
import com.hjedu.examination.service.impl.IContestCaseRandom2Service;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ContestCaseReport implements Serializable {

    IContestCaseDAO examCaseDAO = SpringHelper.getSpringBean("ContestCaseDAO");
    IContestCaseRandom2Service examCaseService = SpringHelper.getSpringBean("ContestCaseRandom2Service");
    ContestCase examCase = null;
    private PieChartModel pieModel1;
    private PieChartModel pieModel2;

    public PieChartModel getPieModel1() {
        return pieModel1;
    }

    public void setPieModel1(PieChartModel pieModel1) {
        this.pieModel1 = pieModel1;
    }

    public PieChartModel getPieModel2() {
        return pieModel2;
    }

    public void setPieModel2(PieChartModel pieModel2) {
        this.pieModel2 = pieModel2;
    }

    public ContestCase getExamCase() {
        return examCase;
    }

    public void setExamCase(ContestCase examCase) {
        this.examCase = examCase;
    }


    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        String case_id = request.getParameter("case_id");
        if (case_id != null) {
            this.examCase = this.examCaseDAO.findContestCase(case_id);
            this.examCase=this.examCaseService.resumeExamCase(examCase);
        }
    }

    


    public String submitReport() {
        String result = "ListContestCaseReport?faces-redirect=true";
        this.examCaseService.computeTotalScore(examCase);
        this.examCaseDAO.updateContestCase(examCase);
        return result;
    }

}
