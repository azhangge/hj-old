/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hjedu.examination.service.impl;

import java.util.List;

import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.service.IWrongExamCaseService;
import com.huajie.util.SpringHelper;

/**
 *
 * @author 铁铁
 */
public class WrongExamCaseService extends IWrongExamCaseService {

    @Override
    public void checkAllExamCase() {
        IExamCaseDAO dao = SpringHelper.getSpringBean("ExamCaseDAO");
        List<ExamCase> ecs = dao.findAllExamCase();
        for (ExamCase ec : ecs) {
            checkWrongItems(ec);
        }
    }
    
    @Override
    public void checkExamCaseByExam(String examId) {
        IExamCaseDAO dao = SpringHelper.getSpringBean("ExamCaseDAO");
        List<ExamCase> ecs = dao.findExamCaseByExamination(examId);
        for (ExamCase ec : ecs) {
            checkWrongItems(ec);
        }
    }
    
    @Override
    public void checkExamCaseByUser(String uid) {
        IExamCaseDAO dao = SpringHelper.getSpringBean("ExamCaseDAO");
        List<ExamCase> ecs = dao.findExamCaseByUser(uid);
        for (ExamCase ec : ecs) {
            checkWrongItems(ec);
        }
    }

    
}
