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
@Table(name = "PRICE_LIST_ITEMS", schema = "mycart")
public class PriceListitems {

    private long priceListItemId;
    private long priceListId;
    private long stockItemId;
    private String plType;
    private long amount;

    @Id
    @Column(name = "PRICE_LIST_ITEMS_ID")
    public long getPriceListItemId() {
        return priceListItemId;
    }

    public void setPriceListItemId(long priceListItemId) {
        this.priceListItemId = priceListItemId;
    }

    @Column(name = "PRICE_LIST_ID")
    public long getPriceListId() {
        return priceListId;
    }

    public void setPriceListId(long priceListId) {
        this.priceListId = priceListId;
    }

    @Column(name = "STOCK_ITEM_ID")
    public long getStockItemId() {
        return stockItemId;
    }

    public void setStockItemId(long stockItemId) {
        this.stockItemId = stockItemId;
    }

    @Column(name = "PL_TYPE")
    public String getPlType() {
        return plType;
    }

    public void setPlType(String plType) {
        this.plType = plType;
    }

    @Column(name = "AMOUNT")
    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

}
