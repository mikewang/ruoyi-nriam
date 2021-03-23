package com.ruoyi.performance.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class PerRelation extends BaseEntity {

    private String indicatortype;

    private Integer level1id;

    private String level1name;

    private Integer level2id;

    private String level2name;

    public String getIndicatortype() {
        return indicatortype;
    }

    public void setIndicatortype(String indicatortype) {
        this.indicatortype = indicatortype;
    }

    public Integer getLevel1id() {
        return level1id;
    }

    public void setLevel1id(Integer level1id) {
        this.level1id = level1id;
    }

    public String getLevel1name() {
        return level1name;
    }

    public void setLevel1name(String level1name) {
        this.level1name = level1name;
    }

    public Integer getLevel2id() {
        return level2id;
    }

    public void setLevel2id(Integer level2id) {
        this.level2id = level2id;
    }

    public String getLevel2name() {
        return level2name;
    }

    public void setLevel2name(String level2name) {
        this.level2name = level2name;
    }

    @Override
    public String toString() {
        return "PerRelation{" +
                "indicatortype='" + indicatortype + '\'' +
                ", level1id=" + level1id +
                ", level1name='" + level1name + '\'' +
                ", level2id=" + level2id +
                ", level2name='" + level2name + '\'' +
                '}';
    }
}