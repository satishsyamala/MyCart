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
@Table(name = "STOCK_IMAGES", schema = "aqua")
public class StockImages {

    private long stockImageId;
    private long stockItemId;
    private String image;

    @Id
    @Column(name = "STOCK_IMAGE_ID")
    public long getStockImageId() {
        return stockImageId;
    }

    public void setStockImageId(long stockImageId) {
        this.stockImageId = stockImageId;
    }

    @Column(name = "STOCK_ITEM_ID")
    public long getStockItemId() {
        return stockItemId;
    }

    public void setStockItemId(long stockItemId) {
        this.stockItemId = stockItemId;
    }

    @Column(name = "IMAGE")
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
