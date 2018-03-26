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
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.hjedu.examination.dao.ICaseQuestionDAO;
import com.hjedu.examination.dao.IDictionaryDAO;
import com.hjedu.examination.dao.IModuleExamPartDAO;
import com.hjedu.examination.dao.IModuleExaminationDAO;
import com.hjedu.examination.dao.IModuleModule2PartDAO;
import com.hjedu.examination.entity.CaseQuestion;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.module2.ModuleExam2Part;
import com.hjedu.examination.entity.module2.ModuleExamination2;
import com.hjedu.examination.entity.module2.ModuleModule2Part;
import com.hjedu.examination.service.impl.ExamDepartmentService;
import com.hjedu.examination.service.impl.ExamModule2Service;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAModuleExam22 implements Serializable {
    
    ModuleExamination2 exam;
    IModuleExaminationDAO manualDAO = SpringHelper.getSpringBean("ModuleExaminationDAO");
    IModuleExamPartDAO partDAO = SpringHelper.getSpringBean("ModuleExamPartDAO");
    ExamModule2Service module2DAO = SpringHelper.getSpringBean("ExamModule2Service");
    IModuleModule2PartDAO mpartDAO = SpringHelper.getSpringBean("ModuleModule2PartDAO");
    ICaseQuestionDAO caseqDAO = SpringHelper.getSpringBean("CaseQuestionDAO");
    ILogService logger = SpringHelper.getSpringBean("LogService");
    private boolean flag = false;
    
    ModuleExam2Part newPart = new ModuleExam2Part();
    
    TreeNode root2 = new DefaultTreeNode();
    List<ExamModuleModel> modules = new ArrayList();
    //TreeNode root = new DefaultTreeNode();
    List<ModuleModule2Part> mparts = new ArrayList();//存放PART和MODULE的对应关系，其中包含了每个模块各种题型所应的大题及试题数量

    //主要用于设置开放的部门
    TreeNode root3 = new DefaultTreeNode();
    ExamDepartmentService dicDAO3 = SpringHelper.getSpringBean("ExamDepartmentService");
//    IDictionaryDAO dicDAO3 = SpringHelper.getSpringBean("DictionaryDAO");
    List<DictionaryModel> dics3 = new ArrayList();
    private TreeNode[] selectedNodes;
    List<DictionaryModel> selectedNodes2 = new LinkedList();
    private List<TreeNode> nodes = new LinkedList();
    String bussinessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    
    public boolean isFlag() {
        return flag;
    }
    
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    
    public List<ExamModuleModel> getModules() {
        return modules;
    }
    
    public void setModules(List<ExamModuleModel> modules) {
        this.modules = modules;
    }
    
    public TreeNode getRoot2() {
        return root2;
    }
    
    public void setRoot2(TreeNode root2) {
        this.root2 = root2;
    }
    
    public ModuleExamination2 getExam() {
        return exam;
    }
    
    public void setExam(ModuleExamination2 exam) {
        this.exam = exam;
    }
    
    public ModuleExam2Part getNewPart() {
        return newPart;
    }
    
    public void setNewPart(ModuleExam2Part newPart) {
        this.newPart = newPart;
    }
    
    public List<ModuleModule2Part> getMparts() {
        return mparts;
    }
    
    public void setMparts(List<ModuleModule2Part> mparts) {
        this.mparts = mparts;
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
    
    @PostConstruct
    public void init() {
        String idm = JsfHelper.getRequest().getParameter("module_id");
        //String idt = JsfHelper.getRequest().getParameter("id");
        ExamModuleModel em = this.module2DAO.findExamModuleModel(idm);
        ModuleExamination2 ett = em.getExam();
        if (ett != null) {
            this.flag = true;
            this.exam = ett;
        } else {
            //至少保证试卷中有一个PART
            exam = new ModuleExamination2();
            
            exam.setModule(em);
            //
            List<ModuleExam2Part> parts = new ArrayList();
            ModuleExam2Part tempPart = new ModuleExam2Part();
            tempPart.setName("第一部分");
            tempPart.setExam(exam);
            parts.add(tempPart);
            exam.setParts(parts);
            //将新部分的关联试卷设为本试卷

        }
        newPart.setExam(exam);
        //newPart.setItems(new ArrayList());
        this.loadStructure2(idm);
        this.loadStructure3();
    }
    
    public String addNewPart() {
        exam.getParts().add(newPart);
        newPart = new ModuleExam2Part();
        newPart.setExam(exam);
        //newPart.setItems(new ArrayList());
        return null;
    }
    
    public String deletePart(String id) {
        ModuleExam2Part pp = null;
        for (ModuleExam2Part p : exam.getParts()) {
            if (p.getId().equals(id)) {
                pp = p;
                break;
            }
        }
        if (pp != null) {
            this.exam.getParts().remove(pp);
            this.partDAO.deleteModuleExamPart(id);
        }
        return null;
    }
    
    private void loadStructure2(String mid) {
        
        ExamModuleModel dm = module2DAO.findExamModuleModel(mid);
        this.root2 = new DefaultTreeNode();
        
        ModuleModule2Part mp = this.buildAModuleModule2Part(dm);
        TreeNode t = new DefaultTreeNode(mp, root2);
        
        test2(dm, t);
        this.modules = null;
        this.modules = dm.getSons();
        
    }
    
    public void test2(ExamModuleModel dd, TreeNode node) {
        if (!flag) {
            //如果是新增考试，则新建ModuleExamination并绑定exam

        } else {
            //如果是修改考试，则在已经存在的ModuleExamination绑定exam

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
                ModuleModule2Part mp = this.buildAModuleModule2Part(d);
                TreeNode t = new DefaultTreeNode(mp, node);
                test2(d, t);
            }
        }
    }

    /**
     *
     * @param d 传入一个试题模块对象
     * @return 获得一个试题模块和分段的连接中间对象
     */
    private ModuleModule2Part buildAModuleModule2Part(ExamModuleModel d) {
        if (!flag) {
            ModuleModule2Part mp = new ModuleModule2Part();
            mp.setModule(d);
            mp.setExam(exam);
            this.mparts.add(mp);
            return mp;
        } else {
            ModuleModule2Part mp = mpartDAO.findModuleModule2PartByExamAndModule(this.exam.getId(), d.getId());
            //mp.setModule(d);
            if (mp == null) {
                mp = new ModuleModule2Part();
                mp.setModule(d);
                mp.setExam(exam);
            }
            this.mparts.add(mp);
            return mp;
        }
    }
    
    private int computePartItemNum(String pid, List<ModuleModule2Part> mparts) {
        int num = 0;
        for (ModuleModule2Part p : mparts) {
            if (pid.equals(p.getChoicePartId())) {
                num += p.getModule().getChoiceNum();
            }
            if (pid.equals(p.getMchoicePartId())) {
                num += p.getModule().getMultiChoiceNum();
            }
            if (pid.equals(p.getFillPartId())) {
                num += p.getModule().getFillNum();
            }
            if (pid.equals(p.getJudgePartId())) {
                num += p.getModule().getJudgeNum();
            }
            if (pid.equals(p.getEssayPartId())) {
                num += p.getModule().getEssayNum();
            }
            if (pid.equals(p.getFilePartId())) {
                num += p.getModule().getFileNum();
            }
            if (pid.equals(p.getCasePartId())) {
                List<CaseQuestion> ques = this.caseqDAO.findCaseQuestionByModule(p.getModule().getId());
                if (ques != null) {
                    for (CaseQuestion cq : ques) {
                        num += cq.getTotalItemNum();
                    }
                }
                
            }
        }
        return num;
    }
    
    private void resetMParts() {
        List<ModuleExam2Part> parts = exam.getParts();
        int total = 0;
        for (ModuleExam2Part p : parts) {
            int num = computePartItemNum(p.getId(), mparts);
            p.setItemNum(num);
            total += num;
            //p.setTotalScore(num*p.getScore());
            //System.out.println(p.getPaper());
        }
        //以下代码彻底杜绝items里边有重复试题的可能
        //System.out.println("待保存题目总数："+items.size());
        exam.setTotalNum(total);
        if (!flag) {
            //mpart与exam关联，在保存mpart前应该先保存exam
            this.manualDAO.addExamination(exam);
        }
        for (ModuleModule2Part p : mparts) {
            if (flag) {
                //this.manualDAO.updateExamination(exam);
                this.mpartDAO.updateModuleModule2Part(p);
            } else {
                try {
                    
                    this.mpartDAO.addModuleModule2Part(p);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
            //System.out.println(p.getPaper());
        }
        for (ModuleExam2Part p : parts) {

            //System.out.println("大题"+p.getName()+"中小题数："+p.getItemNum());
        }
    }
    
    private boolean testIfSelected(DictionaryModel dd) {
        if (this.exam.getGroupStr() != null) {
            if (this.exam.getGroupStr().trim().contains(String.valueOf(dd.getId()))) {
                dd.setSelected(true);
                return true;
            } else {
                dd.setSelected(false);
                return false;
            }
        }
        return false;
    }

    //加载部门
    private void loadStructure3() {
        this.root3 = new DefaultTreeNode();
        DictionaryModel dm = dicDAO3.findDictionaryModel(this.bussinessId,bussinessId);
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
        DictionaryModel emm = this.dicDAO3.findDictionaryModel(id,bussinessId);
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

    //以下三方法用于提交考试前获取考试开放的部门
    public List<DictionaryModel> fetchDictionaryModels() {
        this.selectedNodes2.clear();
        this.testDictionaryModel2(this.selectedNodes2, root3);
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
            sb.append(";");
        }
        String sbb = sb.toString().trim();
        if ("".equals(sbb)) {
            String root123 = this.bussinessId;
            sbb = root123;
        }
        this.exam.setGroupStr(sbb);
        List departments = new ArrayList<>();
        departments.addAll(set);
        this.exam.setDepartments(departments);
    }
//获取结束

    public String done() {
        this.resetMParts();
        //this.paper.setModulePapers(mes);
        this.checkDepartment2();
        if (flag) {
            this.manualDAO.updateExamination(exam);
            this.logger.log("修改了随机试卷2：" + exam.getName());
        } else {
            this.logger.log("添加了随机试卷2：" + exam.getName());
            this.manualDAO.updateExamination(exam);//因为在resetParts中已经将examination保存了，此处只更新
        }
        ExamModule2Service ems = SpringHelper.getSpringBean("ExamModule2Service");
        ems.reBuildCache();
        return "ModuleExamMag?faces-redirect=true";
    }
}
