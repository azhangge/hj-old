package com.hjedu.examination.service.impl;

import java.io.Serializable;
import java.util.List;
import org.aspectj.lang.JoinPoint;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IWrongKnowledgeDAO;
import com.hjedu.examination.dao.IWrongQuestionDAO;
import com.hjedu.examination.entity.ExamKnowledge;
import com.hjedu.examination.entity.module.ModuleExamCase;
import com.hjedu.examination.entity.module.ModuleExamCaseItemCase;
import com.hjedu.examination.entity.module.ModuleExamCaseItemChoice;
import com.hjedu.examination.entity.module.ModuleExamCaseItemEssay;
import com.hjedu.examination.entity.module.ModuleExamCaseItemFile;
import com.hjedu.examination.entity.module.ModuleExamCaseItemFill;
import com.hjedu.examination.entity.module.ModuleExamCaseItemJudge;
import com.hjedu.examination.entity.module.ModuleExamCaseItemMultiChoice;

public class ModuleWrongQuestionAngel implements Serializable {

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
    public void checkAndSaveWrongQuestion(JoinPoint jp, ModuleExamCase ec) {

        //ExamCase ec = (ExamCase) o;
        BbsUser bu = ec.getUser();
        List<ModuleExamCaseItemChoice> cqs = ec.getChoiceItems();
        List<ModuleExamCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
        List<ModuleExamCaseItemFill> fqs = ec.getFillItems();
        List<ModuleExamCaseItemJudge> jqs = ec.getJudgeItems();
        List<ModuleExamCaseItemEssay> eqs = ec.getEssayItems();
        List<ModuleExamCaseItemFile> ffqs = ec.getFileItems();
        List<ModuleExamCaseItemCase> caqs = ec.getCaseItems();
        //计算每道单选题是否正确
        for (ModuleExamCaseItemChoice c : cqs) {
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
        for (ModuleExamCaseItemMultiChoice c : mcqs) {
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
        for (ModuleExamCaseItemFill c : fqs) {
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
        for (ModuleExamCaseItemJudge c : jqs) {
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
        for (ModuleExamCaseItemEssay c : eqs) {
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
        for (ModuleExamCaseItemFile c : ffqs) {
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
        for (ModuleExamCaseItemCase c : caqs) {
            List<ModuleExamCaseItemChoice> cqss = c.getChoiceItems();
            //计算综合题中每道单选题是否正确
            for (ModuleExamCaseItemChoice cc : cqss) {
                if (!cc.getIfRight()) {
                    this.wrongDAO.wrongTimesPlusOne(cc.getQuestion().getId(), bu.getId(), "choice");
                    //加入我的难点
                    List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                    for (ExamKnowledge k : knows) {
                        wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                    }
                }
            }
            List<ModuleExamCaseItemMultiChoice> mcqss = c.getMultiChoiceItems();
            //计算综合题中每道单选题是否正确
            for (ModuleExamCaseItemMultiChoice cc : mcqss) {
                if (!cc.getIfRight()) {
                    this.wrongDAO.wrongTimesPlusOne(cc.getQuestion().getId(), bu.getId(), "mchoice");
                    //加入我的难点
                    List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                    for (ExamKnowledge k : knows) {
                        wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                    }
                }
            }
            List<ModuleExamCaseItemFill> fqss = c.getFillItems();
            //计算综合题中每道单选题是否正确
            for (ModuleExamCaseItemFill cc : fqss) {
                if (cc.getRightNum() < cc.getTotalNum()) {
                    this.wrongDAO.wrongTimesPlusOne(cc.getQuestion().getId(), bu.getId(), "fill");
                    //加入我的难点
                    List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                    for (ExamKnowledge k : knows) {
                        wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                    }
                }
            }
            List<ModuleExamCaseItemJudge> jqss = c.getJudgeItems();
            //计算综合题中每道单选题是否正确
            for (ModuleExamCaseItemJudge cc : jqss) {
                if (!cc.getIfRight()) {
                    this.wrongDAO.wrongTimesPlusOne(cc.getQuestion().getId(), bu.getId(), "judge");
                    //加入我的难点
                    List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                    for (ExamKnowledge k : knows) {
                        wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                    }
                }
            }
            List<ModuleExamCaseItemEssay> eqss = c.getEssayItems();
            //计算综合每道问答题得分
            for (ModuleExamCaseItemEssay cc : eqss) {
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
