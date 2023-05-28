/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cart.service.impl;

import com.cart.dao.intf.ProductDaoIntf;
import com.cart.dao.intf.UserDaoIntf;
import com.cart.pojo.DeliveryAddress;
import com.cart.pojo.DeliveryCharge;
import com.cart.pojo.GeneralSetting;
import com.cart.pojo.Seller;
import com.cart.pojo.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.cart.service.intf.UserServiceIntf;
import com.cart.util.ConvertJSONtoObject;
import com.cart.util.GeneralUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Shrehan
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class UserServiceImpl implements UserServiceIntf {

	private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
	@Autowired
	private UserDaoIntf userDaoIntf;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Autowired
	private ProductDaoIntf productDaoIntf;

	@Override
	public JSONObject loginValidation(String userName, String password, JSONObject json) {
		return userDaoIntf.loginValidation(userName, password, json);
	}

	@Override
	public String saveUser(JSONObject json) {
		return userDaoIntf.saveUser(json);
	}

	@Override
	public JSONObject updateUser(JSONObject json) {
		return userDaoIntf.updateUser(json);
	}

	@Override
	public JSONObject addDeliveryAddress(JSONObject json) {
		return userDaoIntf.addDeliveryAddress(json);
	}

	@Override
	public JSONObject updateDeliveryAddress(JSONObject json) {
		DeliveryAddress delAdd = new DeliveryAddress();
		ConvertJSONtoObject.jsonToObject(delAdd, json);
		return userDaoIntf.updateDeliveryAddress(delAdd);
	}

	@Override
	public JSONArray getAddress(long userId, Date date) {
		JSONArray array = new JSONArray();
		List<DeliveryAddress> ls = userDaoIntf.getDeliveryAddress(userId, 0, null, date);
		for (DeliveryAddress d : ls) {
			JSONObject o = ConvertJSONtoObject.pojoToJSON(d);
			o.put("default", d.isDefaultDel() ? "Yes" : "No");
			array.add(o);
		}
		return array;
	}

	@Override
	public JSONObject getSellerDetails(JSONObject json) {
		long userId = GeneralUtil.objTolong(json.get("user_id"));
		Seller s = userDaoIntf.getSellerDetails(userId);
		return ConvertJSONtoObject.pojoToJSON(s);
	}

	@Override
	public JSONObject saveOrUpdateSellerDetails(JSONObject json) {
		return userDaoIntf.saveOrUpdateSellerDetails(json);
	}

	@Override
	public JSONArray getSellerMappedItems(JSONObject json) {
		JSONArray result = new JSONArray();
		JSONArray d = userDaoIntf.getSellerMappedItems(json);
		List<Long> stIds = new ArrayList<>();
		for (int i = 0; i < d.size(); i++) {
			JSONObject j = (JSONObject) d.get(i);
			stIds.add(GeneralUtil.objTolong(j.get("stock_item_id")));
		}
		long sellerId = GeneralUtil.objTolong(json.get("seller_id"));
		Map<Long, JSONArray> pk = productDaoIntf.getPackSizes(stIds, sellerId);
		for (int i = 0; i < d.size(); i++) {
			JSONObject j = (JSONObject) d.get(i);
			if (pk.containsKey(GeneralUtil.objTolong(j.get("stock_item_id")))) {
				j.put("packSize", pk.get(GeneralUtil.objTolong(j.get("stock_item_id"))));
			}
			result.add(j);
		}

		return result;
	}

	@Override
	public JSONObject mapStockItems(JSONObject json) {
		JSONObject req = ConvertJSONtoObject.jsonToObject(json);
		req.put("name", ((JSONObject) json.get("name")).get("value"));
		logger.info("req :  " + req.toString());
		return userDaoIntf.mapStockItems(req);
	}

	@Override
	public JSONObject updatePriceAndQty(JSONObject json) {
		return userDaoIntf.updatePriceAndQty(json);
	}

	@Override
	public JSONArray getAdminUsers(JSONObject json) {
		return userDaoIntf.getAdminUsers(json);
	}

	@Override
	public JSONArray getSellers(JSONObject json) {
		return userDaoIntf.getSellers(json);
	}

	@Override
	public JSONArray getConsumers(JSONObject json) {
		return userDaoIntf.getConsumers(json);
	}

	@Override
	public JSONArray getSellerUsers(JSONObject json) {
		return userDaoIntf.getSellerUsers(json);
	}

	@Override
	public JSONArray getDataForView(JSONObject obj) {
		return userDaoIntf.getDataForView(obj);
	}

	@Override
	public JSONObject getDataForTable(JSONObject json) {
		return userDaoIntf.getDataForTable(json);
	}

	@Override
	public JSONObject changesPassword(JSONObject json) {
		long userId = GeneralUtil.objTolong(json.get("user_id"));
		String oldpass = json.get("old_pass").toString();
		String newpass = json.get("new_pass").toString();
		return userDaoIntf.changesPassword(userId, oldpass, newpass);
	}

	@Override
	public JSONObject getGeneralSetting(long sellerId) {
		GeneralSetting gn = userDaoIntf.getGeneralSetting(sellerId);
		if (gn != null) {
			JSONObject ob = ConvertJSONtoObject.pojoToJSON(gn);
			ob.put("paymentModes", GeneralUtil.jsonArrayToMaltiple(gn.getPaymentModes()));
			return ob;
		} else {
			return null;
		}
	}

	@Override
	public JSONObject getDeliveryCharge(long sellerId) {
		DeliveryCharge gn = userDaoIntf.getDeliveryCharge(sellerId);
		if (gn != null) {
			return ConvertJSONtoObject.pojoToJSON(gn);
		} else {
			return null;
		}
	}

	@Override
	public String saveGeneralSettings(JSONObject json) {
		return userDaoIntf.saveGeneralSettings(json);
	}

	@Override
	public String saveDeliveryCharge(JSONObject json) {
		return userDaoIntf.saveDeliveryCharge(json);
	}

	@Override
	public JSONObject deliveryAddressChange(JSONObject json) {
		return userDaoIntf.deliveryAddressChange(json);
	}

	@Override
	public JSONObject removeStockMap(JSONObject obj) {
		return userDaoIntf.removeStockMap(obj);
	}

	@Override
	public JSONArray getSubscriptionPlans(JSONObject json) {
		return userDaoIntf.getSubscriptionPlans(json);
	}

	@Override
	public String saveSubscriptionPlans(JSONObject json) {
		return userDaoIntf.saveSubscriptionPlans(json);
	}

	@Override
	public String updateSubscriptionPlans(JSONObject json) {
		return userDaoIntf.updateSubscriptionPlans(json);
	}

	@Override
	public JSONObject addsubscription(JSONObject json) {
		return userDaoIntf.addsubscription(json);
	}

	public JSONArray getSubcriptionDetails(JSONObject json) {
		return userDaoIntf.getSubcriptionDetails(json);
	}

	public JSONObject forgotPassword(JSONObject json) {
		return userDaoIntf.forgotPassword(json);
	}

	public UserDetails getUserByMoblleNo(String mobleNo) {
		Users us = userDaoIntf.getUserByMoblleNo(mobleNo);
		return new User(us.getMobileNo(), bcryptEncoder.encode(us.getPassword()), new ArrayList<>());
	}

	public String generateToken(JSONObject json) {
		return userDaoIntf.generateToken(GeneralUtil.objToString(json.get("user_name")),
				GeneralUtil.objToString(json.get("password")), true);
	}

}
