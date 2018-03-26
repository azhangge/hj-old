package com.hjedu.examination.controller;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.chart.PieChartModel;

import com.hjedu.examination.dao.IBuffetExamCaseDAO;
import com.hjedu.examination.entity.buffet.BuffetExamCase;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemCase;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemChoice;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemEssay;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemFile;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemFill;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemJudge;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemMultiChoice;
import com.hjedu.examination.service.impl.IBuffetExamCaseService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class BuffetExamCaseReport implements Serializable {

    IBuffetExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("BuffetExamCaseDAO");
    IBuffetExamCaseService examCaseService = SpringHelper.getSpringBean("BuffetExamCaseService");
    BuffetExamCase examCase = null;
    private PieChartModel pieModel1;
    private PieChartModel pieModel2;
    List<BuffetExamCaseItemCase> cases;
    List<BuffetExamCaseItemChoice> choices;
    List<BuffetExamCaseItemMultiChoice> mchoices;
    List<BuffetExamCaseItemFill> fills;
    List<BuffetExamCaseItemJudge> judges;
    List<BuffetExamCaseItemEssay> essaies;
    List<BuffetExamCaseItemFile> files;

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

    public BuffetExamCase getExamCase() {
        return examCase;
    }

    public void setExamCase(BuffetExamCase examCase) {
        this.examCase = examCase;
    }

    public List<BuffetExamCaseItemCase> getCases() {
        return cases;
    }

    public void setCases(List<BuffetExamCaseItemCase> cases) {
        this.cases = cases;
    }

    public List<BuffetExamCaseItemChoice> getChoices() {
        return choices;
    }

    public void setChoices(List<BuffetExamCaseItemChoice> choices) {
        this.choices = choices;
    }

    public List<BuffetExamCaseItemMultiChoice> getMchoices() {
        return mchoices;
    }

    public void setMchoices(List<BuffetExamCaseItemMultiChoice> mchoices) {
        this.mchoices = mchoices;
    }

    public List<BuffetExamCaseItemFill> getFills() {
        return fills;
    }

    public void setFills(List<BuffetExamCaseItemFill> fills) {
        this.fills = fills;
    }

    public List<BuffetExamCaseItemJudge> getJudges() {
        return judges;
    }

    public void setJudges(List<BuffetExamCaseItemJudge> judges) {
        this.judges = judges;
    }

    public List<BuffetExamCaseItemEssay> getEssaies() {
        return essaies;
    }

    public void setEssaies(List<BuffetExamCaseItemEssay> essaies) {
        this.essaies = essaies;
    }

    public List<BuffetExamCaseItemFile> getFiles() {
        return files;
    }

    public void setFiles(List<BuffetExamCaseItemFile> files) {
        this.files = files;
    }


    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        String case_id = request.getParameter("case_id");
        if (case_id != null) {
            this.examCase = this.examCaseDAO.findBuffetExamCase(case_id);
            this.examCase=this.examCaseService.resumeExamCase(examCase);
            this.cases = examCase.getCaseItems();
            this.choices = examCase.getChoiceItems();
            this.mchoices = examCase.getMultiChoiceItems();
            this.fills = examCase.getFillItems();
            this.judges = examCase.getJudgeItems();
            this.essaies = examCase.getEssayItems();
            this.files = examCase.getFileItems();

            
            
        }
        createPieModel();
    }

    

    private void createPieModel() {
        pieModel1 = new PieChartModel();
        pieModel2 = new PieChartModel();
        DecimalFormat df = new DecimalFormat("0.0");


    }

    public String submitReport() {
        String result = "ListBuffetExamCaseReport?faces-redirect=true";
        this.examCaseService.computeTotalScore(examCase);
        this.examCaseDAO.updateBuffetExamCase(examCase);
        return result;
    }

}
