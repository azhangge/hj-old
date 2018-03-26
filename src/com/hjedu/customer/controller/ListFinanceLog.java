package com.hjedu.customer.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.customer.dao.IFinanceLogDAO;
import com.hjedu.customer.entity.FinanceLog;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListFinanceLog implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IFinanceLogDAO orderDAO = SpringHelper.getSpringBean("FinanceLogDAO");

    List<FinanceLog> logs;

    public List<FinanceLog> getLogs() {
        return logs;
    }

    public void setLogs(List<FinanceLog> logs) {
        this.logs = logs;
    }

    @PostConstruct
    public void init() {

        this.synDB();

    }

    private void synDB() {
        this.logs = this.orderDAO.findAllFinanceLog();

    }

    public void delete(String id) {
        this.orderDAO.deleteFinanceLog(id);
        this.logger.log("删除了财务记录");
        this.synDB();
    }

    public void batchDelete() {
        for (FinanceLog c : this.logs) {
            if (c.isSelected()) {
                this.orderDAO.deleteFinanceLog(c.getId());
            }
        }
        this.synDB();
    }

}
