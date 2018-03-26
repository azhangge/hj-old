package com.hjedu.examination.service.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.ICaseQuestionDAO;
import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IEssayQuestionDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.dao.IFillQuestionDAO;
import com.hjedu.examination.dao.IJudgeQuestionDAO;
import com.hjedu.examination.dao.IModuleExamCaseDAO;
import com.hjedu.examination.dao.IModuleExamInfoDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.entity.CaseQuestion;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.EssayQuestion;
import com.hjedu.examination.entity.ExamChoice;
import com.hjedu.examination.entity.ExamMultiChoice;
import com.hjedu.examination.entity.FileQuestion;
import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.hjedu.examination.entity.module.ModuleExamCase;
import com.hjedu.examination.entity.module.ModuleExamCaseItemCase;
import com.hjedu.examination.entity.module.ModuleExamCaseItemChoice;
import com.hjedu.examination.entity.module.ModuleExamCaseItemEssay;
import com.hjedu.examination.entity.module.ModuleExamCaseItemFile;
import com.hjedu.examination.entity.module.ModuleExamCaseItemFill;
import com.hjedu.examination.entity.module.ModuleExamCaseItemFillBlock;
import com.hjedu.examination.entity.module.ModuleExamCaseItemJudge;
import com.hjedu.examination.entity.module.ModuleExamCaseItemMultiChoice;
import com.hjedu.examination.entity.module.ModuleExamInfo;
import com.hjedu.examination.entity.module2.ModuleExam2CaseItemAdapter;
import com.hjedu.examination.service.IModuleExamCaseService;
import com.hjedu.examination.util.ExamCaseServiceUtils;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.huajie.util.HTMLCleaner;
import com.huajie.util.MyLogger;
import com.huajie.util.SimilarAlgorithm;
import com.huajie.util.SpringHelper;

public class ModuleExamCaseService implements Serializable, IModuleExamCaseService {

    public String answerB;
    public String answerE;
    IModuleExamCaseDAO examCaseDAO;
    IChoiceQuestionDAO choiceQuestionDAO;
    IMultiChoiceQuestionDAO multiChoiceQuestionDAO;
    IFillQuestionDAO fillQuestionDAO;
    IJudgeQuestionDAO judgeQuestionDAO;
    IEssayQuestionDAO essayQuestionDAO;
    IFileQuestionDAO fileQuestionDAO;
    ICaseQuestionDAO caseQuestionDAO;
    IModuleExamInfoDAO moduleExamInfoDAO;

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

    public IModuleExamCaseDAO getExamCaseDAO() {
        return examCaseDAO;
    }

