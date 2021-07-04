package com.ruoyi.sku.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

public class SkuPhoto extends BaseEntity {

    private Long photoId;


    private String photoSizeValue;


    private Date created;


    private Date modified;


    private Long skuId;


    private Integer status;


    private String photoText;

    private String photoSizeLabel;

    public Long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Long photoId) {
        this.photoId = photoId;
    }

    public String getPhotoSizeValue() {
        return photoSizeValue;
    }

    public void setPhotoSizeValue(String photoSizeValue) {
        this.photoSizeValue = photoSizeValue;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPhotoText() {
        return photoText;
    }

    public void setPhotoText(String photoText) {
        this.photoText = photoText;
    }

    public String getPhotoSizeLabel() {
        return photoSizeLabel;
    }

    public void setPhotoSizeLabel(String photoSizeLabel) {
        this.photoSizeLabel = photoSizeLabel;
    }

    @Override
    public String toString() {
        return "SkuPhoto{" +
                "photoId=" + photoId +
                ", photoSizeValue='" + photoSizeValue + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                ", skuId=" + skuId +
                ", status=" + status +
                ", photoText='" + photoText + '\'' +
                ", photoSizeLabel='" + photoSizeLabel + '\'' +
                '}';
    }
}