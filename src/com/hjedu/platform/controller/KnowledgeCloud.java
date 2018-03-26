package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.tagcloud.DefaultTagCloudModel;
import org.primefaces.model.tagcloud.TagCloudModel;

import com.hjedu.course.vo.RereTagCloudItem;
import com.hjedu.examination.dao.IExamKnowledgeDAO;
import com.hjedu.examination.entity.ExamKnowledge;
import com.hjedu.examination.entity.ExamModuleModel;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

/**
 * 按试题模块显示KnowledgeCloud
 * @author huajie
 */
@ManagedBean
@ViewScoped
public class KnowledgeCloud implements Serializable{

    IExamKnowledgeDAO dao = SpringHelper.getSpringBean("ExamKnowledgeDAO");
    ExamModuleModel em = null;
    List<ExamKnowledge> knows = null;
    private TagCloudModel model;

    @PostConstruct
    public void init() {
        String moduleId = JsfHelper.getRequest().getParameter("module_id");
        if (moduleId != null) {
            this.knows = dao.findExamKnowledgeByModule(moduleId);
            this.createTags();
        }
    }

    private void createTags() {
        model = new DefaultTagCloudModel();
        for (ExamKnowledge k : this.knows) {
            RereTagCloudItem item = new RereTagCloudItem(k.getTitle(), 1);
            item.setId(k.getId());
            model.addTag(item);
        }

    }

    public void onSelect(SelectEvent event) {
        RereTagCloudItem item = (RereTagCloudItem) event.getObject();
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Selected", item.getLabel());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    /**
     * 从本页已经加载的知识点列表中查找某知识点，避免多次访问数据库
     * @param kid
     * @return 
     */
    private ExamKnowledge findKnowledgeInList(String kid) {
        if (this.knows != null) {
            for (ExamKnowledge k : this.knows) {
                if (k.getId().equals(kid)) {
                    return k;
                }
            }
        }
        return null;
    }

}
