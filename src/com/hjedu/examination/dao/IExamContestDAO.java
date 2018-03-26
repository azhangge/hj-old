package com.hjedu.examination.dao;

import java.util.List;

import com.hjedu.examination.entity.contest.ExamContestSession;

public interface IExamContestDAO {

    public abstract void addExamContest(ExamContestSession m);

    public abstract void updateExamContest(ExamContestSession m);

    public abstract void deleteExamContest(String id);

    public abstract ExamContestSession findExamContest(String id);

    public abstract List<ExamContestSession> findAllExamContest();
    
    public List<ExamContestSession> findAllShowedExamContest();
    
    public List<ExamContestSession> findExamContestByLabel(String labelId);

}
