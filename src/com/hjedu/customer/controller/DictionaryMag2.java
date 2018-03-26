package com.hjedu.customer.controller;

import java.io.Serializable;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.dao.IDictionaryDAO;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.service.impl.ExamDepartmentService;
import com.hjedu.platform.dao.ComplexDepartmentLogic;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class DictionaryMag2 implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    ExamDepartmentService dicDAO = SpringHelper.getSpringBean("ExamDepartmentService");
    ComplexDepartmentLogic complex = SpringHelper.getSpringBean("ComplexDepartmentLogic");
    IDictionaryDAO dictionaryDAO = SpringHelper.getSpringBean("DictionaryDAO");
    List<DictionaryModel> dics = new ArrayList();
    DictionaryModel dic = new DictionaryModel();
    boolean flag = false;
    List<SelectItem> ss = new ArrayList();
    TreeNode root = new DefaultTreeNode();
    List<DictionaryModel> selectedDics = new LinkedList();
    String businessId;

    public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId; 
	}
    
    public DictionaryModel getDic() {
        return dic; 
    }

    public void setDic(DictionaryModel dic) {
        this.dic = dic;
    }

    public List<DictionaryModel> getDics() {
        return dics;
    }

    public void setDics(List<DictionaryModel> dics) {
        this.dics = dics;
    }

    public List<DictionaryModel> getSelectedDics() {
        return selectedDics;
    }

    public void setSelectedDics(List<DictionaryModel> selectedDics) {
        this.selectedDics = selectedDics;
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
    }

    public void loadStructure() {
        this.root = new DefaultTreeNode();
        DictionaryModel dm = dicDAO.findDictionaryModel(businessId,businessId);
        this.dics = null;
        
        if(dm != null) {
        	test(dm, root);
        	this.dics = dm.getSons();
        }   
    }
    
	public void test(DictionaryModel dd, TreeNode node) {
        node.setExpanded(true);
//        List<DictionaryModel> ls = dicDAO.findAllSonDictionaryModel(dd.getId());
        List<DictionaryModel> ls = dictionaryDAO.findDictionaryModelByFatherID(dd.getId());
        ls = ExternalUserUtil.filterByAdmin(ls);
        ls=dicDAO.cloneList(ls);
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

    public List<DictionaryModel> fetchDics() {
        this.selectedDics.clear();
        this.test2(this.selectedDics, root);
        return this.selectedDics;
    }
    
    public void test2(List<DictionaryModel> ms, TreeNode root) {
        List<TreeNode> ls = root.getChildren();
        if (ls.isEmpty()) {
            return;
        } else {
            for (TreeNode d : ls) {
                DictionaryModel em = (DictionaryModel) d.getData();
                if (em.isSelected()) {
                    ms.add(em);
                }
                test2(ms, d);
            }
        }
    }
    public String addDic() {
        this.flag = false;
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String fid = req.getParameter("fid");
        if (fid != null) {
            this.dic = new DictionaryModel();
            dic.setFatherId(fid);
            dic.setBusinessId(businessId);
        }
        return null;
    }

    public String addOrEditOk() {
        if (!flag) {
        	//还需要添加部门与管理员的关联关系
        	AdminSessionBean asb = JsfHelper.getBean("adminSessionBean");
        	if (asb != null) {
        		AdminInfo ai = asb.getAdmin();
        		ArrayList<AdminInfo> listAi = new ArrayList<AdminInfo>();
        		listAi.add(ai);
        		if (ai != null) {
        			dic.setAdmins(listAi);
        		}
        	}
            this.dicDAO.addDictionaryModel(dic);
            dic.setAncestors(complex.genAncestors(dic.getId()));
            logger.log("添加了部门：" + this.dic.getName());
        } else {
            logger.log("修改了部门：" + this.dic.getName());
        }
        this.dicDAO.updateDictionaryModel(dic);
        this.loadStructure();
        //Menu menu = JsfHelper.getBean("menu");
        //menu.loadStructure();
        this.refreshChangePanel();
        return null;
    }

    public String editDic() {
        this.flag = true;
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String id = req.getParameter("id");
        if (id != null) {
            this.dic = dicDAO.findDictionaryModel(id,businessId);
        }
        return null;
    }

    public String delDic(String id) {
        this.dic = dicDAO.findDictionaryModel(id,businessId);
        logger.log("删除了部门：" + this.dic.getName());
        dicDAO.deleteDictionaryModelAndAllDescendants(id,businessId);

        this.loadStructure();
        //Menu menu = JsfHelper.getBean("menu");
        //menu.loadStructure();
        this.refreshChangePanel();
        return null;
    }
    
    private void refreshChangePanel() {
        ChangeDictionaryDir change = JsfHelper.getBean("changeDictionaryDir");
        if (change != null) {
            change.init();
        }
    }
}
