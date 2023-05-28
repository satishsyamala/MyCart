package com.cart.dao.intf;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public interface DeliveryDaoIntf {
	
	
	public JSONObject locationData(JSONObject json);
	
	public JSONObject acceptOrder(JSONObject json);
	
	public JSONArray getAssignedOrder(JSONObject json,String status);
	
	public JSONObject userActiveOrInActive(JSONObject json);

}
