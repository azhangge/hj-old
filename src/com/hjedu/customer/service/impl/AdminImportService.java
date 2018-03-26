package com.hjedu.customer.service.impl;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import com.hjedu.customer.dao.IAdminDAO;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IDictionaryDAO;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.service.impl.ExamDepartmentService;
import com.hjedu.platform.dao.ComplexDepartmentLogic;
import com.huajie.app.util.StringUtil;
import com.huajie.util.CookieUtils;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

import jxl.Sheet;
import jxl.Workbook;

public class AdminImportService implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	IAdminDAO adminDAO = SpringHelper.getSpringBean("AdminDAO");
    //IDictionaryDAO dicDAO = SpringHelper.getSpringBean("DictionaryDAO");
    ExamDepartmentService dicDAO = SpringHelper.getSpringBean("ExamDepartmentService");
    ComplexDepartmentLogic complex3 = SpringHelper.getSpringBean("ComplexDepartmentLogic");
    ComplexBbsUserService cbus = SpringHelper.getSpringBean("ComplexBbsUserService");

    public void import1(String nfn) throws Exception {
        try {
            Workbook book = Workbook.getWorkbook(new File(nfn));
            this.importBbsUser(book);
            book.close();
        } catch (Exception e) {
            throw e;
        }
    }

    private void importBbsUser(Workbook book) throws Exception {
        Sheet sheet = book.getSheet(0);
        int total = sheet.getRows();
        AdminInfo ai = ExternalUserUtil.getAdminBySession();
        for (int i = 1; i < total; i++) {
            //不导入URN重复用户
            String urn = sheet.getCell(0, i).getContents().trim();
            String pwd = sheet.getCell(1, i).getContents().trim();
            String persona = sheet.getCell(2, i).getContents().trim();
            String nickname = sheet.getCell(3, i).getContents().trim();
            String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
            AdminInfo temp = adminDAO.findAdminByUrnByBusinessId(urn,businessId);
            if (temp != null||StringUtil.isEmpty(urn)||StringUtil.isEmpty(pwd)||StringUtil.isEmpty(nickname)) {
                continue;
            }
            AdminInfo cq = new AdminInfo();
            cq.setUrn(urn);
            cq.setPwd(pwd);
            cq.setPersona(persona);
            cq.setNickname(nickname);
            cq.setSuperId(ai.getId());
            cq.setGrp("user");
            cq.setEnabled(false);
            //上次登录时间
            cq.setLtime(new Date());
            cq.setGenTime(new Date());
            this.adminDAO.addAdmin(cq);
        }
    }

}
