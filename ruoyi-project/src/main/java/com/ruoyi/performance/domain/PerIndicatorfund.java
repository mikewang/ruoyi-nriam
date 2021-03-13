package com.ruoyi.performance.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

public class PerIndicatorfund extends BaseEntity {

    private Integer indicatorfundid;


    private String indicatortype;

    private Integer permoney;

    private BigDecimal points;

    public Integer getIndicatorfundid() {
        return indicatorfundid;
    }

    public void setIndicatorfundid(Integer indicatorfundid) {
        this.indicatorfundid = indicatorfundid;
    }

    public String getIndicatortype() {
        return indicatortype;
    }

    public void setIndicatortype(String indicatortype) {
        this.indicatortype = indicatortype;
    }

    public Integer getPermoney() {
        return permoney;
    }

    public void setPermoney(Integer permoney) {
        this.permoney = permoney;
    }

    public BigDecimal getPoints() {
        return points;
    }

    public void setPoints(BigDecimal points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "PerIndicatorfund{" +
                "indicatorfundid=" + indicatorfundid +
                ", indicatortype='" + indicatortype + '\'' +
                ", permoney=" + permoney +
                ", points=" + points +
                '}';
    }
}