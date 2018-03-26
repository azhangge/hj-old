package com.huajie.exam.thread;

import com.hjedu.examination.entity.module.ModuleExamCase;
import com.hjedu.examination.service.IModuleExamCaseService;
import com.huajie.exam.pool.ModuleExamCaseController;
import com.huajie.util.SpringHelper;

public class ModuleExamCaseSaver implements Runnable {

    ModuleExamCase examCase;
    private IModuleExamCaseService examCaseService = SpringHelper.getSpringBean("Module2ExamCaseService");

    public ModuleExamCaseSaver(ModuleExamCase examCase) {
        this.examCase = examCase;
    }

    @Override
    public void run() {
        //examCaseService.preSaveExamCase(examCase);
        examCase=examCaseService.computeExamCase(examCase);
        ModuleExamCaseController.getSaveQueue().add(examCase);

    }
}
