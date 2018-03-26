package com.hjedu.examination.service.impl;

import java.util.LinkedList;
import java.util.List;

import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IEssayQuestionDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.dao.IFillQuestionDAO;
import com.hjedu.examination.dao.IJudgeQuestionDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.entity.ExamCaseItemAdapter;
import com.hjedu.examination.entity.ExamCaseItemChoice;
import com.hjedu.examination.entity.ExamCaseItemEssay;
import com.hjedu.examination.entity.ExamCaseItemFile;
import com.hjedu.examination.entity.ExamCaseItemFill;
import com.hjedu.examination.entity.ExamCaseItemJudge;
import com.hjedu.examination.entity.ExamCaseItemMultiChoice;
import com.hjedu.examination.entity.GeneralExamCaseItem;
import com.hjedu.examination.entity.buffet.BuffetExamCase;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemAdapter;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemChoice;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemEssay;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemFile;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemFill;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemJudge;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemMultiChoice;
import com.hjedu.examination.entity.buffet.BuffetExaminationPart;
import com.huajie.util.SpringHelper;

public abstract class IBuffetExamCaseService {

    public abstract void buildItemFillBlocks(BuffetExamCaseItemFill fill);

    public abstract void buildExamCase(BuffetExamCase ec);

    public abstract BuffetExamCase resumeExamCase(BuffetExamCase ec);

    public abstract BuffetExamCase computeExamCase(BuffetExamCase ec);

    public abstract double computeTotalScore(BuffetExamCase ec);
    
    
    
    /**
     * 将从APK传来的GSON恢复为可有效保存的ExamCase
     *
     * @param ec
     * @return
     */
    public BuffetExamCase restoreFromJSON(BuffetExamCase ec) {
        if (ec == null) {
            return null;
        }
        IChoiceQuestionDAO choiceQuestionDAO = SpringHelper.getSpringBean("ChoiceQuestionDAO");
        IMultiChoiceQuestionDAO multiChoiceQuestionDAO = SpringHelper.getSpringBean("MultiChoiceQuestionDAO");
        IFillQuestionDAO fillQuestionDAO = SpringHelper.getSpringBean("FillQuestionDAO");
        IJudgeQuestionDAO judgeQuestionDAO = SpringHelper.getSpringBean("JudgeQuestionDAO");
        IEssayQuestionDAO essayQuestionDAO = SpringHelper.getSpringBean("EssayQuestionDAO");
        IFileQuestionDAO fileQuestionDAO = SpringHelper.getSpringBean("FileQuestionDAO");
        List<BuffetExamCaseItemChoice> cqs = new LinkedList();
        List<BuffetExamCaseItemMultiChoice> mcqs = new LinkedList();
        List<BuffetExamCaseItemFill> fqs = new LinkedList();
        List<BuffetExamCaseItemJudge> jqs = new LinkedList();
        List<BuffetExamCaseItemEssay> eqs = new LinkedList();
        List<BuffetExamCaseItemFile> ffqs = new LinkedList();
        List<BuffetExaminationPart> vparts = ec.getParts();
        if (vparts != null) {
            for (BuffetExaminationPart vpart : vparts) {
                List<BuffetExamCaseItemAdapter> adapters = vpart.getAdapters();
                if (adapters != null) {
                    for (BuffetExamCaseItemAdapter ada : adapters) {
                        if ("choice".equals(ada.getQtype())) {
                            BuffetExamCaseItemChoice ei = ada.getChoiceItem();
                            ei.setExamCase(ec);
                            ei.setQuestion(choiceQuestionDAO.findChoiceQuestion(ada.getQuestion().getId()));
                            cqs.add(ei);
                        }
                        if ("mchoice".equals(ada.getQtype())) {
                            BuffetExamCaseItemMultiChoice ei = ada.getMultiChoiceItem();
                            ei.setExamCase(ec);
                            ei.setQuestion(multiChoiceQuestionDAO.findMultiChoiceQuestion(ada.getQuestion().getId()));
                            mcqs.add(ada.getMultiChoiceItem());
                        }
                        if ("fill".equals(ada.getQtype())) {
                            BuffetExamCaseItemFill ei = ada.getFillItem();
                            ei.setExamCase(ec);
                            ei.setQuestion(fillQuestionDAO.findFillQuestion(ada.getQuestion().getId()));
                            fqs.add(ada.getFillItem());
                        }
                        if ("judge".equals(ada.getQtype())) {
                            BuffetExamCaseItemJudge ei = ada.getJudgeItem();
                            ei.setExamCase(ec);
                            ei.setQuestion(judgeQuestionDAO.findJudgeQuestion(ada.getQuestion().getId()));
                            jqs.add(ada.getJudgeItem());
                        }
                        if ("essay".equals(ada.getQtype())) {
                            BuffetExamCaseItemEssay ei = ada.getEssayItem();
                            ei.setExamCase(ec);
                            ei.setQuestion(essayQuestionDAO.findEssayQuestion(ada.getQuestion().getId()));
                            eqs.add(ada.getEssayItem());
                        }
                        if ("file".equals(ada.getQtype())) {
                            BuffetExamCaseItemFile ei = ada.getFileItem();
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

    public ExamCaseItemChoice loadFromAdapterToChoice(ExamCaseItemAdapter ada) {
        ExamCaseItemChoice e = new ExamCaseItemChoice();
        GeneralExamCaseItem gi = ada.getItem();
        if (gi != null) {
            IChoiceQuestionDAO dao = SpringHelper.getSpringBean("ChoiceQuestionDAO");
            e.setId(gi.getId());
            e.setQuestion(dao.findChoiceQuestion(ada.getQuestion().getId()));
            //e.setDone(gi.getDone());
            //e.setIfRight(gi.getIfRight());

        }
        return e;
    }
    

}
