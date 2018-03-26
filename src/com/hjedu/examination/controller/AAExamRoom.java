package com.hjedu.examination.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.examination.dao.IExamRoomDAO;
import com.hjedu.examination.entity.ExamRoom;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class AAExamRoom implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IExamRoomDAO roomDAO = SpringHelper.getSpringBean("ExamRoomDAO");
    ExamRoom room = null;
    boolean flag = false;
    
    public boolean isFlag() {
        return flag;
    }
    
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    
    public ExamRoom getRoom() {
        return room;
    }
    
    public void setRoom(ExamRoom room) {
        this.room = room;
    }
    
    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        String id = request.getParameter("id");
        if (id != null) {
            this.flag = true;
            this.room = this.roomDAO.findExamRoom(id);
        } else {
            this.room = new ExamRoom();
        }
    }
    
    public String done() {
        if (flag) {
            this.logger.log("修改了考场：" + room.getName());
            this.roomDAO.updateExamRoom(room);
        } else {
            this.logger.log("添加了考场：" + room.getName());
            this.roomDAO.addExamRoom(room);
        }
        return "ListExamRoom?faces-redirect=true";
    }
}
