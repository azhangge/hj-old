package com.hjedu.examination.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.ICaseQuestionDAO;
import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IEssayQuestionDAO;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.dao.IFillQuestionDAO;
import com.hjedu.examination.dao.IJudgeQuestionDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
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
import com.hjedu.examination.entity.ExamPaperRandom;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.entity.FileQuestion;
import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.examination.entity.ModuleRandomPaper;
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
 * 简单随机试卷考试业务逻辑实现类
 *
 * @author huajie
 */
public class ExamCaseService extends IExamCaseService implements Serializable {

    IExamCaseDAO examCaseDAO;
    IChoiceQuestionDAO choiceQuestionDAO;
    IMultiChoiceQuestionDAO multiChoiceQuestionDAO;
    IFillQuestionDAO fillQuestionDAO;
    IJudgeQuestionDAO judgeQuestionDAO;
    IEssayQuestionDAO essayQuestionDAO;
    IFileQuestionDAO fileQuestionDAO;
    ICaseQuestionDAO caseQuestionDAO;
    ThreadPoolTaskExecutor exec;

    public IExamCaseDAO getExamCaseDAO() {
        return examCaseDAO;
    }

    public void setExamCaseDAO(IExamCaseDAO examCaseDAO) {
        this.examCaseDAO = examCaseDAO;
    }

    public ThreadPoolTaskExecutor getExec() {
        return exec;
    }

