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
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.dao.IFillQuestionDAO;
import com.hjedu.examination.dao.IJudgeQuestionDAO;
import com.hjedu.examination.dao.IModuleExamCaseDAO;
import com.hjedu.examination.dao.IModuleModule2PartDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.entity.CaseQuestion;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.EssayQuestion;
import com.hjedu.examination.entity.ExamChoice;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.ExamMultiChoice;
import com.hjedu.examination.entity.ExamRoom;
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
import com.hjedu.examination.entity.module.ModuleGeneralExamCaseItem;
import com.hjedu.examination.entity.module2.ModuleExam2CaseItemAdapter;
import com.hjedu.examination.entity.module2.ModuleExam2Part;
import com.hjedu.examination.entity.module2.ModuleExamination2;
import com.hjedu.examination.entity.module2.ModuleModule2Part;
import com.hjedu.examination.service.IModuleExamCaseService;
import com.hjedu.examination.util.ExamCaseServiceUtils;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.huajie.util.HTMLCleaner;
import com.huajie.util.SimilarAlgorithm;
import com.huajie.util.SpringHelper;

public class Module2ExamCaseService implements Serializable, IModuleExamCaseService {

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
    ThreadPoolTaskExecutor exec;

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

    private void choiceOrderAdapter(List<ExamChoice> choices) {
        ExamCaseServiceUtils.choiceOrderAdapter(choices);
    }

    private void multiChoiceOrderAdapter(List<ExamMultiChoice> choices) {
        ExamCaseServiceUtils.multiChoiceOrderAdapter(choices);
    }

    private ModuleExamCaseItemCase buildCaseItem(CaseQuestion j, ModuleExamCase ec) {

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
//            if (ec.getExamination().isChoiceRandom()&&cq.isAllowChoiceRandom()) {
//                this.choiceOrderAdapter(ecs);
//            }
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
//            if (ec.getExamination().isMultiChoiceRandom()&&cq.isAllowChoiceRandom()) {
//                this.multiChoiceOrderAdapter(ecs);
//            }
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
        return ecie;
    }

    private void addAdapterForPart(ModuleExam2CaseItemAdapter adapter, String partId, List<ModuleExam2Part> parts) {
        for (ModuleExam2Part part : parts) {
            if (part.getId().equals(partId)) {
                adapter.setModuleExam2Part(part);
                part.getAdapters().add(adapter);
                break;
            }
        }
    }

    //获得用户选定的题数
    private int computeUserNum(String partId, List<ModuleExam2Part> parts) {
        //long weight = 0;
        for (ModuleExam2Part part : parts) {
            if (part.getId().equals(partId)) {
                return part.getUserNum();
            }
        }
        return 0;
    }

