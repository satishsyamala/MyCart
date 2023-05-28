/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cart.util;

import com.cart.dao.impl.TransactionsDaoImpl;
import com.cart.file.ExcelReadAndWrite;
import com.cart.file.ReadJsonFile;
import static com.cart.file.ReadJsonFile.getJSONObject;
import com.cart.pojo.OrderTrack;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.util.ResourceUtils;

/**
 *
 * @author USER
 */
public class GeneralUtil {

	public static final String SCHEMA_NAME = "mycart.";
	public static final String DB = "ORACLE";
	private static final Logger logger = LogManager.getLogger(GeneralUtil.class);
	@Autowired
	private static Environment env;

	public static long getPrimaryKey(String seqName, EntityManager entityManager) {
		if (DB.equalsIgnoreCase("PG")) {
			return getPGSequenceNext(seqName, entityManager);
		} else {
			return getOracleSequenceNext(seqName, entityManager);
		}
	}

	private static long getPGSequenceNext(String seqName, EntityManager entityManager) {
		long pk = 0;
		String sql = "SELECT nextval('" + SCHEMA_NAME + "\"" + seqName + "\"')";
		System.out.println(sql);
		List<Object> ls = entityManager.createNativeQuery(sql).getResultList();
		if (!ls.isEmpty()) {
			pk = Long.parseLong(ls.get(0).toString());
		}
		return pk;
	}

	private static long getOracleSequenceNext(String seqName, EntityManager entityManager) {
		long pk = 0;
		String sql = "SELECT " + SCHEMA_NAME + seqName + ".nextval from dual";
		// System.out.println(sql);
		List<Object> ls = entityManager.createNativeQuery(sql).getResultList();
		if (!ls.isEmpty()) {
			pk = Long.parseLong(ls.get(0).toString());
		}
		return pk;
	}

