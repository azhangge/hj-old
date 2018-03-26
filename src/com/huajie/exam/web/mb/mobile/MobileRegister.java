package com.huajie.exam.web.mb.mobile;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.service.IThirdPartyUserService;
import com.hjedu.customer.service.IUserSessionStateService;
import com.hjedu.customer.service.impl.ComplexBbsUserService;
import com.hjedu.examination.dao.IDictionaryDAO;
import com.hjedu.examination.dao.IExamDepartmentDAO;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.entity.ExamDepartment;
import com.hjedu.examination.service.impl.ExamDepartmentService;
import com.hjedu.platform.controller.ApplicationBean;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.dao.ISystemInfoDAO;
import com.hjedu.platform.service.impl.EmailService;
import com.huajie.exam.web.mb.*;
import com.huajie.util.CookieUtils;
import com.huajie.util.IpHelper;
import com.huajie.util.JsfHelper;
import com.huajie.util.MD5;
import com.huajie.util.SpringHelper;

/**
 * @author huajie
 *
 */
@ManagedBean
@ViewScoped
public class MobileRegister implements Serializable {

    BbsUser user;
    IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
    ISystemConfigDAO scd = SpringHelper.getSpringBean("SystemConfigDAO");
    EmailService emailService = SpringHelper.getSpringBean("EmailService");
    ISystemInfoDAO infoDAO = SpringHelper.getSpringBean("SystemInfoDAO");
    IUserSessionStateService iussService = SpringHelper.getSpringBean("UserSessionStateService");
    String pwd_re = "";
    String authStr = "";
    String id_temp = UUID.randomUUID().toString();
    String urnTip = "";
    boolean urnOk = true;
    String emailTip = "";
    boolean emailOk = true;
    boolean accept = true;
    List<ExamDepartment> depts = new ArrayList();
    IDictionaryDAO dicDAO = SpringHelper.getSpringBean("DictionaryDAO");
    List<DictionaryModel> dics = new ArrayList();
    DictionaryModel dic = new DictionaryModel();
    TreeNode root = new DefaultTreeNode();
    TreeNode selectedNode;
    private TreeNode[] selectedNodes;

    String bussinessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    
    List<String> selectedDepts = new ArrayList();

    public String getId_temp() {
        return id_temp;
    }

    public void setId_temp(String id_temp) {
        this.id_temp = id_temp;
    }

    public String getAuthStr() {
        return authStr;
    }

