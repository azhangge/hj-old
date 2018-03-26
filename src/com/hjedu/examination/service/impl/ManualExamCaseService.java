package com.hjedu.examination.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.entity.CaseQuestion;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.EssayQuestion;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.ExamCaseItemAdapter;
import com.hjedu.examination.entity.ExamCaseItemCase;
import com.hjedu.examination.entity.ExamCaseItemChoice;
import com.hjedu.examination.entity.ExamCaseItemEssay;
import com.hjedu.examination.entity.ExamCaseItemFile;
import com.hjedu.examination.entity.ExamCaseItemFill;
import com.hjedu.examination.entity.ExamCaseItemFillBlock;
import com.hjedu.examination.entity.ExamCaseItemJudge;
import com.hjedu.examination.entity.ExamCaseItemMultiChoice;
import com.hjedu.examination.entity.ExamChoice;
import com.hjedu.examination.entity.ExamMultiChoice;
import com.hjedu.examination.entity.ExamPaperManualPart;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.entity.FileQuestion;
import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.examination.entity.ManualPartItem;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.hjedu.examination.entity.VirtualExamPart;
import com.hjedu.examination.service.IExamCaseService;
import com.hjedu.examination.util.ExamCaseServiceUtils;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.huajie.exam.thread.ExamChoiceStatisticProcessor;
import com.huajie.util.HTMLCleaner;
import com.huajie.util.MyLogger;
import com.huajie.util.SimilarAlgorithm;
import com.huajie.util.SpringHelper;

/**
 * 本类为人工试卷考试的服务类，实现了人工试卷考试需要的各项功能
 *
 * @author www.wbdwl.com
 */
public class ManualExamCaseService extends IExamCaseService implements Serializable {

    ThreadPoolTaskExecutor exec;

    public ThreadPoolTaskExecutor getExec() {
        return exec;
    }

    public void setExec(ThreadPoolTaskExecutor exec) {
        this.exec = exec;
    }

    private List<VirtualExamPart> buildVirtualParts(List<ExamPaperManualPart> parts) {
        List<VirtualExamPart> vparts = new ArrayList();
        List<ExamPaperManualPart> parts2 = new ArrayList();
        parts2.addAll(parts);
        for (ExamPaperManualPart p : parts2) {
            VirtualExamPart vp = new VirtualExamPart();
            //与试卷分段同ID
            vp.setId(p.getId());
            vp.setName(p.getName());
            vp.setDescription(p.getDescription());
            vp.setOrd(p.getOrd());

            vp.setTotalScore(p.getTotalScore());

            vparts.add(vp);
        }
        return vparts;
    }

