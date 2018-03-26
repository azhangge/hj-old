package com.hjedu.customer.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.json.JSONObject;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

import com.hjedu.businessuser.dao.IBusinessUserDao;
import com.hjedu.businessuser.entity.BusinessUser;
import com.hjedu.customer.UserUtil;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.service.impl.ComplexBbsUserService;
import com.hjedu.examination.dao.IExamDepartmentDAO;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.entity.ExamDepartment;
import com.hjedu.examination.service.impl.ExamDepartmentService;
import com.hjedu.platform.controller.AdminNewFile;
import com.hjedu.platform.controller.ApplicationBean;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.service.ILogService;
import com.huajie.app.util.NetworkUtil;
import com.huajie.app.util.StringUtil;
import com.huajie.app.util.Validator;
import com.huajie.corss.model.SubUser;
import com.huajie.corss.util.Conn;
import com.huajie.corss.util.ObjectConvent;
import com.huajie.util.Cat;
import com.huajie.util.CookieUtils;
import com.huajie.util.DESTool;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AddClientUser implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IBbsUserDAO clientUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
    ISystemConfigDAO sysDAO = SpringHelper.getSpringBean("SystemConfigDAO");
    IBusinessUserDao businessUserDAO = SpringHelper.getSpringBean("BusinessUserDAO");
        
    List<SelectItem> uts = new ArrayList();
    BbsUser user = new BbsUser();
    Map deptMap = new HashMap();

    ExamDepartmentService dicDAO = SpringHelper.getSpringBean("ExamDepartmentService");
    List<DictionaryModel> dics = new ArrayList();
    DictionaryModel dic = new DictionaryModel();
    TreeNode root = new DefaultTreeNode();
    TreeNode selectedNode;
    private TreeNode[] selectedNodes;
    boolean testValidDate = true;

    String usernameTip = "";
    boolean usernameOk = true;

    String telTip = "";
    boolean telOk = true;
    
    String emailTip = "";
    boolean emailOk = true;
    
    String businessId;
    
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

    public String getUsernameTip() {
		return usernameTip;
	}

	public void setUsernameTip(String usernameTip) {
		this.usernameTip = usernameTip;
	}

	public boolean isUsernameOk() {
		return usernameOk;
	}

	public void setUsernameOk(boolean usernameOk) {
		this.usernameOk = usernameOk;
	}

	public String getTelTip() {
		return telTip;
	}

	public void setTelTip(String telTip) {
		this.telTip = telTip;
	}

	public boolean isTelOk() {
		return telOk;
	}

	public void setTelOk(boolean telOk) {
		this.telOk = telOk;
	}

	public BbsUser getUser() {
        return user;
    }

    public void setUser(BbsUser user) {
        this.user = user;
    }

    public List<DictionaryModel> getDics() {
        return dics;
    }

    public void setDics(List<DictionaryModel> dics) {
        this.dics = dics;
    }

    public boolean isTestValidDate() {
        return testValidDate;
    }

    public void setTestValidDate(boolean testValidDate) {
        this.testValidDate = testValidDate;
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

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public List<SelectItem> getUts() {
        return uts;
    }

    public void setUts(List<SelectItem> uts) {
        this.uts = uts;

    }

    public Map getDeptMap() {
        return deptMap;
    }

    public void setDeptMap(Map deptMap) {
        this.deptMap = deptMap;
    }

    @PostConstruct
    public void init() {
        //this.loadDepts();
    	this.businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        this.loadStructure();
    }

    public void loadDepts() {
        this.deptMap.clear();
        IExamDepartmentDAO deptDAO = SpringHelper.getSpringBean("ExamDepartmentDAO");
        List<ExamDepartment> depts = deptDAO.findAllExamDepartment(this.businessId);
        for (ExamDepartment e : depts) {
            this.deptMap.put(e.getName(), e.getName());
        }
    }

    private void loadStructure() {
        this.root = new DefaultTreeNode();
        DictionaryModel dm = dicDAO.findDictionaryModel(this.businessId,this.businessId);
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
                TreeNode t = new DefaultTreeNode(d, node);
                test(d, t);
            }
        }
    }

    /**
     * 根据部门设置的用户默认有效期改变用户的有效时间
     *
     * @param dmsx
     */
    private void testValidTime(List<DictionaryModel> dmsx) {
        if (this.testValidDate) {
            long days = 0;
            //找出所有部门中有效期最短的
            for (DictionaryModel dm : dmsx) {
                int dds = dm.getDefaultDays();
                if (days == 0) {
                    days = dds;
                } else if (days > dds) {
                    days = dds;
                }
            }
            if (days != 0) {
                long begain = user.getAvailableTime().getTime();
                begain += days * (24L * 60L * 60L * 1000L);
                user.setExpireTime(new Date(begain));
            }
        }
    }

    public String finish() {
//        if (this.selectedNode != null) {
//            DictionaryModel dm = (DictionaryModel) this.selectedNode.getData();
//            this.user.setGroupId(dm.getId().toString());
//        }
        
//        this.user.setGroupId(ExternalUserUtil.getAdminBySession().getId());
        //验证部门是否作了选择
        boolean departOk = true;
        if (this.selectedNodes == null) {
            departOk = false;
        } else {
            if (this.selectedNodes.length == 0) {
                departOk = false;
            }
        }
        List<DictionaryModel> dmsx = new ArrayList();
        //所选部门若为空，则设置其部门为默认部门，若默认部门为空，则提示用户必须选择一个部门
        StringBuilder sb = new StringBuilder();
        if (departOk) {
            for (TreeNode t : this.selectedNodes) {
                DictionaryModel dmm = (DictionaryModel) t.getData();
                sb.append(dmm.getId());
                sb.append(";");
                dmsx.add(dmm);
            }
            this.user.setGroupStr(sb.toString());
        } else {
            String departStr = this.dicDAO.findAllDefaultDepartmentStr();
            dmsx=this.dicDAO.findAllDefaultDepartments();
            if (departStr == null || "".equals(departStr)) {
                ApplicationBean ab = JsfHelper.getBean("applicationBean");
                JsfHelper.error("必须选择一个" + ab.getDepartmentStr() + "！");
                return null;
            } else {
                this.user.setGroupStr(departStr);
            }
        }
        
        this.testValidTime(dmsx);
        this.user.setGroupStr(sb.toString());
        FacesMessage fm = new FacesMessage();
        fm.setSeverity(FacesMessage.SEVERITY_ERROR);
        
        if ((this.user.getUsername().equals(""))){
        	 fm.setSummary("用户名不能为空");
             FacesContext.getCurrentInstance().addMessage("", fm);
             return null;
        }
        if ((this.user.getPassword().equals(""))){
       	 	fm.setSummary("密码不能为空");
            FacesContext.getCurrentInstance().addMessage("", fm);
            return null;
       }
        if ((this.user.getTel().equals(""))){
          	 fm.setSummary("手机号不能为空");
             FacesContext.getCurrentInstance().addMessage("", fm);
             return null;
          } 	
        
        if(this.usernameOk==false || this.telOk==false || this.emailOk==false){
        	 fm.setSummary("此手机号已存在");
             FacesContext.getCurrentInstance().addMessage("", fm);
             return null;
        }

        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (this.user.getRegIp() == null || this.user.getRegIp().equals("")) {
            try {
				this.user.setRegIp(NetworkUtil.getIpAddress(req));
			} catch (IOException e) {
				e.printStackTrace();
			}
            
        }
        //将密码变换
        ComplexBbsUserService cbus = SpringHelper.getSpringBean("ComplexBbsUserService");
        String pwd=user.getPassword();
        cbus.handlePwd(user);
        
        
        
        String pid = "";
        String email = "";
        String name = "";
        String gender = "";
        String qq = "";
        Date birthday = null;
        
        if(StringUtil.isNotEmpty(this.user.getPid())){
        	pid = this.user.getPid();
        }
        if(StringUtil.isNotEmpty(this.user.getEmail())){
        	email = this.user.getEmail();
        }
        if(StringUtil.isNotEmpty(this.user.getName())){
        	name = this.user.getName();
        }
        if(StringUtil.isNotEmpty(this.user.getGender())){
        	gender = this.user.getGender();
        }
        if(StringUtil.isNotEmpty(this.user.getQq())){
        	qq = this.user.getQq();
        }
        if(this.user.getBirthDay()!=null){
    		birthday = this.user.getBirthDay();
    	}
        //前期处理结束...
        
        List<BbsUser> bbsUserList = clientUserDAO.findBbsUserByPhones(this.user.getTel());
        BusinessUser openBussinessUser = businessUserDAO.findOpenBussinessUser();
        if(bbsUserList.isEmpty()){//公有私有都没有用户  新增操作
        	this.user.setId(UUID.randomUUID().toString());
			this.user.setBusinessId(this.businessId);
			if (StringUtils.isEmpty(this.user.getGroupStr())) {
				this.user.setGroupStr(this.businessId + ";");
			}
			clientUserDAO.addBbsUser(this.user); 
        	if(!openBussinessUser.getId().equals(this.businessId)){//如果在私有系统增加用户，则需要在公有系统再次添加了一条新数据
        		this.user.setId(UUID.randomUUID().toString());
    			this.user.setBusinessId(openBussinessUser.getId());
    			this.user.setGroupStr(openBussinessUser.getId()+ ";");
    			clientUserDAO.addBbsUser(this.user); 
        	}
        } else {
        	boolean isAdd = true;
        	for(BbsUser bu1 :bbsUserList){
        		if(StringUtil.isNotEmpty(pid)){
            		bu1.setPid(pid);
            	}
            	if(StringUtil.isNotEmpty(email)){
            		bu1.setEmail(email);
            	}
            	if(StringUtil.isNotEmpty(name)){
            		bu1.setName(name);
            	}
            	if(StringUtil.isNotEmpty(gender)){
            		bu1.setGender(gender);
            	}
            	if(StringUtil.isNotEmpty(qq)){
            		bu1.setQq(qq);
            	}
            	if(birthday!=null){
            		bu1.setBirthDay(birthday);
            	}
            	if(bu1.getBusinessId().equals(this.businessId)){//判断用户已经存在，直接修改
            		isAdd = false;
            	}
            	clientUserDAO.updateBbsUser(bu1);
        	}
        	if(isAdd){
        		this.user.setId(UUID.randomUUID().toString());
    			this.user.setBusinessId(this.businessId);
    			this.user.setGroupStr(this.businessId+ ";");
    			clientUserDAO.addBbsUser(this.user);
        	}
        }
        
//        for(BbsUser bu1:bbsUserList){
//        	if(StringUtil.isNotEmpty(pid)){
//        		bu1.setPid(pid);
//        	}
//        	if(StringUtil.isNotEmpty(email)){
//        		bu1.setEmail(email);
//        	}
//        	if(StringUtil.isNotEmpty(name)){
//        		bu1.setName(name);
//        	}
//        	if(StringUtil.isNotEmpty(gender)){
//        		bu1.setGender(gender);
//        	}
//        	if(StringUtil.isNotEmpty(qq)){
//        		bu1.setQq(qq);
//        	}
//        	if(birthday!=null){
//        		bu1.setBirthDay(birthday);
//        	}
//        	clientUserDAO.updateBbsUser(bu1);
//        }
//        
//        List<BusinessUser> publicList = businessUserDAO.findAllOpenBussinessUser();
//		BusinessUser businessUser = businessUserDAO.findBussinessUser(bussinessId);
//		if(businessUser != null){
//			if(businessUser.getIsOpen()==false){
//				publicList.add(businessUser);
//			}
//		}
//		
//		for(BusinessUser bus:publicList){
//			for(BbsUser bu2:bbsUserList){
//				if(bu2.getBusinessId().equals(bus.getId()) && bu2.getBusinessId().equals(bussinessId)){//已注册
//					publicList.remove(bus);
//				}
//			}
//		}
//        
//        for(BusinessUser busu:publicList){
////			BbsUser bbsu = new BbsUser();
//			this.user.setId(UUID.randomUUID().toString());
//			this.user.setBusinessId(busu.getId());
//			clientUserDAO.addBbsUser(this.user); 
//		}
        return "ListClientUser?faces-redirect=true";
    }
    
    public String onChangeUsername() {
        String username = this.user.getUsername().trim();
        if (username != null && !"".equals(username)) {
            BbsUser bu = this.clientUserDAO.findBbsUserByUrn(username);
            if (bu != null) {
                this.usernameOk = false;
                this.usernameTip = "此用户名已存在";
            } else {
                this.usernameOk = true;
                this.usernameTip = "此用户名可以添加";
            }
        } else{
        	this.usernameOk = false;
            this.usernameTip = "用户名不能为空";
        }
        return null;
    }
    
    public String onChangeTel() {
    	String phone = this.user.getTel().trim();
    	if (phone != null && !"".equals(phone)) {
    		if(Validator.isMobile(phone)){//是合格的手机号
    			BbsUser bu=clientUserDAO.findBbsUserByPhoneBusinessId(phone, businessId);
	            if ( bu!=null) {
	            	this.telOk = false;
	                this.telTip = "此手机号已存在";
            	} else {
	                this.telOk = true;
	                this.telTip = "此手机号可以注册";
	            }
    		}else{
    			this.telOk = false;
                this.telTip = "请填写正确的手机号";
    		}
        } else{
        	this.telOk = false;
            this.telTip = "手机号不能为空";
        }
    	return null;
    }

    public String onChangeUsername2() {
        String username = this.user.getUsername().trim();
        if (username != null && !"".equals(username)) {
        }
    	return null;
    }
    
    public String onChangeEmail2() {
    	String email = this.user.getEmail().trim();
    	if (email != null && !"".equals(email)) {
		}
    	return null;
    }
    
    public void up_action(FileUploadEvent event){
		try {
			UploadedFile item = event.getFile();
			String n = item.getFileName();
			int l2 = n.lastIndexOf(".");
			String ext = "." + n.substring(l2 + 1).toLowerCase();
			String imgId = Cat.getUniqueId();
			
			AdminNewFile.saveFile(item, ext,imgId);
			MyLogger.echo("upload executed.");
			String picUrl = "servlet/ShowImage?id=" + imgId;
			this.user.setPicUrl(picUrl);
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}
    


}
