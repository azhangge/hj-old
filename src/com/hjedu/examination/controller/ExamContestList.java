package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IExamLabelTypeDAO;
import com.hjedu.examination.entity.ExamLabel;
import com.hjedu.examination.entity.ExamLabelType;
import com.hjedu.examination.entity.contest.ExamContestSession;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.entity.SystemConfig;
import com.huajie.exam.agent.ContestAgent;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ExamContestList implements Serializable {

    List<ExamContestSession> exams;
    List<ExamContestSession> accessibleExams;
    List<ExamContestSession> todayExams;
    Date nowTime = new Date();
    String lid = null;
    ISystemConfigDAO systemConfigDAO = SpringHelper.getSpringBean("SystemConfigDAO");
    IExamLabelTypeDAO labelTypeDAO = SpringHelper.getSpringBean("ExamLabelTypeDAO");
    List<ExamLabelType> labelTypes;

    public List<ExamContestSession> getExams() {
        return exams;
    }

    public void setExams(List<ExamContestSession> exams) {
        this.exams = exams;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public List<ExamLabelType> getLabelTypes() {
        return labelTypes;
    }

    public void setLabelTypes(List<ExamLabelType> labelTypes) {
        this.labelTypes = labelTypes;
    }

    public List<ExamContestSession> getTodayExams() {
        List<ExamContestSession> exs = new LinkedList();
        if (this.exams != null) {
            for (ExamContestSession ex : this.exams) {
                if (ex.isIfToday()) {
                    exs.add(ex);
                }
            }
        }
        todayExams = exs;
        Collections.sort(todayExams);
        return todayExams;
    }

    public void setTodayExams(List<ExamContestSession> todayExams) {
        this.todayExams = todayExams;
    }

    public Date getNowTime() {
        return nowTime;
    }

    public void setNowTime(Date nowTime) {
        this.nowTime = nowTime;
    }

    public List<ExamContestSession> getAccessibleExams() {
        return accessibleExams;
    }

    public void setAccessibleExams(List<ExamContestSession> accessibleExams) {
        this.accessibleExams = accessibleExams;
    }

    @PostConstruct
    public void init() {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        lid = JsfHelper.getRequest().getParameter("lid");
        if (lid != null) {
            List<ExamContestSession> css = new ArrayList();
            List<ExamContestSession> contests = ContestAgent.getContests();
            for (ExamContestSession es : contests) {
                if (es.getLabelStr() != null) {
                    if (es.getLabelStr().contains(lid)) {
                        css.add(es);
                    }
                }
            }
            this.exams = css;
        } else {
            this.exams = ContestAgent.getContests();
        }
        Collections.sort(exams);
        
        this.labelTypes = this.labelTypeDAO.findAllExamLabelTypeByBusinessId(businessId);
        Collections.sort(labelTypes);
        
        SystemConfig sc = this.systemConfigDAO.getSystemConfig();
        if (sc.getOnlyDept()) {
            //this.filterAccessibleExams();//不适用于竞赛
            this.filterLabelTypes();
        }
        
        
    }
    
    
    public List<ExamContestSession> filterAccessibleExams() {
        List<ExamContestSession> exs = new LinkedList();
        if (this.exams != null) {
            for (ExamContestSession ex : this.exams) {
                if (ex.isAvailable()) {
                    exs.add(ex);
                }
            }
        }
        accessibleExams = exs;
        return accessibleExams;
    }
    
    //筛选出本部门需要显示的考试标签，
    public void filterLabelTypes() {
        ClientSession cs = JsfHelper.getBean("clientSession");
        BbsUser bu = cs.getUsr();
        if (bu != null) {
            List<ExamContestSession> exs = this.getTodayExams();
            for (ExamLabelType lt : this.labelTypes) {
                List<ExamLabel> labels = lt.getContestLabels();
                List<ExamLabel> labels2 = new ArrayList();
                for (ExamLabel el : labels) {
                    for (ExamContestSession ex : exs) {
                        String str = ex.getLabelStr();
                        if (str != null) {
                            if (str.contains(el.getId())) {
                                labels2.add(el);
                                break;
                            }
                        }
                    }
                }
                lt.setContestLabels2(labels2);
            }
        }
    }
    
    

    public String paticipateExam(String examId) {
        return null;
    }

}
