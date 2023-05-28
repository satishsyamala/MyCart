/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cart.controller;

import com.cart.service.intf.UserServiceIntf;
import com.cart.util.AESEncryption;
import com.cart.util.GeneralUtil;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;

/**
 *
 * @author Shrehan
 */
@CrossOrigin("*")
@RestController
@RequestMapping(value = "/user")
public class UserController {

	private static final Logger logger = LogManager.getLogger(UserController.class);

	@Autowired
	private UserServiceIntf userServiceIntf;

	@GetMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject loginValidation(@RequestParam("username") String userName,
			@RequestParam("password") String password) {
		logger.info(userName);
		return userServiceIntf.loginValidation(userName, password, null);
	}

	@PostMapping(value = "/login-check", produces = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject loginCheck(@RequestBody String text) {

		JSONObject result = new JSONObject();
		try {
			JSONObject json = AESEncryption.decrypt(text);
			String userName = GeneralUtil.objToString(json.get("user_name"));
			String password = GeneralUtil.objToString(json.get("password"));
			result = userServiceIntf.loginValidation(userName, password, json);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return result;

	}

	@GetMapping(value = "/image", produces = "image/jpeg")
	public byte[] getImage(@RequestParam("path") String path) {
		byte[] bytearray = null;
		try {
			logger.info("path : " + path);
			logger.info(GeneralUtil.getPropertyValue("basepath"));
			File file1 = new File(GeneralUtil.getPropertyValue("basepath") + path);
			if (!file1.exists()) {
				file1 = new File(GeneralUtil.getPropertyValue("basepath") + "mycart/noImage.png");
			}
			bytearray = new byte[(int) file1.length()];
			FileInputStream fileInputStream = new FileInputStream(file1);
			fileInputStream.read(bytearray);
			fileInputStream.close();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return bytearray;
	}

	@PostMapping(value = "/adduser", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject saveUser(@RequestBody String type) {
		JSONObject rejson = new JSONObject();
		String result = "";
		try {
			result = userServiceIntf.saveUser(AESEncryption.decrypt(type));
		} catch (Exception e) {
			e.printStackTrace();
			result = "fail";
		}
		rejson.put("reason", result);
		return rejson;
	}

	@PostMapping(value = "/updateuser", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject updateUser(@RequestBody String type) {
		JSONObject result = new JSONObject();
		try {
			result = userServiceIntf.updateUser(AESEncryption.decrypt(type));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reason", "Failed");
		}
		return result;
	}

	@PostMapping(value = "/add-address", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject addDeliveryAddress(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {
			result = userServiceIntf.addDeliveryAddress(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reason", "Failed");
		}
		return result;

	}

	@PostMapping(value = "/update-address", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject updateDeliveryAddress(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {
			result = userServiceIntf.updateDeliveryAddress(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reason", "Failed");
		}
		return result;

	}

	@PostMapping(value = "/get-address", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONArray getAddress(@RequestBody String text) {
		JSONArray result = new JSONArray();
		try {
			JSONObject json = AESEncryption.decrypt(text);
			long userId = Long.parseLong(json.get("userId").toString());
			Date date = null;
			if (json.containsKey("syncdate")) {
				date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(json.get("syncdate").toString());
			}
			result = userServiceIntf.getAddress(userId, date);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return result;
	}

	@PostMapping(value = "/seller-details", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject getSellerDetails(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {

			result = userServiceIntf.getSellerDetails(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reason", "Failed");
		}
		return result;

	}

	@PostMapping(value = "/save-seller", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject saveOrUpdateSellerDetails(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {
			result = userServiceIntf.saveOrUpdateSellerDetails(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reason", "Failed");
		}
		return result;

	}

	@PostMapping(value = "/get-seller-stock", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONArray getStockItems(@RequestBody String text) {
		JSONArray result = new JSONArray();
		try {
			result = userServiceIntf.getSellerMappedItems(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();

		}
		return result;
	}

	@PostMapping(value = "/map-stocks", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject mapStockItems(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {
			result = userServiceIntf.mapStockItems(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reason", "Failed");
		}
		return result;

	}

	@PostMapping(value = "/update-price", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject updatePriceAndQty(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {
			result = userServiceIntf.updatePriceAndQty(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reason", "Failed");
		}
		return result;

	}

	@PostMapping(value = "/remove-stock", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject removeStockMap(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {

			result = userServiceIntf.removeStockMap(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reason", "Failed");
		}
		return result;

	}

	@PostMapping(value = "/get-admin-user", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONArray getAdminUsers(@RequestBody String text) {
		JSONArray result = new JSONArray();
		try {

			result = userServiceIntf.getAdminUsers(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@PostMapping(value = "/get-sellers", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONArray getSellers(@RequestBody String text) {
		JSONArray result = new JSONArray();
		try {

			result = userServiceIntf.getSellers(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@PostMapping(value = "/get-consumers", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONArray getConsumers(@RequestBody String text) {
		JSONArray result = new JSONArray();
		try {

			result = userServiceIntf.getConsumers(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@PostMapping(value = "/get-seller-users", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONArray getSellerUsers(@RequestBody String text) {
		JSONArray result = new JSONArray();
		try {

			result = userServiceIntf.getSellerUsers(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@PostMapping(value = "/get-view-data", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONArray getDataForView(@RequestBody String text) {
		JSONArray result = new JSONArray();
		try {

			result = userServiceIntf.getDataForView(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@PostMapping(value = "/get-table-data", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject getDataForTable(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {

			result = userServiceIntf.getDataForTable(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@PostMapping(value = "/change-password", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject changesPassword(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {

			result = userServiceIntf.changesPassword(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reason", "Failed");
		}
		return result;
	}

	@PostMapping(value = "/get-general-settings", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject getGeneralSettings(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {

			JSONObject json = AESEncryption.decrypt(text);
			result = userServiceIntf.getGeneralSetting(GeneralUtil.objTolong(json.get("sellerId")));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reason", "Failed");
		}
		return result;
	}

	@PostMapping(value = "/get-delivery-charges", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject getDeliverCharges(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {
			JSONObject json = AESEncryption.decrypt(text);
			result = userServiceIntf.getDeliveryCharge(GeneralUtil.objTolong(json.get("sellerId")));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reason", "Failed");
		}
		return result;
	}

	@PostMapping(value = "/save-general-settings", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject saveGeneralSettings(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {

			result.put("reason", userServiceIntf.saveGeneralSettings(AESEncryption.decrypt(text)));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reason", "Failed");
		}
		return result;
	}

	@PostMapping(value = "/save-delivery-charges", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject saveDeliverCharges(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {

			result.put("reason", userServiceIntf.saveDeliveryCharge(AESEncryption.decrypt(text)));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reason", "Failed");
		}
		return result;
	}

	@PostMapping(value = "/address-change", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject addressChange(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {

			result = userServiceIntf.deliveryAddressChange(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reason", "Failed");
		}
		return result;

	}

	@PostMapping(value = "/get-subscription-plans", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONArray getSubscriptionPlans(@RequestBody String text) {
		JSONArray result = new JSONArray();
		try {

			result = userServiceIntf.getSubscriptionPlans(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@PostMapping(value = "/save-subscription-plans", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject saveSubscriptionPlans(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {

			result.put("reason", userServiceIntf.saveSubscriptionPlans(AESEncryption.decrypt(text)));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reason", "Failed");
		}
		return result;

	}

	@PostMapping(value = "/update-subscription-plans", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject updateSubscriptionPlans(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {

			result.put("reason", userServiceIntf.updateSubscriptionPlans(AESEncryption.decrypt(text)));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reason", "Failed");
		}
		return result;

	}

	@PostMapping(value = "/add-subscription", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject addsubscription(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {

			result = userServiceIntf.addsubscription(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reason", "Failed");
		}
		return result;

	}
	


	@PostMapping(value = "/pending-subscription", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONArray getSubcriptionDetails(@RequestBody String text) {
		JSONArray result = new JSONArray();
		try {

			result = userServiceIntf.getSubcriptionDetails(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@PostMapping(value = "/forgot-password", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject getForgotPassword(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {

			result = userServiceIntf.forgotPassword(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@PostMapping(value = "/gen-token", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject generatetoken(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {

			String token = userServiceIntf.generateToken(AESEncryption.decrypt(text));
			if (!token.equalsIgnoreCase("invalid")) {
				result.put("token", token);
				return result;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
