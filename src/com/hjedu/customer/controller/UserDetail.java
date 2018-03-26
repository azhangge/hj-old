package com.hjedu.customer.controller;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.json.JSONObject;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.service.impl.ComplexBbsUserService;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.service.impl.ExamDepartmentService;
import com.hjedu.platform.controller.AdminNewFile;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.dao.IDictCityDAO;
import com.hjedu.platform.dao.IDictProvinceDAO;
//import com.hjedu.platform.dao.IImgDAO;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.entity.DictCity;
import com.hjedu.platform.entity.DictProvince;
import com.huajie.app.util.FileUtil;
import com.huajie.app.util.StringUtil;
import com.huajie.corss.util.Conn;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class UserDetail implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	BbsUser usr = null;
    String pwd_temp = "";
    String pwd_n1;
    String pwd_n2;
    String okScript;
    private IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
//    private IImgDAO imgDAO = SpringHelper.getSpringBean("ImgDAO");
    List<SelectItem> provs = new ArrayList<SelectItem>();
    List<SelectItem> citys = new ArrayList<SelectItem>();
    IDictCityDAO cityDAO = SpringHelper.getSpringBean("DictCityDAO");
    IDictProvinceDAO provinceDAO = SpringHelper.getSpringBean("DictProvinceDAO");
    ISystemConfigDAO scd = SpringHelper.getSpringBean("SystemConfigDAO");
    ISystemConfigDAO scDAO = SpringHelper.getSpringBean("SystemConfigDAO");
    String constants_sub_id=this.scDAO.getSystemConfig().getSub_id();
    String yun_host=this.scDAO.getSystemConfig().getYun_host();
    
    FileUpload fu = new FileUpload();
    int province = 10;
    int city = 1;
    TreeNode root = new DefaultTreeNode();
    ExamDepartmentService dicDAO = SpringHelper.getSpringBean("ExamDepartmentService");
    List<DictionaryModel> dics = new ArrayList<DictionaryModel>();
    private TreeNode selectedNode;
    private TreeNode[] selectedNodes;

    /**
     * 默认图像序号
     */
    private int default_image = 0;

    String businessId;
