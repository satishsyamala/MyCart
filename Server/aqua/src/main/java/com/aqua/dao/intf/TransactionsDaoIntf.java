/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aqua.dao.intf;

import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.aqua.pojo.CartItems;

/**
 *
 * @author USER
 */
public interface TransactionsDaoIntf {
    
    public JSONArray getItemsForSell(JSONObject filters);

    public JSONObject addItemToCart(JSONObject json);

    public JSONObject updateQty(JSONObject json);

    public JSONObject deleteItemCart(JSONObject json);
    
    public JSONObject getCartItmes(JSONObject json);
    
    public JSONArray Last10OrderItems(JSONObject json);
    
    public JSONObject createOrder(JSONObject json);
    
     public JSONArray getUserOrders(JSONObject json);
     
     public JSONObject getOrdersDetails(JSONObject json);
     
     public JSONArray getOrdersForProcess(JSONObject filters);
     
     public JSONArray getSellerDetails(JSONArray item, long delId, String key, int pageSize);
     
     public JSONObject getOrderProccess(JSONObject json);
     
      public JSONArray getSellers(JSONObject filters);
}
