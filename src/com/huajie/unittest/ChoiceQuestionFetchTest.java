
package com.huajie.unittest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.huajie.util.RereRandom;
import com.huajie.util.SpringHelper;

/**
 *
 * @author wbdwl.com
 */
public class ChoiceQuestionFetchTest {
    
    IChoiceQuestionDAO dao = SpringHelper.getSpringBean("ChoiceQuestionDAO");
    
    /**
     * 使用随机数逐条获取试题
     * @param set2
     * @param mid 
     */
    public void fetchOneByOne(Set<Long> set2, String mid) {
        List<ChoiceQuestion> cs2 = new ArrayList();
        for (Long lo : set2) {
            ChoiceQuestion cq = dao.findQuestionByIndex(lo.intValue(), mid);
            cs2.add(cq);
        }
        Collections.sort(cs2);
        for (ChoiceQuestion cq : cs2) {
            System.out.print(cq.getId() + ",");
        }
        System.out.println();
        
    }
    
    /**
     * 使用随机数批量获取试题
     * @param set
     * @param mid 
     */
    public void fetchByBatch(Set<Long> set, String mid) {
        List<ChoiceQuestion> cs = dao.findQuestionsByIndexSet(set, mid);
        Collections.sort(cs);
        for (ChoiceQuestion cq : cs) {
            System.out.print(cq.getId() + ",");
        }
        System.out.println();
        
    }
    
    /**
     * 本测试用于检验使用随机数批量获取试题与逐条获取试题的速度
     * 实验表明：每次随机取50题，取100次，批量方式13秒，逐题方式48秒，批量方式有巨大速度优势
     * @param argsp 
     */
    public static void main(String argsp[]) {
        ChoiceQuestionFetchTest test = new ChoiceQuestionFetchTest();
        String mid = "a1805fca-39e1-4015-9d37-1d2db4766bc5";
        
        
        //逐条获取
        long b2 = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            Set<Long> set2 = RereRandom.randomSet(0, 167, 100);
            test.fetchOneByOne(set2, mid);
        }
        
        long e2 = System.currentTimeMillis();
        System.out.println(e2 - b2);
        
        System.out.println("---------------------------------");
        //批量获取
        long b1 = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            Set<Long> set = RereRandom.randomSet(0, 167, 100);
            test.fetchByBatch(set, mid);
        }
        long e1 = System.currentTimeMillis();
        System.out.println(e1 - b1);
        
    }
    
}
