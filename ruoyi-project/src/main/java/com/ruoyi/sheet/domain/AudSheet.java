package com.ruoyi.sheet.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.project.domain.AudProject;

import java.math.BigDecimal;
import java.util.List;

public class AudSheet extends BaseEntity {

    private Integer sheetid;

    private String sheetcode;

    private String sheettype;

    private Integer relatedcontractid;

    private String thispaytimes;

    private Integer sheetuserid;

    private String sheettime;

    private Integer projectid;

    private Integer organizationid;

    private BigDecimal hejiZong;


    private BigDecimal hejiXiaoji;


    private BigDecimal hejiYiqian;

    private BigDecimal hejiBennian;

    private BigDecimal hejiBenci;

    private String daxie;

    private String referenceid;

    private String reason;

    private String payconfirmtime;

    private Integer payconfirmuserid;

    private Integer sheetstatus;

    private String sheetuseridlinktext;
    private String projectidlinktext;
    private String organizationidlinktext;
    private String sheetstatuslinktext;
    private String payconfirmuseridlinktext;

    private Integer projectmanagerid;

    private AudProject projectinfo;



    private List<AudBudgetpay> budgetpayList;

    private Integer operateCode = 1;
    private Integer confirmResult;
    private String confirmNote;
    private Integer confirmUserid;
    private String audittype;

    private List<AudSheetauditrecord> sheetAuditRecordList;

    public Integer getSheetid() {
        return sheetid;
    }

    public void setSheetid(Integer sheetid) {
        this.sheetid = sheetid;
    }

    public String getSheetcode() {
        return sheetcode;
    }

    public void setSheetcode(String sheetcode) {
        this.sheetcode = sheetcode;
    }

    public String getSheettype() {
        return sheettype;
    }

    public void setSheettype(String sheettype) {
        this.sheettype = sheettype;
    }

    public Integer getRelatedcontractid() {
        return relatedcontractid;
    }

    public void setRelatedcontractid(Integer relatedcontractid) {
        this.relatedcontractid = relatedcontractid;
    }

    public String getThispaytimes() {
        return thispaytimes;
    }

    public void setThispaytimes(String thispaytimes) {
        this.thispaytimes = thispaytimes;
    }

    public Integer getSheetuserid() {
        return sheetuserid;
    }

    public void setSheetuserid(Integer sheetuserid) {
        this.sheetuserid = sheetuserid;
    }

    public String getSheettime() {
        return sheettime;
    }

    public void setSheettime(String sheettime) {
        this.sheettime = sheettime;
    }

    public Integer getProjectid() {
        return projectid;
    }

    public void setProjectid(Integer projectid) {
        this.projectid = projectid;
    }

    public Integer getOrganizationid() {
        return organizationid;
    }

    public void setOrganizationid(Integer organizationid) {
        this.organizationid = organizationid;
    }

    public BigDecimal getHejiZong() {
        return hejiZong;
    }

    public void setHejiZong(BigDecimal hejiZong) {
        this.hejiZong = hejiZong;
    }

    public BigDecimal getHejiXiaoji() {
        return hejiXiaoji;
    }

    public void setHejiXiaoji(BigDecimal hejiXiaoji) {
        this.hejiXiaoji = hejiXiaoji;
    }

    public BigDecimal getHejiYiqian() {
        return hejiYiqian;
    }

    public void setHejiYiqian(BigDecimal hejiYiqian) {
        this.hejiYiqian = hejiYiqian;
    }

    public BigDecimal getHejiBennian() {
        return hejiBennian;
    }

    public void setHejiBennian(BigDecimal hejiBennian) {
        this.hejiBennian = hejiBennian;
    }

    public BigDecimal getHejiBenci() {
        return hejiBenci;
    }

    public void setHejiBenci(BigDecimal hejiBenci) {
        this.hejiBenci = hejiBenci;
    }

    public String getDaxie() {
        return daxie;
    }

    public void setDaxie(String daxie) {
        this.daxie = daxie;
    }

    public String getReferenceid() {
        return referenceid;
    }

