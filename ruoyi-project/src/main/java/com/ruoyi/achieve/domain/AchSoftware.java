package com.ruoyi.achieve.domain;

import com.ruoyi.common.core.domain.model.BasDoc;

import java.util.List;

public class AchSoftware {

    private Integer softwareid;

    private String softwarename;

    private String sotfwarecode;

    private String certifacatedate;


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


    private List<AchAuthor> authorList;
    private String authors;

    private List<BasDoc> docList;

    private Integer statusColor = 0;

    private Integer confirmResult;
    private String confirmNote;
    private Integer confirmUserid;
    private Integer applyid;

    public Integer getSoftwareid() {
        return softwareid;
    }

    public void setSoftwareid(Integer softwareid) {
        this.softwareid = softwareid;
    }

    public String getSoftwarename() {
        return softwarename;
    }

    public void setSoftwarename(String softwarename) {
        this.softwarename = softwarename;
    }

    public String getSotfwarecode() {
        return sotfwarecode;
    }

    public void setSotfwarecode(String sotfwarecode) {
        this.sotfwarecode = sotfwarecode;
    }

    public String getCertifacatedate() {
        return certifacatedate;
    }

    public void setCertifacatedate(String certifacatedate) {
        this.certifacatedate = certifacatedate;
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
        return "AchSoftware{" +
                "softwareid=" + softwareid +
                ", softwarename='" + softwarename + '\'' +
                ", sotfwarecode='" + sotfwarecode + '\'' +
                ", certifacatedate='" + certifacatedate + '\'' +
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