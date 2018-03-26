package com.hjedu.examination.entity.lazy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.hjedu.examination.dao.IFillQuestionDAO;
import com.hjedu.examination.entity.FillQuestion;
import com.huajie.util.SpringHelper;

public class LazyFillQuestionList extends LazyDataModel<FillQuestion> {

    private List<FillQuestion> models;
    private final IFillQuestionDAO questionDAO = SpringHelper.getSpringBean("FillQuestionDAO");
    private String mid;

    public LazyFillQuestionList(String mid) {
        this.mid = mid;
    }

    public List<FillQuestion> getModels() {
        return models;
    }

    public void setModels(List<FillQuestion> models) {
        this.models = models;
    }

    
    
    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    @Override
    public FillQuestion getRowData(String rowKey) {
        for (FillQuestion car : models) {
            if (car.getId().equals(rowKey)) {
                return car;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(FillQuestion car) {
        return car.getId();
    }

    @Override
    public List<FillQuestion> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        boolean ifFilter = false;
        List<FillQuestion> data = new ArrayList();
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
            int num = (int) this.questionDAO.getQuestionNumByModule(mid);
            this.setRowCount(num);
            //paginate  
            models = questionDAO.findFillQuestionByModule(mid, first, pageSize);
            Collections.sort(models);
            data.addAll(models);
        } else if (ifFilter && sortField == null) {
            //System.out.println("filter no sort");
            //filter  
            models = questionDAO.findFillQuestionByModuleAndLike(mid, filters);
            this.setRowCount(models.size());
            Collections.sort(models);
            data.addAll(models);
        } //sort  
        else if (!ifFilter&&sortField != null) {
            //System.out.println("no filter  sort");
            int num = (int) this.questionDAO.getQuestionNumByModule(mid);
            this.setRowCount(num);
            String ot = SortOrder.ASCENDING.equals(sortOrder) ? "asc" : "desc";
            models = questionDAO.findOrderedFillQuestionByModule(mid, first, pageSize, sortField, ot);
            data.addAll(models);
            //Collections.sort(data, new LazySorter<FillQuestion>(FillQuestion.class, sortField, sortOrder));
        }
        else if (ifFilter&&sortField != null) {
            //System.out.println("filter  sort");
            models = questionDAO.findFillQuestionByModuleAndLike(mid, filters);
            this.setRowCount(models.size());
            data.addAll(models);
            Collections.sort(data, new LazySorter<FillQuestion>(FillQuestion.class, sortField, sortOrder));
        }
        return data;
    }
}
