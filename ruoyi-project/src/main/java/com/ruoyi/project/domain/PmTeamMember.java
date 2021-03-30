package com.ruoyi.project.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class PmTeamMember extends BaseEntity {

    private Integer teamid;


    private Integer teamrole;

    private String teamroleName;

    private Integer userId;
    private String realName;
    private String hotKey;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getHotKey() {
        return hotKey;
    }

    public void setHotKey(String hotKey) {
        this.hotKey = hotKey;
    }

    @Override
    public String toString() {
        return "PmTeamMember{" +
                "teamid=" + teamid +
                ", teamrole=" + teamrole +
                ", teamroleName='" + teamroleName + '\'' +
                ", userId=" + userId +
                ", realName='" + realName + '\'' +
                ", hotKey='" + hotKey + '\'' +
                '}';
    }
}