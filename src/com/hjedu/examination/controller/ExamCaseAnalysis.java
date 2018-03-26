package com.hjedu.examination.controller;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.DualListModel;
import org.primefaces.model.TreeNode;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.service.impl.ExamDepartmentService;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ExamCaseAnalysis implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
    IExaminationDAO examinationDAO = SpringHelper.getSpringBean("ExaminationDAO");
    List<ExamCase> cases = new ArrayList();
    private DualListModel<Examination> examinations;
    //以下属性用于绑定页面计算部门或考试的平均分
    String departmentId;
    String examinationId;
    Date date1 = new Date(System.currentTimeMillis() - ((long) 1000) * 60 * 60 * 24 * 30);
    Date date2 = new Date();
    ExamDepartmentService dicDAO = SpringHelper.getSpringBean("ExamDepartmentService");
    List<DictionaryModel> dics = new ArrayList();
    DictionaryModel dic = new DictionaryModel();
    boolean flag = false;
    List<SelectItem> ss = new ArrayList();
    TreeNode root = new DefaultTreeNode();
    private TreeNode[] selectedNodes;
    private List<TreeNode> nodes = new LinkedList();
    double averageScore = 0D;
    double averageChoiceScore = 0D;
    double averageFillScore = 0D;
    double averageJudgeScore = 0D;
    double averageEssayScore = 0D;
    double averageFileScore = 0D;
    double averageCaseScore = 0D;
    double averageMultiChoiceScore = 0D;

    DescriptiveStatistics stats = new DescriptiveStatistics();
    Frequency freq = new Frequency();
    BarChartModel barModel;
    LineChartModel lineModel;

    String bussinessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    
    public DictionaryModel getDic() {
        return dic;
    }

    public void setDic(DictionaryModel dic) {
        this.dic = dic;
    }

    public Frequency getFreq() {
        return freq;
    }

    public void setFreq(Frequency freq) {
        this.freq = freq;
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public List<DictionaryModel> getDics() {
        return dics;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getExaminationId() {
        return examinationId;
    }

    public double getAverageChoiceScore() {
        return averageChoiceScore;
    }

    public void setAverageChoiceScore(double averageChoiceScore) {
        this.averageChoiceScore = averageChoiceScore;
    }

    public double getAverageFillScore() {
        return averageFillScore;
    }

    public void setAverageFillScore(double averageFillScore) {
        this.averageFillScore = averageFillScore;
    }

    public double getAverageJudgeScore() {
        return averageJudgeScore;
    }

    public void setAverageJudgeScore(double averageJudgeScore) {
        this.averageJudgeScore = averageJudgeScore;
    }

    public double getAverageEssayScore() {
        return averageEssayScore;
    }

    public void setAverageEssayScore(double averageEssayScore) {
        this.averageEssayScore = averageEssayScore;
    }

    public double getAverageFileScore() {
        return averageFileScore;
    }

    public void setAverageFileScore(double averageFileScore) {
        this.averageFileScore = averageFileScore;
    }

    public double getAverageCaseScore() {
        return averageCaseScore;
    }

    public void setAverageCaseScore(double averageCaseScore) {
        this.averageCaseScore = averageCaseScore;
    }

    public double getAverageMultiChoiceScore() {
        return averageMultiChoiceScore;
    }

    public void setAverageMultiChoiceScore(double averageMultiChoiceScore) {
        this.averageMultiChoiceScore = averageMultiChoiceScore;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public void setExaminationId(String examinationId) {
        this.examinationId = examinationId;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public DescriptiveStatistics getStats() {
        return stats;
    }

    public void setStats(DescriptiveStatistics stats) {
        this.stats = stats;
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public LineChartModel getLineModel() {
        return lineModel;
    }

    public void setLineModel(LineChartModel lineModel) {
        this.lineModel = lineModel;
    }

    public List<ExamCase> getCases() {
        return cases;
    }

    public void setCases(List<ExamCase> cases) {
        this.cases = cases;
    }

    public List<TreeNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<TreeNode> nodes) {
        this.nodes = nodes;
    }

    public DualListModel<Examination> getExaminations() {
        return examinations;
    }

    public void setExaminations(DualListModel<Examination> examinations) {
        this.examinations = examinations;
    }

    public void setDics(List<DictionaryModel> dics) {
        this.dics = dics;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public List<SelectItem> getSs() {
        return ss;
    }

    public void setSs(List<SelectItem> ss) {
        this.ss = ss;
    }

    @PostConstruct
    public void init() {
        barModel = new BarChartModel();
        barModel.setTitle("成绩区间分布柱状图");
        barModel.setAnimate(true);
        ChartSeries cs = new ChartSeries();
        cs.set("无数据", 0);
        barModel.addSeries(cs);
        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("成绩区间");
        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("成绩数");
        yAxis.setTickFormat("%d");

        lineModel = new LineChartModel();
        lineModel.setShowPointLabels(true);
        lineModel.setZoom(true);
        lineModel.setTitle("成绩分布曲线图");
        lineModel.setAnimate(true);
        ChartSeries cs2 = new ChartSeries();
        cs2.set("0", 0);
        lineModel.addSeries(cs2);
        Axis xAxis2 = lineModel.getAxis(AxisType.X);
        xAxis2.setLabel("考试成绩");
        xAxis2.setTickFormat("%.2f");
        Axis yAxis2 = lineModel.getAxis(AxisType.Y);
        yAxis2.setLabel("成绩数");
        yAxis2.setTickFormat("%d");

        this.stats.addValue(0.0);
        this.freq.addValue(0.0);
        //this.cases = this.examCaseDAO.findAllExamCase();
        this.loadStructure();
        this.buildExamination();
        //this.computeAverageScore();
    }

    /**
     * 加载所有考试
     */
    private void buildExamination() {

        List<Examination> exams = this.examinationDAO.findAllExamination(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        List<Examination> source = new ArrayList<Examination>();
        List<Examination> target = new ArrayList<Examination>();
        source.addAll(exams);
        examinations = new DualListModel<Examination>(source, target);
    }

    /**
     * 加载部门结构
     */
    private void loadStructure() {
        this.root = new DefaultTreeNode();
        DictionaryModel dm = dicDAO.findDictionaryModel(this.bussinessId,bussinessId);
        test(dm, root);
        this.dics = null;
        this.dics = dm.getSons();
    }

    public void test(DictionaryModel dd, TreeNode node) {
        //node.setExpanded(true);
        List<DictionaryModel> ls = dicDAO.findAllSonDictionaryModel(dd.getId());
        Collections.sort(ls);
        dd.setSons(ls);
        if (ls.isEmpty()) {
            return;
        } else {
            for (DictionaryModel d : ls) {
                TreeNode t = new DefaultTreeNode(d, node);
                test(d, t);
            }
        }
    }

    public String analyze() {
        //找出所有选中的部门
        Set<DictionaryModel> dms = new HashSet();
        if (this.selectedNodes != null) {
            for (TreeNode t : this.selectedNodes) {
                DictionaryModel dm = (DictionaryModel) t.getData();
                if (dm != null) {
                    dms.add(dm);
                    List<DictionaryModel> ds = this.dicDAO.loadAllDescendants(dm.getId());
                    if (ds != null) {
                        dms.addAll(ds);
                    }
                }
            }
        }

        //找出所有选中的考试
        List<Examination> exams = this.examinations.getTarget();
        //找出所有考试的考试成绩实例
        List<ExamCase> ecs = new LinkedList();
        for (Examination ex : exams) {
            List<ExamCase> ec = this.examCaseDAO.findSubmittedExamCaseByExamination(ex.getId());
            ecs.addAll(ec);
        }
        //将符合部门筛选条件的考试成绩实例加入cases中
        this.cases.clear();
        for (ExamCase ec : ecs) {
            BbsUser bu = ec.getUser();
            if (bu != null) {
                if (this.testIfIn(dms, bu, ec.getGenTime())) {
                    this.cases.add(ec);
                }
            }
        }
        this.computeAverageScore();
        return null;
    }

    private boolean testIfIn(Set<DictionaryModel> dms, BbsUser bu, Date date) {
        //此方法用来测试bu考生在不在dms这个部门列表中
        String gid = bu.getGroupStr();
        if (gid != null) {
            for (DictionaryModel d : dms) {
                if (gid.contains(d.getId())) {
                    if (date.getTime() >= date1.getTime() && date.getTime() <= date2.getTime()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void computeAverageScore() {

        this.averageScore = 0D;
        this.averageCaseScore = 0D;
        this.averageChoiceScore = 0D;
        this.averageEssayScore = 0D;
        this.averageFileScore = 0D;
        this.averageFillScore = 0D;
        this.averageJudgeScore = 0D;
        this.averageMultiChoiceScore = 0;
        double size = this.cases.size();
        this.stats.clear();
        this.freq.clear();
        this.barModel.getSeries().clear();
        this.lineModel.getSeries().clear();
        if (size != 0) {
            for (ExamCase ec : this.cases) {
                this.stats.addValue(ec.getScore());//加入描述统计
                this.freq.addValue(ec.getScore());//加入频次统计
                this.averageScore += ec.getScore();
                this.averageCaseScore += ec.getCaseScore();
                this.averageChoiceScore += ec.getChoiceScore();
                this.averageEssayScore += ec.getEssayScore();
                this.averageFileScore += ec.getFileScore();
                this.averageFillScore += ec.getFillScore();
                this.averageJudgeScore += ec.getJudgeScore();
                this.averageMultiChoiceScore += ec.getMultiChoiceScore();
            }
            this.averageScore = this.averageScore / size;
            this.averageCaseScore = this.averageCaseScore / size;
            this.averageChoiceScore = this.averageChoiceScore / size;
            this.averageEssayScore = this.averageEssayScore / size;
            this.averageFileScore = this.averageFileScore / size;
            this.averageFillScore = this.averageFillScore / size;
            this.averageJudgeScore = this.averageJudgeScore / size;
            this.averageMultiChoiceScore = this.averageMultiChoiceScore / size;
        }
        this.createChart();
    }

    public void createChart() {

        ChartSeries cs = new ChartSeries();//bar
        int sectNum = 10;
        double interval = this.stats.getMax() - this.stats.getMin();
        double section = interval / sectNum;
        for (int i = 1; i <= sectNum; i++) {
            double sub = this.stats.getMin() + (i - 1) * section;
            double sup = this.stats.getMin() + i * section;

            long count = 0;
            Iterator it = this.freq.valuesIterator();
            while (it.hasNext()) {
                Double val = (Double) it.next();
                boolean condition = false;
                if (i != sectNum) {
                    condition = (val >= sub && val < sup);
                } else {
                    condition = (val >= sub && val <= sup);
                }
                if (condition) {
                    long cc = this.freq.getCount(val);
                    count += cc;
                }
            }
            String label;
            DecimalFormat df = new DecimalFormat("######0.00");
            if (i != sectNum) {
                label = "[" + df.format(sub) + " - " + df.format(sup) + ")";
            } else {
                label = "[" + df.format(sub) + " - " + df.format(sup) + "]";;
            }
            cs.set(label, count);

        }
        barModel.addSeries(cs);

        ChartSeries cs2 = new ChartSeries();//line
        Iterator it = this.freq.valuesIterator();
        while (it.hasNext()) {
            Double val = (Double) it.next();
            long cc = this.freq.getCount(val);
            cs2.set(val, cc);
        }
        lineModel.addSeries(cs2);

    }

}
