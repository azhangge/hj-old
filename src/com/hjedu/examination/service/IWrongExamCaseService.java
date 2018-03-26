package com.hjedu.examination.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.dao.IWrongKnowledgeDAO;
import com.hjedu.examination.dao.IWrongQuestionDAO;
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
import com.hjedu.examination.entity.ExamKnowledge;
import com.hjedu.examination.entity.ExamMultiChoice;
import com.hjedu.examination.entity.ExamPaperRandom;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.entity.FileQuestion;
import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.hjedu.examination.entity.VirtualExamPart;
import com.hjedu.examination.entity.WrongExamScores;
import com.hjedu.examination.entity.WrongTestInfo;
import com.hjedu.examination.util.ExamCaseServiceUtils;
import com.huajie.util.HTMLCleaner;
import com.huajie.util.SimilarAlgorithm;
import com.huajie.util.SpringHelper;

public abstract class IWrongExamCaseService {

    /**
     * 检查错题
     */
    public abstract void checkAllExamCase();

    /**
     * 检查错题
     */
    public abstract void checkExamCaseByExam(String examId);

    /**
     * 检查错题
     */
    public abstract void checkExamCaseByUser(String uid);

    /**
     * 单选题选项随机化 本方法主要供子类内部使用
     *
     * @param choices 单选题选项
     */
    protected void choiceOrderAdapter(List<ExamChoice> choices) {
        ExamCaseServiceUtils.choiceOrderAdapter(choices);
    }

    /**
     * 多选题选项随机化 本方法主要供子类内部使用
     *
     * @param choices 多选题选项
     */
    protected void multiChoiceOrderAdapter(List<ExamMultiChoice> choices) {
        ExamCaseServiceUtils.multiChoiceOrderAdapter(choices);
    }

    /**
     * 装载综合题的各小题，根据综合题中的各小题构建各小题作答实例 本方法主要供子类内部使用
     *
     * @param j 综合题
     * @param ec 考试实例
     * @return 返回装载好的综合题作答实例
     */
    protected ExamCaseItemCase buildCaseItem(CaseQuestion j, ExamCase ec) {

        ExamCaseItemCase ecie = new ExamCaseItemCase();
        ecie.setExamCase(ec);
        ecie.setQuestion(j);
        //将选择题加入综合题中
        List<ExamCaseItemChoice> ecicqcs = new LinkedList();
        List<ChoiceQuestion> cqscc = j.getChoices();
        for (ChoiceQuestion c : cqscc) {
            ExamCaseItemChoice ecic = new ExamCaseItemChoice();
            //ecic.setExamCase(ec);
            ecic.setCaseItem(ecie);
            List<ExamChoice> ecs = c.getChoices();
            //Collections.sort(ecs);
            //配置单选题选项随机
            if (ec.getExamination().isChoiceRandom() && c.isAllowChoiceRandom()) {
                this.choiceOrderAdapter(ecs);
            }
            c.setChoices(ecs);
            ecic.setQuestion(c);
            ecicqcs.add(ecic);
        }
        ecie.setChoiceItems(ecicqcs);

        //将多选题加入综合题中
        List<ExamCaseItemMultiChoice> mcieqs = new LinkedList();
        List<MultiChoiceQuestion> mqscc = j.getMultiChoices();
        for (MultiChoiceQuestion c : mqscc) {
            ExamCaseItemMultiChoice eciee = new ExamCaseItemMultiChoice();
            //ecie.setExamCase(ec);
            eciee.setCaseItem(ecie);
            List<ExamMultiChoice> ecs = c.getChoices();
            //Collections.sort(ecs);
            //配置单选题选项随机
            if (ec.getExamination().isChoiceRandom() && c.isAllowChoiceRandom()) {
                this.multiChoiceOrderAdapter(ecs);
            }
            eciee.setQuestion(c);
            mcieqs.add(eciee);
        }
        ecie.setMultiChoiceItems(mcieqs);

        //将填空题加入综合题中
        List<ExamCaseItemFill> fcieqs = new LinkedList();
        List<FillQuestion> fqscc = j.getFills();
        for (FillQuestion c : fqscc) {
            ExamCaseItemFill eciee = new ExamCaseItemFill();
            //ecie.setExamCase(ec);
            eciee.setCaseItem(ecie);
            eciee.setQuestion(c);
            fcieqs.add(eciee);
        }
        ecie.setFillItems(fcieqs);

        //将判断题加入综合题中
        List<ExamCaseItemJudge> jcieqs = new LinkedList();
        List<JudgeQuestion> jqscc = j.getJudges();
        for (JudgeQuestion c : jqscc) {
            ExamCaseItemJudge eciee = new ExamCaseItemJudge();
            //ecie.setExamCase(ec);
            eciee.setCaseItem(ecie);
            eciee.setQuestion(c);
            jcieqs.add(eciee);
        }
        ecie.setJudgeItems(jcieqs);

        //将问答题加入综合题中
        List<ExamCaseItemEssay> ecieqs = new LinkedList();
        List<EssayQuestion> eqscc = j.getEssaies();
        for (EssayQuestion c : eqscc) {
            ExamCaseItemEssay eciee = new ExamCaseItemEssay();
            //ecie.setExamCase(ec);
            eciee.setCaseItem(ecie);
            eciee.setQuestion(c);
            ecieqs.add(eciee);
        }
        ecie.setEssayItems(ecieqs);
        //向临时变量中加入综合题
        return ecie;
    }

