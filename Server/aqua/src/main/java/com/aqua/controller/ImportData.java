/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aqua.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aqua.file.ReadJsonFile;
import com.aqua.service.intf.ImportDataServiceIntf;
import com.aqua.util.AESEncryption;
import com.aqua.util.GeneralUtil;

/**
 *
 * @author USER
 */
@CrossOrigin("*")
@RestController
@RequestMapping(value = "/import")
public class ImportData {

	private static final Logger logger = LogManager.getLogger(ImportData.class);
	@Autowired
	private ImportDataServiceIntf importDataServiceIntf;

	@PostMapping(value = "/get-import", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONArray getCategory(@RequestBody String text) {
		JSONArray result = new JSONArray();
		try {
			result = importDataServiceIntf.getImportData(AESEncryption.decrypt(text));
		} catch (Exception e) {
			e.printStackTrace();

		}
		return result;
	}

	@GetMapping(value = "/template")
	public ResponseEntity<Resource> getImage(@RequestParam("module") String module) {
		byte[] bytearray = null;
		try {
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + module + ".xls")
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
					.body(ReadJsonFile.getTemplateFile(module));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@GetMapping(value = "/file")
	public ResponseEntity<Resource> getFile(@RequestParam("path") String path) {
		try {
			File file = new File(GeneralUtil.getPropertyValue("basepath") + path);
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
					.body(GeneralUtil.getInputStreamFromPath(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@GetMapping(value = "/video")
	public ResponseEntity<InputStreamResource> getVideoByName(@RequestParam("path") String path) {
		try {
			File file = new File(GeneralUtil.getPropertyValue("basepath") + path);

			InputStream fis = new FileInputStream(file);
			return ResponseEntity.ok().header("Content-Type", "video/webm")
					.header("Accept-Ranges", "bytes").header("Content-Length", file.length() + "")
					.body(new InputStreamResource(fis));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@PostMapping(value = "/uploadfile", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject uplaodFile(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {
			result = importDataServiceIntf.uploadFile(AESEncryption.decrypt(text));
		} catch (Exception e) {

		}
		return result;
	}

	@GetMapping(value = "/updateimage")
	public void updateimage() {
		importDataServiceIntf.updateImageForStock();
	}

	@PostMapping(value = "/stock-image", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject stockImage(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {
			result = importDataServiceIntf.stockImage(AESEncryption.decrypt(text));
		} catch (Exception e) {

		}
		return result;
	}

	@PostMapping(value = "/save-video", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject saveVideo(@RequestBody String text) {
		JSONObject result = new JSONObject();
		try {
			JSONObject obj = AESEncryption.decrypt(text);
			String file = obj.get("video").toString();
			String oid = obj.get("orderId").toString();
			logger.info("Hi ");
			// logger.info("File : "+file);
			GeneralUtil.writeVideoFolder(file.toString(), "mycart/"+oid+"/", "record");
		} catch (Exception e) {

		}
		return result;
	}
}
