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
import com.hjedu.examination.dao.IModuleRandom2PartDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.entity.CaseQuestion;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.EssayQuestion;
import com.hjedu.examination.entity.ExamChoice;
import com.hjedu.examination.entity.ExamMultiChoice;
import com.hjedu.examination.entity.ExamRoom;
import com.hjedu.examination.entity.FileQuestion;
import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.examination.entity.JudgeQuestion;
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
import com.hjedu.examination.entity.contest.ContestCaseStub;
import com.hjedu.examination.entity.contest.ExamContestSession;
import com.hjedu.examination.entity.random2.ExamPaperRandom2;
import com.hjedu.examination.entity.random2.ModuleRandom2Part;
import com.hjedu.examination.entity.random2.Random2PaperPart;
import com.hjedu.examination.util.ExamCaseServiceUtils;
import com.huajie.util.HTMLCleaner;
import com.huajie.util.SimilarAlgorithm;
import com.huajie.util.SpringHelper;

public class ContestCaseRandom2Service extends IContestCaseRandom2Service implements Serializable {

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
        while (true) {
            try {
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
            } catch (Exception eee) {
                eee.printStackTrace();
            }
        }
        fill.setBlocks(blocks);
        fill.setLastStr(t);
    }

    private void choiceOrderAdapter(List<ExamChoice> choices) {
        ExamCaseServiceUtils.choiceOrderAdapter(choices);
    }

    private void multiChoiceOrderAdapter(List<ExamMultiChoice> choices) {
        ExamCaseServiceUtils.multiChoiceOrderAdapter(choices);
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
            //ecic.setExamCase(ec);
            ecic.setCaseItem(ecie);
            List<ExamChoice> ecs = c.getChoices();
            //Collections.sort(ecs);
            //配置单选题选项随机
            if (ec.getExamination().isChoiceRandom()&&c.isAllowChoiceRandom()) {
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
            //ecie.setExamCase(ec);
            eciee.setCaseItem(ecie);
            List<ExamMultiChoice> ecs = c.getChoices();
            //Collections.sort(ecs);
            //配置单选题选项随机
            if (ec.getExamination().isMultiChoiceRandom()&&c.isAllowChoiceRandom()) {
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
            //ecie.setExamCase(ec);
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
            //ecie.setExamCase(ec);
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
            //ecie.setExamCase(ec);
            eciee.setCaseItem(ecie);
            eciee.setQuestion(c);
            ecieqs.add(eciee);
        }
        ecie.setEssayItems(ecieqs);
        //向临时变量中加入综合题
        return ecie;
    }

    private void addAdapterForPart(ContestCaseItemAdapter adapter, String partId, List<Random2PaperPart> parts) {
        for (Random2PaperPart part : parts) {
            if (part.getId().equals(partId)) {
                part.getCadapters().add(adapter);
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
        double totalWeight = this.computeTotalWeightForPart(partId, mps);
        double unum = computeUserNum(partId, parts);
        if (totalWeight == 0) {
            totalWeight = 1;
        }
        long cnum = (long) ((w / totalWeight) * unum) + 1;
        return cnum;
    }

    /**
     * 构建竞赛试卷存根，每天内所有参加竞赛者试题一样，第二天开始时根据随机试卷规则重新生成试卷
     * @param paper 随机试卷
     * @return 
     */
    public ContestCaseStub buildExamCaseStub(ExamPaperRandom2 paper) {
        ContestCaseStub stub = new ContestCaseStub();
        //ExamPaperRandom2 paper=exam.getRandom2Paper();

        //List<Random2PaperPart> parts = paper.getParts();
        int uniOrd = 0;//对全部试题统一标号
        //Collections.sort(parts);
        IModuleRandom2PartDAO mpDAO = SpringHelper.getSpringBean("ModuleRandom2PartDAO");
        //IRandom2PaperPartDAO partDAO = SpringHelper.getSpringBean("Random2PaperPartDAO");
        List<ModuleRandom2Part> mps = mpDAO.findModuleRandom2PartByExam(paper.getId());
        stub.getMps().clear();
        stub.getMps().addAll(mps);
        for (ModuleRandom2Part mp : mps) {
            //计算单选题数量并从模块中抽选试题
            long cnum = mp.getChoiceWeight();
            List<ChoiceQuestion> qs1 = this.choiceQuestionDAO.getRandomQuestionOfNumInModule(cnum, mp.getModule().getId());
            System.out.println("取出的单选题数量：" + qs1.size());
            stub.getChoices().put(mp.getId(), qs1);
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中

            //计算单选题数量并从模块中抽选试题
            long cnum2 = mp.getMultiChoiceWeight();
            List<MultiChoiceQuestion> qs2 = this.multiChoiceQuestionDAO.getRandomQuestionOfNumInModule(cnum2, mp.getModule().getId());
            stub.getMchoices().put(mp.getId(), qs2);
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中

            //计算单选题数量并从模块中抽选试题
            long cnum3 = mp.getFillWeight();
            List<FillQuestion> qs3 = this.fillQuestionDAO.getRandomQuestionOfNumInModule(cnum3, mp.getModule().getId());
            stub.getFills().put(mp.getId(), qs3);
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中

            //计算单选题数量并从模块中抽选试题
            long cnum4 = mp.getJudgeWeight();
            List<JudgeQuestion> qs4 = this.judgeQuestionDAO.getRandomQuestionOfNumInModule(cnum4, mp.getModule().getId());
            stub.getJudges().put(mp.getId(), qs4);
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中

            //计算单选题数量并从模块中抽选试题
            long cnum5 = mp.getEssayWeight();
            List<EssayQuestion> qs5 = this.essayQuestionDAO.getRandomQuestionOfNumInModule(cnum5, mp.getModule().getId());
            stub.getEssaies().put(mp.getId(), qs5);
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中

            //计算单选题数量并从模块中抽选试题
            long cnum6 = mp.getFileWeight();
            List<FileQuestion> qs6 = this.fileQuestionDAO.getRandomQuestionOfNumInModule(cnum6, mp.getModule().getId());
            stub.getFiles().put(mp.getId(), qs6);
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中

            //计算综合题数量并从模块中抽选试题
            long cnum7 = mp.getCaseWeight();
            List<CaseQuestion> qs7 = this.caseQuestionDAO.getRandomQuestionOfNumInModule(cnum7, mp.getModule().getId());
            stub.getCases().put(mp.getId(), qs7);
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中

        }
        return stub;

    }

    public void buildExamCaseFromStub(ContestCase ec, ContestCaseStub stub) {
        ExamContestSession exam = ec.getExamination();
        ec.setSessionStr(exam.getSessionStr());
        ExamPaperRandom2 paper = exam.getRandom2Paper();
        ec.setChoiceItems(new ArrayList());
        ec.setMultiChoiceItems(new ArrayList());
        ec.setFillItems(new ArrayList());
        ec.setJudgeItems(new ArrayList());
        ec.setEssayItems(new ArrayList());
        ec.setFileItems(new ArrayList());
        ec.setCaseItems(new ArrayList());

        List<Random2PaperPart> parts = paper.getParts();
        int uniOrd = 0;//对全部试题统一标号
        //Collections.sort(parts);
        //IRandom2PaperPartDAO partDAO = SpringHelper.getSpringBean("Random2PaperPartDAO");
        List<ModuleRandom2Part> mps = stub.getMps();
        for (ModuleRandom2Part mp : mps) {
            //计算单选题数量并从模块中抽选试题
            List<ChoiceQuestion> qs1 = stub.getChoices().get(mp.getId());
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (ChoiceQuestion cq : qs1) {
                ContestCaseItemChoice ei = new ContestCaseItemChoice();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题F
                List<ExamChoice> ecs = ei.getQuestion().getChoices();
                if (ec.getExamination().isChoiceRandom()&&cq.isAllowChoiceRandom()) {
                    this.choiceOrderAdapter(ecs);
                }
                ei.getQuestion().setChoices(ecs);
                ei.setExamCase(ec);
                ei.setPartId(mp.getChoicePartId());
                //System.out.println("PartId from service:"+ei.getPartId());
                ContestCaseItemAdapter adapter = new ContestCaseItemAdapter();
                adapter.setOrd(cq.getOrd());
                adapter.setChoiceItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("choice");
                adapter.setQuestion(cq);
                //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
                adapter.setItem(ei);
                ec.getChoiceItems().add(ei);
                this.addAdapterForPart(adapter, mp.getChoicePartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用

            }
            //计算单选题数量并从模块中抽选试题
            List<MultiChoiceQuestion> qs2 = stub.getMchoices().get(mp.getId());
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (MultiChoiceQuestion cq : qs2) {
                ContestCaseItemMultiChoice ei = new ContestCaseItemMultiChoice();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题F
                List<ExamMultiChoice> ecs = ei.getQuestion().getChoices();
                if (ec.getExamination().isMultiChoiceRandom()&&cq.isAllowChoiceRandom()) {
                    this.multiChoiceOrderAdapter(ecs);
                }
                ei.getQuestion().setChoices(ecs);
                ei.setExamCase(ec);
                ei.setPartId(mp.getMchoicePartId());
                ContestCaseItemAdapter adapter = new ContestCaseItemAdapter();
                adapter.setOrd(cq.getOrd());
                adapter.setMultiChoiceItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("mchoice");
                adapter.setQuestion(cq);
                //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
                adapter.setItem(ei);
                ec.getMultiChoiceItems().add(ei);
                this.addAdapterForPart(adapter, mp.getMchoicePartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
            }

            //计算单选题数量并从模块中抽选试题
            List<FillQuestion> qs3 = stub.getFills().get(mp.getId());
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (FillQuestion cq : qs3) {
                ContestCaseItemFill ei = new ContestCaseItemFill();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题
                ei.setExamCase(ec);
                ei.setPartId(mp.getFillPartId());
                ContestCaseItemAdapter adapter = new ContestCaseItemAdapter();
                adapter.setOrd(cq.getOrd());
                adapter.setFillItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("fill");
                adapter.setQuestion(cq);
                //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
                adapter.setItem(ei);
                ec.getFillItems().add(ei);
                this.addAdapterForPart(adapter, mp.getFillPartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
            }

            //计算单选题数量并从模块中抽选试题
            List<JudgeQuestion> qs4 = stub.getJudges().get(mp.getId());;
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (JudgeQuestion cq : qs4) {
                ContestCaseItemJudge ei = new ContestCaseItemJudge();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题F
                ei.setExamCase(ec);
                ei.setPartId(mp.getJudgePartId());
                ContestCaseItemAdapter adapter = new ContestCaseItemAdapter();
                adapter.setOrd(cq.getOrd());
                adapter.setJudgeItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("judge");
                adapter.setQuestion(cq);
                //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
                adapter.setItem(ei);
                ec.getJudgeItems().add(ei);
                this.addAdapterForPart(adapter, mp.getJudgePartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
            }

            //计算单选题数量并从模块中抽选试题
            List<EssayQuestion> qs5 = stub.getEssaies().get(mp.getId());;
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (EssayQuestion cq : qs5) {
                ContestCaseItemEssay ei = new ContestCaseItemEssay();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题F
                ei.setExamCase(ec);
                ei.setPartId(mp.getEssayPartId());
                ContestCaseItemAdapter adapter = new ContestCaseItemAdapter();
                adapter.setOrd(cq.getOrd());
                adapter.setEssayItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("essay");
                adapter.setQuestion(cq);
                //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
                adapter.setItem(ei);
                ec.getEssayItems().add(ei);
                this.addAdapterForPart(adapter, mp.getEssayPartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
            }

            //计算单选题数量并从模块中抽选试题
            List<FileQuestion> qs6 = stub.getFiles().get(mp.getId());
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (FileQuestion cq : qs6) {
                ContestCaseItemFile ei = new ContestCaseItemFile();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题
                ei.setExamCase(ec);
                ei.setPartId(mp.getFilePartId());
                ContestCaseItemAdapter adapter = new ContestCaseItemAdapter();
                adapter.setOrd(cq.getOrd());
                adapter.setFileItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("file");
                adapter.setQuestion(cq);
                //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
                adapter.setItem(ei);
                ec.getFileItems().add(ei);
                this.addAdapterForPart(adapter, mp.getFilePartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
            }

            //计算综合题数量并从模块中抽选试题
            List<CaseQuestion> qs7 = stub.getCases().get(mp.getId());;
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (CaseQuestion cq : qs7) {
                //ContestCaseItemCase ei = new ContestCaseItemCase();//构建一个考试条目
                ContestCaseItemCase ei = this.buildCaseItem(cq, ec);//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题
                ei.setExamCase(ec);
                ei.setPartId(mp.getCasePartId());
                ContestCaseItemAdapter adapter = new ContestCaseItemAdapter();
                adapter.setOrd(cq.getOrd());
                adapter.setCaseItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("case");
                adapter.setQuestion(cq);
                //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
                adapter.setItem(ei);
                ec.getCaseItems().add(ei);
                this.addAdapterForPart(adapter, mp.getCasePartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
            }
        }
        for (Random2PaperPart part : parts) {
            Collections.sort(part.getCadapters());
        }

        //计算试卷总分
        double totalScore = 0d;

        for (Random2PaperPart part : parts) {
            List<ContestCaseItemAdapter> ads2 = part.getCadapters();
            for (ContestCaseItemAdapter ad : ads2) {
                if (ad.getQtype().equals("choice")) {
                    totalScore += part.getChoiceScore();
                }
                if (ad.getQtype().equals("mchoice")) {
                    totalScore += part.getMultiChoiceScore();
                }
                if (ad.getQtype().equals("fill")) {
                    totalScore += part.getFillScore();
                }
                if (ad.getQtype().equals("judge")) {
                    totalScore += part.getJudgeScore();
                }
                if (ad.getQtype().equals("essay")) {
                    totalScore += part.getEssayScore();
                }
                if (ad.getQtype().equals("file")) {
                    totalScore += part.getFileScore();
                }
                if (ad.getQtype().equals("case")) {
                    ContestCaseItemCase ei = ad.getCaseItem();
                    totalScore += ei.getQuestion().getTotalScore();
                }
            }
        }

        //将试卷满分算出
        ec.setTotalFullScore(totalScore);

        Collections.sort(parts);
        ec.setCparts(parts);//将装载好的parts加入考试中

    }

    public void buildExamCase(ContestCase ec) {
        ExamContestSession exam = ec.getExamination();
        ec.setSessionStr(exam.getSessionStr());
        ExamPaperRandom2 paper = exam.getRandom2Paper();
        ec.setChoiceItems(new ArrayList());
        ec.setMultiChoiceItems(new ArrayList());
        ec.setFillItems(new ArrayList());
        ec.setJudgeItems(new ArrayList());
        ec.setEssayItems(new ArrayList());
        ec.setFileItems(new ArrayList());
        ec.setCaseItems(new ArrayList());

        List<Random2PaperPart> parts = paper.getParts();
        int uniOrd = 0;//对全部试题统一标号
        //Collections.sort(parts);
        IModuleRandom2PartDAO mpDAO = SpringHelper.getSpringBean("ModuleRandom2PartDAO");
        //IRandom2PaperPartDAO partDAO = SpringHelper.getSpringBean("Random2PaperPartDAO");
        List<ModuleRandom2Part> mps = mpDAO.findModuleRandom2PartByExam(exam.getId());
        for (ModuleRandom2Part mp : mps) {
            //计算单选题数量并从模块中抽选试题
            long cnum = computeQuestionNum(mp.getChoiceWeight(), mp.getChoicePartId(), mps, parts);
            List<ChoiceQuestion> qs1 = this.choiceQuestionDAO.getRandomQuestionOfNumInModule(cnum, mp.getModule().getId());
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (ChoiceQuestion cq : qs1) {
                ContestCaseItemChoice ei = new ContestCaseItemChoice();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题F
                List<ExamChoice> ecs = ei.getQuestion().getChoices();
                if (ec.getExamination().isChoiceRandom()&&cq.isAllowChoiceRandom()) {
                    this.choiceOrderAdapter(ecs);
                }
                ei.getQuestion().setChoices(ecs);
                ei.setExamCase(ec);
                ei.setPartId(mp.getChoicePartId());
                ContestCaseItemAdapter adapter = new ContestCaseItemAdapter();
                adapter.setChoiceItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("choice");
                adapter.setQuestion(cq);
                //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
                adapter.setItem(ei);
                //ec.getChoiceItems().add(ei);
                this.addAdapterForPart(adapter, mp.getChoicePartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用

            }
            //计算单选题数量并从模块中抽选试题
            long cnum2 = computeQuestionNum(mp.getMultiChoiceWeight(), mp.getMchoicePartId(), mps, parts);
            List<MultiChoiceQuestion> qs2 = this.multiChoiceQuestionDAO.getRandomQuestionOfNumInModule(cnum2, mp.getModule().getId());
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (MultiChoiceQuestion cq : qs2) {
                ContestCaseItemMultiChoice ei = new ContestCaseItemMultiChoice();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题F
                List<ExamMultiChoice> ecs = ei.getQuestion().getChoices();
                if (ec.getExamination().isMultiChoiceRandom()&&cq.isAllowChoiceRandom()) {
                    this.multiChoiceOrderAdapter(ecs);
                }
                ei.getQuestion().setChoices(ecs);
                ei.setExamCase(ec);
                ei.setPartId(mp.getMchoicePartId());
                ContestCaseItemAdapter adapter = new ContestCaseItemAdapter();
                adapter.setMultiChoiceItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("mchoice");
                adapter.setQuestion(cq);
                //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
                adapter.setItem(ei);
                //ec.getMultiChoiceItems().add(ei);
                this.addAdapterForPart(adapter, mp.getMchoicePartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
            }

            //计算单选题数量并从模块中抽选试题
            long cnum3 = computeQuestionNum(mp.getFillWeight(), mp.getFillPartId(), mps, parts);
            List<FillQuestion> qs3 = this.fillQuestionDAO.getRandomQuestionOfNumInModule(cnum3, mp.getModule().getId());
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (FillQuestion cq : qs3) {
                ContestCaseItemFill ei = new ContestCaseItemFill();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题
                ei.setExamCase(ec);
                ei.setPartId(mp.getFillPartId());
                ContestCaseItemAdapter adapter = new ContestCaseItemAdapter();
                adapter.setFillItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("fill");
                adapter.setQuestion(cq);
                //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
                adapter.setItem(ei);
                //ec.getFillItems().add(ei);
                this.addAdapterForPart(adapter, mp.getFillPartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
            }

            //计算单选题数量并从模块中抽选试题
            long cnum4 = computeQuestionNum(mp.getJudgeWeight(), mp.getJudgePartId(), mps, parts);
            List<JudgeQuestion> qs4 = this.judgeQuestionDAO.getRandomQuestionOfNumInModule(cnum4, mp.getModule().getId());
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (JudgeQuestion cq : qs4) {
                ContestCaseItemJudge ei = new ContestCaseItemJudge();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题F
                ei.setExamCase(ec);
                ei.setPartId(mp.getJudgePartId());
                ContestCaseItemAdapter adapter = new ContestCaseItemAdapter();
                adapter.setJudgeItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("judge");
                adapter.setQuestion(cq);
                //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
                adapter.setItem(ei);
                //ec.getJudgeItems().add(ei);
                this.addAdapterForPart(adapter, mp.getJudgePartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
            }

            //计算单选题数量并从模块中抽选试题
            long cnum5 = computeQuestionNum(mp.getEssayWeight(), mp.getEssayPartId(), mps, parts);
            List<EssayQuestion> qs5 = this.essayQuestionDAO.getRandomQuestionOfNumInModule(cnum5, mp.getModule().getId());
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (EssayQuestion cq : qs5) {
                ContestCaseItemEssay ei = new ContestCaseItemEssay();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题F
                ei.setExamCase(ec);
                ei.setPartId(mp.getEssayPartId());
                ContestCaseItemAdapter adapter = new ContestCaseItemAdapter();
                adapter.setEssayItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("essay");
                adapter.setQuestion(cq);
                //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
                adapter.setItem(ei);
                //ec.getEssayItems().add(ei);
                this.addAdapterForPart(adapter, mp.getEssayPartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
            }

            //计算单选题数量并从模块中抽选试题
            long cnum6 = computeQuestionNum(mp.getFileWeight(), mp.getFilePartId(), mps, parts);
            List<FileQuestion> qs6 = this.fileQuestionDAO.getRandomQuestionOfNumInModule(cnum6, mp.getModule().getId());
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (FileQuestion cq : qs6) {
                ContestCaseItemFile ei = new ContestCaseItemFile();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题
                ei.setExamCase(ec);
                ei.setPartId(mp.getFilePartId());
                ContestCaseItemAdapter adapter = new ContestCaseItemAdapter();
                adapter.setFileItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("file");
                adapter.setQuestion(cq);
                //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
                adapter.setItem(ei);
                //ec.getFileItems().add(ei);
                this.addAdapterForPart(adapter, mp.getFilePartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
            }

            //计算综合题数量并从模块中抽选试题
            long cnum7 = computeQuestionNum(mp.getCaseWeight(), mp.getCasePartId(), mps, parts);
            List<CaseQuestion> qs7 = this.caseQuestionDAO.getRandomQuestionOfNumInModule(cnum7, mp.getModule().getId());
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (CaseQuestion cq : qs7) {
                //ContestCaseItemCase ei = new ContestCaseItemCase();//构建一个考试条目
                ContestCaseItemCase ei = this.buildCaseItem(cq, ec);//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题
                ei.setExamCase(ec);
                ei.setPartId(mp.getCasePartId());
                ContestCaseItemAdapter adapter = new ContestCaseItemAdapter();
                adapter.setCaseItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("case");
                adapter.setQuestion(cq);
                //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
                adapter.setItem(ei);
                //ec.getCaseItems().add(ei);
                this.addAdapterForPart(adapter, mp.getCasePartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
            }
        }

        //计算试卷总分
        double totalScore = 0d;

        for (Random2PaperPart part : parts) {
            List<ContestCaseItemAdapter> ads2 = part.getCadapters();
            for (ContestCaseItemAdapter ad : ads2) {
                if (ad.getQtype().equals("choice")) {
                    totalScore += part.getChoiceScore();
                }
                if (ad.getQtype().equals("mchoice")) {
                    totalScore += part.getMultiChoiceScore();
                }
                if (ad.getQtype().equals("fill")) {
                    totalScore += part.getFillScore();
                }
                if (ad.getQtype().equals("judge")) {
                    totalScore += part.getJudgeScore();
                }
                if (ad.getQtype().equals("essay")) {
                    totalScore += part.getEssayScore();
                }
                if (ad.getQtype().equals("file")) {
                    totalScore += part.getFileScore();
                }
                if (ad.getQtype().equals("case")) {
                    ContestCaseItemCase ei = ad.getCaseItem();
                    totalScore += ei.getQuestion().getTotalScore();
                }
            }
        }

        //将试卷满分算出
        ec.setTotalFullScore(totalScore);

        Collections.sort(parts);

        ec.setCparts(parts);//将装载好的parts加入考试中

    }

    @Override
    public ContestCase resumeExamCase(ContestCase ec) {
        ExamContestSession exam = ec.getExamination();
        ExamPaperRandom2 paper = exam.getRandom2Paper();
        List<ContestCaseItemChoice> cqs = ec.getChoiceItems();
        List<ContestCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
        List<ContestCaseItemFill> fqs = ec.getFillItems();
        List<ContestCaseItemJudge> jqs = ec.getJudgeItems();
        List<ContestCaseItemEssay> eqs = ec.getEssayItems();
        List<ContestCaseItemFile> ffqs = ec.getFileItems();
        List<ContestCaseItemCase> ccqs = ec.getCaseItems();
        List<Random2PaperPart> parts = paper.getParts();

        //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
        for (ContestCaseItemChoice cq : cqs) {
            ContestCaseItemAdapter adapter = new ContestCaseItemAdapter();
            adapter.setOrd(cq.getQuestion().getOrd());
            adapter.setChoiceItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("choice");
            adapter.setQuestion(cq.getQuestion());
            //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
            adapter.setItem(cq);
            adapter.setIndex(cq.getIndex());
            this.addAdapterForPart(adapter, cq.getPartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
        }

        for (ContestCaseItemMultiChoice cq : mcqs) {
            ContestCaseItemAdapter adapter = new ContestCaseItemAdapter();
            adapter.setOrd(cq.getQuestion().getOrd());
            adapter.setMultiChoiceItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("mchoice");
            adapter.setQuestion(cq.getQuestion());
            //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
            adapter.setItem(cq);
            adapter.setIndex(cq.getIndex());
            this.addAdapterForPart(adapter, cq.getPartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
        }
        for (ContestCaseItemFill cq : fqs) {
            ContestCaseItemAdapter adapter = new ContestCaseItemAdapter();
            adapter.setOrd(cq.getQuestion().getOrd());
            adapter.setFillItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("fill");
            adapter.setQuestion(cq.getQuestion());
            //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
            adapter.setItem(cq);
            adapter.setIndex(cq.getIndex());
            this.addAdapterForPart(adapter, cq.getPartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
        }

        for (ContestCaseItemJudge cq : jqs) {
            ContestCaseItemAdapter adapter = new ContestCaseItemAdapter();
            adapter.setOrd(cq.getQuestion().getOrd());
            adapter.setJudgeItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("judge");
            adapter.setQuestion(cq.getQuestion());
            //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
            adapter.setItem(cq);
            adapter.setIndex(cq.getIndex());
            this.addAdapterForPart(adapter, cq.getPartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
        }

        for (ContestCaseItemEssay cq : eqs) {

            ContestCaseItemAdapter adapter = new ContestCaseItemAdapter();
            adapter.setOrd(cq.getQuestion().getOrd());
            adapter.setEssayItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("essay");
            adapter.setQuestion(cq.getQuestion());
            //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
            adapter.setItem(cq);
            adapter.setIndex(cq.getIndex());
            this.addAdapterForPart(adapter, cq.getPartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
        }
        for (ContestCaseItemFile cq : ffqs) {
            ContestCaseItemAdapter adapter = new ContestCaseItemAdapter();
            adapter.setOrd(cq.getQuestion().getOrd());
            adapter.setFileItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("file");
            adapter.setQuestion(cq.getQuestion());
            //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
            adapter.setItem(cq);
            adapter.setIndex(cq.getIndex());
            this.addAdapterForPart(adapter, cq.getPartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
        }

        for (ContestCaseItemCase cq : ccqs) {
            ContestCaseItemAdapter adapter = new ContestCaseItemAdapter();
            adapter.setOrd(cq.getQuestion().getOrd());
            adapter.setCaseItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("case");
            adapter.setQuestion(cq.getQuestion());
            //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
            adapter.setItem(cq);
            adapter.setIndex(cq.getIndex());
            this.addAdapterForPart(adapter, cq.getPartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
        }
        for (Random2PaperPart part : parts) {
            Collections.sort(part.getCadapters());
        }
        Collections.sort(parts);
        ec.setCparts(parts);//将装载好的parts加入考试中
        return ec;
    }

    @Override
    public ContestCase computeExamCase(ContestCase ec) {
        double totalScore = 0;
        double choiceScore = 0;
        double multiChoiceScore = 0;
        double fillScore = 0;
        double judgeScore = 0;
        double essayScore = 0;
        double fileScore = 0;
        double caseScore = 0;

        List<Random2PaperPart> parts = ec.getCparts();

        //Collections.sort(parts);
        for (Random2PaperPart part : parts) {
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

                    ei.setRightAnswer(ei.getQuestion().getAnswer().trim());
                    ei.setIfRight(ei.getRightAnswer().equalsIgnoreCase(ei.getUserAnswer()));
                    if (ei.getIfRight()) {
                        choiceScore += part.getChoiceScore();
                    }
                    //System.out.println("题目：" + ei.getQuestion().getName() + ",选择：" + ei.getUserAnswer() + "，答案：" + ei.getRightAnswer() + "，是否正确：" + ei.getIfRight());
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
                        multiChoiceScore += part.getMultiChoiceScore();
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
                        fillScore += part.getFillScore() * dt;
                    }

                } else if ("judge".equals(adapter.getQtype())) {
                    ContestCaseItemJudge c = (ContestCaseItemJudge) adapter.getJudgeItem();
                    c.setRightAnswer(String.valueOf(c.getQuestion().getAnswer()));
                    c.setIfRight(c.getRightAnswer().equals(c.getUserAnswer()));
                    if (c.getIfRight()) {
                        judgeScore += part.getJudgeScore();
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
                    essayScore += part.getEssayScore() * ratio;

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
                    fileScore += part.getFileScore() * ratio;

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

        ec.setScore(totalScore);
        //ec.setBbsScore(bbsScore);
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

        List<Random2PaperPart> parts = ec.getCparts();

        //Collections.sort(parts);
        for (Random2PaperPart part : parts) {
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
        //double ratio = totalScore / ec.getExaminationTotalScore();
        //long bbsScore = (long) (ec.getExamination().getBbsScore() * ratio);

        ec.setScore(totalScore);
        long oldScore = ec.getBbsScore();
        //ec.setBbsScore(ec.getNewBbsScore());

        //重新改成绩后重新计算用户积分
        IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
        BbsUser bu = ec.getUser();
        //bu.setScore(bu.getScore() + ec.getNewBbsScore() - oldScore);
        //IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
        //bsl.log("管理员重新计算考试得分", (int) (ec.getNewBbsScore() - oldScore), bu);
        //userDAO.updateBbsUser(bu);
        return totalScore;
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
