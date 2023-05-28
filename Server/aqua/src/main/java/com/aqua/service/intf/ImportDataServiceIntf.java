/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aqua.service.intf;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author USER
 */
public interface ImportDataServiceIntf {

    public JSONObject uploadFile(JSONObject json);

    public JSONArray getImportData(JSONObject json);
    
    public void importScheduler();
    
    public void updateImageForStock();
    
    public JSONObject stockImage(JSONObject json);
            
}