	public static String writeImageFolder(String imageData, String path, String imageName) {
		String result = "";
		try {
			String[] parts = imageData.split(",");
			String imageType = imageData.split(";")[0].split("/")[1];
			String imageString = parts[1];
			BufferedImage image = null;
			byte[] imageByte;
			imageByte = Base64.decodeBase64(imageString);
			ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
			image = ImageIO.read(bis);
			bis.close();
			String basePath = getPropertyValue("basepath");
			result = path + "/" + imageName + "." + imageType;
			File dir = new File(basePath + path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File outputfile = new File(basePath + result);
			ImageIO.write(image, "png", outputfile);
		} catch (Exception e) {

		}
		return result;
	}

	public static String writeVideoFolder(String imageData, String path, String imageName) {
		String result = "";
		try {
			String[] parts = imageData.split(",");

			String imageType = imageData.split(";")[0].split("/")[1];
			String imageString = parts[2];
			logger.info("Patch : " + imageString);
			BufferedImage image = null;
			byte[] imageByte;
			imageByte = Base64.decodeBase64(imageString);
			String basePath = getPropertyValue("basepath");
			result = path + "/" + imageName + "." + imageType;
			logger.info("Patch : " + basePath + result);
			File dir = new File(basePath + path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			FileOutputStream out = new FileOutputStream(basePath + result);
			out.write(imageByte);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static InputStreamResource getInputStreamFromPath(File file) {
		try {

			byte[] bytearray = new byte[(int) file.length()];
			FileInputStream fileInputStream = new FileInputStream(file);
			fileInputStream.read(bytearray);
			fileInputStream.close();
			return new InputStreamResource(new ByteArrayInputStream(bytearray));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String writeExcelFile(String imageData, String path, String imageName) {
		String result = "";
		try {
			String[] parts = imageData.split(",");
			String imageType = ".xls";
			String imageString = parts[1];
			BufferedImage image = null;
			byte[] imageByte;
			imageByte = Base64.decodeBase64(imageString);
			String basePath = getPropertyValue("basepath");
			result = path + "/" + imageName + "." + imageType;
			File dir = new File(basePath + path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			try (FileOutputStream fos = new FileOutputStream(basePath + result)) {
				fos.write(imageByte);
			}
		} catch (Exception e) {
		}
		return result;
	}

	public static String getPropertyValue(String key) {
		return "D:/";
	}

	public static long objTolong(Object obj) {
		try {
			return obj != null ? Long.parseLong(obj.toString()) : 0;
		} catch (Exception e) {
			return 0;
		}
	}

	public static String objToString(Object obj) {
		try {
			return obj != null ? obj.toString() : "";
		} catch (Exception e) {
			return "";
		}
	}

	public static String objToStringNull(Object obj) {
		try {
			return obj != null ? obj.toString() : null;
		} catch (Exception e) {
			return "";
		}
	}

	public static String objToStringNA(Object obj) {
		try {
			return obj != null ? obj.toString() : "NA";
		} catch (Exception e) {
			return "NA";
		}
	}

	public static int objToInt(Object obj) {
		try {
			return obj != null ? Integer.parseInt(obj.toString()) : 0;
		} catch (Exception e) {
			return 0;
		}
	}

	public static double objToDouble(Object obj) {
		try {
			return obj != null ? Double.parseDouble(obj.toString()) : 0;
		} catch (Exception e) {
			return 0;
		}
	}

	public static Date objToDate(Object obj) {
		SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy");
		Date cr = new Date();
		try {
			cr = s.parse(s.format(new Date()));
			return obj != null ? (s.parse(obj.toString())) : cr;
		} catch (Exception e) {
			return cr;
		}
	}

	public static boolean objToBoolean(Object obj) {

		try {
			if (obj != null && (obj.toString().equalsIgnoreCase("true") || objTolong(obj) > 0)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public static Date objToDateTime(Object obj) {
		try {
			return obj != null ? (new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(obj.toString())) : new Date();
		} catch (Exception e) {
			return new Date();
		}
	}

	public static JSONArray getQueryColumns(String query, EntityManager entity, String fileName) {

		JSONArray result = new JSONArray();
		try {
			if (fileName != null) {
				result = ReadJsonFile.getJSONObject(fileName);
			}
			if (result == null || result.isEmpty()) {
				String param1 = "";
				String param2 = "";
				query = query.replaceAll("'", "''");
				if (query.length() < 3990) {
					param1 = query;
				} else {
					param1 = query.substring(0, 3990);
					param1 = query.substring(3990, query.length());
				}
				String sql = "select lower(column_value) from table(GET_COLUMNS_NAME('" + param1 + "','" + param2
						+ "'))";
				logger.info("Sql+ " + sql);
				List<String> ls = entity.createNativeQuery(sql).getResultList();
				for (String s : ls) {
					JSONObject o = new JSONObject();
					o.put("key", s.split("@")[0]);
					int t = Integer.parseInt(s.split("@")[1]);
					o.put("type", t == 1 ? "text"
							: t == 2 ? "number" : t == 101 ? "double" : t == 180 ? "date" : t == 113 ? "blob" : "text");
					result.add(o);
				}
			}
			logger.info("result+ " + result.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static JSONArray getQueryColumnsNoLower(String query, EntityManager entity, String fileName) {

		JSONArray result = new JSONArray();
		try {
			if (fileName != null) {
				result = ReadJsonFile.getJSONObject(fileName);
			}
			if (result == null || result.isEmpty()) {
				String param1 = "";
				String param2 = "";
				query = query.replaceAll("'", "''");
				if (query.length() < 3990) {
					param1 = query;
				} else {
					param1 = query.substring(0, 3990);
					param1 = query.substring(3990, query.length());
				}
				String sql = "select (column_value) from table(GET_COLUMNS_NAME('" + param1 + "','" + param2 + "'))";
				logger.info("Sql+ " + sql);
				List<String> ls = entity.createNativeQuery(sql).getResultList();
				for (String s : ls) {
					JSONObject o = new JSONObject();
					o.put("key", s.split("@")[0]);
					int t = Integer.parseInt(s.split("@")[1]);
					o.put("type", t == 1 ? "text"
							: t == 2 ? "number" : t == 101 ? "double" : t == 180 ? "date" : t == 113 ? "blob" : "text");
					result.add(o);
				}
			}
			logger.info("result+ " + result.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static JSONArray getQueryColumnsReports(String query, EntityManager entity, String fileName) {

		JSONArray result = new JSONArray();
		try {
			if (fileName != null) {
				result = ReadJsonFile.getJSONObject(fileName);
			}
			if (result == null || result.isEmpty()) {
				String param1 = "";
				String param2 = "";
				query = query.replaceAll("'", "''");
				if (query.length() < 3990) {
					param1 = query;
				} else {
					param1 = query.substring(0, 3990);
					param1 = query.substring(3990, query.length());
				}
				String sql = "select (column_value) from table(GET_COLUMNS_NAME('" + param1 + "','" + param2 + "'))";

				List<String> ls = entity.createNativeQuery(sql).getResultList();
				for (String s : ls) {
					JSONObject o = new JSONObject();
					o.put("key", s.split("@")[0]);
					int t = Integer.parseInt(s.split("@")[1]);
					o.put("type", t == 1 ? "text"
							: t == 2 ? "number" : t == 101 ? "double" : t == 180 ? "date" : t == 113 ? "blob" : "text");
					result.add(o);
				}
			}
			logger.info("result+ " + result.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static JSONArray resultToJSONArray(JSONArray columns, List<Object[]> data) {
		JSONArray result = new JSONArray();
		try {
			for (Object[] o : data) {
				JSONObject json = new JSONObject();
				for (int i = 0; i < columns.size(); i++) {
					String key = ((JSONObject) columns.get(i)).get("key").toString();
					String type = ((JSONObject) columns.get(i)).get("type").toString();

					if (type.equals("text")) {
						json.put(key, o[i] != null ? o[i].toString() : "");
					} else if (type.equals("number")) {
						json.put(key, objTolong(o[i]));
					} else if (type.equals("double")) {
						json.put(key, objToDouble(o[i]));
					} else if (type.equals("date")) {
						json.put(key, objToDate(o[i]));
					} else if (type.equals("boolean")) {
						json.put(key, objToBoolean(o[i]));
					} else if (type.equals("blob")) {
						json.put(key, blobToString(o[i]));
					} else if (type.equals("packs")) {
						json.put(key, getPackConfig(o[i]));
					} else if (type.equals("array")) {
						json.put(key, getArrayObject(o[i]));
					} else {
						json.put(key, o[i] != null ? o[i].toString() : "");
					}

				}
				result.add(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("result+ " + result.toString());
		return result;
	}

	public static JSONArray getArrayObject(Object s) {
		JSONArray ps = new JSONArray();
		try {
			if (s != null) {
				String a[] = s.toString().split("#");
				for (String b : a) {
					String[] c = b.split("@");
					JSONObject pack = new JSONObject();
					pack.put("pack", c[0].toString());
					pack.put("cart_item_id", objTolong(c[1]));
					pack.put("quantity", objToDouble(c[2]));
					ps.add(pack);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ps;
	}

	public static JSONObject getPackConfig(Object s) {
		JSONObject json = new JSONObject();
		try {
			if (s != null) {
				String a[] = s.toString().split("#");
				JSONArray ps = new JSONArray();
				for (String b : a) {
					String[] c = b.split("@");
					JSONObject pack = new JSONObject();
					pack.put("name", c[0].toString());
					pack.put("quantity", objToDouble(c[1]));
					pack.put("price", objToDouble(c[2]));
					pack.put("default_pack", objToInt(c[3]));
					pack.put("key", objTolong(c[4]));
					ps.add(pack);
					if (objToInt(c[3]) == 1)
						json.put("default", pack);
				}
				if (!json.containsKey("default") && ps.size() > 0)
					json.put("default", ps.get(0));
				json.put("packs", ps);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return json;
	}

	public static JSONArray resultToJSONArray(JSONArray columns, List<Object[]> data, String dbKeys) {
		JSONArray result = new JSONArray();
		try {
			List<String> doubleKeys = getDoubleKeys(dbKeys);
			for (Object[] o : data) {
				JSONObject json = new JSONObject();
				for (int i = 0; i < columns.size(); i++) {
					String key = ((JSONObject) columns.get(i)).get("key").toString();
					String type = ((JSONObject) columns.get(i)).get("type").toString();

					if (type.equals("text")) {
						json.put(key, o[i] != null ? o[i].toString() : "");
					} else if (type.equals("number")) {
						if (doubleKeys != null && doubleKeys.contains(key))
							json.put(key, objToDouble(o[i]));
						else
							json.put(key, objTolong(o[i]));
					} else if (type.equals("double")) {
						json.put(key, objToDouble(o[i]));
					} else if (type.equals("date")) {
						json.put(key, objToDate(o[i]));
					} else if (type.equals("boolean")) {
						json.put(key, objToBoolean(o[i]));
					} else if (type.equals("blob")) {
						json.put(key, blobToString(o[i]));
					} else {
						json.put(key, o[i] != null ? o[i].toString() : "");
					}

				}
				result.add(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("result+ " + result.toString());
		return result;
	}

	public static List<String> getDoubleKeys(String s) {
		List<String> result = null;
		if (s != null && s.trim().length() > 0) {
			String[] a = s.split(",");
			result = Arrays.asList(a);
		}
		return result;
	}

	public static String blobToString(Object obj) {
		String text = "";
		logger.info("obj : " + obj.toString());
		try {
			if (obj != null) {
				Blob blob = (Blob) obj;
				logger.info("blob : " + blob.toString());
				byte[] bdata = blob.getBytes(1, (int) blob.length());
				text = new String(bdata);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}

	public static JSONArray resultToJSONArrayOfArray(JSONArray columns, List<Object[]> data) {
		JSONArray result = new JSONArray();
		try {
			for (Object[] o : data) {
				JSONArray row = new JSONArray();
				for (int i = 0; i < columns.size(); i++) {
					JSONObject json = new JSONObject();
					String key = ((JSONObject) columns.get(i)).get("key").toString();
					String type = ((JSONObject) columns.get(i)).get("type").toString();
					json.put("key", key);
					if (type.equals("text")) {
						json.put("value", o[i] != null ? o[i].toString() : "");
					} else if (type.equals("number")) {
						json.put("value", objTolong(o[i]));
					} else if (type.equals("double")) {
						json.put("value", objToDouble(o[i]));
					} else if (type.equals("date")) {
						json.put("value", objToDate(o[i]));
					} else if (type.equals("boolean")) {
						json.put("value", objToBoolean(o[i]));
					} else if (type.equals("blob")) {
						json.put(key, blobToString(o[i]));
					} else {
						json.put("value", o[i] != null ? o[i].toString() : "");
					}
					row.add(json);
				}
				result.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("result+ " + result.toString());
		return result;
	}

	public static OrderTrack getOrderTrackObject(EntityManager entityManager, String status, String description,
			long orderId) {
		OrderTrack ot = new OrderTrack();
		ot.setOrderTrackId(getPrimaryKey("SEQ_ORDER_TRACK", entityManager));
		ot.setStatus(status);
		ot.setDescription(description);
		ot.setOrderId(orderId);
		ot.setOnDate(new Date());
		if (status.equalsIgnoreCase("order placed")) {
			ot.setImage("pi pi-shopping-cart");
		} else if (status.equalsIgnoreCase("accepted")) {
			ot.setImage("pi pi-cog");
		} else if (status.equalsIgnoreCase("packed")) {
			ot.setImage("pi pi-shopping-cart");
		} else if (status.equalsIgnoreCase("out for delivery")) {
			ot.setImage("pi pi-shopping-cart");
		} else if (status.equalsIgnoreCase("delivered")) {
			ot.setImage("pi pi-shopping-cart");
		} else if (status.equalsIgnoreCase("ready for pickup")) {
			ot.setImage("pi pi-shopping-cart");
		}
		entityManager.persist(ot);
		return ot;
	}

	public static String stringFromArray(JSONArray array, String key, int dataType) {
		String ids = "";
		for (int i = 0; i < array.size(); i++) {
			JSONObject a = (JSONObject) array.get(i);
			if (dataType == 1) {
				ids += a.get(key).toString() + ",";
			} else {
				ids += "'" + a.get(key).toString() + "',";
			}
		}
		if (ids.length() > 1) {
			ids = ids.substring(0, ids.length() - 1);
		}
		return ids;
	}

	public static int otpGenerate(int from, int to) {
		return from + (int) (Math.random() * to);
	}

	public static JSONArray stringToMaltiple(String input) {
		JSONArray array = new JSONArray();
		String[] s = input.split(",");
		for (String o : s) {
			JSONObject j = new JSONObject();
			j.put("name", o);
			j.put("key", o);
			array.add(j);
		}
		return array;
	}

	public static JSONArray jsonArrayToMaltiple(String s) {
		JSONArray array = new JSONArray();
		try {
			JSONArray input = (JSONArray) new JSONParser().parse(s);

			for (int i = 0; i < input.size(); i++) {
				JSONObject j = new JSONObject();
				j.put("name", input.get(i).toString());
				j.put("key", input.get(i).toString());
				array.add(j);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return array;
	}

	public static JSONObject barOrLineChart(List<Object[]> data) {
		JSONObject json = new JSONObject();
		JSONArray labels = new JSONArray();
		JSONArray datasets = new JSONArray();
		List<String> group = new ArrayList<>();
		List<String> lab = new ArrayList<>();
		Map<String, Double> labIndex = new HashMap<>();

		for (Object[] o : data) {
			String l = objToString(o[0]).trim();
			String d = objToString(o[1]);
			double v = objTolong(o[2]);
			if (!labels.contains(l))
				labels.add(l);
			if (!lab.contains(l))
				lab.add(l);
			if (!group.contains(d))
				group.add(d);
			labIndex.put(l + "@" + d, v);
		}
		logger.info("labIndex+ " + labIndex);
		List<String> col = getColors();
		int index = 0;
		for (String d : group) {
			JSONObject g = new JSONObject();
			g.put("label", d);
			JSONArray va = new JSONArray();
			for (String l : lab)
				va.add(labIndex.containsKey(l + "@" + d) ? labIndex.get(l + "@" + d) : 0);
			g.put("data", va);
			g.put("backgroundColor", col.get(index++));
			datasets.add(g);
		}
		json.put("labels", labels);
		json.put("datasets", datasets);
		logger.info("json+ " + json.toString());
		return json;
	}

	public static JSONObject pieOrDoughnutChart(List<Object[]> data, String lable) {
		JSONObject json = new JSONObject();
		JSONArray labels = new JSONArray();
		JSONArray value = new JSONArray();
		JSONArray color = new JSONArray();
		JSONArray datasets = new JSONArray();

		int index = 0;
		List<String> col = getColors();
		for (Object[] o : data) {
			String l = objToString(o[0]).trim();
			double v = objTolong(o[1]);
			labels.add(l);
			value.add(v);
			color.add(col.get(index++));
		}
		json.put("labels", labels);
		JSONObject g = new JSONObject();
		g.put("label", lable);
		g.put("data", value);
		g.put("backgroundColor", color);
		datasets.add(g);
		json.put("datasets", datasets);
		logger.info("json+ " + json.toString());
		return json;
	}

	public static List<String> getColors() {
		List<String> col = new ArrayList<>();
		col.add("rgb(75, 192, 192)");
		col.add("rgb(255, 99, 132)");
		col.add("rgb(54, 162, 235)");
		col.add("rgb(255,255,36)");
		col.add("rgb(222,200,171)");
		col.add("rgb(0,87,0)");
		col.add("rgb(0,87,87)");
		col.add("rgb(173,0,173)");
		col.add("rgb(4,170,233)");
		col.add("rgb(255, 99, 132)");
		col.add("rgb(54, 162, 235)");
		col.add("rgb(75, 192, 192)");
		return col;
	}

	public static long daysBetweenTwoDates(Date date1, Date date2) {
		if (date2 == null)
			date2 = objToDate(dateToString(new Date()));
		long diff = date1.getTime() - date2.getTime();
		return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}

	public static long daysBetweenFromCurrentDate(Date date1) {
		if (date1 != null)
			return daysBetweenTwoDates(date1, null);
		else
			return -1;
	}

	public static String dateToString(Date date) {
		return dateToString(date, "dd-MM-yyyy");
	}

	public static String dateToString(Date date, String formate) {
		return (new SimpleDateFormat(formate)).format(date);
	}

	public static void main(String[] args) {
		System.out.print(dateToString(addingDaysToDate(objToDate("02-12-2021"), 2)));
	}

	public static Date addingDaysToDate(Date date, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(date); // Using today's date
		c.add(Calendar.DATE, days); // Adding 5 days
		return c.getTime();
	}

	public static String dateChartString(String type) {
		if (type != null) {
			if (type.equalsIgnoreCase("day"))
				return "yyyymmdd";
			else if (type.equalsIgnoreCase("year"))
				return "yyyy";
			else
				return "yyyymm";
		} else {
			return "yyyymm";
		}
	}

	public static String dateChartDisplayString(String type) {
		if (type != null) {
			if (type.equalsIgnoreCase("day"))
				return "dd-Mon-yy";
			else if (type.equalsIgnoreCase("year"))
				return "yyyy";
			else
				return "Month";
		} else {
			return "Month";
		}
	}

	public static int dateChartDays(String type) {
		if (type != null) {
			if (type.equalsIgnoreCase("day"))
				return 10;
			else if (type.equalsIgnoreCase("year"))
				return last2Years();
			else
				return last12Months();
		} else {
			return last12Months();
		}
	}

	public static int last12Months() {
		try {
			SimpleDateFormat a = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat a1 = new SimpleDateFormat("MM-yyyy");
			Date d = addMonth(new Date(), -11);
			logger.info(d);
			Date ms = a.parse("01-" + a1.format(d));
			return getDifferanceBWDays(ms, new Date());
		} catch (Exception e) {
			return 0;
		}
	}

	public static int last2Years() {
		try {
			SimpleDateFormat a = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat a1 = new SimpleDateFormat("yyyy");
			int year = Integer.parseInt(a1.format(new Date())) - 1;
			Date ms = a.parse("01-01-" + year);
			return getDifferanceBWDays(ms, new Date());
		} catch (Exception e) {
			return 0;
		}

	}

	public static Date addDays(Date date, int days) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}

	public static Date addMonth(Date date, int month) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.MONTH, month);
		return cal.getTime();
	}

	public static int getDifferanceBWDays(Date startDate, Date currenetDate) {
		if (startDate != null && currenetDate != null) {
			int diffInDays = 0;
			try {
				SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy");
				currenetDate = sd.parse(sd.format(currenetDate));
				startDate = sd.parse(sd.format(startDate));
				diffInDays = (int) ((currenetDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
			} catch (Exception e) {

			}
			return diffInDays;
		} else {
			return -1;
		}
	}

	public static File getFileFromResource(String file) {
		try {
			return new ClassPathResource(file).getFile();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
