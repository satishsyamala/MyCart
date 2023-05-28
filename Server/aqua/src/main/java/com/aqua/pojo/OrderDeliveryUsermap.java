package com.aqua.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ORDER_DELIVERY_USER_MAP", schema = "aqua")
public class OrderDeliveryUsermap {

	private long orderDeliveryUserMapId;
	private long orderId;
	private long userId;
	private String assignedStatus;
	private Date assignedDate;
	private Date acceptedDate;
	private Date pickUpDate;
	private String qrCode;
	private long sellerId;
	private String userType;
	private double deliveryCharges;
	private String settiledAmtStatus;
	private int pickupOtp;

	@Id
	@Column(name = "ORDER_DELIVERY_USER_MAP_ID")
	public long getOrderDeliveryUserMapId() {
		return orderDeliveryUserMapId;
	}

	public void setOrderDeliveryUserMapId(long orderDeliveryUserMapId) {
		this.orderDeliveryUserMapId = orderDeliveryUserMapId;
	}

	@Column(name = "ORDER_ID")
	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	@Column(name = "USER_ID")
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	@Column(name = "ASSIGN_STATUS")
	public String getAssignedStatus() {
		return assignedStatus;
	}

	public void setAssignedStatus(String assignedStatus) {
		this.assignedStatus = assignedStatus;
	}

	@Column(name = "ASSIGNED_DATE")
	public Date getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(Date assignedDate) {
		this.assignedDate = assignedDate;
	}

	@Column(name = "ACCEPTED_DATE")
	public Date getAcceptedDate() {
		return acceptedDate;
	}

	public void setAcceptedDate(Date acceptedDate) {
		this.acceptedDate = acceptedDate;
	}

	@Column(name = "PICK_UP_DATE")
	public Date getPickUpDate() {
		return pickUpDate;
	}

	public void setPickUpDate(Date pickUpDate) {
		this.pickUpDate = pickUpDate;
	}

	@Column(name = "QR_CODE")
	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	@Column(name = "SELLER_ID")
	public long getSellerId() {
		return sellerId;
	}

	public void setSellerId(long sellerId) {
		this.sellerId = sellerId;
	}

	@Column(name = "USER_TYPE")
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@Column(name = "DELIVERY_CHARGE")
	public double getDeliveryCharges() {
		return deliveryCharges;
	}

	public void setDeliveryCharges(double deliveryCharges) {
		this.deliveryCharges = deliveryCharges;
	}

	@Column(name = "SETTILED_AMT_STATUS")
	public String getSettiledAmtStatus() {
		return settiledAmtStatus;
	}

	public void setSettiledAmtStatus(String settiledAmtStatus) {
		this.settiledAmtStatus = settiledAmtStatus;
	}
	
	@Column(name = "PICKUP_OTP")
	public int getPickupOtp() {
		return pickupOtp;
	}

	public void setPickupOtp(int pickupOtp) {
		this.pickupOtp = pickupOtp;
	}
	
	

}
