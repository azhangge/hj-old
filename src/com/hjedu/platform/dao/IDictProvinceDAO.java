package com.hjedu.platform.dao;

import java.util.List;

import com.hjedu.platform.entity.DictProvince;



public interface IDictProvinceDAO {
	
    public void save(DictProvince entity);
   
    public void delete(DictProvince entity);
   
	public DictProvince update(DictProvince entity);
	public DictProvince findById( Integer id);
	
	public List<DictProvince> findByProperty(String propertyName, Object value
			, int...rowStartIdxAndCount
		);
	public List<DictProvince> findBySProvname(Object SProvname
			, int...rowStartIdxAndCount
		);
	public List<DictProvince> findBySType(Object SType
			, int...rowStartIdxAndCount
		);
	public List<DictProvince> findBySState(Object SState
			, int...rowStartIdxAndCount
		);
	
	public List<DictProvince> findAll(
			int...rowStartIdxAndCount
		);	
}