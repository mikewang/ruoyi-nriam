package com.ruoyi.contract.domain;

import com.ruoyi.sheet.domain.SrmSupplierinfo;
import com.ruoyi.project.domain.AudProject;
import java.math.BigDecimal;
import java.util.List;

public class AudContract {

    private Integer contractid;

    private String contractcode;

    private String contractname;

    private Integer contracttype;

    private Integer contractuserid;

    private String contracttime;

    private Integer projectid;

    private Integer organizationid;

    private BigDecimal contractmoney;

    private String daxie;

    private Integer supplierid;

    private Integer paytotaltimes;

    private Integer payedtimes;

    private List<AudContractpay> contractpayList;

    private String reason;

    private String passtime;

    private String signconfirmtime;

    private Integer signconfirmuserid;

    private String firstpaytime;

    private Integer sheetstatus;

    private String contracttypelinktext;
    private String contractuseridlinktext;
    private String projectidlinktext;
    private String organizationidlinktext;
    private String supplieridlinktext;
    private String sheetstatuslinktext;

    private AudProject projectinfo;
    private SrmSupplierinfo supplierinfo;

    public Integer getContractid() {
        return contractid;
    }

    public void setContractid(Integer contractid) {
        this.contractid = contractid;
    }

    public String getContractcode() {
        return contractcode;
    }

    public void setContractcode(String contractcode) {
        this.contractcode = contractcode;
    }

    public String getContractname() {
        return contractname;
    }

    public void setContractname(String contractname) {
        this.contractname = contractname;
    }

    public Integer getContracttype() {
        return contracttype;
    }

    public void setContracttype(Integer contracttype) {
        this.contracttype = contracttype;
    }

    public Integer getContractuserid() {
        return contractuserid;
    }

    public void setContractuserid(Integer contractuserid) {
        this.contractuserid = contractuserid;
    }

    public String getContracttime() {
        return contracttime;
    }

    public void setContracttime(String contracttime) {
        this.contracttime = contracttime;
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

    public BigDecimal getContractmoney() {
        return contractmoney;
    }

    public void setContractmoney(BigDecimal contractmoney) {
        this.contractmoney = contractmoney;
    }

    public String getDaxie() {
        return daxie;
    }

    public void setDaxie(String daxie) {
        this.daxie = daxie;
    }

    public Integer getSupplierid() {
        return supplierid;
    }

    public void setSupplierid(Integer supplierid) {
        this.supplierid = supplierid;
    }

    public Integer getPaytotaltimes() {
        return paytotaltimes;
    }

    public void setPaytotaltimes(Integer paytotaltimes) {
        this.paytotaltimes = paytotaltimes;
    }

    public Integer getPayedtimes() {
        return payedtimes;
    }

    public void setPayedtimes(Integer payedtimes) {
        this.payedtimes = payedtimes;
    }

    public List<AudContractpay> getContractpayList() {
        return contractpayList;
    }

    public void setContractpayList(List<AudContractpay> contractpayList) {
        this.contractpayList = contractpayList;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPasstime() {
        return passtime;
    }

    public void setPasstime(String passtime) {
        this.passtime = passtime;
    }

    public String getSignconfirmtime() {
        return signconfirmtime;
    }

    public void setSignconfirmtime(String signconfirmtime) {
        this.signconfirmtime = signconfirmtime;
    }

    public Integer getSignconfirmuserid() {
        return signconfirmuserid;
    }

    public void setSignconfirmuserid(Integer signconfirmuserid) {
        this.signconfirmuserid = signconfirmuserid;
    }

    public String getFirstpaytime() {
        return firstpaytime;
    }

    public void setFirstpaytime(String firstpaytime) {
        this.firstpaytime = firstpaytime;
    }

    public Integer getSheetstatus() {
        return sheetstatus;
    }

    public void setSheetstatus(Integer sheetstatus) {
        this.sheetstatus = sheetstatus;
    }

    public String getContracttypelinktext() {
        return contracttypelinktext;
    }

    public void setContracttypelinktext(String contracttypelinktext) {
        this.contracttypelinktext = contracttypelinktext;
    }

    public String getContractuseridlinktext() {
        return contractuseridlinktext;
    }

    public void setContractuseridlinktext(String contractuseridlinktext) {
        this.contractuseridlinktext = contractuseridlinktext;
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

    public String getSupplieridlinktext() {
        return supplieridlinktext;
    }

    public void setSupplieridlinktext(String supplieridlinktext) {
        this.supplieridlinktext = supplieridlinktext;
    }

    public String getSheetstatuslinktext() {
        return sheetstatuslinktext;
    }

    public void setSheetstatuslinktext(String sheetstatuslinktext) {
        this.sheetstatuslinktext = sheetstatuslinktext;
    }

    public AudProject getProjectinfo() {
        return projectinfo;
    }

    public void setProjectinfo(AudProject projectinfo) {
        this.projectinfo = projectinfo;
    }

    public SrmSupplierinfo getSupplierinfo() {
        return supplierinfo;
    }

    public void setSupplierinfo(SrmSupplierinfo supplierinfo) {
        this.supplierinfo = supplierinfo;
    }

    @Override
    public String toString() {
        return "AudContract{" +
                "contractid=" + contractid +
                ", contractcode='" + contractcode + '\'' +
                ", contractname='" + contractname + '\'' +
                ", contracttype=" + contracttype +
                ", contractuserid=" + contractuserid +
                ", contracttime='" + contracttime + '\'' +
                ", projectid=" + projectid +
                ", organizationid=" + organizationid +
                ", contractmoney=" + contractmoney +
                ", daxie='" + daxie + '\'' +
                ", supplierid=" + supplierid +
                ", paytotaltimes=" + paytotaltimes +
                ", payedtimes=" + payedtimes +
                ", reason='" + reason + '\'' +
                ", passtime='" + passtime + '\'' +
                ", signconfirmtime='" + signconfirmtime + '\'' +
                ", signconfirmuserid=" + signconfirmuserid +
                ", firstpaytime='" + firstpaytime + '\'' +
                ", sheetstatus=" + sheetstatus +
                ", contracttypelinktext='" + contracttypelinktext + '\'' +
                ", contractuseridlinktext='" + contractuseridlinktext + '\'' +
                ", projectidlinktext='" + projectidlinktext + '\'' +
                ", organizationidlinktext='" + organizationidlinktext + '\'' +
                ", supplieridlinktext='" + supplieridlinktext + '\'' +
                ", sheetstatuslinktext='" + sheetstatuslinktext + '\'' +
                '}';
    }
}