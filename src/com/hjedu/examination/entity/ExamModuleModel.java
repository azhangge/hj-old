package com.hjedu.examination.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.ICaseQuestionDAO;
import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IEssayQuestionDAO;
import com.hjedu.examination.dao.IExamKnowledgeDAO;
import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.dao.IFillQuestionDAO;
import com.hjedu.examination.dao.IJudgeQuestionDAO;
import com.hjedu.examination.dao.IModuleExamCaseDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.entity.module.ModuleExamCase;
import com.hjedu.examination.entity.module2.ModuleExamination2;
import com.hjedu.platform.controller.ClientSession;
import com.huajie.util.JsfHelper;
import com.huajie.util.ObjectWithAdmin;
import com.huajie.util.SpringHelper;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.eclipse.persistence.annotations.Noncacheable;


/**
 * 习题模块
 * 凡是需要关联的一对多或者多对多关系，都应设为EAGER，LAZY关系或者不用，或者设置为getter方法中使用DAO获取
 * 关联还是导致数据库加载缓慢、外健约束导致数据库表破坏的原因
 * 否则在分布式环境下进行被序列化后数据库SESSION将失效，导致获取LAZY关联全部失效
 * @author huajie
 */
@Entity
@Table(name = "exam_module")
public class ExamModuleModel implements Serializable, Comparable,ObjectWithAdmin {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();
    @Column(name = "name")
    private String name;
    @Column(name = "father_id")
    private String fatherId;
    @Column(name = "ord")
    private int ord;
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genTime = new Date();
    @Column(name = "ancestors", length = 900)
    private String ancestors = "";

