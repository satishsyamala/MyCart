/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cart.dao.impl;

import com.cart.dao.intf.TransactionsDaoIntf;
import com.cart.file.ReadJsonFile;
import com.cart.pojo.CartItems;
import com.cart.pojo.Offers;
import com.cart.pojo.OrderDeliveryUsermap;
import com.cart.pojo.OrderItems;
import com.cart.pojo.OrderTrack;
import com.cart.pojo.Orders;
import com.cart.pojo.StockItems;
import com.cart.util.GeneralUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author USER
 */
@Repository
public class TransactionsDaoImpl implements TransactionsDaoIntf {

	private static final Logger logger = LogManager.getLogger(TransactionsDaoImpl.class);
	@Autowired
	private EntityManager entityManager;

	@Override
	public JSONArray getSellers(JSONObject filters) {
		JSONArray result = new JSONArray();
		try {
			String sql = "select a.seller_id,a.shop_name,a.mobile_no,to_char(round(fun_geo_deviation(a.lotitude,a.longitude,b.latitude,b.longitude)/1000,2)) distance,a.image,a.lotitude,a.longitude,a.address||', '||a.city||', '||a.state address,nvl(c.min_order_value,0) min_order_value from sellers a,delivery_address b,gen_settings c "
					+ " where b.user_id=" + filters.get("userId").toString()
					+ " and a.seller_id=c.seller_id(+) and trunc(nvl(a.expired_date,sysdate-1))>=trunc(sysdate)  and round(fun_geo_deviation(b.latitude,b.longitude,a.lotitude,a.longitude)/1000)<=decode(nvl(c.distance_km,0),0,20,c.distance_km) and b.IS_DEFAULT=1";

			if (filters.containsKey("shop_name") && filters.get("shop_name") != null
					&& filters.get("shop_name").toString().length() > 0) {
				sql += " and lower(a.shop_name) like '%'||:shop_name||'%' ";
			}

			if (filters.containsKey("stock_name") && filters.get("stock_name") != null
					&& filters.get("stock_name").toString().length() > 0) {
				sql += " and a.seller_id in (select DISTINCT a1.seller_id from seller_stock_map a1,stock_items b1 where a1.stock_item_id=b1.stock_item_id"
						+ " and  (lower(b1.name) like '%'||:sku_name||'%' or lower(b1.description) like '%'||:sku_name||'%') ) ";
			}

			sql += " order by  ";
			if (filters.containsKey("orderby") && filters.get("orderby") != null
					&& filters.get("orderby").toString().length() > 0) {
				String orderBy = filters.get("orderby").toString();
				sql += orderBy.equals("name") ? " a.shop_name "
						: " round(fun_geo_deviation(a.lotitude,a.longitude,b.latitude,b.longitude)/1000,2)";
			} else {
				sql += " round(fun_geo_deviation(a.lotitude,a.longitude,b.latitude,b.longitude)/1000,2) ";
			}
			logger.info("SQl : " + sql);
			Query q = entityManager.createNativeQuery(sql);
			if (filters.containsKey("shop_name") && filters.get("shop_name") != null
					&& filters.get("shop_name").toString().length() > 0) {
				q.setParameter("shop_name", filters.get("shop_name").toString().toLowerCase());
			}
			if (filters.containsKey("stock_name") && filters.get("stock_name") != null
					&& filters.get("stock_name").toString().length() > 0) {
				q.setParameter("sku_name", filters.get("stock_name").toString().toLowerCase());
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
			JSONArray columns = GeneralUtil.getQueryColumns(sql, entityManager, null);
			List<Object[]> ls = q.getResultList();
			result = GeneralUtil.resultToJSONArray(columns, ls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public JSONArray getItemsForSell(JSONObject filters) {
		JSONArray result = new JSONArray();
		try {
			String latitude = "";
			String longitude = "";
			long addressId = 0;
			long userId = GeneralUtil.objTolong(filters.get("userId"));
			if (GeneralUtil.objToString(filters.get("latitude")).length() > 0
					&& GeneralUtil.objToString(filters.get("longitude")).length() > 0) {
				latitude = GeneralUtil.objToString(filters.get("latitude"));
				longitude = GeneralUtil.objToString(filters.get("longitude"));
			} else {
				String sql = "";
				if (GeneralUtil.objTolong(filters.get("sel_del")) > 0) {
					sql = "select a.latitude,a.longitude,a.DELIVERY_ADD_ID from delivery_address a "
							+ "where a.user_id=" + userId + " and a.DELIVERY_ADD_ID="
							+ GeneralUtil.objTolong(filters.get("sel_del"));
				} else {
					sql = "select a.latitude,a.longitude,a.DELIVERY_ADD_ID from delivery_address a "
							+ "where a.user_id=" + userId + " and a.is_default=1 ";
				}
				List<Object[]> ls = entityManager.createNativeQuery(sql).getResultList();
				if (!ls.isEmpty()) {
					latitude = GeneralUtil.objToString(ls.get(0)[0]);
					longitude = GeneralUtil.objToString(ls.get(0)[1]);
					addressId = GeneralUtil.objTolong(ls.get(0)[2]);
				}
				logger.info("Sql : " + sql);
			}
			boolean sellerInfo = GeneralUtil.objToBoolean(filters.get("isSeller"));

			String sql = "select a.*,decode(a.discount_price,0,a.price,a.discount_price) act_price,(select LISTAGG(ci.pack||'@'||ci.cart_item_id||'@'||ci.quantity,'#') WITHIN group(order by ci.pack) "
					+ " from cart_items ci where ci.user_id=" + userId
					+ " and ci.stock_item_id=a.stock_item_id and ci.status='active' and ci.seller_id=a.seller_id and ci.ADDRESS_ID="
					+ addressId + ") isincart," + addressId + " del_id,"
					+ "(select LISTAGG(pc.pack_name||'@'||pc.quantity||'@'||sp.price||'@'||sp.default_pack||'@'||pc.pack_config_id,'#') WITHIN group(order by pc.quantity) from seller_stock_map ss,seller_sp_price  sp,pack_config pc "
					+ "where ss.seller_stock_map_id=sp.seller_stock_map_id and sp.pack_config_id=pc.pack_config_id "
					+ "and ss.seller_id=a.seller_id and sp.stock_item_id=a.stock_item_id " + ") multiPack  from ("
					+ salesStockSql(latitude, longitude, GeneralUtil.objTolong(filters.get("seller_id")), sellerInfo)
					+ ") a " + " where 1=1 ";

			if (filters.containsKey("stock_item_id") && GeneralUtil.objTolong(filters.get("stock_item_id")) > 0) {
				sql += " and a.stock_item_id=" + filters.get("stock_item_id").toString();
			}

			if (filters.containsKey("seller_id") && GeneralUtil.objTolong(filters.get("seller_id")) > 0) {
				sql += " and a.seller_id =" + filters.get("seller_id").toString() + "";
			}

			if (filters.containsKey("categoryId") && GeneralUtil.objTolong(filters.get("categoryId")) > 0) {
				sql += " and a.category_id=" + filters.get("categoryId").toString();
			}
			if (filters.containsKey("subCategoryId") && GeneralUtil.objTolong(filters.get("subCategoryId")) > 0) {
				sql += " and a.sub_cat_id=" + filters.get("subCategoryId").toString();
			}
			if (filters.containsKey("brandId") && GeneralUtil.objTolong(filters.get("brandId")) > 0) {
				sql += " and a.brand_id=" + filters.get("brandId").toString();
			}
			if (filters.containsKey("name") && filters.get("name") != null
					&& filters.get("name").toString().length() > 0) {
				sql += " and (lower(a.name) like '%'||:sku_name||'%' or lower(a.description) like '%'||:sku_name||'%') ";
			}

			if (filters.containsKey("invstock") && filters.get("invstock") != null
					&& filters.get("invstock").toString().length() > 0) {
				sql += " and a.inventory_status='" + filters.get("invstock").toString().toUpperCase() + "'";
			}
			logger.info("GeneralUtil.objTolong(filters.containsKey(\"offerid\")) "
					+ GeneralUtil.objTolong(filters.get("offerid")));
			if (filters.containsKey("offerid") && GeneralUtil.objTolong(filters.get("offerid")) > 0) {
				sql += " and a.stock_item_id in (select a.stock_item_id from price_list_items a where a.price_list_id="
						+ GeneralUtil.objTolong(filters.get("offerid")) + ")";
			}

			sql += " order by  ";

			if (filters.containsKey("orderby") && filters.get("orderby") != null
					&& filters.get("orderby").toString().length() > 0) {
				String orderBy = filters.get("orderby").toString();
				sql += (orderBy.equals("name") ? " a.name "
						: orderBy.equals("plh") ? " decode(a.discount_price,0,a.price,a.discount_price) "
								: orderBy.equals("phl") ? " decode(a.discount_price,0,a.price,a.discount_price) desc "
										: " a.rating ")
						+ " ,";
			}
			sql += "  a.stock_item_id ";
			logger.info("SQl : " + sql);
			Query q = entityManager.createNativeQuery(sql);
			if (filters.containsKey("name") && filters.get("name") != null
					&& filters.get("name").toString().length() > 0) {
				q.setParameter("sku_name", filters.get("name").toString().toLowerCase());
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
			JSONArray columns = GeneralUtil.getQueryColumns(sql, entityManager, "salesitem");
			List<Object[]> ls = q.getResultList();
			result = GeneralUtil.resultToJSONArray(columns, ls);
			if (filters.containsKey("stock_item_id") && GeneralUtil.objTolong(filters.get("stock_item_id")) > 0) {
				String sql1 = "select a.image,a.stock_item_id from stock_images a where a.stock_item_id="
						+ GeneralUtil.objTolong(filters.get("stock_item_id"));
				JSONArray ims = new JSONArray();
				logger.info("sql1 : " + sql1);
				List<Object[]> ls1 = entityManager.createNativeQuery(sql1).getResultList();
				for (Object[] o : ls1) {
					JSONObject jo = new JSONObject();
					jo.put("image", o[0].toString());
					jo.put("code", o[1].toString());
					ims.add(jo);
				}
				((JSONObject) result.get(0)).put("images", ims);
			}
			logger.info("result : " + result.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public List<CartItems> getCartItems(long userId, String status, long stockId, JSONObject filter, long cartItemId,
			long addressId) {
		List<CartItems> cart = new ArrayList<>();
		try {
			String sql = "From CartItems a where 1=1 ";
			if (cartItemId > 0) {
				sql += " and a.cartItemId=" + cartItemId;
			}
			if (userId > 0) {
				sql += " and a.userId=" + userId;
			}
			if (status != null && status.length() > 0) {
				sql += " and a.status='" + status + "'";
			}
			if (stockId > 0) {
				sql += " and a.stockItemId=" + stockId;
			}
			if (addressId > 0)
				sql += " and a.addressId=" + addressId;
			sql += " order by a.addedOn desc ";
			cart = entityManager.createQuery(sql).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cart;
	}

	@Override
	public JSONObject getCartItmes(JSONObject json) {
		JSONObject result = new JSONObject();
		try {

			long addressId = GeneralUtil.objTolong(json.get("address_id"));

			String sql = "select a.stock_item_id,a.name,c.order_by,b.pack pack_size,a.uom,c.name category_name,b.image,b.quantity,(select max(b.price) from seller_stock_map a1,seller_sp_price b1,pack_config c1 "
					+ "where a1.seller_stock_map_id=b1.seller_stock_map_id and a1.stock_item_id=a.stock_item_id and b1.pack_config_id=c1.pack_config_id "
					+ "and c1.quantity=b.pack_qty and a1.seller_id=sl.seller_id)  price,case when a.price>sm.price then sm.price else 0 end discount_price,case when a.price>sm.price then 'true' else 'false' end isdiscount,b.added_on,a.description"
					+ ",b.cart_item_id,b.offer_id,c.image cat_image,b.seller_id,b.address_id,sl.shop_name,sl.image sh_image,b.pack_qty from stock_items a,seller_stock_map sm,cart_items b,category c,sellers sl "
					+ " where a.stock_item_id=b.stock_item_id and sl.seller_id=b.seller_id and a.stock_item_id=sm.stock_item_id     and sm.seller_id=b.seller_id and c.category_id=a.category_id  "
					+ "and  b.status='active' and b.user_id=" + json.get("userId").toString();
			if (addressId > 0)
				sql += " and b.address_id=" + addressId;

			sql += " order by  b.added_on ";
			logger.info("SQl " + sql);
			JSONArray columns = GeneralUtil.getQueryColumns(sql, entityManager, "cartitems");
			List<Object[]> ls = entityManager.createNativeQuery(sql).getResultList();
			JSONArray items = GeneralUtil.resultToJSONArray(columns, ls);
			result.put("items", items);
			result.put("sellers", sellerDelieryCharges(GeneralUtil.objTolong(json.get("userId")), addressId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public JSONArray Last10OrderItems(JSONObject json) {
		JSONArray result = new JSONArray();
		try {

			long addressId = GeneralUtil.objTolong(json.get("address_id"));

			String sql = "select c.status,c.name,c.image from orders a,order_items b,stock_items c where a.order_id=b.order_id "
					+ "and b.stock_item_id=c.stock_item_id and a.user_id=" + json.get("userId").toString();
			if (addressId > 0)
				sql += " and a.delivery_add_id=" + addressId;

			sql += " order by  a.order_date ";
			logger.info("SQl " + sql);
			JSONArray columns = GeneralUtil.getQueryColumns(sql, entityManager, null);
			List<Object[]> ls = entityManager.createNativeQuery(sql).setMaxResults(9).getResultList();
			result = GeneralUtil.resultToJSONArray(columns, ls);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public JSONObject sellerDelieryCharges(long userId, long deliveryId) {
		JSONObject result = new JSONObject();
		try {
			String latitude = "";
			String longitude = "";
			String sql1 = "select a.latitude,a.longitude,a.DELIVERY_ADD_ID from delivery_address a "
					+ "where a.user_id=" + userId + " and a.DELIVERY_ADD_ID=" + deliveryId;

			List<Object[]> ls1 = entityManager.createNativeQuery(sql1).getResultList();
			if (!ls1.isEmpty()) {
				latitude = GeneralUtil.objToString(ls1.get(0)[0]);
				longitude = GeneralUtil.objToString(ls1.get(0)[1]);
			}

			String sql = "select DISTINCT mm.seller_id,nvl(a.payment_modes,'[\"Cash On Delivery\"]') payment_modes,nvl(a.min_delivery_days,2) min_delivery_days,  "
					+ "nvl(b.min_delivery_charge,0) min_delivery_charge,  "
					+ "nvl(b.max_delivery_charge,0) max_delivery_charge,  "
					+ "nvl(b.max_value_for_free_delv,0) max_value_for_free_delv,  "
					+ "nvl(b.min_dist_for_min_charge,0) min_dist_for_min_charge,  "
					+ "nvl(b.dele_charge_for_km,0) dele_charge_for_km, round(fun_geo_deviation(" + latitude + ","
					+ longitude
					+ ",s.lotitude,s.longitude)/1000) dist,s.shop_name,nvl(a.min_order_value,0) min_order_value "
					+ "from gen_settings a,delivery_charges b,sellers s,(select DISTINCT c.seller_id from cart_items c where c.status='active'  "
					+ "and c.user_id=" + userId + " and c.address_id=" + deliveryId + ") mm  "
					+ "where  mm.seller_id=s.seller_id and mm.seller_id=a.seller_id(+)  "
					+ "and mm.seller_id=b.seller_id(+) ";
			logger.info("Sql : " + sql);
			JSONArray columns = GeneralUtil.getQueryColumns(sql, entityManager, null);
			List<Object[]> ls = entityManager.createNativeQuery(sql).getResultList();
			JSONArray items = GeneralUtil.resultToJSONArray(columns, ls);
			for (int i = 0; i < items.size(); i++) {
				JSONObject o = (JSONObject) items.get(i);
				logger.info(o.get("payment_modes").toString());
				o.put("payment_modes", new JSONParser().parse(o.get("payment_modes").toString()));
				result.put(o.get("seller_id").toString(), o);

			}

			logger.info("sellerDelieryCharges : " + result.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public JSONArray getUserOrders(JSONObject json) {
		JSONArray result = new JSONArray();
		try {
			String sql = "select a.order_id,a.name,a.order_no,a.transaction_no,to_char(a.order_date,'dd-mm-yyyy HH24:mi') order_date,to_char(a.delivery_date,'dd-mm-yyyy') delivery_date,a.image,a.final_price,a.status,a.track_status,a.delivery_type from orders a where a.user_id="
					+ json.get("userId").toString();
			if (json.containsKey("status") && json.get("status") != null
					&& json.get("status").toString().length() > 0) {
				sql += " and and a.status='" + json.get("invstock").toString().toUpperCase() + "'";
			}

			if (json.containsKey("name") && json.get("name") != null && json.get("name").toString().length() > 0) {
				sql += " and lower(a.name) like '%'||:order_name||'%' ";
			}

			if (json.containsKey("address_id") && GeneralUtil.objTolong(json.get("address_id")) > 0) {
				sql += " and a.delivery_add_id=" + GeneralUtil.objTolong(json.get("address_id"));
			}

			if (json.containsKey("select_seller_id") && GeneralUtil.objTolong(json.get("select_seller_id")) > 0) {
				sql += " and a.seller_id=" + GeneralUtil.objTolong(json.get("select_seller_id"));
			}

			sql += " order by ";
			if (json.containsKey("orderby") && json.get("orderby") != null
					&& json.get("orderby").toString().length() > 0) {
				String orderBy = json.get("orderby").toString();
				sql += (orderBy.equals("oda") ? " a.order_date "
						: orderBy.equals("odd") ? " a.order_date desc "
								: orderBy.equals("dda") ? " a.delivery_date " : " a.delivery_date desc ")
						+ " ";
			} else {
				sql += " a.order_date desc ";
			}
			Query q = entityManager.createNativeQuery(sql);
			if (json.containsKey("name") && json.get("name") != null && json.get("name").toString().length() > 0) {
				q.setParameter("order_name", json.get("name").toString().toLowerCase());
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

			JSONArray columns = GeneralUtil.getQueryColumns(sql, entityManager, null);
			List<Object[]> ls = entityManager.createNativeQuery(sql).getResultList();
			result = GeneralUtil.resultToJSONArray(columns, ls);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public JSONObject addItemToCart(JSONObject json) {
		JSONObject obj = new JSONObject();
		String reason = "";
		try {
			logger.info("json : " + json.toString());
			CartItems cart = new CartItems();
			cart.setCartItemId(GeneralUtil.getPrimaryKey("SEQ_CART_ITEMS", entityManager));
			cart.setUserId(GeneralUtil.objTolong(json.get("userId")));
			JSONObject st = (JSONObject) json.get("stock_items");
			logger.info("it : " + GeneralUtil.objTolong(st.get("stock_item_id")));
			cart.setStockItemsId(GeneralUtil.objTolong(st.get("stock_item_id")));
			cart.setCatOrderBy(st.get("order_by").toString());
			cart.setAddedOn(new Date());
			cart.setQuantitiy(GeneralUtil.objToInt(json.get("quantity")));
			cart.setPrice(GeneralUtil.objToDouble(json.get("price")));
			cart.setDisPer(GeneralUtil.objToDouble(json.get("isdiscount")));
			cart.setDiscount(GeneralUtil.objToDouble(json.get("discount_price")));
			cart.setSellerId(GeneralUtil.objTolong(json.get("seller_id")));
			cart.setAddressId(GeneralUtil.objTolong(json.get("address_id")));
			cart.setPackQty(GeneralUtil.objToDouble(json.get("pack_qty")));
			cart.setPack(GeneralUtil.objToString(json.get("pack")));
			if (cart.getDisPer() > 0) {
				cart.setFinalPrice(cart.getQuantitiy() * cart.getDiscount());
			} else {
				cart.setFinalPrice(cart.getQuantitiy() * cart.getPrice());
			}
			cart.setTax(0);
			cart.setStatus("active");
			if (json.containsKey("json") && GeneralUtil.objTolong(json.get("offer_id")) > 0) {
				cart.setOffersId(GeneralUtil.objTolong(json.get("offer_id")));
			}
			cart.setImage(json.get("image").toString());
			entityManager.persist(cart);
			obj.put("cart_item_id", cart.getCartItemId());
			reason = "success";
		} catch (Exception e) {
			e.printStackTrace();
			reason = "failed";
		}
		obj.put("reason", reason);
		logger.info("obj : " + obj);
		return obj;
	}

	public JSONObject updateQty(JSONObject json) {
		JSONObject obj = new JSONObject();
		String reason = "";
		try {
			logger.info("obj : " + json.toString());

			long cartItemId = GeneralUtil.objTolong(json.get("cart_item_id"));
			List<CartItems> ls = getCartItems(0, "active", 0, new JSONObject(), cartItemId, 0);
			if (!ls.isEmpty()) {
				CartItems cart = ls.get(0);
				cart.setQuantitiy(GeneralUtil.objToInt(json.get("quantity")));
				logger.info("obj : " + cart.getQuantitiy());
				cart.setPrice(GeneralUtil.objToDouble(json.get("price")));
				cart.setDisPer(GeneralUtil.objToDouble(json.get("isdiscount")));
				cart.setDiscount(GeneralUtil.objToDouble(json.get("discount_price")));
				if (cart.getDisPer() > 0) {
					cart.setFinalPrice(cart.getQuantitiy() * cart.getDiscount());
				} else {
					cart.setFinalPrice(cart.getQuantitiy() * cart.getPrice());
				}
				cart.setStatus("active");
				if (json.containsKey("json") && GeneralUtil.objTolong(json.get("offer_id")) > 0) {
					cart.setOffersId(GeneralUtil.objTolong(json.get("offer_id")));
				}
				// cart.setImage(json.get("image").toString());
				entityManager.merge(cart);
				reason = "success";
			}

		} catch (Exception e) {
			reason = "failed";
		}
		obj.put("reason", reason);
		return obj;
	}

	public JSONObject deleteItemCart(JSONObject json) {
		JSONObject obj = new JSONObject();
		String reason = "";
		try {

			long cartItemId = GeneralUtil.objTolong(json.get("cart_item_id"));
			List<CartItems> ls = getCartItems(0, "active", 0, new JSONObject(), cartItemId, 0);
			if (!ls.isEmpty()) {
				CartItems cart = ls.get(0);
				cart.setStatus("removed");
				entityManager.merge(cart);
			}
			reason = "success";
		} catch (Exception e) {
			reason = "failed";
		}
		obj.put("reason", reason);
		return obj;
	}

	public JSONObject createOrder(JSONObject json) {
		JSONObject obj = new JSONObject();
		String reason = "";
		try {
			Orders o = new Orders();
			o.setOrderId(GeneralUtil.getPrimaryKey("SEQ_ORDERS", entityManager));
			o.setCatOrderBy(json.get("order_by").toString());
			o.setDeliveryDate(GeneralUtil.objToDate(json.get("delivery_date")));
			o.setDelveryAddressId(GeneralUtil.objTolong(json.get("del_address_id")));
			o.setDiscount(GeneralUtil.objToDouble(json.get("discount")));
			o.setFinalPrice(GeneralUtil.objToDouble(json.get("final_price")));
			o.setImage(json.get("image").toString());
			o.setName(json.get("name").toString());
			o.setOrderDate(GeneralUtil.objToDateTime(json.get("order_date")));
			o.setOrderNo(json.get("order_ref").toString());
			o.setAddress(json.get("address").toString());
			o.setPaymentType(json.get("payment_type").toString());
			o.setPrice(GeneralUtil.objToDouble(json.get("price")));
			o.setStatus("pending");
			o.setStatus("Order Placed");
			o.setTransactionNo(json.get("tran_ref").toString());
			o.setUserId(GeneralUtil.objTolong(json.get("userId")));
			o.setDeliveryCharge(GeneralUtil.objTolong(json.get("delv_charge")));
			OrderTrack ot = GeneralUtil.getOrderTrackObject(entityManager, "Order Placed", "Order Placed",
					o.getOrderId());
			o.setTrackStatus(ot.getStatus());
			o.setLastTrackId(ot.getOrderTrackId());
			o.setLastTrackDate(ot.getOnDate());
			o.setDeliveryType(json.get("deltype").toString());
			JSONArray items = (JSONArray) json.get("cart_times");
			if (json.containsKey("seller_id") && GeneralUtil.objTolong(json.get("seller_id")) > 0) {
				o.setSellerId(GeneralUtil.objTolong(json.get("seller_id")));
			} else {
				JSONArray sell = getSellerDetails(items, o.getDelveryAddressId(), "stock_item_id", 1);
				if (sell.size() > 0) {
					o.setSellerId(GeneralUtil.objTolong(((JSONObject) sell.get(0)).get("seller_id")));
				}
			}
			entityManager.persist(o);

			for (int i = 0; i < items.size(); i++) {
				JSONObject itjson = (JSONObject) items.get(i);
				logger.info("obj : " + itjson.toJSONString());
				OrderItems oi = new OrderItems();
				oi.setOrderItemId(GeneralUtil.getPrimaryKey("SEQ_ORDER_ITEMS", entityManager));
				oi.setOrderId(o.getOrderId());
				oi.setCartItemId(GeneralUtil.objTolong(itjson.get("cart_item_id")));
				double price = GeneralUtil.objToDouble(itjson.get("price"));
				int qty = GeneralUtil.objToInt(itjson.get("quantity"));
				double discount = 0;
				if (GeneralUtil.objToBoolean(itjson.get("isdiscount"))) {
					discount = GeneralUtil.objToDouble(itjson.get("discount_price"));
				} else {
					discount = price;
				}
				oi.setPrice(price * qty);
				oi.setFinalPrice(qty * discount);
				oi.setDiscount(oi.getPrice() - oi.getFinalPrice());
				oi.setImage(itjson.get("image").toString());
				oi.setOfferId(GeneralUtil.objTolong(itjson.get("offer_id")));
				oi.setQuantitiy(GeneralUtil.objToInt(itjson.get("quantity")));
				oi.setStockItemId(GeneralUtil.objTolong(itjson.get("stock_item_id")));
				oi.setTax(0);
				oi.setUnitPrice(GeneralUtil.objToDouble(itjson.get("price")));
				oi.setPackQty(GeneralUtil.objToDouble(itjson.get("pack_qty")));
				oi.setPack(GeneralUtil.objToString(itjson.get("pack_size")));
				entityManager.persist(oi);
				List<CartItems> ls = getCartItems(0, "active", 0, new JSONObject(), oi.getCartItemId(), 0);
				if (!ls.isEmpty()) {
					CartItems ci = ls.get(0);
					ci.setStatus("ordered");
					entityManager.merge(ci);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			reason = "failed";
		}
		obj.put("reason", reason);
		return obj;
	}

	public JSONObject getOrderProccess(JSONObject json) {
		JSONObject obj = new JSONObject();
		String reason = "";
		try {
			long orderId = GeneralUtil.objTolong(json.get("order_id"));
			String nextStatus = getNextStatus(json.get("status").toString(), json.get("delivery_type").toString());
			String discretion = nextStatus;
			boolean otpcheck = true;
			if (nextStatus.equalsIgnoreCase("Delivered") && !GeneralUtil.objToBoolean(json.get("nootp"))) {
				otpcheck = checkOptValidation(orderId, GeneralUtil.objToInt(json.get("enterotp")));
			}
			if (nextStatus.equalsIgnoreCase("Out For Delivery")) {
				otpcheck = checkPickOtpValidation(GeneralUtil.objTolong(json.get("order_delivery_user_map_id")),
						GeneralUtil.objTolong(json.get("enterotp")));

			}
			if (otpcheck) {
				int otp = 0;
				long userId = 100;
				if (nextStatus.equalsIgnoreCase("Ready For Pickup")
						|| nextStatus.equalsIgnoreCase("Out For Delivery")) {
					otp = GeneralUtil.otpGenerate(1001, 9999);
					discretion += otp + "OTP for "
							+ (nextStatus.equalsIgnoreCase("Ready For Pickup") ? "Pickup" : "Delivery");
				} else if (nextStatus.equalsIgnoreCase("Assigned")) {
					userId = assignedTodeliveryUser(orderId, 0, json);

				}

				if (userId > 0) {
					OrderTrack ot = GeneralUtil.getOrderTrackObject(entityManager, nextStatus, discretion, orderId);
					String sql = "update orders a set a.status='" + nextStatus + "',a.track_status='" + nextStatus
							+ "',a.last_track_id=" + ot.getOrderTrackId();
					if (nextStatus.equalsIgnoreCase("delivered")) {
						sql += " ,a.ACT_DELIVERY_DATE=sysdate ";
					}
					if (nextStatus.equalsIgnoreCase("Ready For Pickup")
							|| (nextStatus.equalsIgnoreCase("Out For Delivery")
									&& !GeneralUtil.objToBoolean(json.get("nootp")))) {
						sql += " , a.otp=" + otp;
					}
					sql += " where a.order_id=" + orderId + " and a.status='" + json.get("status").toString() + "'";
					int count = entityManager.createNativeQuery(sql).executeUpdate();
					if (count > 0) {
						if (nextStatus.equalsIgnoreCase("delivered")) {
							sql = "update order_delivery_user_map a set a.assign_status='" + nextStatus
									+ "'  where a.assign_status='Pickup' and a.order_delivery_user_map_id="
									+ GeneralUtil.objTolong(json.get("order_delivery_user_map_id"));
							entityManager.createNativeQuery(sql).executeUpdate();

						}
						reason = "success";
					} else {
						reason = "Order is not proccessed";
					}
				} else {
					reason = "Delivery Users are not available at this time. Please Try later";
				}
			} else {
				if (nextStatus.equalsIgnoreCase("Out For Delivery"))
					reason = "Invalid Pickup OTP";
				else
					reason = "Invalid OTP";
			}
		} catch (Exception e) {
			e.printStackTrace();
			reason = "failed";
		}
		obj.put("reason", reason);
		return obj;
	}

	public long assignedTodeliveryUser(long orderId, long rejectUserId, JSONObject json) {
		long userId = 0;
		try {
			if (json == null || !GeneralUtil.objToString(json.get("del_opt")).equalsIgnoreCase("self")) {
				String sql = "select c.user_id,a.seller_id,a.delivery_charge,c.user_latest_info_id,fun_geo_deviation(b.lotitude,b.longitude,c.latitude,c.longitude) dis from orders a,sellers b,user_latest_info c,users d "
						+ "where c.user_id=d.user_id and  a.seller_id=b.seller_id and c.is_active=1 and d.delivery_type='Admin' and c.accepted_orders+c.pending_orders<=5 "
						+ "and a.order_id=" + orderId;
				if (rejectUserId > 0)
					sql += " and c.user_id!=" + rejectUserId;

				sql += " order by fun_geo_deviation(b.lotitude,b.longitude,c.latitude,c.longitude) ";

				logger.info("Sql : " + sql);
				List<Object[]> ls = entityManager.createNativeQuery(sql).setMaxResults(1).getResultList();
				if (!ls.isEmpty()) {
					OrderDeliveryUsermap o = new OrderDeliveryUsermap();
					o.setOrderDeliveryUserMapId(
							GeneralUtil.getPrimaryKey("SEQ_ORDER_DELIVERY_USER_MAP", entityManager));
					o.setOrderId(orderId);
					o.setUserId(GeneralUtil.objTolong(ls.get(0)[0]));
					o.setAssignedStatus("assigned");
					o.setAssignedDate(new Date());
					o.setSellerId(GeneralUtil.objTolong(ls.get(0)[1]));
					o.setDeliveryCharges(GeneralUtil.objToDouble(ls.get(0)[2]));
					entityManager.persist(o);
					sql = "update user_latest_info a set a.notification_status=1,a.accepted_orders=a.accepted_orders+1 where a.user_latest_info_id="
							+ GeneralUtil.objTolong(ls.get(0)[3]);
					logger.info("Sql : " + sql);
					entityManager.createNativeQuery(sql).executeUpdate();
					userId = o.getUserId();
				}
			} else {
				JSONObject sel = (JSONObject) json.get("sel_user");
				OrderDeliveryUsermap o = new OrderDeliveryUsermap();
				o.setOrderDeliveryUserMapId(GeneralUtil.getPrimaryKey("SEQ_ORDER_DELIVERY_USER_MAP", entityManager));
				o.setOrderId(orderId);
				o.setUserId(GeneralUtil.objTolong(sel.get("user_id")));
				o.setAssignedStatus("Accept");
				o.setAcceptedDate(new Date());
				o.setPickupOtp(GeneralUtil.otpGenerate(1001, 9999));
				o.setAssignedDate(new Date());
				o.setSellerId(GeneralUtil.objTolong(sel.get("seller_id")));
				o.setDeliveryCharges(0);
				entityManager.persist(o);
				String sql = "update user_latest_info a set a.notification_status=1,a.accepted_orders=a.accepted_orders+1 where a.user_id="
						+ o.getUserId();
				logger.info("Sql : " + sql);
				entityManager.createNativeQuery(sql).executeUpdate();
				userId = o.getUserId();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return userId;
	}

	public boolean checkOptValidation(long orderId, int otp) {
		boolean result = false;
		try {
			String sql = "select a.order_id from orders a where a.order_id=" + orderId + " and a.otp=" + otp;
			logger.info("SQl : " + sql);
			List<Object> ls = entityManager.createNativeQuery(sql).getResultList();
			if (!ls.isEmpty()) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean checkPickOtpValidation(long odum, long otp) {
		boolean result = false;
		try {
			String sql = "select a.order_delivery_user_map_id from order_delivery_user_map a where a.order_delivery_user_map_id="
					+ odum + " and a.pickup_otp=" + otp;
			logger.info("SQl : " + sql);
			List<Object> ls = entityManager.createNativeQuery(sql).getResultList();
			if (!ls.isEmpty()) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getNextStatus(String status, String delType) {
		if (status.equalsIgnoreCase("Order Placed")) {
			return "Accepted";
		} else if (delType.equalsIgnoreCase("delivery")) {
			if (status.equalsIgnoreCase("Accepted")) {
				return "Assigned";
			} else if (status.equalsIgnoreCase("Assigned")) {
				return "Out For Delivery";
			} else if (status.equalsIgnoreCase("Out For Delivery")) {
				return "Delivered";
			}
		} else {
			if (status.equalsIgnoreCase("Accepted")) {
				return "Ready For Pickup";
			} else if (status.equalsIgnoreCase("Ready For Pickup")) {
				return "Delivered";
			}
		}
		return null;
	}

	public JSONObject getOrdersDetails(JSONObject json) {
		JSONObject result = new JSONObject();
		try {

			if (json.containsKey("order_id")) {
				String sql = "select a.order_id,a.name,a.order_no,a.transaction_no,to_char(a.order_date,'dd-mm-yyyy HH24:mi') order_date,to_char(a.delivery_date,'dd-mm-yyyy') delivery_date,a.image,a.final_price,a.status,a.track_status,a.address,to_char(a.act_delivery_date,'dd-mm-yyyy') act_delivery_date, "
						+ " b.first_name||' '||b.last_name full_name,b.moblie_no,a.price,a.payment_type,a.discount,a.delivery_type,a.seller_id,a.otp,a.DELIVERY_CHARGE,du.assign_status,dus.first_name,du.accepted_date,du.pick_up_date from orders a,users b,order_delivery_user_map du,users dus where a.user_id=b.user_id and a.order_id=du.order_id(+)  and 'Reject'!=du.assign_status(+) and du.user_id=dus.user_id(+) and a.order_id="
						+ json.get("order_id").toString();
				JSONArray columns = GeneralUtil.getQueryColumns(sql, entityManager, null);
				List<Object[]> ls = entityManager.createNativeQuery(sql).getResultList();
				result = (JSONObject) GeneralUtil.resultToJSONArray(columns, ls).get(0);
				result.put("items", getOrderItems(json));
				result.put("track", getOrderTrack(GeneralUtil.objTolong(json.get("order_id")),
						result.get("delivery_type").toString()));
				result.put("seller", getSellerDetails(GeneralUtil.objTolong(result.get("seller_id"))));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public JSONObject getSellerDetails(long sellerId) {
		JSONObject result = new JSONObject();
		try {

			if (sellerId > 0) {
				String sql = "select a.seller_id,a.shop_name,a.mobile_no,a.licence_no,a.address||', '||a.city||', '||a.state||'-'||a.pin_code address,a.lotitude,a.longitude "
						+ " from sellers a where a.seller_id=" + sellerId;
				JSONArray columns = GeneralUtil.getQueryColumns(sql, entityManager, null);
				List<Object[]> ls = entityManager.createNativeQuery(sql).getResultList();
				result = (JSONObject) GeneralUtil.resultToJSONArray(columns, ls).get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public JSONArray getOrderTrack(long orderId, String type) {

		JSONArray result = new JSONArray();
		try {
			JSONArray tempJson = ReadJsonFile
					.getJSONObject(type.equalsIgnoreCase("Delivery") ? "deliverytrack" : "pickuptrack");
			String sql = "From OrderTrack a where a.orderId=" + orderId + " order by a.onDate";
			logger.info("Sql " + sql);
			List<OrderTrack> ls = entityManager.createQuery(sql).getResultList();
			for (int i = 0; i < tempJson.size(); i++) {
				JSONObject o = (JSONObject) tempJson.get(i);
				for (OrderTrack or : ls) {
					logger.info("Status " + or.getStatus());
					logger.info("Json Status " + o.get("status").toString());
					if (or.getStatus().equalsIgnoreCase(o.get("status").toString())) {
						o.put("date", new SimpleDateFormat("dd-MM-yyyy HH:mm").format(or.getOnDate()));
						o.put("description", or.getDescription());
						o.put("color", "#0ec9e6");
						break;
					}
				}
				result.add(o);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Track " + result.toString());
		return result;
	}

	public JSONArray getOrderItems(JSONObject json) {
		JSONArray result = new JSONArray();
		try {

			if (json.containsKey("order_id")) {
				String sql = "select a.order_item_id,a.stock_item_id,b.name,a.image,a.quantity,a.price,a.diccount,a.final_price,c.name cat_name,a.pack pack_size,b.uom,a.pack pack_uom from order_items a,stock_items b,category c "
						+ "where a.stock_item_id=b.stock_item_id and b.category_id=c.category_id and a.order_id="
						+ json.get("order_id").toString();
				JSONArray columns = GeneralUtil.getQueryColumns(sql, entityManager, null);
				List<Object[]> ls = entityManager.createNativeQuery(sql).getResultList();
				result = GeneralUtil.resultToJSONArray(columns, ls);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public JSONArray getOrdersForProcess(JSONObject filters) {
		JSONArray result = new JSONArray();
		try {

			String sql = "select a.order_id,a.order_no,a.name,fun_time(a.order_date) order_date,a.address,fun_date(a.delivery_date) delivery_date,b.moblie_no,"
					+ " a.final_price,a.delivery_type,a.status,s.shop_name from orders a,users b,sellers s where a.user_id=b.user_id and a.seller_id=s.seller_id ";

			if (filters.containsKey("mobile_no") && filters.get("mobile_no").toString().length() > 0) {
				sql += " and b.moblie_no='" + filters.get("mobile_no").toString() + "'";
			}

			if (filters.containsKey("status") && filters.get("status").toString().length() > 0) {
				sql += " and a.status='" + filters.get("status").toString() + "'";
			}
			if (filters.containsKey("order_no") && filters.get("order_no").toString().length() > 0) {
				sql += " and a.order_no='" + filters.get("order_no").toString() + "'";
			}
			if (filters.containsKey("seller_id") && GeneralUtil.objTolong(filters.get("seller_id")) > 0) {
				sql += " and a.seller_id=" + filters.get("seller_id").toString();
			}
			if (filters.containsKey("stockhier") && filters.get("stockhier").toString().length() > 0) {
				sql += " and a.order_id in (select DISTINCT x.order_id from order_items x,stock_items y  where x.stock_item_id=y.stock_item_id\r\n"
						+ "and y.hierarchy like '"+filters.get("stockhier").toString()+"%') ";
			}
			
			if (filters.containsKey("from_date") && GeneralUtil.objToDate(filters.get("from_date")) != null) {
				sql += " and trunc(a.order_date) between to_date('" + filters.get("from_date").toString()
						+ "','dd-mm-yyyy') and to_date('" + filters.get("to_date").toString() + "','dd-mm-yyyy') ";
			}

			sql += " order by  ";

			if (filters.containsKey("orderby") && filters.get("orderby") != null
					&& filters.get("orderby").toString().length() > 0) {
				String orderBy = filters.get("orderby").toString();
				sql += (orderBy.equals("oda") ? " a.order_date "
						: orderBy.equals("odd") ? " a.order_date desc "
								: orderBy.equals("dda") ? " a.delivery_date " : " a.delivery_date desc ")
						+ " ";
			} else {
				sql += " a.order_date desc ";
			}

			logger.info("SQl : " + sql);
			Query q = entityManager.createNativeQuery(sql);

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
			JSONArray columns = GeneralUtil.getQueryColumns(sql, entityManager, null);
			List<Object[]> ls = q.getResultList();
			result = GeneralUtil.resultToJSONArray(columns, ls);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public JSONArray getSellerDetails(JSONArray item, long delId, String key, int pageSize) {
		JSONArray result = new JSONArray();
		try {

			String sql = "select a.latitude,a.longitude from delivery_address a where a.delivery_add_id=" + delId;
			List<Object[]> ls = entityManager.createNativeQuery(sql).getResultList();
			if (!ls.isEmpty()) {

				String ids = GeneralUtil.stringFromArray(item, key, 1);
				String sql1 = "select mm.seller_id,mm.shop_name,mm.image,mm.no_of_items,case when mm.dist<=0 then 0 else round(mm.dist/1000,1) end Dist from ( "
						+ " select a.seller_id,b.shop_name,b.image,count(*)no_of_items,fun_geo_deviation("
						+ ls.get(0)[0] + "	," + ls.get(0)[1]
						+ ",b.lotitude,b.longitude) dist,b.lotitude,b.longitude from seller_stock_map a,sellers b  "
						+ " where a.stock_item_id in (" + ids
						+ ") and a.seller_id=b.seller_id GROUP by a.seller_id,b.shop_name,b.image,b.lotitude,b.longitude "
						+ " order by 4 desc,5 ) mm ";
				JSONArray columns = GeneralUtil.getQueryColumns(sql1, entityManager, null);
				logger.info("sql1 : " + sql1);
				Query q = entityManager.createNativeQuery(sql1);
				if (pageSize > 0) {
					q.setMaxResults(pageSize);
				}
				List<Object[]> data = q.getResultList();
				result = GeneralUtil.resultToJSONArray(columns, data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String salesStockSql(String latitude, String longitude, long sellerId, boolean isSeller) {
		String sql = "select * from (  " + "select   " + "ss.stock_item_id,  " + "'Consumer' user_type,  "
				+ "si.code,  " + "si.name,  " + "si.category_id,  " + "d.name category_name,  " + "d.order_by,  "
				+ "si.sub_cat_id,  " + "e.name sub_cat_name,  " + "si.brand_id,  " + "f.name brand_name,  "
				+ "si.description,  " + "si.pack_size,  " + "si.image image,       " + "si.uom uom,       "
				+ "ss.price price,  " + " 0  discount_price,  "
				+ "'false' isdiscount,  " + "ss.quantity,  "
				+ "si.rating,  " + "'INSTOCK' inventory_status,  " + "si.status,  " + "'closed' sync_status,  "
				+ "mm.shop_name,mm.seller_id,round(mm.dist/1000) dist,mm.seller_image,  "
				+ "row_number() OVER (PARTITION BY ss.stock_item_id ORDER BY mm.dist) rank_nu  " + "from   "
				+ "(select a.seller_id,a.shop_name,a.address,round(fun_geo_deviation(" + latitude + "," + longitude
				+ ",a.lotitude,a.longitude))   "
				+ "dist,b.distance_km,a.image seller_image from sellers a,gen_settings b  "
				+ "where a.seller_id=b.seller_id(+)  " + "and round(fun_geo_deviation(" + latitude + "," + longitude
				+ ",a.lotitude,a.longitude)/1000)<=decode(nvl(b.distance_km,0),0,500,b.distance_km) ";
		if (sellerId > 0)
			sql += " and a.seller_id=" + sellerId;
		sql += " and trunc(nvl(a.expired_date,sysdate-1))>=trunc(sysdate) ) mm,  "
				+ "stock_items si,seller_stock_map ss,category d,sub_category e,brands f where ss.seller_id=mm.seller_id  "
				+ "and si.stock_item_id=ss.stock_item_id and si.category_id=d.category_id  and si.sub_cat_id=e.sub_cat_id    and si.brand_id=f.brand_id(+)) nn  ";
		if (!isSeller)
			sql += "where nn.rank_nu=1 ";
		return sql;
	}

}
