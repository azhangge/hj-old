package com.hjedu.examination.service.impl;

import java.io.Serializable;
import java.util.List;
import org.aspectj.lang.JoinPoint;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IWrongKnowledgeDAO;
import com.hjedu.examination.dao.IWrongQuestionDAO;
import com.hjedu.examination.entity.ExamKnowledge;
import com.hjedu.examination.entity.module.ModuleExamCase;
import com.hjedu.examination.entity.module2.ModuleExam2CaseItemAdapter;
import com.hjedu.examination.entity.module2.ModuleExam2Part;

public class Module2WrongQuestionAngel implements Serializable {

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
    public void checkAndSaveSingleWrongQuestion(JoinPoint jp, ModuleExam2CaseItemAdapter adapter) {

        //ExamCase ec = (ExamCase) o;
        BbsUser bu = adapter.getItem().getExamCase().getUser();
        //计算每道单选题是否正确
        String qtype = "choice";
        if (adapter.getQtype().equals("case")) {
            if ("choice".equals(adapter.getItem().getCaseType())) {
                qtype = "choice";
            } else if ("mchoice".equals(adapter.getItem().getCaseType())) {
                qtype = "mchoice";
            } else if ("fill".equals(adapter.getItem().getCaseType())) {
                qtype = "fill";
            } else if ("judge".equals(adapter.getItem().getCaseType())) {
                qtype = "judge";
            } else if ("essay".equals(adapter.getItem().getCaseType())) {
                qtype = "essay";
            } else if ("file".equals(adapter.getItem().getCaseType())) {
                qtype = "file";
            }
        } else {
            qtype = adapter.getQtype();
        }
        try {
            if (!adapter.getItem().getIfRight()) {
                this.wrongDAO.wrongTimesPlusOne(adapter.getQuestion().getId(), bu.getId(), qtype);
                //加入我的难点
                List<ExamKnowledge> knows = null;
                if (qtype.equals("choice")) {
                    knows = adapter.getChoiceItem().getQuestion().getKnowledges();
                } else if (qtype.equals("mchoice")) {
                    knows = adapter.getMultiChoiceItem().getQuestion().getKnowledges();
                } else if (qtype.equals("fill")) {
                    knows = adapter.getFillItem().getQuestion().getKnowledges();
                } else if (qtype.equals("judge")) {
                    knows = adapter.getJudgeItem().getQuestion().getKnowledges();
                } else if (qtype.equals("essay")) {
                    knows = adapter.getEssayItem().getQuestion().getKnowledges();
                } else if (qtype.equals("file")) {
                    knows = adapter.getFileItem().getQuestion().getKnowledges();
                }
                if (knows != null) {
                    for (ExamKnowledge k : knows) {
                        wrong2DAO.wrongTimesPlusOne(k.getId(), bu.getId());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*在考试结束提交后计算试卷中的错题
     */
    public void checkAndSaveWrongQuestion(JoinPoint jp, ModuleExamCase ec) {
        List<ModuleExam2Part> parts = ec.getCparts();

        //Collections.sort(parts);
        for (ModuleExam2Part part : parts) {
            //List<ManualPartItem> items = part.getItems();
            List<ModuleExam2CaseItemAdapter> adapters = part.getAdapters();
            for (ModuleExam2CaseItemAdapter adapter : adapters) {
                BbsUser bu = adapter.getItem().getExamCase().getUser();
                //计算每道单选题是否正确
                String qtype = "choice";
                if (adapter.getQtype().equals("case")) {
                    if ("choice".equals(adapter.getItem().getCaseType())) {
                        qtype = "choice";
                    } else if ("mchoice".equals(adapter.getItem().getCaseType())) {
                        qtype = "mchoice";
                    } else if ("fill".equals(adapter.getItem().getCaseType())) {
                        qtype = "fill";
                    } else if ("judge".equals(adapter.getItem().getCaseType())) {
                        qtype = "judge";
                    } else if ("essay".equals(adapter.getItem().getCaseType())) {
                        qtype = "essay";
                    } else if ("file".equals(adapter.getItem().getCaseType())) {
                        qtype = "file";
                    }
                } else {
                    qtype = adapter.getQtype();
                }
                try {
                    if (!adapter.getItem().getIfRight()&&adapter.getItem().isDone()) {
                        this.wrongDAO.recordWrong(adapter.getQuestion().getId(), bu.getId(), qtype);
                        //加入我的难点
                        List<ExamKnowledge> knows = null;
                        if (qtype.equals("choice")) {
                            knows = adapter.getChoiceItem().getQuestion().getKnowledges();
                        } else if (qtype.equals("mchoice")) {
                            knows = adapter.getMultiChoiceItem().getQuestion().getKnowledges();
                        } else if (qtype.equals("fill")) {
                            knows = adapter.getFillItem().getQuestion().getKnowledges();
                        } else if (qtype.equals("judge")) {
                            knows = adapter.getJudgeItem().getQuestion().getKnowledges();
                        } else if (qtype.equals("essay")) {
                            knows = adapter.getEssayItem().getQuestion().getKnowledges();
                        } else if (qtype.equals("file")) {
                            knows = adapter.getFileItem().getQuestion().getKnowledges();
                        }
                        if (knows != null) {
                            for (ExamKnowledge k : knows) {
                                wrong2DAO.recordWrong(k.getId(), bu.getId());
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
