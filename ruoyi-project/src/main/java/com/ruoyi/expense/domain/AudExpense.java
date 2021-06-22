package com.ruoyi.expense.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.project.domain.AudProject;

import java.math.BigDecimal;

public class AudExpense extends BaseEntity {

    private Integer expensesheetid;


    private String expensename;


    private String expensesheetcode;


    private Integer sheetuserid;

    private String sheettime;


    private Integer projectid;


    private Integer organizationid;


    private BigDecimal money;


    private String daxie;


    private String reason;


    private Integer vouchercount;


    private String passtime;

    private Integer sheetstatus;


    private String sheetuseridlinktext;
    private String projectidlinktext;
    private String organizationidlinktext;
    private String sheetstatuslinktext;

    private AudProject projectinfo;

    public Integer getExpensesheetid() {
        return expensesheetid;
    }

    public void setExpensesheetid(Integer expensesheetid) {
        this.expensesheetid = expensesheetid;
    }

    public String getExpensename() {
        return expensename;
    }

    public void setExpensename(String expensename) {
        this.expensename = expensename;
    }

    public String getExpensesheetcode() {
        return expensesheetcode;
    }

    public void setExpensesheetcode(String expensesheetcode) {
        this.expensesheetcode = expensesheetcode;
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

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getDaxie() {
        return daxie;
    }

    public void setDaxie(String daxie) {
        this.daxie = daxie;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getVouchercount() {
        return vouchercount;
    }

    public void setVouchercount(Integer vouchercount) {
        this.vouchercount = vouchercount;
    }

    public String getPasstime() {
        return passtime;
    }

    public void setPasstime(String passtime) {
        this.passtime = passtime;
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


    public AudProject getProjectinfo() {
        return projectinfo;
    }

    public void setProjectinfo(AudProject projectinfo) {
        this.projectinfo = projectinfo;
    }

    @Override
    public String toString() {
        return "AudExpense{" +
                "expensesheetid=" + expensesheetid +
                ", expensename='" + expensename + '\'' +
                ", expensesheetcode='" + expensesheetcode + '\'' +
                ", sheetuserid=" + sheetuserid +
                ", sheettime='" + sheettime + '\'' +
                ", projectid=" + projectid +
                ", organizationid=" + organizationid +
                ", money=" + money +
                ", daxie='" + daxie + '\'' +
                ", reason='" + reason + '\'' +
                ", vouchercount=" + vouchercount +
                ", passtime='" + passtime + '\'' +
                ", sheetstatus=" + sheetstatus +
                '}';
    }
}