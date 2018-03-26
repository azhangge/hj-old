package com.hjedu.examination.entity;

import com.google.gson.annotations.Expose;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IEssayQuestionDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.dao.IFillQuestionDAO;
import com.hjedu.examination.dao.IJudgeQuestionDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.huajie.util.SpringHelper;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
/**
 * 习题模块
 * 用户收藏题目表
 * @author h j
 *
 */
@Entity
@Table(name = "question_collection")
public class QuestionCollection implements Serializable {

    static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    String id = UUID.randomUUID().toString();
    @Column(name = "qid")
    @Expose        
    String qid;
    @Column(name = "qtype")
    @Expose
    String qtype;
    @ManyToOne
    @JoinColumn(name = "user_id")
    BbsUser bbsUser;

    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    Date genTime = new Date();

    @Transient
    GeneralQuestion question;

    @Transient
    @Expose
    ChoiceQuestion choiceQuestion;
    @Transient
    @Expose
    MultiChoiceQuestion multiChoiceQuestion;
    @Transient
    @Expose
    FillQuestion fillQuestion;
    @Transient
    @Expose
    JudgeQuestion judgeQuestion;
    @Transient
    @Expose
    EssayQuestion essayQuestion;
    @Transient
    @Expose
    FileQuestion fileQuestion;

    @Transient
    boolean selected = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getQtype() {
        return qtype;
    }

    public void setQtype(String qtype) {
        this.qtype = qtype;
    }

    public BbsUser getBbsUser() {
        return bbsUser;
    }

    public void setBbsUser(BbsUser bbsUser) {
        this.bbsUser = bbsUser;
    }

    public GeneralQuestion getQuestion() {
        return question;
    }

    public void setQuestion(GeneralQuestion question) {
        this.question = question;
    }

    public ChoiceQuestion getChoiceQuestion() {
        if (this.qtype.equals("choice")) {
            IChoiceQuestionDAO cDAO = SpringHelper.getSpringBean("ChoiceQuestionDAO");
            choiceQuestion = cDAO.findChoiceQuestion(qid);
        }
        return choiceQuestion;
    }

    public void setChoiceQuestion(ChoiceQuestion choiceQuestion) {
        this.choiceQuestion = choiceQuestion;
    }

    public MultiChoiceQuestion getMultiChoiceQuestion() {
        if (this.qtype.equals("mchoice")) {
            IMultiChoiceQuestionDAO cDAO = SpringHelper.getSpringBean("MultiChoiceQuestionDAO");
            multiChoiceQuestion = cDAO.findMultiChoiceQuestion(qid);
        }
        return multiChoiceQuestion;
    }

    public void setMultiChoiceQuestion(MultiChoiceQuestion multiChoiceQuestion) {
        this.multiChoiceQuestion = multiChoiceQuestion;
    }

    public FillQuestion getFillQuestion() {
        if (this.qtype.equals("fill")) {
            IFillQuestionDAO cDAO = SpringHelper.getSpringBean("FillQuestionDAO");
            fillQuestion = cDAO.findFillQuestion(qid);
        }
        return fillQuestion;
    }

    public void setFillQuestion(FillQuestion fillQuestion) {
        this.fillQuestion = fillQuestion;
    }

    public JudgeQuestion getJudgeQuestion() {
        if (this.qtype.equals("judge")) {
            IJudgeQuestionDAO cDAO = SpringHelper.getSpringBean("JudgeQuestionDAO");
            judgeQuestion = cDAO.findJudgeQuestion(qid);
        }
        return judgeQuestion;
    }

    public void setJudgeQuestion(JudgeQuestion judgeQuestion) {
        this.judgeQuestion = judgeQuestion;
    }

    public EssayQuestion getEssayQuestion() {
        if (this.qtype.equals("essay")) {
            IEssayQuestionDAO cDAO = SpringHelper.getSpringBean("EssayQuestionDAO");
            essayQuestion = cDAO.findEssayQuestion(qid);
        }
        return essayQuestion;
    }

    public void setEssayQuestion(EssayQuestion essayQuestion) {
        this.essayQuestion = essayQuestion;
    }

    public FileQuestion getFileQuestion() {
        if (this.qtype.equals("file")) {
            IFileQuestionDAO cDAO = SpringHelper.getSpringBean("FileQuestionDAO");
            fileQuestion = cDAO.findFileQuestion(qid);
        }
        return fileQuestion;
    }

    public void setFileQuestion(FileQuestion fileQuestion) {
        this.fileQuestion = fileQuestion;
    }

}
