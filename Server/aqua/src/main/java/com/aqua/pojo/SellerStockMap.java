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
@Table(name = "SELLER_STOCK_MAP", schema = "aqua")
public class SellerStockMap {

    private long sellerStockMapId;
    private long sellerId;
    private long stockItemId;
    private long quantity;
    private double price;

    @Id
    @Column(name = "SELLER_STOCK_MAP_ID")
    public long getSellerStockMapId() {
        return sellerStockMapId;
    }

    public void setSellerStockMapId(long sellerStockMapId) {
        this.sellerStockMapId = sellerStockMapId;
    }

    @Column(name = "SELLER_ID")
    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

    @Column(name = "STOCK_ITEM_ID")
    public long getStockItemId() {
        return stockItemId;
    }

    public void setStockItemId(long stockItemId) {
        this.stockItemId = stockItemId;
    }

    @Column(name = "QUANTITY")
    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    @Column(name = "Price")
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
