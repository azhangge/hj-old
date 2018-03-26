package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.entity.ExamLabelType;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.service.impl.ExaminationService;
import com.hjedu.platform.service.ILogService;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.exam.thread.ExamCaseRanker;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListExamination implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    ExaminationService examDAO = SpringHelper.getSpringBean("ExaminationService");
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    List<Examination> exams;
    ExamModuleModel module;
    private SelectItem[] filterOptions;

    public List<Examination> getExams() {
        return exams;
    }

    public void setExams(List<Examination> exams) {
        this.exams = exams;
    }

    public ExamModuleModel getModule() {
        return module;
    }

    public void setModule(ExamModuleModel module) {
        this.module = module;
    }

    public SelectItem[] getFilterOptions() {
		return filterOptions;
	}

	public void setFilterOptions(SelectItem[] filterOptions) {
		this.filterOptions = filterOptions;
	}

	@PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        this.exams = this.examDAO.findAllClones();
        //由于有缓存，在初始化综合考试界面时过滤数据
        this.exams = ExternalUserUtil.filterByAdmin(this.exams);
        this.createFilterOptions();
    }
    
    private void createFilterOptions() {
        SelectItem[] options = new SelectItem[3];
        options[0] = new SelectItem("", "请选择");
        options[1] = new SelectItem("0", "默认考试");
        options[2] = new SelectItem("1", "集中考试");
        this.filterOptions = options;
    }

    public void delete(String id) {
        Examination ee = this.examDAO.findExamination(id);
        this.logger.log("删除了考试：" + ee.getName());
        this.examDAO.deleteExamination(id);
        this.exams = this.examDAO.findAllExamination();
        this.exams = ExternalUserUtil.filterByAdmin(this.exams);
        Collections.sort(exams);
        Collections.reverse(exams);
        ExamPaperPool.deleteExamination(id);//更新试卷池中的考试
    }

    public String editOrd(String id) {
        for (Examination cq : this.exams) {
            if (id.equals(cq.getId())) {
                //MyLogger.println(cq.getName());
                this.examDAO.updateExamination(cq);
                break;
            }
        }
        return null;
    }

    public String rankExamCase(String id) {
        Examination ee = this.examDAO.findExamination(id);
        ee.setLastRankTime(new Date());
        this.examDAO.updateExamination(ee);
        ThreadPoolTaskExecutor exec = SpringHelper.getSpringBean("taskExecutor");
        exec.execute(new ExamCaseRanker(id));
        JsfHelper.info("手动生成排名信息的线程已经启动，请稍后在成绩管理中查看排名信息。");
        return null;
    }

    /**
     * 按考试将考试成绩全部删除
     *
     * @param id
     * @return
     */
    public String deleteExamCaseByExam(String id) {
        final String eid = id;
        Runnable run = new Runnable() {
            public void run() {
                IExamCaseDAO caseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
                caseDAO.deleteBulkExamCaseByExam(eid);
            }
        };
        ThreadPoolTaskExecutor exec = SpringHelper.getSpringBean("taskExecutor");
        exec.execute(run);
        JsfHelper.info("按考试清除成绩的任务已经启动，操作过程可能较慢，请稍后到成绩管理中查看清除结果。");
        return null;
    }

    public String refreshPaperPool() {
        this.examDAO.reBuildCache();//重构缓存
        ExamPaperPool.refreshAllExamination();
        JsfHelper.info("刷新试卷池任务已经添加，系统将为所有考试重新构建缓存实例，这将在一段时间内造成数据库忙碌、和CPU消耗。。。");
        return null;
    }

    public static void main(String args[]) {
        ExaminationService examDAO = SpringHelper.getSpringBean("ExaminationService");
        List<Examination> es = examDAO.findAllClones();
        for (Examination e : es) {
            System.out.println(e);
        }

    }

}
