package com.ruoyi.sku.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;
import java.util.List;

public class SkuInfo extends BaseEntity {

    private Long skuId;

    private String skuName;

    private Long userId;

    private Date created;

    private Integer status;

    private List<SkuPhoto> photoList;
    private List<String>  photoSizeValues;

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<SkuPhoto> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<SkuPhoto> photoList) {
        this.photoList = photoList;
    }

    public List<String> getPhotoSizeValues() {
        return photoSizeValues;
    }

    public void setPhotoSizeValues(List<String> photoSizeValues) {
        this.photoSizeValues = photoSizeValues;
    }

    @Override
    public String toString() {
        return "SkuInfo{" +
                "skuId=" + skuId +
                ", skuName='" + skuName + '\'' +
                ", userId=" + userId +
                ", created=" + created +
                ", status=" + status +
                ", photoList=" + photoList +
                ", photoSizeValues=" + photoSizeValues +
                '}';
    }
}