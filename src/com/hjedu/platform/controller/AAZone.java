package com.hjedu.platform.controller;

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
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IDictionaryDAO;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.service.impl.ExamDepartmentService;
import com.hjedu.platform.dao.IBbsZoneDAO;
import com.hjedu.platform.entity.BbsZone;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAZone implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IBbsZoneDAO bbsZoneDAO = SpringHelper.getSpringBean("BbsZoneDAO");
    IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
    BbsZone zone;
    boolean flag = false;
    String str = "";
    List<BbsUser> newUsers = new ArrayList();

    TreeNode root = new DefaultTreeNode();
    ExamDepartmentService dicDAO = SpringHelper.getSpringBean("ExamDepartmentService");
    List<DictionaryModel> dics = new ArrayList();
    private TreeNode[] selectedNodes;
    List<DictionaryModel> selectedNodes2 = new LinkedList();
    private List<TreeNode> nodes = new LinkedList();
    String bussinessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    
    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public List<BbsUser> getNewUsers() {
        return newUsers;
    }

    public void setNewUsers(List<BbsUser> newUsers) {
        this.newUsers = newUsers;
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

    public List<TreeNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<TreeNode> nodes) {
        this.nodes = nodes;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public BbsZone getZone() {
        return zone;
    }

    public void setZone(BbsZone zone) {
        this.zone = zone;
    }

    @PostConstruct
    public void init() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String id = req.getParameter("id");
        if (id != null) {
            this.zone = this.bbsZoneDAO.findById(id);
            this.flag = true;
        } else {
            this.zone = new BbsZone();
        }
        if (this.zone.getManagers() == null) {
            this.zone.setManagers(new ArrayList());
        }
        this.loadStructure();
    }

    private boolean testIfSelected(DictionaryModel dd) {
        if (this.zone.getGroupStr() != null) {
            if (this.zone.getGroupStr().trim().contains(String.valueOf(dd.getId()))) {
                dd.setSelected(true);
                return true;
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
        this.zone.setGroupStr(sbb);
    }
//获取结束

    public void deleteMag(String id) {
        BbsUser u = null;
        List<BbsUser> us = this.zone.getManagers();
        for (BbsUser b : us) {
            if (id == b.getId()) {
                u = b;
                break;
            }
        }
        if (u != null) {
            us.remove(u);
        }
    }

    public void addMag(String id) {
        BbsUser u = null;
        List<BbsUser> us1 = this.zone.getManagers();
        for (BbsUser b : this.newUsers) {
            if (id == b.getId()) {
                u = b;
                break;
            }
        }
        if (u != null) {
            newUsers.remove(u);
            us1.add(u);
        }
    }

    public void searchUser() {
        this.newUsers = this.bbsUserDAO.findBbsUsersLikeUrn(str);
        List<BbsUser> us1 = this.zone.getManagers();
        for (BbsUser b : us1) {
            BbsUser u = null;
            for (BbsUser bu : this.newUsers) {
                if (bu.getId().equals(b.getId())) {
                    u = bu;
                    System.out.println(u.getUsername());
                    break;
                }
            }
            if (u != null) {
                newUsers.remove(u);
            }
        }
    }

    public String done() {
        FacesContext fc = FacesContext.getCurrentInstance();
        this.checkDepartment2();
        if (!flag) {
            this.bbsZoneDAO.save(zone);
            logger.log("添加了版面：" + this.zone.getName());
        } else {
            this.bbsZoneDAO.update(zone);
            logger.log("修改了版面：" + this.zone.getName());
        }
        return "ListZone?faces-redirect=true";
    }
}
