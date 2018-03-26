/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huajie.exam.thread;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.contest.ContestCase;
import com.hjedu.examination.service.IExamCaseCacheService;
import com.huajie.util.SpringHelper;

/**
 *
 * @author huajie.com This thread is used to rank the ExamCase by score.
 */
public class ExamCaseRanker implements Runnable {

    private final IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
    IExamCaseCacheService cacheService = SpringHelper.getSpringBean("ExamCaseCacheService");
    private final String examId;

    public ExamCaseRanker(String examId) {
        this.examId= examId;
    }

    @Override
    public void run() {
        List<ExamCase> cases = this.examCaseDAO.findExamCaseByExamination(examId);
        rankExamCase(cases);
        int r = 1;
        for (ExamCase cc : cases) {
            cc.setRanking(r);
            r++;
        }
        //更新缓存
        final List<ExamCase> ts2 = cases;
        new Thread(new Runnable() {
            public void run() {
                for (ExamCase ec : ts2) {
                    cacheService.updateExamCaseIfExists(ec);
                }
            }
        }).start();
        //批量更新数据库
        this.examCaseDAO.updateBulkExamCase(cases);
    }

    @SuppressWarnings("unchecked")
	public static void rankExamCase(List<ExamCase> cases) {
        Collections.sort(cases, new Comparator() {
            @Override
            public int compare(Object a, Object b) {
                ExamCase aa = (ExamCase) a;
                ExamCase bb = (ExamCase) b;
                double gap = aa.getScore() - bb.getScore();
                if (gap >= 0) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        
    }

}
