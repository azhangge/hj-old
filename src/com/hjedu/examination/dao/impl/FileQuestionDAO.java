package com.hjedu.examination.dao.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletContext;

import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.dao.IWrongQuestionDAO;
import com.hjedu.examination.entity.FileQuestion;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.service.impl.HashCodeService;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.HTMLCleaner;

public class FileQuestionDAO implements IFileQuestionDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;
    ISystemConfigDAO systemConfigDAO;
    IWrongQuestionDAO wqDAO;
    HashCodeService hashCodeService;

    public IWrongQuestionDAO getWqDAO() {
        return wqDAO;
    }

    public void setWqDAO(IWrongQuestionDAO wqDAO) {
        this.wqDAO = wqDAO;
    }

    public HashCodeService getHashCodeService() {
        return hashCodeService;
    }

    public void setHashCodeService(HashCodeService hashCodeService) {
        this.hashCodeService = hashCodeService;
    }

    public ISystemConfigDAO getSystemConfigDAO() {
        return systemConfigDAO;
    }

    public void setSystemConfigDAO(ISystemConfigDAO systemConfigDAO) {
        this.systemConfigDAO = systemConfigDAO;
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addFileQuestion(FileQuestion m) {
        m.setCleanName(HTMLCleaner.delHTMLTag(m.getName()));
        m.setHashCode(this.hashCodeService.fetchHashCode(m));
        this.entityManager.persist(m);
    }

    @Override
    public void deleteFileQuestion(String id) {
        String q1 = "delete from ExamCaseItemFile cc where cc.question.id='" + id + "'";
        String q2 = "delete from BuffetExamCaseItemFile cc where cc.question.id='" + id + "'";
        String q3 = "delete from ModuleExamCaseItemFile cc where cc.question.id='" + id + "'";
        String q4 = "delete from ContestCaseItemFile cc where cc.question.id='" + id + "'";
        String q5 = "delete from ManualPartItem cc where cc.questionId='" + id + "'";
        
        this.entityManager.createQuery(q1).executeUpdate();
        this.entityManager.createQuery(q2).executeUpdate();
        this.entityManager.createQuery(q3).executeUpdate();
        this.entityManager.createQuery(q4).executeUpdate();
        this.entityManager.createQuery(q5).executeUpdate();
        
        FileQuestion c = this.entityManager.find(FileQuestion.class, id);
        if (c != null) {
            this.entityManager.remove(c);
            this.wqDAO.deleteWrongQuestionByQuestion(id);
        }

    }

    @Override
    public List<FileQuestion> findAllFileQuestion() {
        String q = "Select cq from FileQuestion cq order by cq.genTime desc";
        List<FileQuestion> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public FileQuestion findFileQuestion(String id) {
        FileQuestion c = this.entityManager.find(FileQuestion.class, id);
        return c;
    }

    @Override
    public List<FileQuestion> findFileQuestionByModule(String id) {
        String q = "Select cq from FileQuestion cq where cq.module.id=:id order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        List<FileQuestion> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public FileQuestion findFileQuestionByName(String id) {
        String q = "Select cq from FileQuestion cq where cq.name=:name order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("name", id);
        List<FileQuestion> cqs = qu.getResultList();
        if (cqs.isEmpty()) {
            return null;
        } else {
            return cqs.get(0);
        }
    }
    
    @Override
    public FileQuestion findFileQuestionByHashCode(String id) {
        String q = "Select cq from FileQuestion cq where cq.hashCode=:name order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("name", id);
        List<FileQuestion> cqs = qu.getResultList();
        if (cqs.isEmpty()) {
            return null;
        } else {
            return cqs.get(0);
        }
    }

    @Override
    public void updateFileQuestion(FileQuestion m) {
        m.setCleanName(HTMLCleaner.delHTMLTag(m.getName()));
        m.setHashCode(this.hashCodeService.fetchHashCode(m));
        this.entityManager.merge(m);
    }

    @Override
    public long getQuestionNumByModule(String id) {
        String q = "Select count(ms) from FileQuestion ms where ms.module.id=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        long num = ((Long) qu.getResultList().get(0)).longValue();
        return num;
    }

    @Override
    public void deleteFileQuestionByModule(String moduleId) {
        List<FileQuestion> jqs = this.findFileQuestionByModule(moduleId);
        for (FileQuestion j : jqs) {
            this.deleteFileQuestion(j.getId());
        }
    }

    @Override
    public void saveFile(InputStream fis, String id) {
        try {
            byte[] bb = new byte[1024];
            String dir = "";
            boolean f = this.systemConfigDAO.getSystemConfig().getIfRelative();
            if (f) {
                String tp = this.systemConfigDAO.getSystemConfig().getFilePath();
                if (!tp.endsWith("/")) {
                    tp = tp + "/";
                }
                dir = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(tp);

            } else {
                dir = this.systemConfigDAO.getSystemConfig().getFilePath();

            }
            if (!dir.endsWith("\\")) {
                dir = dir + "\\";
            }

            File f_dir0 = new File(dir);
            if (!f_dir0.exists()) {
                f_dir0.mkdir();
            }

            dir = dir + "user_question_file";

            File f_dir = new File(dir);
            if (!f_dir.exists()) {
                f_dir.mkdir();
            }
            System.out.println("用户文件题文件存储路径：" + dir);

            String nfn = dir + "\\" + id;
            File ffff = new File(nfn);
            FileOutputStream fos = new FileOutputStream(ffff);
            // DESTool des = new DESTool();
            // OutputStream cos = des.encryptOutputStream(fos);// 将文件流转换为加密过的文件流
            BufferedInputStream is = new BufferedInputStream(fis);
            BufferedOutputStream os = new BufferedOutputStream(fos);// 将加密过的文件流转换为缓冲流
            int len = 0;
            long t = 0;
            while ((len = is.read(bb)) != -1) {
                t += len;
                os.write(bb, 0, len);
            }
            is.close();
            os.close();
            fis.close();
            // cos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delFile(String id) {
        String dir;
        boolean f = this.systemConfigDAO.getSystemConfig().getIfRelative();
        if (f) {
            String tp = this.systemConfigDAO.getSystemConfig().getFilePath();
            if (!tp.endsWith("/")) {
                tp = tp + "/";
            }
            dir = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(tp);
        } else {
            dir = this.systemConfigDAO.getSystemConfig().getFilePath();
        }
        if (!dir.endsWith("\\")) {
            dir = dir + "\\";
        }
        dir = dir + "user_question_file";
        String nfn = dir + "\\" + id;
        File f1 = new File(nfn);
        if (f1.exists()) {
            f1.delete();
        }

    }

    @Override
    public boolean checkIfAttached(String id) {
        String dir;
        boolean f = this.systemConfigDAO.getSystemConfig().getIfRelative();
        if (f) {
            String tp = this.systemConfigDAO.getSystemConfig().getFilePath();
            if (!tp.endsWith("/")) {
                tp = tp + "/";
            }
            dir = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(tp);
        } else {
            dir = this.systemConfigDAO.getSystemConfig().getFilePath();
        }
        if (!dir.endsWith("\\")) {
            dir = dir + "\\";
        }
        dir = dir + "user_question_file";
        String nfn = dir + "\\" + id;
        File f1 = new File(nfn);
        if (f1.exists()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public FileQuestion findQuestionByIndex(int index, String mid) {
        String q = "Select cq from FileQuestion cq where cq.module.id=:id order by cq.id desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", mid);
        qu.setFirstResult(index);
        qu.setMaxResults(1);
        FileQuestion cqs = (FileQuestion) qu.getResultList().get(0);
        return cqs;
    }
    
    @Override
    public List<FileQuestion> findQuestionByLike(String str,String businessId) {
    	AdminInfo ai = ExternalUserUtil.getAdminBySession();
        String q1 = "Select cq from FileQuestion cq where cq.businessId=:businessId and cq.cleanName like :str and cq.module in :modules";
        Query qu = this.entityManager.createQuery(q1);
        qu.setParameter("str", "%"+str+"%");
        qu.setParameter("modules", ai.getModules());
        qu.setParameter("businessId", businessId);
        List<FileQuestion> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<FileQuestion> getRandomQuestionOfNumInModule(long num, String mid) {
        List<FileQuestion> cqs = new ArrayList();
        if (num == 0) {
            return cqs;
        }
        long total = this.getQuestionNumByModule(mid);
        long n = num > total ? total : num;
        if (total == n) {
            List temp = this.findFileQuestionByModule(mid);
            Collections.shuffle(temp);
            return temp;
        }
        Set<Long> set = new HashSet();
        while (set.size() < n) {
            long tem = (long) (total * Math.random());
            set.add(tem);
        }
        for (long l : set) {
            FileQuestion cq = this.findQuestionByIndex((int) l, mid);
            cqs.add(cq);
        }
        return cqs;
    }
}
