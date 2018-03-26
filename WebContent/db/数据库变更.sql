--LESSON_TYPE_LOG
alter table LESSON_TYPE_LOG add study_plan_id      VARCHAR2(255);
alter table LESSON_TYPE_LOG add type               VARCHAR2(255);
alter table LESSON_TYPE_LOG add  if_required        NUMBER(1) default 0;
alter table LESSON_TYPE_LOG add  pass_class_num     NUMBER(10);
alter table LESSON_TYPE_LOG add  plan_class_num     NUMBER(10);
--study_plan
alter table study_plan add available_begain    TIMESTAMP(6);
alter table study_plan add  available_end       TIMESTAMP(6);
alter table study_plan add  required_course_num NUMBER(10);
alter table study_plan add  required_exam_num   NUMBER(10);
alter table study_plan add  if_buy_courses      NUMBER(1) default 0;
alter table study_plan add  finished_rclass_num   NUMBER(10);
--teacher
alter table teacher add teacher_type NUMBER(1) default 0;
alter table teacher add if_show NUMBER(1) default 0;
update teacher set if_show=1;
update teacher set teacher_type=0;
--y_system_config
alter table y_system_config add pic_change_time       NUMBER(10);
--EXAM_CASE
alter table EXAM_CASE add if_passed          NUMBER(1) default 0;
--0315
alter table exam_lesson_type add approved NUMBER(10) default 0;
alter table teacher add user_id VARCHAR2(255);
alter table teacher add COURSE_TYPE_STR CLOB;
alter table rerebbs_user add TEACHER_ID VARCHAR2(255);
alter table plan_log add finished_rclass_num   NUMBER(10);
--0316
alter table EXAM_LESSON_TYPE add practice_num     NUMBER(10);
alter table y_lesson add practice_num     NUMBER(10);
--0318
alter table teacher add need_approved   NUMBER(1) default 0;
--0330
update exam_LESSON_TYPE f set f.user_num=0 where f.user_num is null;
alter table examination add exam_type            NUMBER(10);
alter table examination add i_pass_score         VARCHAR2(255);
alter table examination add exam_notice          CLOB;
alter table examination add notice_name          VARCHAR2(255);
alter table examination add pub_time             TIMESTAMP(6);

alter table rerebbs_user add COMPANY VARCHAR2(255);
update examination set exam_type='0' where exam_type is null;

--0406
alter table exam_LESSON_TYPE add reason            CLOB;
alter table exam_LESSON_TYPE add approveteacher_id VARCHAR2(255);
alter table exam_LESSON_TYPE add course_type_cn    CLOB;
alter table exam_LESSON_TYPE add total_time        NUMBER(19);

alter table STUDY_PLAN add course_num          NUMBER(10);
alter table STUDY_PLAN add user_num            NUMBER(10);
--0415
alter table REREBBS_USER add collection_courses CLOB;

--0421
alter table REREBBS_CLIENT_FILE add PDF_MD5 VARCHAR2(255 BYTE) ;
alter table REREBBS_CLIENT_FILE add PDF_SIZE VARCHAR2(255 BYTE) ;
--0503
alter table teacher add work_years VARCHAR2(255 BYTE) ;
alter table teacher add COMPANY VARCHAR2(255 BYTE) ;
alter table Y_SYSTEM_INFO add url VARCHAR2(255);

--0509
alter table teacher add id_num VARCHAR2(200 CHAR);
--0517
alter table teacher add back_picture    VARCHAR2(255);
--0526
alter table GENERAL_QUESTION add   qtype      VARCHAR2(255);
alter table GENERAL_QUESTION add  question   VARCHAR2(4000 CHAR);
alter table GENERAL_QUESTION add  realoption VARCHAR2(255);
alter table GENERAL_QUESTION add  type       VARCHAR2(255);
alter table GENERAL_QUESTION add  answers    VARCHAR2(255);
--0531
alter table teacher add   front_picture   VARCHAR2(255);
alter table teacher add  gender          NUMBER(10);
--0619
alter table Y_SYSTEM_INFO add welcomePage VARCHAR2(255);