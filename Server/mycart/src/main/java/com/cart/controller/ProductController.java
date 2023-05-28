/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cart.controller;

import com.cart.service.intf.ProductServiceIntf;
import com.cart.util.AESEncryption;
import com.cart.util.GeneralUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author USER
 */
@CrossOrigin("*")
@RestController
@RequestMapping(value = "/product")
public class ProductController {

	private static final Logger logger = LogManager.getLogger(UserController.class);

	@Autowired
	private ProductServiceIntf productServiceIntf;

	@PostMapping(value = "/get-category", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONArray getCategory(@RequestBody String text) {
		JSONArray result = new JSONArray();
		try {

			JSONObject json = AESEncryption.decrypt(text);
			logger.info(json.toString());
			long id = 0;
			if (json.containsKey("name")) {
				id = Long.parseLong(json.get("id").toString());
			}
			String name = null;
			if (json.containsKey("name")) {
				name = json.get("name").toString();
			}
			Date date = null;
			if (json.containsKey("syncdate")) {
				date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(json.get("syncdate").toString());
			}
			result = productServiceIntf.getCategory(id, name, date);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return result;
	}

	@PostMapping(value = "/add-category", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject addCategory(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {

			result = productServiceIntf.addCategory(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reason", "Failed");
		}
		return result;

	}

	@PostMapping(value = "/update-category", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject updateCategory(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {

			result = productServiceIntf.updateCategory(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reason", "Failed");
		}
		return result;

	}

	@PostMapping(value = "/get-sub-category", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONArray getSubCategory(@RequestBody String text) {
		JSONArray result = new JSONArray();
		try {

			JSONObject json = AESEncryption.decrypt(text);
			logger.info(json.toString());
			long id = 0;
			if (json.containsKey("id")) {
				id = Long.parseLong(json.get("id").toString());
			}
			long catid = 0;
			if (json.containsKey("catid")) {
				catid = Long.parseLong(json.get("catid").toString());
			}
			String name = null;
			if (json.containsKey("name")) {
				name = json.get("name").toString();
			}
			Date date = null;
			if (json.containsKey("syncdate")) {
				date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(json.get("syncdate").toString());
			}
			result = productServiceIntf.getSubCategories(id, catid, name, date);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return result;
	}

	@PostMapping(value = "/get-cat-options", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject getBusOptions(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {
			result = productServiceIntf.getBusOptions(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();

		}
		return result;
	}

	@PostMapping(value = "/add-sub-category", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject addSubCategory(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {

			result = productServiceIntf.addSubCategory(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reason", "Failed");
		}
		return result;

	}

	@PostMapping(value = "/update-sub-category", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject updateSubCategory(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {

			result = productServiceIntf.updateSubCategory(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reason", "Failed");
		}
		return result;

	}

	@PostMapping(value = "/get-brands", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONArray getBrands(@RequestBody String text) {
		JSONArray result = new JSONArray();
		try {

			JSONObject json = AESEncryption.decrypt(text);
			logger.info(json.toString());
			long id = 0;
			if (json.containsKey("id")) {
				id = Long.parseLong(json.get("id").toString());
			}
			long catid = 0;
			if (json.containsKey("categoryId")) {
				catid = Long.parseLong(json.get("categoryId").toString());
			}
			long subCatid = 0;
			if (json.containsKey("subCategoryId")) {
				subCatid = Long.parseLong(json.get("subCategoryId").toString());
			}
			String name = null;
			if (json.containsKey("name")) {
				name = json.get("name").toString();
			}
			Date date = null;
			if (json.containsKey("syncdate")) {
				date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(json.get("syncdate").toString());
			}
			result = productServiceIntf.getBrands(id, catid, subCatid, name, date);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return result;
	}

	@PostMapping(value = "/add-brands", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject addBrands(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {

			result = productServiceIntf.addBrands(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reason", "Failed");
		}
		return result;

	}

	@PostMapping(value = "/update-brands", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject updateBrands(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {

			result = productServiceIntf.updateBrands(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reason", "Failed");
		}
		return result;

	}

	@PostMapping(value = "/get-stock-admin", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONArray getStockAdmin(@RequestBody String text) {
		JSONArray result = new JSONArray();
		try {
			result = productServiceIntf.getStockItems(AESEncryption.decrypt(text), "admin");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@PostMapping(value = "/get-stock-seller", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONArray getStockSeller(@RequestBody String text) {
		JSONArray result = new JSONArray();
		try {
			result = productServiceIntf.getStockItems(AESEncryption.decrypt(text), "seller");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@PostMapping(value = "/add-stock-item", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject addStockItem(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {

			result = productServiceIntf.addStockItems(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reason", "Failed");
		}
		return result;

	}

	@PostMapping(value = "/update-stock-item", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject updateStockItem(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {

			result = productServiceIntf.updateStockItems(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reason", "Failed");
		}
		return result;

	}

	@PostMapping(value = "/stock-export")
	public ResponseEntity<Resource> getFile(@RequestBody String text) {
		try {
			File file = productServiceIntf.exportStockItems(AESEncryption.decrypt(text));
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
					.body(GeneralUtil.getInputStreamFromPath(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@PostMapping(value = "/pack-options",  produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject getPackOption(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {
			result = productServiceIntf.getPackOptions(null);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reason", "Failed");
		}
		return result;

	}
	

}
