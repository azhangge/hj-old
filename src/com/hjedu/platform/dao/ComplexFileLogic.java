package com.hjedu.platform.dao;


import java.util.List;

import com.hjedu.platform.entity.BbsFileModel;


public interface ComplexFileLogic {

    public List<BbsFileModel> buildQulifiedStructureByDic(String fid, List<BbsFileModel> files);

    public List<BbsFileModel> buildStructure(List<BbsFileModel> cfs1);

    public List<BbsFileModel> buildStructure(String fid, List<BbsFileModel> cfs1);
    
    public String genComplexGrapaID(String fatherId,List<BbsFileModel> files) ;
    
    
    public List<BbsFileModel> genFamilyTree(String id);
    
    public List<BbsFileModel> genComplexFamilyTree(String id, List<BbsFileModel> files);

    public String genAncestors(String id);
    
    
}
