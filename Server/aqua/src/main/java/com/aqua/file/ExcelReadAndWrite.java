/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aqua.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.aqua.util.GeneralUtil;

/**
 *
 * @author USER
 */
public class ExcelReadAndWrite {

	public static List<List<String>> getExcelXLSX(File file) {
		List<List<String>> data = new ArrayList<List<String>>();
		try {
			FileInputStream fis = new FileInputStream(file);
			HSSFWorkbook wb = new HSSFWorkbook(fis);
			HSSFSheet mySheet = wb.getSheetAt(0);
			Iterator<Row> rowIterator = mySheet.iterator();
			int colCount = 0;
			while (rowIterator.hasNext()) {
				HSSFRow row = (HSSFRow) rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				List<String> d = new ArrayList<>();
				if (colCount == 0) {
					colCount = row.getLastCellNum();
				}
				for (int i = 0; i < colCount; i++) {
					HSSFCell cell = row.getCell(i, MissingCellPolicy.CREATE_NULL_AS_BLANK);
					String value = "";
					switch (cell.getCellType()) {
					case STRING:
						value = cell.getStringCellValue();
						break;
					case NUMERIC:
						value = cell.getNumericCellValue() + "";
						break;
					case BOOLEAN:
						value = cell.getBooleanCellValue() + "";
						break;
					default:
					}

					d.add(value);
				}
				data.add(d);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;

	}

	public static List<Map> readXLSFileWithBlankCells(File filename) {

		List<Map> data = new ArrayList<Map>();
		InputStream excelFileToRead = null;
		try {
			excelFileToRead = new FileInputStream(filename);
			HSSFWorkbook wb = new HSSFWorkbook(excelFileToRead);
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow row;
			HSSFCell cell;
			Iterator rows = sheet.rowIterator();
			int colCount = 0;
			List<String> headers = new ArrayList<String>();
			int headCount = 0;
			int rowsCount = 0;
			while (rows.hasNext()) {
				Map map = new HashMap<String, String>();
				row = (HSSFRow) rows.next();
				if (colCount == 0) {
					colCount = row.getLastCellNum();
				}
				int validRow = 0;
				for (int i = 0; i < colCount; i++) {
					String result = "";
					cell = row.getCell(i, MissingCellPolicy.CREATE_NULL_AS_BLANK);
					cell.setCellType(getCellType("text"));
					if (cell.getStringCellValue() != null && cell.getStringCellValue().trim().length() > 0) {
						result = cell.getStringCellValue().replaceAll("[\\t\\n\\r]", " ") + "";
					}
					if (headCount == 0) {
						headers.add(result);

					} else {
						if (result != null && result.trim().length() > 0) {
							validRow++;
						}

						map.put(headers.get(i), result.trim());
					}
				}

				headCount = 1;
				if (!map.isEmpty() && validRow > 0) {
					data.add(map);
				}

				rowsCount++;

			}
			wb.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (excelFileToRead != null) {
				try {
					excelFileToRead.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return data;

	}

	public static File createExcelFileWithStyles(List<List<String>> data, String fileName, int headerSize) {
		File file = new File(fileName);
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("sheet");

		HSSFFont headerFont = (HSSFFont) workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 12);

		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setFont(headerFont);
		headerStyle.setBorderBottom(BorderStyle.MEDIUM);
		headerStyle.setBorderTop(BorderStyle.MEDIUM);

		int rownum = 0;
		for (List<String> key : data) {
			Row row = sheet.createRow(rownum++);
			int cellnum = 0;
			for (String obj : key) {
				Cell cell = row.createCell(cellnum++);
				if (rownum < headerSize) {
					cell.setCellStyle(headerStyle);
				}

				cell.setCellType(getCellType("text"));
				if (obj != null) {
					cell.setCellValue(obj);
				} else {
					cell.setCellValue("");
				}
			}
		}
		try {
			FileOutputStream out = new FileOutputStream(new File(fileName));
			workbook.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	public static File createExcelFile(JSONArray columns, List<Object[]> data, String fileName) {
		String path=GeneralUtil.getPropertyValue("report_downlod")+"mycart/downloads/"+fileName;
		File file = new File(path);
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("sheet");

		HSSFFont headerFont = (HSSFFont) workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 12);

		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setFont(headerFont);
		headerStyle.setBorderBottom(BorderStyle.MEDIUM);
		headerStyle.setBorderTop(BorderStyle.MEDIUM);
		int rownum = 0;
		Row row1 = sheet.createRow(rownum++);
		for (int i = 0; i < columns.size(); i++) {
			JSONObject j = (JSONObject) columns.get(i);
			Cell cell = row1.createCell(i);
			cell.setCellType(getCellType("text"));
			cell.setCellValue(j.get("key").toString());
		}

		for (Object[] obj : data) {
			Row row = sheet.createRow(rownum++);
			for (int i = 0; i < columns.size(); i++) {
				String type = ((JSONObject) columns.get(i)).get("type").toString();
				Cell cell = row.createCell(i);
				cell.setCellType(getCellType(type));
				setVauleToCell(obj[i], type, cell);
			}
		}
		try {
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
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
				cell.setCellType(getCellType("text"));
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

	public static void setVauleToCell(Object obj, String type, Cell cell) {
		if (obj != null) {
			if (type.equalsIgnoreCase("text")) {
				cell.setCellValue(GeneralUtil.objToString(obj));
			} else if (type.equalsIgnoreCase("number") || type.equalsIgnoreCase("double")) {
				cell.setCellValue(GeneralUtil.objToDouble(obj));
			} else if (type.equalsIgnoreCase("date")) {
				cell.setCellValue((Date) obj);
			} else {
				cell.setCellValue(obj.toString());
			}
		} else {
			cell.setCellValue("");
		}
	}

	public static CellType getCellType(String type) {
		if (type.equalsIgnoreCase("text")) {
			return CellType.STRING;
		} else if (type.equalsIgnoreCase("number") || type.equalsIgnoreCase("double")) {
			return CellType.NUMERIC;
		}
		return CellType.STRING;
	}

}
