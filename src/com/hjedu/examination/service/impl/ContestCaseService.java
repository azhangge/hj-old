package com.hjedu.examination.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.ICaseQuestionDAO;
import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IContestCaseDAO;
import com.hjedu.examination.dao.IEssayQuestionDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.dao.IFillQuestionDAO;
import com.hjedu.examination.dao.IJudgeQuestionDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.entity.CaseQuestion;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.EssayQuestion;
import com.hjedu.examination.entity.ExamChoice;
import com.hjedu.examination.entity.ExamMultiChoice;
import com.hjedu.examination.entity.ExamPaperManualPart;
import com.hjedu.examination.entity.ExamRoom;
import com.hjedu.examination.entity.FileQuestion;
import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.examination.entity.ManualPartItem;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.hjedu.examination.entity.contest.ContestCase;
import com.hjedu.examination.entity.contest.ContestCaseItemAdapter;
import com.hjedu.examination.entity.contest.ContestCaseItemCase;
import com.hjedu.examination.entity.contest.ContestCaseItemChoice;
import com.hjedu.examination.entity.contest.ContestCaseItemEssay;
import com.hjedu.examination.entity.contest.ContestCaseItemFile;
import com.hjedu.examination.entity.contest.ContestCaseItemFill;
import com.hjedu.examination.entity.contest.ContestCaseItemFillBlock;
import com.hjedu.examination.entity.contest.ContestCaseItemJudge;
import com.hjedu.examination.entity.contest.ContestCaseItemMultiChoice;
import com.hjedu.examination.entity.contest.ExamContestSession;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.huajie.util.HTMLCleaner;
import com.huajie.util.SimilarAlgorithm;
import com.huajie.util.SpringHelper;

public class ContestCaseService implements Serializable {

    public String answerB;
    public String answerE;
    IContestCaseDAO examCaseDAO;
    IChoiceQuestionDAO choiceQuestionDAO;
    IMultiChoiceQuestionDAO multiChoiceQuestionDAO;
    IFillQuestionDAO fillQuestionDAO;
    IJudgeQuestionDAO judgeQuestionDAO;
    IEssayQuestionDAO essayQuestionDAO;
    IFileQuestionDAO fileQuestionDAO;
    ICaseQuestionDAO caseQuestionDAO;