    /**
     * 验证或建立虚拟试卷大题
     *
     * @param pType 大题类型
     * @param vparts 大题列表
     * @param paper 试卷
     * @return
     */
    private void tryToBuildVirtualParts(String pType, List<VirtualExamPart> vparts, ExamPaperRandom paper) {
        boolean exists = false;
        //验证大题是否存在
        for (VirtualExamPart p : vparts) {
            if (p.getId().equals(pType)) {
                exists = true;
                break;
            }
        }
        //大题若不存在，则生成
        if (!exists) {
            VirtualExamPart vp = new VirtualExamPart();
            //与试卷分段同ID
            vp.setId(pType);
            String name = "";
            int ord = 0;

            if ("choice".equals(pType)) {
                name = "单选题";
                ord = 1;
            } else if ("mchoice".equals(pType)) {
                name = "多选题";
                ord = 2;
            } else if ("fill".equals(pType)) {
                name = "填空题";
                ord = 3;
            } else if ("judge".equals(pType)) {
                name = "判断题";
                ord = 4;
            } else if ("essay".equals(pType)) {
                name = "简答题";
                ord = 5;
            } else if ("file".equals(pType)) {
                name = "文件题";
                ord = 6;
            } else if ("case".equals(pType)) {
                name = "综合题";
                ord = 7;
            }

            vp.setName(name);
            vp.setOrd(ord);
            vp.setDescription("");
            vp.setChoiceScore(paper.getChoiceScore());
            vp.setMultiChoiceScore(paper.getMultiChoiceScore());
            vp.setFillScore(paper.getFillScore());
            vp.setJudgeScore(paper.getJudgeScore());
            vp.setEssayScore(paper.getEssayScore());
            vp.setFileScore(paper.getFileScore());

            vparts.add(vp);
        }
    }

    /**
     * 将小题目加入虚拟大题
     *
     * @param adapter 小题适配器
     * @param pType 题目类型
     * @param parts 虚拟大题
     * @param paper 试卷类型
     */
    private void addAdapterForPart(ExamCaseItemAdapter adapter, String pType, List<VirtualExamPart> parts, ExamPaperRandom paper) {
        //验证大题情况
        this.tryToBuildVirtualParts(pType, parts, paper);
        for (VirtualExamPart part : parts) {
            if (part.getId().equals(pType)) {
                adapter.setVirtualPart(part);
                part.getAdapters().add(adapter);
                break;
            }
        }
    }

