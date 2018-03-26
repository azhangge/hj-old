package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.hjedu.examination.dao.IExamKnowledgeDAO;
import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.entity.ExamKnowledge;
import com.hjedu.examination.entity.ExamModuleModel;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class KnowledgeTargetPanel implements Serializable {

    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    IExamKnowledgeDAO knowledgeDAO = SpringHelper.getSpringBean("ExamKnowledgeDAO");
    List<ExamModuleModel> targets;
    ExamModuleModel target = null;
    List<ExamModuleModel> dics = new ArrayList();
    ExamModuleModel dic = new ExamModuleModel();
    TreeNode root = new DefaultTreeNode();
    TreeNode selectedNode;
    String businessId;
    
    public ExamModuleModel getTarget() {
        return target;
    }

    public void setTarget(ExamModuleModel target) {
        this.target = target;
    }

    public List<ExamModuleModel> getDics() {
        return dics;
    }

    public void setDics(List<ExamModuleModel> dics) {
        this.dics = dics;
    }

    public ExamModuleModel getDic() {
        return dic;
    }

    public void setDic(ExamModuleModel dic) {
        this.dic = dic;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public List<ExamModuleModel> getTargets() {
        return targets;
    }

    public void setTargets(List<ExamModuleModel> targets) {
        this.targets = targets;
    }

    @PostConstruct
    public void init() {
        //HttpServletRequest request = JsfHelper.getRequest();
    	this.businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        this.targets = this.moduleDAO.findAllExamModuleModel(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        this.loadStructure();
    }

    private void loadStructure() {
        this.root = new DefaultTreeNode();
        ExamModuleModel dm = moduleDAO.findExamModuleModel(this.businessId);
        test(dm, root);
        this.dics = null;
        this.dics = dm.getSons();
    }

    public void test(ExamModuleModel dd, TreeNode node) {
        //node.setExpanded(true);
        List<ExamModuleModel> ls = moduleDAO.findAllSonExamModuleModel(dd.getId(), CookieUtils.getBusinessId(JsfHelper.getRequest()));
        Collections.sort(ls);
        dd.setSons(ls);
        if (ls.isEmpty()) {
            node.setSelectable(true);
            return;
        } else {
            node.setSelectable(false);
            for (ExamModuleModel d : ls) {
                TreeNode t = new DefaultTreeNode(d, node);
                test(d, t);
            }
        }
    }

    public void batchMove() {
        if (this.selectedNode != null) {
            this.target = (ExamModuleModel) this.selectedNode.getData();
        }
        if (this.target != null) {
            ListExamKnowledge lcq = JsfHelper.getBean("listExamKnowledge");
            List<ExamKnowledge> fqs = lcq.getKnows();
            for (ExamKnowledge c : fqs) {
                if (c.isSelected()) {
                    c.setModule(target);
                    this.knowledgeDAO.updateExamKnowledge(c);
                }
            }
            lcq.setKnows(this.knowledgeDAO.findExamKnowledgeByModule(lcq.getModule().getId()));
        }
    }
}
