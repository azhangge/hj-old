package com.hjedu.platform.service.impl;

import java.io.Serializable;
import java.util.*;

import com.hjedu.platform.dao.ComplexGoodsModuleLogic;
import com.huajie.seller.dao.IGoodsModule2DAO;
import com.huajie.seller.model.GoodsModuleModel;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;


public class ComplexGoodsModuleLogicImpl implements ComplexGoodsModuleLogic,Serializable{

    IGoodsModule2DAO moduleDAO;

    public IGoodsModule2DAO getModuleDAO() {
        return moduleDAO;
    }

    public void setModuleDAO(IGoodsModule2DAO moduleDAO) {
        this.moduleDAO = moduleDAO;
    }

    

    
    
    public List<GoodsModuleModel> buildQulifiedStructureByDic(String fid, List<GoodsModuleModel> files) {
        if (SpringHelper.getSpringBean("goods_module_root_id").toString().equals(fid)) {
//            for (GoodsModuleModel c : files) {
//                c.setFamilyTree(this.moduleDAO.genDeptFamilyTree(c.getFatherID()));
//            }
            MyLogger.echo("模块" + fid + "符合条件的子模块数：" + files.size());
            return this.buildStructure(fid, files);
        } else {
            List<GoodsModuleModel> ts = new ArrayList();//fid所有子文件中符合条件的找出来
            for (GoodsModuleModel c : files) {
                if (testIfAncestor(c, fid)) {
                    ts.add(c);
                }
            }
//            for (GoodsModuleModel c : ts) {
//                c.setFamilyTree(this.moduleDAO.genDeptFamilyTree(c.getFatherID()));
//            }
            MyLogger.echo("模块" + fid + "符合条件的子模块数：" + ts.size());
            return this.buildStructure(fid, ts);
        }
    }

