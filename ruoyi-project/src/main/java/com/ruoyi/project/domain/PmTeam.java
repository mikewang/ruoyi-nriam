package com.ruoyi.project.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PmTeam extends BaseEntity {

    private Integer teamid;


    private String teamname;


    private Integer teamleaderid;


    private String memo;


    private Integer createuserid;


    private String createtime;


    private Boolean ifdeleted;

    private String teamleaderidlinktext;
    private String createuseridlinktext;

    private String members;

    private List<HashMap> memberList;

    private List<ArrayList<Integer>> checkedIdList;



    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public List<HashMap> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<HashMap> memberList) {
        this.memberList = memberList;
    }

    public Integer getTeamid() {
        return teamid;
    }


    public void setTeamid(Integer teamid) {
        this.teamid = teamid;
    }


    public String getTeamname() {
        return teamname;
    }


    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }


    public Integer getTeamleaderid() {
        return teamleaderid;
    }


    public void setTeamleaderid(Integer teamleaderid) {
        this.teamleaderid = teamleaderid;
    }


    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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


    public Boolean getIfdeleted() {
        return ifdeleted;
    }

    public void setIfdeleted(Boolean ifdeleted) {
        this.ifdeleted = ifdeleted;
    }

    public String getTeamleaderidlinktext() {
        return teamleaderidlinktext;
    }

    public void setTeamleaderidlinktext(String teamleaderidlinktext) {
        this.teamleaderidlinktext = teamleaderidlinktext;
    }

    public String getCreateuseridlinktext() {
        return createuseridlinktext;
    }

    public void setCreateuseridlinktext(String createuseridlinktext) {
        this.createuseridlinktext = createuseridlinktext;
    }

    @Override
    public String toString() {
        return "PmTeam{" +
                "teamid=" + teamid +
                ", teamname='" + teamname + '\'' +
                ", teamleaderid=" + teamleaderid +
                ", memo='" + memo + '\'' +
                ", createuserid=" + createuserid +
                ", createtime='" + createtime + '\'' +
                ", ifdeleted=" + ifdeleted +
                ", teamleaderidlinktext='" + teamleaderidlinktext + '\'' +
                ", createuseridlinktext='" + createuseridlinktext + '\'' +
                ", members='" + members + '\'' +
                ", memberList=" + memberList +
                ", checkedIdList=" + checkedIdList +
                '}';
    }

    public List<ArrayList<Integer>> getCheckedIdList() {
        return checkedIdList;
    }

    public void setCheckedIdList(List<ArrayList<Integer>> checkedIdList) {
        this.checkedIdList = checkedIdList;
    }

}