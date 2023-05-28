/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cart.dao.intf;

import com.cart.pojo.Brands;
import com.cart.pojo.Category;
import com.cart.pojo.StockItems;
import com.cart.pojo.SubCategory;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author USER
 */
public interface ProductDaoIntf {

    public List<Category> getCategories(long id, String name, Date date);

    public JSONObject addCategory(JSONObject json);

    public JSONObject updateCategory(JSONObject json);

    public JSONObject addSubCategory(JSONObject json);

    public JSONObject updateSubCategory(JSONObject json);

    public List<SubCategory> getSubCategories(long id, long catId, String name, Date date);

    public JSONObject addBrands(JSONObject json);

    public JSONObject updateBrands(JSONObject json);

    public List<Brands> getBrands(long id, long catId,long subCatId, String name, Date date);
    
    public JSONObject addStockItems(JSONObject json);

    public JSONObject updateStockItems(JSONObject json);

    public List<StockItems> getStockItems(Map<String,Object> filter,String userType);
    
    public Map<String,Object> exportStockItems(Map<String, Object> filters, String userType);
    
    public JSONObject getPackOptions(JSONObject json);
    
    public Map<Long, JSONArray> getPackSizes(List<Long> itemIds,long sellerId);
}
