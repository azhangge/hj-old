package com.hjedu.platform.controller;

import java.io.File;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.huajie.util.I18nHelper;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

/**
 * 本类用于根据I18N设置加载语言包
 * @author huajie
 */
@ManagedBean
@RequestScoped
public class I18nBean {

    private String langWorkMode = SpringHelper.getSpringBean("lang_work_mode");//dynamic  static
    private String language = SpringHelper.getSpringBean("i18n_lang");//auto zh_CN en
    //private Map<String, Map<String, Map>> langs;//按语言将界面信息全部加载，耗内存操作
    String basePath = "";
    List<String> supportLangs = SpringHelper.getSpringBean("support_langs");
    private String lastLang = SpringHelper.getSpringBean("last_lang");
    private Map buffer = null;

    public String getLangWorkMode() {
        return langWorkMode;
    }

    public void setLangWorkMode(String langWorkMode) {
        this.langWorkMode = langWorkMode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    @PostConstruct
    public void init() {
        basePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("language");

    }

    /**
     * 根据访问页面加载语言Map
     *
     * @return
     */
    private Map fetchLangMap() {
        if (buffer == null) {//如果缓存中已经有，就不再次加载，避免每个语句都构造一次MAP，以使MAP在同一个页面中共享，此方法仅适用于同一页面的调用，调用不同页面的标签需要重新加载MAP
            String path = fetchPagePath();
            Map map = fetchLangMap(path);
            return map;
        } else {
            return buffer;
        }
    }

    /**
     * 根据指定的文件名称加载语言Map
     *
     * @param page 文件名称，如m/Welcome
     * @return
     */
    private Map fetchLangMap(String page) {
        String country = fetchLang();
        String realPath = basePath + File.separator + country + File.separator + page + ".la";
        //System.out.println(realPath);
        Map map = I18nHelper.loadAsMap(realPath);
        return map;

    }

    /**
     * 根据语言标签加载语言
     *
     * @param key 语言标签
     * @return
     */
    public String lang(String key) {
        Map<String, String> map = fetchLangMap();
        String x = map.get(key);
        return x;
    }

    /**
     * 根据指定页面的语言标签加载语言
     *
     * @param page 页面,如：talk/Default
     * @param key 语言标签
     * @return
     */
    public String lang(String page, String key) {
        Map<String, String> map = fetchLangMap(page);
        String x = map.get(key);
        return x;
    }

    /**
     * 获得语言，若指定为AUTO，则根据浏览器语言偏好加载，否则，加载指定语言
     *
     * @return
     */
    private String fetchLang() {
        String la;
        if ("auto".equals(language)) {
            la = JsfHelper.getRequest().getLocale().getLanguage();
            //System.out.println(la);
            String country = JsfHelper.getRequest().getLocale().getCountry();
            if (!"en".equals(la)) {
                la = la + "_" + country;
            }

        } else {
            la = language;
        }
        if (!supportLangs.contains(la)) {
            la = lastLang;
        }
        //System.out.println(la);
        return la;
    }

    /**
     * 获得访问页面的路径
     *
     * @return
     */
    private String fetchPagePath() {
        String path = "";
        String temp = JsfHelper.getRequest().getServletPath();
        if (temp != null) {
            int p = temp.lastIndexOf(".");
            if (p != -1) {
                try {
                    path = temp.substring(1, p);//由于URI一定以‘/’开头，所以此处为1开始
                } catch (Exception e) {
                }

            }
        }

        return path;
    }

}
