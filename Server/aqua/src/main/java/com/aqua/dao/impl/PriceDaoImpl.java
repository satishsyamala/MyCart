/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aqua.dao.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.aqua.dao.intf.PriceDaoIntf;
import com.aqua.pojo.PriceList;
import com.aqua.pojo.PriceListUserType;
import com.aqua.pojo.PriceListitems;
import com.aqua.util.ConvertJSONtoObject;
import com.aqua.util.GeneralUtil;

/**
 *
 * @author USER
 */
@Repository
public class PriceDaoImpl implements PriceDaoIntf {

	private static final Logger logger = LogManager.getLogger(PriceDaoImpl.class);
	@Autowired
	private EntityManager entityManager;

	public JSONArray getPriceListDetails(JSONObject filters) {
		JSONArray result = new JSONArray();
		try {
			String sql = "select a.price_list_id,a.name,fun_date(a.start_date) start_date,fun_date(a.end_date) end_date,fun_time(a.created_on) created_on,a.created_by,a.status,a.image,a.seller_id "
					+ "  from price_list a  where 1=1 ";

			if (filters.containsKey("seller_id") && GeneralUtil.objTolong(filters.get("seller_id")) > 0) {
				sql += " and a.seller_id=" + filters.get("seller_id").toString();
			}
			if (filters.containsKey("date") && GeneralUtil.objToDate(filters.get("date")) != null) {
				sql += " and  to_date('" + filters.get("date").toString()
						+ "','dd-mm-yyyy') BETWEEN trunc(a.start_date) and trunc(a.end_date) ";
			}
			sql += " order by a.start_date";
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

	public JSONObject savePriceList(JSONObject json) {
		JSONObject result = new JSONObject();
		String reason = "";
		try {

			PriceList p = new PriceList();
			ConvertJSONtoObject.jsonToObject(p, json);
			logger.info("End Date : " + p.getEndDate());
			p.setPriceListId(GeneralUtil.getPrimaryKey("SEQ_PRICE_LIST", entityManager));
			p.setStatus("active");
			if (((JSONObject) json.get("image")).get("value") != null
					&& ((JSONObject) json.get("image")).get("value").toString().length() > 0) {
				String image = GeneralUtil.writeImageFolder(((JSONObject) json.get("image")).get("value").toString(),
						"mycart/offers/"+p.getSellerId(),  p.getPriceListId()+"");
				p.setImage(image);
			}
			entityManager.persist(p);

			if (json.containsKey("items")) {
				JSONArray items = (JSONArray) json.get("items");
				for (int i = 0; i < items.size(); i++) {
					JSONObject item = (JSONObject) items.get(i);
					PriceListitems put = new PriceListitems();
					put.setPriceListItemId(GeneralUtil.getPrimaryKey("SEQ_PRICE_LIST_ITEMS", entityManager));
					put.setPriceListId(p.getPriceListId());
					put.setStockItemId(GeneralUtil.objTolong(item.get("stockItemId")));
					entityManager.persist(put);
				}
			}
			reason = "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.put("reason", reason);
		return result;
	}

	public JSONArray getOffers(JSONObject filters) {
		JSONArray result = new JSONArray();
		try {
			String latitude = "";
			String longitude = "";
			long addressId = 0;
			long userId = GeneralUtil.objTolong(filters.get("user_id"));
			if (GeneralUtil.objToString(filters.get("latitude")).length() > 0
					&& GeneralUtil.objToString(filters.get("longitude")).length() > 0) {
				latitude = GeneralUtil.objToString(filters.get("latitude"));
				longitude = GeneralUtil.objToString(filters.get("longitude"));
			} else {
				String sql1 = "select a.latitude,a.longitude,a.DELIVERY_ADD_ID from delivery_address a "
						+ "where a.user_id=" + userId + " and a.is_default=1 ";
				List<Object[]> ls = entityManager.createNativeQuery(sql1).getResultList();
				if (!ls.isEmpty()) {
					latitude = GeneralUtil.objToString(ls.get(0)[0]);
					longitude = GeneralUtil.objToString(ls.get(0)[1]);
					addressId = GeneralUtil.objTolong(ls.get(0)[2]);
				}
				logger.info("Sql : " + sql1);
			}
			String sql = " SELECT     a.seller_id,    a.shop_name,    a.address,    round(fun_geo_deviation(" + latitude
					+ ", " + longitude + ", a.lotitude, a.longitude)) dist, "
					+ "    b.distance_km,    a.image seller_image,    pl.name, to_char(pl.start_date,'dd-Mon-yyyy') start_date,   to_char(pl.end_date,'dd-Mon-yyyy') end_date, "
					+ "    pl.end_date-trunc(sysdate) exp_days,    pl.image offer_image,    pl.price_list_id,pl.offer_type,pl.amt_per "
					+ "  FROM    sellers      a,    gen_settings b,    price_list pl "
					+ "  WHERE    a.seller_id = b.seller_id (+)    and a.seller_id=pl.seller_id    and trunc(sysdate) between trunc(pl.start_date) and trunc(pl.end_date) "
					+ "    and pl.status='active'    AND round(fun_geo_deviation(" + latitude + ", " + longitude
					+ ", a.lotitude, a.longitude) / 1000) <= decode(nvl(b.distance_km, 0), "
					+ "    0, 500, b.distance_km) ";
			Query q = entityManager.createNativeQuery(sql);
			JSONArray columns = GeneralUtil.getQueryColumns(sql, entityManager, null);
			List<Object[]> ls = q.getResultList();
			result = GeneralUtil.resultToJSONArray(columns, ls);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

}
