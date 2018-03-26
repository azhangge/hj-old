package com.huajie.app.servlet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.primefaces.json.JSONObject;

import com.huajie.app.util.FileUtil;
import com.huajie.app.util.StringUtil;
import com.jspsmart.upload.Request;
import com.jspsmart.upload.SmartUpload;

@WebServlet(urlPatterns = {"/servlet/app/UploadServlet"})
public class UploadServlet extends HttpServlet{
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
        Map<String, String> userInfoGetView=new HashMap<>();
        try {
//            upload(this.getServletConfig(),request,response);
            uploadStream(request, response);
            JSONObject jsonObject = new JSONObject(userInfoGetView);
            out.print(jsonObject);
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
        finally {
			out.close();
		}
	}
	
	private void uploadStream(HttpServletRequest request, HttpServletResponse response){
		try {
			//1、创建一个DiskFileItemFactory工厂
			DiskFileItemFactory factory = new DiskFileItemFactory();
			//2、创建一个文件上传解析器
			ServletFileUpload upload = new ServletFileUpload(factory);
			//解决上传文件名的中文乱码
			upload.setHeaderEncoding("UTF-8"); 
			List<FileItem> list = upload.parseRequest(request);
			for(FileItem item : list){
                //如果fileitem中封装的是普通输入项的数据
                if(!item.getFieldName().equals("file")){
                    String name = item.getFieldName();
                    //解决普通输入项的数据的中文乱码问题
                    String value = item.getString("UTF-8");
                    value = new String(value.getBytes("iso8859-1"),"UTF-8");
                    System.out.println(name + "=" + value);
                }else{//如果fileitem中封装的是上传文件
                    //获取item中的上传文件的输入流
                    InputStream in = item.getInputStream();
                    //创建一个文件输出流
                    String id = UUID.randomUUID().toString();
                    FileOutputStream out = new FileOutputStream(FileUtil.getImageRealPathById(id));
                    //创建一个缓冲区
                    byte buffer[] = new byte[1024];
                    //判断输入流中的数据是否已经读完的标识
                    int len = 0;
                    //循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
                    while((len=in.read(buffer))>0){
                        //使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中
                        out.write(buffer, 0, len);
                    }
                    //关闭输入流
                    in.close();
                    //关闭输出流
                    out.close();
                    //删除处理文件上传时生成的临时文件
                    item.delete();
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
		
	}
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @SuppressWarnings("static-access")
	public Map<String, String> upload(ServletConfig config,HttpServletRequest request,HttpServletResponse response) {
		Map<String, String> map = new HashMap<>();
		String id = UUID.randomUUID().toString();
        try {    
            SmartUpload smartUpload = new SmartUpload();  
            smartUpload.initialize(config, request, response);    
            smartUpload.upload();  
            //获取其他的数据  
            Request otherReq = smartUpload.getRequest();  
            if(otherReq!=null){
            	String token = otherReq.getParameter("token");
            	//获取从客户端传输过来的用户名,在客户端中对参数进行参数URL编码了，所以服务器这里要进行URL解码  
            	if(StringUtil.isNotEmpty(token))
            	token= URLDecoder.decode(token,"utf-8");  
            }
            //获取上传的文件，因为知道在客户端一次就上传一个文件，所以我们就直接取第一个文件  
            com.jspsmart.upload.File smartFile = smartUpload.getFiles().getFile(0);    
            //判断文件是否丢失  
            if (!smartFile.isMissing()) {    
                //另保存至本地  
                smartFile.saveAs(FileUtil.getImageRealPathById(id), smartUpload.SAVE_PHYSICAL); 
                map.put("result", "1");
            }  
        } catch (Exception e) {
        	e.printStackTrace();
        }    
		return map;
	}
}
