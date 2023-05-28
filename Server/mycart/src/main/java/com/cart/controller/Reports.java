package com.cart.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cart.service.intf.ReportsServiceIntf;
import com.cart.util.AESEncryption;
import com.cart.util.GeneralUtil;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/report")
public class Reports {
	private static final Logger logger = LogManager.getLogger(Reports.class);

	@Autowired
	private ReportsServiceIntf reportsServiceIntf;

	@PostMapping(value = "/get-dashboard", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject addCategory(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {
			result = reportsServiceIntf.getDashBoardData(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reason", "Failed");
		}
		return result;

	}

	@PostMapping(value = "/get-reports", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONArray getReports(@RequestBody String text) {
		JSONArray result = new JSONArray();
		try {
			result = reportsServiceIntf.getDynamicReports(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@PostMapping(value = "/reports-json", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject getReportJson(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {
			result = reportsServiceIntf.getReportJSON(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@PostMapping(value = "/reports-data", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONArray getReportData(@RequestBody String text) {
		JSONArray result = new JSONArray();
		try {
			result = reportsServiceIntf.getReportData(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@PostMapping(value = "/report-export")
	public ResponseEntity<Resource> getFile(@RequestBody String text) {
		try {
			File file = reportsServiceIntf.exportExcell(AESEncryption.decrypt(text));
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
					.body(GeneralUtil.getInputStreamFromPath(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@PostMapping(value = "/reports-dashboard", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject getReportDashBoard(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {
			result = reportsServiceIntf.getReportDashBoards(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
