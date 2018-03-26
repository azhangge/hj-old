package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.menu.MenuModel;

import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IModuleExamCaseDAO;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.module2.ModuleExamination2;
import com.hjedu.examination.service.impl.ExamModule2Service;
import com.hjedu.platform.controller.ClientSession;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ExamModule2List implements Serializable {
    
    ExamModule2Service dicDAO = SpringHelper.getSpringBean("ExamModule2Service");
    ExamModuleModel dic = new ExamModuleModel();
    boolean flag = false;
    List<SelectItem> ss = new ArrayList();
    TreeNode root;
    MenuModel root2;
    AdminSessionBean asb = JsfHelper.getBean("adminSessionBean");
    //String moduleStr = "";//当前用户所属部门关联的试题模块

    String moduleId1 = "";
    String moduleId2 = "";
    String moduleId3 = "";
    String moduleId4 = "";
    List<ExamModuleModel> modules1 = new ArrayList();
    List<ExamModuleModel> modules2 = new ArrayList();
    List<ExamModuleModel> modules3 = new ArrayList();
    List<ExamModuleModel> modules4 = new ArrayList();
    
    boolean ifOnlyOne = false;
    ExamModuleModel theOne = null;
    String businessId;
    
    public ExamModuleModel getDic() {
        return dic;
    }
    
    public boolean isIfOnlyOne() {
        return ifOnlyOne;
    }
    
    public void setIfOnlyOne(boolean ifOnlyOne) {
        this.ifOnlyOne = ifOnlyOne;
    }
    
    public ExamModuleModel getTheOne() {
        return theOne;
    }
    
    public void setTheOne(ExamModuleModel theOne) {
        this.theOne = theOne;
    }
    
    public String getModuleId1() {
        return moduleId1;
    }
    
    public void setModuleId1(String moduleId1) {
        this.moduleId1 = moduleId1;
    }
    
    public String getModuleId2() {
        return moduleId2;
    }
    
    public void setModuleId2(String moduleId2) {
        this.moduleId2 = moduleId2;
    }
    
    public String getModuleId3() {
        return moduleId3;
    }
    
    public void setModuleId3(String moduleId3) {
        this.moduleId3 = moduleId3;
    }
    
    public String getModuleId4() {
        return moduleId4;
    }
    
    public void setModuleId4(String moduleId4) {
        this.moduleId4 = moduleId4;
    }
    
    public List<ExamModuleModel> getModules1() {
        return modules1;
    }
    
    public void setModules1(List<ExamModuleModel> modules1) {
        this.modules1 = modules1;
    }
    
    public List<ExamModuleModel> getModules2() {
        return modules2;
    }
    
    public void setModules2(List<ExamModuleModel> modules2) {
        this.modules2 = modules2;
    }
    
    public List<ExamModuleModel> getModules3() {
        return modules3;
    }
    
    public void setModules3(List<ExamModuleModel> modules3) {
        this.modules3 = modules3;
    }
    
    public List<ExamModuleModel> getModules4() {
        return modules4;
    }
    
    public void setModules4(List<ExamModuleModel> modules4) {
        this.modules4 = modules4;
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
        //以下代码为适配用户从外部环境中加载试题模块
        String uid = JsfHelper.getRequest().getParameter("uid");
        if (uid != null) {
            ClientSession cs = JsfHelper.getBean("clientSession");
            if (cs == null) {
                cs = new ClientSession();
                JsfHelper.getRequest().getSession().setAttribute("clientSession", cs);
            }
            IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
            BbsUser bu = userDAO.findBbsUser(uid);
            if (bu != null) {
                cs.setUsr(bu);
            }
        }
        //外部加载适配结束
//        this.modules1 = loadModules(this.dicDAO.findAllSonExamModuleModel(this.businessId));
        this.modules1 = this.dicDAO.findAllSonExamModuleModel(this.businessId);
        this.loadStructure(null);
    }
    
    
    public String changeModule(int deep) {
        if (deep == 1) {
            if (this.businessId.equals(moduleId1)) {
                this.modules1 = loadModules(this.dicDAO.findAllSonExamModuleModel(this.businessId));
                modules2 = new ArrayList();
                modules3 = new ArrayList();
                modules4 = new ArrayList();
                this.loadStructure(null);
            } else {
                this.modules2 = loadModules(this.dicDAO.findAllSonExamModuleModel(this.moduleId1));
                this.loadStructure(this.moduleId1);
                modules3 = new ArrayList();
                modules4 = new ArrayList();
            }
        } else if (deep == 2) {
            if ("10".equals(moduleId2)) {
                //this.modules1 = loadModules(this.dicDAO.findAllSonExamModuleModel(moduleId2));
                modules3 = new ArrayList();
                modules4 = new ArrayList();
                this.loadStructure(moduleId1);
            } else {
                this.modules3 = loadModules(this.dicDAO.findAllSonExamModuleModel(this.moduleId2));
                this.loadStructure(this.moduleId2);
                modules4 = new ArrayList();
            }
        } else if (deep == 3) {
            //this.modules4 = this.dicDAO.findAllSonExamModuleModel(this.moduleId4);
            if ("100".equals(moduleId3)) {
                //this.modules1 = loadModules(this.dicDAO.findAllSonExamModuleModel(moduleId2));
                this.loadStructure(moduleId2);
            } else {
                this.loadStructure(this.moduleId3);
            }
        }
        this.checkTheOne();
        return null;
    }

    /**
     * 此方法用于检测筛选后是否只剩余唯一的章节练习。此方法用于触屏版选择唯一章节练习
     */
    private void checkTheOne() {
        try {
            this.ifOnlyOne = false;
            theOne = null;
            int num = root.getChildCount();
            if (num == 1) {
                int num2 = root.getChildren().get(0).getChildCount();
                if (num2 == 0) {
                    this.ifOnlyOne = true;
                    theOne = (ExamModuleModel) root.getChildren().get(0).getData();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    private List<ExamModuleModel> loadModules(List<ExamModuleModel> mms) {
        List<ExamModuleModel> modules = new ArrayList();
        ClientSession cs = JsfHelper.getBean("clientSession");
        BbsUser ai = cs.getUsr();
        if (mms != null) {
            for (ExamModuleModel m : mms) {
                ModuleExamination2 me = m.getExam();
                //System.out.println(me);
                if (me != null) {
                    String aems = me.getGroupStr();
                    if (aems != null && ai != null) {
                        if (ai.testIfIn(aems)) {
                            modules.add(m);
                        }
                    }
                }
            }
        }
        return modules;
    }
    
    public void loadStructure(String id) {
        if (id == null || this.businessId.equals(id)) {
            id = this.businessId;
            ExamModuleModel l = dicDAO.findExamModuleModel(id);
            this.root = new DefaultTreeNode(l, null);
            test(l, root);
        } else {
            ExamModuleModel l = dicDAO.findExamModuleModel(id);
            this.root = new DefaultTreeNode(l, null);
            test(l, new DefaultTreeNode(l, root));
        }
        
    }
    
    public void test(ExamModuleModel dd, TreeNode node) {
        if (dd == null) {
            return;
        }
        node.setExpanded(true);
        //System.out.println("test begain.");
        List<ExamModuleModel> ls = dicDAO.findAllSonExamModuleModel(dd.getId());
        Collections.sort(ls);
        dd.setSons(ls);
        if (ls.isEmpty()) {
            return;
        } else {
            for (ExamModuleModel d : ls) {
                if (this.testIfShow(d)) {
                    TreeNode t = new DefaultTreeNode(d, node);
                    test(d, t);
                }
            }
        }
    }
    
    private boolean testIfShow(ExamModuleModel d) {
        boolean con = true;
        //List<ExamModuleModel> aems = null;
        ClientSession cs = JsfHelper.getBean("clientSession");
        if (cs != null) {
            BbsUser ai = cs.getUsr();
            if (ai != null) {
                //System.out.println("user group str:"+gstr);
                ModuleExamination2 me = d.getExam();
                //System.out.println(me);
                if (me == null) {
                    return false;
                }
                if (!me.isIfOpen()) {
                    return false;
                }
//                String aems = me.getGroupStr();
//                if (aems != null) {
//                    if (ai.testIfIn(aems)) {
//                        con = true;
//                    }
//                }
            }
        }
        return con;
    }
    
    public String delCases(String mid) {
        IModuleExamCaseDAO caseDAO = SpringHelper.getSpringBean("ModuleExamCaseDAO");
        caseDAO.deleteModuleExamCase(mid);
        JsfHelper.info("本章节的练习记录已经清除！");
        return null;
    }
    
}
