package com.hjedu.examination.controller;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.ExamCaseFacet;
import com.hjedu.examination.service.IExamCaseCacheService;
import com.hjedu.platform.controller.ClientSession;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class IntensiveExamCaseReport implements Serializable {

    IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
    IExamCaseCacheService cacheService = SpringHelper.getSpringBean("ExamCaseCacheService");
    List<ExamCaseFacet> cases;
    //List<ExamCase> recentCases;
    private LineChartModel lineModel;

    boolean ifRecent = true;

    public List<ExamCaseFacet> getCases() {
        return cases;
    }

    public void setCases(List<ExamCaseFacet> cases) {
        this.cases = cases;
    }

    public LineChartModel getLineModel() {
        return lineModel;
    }

    public boolean isIfRecent() {
        return ifRecent;
    }

    public void setIfRecent(boolean ifRecent) {
        this.ifRecent = ifRecent;
    }

    @PostConstruct
    public void init() {
    	this.ifRecent = false;
        this.synDB();

    }

    /**
     * 删除成绩，数据库和缓存都删除
     *
     * @param id
     */
    public void deleteReport(String id) {
        this.examCaseDAO.deleteExamCase(id);
        this.synDB();
    }

    /**
     * 从数据库加载所有成绩
     */
    public void loadAllCases() {
        this.ifRecent = false;
        this.synDB();
    }

    /**
     * 从缓存加载用户的成绩
     */
    public void loadRecentCases() {
        this.ifRecent = true;
        this.synDB();
    }

    /**
     * 根据需要加载数据,并重新加载曲线图
     */
    public void synDB() {
        ClientSession cs = JsfHelper.getBean("clientSession");
        BbsUser u = cs.getUsr();
        if (u != null) {
            if (ifRecent) {
                List<ExamCase> ecs = this.cacheService.findRecentExamCasesByUser(u.getId());
                List<ExamCaseFacet> facets = new ArrayList();
                for (ExamCase ec : ecs) {
                    facets.add(new ExamCaseFacet(ec));
                }
                this.cases=facets;
                //Collections.sort(cases);
            } else {
                this.cases = this.examCaseDAO.findIntensiveExamCaseFacetByUser(u.getId());
            }
        }
        this.createDateModel();
    }

    /**
     * 创建一个成绩的线图
     */
    private void createDateModel() {
        lineModel = new LineChartModel();
        lineModel.setShowPointLabels(true);
        lineModel.setLegendPosition("nw");
        lineModel.setTitle("考试成绩曲线图");
        lineModel.setZoom(true);
        lineModel.getAxis(AxisType.Y).setLabel("成绩");
        Axis axis = new CategoryAxis();
        axis.setTickAngle(-50);
        //axis.setMax("2014-02-01");
        //axis.setTickFormat("%b %#d, %y");
        lineModel.getAxes().put(AxisType.X, axis);

        try {
            //按考试ID将Serials保存
            Map<String, LineChartSeries> map = new HashMap();
            //反转排序
            if (cases != null) {
                Collections.reverse(cases);
                for (ExamCaseFacet ec : this.cases) {
                    //当序列数要多于6个时，停止增加序列，以防止无法显示太多序列
                    if (map.size() >= 6) {
                        break;
                    }
                    if (!ec.isIfPub() || ec.getExamination().getId().equals("7") || "saved".equals(ec.getStat())) {
                        continue;
                    }
                    LineChartSeries series = map.get(ec.getExamination().getId());
                    if (series == null) {
                        series = new LineChartSeries();
                        series.setLabel(ec.getExamination().getName());
                        map.put(ec.getExamination().getId(), series);
                    }
                    int i = series.getData().size() + 1;
                    //Date dt = ec.getGenTime();
                    double score = ec.getScore();
                    DecimalFormat df = new DecimalFormat("0.0");
                    //String dtt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dt);
                    series.set(String.valueOf(i), Double.parseDouble(df.format(score)));
                }
                //再次反转还原
                Collections.reverse(cases);
                Set<String> set = map.keySet();
                for (String s : set) {
                    LineChartSeries series = map.get(s);
                    lineModel.addSeries(series);
                }
                //若无数据，则加入以下文字
                if (map.isEmpty()) {
                    LineChartSeries series = new LineChartSeries();
                    series.setLabel("暂无数据");
                    series.set(String.valueOf(0), 0);
                    lineModel.addSeries(series);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
