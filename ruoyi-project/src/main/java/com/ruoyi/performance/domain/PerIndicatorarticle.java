package com.ruoyi.performance.domain;

import java.math.BigDecimal;

public class PerIndicatorarticle {

    private Integer indicatorarticleid;

    private String articletype;

    private BigDecimal points;

    public Integer getIndicatorarticleid() {
        return indicatorarticleid;
    }

    public void setIndicatorarticleid(Integer indicatorarticleid) {
        this.indicatorarticleid = indicatorarticleid;
    }

    public String getArticletype() {
        return articletype;
    }

    public void setArticletype(String articletype) {
        this.articletype = articletype;
    }

    public BigDecimal getPoints() {
        return points;
    }

    public void setPoints(BigDecimal points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "PerIndicatorarticle{" +
                "indicatorarticleid=" + indicatorarticleid +
                ", articletype='" + articletype + '\'' +
                ", points=" + points +
                '}';
    }
}