package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.examination.dao.IExamKnowledgeDAO;
import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.entity.CaseQuestion;
import com.hjedu.examination.entity.ExamKnowledge;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListExamKnowledge implements Serializable {
    ILogService logger = SpringHelper.getSpringBean("LogService");
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    IExamKnowledgeDAO knowledgeDAO = SpringHelper.getSpringBean("ExamKnowledgeDAO");
    ExamModuleModel module;
    List<ExamKnowledge> knows;
    
   
    public ExamModuleModel getModule() {
        return module;
    }
    
    public void setModule(ExamModuleModel module) {
        this.module = module;
    }

    public List<ExamKnowledge> getKnows() {
        return knows;
    }

    public void setKnows(List<ExamKnowledge> knows) {
        this.knows = knows;
    }
    
    
    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        String id = request.getParameter("module_id");
        this.module = this.moduleDAO.findExamModuleModel(id);
        if (id == null || "".equals(id)) {
            this.knows = this.knowledgeDAO.findAllExamKnowledge();
        } else {
            this.knows = this.knowledgeDAO.findExamKnowledgeByModule(id);
        }
    }
    
    public void delete(String id) {
        ExamKnowledge cq=this.knowledgeDAO.findExamKnowledge(id);
        this.logger.log("删除了知识点："+cq.getTitle());
        this.knowledgeDAO.deleteExamKnowledge(id);
        this.knows = this.knowledgeDAO.findExamKnowledgeByModule(this.module.getId());
    }
    
    public void batchDelete() {
        for (ExamKnowledge c : this.knows) {
            if (c.isSelected()) {
                this.logger.log("删除了知识点："+c.getTitle());
                this.knowledgeDAO.deleteExamKnowledge(c.getId());
            }
        }
        this.knows = this.knowledgeDAO.findExamKnowledgeByModule(this.module.getId());
    }
    
    public void deleteAll() {
        this.logger.log("删除了模块"+module.getName()+"下的所有知识点");
        this.knowledgeDAO.deleteExamKnowledgeByModule(this.module.getId());
        this.knows = this.knowledgeDAO.findExamKnowledgeByModule(this.module.getId());
    }
    
}
