package com.hjedu.course.lazy;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import com.hjedu.course.dao.ILessonDAO;
import com.hjedu.course.vo.LessonVO;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

public class LazyLesson extends LazyDataModel<LessonVO> {
	private static final long serialVersionUID = 1L;
	ILessonDAO LessonDAO = SpringHelper.getSpringBean("LessonDAO");

    @Override
    public List<LessonVO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    	boolean ifFilter = false;
        List<LessonVO> models = new LinkedList<>();
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
            num = (int) LessonDAO.getNumByCondition(null,businessId);
            models = LessonDAO.findLessonVOsByCondition(null, first, pageSize, null, 0,businessId);
        } else if (ifFilter && sortField == null) {
        	num = (int) LessonDAO.getNumByCondition(filters,businessId);
            models = LessonDAO.findLessonVOsByCondition(filters, first, pageSize, null, 0,businessId);
        }
        else if (!ifFilter&&sortField != null) {
            num = (int)LessonDAO.getNumByCondition(null,businessId);
            models = LessonDAO.findLessonVOsByCondition(null, first, pageSize, sortField, type,businessId);
        }
        else if (ifFilter&&sortField != null) {
        	num = (int)LessonDAO.getNumByCondition(filters,businessId);
            models = LessonDAO.findLessonVOsByCondition(filters, first, pageSize, sortField, type,businessId);
        }
        this.setRowCount(num);
        return models;
    }
}
