/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cart.service.intf;

import com.cart.pojo.StockItems;
import com.cart.pojo.SubCategory;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author USER
 */
public interface ProductServiceIntf {

    public JSONArray getCategory(long id, String name, Date date);

    public JSONObject addCategory(JSONObject json);

    public JSONObject updateCategory(JSONObject json);

    public JSONArray getSubCategories(long id, long catId, String name, Date date);

    public JSONObject addSubCategory(JSONObject json);

    public JSONObject updateSubCategory(JSONObject json);

    public JSONObject getBusOptions(JSONObject json);
    
    public JSONArray getBrands(long id, long catId,long subCatId, String name, Date date);

    public JSONObject addBrands(JSONObject json);

    public JSONObject updateBrands(JSONObject json);
    
     public JSONObject addStockItems(JSONObject json);

    public JSONObject updateStockItems(JSONObject json);

    public JSONArray getStockItems(JSONObject json,String userType);
    
    public File exportStockItems(JSONObject json);
    
    public JSONObject getPackOptions(JSONObject json);
}
