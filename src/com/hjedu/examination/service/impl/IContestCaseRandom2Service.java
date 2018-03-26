package com.hjedu.examination.service.impl;

import java.util.LinkedList;
import java.util.List;

import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IEssayQuestionDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.dao.IFillQuestionDAO;
import com.hjedu.examination.dao.IJudgeQuestionDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.entity.contest.ContestCase;
import com.hjedu.examination.entity.contest.ContestCaseItemAdapter;
import com.hjedu.examination.entity.contest.ContestCaseItemChoice;
import com.hjedu.examination.entity.contest.ContestCaseItemEssay;
import com.hjedu.examination.entity.contest.ContestCaseItemFile;
import com.hjedu.examination.entity.contest.ContestCaseItemFill;
import com.hjedu.examination.entity.contest.ContestCaseItemJudge;
import com.hjedu.examination.entity.contest.ContestCaseItemMultiChoice;
import com.hjedu.examination.entity.contest.ContestCaseStub;
import com.hjedu.examination.entity.random2.ExamPaperRandom2;
import com.hjedu.examination.entity.random2.Random2PaperPart;
import com.huajie.util.SpringHelper;

public abstract class IContestCaseRandom2Service {

    public abstract void buildItemFillBlocks(ContestCaseItemFill fill);
    
    public abstract ContestCaseStub buildExamCaseStub(ExamPaperRandom2 paper);

    public abstract void buildExamCaseFromStub(ContestCase ec, ContestCaseStub stub);

    public abstract void buildExamCase(ContestCase ec);

    public abstract ContestCase resumeExamCase(ContestCase ec);

    public abstract ContestCase computeExamCase(ContestCase ec);

    public abstract double computeTotalScore(ContestCase ec);

    
    
    /**
     * 将从APK传来的GSON恢复为可有效保存的ExamCase
     *
     * @param ec
     * @return
     */
    public ContestCase restoreFromJSON(ContestCase ec) {
        if (ec == null) {
            return null;
        }
        IChoiceQuestionDAO choiceQuestionDAO = SpringHelper.getSpringBean("ChoiceQuestionDAO");
        IMultiChoiceQuestionDAO multiChoiceQuestionDAO = SpringHelper.getSpringBean("MultiChoiceQuestionDAO");
        IFillQuestionDAO fillQuestionDAO = SpringHelper.getSpringBean("FillQuestionDAO");
        IJudgeQuestionDAO judgeQuestionDAO = SpringHelper.getSpringBean("JudgeQuestionDAO");
        IEssayQuestionDAO essayQuestionDAO = SpringHelper.getSpringBean("EssayQuestionDAO");
        IFileQuestionDAO fileQuestionDAO = SpringHelper.getSpringBean("FileQuestionDAO");
        List<ContestCaseItemChoice> cqs = new LinkedList();
        List<ContestCaseItemMultiChoice> mcqs = new LinkedList();
        List<ContestCaseItemFill> fqs = new LinkedList();
        List<ContestCaseItemJudge> jqs = new LinkedList();
        List<ContestCaseItemEssay> eqs = new LinkedList();
        List<ContestCaseItemFile> ffqs = new LinkedList();
        List<Random2PaperPart> vparts = ec.getCparts();
        if (vparts != null) {
            for (Random2PaperPart vpart : vparts) {
                List<ContestCaseItemAdapter> adapters = vpart.getCadapters();
                if (adapters != null) {
                    for (ContestCaseItemAdapter ada : adapters) {
                        if ("choice".equals(ada.getQtype())) {
                            ContestCaseItemChoice ei = ada.getChoiceItem();
                            ei.setExamCase(ec);
                            ei.setQuestion(choiceQuestionDAO.findChoiceQuestion(ada.getQuestion().getId()));
                            cqs.add(ei);
                        }
                        if ("mchoice".equals(ada.getQtype())) {
                            ContestCaseItemMultiChoice ei = ada.getMultiChoiceItem();
                            ei.setExamCase(ec);
                            ei.setQuestion(multiChoiceQuestionDAO.findMultiChoiceQuestion(ada.getQuestion().getId()));
                            mcqs.add(ada.getMultiChoiceItem());
                        }
                        if ("fill".equals(ada.getQtype())) {
                            ContestCaseItemFill ei = ada.getFillItem();
                            ei.setExamCase(ec);
                            ei.setQuestion(fillQuestionDAO.findFillQuestion(ada.getQuestion().getId()));
                            fqs.add(ada.getFillItem());
                        }
                        if ("judge".equals(ada.getQtype())) {
                            ContestCaseItemJudge ei = ada.getJudgeItem();
                            ei.setExamCase(ec);
                            ei.setQuestion(judgeQuestionDAO.findJudgeQuestion(ada.getQuestion().getId()));
                            jqs.add(ada.getJudgeItem());
                        }
                        if ("essay".equals(ada.getQtype())) {
                            ContestCaseItemEssay ei = ada.getEssayItem();
                            ei.setExamCase(ec);
                            ei.setQuestion(essayQuestionDAO.findEssayQuestion(ada.getQuestion().getId()));
                            eqs.add(ada.getEssayItem());
                        }
                        if ("file".equals(ada.getQtype())) {
                            ContestCaseItemFile ei = ada.getFileItem();
                            ei.setExamCase(ec);
                            ei.setQuestion(fileQuestionDAO.findFileQuestion(ada.getQuestion().getId()));
                            ffqs.add(ada.getFileItem());
                        }
                    }
                }
            }
        }
        ec.setChoiceItems(cqs);
        ec.setMultiChoiceItems(mcqs);
        ec.setFillItems(fqs);
        ec.setJudgeItems(jqs);
        ec.setEssayItems(eqs);
        ec.setFileItems(ffqs);
        return ec;
    }
    
    
}
