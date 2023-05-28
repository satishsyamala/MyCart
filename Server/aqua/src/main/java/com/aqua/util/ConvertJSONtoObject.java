/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aqua.util;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Set;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.aqua.controller.UserController;
import com.aqua.pojo.DeliveryAddress;
import com.aqua.pojo.Users;
import com.google.gson.Gson;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.StringJoiner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;

/**
 *
 * @author USER
 */
public class ConvertJSONtoObject {

    private static final Logger logger = LogManager.getLogger(ConvertJSONtoObject.class);

    public static void jsonToObject(Object obj, JSONObject json) {
        Set<String> keys = json.keySet();
        Class c = obj.getClass();
        for (String key : keys) {
            try {
                JSONObject field = (JSONObject) json.get(key);
                logger.info(field.toJSONString());
                if (field.get("value") != null) {
                    Field f = c.getDeclaredField(key);
                    f.setAccessible(true);
                    logger.info(key + "   : " + f.getType().getName());
                    if (field.get("type").toString().equals("text") || field.get("type").toString().equals("textarea")) {
                        f.set(obj, field.get("value").toString());
                    } else if (field.get("type").toString().equals("password")) {
                        f.set(obj, field.get("value").toString());
                    } else if (field.get("type").toString().equals("number")) {
                        if (f.getType().getName().equalsIgnoreCase("Integer") || f.getType().getName().equalsIgnoreCase("int")) {
                            f.setInt(obj, Integer.parseInt(field.get("value").toString()));
                        } else if (f.getType().getName().equalsIgnoreCase("Long")) {
                            f.setLong(obj, Long.parseLong(field.get("value").toString()));
                        } else {
                            f.setDouble(obj, Double.parseDouble(field.get("value").toString()));
                        }
                    } else if (field.get("type").toString().equals("calender")) {
                        f.set(obj, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(field.get("value").toString()));
                    } else if (field.get("type").toString().equals("dropdown") || field.get("type").toString().equals("radiobutton")) {
                        JSONObject temp = (JSONObject) field.get("value");
                        if (f.getType().getName().equalsIgnoreCase("Integer") || f.getType().getName().equalsIgnoreCase("int")) {
                            f.setInt(obj, Integer.parseInt(temp.get("key").toString()));
                        } else if (f.getType().getName().equalsIgnoreCase("Double")) {
                            f.setDouble(obj, Double.parseDouble(temp.get("key").toString()));
                        } else if (f.getType().getName().equalsIgnoreCase("Long")) {
                            f.setLong(obj, Long.parseLong(temp.get("key").toString()));
                        } else {
                            f.set(obj, temp.get("key").toString());
                        }
                    } else if (field.get("type").toString().equals("onecheckbox")) {
                        System.out.println(key + " : " + field.get("value").toString()+" : "+f.getType().getName());
                        if (f.getType().getName().equalsIgnoreCase("Integer") || f.getType().getName().equalsIgnoreCase("int")) {
                            f.set(obj, GeneralUtil.objToBoolean(field.get("value")) ? 1 : 0);
                        } else if (f.getType().getName().equalsIgnoreCase("boolean")) {
                            f.set(obj, GeneralUtil.objToBoolean(field.get("value")));
                        } else {
                            f.set(obj, field.get("value").toString());
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(key);

            }
        }
    }

    public static JSONObject jsonToObject(JSONObject json) {
        Set<String> keys = json.keySet();
        JSONObject o = new JSONObject();
        for (String key : keys) {
            try {
                JSONObject field = (JSONObject) json.get(key);
                logger.info(field.toJSONString());
                if (field.get("value") != null) {

                    if (field.get("type").toString().equals("text") || field.get("type").toString().equals("textarea")) {
                        o.put(key, field.get("value").toString());
                    } else if (field.get("type").toString().equals("password")) {
                        o.put(key, field.get("value").toString());
                    } else if (field.get("type").toString().equals("number")) {
                        o.put(key, field.get("value").toString());
                    } else if (field.get("type").toString().equals("calender")) {
                        o.put(key, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(field.get("value").toString()));
                    } else if (field.get("type").toString().equals("dropdown") || field.get("type").toString().equals("radiobutton")) {
                        JSONObject temp = (JSONObject) field.get("value");
                        o.put(key, temp.get("key").toString());
                    }
                }
            } catch (Exception e) {
                System.out.println(key);
                e.printStackTrace();
            }
        }
        logger.info(o.toString());
        return o;
    }

    public static void ListObject(JSONArray arr, List<String> row, Object obj) {
        Class c = obj.getClass();
        for (int i = 0; i < arr.size(); i++) {
            try {
                JSONObject json = (JSONObject) arr.get(i);
                Field f = c.getDeclaredField(json.get("key").toString());
                f.setAccessible(true);
                logger.info(json.get("key").toString() + "   : " + f.getType().getName());
                if (row.get(i) != null && row.get(i).length() > 0) {
                    if (json.get("type").equals("number")) {
                        f.set(obj, GeneralUtil.objToInt(row.get(i)));
                    } else if (json.get("type").equals("double")) {
                        f.set(obj, GeneralUtil.objToDouble(row.get(i)));
                    } else {
                        f.set(obj, row.get(i));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static JSONObject pojoToJSON(Object obj) {
        JSONObject json = new JSONObject();
        try {
            Gson g = new Gson();
            JSONParser p = new JSONParser();
            json = (JSONObject) p.parse(g.toJson(obj));
           logger.info(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
       
        return json;
    }
    
    public static String multipleToString(JSONArray array)
    {
        StringJoiner a=new StringJoiner(",");
        for(int i=0;i<array.size();i++)
        {
            a.add(((JSONObject)array.get(i)).get("key").toString());
        }
        return a.toString();
    }
    
    public static String multipleToJSONArray(JSONArray array)
    {
        JSONArray a=new JSONArray();
        for(int i=0;i<array.size();i++)
        {
            a.add(((JSONObject)array.get(i)).get("key").toString());
        }
        return a.toString();
    }

    public static void main(String[] args) throws ParseException {
        DeliveryAddress d = new DeliveryAddress();
        String j = "{\"address\":{\"valid\":false,\"minlength\":0,\"maxlength\":200,\"label\":\"Address\",\"type\":\"textarea\",\"validmsg\":null,\"value\":\"Banjarahiils\",\"required\":true},\"city\":{\"valid\":false,\"minlength\":0,\"maxlength\":20,\"label\":\"City\",\"type\":\"text\",\"validmsg\":null,\"value\":\"Hyderabad\",\"required\":true},\"createdBy\":{\"type\":\"text\",\"value\":\"Satish\"},\"pinCode\":{\"valid\":false,\"minlength\":0,\"maxlength\":20,\"label\":\"Pin Code\",\"type\":\"text\",\"validmsg\":null,\"value\":\"500036\",\"required\":true},\"latitude\":{\"valid\":false,\"minlength\":0,\"maxlength\":20,\"label\":\"Latitude\",\"type\":\"text\",\"validmsg\":null,\"value\":\"17.354519186385374\",\"required\":true},\"state\":{\"valid\":false,\"minlength\":0,\"maxlength\":20,\"label\":\"State\",\"type\":\"text\",\"validmsg\":null,\"value\":\"Telangana\",\"required\":true},\"userId\":{\"type\":\"number\",\"value\":10},\"longitude\":{\"valid\":false,\"minlength\":0,\"maxlength\":20,\"label\":\"Longitude\",\"type\":\"text\",\"validmsg\":null,\"value\":\"78.39568346793165\",\"required\":true}}";
        JSONParser p = new JSONParser();
        JSONObject json = (JSONObject) p.parse(j);
        System.out.println(json.toString());
        jsonToObject(d, json);
        System.out.println(d.getUserId());

        System.out.println(pojoToJSON(d));

    }

}
