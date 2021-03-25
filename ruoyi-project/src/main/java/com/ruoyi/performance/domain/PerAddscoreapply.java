package com.ruoyi.performance.domain;

import com.ruoyi.common.core.domain.model.BasDoc;

import java.math.BigDecimal;
import java.util.Dictionary;
import java.util.List;

public class PerAddscoreapply {

    private Integer applyid;

    private Integer teamid;

    private Integer year;

    private Integer level1id;

    private String level1name;

    private Integer level2id;

    private String level2name;

    private Integer level3id;

    private String level3name;

    private BigDecimal applyscores;

    private Integer applyuserid;

    private String applytime;

    private String applycontent;

    private Integer audituserid;

    private String audittime;


    private String auditopinion;

    private String applystatus;

    private String teamidlinktext;
    private String applyuseridlinktext;
    private String audituseridlinktext;

    private List<BasDoc> fileList;

    private BigDecimal lastApplyscores;
    private String auditResult;

    public List<BasDoc> getFileList() {
        return fileList;
    }

    public void setFileList(List<BasDoc> fileList) {
        this.fileList = fileList;
    }

    public Integer getApplyid() {
        return applyid;
    }

    public void setApplyid(Integer applyid) {
        this.applyid = applyid;
    }

    public Integer getTeamid() {
        return teamid;
    }

    public void setTeamid(Integer teamid) {
        this.teamid = teamid;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getLevel1id() {
        return level1id;
    }

    public void setLevel1id(Integer level1id) {
        this.level1id = level1id;
    }

    public String getLevel1name() {
        return level1name;
    }

    public void setLevel1name(String level1name) {
        this.level1name = level1name;
    }

    public Integer getLevel2id() {
        return level2id;
    }

    public void setLevel2id(Integer level2id) {
        this.level2id = level2id;
    }

    public String getLevel2name() {
        return level2name;
    }

    public void setLevel2name(String level2name) {
        this.level2name = level2name;
    }

    public Integer getLevel3id() {
        return level3id;
    }

    public void setLevel3id(Integer level3id) {
        this.level3id = level3id;
    }

    public String getLevel3name() {
        return level3name;
    }

    public void setLevel3name(String level3name) {
        this.level3name = level3name;
    }

    public BigDecimal getApplyscores() {
        return applyscores;
    }

    public void setApplyscores(BigDecimal applyscores) {
        this.applyscores = applyscores;
    }

    public Integer getApplyuserid() {
        return applyuserid;
    }

    public void setApplyuserid(Integer applyuserid) {
        this.applyuserid = applyuserid;
    }

    public String getApplytime() {
        return applytime;
    }

    public void setApplytime(String applytime) {
        this.applytime = applytime;
    }

    public String getApplycontent() {
        return applycontent;
    }

    public void setApplycontent(String applycontent) {
        this.applycontent = applycontent;
    }

    public Integer getAudituserid() {
        return audituserid;
    }

    public void setAudituserid(Integer audituserid) {
        this.audituserid = audituserid;
    }

    public String getAudittime() {
        return audittime;
    }

    public void setAudittime(String audittime) {
        this.audittime = audittime;
    }

    public String getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(String auditResult) {
        this.auditResult = auditResult;
    }

    public String getAuditopinion() {
        return auditopinion;
    }

    public void setAuditopinion(String auditopinion) {
        this.auditopinion = auditopinion;
    }

    public String getApplystatus() {
        return applystatus;
    }

    public void setApplystatus(String applystatus) {
        this.applystatus = applystatus;
    }

    public String getTeamidlinktext() {
        return teamidlinktext;
    }

    public void setTeamidlinktext(String teamidlinktext) {
        this.teamidlinktext = teamidlinktext;
    }

    public String getApplyuseridlinktext() {
        return applyuseridlinktext;
    }

    public void setApplyuseridlinktext(String applyuseridlinktext) {
        this.applyuseridlinktext = applyuseridlinktext;
    }

    public String getAudituseridlinktext() {
        return audituseridlinktext;
    }

    public void setAudituseridlinktext(String audituseridlinktext) {
        this.audituseridlinktext = audituseridlinktext;
    }


    public BigDecimal getLastApplyscores() {
        return lastApplyscores;
    }

    public void setLastApplyscores(BigDecimal lastApplyscores) {
        this.lastApplyscores = lastApplyscores;
    }

    @Override
    public String toString() {
        return "PerAddscoreapply{" +
                "applyid=" + applyid +
                ", teamid=" + teamid +
                ", year=" + year +
                ", level1id=" + level1id +
                ", level1name='" + level1name + '\'' +
                ", level2id=" + level2id +
                ", level2name='" + level2name + '\'' +
                ", level3id=" + level3id +
                ", level3name='" + level3name + '\'' +
                ", applyscores=" + applyscores +
                ", applyuserid=" + applyuserid +
                ", applytime='" + applytime + '\'' +
                ", applycontent='" + applycontent + '\'' +
                ", audituserid=" + audituserid +
                ", audittime='" + audittime + '\'' +
                ", auditopinion='" + auditopinion + '\'' +
                ", applystatus='" + applystatus + '\'' +
                ", teamidlinktext='" + teamidlinktext + '\'' +
                ", applyuseridlinktext='" + applyuseridlinktext + '\'' +
                ", audituseridlinktext='" + audituseridlinktext + '\'' +
                '}';
    }
}