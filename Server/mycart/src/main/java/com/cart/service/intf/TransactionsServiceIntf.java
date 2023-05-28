/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cart.service.intf;

import com.cart.pojo.CartItems;
import java.io.File;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author USER
 */
public interface TransactionsServiceIntf {

    public JSONArray getItemsForSell(JSONObject filters);


    public JSONObject getCartItems(JSONObject filter);

    public JSONArray Last10OrderItems(JSONObject json);
    
    public JSONObject addItemToCart(JSONObject json);

    public JSONObject updateQty(JSONObject json);

    public JSONObject deleteItemCart(JSONObject json);

    public JSONObject createOrder(JSONObject json);

    public JSONArray getUserOrders(JSONObject json);

    public JSONObject getOrdersDetails(JSONObject json);

    public JSONArray getOrdersForProcess(JSONObject filters);

    public JSONArray getSellerDetails(JSONObject json);
    
    public JSONObject getOrderProccess(JSONObject json);
    
     public File downloadOrderPDF(long orderId);
     
      public JSONArray getSellers(JSONObject filters);

}
