package com.aqua.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.aqua.dao.intf.ReportsDaoIntf;
import com.aqua.file.ExcelReadAndWrite;
import com.aqua.service.intf.ReportsServiceIntf;
import com.aqua.util.GeneralUtil;

@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class ReportsServiceImpl implements ReportsServiceIntf {

	@Autowired
	private ReportsDaoIntf reportsDaoIntf;

	@Override
	public JSONObject getDashBoardData(JSONObject json) {
		JSONObject result = new JSONObject();
		if (!json.containsKey("chartType") || json.get("chartType") == null) {
			List<Object[]> ls = reportsDaoIntf.getDashBoardData(json);

			if (!ls.isEmpty()) {
				result.put("total_amt", ls.get(0)[0].toString());
				result.put("total_orders", ls.get(0)[1].toString());
				result.put("completed_orders", ls.get(0)[2].toString());
				result.put("pending_orders", GeneralUtil.objTolong(ls.get(0)[1]) - GeneralUtil.objTolong(ls.get(0)[2]));
			} else {
				result.put("total_amt", 0);
				result.put("total_orders", 0);
				result.put("completed_orders", 0);
				result.put("pending_orders", 0);
			}
			result.put("order_value", reportsDaoIntf.getMonthViseSalesByValue(json));
			result.put("order_count", reportsDaoIntf.getMonthViseSalesByCount(json));
			result.put("order_status", reportsDaoIntf.getMonthViseOrderStatus(json));
			result.put("top_items", reportsDaoIntf.gettopItemsa(json));
		} else {
			if (GeneralUtil.objToString(json.get("chartType")).equalsIgnoreCase("order_count"))
				result.put("order_count", reportsDaoIntf.getMonthViseSalesByCount(json));
			else if (GeneralUtil.objToString(json.get("chartType")).equalsIgnoreCase("order_value"))
				result.put("order_value", reportsDaoIntf.getMonthViseSalesByValue(json));
			else if (GeneralUtil.objToString(json.get("chartType")).equalsIgnoreCase("order_status"))
				result.put("order_status", reportsDaoIntf.getMonthViseOrderStatus(json));
			else if (GeneralUtil.objToString(json.get("chartType")).equalsIgnoreCase("top_items"))
				result.put("top_items", reportsDaoIntf.gettopItemsa(json));
			else if (GeneralUtil.objToString(json.get("chartType")).equalsIgnoreCase("total_orders")
					|| GeneralUtil.objToString(json.get("chartType")).equalsIgnoreCase("total_amt")
					|| GeneralUtil.objToString(json.get("chartType")).equalsIgnoreCase("completed_orders")) {
				List<Object[]> ls = reportsDaoIntf.getDashBoardData(json);
				if (!ls.isEmpty()) {
					if (GeneralUtil.objToString(json.get("chartType")).equalsIgnoreCase("total_orders"))
						result.put("total_orders", ls.get(0)[1].toString());
					if (GeneralUtil.objToString(json.get("chartType")).equalsIgnoreCase("total_amt"))
						result.put("total_amt", ls.get(0)[0].toString());
					if (GeneralUtil.objToString(json.get("chartType")).equalsIgnoreCase("completed_orders"))
						result.put("completed_orders", ls.get(0)[2].toString());
				} else
					result.put(GeneralUtil.objToString(json.get("chartType")), 0);

			}
		}
		return result;
	}

	public JSONArray getDynamicReports(JSONObject json) {
		return reportsDaoIntf.getDynamicReports(json);
	}

	public JSONObject getReportJSON(JSONObject json) {
		return reportsDaoIntf.getReportJSON(json);
	}

	public JSONArray getReportData(JSONObject json) {
		return (JSONArray) reportsDaoIntf.getReportData(json).get("data");
	}

	public static void generateReport(List<List<String>> data, String fileName) throws Exception {
		FileInputStream inputStream = new FileInputStream("mytemplate.xlsx");
		XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
		inputStream.close();
		SXSSFWorkbook wb = new SXSSFWorkbook(wb_template);
		wb.setCompressTempFiles(true);
		SXSSFSheet sh = (SXSSFSheet) wb.getSheetAt(0);
		sh.setRandomAccessWindowSize(100);// keep 100 rows in memory,
		int rows = 0;
		for (List<String> key : data) {
			Row row = sh.createRow(rows++);
			int cellnum = 0;
			for (String obj : key) {
				Cell cell = row.createCell(cellnum++);
				cell.setCellType(ExcelReadAndWrite.getCellType("text"));
				if (obj != null)
					cell.setCellValue(obj.toString());
				else
					cell.setCellValue("");
			}
		}
		FileOutputStream out = new FileOutputStream("D:\\BI_FILES\\DEPLITION_FINAL\\" + fileName);
		wb.write(out);
		out.close();
	}

	public File exportExcell(JSONObject json) {
		try {
			Map<String, Object> filter = new HashMap<>();
			Set<String> keys = json.keySet();
			for (String key : keys) {
				filter.put(key, json.get(key));
			}
			int page = 1, size = 100000;

			json.put("size", size);
			json.put("first", page);
			FileInputStream inputStream = new FileInputStream(
					GeneralUtil.getFileFromResource("templates/mytemplate.xlsx"));
			XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
			inputStream.close();
			SXSSFWorkbook wb = new SXSSFWorkbook(wb_template);
			wb.setCompressTempFiles(true);
			SXSSFSheet sh = (SXSSFSheet) wb.getSheetAt(0);
			sh.setRandomAccessWindowSize(100);// keep 100 rows in memory,
			int rows = 0;
			while (true) {
				Map<String, Object> res = reportsDaoIntf.getReportDataMap(json);
				if (rows == 0) {
					JSONArray head = (JSONArray) res.get("headers");
					Row row1 = sh.createRow(rows++);
					for (int i = 0; i < head.size(); i++) {
						JSONObject j = (JSONObject) head.get(i);
						Cell cell = row1.createCell(i);
						cell.setCellType(ExcelReadAndWrite.getCellType("text"));
						cell.setCellValue(j.get("key").toString());
					}
				}
				List<Object[]> data = (List<Object[]>) res.get("data");
				for (Object[] key : data) {
					Row row = sh.createRow(rows++);
					int cellnum = 0;
					for (Object obj : key) {
						Cell cell = row.createCell(cellnum++);
						cell.setCellType(ExcelReadAndWrite.getCellType("text"));
						if (obj != null)
							cell.setCellValue(obj.toString());
						else
							cell.setCellValue("");
					}
				}
				page += size;
				json.put("first", page);
				if (data.size() != size) {
					break;
				}
			}
			String path = GeneralUtil.getPropertyValue("report_downlod") + "mycart/downloads/"
					+ json.get("name").toString() + ".xlsx";
			File file = new File(path);
			FileOutputStream out = new FileOutputStream(file);
			wb.write(out);
			out.close();
			return file;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject getReportDashBoards(JSONObject json) {
		return reportsDaoIntf.getReportDashBoardJSON(json);
	}

}
