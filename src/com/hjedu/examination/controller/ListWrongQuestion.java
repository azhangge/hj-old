package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListWrongQuestion implements Serializable {

    List<ChoiceQuestion> choices = new ArrayList();
    List<MultiChoiceQuestion> mchoices = new ArrayList();

    IChoiceQuestionDAO choiceDAO = SpringHelper.getSpringBean("ChoiceQuestionDAO");
    IMultiChoiceQuestionDAO mchoiceDAO = SpringHelper.getSpringBean("MultiChoiceQuestionDAO");

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
    
    
    
    

    @PostConstruct
    public void init() {
        //刚进入时就自动查询
        synchronizeDB();
    }

    /**
     * 同步数据库
     */
    private void synchronizeDB() {
        this.choices = choiceDAO.findWrongQuestion();
        this.mchoices = this.mchoiceDAO.findWrongQuestion();
    }
    
    
    /**
     * 批量删除单选题
     */
    public void deleteBulkChoice() {
        if (this.choices != null) {
            for (ChoiceQuestion cq : this.choices) {
                if (cq.isSelected()) {
                    this.choiceDAO.deleteChoiceQuestion(cq.getId());
                }
            }
        }
        synchronizeDB();
    }
    
    /**
     * 批量删除多选题
     */
    public void deleteBulkMultiChoice() {
        if (this.mchoices != null) {
            for (MultiChoiceQuestion cq : this.mchoices) {
                if (cq.isSelected()) {
                    this.mchoiceDAO.deleteMultiChoiceQuestion(cq.getId());
                }
            }
        }
        synchronizeDB();
    }

}