    public String getAnswerB() {
        return answerB;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    public String getAnswerE() {
        return answerE;
    }

    public void setAnswerE(String answerE) {
        this.answerE = answerE;
    }

    public IContestCaseDAO getExamCaseDAO() {
        return examCaseDAO;
    }

    public void setExamCaseDAO(IContestCaseDAO examCaseDAO) {
        this.examCaseDAO = examCaseDAO;
    }

    public ICaseQuestionDAO getCaseQuestionDAO() {
        return caseQuestionDAO;
    }

    public void setCaseQuestionDAO(ICaseQuestionDAO caseQuestionDAO) {
        this.caseQuestionDAO = caseQuestionDAO;
    }

    public IChoiceQuestionDAO getChoiceQuestionDAO() {
        return choiceQuestionDAO;
    }

    public void setChoiceQuestionDAO(IChoiceQuestionDAO choiceQuestionDAO) {
        this.choiceQuestionDAO = choiceQuestionDAO;
    }

    public IMultiChoiceQuestionDAO getMultiChoiceQuestionDAO() {
        return multiChoiceQuestionDAO;
    }

    public void setMultiChoiceQuestionDAO(IMultiChoiceQuestionDAO multiChoiceQuestionDAO) {
        this.multiChoiceQuestionDAO = multiChoiceQuestionDAO;
    }

    public IFillQuestionDAO getFillQuestionDAO() {
        return fillQuestionDAO;
    }

    public void setFillQuestionDAO(IFillQuestionDAO fillQuestionDAO) {
        this.fillQuestionDAO = fillQuestionDAO;
    }

    public IJudgeQuestionDAO getJudgeQuestionDAO() {
        return judgeQuestionDAO;
    }

    public void setJudgeQuestionDAO(IJudgeQuestionDAO judgeQuestionDAO) {
        this.judgeQuestionDAO = judgeQuestionDAO;
    }

    public IEssayQuestionDAO getEssayQuestionDAO() {
        return essayQuestionDAO;
    }

    public void setEssayQuestionDAO(IEssayQuestionDAO essayQuestionDAO) {
        this.essayQuestionDAO = essayQuestionDAO;
    }

    public IFileQuestionDAO getFileQuestionDAO() {
        return fileQuestionDAO;
    }

    public void setFileQuestionDAO(IFileQuestionDAO fileQuestionDAO) {
        this.fileQuestionDAO = fileQuestionDAO;
    }

    public void buildItemFillBlocks(ContestCaseItemFill fill) {
        List<ContestCaseItemFillBlock> blocks = new LinkedList();
        blocks.clear();
        String t = fill.getQuestion().getName();
        //MyLogger.echo(t);
        int b = -1;
        int e = -1;
        try {
            while (true) {

                b = t.indexOf(answerB);
                e = t.indexOf(answerE);
                if (b < 0 || e < 0) {
                    break;
                }
                String pre = t.substring(0, b);
                String ans = t.substring(b, e + 1);
                //MyLogger.echo(pre);
                //MyLogger.echo(ans);
                //t = t.replace(pre, "");
                //t = t.replace(ans, "");
                t = t.substring(e + 1);
                ContestCaseItemFillBlock block = new ContestCaseItemFillBlock();
                block.setPreStr(pre);
                ans = ans.replace(answerB, "");
                ans = ans.replace(answerE, "");
                block.setRightAnswer(ans);
                blocks.add(block);

            }
        } catch (Exception eee) {
            eee.printStackTrace();
            System.out.println("出错试题ID：" + fill.getQuestion().getId() + "，内容：" + fill.getQuestion().getName());
        }
        fill.setBlocks(blocks);
        fill.setLastStr(t);
    }

    private void choiceOrderAdapter(List<ExamChoice> choices) {
        Collections.shuffle(choices);
        char be = 'A';
        int i = 0;
        for (ExamChoice c : choices) {
            char ee = (char) (be + i);
            c.setLabelRendered(String.valueOf(ee));
            i++;
        }
    }

    private void multiChoiceOrderAdapter(List<ExamMultiChoice> choices) {
        Collections.shuffle(choices);
        char be = 'A';
        int i = 0;
        for (ExamMultiChoice c : choices) {
            char ee = (char) (be + i);
            c.setLabelRendered(String.valueOf(ee));
            i++;
        }
    }

    private ContestCaseItemCase buildCaseItem(CaseQuestion j, ContestCase ec) {

        ContestCaseItemCase ecie = new ContestCaseItemCase();
        ecie.setExamCase(ec);
        ecie.setQuestion(j);
        //将选择题加入综合题中
        List<ContestCaseItemChoice> ecicqcs = new LinkedList();
        List<ChoiceQuestion> cqscc = j.getChoices();
        for (ChoiceQuestion c : cqscc) {
            ContestCaseItemChoice ecic = new ContestCaseItemChoice();
            //ecic.setContestCase(ec);
            ecic.setCaseItem(ecie);
            List<ExamChoice> ecs = c.getChoices();
            //Collections.sort(ecs);
            //配置单选题选项随机
            if (ec.getExamination().isChoiceRandom()) {
                this.choiceOrderAdapter(ecs);
            }
            c.setChoices(ecs);
            ecic.setQuestion(c);
            ecicqcs.add(ecic);
        }
        ecie.setChoiceItems(ecicqcs);

        //将多选题加入综合题中
        List<ContestCaseItemMultiChoice> mcieqs = new LinkedList();
        List<MultiChoiceQuestion> mqscc = j.getMultiChoices();
        for (MultiChoiceQuestion c : mqscc) {
            ContestCaseItemMultiChoice eciee = new ContestCaseItemMultiChoice();
            //ecie.setContestCase(ec);
            eciee.setCaseItem(ecie);
            List<ExamMultiChoice> ecs = c.getChoices();
            //Collections.sort(ecs);
            //配置单选题选项随机
            if (ec.getExamination().isMultiChoiceRandom()) {
                this.multiChoiceOrderAdapter(ecs);
            }
            eciee.setQuestion(c);
            mcieqs.add(eciee);
        }
        ecie.setMultiChoiceItems(mcieqs);

        //将填空题加入综合题中
        List<ContestCaseItemFill> fcieqs = new LinkedList();
        List<FillQuestion> fqscc = j.getFills();
        for (FillQuestion c : fqscc) {
            ContestCaseItemFill eciee = new ContestCaseItemFill();
            //ecie.setContestCase(ec);
            eciee.setCaseItem(ecie);
            eciee.setQuestion(c);
            fcieqs.add(eciee);
        }
        ecie.setFillItems(fcieqs);

        //将判断题加入综合题中
        List<ContestCaseItemJudge> jcieqs = new LinkedList();
        List<JudgeQuestion> jqscc = j.getJudges();
        for (JudgeQuestion c : jqscc) {
            ContestCaseItemJudge eciee = new ContestCaseItemJudge();
            //ecie.setContestCase(ec);
            eciee.setCaseItem(ecie);
            eciee.setQuestion(c);
            jcieqs.add(eciee);
        }
        ecie.setJudgeItems(jcieqs);

        //将问答题加入综合题中
        List<ContestCaseItemEssay> ecieqs = new LinkedList();
        List<EssayQuestion> eqscc = j.getEssaies();
        for (EssayQuestion c : eqscc) {
            ContestCaseItemEssay eciee = new ContestCaseItemEssay();
            //ecie.setContestCase(ec);
            eciee.setCaseItem(ecie);
            eciee.setQuestion(c);
            ecieqs.add(eciee);
        }
        ecie.setEssayItems(ecieqs);
        //向临时变量中加入综合题
        return ecie;
    }

    public void buildContestCase(ContestCase ec) {
        ExamContestSession exam = ec.getExamination();
        ec.setSessionStr(exam.getSessionStr());
        ec.setChoiceItems(new ArrayList());
        ec.setMultiChoiceItems(new ArrayList());
        ec.setFillItems(new ArrayList());
        ec.setJudgeItems(new ArrayList());
        ec.setEssayItems(new ArrayList());
        ec.setFileItems(new ArrayList());
        ec.setCaseItems(new ArrayList());

        List<ExamPaperManualPart> parts = exam.getManualPaper().getParts();
        int uniOrd = 0;//对全部试题统一标号
        //Collections.sort(parts);
        for (ExamPaperManualPart part : parts) {
            List<ManualPartItem> items = part.getItems();
            Collections.sort(items);
            List<ContestCaseItemAdapter> adapters = new ArrayList();
            for (ManualPartItem item : items) {
                uniOrd++;
                ContestCaseItemAdapter adapter = new ContestCaseItemAdapter();
                adapter.setOrd(uniOrd);//统一标号
                if ("choice".equals(item.getQtype())) {//判断试卷条目的类型
                    ContestCaseItemChoice ei = new ContestCaseItemChoice();//构建一个考试条目
                    ei.setQuestion((ChoiceQuestion) item.getQuestion());//设置考试条目对应的试题F
                    List<ExamChoice> ecs = ei.getQuestion().getChoices();
                    if (ec.getExamination().isChoiceRandom()) {
                        this.choiceOrderAdapter(ecs);
                    }
                    ei.getQuestion().setChoices(ecs);

                    ei.setExamCase(ec);
                    ei.setScore(item.getQuestionScore());
                    adapter.setChoiceItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                    adapter.setQtype("choice");
                    adapter.setScore(item.getQuestionScore());//设置adapter的分值
                    adapter.setItem(ei);
                    ec.getChoiceItems().add(ei);
                } else if ("mchoice".equals(item.getQtype())) {
                    ContestCaseItemMultiChoice ei = new ContestCaseItemMultiChoice();
                    ei.setQuestion((MultiChoiceQuestion) item.getQuestion());
                    List<ExamMultiChoice> ecs = ei.getQuestion().getChoices();
                    if (ec.getExamination().isChoiceRandom()) {
                        this.multiChoiceOrderAdapter(ecs);
                    }
                    ei.getQuestion().setChoices(ecs);

                    ei.setExamCase(ec);
                    ei.setScore(item.getQuestionScore());
                    adapter.setMultiChoiceItem(ei);
                    adapter.setQtype("mchoice");
                    adapter.setScore(item.getQuestionScore());//设置adapter的分值
                    adapter.setItem(ei);
                    ec.getMultiChoiceItems().add(ei);
                } else if ("fill".equals(item.getQtype())) {
                    ContestCaseItemFill ei = new ContestCaseItemFill();
                    ei.setQuestion((FillQuestion) item.getQuestion());
                    ei.setExamCase(ec);
                    ei.setScore(item.getQuestionScore());
                    adapter.setFillItem(ei);
                    adapter.setQtype("fill");
                    adapter.setScore(item.getQuestionScore());//设置adapter的分值
                    adapter.setItem(ei);
                    ec.getFillItems().add(ei);
                } else if ("judge".equals(item.getQtype())) {
                    ContestCaseItemJudge ei = new ContestCaseItemJudge();
                    ei.setQuestion((JudgeQuestion) item.getQuestion());
                    ei.setExamCase(ec);
                    ei.setScore(item.getQuestionScore());
                    adapter.setJudgeItem(ei);
                    adapter.setQtype("judge");
                    adapter.setScore(item.getQuestionScore());//设置adapter的分值
                    adapter.setItem(ei);
                    ec.getJudgeItems().add(ei);
                } else if ("essay".equals(item.getQtype())) {
                    ContestCaseItemEssay ei = new ContestCaseItemEssay();
                    ei.setQuestion((EssayQuestion) item.getQuestion());
                    ei.setExamCase(ec);
                    ei.setScore(item.getQuestionScore());
                    adapter.setEssayItem(ei);
                    adapter.setQtype("essay");
                    adapter.setScore(item.getQuestionScore());//设置adapter的分值
                    adapter.setItem(ei);
                    ec.getEssayItems().add(ei);

                } else if ("file".equals(item.getQtype())) {
                    ContestCaseItemFile ei = new ContestCaseItemFile();
                    ei.setQuestion((FileQuestion) item.getQuestion());
                    ei.setExamCase(ec);
                    ei.setScore(item.getQuestionScore());
                    adapter.setFileItem(ei);
                    adapter.setQtype("file");
                    adapter.setScore(item.getQuestionScore());//设置adapter的分值
                    adapter.setItem(ei);
                    ec.getFileItems().add(ei);
                } else if ("case".equals(item.getQtype())) {

                    CaseQuestion cq = (CaseQuestion) item.getQuestion();
                    ContestCaseItemCase ei = this.buildCaseItem(cq, ec);

                    adapter.setCaseItem(ei);
                    adapter.setQtype("case");
                    adapter.setScore(cq.getTotalScore());//设置adapter的分值,综合题的分值为综合题的原分值
                    adapter.setItem(ei);
                    ec.getCaseItems().add(ei);
                }

                adapters.add(adapter);
            }
            part.setCadapters(adapters);
        }
        ec.setParts(parts);//将装载好的parts加入考试中

    }

    public ContestCase computeContestCase(ContestCase ec) {
        double totalScore = 0;
        double choiceScore = 0;
        double multiChoiceScore = 0;
        double fillScore = 0;
        double judgeScore = 0;
        double essayScore = 0;
        double fileScore = 0;
        double caseScore = 0;

        List<ExamPaperManualPart> parts = ec.getParts();

        //Collections.sort(parts);
        for (ExamPaperManualPart part : parts) {
            //List<ManualPartItem> items = part.getItems();
            List<ContestCaseItemAdapter> adapters = part.getCadapters();
            for (ContestCaseItemAdapter adapter : adapters) {
                if ("choice".equals(adapter.getQtype())) {//判断试卷条目的类型
                    ContestCaseItemChoice ei = adapter.getChoiceItem();//获取考试条目
                    List<ExamChoice> ls = ei.getQuestion().getChoices();
                    for (ExamChoice e : ls) {
                        if (e.isSelected()) {
                            ei.setUserAnswer(e.getLabel().trim());
                            break;
                        }
                    }
                    System.out.println("题目：" + ei.getQuestion().getName() + ",选择：" + ei.getUserAnswer());
                    ei.setRightAnswer(ei.getQuestion().getAnswer().trim());
                    ei.setIfRight(ei.getRightAnswer().equalsIgnoreCase(ei.getUserAnswer()));
                    if (ei.getIfRight()) {
                        choiceScore += ei.getScore();
                    }

                } else if ("mchoice".equals(adapter.getQtype())) {
                    ContestCaseItemMultiChoice c = (ContestCaseItemMultiChoice) adapter.getMultiChoiceItem();
                    //List<ExamMultiChoice> ls = c.getQuestion().getChoices();
                    StringBuilder sb = new StringBuilder();
                    if (c.getSelectedLabels() != null) {
                        for (String l : c.getSelectedLabels()) {
                            sb.append(l);

                        }
                    }
                    char[] cs = sb.toString().toUpperCase().toCharArray();
                    List lss = new LinkedList();
                    for (char c1 : cs) {
                        lss.add(c1);
                    }
                    Collections.sort(lss);
                    Object[] css = lss.toArray();
                    StringBuilder sb2 = new StringBuilder();
                    for (Object oc : css) {
                        sb2.append(oc.toString());
                    }
                    String s = sb2.toString();
                    c.setUserAnswer(s);

                    c.setRightAnswer(c.getQuestion().getAnswer().trim());
                    c.setIfRight(c.getRightAnswer().equalsIgnoreCase(c.getUserAnswer()));
                    if (c.getIfRight()) {
                        multiChoiceScore += c.getScore();
                    }

                } else if ("fill".equals(adapter.getQtype())) {
                    ContestCaseItemFill c = (ContestCaseItemFill) adapter.getFillItem();
                    List<ContestCaseItemFillBlock> ls = c.getBlocks();
                    StringBuilder as = new StringBuilder();
                    StringBuilder us = new StringBuilder();
                    int rightNum = 0;
                    for (ContestCaseItemFillBlock e : ls) {
                        as.append(e.getRightAnswer().trim());
                        as.append(",");
                        us.append(e.getUserAnswer().trim());
                        us.append(",");
                        if (e.getRightAnswer().trim().equals(e.getUserAnswer().trim())) {
                            rightNum++;
                        }
                    }
                    c.setRightNum(rightNum);
                    c.setRightAnswerStr(as.toString());
                    c.setUserAnswerStr(us.toString());
                    int t = c.getTotalNum();
                    if (t != 0) {
                        double dt = ((double) c.getRightNum()) / ((double) c.getTotalNum());
                        if (dt > 1) {
                            dt = 1D;
                        }
                        fillScore += c.getScore() * dt;
                    }

                } else if ("judge".equals(adapter.getQtype())) {
                    ContestCaseItemJudge c = (ContestCaseItemJudge) adapter.getJudgeItem();
                    c.setRightAnswer(String.valueOf(c.getQuestion().getAnswer()));
                    c.setIfRight(c.getRightAnswer().equals(c.getUserAnswer()));
                    if (c.getIfRight()) {
                        judgeScore += c.getScore();
                    }

                } else if ("essay".equals(adapter.getQtype())) {
                    ContestCaseItemEssay c = (ContestCaseItemEssay) adapter.getEssayItem();
                    c.setRightAnswer(c.getQuestion().getRightStr());
                    float ratio = 0f;
                    try {
                        ratio = SimilarAlgorithm.getSimilarity(HTMLCleaner.delHTMLTag(c.getQuestion().getAnswer()), HTMLCleaner.delHTMLTag(c.getUserAnswer()));
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                    c.setRightRatio(ratio);
                    essayScore += c.getScore() * ratio;

                } else if ("file".equals(adapter.getQtype())) {
                    ContestCaseItemFile c = (ContestCaseItemFile) adapter.getFileItem();
                    c.setRightAnswer(c.getQuestion().getRightStr());
                    float ratio = 0f;
                    try {
                        ratio = SimilarAlgorithm.getSimilarity(HTMLCleaner.delHTMLTag(c.getQuestion().getAnswer()), HTMLCleaner.delHTMLTag(c.getUserAnswer()));
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                    c.setRightRatio(ratio);
                    fileScore += c.getScore() * ratio;

                } else if ("case".equals(adapter.getQtype())) {
                    ContestCaseItemCase cic = (ContestCaseItemCase) adapter.getCaseItem();
                    //计算综合题中的选择题得分
                    double choiceScoreTemp = 0;
                    double multiChoiceScoreTemp = 0;
                    double fillScoreTemp = 0;
                    double judgeScoreTemp = 0;
                    double essayScoreTemp = 0;
                    List<ContestCaseItemChoice> cqi = cic.getChoiceItems();
                    for (ContestCaseItemChoice c : cqi) {
                        List<ExamChoice> ls = c.getQuestion().getChoices();
                        for (ExamChoice e : ls) {
                            if (e.isSelected()) {
                                c.setUserAnswer(e.getLabel().trim());
                                break;
                            }
                        }
                        c.setRightAnswer(c.getQuestion().getAnswer().trim());
                        c.setIfRight(c.getRightAnswer().equalsIgnoreCase(c.getUserAnswer()));
                        if (c.getIfRight()) {
                            //综合题中选择题的评分标准计算
                            caseScore += cic.getQuestion().getChoiceScore();
                            choiceScoreTemp += cic.getQuestion().getChoiceScore();
                        }
                    }

                    //计算综合题中每道多选题是否正确
                    List<ContestCaseItemMultiChoice> mqi = cic.getMultiChoiceItems();
                    for (ContestCaseItemMultiChoice c : mqi) {
                        //List<ExamMultiChoice> ls = c.getQuestion().getChoices();
                        StringBuilder sb = new StringBuilder();
                        if (c.getSelectedLabels() != null) {
                            for (String l : c.getSelectedLabels()) {
                                sb.append(l);

                            }
                        }
                        char[] cs = sb.toString().toUpperCase().toCharArray();
                        List lss = new LinkedList();
                        for (char c1 : cs) {
                            lss.add(c1);
                        }
                        Collections.sort(lss);
                        Object[] css = lss.toArray();
                        StringBuilder sb2 = new StringBuilder();
                        for (Object oc : css) {
                            sb2.append(oc.toString());
                        }
                        String s = sb2.toString();
                        c.setUserAnswer(s);

                        c.setRightAnswer(c.getQuestion().getAnswer().trim());
                        c.setIfRight(c.getRightAnswer().equalsIgnoreCase(c.getUserAnswer()));
                        if (c.getIfRight()) {
                            multiChoiceScoreTemp += cic.getQuestion().getMultiChoiceScore();
                            caseScore += cic.getQuestion().getMultiChoiceScore();
                        }
                    }
                    //计算综合题中每道填空题是否正确
                    List<ContestCaseItemFill> fqi = cic.getFillItems();
                    for (ContestCaseItemFill c : fqi) {
                        List<ContestCaseItemFillBlock> ls = c.getBlocks();
                        StringBuilder as = new StringBuilder();
                        StringBuilder us = new StringBuilder();
                        int rightNum = 0;
                        for (ContestCaseItemFillBlock e : ls) {
                            as.append(e.getRightAnswer().trim());
                            as.append(",");
                            us.append(e.getUserAnswer().trim());
                            us.append(",");
                            if (e.getRightAnswer().trim().equals(e.getUserAnswer().trim())) {
                                rightNum++;
                            }
                        }
                        c.setRightNum(rightNum);
                        c.setRightAnswerStr(as.toString());
                        c.setUserAnswerStr(us.toString());
                        int t = c.getTotalNum();
                        if (t != 0) {
                            double dt = ((double) c.getRightNum()) / ((double) c.getTotalNum());
                            if (dt > 1) {
                                dt = 1D;
                            }
                            fillScoreTemp += cic.getQuestion().getFillScore() * dt;
                            caseScore += cic.getQuestion().getFillScore() * dt;
                        }
                    }
                    //计算综合题中每道判断题是否正确
                    List<ContestCaseItemJudge> jqi = cic.getJudgeItems();
                    for (ContestCaseItemJudge c : jqi) {
                        c.setRightAnswer(String.valueOf(c.getQuestion().getAnswer()));
                        c.setIfRight(c.getRightAnswer().equals(c.getUserAnswer()));
                        if (c.getIfRight()) {
                            judgeScoreTemp += cic.getQuestion().getJudgeScore();
                            caseScore += cic.getQuestion().getJudgeScore();
                        }
                    }

                    //计算综合题中每道问答题得分
                    List<ContestCaseItemEssay> eqi = cic.getEssayItems();
                    for (ContestCaseItemEssay c : eqi) {
                        c.setRightAnswer(c.getQuestion().getRightStr());
                        float ratio = SimilarAlgorithm.getSimilarity(HTMLCleaner.delHTMLTag(c.getQuestion().getAnswer()), HTMLCleaner.delHTMLTag(c.getUserAnswer()));
                        c.setRightRatio(ratio);
                        //综合题中问答题的评分标准计算
                        caseScore += cic.getQuestion().getEssayScore() * ratio;
                        essayScoreTemp += cic.getQuestion().getEssayScore() * ratio;
                    }
                    cic.setChoiceScore(choiceScoreTemp);//综合题中的选择题得分
                    cic.setMultiChoiceScore(multiChoiceScoreTemp);
                    cic.setFillScore(fillScoreTemp);
                    cic.setJudgeScore(judgeScoreTemp);
                    cic.setEssayScore(essayScoreTemp);//综合题中的问答题得分

                }
            }
        }

        totalScore = choiceScore + fillScore + judgeScore + multiChoiceScore + essayScore + fileScore + caseScore;

        double ratio = totalScore / ec.getTotalFullScore();
        if (ratio < 0) {
            ratio = 0;
        }
        if (ratio > 1) {
            ratio = 1;
        }
        long bbsScore = 0L;
        if (!ec.getExamination().getId().equals("7")) {
            bbsScore = (long) (ec.getExamination().getBbsScore() * ratio);
        }
        ec.setChoiceScore(choiceScore);
        ec.setMultiChoiceScore(multiChoiceScore);
        ec.setFillScore(fillScore);
        ec.setJudgeScore(judgeScore);
        ec.setEssayScore(essayScore);
        ec.setFileScore(fileScore);
        ec.setCaseScore(caseScore);
        ec.setScore(totalScore);
        ec.setBbsScore(bbsScore);
        return ec;
    }

    public ContestCase preSaveContestCase(ContestCase ec) {
        ExamContestSession exam = ec.getExamination();
        List<ContestCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
        List<ContestCaseItemFill> fqs = ec.getFillItems();
        List<ContestCaseItemCase> ccqs = ec.getCaseItems();

        //获取多选题答案
        for (ContestCaseItemMultiChoice c : mcqs) {
            //List<ExamMultiChoice> ls = c.getQuestion().getChoices();
            StringBuilder sb = new StringBuilder();
            for (String l : c.getSelectedLabels()) {
                sb.append(l);

            }
            char[] cs = sb.toString().toUpperCase().toCharArray();
            List lss = new LinkedList();
            for (char c1 : cs) {
                lss.add(c1);
            }
            Collections.sort(lss);
            Object[] css = lss.toArray();
            StringBuilder sb2 = new StringBuilder();
            for (Object oc : css) {
                sb2.append(oc.toString());
            }
            String s = sb2.toString();
            c.setUserAnswer(s);
        }
        //获取填空题答案
        for (ContestCaseItemFill c : fqs) {
            List<ContestCaseItemFillBlock> ls = c.getBlocks();
            StringBuilder us = new StringBuilder();
            for (ContestCaseItemFillBlock e : ls) {
                String s = e.getUserAnswer();
                if (s != null) {
                    us.append(s.trim());
                }
                us.append(",");
            }
            c.setUserAnswerStr(us.toString());
        }
        //
        for (ContestCaseItemCase cc : ccqs) {
            List<ContestCaseItemMultiChoice> mcqs2 = cc.getMultiChoiceItems();
            List<ContestCaseItemFill> fqs2 = cc.getFillItems();

            //获取综合题中的多选题答案
            for (ContestCaseItemMultiChoice c : mcqs2) {
                //List<ExamMultiChoice> ls = c.getQuestion().getChoices();
                StringBuilder sb = new StringBuilder();
                for (String l : c.getSelectedLabels()) {
                    sb.append(l);

                }
                char[] cs = sb.toString().toUpperCase().toCharArray();
                List lss = new LinkedList();
                for (char c1 : cs) {
                    lss.add(c1);
                }
                Collections.sort(lss);
                Object[] css = lss.toArray();
                StringBuilder sb2 = new StringBuilder();
                for (Object oc : css) {
                    sb2.append(oc.toString());
                }
                String s = sb2.toString();
                c.setUserAnswer(s);
            }
            //获取综合题中的填空题答案
            for (ContestCaseItemFill c : fqs2) {
                List<ContestCaseItemFillBlock> ls = c.getBlocks();
                StringBuilder us = new StringBuilder();
                for (ContestCaseItemFillBlock e : ls) {
                    String s = e.getUserAnswer();
                    if (s != null) {
                        us.append(s.trim());
                    }
                    us.append(",");
                }
                c.setUserAnswerStr(us.toString());
            }
        }

        return ec;
    }

    public ContestCase resumeContestCase(ContestCase ec) {
        ExamContestSession exam = ec.getExamination();
        List<ContestCaseItemChoice> cqs = ec.getChoiceItems();
        List<ContestCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
        List<ContestCaseItemFill> fqs = ec.getFillItems();
        List<ContestCaseItemJudge> jqs = ec.getJudgeItems();
        List<ContestCaseItemEssay> eqs = ec.getEssayItems();
        List<ContestCaseItemFile> ffqs = ec.getFileItems();
        List<ContestCaseItemCase> ccqs = ec.getCaseItems();

        //恢复多选题，多选题中涉及复选框的选中，需要人工处理
        for (ContestCaseItemMultiChoice c : mcqs) {
            List<ExamMultiChoice> ls = c.getQuestion().getChoices();
            String ua = c.getUserAnswer();
            char[] uac = ua.toCharArray();
            List<String> labels = new ArrayList();
            if (uac != null) {
                for (char u : uac) {
                    labels.add(String.valueOf(u));
                }
            }
            c.setSelectedLabels(labels);
        }
        //恢复填空题，填空题涉及各个空中填写的内容的恢复
        for (ContestCaseItemFill c : fqs) {
            List<ContestCaseItemFillBlock> ls = c.getBlocks();
            if (c.getUserAnswerStr() != null) {
                String ss[] = c.getUserAnswerStr().split(",");
                int i = 0;
                for (ContestCaseItemFillBlock e : ls) {
                    String s = null;
                    try {
                        s = ss[i];
                    } catch (Exception ex) {
                    }
                    if (s != null) {
                        e.setUserAnswer(ss[i]);
                    }
                    i++;
                }
            }

        }

        for (ContestCaseItemCase cc : ccqs) {
            List<ContestCaseItemChoice> cqs2 = cc.getChoiceItems();
            List<ContestCaseItemMultiChoice> mcqs2 = cc.getMultiChoiceItems();
            List<ContestCaseItemFill> fqs2 = cc.getFillItems();
            //恢复综合题中的单选题，主要是恢复选项的随机
            for (ContestCaseItemChoice c : cqs2) {
                List<ExamChoice> ls = c.getQuestion().getChoices();
                if (ec.getExamination().isChoiceRandom() && "saved".equals(ec.getStat())) {
                    this.choiceOrderAdapter(ls);
                }
            }
            //恢复综合题中的多选题
            for (ContestCaseItemMultiChoice c : mcqs2) {
                List<ExamMultiChoice> ls = c.getQuestion().getChoices();
                String ua = c.getUserAnswer();
                if (ua != null) {
                    for (ExamMultiChoice e : ls) {
                        if (ua.toLowerCase().contains(e.getLabel().toLowerCase())) {
                            e.setSelected(true);
                        }
                    }
                }
                if (ec.getExamination().isMultiChoiceRandom() && "saved".equals(ec.getStat())) {
                    this.multiChoiceOrderAdapter(ls);
                }
            }
            //恢复综合题中的填空题
            for (ContestCaseItemFill c : fqs2) {
                List<ContestCaseItemFillBlock> ls = c.getBlocks();
                if (c.getUserAnswerStr() != null) {
                    String ss[] = c.getUserAnswerStr().split(",");
                    int i = 0;
                    for (ContestCaseItemFillBlock e : ls) {
                        String s = null;
                        try {
                            s = ss[i];
                        } catch (Exception ex) {
                        }
                        if (s != null) {
                            e.setUserAnswer(ss[i]);
                        }
                        i++;
                    }
                }

            }

        }

        List<ExamPaperManualPart> parts = exam.getManualPaper().getParts();
        int uniOrd = 0;//对全部试题统一标号
        //Collections.sort(parts);
        for (ExamPaperManualPart part : parts) {
            List<ManualPartItem> items = part.getItems();
            Collections.sort(items);
            List<ContestCaseItemAdapter> adapters = new ArrayList();
            for (ManualPartItem item : items) {
                uniOrd++;
                ContestCaseItemAdapter adapter = new ContestCaseItemAdapter();
                adapter.setOrd(uniOrd);//统一标号
                if ("choice".equals(item.getQtype())) {//判断试卷条目的类型
                    for (ContestCaseItemChoice ei : cqs) {
                        if (ei.getQuestion().getId().equals(item.getQuestionId())) {
                            List<ExamChoice> ecs = ei.getQuestion().getChoices();
                            if (ec.getExamination().isChoiceRandom() && "saved".equals(ec.getStat())) {
                                this.choiceOrderAdapter(ecs);
                            }
                            ei.getQuestion().setChoices(ecs);

                            adapter.setChoiceItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                            adapter.setItem(ei);
                            adapter.setQtype("choice");
                            adapter.setScore(item.getQuestionScore());
                            adapter.setOrd(item.getOrd());
                            break;
                        }
                    }
                } else if ("mchoice".equals(item.getQtype())) {
                    for (ContestCaseItemMultiChoice ei : mcqs) {
                        if (ei.getQuestion().getId().equals(item.getQuestionId())) {
                            List<ExamMultiChoice> ecs = ei.getQuestion().getChoices();
                            if (ec.getExamination().isChoiceRandom() && "saved".equals(ec.getStat())) {
                                this.multiChoiceOrderAdapter(ecs);
                            }
                            ei.getQuestion().setChoices(ecs);
                            adapter.setMultiChoiceItem(ei);
                            adapter.setItem(ei);
                            adapter.setQtype("mchoice");
                            adapter.setScore(item.getQuestionScore());
                            adapter.setOrd(item.getOrd());
                            break;
                        }
                    }
                } else if ("fill".equals(item.getQtype())) {
                    for (ContestCaseItemFill ei : fqs) {
                        if (ei.getQuestion().getId().equals(item.getQuestionId())) {
                            adapter.setFillItem(ei);
                            adapter.setItem(ei);
                            adapter.setQtype("fill");
                            adapter.setScore(item.getQuestionScore());
                            adapter.setOrd(item.getOrd());
                            break;
                        }
                    }
                } else if ("judge".equals(item.getQtype())) {
                    for (ContestCaseItemJudge ei : jqs) {
                        if (ei.getQuestion().getId().equals(item.getQuestionId())) {
                            adapter.setJudgeItem(ei);
                            adapter.setItem(ei);
                            adapter.setQtype("judge");
                            adapter.setScore(item.getQuestionScore());
                            adapter.setOrd(item.getOrd());
                            break;
                        }
                    }
                } else if ("essay".equals(item.getQtype())) {
                    for (ContestCaseItemEssay ei : eqs) {
                        if (ei.getQuestion().getId().equals(item.getQuestionId())) {
                            adapter.setEssayItem(ei);
                            adapter.setItem(ei);
                            adapter.setQtype("essay");
                            adapter.setScore(item.getQuestionScore());
                            adapter.setOrd(item.getOrd());
                            break;
                        }
                    }
                } else if ("file".equals(item.getQtype())) {
                    for (ContestCaseItemFile ei : ffqs) {
                        if (ei.getQuestion().getId().equals(item.getQuestionId())) {
                            adapter.setFileItem(ei);
                            adapter.setItem(ei);
                            adapter.setQtype("file");
                            adapter.setScore(item.getQuestionScore());
                            adapter.setOrd(item.getOrd());
                            break;
                        }
                    }
                } else if ("case".equals(item.getQtype())) {
                    for (ContestCaseItemCase ei : ccqs) {
                        if (ei.getQuestion().getId().equals(item.getQuestionId())) {
                            adapter.setCaseItem(ei);
                            adapter.setItem(ei);
                            adapter.setQtype("case");
                            CaseQuestion cq = (CaseQuestion) item.getQuestion();
                            adapter.setScore(cq.getTotalScore());
                            adapter.setOrd(item.getOrd());
                            break;
                        }
                    }
                }
                adapters.add(adapter);
            }
            part.setCadapters(adapters);
        }
        ec.setParts(parts);//将装载好的parts加入考试中
        return ec;
    }

    public double computeTotalScore(ContestCase ec) {
        ExamContestSession exam = ec.getExamination();
        double totalScore = 0;
        double choiceScore = 0;
        double multiChoiceScore = 0;
        double fillScore = 0;
        double judgeScore = 0;
        double essayScore = 0;
        double fileScore = 0;
        double caseScore = 0;
        List<ContestCaseItemChoice> cqs = ec.getChoiceItems();
        List<ContestCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
        List<ContestCaseItemFill> fqs = ec.getFillItems();
        List<ContestCaseItemJudge> jqs = ec.getJudgeItems();
        List<ContestCaseItemEssay> eqs = ec.getEssayItems();
        List<ContestCaseItemFile> ffqs = ec.getFileItems();
        List<ContestCaseItemCase> ccqs = ec.getCaseItems();

        List<ExamPaperManualPart> parts = ec.getParts();

        //Collections.sort(parts);
        for (ExamPaperManualPart part : parts) {
            //List<ManualPartItem> items = part.getItems();
            List<ContestCaseItemAdapter> adapters = part.getCadapters();
            for (ContestCaseItemAdapter adapter : adapters) {
                if ("choice".equals(adapter.getQtype())) {//判断试卷条目的类型
                    ContestCaseItemChoice ei = adapter.getChoiceItem();//获取考试条目
                    if (ei.getIfRight()) {
                        choiceScore += ei.getScore();
                    }

                } else if ("mchoice".equals(adapter.getQtype())) {
                    ContestCaseItemMultiChoice c = (ContestCaseItemMultiChoice) adapter.getMultiChoiceItem();
                    if (c.getIfRight()) {
                        multiChoiceScore += c.getScore();
                    }

                } else if ("fill".equals(adapter.getQtype())) {
                    ContestCaseItemFill c = (ContestCaseItemFill) adapter.getFillItem();
                    int t = c.getTotalNum();
                    if (t != 0) {
                        double dt = ((double) c.getRightNum()) / ((double) c.getTotalNum());
                        if (dt > 1) {
                            dt = 1D;
                        }
                        fillScore += c.getScore() * dt;
                    }

                } else if ("judge".equals(adapter.getQtype())) {
                    ContestCaseItemJudge c = (ContestCaseItemJudge) adapter.getJudgeItem();
                    if (c.getIfRight()) {
                        judgeScore += c.getScore();
                    }

                } else if ("essay".equals(adapter.getQtype())) {
                    ContestCaseItemEssay c = (ContestCaseItemEssay) adapter.getEssayItem();
                    float ratio = c.getRightRatio();
                    if (ratio > 1) {
                        ratio = 1;
                    }
                    essayScore += c.getScore() * ratio;

                } else if ("file".equals(adapter.getQtype())) {
                    ContestCaseItemFile c = (ContestCaseItemFile) adapter.getFileItem();
                    float ratio = c.getRightRatio();
                    if (ratio > 1) {
                        ratio = 1;
                    }
                    fileScore += c.getScore() * ratio;

                } else if ("case".equals(adapter.getQtype())) {
                    ContestCaseItemCase cic = (ContestCaseItemCase) adapter.getCaseItem();
                    //计算综合题中的选择题得分
                    //计算综合题中的选择题得分
                    double choiceScoreTemp = 0;
                    double multiChoiceScoreTemp = 0;
                    double fillScoreTemp = 0;
                    double judgeScoreTemp = 0;
                    double essayScoreTemp = 0;
                    List<ContestCaseItemChoice> cqi = cic.getChoiceItems();
                    for (ContestCaseItemChoice c : cqi) {
                        if (c.getIfRight()) {
                            //综合题中选择题的评分标准计算
                            caseScore += cic.getQuestion().getChoiceScore();
                            choiceScoreTemp += cic.getQuestion().getChoiceScore();
                        }
                    }

                    //计算多选题成绩
                    List<ContestCaseItemMultiChoice> mqi = cic.getMultiChoiceItems();
                    for (ContestCaseItemMultiChoice c : mqi) {
                        if (c.getIfRight()) {
                            multiChoiceScoreTemp += cic.getQuestion().getMultiChoiceScore();
                            caseScore += cic.getQuestion().getMultiChoiceScore();
                        }
                    }
                    //计算填空题成绩
                    List<ContestCaseItemFill> fqi = cic.getFillItems();
                    for (ContestCaseItemFill c : fqi) {
                        int t = c.getTotalNum();
                        if (t != 0) {
                            double dt = ((double) c.getRightNum()) / ((double) c.getTotalNum());
                            if (dt > 1) {
                                dt = 1D;
                            }
                            fillScoreTemp += cic.getQuestion().getFillScore() * dt;
                            caseScore += cic.getQuestion().getFillScore() * dt;
                        }
                    }
                    //计算判断题成绩
                    List<ContestCaseItemJudge> jqi = cic.getJudgeItems();
                    for (ContestCaseItemJudge c : jqi) {
                        if (c.getIfRight()) {
                            caseScore += cic.getQuestion().getJudgeScore();
                            judgeScoreTemp += cic.getQuestion().getJudgeScore();
                        }
                    }

                    //计算综合题中每道问答题得分
                    List<ContestCaseItemEssay> eqi = cic.getEssayItems();
                    for (ContestCaseItemEssay c : eqi) {
                        float ratio = c.getRightRatio();
                        if (ratio > 1) {
                            ratio = 1;
                        }
                        //综合题中问答题的评分标准计算
                        caseScore += cic.getQuestion().getEssayScore() * ratio;
                        essayScoreTemp += cic.getQuestion().getEssayScore() * ratio;
                    }
                    cic.setChoiceScore(choiceScoreTemp);//综合题中的选择题得分
                    cic.setMultiChoiceScore(multiChoiceScoreTemp);
                    cic.setFillScore(fillScoreTemp);
                    cic.setJudgeScore(judgeScoreTemp);
                    cic.setEssayScore(essayScoreTemp);//综合题中的问答题得分

                }
            }
        }

        totalScore = choiceScore + fillScore + judgeScore + multiChoiceScore + essayScore + fileScore + caseScore;
        //double ratio = totalScore / ec.getExamContestTotalScore();
        //long bbsScore = (long) (ec.getExamContest().getBbsScore() * ratio);
        ec.setChoiceScore(choiceScore);
        ec.setMultiChoiceScore(multiChoiceScore);
        ec.setFillScore(fillScore);
        ec.setJudgeScore(judgeScore);
        ec.setEssayScore(essayScore);
        ec.setFileScore(fileScore);
        ec.setCaseScore(caseScore);
        ec.setScore(totalScore);
        long oldScore = ec.getBbsScore();
        ec.setBbsScore(ec.getNewBbsScore());

        //重新改成绩后重新计算用户积分
        IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
        BbsUser bu = ec.getUser();
        //bu.setScore(bu.getScore() + ec.getNewBbsScore() - oldScore);
        IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
        bsl.log("管理员重新计算考试得分", (int) (ec.getNewBbsScore() - oldScore), bu);
        //userDAO.updateBbsUser(bu);
        return totalScore;
    }

    public Boolean checkIfSupplementaryExamContest(String examId, String userId) {
        List<ContestCase> cases = this.examCaseDAO.findContestCaseByContestAndUser(examId, userId);
        if (cases == null) {
            return false;
        }
        boolean ifUnPass = false;
        boolean ifPass = false;
        int unPassTimes = 0;
        int passTimes = 0;
        int totalTimes = 0;
        for (ContestCase ec : cases) {
            double ratio = ec.getScore() / ec.getTotalFullScore();
            if (ratio < 0.6D) {
                ifUnPass = true;
                unPassTimes++;
                totalTimes++;
            } else {
                ifPass = true;
                passTimes++;
                totalTimes++;
            }

        }
        //检测是否为补考的规则为：考试次数大于考试通过次数
        System.out.println(totalTimes + "" + passTimes);
        if (totalTimes > passTimes) {
            return true;
        } else {
            return false;
        }
    }

    public ExamRoom confirmExamRoom(String ip) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Boolean compareIp(String o, String target) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void main(String args[]) {
    }
}
