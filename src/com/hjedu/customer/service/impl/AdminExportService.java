package com.hjedu.customer.service.impl;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import com.hjedu.customer.dao.IAdminDAO;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.dao.IDictionaryDAO;
import com.huajie.app.util.DateUtil;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class AdminExportService implements Serializable {

    IAdminDAO adminDAO = SpringHelper.getSpringBean("AdminDAO");
    IDictionaryDAO dicDAO = SpringHelper.getSpringBean("DictionaryDAO");
    ComplexBbsUserService cbus = SpringHelper.getSpringBean("ComplexBbsUserService");

    public void export(String nfn,AdminInfo ai) {
        try {

            WritableWorkbook book = Workbook.createWorkbook(new File(nfn));
            this.exportAdmin(book,ai);
            book.write();
            book.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportAdmin(WritableWorkbook book,AdminInfo ai) {
        try {
            // 生成名为“单选题”的作业表，参数0表明这是单选题
            WritableSheet sheet1 = book.createSheet("用户", 0);
            Label label0 = new Label(0, 0, "用户名");
            sheet1.addCell(label0);
            Label label1 = new Label(1, 0, "称呼");
            sheet1.addCell(label1);
            //设置该列宽度
            sheet1.setColumnView(1, 20);
            Label label2 = new Label(2, 0, "角色名");
            sheet1.addCell(label2);
            sheet1.setColumnView(2, 20);
            Label label3 = new Label(3, 0, "权限");
            sheet1.setColumnView(3, 50);
            sheet1.addCell(label3);
            Label label4 = new Label(4, 0, "用户组");
            sheet1.addCell(label4);
            sheet1.setColumnView(4, 20);
            Label label5 = new Label(5, 0, "是否启用");
            sheet1.addCell(label5);
            sheet1.setColumnView(5, 20);
            Label label6 = new Label(6, 0, "上次登录时间");
            sheet1.addCell(label6);
            sheet1.setColumnView(6, 20);

            int i1 = 1;
            List<AdminInfo> users = this.adminDAO.findAdminsBySuperId(ai.getId());
            if(users!=null)
            for (AdminInfo em : users) {
                Label label00 = new Label(0, i1, em.getUrn());
                sheet1.addCell(label00);
                Label label01 = new Label(1, i1, em.getNickname());
                sheet1.addCell(label01);
                Label label02 = new Label(2, i1, em.getPersona());
                sheet1.addCell(label02);
                Label label03 = new Label(3, i1, em.getAuthCnStr());
                sheet1.addCell(label03);
                Label label04 = new Label(4, i1, em.getGrp().equals("admin")?"超级管理员":"管理员");
                sheet1.addCell(label04);
                Label label05 = new Label(5, i1, em.getEnabled()?"是":"否");
                sheet1.addCell(label05);
                Label label06 = new Label(6, i1, DateUtil.formateDateToString(em.getLtime()));
                sheet1.addCell(label06);
                i1++;
            }
            MyLogger.echo("用户数量：" + (sheet1.getRows()-1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
