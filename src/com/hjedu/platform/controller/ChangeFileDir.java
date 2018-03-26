package com.hjedu.platform.controller;


import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.hjedu.customer.dao.IBbsFileDAO;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.dao.ComplexFileLogic;
import com.hjedu.platform.entity.BbsFileModel;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ChangeFileDir  implements Serializable{

    IBbsUserDAO cuDAO = SpringHelper.getSpringBean("BbsUserDAO");
    IBbsFileDAO cfDAO = SpringHelper.getSpringBean("BbsFileDAO");
    ComplexFileLogic complex = SpringHelper.getSpringBean("ComplexFileLogic");
    @ManagedProperty(value = "#{clientSession}")
    ClientSession cs;
    TreeNode root = new DefaultTreeNode("root", null);
    List<BbsFileModel> files = null;
    TreeNode selectedNode;
    String errStr = "";

    public ClientSession getCs() {
        return cs;
    }

    public void setCs(ClientSession cs) {
        this.cs = cs;
    }

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
        //保证上传时存在一个用户
        ClientSession cs = JsfHelper.getBean("clientSession");
        BbsUser bu=cs.getUsr();
        if (bu == null) {
            IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
            bu=userDAO.findSysUser();
            cs.setUsr(bu);
        } 
        
        BbsFileModel root1 = new BbsFileModel();
        root1.setId("0");
        root1.setIfFolder(true);
        root1.setAncestors("-1;");
        root1.setFileName("根目录");
        TreeNode r = new DefaultTreeNode(root1, root);
        this.buildDirTree("0", r, cs.getUsr().getId());
    }

    public void change(List<BbsFileModel> files) {
        errStr = "";
        this.files = files;
    }

    private void buildDirTree(String id, TreeNode node, String uid) {
        List<BbsFileModel> ls = cfDAO.findAllSonDirsByUsr(id, uid);
        //MyLogger.echo("顶级文件夹数量：" + ls.size());
        if (ls.isEmpty()) {
            return;
        } else {
            for (BbsFileModel d : ls) {
                TreeNode tn = new DefaultTreeNode(d, node);
                buildDirTree(d.getId(), tn, uid);
            }
        }
    }

    public String doChange() {
        errStr = "";
        BbsFileModel cf = (BbsFileModel) this.selectedNode.getData();
        FacesMessage fm = new FacesMessage();

        if (cf != null) {
            for (BbsFileModel c : this.files) {
//                MyLogger.echo(cf);
//                MyLogger.echo(cf.getAncestors());
//                MyLogger.echo(c.getId());
                if (cf.getAncestors().indexOf(String.valueOf(c.getId())) != -1) {
                    errStr = "逻辑错误，文件夹无法移动向自身或子文件夹！";
                    fm.setSummary(errStr);
                    fm.setSeverity(FacesMessage.SEVERITY_ERROR);
                    FacesContext.getCurrentInstance().addMessage("", fm);
                    return null;
                }
            }
            String id = cf.getId();
            for (BbsFileModel c : this.files) {
                if (c.getId()==id) {
                    continue;
                } else {
                    c.setFatherID(id);
                    this.cfDAO.updateClientFileInfo(c);
                    c.setAncestors(complex.genAncestors(c.getId()));
                    this.cfDAO.updateClientFileInfo(c);
                }
            }
        }

        ClientListFile lcf = JsfHelper.getBean("clientListFile");
        if (lcf != null) {
            lcf.synDB();
        }
        ListAdminFile laf = JsfHelper.getBean("listAdminFile");
        if (laf != null) {
            laf.synDB();
        }
        
        return null;
    }
}
