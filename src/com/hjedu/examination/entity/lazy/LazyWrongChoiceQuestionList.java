package com.hjedu.examination.entity.lazy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IWrongQuestionDAO;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.huajie.util.SpringHelper;

public class LazyWrongChoiceQuestionList extends LazyDataModel<ChoiceQuestion> {

    private List<ChoiceQuestion> models = new ArrayList();
    private final IWrongQuestionDAO wrongDAO = SpringHelper.getSpringBean("WrongQuestionDAO");
    private String uid;

    public LazyWrongChoiceQuestionList(String uid) {
        this.uid = uid;
    }

    public List<ChoiceQuestion> getModels() {
        return models;
    }

    public void setModels(List<ChoiceQuestion> models) {
        this.models = models;
    }

    @Override
    public ChoiceQuestion getRowData(String rowKey) {
        for (ChoiceQuestion c : models) {
            if (c.getId().equals(rowKey)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(ChoiceQuestion car) {
        return car.getId();
    }

    @Override
    public List<ChoiceQuestion> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        boolean ifFilter = false;
        
        List<ChoiceQuestion> data = new ArrayList();
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
            int num = (int) this.wrongDAO.getQuestionNumByTypeAndUsr("choice", uid);
            this.setRowCount(num);
            //paginate  
            models = wrongDAO.findWrongChoiceQuestionByUsr(uid, first, pageSize);
            Collections.sort(models);
            data.addAll(models);
            //System.out.println(models.size());
        } else if (ifFilter && sortField == null) {
            //System.out.println("filter no sort");
            //filter  
            List<ChoiceQuestion> tempC = wrongDAO.findWrongChoiceQuestionByUsr(uid, 0, 999999);
            String name = filters.get("name").toString();
            models.clear();
            if (name != null) {
                for (ChoiceQuestion cq : tempC) {
                    if (cq.getName().contains(name)) {
                        models.add(cq);
                    }
                }
            }
            this.setRowCount(models.size());
            Collections.sort(models);
            data.addAll(models);
        } //sort  
        else if (!ifFilter && sortField != null) {
            //System.out.println("no filter  sort");
            int num = (int) this.wrongDAO.getQuestionNumByTypeAndUsr("choice", uid);
            this.setRowCount(num);
            String ot = SortOrder.ASCENDING.equals(sortOrder) ? "asc" : "desc";
            models = wrongDAO.findWrongChoiceQuestionByUsr(uid, first, pageSize);
            
            data.addAll(models);
            Collections.sort(data, new LazySorter<ChoiceQuestion>(ChoiceQuestion.class, sortField, sortOrder));
        } else if (ifFilter && sortField != null) {
            //System.out.println("filter  sort");
            List<ChoiceQuestion> tempC = wrongDAO.findWrongChoiceQuestionByUsr(uid, 0, 999999);
            String name = filters.get("name").toString();
            models.clear();
            if (name != null) {
                for (ChoiceQuestion cq : tempC) {
                    if (cq.getName().contains(name)) {
                        models.add(cq);
                    }
                }
            }
            this.setRowCount(models.size());
            data.addAll(models);
            Collections.sort(data, new LazySorter<ChoiceQuestion>(ChoiceQuestion.class, sortField, sortOrder));
        }
        return data;
    }
}
