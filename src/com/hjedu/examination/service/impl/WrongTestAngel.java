package com.hjedu.examination.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.aspectj.lang.JoinPoint;

import com.hjedu.examination.dao.IWrongKnowledgeDAO;
import com.hjedu.examination.dao.IWrongQuestionDAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.ExamCaseItemCase;
import com.hjedu.examination.entity.ExamCaseItemChoice;
import com.hjedu.examination.entity.ExamCaseItemEssay;
import com.hjedu.examination.entity.ExamCaseItemFile;
import com.hjedu.examination.entity.ExamCaseItemFill;
import com.hjedu.examination.entity.ExamCaseItemJudge;
import com.hjedu.examination.entity.ExamCaseItemMultiChoice;
import com.hjedu.examination.entity.ExamKnowledge;
import com.hjedu.examination.entity.WrongQuestion;
import com.hjedu.examination.entity.WrongTestInfo;

public class WrongTestAngel implements Serializable {
    
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
    /*在错题中心进行练习结束提交后计算试卷中的错题
    */
    public void checkAndSaveWrongQuestion(JoinPoint jp, ExamCase ec) {
        Object os[] = jp.getArgs();
        WrongTestInfo info = (WrongTestInfo) (os[0]);
        //ExamCase ec = (ExamCase) o;
        List<ExamCaseItemChoice> cqs = ec.getChoiceItems();
        List<ExamCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
        List<ExamCaseItemFill> fqs = ec.getFillItems();
        List<ExamCaseItemJudge> jqs = ec.getJudgeItems();
        List<ExamCaseItemEssay> eqs = ec.getEssayItems();
        List<ExamCaseItemFile> ffqs = ec.getFileItems();
        List<ExamCaseItemCase> caqs = ec.getCaseItems();
        //计算每道单选题是否正确
        for (ExamCaseItemChoice c : cqs) {
            if (!c.isIfRight()) {
                wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), ec.getUser().getId(), "choice");
                //加入我的难点
                List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                for (ExamKnowledge k : knows) {
                    wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                }
            } else {
                if (info.isIfDel()) {
                    System.out.println("One wrong question is deleted.");
                    wrongDAO.deleteWrongQuestionByQuestion(c.getQuestion().getId());
                }
            }
        }
        //计算每道多选题是否正确
        for (ExamCaseItemMultiChoice c : mcqs) {
            if (!c.isIfRight()) {
                wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), ec.getUser().getId(), "mchoice");
                //加入我的难点
                List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                for (ExamKnowledge k : knows) {
                    wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                }
            }else {
                if (info.isIfDel()) {
                    wrongDAO.deleteWrongQuestionByQuestion(c.getQuestion().getId());
                }
            }
        }
        //计算每道填空题是否正确
        for (ExamCaseItemFill c : fqs) {
            if (c.getRightNum() < c.getTotalNum()) {
                wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), ec.getUser().getId(), "fill");
                //加入我的难点
                List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                for (ExamKnowledge k : knows) {
                    wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                }
            }else {
                if (info.isIfDel()) {
                    
                    wrongDAO.deleteWrongQuestionByQuestion(c.getQuestion().getId());
                }
            }
        }
        //计算每道判断题是否正确
        for (ExamCaseItemJudge c : jqs) {
            if (!c.isIfRight()) {
                wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), ec.getUser().getId(), "judge");
                //加入我的难点
                List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                for (ExamKnowledge k : knows) {
                    wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                }
            }else {
                if (info.isIfDel()) {
                    wrongDAO.deleteWrongQuestionByQuestion(c.getQuestion().getId());
                }
            }
        }
        //计算每道问答题得分
        for (ExamCaseItemEssay c : eqs) {
            if (c.getRightRatio() < 0.8) {
                wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), ec.getUser().getId(), "essay");
                //加入我的难点
                List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                for (ExamKnowledge k : knows) {
                    wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                }
            }else {
                if (info.isIfDel()) {
                    wrongDAO.deleteWrongQuestionByQuestion(c.getQuestion().getId());
                }
            }
        }

        //计算每道文件题得分
        for (ExamCaseItemFile c : ffqs) {
            if (c.getRightRatio() < 0.8) {
                wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), ec.getUser().getId(), "file");
                //加入我的难点
                List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                for (ExamKnowledge k : knows) {
                    wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                }
            }else {
                if (info.isIfDel()) {
                    wrongDAO.deleteWrongQuestionByQuestion(c.getQuestion().getId());
                }
            }
        }
        //错题练习中没有综合题
        
    }
}
