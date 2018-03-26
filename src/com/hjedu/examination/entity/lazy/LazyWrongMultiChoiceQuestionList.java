package com.hjedu.examination.entity.lazy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.dao.IWrongQuestionDAO;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.huajie.util.SpringHelper;

public class LazyWrongMultiChoiceQuestionList extends LazyDataModel<MultiChoiceQuestion> {

    private List<MultiChoiceQuestion> models = new ArrayList();
    private final IMultiChoiceQuestionDAO questionDAO = SpringHelper.getSpringBean("MultiChoiceQuestionDAO");
    private final IWrongQuestionDAO wrongDAO = SpringHelper.getSpringBean("WrongQuestionDAO");
    private String uid;

    public LazyWrongMultiChoiceQuestionList(String uid) {
        this.uid = uid;
    }

    public List<MultiChoiceQuestion> getModels() {
        return models;
    }

    public void setModels(List<MultiChoiceQuestion> models) {
        this.models = models;
    }

    @Override
    public MultiChoiceQuestion getRowData(String rowKey) {
        for (MultiChoiceQuestion c : models) {
            if (c.getId().equals(rowKey)) {
                return c;
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
            int num = (int) this.wrongDAO.getQuestionNumByTypeAndUsr("mchoice", uid);
            this.setRowCount(num);
            //paginate  
            models = wrongDAO.findWrongMultiChoiceQuestionByUsr(uid, first, pageSize);
            Collections.sort(models);
            data.addAll(models);
        } else if (ifFilter && sortField == null) {
            //System.out.println("filter no sort");
            //filter  
            List<MultiChoiceQuestion> tempC = wrongDAO.findWrongMultiChoiceQuestionByUsr(uid, 0, 999999);
            String name = filters.get("name").toString();
            models.clear();
            if (name != null) {
                for (MultiChoiceQuestion cq : tempC) {
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
            int num = (int) this.wrongDAO.getQuestionNumByTypeAndUsr("mchoice", uid);
            this.setRowCount(num);
            String ot = SortOrder.ASCENDING.equals(sortOrder) ? "asc" : "desc";
            models = wrongDAO.findWrongMultiChoiceQuestionByUsr(uid, first, pageSize);
            data.addAll(models);
            Collections.sort(data, new LazySorter<MultiChoiceQuestion>(MultiChoiceQuestion.class, sortField, sortOrder));
        } else if (ifFilter && sortField != null) {
            //System.out.println("filter  sort");
            List<MultiChoiceQuestion> tempC = wrongDAO.findWrongMultiChoiceQuestionByUsr(uid, 0, 999999);
            String name = filters.get("name").toString();
            models.clear();
            if (name != null) {
                for (MultiChoiceQuestion cq : tempC) {
                    if (cq.getName().contains(name)) {
                        models.add(cq);
                    }
                }
            }
            this.setRowCount(models.size());
            data.addAll(models);
            Collections.sort(data, new LazySorter<MultiChoiceQuestion>(MultiChoiceQuestion.class, sortField, sortOrder));
        }
        return data;
    }
}
