package com.hjedu.examination.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.ICaseQuestionDAO;
import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IEssayQuestionDAO;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.dao.IFillQuestionDAO;
import com.hjedu.examination.dao.IJudgeQuestionDAO;
import com.hjedu.examination.dao.IModuleBuffetPartDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.entity.CaseQuestion;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.EssayQuestion;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.ExamChoice;
import com.hjedu.examination.entity.ExamMultiChoice;
import com.hjedu.examination.entity.ExamRoom;
import com.hjedu.examination.entity.FileQuestion;
import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.hjedu.examination.entity.buffet.BuffetExamCase;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemAdapter;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemCase;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemChoice;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemEssay;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemFile;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemFill;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemFillBlock;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemJudge;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemMultiChoice;
import com.hjedu.examination.entity.buffet.BuffetExamination;
import com.hjedu.examination.entity.buffet.BuffetExaminationPart;
import com.hjedu.examination.entity.buffet.ModuleBuffetPart;
import com.hjedu.examination.util.ExamCaseServiceUtils;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.huajie.util.HTMLCleaner;
import com.huajie.util.SimilarAlgorithm;
import com.huajie.util.SpringHelper;

public class BuffetExamCaseService extends IBuffetExamCaseService implements Serializable {

    public String answerB;
    public String answerE;
    IExamCaseDAO examCaseDAO;
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

    public IExamCaseDAO getExamCaseDAO() {
        return examCaseDAO;
    }

