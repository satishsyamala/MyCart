/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cart.dao.intf;

import com.cart.pojo.DeliveryAddress;
import com.cart.pojo.DeliveryCharge;
import com.cart.pojo.GeneralSetting;
import com.cart.pojo.Seller;
import com.cart.pojo.Users;

import java.util.Date;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Shrehan
 */
public interface UserDaoIntf {

	public JSONObject loginValidation(String userName, String password, JSONObject json);

	public String saveUser(JSONObject json);

	public JSONObject updateUser(JSONObject json);

	public JSONObject addDeliveryAddress(JSONObject json);

	public JSONObject updateDeliveryAddress(DeliveryAddress delAdd);

	public List<DeliveryAddress> getDeliveryAddress(long useerId, long id, String address, Date date);

	public Seller getSellerDetails(long userId);

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

	public JSONObject changesPassword(long userId, String oldpassword, String newPassword);

	public GeneralSetting getGeneralSetting(long sellerId);

	public DeliveryCharge getDeliveryCharge(long sellerId);

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
	
	public Users getUserByMoblleNo(String mobleNo);
	
	public String generateToken(String mobileNo, String password, boolean validation) ;

}
