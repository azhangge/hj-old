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
import com.hjedu.examination.dao.IBuffetExaminationDAO;
import com.hjedu.examination.dao.IExamLabelTypeDAO;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.entity.ExamLabel;
import com.hjedu.examination.entity.ExamLabelType;
import com.hjedu.examination.entity.buffet.BuffetExamination;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.huajie.app.util.StringUtil;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class BuffetExaminationList implements Serializable {

    List<BuffetExamination> exams = new LinkedList<>();
    List<BuffetExamination> accessibleExams;
    IBuffetExaminationDAO examinationDAO = SpringHelper.getSpringBean("BuffetExaminationDAO");
    Date nowTime = new Date();
    String lid = null;
    ISystemConfigDAO systemConfigDAO = SpringHelper.getSpringBean("SystemConfigDAO");
    IExamLabelTypeDAO labelTypeDAO = SpringHelper.getSpringBean("ExamLabelTypeDAO");
    List<ExamLabelType> labelTypes;

    public List<BuffetExamination> getExams() {
        //this.checkRetry(exams);
        return exams;
    }

    public void setExams(List<BuffetExamination> exams) {
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

    public Date getNowTime() {
        return nowTime;
    }

    public void setNowTime(Date nowTime) {
        this.nowTime = nowTime;
    }

    public List<BuffetExamination> getAccessibleExams() {
        return accessibleExams;
    }

    public void setAccessibleExams(List<BuffetExamination> accessibleExams) {
        this.accessibleExams = accessibleExams;
    }

    public IBuffetExaminationDAO getBuffetExaminationDAO() {

        return examinationDAO;
    }

    public void setBuffetExaminationDAO(IBuffetExaminationDAO examinationDAO) {
        this.examinationDAO = examinationDAO;
    }

    @PostConstruct
    public void init() {
        lid = JsfHelper.getRequest().getParameter("lid");
        String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        if (lid != null) {
            this.exams = this.examinationDAO.findExaminationByLabelAndBusinessId(lid, businessId);
        } else {
        	List<BuffetExamination> bes = this.examinationDAO.findAllShowedExaminationByBusinessId(businessId);
        	ClientSession cs = JsfHelper.getBean("clientSession");
        	if(cs!=null&&cs.getUsr()!=null){
        		BbsUser user = cs.getUsr();
        		String str = user.getGroupStr();
        		if(StringUtil.isNotEmpty(str)){
        			for(BuffetExamination be : bes){
        				if(be!=null){
        					List<DictionaryModel> dms = be.getDepartments();
        					for(DictionaryModel dm : dms){
        						if(dm!=null){
        							if(str.contains(dm.getId())){
        								this.exams.add(be);
        								break;
        							}
        						}
        					}
        				}
        			}
        		}
        	}
        }

        //Collections.sort(exams);
        Collections.reverse(exams);
        //this.checkRetry();
        this.accessibleExams = this.exams;
//        SystemConfig sc = this.systemConfigDAO.getSystemConfig();
//        if (sc.getOnlyDept()) {
//            this.filterAccessibleExams();
//            this.filterLabelTypes();
//        }

    }

    public List<BuffetExamination> filterAccessibleExams() {
        ClientSession cs = JsfHelper.getBean("clientSession");
        String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        BbsUser bu = cs.getUsr();
//        String gstr = "xxzzwwttooff";
//        if (bu != null) {
//            gstr = bu.getGroupId();
//        }
        List<BuffetExamination> exs = new LinkedList<BuffetExamination>();
        if (bu != null) {
            if (this.exams != null) {
                for (BuffetExamination ex : this.exams) {
                    if (bu.testIfIn(ex.getGroupStr())) {
                        exs.add(ex);
                    }
                }
            }
        }

        this.labelTypes = this.labelTypeDAO.findAllExamLabelTypeByBusinessId(businessId);
        Collections.sort(labelTypes);

        //this.checkRetry(exs);
        accessibleExams = exs;
        return accessibleExams;
    }

    //筛选出本部门需要显示的考试标签，
    public void filterLabelTypes() {
        ClientSession cs = JsfHelper.getBean("clientSession");
        BbsUser bu = cs.getUsr();
        if (bu != null) {
            List<BuffetExamination> exs = this.getAccessibleExams();
            for (ExamLabelType lt : this.labelTypes) {
                List<ExamLabel> labels = lt.getBuffetLabels();
                List<ExamLabel> labels2 = new ArrayList();
                for (ExamLabel el : labels) {
                    for (BuffetExamination ex : exs) {
                        String str = ex.getLabelStr();
                        if (str != null) {
                            if (str.contains(el.getId())) {
                                labels2.add(el);
                                break;
                            }
                        }
                    }
                }
                lt.setBuffetLabels2(labels2);
            }
        }
    }

}
