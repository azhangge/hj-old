package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.examination.entity.lazy.LazyOperationLog;
import com.hjedu.platform.dao.IOperationLogDAO;
import com.hjedu.platform.entity.OperationLog;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListOperationLogs implements Serializable{

    ILogService logger = SpringHelper.getSpringBean("LogService");
    List<OperationLog> operationLogList = new ArrayList();
    IOperationLogDAO operationLogDAO = SpringHelper.getSpringBean("OperationLogDAO");
    LazyOperationLog lazyLogs;
    String businessId;

    public LazyOperationLog getLazyLogs() {
		return lazyLogs;
	}

	public void setLazyLogs(LazyOperationLog lazyLogs) {
		this.lazyLogs = lazyLogs;
	}

	public List<OperationLog> getOperationLogList() {
        return this.operationLogList;
    }

    public void setOperationLogList(List<OperationLog> operationLogList) {
        this.operationLogList = operationLogList;
    }

    @PostConstruct
    public void init() {
    	this.businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        synchronizeDB();
    }

    private void synchronizeDB() {
//        this.operationLogList = operationLogDAO.findAllOperationLog();
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
//        lazyLogs = new LazyOperationLog();
    	this.operationLogList = operationLogDAO.findAllOperationLogByBusinessId(businessId);
    }

    public void deleteOperationLog(String id) {
        operationLogDAO.deleteOperationLog(id);
        synchronizeDB();
    }

    public void delAll() {
        operationLogDAO.deleteAll();
        this.synchronizeDB();
    }

    public void batchDel() {
        for (OperationLog c : this.lazyLogs) {
            if (c.isSelected()) {
                operationLogDAO.deleteOperationLog(c.getId());
            }
        }
        this.synchronizeDB();
    }
}
