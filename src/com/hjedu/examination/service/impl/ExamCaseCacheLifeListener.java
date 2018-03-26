package com.hjedu.examination.service.impl;

import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.entity.ExamCase;
import com.huajie.cache.CacheItemLifeListener;
import com.huajie.cache.RereCacheItem;
import com.huajie.util.SpringHelper;

/**
 *
 * @author huajie
 */
public class ExamCaseCacheLifeListener implements CacheItemLifeListener {
    
    @Override
    public void destroy(RereCacheItem item) {
    	if(item!=null)
        try {
            ExamCase ec = (ExamCase) item.getObject();
            IExamCaseDAO dao = SpringHelper.getSpringBean("ExamCaseDAO");
            if (!dao.checkIfExists(ec.getId())&&!"".equals(ec.getStat())) {
                dao.addExamCase(ec);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
}
