
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

import com.hjedu.customer.entity.BbsUser;
/**
 * 
 * 销售记录
 * 销售模块
 *
 */
@Entity
@Table(name = "y_sale_order")
public class SaleOrder  implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();
    
    @Column(name = "oid")
    private String oid;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "body",length = 3600)
    private String body;
    
    @Column(name = "description1")
    private String description1;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private BbsUser user;
    
    @Column(name = "status")
    private String status="created";//订单状态:created,paid,processed
    
    @Column(name = "finance_status")
    private String financeStatus="undo";//订单状态:undo,processed
    
    @Column(name = "order_type")
    private String orderType="goods";//订单类型:goods,cash
    
    @Column(name = "ali_trade_no")
    private String aliTradeNo;
    
    @Column(name = "buyer_email")
    private String buyerEmail;
    
    @Column(name = "if_send_goods")
    private boolean ifSendGoods=false;
    
    @Column(name = "genTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genTime=new Date();
    
    @Column(name = "ord")
    private int ord = 0;

    @Noncacheable
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "order", fetch = FetchType.LAZY,orphanRemoval = true)
    List<SaleOrderItem> items;
    
    @Column(name = "total_money")
    private double totalMoney=0;//订单总金额
    
    @Column(name = "pay_money")
    private double payMoney=0;//需要在线支付的金额
    
    @Transient
    private boolean selected=false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getAliTradeNo() {
        return aliTradeNo;
    }

    public void setAliTradeNo(String aliTradeNo) {
        this.aliTradeNo = aliTradeNo;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
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

    public double getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(double payMoney) {
        this.payMoney = payMoney;
    }

    public String getFinanceStatus() {
        return financeStatus;
    }

    public void setFinanceStatus(String financeStatus) {
        this.financeStatus = financeStatus;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public List<SaleOrderItem> getItems() {
        return items;
    }

    public void setItems(List<SaleOrderItem> items) {
        this.items = items;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BbsUser getUser() {
        return user;
    }

    public void setUser(BbsUser user) {
        this.user = user;
    }

    public boolean isIfSendGoods() {
        return ifSendGoods;
    }

    public void setIfSendGoods(boolean ifSendGoods) {
        this.ifSendGoods = ifSendGoods;
    }

    
    
    
}