    public void setExamCaseDAO(IModuleExamCaseDAO examCaseDAO) {
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

    public IModuleExamInfoDAO getModuleExamInfoDAO() {
        return moduleExamInfoDAO;
    }

    public void setModuleExamInfoDAO(IModuleExamInfoDAO moduleExamInfoDAO) {
        this.moduleExamInfoDAO = moduleExamInfoDAO;
    }

    @Override
    public void buildItemFillBlocks(ModuleExamCaseItemFill fill) {
        List<ModuleExamCaseItemFillBlock> blocks = new LinkedList();
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
                ModuleExamCaseItemFillBlock block = new ModuleExamCaseItemFillBlock();
                block.setPreStr(pre);
                ans = ans.replace(answerB, "");
                ans = ans.replace(answerE, "");
                block.setRightAnswer(ans);
                blocks.add(block);

            }
        } catch (Exception eee) {
            System.out.println("出错试题ID：" + fill.getQuestion().getId() + "，内容：" + fill.getQuestion().getName());
        }
        fill.setBlocks(blocks);
        fill.setLastStr(t);
    }

    @Override
    public void buildExamCase(ModuleExamCase ec) {
        ModuleExamInfo exam = this.moduleExamInfoDAO.findModuleExamInfo();
        List<ChoiceQuestion> cqs = new LinkedList();
        List<MultiChoiceQuestion> mcqs = new LinkedList();
        List<FillQuestion> fqs = new LinkedList();
        List<JudgeQuestion> jqs = new LinkedList();
        List<EssayQuestion> eqs = new LinkedList();
        List<FileQuestion> ffqs = new LinkedList();
        List<CaseQuestion> ccqs = new LinkedList();

        //添加随机试卷试题
        if (ec.getExamModule() != null) {
            //随机取出此模块中的一定量单选题
            cqs.addAll(choiceQuestionDAO.getRandomQuestionOfNumInModule(ec.getChoiceTotal(), ec.getExamModule().getId(), cqs));
            //随机取出此模块中的一定量多选题
            mcqs.addAll(multiChoiceQuestionDAO.getRandomQuestionOfNumInModule(ec.getMultiChoiceTotal(), ec.getExamModule().getId()));
            //随机取出此模块中的一定量填空题
            fqs.addAll(fillQuestionDAO.getRandomQuestionOfNumInModule(ec.getFillTotal(), ec.getExamModule().getId()));
            //随机取出此模块中的一定量判断题
            jqs.addAll(judgeQuestionDAO.getRandomQuestionOfNumInModule(ec.getJudgeTotal(), ec.getExamModule().getId()));
            //随机取出此模块中的一定量问答题
            eqs.addAll(essayQuestionDAO.getRandomQuestionOfNumInModule(ec.getEssayTotal(), ec.getExamModule().getId()));
            //随机取出此模块中的一定量文件题
            ffqs.addAll(fileQuestionDAO.getRandomQuestionOfNumInModule(ec.getFileTotal(), ec.getExamModule().getId()));
            //随机取出此模块中的一定量综合题
            ccqs.addAll(caseQuestionDAO.getRandomQuestionOfNumInModule(ec.getCaseTotal(), ec.getExamModule().getId()));
        }

        //对试题顺序排序
        if (exam.isIfRandom()) {
            Collections.shuffle(cqs);
            Collections.shuffle(mcqs);
            Collections.shuffle(fqs);
            Collections.shuffle(jqs);
            Collections.shuffle(eqs);
            Collections.shuffle(ffqs);
            Collections.shuffle(ccqs);
        } else {
            Collections.sort(cqs);
            Collections.sort(mcqs);
            Collections.sort(fqs);
            Collections.sort(jqs);
            Collections.sort(eqs);
            Collections.sort(ffqs);
            Collections.sort(ccqs);
        }

        //将单选试题加入ExamCase中
        //System.out.println("试卷中单选数量：" + cqs.size());
        List<ModuleExamCaseItemChoice> ecics = new LinkedList();
        for (ChoiceQuestion c : cqs) {
            ModuleExamCaseItemChoice ecic = new ModuleExamCaseItemChoice();
            ecic.setExamCase(ec);
            List<ExamChoice> ecs = c.getChoices();
            //Collections.sort(ecs);
            //配置单选题选项随机
            if (exam.isChoiceRandom()&&c.isAllowChoiceRandom()) {
                this.choiceOrderAdapter(ecs);
            }
            c.setChoices(ecs);
            ecic.setQuestion(c);
            ecics.add(ecic);
        }
        ec.setChoiceItems(ecics);

        //将多选试题加入ExamCase中
        List<ModuleExamCaseItemMultiChoice> emcics = new LinkedList();
        for (MultiChoiceQuestion c : mcqs) {
            ModuleExamCaseItemMultiChoice ecic = new ModuleExamCaseItemMultiChoice();
            ecic.setExamCase(ec);
            List<ExamMultiChoice> ecs = c.getChoices();
            //Collections.sort(ecs);
            //配置多选题选项随机
            if (exam.isMultiChoiceRandom()&&c.isAllowChoiceRandom()) {
                this.multiChoiceOrderAdapter(ecs);
            }
            c.setChoices(ecs);
            ecic.setQuestion(c);
            emcics.add(ecic);
        }
        ec.setMultiChoiceItems(emcics);
        //将填空试题加入ExamCase中
        List<ModuleExamCaseItemFill> ecifs = new LinkedList();
        for (FillQuestion f : fqs) {
            ModuleExamCaseItemFill ecif = new ModuleExamCaseItemFill();
            ecif.setExamCase(ec);
            ecif.setQuestion(f);
            ecifs.add(ecif);
        }
        ec.setFillItems(ecifs);
        //将判断试题加入ExamCase中
        List<ModuleExamCaseItemJudge> ecijs = new LinkedList();
        for (JudgeQuestion j : jqs) {
            ModuleExamCaseItemJudge ecij = new ModuleExamCaseItemJudge();
            ecij.setExamCase(ec);
            ecij.setQuestion(j);
            ecijs.add(ecij);
        }
        ec.setJudgeItems(ecijs);
        //将问答试题加入ExamCase中
        List<ModuleExamCaseItemEssay> ecies = new LinkedList();
        for (EssayQuestion j : eqs) {
            ModuleExamCaseItemEssay ecie = new ModuleExamCaseItemEssay();
            ecie.setExamCase(ec);
            ecie.setQuestion(j);
            ecies.add(ecie);
        }
        ec.setEssayItems(ecies);
        //将文件试题加入ExamCase中
        List<ModuleExamCaseItemFile> eciffs = new LinkedList();
        for (FileQuestion j : ffqs) {
            ModuleExamCaseItemFile ecie = new ModuleExamCaseItemFile();
            ecie.setExamCase(ec);
            ecie.setQuestion(j);
            eciffs.add(ecie);
        }
        ec.setFileItems(eciffs);
        //将综合题加入ExamCase中
        List<ModuleExamCaseItemCase> eciccs = new LinkedList();
        for (CaseQuestion j : ccqs) {
            ModuleExamCaseItemCase ecie = new ModuleExamCaseItemCase();
            ecie.setExamCase(ec);
            ecie.setQuestion(j);
            //将选择题加入综合题中
            List<ModuleExamCaseItemChoice> ecicqcs = new LinkedList();
            List<ChoiceQuestion> cqscc = j.getChoices();
            for (ChoiceQuestion c : cqscc) {
                ModuleExamCaseItemChoice ecic = new ModuleExamCaseItemChoice();
                //ecic.setExamCase(ec);
                ecic.setCaseItem(ecie);
                List<ExamChoice> ecs = c.getChoices();
                //Collections.sort(ecs);
                //配置单选题选项随机
                if (exam.isChoiceRandom()&&c.isAllowChoiceRandom()) {
                    this.choiceOrderAdapter(ecs);
                }
                c.setChoices(ecs);
                ecic.setQuestion(c);
                ecicqcs.add(ecic);
            }
            ecie.setChoiceItems(ecicqcs);

            //将多选题加入综合题中
            List<ModuleExamCaseItemMultiChoice> mcieqs = new LinkedList();
            List<MultiChoiceQuestion> mqscc = j.getMultiChoices();
            for (MultiChoiceQuestion c : mqscc) {
                ModuleExamCaseItemMultiChoice eciee = new ModuleExamCaseItemMultiChoice();
                //ecie.setExamCase(ec);
                eciee.setCaseItem(ecie);
                List<ExamMultiChoice> ecs = c.getChoices();
                //Collections.sort(ecs);
                //配置单选题选项随机
                if (exam.isMultiChoiceRandom()&&c.isAllowChoiceRandom()) {
                    this.multiChoiceOrderAdapter(ecs);
                }
                eciee.setQuestion(c);
                mcieqs.add(eciee);
            }
            ecie.setMultiChoiceItems(mcieqs);

            //将填空题加入综合题中
            List<ModuleExamCaseItemFill> fcieqs = new LinkedList();
            List<FillQuestion> fqscc = j.getFills();
            for (FillQuestion c : fqscc) {
                ModuleExamCaseItemFill eciee = new ModuleExamCaseItemFill();
                //ecie.setExamCase(ec);
                eciee.setCaseItem(ecie);
                eciee.setQuestion(c);
                fcieqs.add(eciee);
            }
            ecie.setFillItems(fcieqs);

            //将判断题加入综合题中
            List<ModuleExamCaseItemJudge> jcieqs = new LinkedList();
            List<JudgeQuestion> jqscc = j.getJudges();
            for (JudgeQuestion c : jqscc) {
                ModuleExamCaseItemJudge eciee = new ModuleExamCaseItemJudge();
                //ecie.setExamCase(ec);
                eciee.setCaseItem(ecie);
                eciee.setQuestion(c);
                jcieqs.add(eciee);
            }
            ecie.setJudgeItems(jcieqs);

            //将问答题加入综合题中
            List<ModuleExamCaseItemEssay> ecieqs = new LinkedList();
            List<EssayQuestion> eqscc = j.getEssaies();
            for (EssayQuestion c : eqscc) {
                ModuleExamCaseItemEssay eciee = new ModuleExamCaseItemEssay();
                //ecie.setExamCase(ec);
                eciee.setCaseItem(ecie);
                eciee.setQuestion(c);
                ecieqs.add(eciee);
            }
            ecie.setEssayItems(ecieqs);
            //向临时变量中加入综合题
            eciccs.add(ecie);
        }
        ec.setCaseItems(eciccs);
    }

    private void choiceOrderAdapter(List<ExamChoice> choices) {
        ExamCaseServiceUtils.choiceOrderAdapter(choices);
    }

    private void multiChoiceOrderAdapter(List<ExamMultiChoice> choices) {
        ExamCaseServiceUtils.multiChoiceOrderAdapter(choices);
    }

    @Override
    public ModuleExamCase resumeExamCase(ModuleExamCase ec) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ModuleExam2CaseItemAdapter computeSingleAdapter(ModuleExam2CaseItemAdapter adapter) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ModuleExamCase computeExamCase(ModuleExamCase ec) {
        ModuleExamInfo exam = this.moduleExamInfoDAO.findModuleExamInfo();
        double totalScore = 0;
        double choiceScore = 0;
        double multiChoiceScore = 0;
        double fillScore = 0;
        double judgeScore = 0;
        double essayScore = 0;
        double fileScore = 0;
        double caseScore = 0;
        List<ModuleExamCaseItemChoice> cqs = ec.getChoiceItems();
        List<ModuleExamCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
        List<ModuleExamCaseItemFill> fqs = ec.getFillItems();
        List<ModuleExamCaseItemJudge> jqs = ec.getJudgeItems();
        List<ModuleExamCaseItemEssay> eqs = ec.getEssayItems();
        List<ModuleExamCaseItemFile> ffqs = ec.getFileItems();
        List<ModuleExamCaseItemCase> ccqs = ec.getCaseItems();
        //计算每道单选题是否正确
        for (ModuleExamCaseItemChoice c : cqs) {
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
                choiceScore += exam.getChoiceScore();
            }
        }
        //计算每道多选题是否正确
        for (ModuleExamCaseItemMultiChoice c : mcqs) {
            List<ExamMultiChoice> ls = c.getQuestion().getChoices();
            StringBuilder sb = new StringBuilder();
            for (ExamMultiChoice e : ls) {
                if (e.isSelected()) {
                    sb.append(e.getLabel());
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
                multiChoiceScore += exam.getMultiChoiceScore();
            }
        }
        //计算每道填空题是否正确
        for (ModuleExamCaseItemFill c : fqs) {
            List<ModuleExamCaseItemFillBlock> ls = c.getBlocks();
            StringBuilder as = new StringBuilder();
            StringBuilder us = new StringBuilder();
            int rightNum = 0;
            for (ModuleExamCaseItemFillBlock e : ls) {
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
                fillScore += exam.getFillScore() * dt;
            }
        }
        //计算每道判断题是否正确
        for (ModuleExamCaseItemJudge c : jqs) {
            c.setRightAnswer(String.valueOf(c.getQuestion().getAnswer()));
            c.setIfRight(c.getRightAnswer().equals(c.getUserAnswer()));
            if (c.getIfRight()) {
                judgeScore += exam.getJudgeScore();
            }
        }
        //计算每道问答题得分
        for (ModuleExamCaseItemEssay c : eqs) {
            c.setRightAnswer(c.getQuestion().getRightStr());
            float ratio = 0f;
            try {
                ratio = SimilarAlgorithm.getSimilarity(HTMLCleaner.delHTMLTag(c.getQuestion().getAnswer()), HTMLCleaner.delHTMLTag(c.getUserAnswer()));
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            c.setRightRatio(ratio);
            essayScore += exam.getEssayScore() * ratio;
        }

        //计算每道文件题得分
        for (ModuleExamCaseItemFile c : ffqs) {
            c.setRightAnswer(c.getQuestion().getRightStr());
            float ratio = 0f;
            try {
                ratio = SimilarAlgorithm.getSimilarity(HTMLCleaner.delHTMLTag(c.getQuestion().getAnswer()), HTMLCleaner.delHTMLTag(c.getUserAnswer()));
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            c.setRightRatio(ratio);
            fileScore += exam.getFileScore() * ratio;
        }

        //计算每道综合题得分
        for (ModuleExamCaseItemCase cic : ccqs) {
            //计算综合题中的选择题得分
            double choiceScoreTemp = 0;
            double multiChoiceScoreTemp = 0;
            double fillScoreTemp = 0;
            double judgeScoreTemp = 0;
            double essayScoreTemp = 0;
            List<ModuleExamCaseItemChoice> cqi = cic.getChoiceItems();
            for (ModuleExamCaseItemChoice c : cqi) {
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
            List<ModuleExamCaseItemMultiChoice> mqi = cic.getMultiChoiceItems();
            for (ModuleExamCaseItemMultiChoice c : mqi) {
                List<ExamMultiChoice> ls = c.getQuestion().getChoices();
                StringBuilder sb = new StringBuilder();
                for (ExamMultiChoice e : ls) {
                    if (e.isSelected()) {
                        sb.append(e.getLabel());
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
            List<ModuleExamCaseItemFill> fqi = cic.getFillItems();
            for (ModuleExamCaseItemFill c : fqi) {
                List<ModuleExamCaseItemFillBlock> ls = c.getBlocks();
                StringBuilder as = new StringBuilder();
                StringBuilder us = new StringBuilder();
                int rightNum = 0;
                for (ModuleExamCaseItemFillBlock e : ls) {
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
            List<ModuleExamCaseItemJudge> jqi = cic.getJudgeItems();
            for (ModuleExamCaseItemJudge c : jqi) {
                c.setRightAnswer(String.valueOf(c.getQuestion().getAnswer()));
                c.setIfRight(c.getRightAnswer().equals(c.getUserAnswer()));
                if (c.getIfRight()) {
                    judgeScoreTemp += cic.getQuestion().getJudgeScore();
                    caseScore += cic.getQuestion().getJudgeScore();
                }
            }

            //计算综合题中每道问答题得分
            List<ModuleExamCaseItemEssay> eqi = cic.getEssayItems();
            for (ModuleExamCaseItemEssay c : eqi) {
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

        totalScore = choiceScore + fillScore + judgeScore + multiChoiceScore + essayScore + fileScore + caseScore;

        double ratio = totalScore / ec.getTotalFullScore();
        if (ratio < 0) {
            ratio = 0;
        }
        if (ratio > 1) {
            ratio = 1;
        }
        long bbsScore = 0L;
        bbsScore = (long) (exam.getBbsScore() * ratio);//章节随机练习获得积分

        ec.setChoiceScore(choiceScore);
        ec.setMultiChoiceScore(multiChoiceScore);
        ec.setFillScore(fillScore);
        ec.setJudgeScore(judgeScore);
        ec.setEssayScore(essayScore);
        ec.setFileScore(fileScore);
        ec.setCaseScore(caseScore);
        ec.setScore(totalScore);
        //ec.setBbsScore(bbsScore);
        return ec;
    }

    @Override
    public ModuleExamCase preSaveExamCase(ModuleExamCase ec) {
        ModuleExamInfo exam = this.moduleExamInfoDAO.findModuleExamInfo();
        List<ModuleExamCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
        List<ModuleExamCaseItemFill> fqs = ec.getFillItems();
        List<ModuleExamCaseItemCase> ccqs = ec.getCaseItems();

        //获取多选题答案
        for (ModuleExamCaseItemMultiChoice c : mcqs) {
            List<ExamMultiChoice> ls = c.getQuestion().getChoices();
            StringBuilder sb = new StringBuilder();
            for (ExamMultiChoice e : ls) {
                if (e.isSelected()) {
                    sb.append(e.getLabel());
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
        }
        //获取填空题答案
        for (ModuleExamCaseItemFill c : fqs) {
            List<ModuleExamCaseItemFillBlock> ls = c.getBlocks();
            StringBuilder us = new StringBuilder();
            for (ModuleExamCaseItemFillBlock e : ls) {
                String s = e.getUserAnswer();
                if (s != null) {
                    us.append(s.trim());
                }
                us.append(",");
            }
            c.setUserAnswerStr(us.toString());
        }
        //
        for (ModuleExamCaseItemCase cc : ccqs) {
            List<ModuleExamCaseItemMultiChoice> mcqs2 = cc.getMultiChoiceItems();
            List<ModuleExamCaseItemFill> fqs2 = cc.getFillItems();

            //获取综合题中的多选题答案
            for (ModuleExamCaseItemMultiChoice c : mcqs2) {
                List<ExamMultiChoice> ls = c.getQuestion().getChoices();
                StringBuilder sb = new StringBuilder();
                for (ExamMultiChoice e : ls) {
                    if (e.isSelected()) {
                        sb.append(e.getLabel());
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
            }
            //获取综合题中的填空题答案
            for (ModuleExamCaseItemFill c : fqs2) {
                List<ModuleExamCaseItemFillBlock> ls = c.getBlocks();
                StringBuilder us = new StringBuilder();
                for (ModuleExamCaseItemFillBlock e : ls) {
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
    public double computeTotalScore(ModuleExamCase ec) {
        ModuleExamInfo exam = this.moduleExamInfoDAO.findModuleExamInfo();
        double totalScore = 0;
        double choiceScore = 0;
        double multiChoiceScore = 0;
        double fillScore = 0;
        double judgeScore = 0;
        double essayScore = 0;
        double fileScore = 0;
        double caseScore = 0;
        List<ModuleExamCaseItemChoice> cqs = ec.getChoiceItems();
        List<ModuleExamCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
        List<ModuleExamCaseItemFill> fqs = ec.getFillItems();
        List<ModuleExamCaseItemJudge> jqs = ec.getJudgeItems();
        List<ModuleExamCaseItemEssay> eqs = ec.getEssayItems();
        List<ModuleExamCaseItemFile> ffqs = ec.getFileItems();
        List<ModuleExamCaseItemCase> ccqs = ec.getCaseItems();
        //计算单选题成绩
        for (ModuleExamCaseItemChoice c : cqs) {

            if (c.getIfRight()) {
                choiceScore += exam.getChoiceScore();
            }
        }
        //计算多选题成绩
        for (ModuleExamCaseItemMultiChoice c : mcqs) {
            if (c.getIfRight()) {
                multiChoiceScore += exam.getMultiChoiceScore();
            }
        }
        //计算填空题成绩
        for (ModuleExamCaseItemFill c : fqs) {
            int t = c.getTotalNum();
            if (t != 0) {
                double dt = ((double) c.getRightNum()) / ((double) c.getTotalNum());
                if (dt > 1) {
                    dt = 1D;
                }
                fillScore += exam.getFillScore() * dt;
            }
        }
        //计算判断题成绩
        for (ModuleExamCaseItemJudge c : jqs) {
            if (c.getIfRight()) {
                judgeScore += exam.getJudgeScore();
            }
        }
        //计算问答题成绩
        for (ModuleExamCaseItemEssay c : eqs) {
            float ratio = c.getRightRatio();
            if (ratio > 1) {
                ratio = 1;
            }
            essayScore += exam.getEssayScore() * ratio;
        }
        //计算文件题成绩
        for (ModuleExamCaseItemFile c : ffqs) {
            float ratio = c.getRightRatio();
            if (ratio > 1) {
                ratio = 1;
            }
            fileScore += exam.getFileScore() * ratio;
        }

        //计算每道综合题得分
        for (ModuleExamCaseItemCase cic : ccqs) {
            //计算综合题中的选择题得分
            double choiceScoreTemp = 0;
            double multiChoiceScoreTemp = 0;
            double fillScoreTemp = 0;
            double judgeScoreTemp = 0;
            double essayScoreTemp = 0;
            List<ModuleExamCaseItemChoice> cqi = cic.getChoiceItems();
            for (ModuleExamCaseItemChoice c : cqi) {
                if (c.getIfRight()) {
                    //综合题中选择题的评分标准计算
                    caseScore += cic.getQuestion().getChoiceScore();
                    choiceScoreTemp += cic.getQuestion().getChoiceScore();
                }
            }

            //计算多选题成绩
            List<ModuleExamCaseItemMultiChoice> mqi = cic.getMultiChoiceItems();
            for (ModuleExamCaseItemMultiChoice c : mqi) {
                if (c.getIfRight()) {
                    multiChoiceScoreTemp += exam.getMultiChoiceScore();
                    caseScore += cic.getQuestion().getMultiChoiceScore();
                }
            }
            //计算填空题成绩
            List<ModuleExamCaseItemFill> fqi = cic.getFillItems();
            for (ModuleExamCaseItemFill c : fqi) {
                int t = c.getTotalNum();
                if (t != 0) {
                    double dt = ((double) c.getRightNum()) / ((double) c.getTotalNum());
                    if (dt > 1) {
                        dt = 1D;
                    }
                    fillScoreTemp += exam.getFillScore() * dt;
                    caseScore += cic.getQuestion().getFillScore() * dt;
                }
            }
            //计算判断题成绩
            List<ModuleExamCaseItemJudge> jqi = cic.getJudgeItems();
            for (ModuleExamCaseItemJudge c : jqi) {
                if (c.getIfRight()) {
                    caseScore += cic.getQuestion().getJudgeScore();
                    judgeScoreTemp += cic.getQuestion().getJudgeScore();
                }
            }

            //计算综合题中每道问答题得分
            List<ModuleExamCaseItemEssay> eqi = cic.getEssayItems();
            for (ModuleExamCaseItemEssay c : eqi) {
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
        FillQuestion fq = new FillQuestion();
        String s = "Java中实现多线程一般使用两种方法，一是[继承Thread类]，二是[实现Runnable接口]。";
        fq.setName(s);
        ModuleExamCaseItemFill fill = new ModuleExamCaseItemFill();
        fill.setQuestion(fq);
        ModuleExamCaseService es = new ModuleExamCaseService();
        es.buildItemFillBlocks(fill);
        List<ModuleExamCaseItemFillBlock> blocks = fill.getBlocks();
        for (ModuleExamCaseItemFillBlock block : blocks) {
            MyLogger.echo(block.getPreStr());
            MyLogger.echo(block.getRightAnswer());
        }
        MyLogger.echo(fill.getLastStr());
    }
}
