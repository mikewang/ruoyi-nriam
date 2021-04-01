package com.ruoyi.achieve.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class AchAuthor extends BaseEntity {

    private Integer authorid;


    private String achievetype;


    private Integer relatedid;


    private Integer ordernumber;


    private Integer ifourunit;


    private String unitname;


    private Integer userid;


    private String personname;


    private Boolean ifreporter;

    private String  useridlinktext;

    private Integer teamid;

    private Integer manageteamid;

    public Integer getAuthorid() {
        return authorid;
    }

    public void setAuthorid(Integer authorid) {
        this.authorid = authorid;
    }

    public String getAchievetype() {
        return achievetype;
    }

    public void setAchievetype(String achievetype) {
        this.achievetype = achievetype;
    }

    public Integer getRelatedid() {
        return relatedid;
    }

    public void setRelatedid(Integer relatedid) {
        this.relatedid = relatedid;
    }

    public Integer getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(Integer ordernumber) {
        this.ordernumber = ordernumber;
    }

    public Integer getIfourunit() {
        return ifourunit;
    }

    public void setIfourunit(Integer ifourunit) {
        this.ifourunit = ifourunit;
    }

    public String getUnitname() {
        return unitname;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getPersonname() {
        return personname;
    }

    public void setPersonname(String personname) {
        this.personname = personname;
    }

    public Boolean getIfreporter() {
        return ifreporter;
    }

    public void setIfreporter(Boolean ifreporter) {
        this.ifreporter = ifreporter;
    }

    public String getUseridlinktext() {
        return useridlinktext;
    }

    public void setUseridlinktext(String useridlinktext) {
        this.useridlinktext = useridlinktext;
    }

    public Integer getTeamid() {
        return teamid;
    }

    public void setTeamid(Integer teamid) {
        this.teamid = teamid;
    }

    public Integer getManageteamid() {
        return manageteamid;
    }

    public void setManageteamid(Integer manageteamid) {
        this.manageteamid = manageteamid;
    }

    @Override
    public String toString() {
        return "AchAuthor{" +
                "authorid=" + authorid +
                ", achievetype='" + achievetype + '\'' +
                ", relatedid=" + relatedid +
                ", ordernumber=" + ordernumber +
                ", ifourunit=" + ifourunit +
                ", unitname='" + unitname + '\'' +
                ", userid=" + userid +
                ", personname='" + personname + '\'' +
                ", ifreporter=" + ifreporter +
                ", useridlinktext='" + useridlinktext + '\'' +
                '}';
    }
}