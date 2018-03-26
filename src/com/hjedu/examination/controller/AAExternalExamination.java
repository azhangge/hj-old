package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.customer.dao.IAdminDAO;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.dao.IDictionaryDAO;
import com.hjedu.examination.dao.IExamDepartmentDAO;
import com.hjedu.examination.dao.IExamLabelDAO;
import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.dao.IExamPaperManualDAO;
import com.hjedu.examination.dao.IExamPaperRandom2DAO;
import com.hjedu.examination.dao.IExamPaperRandomDAO;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.entity.ExamDepartment;
import com.hjedu.examination.entity.ExamLabel;
import com.hjedu.examination.entity.ExamPaperManual;
import com.hjedu.examination.entity.ExamPaperRandom;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.entity.random2.ExamPaperRandom2;
import com.hjedu.platform.service.ILogService;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAExternalExamination implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IExaminationDAO examDAO = SpringHelper.getSpringBean("ExaminationDAO");
    IAdminDAO adminDAO = SpringHelper.getSpringBean("AdminDAO");
    IExamModule2DAO module2DAO = SpringHelper.getSpringBean("ExamModule2DAO");
    IExamDepartmentDAO departmentDAO = SpringHelper.getSpringBean("ExamDepartmentDAO");
    IExamPaperRandomDAO randomDAO = SpringHelper.getSpringBean("ExamPaperRandomDAO");
    IExamPaperManualDAO manualDAO = SpringHelper.getSpringBean("ExamPaperManualDAO");
    IExamPaperRandom2DAO random2DAO = SpringHelper.getSpringBean("ExamPaperRandom2DAO");
    IExamLabelDAO examLabelDAO = SpringHelper.getSpringBean("ExamLabelDAO");
    Examination exam;
    List<ExamDepartment> departments;
    boolean flag = false;
    TreeNode root = new DefaultTreeNode();
    IDictionaryDAO dicDAO = SpringHelper.getSpringBean("DictionaryDAO");
    List<DictionaryModel> dics = new ArrayList();
    private TreeNode[] selectedNodes;
    List<DictionaryModel> selectedNodes2 = new LinkedList();
    private List<TreeNode> nodes = new LinkedList();
    List<AdminInfo> admins = new LinkedList();
    List<ExamPaperRandom> randomPapers;
    List<ExamPaperManual> manualPapers;
    List<ExamPaperRandom2> random2Papers;
    List<ExamLabel> labels;
    String bussinessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    
    public Examination getExam() {
        return exam;
    }

    public void setExam(Examination exam) {
        this.exam = exam;
    }

    public List<ExamLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<ExamLabel> labels) {
        this.labels = labels;
    }

    public List<ExamDepartment> getDepartments() {
        return departments;
    }

    public void setDepartments(List<ExamDepartment> departments) {
        this.departments = departments;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public List<AdminInfo> getAdmins() {
        return admins;
    }

    public void setAdmins(List<AdminInfo> admins) {
        this.admins = admins;
    }

    public List<ExamPaperRandom> getRandomPapers() {
        return randomPapers;
    }

    public void setRandomPapers(List<ExamPaperRandom> randomPapers) {
        this.randomPapers = randomPapers;
    }

    public List<ExamPaperManual> getManualPapers() {
        return manualPapers;
    }

    public void setManualPapers(List<ExamPaperManual> manualPapers) {
        this.manualPapers = manualPapers;
    }

    public List<ExamPaperRandom2> getRandom2Papers() {
        return random2Papers;
    }

    public void setRandom2Papers(List<ExamPaperRandom2> random2Papers) {
        this.random2Papers = random2Papers;
    }

    public List<DictionaryModel> getDics() {
        return dics;
    }

    public void setDics(List<DictionaryModel> dics) {
        this.dics = dics;
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
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
        String id = request.getParameter("id");
        String moduleId = request.getParameter("module_id");
        if (id != null) {
            this.flag = true;
            this.exam = this.examDAO.findExamination(id);
        } else {
            this.exam = new Examination();
            this.exam.setIfExternal(true);
            this.exam.setPaperType("random2");
            //this.exam.setManualId("-1");
            //this.exam.setRandomId("-1");
            //this.buildModuleExaminations(exam);
        }
        this.loadStructure();
        this.loadAdmins();
        this.loadLabels();
        this.randomPapers = this.randomDAO.findAllExamPaperRandom(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        this.manualPapers = this.manualDAO.findAllExamPaperManual(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        this.random2Papers = this.random2DAO.findAllExamPaperRandom2(CookieUtils.getBusinessId(JsfHelper.getRequest()));
    }

    public void loadLabels() {
        this.labels = this.examLabelDAO.findAllExamLabel();
        String lstr = this.exam.getLabelStr();
        if (lstr != null) {
            for (ExamLabel e : labels) {
                if (lstr.contains(e.getId())) {
                    e.setSelected(true);
                }
            }
        }
    }

    public void loadAdmins() {
        this.admins = this.adminDAO.findAllAdmin();
        List<AdminInfo> exs = this.exam.getAdmins();
        if (exs != null && this.admins != null) {
            for (AdminInfo e : admins) {
                for (AdminInfo ex : exs) {
                    if (ex.getId().equals(e.getId())) {
                        e.setSelected(true);
                        break;
                    }
                }
            }
        }
    }

    private boolean testIfSelected(DictionaryModel dd) {
        if (this.exam.getGroupStr().trim().contains(String.valueOf(dd.getId()))) {
            dd.setSelected(true);
            return true;
        } else {
            return false;
        }
    }

    private void loadStructure() {
        this.root = new DefaultTreeNode();
        DictionaryModel dm = dicDAO.findDictionaryModel(bussinessId);
        test(dm, root);
        this.dics = null;
        this.dics = dm.getSons();

    }

    public void test(DictionaryModel dd, TreeNode node) {
        //检查，如果某节点的所有子节点中只要有节点被选中，此节点就应该展开
        boolean k = false;
        List<DictionaryModel> ks = dicDAO.loadAllDescendants(dd.getId());
        for (DictionaryModel ddm : ks) {
            if (this.testIfSelected(ddm)) {
                k = true;
                break;
            }
        }
        if (k) {
            node.setExpanded(true);
        }
        //判断是否应该展开完毕
        //判断是否应该选中,若选中，也要展开（因为可能有子元素未选中，而父元素选中的情况）
        if (this.testIfSelected(dd)) {
            //System.out.println(dd.getId() + dd.getName());
            //node.setSelected(true);
            node.setExpanded(true);
        } else {
            node.setSelected(false);
        }
        //
        List<DictionaryModel> ls = dicDAO.findAllSonDictionaryModel(dd.getId());
        Collections.sort(ls);
        dd.setSons(ls);
        if (ls.isEmpty()) {
            node.setSelectable(true);
            return;
        } else {
            node.setSelectable(false);
            for (DictionaryModel d : ls) {
                TreeNode t = new DefaultTreeNode(d, node);
                test(d, t);
            }
        }
    }

    //以下三个方法用于上级节点选取时级联到子节点
    public void checkSons(String id, boolean b) {
        DictionaryModel emm = this.dicDAO.findDictionaryModel(id);
        List<DictionaryModel> dics1 = this.dicDAO.loadAllDescendants(id);
        dics1.add(emm);
        this.test3(dics1, root, b);
    }

    public void test3(List<DictionaryModel> dds, TreeNode tn, boolean b) {
        //List<ExamModuleModel> mos = this.role.getModules();
        DictionaryModel top = (DictionaryModel) tn.getData();
        if (this.testIfContain(dds, top)) {
            top.setSelected(b);
        }
        List<TreeNode> tns = tn.getChildren();
        if (tns == null) {
            return;
        } else {
            for (TreeNode t : tns) {
                test3(dds, t, b);
            }
        }
    }

    private boolean testIfContain(List<DictionaryModel> dds, DictionaryModel emm) {
        for (DictionaryModel em : dds) {
            if (em != null && emm != null) {
                if (em.getId().equals(emm.getId())) {
                    //break;
                    return true;
                }
            }
        }
        return false;
    }
    //级联结束

    private void checkDepartment() {
        StringBuilder sb = new StringBuilder();
        if (this.departments != null) {
            for (ExamDepartment ed : this.departments) {
                if (ed.isSelected()) {
                    sb.append(ed.getName());
                    sb.append(",");
                }
            }
        }
        this.exam.setGroupStr(sb.toString());
    }

    //以下三方法用于提交考试前获取考试开放的部门
    public List<DictionaryModel> fetchDictionaryModels() {
        this.selectedNodes2.clear();
        this.testDictionaryModel2(this.selectedNodes2, root);
        return this.selectedNodes2;
    }

    public void testDictionaryModel2(List<DictionaryModel> ms, TreeNode root) {
        List<TreeNode> ls = root.getChildren();
        if (ls.isEmpty()) {
            return;
        } else {
            for (TreeNode d : ls) {
                DictionaryModel em = (DictionaryModel) d.getData();
                if (em.isSelected()) {
                    ms.add(em);
                }
                testDictionaryModel2(ms, d);
            }
        }
    }

    private void checkDepartment2() {
        this.fetchDictionaryModels();
        StringBuilder sb = new StringBuilder();
        Set<DictionaryModel> set = new HashSet();
        if (this.selectedNodes2 != null) {
            for (DictionaryModel ed : this.selectedNodes2) {
                set.add(ed);
                //set.addAll(this.dicDAO.loadAllDescendants(d.getId()));

            }
        }
        for (DictionaryModel dm : set) {
            sb.append(dm.getId());
            sb.append(",");
        }
        String sbb = sb.toString().trim();
        if ("".equals(sbb)) {
            String root123 = this.bussinessId;
            sbb = root123;
        }
        this.exam.setGroupStr(sbb);
    }
//获取结束

    public List<AdminInfo> fetchAdmins() {
        List<AdminInfo> exxs = new ArrayList();
        if (this.admins != null) {
            for (AdminInfo ex : this.admins) {
                if (ex.isSelected()) {
                    exxs.add(ex);
                }
            }
        }
        return exxs;
    }
    
    public void resetLabels() {
        StringBuilder sb=new StringBuilder();
        if (this.labels != null) {
            for (ExamLabel ex : this.labels) {
                if (ex.isSelected()) {
                    sb.append(ex.getId());
                    sb.append(";");
                }
            }
        }
        this.exam.setLabelStr(sb.toString());
    }

    public void fetchTotalNum() {
        this.exam.fetchChoiceTotal();
        this.exam.fetchMultiChoiceTotal();
        this.exam.fetchFillTotal();
        this.exam.fetchJudgeTotal();
        this.exam.fetchEssayTotal();
        this.exam.fetchFileTotal();
        this.exam.fetchCaseTotal();
    }

    public String done() {
        this.exam.setAdmins(this.fetchAdmins());
        this.fetchTotalNum();
        this.checkDepartment2();
        this.resetLabels();
        if (flag) {
            this.examDAO.updateExamination(exam);
            ExamPaperPool.refreshExamination(exam.getId());
            this.logger.log("修改了考试：" + exam.getName());
        } else {
            this.logger.log("添加了考试：" + exam.getName());
            this.examDAO.addExamination(exam);
            ExamPaperPool.addExamination(exam);//更新试卷池中的考试
        }
        AdminSessionBean asb = JsfHelper.getBean("adminSessionBean");
        asb.refreshAdmin();
        return "ListExternalExamination?faces-redirect=true";
    }
}
