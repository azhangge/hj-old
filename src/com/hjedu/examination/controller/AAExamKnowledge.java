package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hjedu.examination.dao.IExamKnowledgeDAO;
import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.dao.IKnowledgeLabelDAO;
import com.hjedu.examination.dao.IKnowledgeLabelTypeDAO;
import com.hjedu.examination.entity.CaseQuestion;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.EssayQuestion;
import com.hjedu.examination.entity.ExamKnowledge;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.FileQuestion;
import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.examination.entity.GeneralQuestion;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.examination.entity.KnowledgeLabel;
import com.hjedu.examination.entity.KnowledgeLabelType;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAExamKnowledge implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IExamKnowledgeDAO knowledgeDAO = SpringHelper.getSpringBean("ExamKnowledgeDAO");
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    IKnowledgeLabelDAO examLabelDAO = SpringHelper.getSpringBean("KnowledgeLabelDAO");
    IKnowledgeLabelTypeDAO examLabelTypeDAO = SpringHelper.getSpringBean("KnowledgeLabelTypeDAO");
    ExamKnowledge cq;
    ExamModuleModel em;
    boolean flag = false;
    
    List<KnowledgeLabel> labels;
    List<KnowledgeLabelType> labelTypes;
    

    public ExamKnowledge getCq() {
        return cq;
    }

    public void setCq(ExamKnowledge cq) {
        this.cq = cq;
    }

    public ExamModuleModel getEm() {
        return em;
    }

    public void setEm(ExamModuleModel em) {
        this.em = em;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public List<KnowledgeLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<KnowledgeLabel> labels) {
        this.labels = labels;
    }

    public List<KnowledgeLabelType> getLabelTypes() {
        return labelTypes;
    }

    public void setLabelTypes(List<KnowledgeLabelType> labelTypes) {
        this.labelTypes = labelTypes;
    }

    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        String id = request.getParameter("id");
        String moduleId = request.getParameter("module_id");
        if (id != null) {
            this.flag = true;
            this.cq = this.knowledgeDAO.findExamKnowledge(id);
            this.em = cq.getModule();
        } else {
            this.cq = new ExamKnowledge();
            em = this.moduleDAO.findExamModuleModel(moduleId);
            cq.setModule(em);
        }
        this.loadLabels();
        this.loadRelatedQuestion();
    }
    
    public void loadLabels() {
        this.labelTypes = this.examLabelTypeDAO.findAllKnowledgeLabelType();
        String lstr = this.cq.getLabelStr();
        if (lstr != null) {
            for (KnowledgeLabelType lt : labelTypes) {
                List<KnowledgeLabel> ls = lt.getLabels();
                for (KnowledgeLabel e : ls) {
                    if (lstr.contains(e.getId())) {
                        e.setSelected(true);
                    }
                }
            }
        }
    }

    public void resetLabels() {
        StringBuilder sb = new StringBuilder();
        if (this.labelTypes != null) {
            for (KnowledgeLabelType lt : labelTypes) {
                List<KnowledgeLabel> ls = lt.getLabels();
                for (KnowledgeLabel ex : ls) {
                    if (ex.isSelected()) {
                        sb.append(ex.getId());
                        sb.append(";");
                    }
                }
            }
        }
        this.cq.setLabelStr(sb.toString());
    }

    private void loadRelatedQuestion() {
        ListFillQuestion2 lfq = JsfHelper.getBean("listFillQuestion2");
        ListChoiceQuestion2 lcq = JsfHelper.getBean("listChoiceQuestion2");
        ListJudgeQuestion2 ljq = JsfHelper.getBean("listJudgeQuestion2");
        ListMultiChoiceQuestion2 lmq = JsfHelper.getBean("listMultiChoiceQuestion2");
        ListEssayQuestion2 leq = JsfHelper.getBean("listEssayQuestion2");
        ListFileQuestion2 lffq = JsfHelper.getBean("listFileQuestion2");
        ListCaseQuestion2 cffq = JsfHelper.getBean("listCaseQuestion2");

        List<ChoiceQuestion> choices = cq.getChoices();
        List<ChoiceQuestion> css = lcq.getQuestions();
        if (choices != null) {
            for (ChoiceQuestion c : css) {
                if (choices.contains(c)) {
                    c.setSelected(true);
                }
            }
        }

        List<FillQuestion> fills = cq.getFills();
        List<FillQuestion> fss = lfq.getQuestions();
        if (fills != null) {
            for (FillQuestion c : fss) {
                if (fills.contains(c)) {
                    c.setSelected(true);
                }
            }
        }

        List<MultiChoiceQuestion> multiChoices = cq.getMultiChoices();
        List<MultiChoiceQuestion> mss = lmq.getQuestions();
        if (multiChoices != null) {
            for (GeneralQuestion c : mss) {
                MultiChoiceQuestion cq=(MultiChoiceQuestion)c;
                if (multiChoices.contains(cq)) {
                    c.setSelected(true);
                }
            }
        }

        List<JudgeQuestion> judges = cq.getJudges();
        List<JudgeQuestion> jss = ljq.getQuestions();
        if (judges != null) {
            for (JudgeQuestion c : jss) {
                if (judges.contains(c)) {
                    c.setSelected(true);
                }
            }
        }

        List<EssayQuestion> essaies = cq.getEssaies();
        List<EssayQuestion> ess = leq.getQuestions();
        if (essaies != null) {
            for (EssayQuestion c : ess) {
                if (essaies.contains(c)) {
                    c.setSelected(true);
                }
            }
        }

        List<FileQuestion> files = cq.getFiles();
        List<FileQuestion> ffss = lffq.getQuestions();
        if (files != null) {
            for (FileQuestion c : ffss) {
                if (files.contains(c)) {
                    c.setSelected(true);
                }
            }
        }

        List<CaseQuestion> cases = cq.getCases();
        List<CaseQuestion> cfss = cffq.getQuestions();
        if (cases != null) {
            for (CaseQuestion c : cfss) {
                if (cases.contains(c)) {
                    c.setSelected(true);
                }
            }
        }
    }

    private void buildRelatedQuestion() {
        ListFillQuestion2 lfq = JsfHelper.getBean("listFillQuestion2");
        ListChoiceQuestion2 lcq = JsfHelper.getBean("listChoiceQuestion2");
        ListJudgeQuestion2 ljq = JsfHelper.getBean("listJudgeQuestion2");
        ListMultiChoiceQuestion2 lmq = JsfHelper.getBean("listMultiChoiceQuestion2");
        ListEssayQuestion2 leq = JsfHelper.getBean("listEssayQuestion2");
        ListFileQuestion2 lffq = JsfHelper.getBean("listFileQuestion2");
        ListCaseQuestion2 cffq = JsfHelper.getBean("listCaseQuestion2");

        List<ChoiceQuestion> choices = new LinkedList();
        List<ChoiceQuestion> css = lcq.getQuestions();
        for (GeneralQuestion c : css) {
            if (c.isSelected()) {
                choices.add((ChoiceQuestion)c);
            }
        }
        this.cq.setChoices(choices);

        List<FillQuestion> fills = new LinkedList();
        List<FillQuestion> fss = lfq.getQuestions();
        for (FillQuestion c : fss) {
            if (c.isSelected()) {
                fills.add(c);
            }
        }
        this.cq.setFills(fills);

        List<MultiChoiceQuestion> multiChoices = new LinkedList();
        List<MultiChoiceQuestion> mss = lmq.getQuestions();
        for (GeneralQuestion c : mss) {
            if (c.isSelected()) {
                multiChoices.add((MultiChoiceQuestion)c);
            }
        }
        this.cq.setMultiChoices(multiChoices);

        List<JudgeQuestion> judges = new LinkedList();
        List<JudgeQuestion> jss = ljq.getQuestions();
        for (JudgeQuestion c : jss) {
            if (c.isSelected()) {
                judges.add(c);
            }
        }
        this.cq.setJudges(judges);

        List<EssayQuestion> essaies = new LinkedList();
        List<EssayQuestion> ess = leq.getQuestions();
        for (EssayQuestion c : ess) {
            if (c.isSelected()) {
                essaies.add(c);
            }
        }
        this.cq.setEssaies(essaies);

        List<FileQuestion> files = new LinkedList();
        List<FileQuestion> ffss = lffq.getQuestions();
        for (FileQuestion c : ffss) {
            if (c.isSelected()) {
                files.add(c);
            }
        }
        this.cq.setFiles(files);

        List<CaseQuestion> cases = new LinkedList();
        List<CaseQuestion> cfss = cffq.getQuestions();
        for (CaseQuestion c : cfss) {
            if (c.isSelected()) {
                cases.add(c);
            }
        }
        this.cq.setCases(cases);
    }

    public String done() {
        this.resetLabels();
        this.buildRelatedQuestion();
        if (flag) {
            //MyLogger.echo("选项数：" + choices.size());
            this.knowledgeDAO.updateExamKnowledge(cq);
            this.logger.log("修改了模块：" + cq.getModule().getName() + "下的知识点：" + cq.getTitle());
        } else {
            this.knowledgeDAO.addExamKnowledge(cq);
            this.logger.log("添加了模块：" + cq.getModule().getName() + "下的知识点：" + cq.getTitle());
        }
        //return "ListChoiceQuestion?faces-redirect=true";
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        try {
            String url = "ListExamKnowledge.jspx?module_id=" + this.cq.getModule().getId();
            MyLogger.echo(url);
            response.sendRedirect(url);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addAndContinue() {
        if (!flag) {
            this.knowledgeDAO.addExamKnowledge(cq);
            this.cq = new ExamKnowledge();
            cq.setModule(em);
        }

    }
}
