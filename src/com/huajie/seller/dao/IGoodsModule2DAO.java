package com.huajie.seller.dao;


import java.util.List;

import com.huajie.seller.model.GoodsModuleModel;


public interface IGoodsModule2DAO {

    public abstract void addGoodsModuleModel(GoodsModuleModel p);

    public abstract GoodsModuleModel findGoodsModuleModel(String pid);

    public abstract void updateGoodsModuleModel(GoodsModuleModel p);

    public abstract List<GoodsModuleModel> findAllGoodsModuleModel();

    public abstract List<GoodsModuleModel> findGoodsModuleModelByUrlType(String type);

    public abstract List<GoodsModuleModel> findAllSonGoodsModuleModel(String fatherID);

    public abstract void deleteGoodsModuleModel(String pid);

    public void deleteGoodsModuleModelAndAllDescendants(String id);

    public List<GoodsModuleModel> loadAllDescendants(String fid);

    public void loadAllDescendants(String fid, List<GoodsModuleModel> sons);
    
    public GoodsModuleModel findExamModuleByName(String name);
    public void deleteAllExamModule();
    public List<GoodsModuleModel> findRootGoodsModuleModel() ;
}
