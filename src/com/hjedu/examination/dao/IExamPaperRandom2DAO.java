package com.hjedu.examination.dao;


import java.util.List;

import com.hjedu.examination.entity.random2.ExamPaperRandom2;

public interface IExamPaperRandom2DAO {

    public abstract void addExamPaperRandom2(ExamPaperRandom2 m);

    public abstract void updateExamPaperRandom2(ExamPaperRandom2 m);

    public abstract void deleteExamPaperRandom2(String id);

    public abstract ExamPaperRandom2 findExamPaperRandom2(String id);

    public abstract List<ExamPaperRandom2> findAllExamPaperRandom2(String businessId);

}
