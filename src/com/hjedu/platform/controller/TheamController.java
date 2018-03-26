package com.hjedu.platform.controller;

import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.entity.SystemConfig;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ApplicationScoped
public class TheamController {

    ISystemConfigDAO ts = SpringHelper.getSpringBean("SystemConfigDAO");
    String theme = "";
    ILogService logger = SpringHelper.getSpringBean("LogService");
    Map<String, String> themes = SpringHelper.getSpringBean("themes");

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Map<String, String> getThemes() {
        return themes;
    }

    public void setThemes(Map<String, String> themes) {
        this.themes = themes;
    }

    @PostConstruct
    public void init() {
        this.loadTheme();
    }

    public void loadTheme() {
        SystemConfig sc = ts.getSystemConfig();
        this.theme = sc.getTheme();
    }

    public String changeTheme() {
        MyLogger.echo("theme saved.");
        String cnName = "";
        Set<String> keys = this.themes.keySet();
        for (String key : keys) {
            if (this.themes.get(key).equals(this.theme)) {
                cnName = key;
                break;
            }
        }
        logger.log("变更系统显示风格为：" + cnName);
        ts.saveTheme(this.getTheme());
        JsfHelper.info("保存风格样式成功！");
        return null;
    }
}
