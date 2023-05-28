package com.aqua.service.intf;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public interface DeliveryServiceIntf {
	
	public JSONObject locationData(JSONObject json);
	
	public JSONObject acceptOrder(JSONObject json);
	
	public JSONArray getAssignedOrder(JSONObject json);
	
	public JSONObject userActiveOrInActive(JSONObject json);

}
