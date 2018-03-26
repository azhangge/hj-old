package com.hjedu.platform.service.impl;

import java.io.Serializable;
import java.util.*;

import com.hjedu.customer.dao.IBbsFileDAO;
import com.hjedu.platform.dao.ComplexFileLogic;
import com.hjedu.platform.entity.BbsFileModel;
import com.huajie.util.MyLogger;


public class ComplexFileLogicImpl implements ComplexFileLogic,Serializable{

    IBbsFileDAO clientFileDAO;

    public IBbsFileDAO getClientFileDAO() {
        return clientFileDAO;
    }

    public void setClientFileDAO(IBbsFileDAO clientFileDAO) {
        this.clientFileDAO = clientFileDAO;
    }

    
    
    public List<BbsFileModel> buildQulifiedStructureByDic(String fid, List<BbsFileModel> files) {
        if ("0".equals(fid)) {
//            for (BbsFileModel c : files) {
//                c.setFamilyTree(this.clientFileDAO.genDeptFamilyTree(c.getFatherID()));
//            }
            MyLogger.echo("目录" + fid + "符合条件的子文件数：" + files.size());
            return this.buildStructure(fid, files);
        } else {
            List<BbsFileModel> ts = new ArrayList();//fid所有子文件中符合条件的找出来
            for (BbsFileModel c : files) {
                if (testIfAncestor(c, fid)) {
                    ts.add(c);
                }
            }
//            for (BbsFileModel c : ts) {
//                c.setFamilyTree(this.clientFileDAO.genDeptFamilyTree(c.getFatherID()));
//            }
            MyLogger.echo("目录" + fid + "符合条件的子文件数：" + ts.size());
            return this.buildStructure(fid, ts);
        }
    }

    public List<BbsFileModel> buildStructure1(List<BbsFileModel> cfs1) {//建立所给文件列表的顶层目录结构
        List<BbsFileModel> t1 = new ArrayList();//t1、cfs1都是所有的候选文件，其中如果有非顶级项将被删除，分设t1、cfs1是为了便于删除（否则会报concurrent异常）
        t1.addAll(cfs1);

        //将文件的所有子文件找出
        Set<BbsFileModel> allSonst = new HashSet();//用SET不会产生重复项
        for (BbsFileModel c1 : cfs1) {
            List<BbsFileModel> sons = new ArrayList();
            this.clientFileDAO.loadAllDescendants(c1.getId(), sons);
            allSonst.addAll(sons);
        }
        List<BbsFileModel> allSons = new ArrayList(allSonst);

        //删除非顶级文件
        for (BbsFileModel c : cfs1) {//对候选的每一个文件进行判断
            for (BbsFileModel s : allSons) {
                if (c.getId().equals(s.getId())) {//若候选文件与其中一个后代文件是同一文件
                    BbsFileModel t = null;
                    for (BbsFileModel tt : t1) {//从t1中找出此候选文件
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

    public List<BbsFileModel> buildStructure(List<BbsFileModel> cfs1) {//建立所给文件列表的顶层目录结构
        List<BbsFileModel> t1 = new ArrayList();//t1、cfs1都是所有的候选文件，其中如果有非顶级项将被删除，分设t1、cfs1是为了便于删除（否则会报concurrent异常）
        t1.addAll(cfs1);
        List<BbsFileModel> t2 = new ArrayList();
        t2.addAll(cfs1);
        //删除非顶级文件
        for (BbsFileModel c : cfs1) {//对候选的每一个文件进行判断
            for (BbsFileModel s : t2) {
                if (testIfAncestor(c, s.getId())) {//若候选文件是另一个文件的后代
                    BbsFileModel t = null;
                    for (BbsFileModel tt : t1) {//从t1中找出此候选文件
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

    private boolean testIfAncestor1(BbsFileModel c, BbsFileModel target) {
        if (!target.getIfFolder()) {
            return false;
        }
        boolean t = false;
        for (BbsFileModel cf : c.getFamilyTree()) {
            if (target.getId().equals(cf.getId())) {
                t = true;
                break;
            }
        }
        return t;
    }

    public boolean testIfAncestor(BbsFileModel c, String target) {
        //MyLogger.echo(c.getAncestors()+":::"+target);
        String ancestors = c.getAncestors();
        ancestors = ancestors.replace(c.getId() + ";", "");
        if (ancestors.trim().indexOf(String.valueOf(target)) == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<BbsFileModel> buildStructure(String fid, List<BbsFileModel> cfs1) {//建立所给文件列表的顶层目录结构
        List<BbsFileModel> t1 = new ArrayList();//t1、cfs1都是所有的候选文件，其中如果有非顶级项将被删除，分设t1、cfs1是为了便于删除（否则会报concurrent异常）
        t1.addAll(cfs1);
        List<BbsFileModel> t2 = new ArrayList();
        t2.addAll(cfs1);
        //删除非顶级文件
        for (BbsFileModel c : cfs1) {//对候选的每一个文件进行判断
            if (fid.equals(c.getFatherID())) {//直系子文件一定会列出
                continue;
            } else {
                for (BbsFileModel s : t2) {
                    if (this.testIfAncestor(c, s.getId())) {//若候选文件c是另一个文件的后代
                        BbsFileModel t = null;
                        for (BbsFileModel tt : t1) {//从t1中找出此候选文件
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

    @Override
    public String genComplexGrapaID(String fatherId, List<BbsFileModel> files) {
        String fid = fatherId;
        StringBuffer qs = new StringBuffer();
        for (BbsFileModel c : files) {
            qs.append(c.getId());
            qs.append(";");
        }
        while (!fid.equals("0")) {
            fid = this.clientFileDAO.findClientFile(fid).getFatherID();
            if (qs.indexOf(String.valueOf(fid)) != -1) {
                break;
            }
        }
        return fid;
    }
    
    
    
    public String genAncestors(String id) {
        List<BbsFileModel> cfms = this.genFamilyTree(id);
        StringBuffer sb = new StringBuffer();
        for (BbsFileModel c : cfms) {
            sb.append(c.getId());
            sb.append(';');
        }
        return sb.toString();
    }
    
    
    public List<BbsFileModel> genFamilyTree(String id) {
        List<BbsFileModel> cs = new ArrayList();
        if (!id.equals("0")) {
            BbsFileModel c = this.clientFileDAO.findClientFile(id);
            if (c != null) {
                cs.add(c);
                String tt = c.getFatherID();
                while (!"0".equals(tt)) {
                    c = this.clientFileDAO.findClientFile(tt);
                    if (c != null) {
                        cs.add(c);
                        if (tt.equals(c.getFatherID())) {
                            break;
                        }
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
    
    public List<BbsFileModel> genComplexFamilyTree(String id, List<BbsFileModel> files) {
        List<BbsFileModel> tfs = new ArrayList();
        List<BbsFileModel> cs = this.genFamilyTree(id);
        for (BbsFileModel c : cs) {
            if (this.testIfContain(c, files)||c.getId().equals("0")) {
                tfs.add(c);
            }
        }
        return tfs;
    }
    
    
    
    private boolean testIfContain(BbsFileModel c, List<BbsFileModel> ffs) {
        
        if (c == null) {
            return false;
        } else {
            boolean t1 = false;
            for (BbsFileModel fc : ffs) {
                if (fc.getId().equals(c.getId())) {
                    t1 = true;
                    break;
                }
            }
            return t1;
        }
    }
    
}
