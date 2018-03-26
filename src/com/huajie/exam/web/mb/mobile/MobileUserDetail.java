package com.huajie.exam.web.mb.mobile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.service.impl.ComplexBbsUserService;
import com.hjedu.examination.dao.IDictionaryDAO;
import com.hjedu.examination.dao.IExamDepartmentDAO;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.entity.ExamDepartment;
import com.hjedu.examination.service.impl.ExamDepartmentService;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.dao.IDictCityDAO;
import com.hjedu.platform.dao.IDictProvinceDAO;
import com.hjedu.platform.dao.IImgDAO;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.entity.DictCity;
import com.hjedu.platform.entity.DictProvince;
import com.hjedu.platform.service.impl.ImageService;
import com.huajie.exam.web.mb.*;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class MobileUserDetail implements Serializable {

    BbsUser usr = null;
    String pwd_temp = "";
    String pwd_n1;
    String pwd_n2;
    String okScript;
    private IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
    private IImgDAO imgDAO = SpringHelper.getSpringBean("ImgDAO");
    List<SelectItem> provs = new ArrayList();
    List<SelectItem> citys = new ArrayList();
    IDictCityDAO cityDAO = SpringHelper.getSpringBean("DictCityDAO");
    IDictProvinceDAO provinceDAO = SpringHelper.getSpringBean("DictProvinceDAO");
    ISystemConfigDAO scd = SpringHelper.getSpringBean("SystemConfigDAO");
    FileUpload fu = new FileUpload();
    int province = 10;
    int city = 1;
    TreeNode root = new DefaultTreeNode();
    ExamDepartmentService dicDAO = SpringHelper.getSpringBean("ExamDepartmentService");
    List<DictionaryModel> dics = new ArrayList();
//    private TreeNode selectedNode;
//    private TreeNode[] selectedNodes;
    List<String> selectedDepts = new ArrayList();//选中的部门
    List<ExamDepartment> depts = new ArrayList();//所有部门

    String bussinessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    
    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public List<String> getSelectedDepts() {
        return selectedDepts;
    }

    public void setSelectedDepts(List<String> selectedDepts) {
        this.selectedDepts = selectedDepts;
    }

    public List<ExamDepartment> getDepts() {
        return depts;
    }

    public void setDepts(List<ExamDepartment> depts) {
        this.depts = depts;
    }

    public BbsUser getUsr() {
        return usr;
    }

    public void setUsr(BbsUser usr) {
        this.usr = usr;
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

    public String getPwd_n1() {
        return pwd_n1;
    }

    public void setPwd_n1(String pwd_n1) {
        this.pwd_n1 = pwd_n1;
    }

    public String getPwd_n2() {
        return pwd_n2;
    }

    public void setPwd_n2(String pwd_n2) {
        this.pwd_n2 = pwd_n2;
    }

    public String getPwd_temp() {
        return pwd_temp;
    }

    public void setPwd_temp(String pwd_temp) {
        this.pwd_temp = pwd_temp;
    }

    public String getOkScript() {
        return okScript;
    }

    public void setOkScript(String okScript) {
        this.okScript = okScript;
    }

    public List<SelectItem> getCitys() {
        return citys;
    }

    public void setCitys(List<SelectItem> citys) {
        this.citys = citys;
    }

    public List<SelectItem> getProvs() {
        return provs;
    }

    public void setProvs(List<SelectItem> provs) {
        this.provs = provs;
    }

    @PostConstruct
    public void init() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String temp = request.getParameter("id");
        if (temp != null) {
            this.usr = userDAO.findBbsUser(temp.trim());
        }

        if (this.usr != null) {
            DictProvince p = this.usr.getProvince();
            DictCity c = this.usr.getCity();
            if (p != null) {
                this.province = p.getNProvid();
            }
            if (c != null) {
                this.city = c.getNCityid();
            }
        }

        List<DictProvince> ps = this.provinceDAO.findAll(0, 1000);
        for (DictProvince p : ps) {
            SelectItem s = new SelectItem(p.getNProvid(), p.getSProvname());
            this.provs.add(s);
        }
        List<DictCity> cs = this.cityDAO.findByNProvid(this.province, 0, 1000);
        for (DictCity c : cs) {
            SelectItem s = new SelectItem(c.getNCityid(), c.getSCityname());
            this.citys.add(s);
        }
        this.loadDepts();
        //this.loadStructure();
    }

    public void loadDepts() {
        this.depts.clear();
        IExamDepartmentDAO deptDAO = SpringHelper.getSpringBean("ExamDepartmentDAO");
        depts = deptDAO.findAllShowedExamDepartment(this.bussinessId);
        this.selectedDepts.clear();
        String xx = this.usr.getGroupStr();
        if (xx != null) {
            String xxs[] = xx.split(";");
            if (xxs != null) {
                for (String id : xxs) {
                    if (id != null) {
                        this.selectedDepts.add(id);
                    }
                }
            }
        }
    }

    public String loadCities() {
        this.citys.clear();
        List<DictCity> cs = this.cityDAO.findByNProvid(this.province, 0, 1000);
        for (DictCity c : cs) {
            SelectItem s = new SelectItem(c.getNCityid(), c.getSCityname());
            this.citys.add(s);
        }
        return null;
    }

    public List up_action(FileUploadEvent event) {
        try {
            UploadedFile item = event.getFile();
            String str = "";
            Long tl = item.getSize();
            //this.project2.setRealLength(tl);
            Float k = 1024F;
            Float m = 1024 * 1024F;
            Float g = 1024 * 1024 * 1024F;
            if (tl < k) {
                str = tl.toString() + "B";
            } else if (k <= tl && tl < m) {
                Float te = Math.round(tl / k * 100) / 100.00F;
                str = te.toString() + "KB";
            } else if (tl >= m && tl < g) {
                Float te = Math.round(tl / m * 100) / 100.00F;
                str = te.toString() + "MB";
            } else {
                Float te = Math.round(tl / g * 100) / 100.00F;
                str = te.toString() + "GB";
            }
            //this.project2.setFileSize(str);
            String n = item.getFileName();
            int l1 = n.lastIndexOf("\\");
            int l2 = n.lastIndexOf(".");
            String ext = "." + n.substring(l2 + 1).toLowerCase();
            String name = n.substring(l1 + 1, l2);
            String imgId = UUID.randomUUID().toString();
            this.imgDAO.saveImg(item.getInputstream(), imgId);
            //下面代码将图片转存到相对地址中
            if (ext.toLowerCase().equals(".jpg") || ext.toLowerCase().equals(".gif") || ext.toLowerCase().equals(".png")) {
                ImageService s = SpringHelper.getSpringBean("ImageService");
                s.transferUserImagesToRelativeDir(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()), imgId);
            }
            MyLogger.echo("upload executed.");
            this.fu = new FileUpload();
            String picUrl = "servlet/ShowAbstractImage?id=" + imgId;
            this.usr.setPicUrl(picUrl);
            return null;
        } catch (Exception ee) {
            ee.printStackTrace();
            return null;
        }
    }

    public String saveUserChange() {

        if (scd.getSystemConfig().isAllowChgDept()) {//若允许前台用户修改密码，则验证修改
            StringBuilder sb = new StringBuilder();
            if (!this.selectedDepts.isEmpty()) {
                for (String t : this.selectedDepts) {
                    sb.append(t);
                    sb.append(";");
                }
                this.usr.setGroupStr(sb.toString());
            } else {
//            ApplicationBean ab = JsfHelper.getBean("applicationBean");
//            fm.setSummary("必须选择一个" + ab.getDepartmentStr() + "！");
//            fc.addMessage("", fm);
                DictionaryModel dm = dicDAO.findDictionaryModel(bussinessId,bussinessId);
                sb.append(dm.getId());
                sb.append(";");
                this.usr.setGroupStr(sb.toString());
            }
        }
        //判断密码的情况
        boolean notOk = false;

        boolean hasPwd = true;
        if ((pwd_temp.equals("") && pwd_n1.equals("")) && pwd_n2.equals("")) {
            hasPwd = false;
        }

        FacesMessage fm = new FacesMessage();
        fm.setSeverity(FacesMessage.SEVERITY_ERROR);

        ComplexBbsUserService cbus = SpringHelper.getSpringBean("ComplexBbsUserService");
        boolean b = cbus.checkPwd(usr, this.pwd_temp);
        if (!b && hasPwd) {
            fm.setSummary("原密码不正确！");
            //this.okScript = "alert(\"原密码不正确！\");";

            FacesContext.getCurrentInstance().addMessage("", fm);
            return null;

        } else if ((!this.pwd_n1.equals(this.pwd_n2)) && hasPwd) {
            fm.setSummary("两次输入的新密码不一致！");
            FacesContext.getCurrentInstance().addMessage("", fm);
            //this.okScript = "alert(\"两次输入的新密码不一致！\");";
            return null;
        } else if (this.pwd_n1.equals("") && hasPwd) {
            fm.setSummary("密码不能为空！");
            //this.okScript = "alert(\"密码不能为空！\");";
            FacesContext.getCurrentInstance().addMessage("", fm);
            return null;
        }

        if (this.scd.getSystemConfig().isUsePid()) {
            //强制验证身份证逻辑
            if (usr.getPid().trim().equals("")) {
                fm.setSummary("身份证号不能为空！");
                FacesContext.getCurrentInstance().addMessage("", fm);
                return null;
            } else if (this.usr.getPid().length() != 18) {
                fm.setSummary("身份证号必须为18位！");
                FacesContext.getCurrentInstance().addMessage("", fm);
                return null;
            } else {
                BbsUser us0 = userDAO.findBbsUserByPid(usr.getPid());
                if (us0 != null) {
                    if (!us0.getId().equals(usr.getId())) {
                        fm.setSummary("此身份证号已经存在！");
                        FacesContext.getCurrentInstance().addMessage("", fm);
                        return null;
                    }
                }
            }
        }

        if (hasPwd) {
            this.usr.setPassword(this.pwd_n1);
            //将密码变换
            cbus.handlePwd(usr);
        }
        this.usr.setProvince(this.provinceDAO.findById(this.province));
        this.usr.setCity(this.cityDAO.findById(this.city));
        this.userDAO.updateBbsUser(this.usr);
        ClientSession cs = JsfHelper.getBean("clientSession");
        cs.setUsr(usr);
        JsfHelper.info("修改完成！");

        this.setOkScript("");
        return null;
    }
}
