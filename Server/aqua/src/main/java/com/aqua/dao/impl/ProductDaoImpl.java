/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aqua.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.aqua.dao.intf.ProductDaoIntf;
import com.aqua.pojo.Brands;
import com.aqua.pojo.Category;
import com.aqua.pojo.SellerSPPrice;
import com.aqua.pojo.SellerStockMap;
import com.aqua.pojo.StockItems;
import com.aqua.pojo.StockPackPrice;
import com.aqua.pojo.SubCategory;
import com.aqua.util.ConvertJSONtoObject;
import com.aqua.util.GeneralUtil;

/**
 *
 * @author USER
 */
@Repository
public class ProductDaoImpl implements ProductDaoIntf {

	private static final Logger logger = LogManager.getLogger(ProductDaoImpl.class);
	@Autowired
	private EntityManager entityManager;

	@Override
	public List<Category> getCategories(long id, String name, Date date) {
		List<Category> data = new ArrayList<>();
		try {
			String sql = "From Category a where 1=1 ";
			if (id > 0) {
				sql += " and a.categoryId=" + id;
			}
			if (name != null && name.trim().length() > 0) {
				sql += " and lower(a.name)=:name ";
			}
			if (date != null) {
				sql += " and (a.createdOn>:syncdate or a.updatedOn>:syncdate)";
			}
			sql += " order by a.name ";
			Query q = entityManager.createQuery(sql);
			if (name != null && name.trim().length() > 0) {
				q.setParameter("name", name.toLowerCase());
			}
			if (date != null) {
				q.setParameter("syncdate", date);
			}
			data = q.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public List<Category> checkCategories(long id, String name) {
		List<Category> data = new ArrayList<>();
		try {
			String sql = "From Category a where 1=1 ";
			if (id > 0) {
				sql += " and a.categoryId!=" + id;
			}
			if (name != null && name.trim().length() > 0) {
				sql += " and lower(a.name)=:name ";
			}
			Query q = entityManager.createQuery(sql);
			if (name != null && name.trim().length() > 0) {
				q.setParameter("name", name.toLowerCase());
			}
			data = q.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public JSONObject addCategory(JSONObject json) {
		JSONObject result = new JSONObject();
		String reason = "";
		try {
			Category cat = new Category();
			ConvertJSONtoObject.jsonToObject(cat, json);
			List<Category> ls = getCategories(0, cat.getName(), null);
			if (ls.isEmpty()) {
				cat.setCategoryId(GeneralUtil.getPrimaryKey("SEQ_CATEGORY", entityManager));
				cat.setStatus("active");
				if (((JSONObject) json.get("image")).get("value") != null
						&& ((JSONObject) json.get("image")).get("value").toString().length() > 0) {
					String image = GeneralUtil.writeImageFolder(
							((JSONObject) json.get("image")).get("value").toString(), "mycart/category",
							cat.getCategoryId() + "");
					cat.setImage(image);
				}
				entityManager.persist(cat);
				reason = "success";
				result = ConvertJSONtoObject.pojoToJSON(cat);
			} else {
				reason = "Category already exist";
			}
		} catch (Exception e) {
			e.printStackTrace();
			reason = "Failed";
		}
		result.put("reason", reason);
		return result;
	}

	public JSONObject updateCategory(JSONObject json) {
		JSONObject result = new JSONObject();
		String reason = "";
		try {
			Category cat = new Category();
			ConvertJSONtoObject.jsonToObject(cat, json);
			List<Category> ls = checkCategories(cat.getCategoryId(), cat.getName());
			if (ls.isEmpty()) {
				Category c = getCategories(cat.getCategoryId(), null, null).get(0);
				c.setOrderBy(cat.getOrderBy());
				c.setName(cat.getName());
				c.setUpdatedBy(cat.getUpdatedBy());
				c.setUpdatedOn(cat.getUpdatedOn());
				if (((JSONObject) json.get("image")).get("value") != null
						&& ((JSONObject) json.get("image")).get("value").toString().length() > 0) {
					String image = GeneralUtil.writeImageFolder(
							((JSONObject) json.get("image")).get("value").toString(), "mycart/category",
							cat.getCategoryId() + "");
					c.setImage(image);
				}
				entityManager.persist(c);
				reason = "success";
				result = ConvertJSONtoObject.pojoToJSON(cat);
			} else {
				reason = "Category already exist";
			}
		} catch (Exception e) {
			e.printStackTrace();
			reason = "Failed";
		}
		result.put("reason", reason);
		return result;
	}

	public List<SubCategory> getSubCategories(long id, long catId, String name, Date date) {
		List<SubCategory> data = new ArrayList<>();
		try {
			String sql = "select a.subCategoryId,a.name,a.categoryId,b.name,a.image,a.status From SubCategory a,Category b where a.categoryId=b.categoryId ";
			if (id > 0) {
				sql += " and a.subCategoryId=" + id;
			}
			if (catId > 0) {
				sql += " and a.categoryId=" + catId;
			}
			if (name != null && name.trim().length() > 0) {
				sql += " and a.name=:name ";
			}
			if (date != null) {
				sql += " and (a.createdOn>:syncdate or a.updatedOn>:syncdate)";
			}
			sql += " order by a.name ";
			Query q = entityManager.createQuery(sql);
			if (name != null && name.trim().length() > 0) {
				q.setParameter("name", name);
			}
			if (date != null) {
				q.setParameter("syncdate", date);
			}
			List<Object[]> ls = q.getResultList();
			for (Object[] o : ls) {
				SubCategory sub = new SubCategory();
				sub.setSubCategoryId(GeneralUtil.objTolong(o[0]));
				sub.setName(GeneralUtil.objToString(o[1]));
				sub.setCategoryId(GeneralUtil.objTolong(o[2]));
				sub.setCatName(GeneralUtil.objToString(o[3]));
				sub.setImage(GeneralUtil.objToString(o[4]));
				sub.setStatus(GeneralUtil.objToString(o[5]));
				data.add(sub);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public JSONObject addSubCategory(JSONObject json) {
		JSONObject result = new JSONObject();
		String reason = "";
		try {
			SubCategory cat = new SubCategory();
			ConvertJSONtoObject.jsonToObject(cat, json);
			List<SubCategory> ls = checkSubCategories(0, 0, cat.getCategoryId(), cat.getName());
			if (ls.isEmpty()) {
				cat.setSubCategoryId(GeneralUtil.getPrimaryKey("SEQ_SUB_CATEGORY", entityManager));
				cat.setStatus("active");
				if (((JSONObject) json.get("image")).get("value") != null
						&& ((JSONObject) json.get("image")).get("value").toString().length() > 0) {
					String image = GeneralUtil.writeImageFolder(
							((JSONObject) json.get("image")).get("value").toString(), "mycart/subcategory",
							cat.getSubCategoryId() + "");
					cat.setImage(image);
				}
				entityManager.persist(cat);
				reason = "success";
				result = ConvertJSONtoObject.pojoToJSON(cat);
			} else {
				reason = "Sub Category already exist";
			}
		} catch (Exception e) {
			e.printStackTrace();
			reason = "Failed";
		}
		result.put("reason", reason);
		return result;
	}

	public List<SubCategory> checkSubCategories(long id, long notId, long catId, String name) {
		List<SubCategory> data = new ArrayList<>();
		try {
			String sql = "From SubCategory a where 1=1 ";
			if (id > 0) {
				sql += " and a.subCategoryId=" + id;
			}
			if (notId > 0) {
				sql += " and a.subCategoryId!=" + notId;
			}
			if (catId > 0) {
				sql += " and a.categoryId=" + catId;
			}
			if (name != null && name.trim().length() > 0) {
				sql += " and lower(a.name)=:name ";
			}
			Query q = entityManager.createQuery(sql);
			if (name != null && name.trim().length() > 0) {
				q.setParameter("name", name.toLowerCase());
			}
			data = q.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public JSONObject updateSubCategory(JSONObject json) {
		JSONObject result = new JSONObject();
		String reason = "";
		try {
			SubCategory cat = new SubCategory();
			ConvertJSONtoObject.jsonToObject(cat, json);
			List<SubCategory> ls = checkSubCategories(0, cat.getSubCategoryId(), cat.getCategoryId(), cat.getName());
			if (ls.isEmpty()) {
				SubCategory c = checkSubCategories(cat.getSubCategoryId(), 0, 0, null).get(0);
				c.setCategoryId(cat.getCategoryId());
				c.setName(cat.getName());
				c.setUpdatedBy(cat.getUpdatedBy());
				c.setUpdatedOn(cat.getUpdatedOn());
				if (((JSONObject) json.get("image")).get("value") != null
						&& ((JSONObject) json.get("image")).get("value").toString().length() > 0) {
					String image = GeneralUtil.writeImageFolder(
							((JSONObject) json.get("image")).get("value").toString(), "mycart/subcategory",
							cat.getSubCategoryId() + "");
					c.setImage(image);
				}
				entityManager.persist(c);
				reason = "success";
				result = ConvertJSONtoObject.pojoToJSON(cat);
			} else {
				reason = "Category already exist";
			}
		} catch (Exception e) {
			e.printStackTrace();
			reason = "Failed";
		}
		result.put("reason", reason);
		return result;
	}

	@Override
	public List<Brands> getBrands(long id, long catId, long subCatId, String name, Date date) {
		List<Brands> data = new ArrayList<>();
		try {
			String sql = "select c.brandId,c.name,a.subCategoryId, a.name,b.categoryId,b.name,c.image,c.status From "
					+ " Brands c,SubCategory a,Category b where a.categoryId=b.categoryId and c.subCategoryId=a.subCategoryId";
			if (id > 0) {
				sql += " and a.subCategoryId=" + id;
			}
			if (catId > 0) {
				sql += " and a.categoryId=" + catId;
			}
			if (subCatId > 0) {
				sql += " and a.subCategoryId=" + subCatId;
			}
			if (name != null && name.trim().length() > 0) {
				sql += " and a.name=:name ";
			}
			if (date != null) {
				sql += " and (a.createdOn>:syncdate or a.updatedOn>:syncdate)";
			}
			sql += " order by a.name ";
			Query q = entityManager.createQuery(sql);
			if (name != null && name.trim().length() > 0) {
				q.setParameter("name", name);
			}
			if (date != null) {
				q.setParameter("syncdate", date);
			}
			List<Object[]> ls = q.getResultList();
			for (Object[] o : ls) {
				Brands sub = new Brands();
				sub.setBrandId(GeneralUtil.objTolong(o[0]));
				sub.setName(GeneralUtil.objToString(o[1]));
				sub.setSubCategoryId(GeneralUtil.objTolong(o[2]));
				sub.setSubCatName(GeneralUtil.objToString(o[3]));
				sub.setCategoryId(GeneralUtil.objTolong(o[4]));
				sub.setCatName(GeneralUtil.objToString(o[5]));
				sub.setImage(GeneralUtil.objToString(o[6]));
				sub.setStatus(GeneralUtil.objToString(o[7]));
				data.add(sub);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	@Override
	public JSONObject addBrands(JSONObject json) {
		JSONObject result = new JSONObject();
		String reason = "";
		try {
			Brands cat = new Brands();
			ConvertJSONtoObject.jsonToObject(cat, json);
			List<Brands> ls = checkBrands(0, 0, cat.getSubCategoryId(), 0, cat.getName());
			if (ls.isEmpty()) {
				cat.setBrandId(GeneralUtil.getPrimaryKey("SEQ_BRANDS", entityManager));
				cat.setStatus("active");
				if (((JSONObject) json.get("image")).get("value") != null
						&& ((JSONObject) json.get("image")).get("value").toString().length() > 0) {
					String image = GeneralUtil.writeImageFolder(
							((JSONObject) json.get("image")).get("value").toString(), "mycart/brand",
							cat.getBrandId() + "");
					cat.setImage(image);
				}
				entityManager.persist(cat);
				reason = "success";
				result = ConvertJSONtoObject.pojoToJSON(cat);
			} else {
				reason = "Brand already exist";
			}
		} catch (Exception e) {
			e.printStackTrace();
			reason = "Failed";
		}
		result.put("reason", reason);
		return result;
	}

	public List<Brands> checkBrands(long id, long notId, long subCatId, long catId, String name) {
		List<Brands> data = new ArrayList<>();
		try {
			String sql = "From Brands a where 1=1 ";
			if (id > 0) {
				sql += " and a.brandId=" + id;
			}
			if (notId > 0) {
				sql += " and a.brandId!=" + notId;
			}

			if (subCatId > 0) {
				sql += " and a.subCategoryId=" + subCatId;
			}

			if (catId > 0) {
				sql += " and a.categoryId=" + catId;
			}
			if (name != null && name.trim().length() > 0) {
				sql += " and lower(a.name)=:name ";
			}
			Query q = entityManager.createQuery(sql);
			if (name != null && name.trim().length() > 0) {
				q.setParameter("name", name.toLowerCase());
			}
			data = q.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	@Override
	public JSONObject updateBrands(JSONObject json) {
		JSONObject result = new JSONObject();
		String reason = "";
		try {
			Brands cat = new Brands();
			ConvertJSONtoObject.jsonToObject(cat, json);
			List<Brands> ls = checkBrands(0, cat.getBrandId(), cat.getSubCategoryId(), 0, cat.getName());
			if (ls.isEmpty()) {
				Brands c = checkBrands(cat.getBrandId(), 0, 0, 0, null).get(0);
				c.setSubCategoryId(cat.getSubCategoryId());
				c.setCategoryId(cat.getCategoryId());
				c.setName(cat.getName());
				c.setUpdatedBy(cat.getUpdatedBy());
				c.setUpdatedOn(cat.getUpdatedOn());
				if (((JSONObject) json.get("image")).get("value") != null
						&& ((JSONObject) json.get("image")).get("value").toString().length() > 0) {
					String image = GeneralUtil.writeImageFolder(
							((JSONObject) json.get("image")).get("value").toString(), "mycart/brand",
							cat.getBrandId() + "");
					c.setImage(image);
				}
				entityManager.persist(c);
				reason = "success";
				result = ConvertJSONtoObject.pojoToJSON(cat);
			} else {
				reason = "Brand already exist";
			}
		} catch (Exception e) {
			e.printStackTrace();
			reason = "Failed";
		}
		result.put("reason", reason);
		return result;
	}

	public List<StockItems> getStockItems(Map<String, Object> filters, String userType) {
		List<StockItems> data = new ArrayList<>();
		try {
			String sql = "select a.stock_item_id,a.code,a.name,a.description,a.image,a.status,a.uom";
			sql += userType.equalsIgnoreCase("seller") ? ",decode(g.price,0,a.price,g.price) price" : ",a.price";
			sql += ",a.pack_Size, b.category_id,b.name vt_name,c.sub_cat_id,c.name suct_name, d.brand_id,d.name br_name,a.seller_id,v.pack_size_1,v.price_1,v.pack_size_2,v.price_2,v.pack_size_3,v.price_3,v.pack_size_4,v.price_4 From "
					+ " stock_items a,category b,Sub_Category c,Brands d ";
			if (userType.equalsIgnoreCase("seller")) {
				sql += " ,seller_stock_map g,view_seller_st_packes v ";
			}else {
				sql+=" ,view_st_packs v ";
			}
			
			sql += " where  a.category_Id=b.category_Id " + " and a.sub_Cat_Id=c.sub_Cat_Id "
					+ " and a.brand_Id=d.brand_Id(+) ";
			if (userType.equalsIgnoreCase("seller")) {
				sql += " and v.stock_item_id=a.stock_item_id and v.seller_id=g.seller_id and  a.stock_item_id=g.stock_item_id and g.seller_id=" + filters.get("seller_id").toString();
			}
			else
			{
				sql+=" and v.stock_item_id=a.stock_item_id ";
			}

			if (filters.containsKey("id")) {
				sql += " and a.stock_item_id=" + filters.get("id").toString();
			}
			if (filters.containsKey("categoryId")) {
				sql += " and a.category_Id=" + filters.get("categoryId").toString();
			}
			if (filters.containsKey("subCategoryId")) {
				sql += " and a.sub_Cat_Id=" + filters.get("subCategoryId").toString();
			}
			if (filters.containsKey("brandId")) {
				sql += " and a.brand_Id=" + filters.get("brandId").toString();
			}
			if (filters.containsKey("name")) {
				sql += " and (lower(a.name) like :name||'%' or lower(a.code) like :name||'%') ";
			}
			Date date = null;
			if (filters.containsKey("syncdate")) {
				try {
					date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(filters.get("syncdate").toString());
				} catch (Exception e) {
				}
			}
			if (date != null) {
				sql += " and (a.createdOn>:syncdate or a.updatedOn>:syncdate)";
			}
			sql += " order by a.name ";
			logger.info("SQl " + sql);
			Query q = entityManager.createNativeQuery(sql);
			if (filters.containsKey("name")) {
				q.setParameter("name", filters.get("name").toString());
			}

			if (date != null) {
				q.setParameter("syncdate", date);
			}
			int pageFirst = 0;
			int pagesize = 0;
			if (filters.containsKey("first")) {
				pageFirst = Integer.parseInt(filters.get("first").toString());
			}
			if (filters.containsKey("size")) {
				pagesize = Integer.parseInt(filters.get("size").toString());
			}
			if (pagesize > 0) {
				q.setFirstResult(pageFirst).setMaxResults(pagesize);
			}
			List<Object[]> ls = q.getResultList();
			for (Object[] o : ls) {
				StockItems sub = new StockItems();
				int i = 0;
				sub.setStockItemId(GeneralUtil.objTolong(o[i++]));
				sub.setCode(GeneralUtil.objToString(o[i++]));
				sub.setName(GeneralUtil.objToString(o[i++]));
				sub.setDescription(GeneralUtil.objToString(o[i++]));
				sub.setImage(GeneralUtil.objToString(o[i++]));
				sub.setStatus(GeneralUtil.objToString(o[i++]));
				sub.setUom(GeneralUtil.objToString(o[i++]));
				sub.setPrice(GeneralUtil.objToDouble(o[i++]));
				sub.setPackSize(GeneralUtil.objToInt(o[i++]));
				sub.setCategoryId(GeneralUtil.objTolong(o[i++]));
				sub.setCatName(GeneralUtil.objToString(o[i++]));
				sub.setSubCategoryId(GeneralUtil.objTolong(o[i++]));
				sub.setSubCatName(GeneralUtil.objToString(o[i++]));
				sub.setBrandId(GeneralUtil.objTolong(o[i++]));
				sub.setBrandName(GeneralUtil.objToString(o[i++]));
				sub.setSellerId(GeneralUtil.objTolong(o[i++]));
				data.add(sub);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	@Override
	public JSONObject addStockItems(JSONObject json) {
		JSONObject result = new JSONObject();
		String reason = "";
		try {
			StockItems cat = new StockItems();
			ConvertJSONtoObject.jsonToObject(cat, json);
			List<StockItems> ls = checkStockItems(0, 0, cat.getCode());
			if (ls.isEmpty()) {
				cat.setStockItemId(GeneralUtil.getPrimaryKey("SEQ_STOCK_ITEMS", entityManager));
				cat.setStatus("active");
				List<StockPackPrice> spml = new ArrayList<>();
				JSONArray a = (JSONArray) ((JSONObject) json.get("packSize")).get("value");
				for (int i = 0; i < a.size(); i++) {
					StockPackPrice sp = new StockPackPrice();
					JSONObject j = (JSONObject) a.get(i);
					sp.setPackConfigId(GeneralUtil.objTolong(j.get("key")));
					sp.setDefaultPack(GeneralUtil.objToInt(j.get("default_pack")));
					sp.setPrice(GeneralUtil.objToDouble(j.get("price")));
					sp.setStockItemId(cat.getStockItemId());
					sp.setStockPackPriceId(GeneralUtil.getPrimaryKey("SEQ_STOCK_PACK_PRICE", entityManager));
					spml.add(sp);
					if (sp.getDefaultPack() == 1) {
						cat.setUom(GeneralUtil.objToString(j.get("name")));
						cat.setPackSize(GeneralUtil.objToDouble(j.get("quantity")));
						cat.setPrice(sp.getPrice());
					}

				}

				if (((JSONObject) json.get("image")).get("value") != null
						&& ((JSONObject) json.get("image")).get("value").toString().length() > 0) {
					String image = GeneralUtil.writeImageFolder(
							((JSONObject) json.get("image")).get("value").toString(),
							"mycart/stock/" + cat.getSubCategoryId(), cat.getStockItemId() + "");
					cat.setImage(image);
				}
				if (json.containsKey("seller_id")
						&& GeneralUtil.objTolong(((JSONObject) json.get("seller_id")).get("value")) > 0) {
					SellerStockMap s = new SellerStockMap();
					s.setSellerStockMapId(GeneralUtil.getPrimaryKey("SEQ_SELLER_STOCK_MAP", entityManager));
					s.setSellerId(GeneralUtil.objTolong(((JSONObject) json.get("seller_id")).get("value")));
					s.setPrice(cat.getPrice());
					s.setStockItemId(cat.getStockItemId());
					cat.setSellerId(s.getSellerId());
					entityManager.persist(s);
					JSONArray a1 = (JSONArray) ((JSONObject) json.get("packSize")).get("value");
					for (int i = 0; i < a1.size(); i++) {
						SellerSPPrice sp = new SellerSPPrice();
						JSONObject j = (JSONObject) a1.get(i);
						sp.setPackConfigId(GeneralUtil.objTolong(j.get("key")));
						sp.setDefaultPack(GeneralUtil.objToInt(j.get("default_pack")));
						sp.setPrice(GeneralUtil.objToDouble(j.get("price")));
						sp.setStockItemId(cat.getStockItemId());
						sp.setSellerSPPriceId(GeneralUtil.getPrimaryKey("SEQ_SELLER_SP_PRICE", entityManager));
						sp.setSellerStockMapId(s.getSellerStockMapId());
						entityManager.persist(sp);
					}
				}
                String hr=cat.getCategoryId()+"@"+cat.getSubCategoryId()+"@";
                if(cat.getBrandId()>0)
                	hr+=cat.getBrandId()+"@";
                cat.setHierarchy(hr);
				entityManager.persist(cat);
				for (StockPackPrice sp : spml) {
					entityManager.persist(sp);
				}
				reason = "success";
				result = ConvertJSONtoObject.pojoToJSON(cat);
			} else {
				reason = "Stock code already exist";
			}
		} catch (Exception e) {
			e.printStackTrace();
			reason = "Failed";
		}
		result.put("reason", reason);
		return result;
	}

	public List<StockItems> checkStockItems(long id, long notId, String code) {
		List<StockItems> data = new ArrayList<>();
		try {
			String sql = "From StockItems a where 1=1 ";
			if (id > 0) {
				sql += " and a.stockItemId=" + id;
			}
			if (notId > 0) {
				sql += " and a.stockItemId!=" + notId;
			}

			if (code != null && code.trim().length() > 0) {
				sql += " and lower(a.code)=:code ";
			}
			Query q = entityManager.createQuery(sql);
			if (code != null && code.trim().length() > 0) {
				q.setParameter("code", code.toLowerCase());
			}
			data = q.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	@Override
	public JSONObject updateStockItems(JSONObject json) {
		JSONObject result = new JSONObject();
		String reason = "";
		try {
			StockItems cat = new StockItems();
			ConvertJSONtoObject.jsonToObject(cat, json);
			List<StockItems> ls = checkStockItems(0, cat.getStockItemId(), cat.getCode());
			if (ls.isEmpty()) {
				StockItems c = checkStockItems(cat.getStockItemId(), 0, null).get(0);
				c.setDescription(cat.getDescription());
				c.setSubCategoryId(cat.getSubCategoryId());
				c.setCategoryId(cat.getCategoryId());
				c.setBrandId(cat.getBrandId());
				c.setName(cat.getName());
				c.setUpdatedBy(cat.getUpdatedBy());
				c.setUpdatedOn(cat.getUpdatedOn());
				if (((JSONObject) json.get("image")).get("value") != null
						&& ((JSONObject) json.get("image")).get("value").toString().length() > 0) {
					String image = GeneralUtil.writeImageFolder(
							((JSONObject) json.get("image")).get("value").toString(),
							"mycart/stock/" + cat.getSubCategoryId(), cat.getStockItemId() + "");
					c.setImage(image);
				}
				String ds="delete from stock_pack_price a where a.stock_item_id="+c.getStockItemId();
				entityManager.createNativeQuery(ds).executeUpdate();
				
				List<StockPackPrice> spml = new ArrayList<>();
				JSONArray a = (JSONArray) ((JSONObject) json.get("packSize")).get("value");
				for (int i = 0; i < a.size(); i++) {
					StockPackPrice sp = new StockPackPrice();
					JSONObject j = (JSONObject) a.get(i);
					sp.setPackConfigId(GeneralUtil.objTolong(j.get("key")));
					sp.setDefaultPack(GeneralUtil.objToInt(j.get("default_pack")));
					sp.setPrice(GeneralUtil.objToDouble(j.get("price")));
					sp.setStockItemId(cat.getStockItemId());
					sp.setStockPackPriceId(GeneralUtil.getPrimaryKey("SEQ_STOCK_PACK_PRICE", entityManager));
					spml.add(sp);
					if (sp.getDefaultPack() == 1) {
						c.setUom(GeneralUtil.objToString(j.get("name")));
						c.setPackSize(GeneralUtil.objToDouble(j.get("quantity")));
						c.setPrice(sp.getPrice());
					}

				}
				 String hr=cat.getCategoryId()+"@"+cat.getSubCategoryId()+"@";
	                if(cat.getBrandId()>0)
	                	hr+=cat.getBrandId()+"@";
	                cat.setHierarchy(hr);
				entityManager.persist(c);
				for (StockPackPrice sp : spml) {
					entityManager.persist(sp);
				}
				if (json.containsKey("seller_id")
						&& GeneralUtil.objTolong(((JSONObject) json.get("seller_id")).get("value")) > 0) {
					String sm="select b.seller_stock_map_id from seller_stock_map b where b.seller_id="+GeneralUtil.objTolong(((JSONObject) json.get("seller_id")).get("value"))+" and b.stock_item_id="+c.getStockItemId();
					List<Object> ls1=entityManager.createNativeQuery(sm).getResultList();
					long id=0;
					if(!ls1.isEmpty())
						id=GeneralUtil.objTolong(ls1.get(0));
					ds="delete from seller_sp_price a where a.seller_stock_map_id ="+id;
					entityManager.createNativeQuery(ds).executeUpdate();
					JSONArray a1 = (JSONArray) ((JSONObject) json.get("packSize")).get("value");
					for (int i = 0; i < a1.size(); i++) {
						SellerSPPrice sp = new SellerSPPrice();
						JSONObject j = (JSONObject) a1.get(i);
						sp.setPackConfigId(GeneralUtil.objTolong(j.get("key")));
						sp.setDefaultPack(GeneralUtil.objToInt(j.get("default_pack")));
						sp.setPrice(GeneralUtil.objToDouble(j.get("price")));
						sp.setStockItemId(cat.getStockItemId());
						sp.setSellerSPPriceId(GeneralUtil.getPrimaryKey("SEQ_SELLER_SP_PRICE", entityManager));
						sp.setSellerStockMapId(id);
						entityManager.persist(sp);
					}
				}
				
				reason = "success";
				result = ConvertJSONtoObject.pojoToJSON(cat);
			} else {
				reason = "Brand already exist";
			}
		} catch (Exception e) {
			e.printStackTrace();
			reason = "Failed";
		}
		result.put("reason", reason);
		return result;
	}

	@Override
	public Map<String, Object> exportStockItems(Map<String, Object> filters, String userType) {
		Map<String, Object> data = new HashMap<>();
		try {
			String sql = "select a.stock_item_id,a.code,a.name,a.description,a.image,a.status,a.uom";
			sql += userType.equalsIgnoreCase("seller") ? ",decode(g.price,0,a.price,g.price) price" : ",a.price";
			sql += ",a.pack_Size, b.category_id,b.name vt_name,c.sub_cat_id,c.name suct_name, d.brand_id,d.name br_name,v.pack_size_1,v.price_1,v.pack_size_2,v.price_2,v.pack_size_3,v.price_3,v.pack_size_4,v.price_4 From "
					+ " stock_items a,category b,Sub_Category c,Brands d ";
			if (userType.equalsIgnoreCase("seller")) {
				sql += " ,seller_stock_map g,view_seller_st_packes v ";
			}else {
				sql+=" ,view_st_packs v ";
			}
			
			sql += " where  a.category_Id=b.category_Id " + " and a.sub_Cat_Id=c.sub_Cat_Id "
					+ " and a.brand_Id=d.brand_Id(+) ";
			if (userType.equalsIgnoreCase("seller")) {
				sql += " and v.stock_item_id=a.stock_item_id and v.seller_id=g.seller_id and  a.stock_item_id=g.stock_item_id and g.seller_id=" + filters.get("seller_id").toString();
			}
			else
			{
				sql+=" and v.stock_item_id=a.stock_item_id ";
			}

			if (filters.containsKey("id")) {
				sql += " and a.stock_item_id=" + filters.get("id").toString();
			}
			if (filters.containsKey("categoryId")) {
				sql += " and a.category_Id=" + filters.get("categoryId").toString();
			}
			if (filters.containsKey("subCategoryId")) {
				sql += " and a.sub_Cat_Id=" + filters.get("subCategoryId").toString();
			}
			if (filters.containsKey("brandId")) {
				sql += " and a.brand_Id=" + filters.get("brandId").toString();
			}
			if (filters.containsKey("name")) {
				sql += " and (lower(a.name) like :name||'%' or lower(a.code) like :name||'%') ";
			}
			Date date = null;
			if (filters.containsKey("syncdate")) {
				try {
					date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(filters.get("syncdate").toString());
				} catch (Exception e) {
				}
			}
			if (date != null) {
				sql += " and (a.createdOn>:syncdate or a.updatedOn>:syncdate)";
			}
			sql += " order by a.name ";
			logger.info("SQl " + sql);
			Query q = entityManager.createNativeQuery(sql);
			if (filters.containsKey("name")) {
				q.setParameter("name", filters.get("name").toString());
			}

			if (date != null) {
				q.setParameter("syncdate", date);
			}
			int pageFirst = 0;
			int pagesize = 0;
			if (filters.containsKey("first")) {
				pageFirst = Integer.parseInt(filters.get("first").toString());
			}
			if (filters.containsKey("size")) {
				pagesize = Integer.parseInt(filters.get("size").toString());
			}
			if (pagesize > 0) {
				q.setFirstResult(pageFirst).setMaxResults(pagesize);
			}
			JSONArray columns = GeneralUtil.getQueryColumnsNoLower(sql, entityManager, null);
			List<Object[]> ls = q.getResultList();
			data.put("headers", columns);
			data.put("data", ls);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public JSONObject getPackOptions(JSONObject json) {
		JSONObject result = new JSONObject();
		try {
			String sql = "select a.pack_name  name,a.pack_config_id key,0 price,0  default_pack, a.quantity   from pack_config a where 1=1 order by quantity ";
			JSONArray columns = GeneralUtil.getQueryColumns(sql, entityManager, null);
			List<Object[]> ls = entityManager.createNativeQuery(sql).getResultList();

			JSONArray data = GeneralUtil.resultToJSONArray(columns, ls, "quantity,price");
			result.put("options", data);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Map<Long, JSONArray> getPackSizes(List<Long> itemIds,long sellerId) {
		Map<Long, JSONArray> result = new HashMap<>();
		try {
			String sql = "";
			if (sellerId==0) {
				sql = " select a.stock_item_id,c.pack_name name,c.pack_config_id key,b.price,b.default_pack,c.quantity,b.stock_pack_price_id from stock_items a,stock_pack_price b,pack_config c "
						+ " where a.stock_item_id=b.stock_item_id and b.pack_config_id=c.pack_config_id ";
			} else {
				sql = "select a.stock_item_id,c.pack_name name,c.pack_config_id key,b.price,b.default_pack,c.quantity,b.seller_sp_price_id from stock_items a,seller_stock_map d,seller_sp_price b,pack_config c "
						+ " where a.stock_item_id=b.stock_item_id and d.stock_item_id=a.stock_item_id and d.seller_stock_map_id=b.seller_stock_map_id  and b.pack_config_id=c.pack_config_id and d.seller_id="
						+ sellerId;
			}
			if (itemIds != null && itemIds.size() > 0) {
				sql += " and a.stock_item_id in (:item_Ids) ";
			}
			Query q = entityManager.createNativeQuery(sql);
			if (itemIds != null && itemIds.size() > 0) {
				q.setParameter("item_Ids", itemIds);
			}
			logger.info("SQl "+sql);
			JSONArray columns = GeneralUtil.getQueryColumns(sql, entityManager, null);
			List<Object[]> ls = q.getResultList();
			JSONArray data = GeneralUtil.resultToJSONArray(columns, ls, "quantity,price");
			for (int i = 0; i < data.size(); i++) {
				JSONObject j = (JSONObject) data.get(i);
				long key = GeneralUtil.objTolong(j.get("stock_item_id"));
				if (result.containsKey(key))
					result.get(key).add(j);
				else {
					JSONArray a = new JSONArray();
					a.add(j);
					result.put(key, a);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
