/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aqua.file;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;

/**
 *
 * @author USER
 */
public class ReadJsonFile {

    private static final Logger logger = LogManager.getLogger(ReadJsonFile.class);

    public static InputStreamResource getTemplateFile(String name) {
        try {
            JSONArray json = getJSONObject(name);
            List<String> headers = new ArrayList<>();

            for (int i = 0; i < json.size(); i++) {
                JSONObject temp = (JSONObject) json.get(i);
                String header = temp.get("label").toString();
                if (temp.containsKey("required")) {
                    header += " " + (temp.get("required").toString().equalsIgnoreCase("true") ? "*" : "");
                }
                headers.add(header);
            }
            List<List<String>> data = new ArrayList<>();
            data.add(headers);
            logger.info("Data : " + data);
            File file = ExcelReadAndWrite.createExcelFileWithStyles(data, name + ".xls", 1);
            byte[] bytearray = new byte[(int) file.length()];
            FileInputStream fileInputStream = new FileInputStream(
                    file);
            fileInputStream.read(bytearray);
            fileInputStream.close();
            return new InputStreamResource(new ByteArrayInputStream(bytearray));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray getJSONObject(String fileName) {
        JSONArray json = new JSONArray();
        try {
            JSONParser parser = new JSONParser();

            File resource = new ClassPathResource("json/" + fileName + ".json").getFile();

            json = (JSONArray) parser
                    .parse(new FileReader(resource));
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("File Path : " + json.toString());
        return json;
    }
    
     public static JSONArray getJSONObjectBasePath(String fileName) {
        JSONArray json = new JSONArray();
        try {
            JSONParser parser = new JSONParser();
            json = (JSONArray) parser
                    .parse(new FileReader(new File(fileName)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("File Path : " + json.toString());
        return json;
    }

}
