package com.hjedu.course.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

import com.hjedu.course.dao.ICourseTypeDAO;
import com.hjedu.examination.entity.CourseType;
import com.hjedu.examination.service.ComplexExamModuleLogic;
import com.hjedu.platform.controller.AdminNewFile;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.Cat;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class CourseTypeList implements Serializable {
	private static final long serialVersionUID = 1L;
	ComplexExamModuleLogic complex = SpringHelper.getSpringBean("ComplexExamModuleLogic");
	ILogService logger = SpringHelper.getSpringBean("LogService");
	ICourseTypeDAO CourseTypeDAO = SpringHelper.getSpringBean("CourseTypeDAO");
    boolean flag = false;
    CourseType courseType = new CourseType();
    List<SelectItem> ss = new ArrayList<>();
    TreeNode root;
    TreeNode root3 = new DefaultTreeNode();
    private TreeNode[] selectedNodes;
    List<CourseType> modules = new LinkedList<>();
    private List<TreeNode> nodes = new LinkedList<>();
    String businessId;

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public CourseType getCourseType() {
		return courseType;
	}

	public void setCourseType(CourseType courseType) {
		this.courseType = courseType;
	}

	public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public TreeNode getRoot3() {
        return root3;
    }

    public void setRoot3(TreeNode root3) {
        this.root3 = root3;
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public List<TreeNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<TreeNode> nodes) {
        this.nodes = nodes;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public List<SelectItem> getSs() {
        return ss;
    }

    public void setSs(List<SelectItem> ss) {
        this.ss = ss;
    }

    @PostConstruct
    public void init() {
       this.businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
       this.loadStructure();
    }

    public void buildTreeNode(CourseType ct, TreeNode node) {
        node.setExpanded(false);
        List<CourseType> cts =  CourseTypeDAO.findAllSonCourseType(ct.getId(),this.businessId);
        Collections.sort(cts);
        ct.setSons(cts);
        if (cts.isEmpty()) {
            return;
        } else {
            for (CourseType cty : cts) {
                TreeNode t = new DefaultTreeNode(cty, node);
                buildTreeNode(cty, t);
            }
        }
    }
    
    public void addType(){
    	//新增
        this.flag = false;
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String fid = req.getParameter("fid");
        Integer typeLevel =  Integer.valueOf(req.getParameter("typeLevel"));
        if (fid != null) {
        	this.courseType = new CourseType();
        	courseType.setTypeLevel(++typeLevel);
        	courseType.setFatherId(fid);
            System.out.println("add begin.fid:" + fid);
        }
    }
    
    public void delCt(String id){
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    	CourseType ct = CourseTypeDAO.findCourseType(id);
        logger.log("删除了课程分类：" + ct.getName());
        CourseTypeDAO.deleteCourseTypeAndAllDescendants(id,businessId);
        this.loadStructure();
        this.refreshChangePanel();
    }
    
    public void editCt(String id){
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    	this.flag = true;
//        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//        String id = req.getParameter("id");
        if (id != null) {
            this.courseType = CourseTypeDAO.findCourseType(id);
            System.out.println("edit begain.id:" + id);
        }
    }
    
    public void addOrEditOk(){
        String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        if (!flag) {
            ChangeTypeDir lcf = JsfHelper.getBean("changeTypeDir");
            courseType.setAncestors(lcf.getAncestors(courseType));
            courseType.setBusinessId(CookieUtils.getBusinessId(JsfHelper.getRequest()));
            courseType.setBusinessId(businessId);
            this.CourseTypeDAO.addCourseType(courseType);
            logger.log("添加了课程分类：" + this.courseType.getName());
        } else {
            this.CourseTypeDAO.updateCourseType(courseType);
            logger.log("修改了课程分类：" + this.courseType.getName());
        }
        this.loadStructure();
        this.refreshChangePanel();
    }
    
    /**
     * 更新 移动 模块时显示的目录结构
     */
    private void refreshChangePanel() {
    	ChangeTypeDir change = JsfHelper.getBean("changeTypeDir");
        if (change != null) {
            change.init();
        }
    }

	public void loadStructure() {
		root = new DefaultTreeNode();
		CourseType rootCT = CourseTypeDAO.findCourseType(this.businessId);
		buildTreeNode(rootCT, root);
	}
	

    public List<CourseType> fetchModules() {
        this.modules.clear();
        this.getSelectedDataList(this.modules, root);
        return this.modules;
    }
    
    /**
     * 获取root节点下的所有被选中的子数据
     * @param ms 存放数据的list
     * @param root 
     */
    public void getSelectedDataList(List<CourseType> ms, TreeNode root) {
        List<TreeNode> ls = root.getChildren();
        if (ls.isEmpty()) {
            return;
        } else {
            for (TreeNode d : ls) {
            	CourseType em = (CourseType) d.getData();
                if (em.isSelected()) {
                    ms.add(em);
                }
                getSelectedDataList(ms, d);
            }
        }
    }
    
    public void up_action(FileUploadEvent event) {
		try {
			UploadedFile item = event.getFile();
			String n = item.getFileName();
			int l2 = n.lastIndexOf(".");
			String ext = "." + n.substring(l2 + 1).toLowerCase();
			String imgId = Cat.getUniqueId();
			
			AdminNewFile.saveFile(item, ext,imgId);
			MyLogger.echo("upload executed.");
			String picUrl = "servlet/ShowImage?id=" + imgId;
			this.courseType.setPicture(picUrl);
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}
}
