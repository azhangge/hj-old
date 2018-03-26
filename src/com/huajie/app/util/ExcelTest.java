package com.huajie.app.util;

import java.io.File;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public final class ExcelTest {

	public static void main(String[] args) {
//		System.out.println(ConvertType1("D:\\excel\\试题库定模板.xls"));
		System.out.println(ConvertType2("D:\\excel\\试题库定模板.xls"));
//		System.out.println(ConvertType3("D:\\excel\\试题库定模板.xls"));
//		System.out.println(ConvertType4("D:\\excel\\试题库定模板.xls"));
//		System.out.println(ConvertType5("D:\\excel\\试题库定模板.xls"));
//		System.out.println(ConvertType6("D:\\excel\\试题库定模板.xls"));
	}
	
	public static String ConvertType1(String path){//单选题
		int error_id=0;
		try {
			Workbook book_old = Workbook.getWorkbook(new File(path));
			Sheet sheet_old = book_old.getSheet(0);
			int total_old = sheet_old.getRows();
			System.out.println(total_old);
			WritableWorkbook book_new = Workbook.createWorkbook(new File("D:\\excel\\单选题.xls"));
			WritableSheet sheet_new = book_new.createSheet("单选题",0);
			sheet_new.addCell(new Label(0, 0, "序号"));
			sheet_new.addCell(new Label(1, 0, "知识点名称"));
			sheet_new.addCell(new Label(2, 0, "题目"));
			sheet_new.addCell(new Label(3, 0, "正确答案"));
			sheet_new.addCell(new Label(4, 0, "选项A"));
			sheet_new.addCell(new Label(5, 0, "选项B"));
			sheet_new.addCell(new Label(6, 0, "选项C"));
			sheet_new.addCell(new Label(7, 0, "选项D"));
			sheet_new.addCell(new Label(8, 0, "选项E"));
			sheet_new.addCell(new Label(9, 0, "选项F"));
			sheet_new.addCell(new Label(10, 0, "试题解析"));
			sheet_new.addCell(new Label(11, 0, "综合题序号"));
			int index=1;
			
			for (int i = 1; i < total_old; i++) {
				error_id=i;
				String question_type=sheet_old.getCell(1, i).getContents().trim();//题目类型
				String question_content=sheet_old.getCell(2, i).getContents().trim();//题干
				String answer=sheet_old.getCell(3, i).getContents().trim();//正确答案
				String analysis=sheet_old.getCell(4, i).getContents().trim();//解析
				String knowledge_point=sheet_old.getCell(5, i).getContents().trim();//知识点
				
				if(question_type.equals("1单选题")){
					sheet_new.addCell(new Label(0, index, String.valueOf(index)));
					sheet_new.addCell(new Label(1, index, knowledge_point.substring(3, knowledge_point.length()).trim()));
					sheet_new.addCell(new Label(3, index, answer));
					
					if(question_content.contains("A.")&&question_content.contains("B.")){
						sheet_new.addCell(new Label(2, index, question_content.substring(0,question_content.indexOf("A.")).trim()));
						sheet_new.addCell(new Label(4, index, question_content.substring(question_content.indexOf("A.")+2, question_content.indexOf("B.")).trim()));
					}else if(question_content.contains("A．")&&question_content.contains("B．")){
						sheet_new.addCell(new Label(2, index, question_content.substring(0,question_content.indexOf("A．")).trim()));
						sheet_new.addCell(new Label(4, index, question_content.substring(question_content.indexOf("A．")+2, question_content.indexOf("B．")).trim()));
					}
					if(question_content.contains("B.")&&question_content.contains("C.")){
						sheet_new.addCell(new Label(5, index, question_content.substring(question_content.indexOf("B.")+2, question_content.indexOf("C.")).trim()));
					}else if(question_content.contains("B．")&&question_content.contains("C．")){
						sheet_new.addCell(new Label(5, index, question_content.substring(question_content.indexOf("B．")+2, question_content.indexOf("C．")).trim()));
					}
					if(question_content.contains("C.")&&question_content.contains("D.")){
						sheet_new.addCell(new Label(6, index, question_content.substring(question_content.indexOf("C.")+2, question_content.indexOf("D.")).trim()));
					}else if(question_content.contains("C．")&&question_content.contains("D．")){
						sheet_new.addCell(new Label(6, index, question_content.substring(question_content.indexOf("C．")+2, question_content.indexOf("D．")).trim()));
					}
					if(question_content.contains("D.")){
						sheet_new.addCell(new Label(7, index, question_content.substring(question_content.indexOf("D.")+2, question_content.length()).trim()));
					}else if(question_content.contains("D．")){
						sheet_new.addCell(new Label(7, index, question_content.substring(question_content.indexOf("D．")+2, question_content.length()).trim()));
					}
					sheet_new.addCell(new Label(10, index, analysis));
					index++;
				}
			}
			book_new.write();   
			book_new.close();
			book_old.close(); 
			return "单选题转换成功,共"+index+"题";
		} catch (Exception e) {
			e.printStackTrace();
			return "单选题"+error_id+"转换失败";
		}
	}
	
	public static String ConvertType2(String path){//多选题
		int error_id=0;
		try {
			Workbook book_old = Workbook.getWorkbook(new File(path));
			Sheet sheet_old = book_old.getSheet(0);
			int total_old = sheet_old.getRows();
			WritableWorkbook book_new = Workbook.createWorkbook(new File("D:\\excel\\多选题.xls"));
			WritableSheet sheet_new = book_new.createSheet("多选题",0);
			sheet_new.addCell(new Label(0, 0, "序号"));
			sheet_new.addCell(new Label(1, 0, "知识点名称"));
			sheet_new.addCell(new Label(2, 0, "题目"));
			sheet_new.addCell(new Label(3, 0, "正确答案"));
			sheet_new.addCell(new Label(4, 0, "选项A"));
			sheet_new.addCell(new Label(5, 0, "选项B"));
			sheet_new.addCell(new Label(6, 0, "选项C"));
			sheet_new.addCell(new Label(7, 0, "选项D"));
			sheet_new.addCell(new Label(8, 0, "选项E"));
			sheet_new.addCell(new Label(9, 0, "选项F"));
			sheet_new.addCell(new Label(10, 0, "试题解析"));
			sheet_new.addCell(new Label(11, 0, "综合题序号"));
			int index=1;
			
			for (int i = 1; i < total_old; i++) {
				error_id=i;
				String question_type=sheet_old.getCell(1, i).getContents().trim();//题目类型
				String question_content=sheet_old.getCell(2, i).getContents().trim();//题干
				String answer=sheet_old.getCell(3, i).getContents().trim();//正确答案
				String analysis=sheet_old.getCell(4, i).getContents().trim();//解析
				String knowledge_point=sheet_old.getCell(5, i).getContents().trim();//知识点
				
				if(question_type.equals("2多选题")){
					sheet_new.addCell(new Label(0, index, String.valueOf(index)));
					sheet_new.addCell(new Label(1, index, knowledge_point.substring(3, knowledge_point.length()).trim()));
					sheet_new.addCell(new Label(3, index, answer));
					
					if(question_content.contains("A.")&&question_content.contains("B.")){
						sheet_new.addCell(new Label(2, index, question_content.substring(0,question_content.indexOf("A.")).trim()));
						sheet_new.addCell(new Label(4, index, question_content.substring(question_content.indexOf("A.")+2, question_content.indexOf("B.")).trim()));
					}else if(question_content.contains("A．")&&question_content.contains("B．")){
						sheet_new.addCell(new Label(2, index, question_content.substring(0,question_content.indexOf("A．")).trim()));
						sheet_new.addCell(new Label(4, index, question_content.substring(question_content.indexOf("A．")+2, question_content.indexOf("B．")).trim()));
					}
					if(question_content.contains("B.")&&question_content.contains("C.")){
						sheet_new.addCell(new Label(5, index, question_content.substring(question_content.indexOf("B.")+2, question_content.indexOf("C.")).trim()));
					}else if(question_content.contains("B．")&&question_content.contains("C．")){
						sheet_new.addCell(new Label(5, index, question_content.substring(question_content.indexOf("B．")+2, question_content.indexOf("C．")).trim()));
					}
					if(question_content.contains("C.")&&question_content.contains("D.")){
						sheet_new.addCell(new Label(6, index, question_content.substring(question_content.indexOf("C.")+2, question_content.indexOf("D.")).trim()));
					}else if(question_content.contains("C．")&&question_content.contains("D．")){
						sheet_new.addCell(new Label(6, index, question_content.substring(question_content.indexOf("C．")+2, question_content.indexOf("D．")).trim()));
					}
					if(question_content.contains("D.")&&question_content.contains("E.")){
						sheet_new.addCell(new Label(7, index, question_content.substring(question_content.indexOf("D.")+2, question_content.indexOf("E.")).trim()));
					}else if(question_content.contains("D．")&&question_content.contains("E．")){
						sheet_new.addCell(new Label(7, index, question_content.substring(question_content.indexOf("D．")+2, question_content.indexOf("E．")).trim()));
					}
					if(question_content.contains("E.")){
						sheet_new.addCell(new Label(8, index, question_content.substring(question_content.indexOf("E.")+2, question_content.length()).trim()));
					}else if(question_content.contains("E．")){
						sheet_new.addCell(new Label(8, index, question_content.substring(question_content.indexOf("E．")+2, question_content.length()).trim()));
					}
					sheet_new.addCell(new Label(10, index, analysis));
					index++;
				}
			}
			book_new.write();   
			book_new.close();
			book_old.close();
			return "多选题转换成功,共"+index+"题";
		} catch (Exception e) {
			e.printStackTrace();
			return "多选题"+error_id+"转换失败";
		}
	}
	
	public static String ConvertType3(String path){//判断题
		int error_id=0;
		try {
			Workbook book_old = Workbook.getWorkbook(new File(path));
			Sheet sheet_old = book_old.getSheet(0);
			int total_old = sheet_old.getRows();
			WritableWorkbook book_new = Workbook.createWorkbook(new File("C:\\excel\\判断题.xls"));
			WritableSheet sheet_new = book_new.createSheet("判断题",0);
			sheet_new.addCell(new Label(0, 0, "序号"));
			sheet_new.addCell(new Label(1, 0, "知识点名称"));
			sheet_new.addCell(new Label(2, 0, "题目"));
			sheet_new.addCell(new Label(3, 0, "正确答案"));
			sheet_new.addCell(new Label(4, 0, "试题解析"));
			sheet_new.addCell(new Label(5, 0, "综合题序号"));
			int index=1;
			
			for (int i = 1; i < total_old; i++) {
				error_id=i;
				String question_type=sheet_old.getCell(1, i).getContents().trim();//题目类型
				String question_content=sheet_old.getCell(2, i).getContents().trim();//题干
				String answer=sheet_old.getCell(3, i).getContents().trim();//正确答案
				String analysis=sheet_old.getCell(4, i).getContents().trim();//解析
				String knowledge_point=sheet_old.getCell(5, i).getContents().trim();//知识点
				
				if(question_type.equals("3判断题")){
					sheet_new.addCell(new Label(0, index, String.valueOf(index)));
					sheet_new.addCell(new Label(1, index, knowledge_point.substring(3, knowledge_point.length())));
					sheet_new.addCell(new Label(2, index, question_content));
					if(answer.equals("√")){
						sheet_new.addCell(new Label(3, index, "true"));
					}
					if(answer.equals("×")){
						sheet_new.addCell(new Label(3, index, "false"));
					}
					sheet_new.addCell(new Label(4, index, analysis));
					index++;
				}
			}
			book_new.write();   
			book_new.close();
			book_old.close();
			return "判断题转换成功,共"+index+"题";
		} catch (Exception e) {
			e.printStackTrace();
			return "判断题"+error_id+"转换失败";
		}
	}
	
	public static String ConvertType4(String path){//填空题
		int error_id=0;
		try {
			Workbook book_old = Workbook.getWorkbook(new File(path));
			Sheet sheet_old = book_old.getSheet(0);
			int total_old = sheet_old.getRows();
			WritableWorkbook book_new = Workbook.createWorkbook(new File("C:\\excel\\填空题.xls"));
			WritableSheet sheet_new = book_new.createSheet("填空题",0);
			sheet_new.addCell(new Label(0, 0, "序号"));
			sheet_new.addCell(new Label(1, 0, "知识点名称"));
			sheet_new.addCell(new Label(2, 0, "题目（填空内容写在[]中）"));
			sheet_new.addCell(new Label(3, 0, "试题解析"));
			sheet_new.addCell(new Label(4, 0, "综合题序号"));
			int index=1;
			
			for (int i = 1; i < total_old; i++) {
				error_id=i;
				String question_type=sheet_old.getCell(1, i).getContents().trim();//题目类型
				String question_content=sheet_old.getCell(2, i).getContents().trim();//题干
				String answer=sheet_old.getCell(3, i).getContents().trim();//正确答案
				String analysis=sheet_old.getCell(4, i).getContents().trim();//解析
				String knowledge_point=sheet_old.getCell(5, i).getContents().trim();//知识点
				
				if(question_type.equals("4填空题")){
					sheet_new.addCell(new Label(0, index, String.valueOf(index)));
					sheet_new.addCell(new Label(1, index, knowledge_point.substring(3, knowledge_point.length()).trim()));
					if(question_content.contains("（")&&question_content.contains("）")){
						String result=question_content.replaceAll("（", "["+answer).replaceAll("）", "]");
						if(result.contains(" ")){
							result=result.replaceAll(" ","");
						}
						sheet_new.addCell(new Label(2, index, result));
					}
					sheet_new.addCell(new Label(3, index, analysis));
					index++;
				}
			}
			book_new.write();   
			book_new.close();
			book_old.close();
			return "填空题转换成功,共"+index+"题";
		} catch (Exception e) {
			e.printStackTrace();
			return "填空题"+error_id+"转换失败";
		}
	}
	
	public static String ConvertType5(String path){//问答题
		int error_id=0;
		try {
			Workbook book_old = Workbook.getWorkbook(new File(path));
			Sheet sheet_old = book_old.getSheet(0);
			int total_old = sheet_old.getRows();
			WritableWorkbook book_new = Workbook.createWorkbook(new File("C:\\excel\\问答题.xls"));
			WritableSheet sheet_new = book_new.createSheet("问答题 ",0);
			sheet_new.addCell(new Label(0, 0, "序号"));
			sheet_new.addCell(new Label(1, 0, "知识点名称"));
			sheet_new.addCell(new Label(2, 0, "题目"));
			sheet_new.addCell(new Label(3, 0, "正确答案"));
			sheet_new.addCell(new Label(4, 0, "试题解析"));
			sheet_new.addCell(new Label(5, 0, "综合题序号"));
			int index=1;
			
			for (int i = 1; i < total_old; i++) {
				error_id=i;
				String question_type=sheet_old.getCell(1, i).getContents().trim();//题目类型
				String question_content=sheet_old.getCell(2, i).getContents().trim();//题干
				String answer=sheet_old.getCell(3, i).getContents().trim();//正确答案
				String analysis=sheet_old.getCell(4, i).getContents().trim();//解析
				String knowledge_point=sheet_old.getCell(5, i).getContents().trim();//知识点
				
				if(question_type.equals("5简答题")){
					sheet_new.addCell(new Label(0, index, String.valueOf(index)));
					sheet_new.addCell(new Label(1, index, knowledge_point.substring(3, knowledge_point.length()).trim()));
					sheet_new.addCell(new Label(2, index, question_content));
					sheet_new.addCell(new Label(3, index, answer));
					sheet_new.addCell(new Label(4, index, analysis));
					index++;
				}
			}
			book_new.write();   
			book_new.close();
			book_old.close();
			return "问答题转换成功,共"+index+"题";
		} catch (Exception e) {
			e.printStackTrace();
			return "问答题"+error_id+"转换失败";
		}
	}
	
	public static String ConvertType6(String path){//6案例题
		int error_id=0;
		try {
			Workbook book_old = Workbook.getWorkbook(new File(path));
			Sheet sheet_old = book_old.getSheet(0);
			int total_old = sheet_old.getRows();
			WritableWorkbook book_new = Workbook.createWorkbook(new File("C:\\excel\\案例题.xls"));
			WritableSheet sheet_new = book_new.createSheet("案例题 ",0);
			sheet_new.addCell(new Label(0, 0, "序号"));
			sheet_new.addCell(new Label(1, 0, "知识点名称"));
			sheet_new.addCell(new Label(2, 0, "题目"));
			sheet_new.addCell(new Label(3, 0, "正确答案"));
			sheet_new.addCell(new Label(4, 0, "试题解析"));
			sheet_new.addCell(new Label(5, 0, "综合题序号"));
			int index=1;
			
			for (int i = 1; i < total_old; i++) {
				error_id=i;
				String question_type=sheet_old.getCell(1, i).getContents().trim();//题目类型
				String question_content=sheet_old.getCell(2, i).getContents().trim();//题干
				String answer=sheet_old.getCell(3, i).getContents().trim();//正确答案
				String analysis=sheet_old.getCell(4, i).getContents().trim();//解析
				String knowledge_point=sheet_old.getCell(5, i).getContents().trim();//知识点
				
				if(question_type.equals("6案例题")){
					sheet_new.addCell(new Label(0, index, String.valueOf(index)));
					sheet_new.addCell(new Label(1, index, knowledge_point.substring(3, knowledge_point.length()).trim()));
					sheet_new.addCell(new Label(2, index, question_content));
					sheet_new.addCell(new Label(3, index, answer));
					sheet_new.addCell(new Label(4, index, analysis));
					index++;
				}
			}
			book_new.write();   
			book_new.close();
			book_old.close();
			return "问答题转换成功,共"+index+"题";
		} catch (Exception e) {
			e.printStackTrace();
			return "问答题"+error_id+"转换失败";
		}
	}
	
}
