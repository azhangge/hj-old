package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.hjedu.examination.dao.IDictionaryDAO;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.service.ComplexExamModuleLogic;
import com.hjedu.examination.service.impl.ExamModule2Service;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAModuleExam2 implements Serializable {
    
    ILogService logger = SpringHelper.getSpringBean("LogService");
    ExamModule2Service dicDAO = SpringHelper.getSpringBean("ExamModule2Service");
    ComplexExamModuleLogic complex = SpringHelper.getSpringBean("ComplexExamModuleLogic");
    
    List<ExamModuleModel> dics = new ArrayList();
    
    ExamModuleModel dic = new ExamModuleModel();
    boolean flag = false;
    List<SelectItem> ss = new ArrayList();
    TreeNode root = new DefaultTreeNode();
    List<ExamModuleModel> selectedModules = new LinkedList();
    
    
    //主要用于设置开放的部门
    TreeNode root3 = new DefaultTreeNode();
    IDictionaryDAO dicDAO3 = SpringHelper.getSpringBean("DictionaryDAO");
    List<DictionaryModel> dics3 = new ArrayList();
    private TreeNode[] selectedNodes;
    List<DictionaryModel> selectedNodes2 = new LinkedList();
    private List<TreeNode> nodes = new LinkedList();
    String bussinessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    
    public ExamModuleModel getDic() {
        return dic;
    }
    
    public void setDic(ExamModuleModel dic) {
        this.dic = dic;
    }
    
    public List<ExamModuleModel> getDics() {
        return dics;
    }
    
    public void setDics(List<ExamModuleModel> dics) {
        this.dics = dics;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public TreeNode getRoot3() {
        return root3;
    }

    public void setRoot3(TreeNode root3) {
        this.root3 = root3;
    }

    public List<DictionaryModel> getDics3() {
        return dics3;
    }

    public void setDics3(List<DictionaryModel> dics3) {
        this.dics3 = dics3;
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public List<DictionaryModel> getSelectedNodes2() {
        return selectedNodes2;
    }

    public void setSelectedNodes2(List<DictionaryModel> selectedNodes2) {
        this.selectedNodes2 = selectedNodes2;
    }

    public List<TreeNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<TreeNode> nodes) {
        this.nodes = nodes;
    }
    
    public List<ExamModuleModel> getSelectedModules() {
        return selectedModules;
    }
    
    public void setSelectedModules(List<ExamModuleModel> selectedModules) {
        this.selectedModules = selectedModules;
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
        String id=JsfHelper.getRequest().getParameter("id");
        this.dic=this.dicDAO.findExamModuleModel(id);
        this.loadStructure(id);
        this.loadStructure3();
        flag=true;
    }
    
    
    
    
    
    private boolean testIfSelected(DictionaryModel dd) {
        if (this.dic.getGroupStr() != null) {
            if (this.dic.getGroupStr().trim().contains(String.valueOf(dd.getId()))) {
                dd.setSelected(true);
                return true;
            } 
        }
        return false;
    }

    //加载部门
    private void loadStructure3() {
        this.root3 = new DefaultTreeNode();
        DictionaryModel dm = dicDAO3.findDictionaryModel(bussinessId);
        test5(dm, root3);
        this.dics3 = null;
        this.dics3 = dm.getSons();

    }

    public void test5(DictionaryModel dd, TreeNode node) {
        //检查，如果某节点的所有子节点中只要有节点被选中，此节点就应该展开
        boolean k = false;
        List<DictionaryModel> ks = dicDAO3.loadAllDescendants(dd.getId());
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
        List<DictionaryModel> ls = dicDAO3.findAllSonDictionaryModel(dd.getId());
        Collections.sort(ls);
        dd.setSons(ls);
        if (ls.isEmpty()) {
            node.setSelectable(true);
            return;
        } else {
            node.setSelectable(false);
            for (DictionaryModel d : ls) {
                TreeNode t = new DefaultTreeNode(d, node);
                test5(d, t);
            }
        }
    }

    //以下三个方法用于上级节点选取时级联到子节点
    public void checkSons(String id, boolean b) {
        DictionaryModel emm = this.dicDAO3.findDictionaryModel(id);
        List<DictionaryModel> dics1 = this.dicDAO3.loadAllDescendants(id);
        dics1.add(emm);
        this.test3(dics1, root3, b);
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
    
    
    
    //用于加载本模块及子模块
    public void loadStructure(String moduleId) {
        this.root = new DefaultTreeNode();
        ExamModuleModel dm = dicDAO.findExamModuleModel(moduleId);
        test(dm, root);
        this.dics = null;
        this.dics = dm.getSons();
    }
    
    //用于加载本模块及子模块
    public void test(ExamModuleModel dd, TreeNode node) {
        node.setExpanded(true);
        List<ExamModuleModel> ls = dicDAO.findAllSonExamModuleModel(dd.getId());
        Collections.sort(ls);
        dd.setSons(ls);
        if (ls.isEmpty()) {
            return;
        } else {
            for (ExamModuleModel d : ls) {
                TreeNode t = new DefaultTreeNode(d, node);
                test(d, t);
            }
        }
    }
    
    public void test2(List<ExamModuleModel> ms, TreeNode root) {
        List<TreeNode> ls = root.getChildren();
        if (ls.isEmpty()) {
            return;
        } else {
            for (TreeNode d : ls) {
                ExamModuleModel em = (ExamModuleModel) d.getData();
                if (em.isSelected()) {
                    ms.add(em);
                }
                test2(ms, d);
            }
        }
    }
    
    
    public String addOrEditOk() {
        this.checkDepartment2();
        if (!flag) {
            dic.setAncestors(complex.genAncestors(dic.getId()));
            this.dicDAO.addExamModuleModel(dic);
            logger.log("添加了试题模块：" + this.dic.getName());
        } else {
            this.dicDAO.updateExamModuleModel(dic);
            logger.log("修改了试题模块：" + this.dic.getName());
        }
        
        return null;
    }
    
    
    
    //以下三方法用于提交考试前获取考试开放的部门
    public List<DictionaryModel> fetchDictionaryModels() {
        this.selectedNodes2.clear();
        this.testDictionaryModel2(this.selectedNodes2, root3);
        return this.selectedNodes2;
    }

    public void testDictionaryModel2(List<DictionaryModel> ms, TreeNode root3) {
        List<TreeNode> ls = root3.getChildren();
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
        this.dic.setGroupStr(sbb);
    }
//获取结束
    
}
