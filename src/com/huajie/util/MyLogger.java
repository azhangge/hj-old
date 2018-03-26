package com.huajie.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.huajie.app.servlet.AppServlet;
import com.huajie.app.util.DateUtil;
import com.huajie.app.util.StringUtil;

public class MyLogger {

    public static boolean debug = true;
    public static String logPath = "C:\\exam_log\\debug_log.txt";
    private static final Logger logger = Logger.getLogger(MyLogger.class);
    public static void echo(String s) {
        if (debug) {
            System.out.println(s);
        }
    }

    public static void echo(String s, Exception e) {
        if (debug) {
            System.out.println(s);
            e.printStackTrace();
        }
    }

    public static void println(String s) {
        if (debug) {
//            System.out.println(s);
            try {
            	File f = new File("C:\\exam_log");
            	if(!f.exists()){
            		f.mkdirs();
            	}
                File ff = new File(logPath);
                if(!ff.exists()){
                	ff.createNewFile();
                }
                OutputStream os = new BufferedOutputStream(new FileOutputStream(ff, true));
                PrintWriter pw = new PrintWriter(os);
                pw.append(s);
                pw.append("\r\n");
                pw.close();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
    public static void log(Throwable e){
//    	println(DateUtil.formateDataToString(new Date())+"后台异常："+StringUtil.eToString(e));
    	logger.error(StringUtil.eToString(e));
    }
    
    /**
     * 将异常信息输出到error文件中
     * @param e
     */
    public static void error(Throwable e){
    	logger.error(StringUtil.eToString(e));
    }

    /**
     * 将MAP输出到文件
     *
     * @param map
     */
    public static void explainMap(Map<String, String[]> map) {
        if (debug) {
            System.out.println();
            try {
                File ff = new File(logPath);
                OutputStream os = new BufferedOutputStream(new FileOutputStream(ff, true));
                PrintWriter pw = new PrintWriter(os);
                Set<String> set = map.keySet();
                for (String key : set) {
                    pw.print(key + " ::::: ");
                    String[] vs = map.get(key);
                    if (vs != null) {
                        for (String v : vs) {
                            pw.print(v +"   ////  ");
                        }
                    }
                    pw.println();
                }

                pw.close();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String args[]) {
        MyLogger.println("hi");

    }

}
