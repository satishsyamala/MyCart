/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aqua.dao.intf;

import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.aqua.pojo.ImportData;

/**
 *
 * @author USER
 */
public interface ImportDataDaoIntf {

    public JSONObject uploadFile(JSONObject json);

    public List<ImportData> getImportData(JSONObject json);

    public ImportData getPendingImports();
    
    public String saveImportData(ImportData imp,List<String> row,JSONArray jsonArr);
    
     public void updateImportDataStatus(ImportData importData);
     
     public void updateImageForStock();
     
     public JSONObject stockImage(JSONObject json);
}
