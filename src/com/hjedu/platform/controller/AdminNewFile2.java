package com.hjedu.platform.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultUploadedFile;
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
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.JsonUtil;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AdminNewFile2 extends NewFileAbstract {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
            }else if(ext.toLowerCase().equals(".jpg") || ext.toLowerCase().equals(".gif") || ext.toLowerCase().equals(".png")){
            	path = "user_images";
            	suffix = ".jpg";
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
//        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
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
//        String t = IpHelper.getRemoteAddr(request);
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
		commend.add("-s");
		commend.add("360*360");
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
    
    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }  
    
    public static void uploadFile(String fileName,String uploadPath) {  
        try {  
        	 HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            // 换行符  
            final String newLine = "\r\n";  
            final String boundaryPrefix = "--";  
            // 定义数据分隔线  
            String BOUNDARY = "========7d4a6d158c9";  
            // 服务器的域名  
            String businessId = CookieUtils.getBusinessId(request);
            URL url = new URL(FileUtil.getUrlByBusinessId(businessId)+"servlet/app/UploadServlet");  
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
            // 设置为POST情  
            conn.setRequestMethod("POST");  
            // 发送POST请求必须设置如下两行  
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            conn.setUseCaches(false);  
            // 设置请求头参数  
            conn.setRequestProperty("connection", "Keep-Alive");  
            conn.setRequestProperty("Charsert", "UTF-8");  
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);  
  
            OutputStream out = new DataOutputStream(conn.getOutputStream());  
  
            // 上传文件  
            File file = new File(fileName);  
            StringBuilder sb = new StringBuilder();  
            sb.append(boundaryPrefix);  
            sb.append(BOUNDARY);  
            sb.append(newLine);  
            // 文件参数,photo参数名可以随意修改  
            sb.append("Content-Disposition: form-data;name=\"photo\";filename=\"" + fileName  
                    + "\"" + newLine);  
            sb.append("Content-Type:application/octet-stream");  
            // 参数头设置完以后需要两个换行，然后才是参数内容  
            sb.append(newLine);  
            sb.append(newLine);  
  
            // 将参数头的数据写入到输出流中  
            out.write(sb.toString().getBytes());  
  
            // 数据输入流,用于读取文件数据  
            DataInputStream in = new DataInputStream(new FileInputStream(  
                    file));  
            byte[] bufferOut = new byte[1024];  
            int bytes = 0;  
            // 每次读1KB数据,并且将文件数据写入到输出流中  
            while ((bytes = in.read(bufferOut)) != -1) {  
                out.write(bufferOut, 0, bytes);  
            }  
            // 最后添加换行  
            out.write(newLine.getBytes());  
            in.close();  
  
            // 定义最后数据分隔线，即--加上BOUNDARY再加上--。  
            byte[] end_data = (newLine + boundaryPrefix + BOUNDARY + boundaryPrefix + newLine)  
                    .getBytes();  
            // 写上结尾标识  
            out.write(end_data);  
            out.flush();  
            out.close();  
  
            // 定义BufferedReader输入流来读取URL的响应  
//            BufferedReader reader = new BufferedReader(new InputStreamReader(  
//                    conn.getInputStream()));  
//            String line = null;  
//            while ((line = reader.readLine()) != null) {  
//                System.out.println(line);  
//            }  
  
        } catch (Exception e) {  
            System.out.println("发送POST请求出现异常！" + e);  
            e.printStackTrace();  
        }  
    }  
    
    /**
     * 将文件保存在系统指定目录中
     * @param event
     */
    public void up_action2(FileUploadEvent event) {
        try {
            DefaultUploadedFile item = (DefaultUploadedFile) event.getFile();
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
//            String id = this.project2.getId();
            
            try {  
//                HttpPost httpPost = new HttpPost(FileUtil.getUrl()+"exam2/servlet/app/UploadUserPicture");
            	HttpPost httpPost = new HttpPost("http://192.168.1.176:8080/"+"exam2/servlet/app/UploadServlet");
                CloseableHttpClient client = HttpClients.createDefault();
                MultipartEntityBuilder multipartEntityBuilder=MultipartEntityBuilder.create();
//                byte[] buf=item.getContents();
//                FileUtil.byte2File(buf, "C:\\ucenter_file\\temp", fileName+ext);
//                File file=new File("C:\\ucenter_file\\temp\\"+fileName+ext);
//        		multipartEntityBuilder.addBinaryBody("file", file);
                multipartEntityBuilder.addBinaryBody("file", item.getInputstream());
                Map<String,String> textMap=new HashMap<String,String>();
                textMap.put("token", "123");
        		// 创建参数队列 
        		for(Map.Entry<String,String> tmpMap : textMap.entrySet()){
        			multipartEntityBuilder.addTextBody(tmpMap.getKey(), tmpMap.getValue());
        		}
        		HttpEntity httpEntity=multipartEntityBuilder.build();
        		httpPost.setEntity(httpEntity);
                client.execute(httpPost);
//                if(file.exists()){//删除临时文件
//                	file.delete();
//                }
            } catch (Exception e) {  
                System.out.println("发送POST请求出现异常！" + e);  
                e.printStackTrace();  
            }  
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        stat.reset();
    }
}
