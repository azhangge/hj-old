package com.huajie.exam.thread;

import java.util.concurrent.LinkedBlockingQueue;

import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.service.IExamCaseService;
import com.huajie.exam.pool.ExamCaseController;
import com.huajie.util.SpringHelper;

public class ExamCaseSaveProcessor implements Runnable {

    private IExamCaseService examCaseService = SpringHelper.getSpringBean("ExamCaseService");
    private IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");

    @Override
    public void run() {
        while (ExamCaseController.running) {
            try {
                if (ExamCaseController.getSaveQueue().isEmpty()) {
                    Thread.sleep(100);
                    continue;
                }
                if (ExamCaseController.getSaveProcessQueue().isEmpty()) {
                    //在转移队列内容的过程中有可能有其它数据加入，这样就会导致数据丢失，因此此操作应该加锁
                    synchronized (ExamCaseController.getSaveQueue()) {
                        ExamCaseController.getSaveProcessQueue().addAll(ExamCaseController.getSaveQueue());
                        ExamCaseController.getSaveQueue().clear();
                    }

                }
                if (!ExamCaseController.getSaveProcessQueue().isEmpty()) {
                    try {
                        LinkedBlockingQueue<ExamCase> ls = ExamCaseController.getSaveProcessQueue();
                        //计算过程中可能会有其它成绩加入
                        for (ExamCase examCase : ls) {
                            if (!"submitted".equals(examCase.getStat())) {
                                examCase.setStat("saved");
                            }
                            //将考试实例适配为JSON
                            //examCaseService.adaptToJSON(examCase);
                        }
                        //以批量方式更新
                        this.examCaseDAO.updateBulkExamCase(ls);
                        ExamCaseController.getSaveProcessQueue().clear();
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
