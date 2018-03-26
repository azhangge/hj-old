package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.examination.dao.IExamPaperManualDAO;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.CaseQuestion;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.EssayQuestion;
import com.hjedu.examination.entity.ExamPaperManual;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.entity.FileQuestion;
import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.hjedu.examination.service.impl.ExaminationService;
import com.hjedu.platform.controller.BasketSession;
import com.hjedu.platform.service.ILogService;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAManualPaper implements Serializable {

    ExamPaperManual paper;
    IExamPaperManualDAO manualDAO = SpringHelper.getSpringBean("ExamPaperManualDAO");
    ILogService logger = SpringHelper.getSpringBean("LogService");
    private boolean flag = false;
    List<ChoiceQuestion> choices = new LinkedList();
    List<MultiChoiceQuestion> multiChoices = new LinkedList();
    List<FillQuestion> fills = new LinkedList();
    List<JudgeQuestion> judges = new LinkedList();
    List<EssayQuestion> essaies = new LinkedList();
    List<FileQuestion> files = new LinkedList();
    List<CaseQuestion> cases = new LinkedList();

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public ExamPaperManual getPaper() {
        return paper;
    }

    public void setPaper(ExamPaperManual paper) {
        this.paper = paper;
    }

    public List<ChoiceQuestion> getChoices() {
        return choices;
    }

    public void setChoices(List<ChoiceQuestion> choices) {
        this.choices = choices;
    }

    public List<MultiChoiceQuestion> getMultiChoices() {
        return multiChoices;
    }

    public void setMultiChoices(List<MultiChoiceQuestion> multiChoices) {
        this.multiChoices = multiChoices;
    }

    public List<FillQuestion> getFills() {
        return fills;
    }

    public void setFills(List<FillQuestion> fills) {
        this.fills = fills;
    }

    public List<JudgeQuestion> getJudges() {
        return judges;
    }

    public void setJudges(List<JudgeQuestion> judges) {
        this.judges = judges;
    }

    public List<EssayQuestion> getEssaies() {
        return essaies;
    }

    public void setEssaies(List<EssayQuestion> essaies) {
        this.essaies = essaies;
    }

    public List<FileQuestion> getFiles() {
        return files;
    }

    public void setFiles(List<FileQuestion> files) {
        this.files = files;
    }

    public List<CaseQuestion> getCases() {
        return cases;
    }

    public void setCases(List<CaseQuestion> cases) {
        this.cases = cases;
    }

    @PostConstruct
    public void init() {
        String idt = JsfHelper.getRequest().getParameter("id");
        if (idt != null) {
            this.paper = this.manualDAO.findExamPaperManual(idt);
            this.flag = true;
            this.loadQuestions();
        } else {
            paper = new ExamPaperManual();
        }

    }

    private void loadQuestions() {
        this.choices = paper.getChoices();
        this.multiChoices = paper.getMultiChoices();
        this.fills = paper.getFills();
        this.judges = paper.getJudges();
        this.essaies = paper.getEssaies();
        this.files = paper.getFiles();
        this.cases = paper.getCases();
    }

    private void resetQuestions() {
        paper.setChoices(choices);
        paper.setMultiChoices(multiChoices);
        paper.setFills(fills);
        paper.setJudges(judges);
        paper.setEssaies(essaies);
        paper.setFiles(files);
        paper.setCases(cases);
    }

    public String batchDeleteQuestion(String type) {
        if ("choice".equals(type)) {
            List<ChoiceQuestion> temp = new LinkedList();
            for (ChoiceQuestion cq : this.getChoices()) {
                if (cq.isSelected()) {
                    temp.add(cq);
                }
            }
            for (ChoiceQuestion c : temp) {
                this.getChoices().remove(c);
            }
        }
        if ("mchoice".equals(type)) {
            List<MultiChoiceQuestion> temp = new LinkedList();
            for (MultiChoiceQuestion cq : this.getMultiChoices()) {
                if (cq.isSelected()) {
                    temp.add(cq);
                }
            }
            for (MultiChoiceQuestion c : temp) {
                this.getMultiChoices().remove(c);
            }
        }
        if ("fill".equals(type)) {
            List<FillQuestion> temp = new LinkedList();
            for (FillQuestion cq : this.getFills()) {
                if (cq.isSelected()) {
                    temp.add(cq);
                }
            }
            for (FillQuestion c : temp) {
                this.getFills().remove(c);
            }
        }
        if ("judge".equals(type)) {
            List<JudgeQuestion> temp = new LinkedList();
            for (JudgeQuestion cq : this.getJudges()) {
                if (cq.isSelected()) {
                    temp.add(cq);
                }
            }
            for (JudgeQuestion c : temp) {
                this.getJudges().remove(c);
            }
        }
        if ("essay".equals(type)) {
            List<EssayQuestion> temp = new LinkedList();
            for (EssayQuestion cq : this.getEssaies()) {
                if (cq.isSelected()) {
                    temp.add(cq);
                }
            }
            for (EssayQuestion c : temp) {
                this.getEssaies().remove(c);
            }
        }
        if ("file".equals(type)) {
            List<FileQuestion> temp = new LinkedList();
            for (FileQuestion cq : this.getFiles()) {
                if (cq.isSelected()) {
                    temp.add(cq);
                }
            }
            for (FileQuestion c : temp) {
                this.getFiles().remove(c);
            }
        }
        if ("case".equals(type)) {
            List<CaseQuestion> temp = new LinkedList();
            for (CaseQuestion cq : this.getCases()) {
                if (cq.isSelected()) {
                    temp.add(cq);
                }
            }
            for (CaseQuestion c : temp) {
                this.getCases().remove(c);
            }
        }
        return null;
    }

    public String batchMoveQuestion(String type) {
        BasketSession basketSession = JsfHelper.getBean("basketSession");
        if ("choice".equals(type)) {
            List<ChoiceQuestion> temp = new LinkedList();
            for (ChoiceQuestion cq : basketSession.getBasket().getChoices()) {
                if (cq.isSelected()) {
                    temp.add(cq);
                    if (!this.choices.contains(cq)) {
                        this.choices.add(cq);
                    }
                }
            }

            for (ChoiceQuestion c : temp) {
                basketSession.getBasket().getChoices().remove(c);
            }
        }
        if ("mchoice".equals(type)) {
            List<MultiChoiceQuestion> temp = new LinkedList();
            for (MultiChoiceQuestion cq : basketSession.getBasket().getMultiChoices()) {
                if (cq.isSelected()) {
                    temp.add(cq);
                    if (!this.multiChoices.contains(cq)) {
                        this.multiChoices.add(cq);
                    }
                }
            }
            for (MultiChoiceQuestion c : temp) {
                basketSession.getBasket().getMultiChoices().remove(c);
            }
        }
        if ("fill".equals(type)) {
            List<FillQuestion> temp = new LinkedList();
            for (FillQuestion cq : basketSession.getBasket().getFills()) {
                if (cq.isSelected()) {
                    temp.add(cq);
                    if (!this.fills.contains(cq)) {
                        this.fills.add(cq);

                    }
                }
            }
            for (FillQuestion c : temp) {
                basketSession.getBasket().getFills().remove(c);
            }
        }
        if ("judge".equals(type)) {
            List<JudgeQuestion> temp = new LinkedList();
            for (JudgeQuestion cq : basketSession.getBasket().getJudges()) {
                if (cq.isSelected()) {
                    temp.add(cq);
                    if (!this.judges.contains(cq)) {
                        this.judges.add(cq);
                    }
                }
            }
            for (JudgeQuestion c : temp) {
                basketSession.getBasket().getJudges().remove(c);
            }
        }
        if ("essay".equals(type)) {
            List<EssayQuestion> temp = new LinkedList();
            for (EssayQuestion cq : basketSession.getBasket().getEssaies()) {
                if (cq.isSelected()) {
                    temp.add(cq);
                    if (!this.essaies.contains(cq)) {
                        this.essaies.add(cq);
                    }
                }
            }
            for (EssayQuestion c : temp) {
                basketSession.getBasket().getEssaies().remove(c);
            }
        }
        if ("file".equals(type)) {
            List<FileQuestion> temp = new LinkedList();
            for (FileQuestion cq : basketSession.getBasket().getFiles()) {
                if (cq.isSelected()) {
                    temp.add(cq);
                    if (!this.files.contains(cq)) {
                        this.files.add(cq);
                    }
                }
            }
            for (FileQuestion c : temp) {
                basketSession.getBasket().getFiles().remove(c);
            }
        }
        if ("case".equals(type)) {
            List<CaseQuestion> temp = new LinkedList();
            for (CaseQuestion cq : basketSession.getBasket().getCases()) {
                if (cq.isSelected()) {
                    temp.add(cq);
                    if (!this.cases.contains(cq)) {
                        this.cases.add(cq);
                    }
                }
            }
            for (CaseQuestion c : temp) {
                basketSession.getBasket().getCases().remove(c);
            }
        }
        return null;
    }

    public String moveAllQuestion(String type) {
        BasketSession basketSession = JsfHelper.getBean("basketSession");
        if ("choice".equals(type)) {
            for (ChoiceQuestion cq : basketSession.getBasket().getChoices()) {
                if (!this.choices.contains(cq)) {
                    this.choices.add(cq);
                }
            }
            basketSession.getBasket().getChoices().clear();
        }
        if ("mchoice".equals(type)) {
            for (MultiChoiceQuestion cq : basketSession.getBasket().getMultiChoices()) {
                if (!this.multiChoices.contains(cq)) {
                    this.multiChoices.add(cq);
                }
            }
            basketSession.getBasket().getMultiChoices().clear();
        }
        if ("fill".equals(type)) {
            for (FillQuestion cq : basketSession.getBasket().getFills()) {
                if (!this.fills.contains(cq)) {
                    this.fills.add(cq);
                }
            }
            basketSession.getBasket().getFills().clear();
        }
        if ("judge".equals(type)) {
            for (JudgeQuestion cq : basketSession.getBasket().getJudges()) {
                if (!this.judges.contains(cq)) {
                    this.judges.add(cq);
                }
            }
            basketSession.getBasket().getJudges().clear();
        }
        if ("essay".equals(type)) {
            for (EssayQuestion cq : basketSession.getBasket().getEssaies()) {
                if (!this.essaies.contains(cq)) {
                    this.essaies.add(cq);
                }
            }
            basketSession.getBasket().getEssaies().clear();
        }
        if ("file".equals(type)) {
            for (FileQuestion cq : basketSession.getBasket().getFiles()) {
                if (!this.files.contains(cq)) {
                    this.files.add(cq);
                }
            }
            basketSession.getBasket().getFiles().clear();
        }
        if ("case".equals(type)) {
            for (CaseQuestion cq : basketSession.getBasket().getCases()) {
                if (!this.cases.contains(cq)) {
                    this.cases.add(cq);
                }
            }
            basketSession.getBasket().getCases().clear();
        }
        return null;
    }

    public String batchCopyQuestion(String type) {
        BasketSession basketSession = JsfHelper.getBean("basketSession");
        if ("choice".equals(type)) {
            List<ChoiceQuestion> temp = new LinkedList();
            for (ChoiceQuestion cq : basketSession.getBasket().getChoices()) {
                if (cq.isSelected()) {
                    if (!this.choices.contains(cq)) {
                        temp.add(cq);
                    }
                }
            }
            this.choices.addAll(temp);
        }
        if ("mchoice".equals(type)) {
            List<MultiChoiceQuestion> temp = new LinkedList();
            for (MultiChoiceQuestion cq : basketSession.getBasket().getMultiChoices()) {
                if (cq.isSelected()) {
                    if (!this.multiChoices.contains(cq)) {
                        temp.add(cq);
                    }
                }
            }
            this.multiChoices.addAll(temp);
        }
        if ("fill".equals(type)) {
            List<FillQuestion> temp = new LinkedList();
            for (FillQuestion cq : basketSession.getBasket().getFills()) {
                if (cq.isSelected()) {
                    if (!this.fills.contains(cq)) {
                        temp.add(cq);
                    }
                }
            }
            this.fills.addAll(temp);
        }
        if ("judge".equals(type)) {
            List<JudgeQuestion> temp = new LinkedList();
            for (JudgeQuestion cq : basketSession.getBasket().getJudges()) {
                if (cq.isSelected()) {
                    if (!this.judges.contains(cq)) {
                        temp.add(cq);
                    }
                }
            }
            this.judges.addAll(temp);
        }
        if ("essay".equals(type)) {
            List<EssayQuestion> temp = new LinkedList();
            for (EssayQuestion cq : basketSession.getBasket().getEssaies()) {
                if (cq.isSelected()) {
                    if (!this.essaies.contains(cq)) {
                        temp.add(cq);
                    }
                }
            }
            this.essaies.addAll(temp);
        }
        if ("file".equals(type)) {
            List<FileQuestion> temp = new LinkedList();
            for (FileQuestion cq : basketSession.getBasket().getFiles()) {
                if (cq.isSelected()) {
                    if (!this.files.contains(cq)) {
                        temp.add(cq);
                    }
                }
            }
            this.files.addAll(temp);
        }
        if ("case".equals(type)) {
            List<CaseQuestion> temp = new LinkedList();
            for (CaseQuestion cq : basketSession.getBasket().getCases()) {
                if (cq.isSelected()) {
                    if (!this.cases.contains(cq)) {
                        temp.add(cq);
                    }
                }
            }
            this.cases.addAll(temp);
        }
        return null;
    }

    public String copyAllQuestion(String type) {
        BasketSession basketSession = JsfHelper.getBean("basketSession");
        if ("choice".equals(type)) {
            for (ChoiceQuestion cq : basketSession.getBasket().getChoices()) {
                if (!this.choices.contains(cq)) {
                    this.choices.add(cq);
                }
            }
            //basketSession.getBasket().getChoices().clear();
        }
        if ("mchoice".equals(type)) {
            for (MultiChoiceQuestion cq : basketSession.getBasket().getMultiChoices()) {
                if (!this.multiChoices.contains(cq)) {
                    this.multiChoices.add(cq);
                }
            }
            //basketSession.getBasket().getMultiChoices().clear();
        }
        if ("fill".equals(type)) {
            for (FillQuestion cq : basketSession.getBasket().getFills()) {
                if (!this.fills.contains(cq)) {
                    this.fills.add(cq);
                }
            }
            //basketSession.getBasket().getFills().clear();
        }
        if ("judge".equals(type)) {
            for (JudgeQuestion cq : basketSession.getBasket().getJudges()) {
                if (!this.judges.contains(cq)) {
                    this.judges.add(cq);
                }
            }
            //basketSession.getBasket().getJudges().clear();
        }
        if ("essay".equals(type)) {
            for (EssayQuestion cq : basketSession.getBasket().getEssaies()) {
                if (!this.essaies.contains(cq)) {
                    this.essaies.add(cq);
                }
            }
            //basketSession.getBasket().getEssaies().clear();
        }
        if ("file".equals(type)) {
            for (FileQuestion cq : basketSession.getBasket().getFiles()) {
                if (!this.files.contains(cq)) {
                    this.files.add(cq);
                }
            }
            //basketSession.getBasket().getFiles().clear();
        }
        if ("case".equals(type)) {
            for (CaseQuestion cq : basketSession.getBasket().getCases()) {
                if (!this.cases.contains(cq)) {
                    this.cases.add(cq);
                }
            }
            //basketSession.getBasket().getCases().clear();
        }
        return null;
    }

    public String clearQuestion(String type) {
        if ("choice".equals(type)) {
            this.getChoices().clear();
        }
        if ("mchoice".equals(type)) {
            this.getMultiChoices().clear();
        }
        if ("fill".equals(type)) {
            this.getFills().clear();
        }
        if ("judge".equals(type)) {
            this.getJudges().clear();
        }
        if ("essay".equals(type)) {
            this.getEssaies().clear();
        }
        if ("file".equals(type)) {
            this.getFiles().clear();
        }
        if ("case".equals(type)) {
            this.getCases().clear();
        }
        return null;
    }

    public void synExamination() {
        IExaminationDAO exDAO = SpringHelper.getSpringBean("ExaminationDAO");
        List<Examination> exams = exDAO.findAllExamination(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        List<Examination> exs = new ArrayList();
        if (exams != null) {
            for (Examination ex : exams) {
                if (ex.getManualPaper() != null) {
                    if (this.paper.getId().equals(ex.getManualPaper().getId())) {
                        exs.add(ex);
                    }
                }
            }
        }
        for (Examination exam : exs) {
            exam.fetchChoiceTotal();
            exam.fetchMultiChoiceTotal();
            exam.fetchFillTotal();
            exam.fetchJudgeTotal();
            exam.fetchEssayTotal();
            exam.fetchFileTotal();
            exam.fetchCaseTotal();
            exDAO.updateExamination(exam);
        }
    }

    public String done() {
        this.resetQuestions();
        //this.paper.setModulePapers(mes);
        if (flag) {
            this.manualDAO.updateExamPaperManual(paper);
            ExamPaperPool.refreshPaper(paper.getId(), "manual");
            this.synExamination();
            this.logger.log("修改了人工试卷：" + paper.getName());
        } else {
            this.logger.log("添加了人工试卷：" + paper.getName());
            this.manualDAO.addExamPaperManual(paper);
        }
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
        //更新考试缓存
        ExaminationService examService = SpringHelper.getSpringBean("ExaminationService");
        examService.reBuildCache();
        return "ListManualPaper?faces-redirect=true";
    }
}
