package com.aqua.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SUBSCRIPTION_PLANS", schema = "aqua")
public class SubscriptionPlans {

	private long subscriptionPlanId;
	private String planType;
	private String name;
	private int durationDays;
	private double amount;
	private String image;

	@Id
	@Column(name = "SUBSCRIPTION_PLANS_ID")
	public long getSubscriptionPlanId() {
		return subscriptionPlanId;
	}

	public void setSubscriptionPlanId(long subscriptionPlanId) {
		this.subscriptionPlanId = subscriptionPlanId;
	}

	@Column(name = "PLAN_TYPES")
	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DURATION_DAYS")
	public int getDurationDays() {
		return durationDays;
	}

	public void setDurationDays(int durationDays) {
		this.durationDays = durationDays;
	}

	@Column(name = "AMOUNT")
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Column(name = "IMAGE")
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	

}
