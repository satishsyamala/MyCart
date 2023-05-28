package com.aqua.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "STOCK_PACK_PRICE", schema = "aqua")
public class StockPackPrice {

	private long stockPackPriceId;
	private long stockItemId;
	private long packConfigId;
	private double price;
	private int defaultPack;

	@Id
	@Column(name = "STOCK_PACK_PRICE_ID")
	public long getStockPackPriceId() {
		return stockPackPriceId;
	}

	public void setStockPackPriceId(long stockPackPriceId) {
		this.stockPackPriceId = stockPackPriceId;
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
