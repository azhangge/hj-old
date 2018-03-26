package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.DragDropEvent;

import com.hjedu.examination.controller.ListAllQuestion;
import com.hjedu.examination.controller.ListCaseQuestion;
import com.hjedu.examination.controller.ListChoiceQuestion;
import com.hjedu.examination.controller.ListEssayQuestion;
import com.hjedu.examination.controller.ListFileQuestion;
import com.hjedu.examination.controller.ListFillQuestion;
import com.hjedu.examination.controller.ListJudgeQuestion;
import com.hjedu.examination.controller.ListMultiChoiceQuestion;
import com.hjedu.examination.entity.CaseQuestion;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.EssayQuestion;
import com.hjedu.examination.entity.FileQuestion;
import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.hjedu.examination.entity.QuestionBasket;
import com.huajie.util.JsfHelper;

@ManagedBean
@SessionScoped
public class BasketSession implements Serializable{

    private QuestionBasket basket = new QuestionBasket();

    public QuestionBasket getBasket() {
        return basket;
    }

    public void setBasket(QuestionBasket basket) {
        this.basket = basket;
    }

    @PostConstruct
    public void init() {
    }

    public int getRow(String id) {
        try {
            String[] ss = id.split(":");
            String s = ss[3];
            int i = Integer.parseInt(s);
            return i;
        } catch (Exception e) {
        }
        return -1;
    }

    public void processBasket(String activeId, int row) {
        if (activeId == null || "0".equals(activeId)) {
            ListChoiceQuestion lcq = JsfHelper.getBean("listChoiceQuestion");
            ChoiceQuestion gq = (ChoiceQuestion) lcq.getLcqs().getModels().get(row);
            //System.out.println(gq.getCleanName());
            if (!this.getBasket().getChoices().contains(gq)) {
                this.getBasket().getChoices().add(gq);
            }
        } else if ("1".equals(activeId)) {
            ListMultiChoiceQuestion lcq = JsfHelper.getBean("listMultiChoiceQuestion");
            MultiChoiceQuestion gq = (MultiChoiceQuestion) lcq.getLcqs().getModels().get(row);
            //System.out.println(gq.getCleanName());
            if (!this.getBasket().getMultiChoices().contains(gq)) {
                this.getBasket().getMultiChoices().add(gq);
            }

        } else if ("2".equals(activeId)) {
            ListFillQuestion lcq = JsfHelper.getBean("listFillQuestion");
            FillQuestion gq = (FillQuestion) lcq.getLcqs().getModels().get(row);
            //System.out.println(gq.getCleanName());
            if (!this.getBasket().getFills().contains(gq)) {
                this.getBasket().getFills().add(gq);
            }

        } else if ("3".equals(activeId)) {
            ListJudgeQuestion lcq = JsfHelper.getBean("listJudgeQuestion");
            JudgeQuestion gq = (JudgeQuestion) lcq.getLcqs().getModels().get(row);
            //System.out.println(gq.getCleanName());
            if (!this.getBasket().getJudges().contains(gq)) {
                this.getBasket().getJudges().add(gq);
            }

        } else if ("4".equals(activeId)) {
            ListEssayQuestion lcq = JsfHelper.getBean("listEssayQuestion");
            EssayQuestion gq = (EssayQuestion) lcq.getLcqs().getModels().get(row);
            //System.out.println(gq.getCleanName());
            if (!this.getBasket().getEssaies().contains(gq)) {
                this.getBasket().getEssaies().add(gq);
            }

        } else if ("5".equals(activeId)) {
            ListFileQuestion lcq = JsfHelper.getBean("listFileQuestion");
            FileQuestion gq = (FileQuestion) lcq.getQuestions().get(row);
            //System.out.println(gq.getCleanName());
            if (!this.getBasket().getFiles().contains(gq)) {
                this.getBasket().getFiles().add(gq);
            }

        } else if ("6".equals(activeId)) {
            ListCaseQuestion lcq = JsfHelper.getBean("listCaseQuestion");
            CaseQuestion gq = (CaseQuestion) lcq.getQuestions().get(row);
            //System.out.println(gq.getCleanName());
            if (!this.getBasket().getCases().contains(gq)) {
                this.getBasket().getCases().add(gq);
            }

        }
        System.out.println(row);
    }

