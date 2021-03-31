package com.ruoyi.achieve.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.util.List;

public class AchThesis extends BaseEntity {

    private Integer thesisid;

    private String thesisname;

    private String publishbookname;

    private Integer publishbooklevel;


    private String thesislevel;

    private Integer year;


    private String issue;


    private String period;


    private String page;

    private String keyword;


    private String memo;

    private String doi;


    private Integer teamid;


    private Integer createuserid;


    private String createtime;


    private Integer status;

    private String publishbooklevellinktext;
    private String teamidlinktext;
    private String createuseridlinktext;
    private String statuslinktext;


    private List<AchAuthor> authorList;
    private String authors;


    private Integer statusColor = 0;

    private Integer confirmResult;
    private String confirmNote;
    private Integer confirmUserid;
    private Integer applyid;

    public Integer getThesisid() {
        return thesisid;
    }

    public void setThesisid(Integer thesisid) {
        this.thesisid = thesisid;
    }

    public String getThesisname() {
        return thesisname;
    }

    public void setThesisname(String thesisname) {
        this.thesisname = thesisname;
    }

    public String getPublishbookname() {
        return publishbookname;
    }

    public void setPublishbookname(String publishbookname) {
        this.publishbookname = publishbookname;
    }

    public Integer getPublishbooklevel() {
        return publishbooklevel;
    }

    public void setPublishbooklevel(Integer publishbooklevel) {
        this.publishbooklevel = publishbooklevel;
    }

    public String getThesislevel() {
        return thesislevel;
    }

    public void setThesislevel(String thesislevel) {
        this.thesislevel = thesislevel;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
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

    public String getPublishbooklevellinktext() {
        return publishbooklevellinktext;
    }

    public void setPublishbooklevellinktext(String publishbooklevellinktext) {
        this.publishbooklevellinktext = publishbooklevellinktext;
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
        return "AchThesis{" +
                "thesisid=" + thesisid +
                ", thesisname='" + thesisname + '\'' +
                ", publishbookname='" + publishbookname + '\'' +
                ", publishbooklevel=" + publishbooklevel +
                ", thesislevel='" + thesislevel + '\'' +
                ", year=" + year +
                ", issue='" + issue + '\'' +
                ", period='" + period + '\'' +
                ", page='" + page + '\'' +
                ", keyword='" + keyword + '\'' +
                ", memo='" + memo + '\'' +
                ", doi='" + doi + '\'' +
                ", teamid=" + teamid +
                ", createuserid=" + createuserid +
                ", createtime='" + createtime + '\'' +
                ", status=" + status +
                ", publishbooklevellinktext='" + publishbooklevellinktext + '\'' +
                ", teamidlinktext='" + teamidlinktext + '\'' +
                ", createuseridlinktext='" + createuseridlinktext + '\'' +
                ", statuslinktext='" + statuslinktext + '\'' +
                '}';
    }
}