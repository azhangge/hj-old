package com.hjedu.customer.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.util.Collections;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.hjedu.customer.dao.IBbsFileDAO;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IDictionaryDAO;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.dao.IShareDAO;
import com.hjedu.platform.entity.BbsFileModel;
import com.hjedu.platform.entity.CooperatorWrapper;
import com.hjedu.platform.service.IMessageService;
import com.huajie.util.Cat;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ShareToPers implements Serializable {

    IBbsUserDAO cuDAO = SpringHelper.getSpringBean("BbsUserDAO");
    IBbsFileDAO cfDAO = SpringHelper.getSpringBean("BbsFileDAO");
    IShareDAO shareDAO = SpringHelper.getSpringBean("ShareDAO");
    String nothing;
    List<CooperatorWrapper> users = new ArrayList();
    List<CooperatorWrapper> usersShowed = new ArrayList();
    List depts = new ArrayList();
    String dept = "";
    BbsFileModel file;
    String temp = "";
    //String fileId="";
    IDictionaryDAO dicDAO = SpringHelper.getSpringBean("DictionaryDAO");
    List<DictionaryModel> dics = new ArrayList();
    DictionaryModel dic = new DictionaryModel();
    TreeNode root = new DefaultTreeNode();
    TreeNode selectedNode;
    String bussinessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    
    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getNothing() {
        return Cat.getUniqueId();
    }

    public void setNothing(String nothing) {
        this.nothing = nothing;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public List getDepts() {
        return depts;
    }

    public void setDepts(List depts) {
        this.depts = depts;
    }

    public BbsFileModel getFile() {
        return file;
    }

    public void setFile(BbsFileModel file) {
        this.file = file;
    }

    public List<CooperatorWrapper> getUsers() {
        return users;
    }

    public void setUsers(List<CooperatorWrapper> users) {
        this.users = users;
    }

    public List<CooperatorWrapper> getUsersShowed() {
        return usersShowed;
    }

    public void setUsersShowed(List<CooperatorWrapper> usersShowed) {
        this.usersShowed = usersShowed;
    }

    public List<DictionaryModel> getDics() {
        return dics;
    }

    public void setDics(List<DictionaryModel> dics) {
        this.dics = dics;
    }

    public DictionaryModel getDic() {
        return dic;
    }

    public void setDic(DictionaryModel dic) {
        this.dic = dic;
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
        this.loadStructure();
    }

    private void loadStructure() {
        this.root = new DefaultTreeNode();
        DictionaryModel dm = dicDAO.findDictionaryModel(bussinessId);
        test(dm, root);
        this.dics = null;
        this.dics = dm.getSons();

    }

    public void test(DictionaryModel dd, TreeNode node) {
        //检查，如果某节点的所有子节点中只要有节点被选中，此节点就应该展开
        boolean k = false;

        //判断是否应该展开完毕
        //判断是否应该选中,若选中，也要展开（因为可能有子元素未选中，而父元素选中的情况）
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

    public String share(String id) {
        if (id != null) {
            this.file = this.cfDAO.findClientFile(id.trim());
            //this.loadSelects();
            this.loadChecks();
            this.alterCheckShowed();
        }
        return null;
    }

    private void loadChecks() {//找出所有用户，并设置用户是否被分享文件
        List<BbsUser> cus = cuDAO.findAllBbsUser(bussinessId);
        List<BbsUser> cst = shareDAO.findSharedUsersByFile(this.file.getId());
        System.out.println(cus);
        System.out.println(cst);
        this.users.clear();
        if (cus != null) {
            for (BbsUser cu : cus) {
                boolean t = false;
                if (cst != null) {
                    for (BbsUser ct : cst) {
                        if (ct.getId().trim().equals(cu.getId().trim())) {
                            t = true;
                            System.out.println(ct.getName() + "被设置为TRUE");
                            break;
                        }
                    }
                }
                CooperatorWrapper cw = new CooperatorWrapper();
                cw.setSelected(t);
                cw.setUser(cu);
                this.users.add(cw);
            }
            System.out.println(users.size());
        }
    }

    public String alterCheckShowed() {//设置当前显示用户
        System.out.println("AlterCheckShowed....");
        this.usersShowed.clear();
        DictionaryModel dm = null;
        if (this.selectedNode != null) {
            dm = (DictionaryModel) this.selectedNode.getData();
        }
        if (dm != null) {
            for (CooperatorWrapper c : this.users) {
                if (dm.getId().equals(c.getUser().getGroupId())) {
                    this.usersShowed.add(c);
                    System.out.println(c.getUser().getName() + " added.");
                }
            }
        }
        System.out.println(this.usersShowed.size() + "..");
        return null;
    }

    public String selectChange() {
        this.synSS();
        this.alterCheckShowed();
        return null;
    }

    private void synSS() {//同步usersShowed到users
        System.out.println("Temp:" + this.temp);
        for (CooperatorWrapper c : this.usersShowed) {
            System.out.println(c.getUser().getName() + "的情况：" + c.isSelected());
            for (CooperatorWrapper w : this.users) {
                if (c.getUser().getId().equals(w.getUser().getId())) {
                    w.setSelected(c.isSelected());
                }
            }
        }
    }

    public void selectCheckBox(AjaxBehaviorEvent e) {
        System.out.println("select event;");
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String id = req.getParameter("id");
        for (CooperatorWrapper u : usersShowed) {
            if (u.getUser().getId().equals(id)) {
                u.setSelected(!u.isSelected());
            }
        }
    }

    public String save_action() {
        //ServletContext sc = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        this.synSS();

        List<BbsUser> userTemp = new ArrayList();
        for (CooperatorWrapper cw : this.users) {
            System.out.println(cw.getUser().getName() + "在当前可见用户中，" + this.file.getFileName());
            if (cw.isSelected()) {
                userTemp.add(cw.getUser());
                IMessageService msgService = SpringHelper.getSpringBean("MessageService");
                String preStr = "<strong>分享的文件如下：</strong><br/>" + this.file.getFileName() + "<br/><strong>请到“个人分享中查看”</strong>";
                msgService.sendMessageByAdmin(cw.getUser().getId(), "新分享：" + this.file.getFileName(), preStr);
                System.out.println(cw.getUser().getName() + "被分享了文件：" + this.file.getFileName());
            }
        }
        shareDAO.updateFileShareUser(this.file.getId(), userTemp);
        //将所有子文件也分享
        List<BbsFileModel> sons = new ArrayList();
        this.cfDAO.loadAllDescendants(file.getId(), sons);
        for (BbsFileModel s : sons) {
            shareDAO.updateFileShareUser(s.getId(), userTemp);
        }

        String sth = "分享了文件“" + this.file.getFileName() + "”给：";
        String sst = "";
        int si = userTemp.size();
        for (int i = 0; i < si; i++) {
            BbsUser c = userTemp.get(i);
            if (i == (si - 1)) {
                sst += c.getName() + "。";
            } else {
                sst += c.getName() + "、";
            }
        }
        sth += sst;
        ClientSession ab = (ClientSession) JsfHelper.getBean("clientSession");

        System.out.println("分享文件给了:" + sst);

        MySharedFiles lcf = JsfHelper.getBean(FacesContext.getCurrentInstance(), "mySharedFiles");
        if (lcf != null) {
            lcf.reload();
        }

        return null;
    }
}
