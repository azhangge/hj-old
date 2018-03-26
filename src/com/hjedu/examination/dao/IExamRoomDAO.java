package com.hjedu.examination.dao;

import java.util.List;

import com.hjedu.examination.entity.ExamRoom;

public interface IExamRoomDAO {

    public abstract void addExamRoom(ExamRoom m);

    public abstract void updateExamRoom(ExamRoom m);

    public abstract void deleteExamRoom(String id);

    public abstract ExamRoom findExamRoom(String id);

    public abstract List<ExamRoom> findAllExamRoom();

}
