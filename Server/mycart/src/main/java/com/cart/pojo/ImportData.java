/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cart.pojo;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "IMPORT_DATA", schema = "mycart")
public class ImportData {

    private long importId;
    private String module;
    private String moduleKey;
    private String importedBy;
    private Date importedOn;
    private String status;
    private Date completedOn;
    private int totalRecords;
    private int successRecords;
    private int rejectedRecords;
    private String filePath;
    private String rejectedFilePath;
    private long sellerId;

    @Id
    @Column(name = "IMPORT_ID")
    public long getImportId() {
        return importId;
    }

    public void setImportId(long importId) {
        this.importId = importId;
    }

    @Column(name = "MODULE")
    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    @Column(name = "MODULE_KEY")
    public String getModuleKey() {
        return moduleKey;
    }

    public void setModuleKey(String moduleKey) {
        this.moduleKey = moduleKey;
    }

    @Column(name = "IMPORTED_BY")
    public String getImportedBy() {
        return importedBy;
    }

    public void setImportedBy(String importedBy) {
        this.importedBy = importedBy;
    }

    @Column(name = "IMPORTED_ON")
    public Date getImportedOn() {
        return importedOn;
    }

    public void setImportedOn(Date importedOn) {
        this.importedOn = importedOn;
    }

    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "COMPLETED_ON")
    public Date getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(Date completedOn) {
        this.completedOn = completedOn;
    }

    @Column(name = "TOTAL_RECORDS")
    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    @Column(name = "SUCCESS_RECORDS")
    public int getSuccessRecords() {
        return successRecords;
    }

    public void setSuccessRecords(int successRecords) {
        this.successRecords = successRecords;
    }

    @Column(name = "REJECTED_RECORDS")
    public int getRejectedRecords() {
        return rejectedRecords;
    }

    public void setRejectedRecords(int rejectedRecords) {
        this.rejectedRecords = rejectedRecords;
    }

    @Column(name = "FILE_PATH")
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Column(name = "REJ_FILE_PATH")
    public String getRejectedFilePath() {
        return rejectedFilePath;
    }

    public void setRejectedFilePath(String rejectedFilePath) {
        this.rejectedFilePath = rejectedFilePath;
    }
 @Column(name = "SELLER_ID")
    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }
    
    

}
