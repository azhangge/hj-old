package com.hjedu.platform.service.impl;

import java.io.Serializable;
import java.util.List;

import com.hjedu.platform.dao.IFireWallDAO;
import com.hjedu.platform.dao.IIpRuleDAO;
import com.hjedu.platform.entity.FireWall;
import com.hjedu.platform.entity.IpRule;
import com.huajie.cache.IRereCacheInstance;
import com.huajie.cache.RereCacheInstanceReplicated;
import com.huajie.cache.RereCacheManager;

/**
 * 本类为内置防火墙提供一种缓存机制，使对防火强规则的访问不必每次都访问数据库
 *
 * @author Administrator
 */
public class FireWallService implements Serializable {

    IFireWallDAO wallDAO;
    IIpRuleDAO ipRuleDAO;

    private static IRereCacheInstance ins = null;

    public IRereCacheInstance getInstance() {
        if (ins == null) {
            ins = RereCacheManager.getReplicatedInstance("firewall");
            RereCacheInstanceReplicated ci = (RereCacheInstanceReplicated) ins;
            FireWall wall = this.wallDAO.findFireWall();
            List<IpRule> rs1 = this.ipRuleDAO.findAllBlackIpRule();
            List<IpRule> rs2 = this.ipRuleDAO.findAllWhiteIpRule();
            ci.addWithoutMsg("wall", wall);
            ci.addWithoutMsg("blackrules", rs1);
            ci.addWithoutMsg("whiterules", rs2);
            ci.setLifeLen(0);//永不过期
        }
        return ins;
    }

    public IFireWallDAO getWallDAO() {
        return wallDAO;
    }

    public void setWallDAO(IFireWallDAO wallDAO) {
        this.wallDAO = wallDAO;
    }

    public IIpRuleDAO getIpRuleDAO() {
        return ipRuleDAO;
    }

    public void setIpRuleDAO(IIpRuleDAO ipRuleDAO) {
        this.ipRuleDAO = ipRuleDAO;
    }

    public void reBuildCache() {
        IRereCacheInstance ci = getInstance();
        ci.destroy();
    }


    public List<IpRule> findAllBlackIpRule() {
        IRereCacheInstance ci = getInstance();//exam为实例名
        List<IpRule> rs = (List) ci.fetchObject("blackrules");//exam为条目名
        if (rs == null) {
            rs = this.ipRuleDAO.findAllBlackIpRule();
            ci.add("blackrules", rs);
        }
        return rs;
    }

    public List<IpRule> findAllWhiteIpRule() {
        IRereCacheInstance ci =getInstance();//exam为实例名
        List<IpRule> rs = (List) ci.fetchObject("whiterules");//exam为条目名
        if (rs == null) {
            rs = this.ipRuleDAO.findAllBlackIpRule();
            ci.add("whiterules", rs);
        }
        return rs;
    }

    public FireWall findFireWall() {
        IRereCacheInstance ci = getInstance();//exam为实例名
        FireWall wall = (FireWall) ci.fetchObject("wall");//exam为条目名
        if (wall == null) {
            wall = this.wallDAO.findFireWall();
            ci.add("wall", wall);
        }
        return wall;
    }

    public void updateWall(FireWall wall) {
        this.wallDAO.updateFireWall(wall);
        this.reBuildCache();
    }

    public void updateIpRule(IpRule rule) {
        this.ipRuleDAO.updateIpRule(rule);
        this.reBuildCache();
    }

    public void addIpRule(IpRule rule) {
        this.ipRuleDAO.addIpRule(rule);
        this.reBuildCache();
    }

    public void deleteIpRule(String id) {
        this.ipRuleDAO.deleteIpRule(id);
        this.reBuildCache();
    }

}
