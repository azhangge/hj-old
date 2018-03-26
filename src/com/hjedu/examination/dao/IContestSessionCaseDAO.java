package com.hjedu.examination.dao;


import java.util.List;

import com.hjedu.examination.entity.contest.ContestSessionCase;

public interface IContestSessionCaseDAO {

    public abstract void addContestSessionCase(ContestSessionCase m);

    public abstract void updateContestSessionCase(ContestSessionCase m);

    public abstract void deleteContestSessionCase(String id);

    public abstract ContestSessionCase findContestSessionCase(String id);

    public abstract List<ContestSessionCase> findAllContestSessionCase();
   
    public ContestSessionCase findContestSessionCaseByContestAndSessionStr(String id, String str);
    
    public abstract List<ContestSessionCase> findContestSessionCaseByContest(String examId);
    
    public List<ContestSessionCase> findUnSubmittedContestSessionCaseByContest(String id);
    
    public List<ContestSessionCase> findSubmittedContestSessionCaseByContest(String id);
    
    public long getContestSessionCaseNum() ;
    
    public long getParticipateNumByContest(String examId);
    
    
    public void deleteAllContestSessionCase();
}
