package com.hjedu.examination.controller;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.chart.PieChartModel;

import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.ExamCaseItemCase;
import com.hjedu.examination.entity.ExamCaseItemChoice;
import com.hjedu.examination.entity.ExamCaseItemEssay;
import com.hjedu.examination.entity.ExamCaseItemFile;
import com.hjedu.examination.entity.ExamCaseItemFill;
import com.hjedu.examination.entity.ExamCaseItemJudge;
import com.hjedu.examination.entity.ExamCaseItemMultiChoice;
import com.hjedu.examination.service.IExamCaseService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ExamCaseReport implements Serializable {

    IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
    IExamCaseService examCaseService = SpringHelper.getSpringBean("ExamCaseService");
    ExamCase examCase = null;
    private PieChartModel pieModel1;
    private PieChartModel pieModel2;
    List<ExamCaseItemCase> cases;
    List<ExamCaseItemChoice> choices;
    List<ExamCaseItemMultiChoice> mchoices;
    List<ExamCaseItemFill> fills;
    List<ExamCaseItemJudge> judges;
    List<ExamCaseItemEssay> essaies;
    List<ExamCaseItemFile> files;

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

    public ExamCase getExamCase() {
        return examCase;
    }

    public void setExamCase(ExamCase examCase) {
        this.examCase = examCase;
    }

    public List<ExamCaseItemCase> getCases() {
        return cases;
    }

    public void setCases(List<ExamCaseItemCase> cases) {
        this.cases = cases;
    }

    public List<ExamCaseItemChoice> getChoices() {
        return choices;
    }

    public void setChoices(List<ExamCaseItemChoice> choices) {
        this.choices = choices;
    }

    public List<ExamCaseItemMultiChoice> getMchoices() {
        return mchoices;
    }

    public void setMchoices(List<ExamCaseItemMultiChoice> mchoices) {
        this.mchoices = mchoices;
    }

    public List<ExamCaseItemFill> getFills() {
        return fills;
    }

    public void setFills(List<ExamCaseItemFill> fills) {
        this.fills = fills;
    }

    public List<ExamCaseItemJudge> getJudges() {
        return judges;
    }

    public void setJudges(List<ExamCaseItemJudge> judges) {
        this.judges = judges;
    }

    public List<ExamCaseItemEssay> getEssaies() {
        return essaies;
    }

    public void setEssaies(List<ExamCaseItemEssay> essaies) {
        this.essaies = essaies;
    }

    public List<ExamCaseItemFile> getFiles() {
        return files;
    }

    public void setFiles(List<ExamCaseItemFile> files) {
        this.files = files;
    }


    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        String case_id = request.getParameter("case_id");
        if (case_id != null) {
            this.examCase = this.examCaseDAO.findExamCase(case_id);
            this.cases = examCase.getCaseItems();
            this.choices = examCase.getChoiceItems();
            this.mchoices = examCase.getMultiChoiceItems();
            this.fills = examCase.getFillItems();
            this.judges = examCase.getJudgeItems();
            this.essaies = examCase.getEssayItems();
            this.files = examCase.getFileItems();

            if (this.examCase != null) {
                if (!this.examCase.getExamination().isIfRandom()) {
                    this.orderExamCase(examCase);
                }
            }
        }
        createPieModel();
    }

    private void orderExamCase(ExamCase ec) {
        Collections.sort(this.choices);
        Collections.sort(this.mchoices);
        Collections.sort(this.fills);
        Collections.sort(this.judges);
        Collections.sort(this.essaies);
        Collections.sort(this.files);
        Collections.sort(this.cases);
    }

    private void createPieModel() {
        pieModel1 = new PieChartModel();
        pieModel2 = new PieChartModel();
        
        pieModel1.setTitle("试卷分值构成比例图");
        pieModel1.setShowDataLabels(true);
        pieModel1.setLegendPosition("w");
        pieModel1.setSliceMargin(5);
        
        pieModel2.setTitle("考生得分构成比例图");
        pieModel2.setShowDataLabels(true);
        pieModel2.setLegendPosition("e");
        pieModel2.setSliceMargin(5);
        
        DecimalFormat df = new DecimalFormat("0.0");

        if (this.examCase.getExamination().getChoiceTotal() != 0) {
            double dd = this.examCase.getExamination().getRandomPaper().getChoiceScore() * this.examCase.getExamination().getChoiceTotal();

            pieModel1.set("单选题：" + df.format(dd) + "分", dd);
        }
        if (this.examCase.getExamination().getMultiChoiceTotal() != 0) {
            double dd = this.examCase.getExamination().getRandomPaper().getMultiChoiceScore() * this.examCase.getExamination().getMultiChoiceTotal();
            pieModel1.set("多选题：" + df.format(dd) + "分", dd);
        }
        if (this.examCase.getExamination().getFillTotal() != 0) {
            double dd = this.examCase.getExamination().getRandomPaper().getFillScore() * this.examCase.getExamination().getFillTotal();
            pieModel1.set("填空题：" + df.format(dd) + "分", dd);
        }
        if (this.examCase.getExamination().getJudgeTotal() != 0) {
            double dd = this.examCase.getExamination().getRandomPaper().getJudgeScore() * this.examCase.getExamination().getJudgeTotal();
            pieModel1.set("判断题：" + df.format(dd) + "分", dd);
        }
        if (this.examCase.getExamination().getEssayTotal() != 0) {
            double dd = this.examCase.getExamination().getRandomPaper().getEssayScore() * this.examCase.getExamination().getEssayTotal();
            pieModel1.set("问答题：" + df.format(dd) + "分", dd);
        }
        if (this.examCase.getExamination().getFileTotal() != 0) {
            double dd = this.examCase.getExamination().getRandomPaper().getFileScore() * this.examCase.getExamination().getFileTotal();
            pieModel1.set("文件题：" + df.format(dd) + "分", dd);
        }
        if (this.examCase.getExamination().getCaseTotal() != 0) {
            double dd = this.examCase.getCaseFullScore();
            pieModel1.set("综合题：" + df.format(dd) + "分", dd);
        }

        if (this.examCase.getExamination().getChoiceTotal() != 0) {
            double dd = this.examCase.getChoiceScore();
            pieModel2.set("单选题：" + df.format(dd) + "分", dd);
        }
        if (this.examCase.getExamination().getMultiChoiceTotal() != 0) {
            double dd = this.examCase.getMultiChoiceScore();
            pieModel2.set("多选题：" + df.format(dd) + "分", dd);
        }
        if (this.examCase.getExamination().getFillTotal() != 0) {
            double dd = this.examCase.getFillScore();
            pieModel2.set("填空题：" + df.format(dd) + "分", dd);
        }
        if (this.examCase.getExamination().getJudgeTotal() != 0) {
            double dd = this.examCase.getJudgeScore();
            pieModel2.set("判断题：" + df.format(dd) + "分", dd);
        }
        if (this.examCase.getExamination().getEssayTotal() != 0) {
            double dd = this.examCase.getEssayScore();
            pieModel2.set("问答题：" + df.format(dd) + "分", dd);
        }
        if (this.examCase.getExamination().getFileTotal() != 0) {
            double dd = this.examCase.getFileScore();
            pieModel2.set("文件题：" + df.format(dd) + "分", dd);
        }
        if (this.examCase.getExamination().getCaseTotal() != 0) {
            double dd = this.examCase.getCaseScore();
            pieModel2.set("综合题：" + df.format(dd) + "分", dd);
        }

    }

    public String submitReport() {
        String result = "ListExamCaseReport?faces-redirect=true";
        this.examCaseService.computeTotalScore(examCase);
        this.examCaseDAO.updateExamCase(examCase);
        return result;
    }

}
