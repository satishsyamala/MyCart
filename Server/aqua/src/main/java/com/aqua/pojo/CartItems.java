/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aqua.pojo;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "CART_ITEMS", schema = "aqua")
public class CartItems {

    private long cartItemId;
    private long stockItemsId;
    private long userId;
    private String catOrderBy;
    private Date addedOn;
    private int quantitiy;
    private double unitPrice;
    private double disPer;
    private double price;
    private double discount;
    private double tax;
    private double finalPrice;
    private long offersId;
    private String status;
    private String image;
    private long sellerId;
    private long addressId;
	private double packQty;
	private String pack;

    @Id
    @Column(name = "CART_ITEM_ID")
    public long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(long cartItemId) {
        this.cartItemId = cartItemId;
    }

    @Column(name = "STOCK_ITEM_ID")
    public long getStockItemsId() {
        return stockItemsId;
    }

    public void setStockItemsId(long stockItemsId) {
        this.stockItemsId = stockItemsId;
    }

    @Column(name = "USER_ID")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Column(name = "CAT_ORDER_BY")
    public String getCatOrderBy() {
        return catOrderBy;
    }

    public void setCatOrderBy(String catOrderBy) {
        this.catOrderBy = catOrderBy;
    }

    @Column(name = "ADDED_ON")
    public Date getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(Date addedOn) {
        this.addedOn = addedOn;
    }

    @Column(name = "QUANTITY")
    public int getQuantitiy() {
        return quantitiy;
    }

    public void setQuantitiy(int quantitiy) {
        this.quantitiy = quantitiy;
    }

    @Column(name = "UNIT_PRICE")
    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Column(name = "DIS_PER")
    public double getDisPer() {
        return disPer;
    }

    public void setDisPer(double disPer) {
        this.disPer = disPer;
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

    @Column(name = "OFFER_ID")
    public long getOffersId() {
        return offersId;
    }

    public void setOffersId(long offersId) {
        this.offersId = offersId;
    }

    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "IMAGE")
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Column(name = "SELLER_ID")
	public long getSellerId() {
		return sellerId;
	}

	public void setSellerId(long sellerId) {
		this.sellerId = sellerId;
	}
	 @Column(name = "ADDRESS_ID")
	public long getAddressId() {
		return addressId;
	}

	public void setAddressId(long addressId) {
		this.addressId = addressId;
	}
    
	@Column(name = "PACK_QTY")
	public double getPackQty() {
		return packQty;
	}

	public void setPackQty(double packQty) {
		this.packQty = packQty;
	}

	

	@Column(name = "PACK")
	public String getPack() {
		return pack;
	}

	public void setPack(String pack) {
		this.pack = pack;
	}

}