    /**
     * 构建一个考试实例
     *
     * @param ec
     */
    public void buildExamCase(WrongTestInfo info, ExamCase ec) {
        Examination exam = ec.getExamination();
        List<ChoiceQuestion> cqs = new LinkedList();
        List<MultiChoiceQuestion> mcqs = new LinkedList();
        List<FillQuestion> fqs = new LinkedList();
        List<JudgeQuestion> jqs = new LinkedList();
        List<EssayQuestion> eqs = new LinkedList();
        List<FileQuestion> ffqs = new LinkedList();
        List<CaseQuestion> ccqs = new LinkedList();
        //虚拟试卷大题
        List<VirtualExamPart> vparts = new ArrayList();

        //添加随机试卷试题
        ExamPaperRandom paper1 = exam.getRandomPaper();

        if (paper1 == null) {
            paper1 = new ExamPaperRandom();
            paper1.setName("错练习内置随机试卷");
            paper1.setChoiceScore(1);
            paper1.setMultiChoiceScore(1);
            paper1.setFillScore(1);
            paper1.setJudgeScore(1);
            paper1.setEssayScore(5);
        }

        WrongTestInfo me = info;
        //随机取出此模块中的一定量单选题
        List<ChoiceQuestion> cqsAll = me.getWqWrapper().getChoiceQuestions();
        if (!cqsAll.isEmpty() && (me.getChoiceNum() - 1) >= 0) {
            Collections.shuffle(cqsAll);
            if (cqsAll.size() < me.getChoiceNum()) {
                me.setChoiceNum(cqsAll.size());
            }
            cqs.addAll(cqsAll.subList(0, me.getChoiceNum()));
        }
        //随机取出此模块中的一定量多选题
        List<MultiChoiceQuestion> mcqsAll = me.getWqWrapper().getMultiChoiceQuestions();
        if (!mcqsAll.isEmpty() && (me.getMultiChoiceNum() - 1) >= 0) {
            Collections.shuffle(mcqsAll);
            if (mcqsAll.size() < me.getMultiChoiceNum()) {
                me.setMultiChoiceNum(mcqsAll.size());
            }
            mcqs.addAll(mcqsAll.subList(0, me.getMultiChoiceNum()));
        }
        //随机取出此模块中的一定量填空题
        List<FillQuestion> fqsAll = me.getWqWrapper().getFillQuestions();
        if (!fqsAll.isEmpty() && (me.getFillNum()) >= 0) {
            Collections.shuffle(fqsAll);
            if (fqsAll.size() < me.getFillNum()) {
                me.setFillNum(fqsAll.size());
            }
            fqs.addAll(fqsAll.subList(0, me.getFillNum()));
        }
        //随机取出此模块中的一定量判断题
        List<JudgeQuestion> jqsAll = me.getWqWrapper().getJudgeQuestions();
        if (!jqsAll.isEmpty() && (me.getJudgeNum() - 1) >= 0) {
            Collections.shuffle(jqsAll);
            if (jqsAll.size() < me.getJudgeNum()) {
                me.setJudgeNum(jqsAll.size());
            }
            jqs.addAll(jqsAll.subList(0, me.getJudgeNum()));
        }
        //随机取出此模块中的一定量问答题
        List<EssayQuestion> eqsAll = me.getWqWrapper().getEssayQuestions();
        if (!eqsAll.isEmpty() && (me.getEssayNum() - 1) >= 0) {
            Collections.shuffle(eqsAll);
            if (eqsAll.size() < me.getEssayNum()) {
                me.setEssayNum(eqsAll.size());
            }
            eqs.addAll(eqsAll.subList(0, me.getEssayNum()));
        }
        //随机取出此模块中的一定量文件题
        List<FileQuestion> ffqsAll = me.getWqWrapper().getFileQuestions();
        if (!ffqsAll.isEmpty() && (me.getFileNum() - 1) >= 0) {
            Collections.shuffle(ffqsAll);
            if (ffqsAll.size() < me.getFileNum()) {
                me.setFileNum(ffqsAll.size());
            }
            ffqs.addAll(ffqsAll.subList(0, me.getFileNum()));
        }

        //将单选试题加入ExamCase中
        //System.out.println("试卷中单选数量：" + cqs.size());
        List<ExamCaseItemChoice> ecics = new LinkedList();
        for (ChoiceQuestion c : cqs) {
            ExamCaseItemChoice ecic = new ExamCaseItemChoice();
            ecic.setScore(paper1.getChoiceScore());
            ecic.setExamCase(ec);
            List<ExamChoice> ecs = c.getChoices();
            //Collections.sort(ecs);
            //配置单选题选项随机
            if (ec.getExamination().isChoiceRandom() && c.isAllowChoiceRandom()) {
                this.choiceOrderAdapter(ecs);
            }
            c.setChoices(ecs);
            ecic.setQuestion(c);
            ecics.add(ecic);

            ExamCaseItemAdapter adapter = new ExamCaseItemAdapter();
            adapter.setChoiceItem(ecic);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("choice");
            adapter.setQuestion(ecic.getQuestion());
            adapter.setOrd(ecic.getQuestion().getOrd());//随机试卷内小题若需排序将根据试题的顺序排序
            adapter.setScore(paper1.getChoiceScore());//设置adapter的分值
            adapter.setItem(ecic);
            this.addAdapterForPart(adapter, "choice", vparts, paper1);
        }
        ec.setChoiceItems(ecics);

        //将多选试题加入ExamCase中
        List<ExamCaseItemMultiChoice> emcics = new LinkedList();
        for (MultiChoiceQuestion c : mcqs) {
            ExamCaseItemMultiChoice ecic = new ExamCaseItemMultiChoice();
            ecic.setScore(paper1.getMultiChoiceScore());
            ecic.setExamCase(ec);
            List<ExamMultiChoice> ecs = c.getChoices();
            //Collections.sort(ecs);
            //配置多选题选项随机
            if (ec.getExamination().isChoiceRandom() && c.isAllowChoiceRandom()) {
                this.multiChoiceOrderAdapter(ecs);
            }
            c.setChoices(ecs);
            ecic.setQuestion(c);
            emcics.add(ecic);

            ExamCaseItemAdapter adapter = new ExamCaseItemAdapter();
            adapter.setMultiChoiceItem(ecic);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("mchoice");
            adapter.setQuestion(ecic.getQuestion());
            adapter.setOrd(ecic.getQuestion().getOrd());//随机试卷内小题若需排序将根据试题的顺序排序
            adapter.setScore(paper1.getMultiChoiceScore());//设置adapter的分值
            adapter.setItem(ecic);
            this.addAdapterForPart(adapter, "mchoice", vparts, paper1);
        }
        ec.setMultiChoiceItems(emcics);
        //将填空试题加入ExamCase中
        List<ExamCaseItemFill> ecifs = new LinkedList();
        for (FillQuestion f : fqs) {
            ExamCaseItemFill ecif = new ExamCaseItemFill();
            ecif.setScore(paper1.getFillScore());
            ecif.setExamCase(ec);
            ecif.setQuestion(f);
            ecifs.add(ecif);

            ExamCaseItemAdapter adapter = new ExamCaseItemAdapter();
            adapter.setFillItem(ecif);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("fill");
            adapter.setQuestion(ecif.getQuestion());
            adapter.setOrd(ecif.getQuestion().getOrd());//随机试卷内小题若需排序将根据试题的顺序排序
            adapter.setScore(paper1.getFillScore());//设置adapter的分值
            adapter.setItem(ecif);
            this.addAdapterForPart(adapter, "fill", vparts, paper1);
        }
        ec.setFillItems(ecifs);
        //将判断试题加入ExamCase中
        List<ExamCaseItemJudge> ecijs = new LinkedList();
        for (JudgeQuestion j : jqs) {
            ExamCaseItemJudge ecij = new ExamCaseItemJudge();
            ecij.setScore(paper1.getJudgeScore());
            ecij.setExamCase(ec);
            ecij.setQuestion(j);
            ecijs.add(ecij);

            ExamCaseItemAdapter adapter = new ExamCaseItemAdapter();
            adapter.setJudgeItem(ecij);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("judge");
            adapter.setQuestion(ecij.getQuestion());
            adapter.setOrd(ecij.getQuestion().getOrd());//随机试卷内小题若需排序将根据试题的顺序排序
            adapter.setScore(paper1.getJudgeScore());//设置adapter的分值
            adapter.setItem(ecij);
            this.addAdapterForPart(adapter, "judge", vparts, paper1);
        }
        ec.setJudgeItems(ecijs);
        //将问答试题加入ExamCase中
        List<ExamCaseItemEssay> ecies = new LinkedList();
        for (EssayQuestion j : eqs) {
            ExamCaseItemEssay ecie = new ExamCaseItemEssay();
            ecie.setScore(paper1.getEssayScore());
            ecie.setExamCase(ec);
            ecie.setQuestion(j);
            ecies.add(ecie);

            ExamCaseItemAdapter adapter = new ExamCaseItemAdapter();
            adapter.setEssayItem(ecie);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("essay");
            adapter.setQuestion(ecie.getQuestion());
            adapter.setOrd(ecie.getQuestion().getOrd());//随机试卷内小题若需排序将根据试题的顺序排序
            adapter.setScore(paper1.getEssayScore());//设置adapter的分值
            adapter.setItem(ecie);
            this.addAdapterForPart(adapter, "essay", vparts, paper1);
        }
        ec.setEssayItems(ecies);
        //将文件试题加入ExamCase中
        List<ExamCaseItemFile> eciffs = new LinkedList();
        for (FileQuestion j : ffqs) {
            ExamCaseItemFile ecie = new ExamCaseItemFile();
            ecie.setScore(paper1.getFileScore());
            ecie.setExamCase(ec);
            ecie.setQuestion(j);
            eciffs.add(ecie);

            ExamCaseItemAdapter adapter = new ExamCaseItemAdapter();
            adapter.setFileItem(ecie);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("file");
            adapter.setQuestion(ecie.getQuestion());
            adapter.setOrd(ecie.getQuestion().getOrd());//随机试卷内小题若需排序将根据试题的顺序排序
            adapter.setScore(paper1.getFileScore());//设置adapter的分值
            adapter.setItem(ecie);
            this.addAdapterForPart(adapter, "file", vparts, paper1);
        }
        ec.setFileItems(eciffs);
        //将综合题加入ExamCase中
        List<ExamCaseItemCase> eciccs = new LinkedList();
        for (CaseQuestion j : ccqs) {
            //调用抽象类中的现成方法构建组装综合题
            ExamCaseItemCase ecie = this.buildCaseItem(j, ec);
            eciccs.add(ecie);

            ExamCaseItemAdapter adapter = new ExamCaseItemAdapter();
            adapter.setCaseItem(ecie);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("case");
            adapter.setQuestion(ecie.getQuestion());
            adapter.setOrd(ecie.getQuestion().getOrd());//随机试卷内小题若需排序将根据试题的顺序排序
            //adapter.setScore(paper1.getChoiceScore());//设置adapter的分值
            adapter.setItem(ecie);
            this.addAdapterForPart(adapter, "case", vparts, paper1);
        }
        ec.setCaseItems(eciccs);

        //对试题实例顺序排序
        if (ec.getExamination().isIfRandom()) {
            Collections.shuffle(ecics);
            Collections.shuffle(emcics);
            Collections.shuffle(ecifs);
            Collections.shuffle(ecijs);
            Collections.shuffle(ecies);
            Collections.shuffle(eciffs);
            Collections.shuffle(eciccs);
        } else {
            Collections.sort(ecics);
            Collections.sort(emcics);
            Collections.sort(ecifs);
            Collections.sort(ecijs);
            Collections.sort(ecies);
            Collections.sort(eciffs);
            Collections.sort(eciccs);
        }

        //对大题内的小题进行排序 
        //随机试卷的小题排序规则是：在大题内部按照题目的ord属性进行排序，adapter的ord在上述代码中已经被设置为了题目ord
        for (VirtualExamPart part : vparts) {
            if (exam.isIfRandom()) {
                Collections.shuffle(part.getAdapters());
            } else {
                Collections.sort(part.getAdapters());
            }
        }
        Collections.sort(vparts);
        ec.setVparts(vparts);
        //计算出满分
        double totalFullScore = ec.getChoiceFullScore() + ec.getMultiChoiceFullScore() + ec.getFillFullScore() + ec.getJudgeFullScore() + ec.getEssayFullScore() + ec.getFileFullScore() + ec.getCaseFullScore();
        ec.setTotalFullScore(totalFullScore);
    }

