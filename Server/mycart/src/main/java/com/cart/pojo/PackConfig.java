package com.cart.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PACK_CONFIG", schema = "mycart")
public class PackConfig {

	private long packConfigId;
	private String packName;
	private double quantity;
	private long parentId;
	private String parentIds;

	@Id
	@Column(name = "PACK_CONFIG_ID")
	public long getPackConfigId() {
		return packConfigId;
	}

	public void setPackConfigId(long packConfigId) {
		this.packConfigId = packConfigId;
	}

	@Column(name = "PACK_NAME")
	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	@Column(name = "QUANTITY")
	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	@Column(name = "PARENT")
	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	@Column(name = "PARENT_IDS")
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

}
