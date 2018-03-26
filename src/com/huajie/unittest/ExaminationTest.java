/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huajie.unittest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.service.IExamCaseService;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.util.SpringHelper;

/**
 *
 * @author huajie
 */
public class ExaminationTest {

    static IExaminationDAO dao = SpringHelper.getSpringBean("ExaminationDAO");

    public static void testPaperPool(String eid) {
        ExamPaperPool.check();//试卷池
        try {
            //Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int total = 1;
        long begin = System.currentTimeMillis();
        System.out.println("Test begin.");
        for (int i = 0; i < total; i++) {
            ExamCase ec = ExamPaperPool.takePaper(eid);
            ObjectMapper mapper = new ObjectMapper();
            
            try {
                String json = mapper.writeValueAsString(ec);
                System.out.println(json);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        long end = System.currentTimeMillis();
        long inteval = end - begin;
        System.out.println("Inteval:" + inteval);
    }

    public static void testPaperGenerator(String eid) {
        IExamCaseService examCaseService = null;
        Examination exam = dao.findExamination(eid);
        String paperType = exam.getPaperType();
        if ("random".equals(paperType)) {
            examCaseService = SpringHelper.getSpringBean("ExamCaseService");;
        } else if ("random2".equals(paperType)) {
            examCaseService = SpringHelper.getSpringBean("ExamCaseRandom2Service");
        } else if ("manual".equals(paperType)) {
            examCaseService = SpringHelper.getSpringBean("ManualExamCaseService");
        }
        int total = 100;
        long begin = System.currentTimeMillis();
        System.out.println("Test begin.");
        if (examCaseService != null) {
            for (int i = 0; i < total; i++) {
                ExamCase ec = new ExamCase();
                ec.setExamination(exam);
                examCaseService.buildExamCase(ec);
            }
        }
        long end = System.currentTimeMillis();
        long inteval = end - begin;
        System.out.println("Inteval:" + inteval);
    }

    public static void main(String args[]) {
        String eid = "00815f0d-e517-4687-b79a-9d2c7df7ef8c";
        ExaminationTest.testPaperPool(eid);
        //ExaminationTest.testPaperGenerator(eid);
    }

}