    @Override
    public void buildExamCase(ExamCase ec) {
        Examination exam = ec.getExamination();
        ec.setChoiceItems(new ArrayList());
        ec.setMultiChoiceItems(new ArrayList());
        ec.setFillItems(new ArrayList());
        ec.setJudgeItems(new ArrayList());
        ec.setEssayItems(new ArrayList());
        ec.setFileItems(new ArrayList());
        ec.setCaseItems(new ArrayList());
        if (exam.getManualPaper() == null) {
            return;
        }
        List<ExamPaperManualPart> parts = exam.getManualPaper().getParts();
        //由试卷分段构建考试的虚拟分段
        List<VirtualExamPart> vparts = this.buildVirtualParts(parts);

        int uniOrd = 0;//对全部试题统一标号
        Collections.sort(parts);//将大题排序
        List<ExamPaperManualPart> parts2 = new ArrayList();
        parts2.addAll(parts);
        for (ExamPaperManualPart part : parts2) {
            List<ManualPartItem> items = part.getItems();
            //按照指定的顺序排序
            if (ec.getExamination().isIfRandom()) {
                //如果考试设置了试题顺序随机，将在人工试卷的大题内将试题随机
                Collections.shuffle(items);
            } else {
                Collections.sort(items);
            }
            List<ManualPartItem> items2 = new ArrayList();
            items2.addAll(items);
            List<ExamCaseItemAdapter> adapters = new ArrayList();
            for (ManualPartItem item : items2) {
                uniOrd++;
                ExamCaseItemAdapter adapter = new ExamCaseItemAdapter();
                adapter.setOrd(uniOrd);//统一标号
                //System.out.println(item.getQtype());
                if ("choice".equals(item.getQtype())) {//判断试卷条目的类型
                    ExamCaseItemChoice ei = new ExamCaseItemChoice();//构建一个考试条目
                    ei.setQuestion((ChoiceQuestion) item.getQuestion());//设置考试条目对应的试题F
                    if (ei.getQuestion() == null) {
                        continue;
                    }
                    List<ExamChoice> ecs = ei.getQuestion().getChoices();
                    if (ec.getExamination().isChoiceRandom() && ei.getQuestion().isAllowChoiceRandom()) {
                        this.choiceOrderAdapter(ecs);
                    }
                    ei.getQuestion().setChoices(ecs);

                    ei.setExamCase(ec);
                    ei.setScore(item.getQuestionScore());
                    adapter.setChoiceItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                    adapter.setQtype("choice");
                    adapter.setQuestion(ei.getQuestion());
                    adapter.setScore(item.getQuestionScore());//设置adapter的分值
                    adapter.setItem(ei);
                    ec.getChoiceItems().add(ei);
                } else if ("mchoice".equals(item.getQtype())) {
                    ExamCaseItemMultiChoice ei = new ExamCaseItemMultiChoice();
                    ei.setQuestion((MultiChoiceQuestion) item.getQuestion());//设置考试条目对应的试题F
                    if (ei.getQuestion() == null) {
                        continue;
                    }
                    ei.setQuestion((MultiChoiceQuestion) item.getQuestion());
                    List<ExamMultiChoice> ecs = ei.getQuestion().getChoices();
                    if (ec.getExamination().isMultiChoiceRandom() && ei.getQuestion().isAllowChoiceRandom()) {
                        this.multiChoiceOrderAdapter(ecs);
                    }
                    ei.getQuestion().setChoices(ecs);

                    ei.setExamCase(ec);
                    ei.setScore(item.getQuestionScore());
                    adapter.setMultiChoiceItem(ei);
                    adapter.setQtype("mchoice");
                    adapter.setQuestion(ei.getQuestion());
                    adapter.setScore(item.getQuestionScore());//设置adapter的分值
                    adapter.setItem(ei);
                    ec.getMultiChoiceItems().add(ei);
                    //MyLogger.println("MultiChoice Found in Manual paper.");
                } else if ("fill".equals(item.getQtype())) {
                    ExamCaseItemFill ei = new ExamCaseItemFill();
                    ei.setQuestion((FillQuestion) item.getQuestion());
                    ei.setExamCase(ec);
                    ei.setScore(item.getQuestionScore());
                    adapter.setFillItem(ei);
                    adapter.setQtype("fill");
                    adapter.setQuestion(ei.getQuestion());
                    adapter.setScore(item.getQuestionScore());//设置adapter的分值
                    adapter.setItem(ei);
                    ec.getFillItems().add(ei);
                } else if ("judge".equals(item.getQtype())) {
                    ExamCaseItemJudge ei = new ExamCaseItemJudge();
                    ei.setQuestion((JudgeQuestion) item.getQuestion());
                    ei.setExamCase(ec);
                    ei.setScore(item.getQuestionScore());
                    adapter.setJudgeItem(ei);
                    adapter.setQtype("judge");
                    adapter.setQuestion(ei.getQuestion());
                    adapter.setScore(item.getQuestionScore());//设置adapter的分值
                    adapter.setItem(ei);
                    ec.getJudgeItems().add(ei);
                } else if ("essay".equals(item.getQtype())) {
                    ExamCaseItemEssay ei = new ExamCaseItemEssay();
                    ei.setQuestion((EssayQuestion) item.getQuestion());
                    ei.setExamCase(ec);
                    ei.setScore(item.getQuestionScore());
                    adapter.setEssayItem(ei);
                    adapter.setQtype("essay");
                    adapter.setQuestion(ei.getQuestion());
                    adapter.setScore(item.getQuestionScore());//设置adapter的分值
                    adapter.setItem(ei);
                    ec.getEssayItems().add(ei);

                } else if ("file".equals(item.getQtype())) {
                    ExamCaseItemFile ei = new ExamCaseItemFile();
                    ei.setQuestion((FileQuestion) item.getQuestion());
                    ei.setExamCase(ec);
                    ei.setScore(item.getQuestionScore());
                    adapter.setFileItem(ei);
                    adapter.setQtype("file");
                    adapter.setQuestion(ei.getQuestion());
                    adapter.setScore(item.getQuestionScore());//设置adapter的分值
                    adapter.setItem(ei);
                    ec.getFileItems().add(ei);
                } else if ("case".equals(item.getQtype())) {

                    CaseQuestion cq = (CaseQuestion) item.getQuestion();
                    ExamCaseItemCase ei = this.buildCaseItem(cq, ec);
                    this.orderCaseItems(ei);
                    adapter.setCaseItem(ei);
                    adapter.setQtype("case");
                    adapter.setQuestion(ei.getQuestion());
                    adapter.setScore(cq.getTotalScore());//设置adapter的分值,综合题的分值为综合题的原分值
                    adapter.setItem(ei);
                    ec.getCaseItems().add(ei);
                }
                adapters.add(adapter);
            }

            //将构建的adapters加入对应的分段
            for (VirtualExamPart p : vparts) {
                if (p.getId().equals(part.getId())) {
                    p.setAdapters(adapters);
                }
            }

        }

        //将大题内的小题排序，恢复考试时，若配置了随机，则按原顺序，若未配置随机，则重新排序
        for (VirtualExamPart part : vparts) {
            if (!exam.isIfRandom()) {
                Collections.sort(part.getAdapters());
            }
        }

        Collections.sort(vparts);

        ec.setVparts(vparts);//将装载好的parts加入考试中
        //计算各大题的得分及总分
        ExamCaseServiceUtils.computePartScore(ec);

    }