    public void setExamCaseDAO(IExamCaseDAO examCaseDAO) {
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

    public void buildItemFillBlocks(BuffetExamCaseItemFill fill) {
        List<BuffetExamCaseItemFillBlock> blocks = new LinkedList();
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
                BuffetExamCaseItemFillBlock block = new BuffetExamCaseItemFillBlock();
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
        ExamCaseServiceUtils.choiceOrderAdapter(choices);
    }

    private void multiChoiceOrderAdapter(List<ExamMultiChoice> choices) {
        ExamCaseServiceUtils.multiChoiceOrderAdapter(choices);
    }

    private BuffetExamCaseItemCase buildCaseItem(CaseQuestion j, BuffetExamCase ec) {

        BuffetExamCaseItemCase ecie = new BuffetExamCaseItemCase();
        ecie.setExamCase(ec);
        ecie.setQuestion(j);
        //将选择题加入综合题中
        List<BuffetExamCaseItemChoice> ecicqcs = new LinkedList();
        List<ChoiceQuestion> cqscc = j.getChoices();
        for (ChoiceQuestion c : cqscc) {
            BuffetExamCaseItemChoice ecic = new BuffetExamCaseItemChoice();
            //ecic.setExamCase(ec);
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
        List<BuffetExamCaseItemMultiChoice> mcieqs = new LinkedList();
        List<MultiChoiceQuestion> mqscc = j.getMultiChoices();
        for (MultiChoiceQuestion c : mqscc) {
            BuffetExamCaseItemMultiChoice eciee = new BuffetExamCaseItemMultiChoice();
            //ecie.setExamCase(ec);
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
        List<BuffetExamCaseItemFill> fcieqs = new LinkedList();
        List<FillQuestion> fqscc = j.getFills();
        for (FillQuestion c : fqscc) {
            BuffetExamCaseItemFill eciee = new BuffetExamCaseItemFill();
            //ecie.setExamCase(ec);
            eciee.setCaseItem(ecie);
            eciee.setQuestion(c);
            fcieqs.add(eciee);
        }
        ecie.setFillItems(fcieqs);

        //将判断题加入综合题中
        List<BuffetExamCaseItemJudge> jcieqs = new LinkedList();
        List<JudgeQuestion> jqscc = j.getJudges();
        for (JudgeQuestion c : jqscc) {
            BuffetExamCaseItemJudge eciee = new BuffetExamCaseItemJudge();
            //ecie.setExamCase(ec);
            eciee.setCaseItem(ecie);
            eciee.setQuestion(c);
            jcieqs.add(eciee);
        }
        ecie.setJudgeItems(jcieqs);

        //将问答题加入综合题中
        List<BuffetExamCaseItemEssay> ecieqs = new LinkedList();
        List<EssayQuestion> eqscc = j.getEssaies();
        for (EssayQuestion c : eqscc) {
            BuffetExamCaseItemEssay eciee = new BuffetExamCaseItemEssay();
            //ecie.setExamCase(ec);
            eciee.setCaseItem(ecie);
            eciee.setQuestion(c);
            ecieqs.add(eciee);
        }
        ecie.setEssayItems(ecieqs);
        //向临时变量中加入综合题
        return ecie;
    }

    private void addAdapterForPart(BuffetExamCaseItemAdapter adapter, String partId, List<BuffetExaminationPart> parts) {
        for (BuffetExaminationPart part : parts) {
            if (part.getId().equals(partId)) {
                part.getAdapters().add(adapter);
                break;
            }
        }
    }

    //获得用户选定的题数
    private int computeUserNum(String partId, List<BuffetExaminationPart> parts) {
        //long weight = 0;
        for (BuffetExaminationPart part : parts) {
            if (part.getId().equals(partId)) {
                return part.getUserNum();
            }
        }
        return 0;
    }

    //计算各个大题的总权重
    private long computeTotalWeightForPart(String partId, List<ModuleBuffetPart> mps) {
        long weight = 0;
        for (ModuleBuffetPart mp : mps) {
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
    private long computeQuestionNum(double w, String partId, List<ModuleBuffetPart> mps, List<BuffetExaminationPart> parts) {
        double totalWeight = this.computeTotalWeightForPart(partId, mps);
        double unum = computeUserNum(partId, parts);
        if (totalWeight == 0) {
            totalWeight = 1;
        }
        long cnum = (long) ((w / totalWeight) * unum) + 1;
        return cnum;
    }

    public void buildExamCase(BuffetExamCase ec) {
        BuffetExamination exam = ec.getExamination();
        ec.setChoiceItems(new ArrayList());
        ec.setMultiChoiceItems(new ArrayList());
        ec.setFillItems(new ArrayList());
        ec.setJudgeItems(new ArrayList());
        ec.setEssayItems(new ArrayList());
        ec.setFileItems(new ArrayList());
        ec.setCaseItems(new ArrayList());

        List<BuffetExaminationPart> parts = exam.getParts();
        for (BuffetExaminationPart pp : parts) {
            pp.getAdapters().clear();
        }
        int uniOrd = 0;//对全部试题统一标号
        //Collections.sort(parts);
        IModuleBuffetPartDAO mpDAO = SpringHelper.getSpringBean("ModuleBuffetPartDAO");
        //IBuffetExaminationPartDAO partDAO = SpringHelper.getSpringBean("BuffetExaminationPartDAO");
        List<ModuleBuffetPart> mps = mpDAO.findModuleBuffetPartByExam(exam.getId());
        for (ModuleBuffetPart mp : mps) {
            //计算单选题数量并从模块中抽选试题
            long cnum = computeQuestionNum(mp.getChoiceWeight(), mp.getChoicePartId(), mps, parts);
            List<ChoiceQuestion> qs1 = this.choiceQuestionDAO.getRandomQuestionOfNumInModule(cnum, mp.getModule().getId());
            //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
            for (ChoiceQuestion cq : qs1) {
                BuffetExamCaseItemChoice ei = new BuffetExamCaseItemChoice();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题F
                List<ExamChoice> ecs = ei.getQuestion().getChoices();
                if (ec.getExamination().isChoiceRandom()) {
                    this.choiceOrderAdapter(ecs);
                }
                ei.getQuestion().setChoices(ecs);
                ei.setExamCase(ec);
                ei.setPartId(mp.getChoicePartId());
                BuffetExamCaseItemAdapter adapter = new BuffetExamCaseItemAdapter();
                adapter.setOrd(cq.getOrd());
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
                BuffetExamCaseItemMultiChoice ei = new BuffetExamCaseItemMultiChoice();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题F
                List<ExamMultiChoice> ecs = ei.getQuestion().getChoices();
                if (ec.getExamination().isMultiChoiceRandom()) {
                    this.multiChoiceOrderAdapter(ecs);
                }
                ei.getQuestion().setChoices(ecs);
                ei.setExamCase(ec);
                ei.setPartId(mp.getMchoicePartId());
                BuffetExamCaseItemAdapter adapter = new BuffetExamCaseItemAdapter();
                adapter.setOrd(cq.getOrd());
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
                BuffetExamCaseItemFill ei = new BuffetExamCaseItemFill();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题
                ei.setExamCase(ec);
                ei.setPartId(mp.getFillPartId());
                BuffetExamCaseItemAdapter adapter = new BuffetExamCaseItemAdapter();
                adapter.setOrd(cq.getOrd());
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
                BuffetExamCaseItemJudge ei = new BuffetExamCaseItemJudge();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题F
                ei.setExamCase(ec);
                ei.setPartId(mp.getJudgePartId());
                BuffetExamCaseItemAdapter adapter = new BuffetExamCaseItemAdapter();
                adapter.setOrd(cq.getOrd());
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
                BuffetExamCaseItemEssay ei = new BuffetExamCaseItemEssay();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题F
                ei.setExamCase(ec);
                ei.setPartId(mp.getEssayPartId());
                BuffetExamCaseItemAdapter adapter = new BuffetExamCaseItemAdapter();
                adapter.setOrd(cq.getOrd());
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
                BuffetExamCaseItemFile ei = new BuffetExamCaseItemFile();//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题
                ei.setExamCase(ec);
                ei.setPartId(mp.getFilePartId());
                BuffetExamCaseItemAdapter adapter = new BuffetExamCaseItemAdapter();
                adapter.setOrd(cq.getOrd());
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
                //BuffetExamCaseItemCase ei = new BuffetExamCaseItemCase();//构建一个考试条目
                BuffetExamCaseItemCase ei = this.buildCaseItem(cq, ec);//构建一个考试条目
                ei.setQuestion(cq);//设置考试条目对应的试题
                ei.setExamCase(ec);
                ei.setPartId(mp.getCasePartId());
                BuffetExamCaseItemAdapter adapter = new BuffetExamCaseItemAdapter();
                adapter.setOrd(cq.getOrd());
                adapter.setCaseItem(ei);//PART条目的适配器，在PART条目与考试条目间适配
                adapter.setQtype("case");
                adapter.setQuestion(cq);
                //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
                adapter.setItem(ei);
                //ec.getCaseItems().add(ei);
                this.addAdapterForPart(adapter, mp.getCasePartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
            }
        }

        double totalScore = 0d;

        //以上算法可能会比用户抽取的实际试题数量多，随机进行裁剪
        for (BuffetExaminationPart part : parts) {
            int unum = part.getUserNum();
            List<BuffetExamCaseItemAdapter> ads = part.getAdapters();
            Collections.shuffle(ads);
            //System.out.println("buffet size before deal:" + ads.size());
            int gap = ads.size() - unum;
            if (gap > 0) {
                for (int i = 0; i < gap; i++) {
                    ads.remove(i);
                }
            }
            part.setAdapters(ads);
            //计算PART中的条目数
            part.setItemNum(ads.size());
            //System.out.println("buffet size after deal:" + ads.size());
            List<BuffetExamCaseItemAdapter> ads2 = part.getAdapters();
            for (BuffetExamCaseItemAdapter ad : ads2) {
                if (ad.getQtype().equals("choice")) {
                    BuffetExamCaseItemChoice ei = ad.getChoiceItem();
                    ec.getChoiceItems().add(ei);
                    totalScore += part.getChoiceScore();
                }
                if (ad.getQtype().equals("mchoice")) {
                    BuffetExamCaseItemMultiChoice ei = ad.getMultiChoiceItem();
                    ec.getMultiChoiceItems().add(ei);
                    totalScore += part.getMultiChoiceScore();
                }
                if (ad.getQtype().equals("fill")) {
                    BuffetExamCaseItemFill ei = ad.getFillItem();
                    ec.getFillItems().add(ei);
                    totalScore += part.getFillScore();
                }
                if (ad.getQtype().equals("judge")) {
                    BuffetExamCaseItemJudge ei = ad.getJudgeItem();
                    ec.getJudgeItems().add(ei);
                    totalScore += part.getJudgeScore();
                }
                if (ad.getQtype().equals("essay")) {
                    BuffetExamCaseItemEssay ei = ad.getEssayItem();
                    ec.getEssayItems().add(ei);
                    totalScore += part.getEssayScore();
                }
                if (ad.getQtype().equals("file")) {
                    BuffetExamCaseItemFile ei = ad.getFileItem();
                    ec.getFileItems().add(ei);
                    totalScore += part.getFileScore();
                }
                if (ad.getQtype().equals("case")) {
                    BuffetExamCaseItemCase ei = ad.getCaseItem();
                    ec.getCaseItems().add(ei);
                    totalScore += ei.getQuestion().getTotalScore();
                }
            }

        }
        for (BuffetExaminationPart part : parts) {
            Collections.sort(part.getAdapters());
        }

        Collections.sort(parts);

        ec.setParts(parts);//将装载好的parts加入考试中

        ec.setTotalFullScore(totalScore);

    }

    @Override
    public BuffetExamCase resumeExamCase(BuffetExamCase ec) {
        BuffetExamination exam = ec.getExamination();
        List<BuffetExamCaseItemChoice> cqs = ec.getChoiceItems();
        List<BuffetExamCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
        List<BuffetExamCaseItemFill> fqs = ec.getFillItems();
        List<BuffetExamCaseItemJudge> jqs = ec.getJudgeItems();
        List<BuffetExamCaseItemEssay> eqs = ec.getEssayItems();
        List<BuffetExamCaseItemFile> ffqs = ec.getFileItems();
        List<BuffetExamCaseItemCase> ccqs = ec.getCaseItems();
        List<BuffetExaminationPart> parts = exam.getParts();

        //将试题构建为EXAMCASEITEM并构建为ADAPTER加入PART中
        for (BuffetExamCaseItemChoice cq : cqs) {
            BuffetExamCaseItemAdapter adapter = new BuffetExamCaseItemAdapter();
            adapter.setOrd(cq.getQuestion().getOrd());
            adapter.setChoiceItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("choice");
            adapter.setQuestion(cq.getQuestion());
            //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
            adapter.setItem(cq);
            this.addAdapterForPart(adapter, cq.getPartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
        }

        for (BuffetExamCaseItemMultiChoice cq : mcqs) {
            BuffetExamCaseItemAdapter adapter = new BuffetExamCaseItemAdapter();
            adapter.setOrd(cq.getQuestion().getOrd());
            adapter.setMultiChoiceItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("mchoice");
            adapter.setQuestion(cq.getQuestion());
            //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
            adapter.setItem(cq);
            this.addAdapterForPart(adapter, cq.getPartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
        }
        for (BuffetExamCaseItemFill cq : fqs) {
            BuffetExamCaseItemAdapter adapter = new BuffetExamCaseItemAdapter();
            adapter.setOrd(cq.getQuestion().getOrd());
            adapter.setFillItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("fill");
            adapter.setQuestion(cq.getQuestion());
            //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
            adapter.setItem(cq);
            this.addAdapterForPart(adapter, cq.getPartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
        }

        for (BuffetExamCaseItemJudge cq : jqs) {
            BuffetExamCaseItemAdapter adapter = new BuffetExamCaseItemAdapter();
            adapter.setOrd(cq.getQuestion().getOrd());
            adapter.setJudgeItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("judge");
            adapter.setQuestion(cq.getQuestion());
            //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
            adapter.setItem(cq);
            this.addAdapterForPart(adapter, cq.getPartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
        }

        for (BuffetExamCaseItemEssay cq : eqs) {

            BuffetExamCaseItemAdapter adapter = new BuffetExamCaseItemAdapter();
            adapter.setOrd(cq.getQuestion().getOrd());
            adapter.setEssayItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("essay");
            adapter.setQuestion(cq.getQuestion());
            //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
            adapter.setItem(cq);
            this.addAdapterForPart(adapter, cq.getPartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
        }
        for (BuffetExamCaseItemFile cq : ffqs) {
            BuffetExamCaseItemAdapter adapter = new BuffetExamCaseItemAdapter();
            adapter.setOrd(cq.getQuestion().getOrd());
            adapter.setFileItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("file");
            adapter.setQuestion(cq.getQuestion());
            //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
            adapter.setItem(cq);
            this.addAdapterForPart(adapter, cq.getPartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
        }

        for (BuffetExamCaseItemCase cq : ccqs) {
            BuffetExamCaseItemAdapter adapter = new BuffetExamCaseItemAdapter();
            adapter.setOrd(cq.getQuestion().getOrd());
            adapter.setCaseItem(cq);//PART条目的适配器，在PART条目与考试条目间适配
            adapter.setQtype("case");
            adapter.setQuestion(cq.getQuestion());
            //adapter.setScore(exam.getChoiceScore());//设置adapter的分值
            adapter.setItem(cq);
            this.addAdapterForPart(adapter, cq.getPartId(), parts);//构建ADAPTER并加入PART以备在考试页面使用
        }
        for (BuffetExaminationPart part : parts) {
            Collections.sort(part.getAdapters());
        }

        Collections.sort(parts);

        ec.setParts(parts);//将装载好的parts加入考试中
        return ec;
    }

    public BuffetExamCase computeExamCase(BuffetExamCase ec) {
        double totalScore = 0;
        double choiceScore = 0;
        double multiChoiceScore = 0;
        double fillScore = 0;
        double judgeScore = 0;
        double essayScore = 0;
        double fileScore = 0;
        double caseScore = 0;

        List<BuffetExaminationPart> parts = ec.getParts();

        //Collections.sort(parts);
        for (BuffetExaminationPart part : parts) {
            //List<ManualPartItem> items = part.getItems();
            List<BuffetExamCaseItemAdapter> adapters = part.getAdapters();
            for (BuffetExamCaseItemAdapter adapter : adapters) {
                if ("choice".equals(adapter.getQtype())) {//判断试卷条目的类型
                    BuffetExamCaseItemChoice ei = adapter.getChoiceItem();//获取考试条目
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
                    System.out.println("题目：" + ei.getQuestion().getName() + ",选择：" + ei.getUserAnswer() + "，答案：" + ei.getRightAnswer() + "，是否正确：" + ei.getIfRight());
                } else if ("mchoice".equals(adapter.getQtype())) {
                    BuffetExamCaseItemMultiChoice c = (BuffetExamCaseItemMultiChoice) adapter.getMultiChoiceItem();
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
                    BuffetExamCaseItemFill c = (BuffetExamCaseItemFill) adapter.getFillItem();
                    List<BuffetExamCaseItemFillBlock> ls = c.getBlocks();
                    StringBuilder as = new StringBuilder();
                    StringBuilder us = new StringBuilder();
                    int rightNum = 0;
                    for (BuffetExamCaseItemFillBlock e : ls) {
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
                    BuffetExamCaseItemJudge c = (BuffetExamCaseItemJudge) adapter.getJudgeItem();
                    c.setRightAnswer(String.valueOf(c.getQuestion().getAnswer()));
                    c.setIfRight(c.getRightAnswer().equals(c.getUserAnswer()));
                    if (c.getIfRight()) {
                        judgeScore += part.getJudgeScore();
                    }

                } else if ("essay".equals(adapter.getQtype())) {
                    BuffetExamCaseItemEssay c = (BuffetExamCaseItemEssay) adapter.getEssayItem();
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
                    BuffetExamCaseItemFile c = (BuffetExamCaseItemFile) adapter.getFileItem();
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
                    BuffetExamCaseItemCase cic = (BuffetExamCaseItemCase) adapter.getCaseItem();
                    //计算综合题中的选择题得分
                    double choiceScoreTemp = 0;
                    double multiChoiceScoreTemp = 0;
                    double fillScoreTemp = 0;
                    double judgeScoreTemp = 0;
                    double essayScoreTemp = 0;
                    List<BuffetExamCaseItemChoice> cqi = cic.getChoiceItems();
                    for (BuffetExamCaseItemChoice c : cqi) {
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
                    List<BuffetExamCaseItemMultiChoice> mqi = cic.getMultiChoiceItems();
                    for (BuffetExamCaseItemMultiChoice c : mqi) {
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
                    List<BuffetExamCaseItemFill> fqi = cic.getFillItems();
                    for (BuffetExamCaseItemFill c : fqi) {
                        List<BuffetExamCaseItemFillBlock> ls = c.getBlocks();
                        StringBuilder as = new StringBuilder();
                        StringBuilder us = new StringBuilder();
                        int rightNum = 0;
                        for (BuffetExamCaseItemFillBlock e : ls) {
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
                    List<BuffetExamCaseItemJudge> jqi = cic.getJudgeItems();
                    for (BuffetExamCaseItemJudge c : jqi) {
                        c.setRightAnswer(String.valueOf(c.getQuestion().getAnswer()));
                        c.setIfRight(c.getRightAnswer().equals(c.getUserAnswer()));
                        if (c.getIfRight()) {
                            judgeScoreTemp += cic.getQuestion().getJudgeScore();
                            caseScore += cic.getQuestion().getJudgeScore();
                        }
                    }

                    //计算综合题中每道问答题得分
                    List<BuffetExamCaseItemEssay> eqi = cic.getEssayItems();
                    for (BuffetExamCaseItemEssay c : eqi) {
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
        //System.out.println("------------------------------------");
        List<BuffetExamCaseItemChoice> bics = ec.getChoiceItems();
        for (BuffetExamCaseItemChoice ei : bics) {
            //System.out.println("题目：" + ei.getQuestion().getName() + ",选择：" + ei.getUserAnswer() + "，答案：" + ei.getRightAnswer() + "，是否正确：" + ei.getIfRight());

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
        return ec;
    }

    public double computeTotalScore(BuffetExamCase ec) {
        BuffetExamination exam = ec.getExamination();
        double totalScore = 0;
        double choiceScore = 0;
        double multiChoiceScore = 0;
        double fillScore = 0;
        double judgeScore = 0;
        double essayScore = 0;
        double fileScore = 0;
        double caseScore = 0;
        List<BuffetExamCaseItemChoice> cqs = ec.getChoiceItems();
        List<BuffetExamCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
        List<BuffetExamCaseItemFill> fqs = ec.getFillItems();
        List<BuffetExamCaseItemJudge> jqs = ec.getJudgeItems();
        List<BuffetExamCaseItemEssay> eqs = ec.getEssayItems();
        List<BuffetExamCaseItemFile> ffqs = ec.getFileItems();
        List<BuffetExamCaseItemCase> ccqs = ec.getCaseItems();

        List<BuffetExaminationPart> parts = ec.getParts();

        //Collections.sort(parts);
        for (BuffetExaminationPart part : parts) {
            //List<ManualPartItem> items = part.getItems();
            List<BuffetExamCaseItemAdapter> adapters = part.getAdapters();
            for (BuffetExamCaseItemAdapter adapter : adapters) {
                if ("choice".equals(adapter.getQtype())) {//判断试卷条目的类型
                    BuffetExamCaseItemChoice ei = adapter.getChoiceItem();//获取考试条目
                    if (ei.getIfRight()) {
                        choiceScore += ei.getScore();
                    }

                } else if ("mchoice".equals(adapter.getQtype())) {
                    BuffetExamCaseItemMultiChoice c = (BuffetExamCaseItemMultiChoice) adapter.getMultiChoiceItem();
                    if (c.getIfRight()) {
                        multiChoiceScore += c.getScore();
                    }

                } else if ("fill".equals(adapter.getQtype())) {
                    BuffetExamCaseItemFill c = (BuffetExamCaseItemFill) adapter.getFillItem();
                    int t = c.getTotalNum();
                    if (t != 0) {
                        double dt = ((double) c.getRightNum()) / ((double) c.getTotalNum());
                        if (dt > 1) {
                            dt = 1D;
                        }
                        fillScore += c.getScore() * dt;
                    }

                } else if ("judge".equals(adapter.getQtype())) {
                    BuffetExamCaseItemJudge c = (BuffetExamCaseItemJudge) adapter.getJudgeItem();
                    if (c.getIfRight()) {
                        judgeScore += c.getScore();
                    }

                } else if ("essay".equals(adapter.getQtype())) {
                    BuffetExamCaseItemEssay c = (BuffetExamCaseItemEssay) adapter.getEssayItem();
                    float ratio = c.getRightRatio();
                    if (ratio > 1) {
                        ratio = 1;
                    }
                    essayScore += c.getScore() * ratio;

                } else if ("file".equals(adapter.getQtype())) {
                    BuffetExamCaseItemFile c = (BuffetExamCaseItemFile) adapter.getFileItem();
                    float ratio = c.getRightRatio();
                    if (ratio > 1) {
                        ratio = 1;
                    }
                    fileScore += c.getScore() * ratio;

                } else if ("case".equals(adapter.getQtype())) {
                    BuffetExamCaseItemCase cic = (BuffetExamCaseItemCase) adapter.getCaseItem();
                    //计算综合题中的选择题得分
                    //计算综合题中的选择题得分
                    double choiceScoreTemp = 0;
                    double multiChoiceScoreTemp = 0;
                    double fillScoreTemp = 0;
                    double judgeScoreTemp = 0;
                    double essayScoreTemp = 0;
                    List<BuffetExamCaseItemChoice> cqi = cic.getChoiceItems();
                    for (BuffetExamCaseItemChoice c : cqi) {
                        if (c.getIfRight()) {
                            //综合题中选择题的评分标准计算
                            caseScore += cic.getQuestion().getChoiceScore();
                            choiceScoreTemp += cic.getQuestion().getChoiceScore();
                        }
                    }

                    //计算多选题成绩
                    List<BuffetExamCaseItemMultiChoice> mqi = cic.getMultiChoiceItems();
                    for (BuffetExamCaseItemMultiChoice c : mqi) {
                        if (c.getIfRight()) {
                            multiChoiceScoreTemp += cic.getQuestion().getMultiChoiceScore();
                            caseScore += cic.getQuestion().getMultiChoiceScore();
                        }
                    }
                    //计算填空题成绩
                    List<BuffetExamCaseItemFill> fqi = cic.getFillItems();
                    for (BuffetExamCaseItemFill c : fqi) {
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
                    List<BuffetExamCaseItemJudge> jqi = cic.getJudgeItems();
                    for (BuffetExamCaseItemJudge c : jqi) {
                        if (c.getIfRight()) {
                            caseScore += cic.getQuestion().getJudgeScore();
                            judgeScoreTemp += cic.getQuestion().getJudgeScore();
                        }
                    }

                    //计算综合题中每道问答题得分
                    List<BuffetExamCaseItemEssay> eqi = cic.getEssayItems();
                    for (BuffetExamCaseItemEssay c : eqi) {
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

    public Boolean checkIfSupplementaryExamination(String examId, String userId) {
        List<ExamCase> cases = this.examCaseDAO.findExamCaseByExaminationAndUser(examId, userId);
        if (cases == null) {
            return false;
        }
        boolean ifUnPass = false;
        boolean ifPass = false;
        int unPassTimes = 0;
        int passTimes = 0;
        int totalTimes = 0;
        for (ExamCase ec : cases) {
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
