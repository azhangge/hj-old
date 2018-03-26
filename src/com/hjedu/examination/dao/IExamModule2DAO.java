package com.hjedu.examination.dao;

import java.util.List;

import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.entity.ExamModuleModel;

public interface IExamModule2DAO {

    public abstract void addExamModuleModel(ExamModuleModel p);

    public abstract ExamModuleModel findExamModuleModel(String pid);

    public abstract void updateExamModuleModel(ExamModuleModel p);

    public abstract List<ExamModuleModel> findAllExamModuleModel(String businessId);

    public abstract List<ExamModuleModel> findExamModuleModelByUrlType(String type);

    public abstract List<ExamModuleModel> findAllSonExamModuleModel(String fatherID, String businessId);

    public abstract void deleteExamModuleModel(String pid);

    public void deleteExamModuleModelAndAllDescendants(String id);

    public List<ExamModuleModel> loadAllDescendants(String fid);

    public void loadAllDescendants(String fid, List<ExamModuleModel> sons);

    public ExamModuleModel findExamModuleByName(String name);

    public void deleteAllExamModuleExceptRoot(String rootId);

    public List<ExamModuleModel> findRootExamModuleModelByRootId(String rootId);

	public abstract List<ExamModuleModel> findAllSonExamModuleModelAndAdmin(String id, AdminInfo adminBySession,String businessId);

	List<ExamModuleModel> findExamModuleByIdList(List<String> ids);

	List<ExamModuleModel> findExamModuleBySimilarName(String name);
}
