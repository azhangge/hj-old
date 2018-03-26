package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import org.primefaces.model.menu.Submenu;

import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.service.impl.ExamModule2Service;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@RequestScoped
public class MenuTree implements Serializable {

    ExamModule2Service dicDAO = SpringHelper.getSpringBean("ExamModule2Service");
    ExamModuleModel dic = new ExamModuleModel();
    boolean flag = false;
    List<SelectItem> ss = new ArrayList();
    TreeNode root;
    MenuModel root2;
    AdminSessionBean asb = JsfHelper.getBean("adminSessionBean");
    String businessId;

    public ExamModuleModel getDic() {
        return dic;
    }

    public void setDic(ExamModuleModel dic) {
        this.dic = dic;
    }

    public MenuModel getRoot2() {
        return root2;
    }

    public void setRoot2(MenuModel root2) {
        this.root2 = root2;
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
    	this.businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        this.loadStructure();
        this.loadStructure2();
    }

    public void loadStructure() {
        this.root = new DefaultTreeNode();
        ExamModuleModel l = dicDAO.findExamModuleModel(this.businessId);
        test(l, root);
    }

    public void test(ExamModuleModel dd, TreeNode node) {
        node.setExpanded(true);
        //System.out.println("test begain.");
        List<ExamModuleModel> ls = dicDAO.findAllSonExamModuleModel(dd.getId());
        Collections.sort(ls);
        dd.setSons(ls);
        if (ls.isEmpty()) {
            return;
        } else {
            for (ExamModuleModel d : ls) {
                if (this.asb != null) {//如果asb不为空，说明不是开发者版
                    
                    if (testIfShow(d)) {
                        TreeNode t = new DefaultTreeNode(d, node);
                        test(d, t);
                    }
                }
            }
        }
    }

    /**
     * 测试本模块是否应该向本管理员显示
     * @param d
     * @return 
     */
    public static boolean testIfShow(ExamModuleModel d) {
        AdminSessionBean asb = JsfHelper.getBean("adminSessionBean");
        boolean con = false;
        List<ExamModuleModel> aems = null;
        AdminInfo ai = asb.getAdmin();
        if (ai != null) {
            aems = ai.getModules();
            if (aems != null) {
                int s = aems.size();
            }
        }
        ExamModule2Service moduleCacheService = SpringHelper.getSpringBean("ExamModule2Service");
        List<ExamModuleModel> ems = moduleCacheService.loadAllDescendants(d.getId());
        if (ems != null) {
            ems.add(d);
        }
        for (ExamModuleModel em : ems) {
            //System.out.println(em);
            if (aems != null) {
                if (aems.contains(em)) {
                    con = true;
                    break;
                }
            }
        }
        return con;
    }

    public void loadStructure2() {
        this.root2 = new DefaultMenuModel();
        List<ExamModuleModel> ls = dicDAO.findAllSonExamModuleModel(this.businessId);
        for (ExamModuleModel l : ls) {
            test2(l, null, root2);
        }
    }

    public void test2(ExamModuleModel dd, Submenu mm, MenuModel root2) {
        //System.out.println("test begain.");
        List<ExamModuleModel> ls = dicDAO.findAllSonExamModuleModel(dd.getId());
        Collections.sort(ls);
        dd.setSons(ls);
        if (ls.isEmpty()) {
            DefaultMenuItem item = new DefaultMenuItem();
            item.setValue(dd.getName());
            item.setStyleClass("menu_item");
            item.setIcon("ui-icon-extlink");
            item.setUrl("ListExamKnowledge.jspx?module_id=" + dd.getId());
            if (mm == null) {
                root2.addElement(item);
            } else {
                mm.getElements().add(item);
            }
            return;
        } else {
            DefaultSubMenu sub = new DefaultSubMenu();
            sub.setLabel(dd.getName());
            sub.setStyleClass("menu_sub");
            sub.setIcon("ui-icon-extlink");
            if (mm == null) {
                root2.addElement(sub);
            } else {
                mm.getElements().add(sub);
            }
            for (ExamModuleModel d : ls) {
                test2(d, sub, root2);
            }
        }
    }
}
