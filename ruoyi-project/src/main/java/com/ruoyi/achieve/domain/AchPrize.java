package com.ruoyi.achieve.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class AchPrize extends BaseEntity {

    private Integer prizeid;


    private String prizename;


    private Integer prizetype;


    private Integer prizelevel;


    private Integer prizerank;

    private Integer prizeyear;


    private String memo;


    private String publishunit;


    private String prizecode;


    private Integer ourunitorder;


    private Integer projectid;


    private Integer teamid;


    private Integer createuserid;


    private String createtime;


    private Integer status;

    private String prizetypelinktext;
    private String prizelevellinktext;
    private String prizeranklinktext;

    private String projectidlinktext;
    private String teamidlinktext;
    private String createuseridlinktext;
    private String statuslinktext;

    public Integer getPrizeid() {
        return prizeid;
    }

    public void setPrizeid(Integer prizeid) {
        this.prizeid = prizeid;
    }

    public String getPrizename() {
        return prizename;
    }

    public void setPrizename(String prizename) {
        this.prizename = prizename;
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

    public Integer getPrizeyear() {
        return prizeyear;
    }

    public void setPrizeyear(Integer prizeyear) {
        this.prizeyear = prizeyear;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPublishunit() {
        return publishunit;
    }

    public void setPublishunit(String publishunit) {
        this.publishunit = publishunit;
    }

    public String getPrizecode() {
        return prizecode;
    }

    public void setPrizecode(String prizecode) {
        this.prizecode = prizecode;
    }

    public Integer getOurunitorder() {
        return ourunitorder;
    }

    public void setOurunitorder(Integer ourunitorder) {
        this.ourunitorder = ourunitorder;
    }

    public Integer getProjectid() {
        return projectid;
    }

    public void setProjectid(Integer projectid) {
        this.projectid = projectid;
    }

    public Integer getTeamid() {
        return teamid;
    }

    public void setTeamid(Integer teamid) {
        this.teamid = teamid;
    }

    public Integer getCreateuserid() {
        return createuserid;
    }

    public void setCreateuserid(Integer createuserid) {
        this.createuserid = createuserid;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getProjectidlinktext() {
        return projectidlinktext;
    }

    public void setProjectidlinktext(String projectidlinktext) {
        this.projectidlinktext = projectidlinktext;
    }

    public String getTeamidlinktext() {
        return teamidlinktext;
    }

    public void setTeamidlinktext(String teamidlinktext) {
        this.teamidlinktext = teamidlinktext;
    }

    public String getCreateuseridlinktext() {
        return createuseridlinktext;
    }

    public void setCreateuseridlinktext(String createuseridlinktext) {
        this.createuseridlinktext = createuseridlinktext;
    }

    public String getStatuslinktext() {
        return statuslinktext;
    }

    public void setStatuslinktext(String statuslinktext) {
        this.statuslinktext = statuslinktext;
    }

    @Override
    public String toString() {
        return "AchPrize{" +
                "prizeid=" + prizeid +
                ", prizename='" + prizename + '\'' +
                ", prizetype=" + prizetype +
                ", prizelevel=" + prizelevel +
                ", prizerank=" + prizerank +
                ", prizeyear=" + prizeyear +
                ", memo='" + memo + '\'' +
                ", publishunit='" + publishunit + '\'' +
                ", prizecode='" + prizecode + '\'' +
                ", ourunitorder=" + ourunitorder +
                ", projectid=" + projectid +
                ", teamid=" + teamid +
                ", createuserid=" + createuserid +
                ", createtime='" + createtime + '\'' +
                ", status=" + status +
                ", prizetypelinktext='" + prizetypelinktext + '\'' +
                ", prizelevellinktext='" + prizelevellinktext + '\'' +
                ", prizeranklinktext='" + prizeranklinktext + '\'' +
                ", projectidlinktext='" + projectidlinktext + '\'' +
                ", teamidlinktext='" + teamidlinktext + '\'' +
                ", createuseridlinktext='" + createuseridlinktext + '\'' +
                ", statuslinktext='" + statuslinktext + '\'' +
                '}';
    }
}