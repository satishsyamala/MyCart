package com.cart.util;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ReportUtil {

	

	public static JSONObject otherValues(String s) {
		JSONObject j = new JSONObject();
		try {
			if (s != null && s.length() > 0) {
				String[] a = s.split(",");
				for (String b : a) {
					String[] c = b.split("=");
					String[] d = c[1].split("@");
					JSONArray o = new JSONArray();
					for (String e : d) {
						JSONObject op = new JSONObject();
						String[] f = e.split("#");
						if (f.length > 1) {
							op.put("name", f[0]);
							op.put("key", f[1]);
						} else {
							op.put("name", e);
							op.put("key", e);
						}
						o.add(op);
						j.put(c[0], o);
					}

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return j;
	}

	public static JSONObject dateJson(String name) {

		JSONObject date = new JSONObject();
		date.put("type", "calender");
		date.put("label", name.replace("_", " "));
		date.put("required", true);
		date.put("value", null);
		return date;
	}

	public static JSONObject otherDropDownJson(String name,JSONArray op) {
		JSONObject date = new JSONObject();
		date.put("type", "dropdown");
		date.put("label", name.replace("_", " "));
		date.put("required", false);
		date.put("value", null);
		date.put("options", op);
		return date;
	}
	
	public static JSONObject stockHierarchy(String name,JSONArray op) {
		JSONObject date = new JSONObject();
		date.put("type", "stockhier");
		date.put("label", name.replace("_", " "));
		date.put("required", false);
		date.put("value", null);
		date.put("options", new JSONArray());
		return date;
	}

	public static JSONObject textJson(String name) {
		JSONObject date = new JSONObject();
		date.put("type", "text");
		date.put("label", name.replace("_", " "));
		date.put("required", true);
		date.put("value", null);
		return date;
	}

	public static Map<String, Object> paramValues(String params, JSONObject json) {
		Map<String, Object> map = null;
		if (params != null) {
			map = new HashMap<>();

			String a[] = params.split(",");
			for (String b : a) {
				String[] c = b.split("=");
				Object value = null;
				if (json!=null && json.containsKey(c[0])) {
					JSONObject f = (JSONObject) json.get(c[0]);
					if(c[1].equalsIgnoreCase("other") || c[1].equalsIgnoreCase("byquery") )
					{
						JSONObject v=(JSONObject) f.get("value");
						if(v!=null)
						value = GeneralUtil.objToStringNull(v.get("key"));
						else
							value = null;	
					}else {
						value = GeneralUtil.objToStringNull(f.get("value"));
					}
					
				}
				if (c[1].equalsIgnoreCase("date")) {
					map.put(c[0], GeneralUtil.objToDate(value));
				} else {
					map.put(c[0], value);
				}
			}
		}
		return map;
	}

}
