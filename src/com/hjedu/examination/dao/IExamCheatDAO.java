/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hjedu.examination.dao;

import java.util.List;

import com.hjedu.examination.entity.ExamCheat;

public interface IExamCheatDAO {

    public void updateExamCheat(ExamCheat comModel);

    public void addExamCheat(ExamCheat partnerModel);

    public void deleteAll();

    public void deleteExamCheat(String id);

    public List<ExamCheat> findAllExamCheat();

    public List<ExamCheat> findExamCheatByUsr(final String uid);

    public List<ExamCheat> findExamCheatByExam(final String uid);

    public List<ExamCheat> findExamCheatByCase(final String uid);

    public ExamCheat findExamCheat(String id);
}
