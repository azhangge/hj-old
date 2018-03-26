package com.huajie.exam.pool;

import java.util.concurrent.LinkedBlockingQueue;

import com.hjedu.examination.entity.ExamCase;
import com.huajie.exam.thread.ExamCaseProcessor;
import com.huajie.exam.thread.ExamCaseSaveProcessor;

public class ExamCaseController {

    //双队列一个用于添加一个用于读取
    private static LinkedBlockingQueue<ExamCase> submitQueue = new LinkedBlockingQueue();//试卷提交队列
    private static LinkedBlockingQueue<ExamCase> processQueue = new LinkedBlockingQueue();//试卷提交后的处理队列
    
    private static LinkedBlockingQueue<ExamCase> saveQueue = new LinkedBlockingQueue();//试卷保存队列
    private static LinkedBlockingQueue<ExamCase> saveProcessQueue = new LinkedBlockingQueue();//试卷保存处理队列
    public static  Boolean running=true;

    public static LinkedBlockingQueue<ExamCase> getSubmitQueue() {
        return submitQueue;
    }

    public static void setSubmitQueue(LinkedBlockingQueue<ExamCase> submitQueue) {
        ExamCaseController.submitQueue = submitQueue;
    }

    public static LinkedBlockingQueue<ExamCase> getProcessQueue() {
        return processQueue;
    }

    public static void setProcessQueue(LinkedBlockingQueue<ExamCase> processQueue) {
        ExamCaseController.processQueue = processQueue;
    }

    public static LinkedBlockingQueue<ExamCase> getSaveQueue() {
        return saveQueue;
    }

    public static void setSaveQueue(LinkedBlockingQueue<ExamCase> saveQueue) {
        ExamCaseController.saveQueue = saveQueue;
    }

    public static LinkedBlockingQueue<ExamCase> getSaveProcessQueue() {
        return saveProcessQueue;
    }

    public static void setSaveProcessQueue(LinkedBlockingQueue<ExamCase> saveProcessQueue) {
        ExamCaseController.saveProcessQueue = saveProcessQueue;
    }

    /**
     * 开启交卷队列和保存队列
     */
    public static void check() {
        ExamCaseProcessor ecp = new ExamCaseProcessor();
        ExamCaseSaveProcessor ecsp = new ExamCaseSaveProcessor();
        new Thread(ecp,"ExamCaseProcessor").start();
        new Thread(ecsp,"ExamCaseSaveProcessor").start();
    }
}