//	private int year;
//    private int month;
//    private int day;
//    private int week;
    
    String bussinessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    
	public String getYear(){
    	Calendar cal = Calendar.getInstance();
    	String year = cal.get(Calendar.YEAR) + "";
    	return year; 
    }
	
	public String getMonth(){
		Calendar cal = Calendar.getInstance();
		String month = cal.get(Calendar.MONTH) + 1 + "";
		if(month.length()==1){
			month = "0" + month;
		}
		return month; 
	}
	
	public String getDay(){
		Calendar cal = Calendar.getInstance();
		String day = cal.get(Calendar.DAY_OF_MONTH) + "";
		if(day.length()==1){
			day = "0" + day;
		}
		return day; 
	}
	
	public String getWeek(){
		Calendar cal = Calendar.getInstance();
		String week = cal.get(Calendar.WEEK_OF_YEAR) + ""; 
		if(week.length()==1){
			week = "0" + week;
		}
		return week;
	}
    
    public int getDefault_image() {
		return default_image;
	}

	public void setDefault_image(int default_image) {
		this.default_image = default_image;
	}

	public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
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

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
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
//        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//        String temp = request.getParameter("id");
//        if (temp != null) {
//            this.usr = userDAO.findBbsUser(temp.trim());
//            this.usr = UserUtil.getBbsUser(temp.trim());
//        }
    	this.businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
		ClientSession cs = JsfHelper.getBean("clientSession");
		if (cs != null) {
			BbsUser user = cs.getUsr();
			if(user!=null){
				this.usr = user;
			}
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
        List<DictCity> dc = this.cityDAO.findByNProvid(this.province, 0, 1000);
        for (DictCity c : dc) {
            SelectItem s = new SelectItem(c.getNCityid(), c.getSCityname());
            this.citys.add(s);
        }
        this.loadStructure();
    }

    private boolean testIfSelected(DictionaryModel dd) {
        String str = "";
        if (StringUtil.isNotEmpty(this.usr.getGroupStr())) {
            str = this.usr.getGroupStr();
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
        DictionaryModel dm = dicDAO.findDictionaryModel(this.businessId,bussinessId);
        test(dm, root);
        this.dics = null;
        this.dics = dm.getSons();

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
                if (d.getFrontShow()) {//测试是否要在前台显示
                    TreeNode t = new DefaultTreeNode(d, node);
                    test(d, t);
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

    public void up_action(FileUploadEvent event) {
        try {
            UploadedFile item = event.getFile();
            String n = item.getFileName();
            int l2 = n.lastIndexOf(".");
            String ext = "." + n.substring(l2 + 1).toLowerCase();
            if(!ext.equals(".jpg") && !ext.equals(".jpeg") && !ext.equals(".png") && !ext.equals(".gif")){
           	 JsfHelper.error("不符合的图片类型！");
		    	 return;
           }
           if(item.getSize() >1 * 1024 * 1024F){
           	JsfHelper.error("附件超过限制大小1M！");
		    	 return;
           }
            String imgId = UUID.randomUUID().toString();
            MyLogger.echo("upload executed.");
            AdminNewFile.saveFile(item, ext, imgId);
            String picUrl = "servlet/ShowAbstractImage?id=" + imgId;
            this.usr.setPicUrl(picUrl);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public void up_action2(FileUploadEvent event) {
        try {
            UploadedFile item = event.getFile();
            String n = item.getFileName();
            int l2 = n.lastIndexOf(".");
            String ext = "." + n.substring(l2 + 1).toLowerCase();
            if(!ext.equals(".jpg") && !ext.equals(".jpeg") && !ext.equals(".png") && !ext.equals(".gif")){
            	 JsfHelper.error("不符合的图片类型！");
		    	 return;
            }
            if(item.getSize() >1 * 1024 * 1024F){
            	JsfHelper.error("附件超过限制大小1M！");
		    	 return;
            }
            String imgId = UUID.randomUUID().toString();
            MyLogger.echo("upload executed.");
            
            String ext2=n.substring(l2 + 1).toLowerCase();
            byte[] buf=item.getContents();
            FileUtil.byte2File(buf, "C:\\ucenter_file\\temp", imgId+ext);
            File file=new File("C:\\ucenter_file\\temp\\"+imgId+ext);
            
            if(file.exists()){
            	Map<String,String> textMap=new HashMap<String,String>();
                textMap.put("user_id", this.usr.getExternalId());
                textMap.put("imgId", imgId);
                textMap.put("ext", ext2);
                JSONObject jsonuploadUserPicture=Conn.uploadUserPicture(yun_host,textMap,file);
                String status=(String) jsonuploadUserPicture.get("status");
                if(status.equals("1")){
                	AdminNewFile.saveFile(item, ext, imgId);
                    String picUrl =yun_host+ "/corss/showUserPicture?id=" + imgId+"&type="+ext2;
                    this.usr.setPicUrl(picUrl);
                    this.default_image=100;
		        }
		       if(status.equals("2")){
		    	   JsfHelper.error("系统没有获取到正确的参数");
		    	   return;
		       }
		       if(status.equals("3")){
		    	   JsfHelper.error("没有此用户");
		    	   return;
		       }
		       if(status.equals("0")){
		    	   JsfHelper.error("云服务器异常");
		    	   return;
		       }
            }
            
        } catch (Exception ee) {
            ee.printStackTrace();
            JsfHelper.error("云服务器异常");
        }
    }

    public void up_action3(FileUploadEvent event) {
        try {
            UploadedFile item = event.getFile();
            String n = item.getFileName();
            int l2 = n.lastIndexOf(".");
            String ext = "." + n.substring(l2 + 1).toLowerCase();
            if(!ext.equals(".jpg") && !ext.equals(".jpeg") && !ext.equals(".png") && !ext.equals(".gif")){
            	 JsfHelper.error("不符合的图片类型！");
		    	 return;
            }
            if(item.getSize() >1 * 1024 * 1024F){
            	JsfHelper.error("附件超过限制大小1M！");
		    	 return;
            }
            String imgId = UUID.randomUUID().toString();
            MyLogger.echo("upload executed.");
            
            AdminNewFile.saveFile(item, ext, imgId);
            String picUrl = "servlet/ShowAbstractImage?id=" + imgId;
            this.usr.setPicUrl(picUrl);
            this.default_image=100;
            
//            UploadedFile item = event.getFile();
//            String n = item.getFileName();
//            int l2 = n.lastIndexOf(".");
//            String ext = "." + n.substring(l2 + 1).toLowerCase();
//            String imgId = UUID.randomUUID().toString();
//            MyLogger.echo("upload executed.");
//            AdminNewFile.saveFile(item, ext, imgId);
//            String picUrl = "servlet/ShowAbstractImage?id=" + imgId;
//            this.usr.setPicUrl(picUrl);

            
        } catch (Exception ee) {
            ee.printStackTrace();
            JsfHelper.error("上传异常");
        }
    }
    
    public String saveUserChange() {

        if (scd.getSystemConfig().isAllowChgDept()) {//若允许前台用户修改密码，则验证修改
            //验证部门是否作了选择
            boolean departOk = true;
            if (this.selectedNodes == null) {
                departOk = false;
            } else {
                if (this.selectedNodes.length == 0) {
                    departOk = false;
                }
            }
            //所选部门若为空，则设置其部门为默认部门，若默认部门为空，则提示用户必须选择一个部门
            StringBuilder sb = new StringBuilder();
            if (departOk) {
                for (TreeNode t : this.selectedNodes) {
                    DictionaryModel dmm = (DictionaryModel) t.getData();
                    sb.append(dmm.getId());
                    sb.append(";");
                }
                this.usr.setGroupStr(sb.toString());
            }

        }
        //判断密码的情况
//        boolean notOk = false;

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
        
        if(this.usr.getPicUrl()!=null){//有数据
        	if(this.default_image!=0 && this.default_image!=100){//操作过
                	this.usr.setPicUrl("../image/user_img_big_"+this.default_image+".png");
        	}
        }else{//没有数据
        	if(this.default_image!=100){
        			this.usr.setPicUrl("../image/user_img_big_"+this.default_image+".png");
        	}
        }
        
        ClientSession cs = JsfHelper.getBean("clientSession");
        cs.setUsr(usr);
        
        this.userDAO.updateBbsUser(this.usr);
        JsfHelper.info("修改完成！");
        

        this.setOkScript("");
        return null;
    }
    
    public String saveUserChange2() {
        if (scd.getSystemConfig().isAllowChgDept()) {//若允许前台用户修改密码，则验证修改
            //验证部门是否作了选择
            boolean departOk = true;
            if (this.selectedNodes == null) {
                departOk = false;
            } else {
                if (this.selectedNodes.length == 0) {
                    departOk = false;
                }
            }
            //所选部门若为空，则设置其部门为默认部门，若默认部门为空，则提示用户必须选择一个部门
            StringBuilder sb = new StringBuilder();
            if (departOk) {
                for (TreeNode t : this.selectedNodes) {
                    DictionaryModel dmm = (DictionaryModel) t.getData();
                    sb.append(dmm.getId());
                    sb.append(";");
                }
                this.usr.setGroupStr(sb.toString());
            }
        }

        FacesMessage fm = new FacesMessage();
        fm.setSeverity(FacesMessage.SEVERITY_ERROR);

        this.usr.setProvince(this.provinceDAO.findById(this.province));
        this.usr.setCity(this.cityDAO.findById(this.city));
        
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String businessId = CookieUtils.getBusinessId(request);
        if(this.usr.getPicUrl()!=null){//有数据
        	if(this.default_image!=0 && this.default_image!=100){//操作过
        		this.usr.setPicUrl(FileUtil.getImageURLByIdAndBusinessId("user_img_big_"+this.default_image,"png",businessId)); 
        	}
        }else{//没有数据
        	if(this.default_image!=100){
        		this.usr.setPicUrl(FileUtil.getImageURLByIdAndBusinessId("user_img_big_"+this.default_image,"png",businessId)); 
        	}
        }
        
        ClientSession cs = JsfHelper.getBean("clientSession");
        cs.setUsr(this.usr);
       
        this.userDAO.updateBbsUser(this.usr);
    	JsfHelper.info("修改完成！");

        //第三方平台修改
//        Boolean ifLDAP = Boolean.parseBoolean(SpringHelper.getSpringBean("usercheck_thirdParty").toString());
//        if (ifLDAP) {//使用第三方验证的情况
//            IThirdPartyUserService third=SpringHelper.getSpringBean("ThirdPartyUserService");
//            third.edit(usr.getUsername(),null, this.pwd_n1, usr.getEmail(), 1, null, null);
//        }
        this.setOkScript("");
        return null;
    }
}
