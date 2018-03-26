/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hjedu.customer.dao;

import java.util.List;

import com.hjedu.customer.entity.FinanceLog;


public interface IFinanceLogDAO {

    public void updateFinanceLog(FinanceLog comModel);

    public void addFinanceLog(FinanceLog partnerModel);

    public void deleteAll();

    public void deleteFinanceLog(String id);
    
    public void deleteLogByUser(String lid);

    public List<FinanceLog> findAllFinanceLog();
    
    public List<FinanceLog> findFinanceLogByUsr(final String uid);

    public FinanceLog findFinanceLog(String id);
}
