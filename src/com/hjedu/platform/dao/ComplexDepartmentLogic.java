package com.hjedu.platform.dao;


import java.util.List;

import com.hjedu.examination.entity.DictionaryModel;


public interface ComplexDepartmentLogic {

    public List<DictionaryModel> buildQulifiedStructureByDic(String fid, List<DictionaryModel> files);

    public List<DictionaryModel> buildStructure(List<DictionaryModel> cfs1);

    public List<DictionaryModel> buildStructure(String fid, List<DictionaryModel> cfs1);
    
    public String genComplexGrapaID(String fatherId,List<DictionaryModel> files) ;
    
    public List<DictionaryModel> genFamilyTree(String id, boolean ifContainRoot,String businessId);
    
    public String genFamilyTreeCnStrByIds(String ids, boolean ifContainRoot,String businessId);
    
    public String genFamilyTreeCnStr(String id, boolean ifContainRoot,String businessId);
    
    public List<DictionaryModel> genComplexFamilyTree(String id, List<DictionaryModel> files);

    public String genAncestors(String id);
    
    
}