    public void setReferenceid(String referenceid) {
        this.referenceid = referenceid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPayconfirmtime() {
        return payconfirmtime;
    }

    public void setPayconfirmtime(String payconfirmtime) {
        this.payconfirmtime = payconfirmtime;
    }

    public Integer getPayconfirmuserid() {
        return payconfirmuserid;
    }

    public void setPayconfirmuserid(Integer payconfirmuserid) {
        this.payconfirmuserid = payconfirmuserid;
    }

    public Integer getSheetstatus() {
        return sheetstatus;
    }

    public void setSheetstatus(Integer sheetstatus) {
        this.sheetstatus = sheetstatus;
    }

    public String getSheetuseridlinktext() {
        return sheetuseridlinktext;
    }

    public void setSheetuseridlinktext(String sheetuseridlinktext) {
        this.sheetuseridlinktext = sheetuseridlinktext;
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

    public String getPayconfirmuseridlinktext() {
        return payconfirmuseridlinktext;
    }

    public void setPayconfirmuseridlinktext(String payconfirmuseridlinktext) {
        this.payconfirmuseridlinktext = payconfirmuseridlinktext;
    }

    public Integer getProjectmanagerid() {
        return projectmanagerid;
    }

    public void setProjectmanagerid(Integer projectmanagerid) {
        this.projectmanagerid = projectmanagerid;
    }

    public List<AudBudgetpay> getBudgetpayList() {
        return budgetpayList;
    }

    public void setBudgetpayList(List<AudBudgetpay> budgetpayList) {
        this.budgetpayList = budgetpayList;
    }

    public Integer getOperateCode() {
        return operateCode;
    }

    public void setOperateCode(Integer operateCode) {
        this.operateCode = operateCode;
    }

    public Integer getConfirmResult() {
        return confirmResult;
    }

    public void setConfirmResult(Integer confirmResult) {
        this.confirmResult = confirmResult;
    }

    public String getConfirmNote() {
        return confirmNote;
    }

    public void setConfirmNote(String confirmNote) {
        this.confirmNote = confirmNote;
    }

    public Integer getConfirmUserid() {
        return confirmUserid;
    }

    public void setConfirmUserid(Integer confirmUserid) {
        this.confirmUserid = confirmUserid;
    }

    public String getAudittype() {
        return audittype;
    }

    public void setAudittype(String audittype) {
        this.audittype = audittype;
    }

    public List<AudSheetauditrecord> getSheetAuditRecordList() {
        return sheetAuditRecordList;
    }

    public void setSheetAuditRecordList(List<AudSheetauditrecord> sheetAuditRecordList) {
        this.sheetAuditRecordList = sheetAuditRecordList;
    }

    public AudProject getProjectinfo() {
        return projectinfo;
    }

    public void setProjectinfo(AudProject projectinfo) {
        this.projectinfo = projectinfo;
    }

    @Override
    public String toString() {
        return "AudSheet{" +
                "sheetid=" + sheetid +
                ", sheetcode='" + sheetcode + '\'' +
                ", sheettype='" + sheettype + '\'' +
                ", relatedcontractid=" + relatedcontractid +
                ", thispaytimes='" + thispaytimes + '\'' +
                ", sheetuserid=" + sheetuserid +
                ", sheettime='" + sheettime + '\'' +
                ", projectid=" + projectid +
                ", organizationid=" + organizationid +
                ", hejiZong=" + hejiZong +
                ", hejiXiaoji=" + hejiXiaoji +
                ", hejiYiqian=" + hejiYiqian +
                ", hejiBennian=" + hejiBennian +
                ", hejiBenci=" + hejiBenci +
                ", daxie='" + daxie + '\'' +
                ", referenceid='" + referenceid + '\'' +
                ", reason='" + reason + '\'' +
                ", payconfirmtime='" + payconfirmtime + '\'' +
                ", payconfirmuserid=" + payconfirmuserid +
                ", sheetstatus=" + sheetstatus +
                ", sheetuseridlinktext='" + sheetuseridlinktext + '\'' +
                ", projectidlinktext='" + projectidlinktext + '\'' +
                ", organizationidlinktext='" + organizationidlinktext + '\'' +
                ", sheetstatuslinktext='" + sheetstatuslinktext + '\'' +
                ", payconfirmuseridlinktext='" + payconfirmuseridlinktext + '\'' +
                ", projectmanagerid=" + projectmanagerid +
                ", projectinfo=" + projectinfo +
                ", budgetpayList=" + budgetpayList +
                ", operateCode=" + operateCode +
                ", confirmResult=" + confirmResult +
                ", confirmNote='" + confirmNote + '\'' +
                ", confirmUserid=" + confirmUserid +
                ", audittype='" + audittype + '\'' +
                ", sheetAuditRecordList=" + sheetAuditRecordList +
                '}';
    }

}

