package com.huajie.app.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

//import org.apache.log4j.Logger;

import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.dao.ISystemInfoDAO;
import com.hjedu.platform.entity.BbsFileModel;
import com.hjedu.platform.entity.SystemInfo;
import com.huajie.util.SpringHelper;

/**
 *	文件工具类
 */
public final class FileUtil {
//	private static final  Logger logger = Logger.getLogger(FileUtil.class);
	private static String url = null;
	private static String welcomePage = null;
	
	public static String getFileRealPath(BbsFileModel file){
		String str="";
		if(file!=null){
			String ext = file.getFileExt();
			if(StringUtil.isNotEmpty(ext)){
				String id = file.getId();
				if(ext.equals(".mp4")){
					str= getMp4FilePath(id);
				}else if(ext.equals(".mp3")){
					str= getMp3RealPathById(id);
				}else if(ext.equals(".jpg")){
					str= getImageRealPathById(id);
				}else if(ext.equals(".pdf") || ext.equals(".doc") || ext.equals(".docx") ||ext.equals(".ppt") || ext.equals(".pptx") || ext.equals(".html")){
					str= getDocmentRealPathById(id,ext);
				}else{
					str= getSysFilePath()+ext.substring(1)+"/"+id+ext;
				}
			}
		}
		return str;
	}
	
	public static String getDownloadFilePath(String id,String ext){
		String str = "";
		if(StringUtil.isNotEmpty(ext)){
			if(ext.equals(".mp4")){
				str= getMp4FilePath(id);
			}else if(ext.equals(".mp3")){
				str= getMp3RealPathById(id);
			}else if(ext.equals(".jpg")||ext.equals(".png")||ext.equals(".jpeg") || ext.equals("gif")){
				str= getImageRealPathById(id);
			}else if(ext.equals(".pdf") || ext.equals(".doc") || ext.equals(".docx") ||ext.equals(".ppt") || ext.equals(".pptx") || ext.equals(".html")){
				str= getDocmentRealPathById(id,ext);
			}else{
				str= getSysFilePath()+ext.substring(1)+"/"+id+ext;
			}
		}
		return str;
	}
	
	/**
	 * 获取pdf的物理地址
	 * @param BbsFileModel
	 * @return
	 */
	public static String getPdfFileRealPath(BbsFileModel file){
		String str="";
		if(file!=null){
			String id = file.getId();
			str= getDocmentRealPathById(id,".pdf");
		}
		return str;
	}
	
	/**
	 * 获取不同文档的物理地址
	 * @param id
	 * @param ext
	 * @return
	 */
	public static String getDocmentRealPathById(String id,String ext) {
		String path = getSysFilePath()+ext.substring(1)+"/"+id+ext;
		return path;
	}
	
	public static String getDocmentURLByIdAndBusinessId(String id,String ext,String businessId) {
		String path = getUrlByBusinessId(businessId)+"file/"+ext.substring(1)+"/"+id+ext;
		return path;
	}
	
	public static String getMp4FilePath(String id){
		String path = getSysFilePath()+"user_flashes/"+id+".mp4";
		return path;
	}
	
	public static String getMp4URLByIdAndBusinessId(String id,String businessId){
		String path = getUrlByBusinessId(businessId)+"file/user_flashes/"+id+".mp4";
		return path;
	}
	
	public static String getImageRealPathById(String id){
		String path = getSysFilePath()+"user_images/"+id+".jpg";
		return path;
	}
	
	public static String getImageURLByIdAndBusinessId(String id,String businessId){
		String path = getUrlByBusinessId(businessId)+"file/user_images/"+id+".jpg";
		return path;
	}
	
	public static String getImageURLByIdAndBusinessId(String id,String ext,String businessId){
		String path = getUrlByBusinessId(businessId)+"file/user_images/"+id+"."+ext;
		return path;
	}
	
	public static String getMp3RealPathById(String id){
		String path = getSysFilePath()+"user_mp3s/"+id+".mp3";
		return path;
	}
	
	public static String getMp3URLByIdAndBusinessId(String id,String businessId){
		String path = getUrlByBusinessId(businessId)+"file/user_mp3s/"+id+".mp3";
		return path;
	}
	
	public static String getSysFilePath(){
		 String dir = "";
		 ISystemConfigDAO systemConfigDAO = SpringHelper.getSpringBean("SystemConfigDAO");
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
         return dir;
	}
	
