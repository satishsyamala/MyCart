/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aqua.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aqua.service.intf.PriceServiceIntf;
import com.aqua.util.AESEncryption;

/**
 *
 * @author USER
 */
@CrossOrigin("*")
@RestController
@RequestMapping(value = "/price")
public class PriceController {
    
    @Autowired
    private PriceServiceIntf priceServiceIntf;
    
    @PostMapping(value = "/get-price-list", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public JSONArray getPriceListDetails(@RequestBody String text) {
        JSONArray result = new JSONArray();
        try {
            
           
            result = priceServiceIntf.getPriceListDetails(AESEncryption.decrypt(text));
        } catch (Exception e) {
            e.printStackTrace();

        }
        return result;
    }

    @PostMapping(value = "/save-price-list", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject savePriceList(@RequestBody String text) {
        JSONObject result = new JSONObject();
        try {
            
           
            result = priceServiceIntf.savePriceList(AESEncryption.decrypt(text));
        } catch (Exception e) {

        }
        return result;
    }
    
    @PostMapping(value = "/get-offers", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public JSONArray getOffers(@RequestBody String text) {
        JSONArray result = new JSONArray();
        try {
            
           
            result = priceServiceIntf.getOffers(AESEncryption.decrypt(text));
        } catch (Exception e) {
            e.printStackTrace();

        }
        return result;
    }
}
