/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aqua.file;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.aqua.pojo.ImportData;

/**
 *
 * @author USER
 */
public class ImportValidation {

    public static Map<String, List<List<String>>> validateFile(String module, File file) {
        Map<String, List<List<String>>> map = new HashMap<>();
        List<List<String>> suc = new ArrayList<>();
        List<List<String>> rej = new ArrayList<>();
        try {
            List<List<String>> fileData = ExcelReadAndWrite.getExcelXLSX(file);
            if (!fileData.isEmpty()) {
                JSONArray json = ReadJsonFile.getJSONObject(module);
                List<String> headers = fileData.get(0);
                boolean check = heardCheck(json, headers);
                if (check) {
                    suc.add(headers);
                    headers.add("Reasons");
                    rej.add(headers);
                    for (int i = 1; i < fileData.size(); i++) {
                        String reason = validateRow(json, fileData.get(i));
                        if (reason.length() > 0) {
                            fileData.get(i).add(reason);
                            rej.add(fileData.get(i));
                        } else {
                            suc.add(fileData.get(i));
                        }
                    }
                    if (suc.size() > 1) {
                        map.put("success", suc);
                    }
                    if (rej.size() > 1) {
                        map.put("reject", rej);
                    }
                } else {
                    map.put("failed", suc);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static boolean heardCheck(JSONArray jsonArr, List<String> header) {
        for (int i = 0; i < jsonArr.size(); i++) {
            JSONObject json = (JSONObject) jsonArr.get(i);
            if (!json.get("label").toString().equalsIgnoreCase(header.get(i).replace("*", "").trim())) {
                return false;
            }
        }
        return true;
    }

    public static String validateRow(JSONArray jsonArr, List<String> row) {
        String result = "";
        for (int i = 0; i < jsonArr.size(); i++) {
            JSONObject json = (JSONObject) jsonArr.get(i);
            String reason = "";
            if (json.get("required").toString().equalsIgnoreCase("true") && (row.get(i) == null || row.get(i).trim().length() == 0)) {
                if (result.length() > 0) {
                    result += ", ";
                }
                reason = json.get("label").toString() + " is required ";
            } else {
                if (row.get(i) != null && row.get(i).trim().length() > 0) {
                    if (json.get("type").equals("number")) {
                        try {
                            System.out.println(json.get("label").toString()+" : "+row.get(i).trim());
                            Double.parseDouble(row.get(i).trim());
                        } catch (Exception e) {
                            reason = json.get("label").toString() + " is invalid number ";
                        }
                    } else if (json.get("type").equals("double")) {
                        try {
                            Double.parseDouble(row.get(i).trim());
                        } catch (Exception e) {
                            reason = json.get("label").toString() + " is invalid number ";
                        }
                    }
                }
            }
            if (result.length() > 0) {
                result += ", ";
            }
            if (reason.length() > 0) {
                result += reason;
            }
        }
        return result;
    }
    
    


}