    public void buildWrongTestCase(WrongTestInfo info, ExamCase ec) {
        IChoiceQuestionDAO choiceQuestionDAO = SpringHelper.getSpringBean("ChoiceQuestionDAO");
        IMultiChoiceQuestionDAO multiChoiceQuestionDAO = SpringHelper.getSpringBean("MultiChoiceQuestionDAO");
        //Examination exam = ec.getExamination();
        List<ChoiceQuestion> cqs = new LinkedList();
        List<MultiChoiceQuestion> mcqs = new LinkedList();
        List<FillQuestion> fqs = new LinkedList();
        List<JudgeQuestion> jqs = new LinkedList();
        List<EssayQuestion> eqs = new LinkedList();
        List<FileQuestion> ffqs = new LinkedList();
        WrongTestInfo me = info;
        //随机取出此模块中的一定量单选题
        List<ChoiceQuestion> cqsAll = me.getWqWrapper().getChoiceQuestions();
        if (!cqsAll.isEmpty() && (me.getChoiceNum() - 1) >= 0) {
            Collections.shuffle(cqsAll);
            if (cqsAll.size() < me.getChoiceNum()) {
                me.setChoiceNum(cqsAll.size());
            }
            cqs.addAll(cqsAll.subList(0, me.getChoiceNum()));
        }
        //随机取出此模块中的一定量多选题
        List<MultiChoiceQuestion> mcqsAll = me.getWqWrapper().getMultiChoiceQuestions();
        if (!mcqsAll.isEmpty() && (me.getMultiChoiceNum() - 1) >= 0) {
            Collections.shuffle(mcqsAll);
            if (mcqsAll.size() < me.getMultiChoiceNum()) {
                me.setMultiChoiceNum(mcqsAll.size());
            }
            mcqs.addAll(mcqsAll.subList(0, me.getMultiChoiceNum()));
        }
        //随机取出此模块中的一定量填空题
        List<FillQuestion> fqsAll = me.getWqWrapper().getFillQuestions();
        if (!fqsAll.isEmpty() && (me.getFillNum()) >= 0) {
            Collections.shuffle(fqsAll);
            if (fqsAll.size() < me.getFillNum()) {
                me.setFillNum(fqsAll.size());
            }
            fqs.addAll(fqsAll.subList(0, me.getFillNum()));
        }
        //随机取出此模块中的一定量判断题
        List<JudgeQuestion> jqsAll = me.getWqWrapper().getJudgeQuestions();
        if (!jqsAll.isEmpty() && (me.getJudgeNum() - 1) >= 0) {
            Collections.shuffle(jqsAll);
            if (jqsAll.size() < me.getJudgeNum()) {
                me.setJudgeNum(jqsAll.size());
            }
            jqs.addAll(jqsAll.subList(0, me.getJudgeNum()));
        }
        //随机取出此模块中的一定量问答题
        List<EssayQuestion> eqsAll = me.getWqWrapper().getEssayQuestions();
        if (!eqsAll.isEmpty() && (me.getEssayNum() - 1) >= 0) {
            Collections.shuffle(eqsAll);
            if (eqsAll.size() < me.getEssayNum()) {
                me.setEssayNum(eqsAll.size());
            }
            eqs.addAll(eqsAll.subList(0, me.getEssayNum()));
        }
        //随机取出此模块中的一定量文件题
        List<FileQuestion> ffqsAll = me.getWqWrapper().getFileQuestions();
        if (!ffqsAll.isEmpty() && (me.getFileNum() - 1) >= 0) {
            Collections.shuffle(ffqsAll);
            if (ffqsAll.size() < me.getFileNum()) {
                me.setFileNum(ffqsAll.size());
            }
            ffqs.addAll(ffqsAll.subList(0, me.getFileNum()));
        }

        //对试题顺序排序
        System.out.println(info.isQuestionRandom());
        if (!info.isQuestionRandom()) {
            Collections.sort(cqs);
            Collections.sort(mcqs);
            Collections.sort(fqs);
            Collections.sort(jqs);
            Collections.sort(eqs);
            Collections.sort(ffqs);
        }

        //将单选试题加入ExamCase中
        List<ExamCaseItemChoice> ecics = new LinkedList();
        for (ChoiceQuestion c : cqs) {
            c = choiceQuestionDAO.findChoiceQuestion(c.getId());
            ExamCaseItemChoice ecic = new ExamCaseItemChoice();
            ecic.setExamCase(ec);
            List<ExamChoice> ecs = c.getChoices();
            //Collections.sort(ecs);
            //配置单选题选项随机
            if (info.isChoiceRandom()) {
                this.choiceOrderAdapter(ecs);
            }
            c.setChoices(ecs);
            ecic.setQuestion(c);
            ecics.add(ecic);
        }
        ec.setChoiceItems(ecics);

        //将多选试题加入ExamCase中
        List<ExamCaseItemMultiChoice> emcics = new LinkedList();
        for (MultiChoiceQuestion c : mcqs) {
            c = multiChoiceQuestionDAO.findMultiChoiceQuestion(c.getId());
            ExamCaseItemMultiChoice ecic = new ExamCaseItemMultiChoice();
            ecic.setExamCase(ec);
            List<ExamMultiChoice> ecs = c.getChoices();
            //Collections.sort(ecs);
            //配置单选题选项随机
            if (info.isMultiChoiceRandom()) {
                this.multiChoiceOrderAdapter(ecs);
            }
            c.setChoices(ecs);
            ecic.setQuestion(c);
            emcics.add(ecic);
        }
        ec.setMultiChoiceItems(emcics);
        //将填空试题加入ExamCase中
        List<ExamCaseItemFill> ecifs = new LinkedList();
        for (FillQuestion f : fqs) {
            ExamCaseItemFill ecif = new ExamCaseItemFill();
            ecif.setExamCase(ec);
            ecif.setQuestion(f);
            ecifs.add(ecif);
        }
        ec.setFillItems(ecifs);
        //将判断试题加入ExamCase中
        List<ExamCaseItemJudge> ecijs = new LinkedList();
        for (JudgeQuestion j : jqs) {
            ExamCaseItemJudge ecij = new ExamCaseItemJudge();
            ecij.setExamCase(ec);
            ecij.setQuestion(j);
            ecijs.add(ecij);
        }
        ec.setJudgeItems(ecijs);
        //将问答试题加入ExamCase中
        List<ExamCaseItemEssay> ecies = new LinkedList();
        for (EssayQuestion j : eqs) {
            ExamCaseItemEssay ecie = new ExamCaseItemEssay();
            ecie.setExamCase(ec);
            ecie.setQuestion(j);
            ecies.add(ecie);
        }
        ec.setEssayItems(ecies);
        //将文件试题加入ExamCase中
        List<ExamCaseItemFile> eciffs = new LinkedList();
        for (FileQuestion j : ffqs) {
            ExamCaseItemFile ecie = new ExamCaseItemFile();
            ecie.setExamCase(ec);
            ecie.setQuestion(j);
            eciffs.add(ecie);
        }
        ec.setFileItems(eciffs);
    }

