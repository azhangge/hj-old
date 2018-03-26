package com.hjedu.examination.service.impl;

import java.io.Serializable;
import java.util.*;

import com.alipay.api.domain.BusinessBankAccountInfo;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.platform.dao.ComplexDepartmentLogic;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

public class ComplexDepartmentLogicImpl implements ComplexDepartmentLogic, Serializable {

	
	
    ExamDepartmentService moduleDAO;

    public ExamDepartmentService getModuleDAO() {
        return moduleDAO;
    }

    public void setModuleDAO(ExamDepartmentService moduleDAO) {
        this.moduleDAO = moduleDAO;
    }

    public List<DictionaryModel> buildQulifiedStructureByDic(String fid, List<DictionaryModel> files) {
        if ("0".equals(fid)) {
//            for (DictionaryModel c : files) {
//                c.setFamilyTree(this.moduleDAO.genDeptFamilyTree(c.getFatherID()));
//            }
            MyLogger.echo("模块" + fid + "符合条件的子模块数：" + files.size());
            return this.buildStructure(fid, files);
        } else {
            List<DictionaryModel> ts = new ArrayList();//fid所有子文件中符合条件的找出来
            for (DictionaryModel c : files) {
                if (testIfAncestor(c, fid)) {
                    ts.add(c);
                }
            }
//            for (DictionaryModel c : ts) {
//                c.setFamilyTree(this.moduleDAO.genDeptFamilyTree(c.getFatherID()));
//            }
            MyLogger.echo("模块" + fid + "符合条件的子模块数：" + ts.size());
            return this.buildStructure(fid, ts);
        }
    }

