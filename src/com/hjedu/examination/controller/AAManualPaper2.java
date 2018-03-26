package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.examination.dao.IExamPaperManualDAO;
import com.hjedu.examination.dao.IExamPaperManualPartDAO;
import com.hjedu.examination.dao.IExamPaperRandomDAO;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.CaseQuestion;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.EssayQuestion;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.ExamCaseItemCase;
import com.hjedu.examination.entity.ExamCaseItemChoice;
import com.hjedu.examination.entity.ExamCaseItemEssay;
import com.hjedu.examination.entity.ExamCaseItemFile;
import com.hjedu.examination.entity.ExamCaseItemFill;
import com.hjedu.examination.entity.ExamCaseItemJudge;
import com.hjedu.examination.entity.ExamCaseItemMultiChoice;
import com.hjedu.examination.entity.ExamPaperManual;
import com.hjedu.examination.entity.ExamPaperManualPart;
import com.hjedu.examination.entity.ExamPaperRandom;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.entity.FileQuestion;
import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.examination.entity.ManualPartItem;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.hjedu.examination.service.IExamCaseService;
import com.hjedu.examination.service.impl.ExaminationService;
import com.hjedu.platform.controller.BasketSession;
import com.hjedu.platform.service.ILogService;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.util.CookieUtils;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAManualPaper2 implements Serializable {

    ExamPaperManual paper;
    IExamPaperManualDAO manualDAO = SpringHelper.getSpringBean("ExamPaperManualDAO");
    IExamPaperManualPartDAO partDAO = SpringHelper.getSpringBean("ExamPaperManualPartDAO");
    IExamPaperRandomDAO randomPaperDAO = SpringHelper.getSpringBean("ExamPaperRandomDAO");
    ILogService logger = SpringHelper.getSpringBean("LogService");
    private boolean flag = false;

    ExamPaperManualPart newPart = new ExamPaperManualPart();

    List<ManualPartItem> items = new ArrayList();
    List<ExamPaperRandom> randomPapers = new ArrayList();
    String randomPaperId = "0";

    Set<String> tempQids = new HashSet();

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public List<ExamPaperRandom> getRandomPapers() {
        return randomPapers;
    }

    public void setRandomPapers(List<ExamPaperRandom> randomPapers) {
        this.randomPapers = randomPapers;
    }

    public String getRandomPaperId() {
        return randomPaperId;
    }

    public void setRandomPaperId(String randomPaperId) {
        this.randomPaperId = randomPaperId;
    }

    public ExamPaperManualPart getNewPart() {
        return newPart;
    }

    public void setNewPart(ExamPaperManualPart newPart) {
        this.newPart = newPart;
    }

    public List<ManualPartItem> getItems() {
        return items;
    }

    public void setItems(List<ManualPartItem> items) {
        this.items = items;
    }

    public Set<String> getTempQids() {
        return tempQids;
    }

    public void setTempQids(Set<String> tempQids) {
        this.tempQids = tempQids;
    }

    public ExamPaperManual getPaper() {
        return paper;
    }

    public void setPaper(ExamPaperManual paper) {
        this.paper = paper;
    }

    @PostConstruct
    public void init() {
        String idt = JsfHelper.getRequest().getParameter("id");
        if (idt != null) {
            this.paper = this.manualDAO.findExamPaperManual(idt);
            this.flag = true;
            this.loadPartItems();
        } else {
            //至少保证试卷中有一个PART
            paper = new ExamPaperManual();
            List<ExamPaperManualPart> parts = new ArrayList();
            ExamPaperManualPart tempPart = new ExamPaperManualPart();
            tempPart.setName("第一部分");
            tempPart.setPaper(paper);
            tempPart.setItems(new ArrayList());
            parts.add(tempPart);
            paper.setParts(parts);
            //将新部分的关联试卷设为本试卷

        }
        newPart.setPaper(paper);
        newPart.setItems(new ArrayList());
        this.loadRandomPapers();
    }

    private void loadRandomPapers() {

        this.randomPapers = randomPaperDAO.findAllExamPaperRandom(CookieUtils.getBusinessId(JsfHelper.getRequest()));

    }

    public void loadRandomPaperItems() {

        ExamPaperRandom ran = this.randomPaperDAO.findExamPaperRandom(randomPaperId);
        if (ran != null) {
            ExamCase ec = new ExamCase();
            Examination ex = new Examination();
            ex.setRandomPaper(this.randomPaperDAO.findExamPaperRandom(randomPaperId));
            ec.setExamination(ex);
            IExamCaseService ecs = SpringHelper.getSpringBean("ExamCaseService");
            ecs.buildExamCase(ec);
            List<ExamCaseItemChoice> cqs = ec.getChoiceItems();
            List<ExamCaseItemMultiChoice> mcqs = ec.getMultiChoiceItems();
            List<ExamCaseItemFill> fqs = ec.getFillItems();
            List<ExamCaseItemJudge> jqs = ec.getJudgeItems();
            List<ExamCaseItemEssay> eqs = ec.getEssayItems();
            List<ExamCaseItemFile> ffqs = ec.getFileItems();
            List<ExamCaseItemCase> ccqs = ec.getCaseItems();
            int ord = this.items.size() + 1;
            for (ExamCaseItemChoice c : cqs) {
                if (this.tempQids.contains(c.getQuestion().getId())) {
                    continue;
                }
                ManualPartItem item = new ManualPartItem();
                item.setQuestionId(c.getQuestion().getId());
                item.setQtype("choice");
                item.setQuestionScore(ran.getChoiceScore());
                item.setOrd(ord);
                ord++;
                this.items.add(item);
                this.tempQids.add(c.getQuestion().getId());

            }
            for (ExamCaseItemMultiChoice c : mcqs) {
                if (this.tempQids.contains(c.getQuestion().getId())) {
                    continue;
                }
                ManualPartItem item = new ManualPartItem();
                item.setQuestionId(c.getQuestion().getId());
                item.setQtype("mchoice");
                item.setQuestionScore(ran.getMultiChoiceScore());
                item.setOrd(ord);
                ord++;
                this.items.add(item);
                this.tempQids.add(c.getQuestion().getId());
            }
            for (ExamCaseItemFill c : fqs) {
                if (this.tempQids.contains(c.getQuestion().getId())) {
                    continue;
                }
                ManualPartItem item = new ManualPartItem();
                item.setQuestionId(c.getQuestion().getId());
                item.setQtype("fill");
                item.setQuestionScore(ran.getFillScore());
                item.setOrd(ord);
                ord++;
                this.items.add(item);
                this.tempQids.add(c.getQuestion().getId());
            }
            for (ExamCaseItemJudge c : jqs) {
                if (this.tempQids.contains(c.getQuestion().getId())) {
                    continue;
                }
                ManualPartItem item = new ManualPartItem();
                item.setQuestionId(c.getQuestion().getId());
                item.setQtype("judge");
                item.setQuestionScore(ran.getJudgeScore());
                item.setOrd(ord);
                ord++;
                this.items.add(item);
                this.tempQids.add(c.getQuestion().getId());
            }
            for (ExamCaseItemEssay c : eqs) {
                if (this.tempQids.contains(c.getQuestion().getId())) {
                    continue;
                }
                ManualPartItem item = new ManualPartItem();
                item.setQuestionId(c.getQuestion().getId());
                item.setQtype("essay");
                item.setQuestionScore(ran.getEssayScore());
                item.setOrd(ord);
                ord++;
                this.items.add(item);
                this.tempQids.add(c.getQuestion().getId());
            }
            for (ExamCaseItemFile c : ffqs) {
                if (this.tempQids.contains(c.getQuestion().getId())) {
                    continue;
                }
                ManualPartItem item = new ManualPartItem();
                item.setQuestionId(c.getQuestion().getId());
                item.setQtype("file");
                item.setQuestionScore(ran.getFileScore());
                item.setOrd(ord);
                ord++;
                this.items.add(item);
                this.tempQids.add(c.getQuestion().getId());
            }
            for (ExamCaseItemCase c : ccqs) {
                if (this.tempQids.contains(c.getQuestion().getId())) {
                    continue;
                }
                ManualPartItem item = new ManualPartItem();
                item.setQuestionId(c.getQuestion().getId());
                item.setQtype("case");
                item.setOrd(ord);
                ord++;
                this.items.add(item);
                this.tempQids.add(c.getQuestion().getId());
            }
            //防止出现重复试题
            Set<ManualPartItem> sets = new HashSet();
            sets.addAll(this.items);
            this.items.clear();
            this.items.addAll(sets);
            Collections.sort(items);
            JsfHelper.info("从随机试卷加载试题成功！");
        } else {
            JsfHelper.error("选择试题不能为空！");
        }

    }

    public String addNewPart() {
        paper.getParts().add(newPart);
        newPart = new ExamPaperManualPart();
        newPart.setPaper(paper);
        newPart.setItems(new ArrayList());
        return null;
    }

    public String deletePart(String id) {
        ExamPaperManualPart pp = null;
        for (ExamPaperManualPart p : paper.getParts()) {
            if (p.getId().equals(id)) {
                pp = p;
                break;
            }
        }
        if (pp != null) {
            this.paper.getParts().remove(pp);
            this.partDAO.deleteExamPaperManualPart(id);
        }
        return null;
    }

    private void loadPartItems() {
        List<ExamPaperManualPart> parts = paper.getParts();
        for (ExamPaperManualPart p : parts) {
            this.items.addAll(p.getItems());
        }
        //将TEMPPARTID设置为分段的ID，TEMPPARTID为页面中使用的辅助属性
        for (ManualPartItem iis : items) {
            iis.setTempPid(iis.getPart().getId());
        }
        //将试卷中已经有的试题ID加入temQids，主要是为了防止试题重复
        for (ManualPartItem ii : items) {
            this.tempQids.add(ii.getQuestionId());
        }
    }

    private void resetParts() {
        List<ExamPaperManualPart> parts = paper.getParts();
        for (ExamPaperManualPart p : parts) {
            p.getItems().clear();
            p.setItemNum(0);
            p.setTotalScore(0);
            //System.out.println(p.getPaper());
        }
        //以下代码彻底杜绝items里边有重复试题的可能
        HashSet set12 = new HashSet();
        set12.addAll(items);
        items.clear();
        items.addAll(set12);

        //System.out.println("待保存题目总数："+items.size());
        for (ExamPaperManualPart p : parts) {
            List<ManualPartItem> iss = new LinkedList();
            for (ManualPartItem ii : this.items) {
                //获得ITEM的临时partId，partId主要是为了方便在页面中存储PART的ID，因为页面中无法将选择框的值设为对象，而在ITEM已经设置了多对一关系，Part为对象
                String partId = ii.getTempPid();
                //清除空题，空题产生原因不明
                if (ii.getQuestion() == null) {
                    continue;
                }
                if (p.getId().equals(partId)) {
                    ii.setPart(p);
                    iss.add(ii);
                    p.setItemNum(p.getItemNum() + 1);//设置分段的题目总数
                    double score = 0;
                    if (ii.getQtype().equals("case")) {//如果是综合题，则取综合题的各题原分数总和
                        CaseQuestion cq = (CaseQuestion) ii.getQuestion();
                        score = cq.getTotalScore();
                    } else {
                        score = ii.getQuestionScore();
                    }
                    p.setTotalScore(p.getTotalScore() + score);//设置分段的总分
                }
            }
            p.setItems(iss);
            //System.out.println("大题"+p.getName()+"中小题数："+p.getItemNum());
        }
    }

    public String batchDeleteItem() {
        List<ManualPartItem> tempII = new ArrayList();
        for (ManualPartItem ii : this.items) {
            if (ii.isSelected()) {
                tempII.add(ii);
            }
        }
        List<ExamPaperManualPart> parts = paper.getParts();
        for (ManualPartItem ii : tempII) {
            items.remove(ii);
            this.tempQids.remove(ii.getQuestionId());

            for (ExamPaperManualPart p : parts) {
                if (p.getItems().contains(ii)) {
                    p.getItems().remove(ii);
                    //System.out.println("大题中删除了小题");
                }
            }
        }

        return null;
    }

    public String batchMoveQuestion(String type) {
        BasketSession basketSession = JsfHelper.getBean("basketSession");
        if ("choice".equals(type)) {
            List<ChoiceQuestion> temp = new LinkedList();
            for (ChoiceQuestion cq : basketSession.getBasket().getChoices()) {
                if (cq.isSelected()) {
                    temp.add(cq);
                    if (!this.tempQids.contains(cq.getId())) {
                        ManualPartItem item = new ManualPartItem();
                        item.setQuestionId(cq.getId());
                        item.setQtype("choice");
                        this.items.add(item);
                        this.tempQids.add(cq.getId());
                    }
                }
            }

            for (ChoiceQuestion c : temp) {
                basketSession.getBasket().getChoices().remove(c);
            }
        }
        if ("mchoice".equals(type)) {
            List<MultiChoiceQuestion> temp = new LinkedList();
            for (MultiChoiceQuestion cq : basketSession.getBasket().getMultiChoices()) {
                if (cq.isSelected()) {
                    temp.add(cq);
                    if (!this.tempQids.contains(cq.getId())) {
                        ManualPartItem item = new ManualPartItem();
                        item.setQuestionId(cq.getId());
                        item.setQtype("mchoice");
                        this.items.add(item);
                        this.tempQids.add(cq.getId());
                    }
                }
            }
            for (MultiChoiceQuestion c : temp) {
                basketSession.getBasket().getMultiChoices().remove(c);
            }
        }
        if ("fill".equals(type)) {
            List<FillQuestion> temp = new LinkedList();
            for (FillQuestion cq : basketSession.getBasket().getFills()) {
                if (cq.isSelected()) {
                    temp.add(cq);
                    if (!this.tempQids.contains(cq.getId())) {
                        ManualPartItem item = new ManualPartItem();
                        item.setQuestionId(cq.getId());
                        item.setQtype("fill");
                        this.items.add(item);
                        this.tempQids.add(cq.getId());
                    }
                }
            }
            for (FillQuestion c : temp) {
                basketSession.getBasket().getFills().remove(c);
            }
        }
        if ("judge".equals(type)) {
            List<JudgeQuestion> temp = new LinkedList();
            for (JudgeQuestion cq : basketSession.getBasket().getJudges()) {
                if (cq.isSelected()) {
                    temp.add(cq);
                    if (!this.tempQids.contains(cq.getId())) {
                        ManualPartItem item = new ManualPartItem();
                        item.setQuestionId(cq.getId());
                        item.setQtype("judge");
                        this.items.add(item);
                        this.tempQids.add(cq.getId());
                    }
                }
            }
            for (JudgeQuestion c : temp) {
                basketSession.getBasket().getJudges().remove(c);
            }
        }
        if ("essay".equals(type)) {
            List<EssayQuestion> temp = new LinkedList();
            for (EssayQuestion cq : basketSession.getBasket().getEssaies()) {
                if (cq.isSelected()) {
                    temp.add(cq);
                    if (!this.tempQids.contains(cq.getId())) {
                        ManualPartItem item = new ManualPartItem();
                        item.setQuestionId(cq.getId());
                        item.setQtype("essay");
                        this.items.add(item);
                        this.tempQids.add(cq.getId());
                    }
                }
            }
            for (EssayQuestion c : temp) {
                basketSession.getBasket().getEssaies().remove(c);
            }
        }
        if ("file".equals(type)) {
            List<FileQuestion> temp = new LinkedList();
            for (FileQuestion cq : basketSession.getBasket().getFiles()) {
                if (cq.isSelected()) {
                    temp.add(cq);
                    if (!this.tempQids.contains(cq.getId())) {
                        ManualPartItem item = new ManualPartItem();
                        item.setQuestionId(cq.getId());
                        item.setQtype("file");
                        this.items.add(item);
                        this.tempQids.add(cq.getId());
                    }
                }
            }
            for (FileQuestion c : temp) {
                basketSession.getBasket().getFiles().remove(c);
            }
        }
        if ("case".equals(type)) {
            List<CaseQuestion> temp = new LinkedList();
            for (CaseQuestion cq : basketSession.getBasket().getCases()) {
                if (cq.isSelected()) {
                    temp.add(cq);
                    if (!this.tempQids.contains(cq.getId())) {
                        ManualPartItem item = new ManualPartItem();
                        item.setQuestionId(cq.getId());
                        item.setQtype("case");
                        this.items.add(item);
                        this.tempQids.add(cq.getId());
                    }
                }
            }
            for (CaseQuestion c : temp) {
                basketSession.getBasket().getCases().remove(c);
            }
        }
        return null;
    }

    public String moveAllQuestion(String type) {
        BasketSession basketSession = JsfHelper.getBean("basketSession");
        if ("choice".equals(type)) {
            for (ChoiceQuestion cq : basketSession.getBasket().getChoices()) {
                if (!this.tempQids.contains(cq.getId())) {
                    ManualPartItem item = new ManualPartItem();
                    item.setQuestionId(cq.getId());
                    item.setQtype("choice");
                    this.items.add(item);
                    this.tempQids.add(cq.getId());
                }
            }
            basketSession.getBasket().getChoices().clear();
        }
        if ("mchoice".equals(type)) {
            for (MultiChoiceQuestion cq : basketSession.getBasket().getMultiChoices()) {
                if (!this.tempQids.contains(cq.getId())) {
                    ManualPartItem item = new ManualPartItem();
                    item.setQuestionId(cq.getId());
                    item.setQtype("mchoice");
                    this.items.add(item);
                    this.tempQids.add(cq.getId());
                }
            }
            basketSession.getBasket().getMultiChoices().clear();
        }
        if ("fill".equals(type)) {
            for (FillQuestion cq : basketSession.getBasket().getFills()) {
                if (!this.tempQids.contains(cq.getId())) {
                    ManualPartItem item = new ManualPartItem();
                    item.setQuestionId(cq.getId());
                    item.setQtype("fill");
                    this.items.add(item);
                    this.tempQids.add(cq.getId());
                }
            }
            basketSession.getBasket().getFills().clear();
        }
        if ("judge".equals(type)) {
            for (JudgeQuestion cq : basketSession.getBasket().getJudges()) {
                if (!this.tempQids.contains(cq.getId())) {
                    ManualPartItem item = new ManualPartItem();
                    item.setQuestionId(cq.getId());
                    item.setQtype("judge");
                    this.items.add(item);
                    this.tempQids.add(cq.getId());
                }
            }
            basketSession.getBasket().getJudges().clear();
        }
        if ("essay".equals(type)) {
            for (EssayQuestion cq : basketSession.getBasket().getEssaies()) {
                if (!this.tempQids.contains(cq.getId())) {
                    ManualPartItem item = new ManualPartItem();
                    item.setQuestionId(cq.getId());
                    item.setQtype("essay");
                    this.items.add(item);
                    this.tempQids.add(cq.getId());
                }
            }
            basketSession.getBasket().getEssaies().clear();
        }
        if ("file".equals(type)) {
            for (FileQuestion cq : basketSession.getBasket().getFiles()) {
                if (!this.tempQids.contains(cq.getId())) {
                    ManualPartItem item = new ManualPartItem();
                    item.setQuestionId(cq.getId());
                    item.setQtype("file");
                    this.items.add(item);
                    this.tempQids.add(cq.getId());
                }
            }
            basketSession.getBasket().getFiles().clear();
        }
        if ("case".equals(type)) {
            for (CaseQuestion cq : basketSession.getBasket().getCases()) {
                if (!this.tempQids.contains(cq.getId())) {
                    ManualPartItem item = new ManualPartItem();
                    item.setQuestionId(cq.getId());
                    item.setQtype("case");
                    this.items.add(item);
                    this.tempQids.add(cq.getId());
                }
            }
            basketSession.getBasket().getCases().clear();
        }
        return null;
    }

    public String batchCopyQuestion(String type) {
        BasketSession basketSession = JsfHelper.getBean("basketSession");
        if ("choice".equals(type)) {
            //List<ChoiceQuestion> temp = new LinkedList();
            for (ChoiceQuestion cq : basketSession.getBasket().getChoices()) {
                if (cq.isSelected()) {
                    if (!this.tempQids.contains(cq.getId())) {
                        ManualPartItem item = new ManualPartItem();
                        item.setQuestionId(cq.getId());
                        item.setQtype("choice");
                        this.items.add(item);
                        this.tempQids.add(cq.getId());
                    }
                }
            }
            //this.choices.addAll(temp);
        }
        if ("mchoice".equals(type)) {
            //List<MultiChoiceQuestion> temp = new LinkedList();
            for (MultiChoiceQuestion cq : basketSession.getBasket().getMultiChoices()) {
                if (cq.isSelected()) {
                    if (!this.tempQids.contains(cq.getId())) {
                        ManualPartItem item = new ManualPartItem();
                        item.setQuestionId(cq.getId());
                        item.setQtype("mchoice");
                        this.items.add(item);
                        this.tempQids.add(cq.getId());
                    }
                }
            }
            //this.multiChoices.addAll(temp);
        }
        if ("fill".equals(type)) {
            //List<FillQuestion> temp = new LinkedList();
            for (FillQuestion cq : basketSession.getBasket().getFills()) {
                if (cq.isSelected()) {
                    if (!this.tempQids.contains(cq.getId())) {
                        ManualPartItem item = new ManualPartItem();
                        item.setQuestionId(cq.getId());
                        item.setQtype("fill");
                        this.items.add(item);
                        this.tempQids.add(cq.getId());
                    }
                }
            }
            //this.fills.addAll(temp);
        }
        if ("judge".equals(type)) {
            //List<JudgeQuestion> temp = new LinkedList();
            for (JudgeQuestion cq : basketSession.getBasket().getJudges()) {
                if (cq.isSelected()) {
                    if (!this.tempQids.contains(cq.getId())) {
                        ManualPartItem item = new ManualPartItem();
                        item.setQuestionId(cq.getId());
                        item.setQtype("judge");
                        this.items.add(item);
                        this.tempQids.add(cq.getId());
                    }
                }
            }
            //this.judges.addAll(temp);
        }
        if ("essay".equals(type)) {
            //List<EssayQuestion> temp = new LinkedList();
            for (EssayQuestion cq : basketSession.getBasket().getEssaies()) {
                if (cq.isSelected()) {
                    if (!this.tempQids.contains(cq.getId())) {
                        ManualPartItem item = new ManualPartItem();
                        item.setQuestionId(cq.getId());
                        item.setQtype("essay");
                        this.items.add(item);
                        this.tempQids.add(cq.getId());
                    }
                }
            }
            //this.essaies.addAll(temp);
        }
        if ("file".equals(type)) {
            //List<FileQuestion> temp = new LinkedList();
            for (FileQuestion cq : basketSession.getBasket().getFiles()) {
                if (cq.isSelected()) {
                    if (!this.tempQids.contains(cq.getId())) {
                        ManualPartItem item = new ManualPartItem();
                        item.setQuestionId(cq.getId());
                        item.setQtype("file");
                        this.items.add(item);
                        this.tempQids.add(cq.getId());
                    }
                }
            }
            //this.files.addAll(temp);
        }
        if ("case".equals(type)) {
            //List<CaseQuestion> temp = new LinkedList();
            for (CaseQuestion cq : basketSession.getBasket().getCases()) {
                if (cq.isSelected()) {
                    if (!this.tempQids.contains(cq.getId())) {
                        ManualPartItem item = new ManualPartItem();
                        item.setQuestionId(cq.getId());
                        item.setQtype("case");
                        this.items.add(item);
                        this.tempQids.add(cq.getId());
                    }
                }
            }
            //this.cases.addAll(temp);
        }
        return null;
    }

    public String copyAllQuestion(String type) {
        BasketSession basketSession = JsfHelper.getBean("basketSession");
        if ("choice".equals(type)) {
            for (ChoiceQuestion cq : basketSession.getBasket().getChoices()) {
                if (!this.tempQids.contains(cq.getId())) {
                    ManualPartItem item = new ManualPartItem();
                    item.setQuestionId(cq.getId());
                    item.setQtype("choice");
                    this.items.add(item);
                    this.tempQids.add(cq.getId());
                }
            }
            //basketSession.getBasket().getChoices().clear();
        }
        if ("mchoice".equals(type)) {
            for (MultiChoiceQuestion cq : basketSession.getBasket().getMultiChoices()) {
                if (!this.tempQids.contains(cq.getId())) {
                    ManualPartItem item = new ManualPartItem();
                    item.setQuestionId(cq.getId());
                    item.setQtype("mchoice");
                    this.items.add(item);
                    this.tempQids.add(cq.getId());
                }
            }
            //basketSession.getBasket().getMultiChoices().clear();
        }
        if ("fill".equals(type)) {
            for (FillQuestion cq : basketSession.getBasket().getFills()) {
                if (!this.tempQids.contains(cq.getId())) {
                    ManualPartItem item = new ManualPartItem();
                    item.setQuestionId(cq.getId());
                    item.setQtype("fill");
                    this.items.add(item);
                    this.tempQids.add(cq.getId());
                }
            }
            //basketSession.getBasket().getFills().clear();
        }
        if ("judge".equals(type)) {
            for (JudgeQuestion cq : basketSession.getBasket().getJudges()) {
                if (!this.tempQids.contains(cq.getId())) {
                    ManualPartItem item = new ManualPartItem();
                    item.setQuestionId(cq.getId());
                    item.setQtype("judge");
                    this.items.add(item);
                    this.tempQids.add(cq.getId());
                }
            }
            //basketSession.getBasket().getJudges().clear();
        }
        if ("essay".equals(type)) {
            for (EssayQuestion cq : basketSession.getBasket().getEssaies()) {
                if (!this.tempQids.contains(cq.getId())) {
                    ManualPartItem item = new ManualPartItem();
                    item.setQuestionId(cq.getId());
                    item.setQtype("essay");
                    this.items.add(item);
                    this.tempQids.add(cq.getId());
                }
            }
            //basketSession.getBasket().getEssaies().clear();
        }
        if ("file".equals(type)) {
            for (FileQuestion cq : basketSession.getBasket().getFiles()) {
                if (!this.tempQids.contains(cq.getId())) {
                    ManualPartItem item = new ManualPartItem();
                    item.setQuestionId(cq.getId());
                    item.setQtype("file");
                    this.items.add(item);
                    this.tempQids.add(cq.getId());
                }
            }
            //basketSession.getBasket().getFiles().clear();
        }
        if ("case".equals(type)) {
            for (CaseQuestion cq : basketSession.getBasket().getCases()) {
                if (!this.tempQids.contains(cq.getId())) {
                    ManualPartItem item = new ManualPartItem();
                    item.setQuestionId(cq.getId());
                    item.setQtype("case");
                    this.items.add(item);
                    this.tempQids.add(cq.getId());
                }
            }
            //basketSession.getBasket().getCases().clear();
        }
        return null;
    }

    public String clearQuestion() {
        this.items.clear();
        return null;
    }

    public void synExamination() {
        IExaminationDAO exDAO = SpringHelper.getSpringBean("ExaminationDAO");
        List<Examination> exams = exDAO.findAllExamination(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        List<Examination> exs = new ArrayList();
        if (exams != null) {
            for (Examination ex : exams) {
                if (ex.getManualPaper() != null) {
                    if (this.paper.getId().equals(ex.getManualPaper().getId())) {
                        exs.add(ex);
                    }
                }
            }
        }
        for (Examination exam : exs) {
            exam.fetchChoiceTotal();
            exam.fetchMultiChoiceTotal();
            exam.fetchFillTotal();
            exam.fetchJudgeTotal();
            exam.fetchEssayTotal();
            exam.fetchFileTotal();
            exam.fetchCaseTotal();
            exDAO.updateExamination(exam);
        }
    }

    public String done() {
        this.resetParts();
        //this.paper.setModulePapers(mes);
        //计算总分
        double a = 0;
        for(ManualPartItem mi : this.getItems()){
        	a+=mi.getQuestionScore();
        }
        paper.setTotalScore(a);
        System.out.println("总分为："+a);
        if (flag) {
            this.manualDAO.updateExamPaperManual(paper);
            this.synExamination();
            ExamPaperPool.refreshPaper(paper.getId(), "manual");
            this.logger.log("修改了人工试卷：" + paper.getName());
        } else {
        	List list = new ArrayList<>();
        	list.add(ExternalUserUtil.getAdminBySession());
        	paper.setAdmins(list);
        	paper.setBusinessId(CookieUtils.getBusinessId(JsfHelper.getRequest()));
            this.logger.log("添加了人工试卷：" + paper.getName());
            this.manualDAO.addExamPaperManual(paper);
        }
        
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
        //更新考试缓存
        ExaminationService examService = SpringHelper.getSpringBean("ExaminationService");
        examService.reBuildCache();
        return "ListManualPaper?faces-redirect=true";
    }
}
