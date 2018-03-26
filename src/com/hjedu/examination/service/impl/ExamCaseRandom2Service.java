package com.hjedu.examination.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.ICaseQuestionDAO;
import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IEssayQuestionDAO;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.dao.IFillQuestionDAO;
import com.hjedu.examination.dao.IJudgeQuestionDAO;
import com.hjedu.examination.dao.IModuleRandom2PartDAO;
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
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.entity.FileQuestion;
import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.hjedu.examination.entity.VirtualExamPart;
import com.hjedu.examination.entity.random2.ExamPaperRandom2;
import com.hjedu.examination.entity.random2.ModuleRandom2Part;
import com.hjedu.examination.entity.random2.Random2PaperPart;
import com.hjedu.examination.service.IExamCaseService;
import com.hjedu.examination.util.ExamCaseServiceUtils;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.huajie.exam.thread.ExamChoiceStatisticProcessor;
import com.huajie.util.HTMLCleaner;
import com.huajie.util.SimilarAlgorithm;
import com.huajie.util.SpringHelper;

/**
 * 随机试卷考试业务逻辑实现类
 *
 * @author huajie
 */
public class ExamCaseRandom2Service extends IExamCaseService implements Serializable {

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

    public ICaseQuestionDAO getCaseQuestionDAO() {
        return caseQuestionDAO;
    }

    public ThreadPoolTaskExecutor getExec() {
        return exec;
    }

