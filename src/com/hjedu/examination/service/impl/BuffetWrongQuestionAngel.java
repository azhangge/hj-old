package com.hjedu.examination.service.impl;

import java.io.Serializable;
import java.util.List;
import org.aspectj.lang.JoinPoint;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IWrongKnowledgeDAO;
import com.hjedu.examination.dao.IWrongQuestionDAO;
import com.hjedu.examination.entity.ExamKnowledge;
import com.hjedu.examination.entity.buffet.BuffetExamCase;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemCase;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemChoice;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemEssay;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemFile;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemFill;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemJudge;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemMultiChoice;

public class BuffetWrongQuestionAngel implements Serializable {

    private IWrongQuestionDAO wrongDAO;
    private IWrongKnowledgeDAO wrong2DAO;

    public IWrongQuestionDAO getWrongDAO() {
        return wrongDAO;
    }

    public void setWrongDAO(IWrongQuestionDAO wrongDAO) {
        this.wrongDAO = wrongDAO;
    }

    public IWrongKnowledgeDAO getWrong2DAO() {
        return wrong2DAO;
    }

    public void setWrong2DAO(IWrongKnowledgeDAO wrong2DAO) {
        this.wrong2DAO = wrong2DAO;
    }

    /*在考试结束提交后计算试卷中的错题
     */
    public void checkAndSaveWrongQuestion(JoinPoint jp, BuffetExamCase ec) {

        //ExamCase ec = (ExamCase) o;
        BbsUser bu = ec.getUser();
        List<BuffetExamCaseItemChoice> cqs = ec.getChoiceItems();
        List<BuffetExamCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
        List<BuffetExamCaseItemFill> fqs = ec.getFillItems();
        List<BuffetExamCaseItemJudge> jqs = ec.getJudgeItems();
        List<BuffetExamCaseItemEssay> eqs = ec.getEssayItems();
        List<BuffetExamCaseItemFile> ffqs = ec.getFileItems();
        List<BuffetExamCaseItemCase> caqs = ec.getCaseItems();
        //计算每道单选题是否正确
        for (BuffetExamCaseItemChoice c : cqs) {
            if (!c.getIfRight()) {
                this.wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), bu.getId(), "choice");
                //加入我的难点
                List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                for (ExamKnowledge k : knows) {
                    wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                }
            }
        }
        //计算每道多选题是否正确
        for (BuffetExamCaseItemMultiChoice c : mcqs) {
            if (!c.getIfRight()) {
                this.wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), bu.getId(), "mchoice");
                //加入我的难点
                List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                for (ExamKnowledge k : knows) {
                    wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                }
            }
        }
        //计算每道填空题是否正确
        for (BuffetExamCaseItemFill c : fqs) {
            if (c.getRightNum() < c.getTotalNum()) {
                this.wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), bu.getId(), "fill");
                //加入我的难点
                List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                for (ExamKnowledge k : knows) {
                    wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                }
            }
        }
        //计算每道判断题是否正确
        for (BuffetExamCaseItemJudge c : jqs) {
            if (!c.getIfRight()) {
                this.wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), bu.getId(), "judge");
                //加入我的难点
                List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                for (ExamKnowledge k : knows) {
                    wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                }
            }
        }
        //计算每道问答题得分
        for (BuffetExamCaseItemEssay c : eqs) {
            if (c.getRightRatio() < 0.8) {
                this.wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), bu.getId(), "essay");
                //加入我的难点
                List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                for (ExamKnowledge k : knows) {
                    wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                }
            }
        }

        //计算每道文件题得分
        for (BuffetExamCaseItemFile c : ffqs) {
            if (c.getRightRatio() < 0.8) {
                this.wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), bu.getId(), "file");
                //加入我的难点
                List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                for (ExamKnowledge k : knows) {
                    wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                }
            }
        }
        //计算每道综合题得分
        for (BuffetExamCaseItemCase c : caqs) {
            List<BuffetExamCaseItemChoice> cqss = c.getChoiceItems();
            //计算综合题中每道单选题是否正确
            for (BuffetExamCaseItemChoice cc : cqss) {
                if (!cc.getIfRight()) {
                    this.wrongDAO.wrongTimesPlusOne(cc.getQuestion().getId(), bu.getId(), "choice");
                    //加入我的难点
                    List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                    for (ExamKnowledge k : knows) {
                        wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                    }
                }
            }
            List<BuffetExamCaseItemMultiChoice> mcqss = c.getMultiChoiceItems();
            //计算综合题中每道单选题是否正确
            for (BuffetExamCaseItemMultiChoice cc : mcqss) {
                if (!cc.getIfRight()) {
                    this.wrongDAO.wrongTimesPlusOne(cc.getQuestion().getId(), bu.getId(), "mchoice");
                    //加入我的难点
                    List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                    for (ExamKnowledge k : knows) {
                        wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                    }
                }
            }
            List<BuffetExamCaseItemFill> fqss = c.getFillItems();
            //计算综合题中每道单选题是否正确
            for (BuffetExamCaseItemFill cc : fqss) {
                if (cc.getRightNum() < cc.getTotalNum()) {
                    this.wrongDAO.wrongTimesPlusOne(cc.getQuestion().getId(), bu.getId(), "fill");
                    //加入我的难点
                    List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                    for (ExamKnowledge k : knows) {
                        wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                    }
                }
            }
            List<BuffetExamCaseItemJudge> jqss = c.getJudgeItems();
            //计算综合题中每道单选题是否正确
            for (BuffetExamCaseItemJudge cc : jqss) {
                if (!cc.getIfRight()) {
                    this.wrongDAO.wrongTimesPlusOne(cc.getQuestion().getId(), bu.getId(), "judge");
                    //加入我的难点
                    List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                    for (ExamKnowledge k : knows) {
                        wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                    }
                }
            }
            List<BuffetExamCaseItemEssay> eqss = c.getEssayItems();
            //计算综合每道问答题得分
            for (BuffetExamCaseItemEssay cc : eqss) {
                if (cc.getRightRatio() < 0.8) {
                    this.wrongDAO.wrongTimesPlusOne(cc.getQuestion().getId(), bu.getId(), "essay");
                    //加入我的难点
                    List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                    for (ExamKnowledge k : knows) {
                        wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                    }
                }
            }
        }

    }
}
