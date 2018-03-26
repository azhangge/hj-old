package com.huajie.exam.pool;

import java.util.concurrent.LinkedBlockingQueue;

import com.hjedu.examination.entity.module.ModuleExamCase;
import com.huajie.exam.thread.ModuleExamCaseSaveProcessor;

public class ModuleExamCaseController {

    private static LinkedBlockingQueue<ModuleExamCase> submitQueue = new LinkedBlockingQueue();//试卷提交队列
    private static LinkedBlockingQueue<ModuleExamCase> processQueue = new LinkedBlockingQueue();//试卷提交后的处理队列
    
    private static LinkedBlockingQueue<ModuleExamCase> saveQueue = new LinkedBlockingQueue();//试卷保存队列
    private static LinkedBlockingQueue<ModuleExamCase> saveProcessQueue = new LinkedBlockingQueue();//试卷保存处理队列
    public static  Boolean running=true;

    public static LinkedBlockingQueue<ModuleExamCase> getSubmitQueue() {
        return submitQueue;
    }

    public static void setSubmitQueue(LinkedBlockingQueue<ModuleExamCase> submitQueue) {
        ModuleExamCaseController.submitQueue = submitQueue;
    }

    public static LinkedBlockingQueue<ModuleExamCase> getProcessQueue() {
        return processQueue;
    }

    public static void setProcessQueue(LinkedBlockingQueue<ModuleExamCase> processQueue) {
        ModuleExamCaseController.processQueue = processQueue;
    }

    public static LinkedBlockingQueue<ModuleExamCase> getSaveQueue() {
        return saveQueue;
    }

    public static void setSaveQueue(LinkedBlockingQueue<ModuleExamCase> saveQueue) {
        ModuleExamCaseController.saveQueue = saveQueue;
    }

    public static LinkedBlockingQueue<ModuleExamCase> getSaveProcessQueue() {
        return saveProcessQueue;
    }

    public static void setSaveProcessQueue(LinkedBlockingQueue<ModuleExamCase> saveProcessQueue) {
        ModuleExamCaseController.saveProcessQueue = saveProcessQueue;
    }

    public static void check() {
        //ModuleExamCaseProcessor ecp = new ModuleExamCaseProcessor();
        ModuleExamCaseSaveProcessor ecsp = new ModuleExamCaseSaveProcessor();
        //new Thread(ecp,"ExamCaseProcessor").start();
        new Thread(ecsp,"ExamCaseSaveProcessor").start();
    }
}
