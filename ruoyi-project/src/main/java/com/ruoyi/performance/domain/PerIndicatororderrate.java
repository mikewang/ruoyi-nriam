package com.ruoyi.performance.domain;

import java.math.BigDecimal;

public class PerIndicatororderrate {

    private Integer indicatororderrateid;


    private String authororder;

    private BigDecimal rate;

    public Integer getIndicatororderrateid() {
        return indicatororderrateid;
    }

    public void setIndicatororderrateid(Integer indicatororderrateid) {
        this.indicatororderrateid = indicatororderrateid;
    }

    public String getAuthororder() {
        return authororder;
    }

    public void setAuthororder(String authororder) {
        this.authororder = authororder;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "PerIndicatororderrate{" +
                "indicatororderrateid=" + indicatororderrateid +
                ", authororder='" + authororder + '\'' +
                ", rate=" + rate +
                '}';
    }
}