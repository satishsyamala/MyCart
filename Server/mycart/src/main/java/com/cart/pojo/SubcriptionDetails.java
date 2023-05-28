package com.cart.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "SUBSCRIPTION_DETAILS", schema = "mycart")
public class SubcriptionDetails {

	private long subscriptionDetailsId;
	private long subscriptionPlanId;
	private long sellerId;
	private double amount;
	private Date startDate;
	private Date endDate;
	private Date paidDate;
	private String status;
	private long userId;
	private int durationDays;
	

	@Id
	@Column(name = "SUBSCRIPTION_DETAILS_ID")
	public long getSubscriptionDetailsId() {
		return subscriptionDetailsId;
	}

	public void setSubscriptionDetailsId(long subscriptionDetailsId) {
		this.subscriptionDetailsId = subscriptionDetailsId;
	}

	@Column(name = "SUBSCRIPTION_PLANS_ID")
	public long getSubscriptionPlanId() {
		return subscriptionPlanId;
	}

	public void setSubscriptionPlanId(long subscriptionPlanId) {
		this.subscriptionPlanId = subscriptionPlanId;
	}

	@Column(name = "SELLER_ID")
	public long getSellerId() {
		return sellerId;
	}

	public void setSellerId(long sellerId) {
		this.sellerId = sellerId;
	}

	@Column(name = "AMOUNT")
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Column(name = "START_DATE")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "END_DATE")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "PAID_DATE")
	public Date getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "USER_ID")
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	@Column(name = "DURATION_DAYS")
	public int getDurationDays() {
		return durationDays;
	}

	public void setDurationDays(int durationDays) {
		this.durationDays = durationDays;
	}
	
	
	
	

}
