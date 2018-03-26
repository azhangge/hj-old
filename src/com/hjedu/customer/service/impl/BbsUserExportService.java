package com.hjedu.customer.service.impl;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import com.hjedu.customer.UserUtil;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IDictionaryDAO;
import com.hjedu.examination.entity.DictionaryModel;
import com.huajie.util.CookieUtils;
import com.huajie.util.DESTool;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class BbsUserExportService implements Serializable {

    IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
    IDictionaryDAO dicDAO = SpringHelper.getSpringBean("DictionaryDAO");
    ComplexBbsUserService cbus = SpringHelper.getSpringBean("ComplexBbsUserService");
    
    
    
    public void export(String nfn,AdminInfo admin) {
        try {

            WritableWorkbook book = Workbook.createWorkbook(new File(nfn));
            this.exportBbsUser(book,admin);
            book.write();
            book.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportBbsUser(WritableWorkbook book,AdminInfo admin) {
    	String bussinessId = admin.getBusinessId();
        try {
            // 生成名为“单选题”的作业表，参数0表明这是单选题
            WritableSheet sheet1 = book.createSheet("用户", 0);
            Label label0 = new Label(0, 0, "序号");
            sheet1.addCell(label0);
            sheet1.setColumnView(0, 20);
            Label label1 = new Label(1, 0, "手机号（确保唯一性）");
            sheet1.addCell(label1);
            sheet1.setColumnView(1, 20);
            Label label2 = new Label(2, 0, "用户名（确保唯一性）");
            sheet1.addCell(label2);
            sheet1.setColumnView(2, 20);
            Label label3 = new Label(3, 0, "身份证号（确保唯一性）");
            sheet1.addCell(label3);
            sheet1.setColumnView(3, 50);
            Label label4 = new Label(4, 0, "E-MAIL（确保唯一性）");
            sheet1.addCell(label4);
            sheet1.setColumnView(4, 20);
            Label label5 = new Label(5, 0, "密码");
            sheet1.addCell(label5);
            sheet1.setColumnView(5, 20);
            Label label6 = new Label(6, 0, "积分");
            sheet1.addCell(label6);
            sheet1.setColumnView(6, 20);
            Label label7 = new Label(7, 0, "真实姓名");
            sheet1.addCell(label7);
            sheet1.setColumnView(7, 20);
            Label label8 = new Label(8, 0, "性别");
            sheet1.addCell(label8);
            sheet1.setColumnView(8, 20);

            int i1 = 1;
            List<BbsUser> users = this.bbsUserDAO.findAllBbsUser(bussinessId);
            
            for (BbsUser em : users) {
                String group = "";
                List<DictionaryModel> dms = em.getGroups();
                if (dms != null) {
                    for (DictionaryModel dm : dms) {
                        group += dm.getName()+",";
                    }
                }
                String tel = em.getTel();
                String username = em.getUsername();
                String pid = em.getPid();
                String email = em.getEmail();   
                DESTool dt = new DESTool();
                String pwd = "";
                if(em.getPassword() != null){
                	pwd = dt.decrypt(em.getPassword());
                }
                String score = em.getScore()+"";
                String name = em.getName();
                String gender = em.getGender();

                Label label00 = new Label(0, i1, String.valueOf(i1));
                sheet1.addCell(label00);
                Label label01 = new Label(1, i1,  tel);
                sheet1.addCell(label01);
                Label label02 = new Label(2, i1, username);
                sheet1.addCell(label02);
                Label label03 = new Label(3, i1, pid);
                sheet1.addCell(label03);
                Label label04 = new Label(4, i1, email);
                sheet1.addCell(label04);
                Label label05 = new Label(5, i1, pwd);
                sheet1.addCell(label05);
                Label label06 = new Label(6, i1, score);
                sheet1.addCell(label06);
                Label label07 = new Label(7, i1, name);
                sheet1.addCell(label07);
                Label label08 = new Label(8, i1, gender);
                sheet1.addCell(label08);
                i1++;

            }
            MyLogger.echo("用户数量：" + (sheet1.getRows()-1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        BbsUserExportService ees = new BbsUserExportService();
    }
}
