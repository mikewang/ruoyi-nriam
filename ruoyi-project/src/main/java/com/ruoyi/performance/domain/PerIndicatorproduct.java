package com.ruoyi.performance.domain;

import java.math.BigDecimal;

public class PerIndicatorproduct {

    private Integer indicatorproductid;

    private BigDecimal points;

    public Integer getIndicatorproductid() {
        return indicatorproductid;
    }

    public void setIndicatorproductid(Integer indicatorproductid) {
        this.indicatorproductid = indicatorproductid;
    }

    public BigDecimal getPoints() {
        return points;
    }

    public void setPoints(BigDecimal points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "PerIndicatorproduct{" +
                "indicatorproductid=" + indicatorproductid +
                ", points=" + points +
                '}';
    }
}