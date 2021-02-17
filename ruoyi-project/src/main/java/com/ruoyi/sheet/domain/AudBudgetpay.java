package com.ruoyi.sheet.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

public class AudBudgetpay extends BaseEntity {

    private Integer payid;

    private Integer sheetid;

    private Integer projectid;

    private Integer supplierid;

    private BigDecimal zong;

    private BigDecimal xiaoji;

    private BigDecimal yiqian;

    private BigDecimal bennian;

    private BigDecimal benci;

    private String audittime;

    private String sheettype;
    private String projectidlinktext;
    private String suppliername;
    private String character;

    public Integer getPayid() {
        return payid;
    }

    public void setPayid(Integer payid) {
        this.payid = payid;
    }

    public Integer getSheetid() {
        return sheetid;
    }

    public void setSheetid(Integer sheetid) {
        this.sheetid = sheetid;
    }

    public Integer getProjectid() {
        return projectid;
    }

    public void setProjectid(Integer projectid) {
        this.projectid = projectid;
    }

    public Integer getSupplierid() {
        return supplierid;
    }

    public void setSupplierid(Integer supplierid) {
        this.supplierid = supplierid;
    }

    public BigDecimal getZong() {
        return zong;
    }

    public void setZong(BigDecimal zong) {
        this.zong = zong;
    }

    public BigDecimal getXiaoji() {
        return xiaoji;
    }

    public void setXiaoji(BigDecimal xiaoji) {
        this.xiaoji = xiaoji;
    }

    public BigDecimal getYiqian() {
        return yiqian;
    }

    public void setYiqian(BigDecimal yiqian) {
        this.yiqian = yiqian;
    }

    public BigDecimal getBennian() {
        return bennian;
    }

    public void setBennian(BigDecimal bennian) {
        this.bennian = bennian;
    }

    public BigDecimal getBenci() {
        return benci;
    }

    public void setBenci(BigDecimal benci) {
        this.benci = benci;
    }

    public String getAudittime() {
        return audittime;
    }

    public void setAudittime(String audittime) {
        this.audittime = audittime;
    }

    public String getSheettype() {
        return sheettype;
    }

    public void setSheettype(String sheettype) {
        this.sheettype = sheettype;
    }

    public String getProjectidlinktext() {
        return projectidlinktext;
    }

    public void setProjectidlinktext(String projectidlinktext) {
        this.projectidlinktext = projectidlinktext;
    }

    public String getSuppliername() {
        return suppliername;
    }

    public void setSuppliername(String suppliername) {
        this.suppliername = suppliername;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    @Override
    public String toString() {
        return "AudBudgetpay{" +
                "payid=" + payid +
                ", sheetid=" + sheetid +
                ", projectid=" + projectid +
                ", supplierid=" + supplierid +
                ", zong=" + zong +
                ", xiaoji=" + xiaoji +
                ", yiqian=" + yiqian +
                ", bennian=" + bennian +
                ", benci=" + benci +
                ", audittime='" + audittime + '\'' +
                ", sheettype='" + sheettype + '\'' +
                ", projectidlinktext='" + projectidlinktext + '\'' +
                ", suppliername='" + suppliername + '\'' +
                ", character='" + character + '\'' +
                '}';
    }
}
