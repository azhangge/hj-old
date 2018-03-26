package com.hjedu.course.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.hjedu.course.dao.ChapterDAO;
import com.hjedu.course.dao.HJCourseDAO;
import com.hjedu.course.dao.ICourseTypeDAO;
import com.hjedu.course.entity.Chapter;
import com.hjedu.course.entity.HJCourse;
import com.hjedu.customer.dao.IBbsFileDAO;
import com.hjedu.customer.entity.Teacher;
import com.hjedu.examination.entity.CourseType;
import com.hjedu.platform.controller.AdminNewFile;
import com.hjedu.platform.controller.ClientSession;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.entity.BbsFileModel;
import com.huajie.app.util.FileUtil;
import com.huajie.app.util.StringUtil;
import com.huajie.util.Cat;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class CreateCourse implements Serializable {
	private static final long serialVersionUID = 1L;
	private Logger log4j = Logger.getLogger(CreateCourse.class);
	private HJCourse course;
	private Chapter chapter;
	private boolean add = false;
	private List<CourseType> firstClassifys;
	private List<CourseType> secondClassifys;

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	public Logger getLog4j() {
		return log4j;
	}

	public void setLog4j(Logger log4j) {
		this.log4j = log4j;
	}

	public HJCourse getCourse() {
		return course;
	}

	public void setCourse(HJCourse course) {
		this.course = course;
	}

	public boolean isAdd() {
		return add;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

	public List<CourseType> getFirstClassifys() {
		return firstClassifys;
	}

	public void setFirstClassifys(List<CourseType> firstClassifys) {
		this.firstClassifys = firstClassifys;
	}

	public List<CourseType> getSecondClassifys() {
		return secondClassifys;
	}

	public void setSecondClassifys(List<CourseType> secondClassifys) {
		this.secondClassifys = secondClassifys;
	}

	@PostConstruct
	public void init() {
		HttpServletRequest request = JsfHelper.getRequest();
		String businessId = CookieUtils.getBusinessId(request);
		String id = request.getParameter("id");
		if (StringUtil.isNotEmpty(id)) {
			HJCourseDAO dao = SpringHelper.getSpringBean("HJCourseDAO");
			course = dao.findById(id);
		}
		if (null == course) {
			this.course = new HJCourse();
		}
		this.chapter = new Chapter();
		ICourseTypeDAO ctdao = SpringHelper.getSpringBean("CourseTypeDAO");
		firstClassifys = ctdao.findFirstCourseType(businessId);
		if(StringUtil.isNotEmpty(course.getFirstTypeId())){
			secondClassifys = ctdao.findAllSonCourseType(course.getFirstTypeId(),businessId);
		}
	}
	
	public String getSecondClassifys(String id){
		String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
		ICourseTypeDAO ctdao = SpringHelper.getSpringBean("CourseTypeDAO");
		secondClassifys = ctdao.findAllSonCourseType(id, businessId);
		return null;
	}

	public String submit() {
		HJCourseDAO dao = SpringHelper.getSpringBean("HJCourseDAO");
		ClientSession cs = JsfHelper.getBean("clientSession");
		if (cs != null && cs.getUsr() != null) {
			Teacher t = cs.getUsr().getTeacher();
			if (null != t) {
				course.setTeacher(t);
			}
		}
//		if(StringUtil.isNotEmpty(classifyId)&&StringUtil.isNotEmpty(classifyId2)){
//			this.course.setFirstClassify(classifyId);
//			this.course.setSecondClassify(classifyId2);
//		}
		if (course.getTeacher() != null) {
			dao.update(course);
		} else {
			dao.add(course);
		}
		return "/talk/TeacherCourseList.jspx?faces-redirect=true";
	}

	public void up_action(FileUploadEvent event) {
		String picUrl = savePic(event);
		if (StringUtil.isNotEmpty(picUrl)) {
			this.course.setPicture(picUrl);
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
			if (item.getSize() > 5 * 1024 * 1024) {
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

	public void uploadFile(FileUploadEvent event) {
		try {
			UploadedFile item = event.getFile();
			Long tl = item.getSize();
			BbsFileModel file = new BbsFileModel();
			file.setRealLength(tl);
			String str = getFileSize(item);
			file.setFileSize(str);
			String n = item.getFileName();
			int l1 = n.lastIndexOf("\\");
			int l2 = n.lastIndexOf(".");
			String ext = "." + n.substring(l2 + 1).toLowerCase();
			String name = n.substring(l1 + 1, l2);
			file.setFileExt(ext);
			file.setUploadTime(new Date());
			file.setFileName(name);
			String id = file.getId();
			saveFile(item, ext, id);
			file.setCourse(course);
			List<BbsFileModel> files = this.course.getFiles();
			if (null == files) {
				files = new LinkedList<>();
			}
			files.add(file);
			this.course.setFiles(files);
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}
	
	public void uploadChapterFile(FileUploadEvent event) {
		try {
			UploadedFile item = event.getFile();
			Long tl = item.getSize();
			BbsFileModel file = new BbsFileModel();
			file.setRealLength(tl);
			String str = getFileSize(item);
			file.setFileSize(str);
			String n = item.getFileName();
			int l1 = n.lastIndexOf("\\");
			int l2 = n.lastIndexOf(".");
			String ext = "." + n.substring(l2 + 1).toLowerCase();
			String name = n.substring(l1 + 1, l2);
			file.setFileExt(ext);
			file.setUploadTime(new Date());
			file.setFileName(name);
			String id = file.getId();
			saveFile(item, ext, id);
			file.setChapter(chapter);
			this.chapter.setFile(file);
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	/**
	 * 获取文件大小，单位为B/KB/MB/GB
	 * 
	 * @param tl
	 *            为UploadedFile.getSize()
	 * @return
	 */
	public String getFileSize(UploadedFile item) {
		Long tl = item.getSize();
		String str = "";
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
		return str;
	}

	/**
	 * 保存文件
	 * 
	 * @param item
	 * @param ext文件后缀名
	 */
	public static void saveFile(UploadedFile item, String ext, String id) {
		try {
			InputStream fis = item.getInputstream();
			byte[] bb = new byte[1024];
			String dir = "";
			ISystemConfigDAO systemConfigDAO = SpringHelper.getSpringBean("SystemConfigDAO");
			boolean f = systemConfigDAO.getSystemConfig().getIfRelative();
			if (f) {
				String tp = systemConfigDAO.getSystemConfig().getFilePath();
				if (!tp.endsWith("/")) {
					tp = tp + "/";
				}
				dir = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext())
						.getRealPath(tp);
			} else {
				dir = systemConfigDAO.getSystemConfig().getFilePath();
			}
			if (!(dir.endsWith("\\") || dir.endsWith("/"))) {
				dir = dir + "/";
			}
			File f_dir0 = new File(dir);
			if (!f_dir0.exists()) {
				f_dir0.mkdir();
			}
			String path = ext.substring(1);
			String suffix = ext.toLowerCase();
			if (ext.toLowerCase().equals(".flv") || ext.toLowerCase().equals(".mp4") || ext.toLowerCase().equals(".f4v")
					|| ext.toLowerCase().equals(".3gp") || ext.toLowerCase().equals(".mov")) {
				path = "user_flashes";
				suffix = ".mp4";
			} else if (ext.toLowerCase().equals(".mp3")) {
				path = "user_mp3s";
			} else if (ext.toLowerCase().equals(".jpg") || ext.toLowerCase().equals(".gif")
					|| ext.toLowerCase().equals(".png")) {
				path = "user_images";
				suffix = ".jpg";
			} else if (ext.toLowerCase().equals(".doc")) {
				path = "doc";
				suffix = ".doc";
			} else if (ext.toLowerCase().equals(".docx")) {
				path = "docx";
				suffix = ".docx";
			} else if (ext.toLowerCase().equals(".pptx")) {
				path = "pptx";
				suffix = ".pptx";
			} else if (ext.toLowerCase().equals(".ppt")) {
				path = "ppt";
				suffix = ".ppt";
			} else if (ext.toLowerCase().equals(".pdf")) {
				path = "pdf";
				suffix = ".pdf";
			}

			dir = dir + path;
			File f_dir = new File(dir);
			if (!f_dir.exists()) {
				f_dir.mkdir();
			}
			System.out.println("文件存储路径：" + dir);
			String nfn = dir + "/" + id + suffix;
			File ffff = new File(nfn);
			FileOutputStream fos = new FileOutputStream(ffff);
			BufferedInputStream is = new BufferedInputStream(fis);
			BufferedOutputStream os = new BufferedOutputStream(fos);// 将加密过的文件流转换为缓冲流
			int len = 0;
			while ((len = is.read(bb)) != -1) {
				os.write(bb, 0, len);
			}
			is.close();
			os.close();
			fis.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String createFile() {
		return null;
	}
	
	public String addChapter(){
		this.chapter = new Chapter();
		this.add = true;
		return null;
	}

	public String editChapter(String id) {
//		ChapterDAO dao = SpringHelper.getSpringBean("ChapterDAO");
//		this.chapter = dao.findById(id);
		List<Chapter> cts = this.course.getChapters();
		for(Chapter ct : cts){
			if(ct.getId().equals(id)){
				this.chapter = ct;
				this.add = false;
				break;
			}
		}
		return null;
	}
	
	public String addOrEditChapter(){
		this.chapter.setCourse(course);
		List<Chapter> cts = this.course.getChapters();
		if(null==cts){
			cts = new LinkedList<>();
		}
		if(this.add){
			cts.add(chapter);
		}else{
			List<Chapter> copy = new LinkedList<>();
			copy.addAll(cts);
			for(Chapter ct : copy){
				if(ct.getId().equals(chapter.getId())){
					cts.remove(ct);
					cts.add(chapter);
					break;
				}
			}
		}
		this.course.setChapters(cts);
		return null;
	}
	
	public String batchDelete(int i){
		if(i==0){
			List<BbsFileModel> files = this.course.getFiles();
			List<BbsFileModel> copy = new LinkedList<>();
			copy.addAll(files);
			for(BbsFileModel file : copy){
				if(file.isSelected()){
			        deletePhyFile(file.getId(), file.getFileExt());
					for(BbsFileModel file2 : copy){
						if(file2.getId().equals(file.getId())){
							files.remove(file);
							break;
						}
					}
					deleteFile(file.getId());
				}
			}
		}else if(i==1){
			List<Chapter> cts = this.course.getChapters();
			List<Chapter> copy = new LinkedList<>();
			copy.addAll(cts);
			for(Chapter ct : copy){
				if(ct.isSelected()){
					if(ct.getFile()!=null){
						deletePhyFile(ct.getFile().getId(), ct.getFile().getFileExt());
					}
					deleteChapter(ct.getId());
					for(Chapter ct2 : copy){
						if(ct2.getId().equals(ct.getId())){
							cts.remove(ct);
							break;
						}
					}
				}
			}
		}else if(i==2){
			deletePhyFile(this.chapter.getFile().getId(), this.chapter.getFile().getFileExt());
			this.chapter.setFile(null);
		}
		return null;
	}
	
	public void deletePhyFile(String id,String ext){
		String path = FileUtil.getDownloadFilePath(id,ext);
		File f1 = new File(path);
		if (f1.exists()) {
			f1.delete();
		}
	}
	
	public String deleteChapter(String id){
		ChapterDAO dao = SpringHelper.getSpringBean("ChapterDAO");
		dao.delete(id);
		return null;
	}
	
	public String deleteFile(String id){
		IBbsFileDAO dao = SpringHelper.getSpringBean("BbsFileDAO");
		dao.delClientFile(id);
		return null;
	}
}