    public void onQustionDrop(DragDropEvent ddEvent) {
        String id = ddEvent.getDragId();
        ListAllQuestion laq = JsfHelper.getBean("listAllQuestion");
        String ai = laq.getActiveId();
        System.out.println("id:" + id + ",ai:" + ai);
        int row = this.getRow(id);
        this.processBasket(ai, row);

    }

    public String batchDeleteQuestion(String type) {
        if ("choice".equals(type)) {
            List<ChoiceQuestion> temp = new LinkedList();
            for (ChoiceQuestion cq : this.basket.getChoices()) {
                if (cq.isSelected()) {
                    temp.add(cq);
                }
            }
            for (ChoiceQuestion c : temp) {
                this.basket.getChoices().remove(c);
            }
        }
        if ("mchoice".equals(type)) {
            List<MultiChoiceQuestion> temp = new LinkedList();
            for (MultiChoiceQuestion cq : this.basket.getMultiChoices()) {
                if (cq.isSelected()) {
                    temp.add(cq);
                }
            }
            for (MultiChoiceQuestion c : temp) {
                this.basket.getMultiChoices().remove(c);
            }
        }
        if ("fill".equals(type)) {
            List<FillQuestion> temp = new LinkedList();
            for (FillQuestion cq : this.basket.getFills()) {
                if (cq.isSelected()) {
                    temp.add(cq);
                }
            }
            for (FillQuestion c : temp) {
                this.basket.getFills().remove(c);
            }
        }
        if ("judge".equals(type)) {
            List<JudgeQuestion> temp = new LinkedList();
            for (JudgeQuestion cq : this.basket.getJudges()) {
                if (cq.isSelected()) {
                    temp.add(cq);
                }
            }
            for (JudgeQuestion c : temp) {
                this.basket.getJudges().remove(c);
            }
        }
        if ("essay".equals(type)) {
            List<EssayQuestion> temp = new LinkedList();
            for (EssayQuestion cq : this.basket.getEssaies()) {
                if (cq.isSelected()) {
                    temp.add(cq);
                }
            }
            for (EssayQuestion c : temp) {
                this.basket.getEssaies().remove(c);
            }
        }
        if ("file".equals(type)) {
            List<FileQuestion> temp = new LinkedList();
            for (FileQuestion cq : this.basket.getFiles()) {
                if (cq.isSelected()) {
                    temp.add(cq);
                }
            }
            for (FileQuestion c : temp) {
                this.basket.getFiles().remove(c);
            }
        }
        if ("case".equals(type)) {
            List<CaseQuestion> temp = new LinkedList();
            for (CaseQuestion cq : this.basket.getCases()) {
                if (cq.isSelected()) {
                    temp.add(cq);
                }
            }
            for (CaseQuestion c : temp) {
                this.basket.getCases().remove(c);
            }
        }
        return null;
    }

    public String clearQuestion(String type) {
        if ("choice".equals(type)) {
            this.basket.getChoices().clear();
        }
        if ("mchoice".equals(type)) {
            this.basket.getMultiChoices().clear();
        }
        if ("fill".equals(type)) {
            this.basket.getFills().clear();
        }
        if ("judge".equals(type)) {
            this.basket.getJudges().clear();
        }
        if ("essay".equals(type)) {
            this.basket.getEssaies().clear();
        }
        if ("file".equals(type)) {
            this.basket.getFiles().clear();
        }
        if ("case".equals(type)) {
            this.basket.getCases().clear();
        }
        return null;
    }
}
