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
@Table(name = "SELLER_USER_MAP", schema = "mycart")
public class SellerUserMap {

    private long sellerUserMapId;
    private long sellerId;
    private long userId;

    @Id
    @Column(name = "SELLER_USER_MAP_ID")
    public long getSellerUserMapId() {
        return sellerUserMapId;
    }

    public void setSellerUserMapId(long sellerUserMapId) {
        this.sellerUserMapId = sellerUserMapId;
    }

    @Column(name = "SELLER_ID")
    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

    @Column(name = "USER_ID")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

}
