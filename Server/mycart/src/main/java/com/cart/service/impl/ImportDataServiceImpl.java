/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cart.service.impl;

import com.cart.dao.intf.ImportDataDaoIntf;
import com.cart.file.ExcelReadAndWrite;
import com.cart.file.ImportValidation;
import com.cart.file.ReadJsonFile;
import com.cart.pojo.Category;
import com.cart.pojo.ImportData;
import com.cart.service.intf.ImportDataServiceIntf;
import com.cart.util.ConvertJSONtoObject;
import com.cart.util.GeneralUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
public class ImportDataServiceImpl implements ImportDataServiceIntf {

    @Autowired
    private ImportDataDaoIntf importDataDaoIntf;

    public JSONArray getImportData(JSONObject json) {

        JSONArray array = new JSONArray();
        try {
            List<ImportData> cat = importDataDaoIntf.getImportData(json);
            for (ImportData c : cat) {
                array.add(ConvertJSONtoObject.pojoToJSON(c));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return array;
    }

    public JSONObject uploadFile(JSONObject json) {
        return importDataDaoIntf.uploadFile(json);
    }
    
    public JSONObject stockImage(JSONObject json) {
    	return importDataDaoIntf.stockImage(json);
    }

    public void importScheduler() {
        ImportData imp = importDataDaoIntf.getPendingImports();
        if (imp != null) {
            String bPth = GeneralUtil.getPropertyValue("basepath");
            File file = new File(bPth + imp.getFilePath());
            if (file.exists()) {
                Map<String, List<List<String>>> map = ImportValidation.validateFile(imp.getModuleKey(), file);
                if (!map.containsKey("failed")) {
                    int totalRecodes = 0;
                    int sucRecodes = 0;
                    int rejectedRecode = 0;
                    List<List<String>> rej = new ArrayList<>();
                    List<List<String>> fnSuc = new ArrayList<>();
                    if (map.containsKey("reject")) {
                        rej = map.get("reject");
                        rejectedRecode = rej.size() - 1;
                    }
                    if (map.containsKey("success")) {
                        JSONArray json = ReadJsonFile.getJSONObject(imp.getModuleKey());
                        List<List<String>> suc = map.get("success");
                        fnSuc.add(suc.get(0));
                        for (int i = 1; i < suc.size(); i++) {
                            String msg = importDataDaoIntf.saveImportData(imp, suc.get(i), json);
                            if (msg.equalsIgnoreCase("success")) {
                                sucRecodes++;
                                fnSuc.add(suc.get(i));
                            } else {
                                suc.get(i).add(msg);
                                rej.add(suc.get(i));
                                rejectedRecode++;
                            }
                        }
                    }
                    totalRecodes = sucRecodes + rejectedRecode;
                    if (totalRecodes > 0) {
                        imp.setStatus("Completed");
                        imp.setTotalRecords(totalRecodes);
                        imp.setRejectedRecords(rejectedRecode);
                        imp.setSuccessRecords(sucRecodes);
                    } else {
                        imp.setStatus("Empty File");
                    }
                    if (sucRecodes > 0) {
                        String path = "mycart/imports/success/" + imp.getModule() + "_" + imp.getImportId() + ".xls";
                        createFolder(bPth + "mycart/imports/success");
                        ExcelReadAndWrite.createExcelFileWithStyles(fnSuc, bPth + path, 1);

                    }
                    if (rejectedRecode > 0) {
                        String path = "mycart/imports/rejected/" + imp.getModule() + "_" + imp.getImportId() + ".xls";
                        createFolder(bPth + "mycart/imports/rejected");
                        ExcelReadAndWrite.createExcelFileWithStyles(rej, bPth + path, 1);
                        imp.setRejectedFilePath(path);
                    }
                    file.delete();

                } else {
                    imp.setStatus("Invalid File Format");
                }
            } else {
                imp.setStatus("File Not Found");
            }
            importDataDaoIntf.updateImportDataStatus(imp);
        }
    }

    public void updateImageForStock() {
        importDataDaoIntf.updateImageForStock();
    }

    public static void createFolder(String path) {
        try {
            File f = new File(path);
            if (!f.isDirectory()) {
                f.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
