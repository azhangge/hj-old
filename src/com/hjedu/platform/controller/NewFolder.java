package com.hjedu.platform.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.entity.BbsFileModel;
import com.huajie.util.IpHelper;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class NewFolder extends NewFileAbstract {

    String spName = "";
    private List<BbsFileModel> sons = new ArrayList();

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
        MyLogger.echo("spName set.");
    }

    @PostConstruct
    public void init() {
        //保证上传时存在一个用户
        ClientSession cs = JsfHelper.getBean("clientSession");
        BbsUser bu = cs.getUsr();
        if (bu == null) {
            IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
            bu = userDAO.findSysUser();
            cs.setUsr(bu);
        }

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        this.idt = request.getParameter("id");
        String fid = request.getParameter("fid");
        if (fid != null) {
            this.fatherID = fid;
        }
        String w = request.getParameter("scope");
        if (w != null) {
            this.scope = w;
        }
        this.checkName();
        if (idt != null) {
            this.project2 = this.project2DAO.findClientFile(this.idt);
            if(project2==null){
            	project2 = new BbsFileModel();
            }
            this.fnTemp = this.project2.getFileName();
            this.flag = true;
            this.project2DAO.loadAllDescendants(this.project2.getId(), this.sons);
        }
    }

    public void newAdd() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String scope_t = request.getParameter("scope");
        if (scope_t != null) {
            this.scope = scope_t;
        }
        this.idt = null;
        this.flag = false;
        this.project2 = new BbsFileModel();
        this.checkName();
        MyLogger.echo("Prepare to add folder.");
    }

    public void alter() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        this.idt = request.getParameter("id");
        if (idt != null) {
            this.project2 = this.project2DAO.findClientFile(this.idt);
            this.oldName = this.project2.getFileName();
            this.flag = true;
            this.project2DAO.loadAllDescendants(this.project2.getId(), this.sons);
        }
        String scope_t = request.getParameter("scope");
        if (scope_t != null) {
            this.scope = scope_t;
        }
        this.checkName();
    }

    public void checkName() {
        if (this.project2.getFileName() != null) {
            if (this.project2.getFileName().trim().equals("")) {
                this.errStr = "文件夹还未命名！";
                return;
            }

            if (!this.oldName.trim().equals("")) {
                if (this.oldName.trim().equals(
                        this.project2.getFileName().trim())) {
                    this.errStr = "";
                    return;
                }
            }
            String uid = "0";
            BbsUser bu = null;
            ClientSession cs = JsfHelper.getBean("clientSession");
            bu = cs.getUsr();
            if (bu != null) {
                uid = bu.getId();
            }
            boolean b = this.project2DAO.checkNameIfExistByUsr(this.project2.getFileName().trim(), this.fatherID, uid);
            if (b) {
                this.errStr = "文件夹名已存在，请重新命名！";
            } else {
                this.errStr = "";
            }
        }
    }

    public String createFolder() {
        BbsFileModel cfm = this.project2;
        cfm.setFatherID(this.fatherID);
        cfm.setIfFolder(true);

        ClientSession cs = (ClientSession) JsfHelper.getBean("clientSession");
        BbsUser cu = cs.getUsr();
        cfm.setUser(cu);
        if (this.project2.getFileName().equals("")) {
            this.project2.setFileName("未命名");
        }
        //设置本文件夹及其子文件的范围、子文件的密级
        cfm.setScope(this.scope);
        for (BbsFileModel s : sons) {
            s.setScope(this.scope);
            s.setSecretGrade(cfm.getSecretGrade());
            this.project2DAO.updateClientFileInfo(s);
        }

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String t = IpHelper.getRemoteAddr(request);

        if (!this.flag) {

            cfm.setUploadTime(new Date());
            this.project2DAO.saveClientFileInfo(this.project2);
            this.project2.setAncestors(this.cxLogic.genAncestors(this.project2.getId()));//生成祖先文件列表
            this.project2DAO.updateClientFileInfo(this.project2);
        } else {
            this.project2DAO.updateClientFileInfo(this.project2);
        }

        this.project2 = new BbsFileModel();

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
