package com.hjedu.examination.controller;


import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.examination.dao.IContestCaseDAO;
import com.hjedu.examination.dao.IExamContestDAO;
import com.hjedu.examination.entity.contest.ContestCase;
import com.hjedu.examination.entity.contest.ExamContestSession;
import com.huajie.exam.thread.ContestCaseRanker;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ContestCaseRankList implements Serializable {

    IExamContestDAO contestDAO = SpringHelper.getSpringBean("ExamContestDAO");
    IContestCaseDAO examCaseDAO = SpringHelper.getSpringBean("ContestCaseDAO");
    List<ContestCase> cases;
    ExamContestSession contest;
    String sessionStr;

    public ExamContestSession getContest() {
        return contest;
    }

    public void setContest(ExamContestSession contest) {
        this.contest = contest;
    }

    public String getSessionStr() {
        return sessionStr;
    }

    public void setSessionStr(String sessionStr) {
        this.sessionStr = sessionStr;
    }
    

    public List<ContestCase> getCases() {
        return cases;
    }

    public void setCases(List<ContestCase> cases) {
        this.cases = cases;
    }

    @PostConstruct
    public void init() {
        String cid = JsfHelper.getRequest().getParameter("cid");
        String str = JsfHelper.getRequest().getParameter("str");
        if (cid != null&&str!=null) {
            this.initx(cid, str);
        }
    }
    
    public void initx(String cid,String str) {
        if (cid != null&&str!=null) {
            this.cases = this.examCaseDAO.findContestCaseByContestAndSession(cid,str);
            ContestCaseRanker.rankContestCase(cases);
            this.contest=this.contestDAO.findExamContest(cid);
            sessionStr=str;
        }
    }

}
