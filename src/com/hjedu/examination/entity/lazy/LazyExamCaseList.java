package com.hjedu.examination.entity.lazy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.ExamCaseFacet;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;
import com.sun.star.ucb.Cookie;

public class LazyExamCaseList extends LazyDataModel<ExamCaseFacet> {

    AdminInfo admin = null;
    private List<ExamCaseFacet> models;
    private final IExamCaseDAO questionDAO = SpringHelper.getSpringBean("ExamCaseDAO");

    public LazyExamCaseList() {
    }

    public LazyExamCaseList(AdminInfo admin) {
        this.admin = admin;
    }

    public List<ExamCaseFacet> getModels() {
        return models;
    }

    public void setModels(List<ExamCaseFacet> models) {
        this.models = models;
    }

    public AdminInfo getAdmin() {
        return admin;
    }

    public void setAdmin(AdminInfo admin) {
        this.admin = admin;
    }

    @Override
    public ExamCaseFacet getRowData(String rowKey) {
        for (ExamCaseFacet car : models) {
            if (car.getId().equals(rowKey)) {
                return car;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(ExamCaseFacet car) {
        return car.getId();
    }

    @Override
    public List<ExamCaseFacet> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    	boolean ifFilter = false;
        List<ExamCaseFacet> data = new ArrayList();
        for (String filterProperty : filters.keySet()) {
            String filterValue = filters.get(filterProperty).toString();
            if (filterValue != null) {
                ifFilter = true;
                break;
            }
        }

        if (!ifFilter && sortField == null) {
            //System.out.println("no filter no sort");
            //rowCount  
            int num = 0;
            if (admin == null) {
                num = (int) this.questionDAO.getExamCaseNum(businessId);
            } else {
                num = (int) this.questionDAO.getExamCaseNumByAdmin(admin,businessId);
            }
            this.setRowCount(num);
            //paginate  
            if (admin == null) {
                models = questionDAO.findLotsOfExamCase(first, pageSize,businessId);
            } else {
                models = questionDAO.findLotsOfExamCaseByAdmin(admin, first, pageSize,businessId);
            }
            //Collections.sort(questions);
            data.addAll(models);
        } else if (ifFilter && sortField == null) {
            //System.out.println("filter no sort");
            //filter  
            if (admin == null) {
                models = questionDAO.findExamCaseByLike(filters,first, pageSize,businessId);
            } else {
                models = questionDAO.findExamCaseByLikeAndAdmin(admin, filters,first, pageSize,businessId);
            }
            int num = 0;
            if (admin == null) {
                num = (int) this.questionDAO.getExamCaseNumByLike(filters,businessId);
            } else {
                num = (int) this.questionDAO.getExamCaseNumByLikeAndAdmin(admin,filters,businessId);
            }
            this.setRowCount(num);
            //Collections.sort(questions);
            data.addAll(models);
        } //sort  
        else if (!ifFilter && sortField != null) {
            //System.out.println("no filter  sort");
            int num = 0;
            if (admin == null) {
                num = (int) this.questionDAO.getExamCaseNum(businessId);
            } else {
                num = (int) this.questionDAO.getExamCaseNumByAdmin(admin,businessId);
            }
            this.setRowCount(num);
            String ot = SortOrder.ASCENDING.equals(sortOrder) ? "asc" : "desc";
            if (admin == null) {
                models = questionDAO.findOrderedExamCase(first, pageSize, sortField, ot,businessId);
            } else {
                models = questionDAO.findOrderedExamCaseByAdmin(admin, first, pageSize, sortField, ot,businessId);
            }
            data.addAll(models);
            //Collections.sort(data, new LazySorter<ExamCase>(ExamCase.class, sortField, sortOrder));
        } else if (ifFilter && sortField != null) {
            //System.out.println("filter  sort");
            if (admin == null) {
                models = questionDAO.findExamCaseByLike(filters,first, pageSize,businessId);
            } else {
                models = questionDAO.findExamCaseByLikeAndAdmin(admin, filters,first, pageSize,businessId);
            }
            int num = 0;
            if (admin == null) {
                num = (int) this.questionDAO.getExamCaseNumByLike(filters,businessId);
            } else {
                num = (int) this.questionDAO.getExamCaseNumByLikeAndAdmin(admin,filters,businessId);
            }
            this.setRowCount(num);
            data.addAll(models);
            Collections.sort(data, new LazySorter<ExamCaseFacet>(ExamCaseFacet.class, sortField, sortOrder));
        }
        return data;
    }
}
