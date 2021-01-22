package com.ruoyi.project.domain;

public class PmTeamMember {

    private Integer teamid;


    private Integer teamrole;

    private String teamroleName;

    private Integer userid;
    private String realName;

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

    public Integer getTeamrole() {
        return teamrole;
    }

    public void setTeamrole(Integer teamrole) {
        this.teamrole = teamrole;
    }

    public String getTeamroleName() {
        return teamroleName;
    }

    public void setTeamroleName(String teamroleName) {
        this.teamroleName = teamroleName;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "PmTeamMember{" +
                "teamid=" + teamid +
                ", teamrole=" + teamrole +
                ", teamroleName='" + teamroleName + '\'' +
                ", userid=" + userid +
                ", realName='" + realName + '\'' +
                '}';
    }
}