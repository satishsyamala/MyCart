package com.aqua.service.intf;

import java.io.File;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public interface ReportsServiceIntf {
	public JSONObject getDashBoardData(JSONObject json);
	
	public JSONArray getDynamicReports(JSONObject json);
	
	public JSONObject getReportJSON(JSONObject json);
	
	public JSONArray getReportData(JSONObject json);
	
	public File exportExcell(JSONObject json);
	
	public JSONObject getReportDashBoards(JSONObject json);
}
