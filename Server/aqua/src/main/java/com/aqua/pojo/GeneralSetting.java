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
@Table(name = "GEN_SETTINGS", schema = "aqua")
public class GeneralSetting {

    private long generalSettingId;
    private long sellerId;
    private int distanceKm;
    private long minOrderValue;
    private String paymentModes;
    private int minDeliveryDays;
  

    @Id
    @Column(name = "GEN_SETTINGS_ID")
    public long getGeneralSettingId() {
        return generalSettingId;
    }

    public void setGeneralSettingId(long generalSettingId) {
        this.generalSettingId = generalSettingId;
    }

    @Column(name = "SELLER_ID")
    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

    @Column(name = "DISTANCE_KM")
    public int getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(int distanceKm) {
        this.distanceKm = distanceKm;
    }

    @Column(name = "MIN_ORDER_VALUE")
    public long getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(long minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    @Column(name = "PAYMENT_MODES")
    public String getPaymentModes() {
        return paymentModes;
    }

    public void setPaymentModes(String paymentModes) {
        this.paymentModes = paymentModes;
    }

    @Column(name = "MIN_DELIVERY_DAYS")
    public int getMinDeliveryDays() {
        return minDeliveryDays;
    }

    public void setMinDeliveryDays(int minDeliveryDays) {
        this.minDeliveryDays = minDeliveryDays;
    }

   

}