    public void setExec(ThreadPoolTaskExecutor exec) {
        this.exec = exec;
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
    @Override
    public void buildExamCase(ExamCase ec) {
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
            return;
        }

        List<ModuleRandomPaper> mes = paper1.getModulePapers();
        for (ModuleRandomPaper me : mes) {
            if (me.getModule() != null) {
                //随机取出此模块中的一定量单选题
                cqs.addAll(choiceQuestionDAO.getRandomQuestionOfNumInModule(me.getChoiceNum(), me.getModule().getId()));
                //随机取出此模块中的一定量多选题
                mcqs.addAll(multiChoiceQuestionDAO.getRandomQuestionOfNumInModule(me.getMultiChoiceNum(), me.getModule().getId()));
                //随机取出此模块中的一定量填空题
                fqs.addAll(fillQuestionDAO.getRandomQuestionOfNumInModule(me.getFillNum(), me.getModule().getId()));
                //随机取出此模块中的一定量判断题
                jqs.addAll(judgeQuestionDAO.getRandomQuestionOfNumInModule(me.getJudgeNum(), me.getModule().getId()));
                //随机取出此模块中的一定量问答题
                eqs.addAll(essayQuestionDAO.getRandomQuestionOfNumInModule(me.getEssayNum(), me.getModule().getId()));
                //随机取出此模块中的一定量文件题
                ffqs.addAll(fileQuestionDAO.getRandomQuestionOfNumInModule(me.getFileNum(), me.getModule().getId()));
                //随机取出此模块中的一定量综合题
                ccqs.addAll(caseQuestionDAO.getRandomQuestionOfNumInModule(me.getCaseNum(), me.getModule().getId()));
            }
        }

        //将单选试题加入ExamCase中
        //System.out.println("试卷中单选数量：" + cqs.size());
        List<ExamCaseItemChoice> ecics = new LinkedList();
        for (ChoiceQuestion c : cqs) {
            ExamCaseItemChoice ecic = new ExamCaseItemChoice();
            ecic.setScore(ec.getExamination().getRandomPaper().getChoiceScore());
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
            ecic.setScore(ec.getExamination().getRandomPaper().getMultiChoiceScore());
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
            ecif.setScore(ec.getExamination().getRandomPaper().getFillScore());
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
            ecij.setScore(ec.getExamination().getRandomPaper().getJudgeScore());
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
            ecie.setScore(ec.getExamination().getRandomPaper().getEssayScore());
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
            ecie.setScore(ec.getExamination().getRandomPaper().getFileScore());
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
            this.orderCaseItems(ecie);
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
        //计算各大题的得分及总分
        ExamCaseServiceUtils.computePartScore(ec);
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

        //虚拟试卷大题
        List<VirtualExamPart> vparts = new ArrayList();
        ExamPaperRandom paper1 = exam.getRandomPaper();

        //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
        for (ExamCaseItemChoice cq : cqs) {
            if (cq.getQuestion() == null) {
                continue;
            }
            List<ExamChoice> ls = cq.getQuestion().getChoices();
            if (ec.getExamination().isChoiceRandom() && cq.getQuestion().isAllowChoiceRandom()) {
                this.choiceOrderAdapter(ls);
            }

            ExamCaseItemAdapter adapter = new ExamCaseItemAdapter();
            adapter.setOrd(cq.getIndex());//恢复试题时，按照上次试题显示的顺序进行恢复
            adapter.setChoiceItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("choice");
            adapter.setQuestion(cq.getQuestion());
            adapter.setScore(paper1.getChoiceScore());//设置adapter的分值
            adapter.setItem(cq);
            this.addAdapterForPart(adapter, "choice", vparts, paper1);
        }

        //恢复多选题
        for (ExamCaseItemMultiChoice c : mcqs) {
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
            if (ec.getExamination().isChoiceRandom() && c.getQuestion().isAllowChoiceRandom()) {
                this.multiChoiceOrderAdapter(ls);
            }

            ExamCaseItemAdapter adapter = new ExamCaseItemAdapter();
            adapter.setOrd(c.getIndex());//恢复试题时，按照上次试题显示的顺序进行恢复
            adapter.setMultiChoiceItem(c);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("mchoice");
            adapter.setQuestion(c.getQuestion());
            adapter.setScore(paper1.getMultiChoiceScore());//设置adapter的分值
            adapter.setItem(c);
            this.addAdapterForPart(adapter, "mchoice", vparts, paper1);
        }
        //恢复填空题
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
            ExamCaseItemAdapter adapter = new ExamCaseItemAdapter();
            adapter.setOrd(c.getIndex());//恢复试题时，按照上次试题显示的顺序进行恢复
            adapter.setFillItem(c);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("fill");
            adapter.setQuestion(c.getQuestion());
            adapter.setScore(paper1.getFillScore());//设置adapter的分值
            adapter.setItem(c);
            this.addAdapterForPart(adapter, "fill", vparts, paper1);
        }

        for (ExamCaseItemJudge cq : jqs) {
            if (cq.getQuestion() == null) {
                continue;
            }
            ExamCaseItemAdapter adapter = new ExamCaseItemAdapter();
            adapter.setOrd(cq.getIndex());//恢复试题时，按照上次试题显示的顺序进行恢复
            adapter.setJudgeItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("judge");
            adapter.setQuestion(cq.getQuestion());
            adapter.setScore(paper1.getJudgeScore());//设置adapter的分值
            adapter.setItem(cq);
            this.addAdapterForPart(adapter, "judge", vparts, paper1);

        }

        for (ExamCaseItemEssay cq : eqs) {
            if (cq.getQuestion() == null) {
                continue;
            }
            ExamCaseItemAdapter adapter = new ExamCaseItemAdapter();
            adapter.setOrd(cq.getIndex());//恢复试题时，按照上次试题显示的顺序进行恢复
            adapter.setEssayItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("essay");
            adapter.setQuestion(cq.getQuestion());
            adapter.setScore(paper1.getEssayScore());//设置adapter的分值
            adapter.setItem(cq);
            this.addAdapterForPart(adapter, "essay", vparts, paper1);
        }
        for (ExamCaseItemFile cq : ffqs) {
            if (cq.getQuestion() == null) {
                continue;
            }
            ExamCaseItemAdapter adapter = new ExamCaseItemAdapter();
            adapter.setOrd(cq.getIndex());//恢复试题时，按照上次试题显示的顺序进行恢复
            adapter.setFileItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("file");
            adapter.setQuestion(cq.getQuestion());
            adapter.setScore(paper1.getFileScore());//设置adapter的分值
            adapter.setItem(cq);
            this.addAdapterForPart(adapter, "file", vparts, paper1);
        }

        for (ExamCaseItemCase cc : ccqs) {
            if (cc.getQuestion() == null) {
                continue;
            }
            ExamCaseItemAdapter adapter = new ExamCaseItemAdapter();
            adapter.setOrd(cc.getIndex());
            adapter.setCaseItem(cc);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("case");
            this.orderCaseItems(cc);
            adapter.setQuestion(cc.getQuestion());
            //adapter.setScore(paper1.getChoiceScore());//设置adapter的分值
            adapter.setItem(cc);
            this.addAdapterForPart(adapter, "case", vparts, paper1);

            List<ExamCaseItemChoice> cqs2 = cc.getChoiceItems();
            List<ExamCaseItemMultiChoice> mcqs2 = cc.getMultiChoiceItems();
            List<ExamCaseItemFill> fqs2 = cc.getFillItems();
            //恢复综合题中的单选题选项随机性
            for (ExamCaseItemChoice c : cqs2) {
                List<ExamChoice> ls = c.getQuestion().getChoices();
                if (ec.getExamination().isChoiceRandom() && c.getQuestion().isAllowChoiceRandom()) {
                    this.choiceOrderAdapter(ls);
                }
            }
            //恢复综合题中的多选题
            for (ExamCaseItemMultiChoice c : mcqs2) {
                List<ExamMultiChoice> ls = c.getQuestion().getChoices();
                String ua = c.getUserAnswer();
                if (ua != null) {
                    char[] uac = ua.toCharArray();
                    List<String> labels = new ArrayList();
                    if (uac != null) {
                        for (char u : uac) {
                            labels.add(String.valueOf(u));
                        }
                    }
                    c.setSelectedLabels(labels);
                }
                if (ec.getExamination().isMultiChoiceRandom() && c.getQuestion().isAllowChoiceRandom()) {
                    this.multiChoiceOrderAdapter(ls);
                }
            }
            //恢复综合题中的填空题
            for (ExamCaseItemFill c : fqs2) {
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

        //在恢复试卷时无条件将大题内的小题排序
        for (VirtualExamPart part : vparts) {
            Collections.sort(part.getAdapters());
        }

        Collections.sort(vparts);

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
    public ExamCase computeExamCase(ExamCase ec) {
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
        //计算每道单选题是否正确
        for (ExamCaseItemChoice c : cqs) {
            if (c.getQuestion().getAnswer() == null) {
                continue;
            }
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
                choiceScore += exam.getRandomPaper().getChoiceScore();
            }
        }
        //计算每道多选题是否正确
        for (ExamCaseItemMultiChoice c : mcqs) {
            if (c.getQuestion().getAnswer() == null) {
                continue;
            }
            //List<ExamMultiChoice> ls = c.getQuestion().getChoices();
            StringBuilder sb = new StringBuilder();
            /**
             * for (ExamMultiChoice e : ls) { if (e.isSelected()) {
             * sb.append(e.getLabel()); } } *
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
                multiChoiceScore += exam.getRandomPaper().getMultiChoiceScore();
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
                fillScore += exam.getRandomPaper().getFillScore() * dt;
            }
        }
        //计算每道判断题是否正确
        for (ExamCaseItemJudge c : jqs) {
            c.setRightAnswer(String.valueOf(c.getQuestion().getAnswer()));
            c.setIfRight(c.getRightAnswer().equals(c.getUserAnswer()));
            if (c.isIfRight()) {
                judgeScore += exam.getRandomPaper().getJudgeScore();
            }
        }
        //计算每道问答题得分
        for (ExamCaseItemEssay c : eqs) {
            c.setRightAnswer(c.getQuestion().getRightStr());
            float ratio = 0f;
            try {
                ratio = SimilarAlgorithm.getSimilarity(HTMLCleaner.delHTMLTag(c.getQuestion().getAnswer()), HTMLCleaner.delHTMLTag(c.getUserAnswer()));
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            c.setRightRatio(ratio);
            essayScore += exam.getRandomPaper().getEssayScore() * ratio;
        }

        //计算每道文件题得分
        for (ExamCaseItemFile c : ffqs) {
            c.setRightAnswer(c.getQuestion().getRightStr());
            float ratio = 0f;
            try {
                ratio = SimilarAlgorithm.getSimilarity(HTMLCleaner.delHTMLTag(c.getQuestion().getAnswer()), HTMLCleaner.delHTMLTag(c.getUserAnswer()));
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            c.setRightRatio(ratio);
            fileScore += exam.getRandomPaper().getFileScore() * ratio;
        }

        //计算每道综合题得分
        for (ExamCaseItemCase cic : ccqs) {
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
//                for (ExamMultiChoice e : ls) {
//                    if (e.isSelected()) {
//                        sb.append(e.getLabel());
//                    }
//                }
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

        totalScore = choiceScore + fillScore + judgeScore + multiChoiceScore + essayScore + fileScore + caseScore;

        double ratio = totalScore / ec.getTotalFullScore();
        if (ratio < 0) {
            ratio = 0;
        }
        if (ratio > 1) {
            ratio = 1;
        }
        long bbsScore = 0L;
        //if (!ec.getExamination().getId().equals("7")) {
        bbsScore = (long) (ec.getExamination().getBbsScore() * ratio);
        //}
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
        if (mcqs != null) {
            for (ExamCaseItemMultiChoice c : mcqs) {
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
        }
        //获取填空题答案
        if (fqs != null) {
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
        }
        //
        if (ccqs != null) {
            for (ExamCaseItemCase cc : ccqs) {
                List<ExamCaseItemMultiChoice> mcqs2 = cc.getMultiChoiceItems();
                List<ExamCaseItemFill> fqs2 = cc.getFillItems();

                //获取综合题中的多选题答案
                for (ExamCaseItemMultiChoice c : mcqs2) {
                    List<ExamMultiChoice> ls = c.getQuestion().getChoices();
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
        }

        return ec;
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
        //计算单选题成绩
        for (ExamCaseItemChoice c : cqs) {

            if (c.isIfRight()) {
                choiceScore += exam.getRandomPaper().getChoiceScore();
            }
        }
        //计算多选题成绩
        for (ExamCaseItemMultiChoice c : mcqs) {
            if (c.isIfRight()) {
                multiChoiceScore += exam.getRandomPaper().getMultiChoiceScore();
            }
        }
        //计算填空题成绩
        for (ExamCaseItemFill c : fqs) {
            int t = c.getTotalNum();
            if (t != 0) {
                double dt = ((double) c.getRightNum()) / ((double) c.getTotalNum());
                if (dt > 1) {
                    dt = 1D;
                }
                fillScore += exam.getRandomPaper().getFillScore() * dt;
            }
        }
        //计算判断题成绩
        for (ExamCaseItemJudge c : jqs) {
            if (c.isIfRight()) {
                judgeScore += exam.getRandomPaper().getJudgeScore();
            }
        }
        //计算问答题成绩
        for (ExamCaseItemEssay c : eqs) {
            float ratio = c.getRightRatio();
            if (ratio > 1) {
                ratio = 1;
            }
            essayScore += exam.getRandomPaper().getEssayScore() * ratio;
        }
        //计算文件题成绩
        for (ExamCaseItemFile c : ffqs) {
            float ratio = c.getRightRatio();
            if (ratio > 1) {
                ratio = 1;
            }
            fileScore += exam.getRandomPaper().getFileScore() * ratio;
        }

        //计算每道综合题得分
        for (ExamCaseItemCase cic : ccqs) {
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
                    multiChoiceScoreTemp += exam.getRandomPaper().getMultiChoiceScore();
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
                    fillScoreTemp += exam.getRandomPaper().getFillScore() * dt;
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
        ExamCaseItemFill fill = new ExamCaseItemFill();
        fill.setQuestion(fq);
        ExamCaseService es = new ExamCaseService();
        es.buildItemFillBlocks(fill);
        List<ExamCaseItemFillBlock> blocks = fill.getBlocks();
        for (ExamCaseItemFillBlock block : blocks) {
            MyLogger.echo(block.getPreStr());
            MyLogger.echo(block.getRightAnswer());
        }
        MyLogger.echo(fill.getLastStr());
    }
}
