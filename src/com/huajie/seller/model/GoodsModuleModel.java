package com.huajie.seller.model;


import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.eclipse.persistence.annotations.Noncacheable;

import com.hjedu.customer.entity.BbsUser;
import com.huajie.seller.dao.IGoodsModule2DAO;
import com.huajie.util.SpringHelper;

/**
 * 
 * 商品模型
 * 销售模块
 *
 */

@Entity
@Table(name = "y_goods_module")
public class GoodsModuleModel implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();
    @Column(name = "name")
    private String name;
    @Column(name = "father_id")
    private String fatherId;
    @Column(name = "ord")
    private int ord;
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genTime = new Date();
    @Column(name = "ancestors",length = 900)
    private String ancestors="";
    
    @OneToMany(cascade = {CascadeType.REMOVE}, mappedBy = "module", fetch = FetchType.LAZY)
    @Noncacheable
    List<SaleGoods> goods;
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "exam_admin_module",
            joinColumns = {
        @JoinColumn(name = "module_id", referencedColumnName = "id")},
            inverseJoinColumns = {
        @JoinColumn(name = "admin_id", referencedColumnName = "id")})
    private List<BbsUser> users;
    
    @Column(name = "front_show")
    private Boolean frontShow=true;
    
    @Column(name = "group_str",length = 3600)
    private String groupStr = "";
    
    @Transient
    List<GoodsModuleModel> familyTree;
    
    @Transient
    long totalNum=0;
    @Transient
    private boolean selected = false;
    @Transient
    private List<GoodsModuleModel> sons;
    @Transient
    private int deep;

    public GoodsModuleModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }

    public List<GoodsModuleModel> getSons() {
        return sons;
    }

    public void setSons(List<GoodsModuleModel> sons) {
        this.sons = sons;
    }

    public int getDeep() {
        IGoodsModule2DAO dicDAO = SpringHelper.getSpringBean("GoodsModule2DAO");
        GoodsModuleModel dm = this;
        int i = 1;
        String ttt = SpringHelper.getSpringBean("goods_module_root_id");
        while (!ttt.equals(dm.getFatherId())) {
            dm = dicDAO.findGoodsModuleModel(dm.getFatherId());
            i++;
        }
        this.deep = i;
        return deep;
    }

    public void setDeep(int deep) {
        this.deep = deep;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<SaleGoods> getGoods() {
        return goods;
    }

    public void setGoods(List<SaleGoods> goods) {
        this.goods = goods;
    }

    public List<BbsUser> getUsers() {
        return users;
    }

    public void setUsers(List<BbsUser> users) {
        this.users = users;
    }

    public long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(long totalNum) {
        this.totalNum = totalNum;
    }

    

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    public Boolean getFrontShow() {
        return frontShow;
    }

    public void setFrontShow(Boolean frontShow) {
        this.frontShow = frontShow;
    }


    public List<GoodsModuleModel> getFamilyTree() {
        return familyTree;
    }

    public void setFamilyTree(List<GoodsModuleModel> familyTree) {
        this.familyTree = familyTree;
    }

    public String getGroupStr() {
        return groupStr;
    }

    public void setGroupStr(String groupStr) {
        this.groupStr = groupStr;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GoodsModuleModel)) {
            return false;
        }
        GoodsModuleModel other = (GoodsModuleModel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        GoodsModuleModel oo = (GoodsModuleModel) o;
        return this.ord - oo.getOrd();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "com.huajie.model.GoodsModuleModel[ id=" + id + " ]";
    }
}
