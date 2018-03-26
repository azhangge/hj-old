package com.hjedu.examination.entity.lazy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.huajie.util.SpringHelper;

public class LazyMultiChoiceQuestionList extends LazyDataModel<MultiChoiceQuestion> {

    private List<MultiChoiceQuestion> models;
    private final IMultiChoiceQuestionDAO questionDAO = SpringHelper.getSpringBean("MultiChoiceQuestionDAO");
    private String mid;

    public LazyMultiChoiceQuestionList(String mid) {
        this.mid = mid;
    }

    public List<MultiChoiceQuestion> getModels() {
        return models;
    }

    public void setModels(List<MultiChoiceQuestion> models) {
        this.models = models;
    }

    
    
    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    @Override
    public MultiChoiceQuestion getRowData(String rowKey) {
        for (MultiChoiceQuestion car : models) {
            if (car.getId().equals(rowKey)) {
                return car;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(MultiChoiceQuestion car) {
        return car.getId();
    }

    @Override
    public List<MultiChoiceQuestion> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        boolean ifFilter = false;
        List<MultiChoiceQuestion> data = new ArrayList();
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
            models = questionDAO.findMultiChoiceQuestionByModule(mid, first, pageSize);
            Collections.sort(models);
            data.addAll(models);
        } else if (ifFilter && sortField == null) {
            //System.out.println("filter no sort");
            //filter  
            models = questionDAO.findMultiChoiceQuestionByModuleAndLike(mid, filters);
            this.setRowCount(models.size());
            Collections.sort(models);
            data.addAll(models);
        } //sort  
        else if (!ifFilter&&sortField != null) {
            //System.out.println("no filter  sort");
            int num = (int) this.questionDAO.getQuestionNumByModule(mid);
            this.setRowCount(num);
            String ot = SortOrder.ASCENDING.equals(sortOrder) ? "asc" : "desc";
            models = questionDAO.findOrderedMultiChoiceQuestionByModule(mid, first, pageSize, sortField, ot);
            data.addAll(models);
            //Collections.sort(data, new LazySorter<MultiChoiceQuestion>(MultiChoiceQuestion.class, sortField, sortOrder));
        }
        else if (ifFilter&&sortField != null) {
            //System.out.println("filter  sort");
            models = questionDAO.findMultiChoiceQuestionByModuleAndLike(mid, filters);
            this.setRowCount(models.size());
            data.addAll(models);
            Collections.sort(data, new LazySorter<MultiChoiceQuestion>(MultiChoiceQuestion.class, sortField, sortOrder));
        }
        return data;
    }
}
