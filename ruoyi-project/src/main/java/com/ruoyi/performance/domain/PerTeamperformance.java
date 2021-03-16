package com.ruoyi.performance.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

public class PerTeamperformance extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Integer performanceid;


    private Integer teamid;
//            excel.AddContentRow(new string[] {"团队名称","年度","一级指标","二级指标",
//            "类型","详情","评分依据","分数","状态"});    //标题行

    /** 年度 */
    @Excel(name = "年度")
    private Integer performanceyear;

    @Excel(name = "类型")
    private String indicatortype;


    @Excel(name = "详情")
    private String indicatortypeDetail;

    private Integer indicatorid;


    private Integer achieveid;

    @Excel(name = "评分依据")
    private String description;


    private BigDecimal basepoints;

    private Integer authoruserid;


    private Integer authororder;


    @Excel(name = "分数")
    private BigDecimal points;


    private Integer level1id;

    @Excel(name = "一级指标")
    private String level1name;


    private Integer level2id;

    @Excel(name = "二级指标")
    private String level2name;

    @Excel(name = "状态")
    private String status;

    /** 团队名称 */
    @Excel(name = "团队名称")
    private String teamidlinktext;

    private Integer logcount;

    public Integer getPerformanceid() {
        return performanceid;
    }

    public void setPerformanceid(Integer performanceid) {
        this.performanceid = performanceid;
    }

    public Integer getTeamid() {
        return teamid;
    }

    public void setTeamid(Integer teamid) {
        this.teamid = teamid;
    }

    public Integer getPerformanceyear() {
        return performanceyear;
    }

    public void setPerformanceyear(Integer performanceyear) {
        this.performanceyear = performanceyear;
    }

    public String getIndicatortype() {
        return indicatortype;
    }

    public void setIndicatortype(String indicatortype) {
        this.indicatortype = indicatortype;
    }

    public Integer getIndicatorid() {
        return indicatorid;
    }

    public void setIndicatorid(Integer indicatorid) {
        this.indicatorid = indicatorid;
    }

    public Integer getAchieveid() {
        return achieveid;
    }

    public void setAchieveid(Integer achieveid) {
        this.achieveid = achieveid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getBasepoints() {
        return basepoints;
    }

    public void setBasepoints(BigDecimal basepoints) {
        this.basepoints = basepoints;
    }

    public Integer getAuthoruserid() {
        return authoruserid;
    }

    public void setAuthoruserid(Integer authoruserid) {
        this.authoruserid = authoruserid;
    }

    public Integer getAuthororder() {
        return authororder;
    }

    public void setAuthororder(Integer authororder) {
        this.authororder = authororder;
    }

    public BigDecimal getPoints() {
        return points;
    }

    public void setPoints(BigDecimal points) {
        this.points = points;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTeamidlinktext() {
        return teamidlinktext;
    }

    public void setTeamidlinktext(String teamidlinktext) {
        this.teamidlinktext = teamidlinktext;
    }

    public Integer getLogcount() {
        return logcount;
    }

    public void setLogcount(Integer logcount) {
        this.logcount = logcount;
    }

    public String getIndicatortypeDetail() {
        return indicatortypeDetail;
    }

    public void setIndicatortypeDetail(String indicatortypeDetail) {
        this.indicatortypeDetail = indicatortypeDetail;
    }

    @Override
    public String toString() {
        return "PerTeamperformance{" +
                "performanceid=" + performanceid +
                ", teamid=" + teamid +
                ", performanceyear=" + performanceyear +
                ", indicatortype='" + indicatortype + '\'' +
                ", indicatorid=" + indicatorid +
                ", achieveid=" + achieveid +
                ", description='" + description + '\'' +
                ", basepoints=" + basepoints +
                ", authoruserid=" + authoruserid +
                ", authororder=" + authororder +
                ", points=" + points +
                ", level1id=" + level1id +
                ", level1name='" + level1name + '\'' +
                ", level2id=" + level2id +
                ", level2name='" + level2name + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}