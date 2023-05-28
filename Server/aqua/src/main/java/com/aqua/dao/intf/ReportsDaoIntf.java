package com.aqua.dao.intf;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public interface ReportsDaoIntf {
	public List<Object[]> getDashBoardData(JSONObject json);
	
	public JSONObject getMonthViseSalesByValue(JSONObject json);
	
	public JSONObject getMonthViseSalesByCount(JSONObject json);
	
	public JSONObject getMonthViseOrderStatus(JSONObject json);
	
	public JSONArray gettopItemsa(JSONObject json);
	
	public JSONArray getDynamicReports(JSONObject json);
	
	public JSONObject getReportJSON(JSONObject json);
	
	public JSONObject getReportData(JSONObject json);
	
	public  Map<String,Object> getReportDataMap(JSONObject json);
	
	public JSONObject getReportDashBoardJSON(JSONObject json);
}
