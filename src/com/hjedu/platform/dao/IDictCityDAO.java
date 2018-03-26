package com.hjedu.platform.dao;

import java.util.List;

import com.hjedu.platform.entity.DictCity;

/**
 * Interface for DictCityDAO.
 *
 * @author MyEclipse Persistence Tools
 */
public interface IDictCityDAO {

    public void save(DictCity entity);

    public void delete(DictCity entity);

    public DictCity update(DictCity entity);

    public DictCity findById(Integer id);

    public List<DictCity> findByProperty(String propertyName, Object value, int... rowStartIdxAndCount);

    public List<DictCity> findBySCityname(Object SCityname, int... rowStartIdxAndCount);

    public List<DictCity> findByNProvid(Object NProvid, int... rowStartIdxAndCount);

    public List<DictCity> findBySState(Object SState, int... rowStartIdxAndCount);

    public List<DictCity> findAll(
            int... rowStartIdxAndCount);
}