package com.huajie.exam.thread;

import java.util.List;

import com.hjedu.course.dao.IStudyPlanChangeLogDAO;
import com.hjedu.course.dao.IStudyPlanDAO;
import com.hjedu.course.dao.IStudyPlanLogDAO;
import com.hjedu.course.entity.StudyPlan;
import com.hjedu.course.entity.StudyPlanChangeLog;
import com.hjedu.course.entity.StudyPlanLog;
import com.hjedu.course.service.IStudyPlanLogService;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.service.IExamCaseCacheService;
import com.hjedu.examination.service.IExamCaseService;
import com.huajie.exam.pool.ExamCaseController;
import com.huajie.util.SpringHelper;

public class ExamCaseSaver implements Runnable {

    ExamCase examCase;
    boolean ifPreProcess = true;
    private IExamCaseService examCaseService = SpringHelper.getSpringBean("ExamCaseService");
    IExamCaseCacheService cacheService = SpringHelper.getSpringBean("ExamCaseCacheService");

    public ExamCaseSaver(ExamCase examCase) {
        this.examCase = examCase;
    }

    public ExamCaseSaver(ExamCase examCase, boolean pre) {
        this.examCase = examCase;
        this.ifPreProcess = pre;
    }

    @Override
    public void run() {
        if (ifPreProcess) {
            examCaseService.preSaveExamCase(examCase);
        }
        if (!"submitted".equals(examCase.getStat())) {
                examCase.setStat("saved");
            }
        cacheService.addExamCase(examCase);//加入缓存
        ExamCaseController.getSaveQueue().add(examCase);
    }

    /**
     * 用于最终保存已经提交并处理好的考试成绩
     *
     * @param examCase
     */
    public static void saveProcessedExamCase(ExamCase examCase) {
    	if(examCase.getScore()>=examCase.getExamination().getQualified()){
    		examCase.setIfPassed(true);
    	}
        ExamCaseController.getSaveQueue().add(examCase);
        IStudyPlanLogService dtudyPlanLogService = SpringHelper.getSpringBean("StudyPlanLogService");
    	dtudyPlanLogService.createFinishExamLog(examCase);
    }

}
