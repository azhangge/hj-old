package com.hjedu.examination.service.impl;

import java.io.Serializable;
import java.util.List;
import org.aspectj.lang.JoinPoint;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IWrongKnowledgeDAO;
import com.hjedu.examination.dao.IWrongQuestionDAO;
import com.hjedu.examination.entity.ExamKnowledge;
import com.hjedu.examination.entity.contest.ContestCase;
import com.hjedu.examination.entity.contest.ContestCaseItemCase;
import com.hjedu.examination.entity.contest.ContestCaseItemChoice;
import com.hjedu.examination.entity.contest.ContestCaseItemEssay;
import com.hjedu.examination.entity.contest.ContestCaseItemFile;
import com.hjedu.examination.entity.contest.ContestCaseItemFill;
import com.hjedu.examination.entity.contest.ContestCaseItemJudge;
import com.hjedu.examination.entity.contest.ContestCaseItemMultiChoice;

public class ContestWrongQuestionAngel implements Serializable {

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
    public void checkAndSaveWrongQuestion(JoinPoint jp, ContestCase ec) {

        //ExamCase ec = (ExamCase) o;
        BbsUser bu = ec.getUser();
        List<ContestCaseItemChoice> cqs = ec.getChoiceItems();
        List<ContestCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
        List<ContestCaseItemFill> fqs = ec.getFillItems();
        List<ContestCaseItemJudge> jqs = ec.getJudgeItems();
        List<ContestCaseItemEssay> eqs = ec.getEssayItems();
        List<ContestCaseItemFile> ffqs = ec.getFileItems();
        List<ContestCaseItemCase> caqs = ec.getCaseItems();
        //计算每道单选题是否正确
        if (cqs != null) {
            for (ContestCaseItemChoice c : cqs) {
                if (c != null) {
                    if (!c.getIfRight()) {
                        this.wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), bu.getId(), "choice");
                        //加入我的难点
                        List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                        for (ExamKnowledge k : knows) {
                            wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                        }
                    }
                }
            }
        }
        //计算每道多选题是否正确
        if (mcqs != null) {
            for (ContestCaseItemMultiChoice c : mcqs) {
                if (c != null) {
                    if (!c.getIfRight()) {
                        this.wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), bu.getId(), "mchoice");
                        //加入我的难点
                        List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                        for (ExamKnowledge k : knows) {
                            wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                        }
                    }
                }
            }
        }
        //计算每道填空题是否正确
        if (fqs != null) {
            for (ContestCaseItemFill c : fqs) {
                if (c != null) {
                    if (c.getRightNum() < c.getTotalNum()) {
                        this.wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), bu.getId(), "fill");
                        //加入我的难点
                        List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                        for (ExamKnowledge k : knows) {
                            wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                        }
                    }
                }
            }
        }
        //计算每道判断题是否正确
        if (jqs != null) {
            for (ContestCaseItemJudge c : jqs) {
                if (c != null) {
                    if (!c.getIfRight()) {
                        this.wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), bu.getId(), "judge");
                        //加入我的难点
                        List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                        for (ExamKnowledge k : knows) {
                            wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                        }
                    }
                }
            }
        }
        //计算每道问答题得分
        if (eqs != null) {
            for (ContestCaseItemEssay c : eqs) {
                if (c != null) {
                    if (c.getRightRatio() < 0.8) {
                        this.wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), bu.getId(), "essay");
                        //加入我的难点
                        List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                        for (ExamKnowledge k : knows) {
                            wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                        }
                    }
                }
            }
        }

        //计算每道文件题得分
        if (ffqs != null) {
            for (ContestCaseItemFile c : ffqs) {
                if (c != null) {
                    if (c.getRightRatio() < 0.8) {
                        this.wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), bu.getId(), "file");
                        //加入我的难点
                        List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                        for (ExamKnowledge k : knows) {
                            wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                        }
                    }
                }
            }
        }
        //计算每道综合题得分
        if (caqs != null) {
            for (ContestCaseItemCase c : caqs) {
                if (c != null) {
                    List<ContestCaseItemChoice> cqss = c.getChoiceItems();
                    //计算综合题中每道单选题是否正确
                    if (cqss != null) {
                        for (ContestCaseItemChoice cc : cqss) {
                            if (cc != null) {
                                if (!cc.getIfRight()) {
                                    this.wrongDAO.wrongTimesPlusOne(cc.getQuestion().getId(), bu.getId(), "choice");
                                    //加入我的难点
                                    List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                                    for (ExamKnowledge k : knows) {
                                        wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                                    }
                                }
                            }
                        }
                    }
                    List<ContestCaseItemMultiChoice> mcqss = c.getMultiChoiceItems();
                    //计算综合题中每道单选题是否正确
                    if (mcqss != null) {
                        for (ContestCaseItemMultiChoice cc : mcqss) {
                            if (cc != null) {
                                if (!cc.getIfRight()) {
                                    this.wrongDAO.wrongTimesPlusOne(cc.getQuestion().getId(), bu.getId(), "mchoice");
                                    //加入我的难点
                                    List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                                    for (ExamKnowledge k : knows) {
                                        wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                                    }
                                }
                            }
                        }
                    }
                    List<ContestCaseItemFill> fqss = c.getFillItems();
                    //计算综合题中每道单选题是否正确
                    if (fqss != null) {
                        for (ContestCaseItemFill cc : fqss) {
                            if (cc != null) {
                                if (cc.getRightNum() < cc.getTotalNum()) {
                                    this.wrongDAO.wrongTimesPlusOne(cc.getQuestion().getId(), bu.getId(), "fill");
                                    //加入我的难点
                                    List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                                    for (ExamKnowledge k : knows) {
                                        wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                                    }
                                }
                            }
                        }
                    }
                    List<ContestCaseItemJudge> jqss = c.getJudgeItems();
                    //计算综合题中每道单选题是否正确
                    if (jqss != null) {
                        for (ContestCaseItemJudge cc : jqss) {
                            if (cc != null) {
                                if (!cc.getIfRight()) {
                                    this.wrongDAO.wrongTimesPlusOne(cc.getQuestion().getId(), bu.getId(), "judge");
                                    //加入我的难点
                                    List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                                    for (ExamKnowledge k : knows) {
                                        wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                                    }
                                }
                            }
                        }
                    }
                    List<ContestCaseItemEssay> eqss = c.getEssayItems();
                    //计算综合每道问答题得分
                    if (eqss != null) {
                        for (ContestCaseItemEssay cc : eqss) {
                            if (cc != null) {
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
            }
        }

    }
}
