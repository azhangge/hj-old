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
import com.hjedu.examination.dao.IJudgeQuestionDAO;
import com.hjedu.examination.entity.CaseQuestion;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.JudgeCaseAgent;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.examination.util.ExamCaseServiceUtils;
import com.hjedu.platform.service.ILogService;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class CaseEditJudge implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    ICaseQuestionDAO questionDAO = SpringHelper.getSpringBean("CaseQuestionDAO");
    IJudgeQuestionDAO fillDAO = SpringHelper.getSpringBean("JudgeQuestionDAO");
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    CaseQuestion cq;
    ExamModuleModel em;
    boolean flag = false;
    //ChoiceQuestion newChoice;
    //JudgeQuestion newJudge;
    JudgeCaseAgent fillAgent = new JudgeCaseAgent();

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

    public JudgeCaseAgent getJudgeAgent() {
        return fillAgent;
    }

    public void setJudgeAgent(JudgeCaseAgent fillAgent) {
        this.fillAgent = fillAgent;
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
                cq.setChoices(new ArrayList<ChoiceQuestion>());
            }
            //新增加的综合题的问答题是空的
            if (cq.getJudges() == null) {
                cq.setJudges(new ArrayList<JudgeQuestion>());
            }
        }
    }

    public String delJudge(String id) {
        List<JudgeQuestion> cs = this.cq.getJudges();
        JudgeQuestion ccqq = null;
        for (JudgeQuestion c : cs) {
            if (c.getId().equals(id)) {
                ccqq = c;
                break;
            }
        }
        if (ccqq != null) {
            cs.remove(ccqq);
            this.fillDAO.deleteJudgeQuestion(ccqq.getId());
        }
        return null;
    }

    public String addNewJudge() {
        //this.newChoice = new ChoiceQuestion();
        this.fillAgent.initFromCase(null, cq);
        return null;
    }

    public String editJudge(String id) {
        List<JudgeQuestion> cs = this.cq.getJudges();
        System.out.println(cs.size());
        JudgeQuestion temp = null;
        for (JudgeQuestion c : cs) {
            if (c.getId().equals(id)) {
                temp = c;
                break;
            }
        }
        if (temp != null) {
            System.out.println(temp.getName());
            this.fillAgent.initFromCase(temp, cq);
        }
        return null;
    }

    public String done() {
        if (flag) {
            this.questionDAO.updateCaseQuestion(cq);
            //this.logger.log("修改了模块：" + cq.getModule().getName() + "下的综合题：" + cq.getCleanName());
        }
        //return "ListJudgeQuestion?faces-redirect=true";
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
        List<JudgeQuestion> cqss = this.cq.getJudges();
        if (cqss != null) {
            for (JudgeQuestion cqt : cqss) {
                if (id.equals(cqt.getId())) {
                    this.fillDAO.updateJudgeQuestion(cqt);
                    break;
                }
            }
        }
        return null;
    }
}
