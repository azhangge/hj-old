package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.examination.dao.ICaseQuestionDAO;
import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IEssayQuestionDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.dao.IFillQuestionDAO;
import com.hjedu.examination.dao.IJudgeQuestionDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.entity.CaseQuestion;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.EssayQuestion;
import com.hjedu.examination.entity.FileQuestion;
import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.hjedu.platform.controller.BasketSession;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class SearchQuestion implements Serializable{

    String qtype = "choice";
    String str = "";
    int num = 0;

    IChoiceQuestionDAO choiceDAO = SpringHelper.getSpringBean("ChoiceQuestionDAO");
    IMultiChoiceQuestionDAO mChoiceDAO = SpringHelper.getSpringBean("MultiChoiceQuestionDAO");
    IFillQuestionDAO fillDAO = SpringHelper.getSpringBean("FillQuestionDAO");
    IJudgeQuestionDAO judgeDAO = SpringHelper.getSpringBean("JudgeQuestionDAO");
    IEssayQuestionDAO essayDAO = SpringHelper.getSpringBean("EssayQuestionDAO");
    IFileQuestionDAO fileDAO = SpringHelper.getSpringBean("FileQuestionDAO");
    ICaseQuestionDAO caseDAO = SpringHelper.getSpringBean("CaseQuestionDAO");

    List<ChoiceQuestion> choices = new ArrayList();
    List<MultiChoiceQuestion> mchoices = new ArrayList();
    List<FillQuestion> fills = new ArrayList();
    List<JudgeQuestion> judges = new ArrayList();
    List<EssayQuestion> essaies = new ArrayList();
    List<FileQuestion> files = new ArrayList();
    List<CaseQuestion> cases = new ArrayList();

    public String getQtype() {
        return qtype;
    }

    public void setQtype(String qtype) {
        this.qtype = qtype;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public List<ChoiceQuestion> getChoices() {
        return choices;
    }

    public void setChoices(List<ChoiceQuestion> choices) {
        this.choices = choices;
    }

    public List<MultiChoiceQuestion> getMchoices() {
        return mchoices;
    }

    public void setMchoices(List<MultiChoiceQuestion> mchoices) {
        this.mchoices = mchoices;
    }

    public List<FillQuestion> getFills() {
        return fills;
    }

    public void setFills(List<FillQuestion> fills) {
        this.fills = fills;
    }

    public List<JudgeQuestion> getJudges() {
        return judges;
    }

    public void setJudges(List<JudgeQuestion> judges) {
        this.judges = judges;
    }

    public List<EssayQuestion> getEssaies() {
        return essaies;
    }

    public void setEssaies(List<EssayQuestion> essaies) {
        this.essaies = essaies;
    }

    public List<FileQuestion> getFiles() {
        return files;
    }

    public void setFiles(List<FileQuestion> files) {
        this.files = files;
    }

    public List<CaseQuestion> getCases() {
        return cases;
    }

    public void setCases(List<CaseQuestion> cases) {
        this.cases = cases;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @PostConstruct
    public void init() {

    }

    public String search() {
        if ("".equals(str.trim())) {
            JsfHelper.error("关键词不能为空！");
            return null;
        }
        String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        if (qtype.equals("choice")) {
            this.choices = this.choiceDAO.findQuestionByLike(str,businessId);
            num = choices.size();
        } else if (qtype.equals("mchoice")) {
            this.mchoices = this.mChoiceDAO.findQuestionByLike(str,businessId);
            num = mchoices.size();
        } else if (qtype.equals("fill")) {
            this.fills = this.fillDAO.findQuestionByLike(str,businessId);
            num = fills.size();
        } else if (qtype.equals("judge")) {
            this.judges = this.judgeDAO.findQuestionByLike(str,businessId);
            num = judges.size();
        } else if (qtype.equals("essay")) {
            this.essaies = this.essayDAO.findQuestionByLike(str,businessId);
            num = essaies.size();
        } else if (qtype.equals("file")) {
            this.files = this.fileDAO.findQuestionByLike(str,businessId);
            num = files.size();
        } else if (qtype.equals("case")) {
            this.cases = this.caseDAO.findQuestionByLike(str,businessId);
            num = cases.size();
        }
        return null;
    }

    public String addToBasket() {
        BasketSession bs = JsfHelper.getBean("basketSession");
        if (qtype.equals("choice")) {
            for (ChoiceQuestion c : choices) {
                if (c.isSelected()) {
                    if (!bs.getBasket().getChoices().contains(c)) {
                        bs.getBasket().getChoices().add(c);
                    }
                }
            }
        } else if (qtype.equals("mchoice")) {
            for (MultiChoiceQuestion c : mchoices) {
                if (c.isSelected()) {
                    if (!bs.getBasket().getMultiChoices().contains(c)) {
                        bs.getBasket().getMultiChoices().add(c);
                    }
                }
            }
        } else if (qtype.equals("fill")) {
            for (FillQuestion c : fills) {
                if (c.isSelected()) {
                    if (!bs.getBasket().getFills().contains(c)) {
                        bs.getBasket().getFills().add(c);
                    }
                }
            }
        } else if (qtype.equals("judge")) {
            for (JudgeQuestion c : judges) {
                if (c.isSelected()) {
                    if (!bs.getBasket().getJudges().contains(c)) {
                        bs.getBasket().getJudges().add(c);
                    }
                }
            }
        } else if (qtype.equals("essay")) {
            for (EssayQuestion c : essaies) {
                if (c.isSelected()) {
                    if (!bs.getBasket().getEssaies().contains(c)) {
                        bs.getBasket().getEssaies().add(c);
                    }
                }
            }
        } else if (qtype.equals("file")) {
            for (FileQuestion c : files) {
                if (c.isSelected()) {
                    if (!bs.getBasket().getFiles().contains(c)) {
                        bs.getBasket().getFiles().add(c);
                    }
                }
            }
        } else if (qtype.equals("case")) {
            for (CaseQuestion c : cases) {
                if (c.isSelected()) {
                    if (!bs.getBasket().getCases().contains(c)) {
                        bs.getBasket().getCases().add(c);
                    }
                }
            }
        }
        return null;
    }

}
