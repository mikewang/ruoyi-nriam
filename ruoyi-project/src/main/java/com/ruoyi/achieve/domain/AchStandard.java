package com.ruoyi.achieve.domain;

public class AchStandard {

    private Integer standardid;


    private String standardname;

    private Integer standardtype;

    private String standardcode;


    private Integer publishyear;


    private String publishorganization;


    private String memo;


    private Integer projectid;


    private Integer teamid;


    private Integer createuserid;


    private String createtime;


    private Integer status;


    private String standardtypelinktext;

    private String projectidlinktext;
    private String teamidlinktext;
    private String createuseridlinktext;
    private String statuslinktext;

    public Integer getStandardid() {
        return standardid;
    }

    public void setStandardid(Integer standardid) {
        this.standardid = standardid;
    }

    public String getStandardname() {
        return standardname;
    }

    public void setStandardname(String standardname) {
        this.standardname = standardname;
    }

    public Integer getStandardtype() {
        return standardtype;
    }

    public void setStandardtype(Integer standardtype) {
        this.standardtype = standardtype;
    }

    public String getStandardcode() {
        return standardcode;
    }

    public void setStandardcode(String standardcode) {
        this.standardcode = standardcode;
    }

    public Integer getPublishyear() {
        return publishyear;
    }

    public void setPublishyear(Integer publishyear) {
        this.publishyear = publishyear;
    }

    public String getPublishorganization() {
        return publishorganization;
    }

    public void setPublishorganization(String publishorganization) {
        this.publishorganization = publishorganization;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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

    public String getStandardtypelinktext() {
        return standardtypelinktext;
    }

    public void setStandardtypelinktext(String standardtypelinktext) {
        this.standardtypelinktext = standardtypelinktext;
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
        return "AchStandard{" +
                "standardid=" + standardid +
                ", standardname='" + standardname + '\'' +
                ", standardtype=" + standardtype +
                ", standardcode='" + standardcode + '\'' +
                ", publishyear=" + publishyear +
                ", publishorganization='" + publishorganization + '\'' +
                ", memo='" + memo + '\'' +
                ", projectid=" + projectid +
                ", teamid=" + teamid +
                ", createuserid=" + createuserid +
                ", createtime='" + createtime + '\'' +
                ", status=" + status +
                ", standardtypelinktext='" + standardtypelinktext + '\'' +
                ", projectidlinktext='" + projectidlinktext + '\'' +
                ", teamidlinktext='" + teamidlinktext + '\'' +
                ", createuseridlinktext='" + createuseridlinktext + '\'' +
                ", statuslinktext='" + statuslinktext + '\'' +
                '}';
    }
}