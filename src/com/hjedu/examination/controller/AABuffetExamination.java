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

import com.hjedu.examination.dao.IBuffetExaminationDAO;
import com.hjedu.examination.dao.IBuffetExaminationPartDAO;
import com.hjedu.examination.dao.IExamLabelDAO;
import com.hjedu.examination.dao.IExamLabelTypeDAO;
import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.dao.IModuleBuffetPartDAO;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.entity.ExamLabel;
import com.hjedu.examination.entity.ExamLabelType;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.buffet.BuffetExamination;
import com.hjedu.examination.entity.buffet.BuffetExaminationPart;
import com.hjedu.examination.entity.buffet.ModuleBuffetPart;
import com.hjedu.examination.service.impl.ExamDepartmentService;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AABuffetExamination implements Serializable {

    BuffetExamination exam;
    IBuffetExaminationDAO manualDAO = SpringHelper.getSpringBean("BuffetExaminationDAO");
    IBuffetExaminationPartDAO partDAO = SpringHelper.getSpringBean("BuffetExaminationPartDAO");
    IExamModule2DAO module2DAO = SpringHelper.getSpringBean("ExamModule2DAO");
    IModuleBuffetPartDAO mpartDAO = SpringHelper.getSpringBean("ModuleBuffetPartDAO");
    ILogService logger = SpringHelper.getSpringBean("LogService");
    private boolean flag = false;

    //主要用于设置开放的部门
    TreeNode root = new DefaultTreeNode();
    ExamDepartmentService dicDAO = SpringHelper.getSpringBean("ExamDepartmentService");
    List<DictionaryModel> dics = new ArrayList();
    private TreeNode[] selectedNodes;
    List<DictionaryModel> selectedNodes2 = new LinkedList();
    private List<TreeNode> nodes = new LinkedList();

    BuffetExaminationPart newPart = new BuffetExaminationPart();

    TreeNode root2 = new DefaultTreeNode();
    List<ExamModuleModel> modules = new ArrayList();
    //TreeNode root = new DefaultTreeNode();
    List<ModuleBuffetPart> mparts = new ArrayList();//存放PART和MODULE的对应关系，其中包含了每个模块各种题型所应的大题及试题数量

    IExamLabelDAO examLabelDAO = SpringHelper.getSpringBean("ExamLabelDAO");
    IExamLabelTypeDAO examLabelTypeDAO = SpringHelper.getSpringBean("ExamLabelTypeDAO");
    List<ExamLabel> labels;
    List<ExamLabelType> labelTypes;

    String bussinessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    
    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public BuffetExamination getExam() {
        return exam;
    }

    public void setExam(BuffetExamination exam) {
        this.exam = exam;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
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

    public List<DictionaryModel> getSelectedNodes2() {
        return selectedNodes2;
    }

    public void setSelectedNodes2(List<DictionaryModel> selectedNodes2) {
        this.selectedNodes2 = selectedNodes2;
    }

    public List<ExamModuleModel> getModules() {
        return modules;
    }

    public void setModules(List<ExamModuleModel> modules) {
        this.modules = modules;
    }

    public BuffetExaminationPart getNewPart() {
        return newPart;
    }

    public void setNewPart(BuffetExaminationPart newPart) {
        this.newPart = newPart;
    }

    public List<ExamLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<ExamLabel> labels) {
        this.labels = labels;
    }

    public List<ExamLabelType> getLabelTypes() {
        return labelTypes;
    }

    public void setLabelTypes(List<ExamLabelType> labelTypes) {
        this.labelTypes = labelTypes;
    }

    public TreeNode getRoot2() {
        return root2;
    }

    public void setRoot2(TreeNode root2) {
        this.root2 = root2;
    }

    public List<ModuleBuffetPart> getMparts() {
        return mparts;
    }

    public void setMparts(List<ModuleBuffetPart> mparts) {
        this.mparts = mparts;
    }

    @PostConstruct
    public void init() {
        String idt = JsfHelper.getRequest().getParameter("id");
        if (idt != null) {
            this.flag = true;
            this.exam = this.manualDAO.findExamination(idt);
        } else {
            //至少保证试卷中有一个PART
            exam = new BuffetExamination();
            List<BuffetExaminationPart> parts = new ArrayList();
            BuffetExaminationPart tempPart = new BuffetExaminationPart();
            tempPart.setName("第一部分");
            tempPart.setExam(exam);
            parts.add(tempPart);
            exam.setParts(parts);
            //将新部分的关联试卷设为本试卷

        }
        newPart.setExam(exam);
        //newPart.setItems(new ArrayList());
        this.loadStructure();
        this.loadStructure2();
        this.loadLabels();
    }

    public void loadLabels() {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        this.labelTypes = this.examLabelTypeDAO.findAllExamLabelTypeByBusinessId(businessId);
        String lstr = this.exam.getLabelStr();
        if (lstr != null) {
            for (ExamLabelType lt : labelTypes) {
                List<ExamLabel> ls = lt.getBuffetLabels();
                for (ExamLabel e : ls) {
                    if (lstr.contains(e.getId())) {
                        e.setSelected(true);
                    }
                }
            }
        }
    }

    public void resetLabels() {
        StringBuilder sb = new StringBuilder();
        if (this.labelTypes != null) {
            for (ExamLabelType lt : labelTypes) {
                List<ExamLabel> ls = lt.getBuffetLabels();
                for (ExamLabel ex : ls) {
                    if (ex.isSelected()) {
                        sb.append(ex.getId());
                        sb.append(";");
                    }
                }
            }
        }
        this.exam.setLabelStr(sb.toString());
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

    private void loadStructure() {
        this.root = new DefaultTreeNode();
        DictionaryModel dm = dicDAO.findDictionaryModel(this.bussinessId,bussinessId);
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
        DictionaryModel emm = this.dicDAO.findDictionaryModel(id,bussinessId);
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

    public String addNewPart() {
        exam.getParts().add(newPart);
        newPart = new BuffetExaminationPart();
        newPart.setExam(exam);
        //newPart.setItems(new ArrayList());
        return null;
    }

    public String deletePart(String id) {
        BuffetExaminationPart pp = null;
        for (BuffetExaminationPart p : exam.getParts()) {
            if (p.getId().equals(id)) {
                pp = p;
                break;
            }
        }
        if (pp != null) {
            this.exam.getParts().remove(pp);
            this.partDAO.deleteBuffetExaminationPart(id);
        }
        return null;
    }

    private void loadStructure2() {
        this.root2 = new DefaultTreeNode();
        ExamModuleModel dm = module2DAO.findExamModuleModel(this.bussinessId);
        test2(dm, root2);
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
        List<ExamModuleModel> ls = module2DAO.findAllSonExamModuleModel(dd.getId(), CookieUtils.getBusinessId(JsfHelper.getRequest()));
        Collections.sort(ls);
        dd.setSons(ls);
        if (ls.isEmpty()) {
            node.setSelectable(true);
            return;
        } else {
            node.setSelectable(false);
            for (ExamModuleModel d : ls) {
                ModuleBuffetPart mp = this.buildAModuleBuffetPart(d);
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
    private ModuleBuffetPart buildAModuleBuffetPart(ExamModuleModel d) {
        if (!flag) {
            ModuleBuffetPart mp = new ModuleBuffetPart();
            mp.setModule(d);
            mp.setExam(exam);
            this.mparts.add(mp);
            return mp;
        } else {
            ModuleBuffetPart mp = mpartDAO.findModuleBuffetPartByExamAndModule(this.exam.getId(), d.getId());
            if (mp == null) {
                mp = new ModuleBuffetPart();
                mp.setModule(d);
                mp.setExam(exam);
            }
            //mp.setModule(d);
            this.mparts.add(mp);
            return mp;
        }
    }

    private void resetMParts() {
        List<BuffetExaminationPart> parts = exam.getParts();
        for (BuffetExaminationPart p : parts) {
            p.setItemNum(0);
            p.setTotalScore(0);
            //System.out.println(p.getPaper());
        }
        //以下代码彻底杜绝items里边有重复试题的可能
        //System.out.println("待保存题目总数："+items.size());

        if (!flag) {
            //mpart与exam关联，在保存mpart前应该先保存exam
            this.manualDAO.addExamination(exam);
        }
        for (ModuleBuffetPart p : mparts) {
            //逐一处理可能的空指针异常情况
            if (p.getChoicePartId() == null) {
                p.setChoicePartId("0");
            }
            if (p.getMchoicePartId() == null) {
                p.setMchoicePartId("0");
            }
            if (p.getFillPartId() == null) {
                p.setFillPartId("0");
            }
            if (p.getJudgePartId() == null) {
                p.setJudgePartId("0");
            }
            if (p.getEssayPartId() == null) {
                p.setEssayPartId("0");
            }
            if (p.getFilePartId() == null) {
                p.setFilePartId("0");
            }
            if (p.getCasePartId() == null) {
                p.setCasePartId("0");
            }
            if (flag) {
                //this.manualDAO.updateExamination(exam);
                this.mpartDAO.updateModuleBuffetPart(p);
            } else {
                try {
                    this.mpartDAO.addModuleBuffetPart(p);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
            //System.out.println(p.getPaper());
        }
        for (BuffetExaminationPart p : parts) {

            //System.out.println("大题"+p.getName()+"中小题数："+p.getItemNum());
        }
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
        List departments = new ArrayList<>();
        departments.addAll(set);
        this.exam.setDepartments(departments);
    }
//获取结束

    public String done() {
        this.resetLabels();
        this.resetMParts();
        this.checkDepartment2();
        List list = new ArrayList<>();
        list.add(ExternalUserUtil.getAdminBySession());
        this.exam.setAdmins(list);
        //this.paper.setModulePapers(mes);
        if (flag) {
            this.manualDAO.updateExamination(exam);
            this.logger.log("修改了自主练习：" + exam.getName());
        } else {
            this.logger.log("添加了自主练习：" + exam.getName());
            exam.setBusinessId(CookieUtils.getBusinessId(JsfHelper.getRequest()));
            this.manualDAO.updateExamination(exam);//因为在resetParts中已经将examination保存了，此处只更新
        }
        return "ListBuffetExamination?faces-redirect=true";
    }
}
