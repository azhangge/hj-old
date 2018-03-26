package com.hjedu.customer.dao.impl;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import org.apache.commons.io.IOUtils;

import com.hjedu.customer.dao.IBbsFileDAO;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.dao.IFlashDAO;
import com.hjedu.platform.dao.IImgDAO;
import com.hjedu.platform.dao.IMp3DAO;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.entity.BbsFileModel;
import com.hjedu.platform.entity.FileSaveStatus;
import com.huajie.app.util.FileUtil;
import com.huajie.app.util.StringUtil;

public class BbsFileDAOImpl implements IBbsFileDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;
    IBbsUserDAO cu;
    // LockDAO lockDAO;
    ISystemConfigDAO timesNumDAO;
    IImgDAO imgDAO;
    IFlashDAO flashDAO;
    IMp3DAO mp3DAO;
    String relativeFilePath = "/resources/sys/file/";

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public String getRelativeFilePath() {
        // TODO Auto-generated method stub
        return this.relativeFilePath;
    }

    public void setTimesNumDAO(ISystemConfigDAO timesNumDAO) {
        this.timesNumDAO = timesNumDAO;
    }

    public IImgDAO getImgDAO() {
        return imgDAO;
    }

    public void setImgDAO(IImgDAO imgDAO) {
        this.imgDAO = imgDAO;
    }

    public IFlashDAO getFlashDAO() {
        return flashDAO;
    }

    public void setFlashDAO(IFlashDAO flashDAO) {
        this.flashDAO = flashDAO;
    }

    public IMp3DAO getMp3DAO() {
        return mp3DAO;
    }

    public void setMp3DAO(IMp3DAO mp3DAO) {
        this.mp3DAO = mp3DAO;
    }

    public IBbsUserDAO getCu() {
        return cu;
    }

    public void setCu(IBbsUserDAO cu) {
        this.cu = cu;
    }

    public void delClientFile(String id) {
        BbsFileModel cf = entityManager.find(BbsFileModel.class, id);
        this.delFile(id);
        if(cf!=null){
        	entityManager.remove(cf);
        }
    }

    @Override
    public int delClientFilesByLessonTypeId(List<String> idlist) {
    	if(idlist!=null && idlist.size()>0){
    		String q = "delete from BbsFileModel bsm where bsm.lesson.lessonType.id in :list";
    		Query qu=entityManager.createQuery(q);
    		qu.setParameter("list", idlist);
    		return qu.executeUpdate();
    	}
    	return 0;
    }
    
    public boolean checkNameIfExist(final String name, final String fid) {
        String q = "Select fs from BbsFileModel fs where fs.fileName=:name and fs.fatherID=:fid";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("name", name);
        qu.setParameter("fid", fid);
        List<BbsFileModel> ais = qu.getResultList();

        if (ais.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean checkNameIfExistByUsr(String name, String fid, String uid) {
        String q = "Select fs from BbsFileModel fs where fs.fileName=:name and fs.fatherID=:fid and fs.userId=:uid";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("name", name);
        qu.setParameter("fid", fid);
        qu.setParameter("uid", uid);
        List<BbsFileModel> ais = qu.getResultList();

        if (ais.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public List<BbsFileModel> findAllClientFile() {
        String q = "Select fs from BbsFileModel fs order by fs.uploadTime desc";
        List<BbsFileModel> ais = entityManager.createQuery(q).getResultList();
        for (BbsFileModel p : ais) {
            p.setUser(this.cu.findBbsUser(p.getUserId()));
            List t = this.findAllSonClientFile(p.getId());
            if (t != null) {
                p.setSonNum(t.size());
            }
        }
        return ais;
    }

    public List<BbsFileModel> findAllSonClientFile(final String dic) {
        String q = "Select fs from BbsFileModel fs where fs.fatherID=:dic order by fs.uploadTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("dic", dic);
        List<BbsFileModel> ais = qu.getResultList();
        for (BbsFileModel p : ais) {
            p.setUser(this.cu.findBbsUser(p.getUserId()));
            List t = this.findAllSonClientFile(p.getId());
            if (t != null) {
                p.setSonNum(t.size());
            }
        }
        return ais;
    }
    
    public List<BbsFileModel> findAllFilesOfLesson(final String dic) {
        String q = "Select fs from BbsFileModel fs where fs.lesson.id =:dic order by fs.uploadTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("dic", dic);
        List<BbsFileModel> ais = qu.getResultList();
        for (BbsFileModel p : ais) {
            p.setUser(this.cu.findBbsUser(p.getUserId()));
            List t = this.findAllSonClientFile(p.getId());
            if (t != null) {
                p.setSonNum(t.size());
            }
        }
        return ais;
    }

    public List<BbsFileModel> findAllSonDirsByUsr(final String dic, final String uid) {

        String q = "Select fs from BbsFileModel fs where fs.fatherID=:dic and fs.userId=:uid and fs.ifFolder=true order by fs.uploadTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("dic", dic);
        qu.setParameter("uid", uid);
        List<BbsFileModel> ais = qu.getResultList();
        for (BbsFileModel p : ais) {
            p.setUser(this.cu.findBbsUser(p.getUserId()));
            List t = this.findAllSonClientFile(p.getId());
            if (t != null) {
                p.setSonNum(t.size());
            }
        }
        return ais;
    }

    public List<BbsFileModel> findAllClientFileByUsr(final String uid, final String dic) {
        String q = "Select fs from BbsFileModel fs where fs.userId=:uid and fs.fatherID=:dic order by fs.uploadTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("uid", uid);
        qu.setParameter("dic", dic);
        List<BbsFileModel> ais = qu.getResultList();
        for (BbsFileModel p : ais) {
            p.setUser(this.cu.findBbsUser(p.getUserId()));
            List t = this.findAllSonClientFile(p.getId());
            if (t != null) {
                p.setSonNum(t.size());
            }
        }
        return ais;
    }
    
    public BbsFileModel findAllClientFileByLessionId(String lessonId){
    	String q = "Select fs from BbsFileModel fs where fs.lesson.id=:lessonId";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("lessonId", lessonId);
        List<BbsFileModel> ais = qu.getResultList();
        if(ais==null || ais.size() < 1){
        	return null;
        }else {
			return ais.get(0);
		}        
    }

    public List<BbsFileModel> findAllClientFileByUsr(final String uid) {
        String q = "Select fs from BbsFileModel fs where fs.userId=:uid order by fs.uploadTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("uid", uid);
        List<BbsFileModel> ais = qu.getResultList();
        for (BbsFileModel p : ais) {
            p.setUser(this.cu.findBbsUser(p.getUserId()));
            List t = this.findAllSonClientFile(p.getId());
            if (t != null) {
                p.setSonNum(t.size());
            }
        }
        return ais;
    }

    public BbsFileModel findClientFile(String id) {
        if (id != null) {
            BbsFileModel s = entityManager.find(BbsFileModel.class, id);
            if (s != null) {
                s.setUser(this.cu.findBbsUser(s.getUserId()));
                List t = this.findAllSonClientFile(s.getId());
                if (t != null) {
                    s.setSonNum(t.size());
                }
            }
            return s;
        } else {
            return null;
        }
    }

    public BbsFileModel findClientFile2(String id) {
        if (id != null) {
            BbsFileModel s = entityManager.find(BbsFileModel.class, id);
            return s;
        } else {
            return null;
        }
    }
    
    public void saveClientFileInfo(BbsFileModel s) {
        if (s.getUser() != null) {
            s.setUserId(s.getUser().getId());
        }
        entityManager.persist(s);

    }

    public void updateClientFileInfo(BbsFileModel s) {
        if (s.getUser() != null) {
            s.setUserId(s.getUser().getId());
        }
        entityManager.merge(s);
    }

    public List<BbsFileModel> searchClientFile(final String field, final String keyWords) {
        String q = "Select fs from BbsFileModel fs where fs." + field + " LIKE '%"
                + keyWords + "%' order by ps.uploadTime desc";
        List<BbsFileModel> ais = entityManager.createQuery(q).getResultList();
        for (BbsFileModel p : ais) {
            p.setUser(this.cu.findBbsUser(p.getUserId()));
            List t = this.findAllSonClientFile(p.getId());
            if (t != null) {
                p.setSonNum(t.size());
            }
        }
        return ais;

    }

    private boolean testIfAncestorByDB(BbsFileModel son, String target) {
        String fatherId = son.getFatherID();
        if ("0".equals(target)) {
            return true;
        } else if (fatherId == target) {
            return true;
        } else if (fatherId.equals(0)) {
            return false;
        } else {
            testIfAncestorByDB(this.findClientFile(fatherId), target);
        }
        return false;
    }

    private boolean testIfContain(BbsFileModel c, List<BbsFileModel> ffs) {

        if (c == null) {
            return false;
        } else {
            boolean t1 = false;
            for (BbsFileModel fc : ffs) {
                if (fc.getId() == c.getId()) {
                    t1 = true;
                    break;
                }
            }
            return t1;
        }
    }

    public void saveFile(InputStream iis, String id, String userId) {
        try {
            byte[] bb = new byte[1024];
            InputStream fis = iis;
            String dir = "";
            boolean f = this.timesNumDAO.getSystemConfig().getIfRelative();
            if (f) {
                String tp = this.timesNumDAO.getSystemConfig().getFilePath();
                if (!tp.endsWith("/")) {
                    tp = tp + "/";
                }
                dir = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(tp);

            } else {
                dir = this.timesNumDAO.getSystemConfig().getFilePath();

            }
            if ((!dir.endsWith("\\") || dir.endsWith("/"))) {
                dir = dir + "/";
            }

            File f_dir = new File(dir);
            if (!f_dir.exists()) {
                f_dir.mkdir();
            }
            System.out.println("存储路径：" + dir);

            String rp = dir + userId;
            File f_rp = new File(rp);
            if (!f_rp.exists()) {
                f_rp.mkdir();
            }
            String nfn = rp + "/" + id;
            FileOutputStream fos = new FileOutputStream(nfn);
            // DESTool des = new DESTool();
            // OutputStream cos = des.encryptOutputStream(fos);// 将文件流转换为加密过的文件流

            BufferedInputStream is = new BufferedInputStream(fis);
            BufferedOutputStream os = new BufferedOutputStream(fos);// 将加密过的文件流转换为缓冲流
//            int len = 0;
//            while ((len = is.read(bb)) != -1) {
//                os.write(bb, 0, len);
//            }

            IOUtils.copy(is, os);
            os.flush();

            is.close();
            os.close();
            fis.close();
            // cos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 文件保存路径类似这种形式C:/huajie_exam/a427a963-6edd-41a8-a527-7c39db6df5e7
     */
    @Override
    public void saveFile(InputStream iis, String id, String userId, FileSaveStatus stat) {
        stat.setSaving(true);
        try {
            byte[] bb = new byte[1024];
            InputStream fis = iis;
            String dir = "";
            boolean f = this.timesNumDAO.getSystemConfig().getIfRelative();
            if (f) {
                String tp = this.timesNumDAO.getSystemConfig().getFilePath();
                if (!tp.endsWith("/")) {
                    tp = tp + "/";
                }
                dir = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(tp);

            } else {
                dir = this.timesNumDAO.getSystemConfig().getFilePath();

            }
            if (!(dir.endsWith("\\") || dir.endsWith("/"))) {
                dir = dir + "/";
            }

            File f_dir = new File(dir);
            if (!f_dir.exists()) {
                f_dir.mkdir();
            }
            System.out.println("存储路径：" + dir);

            String rp = dir + userId;
            File f_rp = new File(rp);
            if (!f_rp.exists()) {
                f_rp.mkdir();
            }
            String nfn = rp + "/" + id;
            FileOutputStream fos = new FileOutputStream(nfn);
            BufferedInputStream is = new BufferedInputStream(fis);
            BufferedOutputStream os = new BufferedOutputStream(fos);
            int len = 0;
            while ((len = is.read(bb)) != -1) {
                os.write(bb, 0, len);
                stat.setReaded(stat.getReaded() + len);
            }
            os.flush();
            is.close();
            os.close();
            fis.close();
            // cos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        stat.setSaving(false);
    }

    private void delFile(String id) {
        BbsFileModel file = this.findClientFile(id);
        String nfn = FileUtil.getFileRealPath(file);
        File f1 = new File(nfn);
        if (f1.exists()) {
            f1.delete();
        }
    }

    public void delClientFilebyUsr(final String userId) {
        String sql = "delete from BbsFileModel uls where uls.userId=:userId";
        Query qu = this.entityManager.createQuery(sql);
        qu.setParameter("userId", userId);
        qu.executeUpdate();
    }

    public void delClientFileAndAllDescendants(String id) {
        List<BbsFileModel> sons = new ArrayList();
        this.loadAllDescendants(id, sons);
        this.delClientFile(id);
        for (BbsFileModel s : sons) {
            this.delClientFile(s.getId());
        }
    }

    public List<BbsFileModel> loadAllDescendants(String fid) {
        List<BbsFileModel> sons = new ArrayList();
        this.loadAllDescendants(fid, sons);
        return sons;
    }

    public void loadAllDescendants(String fid, List<BbsFileModel> sons) {
        List<BbsFileModel> ls = this.findAllSonClientFile(fid);
        if (ls.isEmpty()) {
            return;
        } else {
            for (BbsFileModel d : ls) {
                sons.add(d);
                loadAllDescendants(d.getId(), sons);
            }
        }
    }

    
    @Override
    public List<BbsFileModel> findAllDeptSharedClientFileByUsr(final String uid) {

        String q = "Select fs from BbsFileModel fs where fs.userId='" + uid
                + "' and fs.scope='dept' order by fs.uploadTime desc";
        List<BbsFileModel> ais = entityManager.createQuery(q).getResultList();

        for (BbsFileModel p : ais) {
            //p.setUser(this.cu.findClientUser(p.getUserId()));
//            List t = this.findAllSonClientFile(p.getId());
//            if (t != null) {
//                p.setSonNum(t.size());
//            }
        }
        return ais;
    }

    @Override
    public List<BbsFileModel> findAllAccessableClientFileByDept(final String dept,final String dic) {

        String q = "Select fs from BbsFileModel fs where fs.scope='dept' and fs.fatherID='"
                + dic + "' order by fs.uploadTime desc";
        List<BbsFileModel> fs = entityManager.createQuery(q).getResultList();
        List pms = new ArrayList();
        for (BbsFileModel s : fs) {
            BbsUser bu = this.cu.findBbsUser(s.getUserId());
            s.setUser(bu);
            if (bu != null) {
                if (bu.testIfIn(dept)) {
                    List t = this.findAllSonClientFile(s.getId());
                    if (t != null) {
                        s.setSonNum(t.size());
                    }
                    pms.add(s);
                }
            }
        }
        return pms;
    }

    @Override
    public List<BbsFileModel> findAllSharedClientFileByUsr(final String uid) {

        List<BbsFileModel> fs = this.findAllClientFileByUsr(uid);
        List<BbsFileModel> cms = new ArrayList();
        cms.addAll(fs);
        for (BbsFileModel c : fs) {
            String l = c.getSharedUserStr().trim();
            if ("".equals(l)) {
                cms.remove(c);
            }
        }
        return cms;
    }

    @Override
    public List<BbsFileModel> genShareFamilyTree(String id, List<BbsFileModel> ffs) {
        List<BbsFileModel> cs = new ArrayList();
        if (!id.equals("0")) {
            BbsFileModel c = this.findClientFile(id);

            if ((c != null) && testIfContain(c, ffs)) {
                cs.add(c);
                String tt = c.getFatherID();
                while (!"0".trim().equals(tt)) {
                    c = this.findClientFile(tt);
                    if (c != null && testIfContain(c, ffs)) {
                        cs.add(c);
                        tt = c.getFatherID();
                    } else {
                        break;
                    }
                }
            }
        }
        BbsFileModel top = new BbsFileModel();
        top.setId("0");
        top.setFileName("根目录");
        top.setIfFolder(true);
        top.setSecretGrade("内部文件");
        cs.add(top);
        Collections.reverse(cs);

        return cs;
    }

	@Override
	public List<BbsFileModel> findFilesByEXT(String string) {
		String q = "Select fs from BbsFileModel fs where fs.fileExt=:ext";
        List<BbsFileModel> fs = entityManager.createQuery(q).setParameter("ext", string).getResultList();
        return fs;
	}
    
}
