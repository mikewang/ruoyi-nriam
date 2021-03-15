package com.ruoyi.achieve.domain;

public class AchArticle {

    private Integer articleid;


    private String articlename;


    private Integer articletype;


    private Integer publishyear;


    private String publishunit;


    private String publishnumber;


    private String memo;


    private Integer projectid;


    private Integer teamid;


    private Integer createuserid;


    private String createtime;


    private Integer status;


    private String articletypelinktext;

    private String projectidlinktext;
    private String teamidlinktext;
    private String createuseridlinktext;
    private String statuslinktext;

    public Integer getArticleid() {
        return articleid;
    }

    public void setArticleid(Integer articleid) {
        this.articleid = articleid;
    }

    public String getArticlename() {
        return articlename;
    }

    public void setArticlename(String articlename) {
        this.articlename = articlename;
    }

    public Integer getArticletype() {
        return articletype;
    }

    public void setArticletype(Integer articletype) {
        this.articletype = articletype;
    }

    public Integer getPublishyear() {
        return publishyear;
    }

    public void setPublishyear(Integer publishyear) {
        this.publishyear = publishyear;
    }

    public String getPublishunit() {
        return publishunit;
    }

    public void setPublishunit(String publishunit) {
        this.publishunit = publishunit;
    }

    public String getPublishnumber() {
        return publishnumber;
    }

    public void setPublishnumber(String publishnumber) {
        this.publishnumber = publishnumber;
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

    public String getArticletypelinktext() {
        return articletypelinktext;
    }

    public void setArticletypelinktext(String articletypelinktext) {
        this.articletypelinktext = articletypelinktext;
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
        return "AchArticle{" +
                "articleid=" + articleid +
                ", articlename='" + articlename + '\'' +
                ", articletype=" + articletype +
                ", publishyear=" + publishyear +
                ", publishunit='" + publishunit + '\'' +
                ", publishnumber='" + publishnumber + '\'' +
                ", memo='" + memo + '\'' +
                ", projectid=" + projectid +
                ", teamid=" + teamid +
                ", createuserid=" + createuserid +
                ", createtime='" + createtime + '\'' +
                ", status=" + status +
                ", articletypelinktext='" + articletypelinktext + '\'' +
                ", projectidlinktext='" + projectidlinktext + '\'' +
                ", teamidlinktext='" + teamidlinktext + '\'' +
                ", createuseridlinktext='" + createuseridlinktext + '\'' +
                ", statuslinktext='" + statuslinktext + '\'' +
                '}';
    }
}