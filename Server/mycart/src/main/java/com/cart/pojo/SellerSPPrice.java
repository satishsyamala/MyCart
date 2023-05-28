package com.cart.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SELLER_SP_PRICE", schema = "mycart")
public class SellerSPPrice {

	private long sellerSPPriceId;
	private long stockItemId;
	private long sellerStockMapId;
	private long packConfigId;
	private double price;
	private int defaultPack;

	@Id
	@Column(name = "SELLER_SP_PRICE_ID")  
	public long getSellerSPPriceId() {
		return sellerSPPriceId;
	}
	
	public void setSellerSPPriceId(long sellerSPPriceId) {
		this.sellerSPPriceId = sellerSPPriceId;
	}

	
	
	@Column(name = "SELLER_STOCK_MAP_ID")
	public long getSellerStockMapId() {
		return sellerStockMapId;
	}

	public void setSellerStockMapId(long sellerStockMapId) {
		this.sellerStockMapId = sellerStockMapId;
	}

	@Column(name = "STOCK_ITEM_ID")
	public long getStockItemId() {
		return stockItemId;
	}

	
	public void setStockItemId(long stockItemId) {
		this.stockItemId = stockItemId;
	}
	
	

	@Column(name = "PACK_CONFIG_ID")
	public long getPackConfigId() {
		return packConfigId;
	}

	public void setPackConfigId(long packConfigId) {
		this.packConfigId = packConfigId;
	}

	@Column(name = "PRICE")
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	@Column(name = "DEFAULT_PACK")
	public int getDefaultPack() {
		return defaultPack;
	}

	public void setDefaultPack(int defaultPack) {
		this.defaultPack = defaultPack;
	}
	
	

}
