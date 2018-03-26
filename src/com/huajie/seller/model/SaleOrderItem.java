
package com.huajie.seller.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/**
 * 
 * 订单记录
 * 销售模块
 *
 */
@Entity
@Table(name = "y_sale_order_item")
public class SaleOrderItem  implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "genTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genTime=new Date();
    
    @ManyToOne
    @JoinColumn(name = "order_id")
    private SaleOrder order;
    
    @ManyToOne
    @JoinColumn(name = "goods_id")
    private SaleGoods goods;
    
    @Column(name = "quantity")
    private double quantity=0d;
    
    @Column(name = "discount")
    private double discount=1d;
    
    @Column(name = "real_price")
    private double realPrice=0d;
    
    @Column(name = "ord")
    private int ord=0;
    

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

    public SaleOrder getOrder() {
        return order;
    }

    public void setOrder(SaleOrder order) {
        this.order = order;
    }

    public SaleGoods getGoods() {
        return goods;
    }

    public void setGoods(SaleGoods goods) {
        this.goods = goods;
    }

    public double getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(double realPrice) {
        this.realPrice = realPrice;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }
    
    
    
    
}
