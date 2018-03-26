package com.huajie.seller.dao.impl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.huajie.seller.dao.IOrderSerialDAO;
import com.huajie.seller.model.OrderSerial;
import com.huajie.util.SpringHelper;

public class OrderSerialDAO implements IOrderSerialDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addOrderSerial(OrderSerial serial) {
        this.entityManager.merge(serial);
    }

    @Override
    public long generateOrderSerial() {
        
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        OrderSerial os = findOrderSerialByDateStr(dateStr);
        
        if (os == null) {
            os = new OrderSerial();
            os.setDateStr(dateStr);
            this.addOrderSerial(os);
            //System.out.println("new serial"+os.getSerial());
            return os.getSerial();
        } else {
            long serial = os.getSerial();
            serial++;
            os.setSerial(serial);
            this.updateOrderSerial(os);
            //System.out.println("serial altered"+serial);
            
            return serial;
        }
    }

    @Override
    public OrderSerial findOrderSerialByDateStr(String dateStr) {
        System.out.println("begain");
        String q = "Select us from OrderSerial us where us.dateStr=:dateStr";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("dateStr", dateStr);
        List<OrderSerial> as = qu.getResultList();
        if (as.isEmpty()) {
            return null;
        } else {
            OrderSerial ai = (OrderSerial) as.get(0);
            return ai;
        }
    }

    @Override
    public void updateOrderSerial(OrderSerial am) {
        this.entityManager.merge(am);
    }

    @Override
    public OrderSerial findOrderSerial(String id) {
        OrderSerial a = this.entityManager.find(OrderSerial.class, id);
        return a;
    }

    public static void main(String args[]) {
//
          IOrderSerialDAO serialDAO = SpringHelper.getSpringBean("OrderSerialDAO");
//        long s = serialDAO.generateOrderSerial();
//        System.out.println(s);
        
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        OrderSerial os = serialDAO.findOrderSerialByDateStr(dateStr);
        System.out.println(os.getSerial());
        
    }

}