	/**
	 * 
	 * @param fis 文件输入流
	 * @param ext 文件后缀名
	 * @param id 文件id
	 */
    public static void saveFile(InputStream fis,String ext,String id){
		try {
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
    
	public static byte[] File2byte(String filePath)
    {
        byte[] buffer = null;
        try
        {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return buffer;
    }

    public static void byte2File(byte[] buf, String filePath, String fileName)
    {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try
        {
            File dir = new File(filePath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (bos != null)
            {
                try
                {
                    bos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static String getUrlByBusinessId(String businessId) {
		if(StringUtil.isNotEmpty(url)){
			return url;
		}
//		String dburl = null;
//		String user = null;
//		Properties properties = new Properties();
//		File file = new File(System.getProperty("catalina.home") + "/conf/jdbc.properties");
//		if(!file.exists()){
//			file = new File(FileUtil.class.getResource("/jdbc.properties").getPath());
//		}
		try {
//			InputStream inputStream = new FileInputStream(file);
//			properties.load(inputStream);
//			dburl = properties.getProperty("oracle.jdbc.url");
//			user = properties.getProperty("oracle.jdbc.username");
//			url = "http://"+dburl.substring(dburl.lastIndexOf("@")+1,dburl.lastIndexOf(":")-5)+"";
			ISystemInfoDAO infoDAO = SpringHelper.getSpringBean("SystemInfoDAO");
			SystemInfo info = infoDAO.findSystemInfoByBusinessId(businessId);
			if(info!=null){
				url = info.getUrl();
			}
			if(StringUtil.isEmpty(url)){
				url = "http://localhost:8080/";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}
    
    public static String removePartOfStr(String str,String part){
    	String result = str;
    	if(!part.endsWith(";")){
    		part = part + ";";
    	}
    	if(str.contains(part)){
    		result = str.replaceAll(part, "");
    	}
    	return result;
    }

	public static void setUrl(String url2) {
		url = url2;
	}
	
	public static String getWelcomePageByBusinessId(String businessId) {
//		if(StringUtil.isNotEmpty(welcomePage)){
//			return welcomePage;
//		}
		try {
			ISystemInfoDAO infoDAO = SpringHelper.getSpringBean("SystemInfoDAO");
			SystemInfo info = infoDAO.findSystemInfoByBusinessId(businessId);
			if(info!=null){
				welcomePage = info.getWelcomePage();
			}
			if(StringUtil.isEmpty(welcomePage)){
				welcomePage = "/HJ/index.jspx";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return welcomePage;
	}

	public static void setWelcomePage(String welcomePage) {
		FileUtil.welcomePage = welcomePage;
	}

	public static String getDownloadURLByBusinessId(String id,String ext,String businessId){
		String str = "";
		if(StringUtil.isNotEmpty(ext)){
			if(ext.equals(".mp4")){
				str= getMp4URLByIdAndBusinessId(id,businessId);
			}else if(ext.equals(".mp3")){
				str= getMp3URLByIdAndBusinessId(id,businessId);
			}else if(ext.equals(".jpg")){
				str= getImageURLByIdAndBusinessId(id,businessId);
			}else if(ext.equals(".pdf") || ext.equals(".doc") || ext.equals(".docx") ||ext.equals(".ppt") || ext.equals(".pptx") || ext.equals(".html")){
				str= getDocmentURLByIdAndBusinessId(id,ext,businessId);
			}else{
				str= getUrlByBusinessId(businessId)+"file/"+ext.substring(1)+"/"+id+ext;
			}
		}
		return str;
	}
	
	public static String getUploadURLByBusinessId(String id,String ext,String businessId){
		File file = new File(getSysFilePath()+ext.substring(1));
		if(!file.exists()){
			file.mkdirs();
		}
		return getDownloadURLByBusinessId(id, ext,businessId);
	}
	
	public static String getShowUrl(String id,String ext){
		String str = "";
		if(ext.toLowerCase().equals(".jpg") || ext.toLowerCase().equals(".gif") || ext.toLowerCase().equals(".png")){
			str = "servlet/ShowImage?id="+id;
		}
		return str;
	}
	
	public static String getExtByFileName(String fileName){
		String ext = fileName.substring(fileName.indexOf("."), fileName.length());
		return ext;
	}
	
    public static void main(String[] args) {
    	String f="sdsfa.pdf";
    	System.out.println(getExtByFileName(f));
	}
}
