/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aqua.service.intf;

import java.util.Date;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.security.core.userdetails.UserDetails;

import com.aqua.pojo.DeliveryCharge;
import com.aqua.pojo.GeneralSetting;
import com.aqua.pojo.Seller;
import com.aqua.pojo.Users;

/**
 *
 * @author Shrehan
 */
public interface UserServiceIntf {

	public JSONObject loginValidation(String userName, String password, JSONObject json);

	public String saveUser(JSONObject json);

	public JSONObject updateUser(JSONObject json);

	public JSONObject addDeliveryAddress(JSONObject json);

	public JSONObject updateDeliveryAddress(JSONObject json);

	public JSONArray getAddress(long userId, Date date);

	public JSONObject getSellerDetails(JSONObject json);

	public JSONObject saveOrUpdateSellerDetails(JSONObject json);

	public JSONArray getSellerMappedItems(JSONObject json);

	public JSONObject mapStockItems(JSONObject json);

	public JSONObject updatePriceAndQty(JSONObject obj);

	public JSONArray getAdminUsers(JSONObject json);

	public JSONArray getSellers(JSONObject json);

	public JSONArray getConsumers(JSONObject json);

	public JSONArray getSellerUsers(JSONObject json);

	public JSONArray getDataForView(JSONObject obj);

	public JSONObject getDataForTable(JSONObject json);

	public JSONObject changesPassword(JSONObject json);

	public JSONObject getGeneralSetting(long sellerId);

	public JSONObject getDeliveryCharge(long sellerId);

	public String saveGeneralSettings(JSONObject json);

	public String saveDeliveryCharge(JSONObject json);

	public JSONObject deliveryAddressChange(JSONObject json);

	public JSONObject removeStockMap(JSONObject obj);

	public JSONArray getSubscriptionPlans(JSONObject json);

	public String saveSubscriptionPlans(JSONObject json);

	public String updateSubscriptionPlans(JSONObject json);
	
	public JSONObject addsubscription(JSONObject json);
	
	public JSONArray getSubcriptionDetails(JSONObject json);
	
	public JSONObject forgotPassword(JSONObject json);
	
	public UserDetails getUserByMoblleNo(String mobleNo);
	
	public String generateToken(JSONObject json) ;
}
