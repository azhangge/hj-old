/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huajie.exam.thread;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.hjedu.examination.dao.IContestCaseDAO;
import com.hjedu.examination.entity.contest.ContestCase;
import com.huajie.util.SpringHelper;

/**
 *
 * @author huajie.com This thread is used to rank the ContestCase by score.
 */
public class ContestCaseRanker implements Runnable {

    private final IContestCaseDAO examCaseDAO = SpringHelper.getSpringBean("ContestCaseDAO");
    private final String contestId;
    private final String sessionStr;

    public ContestCaseRanker(String contestId,String sessionStr) {
        this.contestId = contestId;
        this.sessionStr=sessionStr;
    }

    @Override
    public void run() {
        List<ContestCase> cases = this.examCaseDAO.findContestCaseByContestAndSession(contestId,sessionStr);
        rankContestCase(cases);
        int r = 1;
        for (ContestCase cc : cases) {
            cc.setRanking(r);
            this.examCaseDAO.updateContestCase(cc);
            r++;
        }
    }

    public static void rankContestCase(List<ContestCase> cases) {
        Collections.sort(cases, new Comparator() {
            @Override
            public int compare(Object a, Object b) {
                ContestCase aa = (ContestCase) a;
                ContestCase bb = (ContestCase) b;
                double gap = aa.getScore() - bb.getScore();
                if (gap >= 0) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        
    }

}
