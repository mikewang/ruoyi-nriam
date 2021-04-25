package com.ruoyi.audit.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

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

    private String audittypeName;
    private String auditresultName;



    // 附加数据，我的审批记录显示用。
    private String code;
    private String name;

    private String sheetuseridlinktext;
    private String supplieridlinktext;

    private String projectidlinktext;
    private String organizationidlinktext;
    private String sheetstatuslinktext;

    private BigDecimal money;


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

    public String getAudittypeName() {
        if (audittype.equals("3")) {
            return "项目负责人审批";
        }
        else if (audittype.equals("4")) {
            return "部门负责人审批";
        }
        else if (audittype.equals("5")) {
            return "分管处审批";
        }
        else if (audittype.equals("6")) {
            return "分管所长审批";
        }
        else if (audittype.equals("7")) {
            return "所长审批";
        }

        return audittypeName;
    }

    public void setAudittypeName(String audittypeName) {
        this.audittypeName = audittypeName;
    }

    public String getAuditresultName() {
        if (auditresult) {
            return "通过";
        }
        else {
            return "不通过";
        }

    }

    public void setAuditresultName(String auditresultName) {
        this.auditresultName = auditresultName;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSheetuseridlinktext() {
        return sheetuseridlinktext;
    }

    public void setSheetuseridlinktext(String sheetuseridlinktext) {
        this.sheetuseridlinktext = sheetuseridlinktext;
    }

    public String getSupplieridlinktext() {
        return supplieridlinktext;
    }

    public void setSupplieridlinktext(String supplieridlinktext) {
        this.supplieridlinktext = supplieridlinktext;
    }

    public String getProjectidlinktext() {
        return projectidlinktext;
    }

    public void setProjectidlinktext(String projectidlinktext) {
        this.projectidlinktext = projectidlinktext;
    }

    public String getOrganizationidlinktext() {
        return organizationidlinktext;
    }

    public void setOrganizationidlinktext(String organizationidlinktext) {
        this.organizationidlinktext = organizationidlinktext;
    }

    public String getSheetstatuslinktext() {
        return sheetstatuslinktext;
    }

    public void setSheetstatuslinktext(String sheetstatuslinktext) {
        this.sheetstatuslinktext = sheetstatuslinktext;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
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
                ", audittypeName='" + audittypeName + '\'' +
                ", auditresultName='" + auditresultName + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}