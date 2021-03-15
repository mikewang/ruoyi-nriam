package com.ruoyi.performance.domain;

import java.math.BigDecimal;

public class PerIndicatorappraisal {

    private Integer indicatorappraisalid;

    private BigDecimal points;

    public Integer getIndicatorappraisalid() {
        return indicatorappraisalid;
    }

    public void setIndicatorappraisalid(Integer indicatorappraisalid) {
        this.indicatorappraisalid = indicatorappraisalid;
    }

    public BigDecimal getPoints() {
        return points;
    }

    public void setPoints(BigDecimal points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "PerIndicatorappraisal{" +
                "indicatorappraisalid=" + indicatorappraisalid +
                ", points=" + points +
                '}';
    }
}