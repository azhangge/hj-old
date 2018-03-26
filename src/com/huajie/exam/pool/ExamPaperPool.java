package com.huajie.exam.pool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.service.IExamCaseService;
import com.hjedu.examination.service.impl.ExaminationService;
import com.huajie.exam.thread.PaperPoolDaemon;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

/**
 * 试卷池，或者为“考试实例池”
 *
 * @author Administrator
 */
public class ExamPaperPool {

    private static Map<String, LinkedBlockingQueue<ExamCase>> paperMap = new ConcurrentHashMap();//试卷池
    private static List<Examination> exams = Collections.synchronizedList(new ArrayList());
    public static Boolean running = true;

    public static Map<String, LinkedBlockingQueue<ExamCase>> getPaperMap() {
        return paperMap;
    }

    public static void setPaperMap(Map<String, LinkedBlockingQueue<ExamCase>> paperMap) {
        ExamPaperPool.paperMap = paperMap;
    }

    public static List<Examination> getExams() {
        return exams;
    }

    public static void setExams(List<Examination> exams) {
        ExamPaperPool.exams = exams;
    }

    /**
     * 删除一个试卷池，通常发生在考试删除时
     *
     * @param id
     */
    public static void deleteExamination(String id) {

        paperMap.remove(id);
    }

    public static void addExamination(Examination exam) {
        exams.add(exam);
        paperMap.put(exam.getId(), new LinkedBlockingQueue<ExamCase>());
    }

    /**
     * 全部刷新试卷池并启动试卷池的守护线程
     */
    public static void check() {
    	if (CookieUtils.getBusinessId(JsfHelper.getRequest()) == null) {
			return;
		}
        refreshAllExamination();
        PaperPoolDaemon ecp = new PaperPoolDaemon();
        new Thread(ecp, "PaperPoolDaemon").start();
    }

    /**
     * 刷新某一个考试，这通常发生在考试修改以后
     *
     * @param id
     */
    public static void refreshExamination(String id) {
        LinkedBlockingQueue<ExamCase> ecq = paperMap.get(id);
        if (ecq != null) {
            ecq.clear();
        }
    }

    /**
     * 将使用某试卷的考试全部刷新，这通常发生在试卷修改以后
     *
     * @param pid 试卷ID
     * @param ptype 试卷类型
     */
    public static void refreshPaper(String pid, String ptype) {
        if (pid != null & ptype != null) {
            for (Examination ex : exams) {
                if ("random".equals(ptype) && "random".equals(ex.getPaperType())) {
                    if (ex.getRandomPaper() != null) {
                        if (pid.equals(ex.getRandomPaper().getId())) {
                            refreshExamination(ex.getId());
                        }
                    }
                } else if ("random2".equals(ptype) && "random2".equals(ex.getPaperType())) {
                    if (ex.getRandom2Paper() != null) {
                        if (pid.equals(ex.getRandom2Paper().getId())) {
                            refreshExamination(ex.getId());
                        }
                    }
                } else if ("manual".equals(ptype) && "manual".equals(ex.getPaperType())) {
                    if (ex.getManualPaper() != null) {
                        if (pid.equals(ex.getManualPaper().getId())) {
                            refreshExamination(ex.getId());
                        }
                    }
                }
            }
        }
    }

    /**
     * 将所有的考试的试卷池刷新，这将引起大幅试卷池缓存重建消耗
     */
    public static void refreshAllExamination() {
        paperMap.clear();
        exams.clear();
        ExaminationService examinationDAO = SpringHelper.getSpringBean("ExaminationService");
        exams.addAll(examinationDAO.findAllExamination());
    }

    /**
     * 从试卷池中取出一个考试实例
     *
     * @param id 考试ID
     * @return 考试实例
     */
    public static ExamCase takePaper(String id) {
        LinkedBlockingQueue<ExamCase> ecq = paperMap.get(id);
        if (ecq == null) {
            //System.out.println("One paper is built.");
            return buildExamCase(id);
        } else {
            try {
                if (ecq.isEmpty()) {
                    //System.out.println("One paper is built.");
                    return buildExamCase(id);
                } else {
                    ExamCase ec = ecq.take();
                    if (ec == null) {
                        //System.out.println("One paper is built.");
                        return buildExamCase(id);
                    } else {
                        //System.out.println("One paper is taken.");
                        ec.setGenTime(new Date());
                        return ec;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

    }

    /**
     * 构建一个考试实例，可能为简单随机试卷实例、或者人工试卷实例、随机试卷实例
     *
     * @param id
     * @return
     */
    public static ExamCase buildExamCase(String id) {
       
        ExaminationService examDAO = SpringHelper.getSpringBean("ExaminationService");
        Examination exam = examDAO.findExamination(id);
        String paperType = exam.getPaperType();
        IExamCaseService ecs = null;
        if ("random".equals(paperType)) {
            ecs = SpringHelper.getSpringBean("ExamCaseService");
        } else if ("random2".equals(paperType)) {
            //System.out.println("In pool: paper type is random2...");
            ecs = SpringHelper.getSpringBean("ExamCaseRandom2Service");
        } else if ("manual".equals(paperType)) {
            ecs = SpringHelper.getSpringBean("ManualExamCaseService");
        }
        if (ecs != null) {
            ExamCase examCase = new ExamCase();
            examCase.setExamination(exam);
            examCase.setName(exam.getName());
            ecs.buildExamCase(examCase);
            return examCase;
        } else {
            return null;
        }
    }
}
