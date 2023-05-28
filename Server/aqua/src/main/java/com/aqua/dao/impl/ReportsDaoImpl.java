/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aqua.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.aqua.dao.intf.ReportsDaoIntf;
import com.aqua.util.GeneralUtil;
import com.aqua.util.ReportUtil;

import netscape.javascript.JSObject;

/**
 *
 * @author USER
 */
@Repository
public class ReportsDaoImpl implements ReportsDaoIntf {

	private static final Logger logger = LogManager.getLogger(ReportsDaoImpl.class);
	@Autowired
	private EntityManager entityManager;

	@Override
	public List<Object[]> getDashBoardData(JSONObject json) {
		List<Object[]> ls = null;
		try {
			long sellerId = GeneralUtil.objTolong(json.get("seller_id"));
			String sql = "select nvl(sum(b.final_price),0) total_amt,count(DISTINCT a.order_id) total_orders,nvl(count("
					+ " DISTINCT case when a.status='Delivered' then"
					+ "  a.order_id end "
					+ " ),0) total_del "
					+ " from orders a,order_items b,stock_items c  "
					+ " where a.order_id=b.order_id and b.stock_item_id=c.stock_item_id and  a.order_date>=sysdate-365 ";
			if (sellerId > 0)
				sql += " and a.seller_id=" + sellerId;
			if (json.containsKey("stockhier") && json.get("stockhier").toString().length() > 0) {
				sql += " and c.hierarchy like '"+json.get("stockhier").toString()+"%' ";
			}
			logger.info("Sql : " + sql);
			ls = entityManager.createNativeQuery(sql).getResultList();
			logger.info("ls : " + ls.size());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ls;
	}

	@Override
	public JSONArray gettopItemsa(JSONObject json) {
		JSONArray result = new JSONArray();
		try {
			long sellerId = GeneralUtil.objTolong(json.get("seller_id"));
			String sql = "select c.image,c.name,sum(b.quantity) coun,sum(b.final_price) amt from orders a,order_items b,stock_items c where a.order_id=b.order_id and b.stock_item_id=c.stock_item_id and a.order_date>=sysdate-365 ";
			if (sellerId > 0)
				sql += " and a.seller_id=" + sellerId;
			if (json.containsKey("stockhier") && json.get("stockhier").toString().length() > 0) {
				sql += " and c.hierarchy like '"+json.get("stockhier").toString()+"%' ";
			}

			sql += " group by c.image,c.name order by 4 desc ";
			logger.info("Sql : " + sql);
			JSONArray columns = GeneralUtil.getQueryColumns(sql, entityManager, null);
			Query q = entityManager.createNativeQuery(sql);
			if (json.containsKey("size")) {
				q.setMaxResults(GeneralUtil.objToInt(json.get("size")));
			}
			List<Object[]> ls = q.getResultList();
			result = GeneralUtil.resultToJSONArray(columns, ls);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public JSONObject getMonthViseSalesByValue(JSONObject json) {
		JSONObject result = new JSONObject();
		try {
			long sellerId = GeneralUtil.objTolong(json.get("seller_id"));
			String a = GeneralUtil.dateChartString(GeneralUtil.objToString(json.get("chartBy")));
			String b = GeneralUtil.dateChartDisplayString(GeneralUtil.objToString(json.get("chartBy")));
			int c = GeneralUtil.dateChartDays(GeneralUtil.objToString(json.get("chartBy")));
			String sql = "select mm.mon,'Amount',mm.cont from ( " + "select to_char(a.order_date,'" + a
					+ "') ,to_char(a.order_date,'" + b
					+ "') mon,sum(b.final_price) cont from orders a,    order_items b,    stock_items c where a.order_id=b.order_id and b.stock_item_id=c.stock_item_id and a.order_date>=sysdate-" + c;

			if (sellerId > 0)
				sql += " and a.seller_id=" + sellerId;
			if (json.containsKey("stockhier") && json.get("stockhier").toString().length() > 0) {
				sql += " and c.hierarchy like '"+json.get("stockhier").toString()+"%' ";
			}

			sql += " GROUP by to_char(a.order_date,'" + a + "'),to_char(a.order_date,'" + b + "') order by 1) mm";
			logger.info("Sql : " + sql);
			List<Object[]> ls = entityManager.createNativeQuery(sql).getResultList();
			result = GeneralUtil.barOrLineChart(ls);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	public JSONObject getMonthViseSalesByCount(JSONObject json) {
		JSONObject result = new JSONObject();
		try {
			long sellerId = GeneralUtil.objTolong(json.get("seller_id"));
			String a = GeneralUtil.dateChartString(GeneralUtil.objToString(json.get("chartBy")));
			String b = GeneralUtil.dateChartDisplayString(GeneralUtil.objToString(json.get("chartBy")));
			int c = GeneralUtil.dateChartDays(GeneralUtil.objToString(json.get("chartBy")));
			String sql = "select mm.mon,'Count',mm.cont from ( " + "select to_char(a.order_date,'" + a
					+ "') ,to_char(a.order_date,'" + b
					+ "') mon,count(*) cont from orders a where a.order_date>=sysdate-" + c;

			if (sellerId > 0)
				sql += " and a.seller_id=" + sellerId;
			if (json.containsKey("stockhier") && json.get("stockhier").toString().length() > 0) {
				sql += " and a.order_id in (select DISTINCT x.order_id from order_items x,stock_items y  where x.stock_item_id=y.stock_item_id\r\n"
						+ "and y.hierarchy like '"+json.get("stockhier").toString()+"%') ";
			}

			sql += " GROUP by to_char(a.order_date,'" + a + "'),to_char(a.order_date,'" + b + "') order by 1) mm";
			logger.info("Sql : " + sql);
			List<Object[]> ls = entityManager.createNativeQuery(sql).getResultList();
			result = GeneralUtil.barOrLineChart(ls);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	public JSONObject getMonthViseOrderStatus(JSONObject json) {
		JSONObject result = new JSONObject();
		try {
			long sellerId = GeneralUtil.objTolong(json.get("seller_id"));
			String a = GeneralUtil.dateChartString(GeneralUtil.objToString(json.get("chartBy")));
			String b = GeneralUtil.dateChartDisplayString(GeneralUtil.objToString(json.get("chartBy")));
			int c = GeneralUtil.dateChartDays(GeneralUtil.objToString(json.get("chartBy")));
			String sql = "select mm.mon,mm.status,mm.cont from ( " + "select to_char(a.order_date,'" + a
					+ "') ,to_char(a.order_date,'" + b
					+ "') mon,a.status,count(*) cont from orders a where a.order_date>=sysdate-" + c;

			if (sellerId > 0)
				sql += " and a.seller_id=" + sellerId;
			if (json.containsKey("stockhier") && json.get("stockhier").toString().length() > 0) {
				sql += " and a.order_id in (select DISTINCT x.order_id from order_items x,stock_items y  where x.stock_item_id=y.stock_item_id\r\n"
						+ "and y.hierarchy like '"+json.get("stockhier").toString()+"%') ";
			}

			sql += " GROUP by to_char(a.order_date,'" + a + "'),to_char(a.order_date,'" + b
					+ "'),a.status order by 1) mm";
			logger.info("Sql : " + sql);
			List<Object[]> ls = entityManager.createNativeQuery(sql).getResultList();
			result = GeneralUtil.barOrLineChart(ls);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	public JSONArray getDynamicReports(JSONObject json) {
		JSONArray result = new JSONArray();
		try {
			long sellerId = GeneralUtil.objTolong(json.get("seller_id"));
			String sql = "select a.group_name,a.dynamic_reports_id,a.name,a.description,nvl(a.chart_type,'table') chart_type from dynamic_reports a where a.parent_id=0";
			if (sellerId > 0)
				sql += " and  (a.report_type=0 or a.report_type=2) ";
			else
				sql += " and  (a.report_type=0 or a.report_type=1) ";
			if (GeneralUtil.objToString(json.get("report_name")).length() > 0) {
				sql += " and lower(a.name) like '%'||:report_name||'%'";
			}

			sql += " order by a.order_by ";
			logger.info("Sql : " + sql);
			JSONArray columns = GeneralUtil.getQueryColumns(sql, entityManager, null);
			Query q = entityManager.createNativeQuery(sql);
			if (GeneralUtil.objToString(json.get("report_name")).length() > 0) {
				q.setParameter("report_name", GeneralUtil.objToString(json.get("report_name")).toLowerCase());
			}
			List<Object[]> ls = q.getResultList();
			JSONArray rep = GeneralUtil.resultToJSONArray(columns, ls);
			Map<String, JSONArray> rmap = new HashMap<>();
			for (int i = 0; i < rep.size(); i++) {
				JSONObject a = (JSONObject) rep.get(i);
				String gn = GeneralUtil.objToString(a.get("group_name"));
				if (rmap.containsKey(gn)) {
					JSONArray ja = rmap.get(gn);
					ja.add(a);
					rmap.put(gn, ja);
				} else {
					JSONArray ja = new JSONArray();
					ja.add(a);
					rmap.put(gn, ja);
				}
			}

			for (String s : rmap.keySet()) {
				JSONObject o = new JSONObject();
				o.put("name", s);
				o.put("reports", rmap.get(s));
				result.add(o);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public JSONObject getReportJSON(JSONObject json) {
		JSONObject result = new JSONObject();
		try {
			int id = GeneralUtil.objToInt(json.get("dynamic_reports_id"));
			int seller = GeneralUtil.objToInt(json.get("seller_id"));
			String sql = "select a.name,a.description,a.query,a.params,a.data_view,a.data_export,a.data_chart,nvl(a.chart_type,'table') chart_type,a.others from dynamic_reports a where a.dynamic_reports_id="
					+ id;
			List<Object[]> ls = entityManager.createNativeQuery(sql).getResultList();
			JSONArray columns = GeneralUtil.getQueryColumns(sql, entityManager, null);
			JSONArray rep = GeneralUtil.resultToJSONArray(columns, ls);
			result = (JSONObject) rep.get(0);
			result.put("form", reportFilterJson(result.get("params").toString(), seller,
					result.get("others").toString(),id));
			JSONArray headers = GeneralUtil.getQueryColumnsNoLower(result.get("query").toString(), entityManager, null);
			result.put("header", headers);
			logger.info("JSON : " + result);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	public  JSONObject reportFilterJson(String params, int sellerId, String others,long drId) {
		JSONObject form = new JSONObject();
		if (params != null) {
			String a[] = params.split(",");
			JSONObject op=ReportUtil.otherValues(others);
			for (String b : a) {
				String[] c = b.split("=");
				if (c[1].equalsIgnoreCase("date")) {
					form.put(c[0], ReportUtil.dateJson(c[0]));
				}  else if (c[1].equalsIgnoreCase("text")) {
					form.put(c[0], ReportUtil.dateJson(c[0]));
				}else if (c[1].equalsIgnoreCase("other")) {
					form.put(c[0], ReportUtil.otherDropDownJson(c[0],(JSONArray)op.get(c[0])));
				}else if (c[1].equalsIgnoreCase("sh")) {
					form.put(c[0], ReportUtil.stockHierarchy(c[0],(JSONArray)op.get(c[0])));
				}
				else if (c[1].equalsIgnoreCase("seller")) {
					JSONObject date = new JSONObject();
					date.put("type", "hidden");
					date.put("hidde", true);
					date.put("value", sellerId>0?sellerId:null);
					form.put(c[0], date);
				}else if (c[1].equalsIgnoreCase("byquery")) {
					form.put(c[0], ReportUtil.otherDropDownJson(c[0],getSubQueryData(drId,c[0],form)));
				} 
			}
		}
		return form;
	}

	public JSONObject getReportData(JSONObject json) {
		JSONObject result = new JSONObject();
		try {
			Map<String, Object> map = getReportDataMap(json);
			if (!map.isEmpty()) {
				List<Object[]> d = (List<Object[]>) map.get("data");
				JSONArray headers = (JSONArray) map.get("headers");
				String type = GeneralUtil.objToString(json.get("chart_type"));
				logger.info(" d : " + d.size() + " " + type);
				if (type.equalsIgnoreCase("table")) {
					JSONArray data = GeneralUtil.resultToJSONArray(headers, d);
					result.put("data", data);
				} else if (type.equalsIgnoreCase("bar") || type.equalsIgnoreCase("line")) {

					JSONObject cj = GeneralUtil.barOrLineChart(d);
					JSONArray ar = new JSONArray();
					ar.add(cj);
					result.put("data", ar);
				} else if (type.equalsIgnoreCase("pie") || type.equalsIgnoreCase("doughnut")) {
					JSONObject o = (JSONObject) headers.get(1);
					JSONObject cj = GeneralUtil.pieOrDoughnutChart(d, GeneralUtil.objToString(o.get("key")));
					JSONArray ar = new JSONArray();
					ar.add(cj);
					result.put("data", ar);
				}
				result.put("headers", headers);
				logger.info(" result : " + result.toString());
			} else {
				result.put("data", new JSONArray());
				result.put("headers", new JSONArray());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Map<String, Object> getReportDataMap(JSONObject json) {
		Map<String, Object> result = new HashMap<>();
		try {
			int id = GeneralUtil.objToInt(json.get("dynamic_reports_id"));
			int seller = GeneralUtil.objToInt(json.get("seller_id"));
			String sql = "select a.query,a.params,nvl(a.chart_type,'table') chart_type from dynamic_reports a where a.dynamic_reports_id="
					+ id;
			List<Object[]> ls = entityManager.createNativeQuery(sql).getResultList();
			if (!ls.isEmpty()) {
				String query = GeneralUtil.blobToString(ls.get(0)[0]);
				String params = GeneralUtil.objToStringNull(ls.get(0)[1]);
				String type = GeneralUtil.objToString(ls.get(0)[2]);
				JSONArray headers = GeneralUtil.getQueryColumnsReports(query, entityManager, null);
				JSONObject filter = (JSONObject) json.get("filters");
				//logger.info("filter  : " + filter.toString());
				Map<String, Object> par = ReportUtil.paramValues(params, filter);

				Query q = entityManager.createNativeQuery(query);
				
				
				
				
				if (par != null) {
					for (String s : par.keySet()) {
						logger.info(s + " : " + par.get(s));
						if (par.get(s) != null)
							q.setParameter(s, par.get(s));
						else {
							logger.info(s + " : null " + par.get(s));
							q.setParameter(s,"");
						}
					}
				}
				int pageSize = GeneralUtil.objToInt(json.get("size"));
				int first = GeneralUtil.objToInt(json.get("first"));
				if (pageSize > 0) {
					q.setFirstResult(first);
					q.setMaxResults(pageSize);
				}
				List<Object[]> d = q.getResultList();
				result.put("data", d);
				result.put("headers", headers);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public JSONObject getReportDashBoardJSON(JSONObject json) {
		JSONObject result = new JSONObject();
		try {

			int id = GeneralUtil.objToInt(json.get("dynamic_reports_id"));
			int selectId = GeneralUtil.objToInt(json.get("selected_id"));
			int seller = GeneralUtil.objToInt(json.get("seller_id"));
			String sql = "select a.DYNAMIC_REPORTS_ID,nvl(a.chart_type,'table') chart_type from dynamic_reports a where a.parent_id="
					+ id;
			if (selectId > 0)
				sql += " and a.DYNAMIC_REPORTS_ID=" + selectId;
			sql += " order by a.ORDER_BY";
			logger.info("SQL : " + sql);
			List<Object[]> ls = entityManager.createNativeQuery(sql).getResultList();
			JSONArray chars = new JSONArray();
			for (Object[] o : ls) {
				JSONObject temp = (JSONObject) json.clone();
				temp.put("dynamic_reports_id", GeneralUtil.objToInt(o[0]));
				temp.put("chart_type", GeneralUtil.objToString(o[1]));
				chars.add(GeneralUtil.objToInt(o[0]));
				JSONObject r = getReportJSON(temp);
				if (selectId > 0) {
					temp.put("filters", json.get("form"));
				} else {
					temp.put("filters", r.get("form"));
				}
				JSONObject rd = getReportData(temp);
				JSONObject ch = new JSONObject();
				ch.put("form", r.get("form"));
				ch.put("dynamic_reports_id", GeneralUtil.objToInt(o[0]));
				ch.put("chart_type", r.get("chart_type"));
				if (r.get("chart_type").toString().equalsIgnoreCase("table")) {
					ch.put("header", r.get("header"));
					ch.put("chart", rd.get("data"));
				} else {
					ch.put("chart", ((JSONArray) rd.get("data")).get(0));
				}
				ch.put("name", r.get("name"));
				result.put(GeneralUtil.objToInt(o[0]), ch);
			}
			result.put("chart_order", chars);
			logger.info("result : " + result.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public JSONArray getSubQueryData(long drId, String paramName, JSONObject filter) {
		JSONArray array = new JSONArray();
		try {
			String sql = "select a.query,a.params from dynamic_report_sub a where a.param_name='"
					+ paramName.toLowerCase() + "' and a.dynamic_report_id=" + drId;
			logger.info(sql + " : " + sql);
			List<Object[]> ls = entityManager.createNativeQuery(sql).getResultList();
			if (!ls.isEmpty()) {
				String query = GeneralUtil.blobToString(ls.get(0)[0]);
				String params = GeneralUtil.objToStringNull(ls.get(0)[1]);
				Map<String, Object> par = ReportUtil.paramValues(params, filter);
				Query q = entityManager.createNativeQuery(query);
				if (par != null) {
					for (String s : par.keySet()) {
						logger.info(s + " : " + par.get(s));
						if (par.get(s) != null)
							q.setParameter(s, par.get(s));
						else
							q.setParameter(s, null);
					}
				}
				JSONArray columns = GeneralUtil.getQueryColumns(query, entityManager, null);
				List<Object[]> d = q.getResultList();
				array = GeneralUtil.resultToJSONArray(columns, d);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info(array + " : " + array.toString());
		return array;
	}

}
