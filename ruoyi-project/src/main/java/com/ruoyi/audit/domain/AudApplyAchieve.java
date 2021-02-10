package com.ruoyi.audit.domain;

public class AudApplyAchieve extends AudApply {

    private String name;
    private String realName;
    private Integer teamid;
    private Integer status;
    private String statuslinktext;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getTeamid() {
        return teamid;
    }

    public void setTeamid(Integer teamid) {
        this.teamid = teamid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatuslinktext() {
        return statuslinktext;
    }

    public void setStatuslinktext(String statuslinktext) {
        this.statuslinktext = statuslinktext;
    }

    @Override
    public String toString() {
        return "AudApplyAchieve{" +
                "name='" + name + '\'' +
                ", realName='" + realName + '\'' +
                ", teamid=" + teamid +
                 '}';
    }
}