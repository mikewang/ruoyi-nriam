package com.ruoyi.performance.domain;

import java.math.BigDecimal;

public class PerIndicatorstandard {

    private Integer indicatorstandardid;


    private String standardtype;

    private BigDecimal points;


    public Integer getIndicatorstandardid() {
        return indicatorstandardid;
    }

    public void setIndicatorstandardid(Integer indicatorstandardid) {
        this.indicatorstandardid = indicatorstandardid;
    }

    public String getStandardtype() {
        return standardtype;
    }

    public void setStandardtype(String standardtype) {
        this.standardtype = standardtype;
    }

    public BigDecimal getPoints() {
        return points;
    }

    public void setPoints(BigDecimal points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "PerIndicatorstandard{" +
                "indicatorstandardid=" + indicatorstandardid +
                ", standardtype='" + standardtype + '\'' +
                ", points=" + points +
                '}';
    }
}