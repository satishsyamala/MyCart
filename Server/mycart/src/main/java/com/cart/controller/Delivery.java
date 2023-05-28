package com.cart.controller;



import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cart.service.intf.DeliveryServiceIntf;
import com.cart.util.AESEncryption;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/delivery")
public class Delivery {
	
	@Autowired
	private DeliveryServiceIntf deliveryServiceIntf;

	@PostMapping(value="/location",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject locationData(@RequestBody String text)
	{
		JSONObject result = new JSONObject();
        try {
            
           
            result = deliveryServiceIntf.locationData(AESEncryption.decrypt(text));
        } catch (Exception e) {

        }
        return result;
	}
	
	@PostMapping(value="/accept-order",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject acceptOrder(@RequestBody String text)
	{
		JSONObject result = new JSONObject();
        try {
            
           
            result = deliveryServiceIntf.acceptOrder(AESEncryption.decrypt(text));
        } catch (Exception e) {

        }
        return result;
	}
	
	@PostMapping(value="/delivery-order",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONArray delivertOrders(@RequestBody String text)
	{
		JSONArray result = new JSONArray();
        try {
            
           
            result = deliveryServiceIntf.getAssignedOrder(AESEncryption.decrypt(text));
        } catch (Exception e) {

        }
        return result;
	}
	
	@PostMapping(value="/active-or-inactive",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject userActiveOrInActive(@RequestBody String text)
	{
		JSONObject result = new JSONObject();
        try {
            
           
            result = deliveryServiceIntf.userActiveOrInActive(AESEncryption.decrypt(text));
        } catch (Exception e) {

        }
        return result;
	}
}
