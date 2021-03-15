package com.ruoyi.performance.domain;

import java.math.BigDecimal;

public class PerIndicatorthesis {

    private Integer indicatorthesisid;


    private String thesislevel;


    private BigDecimal points;

    public Integer getIndicatorthesisid() {
        return indicatorthesisid;
    }

    public void setIndicatorthesisid(Integer indicatorthesisid) {
        this.indicatorthesisid = indicatorthesisid;
    }

    public String getThesislevel() {
        return thesislevel;
    }

    public void setThesislevel(String thesislevel) {
        this.thesislevel = thesislevel;
    }

    public BigDecimal getPoints() {
        return points;
    }

    public void setPoints(BigDecimal points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "PerIndicatorthesis{" +
                "indicatorthesisid=" + indicatorthesisid +
                ", thesislevel='" + thesislevel + '\'' +
                ", points=" + points +
                '}';
    }
}