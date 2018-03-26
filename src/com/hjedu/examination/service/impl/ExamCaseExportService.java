package com.hjedu.examination.service.impl;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;

import com.hjedu.examination.dao.IDictionaryDAO;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.platform.dao.ComplexDepartmentLogic;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExamCaseExportService implements Serializable {

    IExamCaseDAO caseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
    //IDictionaryDAO dicDAO = SpringHelper.getSpringBean("DictionaryDAO");
    ComplexDepartmentLogic cdLogic = SpringHelper.getSpringBean("ComplexDepartmentLogic");

    public void export(String nfn, String businessId) {
        try {

            WritableWorkbook book = Workbook.createWorkbook(new File(nfn));
            List<ExamCase> users = this.caseDAO.findAllSubmittedExamCase();
            this.exportExamCase(book, users,businessId);
            book.write();
            book.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exportComplex(String nfn, List<ExamCase> users) {
        try {

            WritableWorkbook book = Workbook.createWorkbook(new File(nfn));
            this.exportExamCase(book, users, null);
            book.write();
            book.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportExamCase(WritableWorkbook book, List<ExamCase> users,String businessId) {
        try {
            // 生成名为“单选题”的作业表，参数0表明这是单选题
            WritableSheet sheet1 = book.createSheet("考试成绩", 0);
            Label label0 = new Label(0, 0, "序号");
            sheet1.addCell(label0);
            Label label1 = new Label(1, 0, "身份证号");
            sheet1.addCell(label1);
            sheet1.setColumnView(1, 50);
            Label label2 = new Label(2, 0, "学号/工号");
            sheet1.addCell(label2);
            sheet1.setColumnView(2, 20);
            Label label3 = new Label(3, 0, "姓名");
            sheet1.addCell(label3);
            sheet1.setColumnView(3, 20);
            Label label4 = new Label(4, 0, "用户名");
            sheet1.addCell(label4);
            Label label5 = new Label(5, 0, "性别");
            sheet1.addCell(label5);
            Label label6 = new Label(6, 0, "手机/电话");
            sheet1.addCell(label6);
            sheet1.setColumnView(6, 20);
            Label label7 = new Label(7, 0, "考试名称");
            sheet1.addCell(label7);
            sheet1.setColumnView(7, 20);
            Label label8 = new Label(8, 0, "开始时间");
            sheet1.addCell(label8);
            sheet1.setColumnView(8, 20);
            Label label9 = new Label(9, 0, "交卷时间");
            sheet1.addCell(label9);
            sheet1.setColumnView(9, 20);
            Label label10 = new Label(10, 0, "单选类得分");
            sheet1.addCell(label10);
            Label label11 = new Label(11, 0, "多选题得分");
            sheet1.addCell(label11);
            Label label12 = new Label(12, 0, "填空题得分");
            sheet1.addCell(label12);
            Label label13 = new Label(13, 0, "判断题得分");
            sheet1.addCell(label13);
            Label label14 = new Label(14, 0, "问答题得分");
            sheet1.addCell(label14);
            Label label15 = new Label(15, 0, "文件题得分");
            sheet1.addCell(label15);
            Label label16 = new Label(16, 0, "综合题得分");
            sheet1.addCell(label16);
            Label label17 = new Label(17, 0, "总分");
            sheet1.addCell(label17);
            Label label18 = new Label(18, 0, "满分");
            sheet1.addCell(label18);
            Label label19 = new Label(19, 0, "所在部门");
            sheet1.addCell(label19);
            
            Label label20 = new Label(20, 0, "职务");
            sheet1.addCell(label20);

            int i1 = 1;

            for (ExamCase em : users) {
                //String group = em.getUser().getGroupCnStr();
            	if(em==null||em.getUser()==null){
            		continue;
            	}
                String groups = this.cdLogic.genFamilyTreeCnStrByIds(em.getUser().getGroupStr(), false,businessId);

                String pid = em.getUser().getPid();
                String cid = em.getUser().getCid();
                String urn = em.getUser().getUsername();
                String name = em.getUser().getName();
                String gender = em.getUser().getGender();
                String tel = em.getUser().getTel();

                String emName = em.getExamination().getName();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String begain = sdf.format(em.getGenTime());
                String end = sdf.format(em.getSubmitTime());

                Double cscore = em.getChoiceScore();
                Double mscore = em.getMultiChoiceScore();
                Double fillScore = em.getFillScore();
                Double jScore = em.getJudgeScore();
                Double eScore = em.getEssayScore();
                Double fileScore = em.getFileScore();
                Double caseScore = em.getCaseScore();
                Double totalScore = em.getScore();
                Double totalFullScore = em.getTotalFullScore();
                String position=em.getUser().getPosition();

                Label label00 = new Label(0, i1, String.valueOf(i1));
                sheet1.addCell(label00);
                Label label01 = new Label(1, i1, pid);
                sheet1.addCell(label01);
                Label label02 = new Label(2, i1, cid);
                sheet1.addCell(label02);
                Label label03 = new Label(3, i1, name);
                sheet1.addCell(label03);
                Label label04 = new Label(4, i1, urn);
                sheet1.addCell(label04);
                Label label05 = new Label(5, i1, gender);
                sheet1.addCell(label05);
                Label label06 = new Label(6, i1, tel);
                sheet1.addCell(label06);
                Label label07 = new Label(7, i1, emName);
                sheet1.addCell(label07);
                Label label08 = new Label(8, i1, begain);
                sheet1.addCell(label08);
                Label label09 = new Label(9, i1, end);
                sheet1.addCell(label09);
                Label label010 = new Label(10, i1, String.valueOf(cscore));
                sheet1.addCell(label010);
                Label label011 = new Label(11, i1, String.valueOf(mscore));
                sheet1.addCell(label011);
                Label label012 = new Label(12, i1, fillScore.toString());
                sheet1.addCell(label012);
                Label label013 = new Label(13, i1, jScore.toString());
                sheet1.addCell(label013);
                Label label014 = new Label(14, i1, eScore.toString());
                sheet1.addCell(label014);
                Label label015 = new Label(15, i1, fileScore.toString());
                sheet1.addCell(label015);
                Label label016 = new Label(16, i1, caseScore.toString());
                sheet1.addCell(label016);
                Label label017 = new Label(17, i1, totalScore.toString());
                sheet1.addCell(label017);
                Label label018 = new Label(18, i1, totalFullScore.toString());
                sheet1.addCell(label018);

                Label labeltt = new Label(19, i1, groups);
                sheet1.addCell(labeltt);
                
                Label labelPo = new Label(20, i1, position);
                sheet1.addCell(labelPo);

                i1++;

            }
            MyLogger.println("导出的成绩数：" + (sheet1.getRows()-1));
        } catch (Exception e) {
            e.printStackTrace();
            MyLogger.println(e.toString());
        }
    }

    public static void main(String args[]) {
        ExamCaseExportService ees = new ExamCaseExportService();
        IExamCaseDAO caseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
        List<ExamCase> users = caseDAO.findAllSubmittedExamCase();
        ees.exportComplex("G:\\work\\exam3\\build\\web\\upload\\RereExamComplex1438918806883.xls",users);
    }
}
