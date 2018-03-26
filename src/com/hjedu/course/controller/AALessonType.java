package com.hjedu.course.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

import com.hjedu.course.dao.ICourseTypeDAO;
import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.dao.ILessonTypeLogDAO;
import com.hjedu.course.entity.LessonType;
import com.hjedu.course.entity.LessonTypeLog;
import com.hjedu.customer.dao.IAdminDAO;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.dao.ITeacherDAO;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.entity.Teacher;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.CourseType;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.service.impl.ExamDepartmentService;
import com.hjedu.platform.controller.AdminNewFile;
import com.hjedu.platform.dao.IImgDAO;
import com.hjedu.platform.service.ILogService;
import com.huajie.app.util.StringUtil;
import com.huajie.util.Cat;
import com.huajie.util.CookieUtils;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AALessonType implements Serializable {
	ILogService logger = (ILogService) SpringHelper.getSpringBean("LogService");
	ITeacherDAO teacherDAO = SpringHelper.getSpringBean("TeacherDAO");
	ILessonTypeDAO lessonTypeDAO = (ILessonTypeDAO) SpringHelper.getSpringBean("LessonTypeDAO");
	IExaminationDAO examDAO = (IExaminationDAO) SpringHelper.getSpringBean("ExaminationDAO");
	ICourseTypeDAO courseTypeDAO = (ICourseTypeDAO) SpringHelper.getSpringBean("CourseTypeDAO");
	IAdminDAO adminDAO = (IAdminDAO) SpringHelper.getSpringBean("AdminDAO");
	IImgDAO imgDAO = (IImgDAO) SpringHelper.getSpringBean("ImgDAO");
	ExamDepartmentService dicDAO = SpringHelper.getSpringBean("ExamDepartmentService");
	IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
	ILessonTypeLogDAO lessonTypeLogDAO = SpringHelper.getSpringBean("LessonTypeLogDAO");
	
	List<Examination> exams = new LinkedList<>();
	List<AdminInfo> admins = new LinkedList<>();
	LessonType lessonType = null;
	boolean flag = false;
	//主要用于设置课程类型
	TreeNode root = new DefaultTreeNode();
	private TreeNode[] selectedNodes;
	
	private List<Teacher> teachers;
	private String teacherId;

	//主要用于设置开放的部门
    TreeNode root2 = new DefaultTreeNode();
    private TreeNode[] selectedNodes2;
    String businessId; 
    
	public TreeNode[] getSelectedNodes2() {
		return selectedNodes2;
	}

	public void setSelectedNodes2(TreeNode[] selectedNodes2) {
		this.selectedNodes2 = selectedNodes2;
	}

	public TreeNode getRoot2() {
		return root2;
	}

	public void setRoot2(TreeNode root2) {
		this.root2 = root2;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public List<Teacher> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<Teacher> teachers) {
		this.teachers = teachers;
	}

	public TreeNode[] getSelectedNodes() {
		return this.selectedNodes;
	}

	public void setSelectedNodes(TreeNode[] selectedNodes) {
		this.selectedNodes = selectedNodes;
	}

	public TreeNode getRoot() {
		return this.root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public boolean isFlag() {
		return this.flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public List<Examination> getExams() {
		return this.exams;
	}

	public void setExams(List<Examination> exams) {
		this.exams = exams;
	}

	public LessonType getLessonType() {
		return this.lessonType;
	}

	public void setLessonType(LessonType lessonType) {
		this.lessonType = lessonType;
	}

	public List<AdminInfo> getAdmins() {
		return this.admins;
	}

	public void setAdmins(List<AdminInfo> admins) {
		this.admins = admins;
	}

	@PostConstruct
	public void init() {
		
		HttpServletRequest request = JsfHelper.getRequest();
		this.businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
		String id = request.getParameter("id");
		if (id != null) {
			this.flag = true;
			this.lessonType = this.lessonTypeDAO.findLessonType(id);
		} else {
			this.lessonType = new LessonType();
		}
		this.teachers = teacherDAO.findAllTeacher(this.businessId);
		if(this.lessonType.getTeacher()!=null)
		this.teacherId = this.lessonType.getTeacher().getId();

		this.loadExams();
		this.loadAdmins();
		this.loadDepartment();
		this.loadCourseTypes();
	}

	private void loadDepartment() {
        DictionaryModel dm = this.dicDAO.findDictionaryModel(this.businessId,this.businessId);
        test2(dm, this.root2);
	}
	
	private void loadCourseTypes() {
		CourseType ct = this.courseTypeDAO.findCourseType(this.businessId);
		test(ct, this.root);
	}

	public void test2(DictionaryModel dd, TreeNode node) {
		boolean k2 = false;
		List<DictionaryModel> ks = this.dicDAO.loadAllDescendants(dd.getId());
		for (DictionaryModel ddm : ks) {
			if (testIfSelected2(ddm)) {
				k2 = true;
				break;
			}
		}
		if (k2) {
			node.setExpanded(true);
		}
		node.isLeaf();

		if (testIfSelected2(dd)) {
			System.out.println(dd.getId() + dd.getName());
			node.setSelected(true);
			node.setExpanded(true);
		} else {
			node.setSelected(false);
		}

		List<DictionaryModel> ls = this.dicDAO.findAllSonDictionaryModel2(dd.getId());
		Collections.sort(ls);
		dd.setSons(ls);
		if (ls.isEmpty()) {
			node.setSelectable(true);
			return;
		}
		node.setSelectable(false);
		for (DictionaryModel d : ls) {
			TreeNode t = new DefaultTreeNode(d, node);
			test2(d, t);
		}
	}
	
	private boolean testIfSelected2(DictionaryModel dd) {
		String str = "";
		if (this.lessonType.getGroupStr() != null) {
			str = this.lessonType.getGroupStr();
		}
		if (str.contains(dd.getId())) {
			dd.setSelected(true);
			return true;
		}
		return false;
	}
	
	public void test(CourseType dd, TreeNode node) {
		boolean k1 = false;
		String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
		List<CourseType> ks = this.courseTypeDAO.loadAllDescendants(dd.getId(),businessId);
		for (CourseType ddm : ks) {
			if (testIfSelected(ddm)) {
				k1 = true;
				break;
			}
		}
		if (k1) {
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
		if (this.lessonType.getCourseTypeStr() != null) {
			str = this.lessonType.getCourseTypeStr();
		}
		if (str.contains(dd.getId())) {
			dd.setSelected(true);
			return true;
		}
		return false;
	}

	public void loadAdmins() {
		this.admins = this.adminDAO.findAllAdmin();
		List<AdminInfo> exs = this.lessonType.getAdmins();
		if ((exs != null) && (this.admins != null))
			for (AdminInfo e : this.admins)
				for (AdminInfo ex : exs)
					if (ex.getId().equals(e.getId())) {
						e.setSelected(true);
						break;
					}
	}

	public void loadExams() {
		String str = this.lessonType.getExamStr();

		this.exams = this.examDAO.findAllExamination(CookieUtils.getBusinessId(JsfHelper.getRequest()));
		if ((str != null) && (!"".equals(str)) && (this.exams != null))
			for (Examination ex : this.exams)
				if (str.contains(ex.getId()))
					ex.setSelected(true);
	}

	public String fetchExams() {
		StringBuilder sb = new StringBuilder();
		List<Examination> exams = new LinkedList<>();
		if (this.exams != null) {
			for (Examination ex : this.exams) {
				if (ex.isSelected()) {
					exams.add(ex);
					sb.append(ex.getId());
					sb.append(";");
				}
			}
		}
		this.lessonType.setExams(exams);
		return sb.toString();
	}

	public List<AdminInfo> fetchAdmins() {
		List exxs = new ArrayList();
		if (this.admins != null) {
			for (AdminInfo ex : this.admins) {
				if (ex.isSelected()) {
					exxs.add(ex);
				}
			}
		}
		return exxs;
	}

	public void SetDepartment(){
		if ((this.selectedNodes2 != null) && (this.selectedNodes2.length > 0)) {
			int len = this.selectedNodes2.length;
			String groupStr = "";
			for (int i = 0; i < len; i++) {
				DictionaryModel ct = (DictionaryModel) this.selectedNodes2[i].getData();
				groupStr = groupStr + ct.getId() + ";";
			}
			this.lessonType.setGroupStr(groupStr);
		}
	}
	
	public void SetCourseType(){
		if ((this.selectedNodes != null) && (this.selectedNodes.length > 0)) {
			int len = this.selectedNodes.length;
			String labStr = "";
			String typeStr = "";
			String courseTypeCN = "";
			for (int i = 0; i < len; i++) {
				CourseType ct = (CourseType) this.selectedNodes[i].getData();
				labStr = labStr + ct.getAncestors();
				typeStr = typeStr + ct.getId() + ";";
				courseTypeCN = courseTypeCN + ct.getName()+";";
			}
			this.lessonType.setLabelStr(labStr);
			this.lessonType.setCourseTypeStr(typeStr);
			this.lessonType.setCourseTypeCN(courseTypeCN);
		}
	}
	public void SetLessonTypeInUser(){
        Set<String> set = new HashSet<String>();
        if(StringUtil.isNotEmpty(this.lessonType.getGroupStr())){
            String[] str = this.lessonType.getGroupStr().split(";");
    		if(str!=null){
    			set = new HashSet<>(Arrays.asList(str));
    		}
    	}
      
        Set<BbsUser> userset = new HashSet<BbsUser>();
        Set<String> set2 = new HashSet<String>();
        List<BbsUser> uslist = bbsUserDAO.findAllBbsUser(this.businessId);
        if(uslist != null){
        	for(BbsUser us : uslist){
            	if(StringUtil.isNotEmpty(us.getGroupStr())){
            		String[] str2 = us.getGroupStr().split(";");
                	if(str2!=null){
                		set2 = new HashSet<>(Arrays.asList(str2));
            			for(String str:set){
                			if(set2.contains(str)){
                				userset.add(us);
                			}
                		}
                	} 
            	}
            }
        }
        
        
	  	for(BbsUser bu:userset){
	  			String str = "";
	  			 if(StringUtil.isNotEmpty(bu.getLessonTypeStr())){
	  				str = bu.getLessonTypeStr();
	  				if( !bu.getLessonTypeStr().contains(this.lessonType.getId())){
	  					str = str + this.lessonType.getId()+";";
	  					bu.setLessonTypeStr(str);
	  					bbsUserDAO.updateBbsUser(bu);
		  	  			  
		  	  			LessonTypeLog ltl = lessonTypeLogDAO.findLessonTypeLogByTypeAndUsr(this.lessonType.getId(), bu.getId());	  
		  	  			if(ltl==null){
		  	  				LessonTypeLog lessonTypeLog = new LessonTypeLog(this.lessonType,bu);
		  	  				lessonTypeLog.setBusinessId(this.businessId);
		  	  				this.lessonTypeLogDAO.addLessonTypeLog(lessonTypeLog);
		  	  			}
	  				}
	  			 }else{
	  				 str = str + this.lessonType.getId()+";";
	  				 bu.setLessonTypeStr(str);
	  				 bbsUserDAO.updateBbsUser(bu);
	  	  			  
	  	  			LessonTypeLog ltl = lessonTypeLogDAO.findLessonTypeLogByTypeAndUsr(this.lessonType.getId(), bu.getId());	  
	  	  			if(ltl==null){
	  	  				this.lessonTypeLogDAO.addLessonTypeLog(new LessonTypeLog(this.lessonType,bu));
	  	  			}
	  			 }
	  	}
	}
	
	public String done() {
		this.SetDepartment();
		this.SetCourseType();
		this.lessonType.setExamStr(fetchExams());
		List list = new ArrayList();
		list.add(ExternalUserUtil.getAdminBySession());
		this.lessonType.setAdmins(list);
		Teacher tea = teacherDAO.findTeacher(this.teacherId);
		this.lessonType.setTeacher(tea);
		if (this.flag) {
			this.logger.log("修改了课程：" + this.lessonType.getName());
			this.lessonType.setVersion(this.lessonType.getVersion() + 1);
			this.lessonTypeDAO.updateLessonType(this.lessonType);
		} else {
			this.lessonType.setBusinessId(this.businessId);
			this.logger.log("添加了课程：" + this.lessonType.getName());
			this.lessonTypeDAO.addLessonType(this.lessonType);
		}
		this.SetLessonTypeInUser();
		return "ListLessonType?faces-redirect=true";
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
			this.lessonType.setPicture(picUrl);
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		float a =2.1f;
		float b =11.0f;
		float c =1.5f;
		float d =0.5f;
		float e = a+b+c+d;
		System.out.println(e);
	}
}