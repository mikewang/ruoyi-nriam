package com.ruoyi.performance.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

public class PerIndicatorprize  extends BaseEntity {

    private Integer indicatorprizeid;


    private Integer prizetype;


    private Integer prizelevel;


    private Integer prizerank;

    private BigDecimal points;

    private Boolean ifdeleted;

    private String prizetypelinktext;
    private String prizelevellinktext;
    private String prizeranklinktext;


    public Integer getIndicatorprizeid() {
        return indicatorprizeid;
    }

    public void setIndicatorprizeid(Integer indicatorprizeid) {
        this.indicatorprizeid = indicatorprizeid;
    }

    public Integer getPrizetype() {
        return prizetype;
    }

    public void setPrizetype(Integer prizetype) {
        this.prizetype = prizetype;
    }

    public Integer getPrizelevel() {
        return prizelevel;
    }

    public void setPrizelevel(Integer prizelevel) {
        this.prizelevel = prizelevel;
    }

    public Integer getPrizerank() {
        return prizerank;
    }

    public void setPrizerank(Integer prizerank) {
        this.prizerank = prizerank;
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

    public String getPrizetypelinktext() {
        return prizetypelinktext;
    }

    public void setPrizetypelinktext(String prizetypelinktext) {
        this.prizetypelinktext = prizetypelinktext;
    }

    public String getPrizelevellinktext() {
        return prizelevellinktext;
    }

    public void setPrizelevellinktext(String prizelevellinktext) {
        this.prizelevellinktext = prizelevellinktext;
    }

    public String getPrizeranklinktext() {
        return prizeranklinktext;
    }

    public void setPrizeranklinktext(String prizeranklinktext) {
        this.prizeranklinktext = prizeranklinktext;
    }

    @Override
    public String toString() {
        return "PerIndicatorprize{" +
                "indicatorprizeid=" + indicatorprizeid +
                ", prizetype=" + prizetype +
                ", prizelevel=" + prizelevel +
                ", prizerank=" + prizerank +
                ", points=" + points +
                ", ifdeleted=" + ifdeleted +
                ", prizetypelinktext='" + prizetypelinktext + '\'' +
                ", prizelevellinktext='" + prizelevellinktext + '\'' +
                ", prizeranklinktext='" + prizeranklinktext + '\'' +
                '}';
    }
}