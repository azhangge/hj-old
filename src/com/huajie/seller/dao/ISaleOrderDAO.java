package com.huajie.seller.dao;


import java.util.List;

import com.huajie.seller.model.SaleOrder;

public interface ISaleOrderDAO {

    public abstract void addSaleOrder(SaleOrder paramSaleOrder);

    public abstract SaleOrder findSaleOrder(String paramString);

    public abstract void updateSaleOrder(SaleOrder paramSaleOrder);

    public abstract List<SaleOrder> findAllSaleOrder();

    public abstract List<SaleOrder> findSaleOrderByType(String id);

    public abstract void deleteSaleOrder(String paramString);
    
    public List<SaleOrder> findAccessibleSaleOrder(String uid);
    
    public List<SaleOrder> findSaleOrderByAgent(String uid) ;
    
    public List<SaleOrder> findSaleOrderByUser(String uid);
    
    public List<SaleOrder> findFinishedSaleOrderByUser(String uid) ;
    
    public List<SaleOrder> findUnFinishedSaleOrderByUser(String uid) ;
    
    public List<SaleOrder> findSaleOrderBySeller(String uid);
    
    public SaleOrder findSaleOrderByOid(String oid);
}
