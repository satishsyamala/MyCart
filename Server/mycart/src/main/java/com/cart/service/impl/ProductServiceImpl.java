/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cart.service.impl;

import com.cart.dao.intf.ProductDaoIntf;
import com.cart.file.ExcelReadAndWrite;
import com.cart.pojo.Brands;
import com.cart.pojo.Category;
import com.cart.pojo.StockItems;
import com.cart.pojo.SubCategory;
import com.cart.service.intf.ProductServiceIntf;
import com.cart.util.ConvertJSONtoObject;
import com.cart.util.GeneralUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author USER
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class ProductServiceImpl implements ProductServiceIntf {

    @Autowired
    private ProductDaoIntf productDaoIntf;

    public JSONArray getCategory(long id, String name, Date date) {
        JSONArray array = new JSONArray();
        try {
            List<Category> cat = productDaoIntf.getCategories(id, name, date);
            for (Category c : cat) {
                array.add(ConvertJSONtoObject.pojoToJSON(c));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return array;
    }

    public JSONObject addCategory(JSONObject json) {
        return productDaoIntf.addCategory(json);
    }

    public JSONObject updateCategory(JSONObject json) {
        return productDaoIntf.updateCategory(json);
    }

    public JSONArray getSubCategories(long id, long catId, String name, Date date) {
        JSONArray array = new JSONArray();
        try {
            List<SubCategory> cat = productDaoIntf.getSubCategories(id, catId, name, date);
            for (SubCategory c : cat) {
                array.add(ConvertJSONtoObject.pojoToJSON(c));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return array;
    }

    public JSONObject addSubCategory(JSONObject json) {
        return productDaoIntf.addSubCategory(json);
    }

    public JSONObject updateSubCategory(JSONObject json) {
        return productDaoIntf.updateSubCategory(json);
    }

    @Override
    public JSONObject getBusOptions(JSONObject json) {
        JSONObject object = new JSONObject();
        try {
            String type = json.get("type").toString();
            long id = 0;
            boolean action = json.containsKey("action") ? true : false;
            long catId = (json.containsKey("catId") ? GeneralUtil.objTolong(json.get("catId")) : 0);
            long subcat = (json.containsKey("subCatId") ? GeneralUtil.objTolong(json.get("subCatId")) : 0);
            long brand = (json.containsKey("brandId") ? GeneralUtil.objTolong(json.get("brandId")) : 0);
            if (type.equalsIgnoreCase("category")) {
                object.put("category", new JSONArray());
                object.put("subcategory", new JSONArray());
                object.put("brand", new JSONArray());
                 object.put("stock", new JSONArray());
                JSONArray array = new JSONArray();
                List<Category> cat = productDaoIntf.getCategories(id, null, null);
                for (Category c : cat) {
                    array.add(getOptionJSON(c.getName(), c.getCategoryId()));
                }
                object.put("category", array);
                if (array.size() == 0) {
                    action = false;
                }
            }

            if ((action && catId > 0) || type.equalsIgnoreCase("subcategory")) {
                if (json.containsKey("categoryId")) {
                    id = GeneralUtil.objTolong(json.get("categoryId"));
                } else {
                    id = catId;
                }
                List<SubCategory> cat = productDaoIntf.getSubCategories(0, id, null, null);
                JSONArray array = new JSONArray();
                for (SubCategory c : cat) {
                    array.add(getOptionJSON(c.getName(), c.getSubCategoryId()));
                }
                object.put("subcategory", array);
                if (array.size() == 0) {
                    action = false;
                }
            }

            if ((action && subcat > 0) || type.equalsIgnoreCase("brand")) {
                if (json.containsKey("subCategoryId")) {
                    id = GeneralUtil.objTolong(json.get("subCategoryId"));
                } else {
                    id = subcat;
                }
                List<Brands> cat = productDaoIntf.getBrands(0, 0, id, null, null);
                JSONArray array = new JSONArray();
                for (Brands c : cat) {
                    array.add(getOptionJSON(c.getName(), c.getBrandId()));
                }
                object.put("brand", array);

            }
            if ((action && catId > 0 && subcat > 0) || type.equalsIgnoreCase("stock")) {
                Map<String, Object> map = new HashMap<>();
                map.put("categoryId", GeneralUtil.objTolong(json.get("categoryId")));
                map.put("subCategoryId",  GeneralUtil.objTolong(json.get("subCategoryId")));
                if(GeneralUtil.objTolong(json.get("brandId"))>0)
                     map.put("brandId",  GeneralUtil.objTolong(json.get("brandId")));
                JSONArray array = new JSONArray();
                List<StockItems> ls = productDaoIntf.getStockItems(map,"admin");
                for (StockItems c : ls) {
                    array.add(getOptionJSON(c.getName(), c.getStockItemId()));
                }
                object.put("stock", array);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    public JSONObject getOptionJSON(String name, String key) {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("key", key);
        return json;
    }

    public JSONObject getOptionJSON(String name, long key) {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("key", key);
        return json;
    }

    public JSONArray getBrands(long id, long catId, long subCatId, String name, Date date) {
        JSONArray array = new JSONArray();
        try {
            List<Brands> cat = productDaoIntf.getBrands(id, catId, subCatId, name, date);
            for (Brands c : cat) {
                array.add(ConvertJSONtoObject.pojoToJSON(c));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return array;
    }

    public JSONObject addBrands(JSONObject json) {
        return productDaoIntf.addBrands(json);
    }

    public JSONObject updateBrands(JSONObject json) {
        return productDaoIntf.updateBrands(json);
    }

    public JSONObject addStockItems(JSONObject json) {
        return productDaoIntf.addStockItems(json);
    }

    public JSONObject updateStockItems(JSONObject json) {
        return productDaoIntf.updateStockItems(json);
    }

    public JSONArray getStockItems(JSONObject json,String userType) {
        JSONArray array = new JSONArray();
        try {
            Map<String, Object> filter = new HashMap<>();
            Set<String> keys = json.keySet();
            for (String key : keys) {
                filter.put(key, json.get(key));
            }
            List<StockItems> st = productDaoIntf.getStockItems(filter, userType);
            List<Long> stIds=new ArrayList<>();
            JSONArray d = new JSONArray();
            for (StockItems c : st) {
            	stIds.add(c.getStockItemId());
                d.add(ConvertJSONtoObject.pojoToJSON(c));
            }
            long sellerId=GeneralUtil.objTolong(json.get("seller_id"));
            Map<Long,JSONArray> pk=productDaoIntf.getPackSizes(stIds,sellerId);
            for(int i=0;i<d.size();i++)
            {
            	JSONObject j=(JSONObject) d.get(i);
            	if(pk.containsKey(GeneralUtil.objTolong(j.get("stockItemId"))))
            	{
            		j.put("packSize", pk.get(GeneralUtil.objTolong(j.get("stockItemId"))));
            	}
            	array.add(j);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return array;
    }
    
    public File exportStockItems(JSONObject json) {
       try {
            Map<String, Object> filter = new HashMap<>();
            Set<String> keys = json.keySet();
            for (String key : keys) {
                filter.put(key, json.get(key));
            }
            Map<String, Object> data = productDaoIntf.exportStockItems(filter,filter.containsKey("seller_id")?"seller":"admin" );
            return ExcelReadAndWrite.createExcelFile((JSONArray)data.get("headers"), (List<Object[]>)data.get("data"), "stock_item.xls");
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public JSONObject getPackOptions(JSONObject json) {
    	return productDaoIntf.getPackOptions( json);
    }

	
    
    
    

}
