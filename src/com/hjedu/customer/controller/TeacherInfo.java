package com.hjedu.customer.controller;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.hazelcast.util.CollectionUtil;
import com.hjedu.customer.dao.CertificateDAO;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.dao.ITeacherDAO;
import com.hjedu.customer.dao.JobTitleDAO;
import com.hjedu.customer.dao.ProjectExperienceDAO;
import com.hjedu.customer.dao.WorkExperienceDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.entity.Certificate;
import com.hjedu.customer.entity.JobTitle;
import com.hjedu.customer.entity.ProjectExperience;
import com.hjedu.customer.entity.Teacher;
import com.hjedu.customer.entity.WorkExperience;
import com.hjedu.platform.controller.AdminNewFile;
import com.hjedu.platform.controller.ClientSession;
import com.huajie.app.util.StringUtil;
import com.huajie.app.util.Validator;
import com.huajie.util.Cat;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class TeacherInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	ITeacherDAO teacherDAO = SpringHelper.getSpringBean("TeacherDAO");
	IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
	private Teacher teacher;
	boolean flag = false;
	private String picFront;
	private String picBack;

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

	public String getPicFront() {
		return picFront;
	}

	public void setPicFront(String picFront) {
		this.picFront = picFront;
	}

	public String getPicBack() {
		return picBack;
	}

	public void setPicBack(String picBack) {
		this.picBack = picBack;
	}

	@PostConstruct
    public void init() {
		if(this.teacher == null){
			this.teacher = new Teacher();
		}
		ClientSession cs = JsfHelper.getBean("clientSession");
		if(cs!=null){
			BbsUser user = cs.getUsr();
			if(user!=null){
				if(user.getTeacher()!=null){
					Teacher t= teacherDAO.findTeacher(user.getTeacher().getId());
					if(t!=null){
						this.teacher = t;
					}
				}
			}
		}
	}
	
	public void up_action(FileUploadEvent event){
		String picUrl = savePic(event);
		if(StringUtil.isNotEmpty(picUrl)){
			picFront = picUrl;
			this.teacher.setFrontPicture(picUrl);
//			this.teacherDAO.updateTeacher(teacher);
		}
	}
	
	public void up_action2(FileUploadEvent event){
		String picUrl = savePic(event);
		if(StringUtil.isNotEmpty(picUrl)){
			picBack = picUrl;
			this.teacher.setBackPicture(picUrl);
//			this.teacherDAO.updateTeacher(teacher);
		}
	}
	
	
	private String savePic(FileUploadEvent event) {
		String picUrl = "";
		try {
			UploadedFile item = event.getFile();
			String n = item.getFileName();
			int l2 = n.lastIndexOf(".");
			String ext = "." + n.substring(l2 + 1).toLowerCase();
			if (!ext.equals(".jpg") && !ext.equals(".jpeg") && !ext.equals(".png") && !ext.equals(".gif")) {
				JsfHelper.error("不符合的图片类型！");
				return picUrl;
			}
			if(item.getSize()>5*1024*1024){
				JsfHelper.error("图片大小不能超过5M");
				return picUrl;
			}
			String imgId = Cat.getUniqueId();
			AdminNewFile.saveFile(item, ext, imgId);
			picUrl = "servlet/ShowImage?id=" + imgId;
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		return picUrl;
	}

	public void up_action3(FileUploadEvent event){
		String picUrl = savePic(event);
		if(StringUtil.isNotEmpty(picUrl)){
			List<JobTitle> jts = this.teacher.getJobTitles();
			if(jts==null){
				jts = new LinkedList<>();
			}
			JobTitle jt = new JobTitle();
			jt.setPicture(picUrl);
			jt.setTeacher(teacher);
			jts.add(jt);
			this.teacherDAO.updateTeacher(teacher);
		}
	}
	
	public void up_action4(FileUploadEvent event){
		String picUrl = savePic(event);
		if(StringUtil.isNotEmpty(picUrl)){
			List<Certificate> cfs = this.teacher.getCertificate();
			if(cfs==null){
				cfs = new LinkedList<>();
			}
			Certificate cf = new Certificate();
			cf.setPicture(picUrl);
			cf.setTeacher(teacher);
			cfs.add(cf);
			this.teacherDAO.updateTeacher(teacher);
		}
	}
	
	public void addNewJobTitle() {
		List<JobTitle> jts = this.teacher.getJobTitles();
		if(CollectionUtil.isEmpty(jts)){
			jts = new LinkedList<>();
		}
		JobTitle newItem = new JobTitle();
		newItem.setTeacher(teacher);
		jts.add(newItem);
		this.teacher.setJobTitles(jts);
	}
	public void addNewItem(Long type) {
		if(type==1){
			List<JobTitle> jts = this.teacher.getJobTitles();
			if(CollectionUtil.isEmpty(jts)){
				jts = new LinkedList<>();
			}
			JobTitle newItem = new JobTitle();
			newItem.setTeacher(teacher);
			jts.add(newItem);
			this.teacher.setJobTitles(jts);
		}else if(type==2){
			List<WorkExperience> jts = this.teacher.getWorkExperience();
			if(CollectionUtil.isEmpty(jts)){
				jts = new LinkedList<>();
			}
			WorkExperience newItem = new WorkExperience();
			newItem.setTeacher(teacher);
			jts.add(newItem);
			this.teacher.setWorkExperience(jts);
		}else if(type==3){
			List<ProjectExperience> jts = this.teacher.getProjectExperience();
			if(CollectionUtil.isEmpty(jts)){
				jts = new LinkedList<>();
			}
			ProjectExperience newItem = new ProjectExperience();
			newItem.setTeacher(teacher);
			jts.add(newItem);
			this.teacher.setProjectExperience(jts);
		}else if(type==4){
			List<Certificate> jts = this.teacher.getCertificate();
			if(CollectionUtil.isEmpty(jts)){
				jts = new LinkedList<>();
			}
			Certificate newItem = new Certificate();
			newItem.setTeacher(teacher);
			jts.add(newItem);
			this.teacher.setCertificate(jts);
		}
    }
	
	public void deleteNewItem(Long type,String id){
		if(type==1){
			List<JobTitle> jts = this.teacher.getJobTitles();
			for(JobTitle jt : jts){
				if(jt.getId().equals(id)){
					jts.remove(jt);
					JobTitleDAO dao = SpringHelper.getSpringBean("JobTitleDAO");
					dao.delete(id);
					break;
				}
			}
		}else if(type==2){
			List<WorkExperience> jts = this.teacher.getWorkExperience();
			for(WorkExperience jt : jts){
				if(jt.getId().equals(id)){
					jts.remove(jt);
					WorkExperienceDAO dao = SpringHelper.getSpringBean("WorkExperienceDAO");
					dao.delete(id);
					break;
				}
			}
		}else if(type==3){
			List<ProjectExperience> jts = this.teacher.getProjectExperience();
			for(ProjectExperience jt : jts){
				if(jt.getId().equals(id)){
					jts.remove(jt);
					ProjectExperienceDAO dao = SpringHelper.getSpringBean("ProjectExperienceDAO");
					dao.delete(id);
					break;
				}
			}
		}else if(type==4){
			List<Certificate> jts = this.teacher.getCertificate();
			for(Certificate jt : jts){
				if(jt.getId().equals(id)){
					jts.remove(jt);
					CertificateDAO dao = SpringHelper.getSpringBean("CertificateDAO");
					dao.delete(id);
					break;
				}
			}
		}else if(type==5){
			this.teacher.setFrontPicture(null);
			this.teacherDAO.updateTeacher(teacher);
		}else if(type==6){
			this.teacher.setBackPicture(null);
			this.teacherDAO.updateTeacher(teacher);
		}
	}
	
	public String submit(){
		ClientSession cs = JsfHelper.getBean("clientSession");
		if(StringUtil.isEmpty(this.teacher.getName().trim())){
			JsfHelper.error("请填写真实姓名");
			return null;
		}else if(this.teacher.getName().length()>32){
			JsfHelper.error("姓名字数不能超过32个字符");
			return null;
		}
		if(this.teacher.getCompany().length()>100){
			JsfHelper.error("工作单位字数不能超过100个字符");
			return null;
		}
		if(StringUtil.isNotEmpty(this.teacher.getWorkYears())){
//			String regex = "[0-9]*";
			String regex = "^\\d*([.]\\d{1})?$";
			if(!Pattern.matches(regex, teacher.getWorkYears())){
				JsfHelper.error("请填写正确的工作年限");
				return null;
			}
		}
		if(StringUtil.isEmpty(this.teacher.getIdNum())){
			JsfHelper.error("请填写身份证号");
			return null;
		}else{
			String regex = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
			if(!Pattern.matches(regex, this.teacher.getIdNum())){
				JsfHelper.error("请填写正确的身份证号");
				return null;
			}
		}
		if(StringUtil.isEmpty(this.teacher.getFrontPicture())){
			JsfHelper.error("请上传身份证正面照片");
			return null;
		}
		if(StringUtil.isEmpty(this.teacher.getBackPicture())){
			JsfHelper.error("请上传身份证背面照片");
			return null;
		}
		if(cs!=null){
			BbsUser user = cs.getUsr();
			if(user!=null){
//				List<WorkExperience> wes = this.teacher.getWorkExperience();
//				for(WorkExperience we:wes){
//					if(we.getBeginTime()==null && we.getEndTime()==null 
//							&& StringUtil.isEmpty(we.getName()) && StringUtil.isEmpty(we.getDuty())){
//						wes.remove(we);
//						WorkExperienceDAO dao = SpringHelper.getSpringBean("WorkExperienceDAO");
//						dao.delete(we.getId());
//					}
//				}
//				this.teacher.setWorkExperience(wes);
//				
//				List<ProjectExperience> pes = this.teacher.getProjectExperience();
//				for(ProjectExperience pe:pes){
//					if(pe.getBeginTime()==null && pe.getEndTime()==null 
//							&& StringUtil.isEmpty(pe.getName())){
//						pes.remove(pe);
//						ProjectExperienceDAO dao = SpringHelper.getSpringBean("ProjectExperienceDAO");
//						dao.delete(pe.getId());
//					}
//				}
//				this.teacher.setProjectExperience(pes);
				
				user.setTeacher(this.teacher);
				this.teacher.setUser(user);
				userDAO.updateBbsUser(user);
				this.teacher.setBussinessId(user.getBusinessId());
				teacherDAO.updateTeacher(this.teacher);
				this.flag=false;
			}
		}
		return null;
	}
	
	public void edit(){
		if(flag){
			flag=false;
			init();
		}else{
			flag=true;
		}
	}
}
