package com.huajie.exam.thread;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingQueue;

import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.service.impl.ExaminationService;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.util.SpringHelper;

/**
 * 线程池守护线程，主要用于检查试卷池中实例是否充足及更新试卷池
 *
 * @author Administrator
 */
public class PaperPoolDaemon implements Runnable {

    //private final int poolSize = SpringHelper.getSpringBean("paper_pool_size");
    ExaminationService examDAO = SpringHelper.getSpringBean("ExaminationService");

    /**
     * 为每一个考试建立一个键值对应的试卷池
     */
    private void checkExamination() {
        Map<String, LinkedBlockingQueue<ExamCase>> map = ExamPaperPool.getPaperMap();
        List<Examination> exams = ExamPaperPool.getExams();
        StringBuilder bu = new StringBuilder();
        for (Examination ex : exams) {
            if (ex.getPaperType() != null) {
                //此两行程序用于构建所有考试的ID串,以供下面使用
                bu.append(ex.getId());
                bu.append(",");
                //用于加入缺失的考试
                if (!map.containsKey(ex.getId())) {
                    synchronized (map) {//修改MAP时应该锁定，以免发生同步冲突
                        map.put(ex.getId(), new LinkedBlockingQueue<ExamCase>());
                    }
                }
            }
        }
        //检查试卷池里边的考试是否还存在，sb为上方构建的串
        Set<String> set = map.keySet();
        String sb = bu.toString();
        for (String l : set) {
            //若ID串不包含某考试的ID，说明此考试已经被删除
            if (!sb.contains(l.toString())) {
                synchronized (map) {
                    map.remove(l);
                }
            }
        }
    }

    /**
     * 检查各个考试的试卷池是否满了，不满就再加考试实例
     */
    private void checkExamCase() {
        Map<String, LinkedBlockingQueue<ExamCase>> map = ExamPaperPool.getPaperMap();
        Set<String> ids;
        ids = new ConcurrentSkipListSet(map.keySet());
        for (String id : ids) {
            LinkedBlockingQueue<ExamCase> lbq = map.get(id);
            if (lbq != null) {
                int size = lbq.size();
                Examination exam = examDAO.findExamination(id);
                if (exam == null) {
                    ExamPaperPool.deleteExamination(id);
                    continue;
                }
                //根据考试设置中设置的试卷池大小加载考试实例
                int poolSize = exam.getPaperPoolSize();
                if (poolSize < 0) {
                    poolSize = 0;
                }
                if (size < poolSize) {
                    int gap = poolSize - size;
                    for (int i = 0; i < gap; i++) {
                        ExamCase ec = ExamPaperPool.buildExamCase(id);
                        lbq.add(ec);
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        while (ExamPaperPool.running) {
            try {
                this.checkExamination();
                this.checkExamCase();
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
