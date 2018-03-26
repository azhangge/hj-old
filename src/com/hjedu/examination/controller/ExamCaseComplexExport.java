package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.DualListModel;
import org.primefaces.model.TreeNode;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.service.impl.ExamCaseExportService;
import com.hjedu.examination.service.impl.ExamDepartmentService;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ExamCaseComplexExport implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
    IExaminationDAO examinationDAO = SpringHelper.getSpringBean("ExaminationDAO");
    List<ExamCase> cases=new ArrayList();
    private DualListModel<Examination> examinations;
    //以下属性用于绑定页面计算部门或考试的平均分
    String departmentId;
    String examinationId;
    ExamDepartmentService dicDAO = SpringHelper.getSpringBean("ExamDepartmentService");
    List<DictionaryModel> dics = new ArrayList();
    DictionaryModel dic = new DictionaryModel();
    boolean flag = false;
    List<SelectItem> ss = new ArrayList();
    TreeNode root = new DefaultTreeNode();
    private TreeNode[] selectedNodes;
    private List<TreeNode> nodes = new LinkedList();
    Date begainDate = new Date(System.currentTimeMillis() - 1000L * 60L * 60L * 24L * 30L);
    Date endDate = new Date();
    String bussinessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    
    public DictionaryModel getDic() {
        return dic;
    }

    public void setDic(DictionaryModel dic) {
        this.dic = dic;
    }

    public Date getBegainDate() {
        return begainDate;
    }

    public void setBegainDate(Date begainDate) {
        this.begainDate = begainDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
    }

    private void buildExamination() {
        List<Examination> exams = this.examinationDAO.findAllExamination(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        List<Examination> source = new ArrayList<Examination>();
        List<Examination> target = new ArrayList<Examination>();
        source.addAll(exams);
        examinations = new DualListModel<Examination>(source, target);
    }

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

    public String exportComplex() {
        //找出所有选中的部门
        List<DictionaryModel> dms = new LinkedList();
        if (this.selectedNodes != null) {
            for (TreeNode t : this.selectedNodes) {
                DictionaryModel dm = (DictionaryModel) t.getData();
                dms.add(dm);
            }
        }
        StringBuilder dmsStrBuffer = new StringBuilder();
        for (DictionaryModel dm : dms) {
            dmsStrBuffer.append(dm.getId());
            dmsStrBuffer.append(";");
        }
        String dmlStr=dmsStrBuffer.toString();
        //找出所有选中的考试
        List<Examination> exams = this.examinations.getTarget();
        //找出所有考试的考试成绩实例
        List<ExamCase> ecs = new LinkedList();
        for (Examination ex : exams) {
            List<ExamCase> ec = this.examCaseDAO.findSubmittedExamCaseByExamination(ex.getId());
            ecs.addAll(ec);
        }
        //将符合部门筛选条件的考试成绩实例加入cases中
        this.cases.clear();
        for (ExamCase ec : ecs) {
            if (ec.getGenTime().getTime() >= this.begainDate.getTime() && ec.getGenTime().getTime() <= this.endDate.getTime()) {
                BbsUser bu = ec.getUser();
                if (bu != null) {
                    //若未选择部门，则导出全部部门
                    if ("".equals(dmlStr)||bu.testIfIn(dmlStr)) {
                        this.cases.add(ec);
                    }
                }
            }
        }
        ExamCaseExportService ees = SpringHelper.getSpringBean("ExamCaseExportService");
        long id = System.currentTimeMillis();
        String bn = "RereExamComplex" + id + ".xls";
        HttpServletRequest request = JsfHelper.getRequest();
        String nfn = request.getServletContext().getRealPath("upload/" + bn);
        ees.exportComplex(nfn, this.cases);
        MyLogger.println(nfn);
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        try {
            String url = request.getContextPath() + "/servlet/ExportComplexExamCase?id=" + id;
            MyLogger.echo(url);
            response.sendRedirect(url);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
