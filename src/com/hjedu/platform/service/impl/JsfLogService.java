package com.hjedu.platform.service.impl;

import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.platform.dao.IOperationLogDAO;
import com.hjedu.platform.entity.OperationLog;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;

public class JsfLogService implements ILogService,Serializable {

    IOperationLogDAO olDAO;

    public IOperationLogDAO getOlDAO() {
        return olDAO;
    }

    public void setOlDAO(IOperationLogDAO olDAO) {
        this.olDAO = olDAO;
    }

    @Override
    public void log(String str) {
        try {
            AdminSessionBean ab = JsfHelper.getBean("adminSessionBean");
            String uid = "1";
            if (ab != null) {
                AdminInfo ai = ab.getAdmin();
                if (ai != null) {
                    uid = ab.getAdmin().getId();
                }
            }

            OperationLog log = new OperationLog();
            log.setOperation1(str);
            log.setUid(uid);
            log.setGenTime(new Date());
            String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
            log.setBusinessId(businessId);
            olDAO.addOperationLog(log);
            //MyLogger.echo(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	@Override
	public void log(String str, AdminInfo admin) {
		try {
			OperationLog log = new OperationLog();
	        log.setOperation1(str);
	        log.setUid(admin.getId());
	        log.setGenTime(new Date());	    
	        log.setBusinessId(admin.getBusinessId());
	        olDAO.addOperationLog(log);
	        //MyLogger.echo(str);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
