/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cart.controller;

import com.cart.service.intf.TransactionsServiceIntf;
import com.cart.util.GeneralUtil;
import java.io.File;
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
import com.cart.util.AESEncryption;

/**
 *
 * @author USER
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/transaction")
public class Transactions {

	private static final Logger logger = LogManager.getLogger(Transactions.class);
	@Autowired
	private TransactionsServiceIntf transactionsServiceIntf;

	@PostMapping(value = "/get-sellers", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONArray getSellers(@RequestBody String text) {
		JSONArray result = new JSONArray();
		try {

			result = transactionsServiceIntf.getSellers(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();

		}
		return result;
	}

	@PostMapping(value = "/get-sales-items", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONArray getItemsForSell(@RequestBody String text) {
		JSONArray result = new JSONArray();
		try {

			result = transactionsServiceIntf.getItemsForSell(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();

		}
		return result;
	}

	@PostMapping(value = "/get-cart", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject getCategory(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {

			result = transactionsServiceIntf.getCartItems(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();

		}
		return result;
	}

	@PostMapping(value = "/last-10-items", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONArray Last10OrderItems(@RequestBody String text) {
		JSONArray result = new JSONArray();
		try {

			result = transactionsServiceIntf.Last10OrderItems(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@PostMapping(value = "/add-to-cart", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject addItemToCart(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {

			result = transactionsServiceIntf.addItemToCart(AESEncryption.decrypt(text));
		} catch (Exception e) {

		}
		return result;
	}

	@PostMapping(value = "/update-qty", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject updateQty(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {

			result = transactionsServiceIntf.updateQty(AESEncryption.decrypt(text));
		} catch (Exception e) {

		}
		return result;
	}

	@PostMapping(value = "/delete-item", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject deleteItemCart(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {

			result = transactionsServiceIntf.deleteItemCart(AESEncryption.decrypt(text));
		} catch (Exception e) {

		}
		return result;
	}

	@PostMapping(value = "/place-order", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject createOrder(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {

			result = transactionsServiceIntf.createOrder(AESEncryption.decrypt(text));
		} catch (Exception e) {

		}
		return result;
	}

	@PostMapping(value = "/user-orders", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONArray getUserOrders(@RequestBody String text) {
		JSONArray result = new JSONArray();
		try {

			result = transactionsServiceIntf.getUserOrders(AESEncryption.decrypt(text));
		} catch (Exception e) {

		}
		return result;
	}

	@PostMapping(value = "/order-details", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject getOrdersDetails(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {

			result = transactionsServiceIntf.getOrdersDetails(AESEncryption.decrypt(text));
		} catch (Exception e) {

		}
		return result;
	}

	@PostMapping(value = "/all-orders", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONArray getOrdersForProcess(@RequestBody String text) {
		JSONArray result = new JSONArray();
		try {

			result = transactionsServiceIntf.getOrdersForProcess(AESEncryption.decrypt(text));
		} catch (Exception e) {

		}
		return result;
	}

	@PostMapping(value = "/item-seller", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONArray getSellerDetails(@RequestBody String text) {
		JSONArray result = new JSONArray();
		try {

			result = transactionsServiceIntf.getSellerDetails(AESEncryption.decrypt(text));
		} catch (Exception e) {

		}
		return result;
	}

	@PostMapping(value = "/order-process", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject getOrderProccess(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {

			result = transactionsServiceIntf.getOrderProccess(AESEncryption.decrypt(text));
		} catch (Exception e) {

		}
		return result;
	}

	@GetMapping(value = "/order-pdf")
	public ResponseEntity<Resource> getFile(@RequestParam("orderid") long orderid) {
		byte[] bytearray = null;
		try {

			File file = transactionsServiceIntf.downloadOrderPDF(orderid);
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
					.contentType(MediaType.APPLICATION_PDF).body(GeneralUtil.getInputStreamFromPath(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
