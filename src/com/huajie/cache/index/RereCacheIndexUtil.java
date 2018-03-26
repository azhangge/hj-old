package com.huajie.cache.index;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.service.IExamCaseCacheService;
import com.huajie.cache.IRereCacheInstance;
import com.huajie.cache.RereCacheManager;
import com.huajie.cache.annotation.RereIndex;
import com.huajie.util.SpringHelper;

/**
 * 本工具类用于帮助完成索引检查
 *
 * @author huajie.com
 */
public class RereCacheIndexUtil {

    /**
     *
     * @param <T>
     * @param inst
     * @param data
     */
    public static <T> void checkIndexWithPut(IRereCacheInstance inst, T data) {
        Field[] fields = data.getClass().getDeclaredFields();
       //System.out.println("fields:"+fields.length);
        if (fields != null) {
            for (Field f : fields) {
                //System.out.println(f.getName());
                if (f.isAnnotationPresent(RereIndex.class)) {//找出被@RereIndex标注过的属性
                    System.out.println("Find RereIndex:"+f.getName());
                    String name = f.getName();
                    //若注解中的属性name不为空，则使用属性中的值
                    String temp = f.getAnnotation(RereIndex.class).name();
                    if (!StringUtils.isEmpty(temp)) {
                        name = temp;
                    }
                    Method method = null;
                    String id = null;
                    Object value = null;
                    
                    RereCacheIndexInstance index = (RereCacheIndexInstance) inst.getIndexes().get(name);
                    if (index == null) {
                        index = new RereCacheIndexInstance();
                        index.setName(f.getName());
                        inst.getIndexes().put(name, index);
                    }
                    String upperName = name.substring(0, 1).toUpperCase() + name.substring(1);//变为首字母大写形式
                    try {
                        method = data.getClass().getMethod("get" + upperName);
                        value = method.invoke(data);//获取属性的值
                        id = data.getClass().getMethod("getId").invoke(data).toString();//获取id
                        if (value == null) {
                            continue;
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block  
                        e.printStackTrace();
                    }
                    if (id != null && value != null) {
                        index.putData(value.toString(), id);
                    }
                }
            }
        }
    }

    /**
     *
     * @param <T>
     * @param inst
     * @param id
     */
    public static <T> void checkIndexWithDelete(IRereCacheInstance inst, String id) {
        Collection<RereCacheIndexInstance> indexes = inst.getIndexes().values();
        for (RereCacheIndexInstance ind : indexes) {
            ind.deleteData(id);
        }
    }

    /**
     * 按关键字查询索引中对应的ID
     *
     * @param inst
     * @param indexName
     * @param keyWord
     * @return
     */
    public static Set<String> queryIds(IRereCacheInstance inst, String indexName, String keyWord) {
        Set<String> ids = new HashSet();
        RereCacheIndexInstance index = (RereCacheIndexInstance) inst.getIndexes().get(indexName);
        if (index != null) {
            RereCacheIndexItem item = index.getIndexItems().get(keyWord);
            if (item != null) {
                ids = item.getIds();
            }
        }
        return ids;
    }
    
    public static void main(String args[]) {
        IExamCaseDAO caseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
        IExamCaseCacheService cache = SpringHelper.getSpringBean("ExamCaseCacheService");
        List<ExamCase> cases = caseDAO.findAllExamCase();
        for (ExamCase ec : cases) {
            cache.addExamCase(ec);
        }
        IRereCacheInstance ci=RereCacheManager.getReplicatedInstance("exam_cases");
        int num=ci.getIndexes().size();
        System.out.println(num);
        
    }
    
}
