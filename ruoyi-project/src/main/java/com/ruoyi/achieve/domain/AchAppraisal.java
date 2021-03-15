package com.ruoyi.achieve.domain;

public class AchAppraisal {

    private Integer appraisalid;


    private String appraisalname;


    private String bookcode;


    private String certificateunit;


    private Integer publishyear;


    private String opinion;

    private String memo;


    private Integer projectid;

    private Integer teamid;


    private Integer createuserid;


    private String createtime;

    private Integer status;

    private String projectidlinktext;
    private String teamidlinktext;
    private String createuseridlinktext;
    private String statuslinktext;

    public Integer getAppraisalid() {
        return appraisalid;
    }

    public void setAppraisalid(Integer appraisalid) {
        this.appraisalid = appraisalid;
    }

    public String getAppraisalname() {
        return appraisalname;
    }

    public void setAppraisalname(String appraisalname) {
        this.appraisalname = appraisalname;
    }

    public String getBookcode() {
        return bookcode;
    }

    public void setBookcode(String bookcode) {
        this.bookcode = bookcode;
    }

    public String getCertificateunit() {
        return certificateunit;
    }

    public void setCertificateunit(String certificateunit) {
        this.certificateunit = certificateunit;
    }

    public Integer getPublishyear() {
        return publishyear;
    }

    public void setPublishyear(Integer publishyear) {
        this.publishyear = publishyear;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
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
        return "AchAppraisal{" +
                "appraisalid=" + appraisalid +
                ", appraisalname='" + appraisalname + '\'' +
                ", bookcode='" + bookcode + '\'' +
                ", certificateunit='" + certificateunit + '\'' +
                ", publishyear=" + publishyear +
                ", opinion='" + opinion + '\'' +
                ", memo='" + memo + '\'' +
                ", projectid=" + projectid +
                ", teamid=" + teamid +
                ", createuserid=" + createuserid +
                ", createtime='" + createtime + '\'' +
                ", status=" + status +
                ", projectidlinktext='" + projectidlinktext + '\'' +
                ", teamidlinktext='" + teamidlinktext + '\'' +
                ", createuseridlinktext='" + createuseridlinktext + '\'' +
                ", statuslinktext='" + statuslinktext + '\'' +
                '}';
    }
}