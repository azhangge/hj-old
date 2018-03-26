package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IExamCaseLogDAO;
import com.hjedu.examination.dao.IExamLabelTypeDAO;
import com.hjedu.examination.entity.ExamCaseLog;
import com.hjedu.examination.entity.ExamLabel;
import com.hjedu.examination.entity.ExamLabelType;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.service.impl.ExaminationService;
import com.hjedu.platform.controller.ApplicationBean;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.entity.SystemConfig;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ExaminationList implements Serializable {

    List<Examination> exams;
    List<Examination> accessibleExams;
    //IExaminationDAO examinationDAO = SpringHelper.getSpringBean("ExaminationDAO");
    ExaminationService examService = SpringHelper.getSpringBean("ExaminationService");
    //ISystemConfigDAO systemConfigDAO = SpringHelper.getSpringBean("SystemConfigDAO");
    IExamLabelTypeDAO labelTypeDAO = SpringHelper.getSpringBean("ExamLabelTypeDAO");
    IExamCaseLogDAO logDAO = SpringHelper.getSpringBean("ExamCaseLogDAO");
    
    List<ExamLabelType> labelTypes;
    SystemConfig systemConfig=null;//系统配置

    Date nowTime = new Date();
    String lid = null;
    String userId = "";

    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<Examination> getExams() {
        //this.checkRetry(exams);
        return exams;
    }

    public void setExams(List<Examination> exams) {
        this.exams = exams;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public Date getNowTime() {
        return nowTime;
    }

    public void setNowTime(Date nowTime) {
        this.nowTime = nowTime;
    }

    public List<Examination> getAccessibleExams() {
//        this.filterAccessibleExams();
    	accessibleExams=this.exams;
        //this.checkRetry(accessibleExams);
        return accessibleExams;
    }

    public void setAccessibleExams(List<Examination> accessibleExams) {
        this.accessibleExams = accessibleExams;
    }

    public List<ExamLabelType> getLabelTypes() {
        this.filterLabelTypes();
        return labelTypes;
    }

    public void setLabelTypes(List<ExamLabelType> labelTypes) {
        this.labelTypes = labelTypes;
    }

    @PostConstruct
    public void init() {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        ApplicationBean ab=JsfHelper.getBean("applicationBean");
        this.systemConfig=ab.getSystemConfig();
        
        lid = JsfHelper.getRequest().getParameter("lid");
        if (lid != null) {
            //克隆新数据，防止对唯一缓存数据的共同修改，相互影响
            //List<Examination> temps=this.examService.findExaminationByLabel(lid);
            //this.exams = this.examService.cloneList(temps);
            this.exams=this.examService.findExaminationByLabel(lid);
        } else {
            //List<Examination> temps=this.examService.findAllShowedExamination();
            //this.exams = this.examService.cloneList(temps);
            this.exams=this.examService.findAllShowedExamination();
        }
        Collections.sort(exams);
        Collections.reverse(exams);

        this.labelTypes = this.labelTypeDAO.findAllExamLabelTypeByBusinessId(businessId);
        Collections.sort(labelTypes);
        this.checkRetry(exams);
        ClientSession cs = JsfHelper.getBean("clientSession");
        this.userId = cs.getUsr().getId();
        if (systemConfig.getOnlyDept()) {
            //this.filterAccessibleExams();
            //this.filterLabelTypes();
        }//此处的处理已经移动到相应的GET方法中
    }

    /**
     * 根据用户部门获取有权限参考的考试
     * @return 
     */
    public List<Examination> filterAccessibleExams() {
        ClientSession cs = JsfHelper.getBean("clientSession");
        BbsUser bu = cs.getUsr();
        List<Examination> exs = new ArrayList();
        if (bu != null) {
            if (this.exams != null) {
                for (Examination ex : this.exams) {
                    if (bu.testIfIn(ex.getGroupStr())) {
                        exs.add(ex);
                    }
                }
            }
        }
        //this.checkRetry(exs);
        accessibleExams = exs;
        return accessibleExams;
    }

    /**
     * 
     */
    //筛选出本部门需要显示的考试标签，
    public void filterLabelTypes() {
        ClientSession cs = JsfHelper.getBean("clientSession");
        BbsUser bu = cs.getUsr();
        if (bu != null) {
            if (systemConfig.getOnlyDept()) {
                List<Examination> exs = this.getAccessibleExams();
                for (ExamLabelType lt : this.labelTypes) {
                    List<ExamLabel> labels = lt.getExamLabels();
                    List<ExamLabel> labels2 = new ArrayList();
                    for (ExamLabel el : labels) {
                        for (Examination ex : exs) {
                            String str = ex.getLabelStr();
                            if (str != null) {
                                if (str.contains(el.getId())) {
                                    labels2.add(el);
                                    break;
                                }
                            }
                        }
                    }
                    lt.setExamLabels2(labels2);
                }
            }
        }
    }

    /**
     * 点击 开始考试 后触发该方法
     * @param examId
     * @return 
     */
    public String paticipateExam(String examId) {
        final ExamCaseLog log = new ExamCaseLog();
        log.setExamination(this.examService.findExamination(examId));
        ClientSession cs = JsfHelper.getBean("clientSession");
        BbsUser bu = cs.getUsr();
        if (bu != null) {
            log.setUser(bu);
            HttpServletRequest req = JsfHelper.getRequest();
            log.setIp(req.getRemoteAddr());
            //增加抽卷LOG，并验证是否是少于抽卷时间
            IExamCaseLogDAO logDAO2 = SpringHelper.getSpringBean("ExamCaseLogDAO");
            logDAO2.addExamCaseLog(log);
            this.checkRetry(exams);
        }
        return null;
    }

    /**
     * 验证每个考试的抽卷间隔
     *
     * @param exs
     */
    private void checkRetry(List<Examination> exs) {
        ClientSession cs = JsfHelper.getBean("clientSession");
        BbsUser bu = cs.getUsr();
        if (bu != null) {
            String uid = bu.getId();
            for (Examination ex : exs) {
                if (ex.isIfTestRetry()) {
                    ExamCaseLog log = logDAO.findLatestExamCaseLogByUser(uid, ex.getId());
                    ex.setRetry(false);
                    if (log != null) {
                        long l1 = System.currentTimeMillis();
                        long l2 = log.getGenTime().getTime();
                        long interval = l1 - l2;
                        if (interval < 1000L * 60L * ex.getRetryInterval()) {
                            ex.setRetry(true);
                        } else {
                            //由于考试信息已经被缓存，设置为true的不会自动变为false，必须手工设置
                            ex.setRetry(false);
                        }
                    }
                }
            }
        }
    }

    

}
