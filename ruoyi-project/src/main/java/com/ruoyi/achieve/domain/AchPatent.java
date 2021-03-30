package com.ruoyi.achieve.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.model.BasDoc;

import java.util.List;

public class AchPatent extends BaseEntity {

    private Integer patentid;

    private String patentcode;

    private String patentname;

    private String patenttype;

    private String applyyear;

    private String passtime;

    private String patentperson;

    private Integer ourunitorder;

    private Integer projectid;

    private Integer teamid;

    private String memo;

    private Integer createuserid;

    private String createtime;

    private Integer status;

    private String projectidlinktext;
    private String teamidlinktext;

    private String createuseridlinktext;
    private String statuslinktext;
    private Integer fixparamordernum;

    private List<AchAuthor> authorList;
    private String authors;

    private List<BasDoc> docList;

    //忘记这个 list 是做什么用的了。哦。项目的各种状态过滤用的，也是附加操作的属性，
    private List<Integer> statusList;
    // 附加操作的查询使用的属性
    private String patentYear;

    private Integer statusColor = 0;


    private Integer confirmResult;
    private String confirmNote;
    private Integer confirmUserid;
    private Integer applyid;



    public Integer getPatentid() {
        return patentid;
    }

    public void setPatentid(Integer patentid) {
        this.patentid = patentid;
    }

    public String getPatentcode() {
        return patentcode;
    }

    public void setPatentcode(String patentcode) {
        this.patentcode = patentcode;
    }

    public String getPatentname() {
        return patentname;
    }

    public void setPatentname(String patentname) {
        this.patentname = patentname;
    }

    public String getPatenttype() {
        return patenttype;
    }

    public void setPatenttype(String patenttype) {
        this.patenttype = patenttype;
    }

    public String getApplyyear() {
        return applyyear;
    }

    public void setApplyyear(String applyyear) {
        this.applyyear = applyyear;
    }

    public String getPasstime() {
        return passtime;
    }

    public void setPasstime(String passtime) {
        this.passtime = passtime;
    }

    public String getPatentperson() {
        return patentperson;
    }

    public void setPatentperson(String patentperson) {
        this.patentperson = patentperson;
    }

    public Integer getOurunitorder() {
        return ourunitorder;
    }

    public void setOurunitorder(Integer ourunitorder) {
        this.ourunitorder = ourunitorder;
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

    public Integer getFixparamordernum() {
        return fixparamordernum;
    }

    public void setFixparamordernum(Integer fixparamordernum) {
        this.fixparamordernum = fixparamordernum;
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

    public List<BasDoc> getDocList() {
        return docList;
    }

    public void setDocList(List<BasDoc> docList) {
        this.docList = docList;
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

    public List<Integer> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Integer> statusList) {
        this.statusList = statusList;
    }

    public String getPatentYear() {
        return patentYear;
    }

    public void setPatentYear(String patentYear) {
        this.patentYear = patentYear;
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
        return "AchPatent{" +
                "patentid=" + patentid +
                ", patentcode='" + patentcode + '\'' +
                ", patentname='" + patentname + '\'' +
                ", patenttype='" + patenttype + '\'' +
                ", applyyear='" + applyyear + '\'' +
                ", passtime='" + passtime + '\'' +
                ", patentperson='" + patentperson + '\'' +
                ", ourunitorder=" + ourunitorder +
                ", projectid=" + projectid +
                ", teamid=" + teamid +
                ", memo='" + memo + '\'' +
                ", createuserid=" + createuserid +
                ", createtime='" + createtime + '\'' +
                ", status=" + status +
                ", projectidlinktext='" + projectidlinktext + '\'' +
                ", teamidlinktext='" + teamidlinktext + '\'' +
                ", createuseridlinktext='" + createuseridlinktext + '\'' +
                ", statuslinktext='" + statuslinktext + '\'' +
                ", fixparamordernum=" + fixparamordernum +
                ", authorList=" + authorList +
                ", authors='" + authors + '\'' +
                ", docList=" + docList +
                ", statusList=" + statusList +
                ", patentYear='" + patentYear + '\'' +
                ", statusColor=" + statusColor +
                ", confirmResult=" + confirmResult +
                ", confirmNote='" + confirmNote + '\'' +
                ", confirmUserid=" + confirmUserid +
                ", applyid=" + applyid +
                '}';
    }
}