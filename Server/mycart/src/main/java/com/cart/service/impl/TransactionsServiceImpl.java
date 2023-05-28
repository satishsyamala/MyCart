/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cart.service.impl;

import com.cart.dao.intf.TransactionsDaoIntf;
import com.cart.invoice.OrderPDF;
import com.cart.service.intf.TransactionsServiceIntf;
import com.cart.util.GeneralUtil;
import java.io.File;
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
public class TransactionsServiceImpl implements TransactionsServiceIntf {

	@Autowired
	private TransactionsDaoIntf transactionsDaoIntf;

	@Override
	public JSONArray getItemsForSell(JSONObject filters) {
		return transactionsDaoIntf.getItemsForSell(filters);
	}

	@Override
	public JSONObject getCartItems(JSONObject json) {
		return transactionsDaoIntf.getCartItmes(json);

	}

	public JSONArray Last10OrderItems(JSONObject json) {
		return transactionsDaoIntf.Last10OrderItems(json);
	}

	@Override
	public JSONObject addItemToCart(JSONObject json) {
		return transactionsDaoIntf.addItemToCart(json);
	}

	@Override
	public JSONObject updateQty(JSONObject json) {
		return transactionsDaoIntf.updateQty(json);
	}

	@Override
	public JSONObject deleteItemCart(JSONObject json) {
		return transactionsDaoIntf.deleteItemCart(json);
	}

	@Override
	public JSONObject createOrder(JSONObject json) {
		return transactionsDaoIntf.createOrder(json);
	}

	@Override
	public JSONArray getUserOrders(JSONObject json) {
		return transactionsDaoIntf.getUserOrders(json);
	}

	@Override
	public JSONObject getOrdersDetails(JSONObject json) {
		return transactionsDaoIntf.getOrdersDetails(json);
	}

	@Override
	public JSONArray getOrdersForProcess(JSONObject json) {
		return transactionsDaoIntf.getOrdersForProcess(json);
	}

	@Override
	public JSONArray getSellerDetails(JSONObject json) {
		JSONArray item = (JSONArray) json.get("items");
		long delId = GeneralUtil.objTolong(json.get("del_address_id"));
		return transactionsDaoIntf.getSellerDetails(item, delId, "stock_item_id", 10);
	}

	@Override
	public JSONObject getOrderProccess(JSONObject json) {
		return transactionsDaoIntf.getOrderProccess(json);
	}

	public File downloadOrderPDF(long orderId) {
		JSONObject json = new JSONObject();
		json.put("order_id", orderId);
		JSONObject order = transactionsDaoIntf.getOrdersDetails(json);
		return OrderPDF.orderPdf(order);
	}

	@Override
	public JSONArray getSellers(JSONObject filters) {
		return transactionsDaoIntf.getSellers(filters);
	}

}
