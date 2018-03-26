package com.huajie.seller.dao;


import java.util.List;
import java.util.Map;

import com.huajie.seller.model.SaleGoods;

public interface ISaleGoodsDAO {

    public abstract void addSaleGoods(SaleGoods m);

    public abstract void updateSaleGoods(SaleGoods m);

    public abstract void deleteSaleGoods(String id);

    public abstract void deleteSaleGoodsByModule(String moduleId);

    public abstract SaleGoods findSaleGoods(String id);

    public abstract SaleGoods findSaleGoodsByName(String id);
    
    public abstract SaleGoods findSaleGoodsByHashCode(String code);

    public abstract List<SaleGoods> findAllSaleGoods();

    public abstract List<SaleGoods> findSaleGoodsByModule(String moduleId);

    public List<SaleGoods> findSaleGoodsByModule(String id, int offSet, int num);

    public List<SaleGoods> findSaleGoodsByModuleAndLike(String id, Map<String,Object> fms);

    public List<SaleGoods> findGoodsByLike(String str);

    public List<SaleGoods> findOrderedSaleGoodsByModule(String id, int offSet, int num, String field, String type);

    public long getGoodsNumByModule(String id);

    public List<SaleGoods> getRandomGoodsOfNumInModule(long num, String mid);

    public List<SaleGoods> getRandomGoodsOfNumInModule(long num, String mid, List<SaleGoods> cqs2);

    public SaleGoods findGoodsByIndex(int index, String mid);

    public SaleGoods findGoodsByGid(String mid);
    
    public SaleGoods findGoodsBySaleCode(String mid);
}