    /*在错题中心进行练习结束提交后的阅卷方法
     */
    public ExamCase computeWrongTestCase(WrongTestInfo info, ExamCase ec) {
        Examination exam = ec.getExamination();
        double totalScore = 0;
        double choiceScore = 0;
        double multiChoiceScore = 0;
        double fillScore = 0;
        double judgeScore = 0;
        double essayScore = 0;
        double fileScore = 0;
        List<ExamCaseItemChoice> cqs = ec.getChoiceItems();
        List<ExamCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
        List<ExamCaseItemFill> fqs = ec.getFillItems();
        List<ExamCaseItemJudge> jqs = ec.getJudgeItems();
        List<ExamCaseItemEssay> eqs = ec.getEssayItems();
        List<ExamCaseItemFile> ffqs = ec.getFileItems();
        //计算每道单选题是否正确
        for (ExamCaseItemChoice c : cqs) {
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
                choiceScore += WrongExamScores.choiceScore;
            }
        }
        //计算每道多选题是否正确
        for (ExamCaseItemMultiChoice c : mcqs) {
            //List<ExamMultiChoice> ls = c.getQuestion().getChoices();
            StringBuilder sb = new StringBuilder();
            /**
             * for (ExamMultiChoice e : ls) { if (e.isSelected()) {
             * sb.append(e.getLabel()); } }
             *
             */
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
                multiChoiceScore += WrongExamScores.multiChoiceScore;
            }
        }
        //计算每道填空题是否正确
        for (ExamCaseItemFill c : fqs) {
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
                fillScore += WrongExamScores.fillScore * dt;
            }
        }
        //计算每道判断题是否正确
        for (ExamCaseItemJudge c : jqs) {
            c.setRightAnswer(String.valueOf(c.getQuestion().getAnswer()));
            c.setIfRight(c.getRightAnswer().equals(c.getUserAnswer()));
            if (c.isIfRight()) {
                judgeScore += WrongExamScores.judgeScore;
            }
        }
        //计算每道问答题得分
        for (ExamCaseItemEssay c : eqs) {
            c.setRightAnswer(c.getQuestion().getRightStr());
            float ratio = SimilarAlgorithm.getSimilarity(HTMLCleaner.delHTMLTag(c.getQuestion().getAnswer()), HTMLCleaner.delHTMLTag(c.getUserAnswer()));
            c.setRightRatio(ratio);
            essayScore += WrongExamScores.essayScore * ratio;
        }

        //计算每道文件题得分
        for (ExamCaseItemFile c : ffqs) {
            c.setRightAnswer(c.getQuestion().getRightStr());
            float ratio = SimilarAlgorithm.getSimilarity(HTMLCleaner.delHTMLTag(c.getQuestion().getAnswer()), HTMLCleaner.delHTMLTag(c.getUserAnswer()));
            c.setRightRatio(ratio);
            fileScore += WrongExamScores.fileScore * ratio;
        }

        totalScore = choiceScore + fillScore + judgeScore + multiChoiceScore + essayScore + fileScore;

        double ratio = totalScore / ec.getTotalFullScore();
        if (ratio < 0) {
            ratio = 0;
        }
        if (ratio > 1) {
            ratio = 1;
        }
        long bbsScore = (long) (ec.getExamination().getBbsScore() * ratio);
        ec.setChoiceScore(choiceScore);
        ec.setMultiChoiceScore(multiChoiceScore);
        ec.setFillScore(fillScore);
        ec.setJudgeScore(judgeScore);
        ec.setEssayScore(essayScore);
        ec.setFileScore(fileScore);
        ec.setScore(totalScore);
        ec.setBbsScore(bbsScore);
        return ec;
    }

    /**
     * 检查ExamCase中的错题并记录
     *
     * @param ec
     */
    public void checkWrongItems(ExamCase ec) {
        IWrongQuestionDAO wrongDAO = SpringHelper.getSpringBean("WrongQuestionDAO");
        IWrongKnowledgeDAO wrong2DAO = SpringHelper.getSpringBean("WrongKnowledgeDAO");
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
                wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), c.getExamCase().getUser().getId(), "choice");
                //加入我的难点
                List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                for (ExamKnowledge k : knows) {
                    wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                }
            }
        }
        //计算每道多选题是否正确
        for (ExamCaseItemMultiChoice c : mcqs) {
            if (!c.isIfRight()) {
                wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), c.getExamCase().getUser().getId(), "mchoice");
                //加入我的难点
                List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                for (ExamKnowledge k : knows) {
                    wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                }
            }
        }
        //计算每道填空题是否正确
        for (ExamCaseItemFill c : fqs) {
            if (c.getRightNum() < c.getTotalNum()) {
                wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), c.getExamCase().getUser().getId(), "fill");
                //加入我的难点
                List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                for (ExamKnowledge k : knows) {
                    wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                }
            }
        }
        //计算每道判断题是否正确
        for (ExamCaseItemJudge c : jqs) {
            if (!c.isIfRight()) {
                wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), c.getExamCase().getUser().getId(), "judge");
                //加入我的难点
                List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                for (ExamKnowledge k : knows) {
                    wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                }
            }
        }
        //计算每道问答题得分
        for (ExamCaseItemEssay c : eqs) {
            if (c.getRightRatio() < 0.8) {
                wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), c.getExamCase().getUser().getId(), "essay");
                //加入我的难点
                List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                for (ExamKnowledge k : knows) {
                    wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                }
            }
        }

        //计算每道文件题得分
        for (ExamCaseItemFile c : ffqs) {
            if (c.getRightRatio() < 0.8) {
                wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), c.getExamCase().getUser().getId(), "file");
                //加入我的难点
                List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                for (ExamKnowledge k : knows) {
                    wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                }
            }
        }
        //计算每道综合题得分
        for (ExamCaseItemCase c : caqs) {
            List<ExamCaseItemChoice> cqss = c.getChoiceItems();
            //计算综合题中每道单选题是否正确
            for (ExamCaseItemChoice cc : cqss) {
                if (!cc.isIfRight()) {
                    wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), c.getExamCase().getUser().getId(), "choice");
                    //加入我的难点
                    List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                    for (ExamKnowledge k : knows) {
                        wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                    }
                }
            }
            List<ExamCaseItemMultiChoice> mcqss = c.getMultiChoiceItems();
            //计算综合题中每道单选题是否正确
            for (ExamCaseItemMultiChoice cc : mcqss) {
                if (!cc.isIfRight()) {
                    wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), c.getExamCase().getUser().getId(), "mchoice");
                    //加入我的难点
                    List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                    for (ExamKnowledge k : knows) {
                        wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                    }
                }
            }
            List<ExamCaseItemFill> fqss = c.getFillItems();
            //计算综合题中每道单选题是否正确
            for (ExamCaseItemFill cc : fqss) {
                if (cc.getRightNum() < cc.getTotalNum()) {
                    wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), c.getExamCase().getUser().getId(), "fill");
                    //加入我的难点
                    List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                    for (ExamKnowledge k : knows) {
                        wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                    }
                }
            }
            List<ExamCaseItemJudge> jqss = c.getJudgeItems();
            //计算综合题中每道单选题是否正确
            for (ExamCaseItemJudge cc : jqss) {
                if (!cc.isIfRight()) {
                    wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), c.getExamCase().getUser().getId(), "judge");
                    //加入我的难点
                    List<ExamKnowledge> knows = c.getQuestion().getKnowledges();
                    for (ExamKnowledge k : knows) {
                        wrong2DAO.wrongTimesPlusOne(k.getId(), ec.getUser().getId());
                    }
                }
            }
            List<ExamCaseItemEssay> eqss = c.getEssayItems();
            //计算综合每道问答题得分
            for (ExamCaseItemEssay cc : eqss) {
                if (cc.getRightRatio() < 0.8) {
                    wrongDAO.wrongTimesPlusOne(c.getQuestion().getId(), c.getExamCase().getUser().getId(), "essay");
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
