package com.ruoyi.performance.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

public class PerFund extends BaseEntity {

    private Integer fundid;


    private Integer projectid;


    private Integer year;


    private BigDecimal fund;


    private String status;
//
//    public PerFund(){
//
//        fund = BigDecimal.ZERO;
//
//    }

    public Integer getFundid() {
        return fundid;
    }

    public void setFundid(Integer fundid) {
        this.fundid = fundid;
    }

    public Integer getProjectid() {
        return projectid;
    }

    public void setProjectid(Integer projectid) {
        this.projectid = projectid;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getFund() {
        return fund;
    }

    public void setFund(BigDecimal fund) {
        this.fund = fund;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PerFund{" +
                "fundid=" + fundid +
                ", projectid=" + projectid +
                ", year=" + year +
                ", fund=" + fund +
                ", status='" + status + '\'' +
                '}';
    }
}