    @Override
    public ExamCase computeExamCase(ExamCase ec) {
        double totalScore = 0;//总分
        double choiceScore = 0;//单选题分数
        double multiChoiceScore = 0;//多选题分数
        double fillScore = 0;//填空题分数
        double judgeScore = 0;//判断题分数
        double essayScore = 0;//问答题分数
        double fileScore = 0;
        double caseScore = 0;

        List<VirtualExamPart> parts = ec.getVparts();

        //Collections.sort(parts);
        for (VirtualExamPart part : parts) {
            //List<ManualPartItem> items = part.getItems();
            List<ExamCaseItemAdapter> adapters = part.getAdapters();
            for (ExamCaseItemAdapter adapter : adapters) {
                if ("choice".equals(adapter.getQtype())) {//判断试卷条目的类型
                    ExamCaseItemChoice ei = adapter.getChoiceItem();//获取考试条目
                    if (ei.getQuestion().getAnswer() == null) {
                        continue;
                    }
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
                    if (ei.isIfRight()) {
                        choiceScore += ei.getScore();
                    }

                } else if ("mchoice".equals(adapter.getQtype())) {
                    ExamCaseItemMultiChoice c = (ExamCaseItemMultiChoice) adapter.getMultiChoiceItem();
                    if (c.getQuestion().getAnswer() == null) {
                        continue;
                    }
                    //List<ExamMultiChoice> ls = c.getQuestion().getChoices();
                    StringBuilder sb = new StringBuilder();
//                    for (ExamMultiChoice e : ls) {
//                        if (e.isSelected()) {
//                            sb.append(e.getLabel());
//                        }
//                    }
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
                    if (c.isIfRight()) {
                        multiChoiceScore += c.getScore();
                    }

                } else if ("fill".equals(adapter.getQtype())) {
                    ExamCaseItemFill c = (ExamCaseItemFill) adapter.getFillItem();
                    List<ExamCaseItemFillBlock> ls = c.getBlocks();
                    StringBuilder as = new StringBuilder();
                    StringBuilder us = new StringBuilder();
                    int rightNum = 0;
                    for (ExamCaseItemFillBlock e : ls) {
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
                    ExamCaseItemJudge c = (ExamCaseItemJudge) adapter.getJudgeItem();
                    c.setRightAnswer(String.valueOf(c.getQuestion().getAnswer()));
                    c.setIfRight(c.getRightAnswer().equals(c.getUserAnswer()));
                    if (c.isIfRight()) {
                        judgeScore += c.getScore();
                    }

                } else if ("essay".equals(adapter.getQtype())) {
                    ExamCaseItemEssay c = (ExamCaseItemEssay) adapter.getEssayItem();
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
                    ExamCaseItemFile c = (ExamCaseItemFile) adapter.getFileItem();
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
                    ExamCaseItemCase cic = (ExamCaseItemCase) adapter.getCaseItem();
                    //计算综合题中的选择题得分
                    double choiceScoreTemp = 0;
                    double multiChoiceScoreTemp = 0;
                    double fillScoreTemp = 0;
                    double judgeScoreTemp = 0;
                    double essayScoreTemp = 0;
                    List<ExamCaseItemChoice> cqi = cic.getChoiceItems();
                    for (ExamCaseItemChoice c : cqi) {
                        List<ExamChoice> ls = c.getQuestion().getChoices();
                        for (ExamChoice e : ls) {
                            if (e.isSelected()) {
                                c.setUserAnswer(e.getLabel().trim());
                                break;
                            }
                        }
                        c.setRightAnswer(c.getQuestion().getAnswer().trim());
                        c.setIfRight(c.getRightAnswer().equalsIgnoreCase(c.getUserAnswer()));
                        if (c.isIfRight()) {
                            //综合题中选择题的评分标准计算
                            caseScore += cic.getQuestion().getChoiceScore();
                            choiceScoreTemp += cic.getQuestion().getChoiceScore();
                        }
                    }

                    //计算综合题中每道多选题是否正确
                    List<ExamCaseItemMultiChoice> mqi = cic.getMultiChoiceItems();
                    for (ExamCaseItemMultiChoice c : mqi) {
                        List<ExamMultiChoice> ls = c.getQuestion().getChoices();
                        StringBuilder sb = new StringBuilder();
//                        for (ExamMultiChoice e : ls) {
//                            if (e.isSelected()) {
//                                sb.append(e.getLabel());
//                            }
//                        }
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
                        if (c.isIfRight()) {
                            multiChoiceScoreTemp += cic.getQuestion().getMultiChoiceScore();
                            caseScore += cic.getQuestion().getMultiChoiceScore();
                        }
                    }
                    //计算综合题中每道填空题是否正确
                    List<ExamCaseItemFill> fqi = cic.getFillItems();
                    for (ExamCaseItemFill c : fqi) {
                        List<ExamCaseItemFillBlock> ls = c.getBlocks();
                        StringBuilder as = new StringBuilder();
                        StringBuilder us = new StringBuilder();
                        int rightNum = 0;
                        for (ExamCaseItemFillBlock e : ls) {
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
                    List<ExamCaseItemJudge> jqi = cic.getJudgeItems();
                    for (ExamCaseItemJudge c : jqi) {
                        c.setRightAnswer(String.valueOf(c.getQuestion().getAnswer()));
                        c.setIfRight(c.getRightAnswer().equals(c.getUserAnswer()));
                        if (c.isIfRight()) {
                            judgeScoreTemp += cic.getQuestion().getJudgeScore();
                            caseScore += cic.getQuestion().getJudgeScore();
                        }
                    }

                    //计算综合题中每道问答题得分
                    List<ExamCaseItemEssay> eqi = cic.getEssayItems();
                    for (ExamCaseItemEssay c : eqi) {
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

        //计算各大题的得分及总分
        ExamCaseServiceUtils.computePartScore(ec);

        //启动新线程对作答情况进行统计
        if (ec.getExamination().isAddStatistic()) {
            ExamChoiceStatisticProcessor processor = new ExamChoiceStatisticProcessor(ec);
            exec.execute(processor);
        }
        //记录考试获取的积分
        BbsUser bu = ec.getUser();
        IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
        bsl.log("考试获得积分", bbsScore, bu);

        return ec;
    }

    @Override
    public ExamCase preSaveExamCase(ExamCase ec) {
        Examination exam = ec.getExamination();
        List<ExamCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
        List<ExamCaseItemFill> fqs = ec.getFillItems();
        List<ExamCaseItemCase> ccqs = ec.getCaseItems();

        //获取多选题答案
        for (ExamCaseItemMultiChoice c : mcqs) {
            List<ExamMultiChoice> ls = c.getQuestion().getChoices();
            StringBuilder sb = new StringBuilder();
//            for (ExamMultiChoice e : ls) {
//                if (e.isSelected()) {
//                    sb.append(e.getLabel());
//                }
//            }
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
        for (ExamCaseItemFill c : fqs) {
            List<ExamCaseItemFillBlock> ls = c.getBlocks();
            StringBuilder us = new StringBuilder();
            for (ExamCaseItemFillBlock e : ls) {
                String s = e.getUserAnswer();
                if (s != null) {
                    us.append(s.trim());
                }
                us.append(",");
            }
            c.setUserAnswerStr(us.toString());
        }
        //
        for (ExamCaseItemCase cc : ccqs) {
            List<ExamCaseItemMultiChoice> mcqs2 = cc.getMultiChoiceItems();
            List<ExamCaseItemFill> fqs2 = cc.getFillItems();

            //获取综合题中的多选题答案
            for (ExamCaseItemMultiChoice c : mcqs2) {
                List<ExamMultiChoice> ls = c.getQuestion().getChoices();
                StringBuilder sb = new StringBuilder();
//                for (ExamMultiChoice e : ls) {
//                    if (e.isSelected()) {
//                        sb.append(e.getLabel());
//                    }
//                }
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
            for (ExamCaseItemFill c : fqs2) {
                List<ExamCaseItemFillBlock> ls = c.getBlocks();
                StringBuilder us = new StringBuilder();
                for (ExamCaseItemFillBlock e : ls) {
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

    @Override
    public ExamCase resumeExamCase(ExamCase ec) {
        Examination exam = ec.getExamination();
        List<ExamCaseItemChoice> cqs = ec.getChoiceItems();
        List<ExamCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
        List<ExamCaseItemFill> fqs = ec.getFillItems();
        List<ExamCaseItemJudge> jqs = ec.getJudgeItems();
        List<ExamCaseItemEssay> eqs = ec.getEssayItems();
        List<ExamCaseItemFile> ffqs = ec.getFileItems();
        List<ExamCaseItemCase> ccqs = ec.getCaseItems();

        //恢复多选题，多选题中涉及复选框的选中，需要人工处理
        for (ExamCaseItemMultiChoice c : mcqs) {
            if (c.getQuestion() == null) {
                continue;
            }
            List<ExamMultiChoice> ls = c.getQuestion().getChoices();
            String ua = c.getUserAnswer();

            List<String> labels = new ArrayList();
            if (ua != null) {
                char[] uac = ua.toCharArray();
                if (uac != null) {
                    for (char u : uac) {
                        labels.add(String.valueOf(u));
                    }
                }
            }
            c.setSelectedLabels(labels);
        }
        //恢复填空题，填空题涉及各个空中填写的内容的恢复
        for (ExamCaseItemFill c : fqs) {
            if (c.getQuestion() == null) {
                continue;
            }
            List<ExamCaseItemFillBlock> ls = c.getBlocks();
            if (c.getUserAnswerStr() != null) {
                String ss[] = c.getUserAnswerStr().split(",");
                int i = 0;
                for (ExamCaseItemFillBlock e : ls) {
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

        for (ExamCaseItemCase cc : ccqs) {
            if (cc.getQuestion() == null) {
                continue;
            }
            List<ExamCaseItemChoice> cqs2 = cc.getChoiceItems();
            List<ExamCaseItemMultiChoice> mcqs2 = cc.getMultiChoiceItems();
            List<ExamCaseItemFill> fqs2 = cc.getFillItems();
            //恢复综合题中的单选题，主要是恢复选项的随机
            for (ExamCaseItemChoice c : cqs2) {
                List<ExamChoice> ls = c.getQuestion().getChoices();
                if (ec.getExamination().isChoiceRandom() && c.getQuestion().isAllowChoiceRandom() && "saved".equals(ec.getStat())) {
                    this.choiceOrderAdapter(ls);
                }
            }
            //恢复综合题中的多选题
            for (ExamCaseItemMultiChoice c : mcqs2) {
                if (c.getQuestion() == null) {
                    continue;
                }
                List<ExamMultiChoice> ls = c.getQuestion().getChoices();

                List<String> labels = new ArrayList();
                String ua = c.getUserAnswer();
                if (ua != null) {
                    char[] uac = ua.toCharArray();
                    if (uac != null) {
                        for (char u : uac) {
                            labels.add(String.valueOf(u));
                        }
                    }
                }
                c.setSelectedLabels(labels);
                if (ec.getExamination().isChoiceRandom() && c.getQuestion().isAllowChoiceRandom() && "saved".equals(ec.getStat())) {
                    this.multiChoiceOrderAdapter(ls);
                }
            }
            //恢复综合题中的填空题
            for (ExamCaseItemFill c : fqs2) {
                if (c.getQuestion() == null) {
                    continue;
                }
                List<ExamCaseItemFillBlock> ls = c.getBlocks();
                if (c.getUserAnswerStr() != null) {
                    String ss[] = c.getUserAnswerStr().split(",");
                    int i = 0;
                    for (ExamCaseItemFillBlock e : ls) {
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
        //由试卷分段构建考试的虚拟分段
        List<VirtualExamPart> vparts = this.buildVirtualParts(parts);

        int uniOrd = 0;//对全部试题统一标号
        //Collections.sort(parts);
        for (ExamPaperManualPart part : parts) {
            List<ManualPartItem> items = part.getItems();
            Collections.sort(items);
            List<ExamCaseItemAdapter> adapters = new ArrayList();
            for (ManualPartItem item : items) {
                uniOrd++;
                ExamCaseItemAdapter adapter = new ExamCaseItemAdapter();
                adapter.setOrd(uniOrd);//统一标号

                if ("choice".equals(item.getQtype())) {//判断试卷条目的类型
                    for (ExamCaseItemChoice ei : cqs) {
                        if (ei.getQuestion().getId().equals(item.getQuestionId())) {
                            List<ExamChoice> ecs = ei.getQuestion().getChoices();
                            if (ec.getExamination().isChoiceRandom() && ei.getQuestion().isAllowChoiceRandom() && "saved".equals(ec.getStat())) {
                                this.choiceOrderAdapter(ecs);
                            }
                            ei.getQuestion().setChoices(ecs);

                            adapter.setChoiceItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                            adapter.setItem(ei);
                            adapter.setQtype("choice");
                            adapter.setQuestion(ei.getQuestion());
                            adapter.setScore(item.getQuestionScore());
                            adapter.setOrd(ei.getIndex());//恢复老考试时应按其原来顺序
                            break;
                        }
                    }
                } else if ("mchoice".equals(item.getQtype())) {
                    for (ExamCaseItemMultiChoice ei : mcqs) {
                        if (ei.getQuestion().getId().equals(item.getQuestionId())) {
                            List<ExamMultiChoice> ecs = ei.getQuestion().getChoices();
                            if (ec.getExamination().isChoiceRandom() && ei.getQuestion().isAllowChoiceRandom() && "saved".equals(ec.getStat())) {
                                this.multiChoiceOrderAdapter(ecs);
                            }
                            ei.getQuestion().setChoices(ecs);
                            adapter.setMultiChoiceItem(ei);
                            adapter.setItem(ei);
                            adapter.setQtype("mchoice");
                            adapter.setQuestion(ei.getQuestion());
                            adapter.setScore(item.getQuestionScore());
                            adapter.setOrd(ei.getIndex());//恢复老考试时应按其原来顺序
                            break;
                        }
                    }
                } else if ("fill".equals(item.getQtype())) {
                    for (ExamCaseItemFill ei : fqs) {
                        if (ei.getQuestion().getId().equals(item.getQuestionId())) {
                            adapter.setFillItem(ei);
                            adapter.setItem(ei);
                            adapter.setQtype("fill");
                            adapter.setQuestion(ei.getQuestion());
                            adapter.setScore(item.getQuestionScore());
                            adapter.setOrd(ei.getIndex());//恢复老考试时应按其原来顺序
                            break;
                        }
                    }
                } else if ("judge".equals(item.getQtype())) {
                    for (ExamCaseItemJudge ei : jqs) {
                        if (ei.getQuestion().getId().equals(item.getQuestionId())) {
                            adapter.setJudgeItem(ei);
                            adapter.setItem(ei);
                            adapter.setQtype("judge");
                            adapter.setQuestion(ei.getQuestion());
                            adapter.setScore(item.getQuestionScore());
                            adapter.setOrd(ei.getIndex());//恢复老考试时应按其原来顺序
                            break;
                        }
                    }
                } else if ("essay".equals(item.getQtype())) {
                    for (ExamCaseItemEssay ei : eqs) {
                        if (ei.getQuestion().getId().equals(item.getQuestionId())) {
                            adapter.setEssayItem(ei);
                            adapter.setItem(ei);
                            adapter.setQtype("essay");
                            adapter.setQuestion(ei.getQuestion());
                            adapter.setScore(item.getQuestionScore());
                            adapter.setOrd(ei.getIndex());//恢复老考试时应按其原来顺序
                            break;
                        }
                    }
                } else if ("file".equals(item.getQtype())) {
                    for (ExamCaseItemFile ei : ffqs) {
                        if (ei.getQuestion().getId().equals(item.getQuestionId())) {
                            adapter.setFileItem(ei);
                            adapter.setItem(ei);
                            adapter.setQtype("file");
                            adapter.setQuestion(ei.getQuestion());
                            adapter.setScore(item.getQuestionScore());
                            adapter.setOrd(ei.getIndex());//恢复老考试时应按其原来顺序
                            break;
                        }
                    }
                } else if ("case".equals(item.getQtype())) {
                    for (ExamCaseItemCase ei : ccqs) {
                        if (ei.getQuestion().getId().equals(item.getQuestionId())) {
                            this.orderCaseItems(ei);
                            adapter.setCaseItem(ei);
                            adapter.setItem(ei);
                            adapter.setQtype("case");
                            adapter.setQuestion(ei.getQuestion());
                            CaseQuestion cq = (CaseQuestion) item.getQuestion();
                            adapter.setScore(cq.getTotalScore());
                            adapter.setOrd(ei.getIndex());//恢复老考试时应按其原来顺序
                            break;
                        }
                    }
                }
                //若在此处QUESTION仍为空，说明此试题已经不存在
                if (adapter.getQuestion() == null) {
                    continue;
                }
                adapters.add(adapter);
            }
            //将构建的adapters加入对应的分段
            for (VirtualExamPart p : vparts) {
                if (p.getId().equals(part.getId())) {
                    p.setAdapters(adapters);
                }
            }
        }

        //将大题内的小题排序，恢复考试时，若配置了随机，则按原顺序，若未配置随机，则重新排序
        for (VirtualExamPart part : vparts) {
            Collections.sort(part.getAdapters());

        }

        ec.setVparts(vparts);//将装载好的parts加入考试中

        //计算各大题的得分及总分
        ExamCaseServiceUtils.computePartScore(ec);
        return ec;
    }

    @Override
    public boolean computeSingleAdapter(ExamCaseItemAdapter adapter) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double computeTotalScore(ExamCase ec) {
        Examination exam = ec.getExamination();
        double totalScore = 0;
        double choiceScore = 0;
        double multiChoiceScore = 0;
        double fillScore = 0;
        double judgeScore = 0;
        double essayScore = 0;
        double fileScore = 0;
        double caseScore = 0;
        List<ExamCaseItemChoice> cqs = ec.getChoiceItems();
        List<ExamCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
        List<ExamCaseItemFill> fqs = ec.getFillItems();
        List<ExamCaseItemJudge> jqs = ec.getJudgeItems();
        List<ExamCaseItemEssay> eqs = ec.getEssayItems();
        List<ExamCaseItemFile> ffqs = ec.getFileItems();
        List<ExamCaseItemCase> ccqs = ec.getCaseItems();

        List<VirtualExamPart> parts = ec.getVparts();

        //Collections.sort(parts);
        for (VirtualExamPart part : parts) {
            //List<ManualPartItem> items = part.getItems();
            List<ExamCaseItemAdapter> adapters = part.getAdapters();
            for (ExamCaseItemAdapter adapter : adapters) {
                if ("choice".equals(adapter.getQtype())) {//判断试卷条目的类型
                    ExamCaseItemChoice ei = adapter.getChoiceItem();//获取考试条目
                    if (ei.isIfRight()) {
                        choiceScore += ei.getScore();
                    }

                } else if ("mchoice".equals(adapter.getQtype())) {
                    ExamCaseItemMultiChoice c = (ExamCaseItemMultiChoice) adapter.getMultiChoiceItem();
                    if (c.isIfRight()) {
                        multiChoiceScore += c.getScore();
                    }

                } else if ("fill".equals(adapter.getQtype())) {
                    ExamCaseItemFill c = (ExamCaseItemFill) adapter.getFillItem();
                    int t = c.getTotalNum();
                    if (t != 0) {
                        double dt = ((double) c.getRightNum()) / ((double) c.getTotalNum());
                        if (dt > 1) {
                            dt = 1D;
                        }
                        fillScore += c.getScore() * dt;
                    }

                } else if ("judge".equals(adapter.getQtype())) {
                    ExamCaseItemJudge c = (ExamCaseItemJudge) adapter.getJudgeItem();
                    if (c.isIfRight()) {
                        judgeScore += c.getScore();
                    }

                } else if ("essay".equals(adapter.getQtype())) {
                    ExamCaseItemEssay c = (ExamCaseItemEssay) adapter.getEssayItem();
                    float ratio = c.getRightRatio();
                    if (ratio > 1) {
                        ratio = 1;
                    }
                    essayScore += c.getScore() * ratio;

                } else if ("file".equals(adapter.getQtype())) {
                    ExamCaseItemFile c = (ExamCaseItemFile) adapter.getFileItem();
                    float ratio = c.getRightRatio();
                    if (ratio > 1) {
                        ratio = 1;
                    }
                    fileScore += c.getScore() * ratio;

                } else if ("case".equals(adapter.getQtype())) {
                    ExamCaseItemCase cic = (ExamCaseItemCase) adapter.getCaseItem();
                    //计算综合题中的选择题得分
                    //计算综合题中的选择题得分
                    double choiceScoreTemp = 0;
                    double multiChoiceScoreTemp = 0;
                    double fillScoreTemp = 0;
                    double judgeScoreTemp = 0;
                    double essayScoreTemp = 0;
                    List<ExamCaseItemChoice> cqi = cic.getChoiceItems();
                    for (ExamCaseItemChoice c : cqi) {
                        if (c.isIfRight()) {
                            //综合题中选择题的评分标准计算
                            caseScore += cic.getQuestion().getChoiceScore();
                            choiceScoreTemp += cic.getQuestion().getChoiceScore();
                        }
                    }

                    //计算多选题成绩
                    List<ExamCaseItemMultiChoice> mqi = cic.getMultiChoiceItems();
                    for (ExamCaseItemMultiChoice c : mqi) {
                        if (c.isIfRight()) {
                            multiChoiceScoreTemp += cic.getQuestion().getMultiChoiceScore();
                            caseScore += cic.getQuestion().getMultiChoiceScore();
                        }
                    }
                    //计算填空题成绩
                    List<ExamCaseItemFill> fqi = cic.getFillItems();
                    for (ExamCaseItemFill c : fqi) {
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
                    List<ExamCaseItemJudge> jqi = cic.getJudgeItems();
                    for (ExamCaseItemJudge c : jqi) {
                        if (c.isIfRight()) {
                            caseScore += cic.getQuestion().getJudgeScore();
                            judgeScoreTemp += cic.getQuestion().getJudgeScore();
                        }
                    }

                    //计算综合题中每道问答题得分
                    List<ExamCaseItemEssay> eqi = cic.getEssayItems();
                    for (ExamCaseItemEssay c : eqi) {
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
        //double ratio = totalScore / ec.getExaminationTotalScore();
        //long bbsScore = (long) (ec.getExamination().getBbsScore() * ratio);
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

    public static void main(String args[]) {
        //IExamCaseDAO caseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
        ExaminationService examDAO = SpringHelper.getSpringBean("ExaminationService");
        IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
        IExamCaseService examCaseService = SpringHelper.getSpringBean("ManualExamCaseService");
        BbsUser bu = userDAO.findSysUser();
        String eid = "ef367103-40b5-49a2-890e-3b4417aa059b";

        Examination examt = examDAO.findExamination(eid);
        System.out.println(examt.getName());
        ExamCase ec = new ExamCase();
        ec.setUser(bu);
        ec.setExamination(examt);
        examCaseService.buildExamCase(ec);
        System.exit(0);
    }

}
