/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aqua.pojo;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "STOCK_ITEMS", schema = "aqua")
public class StockItems {

	private long stockItemId;
	private String code;
	private String name;
	private String description;
	private long brandId;
	private long subCategoryId;
	private long categoryId;
	private String image;
	private String uom;
	private double packSize;
	private double price;
	private Date createdOn;
	private String createdBy;
	private Date updatedOn;
	private String updatedBy;
	private String status;
	private long sellerId;
	private long packConfigId;
	private String packInfo;
	private String packName;
	private String hierarchy;

	public String brandName;
	public String subCatName;
	public String catName;
	private String packSize1;
	private double price1;
	private String packSize2;
	private double price2;
	private String packSize3;
	private double price3;
	private String packSize4;
	private double price4;

	public StockItems() {

	}

	public StockItems(long stockItemId) {
		this.stockItemId = stockItemId;
	}

	@Id
	@Column(name = "STOCK_ITEM_ID")
	public long getStockItemId() {
		return stockItemId;
	}

	public void setStockItemId(long stockItemId) {
		this.stockItemId = stockItemId;
	}

	@Column(name = "CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "UOM")
	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	@Column(name = "PACK_SIZE")
	public double getPackSize() {
		return packSize;
	}

	public void setPackSize(double packSize) {
		this.packSize = packSize;
	}

	@Column(name = "PRICE")
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Column(name = "BRAND_ID")
	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	@Column(name = "SUB_CAT_ID")
	public long getSubCategoryId() {
		return subCategoryId;
	}

	public void setSubCategoryId(long subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	@Column(name = "CATEGORY_ID")
	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	@Column(name = "IMAGE")
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Column(name = "CREATED_ON")
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "UPDATED_ON")
	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Column(name = "UPDATED_BY")
	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "SELLER_ID")
	public long getSellerId() {
		return sellerId;
	}

	public void setSellerId(long sellerId) {
		this.sellerId = sellerId;
	}

	@Column(name = "PACK_CONFIG_ID")
	public long getPackConfigId() {
		return packConfigId;
	}

	public void setPackConfigId(long packConfigId) {
		this.packConfigId = packConfigId;
	}

	@Column(name = "PACK_INFO")
	public String getPackInfo() {
		return packInfo;
	}

	public void setPackInfo(String packInfo) {
		this.packInfo = packInfo;
	}

	@Column(name = "PACK_NAME")
	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}
	
		
	@Column(name = "HIERARCHY")
	public String getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(String hierarchy) {
		this.hierarchy = hierarchy;
	}

	@Transient
	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	@Transient
	public String getSubCatName() {
		return subCatName;
	}

	public void setSubCatName(String subCatName) {
		this.subCatName = subCatName;
	}

	@Transient
	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	@Transient
	public String getPackSize1() {
		return packSize1;
	}

	public void setPackSize1(String packSize1) {
		this.packSize1 = packSize1;
	}

	@Transient
	public double getPrice1() {
		return price1;
	}

	public void setPrice1(double price1) {
		this.price1 = price1;
	}

	@Transient
	public String getPackSize2() {
		return packSize2;
	}

	public void setPackSize2(String packSize2) {
		this.packSize2 = packSize2;
	}

	@Transient
	public double getPrice2() {
		return price2;
	}

	public void setPrice2(double price2) {
		this.price2 = price2;
	}

	@Transient
	public String getPackSize3() {
		return packSize3;
	}

	public void setPackSize3(String packSize3) {
		this.packSize3 = packSize3;
	}

	@Transient
	public double getPrice3() {
		return price3;
	}

	public void setPrice3(double price3) {
		this.price3 = price3;
	}

	@Transient
	public String getPackSize4() {
		return packSize4;
	}

	public void setPackSize4(String packSize4) {
		this.packSize4 = packSize4;
	}

	@Transient
	public double getPrice4() {
		return price4;
	}

	public void setPrice4(double price4) {
		this.price4 = price4;
	}

}
