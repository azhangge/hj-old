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

import com.hjedu.examination.dao.IModuleExamCaseDAO;
import com.hjedu.examination.dao.IModuleExamInfoDAO;
import com.hjedu.examination.entity.module.ModuleExamCase;
import com.hjedu.examination.entity.module.ModuleExamCaseItemCase;
import com.hjedu.examination.entity.module.ModuleExamCaseItemChoice;
import com.hjedu.examination.entity.module.ModuleExamCaseItemEssay;
import com.hjedu.examination.entity.module.ModuleExamCaseItemFile;
import com.hjedu.examination.entity.module.ModuleExamCaseItemFill;
import com.hjedu.examination.entity.module.ModuleExamCaseItemJudge;
import com.hjedu.examination.entity.module.ModuleExamCaseItemMultiChoice;
import com.hjedu.examination.entity.module.ModuleExamInfo;
import com.hjedu.examination.service.IModuleExamCaseService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ModuleExamCaseReport implements Serializable {

    IModuleExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ModuleExamCaseDAO");
    IModuleExamCaseService examCaseService = SpringHelper.getSpringBean("ModuleExamCaseService");
    IModuleExamInfoDAO examInfoDAO=SpringHelper.getSpringBean("ModuleExamInfoDAO");
    ModuleExamCase examCase = null;
    ModuleExamInfo examInfo;
    private PieChartModel pieModel1;
    private PieChartModel pieModel2;
    List<ModuleExamCaseItemCase> cases;
    List<ModuleExamCaseItemChoice> choices;
    List<ModuleExamCaseItemMultiChoice> mchoices;
    List<ModuleExamCaseItemFill> fills;
    List<ModuleExamCaseItemJudge> judges;
    List<ModuleExamCaseItemEssay> essaies;
    List<ModuleExamCaseItemFile> files;

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

    public ModuleExamCase getExamCase() {
        return examCase;
    }

    public void setExamCase(ModuleExamCase examCase) {
        this.examCase = examCase;
    }

    public ModuleExamInfo getExamInfo() {
        return examInfo;
    }

    public void setExamInfo(ModuleExamInfo examInfo) {
        this.examInfo = examInfo;
    }

    public List<ModuleExamCaseItemCase> getCases() {
        return cases;
    }

    public void setCases(List<ModuleExamCaseItemCase> cases) {
        this.cases = cases;
    }

    public List<ModuleExamCaseItemChoice> getChoices() {
        return choices;
    }

    public void setChoices(List<ModuleExamCaseItemChoice> choices) {
        this.choices = choices;
    }

    public List<ModuleExamCaseItemMultiChoice> getMchoices() {
        return mchoices;
    }

    public void setMchoices(List<ModuleExamCaseItemMultiChoice> mchoices) {
        this.mchoices = mchoices;
    }

    public List<ModuleExamCaseItemFill> getFills() {
        return fills;
    }

    public void setFills(List<ModuleExamCaseItemFill> fills) {
        this.fills = fills;
    }

    public List<ModuleExamCaseItemJudge> getJudges() {
        return judges;
    }

    public void setJudges(List<ModuleExamCaseItemJudge> judges) {
        this.judges = judges;
    }

    public List<ModuleExamCaseItemEssay> getEssaies() {
        return essaies;
    }

    public void setEssaies(List<ModuleExamCaseItemEssay> essaies) {
        this.essaies = essaies;
    }

    public List<ModuleExamCaseItemFile> getFiles() {
        return files;
    }

    public void setFiles(List<ModuleExamCaseItemFile> files) {
        this.files = files;
    }

    @PostConstruct
    public void init() {
        this.examInfo=this.examInfoDAO.findModuleExamInfo();
        HttpServletRequest request = JsfHelper.getRequest();
        String case_id = request.getParameter("case_id");
        if (case_id != null) {
            this.examCase = this.examCaseDAO.findModuleExamCase(case_id);
            this.cases = examCase.getCaseItems();
            this.choices = examCase.getChoiceItems();
            this.mchoices = examCase.getMultiChoiceItems();
            this.fills = examCase.getFillItems();
            this.judges = examCase.getJudgeItems();
            this.essaies = examCase.getEssayItems();
            this.files = examCase.getFileItems();

            if (this.examCase != null) {
                if (!this.examInfo.isIfRandom()) {
                    this.orderExamCase(examCase);
                }
            }
        }
        createPieModel();
    }

    private void orderExamCase(ModuleExamCase ec) {

        Collections.sort(this.cases);
        Collections.sort(this.choices);
        Collections.sort(this.mchoices);
        Collections.sort(this.fills);
        Collections.sort(this.judges);
        Collections.sort(this.essaies);
        Collections.sort(this.files);
    }

    private void createPieModel() {
        pieModel1 = new PieChartModel();
        pieModel2 = new PieChartModel();
        DecimalFormat df=new DecimalFormat("0.0");  

        if (this.examCase.getChoiceTotal() != 0) {
            double dd = this.examInfo.getChoiceScore() * this.examCase.getChoiceTotal();
            
            pieModel1.set("单选题：" + df.format(dd) + "分", dd);
        }
        if (this.examCase.getMultiChoiceTotal() != 0) {
            double dd = this.examInfo.getMultiChoiceScore() * this.examCase.getMultiChoiceTotal();
            pieModel1.set("多选题：" + df.format(dd) + "分", dd);
        }
        if (this.examCase.getFillTotal() != 0) {
            double dd = this.examInfo.getFillScore() * this.examCase.getFillTotal();
            pieModel1.set("填空题：" + df.format(dd) + "分", dd);
        }
        if (this.examCase.getJudgeTotal() != 0) {
            double dd = this.examInfo.getJudgeScore() * this.examCase.getJudgeTotal();
            pieModel1.set("判断题：" + df.format(dd) + "分", dd);
        }
        if (this.examCase.getEssayTotal() != 0) {
            double dd = this.examInfo.getEssayScore() * this.examCase.getEssayTotal();
            pieModel1.set("问答题：" + df.format(dd) + "分", dd);
        }
        if (this.examCase.getFileTotal() != 0) {
            double dd = this.examInfo.getFileScore() * this.examCase.getFileTotal();
            pieModel1.set("文件题：" + df.format(dd) + "分", dd);
        }
        if (this.examCase.getCaseTotal() != 0) {
            double dd = this.examCase.getCaseFullScore();
            pieModel1.set("综合题：" + df.format(dd) + "分", dd);
        }

        if (this.examCase.getChoiceTotal() != 0) {
            double dd = this.examCase.getChoiceScore();
            pieModel2.set("单选题：" + df.format(dd) + "分", dd);
        }
        if (this.examCase.getMultiChoiceTotal() != 0) {
            double dd = this.examCase.getMultiChoiceScore();
            pieModel2.set("多选题：" + df.format(dd) + "分", dd);
        }
        if (this.examCase.getFillTotal() != 0) {
            double dd = this.examCase.getFillScore();
            pieModel2.set("填空题：" + df.format(dd) + "分", dd);
        }
        if (this.examCase.getJudgeTotal() != 0) {
            double dd = this.examCase.getJudgeScore();
            pieModel2.set("判断题：" + df.format(dd) + "分", dd);
        }
        if (this.examCase.getEssayTotal() != 0) {
            double dd = this.examCase.getEssayScore();
            pieModel2.set("问答题：" + df.format(dd) + "分", dd);
        }
        if (this.examCase.getFileTotal() != 0) {
            double dd = this.examCase.getFileScore();
            pieModel2.set("文件题：" + df.format(dd) + "分", dd);
        }
        if (this.examCase.getCaseTotal() != 0) {
            double dd = this.examCase.getCaseScore();
            pieModel2.set("综合题：" + df.format(dd) + "分", dd);
        }


    }

 
}
