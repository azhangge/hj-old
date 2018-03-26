package com.hjedu.customer.dao;

import java.util.List;

import com.hjedu.customer.entity.Teacher;

public interface ITeacherDAO {
	public abstract void addTeacher(Teacher teacher);

    public abstract void updateTeacher(Teacher teacher);

    public abstract void deleteTeacher(String id);
    
    public abstract void deleteTeacherByUrn(String urn,String bussinessId);

    public abstract Teacher findTeacher(String id);
    
    public abstract Teacher findTeacherByUrn(String name,String bussinessId);
    
    public abstract List<Teacher> findTeachersLikeUrn(String urn,String bussinessId);

    public abstract List<Teacher> findAllTeacher(String bussinessId);

	public abstract List<Teacher> findAllShowTeacher(String bussinessId);

}