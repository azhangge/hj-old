package com.hjedu.examination.controller;

import com.google.gson.annotations.Expose;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IEssayQuestionDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.dao.IFillQuestionDAO;
import com.hjedu.examination.dao.IJudgeQuestionDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.dao.IQuestionCollectionDAO;
import com.hjedu.examination.entity.QuestionCollection;
import com.hjedu.platform.controller.ClientSession;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class QuestionCollectionList implements Serializable {

    ClientSession cs = JsfHelper.getBean("clientSession");
    IQuestionCollectionDAO collectDAO = SpringHelper.getSpringBean("QuestionCollectionDAO");
    IChoiceQuestionDAO choiceQuestionDAO = SpringHelper.getSpringBean("ChoiceQuestionDAO");
    IMultiChoiceQuestionDAO multiChoiceQuestionDAO = SpringHelper.getSpringBean("MultiChoiceQuestionDAO");
    IFillQuestionDAO fillQuestionDAO = SpringHelper.getSpringBean("FillQuestionDAO");
    IJudgeQuestionDAO judgeQuestionDAO = SpringHelper.getSpringBean("JudgeQuestionDAO");
    IEssayQuestionDAO essayQuestionDAO = SpringHelper.getSpringBean("EssayQuestionDAO");
    IFileQuestionDAO fileQuestionDAO = SpringHelper.getSpringBean("FileQuestionDAO");
    @Expose
    List<QuestionCollection> choices;
    @Expose
    List<QuestionCollection> mchoices;
    @Expose
    List<QuestionCollection> fills;
    @Expose
    List<QuestionCollection> judges;
    @Expose
    List<QuestionCollection> essaies;
    @Expose
    List<QuestionCollection> files;

    BbsUser bu;

    public ClientSession getCs() {
        return cs;
    }

    public void setCs(ClientSession cs) {
        this.cs = cs;
    }

    public IQuestionCollectionDAO getCollectDAO() {
        return collectDAO;
    }

    public void setCollectDAO(IQuestionCollectionDAO collectDAO) {
        this.collectDAO = collectDAO;
    }

    public List<QuestionCollection> getChoices() {
        return choices;
    }

    public void setChoices(List<QuestionCollection> choices) {
        this.choices = choices;
    }

    public List<QuestionCollection> getMchoices() {
        return mchoices;
    }

    public void setMchoices(List<QuestionCollection> mchoices) {
        this.mchoices = mchoices;
    }

    public List<QuestionCollection> getFills() {
        return fills;
    }

    public void setFills(List<QuestionCollection> fills) {
        this.fills = fills;
    }

    public List<QuestionCollection> getJudges() {
        return judges;
    }

    public void setJudges(List<QuestionCollection> judges) {
        this.judges = judges;
    }

    public List<QuestionCollection> getEssaies() {
        return essaies;
    }

    public void setEssaies(List<QuestionCollection> essaies) {
        this.essaies = essaies;
    }

    public List<QuestionCollection> getFiles() {
        return files;
    }

    public void setFiles(List<QuestionCollection> files) {
        this.files = files;
    }

    @PostConstruct
    public void init() {
        bu = cs.getUsr();
        this.synDB(bu);
    }

    public void synDB(BbsUser bu) {

        if (bu != null) {
            this.choices = this.collectDAO.findQuestionCollectionByUser(bu.getId(), "choice");
            this.mchoices = this.collectDAO.findQuestionCollectionByUser(bu.getId(), "mchoice");
            this.fills = this.collectDAO.findQuestionCollectionByUser(bu.getId(), "fill");
            this.judges = this.collectDAO.findQuestionCollectionByUser(bu.getId(), "judge");
            this.essaies = this.collectDAO.findQuestionCollectionByUser(bu.getId(), "essay");
            this.files = this.collectDAO.findQuestionCollectionByUser(bu.getId(), "file");
        }
    }

    public void synDB(BbsUser bu, String qtype) {

        if (bu != null) {
            if ("choice".equals(qtype)) {
                this.choices = this.collectDAO.findQuestionCollectionByUser(bu.getId(), "choice");
            }
            if ("mchoice".equals(qtype)) {
                this.mchoices = this.collectDAO.findQuestionCollectionByUser(bu.getId(), "mchoice");
            }
            if ("fill".equals(qtype)) {
                this.fills = this.collectDAO.findQuestionCollectionByUser(bu.getId(), "fill");
            }
            if ("judge".equals(qtype)) {
                this.judges = this.collectDAO.findQuestionCollectionByUser(bu.getId(), "judge");
            }
            if ("essay".equals(qtype)) {
                this.essaies = this.collectDAO.findQuestionCollectionByUser(bu.getId(), "essay");
            }
            if ("file".equals(qtype)) {
                this.files = this.collectDAO.findQuestionCollectionByUser(bu.getId(), "file");
            }
        }
    }

    public String delete(String id) {
        this.collectDAO.deleteQuestionCollection(id);
        this.synDB(bu);
        return null;
    }

    public String deleteAllByUserAndType(String userId, String type) {
        this.collectDAO.deleteAllQuestionCollectionByUser(userId, type);
        this.synDB(bu);
        return null;
    }
}
