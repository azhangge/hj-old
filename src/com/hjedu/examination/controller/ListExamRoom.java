package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.examination.dao.IExamRoomDAO;
import com.hjedu.examination.entity.ExamRoom;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListExamRoom implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IExamRoomDAO roomDAO = SpringHelper.getSpringBean("ExamRoomDAO");
    List<ExamRoom> rooms;

    public List<ExamRoom> getRooms() {
        return rooms;
    }

    public void setRooms(List<ExamRoom> rooms) {
        this.rooms = rooms;
    }

    @PostConstruct
    public void init() {
        this.rooms = this.roomDAO.findAllExamRoom();
    }

    public void delete(String id) {
        ExamRoom er=this.roomDAO.findExamRoom(id);
        this.logger.log("删除了考场："+er.getName());
        this.roomDAO.deleteExamRoom(id);
        this.rooms = this.roomDAO.findAllExamRoom();
    }
}
