package com.ruoyi.performance.domain;

import java.math.BigDecimal;

public class PerIncome {

    private Integer incomeid;


    private Integer teamid;


    private Integer year;


    private String projectname;


    private String projectcode;


    private Integer managerid;


    private String period;


    private BigDecimal contractmoney;


    private BigDecimal thisyearmoney;

    private String type;

    private String status;
    private String teamidlinktext;
    private String manageridlinktext;

    public Integer getIncomeid() {
        return incomeid;
    }

    public void setIncomeid(Integer incomeid) {
        this.incomeid = incomeid;
    }

    public Integer getTeamid() {
        return teamid;
    }

    public void setTeamid(Integer teamid) {
        this.teamid = teamid;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getProjectcode() {
        return projectcode;
    }

    public void setProjectcode(String projectcode) {
        this.projectcode = projectcode;
    }

    public Integer getManagerid() {
        return managerid;
    }

    public void setManagerid(Integer managerid) {
        this.managerid = managerid;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public BigDecimal getContractmoney() {
        return contractmoney;
    }

    public void setContractmoney(BigDecimal contractmoney) {
        this.contractmoney = contractmoney;
    }

    public BigDecimal getThisyearmoney() {
        return thisyearmoney;
    }

    public void setThisyearmoney(BigDecimal thisyearmoney) {
        this.thisyearmoney = thisyearmoney;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTeamidlinktext() {
        return teamidlinktext;
    }

    public void setTeamidlinktext(String teamidlinktext) {
        this.teamidlinktext = teamidlinktext;
    }

    public String getManageridlinktext() {
        return manageridlinktext;
    }

    public void setManageridlinktext(String manageridlinktext) {
        this.manageridlinktext = manageridlinktext;
    }

    @Override
    public String toString() {
        return "PerIncome{" +
                "incomeid=" + incomeid +
                ", teamid=" + teamid +
                ", year=" + year +
                ", projectname='" + projectname + '\'' +
                ", projectcode='" + projectcode + '\'' +
                ", managerid=" + managerid +
                ", period='" + period + '\'' +
                ", contractmoney=" + contractmoney +
                ", thisyearmoney=" + thisyearmoney +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", teamidlinktext='" + teamidlinktext + '\'' +
                ", manageridlinktext='" + manageridlinktext + '\'' +
                '}';
    }
}