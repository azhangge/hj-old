package com.huajie.app.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;

/** 
 * doc docx ppt pptx格式转换 
 */  
public class DocConverter {  
    private static final int environment = 1;// 环境 1：windows 2:linux  
    private String docFilePath;
    private String pdfFilePath;
    private String outputPath = "";// 输入路径 ，如果不设置就输出在默认的位置  
    private String filePath;  
    private String fileName;
    private File pdfFile;  
    private File docFile;  
    
    public DocConverter(String docFilePath,String pdfFilePath) {  
        ini(docFilePath,pdfFilePath);
    }  
  
    /** 
     * 重新设置file 
     *  
     * @param fileString 
     */  
    public void setFile(String docFilePath,String pdfFilePath) {  
        ini(docFilePath,pdfFilePath);  
    }  
    
    static String loadStream(InputStream in) throws IOException {  
        int ptr = 0;  
        in = new BufferedInputStream(in);  
        StringBuffer buffer = new StringBuffer();  
        while ((ptr = in.read()) != -1) {  
            buffer.append((char) ptr);  
        }  
        return buffer.toString();  
    }
    
    /** 
     * 初始化 
     *  
     * @param fileString 
     */  
    private void ini(String docFilePath,String pdfFilePath) {  
        this.docFilePath = docFilePath;  
        filePath = docFilePath.substring(0, docFilePath.lastIndexOf("."));
        String docTempFilePath = docFilePath.replaceAll("\\\\", "/"); 
        fileName = docTempFilePath.substring(docTempFilePath.lastIndexOf("/"),docTempFilePath.lastIndexOf("."));
        docFile = new File(docFilePath);   
        pdfFile = new File(pdfFilePath + fileName +".pdf");  
    }  
      
    /** 
     * 转为PDF 
     *  
     * @param file 
     */  
    private void doc2pdf(String host,int port) throws Exception {  
        if (docFile.exists()) {  
            if (!pdfFile.exists()) {  
                OpenOfficeConnection connection = new SocketOpenOfficeConnection(host,port);  
                try {  
                    connection.connect();  
                    DocumentConverter converter = new OpenOfficeDocumentConverter(connection);  
//                    DocumentConverter converter =new StreamOpenOfficeDocumentConverter(connection);  
                    converter.convert(docFile, pdfFile);  
                    // close the connection  
                    connection.disconnect();  
                    System.out.println("****pdf转换成功，PDF输出：" + pdfFile.getPath()+ "****");  
                } catch (java.net.ConnectException e) {  
                    e.printStackTrace();  
                    System.out.println("****pdf转换器异常，openoffice服务未启动！****");  
                    throw e;  
                } catch (com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException e) {  
                    e.printStackTrace();  
                    System.out.println("****pdf转换器异常，读取转换文件失败****");  
                    throw e;  
                } catch (Exception e) {  
                    e.printStackTrace();  
                    throw e;  
                }  
            } else {  
                System.out.println("****已经转换为pdf，不需要再进行转化****");  
            }  
        } else {  
            System.out.println("****pdf转换器异常，需要转换的文档不存在，无法转换****");  
        }  
    }  

  
  
    /** 
     * 转换主方法 
     */  
    @SuppressWarnings("unused")  
    public boolean conver(String host,int port) {  
        try {  
            doc2pdf(host,port);
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;  
        }  
        return true;
    }  
  
    /** 
     * 返回文件路径 
     *  
     * @param s 
     */  
    public String getpdfPath() {  
        if (pdfFile.exists()) {  
            String tempString = pdfFile.getPath();  
            tempString = tempString.replaceAll("\\\\", "/");  
            return tempString;  
        } else {  
            return "";  
        }  
    }

    public static void main(String[] args) {
    	DocConverter d = new DocConverter("C:\\huajie_exam/docx/06f5b1e5-1c5b-483e-acbc-f283535f4664.docx","C:\\huajie_exam/pdf/");  
    	d.conver("192.168.1.245",8100);  
//    	String yun_host = "http://192.168.1.245:8080/ucenter";
//    	String result = yun_host.substring(7,yun_host.lastIndexOf(":"));
//    	System.out.println(result);
//    	String a="C:\\huajie_exam/user_documents/06f5b1e5-1c5b-483e-acbc-f283535f4664.docx";
//    	a = a.replaceAll("\\\\", "/"); 
//    	File f=new File(a);
//    	if(f.exists()){
//    		System.out.println(1);
//    	}else{
//    		System.out.println(2);
//    	}
	}
} 