    public List<DictionaryModel> buildStructure1(List<DictionaryModel> cfs1) {//建立所给文件列表的顶层目录结构
        List<DictionaryModel> t1 = new ArrayList();//t1、cfs1都是所有的候选文件，其中如果有非顶级项将被删除，分设t1、cfs1是为了便于删除（否则会报concurrent异常）
        t1.addAll(cfs1);

        //将文件的所有子文件找出
        Set<DictionaryModel> allSonst = new HashSet();//用SET不会产生重复项
        for (DictionaryModel c1 : cfs1) {
            List<DictionaryModel> sons = new ArrayList();
            this.moduleDAO.loadAllDescendants(c1.getId(), sons);
            allSonst.addAll(sons);
        }
        List<DictionaryModel> allSons = new ArrayList(allSonst);

        //删除非顶级文件
        for (DictionaryModel c : cfs1) {//对候选的每一个文件进行判断
            for (DictionaryModel s : allSons) {
                if (c.getId().equals(s.getId())) {//若候选文件与其中一个后代文件是同一文件
                    DictionaryModel t = null;
                    for (DictionaryModel tt : t1) {//从t1中找出此候选文件
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

    public List<DictionaryModel> buildStructure(List<DictionaryModel> cfs1) {//建立所给文件列表的顶层目录结构
        List<DictionaryModel> t1 = new ArrayList();//t1、cfs1都是所有的候选文件，其中如果有非顶级项将被删除，分设t1、cfs1是为了便于删除（否则会报concurrent异常）
        t1.addAll(cfs1);
        List<DictionaryModel> t2 = new ArrayList();
        t2.addAll(cfs1);
        //删除非顶级文件
        for (DictionaryModel c : cfs1) {//对候选的每一个文件进行判断
            for (DictionaryModel s : t2) {
                if (testIfAncestor(c, s.getId())) {//若候选文件是另一个文件的后代
                    DictionaryModel t = null;
                    for (DictionaryModel tt : t1) {//从t1中找出此候选文件
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

    private boolean testIfAncestor1(DictionaryModel c, DictionaryModel target) {

        boolean t = false;
        for (DictionaryModel cf : c.getFamilyTree()) {
            if (target.getId().equals(cf.getId())) {
                t = true;
                break;
            }
        }
        return t;
    }

    public boolean testIfAncestor(DictionaryModel c, String target) {
        //MyLogger.echo(c.getAncestors()+":::"+target);
        String ancestors = c.getAncestors();
        ancestors = ancestors.replace(c.getId() + ";", "");
        if (ancestors.trim().indexOf(String.valueOf(target)) == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<DictionaryModel> buildStructure(String fid, List<DictionaryModel> cfs1) {//建立所给文件列表的顶层目录结构
        List<DictionaryModel> t1 = new ArrayList();//t1、cfs1都是所有的候选文件，其中如果有非顶级项将被删除，分设t1、cfs1是为了便于删除（否则会报concurrent异常）
        t1.addAll(cfs1);
        List<DictionaryModel> t2 = new ArrayList();
        t2.addAll(cfs1);
        //删除非顶级文件
        for (DictionaryModel c : cfs1) {//对候选的每一个文件进行判断
            if (fid.equals(c.getFatherId())) {//直系子文件一定会列出
                continue;
            } else {
                for (DictionaryModel s : t2) {
                    if (this.testIfAncestor(c, s.getId())) {//若候选文件c是另一个文件的后代
                        DictionaryModel t = null;
                        for (DictionaryModel tt : t1) {//从t1中找出此候选文件
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

    public String genComplexGrapaID(String fatherId, List<DictionaryModel> files) {
        String fid = fatherId;
        StringBuffer qs = new StringBuffer();
        for (DictionaryModel c : files) {
            qs.append(c.getId());
            qs.append(";");
        }
        String bussinessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        while (!bussinessId.equals(fid)) {
            fid = this.moduleDAO.findDictionaryModel(fid,bussinessId).getFatherId();
            if (qs.indexOf(String.valueOf(fid)) != -1) {
                break;
            }
        }
        return fid;
    }

    public String genAncestors(String id) {
        List<DictionaryModel> cfms = this.genFamilyTree(id, true, CookieUtils.getBusinessId(JsfHelper.getRequest()));
        StringBuffer sb = new StringBuffer();
        for (DictionaryModel c : cfms) {
            sb.append(c.getId());
            sb.append(';');
        }
        return sb.toString();
    }

    /**
     * 返回一个包含本节点所有祖先节点的列表
     *
     * @param id 本级ID
     * @param ifContainRoot 返回结果是否包括顶级部门
     * @return
     */
    @Override
    public List<DictionaryModel> genFamilyTree(String id, boolean ifContainRoot,String bussinessId) {
        List<DictionaryModel> cs = new ArrayList();
    	
        if (!bussinessId.equals(id)) {
            DictionaryModel c = this.moduleDAO.findDictionaryModel(id,bussinessId);
            if (c != null) {
                cs.add(c);//将本级部门加入
                String tt = c.getFatherId();
                while (!bussinessId.equals(tt)) {
                    c = this.moduleDAO.findDictionaryModel(tt,bussinessId);//将上级部门加入
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
        if (ifContainRoot) {
            DictionaryModel top = new DictionaryModel();
            top.setId(bussinessId);
            top.setName("根部门");
            cs.add(top);
        }
        Collections.reverse(cs);
        return cs;
    }

    /**
     *
     * @param ids
     * @param ifContainRoot
     * @return
     */
    @Override
    public String genFamilyTreeCnStrByIds(String ids, boolean ifContainRoot,String businessId) {
        StringBuilder sb = new StringBuilder();
        if (ids != null) {
            String[] idss = ids.split(";");
            if (idss != null) {
                int c = 0;
                for (String id : idss) {
                    if (id != null) {
                        sb.append(this.genFamilyTreeCnStr(id, ifContainRoot,businessId));
                        sb.append("; ");
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * 返回部门字符串列表结构
     *
     * @param id
     * @param ifContainRoot
     * @return
     */
    @Override
    public String genFamilyTreeCnStr(String id, boolean ifContainRoot,String businessId) {
        List<DictionaryModel> dms = this.genFamilyTree(id, ifContainRoot,businessId);
        StringBuilder sb = new StringBuilder();
        int c = 0;
        for (DictionaryModel dm : dms) {
            c++;
            sb.append(dm.getName());
            if (c != dms.size()) {
                sb.append(" -> ");
            }
        }
        //System.out.println(id+":"+sb.toString());
        return sb.toString();
    }

    @Override
    public List<DictionaryModel> genComplexFamilyTree(String id, List<DictionaryModel> files) {
    	String bussinessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        List<DictionaryModel> tfs = new ArrayList();
        List<DictionaryModel> cs = this.genFamilyTree(id, true, bussinessId);
        for (DictionaryModel c : cs) {
            if (this.testIfContain(c, files) || c.getId().equals(bussinessId)) {
                tfs.add(c);
            }
        }
        return tfs;
    }

    private boolean testIfContain(DictionaryModel c, List<DictionaryModel> ffs) {

        if (c == null) {
            return false;
        } else {
            boolean t1 = false;
            for (DictionaryModel fc : ffs) {
                if (fc.getId().equals(c.getId())) {
                    t1 = true;
                    break;
                }
            }
            return t1;
        }
    }

    public static void main(String args[]) {
        ExamDepartmentService eds = SpringHelper.getSpringBean("ExamDepartmentService");
        IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");
        String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        ComplexDepartmentLogic cdl = SpringHelper.getSpringBean("ComplexDepartmentLogic");
        List<BbsUser> us = userDAO.findAllBbsUser(businessId);
        
        
        for (BbsUser bu : us) {
            String name = bu.getName();
            String groupStr=bu.getGroupStr();
            String cn = cdl.genFamilyTreeCnStrByIds(groupStr, false,businessId);
            System.out.println(name + " : "+groupStr+" : " + cn);
        }

    }

}
