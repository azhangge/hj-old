package com.hjedu.customer.controller;


import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.component.menu.Menu;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.hjedu.examination.dao.IDictionaryDAO;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.service.impl.ExamDepartmentService;
import com.hjedu.platform.dao.ComplexDepartmentLogic;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ChangeDictionaryDir implements Serializable {

    ExamDepartmentService cfDAO = SpringHelper.getSpringBean("ExamDepartmentService");
    ComplexDepartmentLogic complex = SpringHelper.getSpringBean("ComplexDepartmentLogic");
    TreeNode root ;
    List<DictionaryModel> modules = null;
    TreeNode selectedNode;
    String errStr = "";
    
    String businessId;

    public String getErrStr() {
        return errStr;
    }

    public void setErrStr(String errStr) {
        this.errStr = errStr;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    @PostConstruct
    public void init() {
        root = new DefaultTreeNode("root", null);
        DictionaryModel root1 = new DictionaryModel();
        this.businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        root1.setId(this.businessId);

        root1.setAncestors("-1;");
        root1.setName("根部门");
        TreeNode r = new DefaultTreeNode(root1, root);
        this.buildDirTree(this.businessId, r);
    }

    public void change(List<DictionaryModel> files) {
        errStr = "";
        this.modules = files;
    }

    private void buildDirTree(String id, TreeNode node) {
        List<DictionaryModel> ls = cfDAO.findAllSonDictionaryModel(id);
        //MyLogger.echo("顶级文件夹数量：" + ls.size());
        if (ls.isEmpty()) {
            return;
        } else {
            for (DictionaryModel d : ls) {
                TreeNode tn = new DefaultTreeNode(d, node);
                buildDirTree(d.getId(), tn);
            }
        }
    }

    public String doChange() {
        errStr = "";
        DictionaryModel cf = (DictionaryModel) this.selectedNode.getData();
        FacesMessage fm = new FacesMessage();

        DictionaryMag2 lcf = JsfHelper.getBean("dictionaryMag2");
        List<DictionaryModel> emms = lcf.fetchDics();
        if ((cf != null) && (emms != null)) {
            for (DictionaryModel c : emms) {
                if (cf.getAncestors().contains(c.getId())) {
                    errStr = "逻辑错误，试题模块无法移动向自身或子模块！";
                    JsfHelper.error(errStr);
                    return null;
                }
                String id = cf.getId();
                c.setFatherId(id);
                this.cfDAO.updateDictionaryModel(c);
                c.setAncestors(complex.genAncestors(c.getId()));
                this.cfDAO.updateDictionaryModel(c);

            }
            this.init();//更新本页显示的目录结构
            DictionaryMag2 lcf2 = JsfHelper.getBean("dictionaryMag2");
            if (lcf2 != null) {
                lcf2.loadStructure();
            }
        }
        return null;
    }
}
