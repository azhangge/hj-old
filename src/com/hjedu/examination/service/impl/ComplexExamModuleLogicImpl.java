package com.hjedu.examination.service.impl;

import java.io.Serializable;
import java.util.*;

import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.service.ComplexExamModuleLogic;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

public class ComplexExamModuleLogicImpl implements ComplexExamModuleLogic, Serializable {

    ExamModule2Service moduleDAO;

    public ExamModule2Service getModuleDAO() {
        return moduleDAO;
    }

    public void setModuleDAO(ExamModule2Service moduleDAO) {
        this.moduleDAO = moduleDAO;
    }

    public List<ExamModuleModel> buildQulifiedStructureByDic(String fid, List<ExamModuleModel> files) {
        if ("0".equals(fid)) {
//            for (ExamModuleModel c : files) {
//                c.setFamilyTree(this.moduleDAO.genDeptFamilyTree(c.getFatherID()));
//            }
            MyLogger.echo("模块" + fid + "符合条件的子模块数：" + files.size());
            return this.buildStructure(fid, files);
        } else {
            List<ExamModuleModel> ts = new ArrayList();//fid所有子文件中符合条件的找出来
            for (ExamModuleModel c : files) {
                if (testIfAncestor(c, fid)) {
                    ts.add(c);
                }
            }
//            for (ExamModuleModel c : ts) {
//                c.setFamilyTree(this.moduleDAO.genDeptFamilyTree(c.getFatherID()));
//            }
            MyLogger.echo("模块" + fid + "符合条件的子模块数：" + ts.size());
            return this.buildStructure(fid, ts);
        }
    }

    public List<ExamModuleModel> buildStructure1(List<ExamModuleModel> cfs1) {//建立所给文件列表的顶层目录结构
        List<ExamModuleModel> t1 = new ArrayList();//t1、cfs1都是所有的候选文件，其中如果有非顶级项将被删除，分设t1、cfs1是为了便于删除（否则会报concurrent异常）
        t1.addAll(cfs1);

        //将文件的所有子文件找出
        Set<ExamModuleModel> allSonst = new HashSet();//用SET不会产生重复项
        for (ExamModuleModel c1 : cfs1) {
            List<ExamModuleModel> sons = new ArrayList();
            this.moduleDAO.loadAllDescendants(c1.getId(), sons);
            allSonst.addAll(sons);
        }
        List<ExamModuleModel> allSons = new ArrayList(allSonst);

        //删除非顶级文件
        for (ExamModuleModel c : cfs1) {//对候选的每一个文件进行判断
            for (ExamModuleModel s : allSons) {
                if (c.getId().equals(s.getId())) {//若候选文件与其中一个后代文件是同一文件
                    ExamModuleModel t = null;
                    for (ExamModuleModel tt : t1) {//从t1中找出此候选文件
                        if (c.getId().equals(tt.getId())) {
                            t = tt;
                            break;
                        }
                    }
                    if (t != null) {
                        t1.remove(t);//将此候选文件删除
                    }
                }
            }
        }
        return t1;
    }

    public List<ExamModuleModel> buildStructure(List<ExamModuleModel> cfs1) {//建立所给文件列表的顶层目录结构
        List<ExamModuleModel> t1 = new ArrayList();//t1、cfs1都是所有的候选文件，其中如果有非顶级项将被删除，分设t1、cfs1是为了便于删除（否则会报concurrent异常）
        t1.addAll(cfs1);
        List<ExamModuleModel> t2 = new ArrayList();
        t2.addAll(cfs1);
        //删除非顶级文件
        for (ExamModuleModel c : cfs1) {//对候选的每一个文件进行判断
            for (ExamModuleModel s : t2) {
                if (testIfAncestor(c, s.getId())) {//若候选文件是另一个文件的后代
                    ExamModuleModel t = null;
                    for (ExamModuleModel tt : t1) {//从t1中找出此候选文件
                        if (c.getId().equals(tt.getId())) {
                            t = tt;
                            break;
                        }
                    }
                    if (t != null) {
                        t1.remove(t);//将此候选文件删除
                    }
                }
            }
        }
        return t1;
    }

    private boolean testIfAncestor1(ExamModuleModel c, ExamModuleModel target) {

        boolean t = false;
        for (ExamModuleModel cf : c.getFamilyTree()) {
            if (target.getId().equals(cf.getId())) {
                t = true;
                break;
            }
        }
        return t;
    }