    @Noncacheable
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "module")
    private ModuleExamination2 exam;//章节逐题练习

    //级联删除操作已经取消，在DAO中手动删除
    

    

    @ManyToMany()
    @Noncacheable
    @JoinTable(name = "exam_admin_module",
            joinColumns = {
                @JoinColumn(name = "module_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "admin_id", referencedColumnName = "id")})
    private List<AdminInfo> admins;

    @Column(name = "front_show")
    private Boolean frontShow = true;

    @Lob
    @Column(name = "group_str")
    private String groupStr = "";

    @Transient
    List<ExamModuleModel> familyTree;
    @Transient
    long choiceNum = 0;
    @Transient
    long multiChoiceNum = 0;
    @Transient
    long fillNum = 0;
    @Transient
    long judgeNum = 0;
    @Transient
    long essayNum = 0;
    @Transient
    long fileNum = 0;
    @Transient
    long caseNum = 0;
    @Transient
    long totalNum = 0;
    
    @Transient
    long knowledgeNum = 0;
    
    @Transient//用于考试设置页面计算每个试题模块设置多少题目
    ModuleRandomPaper modulePaper;
    
    @Transient
    private boolean selected = false;
    @Transient
    private List<ExamModuleModel> sons;
    @Transient
    private ModuleExamCase examCase;
    @Transient
    private int progress = 1;
    @Transient
    private int doneNum = 0;
    @Transient
    @JsonIgnore
    private int wrongNum = 0;
    @Transient
    private int deep;
    @Transient
    private String adminStr;
    
    @Column(name = "MD5")
    private String MD5;
    
    @Column(name = "version")
    private long version;
    
    @Column(name = "JsonFilePath")
    private String JsonFilePath;
    
    @Column(name = "JsonDownLoadUrl")
    private String JsonDownLoadUrl;

    /**
     * 企业id
     */
    @Column(name = "business_id")
	private String businessId;
    
    public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	
    public String getJsonDownLoadUrl() {
		return JsonDownLoadUrl;
	}

	public void setJsonDownLoadUrl(String jsonDownLoadUrl) {
		JsonDownLoadUrl = jsonDownLoadUrl;
	}

	public String getJsonFilePath() {
		return JsonFilePath;
	}

	public void setJsonFilePath(String jsonFilePath) {
		JsonFilePath = jsonFilePath;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public String getMD5() {
		return MD5;
	}

	public void setMD5(String mD5) {
		MD5 = mD5;
	}

	public ExamModuleModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }


    public List<ExamModuleModel> getSons() {
        return sons;
    }

    public void setSons(List<ExamModuleModel> sons) {
        this.sons = sons;
    }

    public int getDeep(String rootId) {
        IExamModule2DAO dicDAO = SpringHelper.getSpringBean("ExamModule2DAO");
        ExamModuleModel dm = this;
        int i = 1;
        while (!rootId.equals(dm.getFatherId())) {
            dm = dicDAO.findExamModuleModel(dm.getFatherId());
            i++;
        }
        this.deep = i;
        return deep;
    }

    public void setDeep(int deep) {
        this.deep = deep;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<AdminInfo> getAdmins() {
        return admins;
    }

    public void setAdmins(List<AdminInfo> admins) {
        this.admins = admins;
    }

    public ModuleExamination2 getExam() {
        return exam;
    }

    public void setExam(ModuleExamination2 exam) {
        this.exam = exam;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    public ModuleRandomPaper getModulePaper() {
        return modulePaper;
    }

    public void setModulePaper(ModuleRandomPaper modulePaper) {
        this.modulePaper = modulePaper;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public long getChoiceNum() {
        IChoiceQuestionDAO questionDAO = SpringHelper.getSpringBean("ChoiceQuestionDAO");
        choiceNum = questionDAO.getQuestionNumByModule(id);
        return choiceNum;
    }

    public void setChoiceNum(long choiceNum) {
        this.choiceNum = choiceNum;
    }

    public long getMultiChoiceNum() {
        IMultiChoiceQuestionDAO questionDAO = SpringHelper.getSpringBean("MultiChoiceQuestionDAO");
        multiChoiceNum = questionDAO.getQuestionNumByModule(id);
        return multiChoiceNum;
    }

    public void setMultiChoiceNum(long multiChoiceNum) {
        this.multiChoiceNum = multiChoiceNum;
    }

    public long getFillNum() {
        IFillQuestionDAO questionDAO = SpringHelper.getSpringBean("FillQuestionDAO");
        choiceNum = questionDAO.getQuestionNumByModule(id);
        return choiceNum;
    }

    public void setFillNum(long fillNum) {
        this.fillNum = fillNum;
    }

    public long getJudgeNum() {
        IJudgeQuestionDAO questionDAO = SpringHelper.getSpringBean("JudgeQuestionDAO");
        choiceNum = questionDAO.getQuestionNumByModule(id);
        return choiceNum;
    }

    public void setJudgeNum(long judgeNum) {
        this.judgeNum = judgeNum;
    }

    public long getFileNum() {
        IFileQuestionDAO questionDAO = SpringHelper.getSpringBean("FileQuestionDAO");
        fileNum = questionDAO.getQuestionNumByModule(id);
        return fileNum;
    }

    public void setFileNum(long fileNum) {
        this.fileNum = fileNum;
    }

    public long getEssayNum() {
        IEssayQuestionDAO questionDAO = SpringHelper.getSpringBean("EssayQuestionDAO");
        essayNum = questionDAO.getQuestionNumByModule(id);
        return essayNum;
    }

    public long getCaseNum() {
        ICaseQuestionDAO questionDAO = SpringHelper.getSpringBean("CaseQuestionDAO");
        caseNum = questionDAO.getQuestionNumByModule(id);
        return caseNum;
    }

    public void setCaseNum(long caseNum) {
        this.caseNum = caseNum;
    }

    public void setEssayNum(long essayNum) {
        this.essayNum = essayNum;
    }


    public long getTotalNum() {
        totalNum = this.getChoiceNum() + this.getMultiChoiceNum() + this.getFillNum() + this.getJudgeNum() + this.getEssayNum() + this.getFileNum() + this.getCaseNum();
        return totalNum;
    }

    public void setTotalNum(long totalNum) {
        this.totalNum = totalNum;
    }

    public Boolean getFrontShow() {
        return frontShow;
    }

    public void setFrontShow(Boolean frontShow) {
        this.frontShow = frontShow;
    }

    public List<ExamModuleModel> getFamilyTree() {
        return familyTree;
    }

    public void setFamilyTree(List<ExamModuleModel> familyTree) {
        this.familyTree = familyTree;
    }

    public String getGroupStr() {
        return groupStr;
    }

    public void setGroupStr(String groupStr) {
        this.groupStr = groupStr;
    }

    public ModuleExamCase getExamCase() {
        IModuleExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ModuleExamCaseDAO");
        ClientSession cs = JsfHelper.getBean("clientSession");
        BbsUser bu = cs.getUsr();
        ModuleExamination2 examt = this.getExam();
        if (bu != null && examt != null) {
            examCase = examCaseDAO.findModuleExamCaseByExaminationAndUser(examt.getId(), bu.getId());
            return examCase;
        } else {
            return null;
        }
    }

    public void setExamCase(ModuleExamCase examCase) {
        this.examCase = examCase;
    }

    public int getProgress() {
        ModuleExamCase mc = getExamCase();
        if (mc != null) {
            progress = mc.getProgress();
        }
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getDoneNum() {
        ModuleExamCase mc = getExamCase();
        if (mc != null) {
            doneNum = mc.getDoneNum();
        } else {
            doneNum = 0;
        }
        return doneNum;
    }

    public void setDoneNum(int doneNum) {
        this.doneNum = doneNum;
    }

    public int getWrongNum() {
        ModuleExamCase mc = getExamCase();
        if (mc != null) {
            wrongNum = mc.getWrongNum();
        }
        return wrongNum;
    }

    public void setWrongNum(int wrongNum) {
        this.wrongNum = wrongNum;
    }

    public String getAdminStr() {
        StringBuilder sb = new StringBuilder();
        for (AdminInfo ai : this.admins) {
            sb.append(ai.getId());
            sb.append(";");
        }
        adminStr = sb.toString();
        return adminStr;
    }

    public void setAdminStr(String adminStr) {
        this.adminStr = adminStr;
    }

    public long getKnowledgeNum() {
        IExamKnowledgeDAO dao=SpringHelper.getSpringBean("ExamKnowledgeDAO");
        knowledgeNum=dao.getKnowledgeNumByModule(id);
        return knowledgeNum;
    }

    public void setKnowledgeNum(long knowledgeNum) {
        this.knowledgeNum = knowledgeNum;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExamModuleModel)) {
            return false;
        }
        ExamModuleModel other = (ExamModuleModel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        ExamModuleModel oo = (ExamModuleModel) o;
        return this.ord - oo.getOrd();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "com.huajie.model.ExamModuleModel[ id=" + id + " ]";
    }
}
