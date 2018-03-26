package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hjedu.examination.dao.ICaseQuestionDAO;
import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.entity.CaseQuestion;
import com.hjedu.examination.entity.EssayQuestion;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.MultiChoiceCaseAgent;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.hjedu.examination.util.ExamCaseServiceUtils;
import com.hjedu.platform.service.ILogService;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class CaseEditMultiChoice implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    ICaseQuestionDAO questionDAO = SpringHelper.getSpringBean("CaseQuestionDAO");
    IMultiChoiceQuestionDAO choiceDAO = SpringHelper.getSpringBean("MultiChoiceQuestionDAO");
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    CaseQuestion cq;
    ExamModuleModel em;
    boolean flag = false;
    //MultiChoiceQuestion newChoice;
    //EssayQuestion newEssay;
    MultiChoiceCaseAgent choiceAgent = new MultiChoiceCaseAgent();

    public CaseQuestion getCq() {
        return cq;
    }

    public void setCq(CaseQuestion cq) {
        this.cq = cq;
    }

    public ExamModuleModel getEm() {
        return em;
    }

    public void setEm(ExamModuleModel em) {
        this.em = em;
    }

    public MultiChoiceCaseAgent getChoiceAgent() {
        return choiceAgent;
    }

    public void setChoiceAgent(MultiChoiceCaseAgent choiceAgent) {
        this.choiceAgent = choiceAgent;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        String id = request.getParameter("id");
        String moduleId = request.getParameter("module_id");
        if (id != null) {
            this.flag = true;
            this.cq = this.questionDAO.findCaseQuestion(id);
            ExamCaseServiceUtils.orderCaseQuestion(cq);
            this.em = cq.getModule();
            //新增加的综合题的单选题是空的
            if (cq.getChoices() == null) {
                cq.setMultiChoices(new ArrayList<MultiChoiceQuestion>());
            }
            //新增加的综合题的问答题是空的
            if (cq.getEssaies() == null) {
                cq.setEssaies(new ArrayList<EssayQuestion>());
            }
        }

    }

    public String delChoice(String id) {
        List<MultiChoiceQuestion> cs = this.cq.getMultiChoices();
        MultiChoiceQuestion ccqq = null;
        for (MultiChoiceQuestion c : cs) {
            if (c.getId().equals(id)) {
                ccqq = c;
                break;
            }
        }
        if (ccqq != null) {
            cs.remove(ccqq);
            this.choiceDAO.deleteMultiChoiceQuestion(ccqq.getId());
        }
        return null;
    }

    public String addNewChoice() {
        //this.newChoice = new MultiChoiceQuestion();
        this.choiceAgent.initFromCase(null, cq);
        return null;
    }

    public String editChoice(String id) {
        List<MultiChoiceQuestion> cs = this.cq.getMultiChoices();
        System.out.println(cs.size());
        MultiChoiceQuestion temp = null;
        for (MultiChoiceQuestion c : cs) {
            if (c.getId().equals(id)) {
                temp = c;
                break;
            }
        }
        if (temp != null) {
            System.out.println(temp.getName());
            this.choiceAgent.initFromCase(temp, cq);
        }
        return null;
    }

    public String done() {
//        List<EssayQuestion> cqsTemp = cq.getEssaies();
//        for (EssayQuestion cqst : cqsTemp) {
//            System.out.println(cqst.getName());
//        }
        if (flag) {
            this.questionDAO.updateCaseQuestion(cq);
            this.logger.log("修改了模块：" + cq.getModule().getName() + "下的综合题：" + cq.getCleanName());
        }
        //return "ListFillQuestion?faces-redirect=true";
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        try {
            String url = "AACaseQuestion.jspx?id=" + this.cq.getId();
            MyLogger.echo(url);
            response.sendRedirect(url);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
        }
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
        return null;
    }

    public String editOrd(String id) {
        List<MultiChoiceQuestion> cqss = this.cq.getMultiChoices();
        if (cqss != null) {
            for (MultiChoiceQuestion cqt : cqss) {
                if (id.equals(cqt.getId())) {
                    this.choiceDAO.updateMultiChoiceQuestion(cqt);
                    break;
                }
            }
        }
        return null;
    }
}
