package com.huajie.web.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import org.springframework.stereotype.Component;

import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.Examination;
import com.huajie.util.SpringHelper;

@FacesConverter(forClass=Examination.class, value="examinationConverter")
@Component("examinationConverter")
public class ExaminationConverter implements javax.faces.convert.Converter {
    
    
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String str) {
        String id = "-1";
        if (str != null) {
            id = str;
            IExaminationDAO examinationDAO = SpringHelper.getSpringBean("ExaminationDAO");
            Examination ex = examinationDAO.findExamination(id);
            return ex;
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if (o != null) {
            Examination ex = (Examination) o;
            return ex.getId().toString();
        } else {
            return null;
        }
    }
}
