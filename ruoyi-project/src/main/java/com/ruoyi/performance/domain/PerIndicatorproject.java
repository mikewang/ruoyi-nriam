package com.ruoyi.performance.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

public class PerIndicatorproject extends BaseEntity {

    private Integer indicatorprojectid;

    private Integer projecttype;

    private String jointype;

    private BigDecimal points;

    private Boolean ifdeleted;

    private String projecttypelinktext;

    public Integer getIndicatorprojectid() {
        return indicatorprojectid;
    }

    public void setIndicatorprojectid(Integer indicatorprojectid) {
        this.indicatorprojectid = indicatorprojectid;
    }

    public Integer getProjecttype() {
        return projecttype;
    }

    public void setProjecttype(Integer projecttype) {
        this.projecttype = projecttype;
    }

    public String getJointype() {
        return jointype;
    }

    public void setJointype(String jointype) {
        this.jointype = jointype;
    }

    public BigDecimal getPoints() {
        return points;
    }

    public void setPoints(BigDecimal points) {
        this.points = points;
    }

    public Boolean getIfdeleted() {
        return ifdeleted;
    }

    public void setIfdeleted(Boolean ifdeleted) {
        this.ifdeleted = ifdeleted;
    }

    public String getProjecttypelinktext() {
        return projecttypelinktext;
    }

    public void setProjecttypelinktext(String projecttypelinktext) {
        this.projecttypelinktext = projecttypelinktext;
    }

    @Override
    public String toString() {
        return "PerIndicatorproject{" +
                "indicatorprojectid=" + indicatorprojectid +
                ", projecttype=" + projecttype +
                ", jointype='" + jointype + '\'' +
                ", points=" + points +
                ", ifdeleted=" + ifdeleted +
                ", projecttypelinktext='" + projecttypelinktext + '\'' +
                '}';
    }
}