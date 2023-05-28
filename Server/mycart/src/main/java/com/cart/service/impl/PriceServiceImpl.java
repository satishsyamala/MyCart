/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cart.service.impl;

import com.cart.dao.intf.PriceDaoIntf;
import com.cart.service.intf.PriceServiceIntf;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author USER
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class PriceServiceImpl implements PriceServiceIntf {

    @Autowired
    private PriceDaoIntf priceDaoIntf;
    
    public JSONArray getPriceListDetails(JSONObject filters){
        return priceDaoIntf.getPriceListDetails(filters);
    }

    public JSONObject savePriceList(JSONObject json){
        return priceDaoIntf.savePriceList(json);
    }
    
    public JSONArray getOffers(JSONObject filters) {
    	return priceDaoIntf.getOffers(filters);
    }

}
