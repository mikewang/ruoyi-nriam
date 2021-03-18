package com.ruoyi.performance.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.util.ArrayList;
import java.util.List;

public class PerIndicator extends BaseEntity {

    private Integer indicatorid;


    private String indicatortype;


    private String indicatorname;


    private Integer indicatorlevel;

    private Integer level1id;


    private Integer level2id;


    private String score;


    private Integer ordernumber;

    private Boolean ifdisplay;

    private Boolean ifselfapply;

    private Boolean ifdeleted;
    private String level1name;
    private String level2name;

    private Integer childcount;

    private Integer parentid;

    private List<PerIndicator> childList;


    public Integer getIndicatorid() {
        return indicatorid;
    }

    public void setIndicatorid(Integer indicatorid) {
        this.indicatorid = indicatorid;
    }

    public String getIndicatortype() {
        return indicatortype;
    }

    public void setIndicatortype(String indicatortype) {
        this.indicatortype = indicatortype;
    }

    public String getIndicatorname() {
        return indicatorname;
    }

    public void setIndicatorname(String indicatorname) {
        this.indicatorname = indicatorname;
    }

    public Integer getIndicatorlevel() {
        return indicatorlevel;
    }

    public void setIndicatorlevel(Integer indicatorlevel) {
        this.indicatorlevel = indicatorlevel;
    }

    public Integer getLevel1id() {
        return level1id;
    }

    public void setLevel1id(Integer level1id) {
        this.level1id = level1id;
    }

    public Integer getLevel2id() {
        return level2id;
    }

    public void setLevel2id(Integer level2id) {
        this.level2id = level2id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Integer getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(Integer ordernumber) {
        this.ordernumber = ordernumber;
    }

    public Boolean getIfdisplay() {
        return ifdisplay;
    }

    public void setIfdisplay(Boolean ifdisplay) {
        this.ifdisplay = ifdisplay;
    }

    public Boolean getIfselfapply() {
        return ifselfapply;
    }

    public void setIfselfapply(Boolean ifselfapply) {
        this.ifselfapply = ifselfapply;
    }

    public Boolean getIfdeleted() {
        return ifdeleted;
    }

    public void setIfdeleted(Boolean ifdeleted) {
        this.ifdeleted = ifdeleted;
    }

    public String getLevel1name() {
        return level1name;
    }

    public void setLevel1name(String level1name) {
        this.level1name = level1name;
    }

    public String getLevel2name() {
        return level2name;
    }

    public void setLevel2name(String level2name) {
        this.level2name = level2name;
    }

    public Integer getChildcount() {
        return childcount;
    }

    public void setChildcount(Integer childcount) {
        this.childcount = childcount;
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public List<PerIndicator> getChildList() {
        return childList;
    }

    @Override
    public String toString() {
        return "PerIndicator{" +
                "indicatorid=" + indicatorid +
                ", indicatortype='" + indicatortype + '\'' +
                ", indicatorname='" + indicatorname + '\'' +
                ", indicatorlevel=" + indicatorlevel +
                ", level1id=" + level1id +
                ", level2id=" + level2id +
                ", score='" + score + '\'' +
                ", ordernumber=" + ordernumber +
                ", ifdisplay=" + ifdisplay +
                ", ifselfapply=" + ifselfapply +
                ", ifdeleted=" + ifdeleted +
                ", level1name='" + level1name + '\'' +
                ", level2name='" + level2name + '\'' +
                ", childcount=" + childcount +
                ", parentid=" + parentid +
                ", childList=" + childList +
                '}';
    }

    public void setChildList(List<PerIndicator> childList) {
        this.childList = childList;
    }

}