    public void setExec(ThreadPoolTaskExecutor exec) {
        this.exec = exec;
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
     * 将adapter加入vpart,并根据vpart设置自己对应的分值
     *
     * @param adapter
     * @param partId
     * @param parts
     */
    private void addAdapterForPart(ExamCaseItemAdapter adapter, String partId, List<VirtualExamPart> parts) {
        for (VirtualExamPart part : parts) {
            if (part.getId().equals(partId)) {
                adapter.setVirtualPart(part);
                double score = 0;
                if ("choice".equals(adapter.getQtype())) {
                    score = part.getChoiceScore();
                } else if ("mchoice".equals(adapter.getQtype())) {
                    score = part.getMultiChoiceScore();
                } else if ("fill".equals(adapter.getQtype())) {
                    score = part.getFillScore();
                } else if ("judge".equals(adapter.getQtype())) {
                    score = part.getJudgeScore();
                } else if ("essay".equals(adapter.getQtype())) {
                    score = part.getEssayScore();
                } else if ("file".equals(adapter.getQtype())) {
                    score = part.getFileScore();
                }
                adapter.setScore(score);
                part.getAdapters().add(adapter);
                break;
            }
        }
    }

    //获得用户选定的题数
    private int computeUserNum(String partId, List<Random2PaperPart> parts) {
        //long weight = 0;
        for (Random2PaperPart part : parts) {
            if (part.getId().equals(partId)) {
                return part.getUserNum();
            }
        }
        return 0;
    }

    //计算各个大题的总权重
    private long computeTotalWeightForPart(String partId, List<ModuleRandom2Part> mps) {
        long weight = 0;
        for (ModuleRandom2Part mp : mps) {
            if (partId.equals(mp.getChoicePartId())) {
                weight += mp.getChoiceWeight();
            }
            if (partId.equals(mp.getMchoicePartId())) {
                weight += mp.getMultiChoiceWeight();
            }
            if (partId.equals(mp.getFillPartId())) {
                weight += mp.getFillWeight();
            }
            if (partId.equals(mp.getJudgePartId())) {
                weight += mp.getJudgeWeight();
            }
            if (partId.equals(mp.getEssayPartId())) {
                weight += mp.getEssayWeight();
            }
            if (partId.equals(mp.getFilePartId())) {
                weight += mp.getFileWeight();
            }
            if (partId.equals(mp.getCasePartId())) {
                weight += mp.getCaseWeight();
            }
        }
        return weight;
    }

    //计算在某个模块上的某种题型应该取多少
    private long computeQuestionNum(double w, String partId, List<ModuleRandom2Part> mps, List<Random2PaperPart> parts) {
//        double totalWeight = this.computeTotalWeightForPart(partId, mps);
//        double unum = computeUserNum(partId, parts);
//        if (totalWeight == 0) {
//            totalWeight = 1;
//        }
//        long cnum = (long) ((w / totalWeight) * unum) + 1;
        long cnum = (long) w;
        return cnum;
    }

    private List<VirtualExamPart> buildVirtualParts(List<Random2PaperPart> parts) {
        List<VirtualExamPart> vparts = new ArrayList();
        for (Random2PaperPart p : parts) {
            VirtualExamPart vp = new VirtualExamPart();
            //与试卷分段同ID
            vp.setId(p.getId());
            vp.setName(p.getName());
            vp.setDescription(p.getDescription());
            vp.setOrd(p.getOrd());
            vp.setChoiceScore(p.getChoiceScore());
            vp.setMultiChoiceScore(p.getMultiChoiceScore());
            vp.setFillScore(p.getFillScore());
            vp.setJudgeScore(p.getJudgeScore());
            vp.setEssayScore(p.getEssayScore());
            vp.setFileScore(p.getFileScore());
            vp.setItemNum(p.getItemNum());
            vp.setIfShowName(p.isIfShowName());

            vparts.add(vp);
        }
        return vparts;
    }

    @Override
    public void buildExamCase(ExamCase ec) {
        Examination exam = ec.getExamination();
        //ec.setSessionStr(exam.getSessionStr());
        ExamPaperRandom2 paper = exam.getRandom2Paper();

        if (paper == null) {
            return;
        }

        ec.setChoiceItems(new ArrayList());
        ec.setMultiChoiceItems(new ArrayList());
        ec.setFillItems(new ArrayList());
        ec.setJudgeItems(new ArrayList());
        ec.setEssayItems(new ArrayList());
        ec.setFileItems(new ArrayList());
        ec.setCaseItems(new ArrayList());

        double totalScore = 0d;

        List<Random2PaperPart> parts = paper.getParts();
        //由试卷分段构建考试的虚拟分段
        List<VirtualExamPart> vparts = this.buildVirtualParts(parts);

        int uniOrd = 0;//对全部试题统一排序号
        //Collections.sort(parts);
        IModuleRandom2PartDAO mpDAO = SpringHelper.getSpringBean("ModuleRandom2PartDAO");
        //IRandom2PaperPartDAO partDAO = SpringHelper.getSpringBean("Random2PaperPartDAO");
        List<ModuleRandom2Part> mps = mpDAO.findModuleRandom2PartByExam(paper.getId());
        for (ModuleRandom2Part mp : mps) {
            //计算单选题数量并从模块中抽选试题
            long cnum = computeQuestionNum(mp.getChoiceWeight(), mp.getChoicePartId(), mps, parts);
            List<ChoiceQuestion> qs1 = this.choiceQuestionDAO.getRandomQuestionOfNumInModule(cnum, mp.getModule().getId());
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (ChoiceQuestion cq : qs1) {
                ExamCaseItemChoice ei = new ExamCaseItemChoice();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题F
                if (cq == null) {
                    continue;
                }
                List<ExamChoice> ecs = ei.getQuestion().getChoices();
                if (ec.getExamination().isChoiceRandom() && ei.getQuestion().isAllowChoiceRandom()) {
                    this.choiceOrderAdapter(ecs);
                }
                ei.getQuestion().setChoices(ecs);
                ei.setExamCase(ec);
                ei.setPartId(mp.getChoicePartId());
                ExamCaseItemAdapter adapter = new ExamCaseItemAdapter();
                adapter.setChoiceItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("choice");
                adapter.setQuestion(ei.getQuestion());
                adapter.setOrd(ei.getQuestion().getOrd());//随机试卷内小题若需排序将根据试题的顺序排序
                //adapter.setScore(mp.getChoiceScore());//设置adapter的分值
                
                adapter.setItem(ei);
                ec.getChoiceItems().add(ei);
                this.addAdapterForPart(adapter, mp.getChoicePartId(), vparts);//构建ADAPTER并加入PART以备在考试页面使用

            }
            //计算单选题数量并从模块中抽选试题
            long cnum2 = computeQuestionNum(mp.getMultiChoiceWeight(), mp.getMchoicePartId(), mps, parts);
            List<MultiChoiceQuestion> qs2 = this.multiChoiceQuestionDAO.getRandomQuestionOfNumInModule(cnum2, mp.getModule().getId());
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (MultiChoiceQuestion cq : qs2) {
                ExamCaseItemMultiChoice ei = new ExamCaseItemMultiChoice();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题F
                List<ExamMultiChoice> ecs = ei.getQuestion().getChoices();
                if (ec.getExamination().isChoiceRandom() && cq.isAllowChoiceRandom()) {
                    this.multiChoiceOrderAdapter(ecs);
                }
                ei.getQuestion().setChoices(ecs);
                ei.setExamCase(ec);
                ei.setPartId(mp.getMchoicePartId());
                ExamCaseItemAdapter adapter = new ExamCaseItemAdapter();
                adapter.setMultiChoiceItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("mchoice");
                adapter.setQuestion(ei.getQuestion());
                adapter.setOrd(ei.getQuestion().getOrd());
                //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
                adapter.setItem(ei);
                ec.getMultiChoiceItems().add(ei);
                this.addAdapterForPart(adapter, mp.getMchoicePartId(), vparts);//构建ADAPTER并加入PART以备在考试页面使用
            }

            //计算单选题数量并从模块中抽选试题
            long cnum3 = computeQuestionNum(mp.getFillWeight(), mp.getFillPartId(), mps, parts);
            List<FillQuestion> qs3 = this.fillQuestionDAO.getRandomQuestionOfNumInModule(cnum3, mp.getModule().getId());
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (FillQuestion cq : qs3) {
                ExamCaseItemFill ei = new ExamCaseItemFill();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题
                ei.setExamCase(ec);
                ei.setPartId(mp.getFillPartId());
                ExamCaseItemAdapter adapter = new ExamCaseItemAdapter();
                adapter.setFillItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("fill");
                adapter.setQuestion(ei.getQuestion());
                adapter.setOrd(ei.getQuestion().getOrd());
                //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
                adapter.setItem(ei);
                ec.getFillItems().add(ei);
                this.addAdapterForPart(adapter, mp.getFillPartId(), vparts);//构建ADAPTER并加入PART以备在考试页面使用
            }

            //计算单选题数量并从模块中抽选试题
            long cnum4 = computeQuestionNum(mp.getJudgeWeight(), mp.getJudgePartId(), mps, parts);
            List<JudgeQuestion> qs4 = this.judgeQuestionDAO.getRandomQuestionOfNumInModule(cnum4, mp.getModule().getId());
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (JudgeQuestion cq : qs4) {
                ExamCaseItemJudge ei = new ExamCaseItemJudge();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题F
                ei.setExamCase(ec);
                ei.setPartId(mp.getJudgePartId());
                ExamCaseItemAdapter adapter = new ExamCaseItemAdapter();
                adapter.setJudgeItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("judge");
                adapter.setQuestion(ei.getQuestion());
                adapter.setOrd(ei.getQuestion().getOrd());
                //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
                adapter.setItem(ei);
                ec.getJudgeItems().add(ei);
                this.addAdapterForPart(adapter, mp.getJudgePartId(), vparts);//构建ADAPTER并加入PART以备在考试页面使用
            }

            //计算单选题数量并从模块中抽选试题
            long cnum5 = computeQuestionNum(mp.getEssayWeight(), mp.getEssayPartId(), mps, parts);
            List<EssayQuestion> qs5 = this.essayQuestionDAO.getRandomQuestionOfNumInModule(cnum5, mp.getModule().getId());
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (EssayQuestion cq : qs5) {
                ExamCaseItemEssay ei = new ExamCaseItemEssay();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题F
                ei.setExamCase(ec);
                ei.setPartId(mp.getEssayPartId());
                ExamCaseItemAdapter adapter = new ExamCaseItemAdapter();
                adapter.setEssayItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("essay");
                adapter.setQuestion(ei.getQuestion());
                adapter.setOrd(ei.getQuestion().getOrd());
                //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
                adapter.setItem(ei);
                ec.getEssayItems().add(ei);
                this.addAdapterForPart(adapter, mp.getEssayPartId(), vparts);//构建ADAPTER并加入PART以备在考试页面使用
            }

            //计算单选题数量并从模块中抽选试题
            long cnum6 = computeQuestionNum(mp.getFileWeight(), mp.getFilePartId(), mps, parts);
            List<FileQuestion> qs6 = this.fileQuestionDAO.getRandomQuestionOfNumInModule(cnum6, mp.getModule().getId());
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (FileQuestion cq : qs6) {
                ExamCaseItemFile ei = new ExamCaseItemFile();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题
                ei.setExamCase(ec);
                ei.setPartId(mp.getFilePartId());
                ExamCaseItemAdapter adapter = new ExamCaseItemAdapter();
                adapter.setFileItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("file");
                adapter.setQuestion(ei.getQuestion());
                adapter.setOrd(ei.getQuestion().getOrd());
                //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
                adapter.setItem(ei);
                ec.getFileItems().add(ei);
                this.addAdapterForPart(adapter, mp.getFilePartId(), vparts);//构建ADAPTER并加入PART以备在考试页面使用
            }

            //计算综合题数量并从模块中抽选试题
            long cnum7 = computeQuestionNum(mp.getCaseWeight(), mp.getCasePartId(), mps, parts);
            List<CaseQuestion> qs7 = this.caseQuestionDAO.getRandomQuestionOfNumInModule(cnum7, mp.getModule().getId());
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (CaseQuestion cq : qs7) {
                ExamCaseItemCase ei = this.buildCaseItem(cq, ec);//构建一个考试条目
                this.orderCaseItems(ei);
                ei.setPartId(mp.getCasePartId());
                ExamCaseItemAdapter adapter = new ExamCaseItemAdapter();
                adapter.setCaseItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("case");
                adapter.setQuestion(ei.getQuestion());
                adapter.setOrd(ei.getQuestion().getOrd());
                //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
                adapter.setItem(ei);
                ec.getCaseItems().add(ei);
                this.addAdapterForPart(adapter, mp.getCasePartId(), vparts);//构建ADAPTER并加入PART以备在考试页面使用
            }
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

        totalScore = paper.getTotalScore();
        //将试卷满分算出
        ec.setTotalFullScore(totalScore);
        System.out.println("试卷总分：" + totalScore);
        Collections.sort(vparts);
        ec.setVparts(vparts);//将装载好的parts加入考试中
        //计算各大题的得分及总分
 //       ExamCaseServiceUtils.computePartScore(ec);

    }

    @Override
    public ExamCase resumeExamCase(ExamCase ec) {
        if (ec == null) {
            return null;
        }
        Examination exam = ec.getExamination();
        ExamPaperRandom2 paper = exam.getRandom2Paper();
        List<ExamCaseItemChoice> cqs = ec.getChoiceItems();
        List<ExamCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
        List<ExamCaseItemFill> fqs = ec.getFillItems();
        List<ExamCaseItemJudge> jqs = ec.getJudgeItems();
        List<ExamCaseItemEssay> eqs = ec.getEssayItems();
        List<ExamCaseItemFile> ffqs = ec.getFileItems();
        List<ExamCaseItemCase> ccqs = ec.getCaseItems();

        List<Random2PaperPart> parts = paper.getParts();
        List<VirtualExamPart> vparts = this.buildVirtualParts(parts);

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
            adapter.setOrd(cq.getIndex());
            adapter.setChoiceItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("choice");
            adapter.setQuestion(cq.getQuestion());
            //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
            adapter.setItem(cq);
            this.addAdapterForPart(adapter, cq.getPartId(), vparts);//构建ADAPTER并加入PART以备在考试页面使用
        }

        for (ExamCaseItemMultiChoice cq : mcqs) {
            //将多选详情恢复
            if (cq.getQuestion() == null) {
                continue;
            }
            List<ExamMultiChoice> ls = cq.getQuestion().getChoices();
            List<String> labels = new ArrayList();
            String ua = cq.getUserAnswer();
            if (ua != null) {
                char[] uac = ua.toCharArray();

                if (uac != null) {
                    for (char u : uac) {
                        labels.add(String.valueOf(u));
                    }
                }
            }
            cq.setSelectedLabels(labels);
            if (ec.getExamination().isChoiceRandom() && cq.getQuestion().isAllowChoiceRandom()) {
                this.multiChoiceOrderAdapter(ls);
            }

            ExamCaseItemAdapter adapter = new ExamCaseItemAdapter();
            adapter.setOrd(cq.getIndex());
            adapter.setMultiChoiceItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("mchoice");
            adapter.setQuestion(cq.getQuestion());
            //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
            adapter.setItem(cq);
            this.addAdapterForPart(adapter, cq.getPartId(), vparts);//构建ADAPTER并加入PART以备在考试页面使用
        }
        for (ExamCaseItemFill cq : fqs) {
            //将填空详情恢复
            if (cq.getQuestion() == null) {
                continue;
            }
            List<ExamCaseItemFillBlock> ls = cq.getBlocks();
            if (cq.getUserAnswerStr() != null) {
                String ss[] = cq.getUserAnswerStr().split(",");
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
            adapter.setOrd(cq.getIndex());
            adapter.setFillItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("fill");
            adapter.setQuestion(cq.getQuestion());
            //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
            adapter.setItem(cq);
            this.addAdapterForPart(adapter, cq.getPartId(), vparts);//构建ADAPTER并加入PART以备在考试页面使用
        }

        for (ExamCaseItemJudge cq : jqs) {
            if (cq.getQuestion() == null) {
                continue;
            }
            ExamCaseItemAdapter adapter = new ExamCaseItemAdapter();
            adapter.setOrd(cq.getIndex());
            adapter.setJudgeItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("judge");
            adapter.setQuestion(cq.getQuestion());
            //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
            adapter.setItem(cq);
            this.addAdapterForPart(adapter, cq.getPartId(), vparts);//构建ADAPTER并加入PART以备在考试页面使用
        }

        for (ExamCaseItemEssay cq : eqs) {
            if (cq.getQuestion() == null) {
                continue;
            }
            ExamCaseItemAdapter adapter = new ExamCaseItemAdapter();
            adapter.setOrd(cq.getIndex());
            adapter.setEssayItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("essay");
            adapter.setQuestion(cq.getQuestion());
            //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
            adapter.setItem(cq);
            this.addAdapterForPart(adapter, cq.getPartId(), vparts);//构建ADAPTER并加入PART以备在考试页面使用
        }
        for (ExamCaseItemFile cq : ffqs) {
            if (cq.getQuestion() == null) {
                continue;
            }
            ExamCaseItemAdapter adapter = new ExamCaseItemAdapter();
            adapter.setOrd(cq.getIndex());
            adapter.setFileItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("file");
            adapter.setQuestion(cq.getQuestion());
            //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
            adapter.setItem(cq);
            this.addAdapterForPart(adapter, cq.getPartId(), vparts);//构建ADAPTER并加入PART以备在考试页面使用
        }

        for (ExamCaseItemCase cc : ccqs) {
            if (cc.getQuestion() == null) {
                continue;
            }
            ExamCaseItemAdapter adapter = new ExamCaseItemAdapter();
            adapter.setOrd(cc.getIndex());
            adapter.setCaseItem(cc);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("case");
            adapter.setQuestion(cc.getQuestion());
            //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
            adapter.setItem(cc);
            this.addAdapterForPart(adapter, cc.getPartId(), vparts);//构建ADAPTER并加入PART以备在考试页面使用
            this.orderCaseItems(cc);
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
    public ExamCase preSaveExamCase(ExamCase ec) {
        Examination exam = ec.getExamination();
        List<ExamCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
        List<ExamCaseItemFill> fqs = ec.getFillItems();
        List<ExamCaseItemCase> ccqs = ec.getCaseItems();

        //获取多选题答案
        for (ExamCaseItemMultiChoice c : mcqs) {
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

    public boolean computeSingleAdapter(ExamCaseItemAdapter adapter) {
        boolean ifRight = false;
        if ("choice".equals(adapter.getQtype()) || "choice".equals(adapter.getItem().getCaseType())) {//判断试卷条目的类型
            ExamCaseItemChoice ei = adapter.getChoiceItem();//获取考试条目
            List<ExamChoice> ls = ei.getQuestion().getChoices();
            for (ExamChoice e : ls) {
                if (e.isSelected()) {
                    ei.setUserAnswer(e.getLabel().trim());
                    break;
                }
            }

            boolean oldIfRight = ei.isIfRight();//记录原来是否正确（重复提交的情况）
            ei.setRightAnswer(ei.getQuestion().getAnswer().trim());
            ei.setIfRight(ei.getRightAnswer().equalsIgnoreCase(ei.getUserAnswer()));
            //计算错误题数
            int wnum = ei.getExamCase().getWrongNum();
            if (adapter.getItem().isDone()) {
                if (ei.isIfRight() && !oldIfRight) {
                    ei.getExamCase().setWrongNum(--wnum);
                } else if (!ei.isIfRight() && oldIfRight) {
                    ei.getExamCase().setWrongNum(++wnum);
                }
            } else if (!ei.isIfRight()) {
                ei.getExamCase().setWrongNum(++wnum);
            }

            ifRight = ei.isIfRight();
            //System.out.println("题目：" + ei.getQuestion().getName() + ",选择：" + ei.getUserAnswer() + "，答案：" + ei.getRightAnswer() + "，是否正确：" + ei.isIfRight());
        } else if ("mchoice".equals(adapter.getQtype()) || "mchoice".equals(adapter.getItem().getCaseType())) {
            ExamCaseItemMultiChoice c = (ExamCaseItemMultiChoice) adapter.getMultiChoiceItem();
            List<ExamMultiChoice> ls = c.getQuestion().getChoices();
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
            boolean oldIfRight = c.isIfRight();//记录原来是否正确（重复提交的情况）
            c.setRightAnswer(c.getQuestion().getAnswer().trim());
            c.setIfRight(c.getRightAnswer().equalsIgnoreCase(c.getUserAnswer()));

            //计算错误题数
            int wnum = c.getExamCase().getWrongNum();
            if (adapter.getItem().isDone()) {
                if (c.isIfRight() && !oldIfRight) {
                    c.getExamCase().setWrongNum(--wnum);
                } else if (!c.isIfRight() && oldIfRight) {
                    c.getExamCase().setWrongNum(++wnum);
                }
            } else if (!c.isIfRight()) {
                c.getExamCase().setWrongNum(++wnum);
            }

            ifRight = c.isIfRight();

        } else if ("fill".equals(adapter.getQtype()) || "fill".equals(adapter.getItem().getCaseType())) {
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
                if (e.getRightAnswer().trim().replace(" ", "").equals(e.getUserAnswer().trim().replace(" ", ""))) {
                    rightNum++;
                }
            }
            c.setRightNum(rightNum);

            boolean oldIfRight = c.isIfRight();//记录原来是否正确（重复提交的情况）

            c.setRightAnswerStr(as.toString());
            c.setUserAnswerStr(us.toString());
            int t = c.getTotalNum();
            if (t != 0) {
                double dt = ((double) c.getRightNum()) / ((double) c.getTotalNum());
                if (dt > 1) {
                    dt = 1D;
                }
                ifRight = (dt == 1);
            }
            c.setIfRight(ifRight);

            //计算错误题数
            int wnum = c.getExamCase().getWrongNum();
            if (adapter.getItem().isDone()) {
                if (c.isIfRight() && !oldIfRight) {
                    c.getExamCase().setWrongNum(--wnum);
                } else if (!c.isIfRight() && oldIfRight) {
                    c.getExamCase().setWrongNum(++wnum);
                }
            } else if (!c.isIfRight()) {
                c.getExamCase().setWrongNum(++wnum);
            }

        } else if ("judge".equals(adapter.getQtype()) || "judge".equals(adapter.getItem().getCaseType())) {
            ExamCaseItemJudge c = (ExamCaseItemJudge) adapter.getJudgeItem();
            c.setRightAnswer(String.valueOf(c.getQuestion().getAnswer()));
            boolean oldIfRight = c.isIfRight();//记录原来是否正确（重复提交的情况）
            c.setIfRight(c.getRightAnswer().equals(c.getUserAnswer()));

            //计算错误题数
            int wnum = c.getExamCase().getWrongNum();
            if (adapter.getItem().isDone()) {
                if (c.isIfRight() && !oldIfRight) {
                    c.getExamCase().setWrongNum(--wnum);
                } else if (!c.isIfRight() && oldIfRight) {
                    c.getExamCase().setWrongNum(++wnum);
                }
            } else if (!c.isIfRight()) {
                c.getExamCase().setWrongNum(++wnum);
            }

            ifRight = c.isIfRight();

        } else if ("essay".equals(adapter.getQtype()) || "essay".equals(adapter.getItem().getCaseType())) {
            ExamCaseItemEssay c = (ExamCaseItemEssay) adapter.getEssayItem();
            c.setRightAnswer(c.getQuestion().getRightStr());
            float ratio = 0f;
            try {
                ratio = SimilarAlgorithm.getSimilarity(HTMLCleaner.delHTMLTag(c.getQuestion().getAnswer()), HTMLCleaner.delHTMLTag(c.getUserAnswer()));
            } catch (Exception ee) {
                ee.printStackTrace();
            }

            boolean oldIfRight = c.isIfRight();//记录原来是否正确（重复提交的情况）
            c.setRightRatio(ratio);
            ifRight = (c.getRightRatio() >= 0.8);
            c.setIfRight(ifRight);
            //计算错误题数
            int wnum = c.getExamCase().getWrongNum();
            if (adapter.getItem().isDone()) {
                if (c.isIfRight() && !oldIfRight) {
                    c.getExamCase().setWrongNum(--wnum);
                } else if (!c.isIfRight() && oldIfRight) {
                    c.getExamCase().setWrongNum(++wnum);
                }
            } else if (!c.isIfRight()) {
                c.getExamCase().setWrongNum(++wnum);
            }

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

            boolean oldIfRight = c.isIfRight();//记录原来是否正确（重复提交的情况）
            ifRight = (c.getRightRatio() >= 0.8);
            //计算错误题数
            int wnum = c.getExamCase().getWrongNum();
            if (adapter.getItem().isDone()) {
                if (c.isIfRight() && !oldIfRight) {
                    c.getExamCase().setWrongNum(--wnum);
                } else if (!c.isIfRight() && oldIfRight) {
                    c.getExamCase().setWrongNum(++wnum);
                }
            } else if (!c.isIfRight()) {
                c.getExamCase().setWrongNum(++wnum);
            }
        }

        return ifRight;
    }

    @Override
    public ExamCase computeExamCase(ExamCase ec) {
        double totalScore = 0;
        double choiceScore = 0;
        double multiChoiceScore = 0;
        double fillScore = 0;
        double judgeScore = 0;
        double essayScore = 0;
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

                    ei.setRightAnswer(ei.getQuestion().getAnswer().trim());
                    ei.setIfRight(ei.getRightAnswer().equalsIgnoreCase(ei.getUserAnswer()));
                    if (ei.isIfRight()) {
                        choiceScore += part.getChoiceScore();
                    }
                    //System.out.println("题目：" + ei.getQuestion().getName() + ",选择：" + ei.getUserAnswer() + "，答案：" + ei.getRightAnswer() + "，是否正确：" + ei.isIfRight());
                } else if ("mchoice".equals(adapter.getQtype())) {
                    ExamCaseItemMultiChoice c = (ExamCaseItemMultiChoice) adapter.getMultiChoiceItem();
                    if (c.getQuestion().getAnswer() == null) {
                        continue;
                    }
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
                    if (c.isIfRight()) {
                        multiChoiceScore += part.getMultiChoiceScore();
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
                        fillScore += part.getFillScore() * dt;
                    }

                } else if ("judge".equals(adapter.getQtype())) {
                    ExamCaseItemJudge c = (ExamCaseItemJudge) adapter.getJudgeItem();
                    c.setRightAnswer(String.valueOf(c.getQuestion().getAnswer()));
                    c.setIfRight(c.getRightAnswer().equals(c.getUserAnswer()));
                    if (c.isIfRight()) {
                        judgeScore += part.getJudgeScore();
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
                    essayScore += part.getEssayScore() * ratio;

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
                    fileScore += part.getFileScore() * ratio;

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

        ec.setScore(totalScore);
        long oldScore = ec.getBbsScore();
        ec.setBbsScore(ec.getNewBbsScore());

        //计算各大题的得分及总分
        ExamCaseServiceUtils.computePartScore(ec);

        //重新改成绩后重新计算用户积分
        IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
        BbsUser bu = ec.getUser();
        IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
        bsl.log("管理员重新计算考试得分", (int) (ec.getNewBbsScore() - oldScore), bu);
        userDAO.updateBbsUser(bu);
        return totalScore;
    }

    public static void main(String args[]) {
    }
}
