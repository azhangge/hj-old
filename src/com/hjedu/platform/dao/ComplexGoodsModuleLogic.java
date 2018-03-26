package com.hjedu.platform.dao;


import java.util.List;

import com.huajie.seller.model.GoodsModuleModel;


public interface ComplexGoodsModuleLogic {

    public List<GoodsModuleModel> buildQulifiedStructureByDic(String fid, List<GoodsModuleModel> files);

    public List<GoodsModuleModel> buildStructure(List<GoodsModuleModel> cfs1);

    public List<GoodsModuleModel> buildStructure(String fid, List<GoodsModuleModel> cfs1);
    
    public String genComplexGrapaID(String fatherId,List<GoodsModuleModel> files) ;
    
    
    public List<GoodsModuleModel> genFamilyTree(String id);
    
    public List<GoodsModuleModel> genComplexFamilyTree(String id, List<GoodsModuleModel> files);

    public String genAncestors(String id);
    
    
}
