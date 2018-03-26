package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.DualListModel;
import org.primefaces.model.TreeNode;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IDictionaryDAO;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.AbsentModel;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.service.impl.ExamDepartmentService;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ExamCaseReverseAnalysis implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
    IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
    IExaminationDAO examinationDAO = SpringHelper.getSpringBean("ExaminationDAO");
    List<ExamCase> cases = new ArrayList();
    private DualListModel<Examination> examinations;
    //以下属性用于绑定页面计算部门或考试的平均分
    String departmentId;
    String examinationId;
    Date date1 = new Date(System.currentTimeMillis() - ((long) 1000) * 60 * 60 * 24);
    Date date2 = new Date();
    ExamDepartmentService dicDAO = SpringHelper.getSpringBean("ExamDepartmentService");
    List<DictionaryModel> dics = new ArrayList();
    DictionaryModel dic = new DictionaryModel();
    boolean flag = false;
    List<SelectItem> ss = new ArrayList();
    TreeNode root = new DefaultTreeNode();
    private TreeNode[] selectedNodes;
    private List<TreeNode> nodes = new LinkedList();

    List<AbsentModel> absents = new ArrayList();

    String bussinessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    
    public DictionaryModel getDic() {
        return dic;
    }

    public void setDic(DictionaryModel dic) {
        this.dic = dic;
    }

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public List<AbsentModel> getAbsents() {
        return absents;
    }

    public void setAbsents(List<AbsentModel> absents) {
        this.absents = absents;
    }

    public List<DictionaryModel> getDics() {
        return dics;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getExaminationId() {
        return examinationId;
    }

    public void setExaminationId(String examinationId) {
        this.examinationId = examinationId;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public List<ExamCase> getCases() {
        return cases;
    }

    public void setCases(List<ExamCase> cases) {
        this.cases = cases;
    }

    public List<TreeNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<TreeNode> nodes) {
        this.nodes = nodes;
    }

    public DualListModel<Examination> getExaminations() {
        return examinations;
    }

    public void setExaminations(DualListModel<Examination> examinations) {
        this.examinations = examinations;
    }

    public void setDics(List<DictionaryModel> dics) {
        this.dics = dics;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public List<SelectItem> getSs() {
        return ss;
    }

    public void setSs(List<SelectItem> ss) {
        this.ss = ss;
    }

    @PostConstruct
    public void init() {
        //this.cases = this.examCaseDAO.findAllExamCase();
        this.loadStructure();
        this.buildExamination();
        //this.computeAverageScore();
    }

    /**
     * 加载所有考试
     */
    private void buildExamination() {
        List<Examination> exams = this.examinationDAO.findAllExamination(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        List<Examination> source = new ArrayList<Examination>();
        List<Examination> target = new ArrayList<Examination>();
        source.addAll(exams);
        examinations = new DualListModel<Examination>(source, target);
    }

    /**
     * 加载部门结构
     */
    private void loadStructure() {
        this.root = new DefaultTreeNode();
        DictionaryModel dm = dicDAO.findDictionaryModel(this.bussinessId,bussinessId);
        test(dm, root);
        this.dics = null;
        this.dics = dm.getSons();
    }

    public void test(DictionaryModel dd, TreeNode node) {
        //node.setExpanded(true);
        List<DictionaryModel> ls = dicDAO.findAllSonDictionaryModel(dd.getId());
        Collections.sort(ls);
        dd.setSons(ls);
        if (ls.isEmpty()) {
            return;
        } else {
            for (DictionaryModel d : ls) {
                TreeNode t = new DefaultTreeNode(d, node);
                test(d, t);
            }
        }
    }

    public String analyze() {
        //找出所有选中的部门
        Set<DictionaryModel> dms = new HashSet();
        if (this.selectedNodes != null) {
            for (TreeNode t : this.selectedNodes) {
                DictionaryModel dm = (DictionaryModel) t.getData();
                if (dm != null) {
                    dms.add(dm);
                    List<DictionaryModel> ds = this.dicDAO.loadAllDescendants(dm.getId());
                    if (ds != null) {
                        dms.addAll(ds);
                    }
                }
            }
        }
        this.absents.clear();
        //找出所有选中的考试
        List<Examination> exams = this.examinations.getTarget();
        if (!dms.isEmpty() && exams != null) {
            for (Examination ex : exams) {
                for (DictionaryModel dm : dms) {
                    List<BbsUser> bus = userDAO.findBbsUserByDeptNotInExam(dm.getId(), ex.getId(), date1, date2);//找出所选部门中未参加考试的用户
                    for (BbsUser bu : bus) {
                        AbsentModel am = new AbsentModel();//构建一个缺考模型
                        am.setExam(ex);
                        am.setUser(bu);
                        this.absents.add(am);
                    }
                }
            }
        }
        return null;
    }

}
