package com.hjedu.platform.controller;


import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.platform.dao.IFireWallDAO;
import com.hjedu.platform.entity.FireWall;
import com.hjedu.platform.service.ILogService;
import com.hjedu.platform.service.impl.FireWallService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAFireWall implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    private IFireWallDAO infoDAO = SpringHelper.getSpringBean("FireWallDAO");
    FireWallService fireService = SpringHelper.getSpringBean("FireWallService");
    private FireWall info;

    public FireWall getInfo() {
        return info;
    }

    public void setInfo(FireWall info) {
        this.info = info;
    }

    @PostConstruct
    public void init() {
        this.info = fireService.findFireWall();
    }

    public String apply() {
        infoDAO.updateFireWall(info);
        JsfHelper.info("防火墙修改成功！");
        this.logger.log("防火墙修改");
        fireService.reBuildCache();
        return null;
    }
}
