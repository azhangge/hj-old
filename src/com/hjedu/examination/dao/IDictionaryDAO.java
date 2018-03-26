package com.hjedu.examination.dao;


import java.util.List;

import com.hjedu.examination.entity.DictionaryModel;


public interface IDictionaryDAO {

    public abstract void addDictionaryModel(DictionaryModel dm);

    public abstract DictionaryModel findDictionaryModel(String id);

    public abstract void updateDictionaryModel(DictionaryModel dm);

    public abstract List<DictionaryModel> findAllDictionaryModel(String businessId);

    public abstract List<DictionaryModel> findDictionaryModelByUrlType(String type);

    public abstract List<DictionaryModel> findAllSonDictionaryModel(String fatherID);
    
    public List<DictionaryModel> findAllDefaultDepartments(String businessId);
    
    public String findAllDefaultDepartmentStr(String businessId);

    public abstract void deleteDictionaryModel(String id);

    public void deleteDictionaryModelAndAllDescendants(String id,String businessId);

    public List<DictionaryModel> loadAllDescendants(String fid);

    public void loadAllDescendants(String fid, List<DictionaryModel> sons);
    
    public DictionaryModel findDictionaryModelByName(String name,String businessId) ;
    
    public void deleteAllDictionary(String bussinessId) ;

	List<DictionaryModel> findDictionaryModelByFatherID(String fatherID);

	List<DictionaryModel> findLeafDictionaryModel(String businessId);
}
