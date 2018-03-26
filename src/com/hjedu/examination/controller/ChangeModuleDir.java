package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.service.ComplexExamModuleLogic;
import com.hjedu.examination.service.impl.ExamModule2Service;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ChangeModuleDir implements Serializable {

    ExamModule2Service cfDAO = SpringHelper.getSpringBean("ExamModule2Service");
    ComplexExamModuleLogic complex = SpringHelper.getSpringBean("ComplexExamModuleLogic");
    TreeNode root;
    List<ExamModuleModel> modules = null;
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
    	this.businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        root = new DefaultTreeNode("root", null);
        ExamModuleModel root1 = new ExamModuleModel();
        root1.setId(this.businessId);

        root1.setAncestors("-1;");
        root1.setName("根模块");
        TreeNode r = new DefaultTreeNode(root1, root);
        this.buildDirTree(this.businessId, r);
    }

    public void change(List<ExamModuleModel> files) {
        errStr = "";
        this.modules = files;
    }

    private void buildDirTree(String id, TreeNode node) {
        List<ExamModuleModel> ls = cfDAO.findAllSonExamModuleModel(id);
        //MyLogger.echo("顶级文件夹数量：" + ls.size());
        if (ls == null || ls.isEmpty()) {
            return;
        } else {
            for (ExamModuleModel d : ls) {
                TreeNode tn = new DefaultTreeNode(d, node);
                buildDirTree(d.getId(), tn);
            }
        }
    }

    public String doChange() {
        errStr = "";
        ExamModuleModel cf = (ExamModuleModel) this.selectedNode.getData();
        FacesMessage fm = new FacesMessage();

        ExamModuleMag2 lcf = JsfHelper.getBean("examModuleMag2");
        List<ExamModuleModel> emms = lcf.fetchModules();
        if ((cf != null) && (emms != null)) {
            for (ExamModuleModel c : emms) {
//                MyLogger.println(cf.getId());
//                MyLogger.println(cf.getAncestors());
//                MyLogger.println(c.getId());
                if (cf.getAncestors().contains(c.getId())) {
                    errStr = "逻辑错误，试题模块无法移动向自身或子模块！";
                    JsfHelper.error(errStr);
                    return null;
                }
                String iddx = cf.getId();
                if (!c.getId().equals(iddx)&&iddx!=null) {
                    c.setFatherId(iddx);
                    this.cfDAO.updateExamModuleModel(c);//先保存后生成祖先结构才能生效
                    c.setAncestors(complex.genAncestors(c.getId()));
                    this.cfDAO.updateExamModuleModel(c);
                }
            }
            this.init();//更新本页显示的目录结构
            ExamModuleMag2 lcf2 = JsfHelper.getBean("examModuleMag2");
            if (lcf2 != null) {
                lcf2.loadStructure();
            }
        }
        return null;
    }
}
