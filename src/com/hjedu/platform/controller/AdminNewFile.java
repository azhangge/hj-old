package com.hjedu.platform.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.hjedu.course.dao.ILessonDAO;
import com.hjedu.course.entity.Lesson;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.controller.ListFilesOfLesson;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.entity.BbsFileModel;
import com.hjedu.platform.entity.FileSaveStatus;
import com.huajie.app.util.FileUtil;
import com.huajie.app.util.SessionUtil;
import com.huajie.app.util.StringUtil;
import com.huajie.util.IpHelper;
import com.huajie.util.JsfHelper;
import com.huajie.util.JsonUtil;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AdminNewFile extends NewFileAbstract {

    FileUpload fu = new FileUpload();
    FileSaveStatus stat = new FileSaveStatus();
    ILessonDAO lessonDAO = SpringHelper.getSpringBean("LessonDAO");
    ISystemConfigDAO systemConfigDAO  = SpringHelper.getSpringBean("SystemConfigDAO");
	private String lessonId;

    public FileSaveStatus getStat() {
        return stat;
    }

    public void setStat(FileSaveStatus stat) {
        this.stat = stat;
    }

    public FileUpload getFu() {
        return fu;
    }

    public void setFu(FileUpload fu) {
        this.fu = fu;
    }

    @PostConstruct
    public void init() {
        //保证上传时存在一个用户
        ClientSession cs = JsfHelper.getBean("clientSession");
        BbsUser bu = cs.getUsr();
        if (bu == null) {
            IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
            bu=userDAO.findSysUser();
            cs.setUsr(bu);
        } 
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        this.idt = request.getParameter("id");
        String fid = request.getParameter("fid");
        if (fid != null) {
            this.fatherID = fid;
        } else {
            this.fatherID = "0";
        }
        this.checkName();
        if (idt != null && (!"0".equals(idt))) {
            MyLogger.echo("idt:" + idt);
            this.project2 = project2DAO.findClientFile(this.idt);
            if(project2==null){
            	project2 = new BbsFileModel();
            }
            this.fnTemp = this.project2.getFileName();
            this.flag = true;
        }
    }

    public void newAdd() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String scope_t = request.getParameter("scope");
        if (scope_t != null) {
            this.scope = scope_t;
        }
        this.idt = null;
        this.flag = false;
        this.project2 = new BbsFileModel();
        this.fileName = "";
        this.checkName();
        this.rename = false;
        this.lessonId = request.getParameter("lessonId");
    }

    public String getLessonId() {
		return lessonId;
	}

	public void setLessonId(String lessonId) {
		this.lessonId = lessonId;
	}

	public void alter() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        this.idt = request.getParameter("id");
        if (idt != null) {
            this.project2 = project2DAO.findClientFile(this.idt);
            this.flag = true;
            this.fileName = this.project2.getFileName();
            this.oldName = this.fileName;
        }
        String scope_t = request.getParameter("scope");
        if (scope_t != null) {
            this.scope = scope_t;
        }
        this.checkName();
        this.rename = false;
    }

    public void checkName() {
        if (this.fileName.trim().equals("")) {
            this.errStr = "文件还未命名！";
            return;
        }
        if (!this.oldName.trim().equals("")) {
            if (this.oldName.trim().equals(this.fileName.trim())) {
                this.errStr = "";
                return;
            }
        }
        String uid = "0";
        BbsUser bu = null;
        ClientSession cs = JsfHelper.getBean("clientSession");

        bu = cs.getUsr();
        if (bu != null) {
            uid = bu.getId();
        } else {
            IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
            bu=userDAO.findSysUser();
            cs.setUsr(bu);
            uid = bu.getId();
        }
        boolean b = project2DAO.checkNameIfExistByUsr(fileName.trim(),
                this.fatherID, uid);
        if (b) {
            this.errStr = "文件名已存在，请重新命名！";
        } else {
            this.errStr = "";
        }
    }
    /**
     * 将文件保存在系统指定目录中
     * @param event
     */
    public void up_action(FileUploadEvent event) {
        try {
            UploadedFile item = event.getFile();
            Long tl = item.getSize();
            this.project2.setRealLength(tl);
            String str = getFileSize(item);
            this.project2.setFileSize(str);
            String n = item.getFileName();
            int l1 = n.lastIndexOf("\\");
            int l2 = n.lastIndexOf(".");
            String ext = "." + n.substring(l2 + 1).toLowerCase();
            String name = n.substring(l1 + 1, l2);
            if (this.fileName.equals("")) {
                this.setFileName(name);
            }
            this.project2.setFileExt(ext);
            stat.setTotal(tl);
            this.project2.setUploadTime(new Date());
            this.checkName();
            if (!this.errStr.equals("")) {
//                this.fileOk();
                this.flag = true;
                this.fileName = this.project2.getFileName();
                this.oldName = this.fileName;
            }
            String id = this.project2.getId();
            saveFile(item, ext,id);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        stat.reset();
    }
    
    /**
     * 获取文件大小，单位为B/KB/MB/GB
     * @param tl  为UploadedFile.getSize()
     * @return
     */
    public String getFileSize(UploadedFile item){
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
     * @param item
     * @param ext文件后缀名
     */
    public static void saveFile(UploadedFile item,String ext,String id){
		try {
			InputStream fis = item.getInputstream();
            byte[] bb = new byte[1024];
            String dir = "";
            ISystemConfigDAO systemConfigDAO  = SpringHelper.getSpringBean("SystemConfigDAO");
            boolean f = systemConfigDAO.getSystemConfig().getIfRelative();
            if (f) {
                String tp = systemConfigDAO.getSystemConfig().getFilePath();
                if (!tp.endsWith("/")) {
                    tp = tp + "/";
                }
                dir = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(tp);
            } else {
                dir = systemConfigDAO.getSystemConfig().getFilePath();
            }
            if (!(dir.endsWith("\\")||dir.endsWith("/"))) {
                dir = dir + "/";
            }
            File f_dir0 = new File(dir);
            if (!f_dir0.exists()) {
                f_dir0.mkdir();
            }
            String path = ext.substring(1);
            String suffix = ext.toLowerCase();
            if(ext.toLowerCase().equals(".flv") || ext.toLowerCase().equals(".mp4") || ext.toLowerCase().equals(".f4v") || ext.toLowerCase().equals(".3gp") || ext.toLowerCase().equals(".mov")){
            	path = "user_flashes";
            	suffix = ".mp4";
            }else if(ext.toLowerCase().equals(".mp3")){
            	path = "user_mp3s";
            }else if(ext.toLowerCase().equals(".jpg") || ext.toLowerCase().equals(".jpeg") || ext.toLowerCase().equals(".gif") || ext.toLowerCase().equals(".png")){
            	path = "user_images";
            	suffix = ".jpg";
            }else if(ext.toLowerCase().equals(".doc")){
            	path = "doc";
            	suffix = ".doc";
            }else if(ext.toLowerCase().equals(".docx")){
            	path = "docx";
            	suffix = ".docx";
            }else if(ext.toLowerCase().equals(".pptx")){
            	path = "pptx";
            	suffix = ".pptx";
            }else if(ext.toLowerCase().equals(".ppt")){
            	path = "ppt";
            	suffix = ".ppt";
            }else if(ext.toLowerCase().equals(".pdf")){
            	path = "pdf";
            	suffix = ".pdf";
            }
           
            dir=dir+path;
            File f_dir = new File(dir);
            if (!f_dir.exists()) {
                f_dir.mkdir();
            }
            System.out.println("文件存储路径：" + dir);
            String nfn = dir +"/" + id + suffix;
            File ffff=new File(nfn);
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
    
    /**
     * 持久化BbsFileModel
     */
    private void fileOk() {
        BbsFileModel cfm = this.project2;
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        //文件归属学习资料
        if(this.lessonId!=null){
        	Lesson lesson = lessonDAO.findLesson(this.lessonId);
        	cfm.setLesson(lesson);
        	//如果是MP4文件，后缀加上.mp4，若学习资料的视频源为空，默认播放MP4
        	if(StringUtil.isEmpty(lesson.getSourceUrl())&&cfm!=null&&cfm.getFileExt().equals(".mp4")){
        		String path = cfm.getId()+cfm.getFileExt();
        		lesson.setSourceUrl(path);
        	}
        }
        cfm.setFatherID(this.fatherID);
        cfm.setIfFolder(false);
        cfm.setScope(this.scope);
        cfm.setFileName(this.fileName.trim());
        String t = IpHelper.getRemoteAddr(request);
        cfm.setAdminId(SessionUtil.getAdminBySession().getId());
        if(this.project2.getFileExt().equals(".mp4")){
        	this.project2.setTotal_time(JsonUtil.getVedioTotalTime(cfm,getClass().getResource("/").getFile().toString()));
        	getImageOfVideo(this.project2);
        	this.project2.setFileFullPath("/servlet/ShowImage?id="+cfm.getId());
        }
        String path = "";
		try {
			path = FileUtil.getFileRealPath(cfm);
			File mf = new File(path);
			if(mf.exists()){
				FileInputStream fis = new FileInputStream(path);
				String md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis));
				IOUtils.closeQuietly(fis);
				cfm.setMd5(md5);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        if (!this.flag) {
            cfm.setUploadTime(new Date());
            project2DAO.saveClientFileInfo(this.project2);
            this.project2.setAncestors(this.cxLogic.genAncestors(this.project2.getId()));//生成祖先文件列表
            this.project2DAO.updateClientFileInfo(this.project2);
        } else {
            project2DAO.updateClientFileInfo(this.project2);
        }
        ClientListFile lcf = JsfHelper.getBean("clientListFile");
        if (lcf != null) {
            lcf.synDB();
        }
//        ListAdminFile laf = JsfHelper.getBean("listAdminFile");
//        if (laf != null) {
//            laf.synDB();
//        }
        ListFilesOfLesson lfo = JsfHelper.getBean("listFilesOfLesson");
        if (lfo != null) {
        	lfo.synDB();
        }
        this.fu = new FileUpload();
    }

    public String createFile() {
        this.fileOk();
        Lesson lesson = lessonDAO.findLesson(this.lessonId);
    	lesson.setVersion(lesson.getVersion()+1);
    	ILessonDAO examDAO = SpringHelper.getSpringBean("LessonDAO");
    	examDAO.updateLesson(lesson);
        this.project2 = new BbsFileModel();
        this.fileName = "";
        this.oldName = "";

        return null;
    }
    
    public void getImageOfVideo(BbsFileModel bf){
    	String videoRealPath = "",imageRealPath="";
    	if(bf!=null){
    		videoRealPath = FileUtil.getFileRealPath(bf);
            imageRealPath = FileUtil.getImageRealPathById(bf.getId());
            processJPG(videoRealPath,imageRealPath);
    	}
    }
    
    public boolean processJPG(String oldfilepath, String newfilepath) {
		File file = new File(oldfilepath);
		if (!file.isFile()) {
			return false;
		}
		String classPath = getClass().getResource("/").getFile();
		List<String> commend = new java.util.ArrayList<String>();
		commend.add(classPath+"ffmpeg.exe");
		commend.add("-i");
		commend.add(oldfilepath);
		commend.add("-ss");
		commend.add("1");
		commend.add("-vframes");
		commend.add("1");
		commend.add("-r");
		commend.add("1");
		commend.add("-ac");
		commend.add("1");
		commend.add("-ab");
		commend.add("2");
		commend.add("-f");
		commend.add("image2");
//		commend.add("-s");
//		commend.add("360*360");
		commend.add(newfilepath);
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commend);
			builder.start();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
