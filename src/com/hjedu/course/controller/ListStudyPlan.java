package com.hjedu.course.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import com.hjedu.course.dao.IStudyPlanDAO;
import com.hjedu.course.entity.StudyPlan;
import com.hjedu.course.lazy.LazyStudyPlan;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListStudyPlan implements Serializable {
	private static final long serialVersionUID = 1L;
	ILogService logger = SpringHelper.getSpringBean("LogService");
	LazyStudyPlan casusList;
    private IStudyPlanDAO studyPlanDAO = SpringHelper.getSpringBean("StudyPlanDAO");

	public LazyStudyPlan getCasusList() {
        return casusList;
    }

    public void setCasusList(LazyStudyPlan casusList) {
        this.casusList = casusList;
    }

    @PostConstruct
    public void init() {
    	this.casusList = new LazyStudyPlan();
    }

    public void batDeleteUser() {
        for (StudyPlan bu : this.casusList) {
            if (bu.isSelected()) {
                this.logger.log("删除了学习计划：" + bu.getName());
                this.studyPlanDAO.deleteStudyPlan(bu.getId());
            }
        }
        init();
    }
    public String editOrd(String id) {
        for (StudyPlan cq : this.casusList) {
            if (id.equals(cq.getId())) {
                this.studyPlanDAO.updateStudyPlan(cq);
                break;
            }
        }
        return null;
    }
}
