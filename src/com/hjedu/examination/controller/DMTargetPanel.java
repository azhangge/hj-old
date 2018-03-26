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
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.FileQuestion;
import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.examination.entity.GeneralQuestion;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.hjedu.examination.service.impl.ExamModule2Service;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class DMTargetPanel implements Serializable {

    
    ExamModule2Service moduleDAO = SpringHelper.getSpringBean("ExamModule2Service");
    IFillQuestionDAO fillQuestionDAO = SpringHelper.getSpringBean("FillQuestionDAO");
    IChoiceQuestionDAO choiceQuestionDAO = SpringHelper.getSpringBean("ChoiceQuestionDAO");
    IMultiChoiceQuestionDAO multiChoiceQuestionDAO = SpringHelper.getSpringBean("MultiChoiceQuestionDAO");
    IJudgeQuestionDAO judgeQuestionDAO = SpringHelper.getSpringBean("JudgeQuestionDAO");
    IEssayQuestionDAO essayQuestionDAO = SpringHelper.getSpringBean("EssayQuestionDAO");
    IFileQuestionDAO fileQuestionDAO = SpringHelper.getSpringBean("FileQuestionDAO");
    ICaseQuestionDAO caseQuestionDAO = SpringHelper.getSpringBean("CaseQuestionDAO");
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
    	this.businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        this.targets = this.moduleDAO.findAllExamModuleModel();
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
        List<ExamModuleModel> ls = moduleDAO.findAllSonExamModuleModel(dd.getId());
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
            ListAllQuestion laq = JsfHelper.getBean("listAllQuestion");
            ListFillQuestion lfq = JsfHelper.getBean("listFillQuestion");
            ListChoiceQuestion lcq = JsfHelper.getBean("listChoiceQuestion");
            ListJudgeQuestion ljq = JsfHelper.getBean("listJudgeQuestion");
            ListMultiChoiceQuestion lmq = JsfHelper.getBean("listMultiChoiceQuestion");
            ListEssayQuestion leq = JsfHelper.getBean("listEssayQuestion");
            ListFileQuestion lffq = JsfHelper.getBean("listFileQuestion");
            ListCaseQuestion cffq = JsfHelper.getBean("listCaseQuestion");
            String activeId = laq.getActiveId();
            if (activeId.equals("0")) {
                List<ChoiceQuestion> fqs = lcq.getLcqs().getModels();
                for (GeneralQuestion c : fqs) {
                    if (c.isSelected()) {
                        c.setModule(target);
                        this.choiceQuestionDAO.updateChoiceQuestion((ChoiceQuestion)c);
                    }
                }
                lcq.getLcqs().getModels().clear();
                lcq.getLcqs().getModels().addAll(this.choiceQuestionDAO.findChoiceQuestionByModule(lcq.getModule().getId()));
            } else if (activeId.equals("1")) {
                List<MultiChoiceQuestion> mqs = lmq.getLcqs().getModels();
                for (GeneralQuestion c : mqs) {
                    if (c.isSelected()) {
                        c.setModule(target);
                        this.multiChoiceQuestionDAO.updateMultiChoiceQuestion((MultiChoiceQuestion)c);
                    }
                }
                lmq.getLcqs().getModels().clear();
                lmq.getLcqs().getModels().addAll(this.multiChoiceQuestionDAO.findMultiChoiceQuestionByModule(lmq.getModule().getId()));
            } else if (activeId.equals("2")) {
                List<FillQuestion> fqs = lfq.getLcqs().getModels();
                for (FillQuestion c : fqs) {
                    if (c.isSelected()) {
                        c.setModule(target);
                        this.fillQuestionDAO.updateFillQuestion(c);
                    }
                }
                lfq.getLcqs().setModels(this.fillQuestionDAO.findFillQuestionByModule(lfq.getModule().getId()));
            } else if (activeId.equals("3")) {
                List<JudgeQuestion> fqs = ljq.getLcqs().getModels();
                for (JudgeQuestion c : fqs) {
                    if (c.isSelected()) {
                        c.setModule(target);
                        this.judgeQuestionDAO.updateJudgeQuestion(c);
                    }
                }
                ljq.getLcqs().setModels(this.judgeQuestionDAO.findJudgeQuestionByModule(ljq.getModule().getId()));
            } else if (activeId.equals("4")) {
                List<EssayQuestion> fqs = leq.getLcqs().getModels();
                for (EssayQuestion c : fqs) {
                    if (c.isSelected()) {
                        c.setModule(target);
                        this.essayQuestionDAO.updateEssayQuestion(c);
                    }
                }
                leq.getLcqs().setModels(this.essayQuestionDAO.findEssayQuestionByModule(leq.getModule().getId()));
            } else if (activeId.equals("5")) {
                List<FileQuestion> fqs = lffq.getQuestions();
                for (FileQuestion c : fqs) {
                    if (c.isSelected()) {
                        c.setModule(target);
                        this.fileQuestionDAO.updateFileQuestion(c);
                    }
                }
                lffq.setQuestions(this.fileQuestionDAO.findFileQuestionByModule(leq.getModule().getId()));
            } else if (activeId.equals("6")) {
                List<CaseQuestion> fqs = cffq.getQuestions();
                for (CaseQuestion c : fqs) {
                    if (c.isSelected()) {
                        c.setModule(target);
                        this.caseQuestionDAO.updateCaseQuestion(c);
                    }
                }
                cffq.setQuestions(this.caseQuestionDAO.findCaseQuestionByModule(leq.getModule().getId()));
            }
        }
    }
}
