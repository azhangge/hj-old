package com.hjedu.examination.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.ExamCaseFacet;

public interface IExamCaseDAO {

	public abstract void addExamCase(ExamCase m);

	public abstract void updateExamCase(ExamCase m);

	public void updateBulkExamCase(Collection<ExamCase> ms);

	public abstract void deleteExamCase(String id);

	public void deleteBulkExamCaseByExam(String id);

	public void deleteBulkExamCaseFacet(List<ExamCaseFacet> cases);

	public void removeStatisticByCase(String caseId);

	public void deleteBulkExamCase(List<ExamCase> cases);

	public abstract ExamCase findExamCase(String id);

	public boolean checkIfExists(String id);

	public abstract List<ExamCase> findAllExamCase();

	public abstract List<ExamCase> findAllSubmittedExamCase();

	public List<ExamCase> findAllSavedExamCase();

	public List<ExamCase> findAllUnpubExamCase();

	public List<ExamCase> findAllUnpubExamCaseByExam(String id);

	public abstract List<ExamCase> findExamCaseByExamination(String examId);

	public List<ExamCase> findSubmittedExamCaseByExamination(String id);

	public List<ExamCaseFacet> findLotsOfExamCase(int offSet, int num,String businessId);

	public List<ExamCaseFacet> findOrderedExamCase(int offSet, int num, String field, String type,String businessId);

	public List<ExamCaseFacet> findExamCaseByLike(Map<String, Object> fms, int offSet, int num,String businessId);

	public ExamCase findTopScoreExamCase(String examId);

	public long getTotalExamCaseBbsScore(String userId, Date btime, Date etime);

	public long getExamCaseNum(String businessId);

	public long getExamCaseNumByAdmin(AdminInfo admin,String businessId);

	public long getExamCaseNumByLike(Map<String, Object> fms,String businessId);

	public long getExamCaseNumByLikeAndAdmin(AdminInfo admin, Map<String, Object> fms,String businessId);

	public List<ExamCaseFacet> findLotsOfExamCaseByAdmin(AdminInfo admin, int offSet, int num,String businessId);

	public List<ExamCaseFacet> findOrderedExamCaseByAdmin(AdminInfo admin, int offSet, int num, String field,
			String type,String businessId);

	public List<ExamCaseFacet> findExamCaseByLikeAndAdmin(AdminInfo admin, Map<String, Object> fms, int offSet,
			int num,String businessId);

	public List<ExamCase> findObsolatedExternalExamCase();

	public abstract List<ExamCase> findExamCaseByUser(String userId);

	public abstract List<ExamCaseFacet> findExamCaseFacetByUser(String userId);

	public List<ExamCase> findExamCaseByExaminationAndUser(String examinationId, String uid);

	public long getParticipateNumByExamAndUser(String examId, String uid);

	public long getParticipateNumByExamAndDate(String examId, Date date);

	public long getParticipateNumByExam(String examId);

	public void deleteAllExamCase();

	public List<ExamCaseFacet> findSingleExamCaseForEachUserByExamination(String examId);

	public List<ExamCase> findSingleExamCaseForEachUser();

	public List<ExamCaseFacet> findSingleExamCaseForEachUserByExamAndAdmin(String examId, AdminInfo admin, int offSet,
			int num);

	public List<ExamCase> findSingleExamCaseForEachUserByAdmin(AdminInfo admin, int offSet, int num);

	public List<ExamCaseFacet> findExamCaseByExamAndAdmin(String examId, AdminInfo admin, int offSet, int num);

	public void publishExamCases(List<String> ids);

	List<ExamCase> findFormalExamCaseByExamination(String id);

	ExamCase findUserTopScoreExamCase(String examId, String userId);

	public abstract List<ExamCase> findFinishedExamCasesByUser(String id);

	/**
	 * 设置考试通过
	 * @param ids
	 */
	void setExamCasesPass(List<String> ids);

	List<ExamCaseFacet> findIntensiveExamCaseFacetByUser(String userId);

	List<ExamCase> findExamCaseByExamination2(String id);


}
