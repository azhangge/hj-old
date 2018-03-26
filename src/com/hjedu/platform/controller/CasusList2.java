package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.platform.dao.ICasusDAO;
import com.hjedu.platform.entity.CasusModel;
import com.huajie.util.SpringHelper;

@ManagedBean
@RequestScoped
public class CasusList2 implements Serializable {

    ICasusDAO casusDAO = SpringHelper.getSpringBean("CasusDAO");
    //ICasusTypeDAO dicDAO = SpringHelper.getSpringBean("CasusTypeDAO");
    String pageName = "";
    String pageEnglishName;
    List<CasusModel> casusList;
    List<CasusModel> subList = new LinkedList();
    //long pageType = 1;
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    private int currentPage = 1;
    private int totalPage = 1;
    private int pageSize = 16;
    private int totalSize = 0;
    String filterStr = "";

    public int getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return this.totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public List<CasusModel> getSubList() {
        return subList;
    }

    public void setSubList(List<CasusModel> subList) {
        this.subList = subList;
    }

    public String getPageName() {
        return this.pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public List<CasusModel> getCasusList() {
        return this.casusList;
    }

    public void setCasusList(List<CasusModel> casusList) {
        this.casusList = casusList;
    }

    public String getFilterStr() {
        return filterStr;
    }

    public void setFilterStr(String filterStr) {
        this.filterStr = filterStr;
    }

    public String getPageEnglishName() {
        return pageEnglishName;
    }

    public void setPageEnglishName(String pageEnglishName) {
        this.pageEnglishName = pageEnglishName;
    }

    @PostConstruct
    public void init() {
        String t = this.request.getParameter("name");
        String page = this.request.getParameter("page");
        if (t != null) {
            this.pageEnglishName = t;
        }

        synchronizeDB(this.pageEnglishName);
        if (page != null) {
            this.currentPage = Integer.parseInt(page);
        }
        this.adustPage(this.currentPage);
    }

    public String adustPage(int page) {
        this.currentPage = page;
        int end = this.currentPage * this.pageSize;
        if (end > this.totalSize) {
            end = this.totalSize;
        }
        //end=end-1;
        int begain = (this.currentPage - 1) * this.pageSize;
        if (begain > this.totalSize) {
            begain = 0;
        }
        this.subList = this.casusList.subList(begain, end);
        return null;
    }

    private void synchronizeDB(String tid) {
        if (tid != null) {
            //CasusType dm = dicDAO.findCasusTypeByEnglishName(tid);
            //this.pageName = dm.getName();
            //this.casusList = casusDAO.findCasusesByType(dm.getId());
        } else {
            this.pageName = "全部信息";
            this.casusList = this.casusDAO.findAllCasuses();
        }

        this.totalSize = this.casusList.size();

        this.totalPage = (this.totalSize % this.pageSize == 0) ? (this.totalSize / this.pageSize) : (this.totalSize / this.pageSize + 1);

    }
}
