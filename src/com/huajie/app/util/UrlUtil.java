package com.huajie.app.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.hjedu.platform.dao.ISystemConfigDAO;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

/**
 *	文件工具类
 */
public final class UrlUtil {
	private static final  Logger logger = Logger.getLogger(UrlUtil.class);
	
	/**
	 * 获取url前缀例如：http://27.17.20.242:8080/exam2/
	 * @param request
	 * @return
	 */
	public static String getPrefixUrlByRequest(HttpServletRequest request){
		if(request==null){
			return "";
		}
        String dir = request.getScheme()+"://"+request.getServerName()+":"+
                request.getServerPort()+request.getContextPath()+"/";
        return dir;
	}
	
	/**
	 * 获取url前缀例如：http://27.17.20.242:8080/
	 * @param request
	 * @return
	 */
	public static String getPrefixUrlByRequest2(HttpServletRequest request){
		if(request==null){
			return "";
		}
        String dir = request.getScheme()+"://"+request.getServerName()+":"+
                request.getServerPort()+"/";
        return dir;
	}
	
	/**
	 * 获取下载json文件的url--http://27.17.20.242:8080/exam2/servlet/DownloadJsonFile?id=
	 * @param request
	 * @return
	 */
	public static String getJsonUrlByRequest(HttpServletRequest request){
		if(request==null){
			return "";
		}
        String dir = getPrefixUrlByRequest(request)+"servlet/DownloadJsonFile?id=";
        return dir;
	}
	
	/**
	 * 获取虚拟路径url--http://27.17.20.242:8080/file/
	 * @param request
	 * @return
	 */
	public static String getVirtualUrlByRequest(HttpServletRequest request){
		if(request==null){
			return "";
		}
        String dir = getPrefixUrlByRequest2(request)+"file/";
        return dir;
	}
	/**
	 * 获取doc虚拟路径url--http://27.17.20.242:8080/file/doc/
	 * @param request
	 * @return
	 */
	public static String getDocVirtualUrlByRequest(HttpServletRequest request){
		if(request==null){
			return "";
		}
        String dir = getVirtualUrlByRequest(request) + "doc/";
        return dir;
	}
	/**
	 * 获取docx虚拟路径url--http://27.17.20.242:8080/file/docx/
	 * @param request
	 * @return
	 */
	public static String getDocxVirtualUrlByRequest(HttpServletRequest request){
		if(request==null){
			return "";
		}
        String dir = getVirtualUrlByRequest(request) + "docx/";
        return dir;
	}
	/**
	 * 获取ppt虚拟路径url--http://27.17.20.242:8080/file/ppt/
	 * @param request
	 * @return
	 */
	public static String getPptVirtualUrlByRequest(HttpServletRequest request){
		if(request==null){
			return "";
		}
        String dir = getVirtualUrlByRequest(request) + "ppt/";
        return dir;
	}
	/**
	 * 获取pptx虚拟路径url--http://27.17.20.242:8080/file/pptx/
	 * @param request
	 * @return
	 */
	public static String getPptxVirtualUrlByRequest(HttpServletRequest request){
		if(request==null){
			return "";
		}
        String dir = getVirtualUrlByRequest(request) + "pptx/";
        return dir;
	}
	/**
	 * 获取pdf虚拟路径url--http://27.17.20.242:8080/file/pdf/
	 * @param request
	 * @return
	 */
	public static String getPdfVirtualUrlByRequest(HttpServletRequest request){
		if(request==null){
			return "";
		}
        String dir = getVirtualUrlByRequest(request) + "pdf/";
        return dir;
	}
	/**
	 * 获取pdf虚拟路径url--http://27.17.20.242:8080/file/html/
	 * @param request
	 * @return
	 */
	public static String getHtmlVirtualUrlByRequest(HttpServletRequest request){
		if(request==null){
			return "";
		}
        String dir = getVirtualUrlByRequest(request) + "html/";
        return dir;
	}
	/**
	 * 获取mp4虚拟路径url--http://27.17.20.242:8080/file/user_flashes/
	 * @param request
	 * @return
	 */
	public static String getMp4VirtualUrlByRequest(HttpServletRequest request){
		if(request==null){
			return "";
		}
        String dir = getVirtualUrlByRequest(request) + "user_flashes/";
        return dir;
	}
	/**
	 * 获取图片虚拟路径url--http://27.17.20.242:8080/file/user_images/
	 * @param request
	 * @return
	 */
	public static String getImageVirtualUrlByRequest(HttpServletRequest request){
		if(request==null){
			return "";
		}
        String dir = getVirtualUrlByRequest(request) + "user_images/";
        return dir;
	}
	
	/**
	 * 获取mp3虚拟路径url--http://27.17.20.242:8080/file/user_images/
	 * @param request
	 * @return
	 */
	public static String getMp3VirtualUrlByRequest(HttpServletRequest request){
		if(request==null){
			return "";
		}
        String dir = getVirtualUrlByRequest(request) + "user_mp3s/";
        return dir;
	}
	
	/**
	 * 将url中获取的乱码str转成中文
	 * @param str
	 * @return
	 */
	public static String getCN(String str){
		String result = "";
		if(str!=null){
			try {
				result = new String(str.getBytes("iso-8859-1"),"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
