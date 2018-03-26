package com.hjedu.course.service;

import com.hjedu.course.entity.LessonLog;
import com.hjedu.course.entity.LessonTypeLog;
import com.hjedu.course.entity.StudyPlan;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.entity.ExamCase;

public interface IStudyPlanLogService {
	void createFinishLessonLog(LessonLog lessonLog);
	void createFinishExamLog(ExamCase examCase);
	void createStudyPlanLog(StudyPlan studyPlan, BbsUser user);
}
