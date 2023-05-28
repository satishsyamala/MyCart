/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cart.dao.impl;

import com.cart.dao.intf.ImportDataDaoIntf;
import com.cart.file.ReadJsonFile;
import com.cart.pojo.Brands;
import com.cart.pojo.Category;
import com.cart.pojo.ImportData;
import com.cart.pojo.PackConfig;
import com.cart.pojo.SellerSPPrice;
import com.cart.pojo.SellerStockMap;
import com.cart.pojo.StockImages;
import com.cart.pojo.StockItems;
import com.cart.pojo.StockPackPrice;
import com.cart.pojo.SubCategory;
import com.cart.util.ConvertJSONtoObject;
import com.cart.util.GeneralUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Branch;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author USER
 */
@Repository
public class ImportDataDaoImpl implements ImportDataDaoIntf {

	private static final Logger logger = LogManager.getLogger(ImportDataDaoImpl.class);
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private ProductDaoImpl productDaoImpl;

	public List<ImportData> getImportData(JSONObject json) {
		List<ImportData> data = new ArrayList<>();
		try {
			String sql = "From ImportData a where 1=1 ";
			if (json.containsKey("module")) {
				sql += " and a.moduleKey='" + json.get("module").toString() + "'";
			}
			if (json.containsKey("fromdate") && json.containsKey("todate")) {
				sql += " and date_trunc('day',a.importedOn) between :fromdate and :todate";
			}
			if (json.containsKey("seller_id") && GeneralUtil.objTolong(json.get("seller_id")) > 0) {
				sql += " and a.sellerId=" + GeneralUtil.objTolong(json.get("seller_id"));
			}
			sql += " order by a.importedOn desc";
			logger.info("sql : " + sql);
			Query q = entityManager.createQuery(sql);
			if (json.containsKey("fromdate") && json.containsKey("todate")) {
				q.setParameter("fromdate", GeneralUtil.objToDate(json.containsKey("fromdate")));
				q.setParameter("todate", GeneralUtil.objToDate(json.containsKey("todate")));
			}
			int pageFirst = 0;
			int pagesize = 0;
			if (json.containsKey("first")) {
				pageFirst = Integer.parseInt(json.get("first").toString());
			}
			if (json.containsKey("size")) {
				pagesize = Integer.parseInt(json.get("size").toString());
			}
			if (pagesize > 0) {
				q.setFirstResult(pageFirst).setMaxResults(pagesize);
			}

			data = q.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public JSONObject uploadFile(JSONObject json) {
		JSONObject res = new JSONObject();
		String reasom = "";
		try {
			ImportData im = new ImportData();
			im.setImportId(GeneralUtil.getPrimaryKey("SEQ_IMPORT_DATA", entityManager));
			im.setModule(json.get("module").toString());
			im.setModuleKey(json.get("modulekey").toString());
			im.setImportedBy(json.get("uername").toString());
			im.setSellerId(GeneralUtil.objTolong(json.get("sellerId")));
			im.setImportedOn(GeneralUtil.objToDateTime(json.get("uplodated")));
			im.setStatus("pending");
			String path = GeneralUtil.writeExcelFile(json.get("file").toString(), "mycart/imports/pending/",
					im.getModule() + "_" + im.getImportId());
			im.setFilePath(path);
			entityManager.persist(im);
			reasom = "success";
		} catch (Exception e) {
			e.printStackTrace();
			reasom = "fail";
		}
		res.put("reason", reasom);
		return res;
	}

	public JSONObject stockImage(JSONObject json) {
		JSONObject res = new JSONObject();
		String reasom = "";
		try {
			StockImages im = new StockImages();
			im.setStockImageId(GeneralUtil.getPrimaryKey("SEQ_STOCK_IMAGES", entityManager));
			im.setStockItemId(GeneralUtil.objTolong(json.get("stock_item_id")));

			if (json.get("image") != null && json.get("image").toString().length() > 0) {
				String image = GeneralUtil.writeImageFolder(json.get("image").toString(),
						"mycart/stockimages/" + im.getStockItemId(), im.getStockImageId() + "");
				im.setImage(image);
			}
			logger.info("sql : " + im.getStockItemId());
			logger.info("sql : " + im.getStockImageId());

			entityManager.persist(im);
			reasom = "success";
		} catch (Exception e) {
			e.printStackTrace();
			reasom = "fail";
		}
		res.put("reason", reasom);
		return res;
	}

	public ImportData getPendingImports() {
		ImportData importdata = null;
		try {
			String sql = "From ImportData a where a.status='pending' ";
			sql += " order by a.importedOn";
			List<ImportData> data = entityManager.createQuery(sql).setMaxResults(1).getResultList();
			if (!data.isEmpty()) {
				importdata = data.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return importdata;
	}

	public void updateImportDataStatus(ImportData importData) {
		importData.setCompletedOn(new Date());
		entityManager.merge(importData);
	}

	public String saveImportData(ImportData imp, List<String> row, JSONArray jsonArr) {
		if (imp.getModuleKey().equalsIgnoreCase("category")) {
			return saveCategory(imp, row, jsonArr);
		}
		if (imp.getModuleKey().equalsIgnoreCase("subcategory")) {
			return saveSubCategory(imp, row, jsonArr);
		}
		if (imp.getModuleKey().equalsIgnoreCase("brands")) {
			return saveBrand(imp, row, jsonArr);
		}
		if (imp.getModuleKey().equalsIgnoreCase("stockitems")) {
			return saveStock(imp, row, jsonArr);
		}
		if (imp.getModuleKey().equalsIgnoreCase("price")) {
			return savePrice(imp, row, jsonArr);
		} else {
			return "";
		}
	}

	public String savePrice(ImportData imp, List<String> row, JSONArray jsonArr) {
		String result = "";
		try {
			String sql = "select b.seller_stock_map_id from stock_items a,seller_stock_map b where a.stock_item_id=b.stock_item_id "
					+ " and a.code='" + row.get(0) + "' and b.seller_id=" + imp.getSellerId();
			logger.info("Sql : " + sql);
			List<Object> ls = entityManager.createNativeQuery(sql).getResultList();
			if (!ls.isEmpty()) {
				sql = "update seller_stock_map b set b.price=" + row.get(1);
				if (row.get(1) != null || row.get(2) != null) {

					sql += " where b.seller_stock_map_id=" + ls.get(0).toString();
					logger.info(sql);
					int w = entityManager.createNativeQuery(sql).executeUpdate();
					if (w > 0) {
						result = "success";
					} else {
						result = "failed";
					}
				} else {
					result = "Quantity or Price are required";
				}
			} else {
				result = "Item is not mapped";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String saveCategory(ImportData imp, List<String> row, JSONArray jsonArr) {
		String result = "";
		try {
			Category ca = new Category();
			ConvertJSONtoObject.ListObject(jsonArr, row, ca);
			List<Category> catl = productDaoImpl.checkCategories(0, ca.getName());
			if (catl.isEmpty()) {
				if (ca.getOrderBy().equalsIgnoreCase("Item") || ca.getOrderBy().equalsIgnoreCase("Cart")) {
					ca.setCategoryId(GeneralUtil.getPrimaryKey("SEQ_CATEGORY", entityManager));
					ca.setCreatedBy(imp.getImportedBy());
					ca.setCreatedOn(new Date());
					ca.setStatus("active");
					entityManager.persist(ca);
					result = "success";
				} else {
					result = "Order by must be either Cart or Itme";
				}
			} else {
				result = "Category " + ca.getName() + " already exist ";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String saveSubCategory(ImportData imp, List<String> row, JSONArray jsonArr) {
		String result = "";
		try {
			SubCategory ca = new SubCategory();
			ConvertJSONtoObject.ListObject(jsonArr, row, ca);
			List<Category> catl = productDaoImpl.checkCategories(0, ca.getCatName());
			if (!catl.isEmpty()) {
				List<SubCategory> sub = productDaoImpl.checkSubCategories(0, 0, catl.get(0).getCategoryId(),
						ca.getName());
				if (sub.isEmpty()) {
					ca.setSubCategoryId(GeneralUtil.getPrimaryKey("SEQ_SUB_CATEGORY", entityManager));
					ca.setCategoryId(catl.get(0).getCategoryId());
					ca.setCreatedBy(imp.getImportedBy());
					ca.setCreatedOn(new Date());
					ca.setStatus("active");
					entityManager.persist(ca);
					result = "success";
				} else {
					result = "Sub Category " + ca.getName() + " already exist";
				}
			} else {
				result = "Category " + ca.getCatName() + " not exist ";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String saveBrand(ImportData imp, List<String> row, JSONArray jsonArr) {
		String result = "";
		try {
			Brands ca = new Brands();
			ConvertJSONtoObject.ListObject(jsonArr, row, ca);
			List<Category> catl = productDaoImpl.checkCategories(0, ca.getCatName());
			if (!catl.isEmpty()) {
				List<SubCategory> sub = productDaoImpl.checkSubCategories(0, 0, catl.get(0).getCategoryId(),
						ca.getSubCatName());
				if (!sub.isEmpty()) {
					List<Brands> bra = productDaoImpl.checkBrands(0, 0, sub.get(0).getSubCategoryId(),
							catl.get(0).getCategoryId(), ca.getName());
					if (bra.isEmpty()) {
						ca.setBrandId(GeneralUtil.getPrimaryKey("SEQ_BRANDS", entityManager));
						ca.setCategoryId(catl.get(0).getCategoryId());
						ca.setSubCategoryId(sub.get(0).getSubCategoryId());
						ca.setCreatedBy(imp.getImportedBy());
						ca.setCreatedOn(new Date());
						ca.setStatus("active");
						entityManager.persist(ca);
						result = "success";
					} else {
						result = "Brand " + ca.getName() + " already exist";
					}
				} else {
					result = "Sub Category " + ca.getName() + " not exist";
				}
			} else {
				result = "Category " + ca.getCatName() + " not exist ";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String saveStock(ImportData imp, List<String> row, JSONArray jsonArr) {
		String result = "";
		try {
			StockItems ca = new StockItems();
			ConvertJSONtoObject.ListObject(jsonArr, row, ca);
			List<Category> catl = productDaoImpl.checkCategories(0, ca.getCatName());
			if (!catl.isEmpty()) {
				List<SubCategory> sub = productDaoImpl.checkSubCategories(0, 0, catl.get(0).getCategoryId(),
						ca.getSubCatName());
				if (!sub.isEmpty()) {
					int check = 0;
					long brId = 0;
					if (ca.getBrandName() == null || ca.getBrandName().length() == 0) {
						List<Brands> bra = productDaoImpl.checkBrands(0, 0, sub.get(0).getSubCategoryId(),
								catl.get(0).getCategoryId(), null);
						if (!bra.isEmpty()) {
							check = 1;
						}
					} else {
						List<Brands> bra = productDaoImpl.checkBrands(0, 0, sub.get(0).getSubCategoryId(),
								catl.get(0).getCategoryId(), ca.getBrandName());
						if (bra.isEmpty()) {
							check = 2;
						} else {
							brId = bra.get(0).getBrandId();
						}
					}
					List<StockPackPrice> ls = new ArrayList<>();
					if (ca.getPackSize1() != null && ca.getPackSize1().trim().length() > 0) {
						if (GeneralUtil.objToDouble(ca.getPrice1()) > 0) {
							PackConfig p = getPackConfig(ca.getPackSize1());
							if (p != null) {
								StockPackPrice sp = new StockPackPrice();
								sp.setPackConfigId(p.getPackConfigId());
								sp.setPrice(GeneralUtil.objToDouble(ca.getPrice1()));
								sp.setDefaultPack(1);
								ls.add(sp);
							} else {
								check = 3;
								result = "Pack Size 1 " + ca.getPackSize1() + " not exist ";
							}
						}
					}
					if (ca.getPackSize2() != null && ca.getPackSize2().trim().length() > 0) {
						if (GeneralUtil.objToDouble(ca.getPrice2()) > 0) {
							PackConfig p = getPackConfig(ca.getPackSize2());
							if (p != null) {
								StockPackPrice sp = new StockPackPrice();
								sp.setPackConfigId(p.getPackConfigId());
								sp.setPrice(GeneralUtil.objToDouble(ca.getPrice2()));
								ls.add(sp);
							} else {
								check = 3;
								result = "Pack Size 2 " + ca.getPackSize2() + " not exist ";
							}
						} else {
							check = 3;
							result = "Price 2 Required ";
						}
					}
					if (ca.getPackSize3() != null && ca.getPackSize3().trim().length() > 0) {
						if (GeneralUtil.objToDouble(ca.getPrice3()) > 0) {
							PackConfig p = getPackConfig(ca.getPackSize3());
							if (p != null) {
								StockPackPrice sp = new StockPackPrice();
								sp.setPackConfigId(p.getPackConfigId());
								sp.setPrice(GeneralUtil.objToDouble(ca.getPrice3()));
								ls.add(sp);
							} else {
								check = 3;
								result = "Pack Size 3 " + ca.getPackSize3() + " not exist ";
							}
						} else {
							check = 3;
							result = "Price 3 Required ";
						}
					}
					if (ca.getPackSize4() != null && ca.getPackSize4().trim().length() > 0) {
						if (GeneralUtil.objToDouble(ca.getPrice4()) > 0) {
							PackConfig p = getPackConfig(ca.getPackSize4());
							if (p != null) {
								StockPackPrice sp = new StockPackPrice();
								sp.setPackConfigId(p.getPackConfigId());
								sp.setPrice(GeneralUtil.objToDouble(ca.getPrice4()));
								ls.add(sp);
							} else {
								check = 3;
								result = "Pack Size 4 " + ca.getPackSize4() + " not exist ";
							}
						} else {
							check = 3;
							result = "Price 4 Required ";
						}
					}

					if (check == 0) {
						List<StockItems> st = productDaoImpl.checkStockItems(0, 0, ca.getCode());
						if (st.isEmpty()) {
							ca.setStockItemId(GeneralUtil.getPrimaryKey("SEQ_STOCK_ITEMS", entityManager));
							ca.setCategoryId(catl.get(0).getCategoryId());
							ca.setSubCategoryId(sub.get(0).getSubCategoryId());
							ca.setBrandId(brId);
							ca.setCreatedBy(imp.getImportedBy());
							ca.setCreatedOn(new Date());
							ca.setStatus("active");
							ca.setSellerId(imp.getSellerId());
							 String hr=ca.getCategoryId()+"@"+ca.getSubCategoryId()+"@";
				                if(ca.getBrandId()>0)
				                	hr+=ca.getBrandId()+"@";
				                ca.setHierarchy(hr);
							entityManager.persist(ca);
							for(StockPackPrice sp:ls)
							{
								sp.setStockItemId(ca.getStockItemId());
								sp.setStockPackPriceId(GeneralUtil.getPrimaryKey("SEQ_STOCK_PACK_PRICE", entityManager));
								entityManager.persist(sp);
							}
							if(imp.getSellerId()>0)
							{
								SellerStockMap s = new SellerStockMap();
								s.setSellerStockMapId(GeneralUtil.getPrimaryKey("SEQ_SELLER_STOCK_MAP", entityManager));
								s.setSellerId(imp.getSellerId());
								s.setPrice(ca.getPrice());
								s.setStockItemId(ca.getStockItemId());
								entityManager.persist(s);
								for(StockPackPrice spp:ls)
								{
									SellerSPPrice sp = new SellerSPPrice();
									sp.setPackConfigId(spp.getPackConfigId());
									sp.setDefaultPack(spp.getDefaultPack());
									sp.setPrice(spp.getPrice());
									sp.setStockItemId(ca.getStockItemId());
									sp.setSellerSPPriceId(GeneralUtil.getPrimaryKey("SEQ_SELLER_SP_PRICE", entityManager));
									sp.setSellerStockMapId(s.getSellerStockMapId());
									entityManager.persist(sp);
								}
							}
							
							result = "success";
						} else {
							result = "Stock code " + ca.getCode() + " already exist";
						}
					} else {
						if (check != 3) {
							if (check == 2) {
								result = "Brand " + ca.getBrandName() + " not exist";
							} else {
								result = "Brand  required ";
							}
						}
					}
				} else {
					result = "Sub Category " + ca.getSubCatName() + " not exist";
				}
			} else {
				result = "Category " + ca.getCatName() + " not exist ";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public PackConfig getPackConfig(String name) {
		PackConfig p = null;
		try {
			List<PackConfig> ls = entityManager.createQuery("From PackConfig a where lower(a.packName)=:pack")
					.setParameter("pack", name.toLowerCase()).getResultList();
			if (!ls.isEmpty())
				p = ls.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	public void updateImageForStock() {
		// updateImageStock();
		// updateImageCategory();
		// updateImageSubCategory();
		updateImageBrand();
	}

	public void updateImageStock() {
		JSONArray ar = ReadJsonFile.getJSONObjectBasePath("D:/Temp/MyJSON.json");
		Map<String, String> map = new HashMap<>();
		for (int i = 0; i < ar.size(); i++) {
			JSONObject o = (JSONObject) ar.get(i);
			map.put(o.get("code").toString(), o.get("image").toString());

		}
		List<StockItems> st = productDaoImpl.checkStockItems(0, 0, null);
		for (StockItems s : st) {
			try {
				if (s.getImage() == null) {
					if (map.containsKey(s.getCode())) {
						String path = "D:/" + map.get(s.getCode());
						String dPath = "mycart/stock/" + s.getSubCategoryId();
						if (!(new File("D:/" + dPath)).exists()) {
							new File("D:/" + dPath).mkdirs();
						}

						dPath += "/" + s.getStockItemId() + ".jpeg";
						logger.info("path : " + path);
						logger.info("dPath : " + dPath);
						InputStream is = null;
						OutputStream os = null;
						try {
							is = new FileInputStream(path);
							os = new FileOutputStream("D:/" + dPath);
							byte[] buffer = new byte[1024];
							int length;
							while ((length = is.read(buffer)) > 0) {
								os.write(buffer, 0, length);
							}
							s.setImage(dPath);
							entityManager.merge(s);
						} finally {
							is.close();
							os.close();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void updateImageCategory() {
		JSONArray ar = ReadJsonFile.getJSONObjectBasePath("D:/Temp/MyJSON.json");
		Map<String, String> map = new HashMap<>();
		for (int i = 0; i < ar.size(); i++) {
			JSONObject o = (JSONObject) ar.get(i);
			map.put(o.get("name").toString(), o.get("image").toString());

		}
		List<Category> st = entityManager.createQuery("From Category a").getResultList();
		for (Category s : st) {
			try {
				if (s.getImage() != null) {
					if (map.containsKey(s.getName())) {
						String path = "D:/" + map.get(s.getName());
						String dPath = "mycart/category";
						if (!(new File("D:/" + dPath)).exists()) {
							new File("D:/" + dPath).mkdirs();
						}

						dPath += "/" + s.getCategoryId() + ".jpeg";
						logger.info("path : " + path);
						logger.info("dPath : " + dPath);
						InputStream is = null;
						OutputStream os = null;
						try {
							is = new FileInputStream(path);
							os = new FileOutputStream("D:/" + dPath);
							byte[] buffer = new byte[1024];
							int length;
							while ((length = is.read(buffer)) > 0) {
								os.write(buffer, 0, length);
							}
							s.setImage(dPath);
							entityManager.merge(s);
						} finally {
							is.close();
							os.close();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void updateImageSubCategory() {
		JSONArray ar = ReadJsonFile.getJSONObjectBasePath("D:/Temp/MyJSON.json");
		Map<String, String> map = new HashMap<>();
		for (int i = 0; i < ar.size(); i++) {
			JSONObject o = (JSONObject) ar.get(i);
			map.put(o.get("name").toString(), o.get("image").toString());

		}
		List<SubCategory> st = entityManager.createQuery("From SubCategory a").getResultList();
		for (SubCategory s : st) {
			try {
				if (s.getImage() == null) {
					if (map.containsKey(s.getName())) {
						String path = "D:/" + map.get(s.getName());
						String dPath = "mycart/subcategory";
						if (!(new File("D:/" + dPath)).exists()) {
							new File("D:/" + dPath).mkdirs();
						}

						dPath += "/" + s.getCategoryId() + ".jpeg";
						logger.info("path : " + path);
						logger.info("dPath : " + dPath);
						InputStream is = null;
						OutputStream os = null;
						try {
							is = new FileInputStream(path);
							os = new FileOutputStream("D:/" + dPath);
							byte[] buffer = new byte[1024];
							int length;
							while ((length = is.read(buffer)) > 0) {
								os.write(buffer, 0, length);
							}
							s.setImage(dPath);
							entityManager.merge(s);
						} finally {
							is.close();
							os.close();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void updateImageBrand() {
		JSONArray ar = ReadJsonFile.getJSONObjectBasePath("D:/Temp/MyJSON.json");
		Map<String, String> map = new HashMap<>();
		for (int i = 0; i < ar.size(); i++) {
			JSONObject o = (JSONObject) ar.get(i);
			map.put(o.get("name").toString(), o.get("image").toString());

		}
		List<Brands> st = entityManager.createQuery("From Brands a").getResultList();
		for (Brands s : st) {
			try {
				if (s.getImage() == null) {
					if (map.containsKey(s.getName())) {
						String path = "D:/" + map.get(s.getName());
						String dPath = "mycart/brand";
						if (!(new File("D:/" + dPath)).exists()) {
							new File("D:/" + dPath).mkdirs();
						}

						dPath += "/" + s.getCategoryId() + ".jpeg";
						logger.info("path : " + path);
						logger.info("dPath : " + dPath);
						InputStream is = null;
						OutputStream os = null;
						try {
							is = new FileInputStream(path);
							os = new FileOutputStream("D:/" + dPath);
							byte[] buffer = new byte[1024];
							int length;
							while ((length = is.read(buffer)) > 0) {
								os.write(buffer, 0, length);
							}
							s.setImage(dPath);
							entityManager.merge(s);
						} finally {
							is.close();
							os.close();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
