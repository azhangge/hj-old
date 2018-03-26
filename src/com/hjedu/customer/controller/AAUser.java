package com.hjedu.customer.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.hjedu.businessuser.dao.IBusinessUserDao;
import com.hjedu.businessuser.entity.BusinessUser;
import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.entity.LessonType;
import com.hjedu.customer.dao.IAdminDAO;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.service.impl.ExamModule2Service;
import com.hjedu.examination.service.impl.ExaminationService;
import com.hjedu.platform.entity.AuthorityWraper;
import com.hjedu.platform.service.ILogService;
import com.huajie.app.util.Validator;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAUser implements Serializable {
	BbsUser user = new BbsUser();
    IAdminDAO adminDAO = SpringHelper.getSpringBean("AdminDAO");
    ILogService logger = SpringHelper.getSpringBean("LogService");
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    IExaminationDAO examDAO = SpringHelper.getSpringBean("ExaminationDAO");
    ILessonTypeDAO lessonTypeDAO = SpringHelper.getSpringBean("LessonTypeDAO");
    AdminInfo admin = new AdminInfo();
    boolean flag = false;
    Map mapTemp = new HashMap();
    List<AuthorityWraper> authList = new LinkedList<AuthorityWraper>();
    TreeNode moduleRoot = new DefaultTreeNode();
    TreeNode selectedModules[];
    List<ExamModuleModel> selectedModules2 = new LinkedList<ExamModuleModel>();
    List<Examination> exams = new LinkedList<Examination>();
    List<LessonType> lessonTypes = new LinkedList<LessonType>();
    String urnTip = "";
    boolean urnOk = true;
    String emailTip = "";
    boolean emailOk = true;
    String phoneTip = "";
    boolean phoneOk = true;
    boolean isDefaultAdmin = false;
    String businessId;
    
    public AdminInfo getAdmin() {
        return admin;
    }
    
    public void setAdmin(AdminInfo admin) {
        this.admin = admin;
    }
    
    public List<Examination> getExams() {
        return exams;
    }
    
    public void setExams(List<Examination> exams) {
        this.exams = exams;
    }
    
    public List<LessonType> getLessonTypes() {
        return lessonTypes;
    }
    
    public void setLessonTypes(List<LessonType> lessonTypes) {
        this.lessonTypes = lessonTypes;
    }
    
    public TreeNode[] getSelectedModules() {
        return selectedModules;
    }
    
    public void setSelectedModules(TreeNode[] selectedModules) {
        this.selectedModules = selectedModules;
    }
    
    public List<ExamModuleModel> getSelectedModules2() {
        return selectedModules2;
    }
    
    public void setSelectedModules2(List<ExamModuleModel> selectedModules2) {
        this.selectedModules2 = selectedModules2;
    }
    
    public TreeNode getModuleRoot() {
        return moduleRoot;
    }
    
    public void setModuleRoot(TreeNode moduleRoot) {
        this.moduleRoot = moduleRoot;
    }
    
    public Map getMapTemp() {
        return this.mapTemp;
    }
    
    public void setMapTemp(Map mapTemp) {
        this.mapTemp = mapTemp;
    }
    
    public boolean isFlag() {
        return flag;
    }
    
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    
    public List<AuthorityWraper> getAuthList() {
        return authList;
    }
    
    public void setAuthList(List<AuthorityWraper> authList) {
        this.authList = authList;
    }
 
    public String getUrnTip() {
		return urnTip;
	}

	public void setUrnTip(String urnTip) {
		this.urnTip = urnTip;
	}

	public boolean isUrnOk() {
		return urnOk;
	}

	public void setUrnOk(boolean urnOk) {
		this.urnOk = urnOk;
	}

	public String getEmailTip() {
		return emailTip;
	}

	public void setEmailTip(String emailTip) {
		this.emailTip = emailTip;
	}

	public boolean isEmailOk() {
		return emailOk;
	}

	public void setEmailOk(boolean emailOk) {
		this.emailOk = emailOk;
	}
	
	public String getPhoneTip() {
		return phoneTip;
	}

	public void setPhoneTip(String phoneTip) {
		this.phoneTip = phoneTip;
	}

	public boolean isPhoneOk() {
		return phoneOk;
	}

	public void setPhoneOk(boolean phoneOk) {
		this.phoneOk = phoneOk;
	}

	@PostConstruct
    public void init() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String id = req.getParameter("id");
        
        /* 判断是否为企业客户生成默认操作员 */
        String businessId = req.getParameter("businessId");
        if(StringUtils.isNotBlank(businessId)){
        	this.isDefaultAdmin = true;
        	this.businessId = businessId;
        	IBusinessUserDao businessUserDao = SpringHelper.getSpringBean("BusinessUserDAO");
        	BusinessUser businessUser = businessUserDao.findBussinessUser(businessId);
        	if(businessUser.getHasAdmin()){
        		id = businessUser.getAdminId();
        	}
        } else {
        	this.businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        }
        
        if (id != null) {
            this.admin = this.adminDAO.findAdmin(id);
            this.flag = true;
        }
        this.buildAuthList();
        this.loadModuleStructure();
        this.loadExaminations();
        this.loadLessonTypes();
    }
    
    public void loadExaminations() {
        this.exams = this.examDAO.findAllExamination(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        List<Examination> exs = this.admin.getExams();
        if (exs != null && this.exams != null) {
            for (Examination e : exams) {
                for (Examination ex : exs) {
                    if (ex.getId().equals(e.getId())) {
                        e.setSelected(true);
                        break;
                    }
                }
            }
        }
    }
    
    public void loadLessonTypes() {
        this.lessonTypes = this.lessonTypeDAO.findAllLessonType(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        List<LessonType> exs = this.admin.getLessonTypes();
        if (exs != null && this.lessonTypes != null) {
            for (LessonType e : lessonTypes) {
                for (LessonType ex : exs) {
                    if (ex.getId().equals(e.getId())) {
                        e.setSelected(true);
                        break;
                    }
                }
            }
        }
    }

    //加载所有的试题模块结构
    public void loadModuleStructure() {
        this.moduleRoot = new DefaultTreeNode();
        ExamModuleModel dm = moduleDAO.findExamModuleModel(this.businessId);
        if(dm != null){
        	testLoadModule(dm, moduleRoot);
        }
    }

    /**
     * 按层次结构加载所有的模块元素到node，并验证是否已经被选中
     *
     * @param dd
     * @param node
     */
    public void testLoadModule(ExamModuleModel dd, TreeNode node) {
        List<ExamModuleModel> mos = this.admin.getModules();
        if (mos != null) {
            if (mos.contains(dd)) {
                dd.setSelected(true);
            }
        }
        List<ExamModuleModel> ls = moduleDAO.findAllSonExamModuleModel(dd.getId(), CookieUtils.getBusinessId(JsfHelper.getRequest()));
        Collections.sort(ls);
        dd.setSons(ls);
        if (ls.isEmpty()) {
            node.setSelectable(true);
            return;
        } else {
            node.setSelectable(false);
            for (ExamModuleModel d : ls) {
                TreeNode t = new DefaultTreeNode(d, node);
                testLoadModule(d, t);
            }
        }
    }
    
    private boolean testIfContain(List<ExamModuleModel> dds, ExamModuleModel emm) {
        for (ExamModuleModel em : dds) {
            if (em != null && emm != null) {
                if (em.getId().equals(emm.getId())) {
                    //break;
                    return true;
                }
            }
        }
        return false;
    }
    
    public String onChangeUrn() {
//      String urn = this.user.getUsername().trim(); 
      String urn = this.admin.getUrn().trim();
      
      if (urn != null && !"".equals(urn)) {
    	  AdminInfo ai=this.adminDAO.findAdminByUrnByBusinessId(urn,this.businessId);	
          if (ai != null) {
              this.urnOk = false;
              this.urnTip = "此用户名已存在";
          } else {
              this.urnOk = true;
              this.urnTip = "此用户名可以注册";
          }
      } else{
      	this.urnOk = false;
          this.urnTip = "用户名不能为空";
      }
      return null;
  }
    
    public String onChangeEmail() {
        String email = this.admin.getEmail();
        if (email != null && !"".equals(email)) {
            if (!email.contains("@")) {
                this.emailOk = false;
                this.emailTip = "邮箱格式不正确";
                return null;
            }
            AdminInfo ai = this.adminDAO.findAdminByEmail(email);
            if (ai != null) {
                this.emailOk = false;
                this.emailTip = "此邮箱已存在";
            } else {
                this.emailOk = true;
                this.emailTip = "此邮箱可以注册";
            }
        }
        return null;
    }
    
    public String onChangePhone() {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    	String phone = this.admin.getTel().trim();
    	String truePhone;
    	if (phone != null && !"".equals(phone)) {
    		if(Validator.isMobile(phone)){//是合格的手机号
				truePhone=Validator.trueMobile(phone);//处理后的真实号码
				AdminInfo ai = this.adminDAO.findAdminByPhoneByBusinessId(truePhone,businessId);
	            if (ai != null) {
	                this.phoneOk = false;
	                this.phoneTip = "此手机号已存在";
	            } else {
	                this.phoneOk = true;
	                this.phoneTip = "此手机号可以注册";
	            }
    		}else{
    			this.phoneOk = false;
                this.phoneTip = "请填写正确的手机号";
    		}
        } else{
        	this.phoneOk = false;
            this.phoneTip = "手机号不能为空";
        }
    	return null;
    }
    
    /**
     * 级联子元素全部选中或不选
     *
     * @param id
     * @param b
     */
    public void checkSons(String id, boolean b) {
        ExamModuleModel emm = this.moduleDAO.findExamModuleModel(id);
        List<ExamModuleModel> modules = this.moduleDAO.loadAllDescendants(id);
        modules.add(emm);
        this.testSonModule(modules, moduleRoot, b);
    }

    /**
     * 验证或操作所有的子元素
     *
     * @param dds
     * @param tn
     * @param b
     */
    public void testSonModule(List<ExamModuleModel> dds, TreeNode tn, boolean b) {
        //List<ExamModuleModel> mos = this.role.getModules();
        ExamModuleModel top = (ExamModuleModel) tn.getData();
        if (this.testIfContain(dds, top)) {
            top.setSelected(b);
        }
        List<TreeNode> tns = tn.getChildren();
        if (tns == null) {
            return;
        } else {
            for (TreeNode t : tns) {
                testSonModule(dds, t, b);
            }
        }
    }

    /**
     * 将root中被选中的元素加入ms
     *
     * @param ms
     * @param root
     */
    public void testFetchModule(List<ExamModuleModel> ms, TreeNode root) {
        List<TreeNode> ls = root.getChildren();
        if (ls.isEmpty()) {
            return;
        } else {
            for (TreeNode d : ls) {
                ExamModuleModel em = (ExamModuleModel) d.getData();
                if (em.isSelected()) {
                    ms.add(em);
                }
                testFetchModule(ms, d);
            }
        }
    }

    //取得所有被选中的模块元素
    public List<ExamModuleModel> fetchModules() {
        this.selectedModules2.clear();
        List<ExamModuleModel> exxs = new ArrayList<ExamModuleModel>();
        this.testFetchModule(this.selectedModules2, moduleRoot);
        for (ExamModuleModel em : this.selectedModules2) {
            exxs.add(em);
            MyLogger.println(em.getName());
        }
        return exxs;
    }
    
    public List<Examination> fetchExams() {
        List<Examination> exxs = new ArrayList<Examination>();
        if (this.exams != null) {
            for (Examination ex : this.exams) {
                if (ex.isSelected()) {
                    exxs.add(ex);
                }
            }
        }
        return exxs;
    }
    
    public List<LessonType> fetchLessonTypes() {
        List<LessonType> exxs = new ArrayList<LessonType>();
        if (this.lessonTypes != null) {
            for (LessonType ex : this.lessonTypes) {
                if (ex.isSelected()) {
                    exxs.add(ex);
                }
            }
        }
        return exxs;
    }
    
    private void buildAuthList() {
        Map auths = SpringHelper.getSpringBean("authorities");
        Set keys = auths.keySet();
        for (Object k : keys) {
            Object v = auths.get(k);
            AuthorityWraper aw = new AuthorityWraper();
            aw.setName(k.toString());
            aw.setCnName(v.toString());
            if(!this.flag&&"lesson;user;class".contains(aw.getName())){
            	aw.setSelected(true);
            }
            this.authList.add(aw);
        }
        for (AuthorityWraper aw : authList) {
            if (this.admin.getAuthstr()!=null&&this.admin.getAuthstr().contains(aw.getName().toString())) {
                aw.setSelected(true);
            }
        }
    }
    
    private String buildAuthStr() {
        StringBuilder str = new StringBuilder();
        for (AuthorityWraper aw : authList) {
            if (aw.isSelected()) {
                str.append(aw.getName().toString());
                str.append(";");
            }
        }
        return str.toString();
    }
    
    public String submit_action() {
        this.admin.setModules(this.fetchModules());
        this.admin.setExams(this.fetchExams());
        this.admin.setLessonTypes(this.fetchLessonTypes());
        this.admin.setAuthstr(this.buildAuthStr());

        this.admin.setBusinessId(this.businessId);
        if(this.isDefaultAdmin){
        	this.admin.setGrp("user");
        	this.admin.setChecked(true);
        } 
        
        FacesContext fc = FacesContext.getCurrentInstance();
        AdminSessionBean asb = JsfHelper.getBean("adminSessionBean");
        this.admin.setSuperId(asb.getAdmin().getId());
        
        if (!flag) {
            AdminInfo temp = this.adminDAO.findAdminByUrnByBusinessId(admin.getUrn(),this.businessId);
            if (temp != null) {
                FacesMessage fm = new FacesMessage();
                fm.setSeverity(FacesMessage.SEVERITY_ERROR);
                fm.setSummary("此用户名已存在！");
                fc.addMessage("", fm);
                return null;
            }
            this.admin.setGrp("company");
            this.admin.setLtime(new Date());
            this.admin.setGenTime(new Date());
            adminDAO.addAdmin(admin);
            logger.log("添加了管理员：" + this.admin.getNickname() + ",ID:" + this.admin.getId());
            
            if(this.isDefaultAdmin){
            	IBusinessUserDao businessUserDao = SpringHelper.getSpringBean("BusinessUserDAO");
            	BusinessUser businessUser = businessUserDao.findBussinessUser(this.admin.getBusinessId());
            	if(businessUser!=null){
            		if(!businessUser.getHasAdmin()) {
            			businessUser.setHasAdmin(true);
            			businessUser.setAdminId(this.admin.getId());
            			businessUserDao.updateBusinessUser(businessUser);
            			logger.log("添加了默认管理员：" + this.admin.getNickname() + ",ID:" + this.admin.getId());
            		}
            	}
            }
        } else {
            AdminInfo c = asb.getAdmin();
            if (((c.getId().equals(admin.getId())) && (asb.isIfSuper())) && (((!admin.getEnabled().equals(c.getEnabled())) || (!admin.getGrp().equals(c.getGrp()))))) {
                FacesMessage fm = new FacesMessage();
                fm.setSeverity(FacesMessage.SEVERITY_ERROR);
                fm.setSummary("超级管理员不能修改自身的激活状态、用户组以及权限！");
                fc.addMessage("", fm);
                return null;
            }
            adminDAO.updateAdmin(admin);
            logger.log("修改了管理员：" + admin.getNickname());
            if (c.getId().equals(admin.getId())) {
                asb.setAdmin(admin);
            }
            
        }
        
        asb.refreshAdmin();
        ExamModule2Service moduleService = SpringHelper.getSpringBean("ExamModule2Service");
        moduleService.reBuildCache();
        ExaminationService examService = SpringHelper.getSpringBean("ExaminationService");
        examService.reBuildCache();
        //adminDAO.updateAdmin(admin);
        
        if(this.isDefaultAdmin) {
        	return "ListBusinessUser?faces-redirect=true";
        } else {
        	return "ListUser?faces-redirect=true";
        }
    }
    
    public void changeGrp(){
    	if(this.admin.getGrp().equals("user")){
    		this.admin.setAuthstr("lesson;examination;export;module;sys;score;class;analysis;safety;market;question;notice;user;talk;room;");
    	}else{
    		this.admin.setAuthstr("lesson;user;class");
    	}
    	for (AuthorityWraper aw : authList) {
            if (this.admin.getAuthstr()!=null&&this.admin.getAuthstr().contains(aw.getName().toString())) {
                aw.setSelected(true);
            }else{
            	aw.setSelected(false);
            }
        }
    }
}
