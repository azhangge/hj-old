package com.hjedu.examination.controller;


import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.examination.dao.IModuleExamInfoDAO;
import com.hjedu.examination.entity.module.ModuleExamInfo;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAModuleExamInfo implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    private IModuleExamInfoDAO infoDAO = SpringHelper.getSpringBean("ModuleExamInfoDAO");
    private ModuleExamInfo info;

    public ModuleExamInfo getInfo() {
        return info;
    }

    public void setInfo(ModuleExamInfo info) {
        this.info = info;
    }

    

    @PostConstruct
    public void init() {
        this.info = this.infoDAO.findModuleExamInfo();
    }

    public String apply() {
        this.infoDAO.updateModuleExamInfo(info);
        JsfHelper.info("修改章节练习信息成功！");
        this.logger.log("修改章节练习信息");
        return null;
    }
}
