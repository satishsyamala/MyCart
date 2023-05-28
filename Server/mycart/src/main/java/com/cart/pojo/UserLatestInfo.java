package com.cart.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER_LATEST_INFO", schema = "mycart")
public class UserLatestInfo {

	private long userlastInfoId;
	private long userId;
	private String latitude;
	private String longitude;
	private long lastNotificationId;
	private String notificationStatus;
	private Date lastLocationDate;
	private Date lastNotificationDate;
	private int acceptedOrders;
	private int pendingOrders;
	private int isActive;
	
	
	
	
	
	
	
	

	@Id
	@Column(name = "USER_LATEST_INFO_ID")
	public long getUserlastInfoId() {
		return userlastInfoId;
	}

	public void setUserlastInfoId(long userlastInfoId) {
		this.userlastInfoId = userlastInfoId;
	}

	@Column(name = "USER_ID")
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	@Column(name = "LATITUDE")
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	@Column(name = "LONGITUDE")
	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}


	@Column(name = "LAST_NOTIFICATION_ID")
	public long getLastNotificationId() {
		return lastNotificationId;
	}

	public void setLastNotificationId(long lastNotificationId) {
		this.lastNotificationId = lastNotificationId;
	}

	@Column(name = "NOTIFICATION_STATUS")
	public String getNotificationStatus() {
		return notificationStatus;
	}

	public void setNotificationStatus(String notificationStatus) {
		this.notificationStatus = notificationStatus;
	}

	@Column(name = "LAST_LOCATION_DATE")
	public Date getLastLocationDate() {
		return lastLocationDate;
	}

	public void setLastLocationDate(Date lastLocationDate) {
		this.lastLocationDate = lastLocationDate;
	}

	@Column(name = "LAST_NOTIFICATION_DATE")
	public Date getLastNotificationDate() {
		return lastNotificationDate;
	}

	public void setLastNotificationDate(Date lastNotificationDate) {
		this.lastNotificationDate = lastNotificationDate;
	}

	@Column(name = "ACCEPTED_ORDERS")
	public int getAcceptedOrders() {
		return acceptedOrders;
	}

	public void setAcceptedOrders(int acceptedOrders) {
		this.acceptedOrders = acceptedOrders;
	}

	@Column(name = "PENDING_ORDERS")
	public int getPendingOrders() {
		return pendingOrders;
	}

	public void setPendingOrders(int pendingOrders) {
		this.pendingOrders = pendingOrders;
	}

	@Column(name = "IS_ACTIVE")
	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

}
