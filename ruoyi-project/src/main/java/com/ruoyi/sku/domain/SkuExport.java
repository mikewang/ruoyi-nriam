package com.ruoyi.sku.domain;

import java.util.Date;
import java.util.List;

public class SkuExport {

    private Long exportId;

    private Long userId;


    private Date exportTime;


    private Long skuId;


    private Long photoId;


    private Integer status;


    private String exportPath;

    private List<Date> exportTimes;

    public Long getExportId() {
        return exportId;
    }

    public void setExportId(Long exportId) {
        this.exportId = exportId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getExportTime() {
        return exportTime;
    }

    public void setExportTime(Date exportTime) {
        this.exportTime = exportTime;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Long photoId) {
        this.photoId = photoId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getExportPath() {
        return exportPath;
    }

    public void setExportPath(String exportPath) {
        this.exportPath = exportPath;
    }

    public List<Date> getExportTimes() {
        return exportTimes;
    }

    public void setExportTimes(List<Date> exportTimes) {
        this.exportTimes = exportTimes;
    }

    @Override
    public String toString() {
        return "SkuExport{" +
                "exportId=" + exportId +
                ", userId=" + userId +
                ", exportTime=" + exportTime +
                ", skuId=" + skuId +
                ", photoId=" + photoId +
                ", status=" + status +
                ", exportPath='" + exportPath + '\'' +
                ", exportTimes=" + exportTimes +
                '}';
    }
}