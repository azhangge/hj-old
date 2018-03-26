package com.hjedu.course.lazy;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.hjedu.course.dao.ILessonTypeLogDAO;
import com.hjedu.course.vo.LessonTypeLogVO;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

public class LazyLessonTypeLog extends LazyDataModel<LessonTypeLogVO>{
	
	private static final long serialVersionUID = 1L;
	
	ILessonTypeLogDAO lessonTypeLogDAO = SpringHelper.getSpringBean("LessonTypeLogDAO");
	
	@Override
	public List<LessonTypeLogVO>load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    	boolean ifFilter = false;
    	List<LessonTypeLogVO> models = new LinkedList<>();
    	int num = 0;
        int type = SortOrder.ASCENDING.equals(sortOrder) ? 0 : 1;
        for (String filterProperty : filters.keySet()) {
            String filterValue = filters.get(filterProperty).toString();
            if (filterValue != null) {
                ifFilter = true;
                break;
            }
        }
        
        if (!ifFilter && sortField == null) {
        	num = (int) lessonTypeLogDAO.getNumByCondition(null,businessId);
        	models = lessonTypeLogDAO.findLessonTypeLogVOsByCondition(null, first, pageSize, null, 0,businessId);
        } else if (ifFilter && sortField == null) {
        	num = (int) lessonTypeLogDAO.getNumByCondition(filters,businessId);
            models = lessonTypeLogDAO.findLessonTypeLogVOsByCondition(filters, first, pageSize, null, 0,businessId);
        }
        else if (!ifFilter&&sortField != null) {
            num = (int)lessonTypeLogDAO.getNumByCondition(null,businessId);
            models = lessonTypeLogDAO.findLessonTypeLogVOsByCondition(null, first, pageSize, sortField, type,businessId);
        }
        else if (ifFilter&&sortField != null) {
        	num = (int)lessonTypeLogDAO.getNumByCondition(filters,businessId);
            models = lessonTypeLogDAO.findLessonTypeLogVOsByCondition(filters, first, pageSize, sortField, type,businessId);
        }
        this.setRowCount(num);
        
    	return models;
    	
	}

}
