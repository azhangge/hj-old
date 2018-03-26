package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.mindmap.DefaultMindmapNode;
import org.primefaces.model.mindmap.MindmapNode;

import com.hjedu.course.vo.RereMindmapNode;
import com.hjedu.examination.dao.IExamKnowledgeDAO;
import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.ExamKnowledge;
import com.hjedu.examination.entity.ExamModuleModel;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

/**
 * 按试题模块显示KnowledgeMindmap
 *
 * @author huajie
 */
@ManagedBean
@ViewScoped
public class KnowledgeMindMap implements Serializable {

    IExamKnowledgeDAO dao = SpringHelper.getSpringBean("ExamKnowledgeDAO");
    IExamModule2DAO mdao = SpringHelper.getSpringBean("ExamModule2DAO");
    ExamModuleModel em = null;
    List<ExamKnowledge> knows = null;
    ExamKnowledge selectedKnowledge = null;

    private MindmapNode root;

    public ExamModuleModel getEm() {
        return em;
    }

    public void setEm(ExamModuleModel em) {
        this.em = em;
    }

    public List<ExamKnowledge> getKnows() {
        return knows;
    }

    public void setKnows(List<ExamKnowledge> knows) {
        this.knows = knows;
    }

    public ExamKnowledge getSelectedKnowledge() {
        return selectedKnowledge;
    }

    public void setSelectedKnowledge(ExamKnowledge selectedKnowledge) {
        this.selectedKnowledge = selectedKnowledge;
    }

    public MindmapNode getRoot() {
        return root;
    }

    public void setRoot(MindmapNode root) {
        this.root = root;
    }

    @PostConstruct
    public void init() {
        String moduleId = JsfHelper.getRequest().getParameter("module_id");
        if (moduleId != null) {
            em = mdao.findExamModuleModel(moduleId);
            this.knows = dao.findExamKnowledgeByModule(moduleId);
            this.createMinds();
        }
    }

    private void createMinds() {
        root = new RereMindmapNode(em.getName(), em.getName(), "FFCC00", false);

        for (ExamKnowledge k : this.knows) {
            RereMindmapNode ips = new RereMindmapNode(k.getTitle() + "(" + k.getChoices().size() + ")", k.getContent(), "6e9ebf", true);
            ips.setId(k.getId());
            ips.setType("knowledge");
            
            root.addNode(ips);
        }
    }

    /**
     * 选择某知识点节点时触发此方法
     *
     * @param event
     */
    public void onNodeSelect(SelectEvent event) {
        RereMindmapNode node = (RereMindmapNode) event.getObject();

        //populate if not already loaded
        if (node.getChildren().isEmpty()) {
            String kid = node.getId();
            MyLogger.println(kid);
            if (kid != null) {
                ExamKnowledge know = this.findKnowledgeInList(kid);
                //找出此知识点关联的选择题
                List<ChoiceQuestion> choices = know.getChoices();
                if (choices != null) {
                    for (ChoiceQuestion cq : choices) {
                        //将选择题显示为mindmap节点
                        String name = cq.getCleanName();
                        if (name.length() > 10) {
                            name = name.substring(0, 9);
                        }
                        RereMindmapNode ques = new RereMindmapNode(name, "", "82c542", false);
                        ques.setType("question");
                        node.addNode(ques);
                    }
                }
            }
        }
    }

    public void onNodeDblselect(SelectEvent event) {
        RereMindmapNode node = (RereMindmapNode) event.getObject();
        if (node != null) {
            this.selectedKnowledge = dao.findExamKnowledge(node.getId());
        }
    }

    /**
     * 从本页已经加载的知识点列表中查找某知识点，避免多次访问数据库
     *
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
