package com.ruoyi.project.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

public class AudProjectapply extends BaseEntity {

    private Integer projectapplyid;


    private Integer organizationid;


    private Integer pmid;


    private Integer sheetuserid;


    private String sheettime;


    private Integer projecttype;


    private String managedept;

    private String projectname;


    private String jointype;

    private String leaderorganization;


    private Integer projectyear;


    private BigDecimal totalmoney;


    private BigDecimal selfgetmoney;


    private BigDecimal leavemoney;


    private Boolean ifmoneyplan;


    private String memo;


    private Integer sheetstatus;

    private String organizationIDLinkText;
    private String pmIDLinkText;
    private String sheetUSerIDLinkText;
    private String sheetStatusLinkText;
    private String projectTypeLinkText;

    public Integer getProjectapplyid() {
        return projectapplyid;
    }

    public void setProjectapplyid(Integer projectapplyid) {
        this.projectapplyid = projectapplyid;
    }

    public Integer getOrganizationid() {
        return organizationid;
    }

    public void setOrganizationid(Integer organizationid) {
        this.organizationid = organizationid;
    }

    public Integer getPmid() {
        return pmid;
    }

    public void setPmid(Integer pmid) {
        this.pmid = pmid;
    }

    public Integer getSheetuserid() {
        return sheetuserid;
    }

    public void setSheetuserid(Integer sheetuserid) {
        this.sheetuserid = sheetuserid;
    }

    public String getSheettime() {
        return sheettime;
    }

    public void setSheettime(String sheettime) {
        this.sheettime = sheettime;
    }

    public Integer getProjecttype() {
        return projecttype;
    }

    public void setProjecttype(Integer projecttype) {
        this.projecttype = projecttype;
    }

    public String getManagedept() {
        return managedept;
    }

    public void setManagedept(String managedept) {
        this.managedept = managedept;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getJointype() {
        return jointype;
    }

    public void setJointype(String jointype) {
        this.jointype = jointype;
    }

    public String getLeaderorganization() {
        return leaderorganization;
    }

    public void setLeaderorganization(String leaderorganization) {
        this.leaderorganization = leaderorganization;
    }

    public Integer getProjectyear() {
        return projectyear;
    }

    public void setProjectyear(Integer projectyear) {
        this.projectyear = projectyear;
    }

    public BigDecimal getTotalmoney() {
        return totalmoney;
    }

    public void setTotalmoney(BigDecimal totalmoney) {
        this.totalmoney = totalmoney;
    }

    public BigDecimal getSelfgetmoney() {
        return selfgetmoney;
    }

    public void setSelfgetmoney(BigDecimal selfgetmoney) {
        this.selfgetmoney = selfgetmoney;
    }

    public BigDecimal getLeavemoney() {
        return leavemoney;
    }

    public void setLeavemoney(BigDecimal leavemoney) {
        this.leavemoney = leavemoney;
    }

    public Boolean getIfmoneyplan() {
        return ifmoneyplan;
    }

    public void setIfmoneyplan(Boolean ifmoneyplan) {
        this.ifmoneyplan = ifmoneyplan;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getSheetstatus() {
        return sheetstatus;
    }

    public void setSheetstatus(Integer sheetstatus) {
        this.sheetstatus = sheetstatus;
    }

    public String getOrganizationIDLinkText() {
        return organizationIDLinkText;
    }

    public void setOrganizationIDLinkText(String organizationIDLinkText) {
        this.organizationIDLinkText = organizationIDLinkText;
    }

    public String getPmIDLinkText() {
        return pmIDLinkText;
    }

    public void setPmIDLinkText(String pmIDLinkText) {
        this.pmIDLinkText = pmIDLinkText;
    }

    public String getSheetUSerIDLinkText() {
        return sheetUSerIDLinkText;
    }

    public void setSheetUSerIDLinkText(String sheetUSerIDLinkText) {
        this.sheetUSerIDLinkText = sheetUSerIDLinkText;
    }

    public String getSheetStatusLinkText() {
        return sheetStatusLinkText;
    }

    public void setSheetStatusLinkText(String sheetStatusLinkText) {
        this.sheetStatusLinkText = sheetStatusLinkText;
    }

    public String getProjectTypeLinkText() {
        return projectTypeLinkText;
    }

    public void setProjectTypeLinkText(String projectTypeLinkText) {
        this.projectTypeLinkText = projectTypeLinkText;
    }

    @Override
    public String toString() {
        return "AudProjectapply{" +
                "projectapplyid=" + projectapplyid +
                ", organizationid=" + organizationid +
                ", pmid=" + pmid +
                ", sheetuserid=" + sheetuserid +
                ", sheettime='" + sheettime + '\'' +
                ", projecttype=" + projecttype +
                ", managedept='" + managedept + '\'' +
                ", projectname='" + projectname + '\'' +
                ", jointype='" + jointype + '\'' +
                ", leaderorganization='" + leaderorganization + '\'' +
                ", projectyear=" + projectyear +
                ", totalmoney=" + totalmoney +
                ", selfgetmoney=" + selfgetmoney +
                ", leavemoney=" + leavemoney +
                ", ifmoneyplan=" + ifmoneyplan +
                ", memo='" + memo + '\'' +
                ", sheetstatus=" + sheetstatus +
                ", organizationIDLinkText='" + organizationIDLinkText + '\'' +
                ", pmIDLinkText='" + pmIDLinkText + '\'' +
                ", sheetUSerIDLinkText='" + sheetUSerIDLinkText + '\'' +
                ", sheetStatusLinkText='" + sheetStatusLinkText + '\'' +
                ", projectTypeLinkText='" + projectTypeLinkText + '\'' +
                '}';
    }
}