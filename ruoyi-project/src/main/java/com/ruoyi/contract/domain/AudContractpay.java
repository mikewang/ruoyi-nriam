package com.ruoyi.contract.domain;

import java.math.BigDecimal;

public class AudContractpay {

    private Integer contractid;

    private Integer times;

    private Integer paypercent;

    private BigDecimal percentmoney;

    private String timesname;

    public Integer getContractid() {
        return contractid;
    }

    public void setContractid(Integer contractid) {
        this.contractid = contractid;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Integer getPaypercent() {
        return paypercent;
    }

    public void setPaypercent(Integer paypercent) {
        this.paypercent = paypercent;
    }

    public BigDecimal getPercentmoney() {
        return percentmoney;
    }

    public void setPercentmoney(BigDecimal percentmoney) {
        this.percentmoney = percentmoney;
    }

    public String getTimesname() {

        String  name = "第" + this.times.toString() + "期金额";

        return name;
    }

    public void setTimesname(String timesname) {
        this.timesname = timesname;
    }

    @Override
    public String toString() {
        return "AudContractpay{" +
                "contractid=" + contractid +
                ", times=" + times +
                ", paypercent=" + paypercent +
                ", percentmoney=" + percentmoney +
                '}';
    }
}