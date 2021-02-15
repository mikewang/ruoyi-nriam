package com.ruoyi.sheet.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class AudSheetauditrecord extends BaseEntity {

    private Integer auditid;


    private String sheettype;


    private Integer sheetid;

    private String audittype;


    private Boolean auditresult;


    private String auditopinion;

    private Integer audituserid;


    private String audittime;

    private String signpicName;

    public Integer getAuditid() {
        return auditid;
    }

    public void setAuditid(Integer auditid) {
        this.auditid = auditid;
    }

    public String getSheettype() {
        return sheettype;
    }

    public void setSheettype(String sheettype) {
        this.sheettype = sheettype;
    }

    public Integer getSheetid() {
        return sheetid;
    }

    public void setSheetid(Integer sheetid) {
        this.sheetid = sheetid;
    }

    public String getAudittype() {
        return audittype;
    }

    public void setAudittype(String audittype) {
        this.audittype = audittype;
    }

    public Boolean getAuditresult() {
        return auditresult;
    }

    public void setAuditresult(Boolean auditresult) {
        this.auditresult = auditresult;
    }

    public String getAuditopinion() {
        return auditopinion;
    }

    public void setAuditopinion(String auditopinion) {
        this.auditopinion = auditopinion;
    }

    public Integer getAudituserid() {
        return audituserid;
    }

    public void setAudituserid(Integer audituserid) {
        this.audituserid = audituserid;
    }

    public String getAudittime() {
        return audittime;
    }

    public void setAudittime(String audittime) {
        this.audittime = audittime;
    }

    public String getSignpicName() {
        return signpicName;
    }

    public void setSignpicName(String signpicName) {
        this.signpicName = signpicName;
    }

    @Override
    public String toString() {
        return "AudSheetauditrecord{" +
                "auditid=" + auditid +
                ", sheettype='" + sheettype + '\'' +
                ", sheetid=" + sheetid +
                ", audittype='" + audittype + '\'' +
                ", auditresult=" + auditresult +
                ", auditopinion='" + auditopinion + '\'' +
                ", audituserid=" + audituserid +
                ", audittime='" + audittime + '\'' +
                ", signpicName='" + signpicName + '\'' +
                '}';
    }
}