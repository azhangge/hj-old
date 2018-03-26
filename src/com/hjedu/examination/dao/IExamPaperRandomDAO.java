package com.hjedu.examination.dao;

import java.util.List;

import com.hjedu.examination.entity.ExamPaperRandom;

public interface IExamPaperRandomDAO {

    public abstract void addExamPaperRandom(ExamPaperRandom m);

    public abstract void updateExamPaperRandom(ExamPaperRandom m);

    public abstract void deleteExamPaperRandom(String id);

    public abstract ExamPaperRandom findExamPaperRandom(String id);

    public abstract List<ExamPaperRandom> findAllExamPaperRandom(String businessId);

	List<ExamPaperRandom> findAllExamPaperRandomByAdmin(String businessId);

}
