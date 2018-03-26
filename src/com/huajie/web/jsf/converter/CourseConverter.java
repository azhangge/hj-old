package com.huajie.web.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;

import org.springframework.stereotype.Component;

import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.entity.LessonType;
import com.huajie.app.view.Course;
import com.huajie.util.SpringHelper;

@FacesConverter(forClass=Course.class, value="courseConverter")
@Component("courseConverter")
public class CourseConverter implements javax.faces.convert.Converter {
    
    
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String str) {
        String id = "-1";
        if (str != null) {
            id = str;
            ILessonTypeDAO lessonTypeDAO = SpringHelper.getSpringBean("LessonTypeDAO");
            LessonType ex = lessonTypeDAO.findLessonType(id);
            return ex;
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if (o != null) {
        	LessonType ex = (LessonType) o;
        	return ex.getId().toString();
        } else {
            return null;
        }
    }
}
