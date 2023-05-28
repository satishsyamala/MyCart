/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cart.pojo;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "ORDERS", schema = "mycart")
public class Orders {

    private long orderId;
    private String orderNo;
    private String transactionNo;
    private long userId;
    private String name;
    private String image;
    private String catOrderBy;
    private Date orderDate;
    private long delveryAddressId;
    private String paymentType;
    private Date deliveryDate;
    private double price;
    private double discount;
    private double tax;
    private double finalPrice;
    private String status;
    private long sellerId;
    private long deliveryBy;
    private Date actDeliveryDate;
    private String address;
    private String trackStatus;
    private long lastTrackId;
    private Date lastTrackDate;
    private String deliveryType;
    private int otp;
    private long deliveryCharge;

    @Id
    @Column(name = "ORDER_ID")
    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    @Column(name = "ORDER_NO")
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Column(name = "TRANSACTION_NO")
    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    @Column(name = "USER_ID")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "IMAGE")
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Column(name = "CAT_ORDER_BY")
    public String getCatOrderBy() {
        return catOrderBy;
    }

    public void setCatOrderBy(String catOrderBy) {
        this.catOrderBy = catOrderBy;
    }

    @Column(name = "ORDER_DATE")
    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @Column(name = "DELIVERY_ADD_ID")
    public long getDelveryAddressId() {
        return delveryAddressId;
    }

    public void setDelveryAddressId(long delveryAddressId) {
        this.delveryAddressId = delveryAddressId;
    }

    @Column(name = "PAYMENT_TYPE")
    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    @Column(name = "DELIVERY_DATE")
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @Column(name = "PRICE")
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Column(name = "DISCOUNT")
    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Column(name = "TAX")
    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    @Column(name = "FINAL_PRICE")
    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "SELLER_ID")
    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

    @Column(name = "DELIVERY_BY")
    public long getDeliveryBy() {
        return deliveryBy;
    }

    public void setDeliveryBy(long deliveryBy) {
        this.deliveryBy = deliveryBy;
    }

    @Column(name = "ACT_DELIVERY_DATE")
    public Date getActDeliveryDate() {
        return actDeliveryDate;
    }

    public void setActDeliveryDate(Date actDeliveryDate) {
        this.actDeliveryDate = actDeliveryDate;
    }

    @Column(name = "ADDRESS")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "TRACK_STATUS")
    public String getTrackStatus() {
        return trackStatus;
    }

    public void setTrackStatus(String trackStatus) {
        this.trackStatus = trackStatus;
    }

    @Column(name = "LAST_TRACK_ID")
    public long getLastTrackId() {
        return lastTrackId;
    }

    public void setLastTrackId(long lastTrackId) {
        this.lastTrackId = lastTrackId;
    }

    @Column(name = "LAST_TRACK_DATE")
    public Date getLastTrackDate() {
        return lastTrackDate;
    }

    public void setLastTrackDate(Date lastTrackDate) {
        this.lastTrackDate = lastTrackDate;
    }

    @Column(name = "DELIVERY_TYPE")
    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

     @Column(name = "OTP")
    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

   
    @Column(name = "DELIVERY_CHARGE")
	public long getDeliveryCharge() {
		return deliveryCharge;
	}

	public void setDeliveryCharge(long deliveryCharge) {
		this.deliveryCharge = deliveryCharge;
	}
    
    

}
