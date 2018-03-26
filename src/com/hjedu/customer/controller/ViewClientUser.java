package com.hjedu.customer.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.json.JSONObject;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

import com.hjedu.businessuser.dao.IBusinessUserDao;
import com.hjedu.businessuser.entity.BusinessUser;
import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.dao.ILessonTypeLogDAO;
import com.hjedu.course.entity.LessonType;
import com.hjedu.course.entity.LessonTypeLog;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.entity.UserInfo;
import com.hjedu.customer.service.IThirdPartyUserService;
import com.hjedu.customer.service.impl.ComplexBbsUserService;
import com.hjedu.customer.service.impl.FinanceService;
import com.hjedu.examination.dao.IDictionaryDAO;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.service.impl.ExamDepartmentService;
import com.hjedu.platform.controller.AdminNewFile;
import com.hjedu.platform.controller.ApplicationBean;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.service.IBbsScoreLogService;
import com.hjedu.platform.service.ILogService;
import com.huajie.app.util.DateUtil;
import com.huajie.app.util.HttpClientUtil;
import com.huajie.app.util.StringUtil;
import com.huajie.app.util.Validator;
import com.huajie.corss.model.SubUser;
import com.huajie.corss.util.Conn;
import com.huajie.corss.util.ObjectConvent;
import com.huajie.util.Cat;
import com.huajie.util.CookieUtils;
import com.huajie.util.DESTool;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ViewClientUser implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
    ISystemConfigDAO scDAO = SpringHelper.getSpringBean("SystemConfigDAO");
    FinanceService financeService = SpringHelper.getSpringBean("FinanceService");
    IBbsScoreLogService logService = SpringHelper.getSpringBean("BbsScoreLogService");
    ILessonTypeDAO lessonTypeDAO = (ILessonTypeDAO) SpringHelper.getSpringBean("LessonTypeDAO");
    ILessonTypeLogDAO lessonTypeLogDAO = SpringHelper.getSpringBean("LessonTypeLogDAO");
    IBusinessUserDao businessUserDAO = SpringHelper.getSpringBean("BusinessUserDAO");
    
    ComplexBbsUserService userService = SpringHelper.getSpringBean("ComplexBbsUserService");
    //SelectsDAO ss = SpringHelper.getSpringBean("selectsDAO");
    HttpServletRequest request = JsfHelper.getRequest();
    BbsUser cu;
    List<SelectItem> depts = new ArrayList();
    List<SelectItem> uts = new ArrayList();
    ExamDepartmentService dicDAO = SpringHelper.getSpringBean("ExamDepartmentService");
    List<DictionaryModel> dics = new ArrayList();
    DictionaryModel dic = new DictionaryModel();
    TreeNode root = new DefaultTreeNode();
    TreeNode selectedNode;
    private TreeNode[] selectedNodes;
    String scoreOperator = "plus";
    String moneyOperator = "plus";
    long score = 0;
    double money = 0;
    String id = this.request.getParameter("id");
    
    String businessId;
    
    boolean testValidDate = false;

    String telTip = "";
    boolean telOk = true;

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public BbsUser getCu() {
        return this.cu;
    }

    public void setCu(BbsUser cu) {
        this.cu = cu;
    }

    public List<SelectItem> getDepts() {
        return depts;
    }

    public void setDepts(List<SelectItem> depts) {
        this.depts = depts;
    }

    public boolean isTestValidDate() {
        return testValidDate;
    }

    public void setTestValidDate(boolean testValidDate) {
        this.testValidDate = testValidDate;
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

    public String getScoreOperator() {
        return scoreOperator;
    }
    
    public void setScoreOperator(String scoreOperator) {
        this.scoreOperator = scoreOperator;
    }
    
    public long getScore() {
        return score;
    }
    
    public void setScore(long score) {
        this.score = score;
    }
    
    public String getMoneyOperator() {
        return moneyOperator;
    }
    
    public void setMoneyOperator(String moneyOperator) {
        this.moneyOperator = moneyOperator;
    }
    
    public double getMoney() {
        return money;
    }
    
    public void setMoney(double money) {
        this.money = money;
    }
    
    
    @PostConstruct
    public void init() {
    	this.businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        this.cu = bbsUserDAO.findBbsUser(this.id);

        this.loadStructure();
    }

    private boolean testIfSelected(DictionaryModel dd) {
        String str = "";
        if (cu!=null&&this.cu.getGroupStr() != null) {
            str = this.cu.getGroupStr();
        }
        if (str.contains(dd.getId())) {
            dd.setSelected(true);
            return true;
        } else {
            return false;
        }
    }

    private void loadStructure() {
        this.root = new DefaultTreeNode();
        DictionaryModel dm = dicDAO.findDictionaryModel(this.businessId,this.businessId);
        this.dics = null;
        if (dm != null) {
			test(dm, root);
			this.dics = dm.getSons();
		}

    }

    public void test(DictionaryModel dd, TreeNode node) {
        //检查，如果某节点的所有子节点中只要有节点被选中，此节点就应该展开
        boolean k = false;
        List<DictionaryModel> ks = dicDAO.loadAllDescendants(dd.getId());
        for (DictionaryModel ddm : ks) {
            if (this.testIfSelected(ddm)) {
                k = true;
                break;
            }
        }
        if (k) {
            node.setExpanded(true);
        }
        if (node.isLeaf()) {

        } else {

        }
        //判断是否应该展开完毕
        //判断是否应该选中,若选中，也要展开（因为可能有子元素未选中，而父元素选中的情况）
        if (this.testIfSelected(dd)) {
            System.out.println(dd.getId() + dd.getName());
            node.setSelected(true);
            node.setExpanded(true);
        } else {
            node.setSelected(false);
        }
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

    /**
     * 根据部门设置的用户默认有效期改变用户的有效时间
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
                long begain = this.cu.getAvailableTime().getTime();
                begain += days * (24L * 60L * 60L * 1000L);
                cu.setExpireTime(new Date(begain));
            }
        }
    }

    public String initPwd() {
        String pwd = scDAO.getSystemConfig().getInitPwd();
//        this.cu.setPassword(pwd);
//        this.userService.handlePwd(cu);
 //       this.BbsUserDAO.updateBbsUser(this.cu);
        
        String id = this.cu.getId();
        BbsUser user = bbsUserDAO.findBbsUser(id);
        DESTool dt = new DESTool();
        String pw = dt.encrypt(pwd);
        user.setPassword(pw);
        bbsUserDAO.updateBbsUser(user);
        JsfHelper.info("初始化密码完成！");
        this.logger.log("初始了前台用户密码");
        return null;
    }

    public String finish() {
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
        	DictionaryModel dm0 = null;
            for (TreeNode t : this.selectedNodes) {
                DictionaryModel dmm = (DictionaryModel) t.getData();
                dm0 = dmm;
                sb.append(dmm.getId());
                sb.append(";");
                dmsx.add(dmm);
            }
            if(dm0.getAdmins()!=null&&dm0.getAdmins().size()>0)
            this.cu.setGroupId(dm0.getAdmins().get(0).getId());
            this.cu.setGroupStr(sb.toString());
        } else {
            String departStr = this.dicDAO.findAllDefaultDepartmentStr();
            dmsx=this.dicDAO.findAllDefaultDepartments();
            if (departStr == null || "".equals(departStr)) {
                ApplicationBean ab = JsfHelper.getBean("applicationBean");
                JsfHelper.error("必须选择一个" + ab.getDepartmentStr() + "！");
                return null;
            } else {
                this.cu.setGroupStr(departStr);
            }
        }
        
        this.testValidTime(dmsx);
        this.cu.setGroupStr(sb.toString());

        
        String pid = "";
        String email = "";
        String name = "";
        String gender = "";
        String qq = "";
        Date birthday = null;
       
    	if(StringUtil.isNotEmpty(this.cu.getPid())){
    		pid = this.cu.getPid();
    	}
    	if(StringUtil.isNotEmpty(this.cu.getEmail())){
    		email = this.cu.getEmail();
    	}
    	if(StringUtil.isNotEmpty(this.cu.getName())){
    		name = this.cu.getName();
    	}
    	if(StringUtil.isNotEmpty(this.cu.getGender())){
    		gender = this.cu.getGender();
    	}
    	if(StringUtil.isNotEmpty(this.cu.getQq())){
    		qq = this.cu.getQq();
    	}
    	if(this.cu.getBirthDay()!=null){
    		birthday = this.cu.getBirthDay();
    	}

    	bbsUserDAO.updateBbsUser(this.cu);
    	
    	List<BbsUser> bbsUserList = bbsUserDAO.findBbsUserByPhones(this.cu.getTel());
        for(BbsUser bu1:bbsUserList){
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
        	bbsUserDAO.updateBbsUser(bu1);
        }

        
        
        this.logger.log("修改了手机号为：(" + this.cu.getTel()+")的前台用户");
        return "ListClientUser?faces-redirect=true";
    }

    
    public String onChangeTel() {
    	String phone = this.cu.getTel().trim();
    	if(Validator.isMobile(phone)){//是合格的手机号
    		BbsUser bu = bbsUserDAO.findBbsUserByPhoneBusinessId(phone, this.businessId);
    		if(bu!=null){
    			this.telOk = false;
    			this.telTip = "此手机号已存在";
    		}else{
    			this.telOk = true;
    			this.telTip = "此手机号可以正常录入";
    		}
    	}else{
    		this.telOk = false;
            this.telTip = "请填写正确的手机号";
    	}
    	return null;	
    }
    
    public void SetSecretedLessonTypeInUser(BbsUser bu){
    	List<LessonType> slt = lessonTypeDAO.findAllSecretedLessonType();
    	String uGroupStr = bu.getGroupStr();
    	String uLessonTypeStr = bu.getLessonTypeStr();
    	String[] UserDeptStr = null;
    	Set<String> UserDeptSet = null;//用户部门
    	if(uGroupStr!=null){
    		UserDeptStr = bu.getGroupStr().split(";");
    		UserDeptSet =  new HashSet<>(Arrays.asList(UserDeptStr));
    	}
    	Set<LessonType> lessonTypeSet = new HashSet<LessonType>();
    	for(LessonType lt:slt){
    		String lGroupStr = lt.getGroupStr();
    		if(lGroupStr!=null){
    			String[] LessonTypeGroupStr = lGroupStr.split(";");
    			if(LessonTypeGroupStr!=null){
    				Set<String> LessonTypeGroupSet = new HashSet<>(Arrays.asList(LessonTypeGroupStr));//课程部门
        			for(String str:LessonTypeGroupSet){
        				if(UserDeptSet.contains(str)){
        					lessonTypeSet.add(lt);
        				}
        			}
        		}
    		}
    	}
    	for(LessonType lt:lessonTypeSet){
    		if(!uLessonTypeStr.contains(lt.getId())){
    			String str = bu.getLessonTypeStr();
    			str = str + lt.getId()+";";
    			bu.setLessonTypeStr(str);
    			bbsUserDAO.updateBbsUser(bu);
        	}
    		 LessonTypeLog ltl = lessonTypeLogDAO.findLessonTypeLogByTypeAndUsr(lt.getId(), bu.getId());	  
      		 if(ltl==null){
      			LessonTypeLog lessonTypeLog = new LessonTypeLog(lt,bu);
      			lessonTypeLog.setBusinessId(this.businessId);
    			this.lessonTypeLogDAO.addLessonTypeLog(lessonTypeLog);
      		 }
    	}
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
			this.cu.setPicUrl(picUrl);
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}
    
    
    public void synUser2(long netAdd) {
    	if(this.cu.getScore()+netAdd>=0){
    		this.cu.setScore(this.cu.getScore()+netAdd);
    	}else{
    		this.cu.setScore(0);
    	}
    }
    
    public void operateScore() {
        long netAdd = 0;
        if (!"plus".equalsIgnoreCase(scoreOperator)) {
            score = -score;
            netAdd = score;
        } else {
            netAdd = score;
        }
//        this.logService.log("管理员加减积分", score, cu);
//       logger.log("为用户：" + cu.getUsername() + " 增加了积分：" + netAdd);
        this.synUser2(netAdd);
        score = 0;
    }
}
