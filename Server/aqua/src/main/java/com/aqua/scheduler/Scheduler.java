/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aqua.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.aqua.service.intf.ImportDataServiceIntf;

@Component
public class Scheduler {
    
    @Autowired
    private ImportDataServiceIntf importDataServiceIntf;
    
  @Scheduled(fixedRate = 10000)
   public void fixedDelaySch() {
      importDataServiceIntf.importScheduler();
   }
}
