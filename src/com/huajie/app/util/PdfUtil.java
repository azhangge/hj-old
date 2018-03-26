package com.huajie.app.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.log4j.Logger;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfUtil {
	private static final Logger log4j = Logger.getLogger(PdfUtil.class);
	
	public static boolean CrackCopy(String src,String dest){
		PdfReader.unethicalreading = true;
		PdfReader reader;
		try {
			reader = new PdfReader(new FileInputStream(src));
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
			stamper.setEncryption(null, null, PdfWriter.ALLOW_COPY, false);
			stamper.close();
	        reader.close();
	        return true;
		} catch (Exception e) {
			e.printStackTrace();
			log4j.error(StringUtil.eToString(e));
		} 
		return false;
	}
}
