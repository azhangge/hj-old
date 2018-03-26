package com.huajie.seller.dao;

import com.huajie.seller.model.OrderSerial;


public interface IOrderSerialDAO {
    
    public long generateOrderSerial();
    
    public abstract void addOrderSerial(OrderSerial serial);

    public abstract void updateOrderSerial(OrderSerial paramOrderSerial);

    public abstract OrderSerial findOrderSerial(String paramString);
    
    public abstract OrderSerial findOrderSerialByDateStr(String paramString);
    
}
