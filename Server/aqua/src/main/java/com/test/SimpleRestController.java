/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test;

import java.util.Date;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.aqua.service.intf.ImportDataServiceIntf;
 
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/test")
public class SimpleRestController {
    
    @Autowired
    private ImportDataServiceIntf importDataServiceIntf;
    
    @GetMapping("/example")
    @ResponseBody
    public ResponseEntity<String> example() {
        String s= "Hello User !! " + new Date();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Responded", "MyController");
      
        return ResponseEntity.accepted().headers(headers).body(s);
    }
    
    @GetMapping("/import")
    @ResponseBody
    public ResponseEntity<JSONArray> jsonExample() {
        String s= "Hello User !! " + new Date();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Responded", "MyController");
        System.out.print(s);
        return ResponseEntity.accepted().headers(headers).body(importDataServiceIntf.getImportData(new JSONObject()));
    }
}