    public List<GoodsModuleModel> buildStructure1(List<GoodsModuleModel> cfs1) {//建立所给文件列表的顶层目录结构
        List<GoodsModuleModel> t1 = new ArrayList();//t1、cfs1都是所有的候选文件，其中如果有非顶级项将被删除，分设t1、cfs1是为了便于删除（否则会报concurrent异常）
        t1.addAll(cfs1);

        //将文件的所有子文件找出
        Set<GoodsModuleModel> allSonst = new HashSet();//用SET不会产生重复项
        for (GoodsModuleModel c1 : cfs1) {
            List<GoodsModuleModel> sons = new ArrayList();
            this.moduleDAO.loadAllDescendants(c1.getId(), sons);
            allSonst.addAll(sons);
        }
        List<GoodsModuleModel> allSons = new ArrayList(allSonst);

        //删除非顶级文件
        for (GoodsModuleModel c : cfs1) {//对候选的每一个文件进行判断
            for (GoodsModuleModel s : allSons) {
                if (c.getId()==s.getId()) {//若候选文件与其中一个后代文件是同一文件
                    GoodsModuleModel t = null;
                    for (GoodsModuleModel tt : t1) {//从t1中找出此候选文件
                        if (c.getId()==tt.getId()) {
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

    public List<GoodsModuleModel> buildStructure(List<GoodsModuleModel> cfs1) {//建立所给文件列表的顶层目录结构
        List<GoodsModuleModel> t1 = new ArrayList();//t1、cfs1都是所有的候选文件，其中如果有非顶级项将被删除，分设t1、cfs1是为了便于删除（否则会报concurrent异常）
        t1.addAll(cfs1);
        List<GoodsModuleModel> t2 = new ArrayList();
        t2.addAll(cfs1);
        //删除非顶级文件
        for (GoodsModuleModel c : cfs1) {//对候选的每一个文件进行判断
            for (GoodsModuleModel s : t2) {
                if (testIfAncestor(c, s.getId())) {//若候选文件是另一个文件的后代
                    GoodsModuleModel t = null;
                    for (GoodsModuleModel tt : t1) {//从t1中找出此候选文件
                        if (c.getId()==tt.getId()) {
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

    private boolean testIfAncestor1(GoodsModuleModel c, GoodsModuleModel target) {
        
        boolean t = false;
        for (GoodsModuleModel cf : c.getFamilyTree()) {
            if (target.getId()==cf.getId()) {
                t = true;
                break;
            }
        }
        return t;
    }

    public boolean testIfAncestor(GoodsModuleModel c, String target) {
        //MyLogger.echo(c.getAncestors()+":::"+target);
        String ancestors = c.getAncestors();
        ancestors = ancestors.replace(c.getId() + ";", "");
        if (ancestors.trim().indexOf(String.valueOf(target)) == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<GoodsModuleModel> buildStructure(String fid, List<GoodsModuleModel> cfs1) {//建立所给文件列表的顶层目录结构
        List<GoodsModuleModel> t1 = new ArrayList();//t1、cfs1都是所有的候选文件，其中如果有非顶级项将被删除，分设t1、cfs1是为了便于删除（否则会报concurrent异常）
        t1.addAll(cfs1);
        List<GoodsModuleModel> t2 = new ArrayList();
        t2.addAll(cfs1);
        //删除非顶级文件
        for (GoodsModuleModel c : cfs1) {//对候选的每一个文件进行判断
            if (fid==c.getFatherId()) {//直系子文件一定会列出
                continue;
            } else {
                for (GoodsModuleModel s : t2) {
                    if (this.testIfAncestor(c, s.getId())) {//若候选文件c是另一个文件的后代
                        GoodsModuleModel t = null;
                        for (GoodsModuleModel tt : t1) {//从t1中找出此候选文件
                            if (c.getId()==tt.getId()) {
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

    
    public String genComplexGrapaID(String fatherId, List<GoodsModuleModel> files) {
        String fid = fatherId;
        StringBuffer qs = new StringBuffer();
        for (GoodsModuleModel c : files) {
            qs.append(c.getId());
            qs.append(";");
        }
        while (!"0".equals(fid)) {
            fid = this.moduleDAO.findGoodsModuleModel(fid).getFatherId();
            if (qs.indexOf(String.valueOf(fid)) != -1) {
                break;
            }
        }
        return fid;
    }
    
    
    
    public String genAncestors(String id) {
        List<GoodsModuleModel> cfms = this.genFamilyTree(id);
        StringBuffer sb = new StringBuffer();
        for (GoodsModuleModel c : cfms) {
            sb.append(c.getId());
            sb.append(';');
        }
        return sb.toString();
    }
    
    
    public List<GoodsModuleModel> genFamilyTree(String id) {
        List<GoodsModuleModel> cs = new ArrayList();
        if (!SpringHelper.getSpringBean("goods_module_root_id").toString().equals(id)) {
            GoodsModuleModel c = this.moduleDAO.findGoodsModuleModel(id);
            if (c != null) {
                cs.add(c);
                String tt = c.getFatherId();
                while (!SpringHelper.getSpringBean("goods_module_root_id").toString().equals(tt)) {
                    c = this.moduleDAO.findGoodsModuleModel(tt);
                    if (c != null) {
                        cs.add(c);
                        tt = c.getFatherId();
                    } else {
                        break;
                    }
                }
                
            }
        }
        GoodsModuleModel top = new GoodsModuleModel();
        top.setId(SpringHelper.getSpringBean("goods_module_root_id").toString());
        top.setName("根模块");
        cs.add(top);
        Collections.reverse(cs);
        
        return cs;
    }
    
    public List<GoodsModuleModel> genComplexFamilyTree(String id, List<GoodsModuleModel> files) {
        List<GoodsModuleModel> tfs = new ArrayList();
        List<GoodsModuleModel> cs = this.genFamilyTree(id);
        for (GoodsModuleModel c : cs) {
            if (this.testIfContain(c, files)||c.getId().equals(SpringHelper.getSpringBean("goods_module_root_id").toString())) {
                tfs.add(c);
            }
        }
        return tfs;
    }
    
    
    
    private boolean testIfContain(GoodsModuleModel c, List<GoodsModuleModel> ffs) {
        
        if (c == null) {
            return false;
        } else {
            boolean t1 = false;
            for (GoodsModuleModel fc : ffs) {
                if (fc.getId()==c.getId()) {
                    t1 = true;
                    break;
                }
            }
            return t1;
        }
    }
    
}
