package com.hjedu.course.dao;

import java.util.List;

import com.hjedu.course.entity.RereAsk;
import com.hjedu.course.entity.RereAskZone;



public interface IRereAskZoneDAO {
	
	public void save(RereAskZone entity);

	
	public void delete(String id);

	public RereAskZone update(RereAskZone entity);

	public RereAskZone findById(String id);

	public RereAsk findLatestAsk(String zoneId);
        
	public List<RereAskZone> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	public List<RereAskZone> findByName(Object name, int... rowStartIdxAndCount);

	public List<RereAskZone> findByDescription(Object description,
			int... rowStartIdxAndCount);

	public List<RereAskZone> findByOrderIndex(Object orderIndex,
			int... rowStartIdxAndCount);

	
	public List<RereAskZone> findAll(int... rowStartIdxAndCount);
}