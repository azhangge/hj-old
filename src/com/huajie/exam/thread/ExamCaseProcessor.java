/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huajie.exam.thread;

import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.service.IExamCaseCacheService;
import com.hjedu.examination.service.IExamCaseService;
import com.huajie.exam.pool.ExamCaseController;
import com.huajie.util.SpringHelper;

public class ExamCaseProcessor implements Runnable {

    private IExamCaseService examCaseService = SpringHelper.getSpringBean("ExamCaseService");
    IExamCaseService mexamCaseService = SpringHelper.getSpringBean("ManualExamCaseService");
    IExamCaseService rexamCaseService = SpringHelper.getSpringBean("ExamCaseRandom2Service");
    private IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
    IExamCaseCacheService cacheService = SpringHelper.getSpringBean("ExamCaseCacheService");

    @Override
    public void run() {
        while (ExamCaseController.running) {
            try {
                if (ExamCaseController.getSubmitQueue().isEmpty()) {
                    Thread.sleep(100);
                    continue;
                }
                if (ExamCaseController.getProcessQueue().isEmpty()) {
                    //在转移队列内容的过程中有可能有其它数据加入，这样就会导致数据丢失，因此此操作应该加锁
                    synchronized (ExamCaseController.getSubmitQueue()) {
                        ExamCaseController.getProcessQueue().addAll(ExamCaseController.getSubmitQueue());
                        ExamCaseController.getSubmitQueue().clear();
                    }

                }
                while (!ExamCaseController.getProcessQueue().isEmpty()) {
                    try {
                        ExamCase examCase = ExamCaseController.getProcessQueue().take();
                        if (examCase != null) {
                            if (examCase.getExamination().isIfManualPaper()) {
                                this.mexamCaseService.computeExamCase(examCase);
                            } else if (examCase.getExamination().isIfRandomPaper()) {
                                this.examCaseService.computeExamCase(examCase);
                            } else if (examCase.getExamination().isIfRandom2Paper()) {
                                this.rexamCaseService.computeExamCase(examCase);
                            }
                            examCase.setStat("submitted");
                            cacheService.addExamCase(examCase);//加入缓存
                            //处理好后加入存储队列
                            ExamCaseSaver.saveProcessedExamCase(examCase);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
