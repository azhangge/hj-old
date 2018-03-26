package com.huajie.seller.model.lazy;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.huajie.seller.dao.ISaleGoodsDAO;
import com.huajie.seller.model.SaleGoods;
import com.huajie.util.SpringHelper;

public class LazySaleGoodsList extends LazyDataModel<SaleGoods> {

    private List<SaleGoods> models;
    private final ISaleGoodsDAO questionDAO = SpringHelper.getSpringBean("SaleGoodsDAO");
    private String mid;

    public LazySaleGoodsList(String mid) {
        this.mid = mid;
    }

    public List<SaleGoods> getModels() {
        return models;
    }

    public void setModels(List<SaleGoods> models) {
        this.models = models;
    }

    
    
    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    @Override
    public SaleGoods getRowData(String rowKey) {
        for (SaleGoods c: models) {
            if (c.getId().equals(rowKey)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(SaleGoods car) {
        return car.getId();
    }

    @Override
    public List<SaleGoods> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        boolean ifFilter = false;
        List<SaleGoods> data = new ArrayList();
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
            int num = (int) this.questionDAO.getGoodsNumByModule(mid);
            this.setRowCount(num);
            //paginate  
            models = questionDAO.findSaleGoodsByModule(mid, first, pageSize);
            Collections.sort(models);
            data.addAll(models);
        } else if (ifFilter && sortField == null) {
            //System.out.println("filter no sort");
            //filter  
            models = questionDAO.findSaleGoodsByModuleAndLike(mid, filters);
            this.setRowCount(models.size());
            Collections.sort(models);
            data.addAll(models);
        } //sort  
        else if (!ifFilter&&sortField != null) {
            //System.out.println("no filter  sort");
            int num = (int) this.questionDAO.getGoodsNumByModule(mid);
            this.setRowCount(num);
            String ot = SortOrder.ASCENDING.equals(sortOrder) ? "asc" : "desc";
            models = questionDAO.findOrderedSaleGoodsByModule(mid, first, pageSize, sortField, ot);
            data.addAll(models);
            //Collections.sort(data, new LazySorter<SaleGoods>(SaleGoods.class, sortField, sortOrder));
        }
        else if (ifFilter&&sortField != null) {
            //System.out.println("filter  sort");
            models = questionDAO.findSaleGoodsByModuleAndLike(mid, filters);
            this.setRowCount(models.size());
            data.addAll(models);
            Collections.sort(data, new LazySorter<SaleGoods>(SaleGoods.class, sortField, sortOrder));
        }
        return data;
    }
}