    public void setAuthStr(String authStr) {
        this.authStr = authStr;
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    public List<String> getSelectedDepts() {
        return selectedDepts;
    }

    public void setSelectedDepts(List<String> selectedDepts) {
        this.selectedDepts = selectedDepts;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
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

    public String getPwd_re() {
        return pwd_re;
    }

    public void setPwd_re(String pwd_re) {
        this.pwd_re = pwd_re;
    }

    public BbsUser getUser() {
        return user;
    }

    public void setUser(BbsUser user) {
        this.user = user;
    }

    public boolean isUrnOk() {
        return urnOk;
    }

    public void setUrnOk(boolean urnOk) {
        this.urnOk = urnOk;
    }

    public String getUrnTip() {
        return urnTip;
    }

    public void setUrnTip(String urnTip) {
        this.urnTip = urnTip;
    }

    public boolean isEmailOk() {
        return emailOk;
    }

    public void setEmailOk(boolean emailOk) {
        this.emailOk = emailOk;
    }

    public String getEmailTip() {
        return emailTip;
    }

    public void setEmailTip(String emailTip) {
        this.emailTip = emailTip;
    }

    public List<ExamDepartment> getDepts() {
        return depts;
    }

    public void setDepts(List<ExamDepartment> depts) {
        this.depts = depts;
    }

    @PostConstruct
    public void init() {
        user = new BbsUser();
        this.loadDepts();
        //this.loadStructure();
    }

    public void loadDepts() {
        this.depts.clear();
        IExamDepartmentDAO deptDAO = SpringHelper.getSpringBean("ExamDepartmentDAO");
        depts = deptDAO.findAllShowedExamDepartment(this.bussinessId);
    }

    private void loadStructure() {
        this.root = new DefaultTreeNode();
        DictionaryModel dm = dicDAO.findDictionaryModel(bussinessId);
        test(dm, root);
        this.dics = null;
        this.dics = dm.getSons();
    }

    public void test(DictionaryModel dd, TreeNode node) {
        //node.setExpanded(true);
        List<DictionaryModel> ls = dicDAO.findAllSonDictionaryModel(dd.getId());
        Collections.sort(ls);
        dd.setSons(ls);
        if (ls.isEmpty()) {
            node.setSelectable(true);
            return;
        } else {
            node.setSelectable(false);
            for (DictionaryModel d : ls) {
                if (d.getFrontShow()) {
                    TreeNode t = new DefaultTreeNode(d, node);
                    test(d, t);
                }
            }
        }
    }

    public String onChangeUrn() {
        String urn = this.user.getUsername();
        if (urn != null && !"".equals(urn)) {
            BbsUser bu = this.bbsUserDAO.findBbsUserByUrn(urn);
            if (bu != null) {
                this.urnOk = false;
                this.urnTip = "此用户名已存在！";
            } else {
                this.urnOk = true;
                this.urnTip = "此用户名可以注册！";
            }
        }
        return null;
    }

    public String onChangeEmail() {
        if (this.scd.getSystemConfig().getEmailValidation()) {
            String email = this.user.getEmail();
            if (email != null && !"".equals(email)) {
                if (!email.contains("@")) {
                    this.emailOk = false;
                    this.emailTip = "邮箱格式不正确！";
                    return null;
                }
                BbsUser bu = this.bbsUserDAO.findBbsUserByEmail(email);
                if (bu != null) {
                    this.emailOk = false;
                    this.emailTip = "此邮箱已存在！";
                } else {
                    this.emailOk = true;
                    this.emailTip = "此邮箱可以注册！";
                }
            }
        }
        return null;
    }

    public String refreshAuth() {
        this.id_temp = new Long(System.currentTimeMillis()).toString();
        return null;
    }

    public String reg_ok() {
        //Cat.println("hihi");

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        FacesMessage fm = new FacesMessage();
        FacesContext fc = FacesContext.getCurrentInstance();
        fm.setSeverity(FacesMessage.SEVERITY_ERROR);

        if (user.getUsername().trim().equals("") || user.getPassword().trim().equals("") || this.pwd_re.trim().equals("") || user.getName().trim().equals("") ) {
            fm.setSummary("所有输入框不能为空！");
            fc.addMessage("", fm);
            return null;
        }
        if ("".equals(user.getEmail())&&this.scd.getSystemConfig().getEmailValidation()) {
            JsfHelper.error("邮箱不能为空！");
            return null;
        }
//        //验证所选部门或班级不能为空
//        StringBuilder sb = new StringBuilder();
//        if (this.selectedNodes != null) {
//            for (TreeNode t : this.selectedNodes) {
//                DictionaryModel dmm = (DictionaryModel) t.getData();
//                sb.append(dmm.getId());
//                sb.append(";");
//            }
//
//            this.user.setGroupStr(sb.toString());
//        } else {
//            ApplicationBean ab = JsfHelper.getBean("applicationBean");
//            fm.setSummary("必须选择一个" + ab.getDepartmentStr() + "！");
//            fc.addMessage("", fm);
//            return null;
//        }

        
        //验证部门是否作了选择
        boolean departOk = true;
        if (this.selectedDepts== null) {
            departOk = false;
        } else {
            if (this.selectedDepts.isEmpty()) {
                departOk = false;
            }
        }
        //所选部门若为空，则设置其部门为默认部门，若默认部门为空，则提示用户必须选择一个部门
        StringBuilder sb = new StringBuilder();
        if (departOk) {
            for (String t : this.selectedDepts) {
                sb.append(t);
                sb.append(";");
            }
            this.user.setGroupStr(sb.toString());
        } else {
            String departStr = this.dicDAO.findAllDefaultDepartmentStr(bussinessId);
            if (departStr == null || "".equals(departStr)) {
                ApplicationBean ab = JsfHelper.getBean("applicationBean");
                JsfHelper.error("必须选择一个" + ab.getDepartmentStr() + "！");
                return null;
            } else {
                this.user.setGroupStr(departStr);
            }
        }
        

        if (!user.getPassword().equals(this.pwd_re)) {
            fm.setSummary("密码与确认密码不一致！");
            fc.addMessage("", fm);
            return null;
        }
        HttpSession s = (HttpSession) fc.getExternalContext().getSession(true);
        String str = (String) s.getAttribute("rand");
        if (!this.authStr.equals(str)) {
            fm.setSummary("验证码不正确！");
            fc.addMessage("", fm);
            return null;
        }
        BbsUser us = bbsUserDAO.findBbsUserByUrn(user.getUsername());
        if (us != null) {
            fm.setSummary("此用户已经存在！");
            fc.addMessage("", fm);
            return null;
        }
        BbsUser us0 = bbsUserDAO.findBbsUserByEmail(user.getEmail());
        if (us0 != null && scd.getSystemConfig().getEmailValidation()) {
            fm.setSummary("此E-Mail已经存在！");
            fc.addMessage("", fm);
            return null;
        }
        String ip = IpHelper.getRemoteAddr(request);
        this.user.setRegIp(ip);
        this.user.setLastIp(ip);

        //将密码变换
        ComplexBbsUserService cbus = SpringHelper.getSpringBean("ComplexBbsUserService");
        cbus.handlePwd(user);

        if (this.scd.getSystemConfig().getEmailValidation()) {
            HttpServletRequest req = JsfHelper.getRequest();
            String host = req.getServerName();
            int port = req.getServerPort();
            String sign = MD5.bit32(user.getId());
            String url = "http://" + host + ":" + port
                    + req.getContextPath() + "/servlet/RegisterActivate?urn=";
            String urn = user.getUsername();
            try {
                urn = URLEncoder.encode(urn, "UTF-8");
                System.out.println(url);
            } catch (Exception ee) {
            }
            url = url + urn + "&amp;sign=" + sign;
            //String mailbody = "点击<a href=\"" + url + "\">这里</a>激活帐号！";
            //ServletContext sc = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

            Map map = new HashMap();
            map.put("username", urn);
            map.put("url", url);
            String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
            map.put("sysname", this.infoDAO.findSystemInfoByBusinessId(businessId).getSiteName());
            boolean b = this.emailService.sendWithTemplate(this.user.getEmail(), this.infoDAO.findSystemInfoByBusinessId(businessId).getSiteName() + "系统帐号激活信", "reg_mail.vm", map);
            if (!b) {
                fm.setSummary("本系统要求验证邮箱，但验证邮件无法发出，因此无法注册！");
                fc.addMessage("", fm);
                return null;
            }
            this.user.setActivated(false);
            this.user.setChecked(this.scd.getSystemConfig().getAutoCheck());

            bbsUserDAO.addBbsUser(user);
            return "/mobile/RegEmailOut?faces-redirect=true";
        }
        this.user.setChecked(this.scd.getSystemConfig().getAutoCheck());
        bbsUserDAO.addBbsUser(user);

        if (!this.scd.getSystemConfig().getAutoCheck()) {
            return "/mobile/WaitingCheck?faces-redirect=true";
        }

        ClientSession cs = JsfHelper.getBean("clientSession");
        cs.setIfLogin(true);
        cs.setUsr(user);

        iussService.login(user);
        
        //第三方平台添加用户
        Boolean ifLDAP = Boolean.parseBoolean(SpringHelper.getSpringBean("usercheck_thirdParty").toString());
        if (ifLDAP) {//使用第三方验证的情况
            IThirdPartyUserService third=SpringHelper.getSpringBean("ThirdPartyUserService");
            third.register(user.getUsername(),pwd_re, user.getEmail());
        }

        return "/mobile/Default?faces-redirect=true";

    }
}
