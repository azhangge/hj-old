package com.huajie.exam.thread;

import java.util.concurrent.LinkedBlockingQueue;

import com.hjedu.examination.dao.IModuleExamCaseDAO;
import com.hjedu.examination.entity.module.ModuleExamCase;
import com.huajie.exam.pool.ModuleExamCaseController;
import com.huajie.util.SpringHelper;

public class ModuleExamCaseSaveProcessor implements Runnable {

    //private IExamCaseService examCaseService = SpringHelper.getSpringBean("ExamCaseService");
    private IModuleExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ModuleExamCaseDAO");

    @Override
    public void run() {
        while (ModuleExamCaseController.running) {
            try {
                if (ModuleExamCaseController.getSaveQueue().isEmpty()) {
                    Thread.sleep(100);
                    continue;
                }
                if (ModuleExamCaseController.getSaveProcessQueue().isEmpty()) {
                    ModuleExamCaseController.getSaveProcessQueue().addAll(ModuleExamCaseController.getSaveQueue());
                    ModuleExamCaseController.getSaveQueue().clear();
                }
                if (!ModuleExamCaseController.getSaveProcessQueue().isEmpty()) {
                    LinkedBlockingQueue<ModuleExamCase> ls = ModuleExamCaseController.getSaveProcessQueue();
                    for (ModuleExamCase examCase : ls) {
                        if (!"submitted".equals(examCase.getStat())) {
                            examCase.setStat("saved");
//                            if (this.examCaseDAO.findModuleExamCase(examCase.getId()) == null) {
//                                this.examCaseDAO.addModuleExamCase(examCase);
//                            } else {
//                                this.examCaseDAO.updateModuleExamCase(examCase);
//                            }
                        }
                        this.examCaseDAO.updateModuleExamCase(examCase);
                    }
                    ModuleExamCaseController.getSaveProcessQueue().clear();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