    public boolean testIfAncestor(ExamModuleModel c, String target) {
        //MyLogger.echo(c.getAncestors()+":::"+target);
        String ancestors = c.getAncestors();
        ancestors = ancestors.replace(c.getId() + ";", "");
        if (ancestors.trim().indexOf(String.valueOf(target)) == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<ExamModuleModel> buildStructure(String fid, List<ExamModuleModel> cfs1) {//建立所给文件列表的顶层目录结构
        List<ExamModuleModel> t1 = new ArrayList();//t1、cfs1都是所有的候选文件，其中如果有非顶级项将被删除，分设t1、cfs1是为了便于删除（否则会报concurrent异常）
        t1.addAll(cfs1);
        List<ExamModuleModel> t2 = new ArrayList();
        t2.addAll(cfs1);
        //删除非顶级文件
        for (ExamModuleModel c : cfs1) {//对候选的每一个文件进行判断
            if (fid.equals(c.getFatherId())) {//直系子文件一定会列出
                continue;
            } else {
                for (ExamModuleModel s : t2) {
                    if (this.testIfAncestor(c, s.getId())) {//若候选文件c是另一个文件的后代
                        ExamModuleModel t = null;
                        for (ExamModuleModel tt : t1) {//从t1中找出此候选文件
                            if (c.getId().equals(tt.getId())) {
                                t = tt;
                                break;
                            }
                        }
                        if (t != null) {
                            //MyLogger.echo("文件" + t.getFileName() + "非当前目录顶级文件，被删除。");
                            t1.remove(t);//将此候选文件删除
                        }
                    }
                }
            }
        }
        return t1;
    }

    public String genComplexGrapaID(String fatherId, List<ExamModuleModel> files) {
        String fid = fatherId;
        StringBuffer qs = new StringBuffer();
        for (ExamModuleModel c : files) {
            qs.append(c.getId());
            qs.append(";");
        }
        while (CookieUtils.getBusinessId(JsfHelper.getRequest()).equals(fid)) {
            fid = this.moduleDAO.findExamModuleModel(fid).getFatherId();
            if (qs.indexOf(String.valueOf(fid)) != -1) {
                break;
            }
        }
        return fid;
    }

    /**
     * Generate the id string of its ancestors, including itself
     * @param id module id
     * @return 
     */
    public String genAncestors(String id) {
        List<ExamModuleModel> cfms = this.genFamilyTree(id);
        StringBuffer sb = new StringBuffer();
        for (ExamModuleModel c : cfms) {
            sb.append(c.getId());
            sb.append(';');
        }
        return sb.toString();
    }

    public List<ExamModuleModel> genFamilyTree(String id) {
        List<ExamModuleModel> cs = new ArrayList();
        if (!CookieUtils.getBusinessId(JsfHelper.getRequest()).equals(id)) {
            ExamModuleModel c = this.moduleDAO.findExamModuleModel(id);
            if (c != null) {
                cs.add(c);
                String tt = c.getFatherId();
                //循环找出祖先节点
                while (!CookieUtils.getBusinessId(JsfHelper.getRequest()).toString().equals(tt)) {
                    c = this.moduleDAO.findExamModuleModel(tt);
                    if (c != null) {
                        cs.add(c);
                        if (tt.equals(c.getFatherId())) {
                            break;
                        }
                        tt = c.getFatherId();
                    } else {
                        break;
                    }
                }

            }
        }
        ExamModuleModel top = new ExamModuleModel();
        top.setId(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        top.setName("根模块");
        cs.add(top);
        Collections.reverse(cs);

        return cs;
    }

    public List<ExamModuleModel> genComplexFamilyTree(String id, List<ExamModuleModel> files) {
        List<ExamModuleModel> tfs = new ArrayList();
        List<ExamModuleModel> cs = this.genFamilyTree(id);
        for (ExamModuleModel c : cs) {
            if (this.testIfContain(c, files) || c.getId().equals(CookieUtils.getBusinessId(JsfHelper.getRequest()))) {
                tfs.add(c);
            }
        }
        return tfs;
    }

    private boolean testIfContain(ExamModuleModel c, List<ExamModuleModel> ffs) {

        if (c == null) {
            return false;
        } else {
            boolean t1 = false;
            for (ExamModuleModel fc : ffs) {
                if (fc.getId().equals(c.getId())) {
                    t1 = true;
                    break;
                }
            }
            return t1;
        }
    }

    public static void main(String args[]) {

        IExamModule2DAO cf2DAO = SpringHelper.getSpringBean("ExamModule2DAO");
        ComplexExamModuleLogic complex2 = SpringHelper.getSpringBean("ComplexExamModuleLogic");
        List<ExamModuleModel> cfms2 = cf2DAO.findAllExamModuleModel(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        for (ExamModuleModel c : cfms2) {
            //if (c.getAncestors() == null || "".equals(c.getAncestors())) {
            c.setAncestors(complex2.genAncestors(c.getId()));
            cf2DAO.updateExamModuleModel(c);
            System.out.println(c.getId() + c.getName());
            //}
        }
    }

}
