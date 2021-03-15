package com.ruoyi.achieve.domain;

public class AchTech {

    private Integer techid;

    private String techname;

    private Integer techyear;

    private Boolean iffirst;

    private String keypoint;

    private String performance;


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

    public Integer getTechid() {
        return techid;
    }

    public void setTechid(Integer techid) {
        this.techid = techid;
    }

    public String getTechname() {
        return techname;
    }

    public void setTechname(String techname) {
        this.techname = techname;
    }

    public Integer getTechyear() {
        return techyear;
    }

    public void setTechyear(Integer techyear) {
        this.techyear = techyear;
    }

    public Boolean getIffirst() {
        return iffirst;
    }

    public void setIffirst(Boolean iffirst) {
        this.iffirst = iffirst;
    }

    public String getKeypoint() {
        return keypoint;
    }

    public void setKeypoint(String keypoint) {
        this.keypoint = keypoint;
    }

    public String getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
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
        return "AchTech{" +
                "techid=" + techid +
                ", techname='" + techname + '\'' +
                ", techyear=" + techyear +
                ", iffirst=" + iffirst +
                ", keypoint='" + keypoint + '\'' +
                ", performance='" + performance + '\'' +
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