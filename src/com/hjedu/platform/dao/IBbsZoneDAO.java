package com.hjedu.platform.dao;

import java.util.List;

import com.hjedu.platform.entity.BbsThread;
import com.hjedu.platform.entity.BbsZone;



public interface IBbsZoneDAO {
	
	public void save(BbsZone entity);

	
	public void delete(String id);

	public BbsZone update(BbsZone entity);

	public BbsZone findById(String id);

	public BbsThread findLatestThread(String zoneId);
        
	public List<BbsZone> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	public List<BbsZone> findByName(Object name, int... rowStartIdxAndCount);

	public List<BbsZone> findByDescription(Object description,
			int... rowStartIdxAndCount);

	public List<BbsZone> findByManager(Object manager,
			int... rowStartIdxAndCount);

	public List<BbsZone> findByOrderIndex(Object orderIndex,
			int... rowStartIdxAndCount);

	
	public List<BbsZone> findAll(int... rowStartIdxAndCount);
}