package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.hjedu.customer.dao.IAdminDAO;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.dao.IExamPaperRandomDAO;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.ExamPaperRandom;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.entity.ModuleRandomPaper;
import com.hjedu.examination.service.impl.ExamModule2Service;
import com.hjedu.examination.service.impl.ExaminationService;
import com.hjedu.platform.service.ILogService;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.util.CookieUtils;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AARandomPaper implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IAdminDAO adminDAO = SpringHelper.getSpringBean("AdminDAO");
    ExamModule2Service module2DAO = SpringHelper.getSpringBean("ExamModule2Service");
    IExamPaperRandomDAO randomDAO = SpringHelper.getSpringBean("ExamPaperRandomDAO");
    ExamPaperRandom paper;
    List<ModuleRandomPaper> mes = new LinkedList();
    boolean flag = false;
    TreeNode root2 = new DefaultTreeNode();
    List<ExamModuleModel> modules = new ArrayList();
    TreeNode root = new DefaultTreeNode();
    private TreeNode[] selectedNodes;
    private List<TreeNode> nodes = new LinkedList();
    List<AdminInfo> admins = new LinkedList();
    String businessId;

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public ExamPaperRandom getPaper() {
        return paper;
    }

    public void setPaper(ExamPaperRandom paper) {
        this.paper = paper;
    }

    public List<ExamModuleModel> getModules() {
        return modules;
    }

    public void setModules(List<ExamModuleModel> modules) {
        this.modules = modules;
    }

    public List<AdminInfo> getAdmins() {
        return admins;
    }

    public void setAdmins(List<AdminInfo> admins) {
        this.admins = admins;
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public TreeNode getRoot2() {
        return root2;
    }

    public void setRoot2(TreeNode root2) {
        this.root2 = root2;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        this.businessId = CookieUtils.getBusinessId(request);
        String id = request.getParameter("id");
        String moduleId = request.getParameter("module_id");
        if (id != null) {
            this.flag = true;
            this.paper = this.randomDAO.findExamPaperRandom(id);
            this.mes = paper.getModulePapers();
        } else {
            this.paper = new ExamPaperRandom();
            //this.buildModuleExaminations(exam);
        }
        this.loadStructure2();
        this.computeTotalNumAndScore();
    }

    private void loadStructure2() {
        this.root2 = new DefaultTreeNode();
        ExamModuleModel dm = module2DAO.findExamModuleModel(this.businessId);
        test2(dm, root2);
        this.modules = null;
        this.modules = dm.getSons();

    }

    public void test2(ExamModuleModel dd, TreeNode node) {
        if (!flag) {
            //如果是新增考试，则新建ModuleExamination并绑定exam
            ModuleRandomPaper me = new ModuleRandomPaper();
            me.setRandomPaper(paper);
            me.setModule(dd);
            dd.setModulePaper(me);
            mes.add(me);
        } else {
            //如果是修改考试，则在已经存在的ModuleExamination绑定exam
            ModuleRandomPaper mee = null;
            for (ModuleRandomPaper me : this.mes) {
                if (me.getModule() != null) {
                    if (me.getModule().getId().equals(dd.getId())) {
                        mee = me;
                        break;
                    }
                }
            }
            if (mee != null) {
                dd.setModulePaper(mee);
            } else {
                mee = new ModuleRandomPaper();
                mee.setRandomPaper(paper);
                mee.setModule(dd);
                dd.setModulePaper(mee);
                mes.add(mee);
            }
        }
        //检查，如果某节点的所有子节点中只要有节点被选中，此节点就应该展开
        List<ExamModuleModel> ks = module2DAO.loadAllDescendants(dd.getId());
        node.setExpanded(true);
        //判断是否应该展开完毕
        //判断是否应该选中,若选中，也要展开（因为可能有子元素未选中，而父元素选中的情况）
        //
        List<ExamModuleModel> ls = module2DAO.findAllSonExamModuleModel(dd.getId());
        Collections.sort(ls);
        dd.setSons(ls);
        if (ls.isEmpty()) {
            node.setSelectable(true);
            return;
        } else {
            node.setSelectable(false);
            for (ExamModuleModel d : ls) {
                if (MenuTree.testIfShow(d)) {//验证是否此管理员对本模块有权限
                    TreeNode t = new DefaultTreeNode(d, node);
                    test2(d, t);
                }
            }
        }
    }

    

    public void synExamination() {
        IExaminationDAO exDAO = SpringHelper.getSpringBean("ExaminationDAO");
        List<Examination> exams = exDAO.findAllExamination(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        List<Examination> exs = new ArrayList();
        if (exams != null) {
            for (Examination ex : exams) {
                if (ex.getRandomPaper() != null) {
                    if (this.paper.getId().equals(ex.getRandomPaper().getId())) {
                        exs.add(ex);
                    }
                }
            }
        }
        for (Examination exam : exs) {
            exam.fetchChoiceTotal();
            exam.fetchMultiChoiceTotal();
            exam.fetchFillTotal();
            exam.fetchJudgeTotal();
            exam.fetchEssayTotal();
            exam.fetchFileTotal();
            exam.fetchCaseTotal();
            exDAO.updateExamination(exam);
        }
    }

    public void computeTotalNumAndScore() {
        this.paper.setModulePapers(mes);
        this.paper.computeTotalNumAndScore();

    }

    public String done() {
        //this.paper.setModulePapers(mes);
        this.computeTotalNumAndScore();
        if (flag) {
            this.randomDAO.updateExamPaperRandom(paper);
            ExamPaperPool.refreshPaper(paper.getId(), "random");//将本考试在试卷池中试卷删除
            this.synExamination();
            this.logger.log("修改了随机试卷：" + paper.getName());
        } else {
        	List list = new ArrayList<>();
        	list.add(ExternalUserUtil.getAdminBySession());
        	paper.setAdmins(list);
        	paper.setBusinessId(CookieUtils.getBusinessId(JsfHelper.getRequest()));
            this.logger.log("添加了随机试卷：" + paper.getName());
            this.randomDAO.addExamPaperRandom(paper);
        }
        //ExamPaperPool.checkExamination();//更新试卷池中的考试
        //更新考试缓存
        ExaminationService examService = SpringHelper.getSpringBean("ExaminationService");
        examService.reBuildCache();
        return "ListRandomPaper?faces-redirect=true";
    }
}
