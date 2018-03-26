package com.hjedu.examination.entity.lazy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.dao.IWrongQuestionDAO;
import com.hjedu.examination.entity.FileQuestion;
import com.huajie.util.SpringHelper;

public class LazyWrongFileQuestionList extends LazyDataModel<FileQuestion> {

    private List<FileQuestion> models = new ArrayList();
    private final IFileQuestionDAO questionDAO = SpringHelper.getSpringBean("FileQuestionDAO");
    private final IWrongQuestionDAO wrongDAO = SpringHelper.getSpringBean("WrongQuestionDAO");
    private String uid;

    public LazyWrongFileQuestionList(String uid) {
        this.uid = uid;
    }

    public List<FileQuestion> getModels() {
        return models;
    }

    public void setModels(List<FileQuestion> models) {
        this.models = models;
    }

    @Override
    public FileQuestion getRowData(String rowKey) {
        for (FileQuestion c : models) {
            if (c.getId().equals(rowKey)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(FileQuestion car) {
        return car.getId();
    }

    @Override
    public List<FileQuestion> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        boolean ifFilter = false;
        List<FileQuestion> data = new ArrayList();
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
            int num = (int) this.wrongDAO.getQuestionNumByTypeAndUsr("file", uid);
            this.setRowCount(num);
            //paginate  
            models = wrongDAO.findWrongFileQuestionByUsr(uid, first, pageSize);
            Collections.sort(models);
            data.addAll(models);
        } else if (ifFilter && sortField == null) {
            //System.out.println("filter no sort");
            //filter  
            List<FileQuestion> tempC = wrongDAO.findWrongFileQuestionByUsr(uid, 0, 999999);
            String name = filters.get("name").toString();
            models.clear();
            if (name != null) {
                for (FileQuestion cq : tempC) {
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
            int num = (int) this.wrongDAO.getQuestionNumByTypeAndUsr("file", uid);
            this.setRowCount(num);
            String ot = SortOrder.ASCENDING.equals(sortOrder) ? "asc" : "desc";
            models = wrongDAO.findWrongFileQuestionByUsr(uid, first, pageSize);
            data.addAll(models);
            Collections.sort(data, new LazySorter<FileQuestion>(FileQuestion.class, sortField, sortOrder));
        } else if (ifFilter && sortField != null) {
            //System.out.println("filter  sort");
            List<FileQuestion> tempC = wrongDAO.findWrongFileQuestionByUsr(uid, 0, 999999);
            String name = filters.get("name").toString();
            models.clear();
            if (name != null) {
                for (FileQuestion cq : tempC) {
                    if (cq.getName().contains(name)) {
                        models.add(cq);
                    }
                }
            }
            this.setRowCount(models.size());
            data.addAll(models);
            Collections.sort(data, new LazySorter<FileQuestion>(FileQuestion.class, sortField, sortOrder));
        }
        return data;
    }
}
