package com.cart.service.impl;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cart.dao.intf.DeliveryDaoIntf;
import com.cart.service.intf.DeliveryServiceIntf;
import com.cart.util.GeneralUtil;

@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class DeliveryServiceImpl implements DeliveryServiceIntf {

	@Autowired
	private DeliveryDaoIntf deliveryDaoIntf;

	public JSONObject locationData(JSONObject json) {
		return deliveryDaoIntf.locationData(json);
	}

	public JSONObject acceptOrder(JSONObject json) {
		return deliveryDaoIntf.acceptOrder(json);
	}

	public JSONArray getAssignedOrder(JSONObject json) {
		return deliveryDaoIntf.getAssignedOrder(json, GeneralUtil.objToString(json.get("status")));
	}
	
	public JSONObject userActiveOrInActive(JSONObject json) {
		return deliveryDaoIntf.userActiveOrInActive(json);
	}
}
