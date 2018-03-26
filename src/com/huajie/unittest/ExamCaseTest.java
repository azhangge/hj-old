package com.huajie.unittest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.service.IExamCaseService;
import com.hjedu.examination.service.impl.ExaminationService;
import com.huajie.exam.pool.ExamPaperPool;
import com.huajie.util.SpringHelper;

/**
 * This class is used to test the performance of creating exam case and
 * submitting exam case.
 *
 * @author wbdwl.com
 */
public class ExamCaseTest {

    IExamCaseDAO caseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
    ExaminationService examDAO = SpringHelper.getSpringBean("ExaminationService");
    IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");

    private void init() {

    }

    public ExamCase buildExamCaseFromDB(String eid) {
        IExamCaseService examCaseService = null;
        BbsUser bu = userDAO.findSysUser();
        ExamCase ec = null;
        Examination examt = this.examDAO.findExamination(eid);

        if ("random".equals(examt.getPaperType())) {
            examCaseService = SpringHelper.getSpringBean("ExamCaseService");;
        } else if ("random2".equals(examt.getPaperType())) {
            examCaseService = SpringHelper.getSpringBean("ExamCaseRandom2Service");
        } else if ("manual".equals(examt.getPaperType())) {
            examCaseService = SpringHelper.getSpringBean("ManualExamCaseService");
        }

        ec = ExamPaperPool.takePaper(eid);//从试卷池中取一份试卷
        ec.setUser(bu);
        return ec;
    }

    public static void main(String args[]) {
        ExamCaseTest test = new ExamCaseTest();
        ExamCase ec = test.buildExamCaseFromDB("1cef86ab-8713-4e47-9236-2f79f9b09f32");

        GsonBuilder gb = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation() //不导出实体中没有用@Expose注解的属性  
                .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式  
                .serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")//时间转化为特定格式      
                .setPrettyPrinting() //对json结果格式化.  
                .setVersion(1.0);  //有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化.  
        //@Since(版本号)能完美地实现这个功能.还的字段可能,随着版本的升级而删除,那么  
        //@Until(版本号)也能实现这个功能,GsonBuilder.setVersion(double)方法需要调用.  

        Gson gson = gb.create();
        String json = gson.toJson(ec);
        System.out.println(json);

        ExamCase stud = gson.fromJson(json, ExamCase.class);
        System.out.println(stud.getName());
    }

}
