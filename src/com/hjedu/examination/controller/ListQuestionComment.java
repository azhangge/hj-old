package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import com.hjedu.examination.dao.IQuestionCommentDAO;
import com.hjedu.examination.entity.QuestionComment;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListQuestionComment implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IQuestionCommentDAO logDAO = SpringHelper.getSpringBean("QuestionCommentDAO");
    List<QuestionComment> comments;
    private SelectItem[] filterOptions;

    public List<QuestionComment> getComments() {
        return comments;
    }

    public void setComments(List<QuestionComment> comments) {
        this.comments = comments;
    }

    public SelectItem[] getFilterOptions() {
        return filterOptions;
    }

    public void setFilterOptions(SelectItem[] filterOptions) {
        this.filterOptions = filterOptions;
    }
    

   

    @PostConstruct
    public void init() {
        //HttpServletRequest request = JsfHelper.getRequest();
        this.comments = this.logDAO.findAllQuestionComment();
        this.createFilterOptions();
    }

    public String delete(String id) {
        QuestionComment ll=this.logDAO.findQuestionComment(id);
        this.logger.log("删除了用户"+ll.getUser().getName()+" 对试题："+ll.getQuestion().getCleanName()+"的评论");
        this.logDAO.deleteQuestionComment(id);
        this.comments = this.logDAO.findAllQuestionComment();
        return null;
    }

    public String deleteAll() {
        this.logger.log("清空了所有用户评论");
        this.logDAO.deleteAll();
        this.comments = this.logDAO.findAllQuestionComment();
        return null;
    }
    
    public void batchDelete() {
        for (QuestionComment c : this.comments) {
            if (c.isSelected()) {
                this.logger.log("删除了试题评论：" + c.getWord1());
                this.logDAO.deleteQuestionComment(c.getId());
            }
        }
        this.comments.clear();
        this.comments.addAll(this.logDAO.findAllQuestionComment());
        
    }
    
    private void createFilterOptions()  {  
        SelectItem[] options = new SelectItem[3];  
        options[0] = new SelectItem("", "请选择");
        options[1] = new SelectItem("comment", "评论");
        options[2] = new SelectItem("wrong", "报错");
        this.filterOptions=options;  
    } 
    
}
