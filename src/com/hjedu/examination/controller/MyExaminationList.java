package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.dao.ILessonTypeLogDAO;
import com.hjedu.course.entity.LessonType;
import com.hjedu.course.entity.LessonTypeLog;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IExamCaseLogDAO;
import com.hjedu.examination.dao.IExamLabelTypeDAO;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.ExamCaseLog;
import com.hjedu.examination.entity.ExamLabel;
import com.hjedu.examination.entity.ExamLabelType;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.service.impl.ExaminationService;
import com.hjedu.platform.controller.ApplicationBean;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.entity.SystemConfig;
import com.huajie.app.util.StringUtil;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class MyExaminationList implements Serializable {

    List<Examination> exams;
    List<Examination> accessibleExams;
    ExaminationService examService = SpringHelper.getSpringBean("ExaminationService");
    IExamLabelTypeDAO labelTypeDAO = SpringHelper.getSpringBean("ExamLabelTypeDAO");
    IExamCaseLogDAO logDAO = SpringHelper.getSpringBean("ExamCaseLogDAO");
    ILessonTypeDAO lessonTypeDAO = SpringHelper.getSpringBean("LessonTypeDAO");
	ILessonTypeLogDAO lessonTypeLogDAO = SpringHelper.getSpringBean("LessonTypeLogDAO");
	IExaminationDAO examDAO = SpringHelper.getSpringBean("ExaminationDAO");
    
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
    	accessibleExams=this.exams;
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
        ClientSession cs = JsfHelper.getBean("clientSession");
        if(cs==null||cs.getUsr()==null){
        	return;
        }
        this.userId = cs.getUsr().getId();
        if (lid != null) {
            //克隆新数据，防止对唯一缓存数据的共同修改，相互影响
            //List<Examination> temps=this.examService.findExaminationByLabel(lid);
            //this.exams = this.examService.cloneList(temps);
            this.exams=this.examService.findExaminationByLabel(lid);
        } else {
        	this.exams = new LinkedList<>();
        	List<Examination> exams = examDAO.findAllExamination(CookieUtils.getBusinessId(JsfHelper.getRequest()));

			for(Examination e : exams){
				if(e.getUserStr()!=null && e.getUserStr().contains(userId)){
					this.exams.add(e);
					if(e.checkParticipateTimes(userId)<e.getMaxNum()){
						e.setIfOpen(true);				
					}else{
						e.setIfOpen(false);
					}
				}
        	}
        }
        Collections.sort(exams);
        Collections.reverse(exams);

        this.labelTypes = this.labelTypeDAO.findAllExamLabelTypeByBusinessId(businessId);
        Collections.sort(labelTypes);
        this.checkRetry(exams);
        if (systemConfig.getOnlyDept()) {
            //this.filterAccessibleExams();
            //this.filterLabelTypes();
        }//此处的处理已经移动到相应的GET方法中
    }

//    @PostConstruct
//    public void init() {
//        ApplicationBean ab=JsfHelper.getBean("applicationBean");
//        this.systemConfig=ab.getSystemConfig();
//        
//        lid = JsfHelper.getRequest().getParameter("lid");
//        ClientSession cs = JsfHelper.getBean("clientSession");
//        if(cs==null||cs.getUsr()==null){
//        	return;
//        }
//        this.userId = cs.getUsr().getId();
//        if (lid != null) {
//            //克隆新数据，防止对唯一缓存数据的共同修改，相互影响
//            //List<Examination> temps=this.examService.findExaminationByLabel(lid);
//            //this.exams = this.examService.cloneList(temps);
//            this.exams=this.examService.findExaminationByLabel(lid);
//        } else {
//        	this.exams = new LinkedList<>();
//        	List<LessonType> lts = new LinkedList<>();
//        	if(cs!=null&&cs.getUsr()!=null){
//        		BbsUser user = cs.getUsr();
//        		lts = lessonTypeDAO.findLessonTypeByIdList(StringUtil.idsToIdList(user.getLessonTypeStr()));
//        	}
//        	List<Examination> exams = examDAO.findAllExamination();
//        	List<LessonTypeLog> ltls = lessonTypeLogDAO.findLessonTypeLogByUsr(userId);
//        	Map<String,LessonTypeLog> logMap = new HashMap<>();
//        	for(LessonTypeLog ltl : ltls){
//        		if(ltl!=null){
//        			LessonType lt = ltl.getLessonType();
//        			if(lt!=null){
//        				logMap.put(lt.getId(), ltl);
//        			}
//        		}
//        	}
//        	for(LessonType lt : lts){
//        		if(lt!=null){
//        			float num = logMap.get(lt.getId())==null?0:logMap.get(lt.getId()).getFinishedClassNum();
//        			boolean bool = (num >= lt.getOpenExamNum());
//					for(Examination e : exams){
//						if(e.getUserStr()!=null && e.getUserStr().contains(userId) && e.checkParticipateTimes(userId)>e.getMaxNum() && bool){
//							if(e.checkParticipateTimes(userId)>e.getMaxNum()){
//								e.setIfOpen(true);
//							}else{
//								e.setIfOpen(false);
//							}
//							
//							if(!this.exams.contains(e)){
//								this.exams.add(e);
//							}
//						}else if(lt.getExamStr()!=null && lt.getExamStr().contains(e.getId()) ){
//							if(bool && e.checkParticipateTimes(userId)>e.getMaxNum()){
//								e.setIfOpen(true);
//							}else{
//								e.setIfOpen(false);
//							}
//							if(!this.exams.contains(e)){
//								this.exams.add(e);
//							}
//						}
//					}
//				}
//        	}
//        }
//        Collections.sort(exams);
//        Collections.reverse(exams);
//
//        this.labelTypes = this.labelTypeDAO.findAllExamLabelType();
//        Collections.sort(labelTypes);
//        this.checkRetry(exams);
//        if (systemConfig.getOnlyDept()) {
//            //this.filterAccessibleExams();
//            //this.filterLabelTypes();
//        }//此处的处理已经移动到相应的GET方法中
//    }
    
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
