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
@Table(name = "OFFER_ITEMS", schema = "mycart")
public class OfferItems {

    private long offerItemId;
    private long offerId;
    private long stockItemId;

    @Id
    @Column(name = "OFFER_ITEM_ID")
    public long getOfferItemId() {
        return offerItemId;
    }

    public void setOfferItemId(long offerItemId) {
        this.offerItemId = offerItemId;
    }

    @Column(name = "OFFER_ID")
    public long getOfferId() {
        return offerId;
    }

    public void setOfferId(long offerId) {
        this.offerId = offerId;
    }

    @Column(name = "STOCK_ITEM_ID")
    public long getStockItemId() {
        return stockItemId;
    }

    public void setStockItemId(long stockItemId) {
        this.stockItemId = stockItemId;
    }

}
