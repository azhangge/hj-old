package com.hjedu.customer.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

import com.hjedu.course.dao.ICourseTypeDAO;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.dao.ITeacherDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.entity.Teacher;
import com.hjedu.customer.service.IThirdPartyUserService;
import com.hjedu.customer.service.impl.ComplexBbsUserService;
import com.hjedu.examination.dao.IExamDepartmentDAO;
import com.hjedu.examination.entity.CourseType;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.entity.ExamDepartment;
import com.hjedu.examination.service.impl.ExamDepartmentService;
import com.hjedu.platform.controller.AdminNewFile;
import com.hjedu.platform.controller.ApplicationBean;
import com.hjedu.platform.dao.IImgDAO;
import com.hjedu.platform.service.ILogService;
import com.hjedu.platform.service.impl.ImageService;
import com.huajie.app.util.Validator;
import com.huajie.util.Cat;
import com.huajie.util.CookieUtils;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AddTeacher implements Serializable {
	private static final long serialVersionUID = 1L;
	private Teacher teacher;
	boolean flag;
	TreeNode root = new DefaultTreeNode();
	private TreeNode[] selectedNodes;
	private List<BbsUser> uslist;
    private String userid;

    String bussinessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public List<BbsUser> getUslist() {
		return uslist;
	}

	public void setUslist(List<BbsUser> uslist) {
		this.uslist = uslist;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public TreeNode getRoot() {
		return this.root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}
	
	public TreeNode[] getSelectedNodes() {
		return this.selectedNodes;
	}

	public void setSelectedNodes(TreeNode[] selectedNodes) {
		this.selectedNodes = selectedNodes;
	}
	
	ITeacherDAO teacherDAO = SpringHelper.getSpringBean("TeacherDAO");
	
	IBbsUserDAO bbsDAO = SpringHelper.getSpringBean("BbsUserDAO");
	
	ICourseTypeDAO courseTypeDAO = (ICourseTypeDAO) SpringHelper.getSpringBean("CourseTypeDAO");
	
	@PostConstruct
    public void init() {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String id = req.getParameter("id");
        this.uslist=bbsDAO.findTeacherOpt(bussinessId);
        if (id != null) {
            this.teacher = this.teacherDAO.findTeacher(id);
            if(this.teacher.getUser()!=null){
            	this.userid=this.teacher.getUser().getId();
            }else{
            	this.userid="";
            }
            this.flag = true;
        }else{
        	this.teacher = new Teacher();
        }
        loadCourseTypes();
    }
	
	private void loadCourseTypes() {
		CourseType ct = this.courseTypeDAO.findCourseType(this.bussinessId);
		test(ct, this.root);
	}
	
	public void test(CourseType dd, TreeNode node) {
		String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
		boolean k = false;
		List<CourseType> ks = this.courseTypeDAO.loadAllDescendants(dd.getId(),businessId);
		for (CourseType ddm : ks) {
			if (testIfSelected(ddm)) {
				k = true;
				break;
			}
		}
		if (k) {
			node.setExpanded(true);
		}
		node.isLeaf();

		if (testIfSelected(dd)) {
			System.out.println(dd.getId() + dd.getName());
			node.setSelected(true);
			node.setExpanded(true);
		} else {
			node.setSelected(false);
		}

		List<CourseType> ls = this.courseTypeDAO.findAllSonCourseType(dd.getId(),businessId);
		Collections.sort(ls);
		dd.setSons(ls);
		if (ls.isEmpty()) {
			node.setSelectable(true);
			return;
		}
		node.setSelectable(false);
		for (CourseType d : ls) {
			TreeNode t = new DefaultTreeNode(d, node);
			test(d, t);
		}
	}
	
	private boolean testIfSelected(CourseType dd) {
		String str = "";
		if (this.teacher.getCourseTypeStr() != null) {
			str = this.teacher.getCourseTypeStr();
			if (str.contains(dd.getId())) {
				dd.setSelected(true);
				return true;
			}
		}
		return false;
	}
	
	public String finish(){
		if ((this.selectedNodes != null) && (this.selectedNodes.length > 0)) {
			int len = this.selectedNodes.length;
			String labStr = "";
			String typeStr = "";
			for (int i = 0; i < len; i++) {
				CourseType ct = (CourseType) this.selectedNodes[i].getData();
				if(ct.getFatherId().equals(this.bussinessId)){
					continue;
				}
				labStr = labStr + ct.getAncestors();
				typeStr = typeStr + ct.getId() + ";";
				this.teacher.setLabelStr(labStr);
				this.teacher.setCourseTypeStr(typeStr);
			}
		}
		this.teacher.setBussinessId(bussinessId);
		if(this.flag){
			BbsUser user=this.bbsDAO.findBbsUser(this.userid);
			if(user!=null){
				this.teacher.setUser(user);
				user.setTeacher(teacher);
				this.bbsDAO.updateBbsUser(user);
			}
			this.teacherDAO.updateTeacher(teacher);
		}else{
			BbsUser user=this.bbsDAO.findBbsUser(this.userid);
			if(user!=null){
				this.teacher.setUser(user);
				user.setTeacher(teacher);
				this.bbsDAO.updateBbsUser(user);
				this.teacherDAO.updateTeacher(teacher);
			}else{
				this.teacherDAO.addTeacher(teacher);
			}
		}
		return "ListTeacher?faces-redirect=true";
	}
	
	public void up_action(FileUploadEvent event){
		try {
			UploadedFile item = event.getFile();
			String n = item.getFileName();
			int l2 = n.lastIndexOf(".");
			String ext = "." + n.substring(l2 + 1).toLowerCase();
			String imgId = Cat.getUniqueId();
			AdminNewFile.saveFile(item, ext,imgId);
			String picUrl = "servlet/ShowImage?id=" + imgId;
			this.teacher.setPicture(picUrl);
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}
}
