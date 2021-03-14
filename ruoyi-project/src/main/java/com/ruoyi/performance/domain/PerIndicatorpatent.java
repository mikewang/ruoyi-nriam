package com.ruoyi.performance.domain;

import java.math.BigDecimal;

public class PerIndicatorpatent {

    private Integer indicatorpatentid;


    private String patenttype;


    private BigDecimal points;

    public Integer getIndicatorpatentid() {
        return indicatorpatentid;
    }

    public void setIndicatorpatentid(Integer indicatorpatentid) {
        this.indicatorpatentid = indicatorpatentid;
    }

    public String getPatenttype() {
        return patenttype;
    }

    public void setPatenttype(String patenttype) {
        this.patenttype = patenttype;
    }

    public BigDecimal getPoints() {
        return points;
    }

    public void setPoints(BigDecimal points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "PerIndicatorpatent{" +
                "indicatorpatentid=" + indicatorpatentid +
                ", patenttype='" + patenttype + '\'' +
                ", points=" + points +
                '}';
    }
}