package com.hjedu.platform.controller;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.activation.MimetypesFileTypeMap;
import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.imageio.stream.FileImageOutputStream;

import org.primefaces.event.CaptureEvent;
import org.primefaces.json.JSONObject;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.controller.ExamCaseGeneral;
import com.hjedu.examination.entity.ExamCase;
import com.huajie.app.util.DateUtil;
import com.huajie.app.util.FileUtil;
import com.huajie.app.util.StringUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class PhotoCamView implements Serializable{

    String caseId;
    String userName;
    String filename;
    
    long random=System.nanoTime();

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getRandom() {
        return random;
    }

    public void setRandom(long random) {
        this.random = random;
    }

    /**
     * 计算头像文件名
     * @return 
     */
    private String fetchFileName() {
        this.filename = userName + caseId;
        return this.filename;
    }

    public void oncapture(CaptureEvent captureEvent) {
        random=System.nanoTime();
        ExamCaseGeneral ecg = JsfHelper.getBean("examCaseGeneral");
        this.caseId=ecg.getExamCase().getId();
        BbsUser user = ((ClientSession)JsfHelper.getBean("clientSession")).getUsr();
        this.userName=DateUtil.formateDateToCNString(new Date(),true)+user.getName();
        
        byte[] data = captureEvent.getData();
//        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
//        String newFileName = servletContext.getRealPath("") + File.separator + "upload"
//                + File.separator + "photocams" + File.separator + this.fetchFileName() + ".jpeg";
        File file1 = new File(FileUtil.getSysFilePath() + File.separator + "photocams");
        if(!file1.exists()){
        	file1.mkdirs();
        }
//        final String newFileName = "d:" + File.separator + "photocams" + File.separator + this.fetchFileName() + ".jpeg";
        String newFileName = file1.getPath() + File.separator + userName + ".jpeg";
        FileImageOutputStream imageOutput;
        try {
            imageOutput = new FileImageOutputStream(new File(newFileName));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            String id = StringUtil.getStringAfter(user.getPicUrl(),"?id=");
            String path = FileUtil.getImageRealPathById(id);
            File f1 = new File(path);
            if(!f1.exists()){
            	JsfHelper.info("请上传用户图片！");
            	return;
            }
            checkUserPhoto(FileUtil.getImageRealPathById(id), newFileName);
        } catch (IOException e) {
            throw new FacesException("Error in writing captured image.", e);
        }
    }
    
    /**
     * 上传图片
     * @param urlStr
     * @param textMap
     * @param fileMap
     * @param contentType 没有传入文件类型默认采用application/octet-stream
     * contentType非空采用filename匹配默认的图片类型
     * @return 返回response数据
     */
    @SuppressWarnings("rawtypes")
    public static String formUpload(String urlStr, Map<String, String> textMap,
            Map<String, String> fileMap,String contentType) {
        String res = "";
        HttpURLConnection conn = null;
        // boundary就是request头和上传文件内容的分隔符
        String BOUNDARY = "---------------------------123821742118716"; 
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + BOUNDARY);
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // text
            if (textMap != null) {
                StringBuffer strBuf = new StringBuffer();
                Iterator iter = textMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    strBuf.append("\r\n").append("--").append(BOUNDARY)
                            .append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\""
                            + inputName + "\"\r\n\r\n");
                    strBuf.append(inputValue);
                }
                out.write(strBuf.toString().getBytes());
            }
            // file
            if (fileMap != null) {
                for (Entry<String, String> entry : fileMap.entrySet()) {
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    File file = new File(inputValue);
                    String filename = file.getName();
                    
                    //没有传入文件类型，同时根据文件获取不到类型，默认采用application/octet-stream
                    contentType = new MimetypesFileTypeMap().getContentType(file);
                    //contentType非空采用filename匹配默认的图片类型
                    if(!"".equals(contentType)){
                        if (filename.endsWith(".png")) {
                            contentType = "image/png"; 
                        }else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".jpe")) {
                            contentType = "image/jpeg";
                        }else if (filename.endsWith(".gif")) {
                            contentType = "image/gif";
                        }else if (filename.endsWith(".ico")) {
                            contentType = "image/image/x-icon";
                        }
                    }
                    if (contentType == null || "".equals(contentType)) {
                        contentType = "application/octet-stream";
                    }
                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(BOUNDARY)
                            .append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\""
                            + inputName + "\"; filename=\"" + filename
                            + "\"\r\n");
                    strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
                    out.write(strBuf.toString().getBytes());
                    DataInputStream in = new DataInputStream(
                            new FileInputStream(file));
                    int bytes = 0;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    in.close();
                }
            }
            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();
            // 读取返回数据
            StringBuffer strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            res = strBuf.toString();
            reader.close();
            reader = null;
        } catch (Exception e) {
            System.out.println("发送POST请求出错。" + urlStr);
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }
    
    public void checkUserPhoto(String fileName1,String fileName2) throws IOException {
    	String url = "https://api-cn.faceplusplus.com/facepp/v3/compare";
//        String fileName1 = "D:/photocams/3.jpeg";
//        String fileName2 = "D:/photocams/2.jpeg";
        Map<String, String> textMap = new HashMap<String, String>();
        //可以设置多个input的name，value
        textMap.put("api_key", "hqvXYgkb7xIQ4kd01aMXMMLPgHRUyDCO");
        textMap.put("api_secret", "k4TT0BGrt2bEmrOZwU5Y28ECkkLU4nPn");
        textMap.put("api_secret", "k4TT0BGrt2bEmrOZwU5Y28ECkkLU4nPn");
        //设置file的name，路径
        Map<String, String> fileMap = new HashMap<String, String>();
        fileMap.put("image_file1", fileName1);
        fileMap.put("image_file2", fileName2);
        String contentType = "";//image/png
        try {
        	String ret = formUpload(url, textMap, fileMap,contentType);
        	System.out.println(ret);
        	JSONObject myJsonObject = new JSONObject(ret);
        	if(myJsonObject.has("confidence")){
        		double confidence = myJsonObject.getDouble("confidence");
        		if(confidence>75){
        			System.out.println(confidence+":是同一人");
        			JsfHelper.info("考生图像与上传照片符合！");
        		}else{
        			JsfHelper.info("请注意，正在考试的可能不是本人！");
        			System.out.println(confidence+":不是同一人");
        		}
        	}
        	//{"status":"0","message":"add succeed","baking_url":"group1\/M00\/00\/A8\/CgACJ1Zo-LuAN207AAQA3nlGY5k151.png"}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
