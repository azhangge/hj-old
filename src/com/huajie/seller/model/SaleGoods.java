
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.eclipse.persistence.annotations.Noncacheable;
/**
 * 
 * 在售商品
 * 销售模块
 *
 */
@Entity
@Table(name = "y_sale_goods")
public class SaleGoods implements Serializable,Comparable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();
    
    @Column(name = "gid")
    private String gid;//商品编号
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "sale_code")
    private String saleCode;//条码
    
    @Column(name = "price")
    private double price = 0d;
    
    @Column(name = "discount")
    private double discount = 1d;
    
    @Column(name = "unit_name")
    private String unitName = "";//单位名称，个|套|次...
    
    @Column(name = "hash_code")
    private String hashCode;
    
    @Column(name = "description1")
    private String description1;
    
    @Column(name = "genTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genTime=new Date();
    
    @Column(name = "ord")
    private int ord = 0;
    
    @ManyToOne
    @JoinColumn(name = "module_id")
    GoodsModuleModel module;
    
    
    @OneToMany(cascade = {CascadeType.REMOVE}, mappedBy = "goods", fetch = FetchType.LAZY)
    @Noncacheable
    List<SaleOrderItem> orderItems;
    
    @Transient
    private boolean selected=false;
    
    @Transient
    private double quantity=1d;

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

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public List<SaleOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<SaleOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getSaleCode() {
        return saleCode;
    }

    public void setSaleCode(String saleCode) {
        this.saleCode = saleCode;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getDescription1() {
        return description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public GoodsModuleModel getModule() {
        return module;
    }

    public void setModule(GoodsModuleModel module) {
        this.module = module;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
    
    @Override
    public int compareTo(Object o) {
        SaleGoods cq = (SaleGoods) o;
            return this.getOrd() - cq.getOrd();
    }
    
    
    
    
}