    //计算各个大题的总权重
    private long computeTotalWeightForPart(String partId, List<ModuleModule2Part> mps) {
        long weight = 0;
        for (ModuleModule2Part mp : mps) {
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

    /**
     * 计算在某个模块上的某种题型应该取多少
     *
     * @param w
     * @param partId
     * @param mps
     * @param parts
     * @return
     */
    private long computeQuestionNum(double w, String partId, List<ModuleModule2Part> mps, List<ModuleExam2Part> parts) {
//        double totalWeight = this.computeTotalWeightForPart(partId, mps);
//        double unum = computeUserNum(partId, parts);
//        if (totalWeight == 0) {
//            totalWeight = 1;
//        }
//        long cnum = (long) ((w / totalWeight) * unum) + 1;
        long cnum = (long) w;
        return cnum;
    }

    /**
     * 为一个考试实例装载试题
     *
     * @param ec 考试实例，此方法将为此考试装载试题
     */
    @Override
    public void buildExamCase(ModuleExamCase ec) {

        ModuleExamination2 exam = ec.getExamination();
        //ec.setSessionStr(exam.getSessionStr());
        ec.setChoiceItems(new ArrayList());
        ec.setMultiChoiceItems(new ArrayList());
        ec.setFillItems(new ArrayList());
        ec.setJudgeItems(new ArrayList());
        ec.setEssayItems(new ArrayList());
        ec.setFileItems(new ArrayList());
        ec.setCaseItems(new ArrayList());

        double totalScore = 0d;

        List<ModuleExam2Part> parts = exam.getParts();
        int uniOrd = 0;//对全部试题统一标号
        //Collections.sort(parts);
        IModuleModule2PartDAO mpDAO = SpringHelper.getSpringBean("ModuleModule2PartDAO");
        //IModuleExam2PartDAO partDAO = SpringHelper.getSpringBean("ModuleExam2PartDAO");
        List<ModuleModule2Part> mps = mpDAO.findModuleModule2PartByExam(exam.getId());
        for (ModuleModule2Part mp : mps) {
            //计算单选题数量并从模块中抽选试题
            //long cnum = computeQuestionNum(mp.getChoiceWeight(), mp.getChoicePartId(), mps, parts);
            ExamModuleModel em = mp.getModule();
            if (em == null) {
                continue;
            }
            List<ChoiceQuestion> qs1 = this.choiceQuestionDAO.findChoiceQuestionByModule(em.getId());
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (ChoiceQuestion cq : qs1) {
                ModuleExamCaseItemChoice ei = new ModuleExamCaseItemChoice();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题F
                List<ExamChoice> ecs = ei.getQuestion().getChoices();
                if (ec.getExamination().isChoiceRandom() && cq.isAllowChoiceRandom()) {
                    this.choiceOrderAdapter(ecs);
                }
                ei.getQuestion().setChoices(ecs);
                ei.setExamCase(ec);
                ei.setPartId(mp.getChoicePartId());
                ModuleExam2CaseItemAdapter adapter = new ModuleExam2CaseItemAdapter();
                adapter.setChoiceItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("choice");
                adapter.setQuestion(ei.getQuestion());
                //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
                adapter.setItem(ei);
                //ec.getChoiceItems().add(ei);
                this.addAdapterForPart(adapter, mp.getChoicePartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用

            }
            //计算单选题数量并从模块中抽选试题
            //long cnum2 = computeQuestionNum(mp.getMultiChoiceWeight(), mp.getMchoicePartId(), mps, parts);
            List<MultiChoiceQuestion> qs2 = this.multiChoiceQuestionDAO.findMultiChoiceQuestionByModule(mp.getModule().getId());
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (MultiChoiceQuestion cq : qs2) {
                ModuleExamCaseItemMultiChoice ei = new ModuleExamCaseItemMultiChoice();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题F
                List<ExamMultiChoice> ecs = ei.getQuestion().getChoices();
                if (ec.getExamination().isMultiChoiceRandom() && cq.isAllowChoiceRandom()) {
                    this.multiChoiceOrderAdapter(ecs);
                }
                ei.getQuestion().setChoices(ecs);
                ei.setExamCase(ec);
                ei.setPartId(mp.getMchoicePartId());
                ModuleExam2CaseItemAdapter adapter = new ModuleExam2CaseItemAdapter();
                adapter.setMultiChoiceItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("mchoice");
                adapter.setQuestion(ei.getQuestion());
                //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
                adapter.setItem(ei);
                //ec.getMultiChoiceItems().add(ei);
                this.addAdapterForPart(adapter, mp.getMchoicePartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
            }

            //计算单选题数量并从模块中抽选试题
            //long cnum3 = computeQuestionNum(mp.getFillWeight(), mp.getFillPartId(), mps, parts);
            List<FillQuestion> qs3 = this.fillQuestionDAO.findFillQuestionByModule(mp.getModule().getId());
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (FillQuestion cq : qs3) {
                ModuleExamCaseItemFill ei = new ModuleExamCaseItemFill();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题
                ei.setExamCase(ec);
                ei.setPartId(mp.getFillPartId());
                ModuleExam2CaseItemAdapter adapter = new ModuleExam2CaseItemAdapter();
                adapter.setFillItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("fill");
                adapter.setQuestion(ei.getQuestion());
                //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
                adapter.setItem(ei);
                //ec.getFillItems().add(ei);
                this.addAdapterForPart(adapter, mp.getFillPartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
            }

            //计算单选题数量并从模块中抽选试题
            //long cnum4 = computeQuestionNum(mp.getJudgeWeight(), mp.getJudgePartId(), mps, parts);
            List<JudgeQuestion> qs4 = this.judgeQuestionDAO.findJudgeQuestionByModule(mp.getModule().getId());
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (JudgeQuestion cq : qs4) {
                ModuleExamCaseItemJudge ei = new ModuleExamCaseItemJudge();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题F
                ei.setExamCase(ec);
                ei.setPartId(mp.getJudgePartId());
                ModuleExam2CaseItemAdapter adapter = new ModuleExam2CaseItemAdapter();
                adapter.setJudgeItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("judge");
                adapter.setQuestion(ei.getQuestion());
                //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
                adapter.setItem(ei);
                //ec.getJudgeItems().add(ei);
                this.addAdapterForPart(adapter, mp.getJudgePartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
            }

            //计算单选题数量并从模块中抽选试题
            long cnum5 = computeQuestionNum(mp.getEssayWeight(), mp.getEssayPartId(), mps, parts);
            List<EssayQuestion> qs5 = this.essayQuestionDAO.findEssayQuestionByModule(mp.getModule().getId());
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (EssayQuestion cq : qs5) {
                ModuleExamCaseItemEssay ei = new ModuleExamCaseItemEssay();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题F
                ei.setExamCase(ec);
                ei.setPartId(mp.getEssayPartId());
                ModuleExam2CaseItemAdapter adapter = new ModuleExam2CaseItemAdapter();
                adapter.setEssayItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("essay");
                adapter.setQuestion(ei.getQuestion());
                //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
                adapter.setItem(ei);
                //ec.getEssayItems().add(ei);
                this.addAdapterForPart(adapter, mp.getEssayPartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
            }

            //计算单选题数量并从模块中抽选试题
            long cnum6 = computeQuestionNum(mp.getFileWeight(), mp.getFilePartId(), mps, parts);
            List<FileQuestion> qs6 = this.fileQuestionDAO.findFileQuestionByModule(mp.getModule().getId());
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (FileQuestion cq : qs6) {
                ModuleExamCaseItemFile ei = new ModuleExamCaseItemFile();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题
                ei.setExamCase(ec);
                ei.setPartId(mp.getFilePartId());
                ModuleExam2CaseItemAdapter adapter = new ModuleExam2CaseItemAdapter();
                adapter.setFileItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("file");
                adapter.setQuestion(ei.getQuestion());
                //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
                adapter.setItem(ei);
                //ec.getFileItems().add(ei);
                this.addAdapterForPart(adapter, mp.getFilePartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
            }

            //计算综合题数量并从模块中抽选试题
            long cnum7 = computeQuestionNum(mp.getCaseWeight(), mp.getCasePartId(), mps, parts);
            List<CaseQuestion> qs7 = this.caseQuestionDAO.findCaseQuestionByModule(mp.getModule().getId());
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (CaseQuestion cq : qs7) {
                ModuleExamCaseItemCase ei = this.buildCaseItem(cq, ec);//构建一个考试条目
                ei.setPartId(mp.getCasePartId());
                ModuleExam2CaseItemAdapter adapter = new ModuleExam2CaseItemAdapter();
                adapter.setCaseItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("case");
                adapter.setQuestion(ei.getQuestion());
                //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
                adapter.setItem(ei);
                //ec.getCaseItems().add(ei);
                this.addAdapterForPart(adapter, mp.getCasePartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
            }
        }
        for (ModuleExam2Part part : parts) {
            List<ModuleExam2CaseItemAdapter> ads2 = part.getAdapters();
            for (ModuleExam2CaseItemAdapter ad : ads2) {
                if (ad.getQtype().equals("choice")) {
                    ModuleExamCaseItemChoice ei = ad.getChoiceItem();
                    ec.getChoiceItems().add(ei);
                    totalScore += part.getChoiceScore();
                }
                if (ad.getQtype().equals("mchoice")) {
                    ModuleExamCaseItemMultiChoice ei = ad.getMultiChoiceItem();
                    ec.getMultiChoiceItems().add(ei);
                    totalScore += part.getMultiChoiceScore();
                }
                if (ad.getQtype().equals("fill")) {
                    ModuleExamCaseItemFill ei = ad.getFillItem();
                    ec.getFillItems().add(ei);
                    totalScore += part.getFillScore();
                }
                if (ad.getQtype().equals("judge")) {
                    ModuleExamCaseItemJudge ei = ad.getJudgeItem();
                    ec.getJudgeItems().add(ei);
                    totalScore += part.getJudgeScore();
                }
                if (ad.getQtype().equals("essay")) {
                    ModuleExamCaseItemEssay ei = ad.getEssayItem();
                    ec.getEssayItems().add(ei);
                    totalScore += part.getEssayScore();
                }
                if (ad.getQtype().equals("file")) {
                    ModuleExamCaseItemFile ei = ad.getFileItem();
                    ec.getFileItems().add(ei);
                    totalScore += part.getFileScore();
                }
                if (ad.getQtype().equals("case")) {
                    ModuleExamCaseItemCase ei = ad.getCaseItem();
                    ec.getCaseItems().add(ei);
                    totalScore += ei.getQuestion().getTotalScore();
                }
            }

        }
        for (ModuleExam2Part part : parts) {
            //Collections.sort(part.getCadapters());
        }

        //将试卷满分算出
        ec.setTotalFullScore(totalScore);
        System.out.println("试卷总分：" + totalScore);

        Collections.sort(parts);

        ec.setCparts(parts);//将装载好的parts加入考试中

    }

    /**
     * 恢复考试实例，主要用于续考和查看作答详情
     *
     * @param ec 考试实例
     * @return 返回考试实例
     */
    @Override
    public ModuleExamCase resumeExamCase(ModuleExamCase ec) {
        if (ec == null) {
            return null;
        }
        ModuleExamination2 exam = ec.getExamination();
        List<ModuleExamCaseItemChoice> cqs = ec.getChoiceItems();
        List<ModuleExamCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
        List<ModuleExamCaseItemFill> fqs = ec.getFillItems();
        List<ModuleExamCaseItemJudge> jqs = ec.getJudgeItems();
        List<ModuleExamCaseItemEssay> eqs = ec.getEssayItems();
        List<ModuleExamCaseItemFile> ffqs = ec.getFileItems();
        List<ModuleExamCaseItemCase> ccqs = ec.getCaseItems();
        System.out.println("module exam2:" + exam);
        List<ModuleExam2Part> parts = exam.getParts();

        //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
        for (ModuleExamCaseItemChoice cq : cqs) {

            ModuleExam2CaseItemAdapter adapter = new ModuleExam2CaseItemAdapter();
            adapter.setOrd(cq.getIndex());
            adapter.setChoiceItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("choice");
            //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
            adapter.setItem(cq);
            adapter.setQuestion(cq.getQuestion());
            this.addAdapterForPart(adapter, cq.getPartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
        }

        for (ModuleExamCaseItemMultiChoice cq : mcqs) {
            //将多选详情恢复
            List<ExamMultiChoice> ls = cq.getQuestion().getChoices();
            String ua = cq.getUserAnswer();
            List<String> labels = new ArrayList();
            if (ua != null) {
                char[] uac = ua.toCharArray();
                if (uac != null) {
                    for (char u : uac) {
                        labels.add(String.valueOf(u));
                    }
                }
            }
            cq.setSelectedLabels(labels);
//            if (ua != null) {
//                for (ExamMultiChoice e : ls) {
//                    if (ua.toLowerCase().contains(e.getLabel().toLowerCase())) {
//                        e.setSelected(true);
//                    }
//                }
//            }
            ModuleExam2CaseItemAdapter adapter = new ModuleExam2CaseItemAdapter();
            adapter.setOrd(cq.getIndex());
            adapter.setMultiChoiceItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("mchoice");
            adapter.setQuestion(cq.getQuestion());
            //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
            adapter.setItem(cq);
            this.addAdapterForPart(adapter, cq.getPartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
        }
        for (ModuleExamCaseItemFill cq : fqs) {
            //将填空详情恢复
            List<ModuleExamCaseItemFillBlock> ls = cq.getBlocks();
            if (cq.getUserAnswerStr() != null) {
                String ss[] = cq.getUserAnswerStr().split(",");
                int i = 0;
                for (ModuleExamCaseItemFillBlock e : ls) {
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
            ModuleExam2CaseItemAdapter adapter = new ModuleExam2CaseItemAdapter();
            adapter.setOrd(cq.getIndex());
            adapter.setFillItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("fill");
            //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
            adapter.setItem(cq);
            adapter.setQuestion(cq.getQuestion());
            this.addAdapterForPart(adapter, cq.getPartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
        }

        for (ModuleExamCaseItemJudge cq : jqs) {
            ModuleExam2CaseItemAdapter adapter = new ModuleExam2CaseItemAdapter();
            adapter.setOrd(cq.getIndex());
            adapter.setJudgeItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("judge");
            //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
            adapter.setItem(cq);
            adapter.setQuestion(cq.getQuestion());
            this.addAdapterForPart(adapter, cq.getPartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
        }

        for (ModuleExamCaseItemEssay cq : eqs) {

            ModuleExam2CaseItemAdapter adapter = new ModuleExam2CaseItemAdapter();
            adapter.setOrd(cq.getIndex());
            adapter.setEssayItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("essay");
            adapter.setQuestion(cq.getQuestion());
            //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
            adapter.setItem(cq);
            this.addAdapterForPart(adapter, cq.getPartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
        }
        for (ModuleExamCaseItemFile cq : ffqs) {
            ModuleExam2CaseItemAdapter adapter = new ModuleExam2CaseItemAdapter();
            adapter.setOrd(cq.getIndex());
            adapter.setFileItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("file");
            //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
            adapter.setQuestion(cq.getQuestion());
            adapter.setItem(cq);
            this.addAdapterForPart(adapter, cq.getPartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
        }

        for (ModuleExamCaseItemCase cq : ccqs) {
            ModuleExam2CaseItemAdapter adapter = new ModuleExam2CaseItemAdapter();
            adapter.setOrd(cq.getIndex());
            adapter.setCaseItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("case");
            adapter.setQuestion(cq.getQuestion());
            //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
            adapter.setItem(cq);
            this.addAdapterForPart(adapter, cq.getPartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
        }
        for (ModuleExam2Part part : parts) {
            Collections.sort(part.getAdapters());
        }

        Collections.sort(parts);

        ec.setCparts(parts);//将装载好的parts加入考试中
        return ec;
    }

    @Override
    public ModuleExamCase preSaveExamCase(ModuleExamCase ec) {
        ModuleExamination2 exam = ec.getExamination();
        List<ModuleExamCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
        List<ModuleExamCaseItemFill> fqs = ec.getFillItems();
        List<ModuleExamCaseItemCase> ccqs = ec.getCaseItems();

        //获取多选题答案
        for (ModuleExamCaseItemMultiChoice c : mcqs) {
            List<ExamMultiChoice> ls = c.getQuestion().getChoices();
            StringBuilder sb = new StringBuilder();
//            for (ExamMultiChoice e : ls) {
//                if (e.isSelected()) {
//                    sb.append(e.getLabel());
//                }
//            }
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

    /**
     * 试算并重新适配某章节的错题数
     *
     * @param ei
     * @param oldIfRight
     */
    private void computWrongNum(ModuleGeneralExamCaseItem ei, boolean oldIfRight) {
        try {
            int wnum = ei.getExamCase().getWrongNum();
            int dnum = ei.getExamCase().getDoneNum();
            if (ei.isDone()) {
                if (ei.getIfRight() && !oldIfRight) {
                    ei.getExamCase().setWrongNum(--wnum);
                } else if (!ei.getIfRight() && oldIfRight) {
                    ei.getExamCase().setWrongNum(++wnum);
                }
            } else {
                ei.getExamCase().setDoneNum(++dnum);
                if (!ei.getIfRight()) {
                    ei.getExamCase().setWrongNum(++wnum);
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * 验证单个题目正确与否，并记录当前的进度，重新适配错题数
     *
     * @param adapter
     * @return
     */
    @Override
    public ModuleExam2CaseItemAdapter computeSingleAdapter(ModuleExam2CaseItemAdapter adapter) {
        boolean ifRight = false;
        if ("choice".equals(adapter.getQtype()) || "choice".equals(adapter.getItem().getCaseType())) {//判断试卷条目的类型
            ModuleExamCaseItemChoice ei = adapter.getChoiceItem();//获取考试条目
            List<ExamChoice> ls = ei.getQuestion().getChoices();
            for (ExamChoice e : ls) {
                if (e.isSelected()) {
                    ei.setUserAnswer(e.getLabel().trim());
                    break;
                }
            }

            boolean oldIfRight = ei.getIfRight();//记录原来是否正确（重复提交的情况）
            ei.setRightAnswer(ei.getQuestion().getAnswer().trim());
            ei.setIfRight(ei.getRightAnswer().equalsIgnoreCase(ei.getUserAnswer()));
            //计算错误题数
            this.computWrongNum(ei, oldIfRight);
            ifRight = ei.getIfRight();
            //System.out.println("题目：" + ei.getQuestion().getName() + ",选择：" + ei.getUserAnswer() + "，答案：" + ei.getRightAnswer() + "，是否正确：" + ei.getIfRight());
        } else if ("mchoice".equals(adapter.getQtype()) || "mchoice".equals(adapter.getItem().getCaseType())) {
            ModuleExamCaseItemMultiChoice c = (ModuleExamCaseItemMultiChoice) adapter.getMultiChoiceItem();
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
            boolean oldIfRight = c.getIfRight();//记录原来是否正确（重复提交的情况）
            c.setRightAnswer(c.getQuestion().getAnswer().trim());
            c.setIfRight(c.getRightAnswer().equalsIgnoreCase(c.getUserAnswer()));

            //计算错误题数
            this.computWrongNum(c, oldIfRight);

            ifRight = c.getIfRight();

        } else if ("fill".equals(adapter.getQtype()) || "fill".equals(adapter.getItem().getCaseType())) {
            ModuleExamCaseItemFill c = (ModuleExamCaseItemFill) adapter.getFillItem();
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

            boolean oldIfRight = c.getIfRight();//记录原来是否正确（重复提交的情况）

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
            this.computWrongNum(c, oldIfRight);

        } else if ("judge".equals(adapter.getQtype()) || "judge".equals(adapter.getItem().getCaseType())) {
            ModuleExamCaseItemJudge c = (ModuleExamCaseItemJudge) adapter.getJudgeItem();
            c.setRightAnswer(String.valueOf(c.getQuestion().getAnswer()));
            boolean oldIfRight = c.getIfRight();//记录原来是否正确（重复提交的情况）
            c.setIfRight(c.getRightAnswer().equals(c.getUserAnswer()));

            //计算错误题数
            this.computWrongNum(c, oldIfRight);
            ifRight = c.getIfRight();

        } else if ("essay".equals(adapter.getQtype()) || "essay".equals(adapter.getItem().getCaseType())) {
            ModuleExamCaseItemEssay c = (ModuleExamCaseItemEssay) adapter.getEssayItem();
            c.setRightAnswer(c.getQuestion().getRightStr());
            float ratio = 0f;
            try {
                ratio = SimilarAlgorithm.getSimilarity(HTMLCleaner.delHTMLTag(c.getQuestion().getAnswer()), HTMLCleaner.delHTMLTag(c.getUserAnswer()));
            } catch (Exception ee) {
                ee.printStackTrace();
            }

            boolean oldIfRight = c.getIfRight();//记录原来是否正确（重复提交的情况）
            c.setRightRatio(ratio);
            ifRight = (c.getRightRatio() >= 0.8);
            c.setIfRight(ifRight);
            //计算错误题数
            this.computWrongNum(c, oldIfRight);

        } else if ("file".equals(adapter.getQtype())) {
            ModuleExamCaseItemFile c = (ModuleExamCaseItemFile) adapter.getFileItem();
            c.setRightAnswer(c.getQuestion().getRightStr());
            float ratio = 0f;
            try {
                ratio = SimilarAlgorithm.getSimilarity(HTMLCleaner.delHTMLTag(c.getQuestion().getAnswer()), HTMLCleaner.delHTMLTag(c.getUserAnswer()));
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            c.setRightRatio(ratio);

            boolean oldIfRight = c.getIfRight();//记录原来是否正确（重复提交的情况）
            ifRight = (c.getRightRatio() >= 0.8);
            //计算错误题数
            this.computWrongNum(c, oldIfRight);
        }

        return adapter;
    }

    /**
     * 此方法用于章节练习中的试题批量保存与答案验证
     *
     * @param ec
     * @return
     */
    @Override
    public ModuleExamCase computeExamCase(ModuleExamCase ec) {
        //double totalScore = 0;
        //double choiceScore = 0;
        //double multiChoiceScore = 0;
        //double fillScore = 0;
        //double judgeScore = 0;
        //double essayScore = 0;
        //double fileScore = 0;
        //double caseScore = 0;
        int twrong = 0;
        int tdone = 0;
        List<ModuleExam2Part> parts = ec.getCparts();

        //Collections.sort(parts);
        for (ModuleExam2Part part : parts) {
            //List<ManualPartItem> items = part.getItems();
            List<ModuleExam2CaseItemAdapter> adapters = part.getAdapters();
            for (ModuleExam2CaseItemAdapter adapter : adapters) {
                //if (!adapter.getItem().isDone()) {
                if ("choice".equals(adapter.getQtype())) {//判断试卷条目的类型
                    ModuleExamCaseItemChoice ei = adapter.getChoiceItem();//获取考试条目
                    List<ExamChoice> ls = ei.getQuestion().getChoices();
                    for (ExamChoice e : ls) {
                        if (e.isSelected()) {
                            ei.setUserAnswer(e.getLabel().trim());
                            break;
                        }
                    }

                    ei.setRightAnswer(ei.getQuestion().getAnswer().trim());
                    ei.setIfRight(ei.getRightAnswer().equalsIgnoreCase(ei.getUserAnswer()));
                    //未做的题不进行错题统计
                    if (!"".equals(ei.getUserAnswer()) && ei.getUserAnswer() != null) {
                        //this.computWrongNum(ei, false);
                        ei.setDone(true);
                        tdone++;
                        if (!ei.getIfRight()) {
                            twrong++;
                        }
                    } else {
                        ei.setDone(false);
                    }
                    //System.out.println("题目：" + ei.getQuestion().getName() + ",选择：" + ei.getUserAnswer() + "，答案：" + ei.getRightAnswer() + "，是否正确：" + ei.getIfRight());
                } else if ("mchoice".equals(adapter.getQtype())) {
                    ModuleExamCaseItemMultiChoice c = (ModuleExamCaseItemMultiChoice) adapter.getMultiChoiceItem();
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

                    if (!"".equals(c.getUserAnswer()) && c.getUserAnswer() != null) {
                        //this.computWrongNum(c, false);
                        c.setDone(true);
                        tdone++;
                        if (!c.getIfRight()) {
                            twrong++;
                        }
                    } else {
                        c.setDone(false);
                    }

                } else if ("fill".equals(adapter.getQtype())) {
                    ModuleExamCaseItemFill c = (ModuleExamCaseItemFill) adapter.getFillItem();
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
                    boolean ifRight = false;
                    int t = c.getTotalNum();
                    if (t != 0) {
                        double dt = ((double) c.getRightNum()) / ((double) c.getTotalNum());
                        if (dt > 1) {
                            dt = 1D;
                        }
                        ifRight = (dt == 1);
                    }
                    c.setIfRight(ifRight);
                    if (!"".equals(c.getUserAnswerStr()) && c.getUserAnswerStr() != null) {
                        //this.computWrongNum(c, false);
                        c.setDone(true);
                        tdone++;
                        if (!c.getIfRight()) {
                            twrong++;
                        }
                    } else {
                        c.setDone(false);
                    }
                } else if ("judge".equals(adapter.getQtype())) {
                    ModuleExamCaseItemJudge c = (ModuleExamCaseItemJudge) adapter.getJudgeItem();
                    c.setRightAnswer(String.valueOf(c.getQuestion().getAnswer()));
                    c.setIfRight(c.getRightAnswer().equals(c.getUserAnswer()));

                    if (!"".equals(c.getUserAnswer()) && c.getUserAnswer() != null) {
                        //this.computWrongNum(c, false);
                        c.setDone(true);
                        tdone++;
                        if (!c.getIfRight()) {
                            twrong++;
                        }
                    } else {
                        c.setDone(false);
                    }

                } else if ("essay".equals(adapter.getQtype())) {
                    ModuleExamCaseItemEssay c = (ModuleExamCaseItemEssay) adapter.getEssayItem();
                    c.setRightAnswer(c.getQuestion().getRightStr());
                    float ratio = 0f;
                    try {
                        ratio = SimilarAlgorithm.getSimilarity(HTMLCleaner.delHTMLTag(c.getQuestion().getAnswer()), HTMLCleaner.delHTMLTag(c.getUserAnswer()));
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                    c.setRightRatio(ratio);
                    if (ratio >= 0.6) {
                        c.setIfRight(true);
                    }
                    if (!"".equals(c.getUserAnswer()) && c.getUserAnswer() != null) {
                        //this.computWrongNum(c, false);
                        c.setDone(true);
                        tdone++;
                        if (!c.getIfRight()) {
                            twrong++;
                        }
                    } else {
                        c.setDone(false);
                    }

                } else if ("file".equals(adapter.getQtype())) {
                    ModuleExamCaseItemFile c = (ModuleExamCaseItemFile) adapter.getFileItem();
                    c.setRightAnswer(c.getQuestion().getRightStr());
                    float ratio = 0f;
                    try {
                        ratio = SimilarAlgorithm.getSimilarity(HTMLCleaner.delHTMLTag(c.getQuestion().getAnswer()), HTMLCleaner.delHTMLTag(c.getUserAnswer()));
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                    c.setRightRatio(ratio);
                    if (ratio >= 0.6) {
                        c.setIfRight(true);
                    }
                    if (!"".equals(c.getUserAnswer()) && c.getUserAnswer() != null) {
                        //this.computWrongNum(c, false);
                        c.setDone(true);
                        tdone++;
                        if (!c.getIfRight()) {
                            twrong++;
                        }
                    } else {
                        c.setDone(false);
                    }

                } else if ("case".equals(adapter.getQtype())) {
                    ModuleExamCaseItemCase cic = (ModuleExamCaseItemCase) adapter.getCaseItem();
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
                        if (!"".equals(c.getUserAnswer()) && c.getUserAnswer() != null) {
                            //this.computWrongNum(c, false);
                            c.setDone(true);
                            tdone++;
                            if (!c.getIfRight()) {
                                twrong++;
                            }
                        } else {
                            c.setDone(false);
                        }
                    }

                    //计算综合题中每道多选题是否正确
                    List<ModuleExamCaseItemMultiChoice> mqi = cic.getMultiChoiceItems();
                    for (ModuleExamCaseItemMultiChoice c : mqi) {
                        //List<ExamMultiChoice> ls = c.getQuestion().getChoices();
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
                        if (!"".equals(c.getUserAnswer()) && c.getUserAnswer() != null) {
                            //this.computWrongNum(c, false);
                            c.setDone(true);
                            tdone++;
                            if (!c.getIfRight()) {
                                twrong++;
                            }
                        } else {
                            c.setDone(false);
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
                        boolean ifRight = false;
                        int t = c.getTotalNum();
                        if (t != 0) {
                            double dt = ((double) c.getRightNum()) / ((double) c.getTotalNum());
                            if (dt > 1) {
                                dt = 1D;
                            }
                            ifRight = (dt == 1);
                        }
                        c.setIfRight(ifRight);
                        if (!"".equals(c.getUserAnswerStr()) && c.getUserAnswerStr() != null) {
                            //this.computWrongNum(c, false);
                            c.setDone(true);
                            tdone++;
                            if (!c.getIfRight()) {
                                twrong++;
                            }
                        } else {
                            c.setDone(false);
                        }
                    }
                    //计算综合题中每道判断题是否正确
                    List<ModuleExamCaseItemJudge> jqi = cic.getJudgeItems();
                    for (ModuleExamCaseItemJudge c : jqi) {
                        c.setRightAnswer(String.valueOf(c.getQuestion().getAnswer()));
                        c.setIfRight(c.getRightAnswer().equals(c.getUserAnswer()));
                        if (!"null".equalsIgnoreCase(c.getUserAnswer()) && c.getUserAnswer() != null) {
                            //this.computWrongNum(c, false);
                            c.setDone(true);
                            tdone++;
                            if (!c.getIfRight()) {
                                twrong++;
                            }
                        } else {
                            c.setDone(false);
                        }
                    }

                    //计算综合题中每道问答题得分
                    List<ModuleExamCaseItemEssay> eqi = cic.getEssayItems();
                    for (ModuleExamCaseItemEssay c : eqi) {
                        c.setRightAnswer(c.getQuestion().getRightStr());
                        float ratio = SimilarAlgorithm.getSimilarity(HTMLCleaner.delHTMLTag(c.getQuestion().getAnswer()), HTMLCleaner.delHTMLTag(c.getUserAnswer()));
                        c.setRightRatio(ratio);
                        //综合题中问答题的评分标准计算

                        if (ratio >= 0.6) {
                            c.setIfRight(true);
                        }
                        if (!"".equals(c.getUserAnswer()) && c.getUserAnswer() != null) {
                            //this.computWrongNum(c, false);
                            c.setDone(true);
                            tdone++;
                            if (!c.getIfRight()) {
                                twrong++;
                            }
                        } else {
                            c.setDone(false);
                        }

                    }
                    cic.setChoiceScore(choiceScoreTemp);//综合题中的选择题得分
                    cic.setMultiChoiceScore(multiChoiceScoreTemp);
                    cic.setFillScore(fillScoreTemp);
                    cic.setJudgeScore(judgeScoreTemp);
                    cic.setEssayScore(essayScoreTemp);//综合题中的问答题得分

                }

            }
        }
        ec.setWrongNum(twrong);
        ec.setDoneNum(tdone);
        //启动新线程对作答情况进行统计
        //ExamChoiceStatisticProcessor processor=new ExamChoiceStatisticProcessor(ec);
        //exec.execute(processor);
        return ec;
    }

    @Override
    public double computeTotalScore(ModuleExamCase ec) {
        ModuleExamination2 exam = ec.getExamination();
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

        List<ModuleExam2Part> parts = ec.getCparts();

        //Collections.sort(parts);
        for (ModuleExam2Part part : parts) {
            //List<ManualPartItem> items = part.getItems();
            List<ModuleExam2CaseItemAdapter> adapters = part.getAdapters();
            for (ModuleExam2CaseItemAdapter adapter : adapters) {
                if ("choice".equals(adapter.getQtype())) {//判断试卷条目的类型
                    ModuleExamCaseItemChoice ei = adapter.getChoiceItem();//获取考试条目
                    if (ei.getIfRight()) {
                        choiceScore += ei.getScore();
                    }

                } else if ("mchoice".equals(adapter.getQtype())) {
                    ModuleExamCaseItemMultiChoice c = (ModuleExamCaseItemMultiChoice) adapter.getMultiChoiceItem();
                    if (c.getIfRight()) {
                        multiChoiceScore += c.getScore();
                    }

                } else if ("fill".equals(adapter.getQtype())) {
                    ModuleExamCaseItemFill c = (ModuleExamCaseItemFill) adapter.getFillItem();
                    int t = c.getTotalNum();
                    if (t != 0) {
                        double dt = ((double) c.getRightNum()) / ((double) c.getTotalNum());
                        if (dt > 1) {
                            dt = 1D;
                        }
                        fillScore += c.getScore() * dt;
                    }

                } else if ("judge".equals(adapter.getQtype())) {
                    ModuleExamCaseItemJudge c = (ModuleExamCaseItemJudge) adapter.getJudgeItem();
                    if (c.getIfRight()) {
                        judgeScore += c.getScore();
                    }

                } else if ("essay".equals(adapter.getQtype())) {
                    ModuleExamCaseItemEssay c = (ModuleExamCaseItemEssay) adapter.getEssayItem();
                    float ratio = c.getRightRatio();
                    if (ratio > 1) {
                        ratio = 1;
                    }
                    essayScore += c.getScore() * ratio;

                } else if ("file".equals(adapter.getQtype())) {
                    ModuleExamCaseItemFile c = (ModuleExamCaseItemFile) adapter.getFileItem();
                    float ratio = c.getRightRatio();
                    if (ratio > 1) {
                        ratio = 1;
                    }
                    fileScore += c.getScore() * ratio;

                } else if ("case".equals(adapter.getQtype())) {
                    ModuleExamCaseItemCase cic = (ModuleExamCaseItemCase) adapter.getCaseItem();
                    //计算综合题中的选择题得分
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
                            multiChoiceScoreTemp += cic.getQuestion().getMultiChoiceScore();
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
                            fillScoreTemp += cic.getQuestion().getFillScore() * dt;
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
            }
        }

        totalScore = choiceScore + fillScore + judgeScore + multiChoiceScore + essayScore + fileScore + caseScore;
        //double ratio = totalScore / ec.getExaminationTotalScore();
        //long bbsScore = (long) (ec.getExamination().getBbsScore() * ratio);

        ec.setScore(totalScore);
        long oldScore = ec.getBbsScore();
        ec.setBbsScore(ec.getNewBbsScore());

        //重新改成绩后重新计算用户积分
        IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
        BbsUser bu = ec.getUser();
        IBbsScoreLogService bsl = SpringHelper.getSpringBean("BbsScoreLogService");
        bsl.log("管理员重新计算考试得分", (int) (ec.getNewBbsScore() - oldScore), bu);
        userDAO.updateBbsUser(bu);
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
