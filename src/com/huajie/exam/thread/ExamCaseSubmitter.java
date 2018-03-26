package com.huajie.exam.thread;

import com.hjedu.examination.entity.ExamCase;
import com.huajie.exam.pool.ExamCaseController;

public class ExamCaseSubmitter implements Runnable {

    ExamCase examCase;

    public ExamCaseSubmitter(ExamCase examCase) {
        this.examCase = examCase;
    }

    @Override
    public void run() {
        ExamCaseController.getSubmitQueue().add(examCase);

    }
}
