/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aqua.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "DELIVERY_CHARGES", schema = "aqua")
public class DeliveryCharge {
     private long deliveryChargeId;
    private long sellerId;
    private long minDeliveryCharge;
    private long maxDeliveryCharge;
    private long maxValueForFreeDelivery;
    private long minDistForMinCharge;
    private long deliveryChargeForKm;
    
    

    @Id
    @Column(name = "DELIVERY_CHARGES_ID")
     public long getDeliveryChargeId() {
        return deliveryChargeId;
    }

    public void setDeliveryChargeId(long deliveryChargeId) {
        this.deliveryChargeId = deliveryChargeId;
    }

    @Column(name = "SELLER_ID")
    public long getSellerId() {
        return sellerId;
    }

   

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

   
    @Column(name = "MIN_DELIVERY_CHARGE")
    public long getMinDeliveryCharge() {
        return minDeliveryCharge;
    }

    public void setMinDeliveryCharge(long minDeliveryCharge) {
        this.minDeliveryCharge = minDeliveryCharge;
    }

    @Column(name = "MAX_DELIVERY_CHARGE")
    public long getMaxDeliveryCharge() {
        return maxDeliveryCharge;
    }

    public void setMaxDeliveryCharge(long maxDeliveryCharge) {
        this.maxDeliveryCharge = maxDeliveryCharge;
    }

    @Column(name = "MAX_VALUE_FOR_FREE_DELV")
    public long getMaxValueForFreeDelivery() {
        return maxValueForFreeDelivery;
    }

    public void setMaxValueForFreeDelivery(long maxValueForFreeDelivery) {
        this.maxValueForFreeDelivery = maxValueForFreeDelivery;
    }

    @Column(name = "MIN_DIST_FOR_MIN_CHARGE")
    public long getMinDistForMinCharge() {
        return minDistForMinCharge;
    }

    public void setMinDistForMinCharge(long minDistForMinCharge) {
        this.minDistForMinCharge = minDistForMinCharge;
    }

    @Column(name = "DELE_CHARGE_FOR_KM")
    public long getDeliveryChargeForKm() {
        return deliveryChargeForKm;
    }

    public void setDeliveryChargeForKm(long deliveryChargeForKm) {
        this.deliveryChargeForKm = deliveryChargeForKm;
    }
}
