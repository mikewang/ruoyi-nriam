package com.ruoyi.sku.domain;

import java.util.Date;

public class SkuFile {

    private Long fileId;

    private String photoSizeValue;

    private String fileName;
    private String fileType;

    private String relativepath;


    private Date created;


    private Date modified;

    private Long skuId;


    private Integer status;


    private String photoSizeLabel;

    private String url;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getPhotoSizeValue() {
        return photoSizeValue;
    }

    public void setPhotoSizeValue(String photoSizeValue) {
        this.photoSizeValue = photoSizeValue;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getRelativepath() {
        return relativepath;
    }

    public void setRelativepath(String relativepath) {
        this.relativepath = relativepath;
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

    public String getPhotoSizeLabel() {
        return photoSizeLabel;
    }

    public void setPhotoSizeLabel(String photoSizeLabel) {
        this.photoSizeLabel = photoSizeLabel;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "SkuFile{" +
                "fileId=" + fileId +
                ", photoSizeValue='" + photoSizeValue + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", relativepath='" + relativepath + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                ", skuId=" + skuId +
                ", status=" + status +
                '}';
    }
}