/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cart.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "PRICE_LIST_USER_TYPE", schema = "mycart")
public class PriceListUserType {

    private long priceListUserTypeId;
    private long priceListId;
    private String userType;

    @Id
    @Column(name = "PRICE_LIST_USER_TYPE_ID")

    public long getPriceListUserTypeId() {
        return priceListUserTypeId;
    }

    public void setPriceListUserTypeId(long priceListUserTypeId) {
        this.priceListUserTypeId = priceListUserTypeId;
    }

    @Column(name = "PRICE_LIST_ID")
    public long getPriceListId() {
        return priceListId;
    }

    public void setPriceListId(long priceListId) {
        this.priceListId = priceListId;
    }

    @Column(name = "USER_TYPE")
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

}
