package com.ruoyi.achieve.domain;

import com.ruoyi.common.core.domain.model.BasDoc;

import java.util.List;

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


    private List<AchAuthor> authorList;
    private String authors;

    private List<BasDoc> docList;

    private Integer statusColor = 0;

    private Integer confirmResult;
    private String confirmNote;
    private Integer confirmUserid;
    private Integer applyid;

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

    public List<AchAuthor> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(List<AchAuthor> authorList) {
        this.authorList = authorList;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public List<BasDoc> getDocList() {
        return docList;
    }

    public void setDocList(List<BasDoc> docList) {
        this.docList = docList;
    }

    public Integer getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(Integer statusColor) {
        this.statusColor = statusColor;
    }

    public Integer getConfirmResult() {
        return confirmResult;
    }

    public void setConfirmResult(Integer confirmResult) {
        this.confirmResult = confirmResult;
    }

    public String getConfirmNote() {
        return confirmNote;
    }

    public void setConfirmNote(String confirmNote) {
        this.confirmNote = confirmNote;
    }

    public Integer getConfirmUserid() {
        return confirmUserid;
    }

    public void setConfirmUserid(Integer confirmUserid) {
        this.confirmUserid = confirmUserid;
    }

    public Integer getApplyid() {
        return applyid;
    }

    public void setApplyid(Integer applyid) {
        this.applyid = applyid;
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
                ", authorList=" + authorList +
                ", authors='" + authors + '\'' +
                ", docList=" + docList +
                ", statusColor=" + statusColor +
                ", confirmResult=" + confirmResult +
                ", confirmNote='" + confirmNote + '\'' +
                ", confirmUserid=" + confirmUserid +
                ", applyid=" + applyid +
                '}';
    }
}