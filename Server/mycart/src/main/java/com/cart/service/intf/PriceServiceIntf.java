/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cart.service.intf;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author USER
 */
public interface PriceServiceIntf {
	public JSONArray getPriceListDetails(JSONObject filters);

	public JSONObject savePriceList(JSONObject json);

	public JSONArray getOffers(JSONObject filters);
}
