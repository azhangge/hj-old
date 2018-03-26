
package com.huajie.unittest;

import com.hjedu.examination.entity.ExamCase;
import com.huajie.cache.IRereCacheInstance;
import com.huajie.cache.RereCacheManager;
import com.huajie.exam.pool.ExamPaperPool;

/**
 *
 * @author huajie
 */
public class CacheTest {
    
    public static void testPaperPoolAndCache(String eid) {
        ExamPaperPool.check();//试卷池
        try {
            //Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        IRereCacheInstance ci=RereCacheManager.getLocalInstance("tempCases");
        int total = 2000;
        long begin = System.currentTimeMillis();
        System.out.println("Test begin.");
        for (int i = 0; i < total; i++) {
            ExamCase ec = ExamPaperPool.takePaper(eid);
            ci.add(ec.getId(), ec);
        }
        long end = System.currentTimeMillis();
        long inteval = end - begin;
        System.out.println("Inteval:" + inteval);
    }
    
    public static void main(String args[]) {
        String eid = "00815f0d-e517-4687-b79a-9d2c7df7ef8c";
        testPaperPoolAndCache(eid);
        //ExaminationTest.testPaperGenerator(eid);
    }
    
}
